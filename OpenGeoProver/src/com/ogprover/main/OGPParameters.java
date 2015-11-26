/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.main;

import java.util.HashMap;

import com.ogprover.thmprover.TheoremProver;
import com.ogprover.utilities.logger.GeoGebraLogger;
import com.ogprover.utilities.logger.ILogger;


/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for input parameters of OpenGeoProver</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */
public class OGPParameters {
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
	
	// list of parameters' names
	// usage of parameters/options:
	// 		OGP -I theorem01[.gcl/xml] -i G -O theorem01_output -o A -p W -c 2 -l B [-v] -t 10000 -s 2000
	// there is default value for each parameter; they are defined in OGPConstants.java class as
	// DEF_VAL_PARAM_xxx values.
	/**
	 * <i><b>
	 * Parameter for name of input file (with construction and statement to prove, described 
	 * in GCLC or XML format, or with polynomial representation of geometry problem in XML format);
	 * if omitted then theorem from internal memory will be used for proving; extension is optional;
	 * if extension is omitted then default will be applied that corresponds to given input format; 
	 * parameter 'i' will determine format of input file
	 * </b></i>
	 */
	public static final String PARAM_INPUT_FILE = "I";
	/**
	 * <i><b>
	 * Parameter for format of input file: can be O (for internal OGP XML format for constructions), 
	 * A (for XML format for algebraic form of geometric problem); G for GCLC format; default is O
	 * </b></i>
	 */
	public static final String PARAM_INPUT_FORMAT = "i";
	/**
	 * <i><b>
	 * Parameter for name of output file; default is "output"; extension is determined by output format
	 * if not explicitly set (can be '.xml' or '.tex'); only base file name (without extension) is kept
	 * in this parameter
	 * </b></i>
	 */
	public static final String PARAM_OUTPUT_FILE = "O";
	/**
	 * <i><b>
	 * Parameter for format of output file: can be X (for XML), 
	 * L (for LaTeX); A (for all) and N (for none - report will not be created;
	 * proving will be faster); A is default
	 * </b></i>
	 */
	public static final String PARAM_OUTPUT_FORMAT = "o";
	/**
	 * <i><b>
	 * Parameter for method for proving; default is "W" (Wu), and can also be "G" (Groebner) or "A" (Area method)
	 * </b></i>
	 */
	public static final String PARAM_PROVER = "p";
	/**
	 * <i><b>
	 * Parameter for level of concurrency i.e. number of concurrent threads; default is 1
	 * </b></i>
	 */
	public static final String PARAM_CONCURRENCY_LEVEL = "c";
	/**
	 * <i><b>
	 * Parameter for log level; default is "B" - basic (log errors, warnings and infos);
	 * also can be "E" (only errors), "W" (only warnings and errors), "I" (same as basic),
	 * "D" (basic + debug) and "N" (none i.e. turn off logging).
	 * </b></i>
	 */
	public static final String PARAM_LOG_LEVEL = "l";
	/**
	 * <i><b>
	 * Parameter with no specific value assigned; 
	 * simply if exists in command line its value will be true; 
	 * otherwise false; if it is true, then all log messages will be printed to standard 
	 * output as well; especially info will give messages about actions taken by prover
	 * </b></i>
	 */
	public static final String PARAM_VERBOSE = "v";
	/**
	 * <i><b>
	 * Parameter for time limit in milliseconds for proving single theorem (default is 10 seconds)
	 * </b></i>
	 */
	public static final String PARAM_TIME_LIMIT = "t";
	/**
	 * <i><b>
	 * Parameter for space limit as maximal number of terms in single polynomial obtained during proving process
	 * </b></i>
	 */
	public static final String PARAM_SPACE_LIMIT = "s";
	
