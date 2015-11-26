/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.utilities.logger.ILogger;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for output prover protocol between GeoGebra and OGP</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */
public class GeoGebraOGPOutputProverProtocol extends OGPOutputProverProtocol {
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
	 * Map with prover's output results - key is result name and value is 
	 * string representation of result value.
	 */
	private Map<String, String> outputResultMap;
	/**
	 * List of NDG conditions in textual format that GeoGebra can understand
	 */
	private Vector<String> ndgList;
	
	/**
	 * Names of OGP output results.
	 */
	public static final String OGP_OUTPUT_RES_SUCCESS = "success";				// Success of prover execution - can be "true" or "false"
	public static final String OGP_OUTPUT_RES_FAILURE_MSG = "failureMessage";	// The message about reason of prover failure (if success = "false")
	public static final String OGP_OUTPUT_RES_PROVER = "proverResult";			// Result of theorem proving - can be "true" (proved), "false" (disproved) or "unknown" (cannot be proved/disproved)
	public static final String OGP_OUTPUT_RES_PROVER_MSG = "proverMessage";		// Prover message (e.g. if time for proving has been expired or space limit has been reached)
	public static final String OGP_OUTPUT_RES_TIME = "time";					// Time in seconds spent for execution
	public static final String OGP_OUTPUT_RES_NUMTERMS = "numTerms";			// Maximal number of terms obtained during prover execution
	// TODO - add here other prover results
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @param outputResultMap the outputResultMap to set
	 */
	public void setOutputResultMap(Map<String, String> outputResultMap) {
		this.outputResultMap = outputResultMap;
	}

	/**
	 * @return the outputResultMap
	 */
	public Map<String, String> getOutputResultMap() {
		return outputResultMap;
	}
	
	/**
	 * @param ndgList the ngdList to set
	 */
	public void setNdgList(Vector<String> ndgList) {
		this.ndgList = ndgList;
	}

	/**
	 * @return the ndgList
	 */
	public Vector<String> getNdgList() {
		return ndgList;
	}

	/**
	 * Method for setting the value of output result.
	 * 
	 * @param resName	Result name
	 * @param resValue	Result value in string format
	 */
	public void setOutputResult(String resName, String resValue) {
		if (this.outputResultMap != null)
			this.outputResultMap.put(resName, resValue);
	}
	
	/**
	 * Method for getting the value of output result.
	 * 
	 * @param resName	Result name
	 * @return			Result value in string format
	 */
	public String getOutputResult(String resName) {
		if (this.outputResultMap != null)
			return this.outputResultMap.get(resName);
		return null;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Default constructor method.
	 */
	public GeoGebraOGPOutputProverProtocol() {
		this.outputResultMap = new HashMap<String, String>();
		this.ndgList = new Vector<String>();
	}
	
	/**
	 * Constructor method.
	 * 
	 * @param outputRes		Output results.
	 * @param ndgList		List of NDG conditions.
	 */
	public GeoGebraOGPOutputProverProtocol(Map<String, String> outputRes, Vector<String> ndgList) {
		this.outputResultMap = outputRes;
		this.ndgList = ndgList;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method which retrieves the success flag of prover execution.
	 * 
	 * @return	Value of success flag.
	 */
	public boolean getSuccess() {
		String successStr = this.getOutputResult(GeoGebraOGPOutputProverProtocol.OGP_OUTPUT_RES_SUCCESS);
		if (successStr != null)
			return successStr.equals("true");
		return false;
	}
	
	/**
	 * Method which retrieves the failure message of prover execution.
	 * 
	 * @return	Failure message.
	 */
	public String getFailureMsg() {
		return this.getOutputResult(GeoGebraOGPOutputProverProtocol.OGP_OUTPUT_RES_FAILURE_MSG);
	}
	
	/**
	 * Method which retrieves the prover result.
	 * 
	 * @return	Prover result.
	 */
	public String getProverResult() {
		return this.getOutputResult(GeoGebraOGPOutputProverProtocol.OGP_OUTPUT_RES_PROVER);
	}
	
	/**
	 * Method which retrieves the prover message.
	 * 
	 * @return	Prover message.
	 */
	public String getProverMessage() {
		return this.getOutputResult(GeoGebraOGPOutputProverProtocol.OGP_OUTPUT_RES_PROVER_MSG);
	}
	
	/**
	 * Method which retrieves the spent time of prover execution.
	 * 
	 * @return	Time spent in proving.
	 */
	public double getExecutionTime() {
		String exTimeStr = this.getOutputResult(GeoGebraOGPOutputProverProtocol.OGP_OUTPUT_RES_TIME);
		double exTime = 0.0;
		
		if (exTimeStr != null) {
			try {
				exTime = Double.parseDouble(exTimeStr);
			} catch (NumberFormatException ex) {
				ILogger logger = OpenGeoProver.settings.getLogger();
				logger.warn("Failed to read execution time from string - exception caught: " + ex.toString());
				exTime = 0.0;
			}
		}
		return exTime;
	}
	
	/**
	 * Method which retrieves the maximal number of 
	 * polynomial terms obtained in prover execution.
	 * 
	 * @return	Number of polynomial terms.
	 */
	public int getNumberOfTerms() {
		String numTermsStr = this.getOutputResult(GeoGebraOGPOutputProverProtocol.OGP_OUTPUT_RES_NUMTERMS);
		int numTerms = 0;
		
		if (numTermsStr != null) {
			try {
				numTerms = Integer.parseInt(numTermsStr);
			} catch (NumberFormatException ex) {
				ILogger logger = OpenGeoProver.settings.getLogger();
				logger.warn("Failed to read number of terms from string - exception caught: " + ex.toString());
				numTerms = 0;
			}
		}
		return numTerms;
	}
}
