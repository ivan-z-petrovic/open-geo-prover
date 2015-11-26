/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.geoconstruction;


import java.util.Vector;

import com.ogprover.main.OpenGeoProver;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for construction of center of given circle</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class CenterOfCircle extends ShortcutConstruction {
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
	 * Circle whose center is constructed.
	 */
	private Circle circle;
	
	
	
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
		Point circleCenter = this.circle.getCenter();
		
		if (circleCenter != null)
			return circleCenter;
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
	 * @param pointLabel	Label of center
	 * @param circle		Circle whose center is this point
	 */
	public CenterOfCircle(String pointLabel, Circle circle) {
		this.circle = circle;
		this.shortcutListOfConstructions = new Vector<GeoConstruction>();
		
		// If circle already has constructed center, nothing should be done
		if (circle.getCenter() != null)
			return;
		
		// If there are no three points on given circle, center could not be constructed
		Vector<Point> circlePoints = circle.getPoints();
		if (circlePoints.size() < 3) {
			OpenGeoProver.settings.getLogger().error("Unable to construct center of circle " + circle.getGeoObjectLabel() + " because it doesn't have three points.");
			return;
		}
		
		// we use first three points of circle - if different behavior is required
		// user should provide construction instead of using this shortcut
		Point A = circlePoints.get(0);
		Point B = circlePoints.get(1);
		Point C = circlePoints.get(2);
		Line mAB = new PerpendicularBisector("centerOfCirclePerpBisectorAB" + Math.round(Math.random()*1000), A, B);
		this.shortcutListOfConstructions.add(mAB);
		Line mBC = new PerpendicularBisector("centerOfCirclePerpBisectorBC" + Math.round(Math.random()*1000), B, C);
		this.shortcutListOfConstructions.add(mBC);
		Point centerP = new IntersectionPoint(pointLabel, mAB, mBC);
		this.shortcutListOfConstructions.add(centerP);
		circle.setCenter(centerP); // set the center of circle
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
		return "Center of circle";
	}
}
