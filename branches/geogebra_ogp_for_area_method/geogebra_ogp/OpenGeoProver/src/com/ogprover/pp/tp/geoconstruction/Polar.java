/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.geoconstruction;


import java.util.Vector;

import com.ogprover.main.OpenGeoProver;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for construction of polar (line) for
*     given pole with respect to given circle or conic</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class Polar extends ShortcutConstruction {
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
	 * Pole of this polar.
	 */
	private Point pole;
	/**
	 * Basic set of points (circle or conic section) of this polar.
	 */
	private SetOfPoints baseSet;
	
	
	
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
		return (Line) this.shortcutListOfConstructions.get(this.shortcutListOfConstructions.size() - 1);
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
	
	/**
	 * @param pole the pole to set
	 */
	public void setPole(Point pole) {
		this.pole = pole;
	}

	/**
	 * @return the pole
	 */
	public Point getPole() {
		return pole;
	}

	/**
	 * @param baseSet the baseSet to set
	 */
	public void setBaseSet(SetOfPoints baseSet) {
		this.baseSet = baseSet;
	}

	/**
	 * @return the baseSet
	 */
	public SetOfPoints getBaseSet() {
		return baseSet;
	}
	
	
	

	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method.
	 * 
	 * @param lineLabel	Label of this line
	 * @param pole		Pole whose polar is this line
	 * @param baseSet	Conic or circle that corresponds to this polar line for given pole
	 */
	public Polar(String lineLabel, Point pole, SetOfPoints baseSet) {
		this.shortcutListOfConstructions = new Vector<GeoConstruction>();
		
		// If base set is line, exit this method
		if (baseSet instanceof Line) {
			OpenGeoProver.settings.getLogger().error("Attempt to create polar with respect to line but conic or circle are expected");
			return;
		}
		
		/*
		 * For pole outside circle/conic, polar line is constructed as line that connects
		 * two touch points of circle/conic and tangent lines from pole to that circle/conic.
		 * If pole is on circle/conic, polar is tangent line to that circle/conic in pole.
		 * 
		 * But we will apply general approach for construction of polar that works with
		 * pole in any position regarding circle/conic:
		 * 
		 * Let a and b are two secants of circle/conic through pole and let they have
		 * following intersection points with that circle/conic (respectively): 
		 * A1, A2 and B1, B2. Then let a1, a2, b1 and b2 are tangent lines to circle/conic
		 * in these points. Let a1 and a2 intersect each other at point A and b1 and b2 
		 * intersect each other at point B. Then polar is line AB.
		 */
		Point A1, A2, B1, B2;
		
		if (baseSet instanceof Circle) {
			A1 = new RandomPointFromCircle("polarA1" + Math.round(Math.random()*1000), (Circle)baseSet);
			B1 = new RandomPointFromCircle("polarB1" + Math.round(Math.random()*1000), (Circle)baseSet);
		}
		else if (baseSet instanceof GeneralConicSection) {
			A1 = new RandomPointFromGeneralConic("polarA1" + Math.round(Math.random()*1000), (GeneralConicSection)baseSet);
			B1 = new RandomPointFromGeneralConic("polarB1" + Math.round(Math.random()*1000), (GeneralConicSection)baseSet);
		}
		else {
			OpenGeoProver.settings.getLogger().error("Unknown set of points passed in for construction of polar");
			return;
		}	
		this.shortcutListOfConstructions.add(A1);
		this.shortcutListOfConstructions.add(B1);
		
		Line a = new LineThroughTwoPoints("polara" + Math.round(Math.random()*1000), pole, A1);
		Line b = new LineThroughTwoPoints("polarb" + Math.round(Math.random()*1000), pole, B1);
		this.shortcutListOfConstructions.add(a);
		this.shortcutListOfConstructions.add(b);
		
		A2 = new IntersectionPoint("polarA2" + Math.round(Math.random()*1000), baseSet, a);
		B2 = new IntersectionPoint("polarB2" + Math.round(Math.random()*1000), baseSet, b);
		this.shortcutListOfConstructions.add(A2);
		this.shortcutListOfConstructions.add(B2);
		
		Line a1 = new TangentLine("polara1" + Math.round(Math.random()*1000), A1, baseSet);
		Line a2 = new TangentLine("polara2" + Math.round(Math.random()*1000), A2, baseSet);
		Line b1 = new TangentLine("polarb1" + Math.round(Math.random()*1000), B1, baseSet);
		Line b2 = new TangentLine("polarb2" + Math.round(Math.random()*1000), B2, baseSet);
		this.shortcutListOfConstructions.add(a1);
		this.shortcutListOfConstructions.add(a2);
		this.shortcutListOfConstructions.add(b1);
		this.shortcutListOfConstructions.add(b2);
		
		Point A = new IntersectionPoint("polarA" + Math.round(Math.random()*1000), a1, a2);
		Point B = new IntersectionPoint("polarB" + Math.round(Math.random()*1000), b1, b2);
		this.shortcutListOfConstructions.add(A);
		this.shortcutListOfConstructions.add(B);
		
		this.shortcutListOfConstructions.add(new LineThroughTwoPoints(lineLabel, A, B));
		this.pole = pole;
		this.baseSet = baseSet;
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
		return "Polar";
	}
}
