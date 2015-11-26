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
import com.ogprover.pp.tp.auxiliary.GeneralizedAngleTangent;
import com.ogprover.pp.tp.expressions.AreaOfTriangle;
import com.ogprover.pp.tp.expressions.Difference;
import com.ogprover.pp.tp.expressions.AMExpression;
import com.ogprover.pp.tp.expressions.Product;
import com.ogprover.pp.tp.expressions.PythagorasDifference;
import com.ogprover.pp.tp.geoconstruction.GeoConstruction;
import com.ogprover.pp.tp.geoconstruction.Point;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for statement about similar triangles</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class SimilarTriangles extends DimensionThmStatement {
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
	 * Label of first vertex of first triangle. 
	 * </b></i>
	 */
	public static final String A1Label = "A1";
	/**
	 * <i><b>
	 * Label of second vertex of first triangle. 
	 * </b></i>
	 */
	public static final String B1Label = "B1";
	/**
	 * <i><b>
	 * Label of third vertex of first triangle. 
	 * </b></i>
	 */
	public static final String C1Label = "C1";
	/**
	 * <i><b>
	 * Label of first vertex of second triangle. 
	 * </b></i>
	 */
	public static final String A2Label = "A2";
	/**
	 * <i><b>
	 * Label of second vertex of second triangle. 
	 * </b></i>
	 */
	public static final String B2Label = "B2";
	/**
	 * <i><b>
	 * Label of third vertex of second triangle. 
	 * </b></i>
	 */
	public static final String C2Label = "C2";
	/**
	 * Flag to hold information if triangles are of same orientation or not
	 */
	public boolean trianglesOfSameOrientation = true;
	
	
	/**
	 * <i>
	 * Symbolic polynomial that represents the condition 
	 * for two similar same oriented triangles.
	 * </i>
	 */
	public static SymbolicPolynomial conditionForSimilarTrianglesWithSameOrientation = null;
	/**
	 * <i>
	 * Symbolic polynomial that represents the condition 
	 * for two similar opposite oriented triangles.
	 * </i>
	 */
	public static SymbolicPolynomial conditionForSimilarTrianglesWithOppositeOrientation = null;
	
	static {
		/*
		 * Triangles A1B1C1 and A2B2C2 are similar iff angles A1B1C1 and A2B2C2 
		 * and angles B1C1A1 and B2C2A2 are equals. Orientations of triangles
		 * are very important - triangles need not have same orientation but
		 * vertices A1, B1 and C1 must correspond to vertices A2, B2 and C2 in 
		 * that exact order.
		 */
		// triangles of same orientation
		if (conditionForSimilarTrianglesWithSameOrientation == null) {
			
			SymbolicPolynomial betaAngles = GeneralizedAngleTangent.substitutePointLabelsForTwoAngles((SymbolicPolynomial)GeneralizedAngleTangent.getConditionForEqualsConvexAngles().clone(), A1Label, B1Label, C1Label, A2Label, B2Label, C2Label);
			SymbolicPolynomial gammaAngles = GeneralizedAngleTangent.substitutePointLabelsForTwoAngles((SymbolicPolynomial)GeneralizedAngleTangent.getConditionForEqualsConvexAngles().clone(), B1Label, C1Label, A1Label, B2Label, C2Label, A2Label);
			
			// add conjunction of these conditions i.e. sum of squares
			conditionForSimilarTrianglesWithSameOrientation = (SymbolicPolynomial) betaAngles.clone().multiplyByPolynomial(betaAngles);
			conditionForSimilarTrianglesWithSameOrientation.addPolynomial(gammaAngles.clone().multiplyByPolynomial(gammaAngles));
		}
		
		// triangles of opposite orientation
		if (conditionForSimilarTrianglesWithOppositeOrientation == null) {
			
			SymbolicPolynomial betaAngles = GeneralizedAngleTangent.substitutePointLabelsForTwoAngles((SymbolicPolynomial)GeneralizedAngleTangent.getConditionForSupplementaryConvexAngles().clone(), A1Label, B1Label, C1Label, A2Label, B2Label, C2Label);
			SymbolicPolynomial gammaAngles = GeneralizedAngleTangent.substitutePointLabelsForTwoAngles((SymbolicPolynomial)GeneralizedAngleTangent.getConditionForSupplementaryConvexAngles().clone(), B1Label, C1Label, A1Label, B2Label, C2Label, A2Label);
			
			// add conjunction of these conditions i.e. sum of squares
			conditionForSimilarTrianglesWithOppositeOrientation = (SymbolicPolynomial) betaAngles.clone().multiplyByPolynomial(betaAngles);
			conditionForSimilarTrianglesWithOppositeOrientation.addPolynomial(gammaAngles.clone().multiplyByPolynomial(gammaAngles));
		}
	}
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @return the trianglesOfSameOrientation
	 */
	public boolean areTrianglesOfSameOrientation() {
		return trianglesOfSameOrientation;
	}

	/**
	 * @param trianglesOfSameOrientationFlag the trianglesOfSameOrientation to set
	 */
	public void setTrianglesOfSameOrientationFlag(
			boolean trianglesOfSameOrientationFlag) {
		this.trianglesOfSameOrientation = trianglesOfSameOrientationFlag;
	}
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method.
	 * 
	 * @param A1	First vertex of first triangle
	 * @param B1	Second vertex of first triangle
	 * @param C1	Third vertex of first triangle
	 * @param A2	First vertex of second triangle
	 * @param B2	Second vertex of second triangle
	 * @param C2	Third vertex of second triangle
	 * @param areOfSameOrientation	Flag to determine whether triangles are of same orientation
	 */
	public SimilarTriangles(Point A1, Point B1, Point C1, Point A2, Point B2, Point C2, boolean areOfSameOrientation) {
		this.geoObjects = new Vector<GeoConstruction>();
		this.geoObjects.add(A1);
		this.geoObjects.add(B1);
		this.geoObjects.add(C1);
		this.geoObjects.add(A2);
		this.geoObjects.add(B2);
		this.geoObjects.add(C2);
		this.trianglesOfSameOrientation = areOfSameOrientation;
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
		pointsMap.put(A1Label, ((Point)this.geoObjects.get(0)).clone());
		pointsMap.put(B1Label, ((Point)this.geoObjects.get(1)).clone());
		pointsMap.put(C1Label, ((Point)this.geoObjects.get(2)).clone());
		pointsMap.put(A2Label, ((Point)this.geoObjects.get(3)).clone());
		pointsMap.put(B2Label, ((Point)this.geoObjects.get(4)).clone());
		pointsMap.put(C2Label, ((Point)this.geoObjects.get(5)).clone());
		
		if (this.trianglesOfSameOrientation)
			return OGPTP.instantiateCondition(conditionForSimilarTrianglesWithSameOrientation, pointsMap);
		return OGPTP.instantiateCondition(conditionForSimilarTrianglesWithOppositeOrientation, pointsMap);
	}

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
		sb.append(" are similar");
		return sb.toString();
	}

	@Override
	public AreaMethodTheoremStatement getAreaMethodStatement() {
		/*
		 * We have to verify three times that the angles are the same. See EqualAngles.java.
		 */
		Point a = (Point)this.geoObjects.get(0);
		Point b = (Point)this.geoObjects.get(1);
		Point c = (Point)this.geoObjects.get(2);
		Point d = (Point)this.geoObjects.get(3);
		Point e = (Point)this.geoObjects.get(4);
		Point f = (Point)this.geoObjects.get(5);
		
		AMExpression sabc = new AreaOfTriangle(a, b, c);
		AMExpression sdef = new AreaOfTriangle(d, e, f);
		AMExpression pabc = new PythagorasDifference(a, b, c);
		AMExpression pbca = new PythagorasDifference(b, c, a);
		AMExpression pcab = new PythagorasDifference(c, a, b);
		AMExpression pdef = new PythagorasDifference(d, e, f);
		AMExpression pefd = new PythagorasDifference(e, f, d);
		AMExpression pfde = new PythagorasDifference(f, d, e);
		
		AMExpression product1 = new Product(sabc, pdef);
		AMExpression product1bis = new Product(sdef, pabc);
		AMExpression product2 = new Product(sabc, pefd);
		AMExpression product2bis = new Product(sdef, pbca);
		AMExpression product3 = new Product(sabc, pfde);
		AMExpression product3bis = new Product(sdef, pcab);
		AMExpression difference1 = new Difference(product1, product1bis);
		AMExpression difference2 = new Difference(product2, product2bis);
		AMExpression difference3 = new Difference(product3, product3bis);
		
		Vector<AMExpression> statements = new Vector<AMExpression>();
		statements.add(difference1);
		statements.add(difference2);
		statements.add(difference3);
		
		return new AreaMethodTheoremStatement(getStatementDesc(), statements);
	}
}