/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.manual;

import java.io.IOException;

import org.apache.log4j.Level;

import com.ogprover.main.OGPConfigurationSettings;
import com.ogprover.main.OGPConstants;
import com.ogprover.main.OGPParameters;
import com.ogprover.main.OGPReport;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.test.formats.ogp_xml.OGPCPXMLParser;
import com.ogprover.thmprover.AlgebraicMethodProver;
import com.ogprover.thmprover.TheoremProver;
import com.ogprover.thmprover.WuMethodProver;
import com.ogprover.utilities.OGPTimer;
import com.ogprover.utilities.OGPUtilities;
import com.ogprover.utilities.Stopwatch;
import com.ogprover.utilities.io.LaTeXFileWriter;
import com.ogprover.utilities.io.OGPOutput;
import com.ogprover.utilities.io.SpecialFileFormatting;
import com.ogprover.utilities.io.XMLFileWriter;
import com.ogprover.utilities.logger.FileLogger;
import com.ogprover.utilities.logger.ILogger;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for manual testing of OpenGeoProver</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class MTestOpenGeoProver {
	
	/**
	 * Simulation of proving of a theorem.
	 * 
	 * @param cp	Construction Protocol
	 */
	public static void simulateTheoremProving(OGPTP cp) {
		OGPParameters parameters = OpenGeoProver.settings.getParameters();
		ILogger logger = OpenGeoProver.settings.getLogger();
		OGPOutput output = OpenGeoProver.settings.getOutput();
		Stopwatch stopwatch = OpenGeoProver.settings.getStopwacth();
		OGPReport report = new OGPReport(cp);
		
		/*
		 * Open report file(s).
		 */
		report.openReport();
		
		/*
		 * Validation of CP
		 */
		if (!cp.isValid()) {
			output.close();
			return;
		}
		
		/*
		 * Transformation to Algebraic form
		 */
		OpenGeoProver.settings.getStopwacth().startMeasureTime();
		try {
			output.openSection("Transformation of Construction Protocol to algebraic form");
		} catch (IOException e) {
			logger.error("Failed to write to output file(s).");
			output.close();
			return;
		}
		
		int retCode = cp.convertToAlgebraicForm();
		
		if (retCode != OGPConstants.RET_CODE_SUCCESS) {
			output.close();
			return;
		}
		
		OpenGeoProver.settings.getStopwacth().endMeasureTime();
		try {
			output.openSubSection("Time spent for transformation of Construction Protocol to algebraic form", false);
			output.openEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
			output.openItem();
			output.writePlainText(OGPUtilities.roundUpToPrecision(stopwatch.getTimeIntSec()) + " seconds");
			output.closeItem();
			output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
			output.closeSubSection();
			output.closeSection();
		} catch (IOException e) {
			logger.error("Failed to write to output file(s).");
			output.close();
			return;
		}
		
		/*
		 *  Invoking prover method
		 */
		logger.info("Invoking prover method...");
		int proverType = parameters.getProver();
		AlgebraicMethodProver proverMethod = null;
		
		OGPTimer timer = OpenGeoProver.settings.getTimer();
		if (proverType == TheoremProver.TP_TYPE_WU) { // Wu's method
			proverMethod = new WuMethodProver(cp.getAlgebraicGeoTheorem());
			timer.setTimer(parameters.getTimeLimit()); // setting timer
			stopwatch.startMeasureTime();
			retCode = proverMethod.prove();
			stopwatch.endMeasureTime();
		}
		else if (proverType == TheoremProver.TP_TYPE_GROEBNER) { // Groebner basis method
			// TODO
		}
		else {
			System.out.println("Invalid prover type.");
			output.close();
			return;
		}
		timer.cancel(); // canceling timer
		
		report.printProverResults(retCode);
	}
	
	/**
	 * Testing theorem proving when theorem is represented by index.
	 * 
	 * @param theoNum	Index of theorem in internal list.
	 */
	public static void testTheoremProving(int theoNum) {
		MTestOpenGeoProver.simulateTheoremProving(MTestCP.getPreparedCPForTheorem(theoNum));
	}
	
	/**
	 * Testing theorem proving when theorem is set 
	 * in input XML file in OGP xML format.
	 * 
	 * @param inputFileName		Name of file with theorem representation
	 */
	public static void testTheoremProving(String inputFileName) {
		OGPCPXMLParser parser = new OGPCPXMLParser();
		OGPTP consProtocol = parser.readGeoTheoremFromXML(inputFileName);
		MTestOpenGeoProver.simulateTheoremProving(consProtocol);
	}
	
	
	public static void main (String[] args) {
		OpenGeoProver.settings = new OGPConfigurationSettings();
		
		// turn on debug log level
		((FileLogger) OpenGeoProver.settings.getLogger()).setLevel(Level.DEBUG);
		
		try {
			OpenGeoProver.settings.setOutput(new OGPOutput(new LaTeXFileWriter(OGPConstants.DEF_VAL_PARAM_OUTPUT_FILE), new XMLFileWriter(OGPConstants.DEF_VAL_PARAM_OUTPUT_FILE)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*
		 * Test proving of some theorems (refer to list in MTestCP.java).
		 */
		//MTestOpenGeoProver.testTheoremProving(32); // Butterfly
		//MTestOpenGeoProver.testTheoremProving(38); // Ceva
		//MTestOpenGeoProver.testTheoremProving(49); // power of point - fails due to huge complexity of problem
		//MTestOpenGeoProver.testTheoremProving(50); // power of point with respect to circle
		//MTestOpenGeoProver.testTheoremProving(51); // Ptolemy theorem
		//MTestOpenGeoProver.testTheoremProving(54); // Gergonne point
		
		//MTestOpenGeoProver.testTheoremProving("ogp_butterfly"); 
		//MTestOpenGeoProver.testTheoremProving("ogp_ceva1");
		//MTestOpenGeoProver.testTheoremProving("ogp_three_squares");
		//MTestOpenGeoProver.testTheoremProving("ogp_simson");
		//MTestOpenGeoProver.testTheoremProving("ogp_chou_example63"); // before execution increase the time limit from 10s to 30s
		MTestOpenGeoProver.testTheoremProving("ogp_chou_example82");
		
		OpenGeoProver.settings.getTimer().cancel(); // cancel timer thread
	}
}