	// other variables
	/**
	 * Map of parameters: key is parameter name (letter(s) from option of command line), value is String parameter value
	 */
	private HashMap<String, String> params;
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * Method for adding or updating parameter in map with all parameters.
	 * 
	 * @param key		Parameter key of parameter to be added/updated
	 * @param value	    New parameter value of parameter to be added/updated
	 * @return			Same as return of java.util.HashMap.put and null
	 * 					when parameter name was incorrect
	 */
	public String put(String key, String value) {
		if (key.equals(OGPParameters.PARAM_INPUT_FILE) ||
			key.equals(OGPParameters.PARAM_INPUT_FORMAT) ||
			key.equals(OGPParameters.PARAM_OUTPUT_FILE) ||
			key.equals(OGPParameters.PARAM_OUTPUT_FORMAT) ||
			key.equals(OGPParameters.PARAM_PROVER) ||
			key.equals(OGPParameters.PARAM_CONCURRENCY_LEVEL) ||
			key.equals(OGPParameters.PARAM_LOG_LEVEL) ||
			key.equals(OGPParameters.PARAM_VERBOSE) ||
			key.equals(OGPParameters.PARAM_TIME_LIMIT) ||
			key.equals(OGPParameters.PARAM_SPACE_LIMIT))
			return this.params.put(key, value);
		
		OpenGeoProver.settings.getLogger().error("Bad parameter name passed in.");
		return null;
	}
	
	// Specific put methods
	/**
	 * Method to put input file name
	 * 
	 * @param fileName	File name to put
	 * @return	Value of <b>OGPParameters.put(String key, String value)</b> method
	 */
	public String putInputFile(String fileName) {
		return this.put(OGPParameters.PARAM_INPUT_FILE, fileName);
	}
	
	/**
	 * Method to put input format type
	 * 
	 * @param fmt	Format type to put
	 * @return	Value of <b>OGPParameters.put(String key, String value)</b> method
	 */
	public String putInputFormat(String fmt) {
		return this.put(OGPParameters.PARAM_INPUT_FORMAT, fmt);
	}
	
	/**
	 * Method to put output file name
	 * 
	 * @param fileName	File name to put
	 * @return	Value of <b>OGPParameters.put(String key, String value)</b> method
	 */
	public String putOutputFile(String fileName) {
		return this.put(OGPParameters.PARAM_OUTPUT_FILE, fileName);
	}
	
	/**
	 * Method to put output format type
	 * 
	 * @param fmt	Format type to put
	 * @return	Value of <b>OGPParameters.put(String key, String value)</b> method
	 */
	public String putOutputFormat(String fmt) {
		return this.put(OGPParameters.PARAM_OUTPUT_FORMAT, fmt);
	}
	
	/**
	 * Method to put prover type
	 * 
	 * @param proverType	Prover type to put
	 * @return	Value of <b>OGPParameters.put(String key, String value)</b> method
	 */
	public String putProver(int proverType) {
		String retCode = null;
		
		switch (proverType) {
		case TheoremProver.TP_TYPE_WU:
			retCode = this.put(OGPParameters.PARAM_PROVER, "W");
			break;
		case TheoremProver.TP_TYPE_GROEBNER:
			retCode = this.put(OGPParameters.PARAM_PROVER, "G");
			break;
		case TheoremProver.TP_TYPE_AREA:
			retCode = this.put(OGPParameters.PARAM_PROVER, "A");
			break;
		default: // Wu's prover is default
			retCode = this.put(OGPParameters.PARAM_PROVER, "W");
			break;
		}
		return retCode;
	}
	
	/**
	 * Method to put concurrency level
	 * 
	 * @param n	Concurrency level to put
	 * @return	Value of <b>OGPParameters.put(String key, String value)</b> method
	 */
	public String putConLevel(int n) {
		if (n <= 0 || n > OGPConstants.maxNumOfThreads) {
			OpenGeoProver.settings.getLogger().error("Attempt to put bad number of threads. Setting to default value of 1.");
			return this.put(OGPParameters.PARAM_CONCURRENCY_LEVEL, "1");
		}
		
		return this.put(OGPParameters.PARAM_CONCURRENCY_LEVEL, n + "");
	}
	
	/**
	 * Method to put log level
	 * 
	 * @param logLevel	Log Level to put
	 * @return	Value of <b>OGPParameters.put(String key, String value)</b> method
	 */
	public String putLogLevel(GeoGebraLogger.Level logLevel) {
		String retCode = null;
		
		if (logLevel == null)
			retCode = this.put(OGPParameters.PARAM_LOG_LEVEL, "N");
		else if (logLevel.equals(GeoGebraLogger.INFO))
			retCode = this.put(OGPParameters.PARAM_LOG_LEVEL, "I"); // same as "B" - basic
		else if (logLevel.equals(GeoGebraLogger.ERROR))
			retCode = this.put(OGPParameters.PARAM_LOG_LEVEL, "E");
		else if (logLevel.equals(GeoGebraLogger.WARN))
			retCode = this.put(OGPParameters.PARAM_LOG_LEVEL, "W");
		else if (logLevel.equals(GeoGebraLogger.DEBUG))
			retCode = this.put(OGPParameters.PARAM_LOG_LEVEL, "D");
		else // default is basic level
			retCode = this.put(OGPParameters.PARAM_LOG_LEVEL, "D");
		
		return retCode;
	}
	
