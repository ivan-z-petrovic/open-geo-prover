/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.thmstatement;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.ogprover.polynomials.SymbolicPolynomial;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.expressions.AMExpression;
import com.ogprover.pp.tp.expressions.PythagorasDifference;
import com.ogprover.pp.tp.geoconstruction.GeoConstruction;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.ogprover.pp.tp.geoobject.Segment;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for statement about identical points</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
/*
 * Note: it is not good to use this kind of statement to show
 * theorems for concurrent set of points because proving may
 * not be possible. Instead of that for these theorems use 
 * statement about point on set of points. Statement about
 * identical points is used when needed to show that some 
 * constructed point is e.g. midpoint of segment or other
 * special point like foot of perpendicular line or fourth
 * harmonic conjugate.
 */
public class IdenticalPoints extends PositionThmStatement {
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
	 * <i><b>
	 * Label of first point. 
	 * </b></i>
	 */
	public static final String ALabel = "A";
	/**
	 * <i><b>
	 * Label of second point. 
	 * </b></i>
	 */
	public static final String BLabel = "B";
	
	/**
	 * <i>
	 * Symbolic polynomial that represents the condition 
	 * for two identical points.
	 * </i>
	 */
	public static SymbolicPolynomial conditionForIdenticalPoints = null;
	
	static {
		if (conditionForIdenticalPoints == null) {
			/*
			 * Two points A and B are identical iff AB = 0 or iff AB^2 = 0
			 * (or both pairs of coordinates are equals so we have sum of 
			 * squares that represents of conditions for both coordinates).
			 */
			conditionForIdenticalPoints = (SymbolicPolynomial) Segment.substitutePointLabelsForSquareOfDistance((SymbolicPolynomial) Segment.getConditionForSquareOfDistance().clone(), ALabel, BLabel);
		}
	}
	
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
	 * @param A	First point
	 * @param B	Second point
	 */
	public IdenticalPoints(Point A, Point B) {
		this.geoObjects = new Vector<GeoConstruction>();
		this.geoObjects.add(A);
		this.geoObjects.add(B);
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * @see com.ogprover.pp.tp.thmstatement.ThmStatement#getAlgebraicForm()
	 */
	@Override
	public XPolynomial getAlgebraicForm() {
		Map<String, Point> pointsMap = new HashMap<String, Point>();
		pointsMap.put(ALabel, (Point)this.geoObjects.get(0));
		pointsMap.put(BLabel, (Point)this.geoObjects.get(1));
		
		return OGPTP.instantiateCondition(conditionForIdenticalPoints, pointsMap);
	}

	/**
	 * @see com.ogprover.pp.tp.thmstatement.ThmStatement#getStatementDesc()
	 */
	@Override
	public String getStatementDesc() {
		StringBuilder sb = new StringBuilder();
		sb.append("Points ");
		sb.append(this.getGeoObjects().get(0).getGeoObjectLabel());
		sb.append(" and ");
		sb.append(this.getGeoObjects().get(1).getGeoObjectLabel());
		sb.append(" are identical");
		return sb.toString();
	}



	@Override
	public AreaMethodTheoremStatement getAreaMethodStatement() {
		Point a = (Point)this.geoObjects.get(0);
		Point b = (Point)this.geoObjects.get(1);
		
		AMExpression pythagorasDifference = new PythagorasDifference(a, b, a);
		
		Vector<AMExpression> statements = new Vector<AMExpression>();
		statements.add(pythagorasDifference);
		
		return new AreaMethodTheoremStatement(getStatementDesc(), statements);
	}
}
