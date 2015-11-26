/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.main;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.ogprover.pp.tp.OGPTP;
import com.ogprover.utilities.OGPTimer;
import com.ogprover.utilities.Stopwatch;
import com.ogprover.utilities.io.OGPOutput;
import com.ogprover.utilities.logger.GeoGebraLogger;
import com.ogprover.utilities.logger.ILogger;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class with all configuration settings necessary for prover to work.
 *     Each object of this class represents settings for one prover
 *     execution/session. It contains following settings:
 *     <ul>
 *     		<li>Information about log file for storing technical details about prover execution</li>
 *     		<li>Set of parameters' values necessary for prover work</li>
 *     		<li>Details about output files for prover report</li>
 *     		<li>Timer for limiting time of prover execution</li>
 *     		<li>Information about time spent in execution of some parts of prover work</li>
 *     		<li>Information about space complexity of current prover session</li>
 *     </ul></dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */
public class OGPConfigurationSettings {
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
	 * Specific configuration settings
	 */
	/**
	 * Set of all prover parameters with their specified and default values
	 */
	private OGPParameters parameters = null;
	/**
	 * Object that describes details about prover output reports
	 */
	private OGPOutput output = null;
	/**
	 * Timer used to limit the prover's execution time
	 */
	private OGPTimer timer = null;
	/**
	 * Utility to measure time spent for execution of some parts of prover's work
	 */
	private Stopwatch stopwatch = null;
	/**
	 * Maximal number of terms in polynomials obtained during execution of prover
	 */
	private int maxNumOfTerms = 0;
	// ==================== Log file ====================
	/**
	 * Name of log file
	 */
	private String logFileName = null;
	/**
	 * <i><b>
	 * Default name of log file
	 * </b></i>
	 */
	public static final String defaultLogFileName = OpenGeoProver.class.getSimpleName();
	/**
	 * <i><b>
	 * Date and time format - first pattern
	 * <ul>
	 * 	<li>MM - month</li>
	 * 	<li>dd - day of month</li>
	 * 	<li>yyyy - year</li>
	 * 	<li>HH - hour (00-23)</li>
	 * 	<li>mm - minutes</li>
	 * 	<li>ss - seconds</li>
	 * </ul>
	 * </b></i>
	 */
	public static final String dateTimeFormat1 = "[MM_dd_yyyy@HH_mm_ss]";
	// other formats should be added here ...
	/**
	 * Logger object that manages logging of technical details 
	 * of prover execution in log file
	 */
	private ILogger logger = null;
	/**
	 * Theorem protocol obtained from parsing and conversion
	 */
	private OGPTP parsedTP = null;
	/*
	 * Return codes of some operations 
	 */
	/**
	 * Return code of pseudo division operation
	 */
	private int retCodeOfPseudoDivision = OGPConstants.RET_CODE_SUCCESS;
	
	

	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * Method that sets object with prover parameters
	 * 
	 * @param parameters The parameters to set
	 */
	public void setParameters(OGPParameters parameters) {
		this.parameters = parameters;
	}

	/**
	 * Method that retrieves all prover parameters
	 * 
	 * @return The parameters
	 */
	public OGPParameters getParameters() {
		return parameters;
	}

	/**
	 * Method that sets object with details about prover reports
	 * 
	 * @param output The objects for output details to set
	 */
	public void setOutput(OGPOutput output) {
		this.output = output;
	}

	/**
	 * Method that retrieves the object with details about prover output
	 * 
	 * @return The prover output details
	 */
	public OGPOutput getOutput() {
		return output;
	}

	/**
	 * Method that sets prover timer
	 * 
	 * @param timer The timer object to set
	 */
	public void setTimer(OGPTimer timer) {
		this.timer = timer;
	}

	/**
	 * Method that retrieves prover's timer
	 * 
	 * @return The prover's timer
	 */
	public OGPTimer getTimer() {
		return timer;
	}

	/**
	 * Method that sets the stopwatch
	 * 
	 * @param stopwatch The stopwatch to set
	 */
	public void setStopwacth(Stopwatch stopwatch) {
		this.stopwatch = stopwatch;
	}

	/**
	 * Method that retrieves the stopwatch
	 * 
	 * @return The stopwatch of prover
	 */
	public Stopwatch getStopwacth() {
		return stopwatch;
	}

	/**
	 * Method that sets the maximal number of terms in polynomials 
	 * obtained during execution of prover
	 * 
	 * @param maxNumOfTerms The maximal number of terms to set
	 */
	public void setMaxNumOfTerms(int maxNumOfTerms) {
		this.maxNumOfTerms = maxNumOfTerms;
	}

