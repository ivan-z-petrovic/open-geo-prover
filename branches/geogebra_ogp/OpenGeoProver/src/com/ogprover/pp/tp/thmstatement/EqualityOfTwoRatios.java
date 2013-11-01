/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.thmstatement;

import java.util.Vector;

import com.ogprover.polynomials.XPolynomial;
import com.ogprover.pp.tp.geoconstruction.GeoConstruction;
import com.ogprover.pp.tp.geoobject.Segment;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for statement about equality of two ratios of segments.</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class EqualityOfTwoRatios extends DimensionThmStatement {
	// Segments in ratios need not be collinear (on same or parallel lines).
	
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
	 * Numerator segment of first ratio
	 */
	private Segment numeratorSegmentOfFirstRatio = null;
	/**
	 * Denominator segment of first ratio
	 */
	private Segment denominatorSegmentOfFirstRatio = null;
	/**
	 * Numerator segment of second ratio
	 */
	private Segment numeratorSegmentOfSecondRatio = null;
	/**
	 * Denominator segment of second ratio
	 */
	private Segment denominatorSegmentOfSecondRatio = null;
	/**
	 * Multiplicator coefficient
	 */
	private double multiplicatorCoefficient = 0.0;
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @param numeratorSegmentOfFirstRatio the numeratorSegmentOfFirstRatio to set
	 */
	public void setNumeratorSegmentOfFirstRatio(
			Segment numeratorSegmentOfFirstRatio) {
		this.numeratorSegmentOfFirstRatio = numeratorSegmentOfFirstRatio;
	}

	/**
	 * @return the numeratorSegmentOfFirstRatio
	 */
	public Segment getNumeratorSegmentOfFirstRatio() {
		return numeratorSegmentOfFirstRatio;
	}

	/**
	 * @param denominatorSegmentOfFirstRatio the denominatorSegmentOfFirstRatio to set
	 */
	public void setDenominatorSegmentOfFirstRatio(
			Segment denominatorSegmentOfFirstRatio) {
		this.denominatorSegmentOfFirstRatio = denominatorSegmentOfFirstRatio;
	}

	/**
	 * @return the denominatorSegmentOfFirstRatio
	 */
	public Segment getDenominatorSegmentOfFirstRatio() {
		return denominatorSegmentOfFirstRatio;
	}

	/**
	 * @param numeratorSegmentOfSecondRatio the numeratorSegmentOfSecondRatio to set
	 */
	public void setNumeratorSegmentOfSecondRatio(
			Segment numeratorSegmentOfSecondRatio) {
		this.numeratorSegmentOfSecondRatio = numeratorSegmentOfSecondRatio;
	}

	/**
	 * @return the numeratorSegmentOfSecondRatio
	 */
	public Segment getNumeratorSegmentOfSecondRatio() {
		return numeratorSegmentOfSecondRatio;
	}

	/**
	 * @param denominatorSegmentOfSecondRatio the denominatorSegmentOfSecondRatio to set
	 */
	public void setDenominatorSegmentOfSecondRatio(
			Segment denominatorSegmentOfSecondRatio) {
		this.denominatorSegmentOfSecondRatio = denominatorSegmentOfSecondRatio;
	}

	/**
	 * @return the denominatorSegmentOfSecondRatio
	 */
	public Segment getDenominatorSegmentOfSecondRatio() {
		return denominatorSegmentOfSecondRatio;
	}
	
	/**
	 * @param multiplicatorCoefficient the multiplicatorCoefficient to set
	 */
	public void setMultiplicatorCoefficient(double multiplicatorCoefficient) {
		this.multiplicatorCoefficient = multiplicatorCoefficient;
	}

	/**
	 * @return the multiplicatorCoefficient
	 */
	public double getMultiplicatorCoefficient() {
		return multiplicatorCoefficient;
	}
	
	

	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method.
	 * 
	 * @param numSeg1	Numerator segment of first ratio
	 * @param denSeg1	Denominator segment of first ratio
	 * @param numSeg2	Numerator segment of second ratio
	 * @param denSeg2	Denominator segment of second ratio
	 * @param mulCoeff	Multiplicator coefficient
	 */
	public EqualityOfTwoRatios(Segment numSeg1, Segment denSeg1, Segment numSeg2, Segment denSeg2, double mulCoeff) {
		this.geoObjects = new Vector<GeoConstruction>();
		this.geoObjects.add(numSeg1.getFirstEndPoint());
		this.geoObjects.add(numSeg1.getSecondEndPoint());
		this.geoObjects.add(denSeg1.getFirstEndPoint());
		this.geoObjects.add(denSeg1.getSecondEndPoint());
		this.geoObjects.add(numSeg2.getFirstEndPoint());
		this.geoObjects.add(numSeg2.getSecondEndPoint());
		this.geoObjects.add(denSeg2.getFirstEndPoint());
		this.geoObjects.add(denSeg2.getSecondEndPoint());
		
		this.numeratorSegmentOfFirstRatio = numSeg1;
		this.denominatorSegmentOfFirstRatio = denSeg1;
		this.numeratorSegmentOfSecondRatio = numSeg2;
		this.denominatorSegmentOfSecondRatio = denSeg2;
		this.multiplicatorCoefficient = mulCoeff;
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
		/*
		 * AB/CD = k * EF/GH ==> AB^2*GH^2 - k^2*EF^2*CD^2 = 0
		 */
		return (XPolynomial) this.numeratorSegmentOfFirstRatio.getInstantiatedConditionForSquareOfSegment()
				                                                                    .multiplyByPolynomial(this.denominatorSegmentOfSecondRatio.getInstantiatedConditionForSquareOfSegment())
				                                                                    .subtractPolynomial(this.numeratorSegmentOfSecondRatio.getInstantiatedConditionForSquareOfSegment()
				                                                                    		                                              .multiplyByPolynomial(this.denominatorSegmentOfFirstRatio.getInstantiatedConditionForSquareOfSegment())
				                                                                                                                          .multiplyByRealConstant(this.multiplicatorCoefficient * this.multiplicatorCoefficient));
	}

	/**
	 * @see com.ogprover.pp.tp.thmstatement.ThmStatement#getStatementDesc()
	 */
	@Override
	public String getStatementDesc() {
		StringBuilder sb = new StringBuilder();
		sb.append("Ratio ");
		sb.append(this.numeratorSegmentOfFirstRatio.getDescription());
		sb.append("/");
		sb.append(this.denominatorSegmentOfFirstRatio.getDescription());
		sb.append(" is equal to ratio ");
		sb.append(this.numeratorSegmentOfSecondRatio.getDescription());
		sb.append("/");
		sb.append(this.denominatorSegmentOfSecondRatio.getDescription());
		return sb.toString();
	}

	@Override
	public AreaMethodTheoremStatement getAreaMethodStatement() {
		// TODO Auto-generated method stub
		return null;
	}
}
