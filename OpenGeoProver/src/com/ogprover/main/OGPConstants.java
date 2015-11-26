/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.main;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for all global constants used in project</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */
public class OGPConstants {
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
	
	
	
	// constants for work with real constants
	/**
	 * <i><b>Number of significant decimal places</b></i>
	 */
	public static final int precision = 6; 		     // warning: update EPISLON and precPower if change this value!
	/**
	 * <i><b>
	 * Used for comparison of double values (e.g. if y-epsilon < x < y+epsilon then x == y);
	 * this is actually pow(10, -precision)
	 * </b></i>
	 */
	public static final double EPSILON = 0.000001;   // warning: update this value if precision is changed!
	/**
	 * <i><b>This is pow(10, precision)</b></i>
	 */
	public static final long precPower = 1000000;    // warning: update this value if precision is changed!
	
	
	
	// constants for concurrency
	/**
	 * <i><b>Maximal number of threads for concurrent multiplying of polynomials</b></i>
	 */
	public static final short maxNumOfThreads = 100;
	
	
	
	// constants for error codes
	/**
	 * <i><b>Successfully completed execution</b></i>
	 */
	public static final int RET_CODE_SUCCESS = 0;
	/**
	 * <i><b>Generic error</b></i>
	 */
	public static final int ERR_CODE_GENERAL = -1;
	/**
	 * <i><b>Time's up</b></i>
	 */
	public static final int ERR_CODE_TIME = -2;
	/**
	 * <i><b>Reached upper limit for space (like number of terms in polynomials)</b></i>
	 */
	public static final int ERR_CODE_SPACE = -3;
	/**
	 * <i><b>Null object found</b></i>
	 */
	public static final int ERR_CODE_NULL = -4;
	
	
	
	// constants for default parameter values
	/**
	 * <i><b>Default parameter value for input file name</b></i>
	 */
	public static final String DEF_VAL_PARAM_INPUT_FILE = null;
	/**
	 * <i><b>Default parameter value for format of input file</b></i>
	 */
	public static final String DEF_VAL_PARAM_INPUT_FORMAT = "O";
	/**
	 * <i><b>Default parameter value for output file name</b></i>
	 */
	public static final String DEF_VAL_PARAM_OUTPUT_FILE = "output";
	/**
	 * <i><b>Default parameter value for format of output file</b></i>
	 */
	public static final String DEF_VAL_PARAM_OUTPUT_FORMAT = "A";
	/**
	 * <i><b>Default parameter value for prover method</b></i>
	 */
	public static final String DEF_VAL_PARAM_PROVER = "W";
	/**
	 * <i><b>Default parameter value for concurrency value</b></i>
	 */
	public static final String DEF_VAL_PARAM_CONCURRENCY_LEVEL = "1";
	/**
	 * <i><b>Default parameter value for log level</b></i>
	 */
	public static final String DEF_VAL_PARAM_LOG_LEVEL = "B";
	/**
	 * <i><b>Default parameter value for verbose flag</b></i>
	 */
	public static final String DEF_VAL_PARAM_VERBOSE = "false";
	/**
	 * <i><b>Default parameter value for time limit</b></i>
	 */
	public static final String DEF_VAL_PARAM_TIME_LIMIT = "10000"; // time in milliseconds
	/**
	 * <i><b>Default parameter value for space limit</b></i>
	 */
	public static final String DEF_VAL_PARAM_SPACE_LIMIT = "2000"; // maximal number of terms
	
	
	
	// constants for printing polynomial at output
	/**
	 * <i><b>Maximal number of terms in polynomial to print at output</b></i>
	 */
	public static final int MAX_OUTPUT_POLY_TERMS_NUM = 255;
	/**
	 * <i><b>Maximal number of characters in polynomial to print at output</b></i>
	 */
	public static final int MAX_OUTPUT_POLY_CHARS_NUM = 2000;
	/**
	 * <i><b>Maximal number of characters in polynomial to print at XML output</b></i>
	 */
	public static final int MAX_XML_OUTPUT_POLY_CHARS_NUM = 50000;
	/**
	 * <i><b>Maximal number of characters in single output chunk of polynomial</b></i>
	 */
	public static final int MAX_OUTPUT_POLY_CHUNK_SIZE = 90;
	
	
	// constants used in work with NDG conditions
	/**
	 * <i><b>
	 *    Minimal number of points that form combination 
	 *    for investigation of NDG conditions.
	 * </b></i>
	 */
	// Do not change this value until provide corresponding method 
	// in NDGCondition class for examination of that number of points
	public static final int MIN_NUM_OF_NDGC_POINTS = 2;
	/**
	 * <i><b>
	 *    Maximal number of points that form combination 
	 *    for investigation of NDG conditions.
	 * </b></i>
	 */
	// Do not change this value until provide corresponding method 
	// in NDGCondition class for examination of that number of points
	public static final int MAX_NUM_OF_NDGC_POINTS = 4;
	
}
