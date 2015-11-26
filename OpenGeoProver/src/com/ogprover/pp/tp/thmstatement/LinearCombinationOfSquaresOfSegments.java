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
* <dd>Class for statement about linear combination of squares of segments.</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class LinearCombinationOfSquaresOfSegments extends DimensionThmStatement {
	// Segments need not be collinear or parallel
	//
	// Linear combination of segments' squares:
	// k1*s1^2 + k2*s2^2 + ... + kn*sn^2 = 0, where ki are real constants.
	
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
	 * Segments from linear combination
	 */
	private Vector<GeneralizedSegment> segments = null;
	/**
	 * Coefficients from linear combination
	 */
	private Vector<Double> coefficients = null;
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @param segments the segments to set
	 */
	public void setSegments(Vector<GeneralizedSegment> segments) {
		this.segments = segments;
	}

	/**
	 * @return the segments
	 */
	public Vector<GeneralizedSegment> getSegments() {
		return segments;
	}

	/**
	 * @param coefficients the coefficients to set
	 */
	public void setCoefficients(Vector<Double> coefficients) {
		this.coefficients = coefficients;
	}

	/**
	 * @return the coefficients
	 */
	public Vector<Double> getCoefficients() {
		return coefficients;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method.
	 * 
	 * @param segments		Segments of linear combination
	 * @param coefficients	Coefficients of linear combination
	 */
	public LinearCombinationOfSquaresOfSegments(Vector<GeneralizedSegment> segments, Vector<Double> coefficients) {
		Point tempP = null;
		
		this.segments = segments;
		this.coefficients = coefficients;
		
		this.geoObjects = new Vector<GeoConstruction>();
		
		if (this.segments != null) {
			for (GeneralizedSegment genseg : this.segments) {
				if (genseg instanceof Segment) {
					Segment seg = (Segment)genseg;
					tempP = seg.getFirstEndPoint();
					if (this.geoObjects.indexOf(tempP) < 0)
						this.geoObjects.add(tempP);
				
					tempP = seg.getSecondEndPoint();
					if (this.geoObjects.indexOf(tempP) < 0)
						this.geoObjects.add(tempP);
				}
				else if (genseg instanceof ProductOfTwoSegments) {
					ProductOfTwoSegments prodseg = (ProductOfTwoSegments)genseg;
					Segment seg1 = prodseg.getFirstSegment();
					Segment seg2 = prodseg.getSecondSegment();
					
					tempP = seg1.getFirstEndPoint();
					if (this.geoObjects.indexOf(tempP) < 0)
						this.geoObjects.add(tempP);
				
					tempP = seg1.getSecondEndPoint();
					if (this.geoObjects.indexOf(tempP) < 0)
						this.geoObjects.add(tempP);
					
					tempP = seg2.getFirstEndPoint();
					if (this.geoObjects.indexOf(tempP) < 0)
						this.geoObjects.add(tempP);
				
					tempP = seg2.getSecondEndPoint();
					if (this.geoObjects.indexOf(tempP) < 0)
						this.geoObjects.add(tempP);
				}
			}
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
		XPolynomial statementPoly = new XPolynomial();
		
		int ii = 0;
		for (GeneralizedSegment genseg : this.segments) {
			double coeff = (this.coefficients != null && this.coefficients.get(ii) != null) ? this.coefficients.get(ii).doubleValue() : 1.0;
			
			if (genseg instanceof Segment) {
				Segment seg = (Segment)genseg;
				XPolynomial segpoly = (XPolynomial) seg.getInstantiatedConditionForSquareOfSegment();
				statementPoly.addPolynomial(segpoly.multiplyByRealConstant(coeff));
			}
			else if (genseg instanceof ProductOfTwoSegments) {
				ProductOfTwoSegments prodseg = (ProductOfTwoSegments)genseg;
				Segment seg1 = prodseg.getFirstSegment();
				Segment seg2 = prodseg.getSecondSegment();
				XPolynomial segpoly1 = seg1.getInstantiatedConditionForSquareOfSegment();
				XPolynomial segpoly2 = seg2.getInstantiatedConditionForSquareOfSegment();
				statementPoly.addPolynomial(segpoly1.multiplyByPolynomial(segpoly2).multiplyByRealConstant(coeff));
			}
			ii++;
		}
		
		return statementPoly;
	}

	/**
	 * @see com.ogprover.pp.tp.thmstatement.ThmStatement#getStatementDesc()
	 */
	@Override
	public String getStatementDesc() {
		StringBuilder sb = new StringBuilder();
		sb.append("Linear combination of squares of segments: \n");
		for (int ii = 0; ii < this.coefficients.size(); ii++) {
			if (ii > 0  && this.coefficients.get(ii) > 0)
				sb.append("+");
			sb.append(this.coefficients.get(ii).doubleValue());
			sb.append("*sqr(");
			sb.append(this.segments.get(ii).getDescription());
			sb.append(")\n");
		}
		sb.append(" equals zero");
		return sb.toString();
	}

	@Override
	public AreaMethodTheoremStatement getAreaMethodStatement() {
		OpenGeoProver.settings.getLogger().error("The area method does not currently use floating-point calculus.");
		return null;
	}
}
