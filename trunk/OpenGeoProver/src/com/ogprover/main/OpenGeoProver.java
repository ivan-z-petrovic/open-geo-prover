/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.main;

import java.io.IOException;

import com.ogprover.polynomials.GeoTheorem;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.test.formats.geothm_xml.GeoTheoremXMLParser;
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


/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Main class for OpenGeoProver project</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */
public class OpenGeoProver {
	/*
	 * ======================================================================
	 * ========================== VARIABLES =================================
	 * ======================================================================
	 */
	/**
	 * <i><b>
	 * Version number of class in form xx.yy where
	 * xx is major version/release number and yy is minor
	 * release number.
	 * </b></i>
	 */
	public static final String VERSION_NUM = "1.00"; // this should match the version number from class comment
	/**
	 * <i>
	 * Prover's configuration settings that can be accessed globally
	 * </i>
	 */
	public static OGPConfigurationSettings settings = null;
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * <i>[static method]</i><br>
	 * MAIN method - starting point of OGP
	 * 
	 * @param args		Arguments of command line
	 */
	public static void main(String[] args) {
		OpenGeoProver.settings = new OGPConfigurationSettings();
		OGPParameters parameters = OpenGeoProver.settings.getParameters();
		OGPOutput output = OpenGeoProver.settings.getOutput();
		FileLogger logger = (FileLogger) OpenGeoProver.settings.getLogger();
		Stopwatch stopwatch = OpenGeoProver.settings.getStopwacth();
		
		// prover's introduction message
		StringBuilder sb = new StringBuilder();
		sb.append("\nOpenGeoProver Version ");
		sb.append(OpenGeoProver.VERSION_NUM);
		sb.append(";\nwritten by Ivan Petrovic and Predrag Janicic, University of Belgrade.\n");
		sb.append("Reimplementation of C++ version 2.00;\nwritten by Goran Predovic and Predrag Janicic, University of Belgrade.\n");
		sb.append("Copyright (c) 2005-2011. Not for commercial use.\n\n");
		sb.append("Type \"-h\" or \"--help\" if necessary, for explanaition about correct usage of command line.\n\n");
		System.out.println(sb.toString());
		
		int retCode = OGPConstants.RET_CODE_SUCCESS;
		
		/*
		 *  STEP 1 - Parsing command line and filling parameters
		 */
		if (args.length == 1 && (args[0].equals("-h") || args[0].equals("--help"))) {
			OGPParameters.printHelp();
			OpenGeoProver.settings.getTimer().cancel(); // cancel default timer task
			return;
		}
		
		logger.info("Parsing command line...");
		retCode = OGPParameters.readParametersFromCommandLine(args);
		
		if (retCode != OGPConstants.RET_CODE_SUCCESS) {
			System.out.println("Error happened in parsing command line - will exit the prover!\nPlease provide correct parameters.\n\n");
			OGPParameters.printHelp();
			return;
		}
		
		// setting log level and verbose flag in logger
		logger.setLevel(parameters.getLogLevel());
		logger.setVerbose(parameters.getVerbose());
		
		// creating output files
		if (parameters.createReport()) {
			String outputFmt = parameters.getOutputFormat();
			String outputFile = parameters.getOutputFile();
			LaTeXFileWriter latexWriter = null;
			XMLFileWriter xmlWriter = null;
			
			if (outputFile == null) {
				logger.error("Error: Null name for output file.");
				return;
			}
			
			if (!outputFmt.equals("A") && !outputFmt.equals("L") && !outputFmt.equals("X")) {
				logger.error("Invalid format of output file.");
				return;
			}
			
			if (outputFmt.equals("A") || outputFmt.equals("L")) {
				try {
					latexWriter = new LaTeXFileWriter(outputFile);
				} catch (IOException e) {
					logger.error("Failed to open LaTeX output file.");
					if (latexWriter != null)
						latexWriter.close();
					latexWriter = null;
				}
			}
			
			if (outputFmt.equals("A") || outputFmt.equals("X")) {
				try {
					xmlWriter = new XMLFileWriter(outputFile);
				} catch (IOException e) {
					logger.error("Failed to open XML output file.");
					if (xmlWriter != null)
						xmlWriter.close();
					xmlWriter = null;
				}
			}
			
			output = new OGPOutput(latexWriter, xmlWriter);
			OpenGeoProver.settings.setOutput(output);
		}
		
		
		/*
		 *  STEP 2 - Reading input geometry problem and transforming it into algebraic form.
		 */
		logger.info("Reading input geometry problem...");
		GeoTheorem theorem = null;
		OGPReport report = null;
		OGPTP consProtocol = null;
		
		if (parameters.getInputFile() != null) { // reading problem from file
			if (parameters.getInputFormat().equals("O")) { // xml file which contains geometric construction and theorem statement
				OGPCPXMLParser parser = new OGPCPXMLParser();
				consProtocol = parser.readGeoTheoremFromXML(parameters.getInputFile());

				if (consProtocol == null) {
					logger.error("Failed to read theorem.");
					output.close();
					return;
				}
				
				report = new OGPReport(consProtocol);
				
				// opening output report
				report.openReport();
			}
			else if (parameters.getInputFormat().equals("A")) { // xml file which contains polynomial representation of problem
				GeoTheoremXMLParser parser = new GeoTheoremXMLParser();
				theorem = parser.readGeoTheoremFromXML(parameters.getInputFile());
				
				if (theorem == null) {
					logger.error("Failed to read theorem.");
					output.close();
					return;
				}
				
				consProtocol = new OGPTP();
				consProtocol.setAlgebraicGeoTheorem(theorem);
				consProtocol.setTheoremName(theorem.getName());
				report = new OGPReport(consProtocol);
				
				// opening output report
				report.openReport();
			}
			else if (parameters.getInputFormat().equals("G")) { // gcl file with representation of problem in GCLC language
				// TODO
			}
			else { // unknown format
				logger.error("Unknown format of input file.");
				output.close();
				return;
			}
		}
		else { // reading problem from internal memory
			// TODO
		}
		
		
		/*
		 *  STEP 3 - Invoking prover
		 */
		if (parameters.getInputFormat().equals("O")) {
			/*
			 * Validation of CP
			 */
			if (!consProtocol.isValid()) {
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
			
			retCode = consProtocol.convertToAlgebraicForm();
			theorem = consProtocol.getAlgebraicGeoTheorem();
			
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
		}
		
		logger.info("Invoking prover method...");
		int proverType = parameters.getProver();
		AlgebraicMethodProver proverMethod = null;
		
		OGPTimer timer = OpenGeoProver.settings.getTimer();
		if (proverType == TheoremProver.TP_TYPE_WU) { // Wu's method
			proverMethod = new WuMethodProver(theorem);
			
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
			return;
		}
		timer.cancel(); // canceling timer
		
		
		/*
		 *  STEP 4 - Presenting results to standard output and in report file(s)
		 */
		logger.info("Prover results:\n");
		if (report != null)
			report.printProverResults(retCode);
	}
}