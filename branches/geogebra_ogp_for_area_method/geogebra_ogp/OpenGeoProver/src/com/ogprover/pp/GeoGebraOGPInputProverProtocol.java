/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp;

import java.util.HashMap;
import java.util.Map;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for input prover protocol between GeoGebra and OGP</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */
public class GeoGebraOGPInputProverProtocol extends OGPInputProverProtocol {
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
	 * Text representation of geometry theorem with constructions and theorem
	 * statement. For GeoGebra system, this representation is in XML format.
	 */
	private String geometryTheoremText;
	/**
	 * Map with input prover protocol - key is parameter name and value is 
	 * string representation of parameter value.
	 */
	private Map<String, String> inputParameterMap;
	
	/**
	 * Names of GeoGebra input parameters for OGP.
	 */
	public static final String GG_INPUT_PARAM_METHOD = "method";				// Name of prover method: "WU", "GROEBNER", "AREA"
	public static final String GG_INPUT_PARAM_TIMEOUT = "timeout";				// Time limit for prover execution in seconds
	public static final String GG_INPUT_PARAM_MAXTERMS = "maxterms";			// Maximal number of polynomial terms for prover execution (space limit)
	public static final String GG_INPUT_PARAM_REPORT_FORMAT = "reportFormat";	// Format of output report: "TEX", "XML", "ALL", "NONE"
	// TODO - add here other parameters
	
	// OGP prover methods set from GeoGebra
	public static final String OGP_METHOD_WU = "WU";
	public static final String OGP_METHOD_GROEBNER = "GROEBNER";
	public static final String OGP_METHOD_AREA = "AREA";
	// TODO - add here other prover methods
	
	// Format of output report
	public static final String OGP_REPORT_FORMAT_TEX = "TEX";
	public static final String OGP_REPORT_FORMAT_XML = "XML";
	public static final String OGP_REPORT_FORMAT_ALL = "ALL";
	public static final String OGP_REPORT_FORMAT_NONE = "NONE";
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @param geometryTheoremText the geometryTheoremText to set
	 */
	public void setGeometryTheoremText(String geometryTheoremText) {
		this.geometryTheoremText = geometryTheoremText;
	}

	/**
	 * @return the geometryTheoremText
	 */
	public String getGeometryTheoremText() {
		return geometryTheoremText;
	}

	/**
	 * @param inputParameterMap the inputParameterMap to set
	 */
	public void setInputParameterMap(Map<String, String> inputParameterMap) {
		this.inputParameterMap = inputParameterMap;
	}

	/**
	 * @return the inputParameterMap
	 */
	public Map<String, String> getInputParameterMap() {
		return inputParameterMap;
	}
	
	/**
	 * Method for setting the value of input parameter.
	 * 
	 * @param paramName		Parameter name
	 * @param paramValue	Parameter value in string format
	 */
	public void setInputParmeter(String paramName, String paramValue) {
		if (this.inputParameterMap != null)
			this.inputParameterMap.put(paramName, paramValue);
	}
	
	/**
	 * Method for getting the value of input parameter.
	 * 
	 * @param paramName	Parameter name
	 * @return			Parameter value in string format
	 */
	public String getIntputParameter(String paramName) {
		if (this.inputParameterMap != null)
			return this.inputParameterMap.get(paramName);
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
	public GeoGebraOGPInputProverProtocol() {
		this.geometryTheoremText = null;
		this.inputParameterMap = new HashMap<String, String>();
	}
	
	/**
	 * Constructor method.
	 * 
	 * @param thmText			Text of geometry theorem.
	 * @param inputParameters	Input prover parameters.
	 */
	public GeoGebraOGPInputProverProtocol(String thmText, Map<String, String> inputParameters) {
		this.geometryTheoremText = thmText;
		this.inputParameterMap = inputParameters;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method for setting value of method parameter.
	 * 
	 * @param method	Value of parameter.
	 */
	public void setMethod(String method) {
		this.setInputParmeter(GeoGebraOGPInputProverProtocol.GG_INPUT_PARAM_METHOD, method);
	}
	
	/**
	 * Method for setting value of time out parameter.
	 * 
	 * @param timeout	Value of parameter.
	 */
	public void setTimeOut(double timeout) {
		Double timeOut = timeout;
		this.setInputParmeter(GeoGebraOGPInputProverProtocol.GG_INPUT_PARAM_TIMEOUT, timeOut.toString());
	}
	
	/**
	 * Method for setting value of maxterms parameter.
	 * 
	 * @param maxterms	Value of parameter.
	 */
	public void setMaxTerms(int maxterms) {
		Integer maxTerms = maxterms;
		this.setInputParmeter(GeoGebraOGPInputProverProtocol.GG_INPUT_PARAM_MAXTERMS, maxTerms.toString());
	}
	
	/**
	 * Method for setting value of report format parameter.
	 * 
	 * @param reportFormat	Value of parameter.
	 */
	public void setReportFormat(String reportFormat) {
		this.setInputParmeter(GeoGebraOGPInputProverProtocol.GG_INPUT_PARAM_REPORT_FORMAT, reportFormat);
	}
}