	/**
	 * Method that retrieves the maximal number of terms in polynomials 
	 * obtained during execution of prover
	 * 
	 * @return The maximal number of terms
	 */
	public int getMaxNumOfTerms() {
		return maxNumOfTerms;
	}

	/**
	 * Method that sets prover's logger 
	 * 
	 * @param logger The logger to set
	 */
	public void setLogger(ILogger logger) {
		this.logger = logger;
	}

	/**
	 * Method that retrieves prover's logger
	 * 
	 * @return The logger
	 */
	public ILogger getLogger() {
		return logger;
	}

	/**
	 * @param parsedTP the parsedTP to set
	 */
	public void setParsedTP(OGPTP parsedTP) {
		this.parsedTP = parsedTP;
	}

	/**
	 * @return the parsedTP
	 */
	public OGPTP getParsedTP() {
		return parsedTP;
	}

	/**
	 * @param retCodeOfPseudoDivision the retCodeOfPseudoDivision to set
	 */
	public void setRetCodeOfPseudoDivision(int retCodeOfPseudoDivision) {
		this.retCodeOfPseudoDivision = retCodeOfPseudoDivision;
	}

	/**
	 * @return the retCodeOfPseudoDivision
	 */
	public int getRetCodeOfPseudoDivision() {
		return retCodeOfPseudoDivision;
	}
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Default constructor method
	 */
	public OGPConfigurationSettings() {
		this(null);
	}
	
	/**
	 * Constructor method
	 * 
	 * @param logFileName	Name of log file; if equals to null, default is used
	 */
	public OGPConfigurationSettings(String logFileName) {
		this(logFileName, null);
	}
	
	/**
	 * Constructor method
	 * 
	 * @param logFileName			Name of log file; if equals to null, default is used
	 * @param logFileRootDirectory	Root directory of log file; if null is passed 
	 * 								for this argument, then default location is used
	 * 								("log" sub-directory of current directory)
	 */
	public OGPConfigurationSettings(String logFileName, String logFileRootDirectory) {
		OGPParameters params = new OGPParameters();
		params.putLogLevel(GeoGebraLogger.DEBUG);
		this.setParameters(params);
		this.setOutput(new OGPOutput(null, null));
		this.setTimer(new OGPTimer());
		this.setStopwacth(new Stopwatch());
		
		if (logFileName != null)
			this.logFileName = logFileName;
		else {
			// Default name of log file is: "OpenGeoProver[current date and time in specified format].log"
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat(OGPConfigurationSettings.dateTimeFormat1);
			this.logFileName = OGPConfigurationSettings.defaultLogFileName + sdf.format(cal.getTime());
		}
		
		// Set the file logger
		this.setLogger(GeoGebraLogger.factory(this.logFileName, logFileRootDirectory));
	}
	
	

	/*
	 * ======================================================================
	 * ======================= COMMON OBJECT METHODS ========================
	 * ======================================================================
	 */
	
	

	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method to log space error during pseudo division.
	 * It is called from pseudo division algorithm to save
	 * output results that can be read later by prover to write
	 * them in reports.
	 * 
	 * @param numOfTerms	Maximal number of terms obtained during prover execution
	 */
	public void logSpaceErrorInPseudoDivision(int numOfTerms) {
		StringBuilder sb = new StringBuilder();
		sb.append("Space limit exceeded in pseudo division. Obtained polynomial with ");
		sb.append(numOfTerms);
		sb.append(" terms");
		this.logger.error(sb.toString());
		
		this.setRetCodeOfPseudoDivision(OGPConstants.ERR_CODE_SPACE);
		this.maxNumOfTerms = numOfTerms;
	}
	
	/**
	 * Method to log time error during pseudo division.
	 * It is called from pseudo division algorithm to save
	 * output results that can be read later by prover to write
	 * them in reports.
	 */
	public void logTimeErrorInPseudoDivision() {
		this.logger.error("Time limit exceeded in pseudo division");
		
		this.setRetCodeOfPseudoDivision(OGPConstants.ERR_CODE_TIME);
	}
	
	/**
	 * Method to log general error during pseudo division.
	 * It is called from pseudo division algorithm to save
	 * output results that can be read later by prover to write
	 * them in reports.
	 * 
	 * @param errorMsg		Error message for log file
	 */
	public void logGeneralErrorInPseudoDivision(String errorMsg) {
		this.logger.error(errorMsg);
		
		this.setRetCodeOfPseudoDivision(OGPConstants.ERR_CODE_GENERAL);
	}

}