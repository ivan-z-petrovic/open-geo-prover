/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.geoconstruction;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Abstract for free parametric set of points</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public abstract class FreeParametricSet extends ParametricSet {
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
	 * Flag that determines whether origin of Cartesian coordinate system
	 * belongs to this parametric set of points.
	 */
	protected boolean containsOrigin;
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @param containsOrigin the containsOrigin to set
	 */
	public void setContainsOrigin(boolean containsOrigin) {
		this.containsOrigin = containsOrigin;
	}

	/**
	 * @return the containsOrigin
	 */
	public boolean isContainsOrigin() {
		return containsOrigin;
	}
	
	

	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
     * Method that retrieves number of zero coordinates used when instantiating
     * points from this parametric set of points.
     * 
     * @return	Number of zero coordinates
     */
    public abstract int getNumberOfZeroCoordinates();
	
}

