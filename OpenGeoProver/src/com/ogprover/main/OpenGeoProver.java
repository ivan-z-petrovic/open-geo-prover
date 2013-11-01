/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.main;

import java.io.IOException;

import com.ogprover.api.GeoGebraOGPInterface;
import com.ogprover.pp.GeoGebraOGPInputProverProtocol;
import com.ogprover.pp.GeoGebraOGPOutputProverProtocol;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.utilities.io.CustomFileReader;
import com.ogprover.utilities.logger.ILogger;


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
	 * MAIN method - starting point of OGP - simulation of usage of OGP API for GeoGebra.
	 * 
	 * @param args		Arguments of command line
	 */
	public static void main(String[] args) {
		OpenGeoProver.settings = new OGPConfigurationSettings();
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		// prover's introduction message
		StringBuilder sb = new StringBuilder();
		sb.append("\nOpenGeoProver Version ");
		sb.append(OpenGeoProver.VERSION_NUM);
		sb.append(";\nwritten by Ivan Petrovic and Predrag Janicic, University of Belgrade.\n");
		sb.append("Reimplementation of C++ version 2.00;\nwritten by Goran Predovic and Predrag Janicic, University of Belgrade.\n");
		sb.append("Copyright (c) 2005-2011. Not for commercial use.\n\n");
		sb.append("Type \"-h\" or \"--help\" if necessary, for explanation about correct usage of command line.\n\n");
		System.out.println(sb.toString());
		
		if (args.length != 1) {
			logger.error("Incorrect number of command line arguments - only one is expected");
			OpenGeoProver.settings.getTimer().cancel(); // cancel default timer task
			return;
		}
		
		if (args[0].equals("-h") || args[0].equals("--help")) {
			OGPParameters.printHelp();
			OpenGeoProver.settings.getTimer().cancel(); // cancel default timer task
			return;
		}
		
		// Reading input file with theorem in GeoGebra's XML format
		logger.info("Parsing command line...");
		// Extract only file name without extension
		String xmlFileName = args[0]; // absolute or relative file path (relative with respect to current working directory)
		
		// Create custom file reader for the file with name passed as an argument; read its contents and copy to string
		String xmlString;
		try {
			CustomFileReader fileReader = new CustomFileReader(xmlFileName);
			// Read one by one line and append to string buffer
			StringBuffer sbuff = new StringBuffer();
			String line = null;
			while ((line = fileReader.readLine()) != null) {
				sbuff.append(line);
			}
			// Close file reader
			fileReader.close();
			xmlString = sbuff.toString();
		} catch (IOException e) {
			logger.error("I/O Exception caught: " + e.toString());
			e.printStackTrace();
			return;
		}
		
		// Input prover object
		GeoGebraOGPInputProverProtocol inputObject = new GeoGebraOGPInputProverProtocol();
		inputObject.setGeometryTheoremText(xmlString);
		inputObject.setMethod(GeoGebraOGPInputProverProtocol.OGP_METHOD_WU);
		inputObject.setTimeOut(10);
		inputObject.setMaxTerms(10000);
		inputObject.setReportFormat(GeoGebraOGPInputProverProtocol.OGP_REPORT_FORMAT_NONE);
		
		// OGP API
		GeoGebraOGPInterface ogpInterface = new GeoGebraOGPInterface();
		GeoGebraOGPOutputProverProtocol outputObject = (GeoGebraOGPOutputProverProtocol)ogpInterface.prove(inputObject); // safe cast
		
		// Print results
		OGPTP tp = OpenGeoProver.settings.getParsedTP();
		if (tp != null)
			System.out.println("Prover results for theorem \"" + tp.getTheoremName() + "\":");
		else
			System.out.println("Prover results");
		System.out.println(GeoGebraOGPOutputProverProtocol.OGP_OUTPUT_RES_SUCCESS + ": " + outputObject.getOutputResult(GeoGebraOGPOutputProverProtocol.OGP_OUTPUT_RES_SUCCESS));
		System.out.println(GeoGebraOGPOutputProverProtocol.OGP_OUTPUT_RES_FAILURE_MSG + ": " + outputObject.getOutputResult(GeoGebraOGPOutputProverProtocol.OGP_OUTPUT_RES_FAILURE_MSG));
		System.out.println(GeoGebraOGPOutputProverProtocol.OGP_OUTPUT_RES_PROVER + ": " + outputObject.getOutputResult(GeoGebraOGPOutputProverProtocol.OGP_OUTPUT_RES_PROVER));
		System.out.println(GeoGebraOGPOutputProverProtocol.OGP_OUTPUT_RES_PROVER_MSG + ": " + outputObject.getOutputResult(GeoGebraOGPOutputProverProtocol.OGP_OUTPUT_RES_PROVER_MSG));
		System.out.println(GeoGebraOGPOutputProverProtocol.OGP_OUTPUT_RES_TIME + ": " + outputObject.getOutputResult(GeoGebraOGPOutputProverProtocol.OGP_OUTPUT_RES_TIME));
		System.out.println(GeoGebraOGPOutputProverProtocol.OGP_OUTPUT_RES_NUMTERMS + ": " + outputObject.getOutputResult(GeoGebraOGPOutputProverProtocol.OGP_OUTPUT_RES_NUMTERMS));
		System.out.println();
		System.out.println("NDG conditions");
		for (String ndgCondText : outputObject.getNdgList())
			System.out.println(ndgCondText);
	}
}