	/**
	 * Method to put verbose flag
	 * 
	 * @param verbose	Verbose flag to put
	 * @return	Value of <b>OGPParameters.put(String key, String value)</b> method
	 */
	public String putVerbose(boolean verbose) {
		if (verbose)
			return this.put(OGPParameters.PARAM_VERBOSE, "true");
		return this.put(OGPParameters.PARAM_VERBOSE, "false");
	}
	
	/**
	 * Method to put time limit value in milliseconds
	 * 
	 * @param timeLim	Time limit to put
	 * @return	Value of <b>OGPParameters.put(String key, String value)</b> method
	 */
	public String putTimeLimit(double timeLim) {
		if (timeLim <= 0) {
			OpenGeoProver.settings.getLogger().error("Time limit in milliseconds must be a positive value.");
			// assign default value
			this.put(OGPParameters.PARAM_TIME_LIMIT, OGPConstants.DEF_VAL_PARAM_TIME_LIMIT);
		}
		
		return this.put(OGPParameters.PARAM_TIME_LIMIT, timeLim + "");
	}
	
	/**
	 * Method to put space limit
	 * 
	 * @param spaceLim	Space limit to put
	 * @return	Value of <b>OGPParameters.put(String key, String value)</b> method
	 */
	public String putSpaceLimit(int spaceLim) {
		if (spaceLim <= 0) {
			OpenGeoProver.settings.getLogger().error("Space limit in number of terms must be positive value.");
			// assign default value
			this.put(OGPParameters.PARAM_SPACE_LIMIT, OGPConstants.DEF_VAL_PARAM_SPACE_LIMIT);
		}
		
		return this.put(OGPParameters.PARAM_SPACE_LIMIT, spaceLim + "");
	}
	
	
	/**
	 * Method for fetching a value assigned to specified parameter.
	 * 
	 * @param key		Key which represents parameter.
	 * @return			Value assigned to passed in key or null in case of error.
	 */
	public String get(String key) {
		if (key.equals(OGPParameters.PARAM_INPUT_FILE) ||
			key.equals(OGPParameters.PARAM_INPUT_FORMAT) ||
			key.equals(OGPParameters.PARAM_OUTPUT_FILE) ||
			key.equals(OGPParameters.PARAM_OUTPUT_FORMAT) ||
			key.equals(OGPParameters.PARAM_PROVER) ||
			key.equals(OGPParameters.PARAM_CONCURRENCY_LEVEL) ||
			key.equals(OGPParameters.PARAM_LOG_LEVEL) ||
			key.equals(OGPParameters.PARAM_VERBOSE) ||
			key.equals(OGPParameters.PARAM_TIME_LIMIT) ||
			key.equals(OGPParameters.PARAM_SPACE_LIMIT))
			return this.params.get(key);
		
		OpenGeoProver.settings.getLogger().error("Bad parameter name passed in.");
		return null;
	}
	
	// Specific get methods
	/**
	 * @return	Input file name
	 */
	public String getInputFile() {
		return this.get(OGPParameters.PARAM_INPUT_FILE);
	}
	
	/**
	 * @return	Input file format
	 */
	public String getInputFormat() {
		return this.get(OGPParameters.PARAM_INPUT_FORMAT);
	}
	
	/**
	 * @return	Output file name
	 */
	public String getOutputFile() {
		return this.get(OGPParameters.PARAM_OUTPUT_FILE);
	}
	
	/**
	 * @return	Output file format
	 */
	public String getOutputFormat() {
		return this.get(OGPParameters.PARAM_OUTPUT_FORMAT);
	}
	
	/**
	 * @return	Constant representing prover type
	 */
	public int getProver() {
		String value = this.get(OGPParameters.PARAM_PROVER);
		
		if (value.equals("W"))
			return TheoremProver.TP_TYPE_WU;
		if (value.equals("G"))
			return TheoremProver.TP_TYPE_GROEBNER;
		if (value.equals("A"))
			return TheoremProver.TP_TYPE_AREA;
		
		OpenGeoProver.settings.getLogger().error("Bad value assigned to parameter");
		
		return OGPConstants.ERR_CODE_GENERAL;
	}
	
