/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.thmstatement;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.ogprover.polynomials.SymbolicPolynomial;
import com.ogprover.polynomials.SymbolicTerm;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.auxiliary.GeneralizedAngleTangent;
import com.ogprover.pp.tp.expressions.Difference;
import com.ogprover.pp.tp.expressions.AMExpression;
import com.ogprover.pp.tp.expressions.PythagorasDifference;
import com.ogprover.pp.tp.geoconstruction.GeoConstruction;
import com.ogprover.pp.tp.geoconstruction.Point;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for statement about equilateral triangle</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
/**
 * @author ipetrov
 *
 */
public class EquilateralTriangle extends DimensionThmStatement {
	/*
	 * Equilateral triangle is proved by proving equality of 
	 * two pairs of triangles' edges or two pairs of triangles' angles. 
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
	
	/**
	 * <i><b>
	 * Label of first vertex. 
	 * </b></i>
	 */
	public static final String ALabel = "A";
	/**
	 * <i><b>
	 * Label of second vertex. 
	 * </b></i>
	 */
	public static final String BLabel = "B";
	/**
	 * <i><b>
	 * Label of third vertex. 
	 * </b></i>
	 */
	public static final String CLabel = "C";
	
	/**
	 * <i>
	 * Symbolic polynomial that represents the condition 
	 * for equilateral triangle.
	 * </i>
	 */
	public static SymbolicPolynomial conditionForEquilateralTriangle = null;
	
