/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.thmstatement;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.ogprover.polynomials.SymbolicPolynomial;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.polynomials.XTerm;
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
* <dd>Class for statement about two congruent triangles</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
/**
 * @author ipetrov
 *
 */
public class CongruentTriangles extends DimensionThmStatement {
	/*
	 * Congruence is proved by proving equality of three pairs of
	 * corresponding triangles' sides. 
	 */
	
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
	
	// Symbolic labels for vertices of two triangles
	public static String A1Label = "A1";
	public static String B1Label = "B1";
	public static String C1Label = "C1";
	public static String A2Label = "A2";
	public static String B2Label = "B2";
	public static String C2Label = "C2";
	
	/**
	 * <i>
	 * Symbolic polynomial that represents the condition 
	 * for two congruent triangles.
	 * </i>
	 */
	public static SymbolicPolynomial conditionForCongTriangles = null;
	
	static {
		if (conditionForCongTriangles == null) {
			/*
			 * Let A1B1C1 be a triangle with edges B1C1=a1, C1A1=b1 and A1B1=c1
			 * and A2B2C2 be a triangle with edges B2C2=a2, C2A2=b2 and A2B2=c2.
			 * 
			 * They are congruent iff following three equalities hold:
			 * a1=a2 && b1=b2 && c1=c2.
			 * To calculate the lengths of edges, square roots are used:
			 * a1=sqrt(x1), b1=sqrt(y1), c1=sqrt(z1) and
			 * a2=sqrt(x2), b2=sqrt(y2), c2=sqrt(z2), 
			 * where x1, y1, z1, x2, y2 and z2 are squares of lengths of
			 * triangles' edges.
			 * 
			 * sqrt(x1)=sqrt(x2)
			 * sqrt(y1)=sqrt(y2)
			 * sqrt(z1)=sqrt(z2)
			 * 		Point a = (Point)pointList.get(0);
		Point b = (Point)pointList.get(1);
			 * This system of equalities is equal to following system:
			 * 
			 * sqrt(x1) - sqrt(x2) = 0
			 * sqrt(y1) - sqrt(y2) = 0
			 * sqrt(z1) - sqrt(z2) = 0
			 * 
			 * and this is equivalent to equality which is sum of squares
			 * of these three minor equalities.
			 * 
			 * x1 + x2 + y1 + y2 + z1 + z2 = 2*(sqrt(x1*x2) + sqrt(y1*y2) + sqrt(z1*z2))   ... (*)
			 * 
			 * let P = (x1 + x2 + y1 + y2 + z1 + z2)/2, then (*) becomes
			 * 
			 * P - sqrt(x1*x2) = sqrt(y1*y2) + sqrt(z1*z2), after squaring of this equality it becomes
			 * 
			 * P^2 + x1*x2 - 2*P*sqrt(x1*x2) = y1*y2 + z1*z2 + 2*sqrt(y1*y2*z1*z2)
			 * 2*(sqrt(y1*y2*z1*z2) + P*sqrt(x1*x2)) = P^2 + x1*x2 - y1*y2 - z1*z2 = Q, again square this:
			 * 4*y1*y2*z1*z2 + 4*P^2*x1*x2 + 8*P*sqrt(x1*x2*y1*y2*z1*z2) = Q^2
			 * 8*P*sqrt(x1*x2*y1*y2*z1*z2) = Q^2 - 4*P^2*x1*x2 - 4*y1*y2*z1*z2 = R, and square again:
			 * 64*P^2*x1*x2*y1*y2*z1*z2 - R^2 = 0 and this equality is condition for congruent triangles.
			 * 
			 * Notes:
			 * 	1. Due to squaring, this condition is broader than congruence of triangles.
			 * 	2. Due to complexity of polynomial for condition of congruence of triangles, it is sometimes
			 *     better to separately prove equality of three pairs of triangles' edges. 
			 */
			
			// Since condition is huge symbolic polynomial (e.g. only x1 contains 6^6 terms > 46500) it will not be
			// calculated here, but parts will be instantiated and then the whole polynomial condition will be calculated
			// during transformation to algebraic form.
			/*
			SymbolicPolynomial x1 = Segment.getSubstitutedConditionForSquareOfDistance(B1Label, C1Label);
			SymbolicPolynomial y1 = Segment.getSubstitutedConditionForSquareOfDistance(C1Label, A1Label);
			SymbolicPolynomial z1 = Segment.getSubstitutedConditionForSquareOfDistance(A1Label, B1Label);
			SymbolicPolynomial x2 = Segment.getSubstitutedConditionForSquareOfDistance(B2Label, C2Label);
			SymbolicPolynomial y2 = Segment.getSubstitutedConditionForSquareOfDistance(C2Label, A2Label);
			SymbolicPolynomial z2 = Segment.getSubstitutedConditionForSquareOfDistance(A2Label, B2Label);
			
			SymbolicPolynomial P = (SymbolicPolynomial) x1.clone().addPolynomial(y1).addPolynomial(z1).addPolynomial(x2).addPolynomial(y2).addPolynomial(z2);
			P.multiplyByTerm(new SymbolicTerm(0.5));
			
			SymbolicPolynomial Q = (SymbolicPolynomial) P.clone().multiplyByPolynomial(P);
			Q.addPolynomial(x1.clone().multiplyByPolynomial(x2));
			Q.subtractPolynomial(y1.clone().multiplyByPolynomial(y2));
			Q.subtractPolynomial(z1.clone().multiplyByPolynomial(z2));
			
			SymbolicPolynomial R = (SymbolicPolynomial)Q.clone().multiplyByPolynomial(Q);
			R.subtractPolynomial(P.clone().multiplyByPolynomial(P).multiplyByPolynomial(x1).multiplyByPolynomial(x2).multiplyByTerm(new SymbolicTerm(4)));
			R.subtractPolynomial(y1.clone().multiplyByPolynomial(y2).multiplyByPolynomial(z1).multiplyByPolynomial(z2).multiplyByTerm(new SymbolicTerm(4)));
			
			conditionForCongTriangles = new SymbolicPolynomial(64);
			conditionForCongTriangles.multiplyByPolynomial(P).multiplyByPolynomial(P);
			conditionForCongTriangles.multiplyByPolynomial(x1);
			conditionForCongTriangles.multiplyByPolynomial(x2);
			conditionForCongTriangles.multiplyByPolynomial(y1);
			conditionForCongTriangles.multiplyByPolynomial(y2);
			conditionForCongTriangles.multiplyByPolynomial(z1);
			conditionForCongTriangles.multiplyByPolynomial(z2);
			conditionForCongTriangles.subtractPolynomial(R.clone().multiplyByPolynomial(R));
			*/
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
	 * Constructor method
	 * 
	 * @param A1	First point of first triangle
	 * @param B1	Second point of first triangle
	 * @param C1	Third point of first triangle
	 * @param A2	First point of second triangle
	 * @param B2	Second point of second triangle
	 * @param C2	Third point of second triangle
	 */
	public CongruentTriangles(Point A1, Point B1, Point C1, Point A2, Point B2, Point C2) {
		this.geoObjects = new Vector<GeoConstruction>();
		this.geoObjects.add(A1);
		this.geoObjects.add(B1);
		this.geoObjects.add(C1);
		this.geoObjects.add(A2);
		this.geoObjects.add(B2);
		this.geoObjects.add(C2);
	}
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * @see com.ogprover.pp.tp.thmstatement.ThmStatement#getStatementDesc()
	 */
	@Override
	public String getStatementDesc() {
		StringBuilder sb = new StringBuilder();
		sb.append("Triangles ");
		sb.append(this.geoObjects.get(0).getGeoObjectLabel());
		sb.append(this.geoObjects.get(1).getGeoObjectLabel());
		sb.append(this.geoObjects.get(2).getGeoObjectLabel());
		sb.append(" and ");
		sb.append(this.geoObjects.get(3).getGeoObjectLabel());
		sb.append(this.geoObjects.get(4).getGeoObjectLabel());
		sb.append(this.geoObjects.get(5).getGeoObjectLabel());
		sb.append(" are congruent");
		return sb.toString();
	}
	
	/**
	 * @see com.ogprover.pp.tp.thmstatement.ThmStatement#getAlgebraicForm()
	 */
	@Override
	public XPolynomial getAlgebraicForm() {
		Map<String, Point> pointsMap = new HashMap<String, Point>();
		
		pointsMap.put(A1Label, (Point)geoObjects.get(0));
		pointsMap.put(B1Label, (Point)geoObjects.get(1));
		pointsMap.put(C1Label, (Point)geoObjects.get(2));
		pointsMap.put(A2Label, (Point)geoObjects.get(3));
		pointsMap.put(B2Label, (Point)geoObjects.get(4));
		pointsMap.put(C2Label, (Point)geoObjects.get(5));
		
		XPolynomial x1 = OGPTP.instantiateCondition(Segment.getSubstitutedConditionForSquareOfDistance(B1Label, C1Label), pointsMap);
		XPolynomial y1 = OGPTP.instantiateCondition(Segment.getSubstitutedConditionForSquareOfDistance(C1Label, A1Label), pointsMap);
		XPolynomial z1 = OGPTP.instantiateCondition(Segment.getSubstitutedConditionForSquareOfDistance(A1Label, B1Label), pointsMap);
		XPolynomial x2 = OGPTP.instantiateCondition(Segment.getSubstitutedConditionForSquareOfDistance(B2Label, C2Label), pointsMap);
		XPolynomial y2 = OGPTP.instantiateCondition(Segment.getSubstitutedConditionForSquareOfDistance(C2Label, A2Label), pointsMap);
		XPolynomial z2 = OGPTP.instantiateCondition(Segment.getSubstitutedConditionForSquareOfDistance(A2Label, B2Label), pointsMap);
		
		XPolynomial P = (XPolynomial) x1.clone().addPolynomial(y1).addPolynomial(z1).addPolynomial(x2).addPolynomial(y2).addPolynomial(z2);
		P.multiplyByTerm(new XTerm(0.5));
		
		XPolynomial Q = (XPolynomial) P.clone().multiplyByPolynomial(P);
		Q.addPolynomial(x1.clone().multiplyByPolynomial(x2));
		Q.subtractPolynomial(y1.clone().multiplyByPolynomial(y2));
		Q.subtractPolynomial(z1.clone().multiplyByPolynomial(z2));
		
		XPolynomial R = (XPolynomial)Q.clone().multiplyByPolynomial(Q);
		R.subtractPolynomial(P.clone().multiplyByPolynomial(P).multiplyByPolynomial(x1).multiplyByPolynomial(x2).multiplyByTerm(new XTerm(4)));
		R.subtractPolynomial(y1.clone().multiplyByPolynomial(y2).multiplyByPolynomial(z1).multiplyByPolynomial(z2).multiplyByTerm(new XTerm(4)));
		
		XPolynomial result = new XPolynomial(64);
		result.multiplyByPolynomial(P).multiplyByPolynomial(P);
		result.multiplyByPolynomial(x1);
		result.multiplyByPolynomial(x2);
		result.multiplyByPolynomial(y1);
		result.multiplyByPolynomial(y2);
		result.multiplyByPolynomial(z1);
		result.multiplyByPolynomial(z2);
		result.subtractPolynomial(R.clone().multiplyByPolynomial(R));
		
		return result;
	}




	@Override
	public AreaMethodTheoremStatement getAreaMethodStatement() {
		Point a = (Point)this.geoObjects.get(0);
		Point b = (Point)this.geoObjects.get(1);
		Point c = (Point)this.geoObjects.get(2);
		Point d = (Point)this.geoObjects.get(3);
		Point e = (Point)this.geoObjects.get(4);
		Point f = (Point)this.geoObjects.get(5);
		
		AMExpression squareOfAB = new PythagorasDifference(a, b, a);
		AMExpression squareOfBC = new PythagorasDifference(c, b, c);
		AMExpression squareOfAC = new PythagorasDifference(a, c, a);
		AMExpression squareOfDE = new PythagorasDifference(d, e, d);
		AMExpression squareOfEF = new PythagorasDifference(f, e, f);
		AMExpression squareOfDF = new PythagorasDifference(d, f, d);
		AMExpression difference1 = new Difference(squareOfAB, squareOfDE);
		AMExpression difference2 = new Difference(squareOfBC, squareOfEF);
		AMExpression difference3 = new Difference(squareOfAC, squareOfDF);
		
		Vector<AMExpression> statements = new Vector<AMExpression>();
		statements.add(difference1);
		statements.add(difference2);
		statements.add(difference3);
		
		return new AreaMethodTheoremStatement(getStatementDesc(), statements);
	}
}
