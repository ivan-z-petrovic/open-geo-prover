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
import com.ogprover.pp.tp.expressions.Difference;
import com.ogprover.pp.tp.expressions.AMExpression;
import com.ogprover.pp.tp.expressions.PythagorasDifference;
import com.ogprover.pp.tp.geoconstruction.GeoConstruction;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.ogprover.pp.tp.geoobject.Segment;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for statement about segments of equal lengths</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class SegmentsOfEqualLengths extends DimensionThmStatement {
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
	 * Label of first point of first segment. 
	 * </b></i>
	 */
	public static final String ALabel = "A";
	/**
	 * <i><b>
	 * Label of second point of first segment. 
	 * </b></i>
	 */
	public static final String BLabel = "B";
	/**
	 * <i><b>
	 * Label of first point of second segment. 
	 * </b></i>
	 */
	public static final String CLabel = "C";
	/**
	 * <i><b>
	 * Label of second point of second segment. 
	 * </b></i>
	 */
	public static final String DLabel = "D";
	
	/**
	 * <i>
	 * Symbolic polynomial that represents the condition 
	 * for two segments of equal lengths.
	 * </i>
	 */
	public static SymbolicPolynomial conditionForSegmentsOfEqualLengths = null;
	
	static {
		if (conditionForSegmentsOfEqualLengths == null) {
			/*
			 * Two segments AB and CD have equal lengths iff AB^2 = CD^2
			 */
			SymbolicPolynomial sdAB = Segment.substitutePointLabelsForSquareOfDistance((SymbolicPolynomial) Segment.getConditionForSquareOfDistance().clone(), ALabel, BLabel);
			SymbolicPolynomial sdCD = Segment.substitutePointLabelsForSquareOfDistance((SymbolicPolynomial) Segment.getConditionForSquareOfDistance().clone(), CLabel, DLabel);
			
			conditionForSegmentsOfEqualLengths = (SymbolicPolynomial) sdAB.subtractPolynomial(sdCD);
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
	 * @param A	First point of first segment
	 * @param B	Second point of first segment
	 * @param C	First point of second segment
	 * @param D	Second point of second segment
	 */
	public SegmentsOfEqualLengths(Point A, Point B, Point C, Point D) {
		this.geoObjects = new Vector<GeoConstruction>();
		this.geoObjects.add(A);
		this.geoObjects.add(B);
		this.geoObjects.add(C);
		this.geoObjects.add(D);
	}
	
	/**
	 * Constructor method.
	 * 
	 * @param seg1	First segment
	 * @param seg2	Second segment
	 */
	public SegmentsOfEqualLengths(Segment seg1, Segment seg2) {
		this(seg1.getFirstEndPoint(), seg1.getSecondEndPoint(), seg2.getFirstEndPoint(), seg2.getSecondEndPoint());
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
		pointsMap.put(CLabel, (Point)this.geoObjects.get(2));
		pointsMap.put(DLabel, (Point)this.geoObjects.get(3));
		
		return OGPTP.instantiateCondition(conditionForSegmentsOfEqualLengths, pointsMap);
	}

	/**
	 * @see com.ogprover.pp.tp.thmstatement.ThmStatement#getStatementDesc()
	 */
	@Override
	public String getStatementDesc() {
		Segment firstSeg = new Segment((Point)this.geoObjects.get(0), (Point)this.geoObjects.get(1));
		Segment secondSeg = new Segment((Point)this.geoObjects.get(2), (Point)this.geoObjects.get(3));
		StringBuilder sb = new StringBuilder();
		sb.append("Segments ");
		sb.append(firstSeg.getDescription());
		sb.append(" and ");
		sb.append(secondSeg.getDescription());
		sb.append(" have equal lengths");
		return sb.toString();
	}

	@Override
	public AreaMethodTheoremStatement getAreaMethodStatement() {
		Point a = (Point)this.geoObjects.get(0);
		Point b = (Point)this.geoObjects.get(1);
		Point c = (Point)this.geoObjects.get(2);
		Point d = (Point)this.geoObjects.get(3);
		
		AMExpression squareOfAB = new PythagorasDifference(a, b, a);
		AMExpression squareOfCD = new PythagorasDifference(c, d, c);
		AMExpression difference = new Difference(squareOfAB, squareOfCD);
		
		Vector<AMExpression> statements = new Vector<AMExpression>();
		statements.add(difference);
		
		return new AreaMethodTheoremStatement(getStatementDesc(), statements);
	}
}
