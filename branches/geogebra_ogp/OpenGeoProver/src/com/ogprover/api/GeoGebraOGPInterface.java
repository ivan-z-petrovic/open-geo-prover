/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.api;

import java.io.IOException;
import java.io.StringReader;

import com.ogprover.api.converter.GGThmConverterForAlgebraicProvers;
import com.ogprover.api.converter.GGThmConverterForAreaMethod;
import com.ogprover.api.converter.GeoGebraTheoremConverter;
import com.ogprover.geogebra.GeoGebraTheorem;
import com.ogprover.main.OGPConstants;
import com.ogprover.main.OGPParameters;
import com.ogprover.main.OGPReport;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.GeoTheorem;
import com.ogprover.pp.GeoGebraOGPInputProverProtocol;
import com.ogprover.pp.GeoGebraOGPOutputProverProtocol;
import com.ogprover.pp.OGPInputProverProtocol;
import com.ogprover.pp.OGPOutputProverProtocol;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.thmprover.AreaMethodProver;
import com.ogprover.thmprover.TheoremProver;
import com.ogprover.thmprover.WuMethodProver;
import com.ogprover.utilities.OGPTimer;
import com.ogprover.utilities.OGPUtilities;
import com.ogprover.utilities.Stopwatch;
import com.ogprover.utilities.io.LaTeXFileWriter;
import com.ogprover.utilities.io.OGPDocHandler;
import com.ogprover.utilities.io.OGPOutput;
import com.ogprover.utilities.io.QDParser;
import com.ogprover.utilities.io.SpecialFileFormatting;
import com.ogprover.utilities.io.XMLFileWriter;
import com.ogprover.utilities.logger.GeoGebraLogger;
import com.ogprover.utilities.logger.ILogger;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for interface between GeoGebra and OGP</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */
public class GeoGebraOGPInterface implements OGPAPI {
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
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Default constructor method.
	 */
	public GeoGebraOGPInterface() {
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method which is called when exiting prover.
	 * 
	 * @param outputObject		Output object to be returned from prover execution
	 * @param errorMsg			Error message
	 * @return					Modified output object passed in as argument
	 */
	private OGPOutputProverProtocol exitProver(GeoGebraOGPOutputProverProtocol outputObject, String errorMsg) {
		OpenGeoProver.settings.getOutput().close();
		outputObject.setOutputResult(GeoGebraOGPOutputProverProtocol.OGP_OUTPUT_RES_SUCCESS, "false");
		outputObject.setOutputResult(GeoGebraOGPOutputProverProtocol.OGP_OUTPUT_RES_FAILURE_MSG, errorMsg);
		OpenGeoProver.settings.getTimer().cancel(); // cancel default timer task
		return outputObject;
	}
	
	/**
	 * Method for reading prover parameters from input object.
	 * 
	 * @param inputObject	Input prover object.
	 * @return				TRUE if parameters were set successfully, FALSE otherwise.
	 */
	private boolean populateParameters(GeoGebraOGPInputProverProtocol inputObject) {
		OGPParameters parameters = OpenGeoProver.settings.getParameters();
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		// Prover method
		String method = inputObject.getIntputParameter(GeoGebraOGPInputProverProtocol.GG_INPUT_PARAM_METHOD);
		if (method.equals(GeoGebraOGPInputProverProtocol.OGP_METHOD_WU))
			parameters.putProver(TheoremProver.TP_TYPE_WU);
		else if (method.equals(GeoGebraOGPInputProverProtocol.OGP_METHOD_GROEBNER))
			parameters.putProver(TheoremProver.TP_TYPE_GROEBNER);
		else if (method.equals(GeoGebraOGPInputProverProtocol.OGP_METHOD_AREA))
			parameters.putProver(TheoremProver.TP_TYPE_AREA);
		else {
			logger.error("Unknown prover method");
			return false;
		}
		
		// Timeout
		double timeLimitInMilliSec = Double.parseDouble(inputObject.getIntputParameter(GeoGebraOGPInputProverProtocol.GG_INPUT_PARAM_TIMEOUT)) * 1000;
		if (timeLimitInMilliSec > 0)
			parameters.putTimeLimit(timeLimitInMilliSec);
		else {
			logger.warn("Incorrect timeout prover parameter - setting default timeout of " + OGPConstants.DEF_VAL_PARAM_TIME_LIMIT + " milliseconds.");
			parameters.putTimeLimit(Double.parseDouble(OGPConstants.DEF_VAL_PARAM_TIME_LIMIT));
		}
		
		// Maximal number of terms
		int maxTerms = Integer.parseInt(inputObject.getIntputParameter(GeoGebraOGPInputProverProtocol.GG_INPUT_PARAM_MAXTERMS));
		if (maxTerms > 0)
			parameters.putSpaceLimit(maxTerms);
		else {
			logger.warn("Incorrect maximal number of terms as prover parameter - setting default number of " + OGPConstants.DEF_VAL_PARAM_SPACE_LIMIT + " terms.");
			parameters.putSpaceLimit(Integer.parseInt(OGPConstants.DEF_VAL_PARAM_SPACE_LIMIT));
		}
		
		// Format of output report
		String reportFormat = inputObject.getIntputParameter(GeoGebraOGPInputProverProtocol.GG_INPUT_PARAM_REPORT_FORMAT);
		if (reportFormat.equals(GeoGebraOGPInputProverProtocol.OGP_REPORT_FORMAT_TEX))
			parameters.putOutputFormat("L");
		else if (reportFormat.equals(GeoGebraOGPInputProverProtocol.OGP_REPORT_FORMAT_XML))
			parameters.putOutputFormat("X");
		else if (reportFormat.equals(GeoGebraOGPInputProverProtocol.OGP_REPORT_FORMAT_ALL))
			parameters.putOutputFormat("A");
		else if (reportFormat.equals(GeoGebraOGPInputProverProtocol.OGP_REPORT_FORMAT_NONE))
			parameters.putOutputFormat("N");
		else {
			logger.warn("Incorrect parameter for format of output report - setting default value of " + OGPConstants.DEF_VAL_PARAM_OUTPUT_FORMAT);
			parameters.putOutputFormat(OGPConstants.DEF_VAL_PARAM_OUTPUT_FORMAT);
		}
		
		/*
		 * TODO - Other parameters have their default values (set in constructor of OGP parameters object) until they are defined in GeoGebra. 
		 */
		
		// Setting log level and verbose flag in logger
		((GeoGebraLogger)logger).setLogLevel(parameters.getLogLevel()); // safe cast
		
		return true;
	}
	
	/**
	 * Method for reading and conversion of input geometry theorem.
	 * 
	 * @param inputObject		Input prover object.
	 * @param theoremProtocol	The OGP object for storage of converted theorem.
	 * @return					TRUE if operation was successful, FALSE otherwise.
	 */
	private boolean readGeometryTheorem(GeoGebraOGPInputProverProtocol inputObject, OGPTP theoremProtocol) {
		ILogger logger = OpenGeoProver.settings.getLogger();
		StringReader sr = new StringReader(inputObject.getGeometryTheoremText()); // string with theorem in XML format
		// Create document handler and call the parser
		OGPDocHandler dh = new OGPDocHandler();
		QDParser qdParser = new QDParser();
		try {
			qdParser.parse(dh, sr);
		} catch (Exception e) {
			logger.error("Parser exception caught: " + e.toString());
			e.printStackTrace();
			return false;
		}
		if (!dh.isSuccess()) {
			logger.error("Failed to parse geometry theorem");
			return false;
		}
		
		// Convert parsed GeoGebra theorem
		GeoGebraTheorem ggThm = dh.getTheorem(); // always different from null if parsing was successful
		GeoGebraTheoremConverter thmCnv = null;
		int proverType = OpenGeoProver.settings.getParameters().getProver();
		
		if (proverType == TheoremProver.TP_TYPE_WU || proverType == TheoremProver.TP_TYPE_GROEBNER) {
			thmCnv = new GGThmConverterForAlgebraicProvers(ggThm, theoremProtocol);
			if (thmCnv.convert() == false) {
				logger.error("Failed to convert geometry theorem");
				return false;
			}
		}
		if (proverType == TheoremProver.TP_TYPE_AREA) {
			thmCnv = new GGThmConverterForAreaMethod(ggThm, theoremProtocol);
			if (thmCnv.convert() == false) {
				logger.error("Failed to convert geometry theorem");
				return false;
			}
		}
		// TODO - other types of prover
		
		return true;
	}
	
	/**
	 * @see com.ogprover.api.OGPAPI#prove(com.ogprover.pp.OGPInputProverProtocol)
	 */
	public OGPOutputProverProtocol prove(OGPInputProverProtocol proverInput) {
		ILogger logger = OpenGeoProver.settings.getLogger();
		OGPParameters parameters = OpenGeoProver.settings.getParameters();
		OGPOutput output = OpenGeoProver.settings.getOutput();
		Stopwatch stopwatch = OpenGeoProver.settings.getStopwacth();
		OGPTP thmProtocol = new OGPTP();
		int retCode = OGPConstants.RET_CODE_SUCCESS;
		GeoTheorem theorem = null;
		OpenGeoProver.settings.setParsedTP(null); // clean previous parsed TP
		
		// Prepare output object
		GeoGebraOGPOutputProverProtocol outputObject = new GeoGebraOGPOutputProverProtocol();
		outputObject.setOutputResult(GeoGebraOGPOutputProverProtocol.OGP_OUTPUT_RES_SUCCESS, "true");
		if (!(proverInput instanceof GeoGebraOGPInputProverProtocol))
			return exitProver(outputObject, "Incorrect input object");
		GeoGebraOGPInputProverProtocol inputObject = (GeoGebraOGPInputProverProtocol)proverInput;
			
		// Set prover parameters
		if (this.populateParameters(inputObject) == false)
			return exitProver(outputObject, "Failed in reading input prover parameters");
		
		int proverType = parameters.getProver();
		
		// Read input theorem
		logger.info("Reading input geometry problem...");
		if (this.readGeometryTheorem(inputObject, thmProtocol) == false)
			return exitProver(outputObject, "Failed in reading input geometry theorem");
		OpenGeoProver.settings.setParsedTP(thmProtocol);
		
		// Creating output files
		if (parameters.createReport()) {
			String outputFmt = parameters.getOutputFormat();
			String outputFile = parameters.getOutputFile();
			LaTeXFileWriter latexWriter = null;
			XMLFileWriter xmlWriter = null;
			
			if (outputFile == null) {
				logger.error("Missing output file name");
				return exitProver(outputObject, "Failed to create output files");
			}
			if (!outputFmt.equals("A") && !outputFmt.equals("L") && !outputFmt.equals("X")) {
				logger.error("Invalid format of output file");
				return exitProver(outputObject, "Failed to create output files");
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
		// Opening output report
		OGPReport report = new OGPReport(thmProtocol);
		report.openReport();
		
		// Validation of Theorem Protocol
		if (!thmProtocol.isValid()) {
			output.close();
			return exitProver(outputObject, "Theorem protocol is invalid");
		}
		
		// Transformation to algebraic form
		Double conversionToAlgFormTime = 0.0;
		if (proverType == TheoremProver.TP_TYPE_WU || proverType == TheoremProver.TP_TYPE_GROEBNER) {
			stopwatch.startMeasureTime();
			try {
				output.openSection("Transformation of Construction Protocol to algebraic form");
			} catch (IOException e) {
				logger.error("Failed to write to output file(s).");
				output.close();
				return exitProver(outputObject, "Failed writing to output files");
			}
			retCode = thmProtocol.convertToAlgebraicForm();
			if (retCode != OGPConstants.RET_CODE_SUCCESS) {
				output.close();
				return exitProver(outputObject, "Failed conversion of geometry theorem to algebraic form");
			}
			theorem = thmProtocol.getAlgebraicGeoTheorem();
			
			OpenGeoProver.settings.getStopwacth().endMeasureTime();
			conversionToAlgFormTime = OGPUtilities.roundUpToPrecision(stopwatch.getTimeIntSec());
			outputObject.setOutputResult(GeoGebraOGPOutputProverProtocol.OGP_OUTPUT_RES_TIME, conversionToAlgFormTime.toString());
			
			// Write details about conversion to output files
			try {
				output.openSubSection("Time spent for transformation of Construction Protocol to algebraic form", false);
				output.openEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
				output.openItem();
				output.writePlainText(conversionToAlgFormTime + " seconds");
				output.closeItem();
				output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
				output.closeSubSection();
				output.closeSection();
			} catch (IOException e) {
				logger.error("Failed to write to output file(s).");
				output.close();
				return exitProver(outputObject, "Failed writing to output files");
			}
		}
		
		// Proving the geometry theorem
		logger.info("Invoking prover method...");
		TheoremProver proverMethod = null;
		OGPTimer timer = OpenGeoProver.settings.getTimer();
		if (proverType == TheoremProver.TP_TYPE_WU) { // Wu's method
			proverMethod = new WuMethodProver(theorem);
		}
		if (proverType == TheoremProver.TP_TYPE_AREA) {
			proverMethod = new AreaMethodProver(thmProtocol);
		}
		// TODO - add here new cases for other types of provers
			
		timer.setTimer(parameters.getTimeLimit()); // setting timer
		stopwatch.startMeasureTime();
		retCode = proverMethod.prove();
		stopwatch.endMeasureTime();
		timer.cancel(); // canceling timer
		
		// Writing results to output files and output object
		logger.info("Prover results:\n");
		report.printProverResults(retCode, outputObject);
		
		return outputObject;
	}
}
