/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.geoconstruction;


import java.util.Vector;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for list of other simple constructions</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class ListOfConstructions extends ShortcutConstruction {
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
	/**
	 * @see com.ogprover.pp.tp.geoconstruction.ShortcutConstruction#getPoint()
	 */
	@Override
	public Point getPoint() {
		// This construction is not for point
		return null;
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.ShortcutConstruction#getLine()
	 */
	@Override
	public Line getLine() {
		// This construction is not for line
		return null;
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.ShortcutConstruction#getCircle()
	 */
	@Override
	public Circle getCircle() {
		// This construction is not for circle
		return null;
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.ShortcutConstruction#getConic()
	 */
	@Override
	public ConicSection getConic() {
		// This construction is not for conic
		return null;
	}
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * 
	 * @param consList	List of constructions
	 */
	public ListOfConstructions(Vector<GeoConstruction> consList) {
		this.shortcutListOfConstructions = consList;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getConstructionDesc()
	 */
	@Override
	public String getConstructionDesc() {
		// Shortcut construction is expanded in CP - therefore no detailed description is required.
		return "List of simple constructions";
	}
}
