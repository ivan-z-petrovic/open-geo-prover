/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.api.converter;

import java.util.Map;

import com.ogprover.geogebra.command.statement.GeoGebraStatementCommand;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.geoobject.GeoObject;
import com.ogprover.pp.tp.thmstatement.ThmStatement;
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
	 * Statement command in GeoGebra format to be converted
	 */
	protected GeoGebraStatementCommand ggStatCmd;
	/**
	 * OGP's Theorem Protocol for storage of converted statement.
	 * It also contains converted geometry constructions except auxiliary objects.
	 */
	protected OGPTP thmProtocol;
	/**
	 * Map with auxiliary geometry objects (angles, segments, vectors, polygons etc.), used in constructions 
	 * of other geometry objects.
	 */
	protected Map<String, GeoObject> auxiliaryObjectsMap;
	/**
	 * Flag which is used for success or failure of conversion operation.
	 */
	protected boolean bSuccess;
	
	
	
	/*
	 * ======================================================================
	 * ========================= ABSTRACT METHODS ===========================
	 * ======================================================================
	 */
	/**
	 * Method which performs the conversion of boolean (true-false) geometry theorem statement.
	 * 
	 * @return	Theorem statement object if operation was successful, null otherwise.
	 */
	public abstract ThmStatement convertBoolean();
	/**
	 * Method which performs the conversion of geometry theorem statement
	 * about three collinear points from GeoGebra to OGP format.
	 * 
	 * @return	Theorem statement object if operation was successful, null otherwise.
	 */
	public abstract ThmStatement convertCollinear();
	/**
	 * Method which performs the conversion of geometry theorem statement
	 * about four concyclic points from GeoGebra to OGP format.
	 * 
	 * @return	Theorem statement object if operation was successful, null otherwise.
	 */
	public abstract ThmStatement convertConcyclic();
	/**
	 * Method which performs the conversion of geometry theorem statement
	 * about three concurrent lines or circles from GeoGebra to OGP format.
	 * 
	 * @return	Theorem statement object if operation was successful, null otherwise.
	 */
	public abstract ThmStatement convertConcurrent();
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
	/**
	 * Method which performs the conversion of geometry theorem statement
	 * about two equal objects from GeoGebra to OGP format.
	 * 
	 * @return	Theorem statement object if operation was successful, null otherwise.
	 */
	public abstract ThmStatement convertEqual();
	// TODO - add new methods here for new statements
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @return the ggStatCmd
	 */
	public GeoGebraStatementCommand getStatementCmd() {
		return this.ggStatCmd;
	}
	/**
	 * @return the thmProtocol
	 */
	public OGPTP getThmProtocol() {
		return this.thmProtocol;
	}
	/**
	 * @return the auxiliaryObjectsMap
	 */
	public Map<String, GeoObject> getAuxObjMap() {
		return this.auxiliaryObjectsMap;
	}
	/**
	 * @return the bSuccess
	 */
	public boolean isbSuccess() {
		return this.bSuccess;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * 
	 * @param ggThmCnv		GeoGebra theorem converter
	 */
	public GeoGebraStatementConverter(GeoGebraTheoremConverter ggThmCnv) {
		this.ggStatCmd = ggThmCnv.getTheorem().getStatement();
		this.thmProtocol = ggThmCnv.getThmProtocol();
		this.auxiliaryObjectsMap = ggThmCnv.getConsConverter().getAuxiliaryObjectsMap(); // note: consCnv must be instantiated before this constructor!
		this.bSuccess = true;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method for conversion of theorem statement from GeoGebra to OGP format.
	 * 
	 * @return	TRUE if operation was successful, FALSE otherwise.
	 */
	public boolean convert() {
		ILogger logger = OpenGeoProver.settings.getLogger();
		String statementName = this.ggStatCmd.getCommandName();
		this.bSuccess = true;
		
		ThmStatement thmStat = null;
		if (statementName.equalsIgnoreCase(GeoGebraStatementCommand.COMMAND_BOOLEAN))
			thmStat = convertBoolean();
		else if (statementName.equalsIgnoreCase(GeoGebraStatementCommand.COMMAND_COLLINEAR))
			thmStat = convertCollinear();
		else if (statementName.equalsIgnoreCase(GeoGebraStatementCommand.COMMAND_CONCYCLIC))
			thmStat = convertConcyclic();
		else if (statementName.equalsIgnoreCase(GeoGebraStatementCommand.COMMAND_CONCURRENT))
			thmStat = convertConcurrent();
		else if (statementName.equalsIgnoreCase(GeoGebraStatementCommand.COMMAND_EQUAL))
			thmStat = convertEqual();
		else if (statementName.equalsIgnoreCase(GeoGebraStatementCommand.COMMAND_PARALLEL))
			thmStat = convertParallel();
		else if (statementName.equalsIgnoreCase(GeoGebraStatementCommand.COMMAND_PERPENDICULAR))
			thmStat = convertPerpendicular();
		// TODO - add new statements here
		
		if (thmStat == null) {
			logger.error("Failed to convert statement - unknown statement name");
			this.bSuccess = false;
			return this.bSuccess;
		}
		
		this.thmProtocol.addThmStatement(thmStat);
		return true;
	}
}