	/**
	 * @return	Integer value of concurrency level (number of parallel threads)
	 */
	public int getConLevel() {
		String value = this.get(OGPParameters.PARAM_CONCURRENCY_LEVEL);
		int n = Integer.parseInt(value);
		
		if (n <= 0 || n > OGPConstants.maxNumOfThreads) {
			OpenGeoProver.settings.getLogger().error("Bad value assigned to parameter");
			return OGPConstants.ERR_CODE_GENERAL;
		}
		
		return n;
	}
	
	/**
	 * @return	One of GeoGebraLogger.Level.xxx enumerated constants representing log level
	 */
	public GeoGebraLogger.Level getLogLevel() {
		String value = this.get(OGPParameters.PARAM_LOG_LEVEL);
		GeoGebraLogger.Level retCode = GeoGebraLogger.INFO;
		
		switch(value.charAt(0)){
		case 'N':
			retCode = null;
			break;
		case 'B':
			retCode = GeoGebraLogger.INFO;
			break;
		case 'E':
			retCode = GeoGebraLogger.ERROR;
			break;
		case 'I':
			retCode = GeoGebraLogger.INFO;
			break;
		case 'W':
			retCode = GeoGebraLogger.WARN;
			break;
		case 'D':
			retCode = GeoGebraLogger.DEBUG;
			break;
		default: // default is basic level
			OpenGeoProver.settings.getLogger().error("Bad value assigned to log level parameter.");
			break;
		}
		
		return retCode;
	}
	
	/**
	 * @return	Boolean value of verbose flag
	 */
	public boolean getVerbose() {
		String value = this.get(OGPParameters.PARAM_VERBOSE);
		if (value.equals("true"))
			return true;
		return false;
	}
	
	/**
	 * @return	Long value of time limit (in milliseconds)
	 */
	public long getTimeLimit() {
		String value = this.get(OGPParameters.PARAM_TIME_LIMIT);
		long timeLim = Math.round(Double.parseDouble(value));
		
		if (timeLim <= 0) {
			OpenGeoProver.settings.getLogger().error("Bad value assigned to parameter");
			return OGPConstants.ERR_CODE_GENERAL;
		}
		
		return timeLim;
	}
	
	/**
	 * @return	Integer value of space limit
	 */
	public int getSpaceLimit() {
		String value = this.get(OGPParameters.PARAM_SPACE_LIMIT);
		int spaceLim = Integer.parseInt(value);
		
		if (spaceLim <= 0) {
			OpenGeoProver.settings.getLogger().error("Bad value assigned to parameter");
			return OGPConstants.ERR_CODE_GENERAL;
		}
		
		return spaceLim;
	}
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 */
	public OGPParameters() {
		this.params = new HashMap<String, String>();
		this.init();
	}
	
