/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.geoconstruction;


import java.util.Vector;

import com.ogprover.main.OpenGeoProver;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for construction of pole 
*     for given polar with respect to given conic or circle</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class Pole extends ShortcutConstruction {
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
	 * Polar of this pole.
	 */
	private Line polar;
	/**
	 * Basic set of points (circle or conic section) of this pole.
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
	
	/**
	 * @param polar the polar to set
	 */
	public void setPolar(Line polar) {
		this.polar = polar;
	}

	/**
	 * @return the polar
	 */
	public Line getPolar() {
		return polar;
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
	 * @param pointLabel	Label of this point
	 * @param polar			Polar line whose pole is this point
	 * @param baseSet		Conic/circle whose pole is this point for given polar line
	 */
	public Pole(String pointLabel, Line polar, SetOfPoints baseSet) {
		this.shortcutListOfConstructions = new Vector<GeoConstruction>();
		
		// If base set is line, exit this method
		if (baseSet instanceof Line) {
			OpenGeoProver.settings.getLogger().error("Attempt to create pole with respect to line but conic or circle are expected");
			return;
		}
		
		/*
		 * For polar with no common points with circle/conic, pole is constructed
		 * in following way: A and B are some two points from polar and T1, T2 are touch
		 * points of tangents from A and conic/circle, while U1 and U2 are touch points
		 * of tangents from B and conic/circle. Then pole is intersection point of
		 * lines T1T2 and U1U2. If polar line is tangent to given conic/circle, then pole
		 * is touch point.
		 * 
		 *  But we will apply general approach for construction of pole that works with 
		 *  polar in any position regarding conic/circle:
		 *  
		 *  Let A and B are two arbitrary points of polar. Let a1 and a2 are some secants
		 *  of conic/circle from A and b1, b2 are two secants of conic/circle from B.
		 *  Let C11, C12 are common points of secant a1 and conic/circle, and similarly
		 *  C21, C22 (a2), D11, D12 (b1) and D21, D22 (b2). Let c11, c12, c21, c22, d11, 
		 *  d12, d21, d22 are tangent lines in this points respectively, to conic/circle.
		 *  Let T1 is intersection of lines c11 and c12 (T1 = c11 x c12) and similarly:
		 *  T2 = c21 x c22, U1 = d11 x d12, U2 = d21 x d22. Then pole is intersection point
		 *  of lines T1T2 and U1U2.
		 *   
		 */
		Point A = new RandomPointFromLine("poleA" + Math.round(Math.random()*1000), polar);
		this.shortcutListOfConstructions.add(A);
		Point B = new RandomPointFromLine("poleB" + Math.round(Math.random()*1000), polar);
		this.shortcutListOfConstructions.add(B);
		
		Point C11, C12, C21, C22, D11, D12, D21, D22;
		
		if (baseSet instanceof Circle) {
			C11 = new RandomPointFromCircle("poleC11" + Math.round(Math.random()*1000), (Circle)baseSet);
			C21 = new RandomPointFromCircle("poleC21" + Math.round(Math.random()*1000), (Circle)baseSet);
			D11 = new RandomPointFromCircle("poleD11" + Math.round(Math.random()*1000), (Circle)baseSet);
			D21 = new RandomPointFromCircle("poleD21" + Math.round(Math.random()*1000), (Circle)baseSet);
		}
		else if (baseSet instanceof GeneralConicSection) {
			C11 = new RandomPointFromGeneralConic("poleC11" + Math.round(Math.random()*1000), (GeneralConicSection)baseSet);
			C21 = new RandomPointFromGeneralConic("poleC21" + Math.round(Math.random()*1000), (GeneralConicSection)baseSet);
			D11 = new RandomPointFromGeneralConic("poleD11" + Math.round(Math.random()*1000), (GeneralConicSection)baseSet);
			D21 = new RandomPointFromGeneralConic("poleD21" + Math.round(Math.random()*1000), (GeneralConicSection)baseSet);
		}
		else {
			OpenGeoProver.settings.getLogger().error("Unknown set of points passed in for construction of pole");
			return;
		}	
		this.shortcutListOfConstructions.add(C11);
		this.shortcutListOfConstructions.add(C21);
		this.shortcutListOfConstructions.add(D11);
		this.shortcutListOfConstructions.add(D21);
		
		Line a1 = new LineThroughTwoPoints("polea1" + Math.round(Math.random()*1000), A, C11);
		Line a2 = new LineThroughTwoPoints("polea2" + Math.round(Math.random()*1000), A, C21);
		Line b1 = new LineThroughTwoPoints("poleb1" + Math.round(Math.random()*1000), B, D11);
		Line b2 = new LineThroughTwoPoints("poleb2" + Math.round(Math.random()*1000), B, D21);
		this.shortcutListOfConstructions.add(a1);
		this.shortcutListOfConstructions.add(a2);
		this.shortcutListOfConstructions.add(b1);
		this.shortcutListOfConstructions.add(b2);
		
		C12 = new IntersectionPoint("poleC12" + Math.round(Math.random()*1000), baseSet, a1);
		C22 = new IntersectionPoint("poleC22" + Math.round(Math.random()*1000), baseSet, a2);
		D12 = new IntersectionPoint("poleD12" + Math.round(Math.random()*1000), baseSet, b1);
		D22 = new IntersectionPoint("poleD22" + Math.round(Math.random()*1000), baseSet, b2);
		this.shortcutListOfConstructions.add(C12);
		this.shortcutListOfConstructions.add(C22);
		this.shortcutListOfConstructions.add(D12);
		this.shortcutListOfConstructions.add(D22);
		
		Line c11 = new TangentLine("polec11" + Math.round(Math.random()*1000), C11, baseSet);
		Line c12 = new TangentLine("polec12" + Math.round(Math.random()*1000), C12, baseSet);
		Line c21 = new TangentLine("polec21" + Math.round(Math.random()*1000), C21, baseSet);
		Line c22 = new TangentLine("polec22" + Math.round(Math.random()*1000), C22, baseSet);
		Line d11 = new TangentLine("poled11" + Math.round(Math.random()*1000), D11, baseSet);
		Line d12 = new TangentLine("poled12" + Math.round(Math.random()*1000), D12, baseSet);
		Line d21 = new TangentLine("poled21" + Math.round(Math.random()*1000), D21, baseSet);
		Line d22 = new TangentLine("poled22" + Math.round(Math.random()*1000), D22, baseSet);
		this.shortcutListOfConstructions.add(c11);
		this.shortcutListOfConstructions.add(c12);
		this.shortcutListOfConstructions.add(c21);
		this.shortcutListOfConstructions.add(c22);
		this.shortcutListOfConstructions.add(d11);
		this.shortcutListOfConstructions.add(d12);
		this.shortcutListOfConstructions.add(d21);
		this.shortcutListOfConstructions.add(d22);
		
		Point T1 = new IntersectionPoint("poleT1" + Math.round(Math.random()*1000), c11, c12);
		Point T2 = new IntersectionPoint("poleT2" + Math.round(Math.random()*1000), c21, c22);
		Point U1 = new IntersectionPoint("poleU1" + Math.round(Math.random()*1000), d11, d12);
		Point U2 = new IntersectionPoint("poleU2" + Math.round(Math.random()*1000), d21, d22);
		this.shortcutListOfConstructions.add(T1);
		this.shortcutListOfConstructions.add(T2);
		this.shortcutListOfConstructions.add(U1);
		this.shortcutListOfConstructions.add(U2);
		
		Line t = new LineThroughTwoPoints("polet" + Math.round(Math.random()*1000), T1, T2);
		Line u = new LineThroughTwoPoints("poleu" + Math.round(Math.random()*1000), U1, U2);
		this.shortcutListOfConstructions.add(t);
		this.shortcutListOfConstructions.add(u);
		
		this.shortcutListOfConstructions.add(new IntersectionPoint(pointLabel, t, u));
		this.polar = polar;
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
		return "Pole";
	}
}