	static {
		if (conditionForEquilateralTriangle == null) {
			/*
			 * The triangle ABC is equilateral iff AB=BC and BC=CA.
			 * 
			 * AB=sqrt((xB-xA)^2+(yB-yA)^2)=sqrt(X) where X=(xB-xA)^2+(yB-yA)^2
			 * BC=sqrt((xC-xB)^2+(yC-yB)^2)=sqrt(Y) where Y=(xC-xB)^2+(yC-yB)^2
			 * CA=sqrt((xA-xC)^2+(yA-yC)^2)=sqrt(Z) where Z=(xA-xC)^2+(yA-yC)^2
			 * 
			 * sqrt(X)=sqrt(Y) && sqrt(Y)=sqrt(Z)
			 * sqrt(X)-sqrt(Y)=0 && sqrt(Y)-sqrt(Z)=0, this conjunction is equivalent to sum of squares of these equalities
			 * X+Y-2*sqrt(XY)+Y+Z-2*sqrt(YZ)=0
			 * 2*(sqrt(XY)+sqrt(YZ))=X+2Y+Z, square this in order to get polynomial (for weaker condition)
			 * 4(XY+YZ+2Y*sqrt(XZ))=X^2+4Y^2+Z^2+4XY+2XZ+4YZ
			 * 8Y*sqrt(XZ)=X^2+4Y^2+Z^2+2XZ, square this in order to get polynomial (for weaker condition)
			 * 16XY^2Z=X^4+16Y^4+Z^4+4X^2Z^2+8X^2Y^2+2X^2Z^2+4X^3Z+8Y^2Z^2+16XY^2Z+4XZ^3
			 * X^4+16Y^4+Z^4+6X^2Z^2+8X^2Y^2+4X^3Z+8Y^2Z^2+4XZ^3=0
			 */
			// Following symbolic condition is very large polynomial
			/*
			SymbolicPolynomial X = Segment.substitutePointLabelsForSquareOfDistance((SymbolicPolynomial) Segment.getConditionForSquareOfDistance().clone(), ALabel, BLabel);
			SymbolicPolynomial Y = Segment.substitutePointLabelsForSquareOfDistance((SymbolicPolynomial) Segment.getConditionForSquareOfDistance().clone(), BLabel, CLabel);
			SymbolicPolynomial Z = Segment.substitutePointLabelsForSquareOfDistance((SymbolicPolynomial) Segment.getConditionForSquareOfDistance().clone(), CLabel, ALabel);
			
			conditionForEquilateralTriangle = (SymbolicPolynomial) X.clone().multiplyByPolynomial(X).multiplyByPolynomial(X).multiplyByPolynomial(X); // X^4
			conditionForEquilateralTriangle.addPolynomial(Y.clone().multiplyByPolynomial(Y).multiplyByPolynomial(Y).multiplyByPolynomial(Y).multiplyByTerm(new SymbolicTerm(16))); //+16Y^4
			conditionForEquilateralTriangle.addPolynomial(Z.clone().multiplyByPolynomial(Z).multiplyByPolynomial(Z).multiplyByPolynomial(Z)); //+Z^4
			conditionForEquilateralTriangle.addPolynomial(X.clone().multiplyByPolynomial(X).multiplyByPolynomial(Z).multiplyByPolynomial(Z).multiplyByTerm(new SymbolicTerm(6))); //+6X^2Z^2
			conditionForEquilateralTriangle.addPolynomial(X.clone().multiplyByPolynomial(X).multiplyByPolynomial(Y).multiplyByPolynomial(Y).multiplyByTerm(new SymbolicTerm(8))); //+8X^2Y^2
			conditionForEquilateralTriangle.addPolynomial(X.clone().multiplyByPolynomial(X).multiplyByPolynomial(X).multiplyByPolynomial(Z).multiplyByTerm(new SymbolicTerm(4))); //+4X^3Z
			conditionForEquilateralTriangle.addPolynomial(Y.clone().multiplyByPolynomial(Y).multiplyByPolynomial(Z).multiplyByPolynomial(Z).multiplyByTerm(new SymbolicTerm(8))); //+8Y^2Z^2
			conditionForEquilateralTriangle.addPolynomial(X.clone().multiplyByPolynomial(Z).multiplyByPolynomial(Z).multiplyByPolynomial(Z).multiplyByTerm(new SymbolicTerm(4))); //+4XZ^3
			*/
			
			// Another possibility is to use angles.
			/*
			 * Triangle ABC is equilateral iff tg(<CAB)=sqrt(3) and tg(<ABC)=sqrt(3) i.e. 2 angles are of 60 degrees (and therefore the third).
			 * 
			 * tg(<A)^2=3 and tg(<B)^2=3
			 * tg(<A)=nA/dA; tg(<B)=nB/dB
			 * nA^2-3dA^2=0 and nB^2-3dB^2=0
			 * 
			 * This conjunction is equivalent to sum of squares of these equalities
			 * (nA^2 - 3dA^2)^2 + (nB^2 - 3dB^2)^2 = 0
			 */
			ArrayList<SymbolicPolynomial> tgA = GeneralizedAngleTangent.getSubstitutedConditionForTangent(CLabel, ALabel, BLabel);
			ArrayList<SymbolicPolynomial> tgB = GeneralizedAngleTangent.getSubstitutedConditionForTangent(ALabel, BLabel, CLabel);
			SymbolicPolynomial nA = tgA.get(GeneralizedAngleTangent.TANGENT_NUMERATOR);
			SymbolicPolynomial dA = tgA.get(GeneralizedAngleTangent.TANGENT_DENOMINATOR);
			SymbolicPolynomial nB = tgB.get(GeneralizedAngleTangent.TANGENT_NUMERATOR);
			SymbolicPolynomial dB = tgB.get(GeneralizedAngleTangent.TANGENT_DENOMINATOR);
			SymbolicPolynomial X = (SymbolicPolynomial) nA.clone().multiplyByPolynomial(nA).subtractPolynomial(dA.clone().multiplyByPolynomial(dA).multiplyByTerm(new SymbolicTerm(3)));
			SymbolicPolynomial Y = (SymbolicPolynomial) nB.clone().multiplyByPolynomial(nB).subtractPolynomial(dB.clone().multiplyByPolynomial(dB).multiplyByTerm(new SymbolicTerm(3)));
			conditionForEquilateralTriangle = (SymbolicPolynomial) X.clone().multiplyByPolynomial(X).addPolynomial(Y.clone().multiplyByPolynomial(Y));
			
			/*
			 * Notes:
			 * 	1. Due to squaring of sqrt(3), this condition is broader than pure condition for equilateral triangles.
			 * 	2. Due to complexity of polynomial for condition of equilateral triangles, it is sometimes
			 *     better to separately prove equality of two pairs of triangle's edges or two pairs of triangle's angles. 
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
	 * @param A	First vertex of triangle
	 * @param B	Second vertex of triangle
	 * @param C	Third vertex of triangle
	 */
	public EquilateralTriangle(Point A, Point B, Point C) {
		this.geoObjects = new Vector<GeoConstruction>();
		this.geoObjects.add(A);
		this.geoObjects.add(B);
		this.geoObjects.add(C);
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
		sb.append("Triangle ");
		sb.append(this.geoObjects.get(0).getGeoObjectLabel());
		sb.append(this.geoObjects.get(1).getGeoObjectLabel());
		sb.append(this.geoObjects.get(2).getGeoObjectLabel());
		sb.append(" is equilateral.");
		return sb.toString();
	}

	/**
	 * @see com.ogprover.pp.tp.thmstatement.ThmStatement#getAlgebraicForm()
	 */
	@Override
	public XPolynomial getAlgebraicForm() {
		Map<String, Point> pointsMap = new HashMap<String, Point>();
		pointsMap.put(ALabel, (Point)this.geoObjects.get(0));
		pointsMap.put(BLabel, (Point)this.geoObjects.get(1));
		pointsMap.put(CLabel, (Point)this.geoObjects.get(2));
		
		return OGPTP.instantiateCondition(conditionForEquilateralTriangle, pointsMap);
	}




	@Override
	public AreaMethodTheoremStatement getAreaMethodStatement() {
		Point a = (Point)this.geoObjects.get(0);
		Point b = (Point)this.geoObjects.get(1);
		Point c = (Point)this.geoObjects.get(2);
		
		AMExpression ab = new PythagorasDifference(a, b, a);
		AMExpression bc = new PythagorasDifference(c, b, c);
		AMExpression ac = new PythagorasDifference(a, c, a);
		
		AMExpression firstDifference = new Difference(ab, bc);
		AMExpression secondDifference = new Difference(ab, ac);
		
		Vector<AMExpression> statements = new Vector<AMExpression>();
		statements.add(firstDifference);
		statements.add(secondDifference);
		
		return new AreaMethodTheoremStatement(getStatementDesc(), statements);
	}
}
