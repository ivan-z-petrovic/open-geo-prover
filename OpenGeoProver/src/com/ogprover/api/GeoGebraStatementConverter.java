/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.api;

import java.util.Vector;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.prover_protocol.cp.thmstatement.ThmStatement;
import com.ogprover.utilities.logger.ILogger;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Abstract class for converter of GeoGebra's geometry theorem statements</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public abstract class GeoGebraStatementConverter {
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
	 * The GeoGebra's string representation of theorem statement.
	 */
	protected String statementText;
	/**
	 * Name of theorem statement extracted from text of statement.
	 */
	protected String statementName;
	/**
	 * Statement's arguments  extracted from text of statement,
	 * which are stored in list of labels of geometry objects or 
	 * some of them can be a number.
	 */
	protected Vector<String> statementArgs;
	/**
	 * The OGP format of theorem statement
	 */
	protected ThmStatement ogpStatement;
	/**
	 * The construction converter with converted GeoGebra objects
	 * to be used for object references when converting statement.
	 */
	protected GeoGebraConstructionConverter consConverter;
	/**
	 * Flag which is used for success or failure of conversion operation.
	 */
	protected boolean bSuccess;
	
	/*
	 * GeoGebra statements
	 */
	public static final String GG_STAT_COLLINEAR = "AreCollinear";
	public static final String GG_STAT_CONCURRENT = "AreConcurrent";
	public static final String GG_STAT_EQUAL = "AreEqual";
	public static final String GG_STAT_PARALLEL = "AreParallel";
	public static final String GG_STAT_PERPENDICULAR = "ArePerpendicular";
	// TODO - add new statements here
	
	
	
	
	
	
	/*
	 * ======================================================================
	 * ========================= ABSTRACT METHODS ===========================
	 * ======================================================================
	 */
	/**
	 * Method which performs the conversion of geometry theorem statement
	 * about three collinear points from GeoGebra to OGP format.
	 * 
	 * @return	Theorem statement object if operation was successful, null otherwise.
	 */
	public abstract ThmStatement convertCollinear();
	/**
	 * Method which performs the conversion of geometry theorem statement
	 * about three concurrent lines or circles from GeoGebra to OGP format.
	 * 
	 * @return	Theorem statement object if operation was successful, null otherwise.
	 */
	public abstract ThmStatement convertConcurrent();
	/**
	 * Method which performs the conversion of geometry theorem statement
	 * about two equal objects from GeoGebra to OGP format.
	 * 
	 * @return	Theorem statement object if operation was successful, null otherwise.
	 */
	public abstract ThmStatement convertEqual();
	/**
	 * Method which performs the conversion of geometry theorem statement
	 * about two parallel lines from GeoGebra to OGP format.
	 * 
	 * @return	Theorem statement object if operation was successful, null otherwise.
	 */
	public abstract ThmStatement convertParallel();
	/**
	 * Method which performs the conversion of geometry theorem statement
	 * about two perpendicular lines from GeoGebra to OGP format.
	 * 
	 * @return	Theorem statement object if operation was successful, null otherwise.
	 */
	public abstract ThmStatement convertPerpendicular();
	// TODO - add new methods here for new statements
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @return the statementText
	 */
	public String getStatementText() {
		return statementText;
	}
	/**
	 * @return the statementName
	 */
	public String getStatementName() {
		return statementName;
	}
	/**
	 * @return the ogpStatement
	 */
	public ThmStatement getOgpStatement() {
		return ogpStatement;
	}
	/**
	 * @return the consConverter
	 */
	public GeoGebraConstructionConverter getConsConverter() {
		return consConverter;
	}
	/**
	 * @param consConverter the consConverter to set
	 */
	public void setConsConverter(GeoGebraConstructionConverter consConverter) {
		this.consConverter = consConverter;
	}
	/**
	 * @return the bSuccess
	 */
	public boolean isbSuccess() {
		return bSuccess;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * 
	 * @param statText		The text of statement from GeoGebra
	 * @param ggConsConv	Construction converter with geometry objects
	 */
	public GeoGebraStatementConverter(String statText, GeoGebraConstructionConverter ggConsConv) {
		this.statementText = statText;
		this.statementName = null; // will be set on parsing
		this.statementArgs = new Vector<String>(); // will be populated on parsing
		this.ogpStatement = null;
		this.consConverter = ggConsConv;
		this.bSuccess = true;
	}
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method which parses statement text and extracts the name of statement
	 * and the list of arguments.
	 * 
	 * @return	TRUE if operation was successful, FALSE otherwise
	 */
	private boolean parseStatementText() {
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		// === Brackets ===
		int lbracIdx = this.statementText.indexOf('[');
		int rbracIdx = this.statementText.lastIndexOf(']');
		if (lbracIdx == -1 || rbracIdx == -1) {
			logger.error("Statement in bad format - missing bracket");
			return false;
		}
		
		// === Statement name ===
		this.statementName = this.statementText.substring(0, lbracIdx);
		if (this.statementName.length() == 0) {
			logger.error("Statement in bad format - missing statement name");
			return false;
		}
		
		// === Argument list ===
		String args = this.statementText.substring(lbracIdx + 1, rbracIdx);
		int length = this.statementText.length();
		
		if (length != rbracIdx + 1) {
			logger.error("Statement in bad format - statement text doesn't end with right bracket");
			return false;
		}
		
		String[] argsArray = args.split(",");
		for (String arg : argsArray)
			this.statementArgs.add(arg.trim());
		
		return true;
	}
	
	/**
	 * Method for conversion of theorem statement from GeoGebra to OGP format.
	 * 
	 * @return	TRUE if operation was successful, FALSE otherwise.
	 */
	public boolean convert() {
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if ((this.bSuccess = this.parseStatementText()) == false) {
			logger.error("Failed to parse statement text");
			return this.bSuccess;
		}
		
		ThmStatement thmStat = null;
		if (this.statementName.equalsIgnoreCase(GeoGebraStatementConverter.GG_STAT_COLLINEAR))
			thmStat = convertCollinear();
		else if (this.statementName.equalsIgnoreCase(GeoGebraStatementConverter.GG_STAT_CONCURRENT))
			thmStat = convertCollinear();
		else if (this.statementName.equalsIgnoreCase(GeoGebraStatementConverter.GG_STAT_EQUAL))
			thmStat = convertCollinear();
		else if (this.statementName.equalsIgnoreCase(GeoGebraStatementConverter.GG_STAT_PARALLEL))
			thmStat = convertCollinear();
		else if (this.statementName.equalsIgnoreCase(GeoGebraStatementConverter.GG_STAT_PERPENDICULAR))
			thmStat = convertCollinear();
		// TODO - add new statements here
		
		if (thmStat == null) {
			logger.error("Failed to convert statement - unknown statement name");
			this.bSuccess = false;
			return this.bSuccess;
		}
		
		this.consConverter.getConsProtocol().setTheoremStatement(thmStat);
		return true;
	}
}