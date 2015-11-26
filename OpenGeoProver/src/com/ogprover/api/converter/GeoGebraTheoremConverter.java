/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.api.converter;

import com.ogprover.geogebra.GeoGebraTheorem;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.utilities.logger.ILogger;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Abstract class for converter of geometry theorems in GeoGebra format</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public abstract class GeoGebraTheoremConverter {
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
	 * Theorem which is converted
	 */
	protected GeoGebraTheorem theorem;
	/**
	 * Converter of geometry construction
	 */
	protected GeoGebraConstructionConverter consConverter;
	/**
	 * Converter of theorem statement
	 */
	protected GeoGebraStatementConverter statConverter;
	/**
	 * OGP's Theorem Protocol for storage of converted geometry theorem.
	 */
	protected OGPTP thmProtocol;
	/**
	 * Flag which is used for success or failure of conversion operation.
	 */
	protected boolean bSuccess;
	
	
	
	/*
	 * ======================================================================
	 * ========================= ABSTRACT METHODS ===========================
	 * ======================================================================
	 */
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @return the theorem
	 */
	public GeoGebraTheorem getTheorem() {
		return this.theorem;
	}
	/**
	 * @return the consConverter
	 */
	public GeoGebraConstructionConverter getConsConverter() {
		return this.consConverter;
	}
	/**
	 * @return the statConverter
	 */
	public GeoGebraStatementConverter getStatConverter() {
		return this.statConverter;
	}
	/**
	 * @return the thmProtocol
	 */
	public OGPTP getThmProtocol() {
		return this.thmProtocol;
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
	 * Constructor method.
	 * 
	 * @param theorem	Theorem to be proved
	 * @param ogpTP		OGP theorem protocol object for storage of converted theorem
	 */
	public GeoGebraTheoremConverter(GeoGebraTheorem theorem, OGPTP ogpTP) {
		this.theorem = theorem;
		this.thmProtocol = ogpTP;
		this.bSuccess = true;
		this.consConverter = null; // instantiated in sub-class
		this.statConverter = null; // instantiated in sub-class
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method which converts geometry theorem from GeoGebra to OGP format.
	 *  
	 * @return	TRUE if conversion was successful, FALSE otherwise.
	 */
	public boolean convert() {
		ILogger logger = OpenGeoProver.settings.getLogger();
		this.bSuccess = true;
		
		// Note: since this class can't be instantiated, this method won't be called 
		// on objects that don't have instantiated converters for constructions and statement.
		
		// Theorem name
		this.thmProtocol.setTheoremName(this.getTheorem().getTheoremName());
		
		// Convert constructions
		if (this.consConverter.convert() == false) {
			logger.error("Failed to convert constructions necessary for theorem");
			this.bSuccess = false;
			return false;
		}
		
		// Convert theorem statement
		if (this.statConverter.convert() == false) {
			logger.error("Failed to convert theorem statement");
			this.bSuccess = false;
			return false;
		}
		
		return true;
	}
}
