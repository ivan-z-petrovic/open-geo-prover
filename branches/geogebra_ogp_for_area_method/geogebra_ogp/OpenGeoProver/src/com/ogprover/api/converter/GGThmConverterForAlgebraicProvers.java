/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.api.converter;

import com.ogprover.geogebra.GeoGebraTheorem;
import com.ogprover.pp.tp.OGPTP;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for converter of geometry theorems in GeoGebra format, to be used 
*     by algebraic provers</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class GGThmConverterForAlgebraicProvers extends GeoGebraTheoremConverter {
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
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method.
	 * 
	 * @param theorem	Theorem to be proved
	 * @param ogpCP		OGP theorem protocol object for storage of converted theorem
	 */
	public GGThmConverterForAlgebraicProvers(GeoGebraTheorem theorem, OGPTP ogpCP) {
		super(theorem, ogpCP);
		this.consConverter = new GGConsConverterForAlgebraicProvers(this); // note: must be called after base class constructor has been called!
		this.statConverter = new GGStatConverterForAlgebraicProvers(this); // note: must be called after consConverter has been instantiated!
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
}
