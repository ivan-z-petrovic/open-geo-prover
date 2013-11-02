/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.thmstatement;

import java.util.Vector;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.pp.tp.auxiliary.GeneralizedSegment;
import com.ogprover.pp.tp.auxiliary.ProductOfTwoSegments;
import com.ogprover.pp.tp.geoconstruction.GeoConstruction;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.ogprover.pp.tp.geoobject.Segment;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for statement about algebraic sum of three segments.</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class AlgebraicSumOfThreeSegments extends DimensionThmStatement {
	// Segments need not be collinear or parallel
	
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
	 * Let AB=a, CD=b and EF=c, then one segment is sum of other two segments:
	 * a = b + c 
	 * or
	 * b = a + c
	 * or
	 * c = a + b
	 * 
	 * this gives equation:
	 * (b + c - a)(a + c - b)(a + b - c) = 0
	 * 
	 * we can multiply also by (a + b + c) since a, b, c >=0 ==> a + b + c >=0
	 * and a + b + c = 0 <==> a = b = c = 0
	 * 
	 * then we get equivalent condition by multiplying by (a + b + c):
	 * 
	 *  [(a + b) - c][(a + b) + c][c + (a - b)][c - (a - b)] = 0
	 *  [(a + b)^2 - c^2][c^2 - (a - b)^2] = 0
	 *  finally we obtain:
	 *  a^4 + b^4 + c^4 - 2*(a^2*b^2 + a^2*c^2 + b^2*c^2) = 0
	 */
	
	/**
	 * First segment of triple of segments.
	 */
	private GeneralizedSegment firstSegment = null;
	/**
	 * Second segment of triple of segments.
	 */
	private GeneralizedSegment secondSegment = null;
	/**
	 * Third segment of triple of segments.
	 */
	private GeneralizedSegment thirdSegment = null;
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @param firstSegment the firstSegment to set
	 */
	public void setFirstSegment(GeneralizedSegment firstSegment) {
		this.firstSegment = firstSegment;
	}

	/**
	 * @return the firstSegment
	 */
	public GeneralizedSegment getFirstSegment() {
		return firstSegment;
	}

	/**
	 * @param secondSegment the secondSegment to set
	 */
	public void setSecondSegment(GeneralizedSegment secondSegment) {
		this.secondSegment = secondSegment;
	}

	/**
	 * @return the secondSegment
	 */
	public GeneralizedSegment getSecondSegment() {
		return secondSegment;
	}

	/**
	 * @param thirdSegment the thirdSegment to set
	 */
	public void setThirdSegment(GeneralizedSegment thirdSegment) {
		this.thirdSegment = thirdSegment;
	}

	/**
	 * @return the thirdSegment
	 */
	public GeneralizedSegment getThirdSegment() {
		return thirdSegment;
	}
	
	
	
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
	 * @param E	First point of third segment
	 * @param F	Second point of third segment
	 */
	public AlgebraicSumOfThreeSegments(Point A, Point B, Point C, Point D, Point E, Point F) {
		this.geoObjects = new Vector<GeoConstruction>();
		this.geoObjects.add(A);
		this.geoObjects.add(B);
		this.geoObjects.add(C);
		this.geoObjects.add(D);
		this.geoObjects.add(E);
		this.geoObjects.add(F);
		this.firstSegment = new Segment(A, B);
		this.secondSegment = new Segment(C, D);
		this.thirdSegment = new Segment(E, F);
	}
	
	/**
	 * Constructor method.
	 * 
	 * @param firstSeg	First segment
	 * @param secondSeg	Second segment
	 * @param thirdSeg	Third segment
	 */
	public AlgebraicSumOfThreeSegments(GeneralizedSegment firstSeg, GeneralizedSegment secondSeg, GeneralizedSegment thirdSeg) {
		this.firstSegment = firstSeg;
		this.secondSegment = secondSeg;
		this.thirdSegment = thirdSeg;
		
		this.geoObjects = new Vector<GeoConstruction>();
		
		if (firstSeg instanceof Segment) {
			Segment seg = (Segment)firstSeg;
			
			this.geoObjects.add(seg.getFirstEndPoint());
			this.geoObjects.add(seg.getSecondEndPoint());
		}
		else if (firstSeg instanceof ProductOfTwoSegments) {
			ProductOfTwoSegments seg = (ProductOfTwoSegments)firstSeg;
			Segment a = seg.getFirstSegment();
			Segment b = seg.getSecondSegment();
			
			this.geoObjects.add(a.getFirstEndPoint());
			this.geoObjects.add(a.getSecondEndPoint());
			this.geoObjects.add(b.getFirstEndPoint());
			this.geoObjects.add(b.getSecondEndPoint());
		}
		
		if (secondSeg instanceof Segment) {
			Segment seg = (Segment)secondSeg;
			
			this.geoObjects.add(seg.getFirstEndPoint());
			this.geoObjects.add(seg.getSecondEndPoint());
		}
		else if (secondSeg instanceof ProductOfTwoSegments) {
			ProductOfTwoSegments seg = (ProductOfTwoSegments)secondSeg;
			Segment a = seg.getFirstSegment();
			Segment b = seg.getSecondSegment();
			
			this.geoObjects.add(a.getFirstEndPoint());
			this.geoObjects.add(a.getSecondEndPoint());
			this.geoObjects.add(b.getFirstEndPoint());
			this.geoObjects.add(b.getSecondEndPoint());
		}
		
		if (thirdSeg instanceof Segment) {
			Segment seg = (Segment)thirdSeg;
			
			this.geoObjects.add(seg.getFirstEndPoint());
			this.geoObjects.add(seg.getSecondEndPoint());
		}
		else if (thirdSeg instanceof ProductOfTwoSegments) {
			ProductOfTwoSegments seg = (ProductOfTwoSegments)thirdSeg;
			Segment a = seg.getFirstSegment();
			Segment b = seg.getSecondSegment();
			
			this.geoObjects.add(a.getFirstEndPoint());
			this.geoObjects.add(a.getSecondEndPoint());
			this.geoObjects.add(b.getFirstEndPoint());
			this.geoObjects.add(b.getSecondEndPoint());
		}
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
		// a^4 + b^4 + c^4 - 2*(a^2*b^2 + a^2*c^2 + b^2*c^2) = 0
		XPolynomial statementPoly = new XPolynomial();
		
		XPolynomial sqra = this.firstSegment.getInstantiatedConditionForSquareOfSegment();
		XPolynomial sqrb = this.secondSegment.getInstantiatedConditionForSquareOfSegment();
		XPolynomial sqrc = this.thirdSegment.getInstantiatedConditionForSquareOfSegment();
		
		statementPoly.addPolynomial(sqra.clone().multiplyByPolynomial(sqra)); // a^4
		statementPoly.addPolynomial(sqrb.clone().multiplyByPolynomial(sqrb)); // b^4
		statementPoly.addPolynomial(sqrc.clone().multiplyByPolynomial(sqrc)); // c^4
		statementPoly.addPolynomial(sqra.clone().multiplyByPolynomial(sqrb).multiplyByRealConstant(-2)); // -2*a^2*b^2
		statementPoly.addPolynomial(sqra.clone().multiplyByPolynomial(sqrc).multiplyByRealConstant(-2)); // -2*a^2*c^2
		statementPoly.addPolynomial(sqrb.clone().multiplyByPolynomial(sqrc).multiplyByRealConstant(-2)); // -2*b^2*c^2
		
		return statementPoly;
	}

	/**
	 * @see com.ogprover.pp.tp.thmstatement.ThmStatement#getStatementDesc()
	 */
	@Override
	public String getStatementDesc() {
		StringBuilder sb = new StringBuilder();
		sb.append("Algebraic sum of segments ");
		sb.append(this.firstSegment.getDescription());
		sb.append(", ");
		sb.append(this.secondSegment.getDescription());
		sb.append(" and ");
		sb.append(this.thirdSegment.getDescription());
		sb.append(" is zero");
		return sb.toString();
	}

	@Override
	public AreaMethodTheoremStatement getAreaMethodStatement() {
		OpenGeoProver.settings.getLogger().error("Statement not currently supported by the area method.");
		return null;
	}
}
