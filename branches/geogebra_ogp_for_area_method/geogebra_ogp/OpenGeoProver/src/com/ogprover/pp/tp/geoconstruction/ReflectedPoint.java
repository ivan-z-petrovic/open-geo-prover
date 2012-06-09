/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.geoconstruction;


import java.util.Vector;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for construction of reflexive point 
*     of given point with respect to given line</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class ReflectedPoint extends ShortcutConstruction {
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
		return (Point) this.shortcutListOfConstructions.get(this.shortcutListOfConstructions.size() - 1);
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
	 * @param pointLabel	Label of this point
	 * @param originalPoint	Original point
	 * @param baseLine		Line of reflexivity 
	 */
	public ReflectedPoint(String pointLabel, Point originalPoint, Line baseLine) {
		this.shortcutListOfConstructions = new Vector<GeoConstruction>();
		
		Line perpLine = new PerpendicularLine("reflexivePointPerpLine" + Math.round(Math.random()*1000), baseLine, originalPoint);
		this.shortcutListOfConstructions.add(perpLine);
		Point footPoint = new IntersectionPoint("reflexivePointFootPoint" + Math.round(Math.random()*1000), perpLine, baseLine);
		this.shortcutListOfConstructions.add(footPoint);
		this.shortcutListOfConstructions.add(new CentralSymmetricPoint(pointLabel, originalPoint, footPoint));
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
		return "Reflexive point";
	}
}