	/*
	 * ======================================================================
	 * ======================= COMMON OBJECT METHODS ========================
	 * ======================================================================
	 */
	/**
	 * Method for initialization of parameters with their default values
	 */
	private void init() {
		this.params.put(OGPParameters.PARAM_INPUT_FILE, OGPConstants.DEF_VAL_PARAM_INPUT_FILE);
		this.params.put(OGPParameters.PARAM_INPUT_FORMAT, OGPConstants.DEF_VAL_PARAM_INPUT_FORMAT);
		this.params.put(OGPParameters.PARAM_OUTPUT_FILE, OGPConstants.DEF_VAL_PARAM_OUTPUT_FILE);
		this.params.put(OGPParameters.PARAM_OUTPUT_FORMAT, OGPConstants.DEF_VAL_PARAM_OUTPUT_FORMAT);
		this.params.put(OGPParameters.PARAM_PROVER, OGPConstants.DEF_VAL_PARAM_PROVER);
		this.params.put(OGPParameters.PARAM_CONCURRENCY_LEVEL, OGPConstants.DEF_VAL_PARAM_CONCURRENCY_LEVEL);
		this.params.put(OGPParameters.PARAM_LOG_LEVEL, OGPConstants.DEF_VAL_PARAM_LOG_LEVEL);
		this.params.put(OGPParameters.PARAM_VERBOSE, OGPConstants.DEF_VAL_PARAM_VERBOSE);
		this.params.put(OGPParameters.PARAM_TIME_LIMIT, OGPConstants.DEF_VAL_PARAM_TIME_LIMIT);
		this.params.put(OGPParameters.PARAM_SPACE_LIMIT, OGPConstants.DEF_VAL_PARAM_SPACE_LIMIT);
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * <i>[static method]</i><br>
	 * Method for printing help about command line.
	 */
	public static void printHelp() {
		StringBuilder sb = new StringBuilder();
		sb.append("Correct command line should include following options/parameters: ");
		sb.append("\n\n-I\tname of input file (format can be gclc or xml file);\n\t name is not required - if not set, theorems from internal memory will be used");
		sb.append("\n\n-i\tformat of input file - can be \"G\" for gclc file, \"O\" for OGP xml file or \n\t\"A\" for xml file with theorem in algebraic form; default is \"");
		sb.append(OGPConstants.DEF_VAL_PARAM_INPUT_FORMAT);
		sb.append("\"");
		sb.append("\n\n-O\tname of output file (format can be tex or xml file);\n\t name is not required - default name is \"");
		sb.append(OGPConstants.DEF_VAL_PARAM_OUTPUT_FILE);
		sb.append("\"");
		sb.append("\n\n-o\tformat of output file - can be \"L\" for (la)tex file, \"X\" for xml file,\n\t \"A\" for all formats or \"N\" to not create the output reports; \n\tdefault is \"");
		sb.append(OGPConstants.DEF_VAL_PARAM_OUTPUT_FORMAT);
		sb.append("\"");
		sb.append("\n\n-p\tprover method - can be \"W\" for Wu's method or \"G\" for Groebner basis method - default is \"");
		sb.append(OGPConstants.DEF_VAL_PARAM_PROVER);
		sb.append("\"");
		sb.append("\n\n-c\tlevel of concurrency i.e. number of parallel threads for polynomials' multiplying -\n\t default is ");
		sb.append(OGPConstants.DEF_VAL_PARAM_CONCURRENCY_LEVEL);
		sb.append(" and maximal value is ");
		sb.append(OGPConstants.maxNumOfThreads);
		sb.append("\n\n-l\tlog level - can be \"N\" (no logging), \"B\" (basic - errors, infos and warnings),\n\t \"E\" (errors), \"W\" (warnings and errors), \"I\" (infos, errors and warnings - same as basic level)\n\t and \"D\" (debug + basic); default is \"");
		sb.append(OGPConstants.DEF_VAL_PARAM_LOG_LEVEL);
		sb.append("\"");
		sb.append("\n\n-v\tverbose flag - to print log messages to standard output (not required)");
		sb.append("\n\n-t\ttime limit in seconds for proving of single theorem; default is ");
		sb.append(Double.parseDouble(OGPConstants.DEF_VAL_PARAM_TIME_LIMIT)/1000);
		sb.append("\n\n-s\tspace limit as maximal number of terms in single polynomial obtained during proving process;\n\t default is ");
		sb.append(OGPConstants.DEF_VAL_PARAM_SPACE_LIMIT);
		sb.append("\n\n");
		
		System.out.println(sb.toString());
	}
	
	/**
	 * <i>[static method]</i><br>
	 * Method for populating prover parameters from command line.
	 * 
	 * @param args	Command line arguments
	 * @return		RET_CODE_SUCCESS or ERR_CODE_xxx
	 */
	public static int readParametersFromCommandLine(String[] args) {
		int ii = 0, argc = args.length;
		OGPParameters parameters = OpenGeoProver.settings.getParameters();
		ILogger logger = OpenGeoProver.settings.getLogger();
		boolean setInputFmt = false, setInputName = false;
		int retCode = OGPConstants.RET_CODE_SUCCESS;
		
		if (parameters == null) {
			parameters = new OGPParameters();
			OpenGeoProver.settings.setParameters(parameters);
		}
		
		// Check if first 
		while (ii < argc && retCode == OGPConstants.RET_CODE_SUCCESS) {
			String currArg = args[ii];
			
			if (currArg.charAt(0) == '-') { // argument with parameter name
				String paramName = currArg.substring(1);
				String paramValue = (ii+1 < argc) ? args[ii+1] : null; // next argument is parameter value
				
				if (paramValue != null && paramValue.charAt(0) == '-') // not value, but next parameter
					paramValue = null;
				
				// input file name
				if (paramName.equals(OGPParameters.PARAM_INPUT_FILE)) {
					if (paramValue == null) {
						logger.error("Missing input file name - ignoring this parameter.");
						retCode = OGPConstants.ERR_CODE_GENERAL;
						ii++;
					}
					else {
						int lastPoint = paramValue.lastIndexOf('.');
						if (lastPoint >= 0) { // extension provided
							String extension = paramValue.substring(lastPoint+1);
						
							if (extension == null || (!extension.equals("xml") && !extension.equals("gcl"))) {
								logger.error("Bad extension provided - ignoring this parameter.");
								retCode = OGPConstants.ERR_CODE_GENERAL;
							}
						}
						else if (setInputFmt){ // check if input format is set - then set extension
							String inputFmt = parameters.getInputFormat();
							
							if (inputFmt == null) {
								logger.error("Missing input format - unable to attach extension to input file name.");
								retCode = OGPConstants.ERR_CODE_GENERAL;
							}
							else {
								if (inputFmt.equals("O") || inputFmt.equals("A"))
									paramValue = paramValue.concat(".xml");
								else if (inputFmt.equals("G"))
									paramValue = paramValue.concat(".gcl");
								else {
									logger.error("Unknown input format - unable to attach extension to input file name.");
									retCode = OGPConstants.ERR_CODE_GENERAL;
								}
							}
						}
						
						if (retCode == OGPConstants.RET_CODE_SUCCESS) {
							parameters.putInputFile(paramValue);
							setInputName = true;
						}
						
						ii += 2;
					}
				}
				
				// input format
				else if (paramName.equals(OGPParameters.PARAM_INPUT_FORMAT)) {
					if (paramValue == null) {
						logger.error("Missing input format - OGP XML is default.");
						retCode = OGPConstants.ERR_CODE_GENERAL;
						ii++;
					}
					else {
						if (!paramValue.equals("O") && !paramValue.equals("A") &&!paramValue.equals("G")) {
							logger.error("Bad extension provided - ignoring this parameter.");
							retCode = OGPConstants.ERR_CODE_GENERAL;
						}
						else {
							parameters.putInputFormat(paramValue);
							setInputFmt = true;
							
							// attach default extension to name of input file if not yet provided
							if (setInputName) {
								String inputName = parameters.getInputFile();
								
								if (inputName.lastIndexOf('.') < 0) { // extension not yet set
									if (paramValue.equals("O") || paramValue.equals("A"))
										inputName = inputName.concat(".xml");
									else
										inputName = inputName.concat(".gcl");
									parameters.putInputFile(inputName);	
								}
							}
						}
						ii += 2;
					}
				}
				
				// output file name
				else if (paramName.equals(OGPParameters.PARAM_OUTPUT_FILE)) {
					if (paramValue == null) {
						logger.error("Missing output file name - ignoring this parameter.");
						retCode = OGPConstants.ERR_CODE_GENERAL;
						ii++;
					}
					else {
						int lastPoint = paramValue.lastIndexOf('.');
						if (lastPoint >= 0) { // extension provided
							String extension = paramValue.substring(lastPoint+1);
						
							if (extension == null || (!extension.equals("xml") && !extension.equals("tex"))) {
								logger.error("Bad extension provided - ignoring this parameter.");
								retCode = OGPConstants.ERR_CODE_GENERAL;
							}
							else { // extract base file name since it could happen that 2 report files will be created, 
								   // with that name but different extensions
								paramValue = paramValue.substring(0, lastPoint);
								
								if (paramValue == null) {
									logger.error("Null output file name provided - ignoring this parameter.");
									retCode = OGPConstants.ERR_CODE_GENERAL;
								}
							}
						}
						
						if (retCode == OGPConstants.RET_CODE_SUCCESS)
							parameters.putOutputFile(paramValue);
						
						ii += 2;
					}
				}
				
				// output format
				else if (paramName.equals(OGPParameters.PARAM_OUTPUT_FORMAT)) {
					if (paramValue == null) {
						logger.error("Missing output format - all is default.");
						retCode = OGPConstants.ERR_CODE_GENERAL;
						ii++;
					}
					else {
						if (!paramValue.equals("X") && !paramValue.equals("L") && !paramValue.equals("A") && !paramValue.equals("N")) {
							logger.error("Bad output format provided - ignoring this parameter.");
							retCode = OGPConstants.ERR_CODE_GENERAL;
						}
						else
							parameters.putOutputFormat(paramValue);
						ii += 2;
					}
				}
				
				// prover
				else if (paramName.equals(OGPParameters.PARAM_PROVER)) {
					if (paramValue == null) {
						logger.error("Missing prover type - Wu's method is default.");
						retCode = OGPConstants.ERR_CODE_GENERAL;
						ii++;
					}
					else {
						if (!paramValue.equals("W") && !paramValue.equals("G")) {
							logger.error("Bad prover type provided - ignoring this parameter.");
							retCode = OGPConstants.ERR_CODE_GENERAL;
						}
						else 
							parameters.put(OGPParameters.PARAM_PROVER, paramValue);
						ii += 2;
					}
				}
				
				// concurrency level
				else if (paramName.equals(OGPParameters.PARAM_CONCURRENCY_LEVEL)) {
					if (paramValue == null) {
						logger.error("Missing concurrency level - single thread is default.");
						retCode = OGPConstants.ERR_CODE_GENERAL;
						ii++;
					}
					else {
						int value = Integer.parseInt(paramValue);
						if (value <= 0 || value > OGPConstants.maxNumOfThreads) {
							logger.error("Bad concurrency level provided - ignoring this parameter.");
							retCode = OGPConstants.ERR_CODE_GENERAL;
						}
						else
							parameters.putConLevel(value);
						ii += 2;
					}
				}
				
				// log level
				else if (paramName.equals(OGPParameters.PARAM_LOG_LEVEL)) {
					if (paramValue == null) {
						logger.error("Missing log level - basic is default.");
						retCode = OGPConstants.ERR_CODE_GENERAL;
						ii++;
					}
					else {
						if (!paramValue.equals("N") && !paramValue.equals("B") &&
							!paramValue.equals("E") && !paramValue.equals("I") &&
							!paramValue.equals("W") && !paramValue.equals("D")) {
							logger.error("Bad log level provided - ignoring this parameter.");
							retCode = OGPConstants.ERR_CODE_GENERAL;
						}
						else
							parameters.put(OGPParameters.PARAM_LOG_LEVEL, paramValue);
						ii += 2;
					}
				}
				
				// verbose flag
				else if (paramName.equals(OGPParameters.PARAM_VERBOSE)) {
					parameters.putVerbose(true);
					ii++;
				}
				
				// time limit
				else if (paramName.equals(OGPParameters.PARAM_TIME_LIMIT)) {
					if (paramValue == null) {
						logger.error("Missing time limit - " + OGPConstants.DEF_VAL_PARAM_TIME_LIMIT + " milliseconds is default.");
						retCode = OGPConstants.ERR_CODE_GENERAL;
						ii++;
					}
					else {
						double value = Double.parseDouble(paramValue);
						if (value <= 0) {
							logger.error("Bad time limit provided - ignoring this parameter.");
							retCode = OGPConstants.ERR_CODE_GENERAL;
						}
						else
							parameters.putTimeLimit(value*1000); // from command line value is set in seconds
						ii += 2;
					}
				}
				
				// space limit
				else if (paramName.equals(OGPParameters.PARAM_SPACE_LIMIT)) {
					if (paramValue == null) {
						logger.error("Missing space limit - " + OGPConstants.DEF_VAL_PARAM_SPACE_LIMIT + " terms is default.");
						retCode = OGPConstants.ERR_CODE_GENERAL;
						ii++;
					}
					else {
						int value = Integer.parseInt(paramValue);
						if (value <= 0) {
							logger.error("Bad space limit provided - ignoring this parameter.");
							retCode = OGPConstants.ERR_CODE_GENERAL;
						}
						else
							parameters.putSpaceLimit(value);
						ii += 2;
					}
				}
				
				// not supported parameter
				else {
					logger.error("Found unknown parameter '" + paramName + "'.");
					retCode = OGPConstants.ERR_CODE_GENERAL;
				}
				
			}
			else {
				logger.error("Non-parametric argument found when expected parameter name.");
				retCode = OGPConstants.ERR_CODE_GENERAL;
				ii++;
			}
		}
		
		return retCode;
	}
	
	/**
	 * Method that checks if output report(s) has/have to be created.
	 * 
	 * @return	TRUE if report has to be created, or FALSE otherwise.
	 */
	public boolean createReport() {
		return !OpenGeoProver.settings.getParameters().getOutputFormat().equals("N");
	}
}
