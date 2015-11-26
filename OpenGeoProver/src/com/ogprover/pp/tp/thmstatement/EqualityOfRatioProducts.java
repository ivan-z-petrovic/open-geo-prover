/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.thmstatement;

import java.util.Vector;

import com.ogprover.polynomials.XPolynomial;
import com.ogprover.pp.tp.auxiliary.RatioOfTwoCollinearSegments;
import com.ogprover.pp.tp.auxiliary.RatioProduct;
import com.ogprover.pp.tp.geoconstruction.GeoConstruction;
import com.ogprover.pp.tp.geoconstruction.Point;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for statement about equality of ratio products.</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class EqualityOfRatioProducts extends DimensionThmStatement {
	// (A1B1/C1D1)*(A2B2/C2D2)*...*(AnBn/CnDn) = k*(E1F1/G1H1)*(E2F2/G2H2)*...*(EmFm/GmHm)
	// each pair of segments that makes a ratio must be a pair of collinear segments 
	// i.e. from same or parallel lines
	
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
	 * Ratio product from the left side of equality
	 */
	private RatioProduct leftProduct = null;
	/**
	 * Ratio product from the right side of equality
	 */
	private RatioProduct rightProduct = null;
	/**
	 * Multiplication factor from the right side of equation
	 */
	private double multiplicatorFactor = 0.0;
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @param leftProduct the leftProduct to set
	 */
	public void setLeftProduct(RatioProduct leftProduct) {
		this.leftProduct = leftProduct;
	}

	/**
	 * @return the leftProduct
	 */
	public RatioProduct getLeftProduct() {
		return leftProduct;
	}

	/**
	 * @param rightProduct the rightProduct to set
	 */
	public void setRightProduct(RatioProduct rightProduct) {
		this.rightProduct = rightProduct;
	}

	/**
	 * @return the rightProduct
	 */
	public RatioProduct getRightProduct() {
		return rightProduct;
	}

	/**
	 * @param multiplicatorFactor the multiplicatorFactor to set
	 */
	public void setMultiplicatorFactor(double multiplicatorFactor) {
		this.multiplicatorFactor = multiplicatorFactor;
	}

	/**
	 * @return the multiplicatorFactor
	 */
	public double getMultiplicatorFactor() {
		return multiplicatorFactor;
	}
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method.
	 * 
	 * @param leftProduct	Ratio product from the left side of equation
	 * @param rightProduct	Ratio product from the right side of equation
	 * @param mulCoeff		Multiplication coefficient
	 */
	public EqualityOfRatioProducts(RatioProduct leftProduct, RatioProduct rightProduct, double mulCoeff) {
		Point tempP = null;
		
		if (leftProduct != null)
			this.leftProduct = leftProduct;
		else
			this.leftProduct = new RatioProduct();
		
		if (rightProduct != null)
			this.rightProduct = rightProduct;
		else
			this.rightProduct = new RatioProduct();
		
		this.multiplicatorFactor = mulCoeff;
		
		this.geoObjects = new Vector<GeoConstruction>();
	
		if (this.leftProduct.getRatios() != null) {
			for (RatioOfTwoCollinearSegments ratio : this.leftProduct.getRatios()) {
				tempP = ratio.getNumeratorSegment().getFirstEndPoint();
				if (this.geoObjects.indexOf(tempP) < 0)
					this.geoObjects.add(tempP);
				tempP = ratio.getNumeratorSegment().getSecondEndPoint();
				if (this.geoObjects.indexOf(tempP) < 0)
					this.geoObjects.add(tempP);
				tempP = ratio.getDenominatorSegment().getFirstEndPoint();
				if (this.geoObjects.indexOf(tempP) < 0)
					this.geoObjects.add(tempP);
				tempP = ratio.getDenominatorSegment().getSecondEndPoint();
				if (this.geoObjects.indexOf(tempP) < 0)
					this.geoObjects.add(tempP);
			}
		}
		
		if (this.rightProduct.getRatios() != null) {
			for (RatioOfTwoCollinearSegments ratio : this.rightProduct.getRatios()) {
				tempP = ratio.getNumeratorSegment().getFirstEndPoint();
				if (this.geoObjects.indexOf(tempP) < 0)
					this.geoObjects.add(tempP);
				tempP = ratio.getNumeratorSegment().getSecondEndPoint();
				if (this.geoObjects.indexOf(tempP) < 0)
					this.geoObjects.add(tempP);
				tempP = ratio.getDenominatorSegment().getFirstEndPoint();
				if (this.geoObjects.indexOf(tempP) < 0)
					this.geoObjects.add(tempP);
				tempP = ratio.getDenominatorSegment().getSecondEndPoint();
				if (this.geoObjects.indexOf(tempP) < 0)
					this.geoObjects.add(tempP);
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
		this.leftProduct.transformToAlgebraicForm();
		this.rightProduct.transformToAlgebraicForm();
		
		return (XPolynomial)this.leftProduct.getNumerator().clone().multiplyByPolynomial(this.rightProduct.getDenominator())
		                                                                                 .subtractPolynomial(this.rightProduct.getNumerator().clone().multiplyByPolynomial(this.leftProduct.getDenominator())
		                                                                		                                                                     .multiplyByRealConstant(this.multiplicatorFactor));
	}

	/**
	 * @see com.ogprover.pp.tp.thmstatement.ThmStatement#getStatementDesc()
	 */
	@Override
	public String getStatementDesc() {
		StringBuilder sb = new StringBuilder();
		sb.append("Ratio product ");
		boolean bFirst = true;
		for (RatioOfTwoCollinearSegments ratio : this.leftProduct.getRatios()) {
			if (!bFirst)
				sb.append("*");
			else
				bFirst = false;
			sb.append("(");
			sb.append(ratio.getNumeratorSegment().getDescription());
			sb.append("/");
			sb.append(ratio.getDenominatorSegment().getDescription());
			sb.append(")");
		}
		sb.append(" is equal to ");
		sb.append(this.multiplicatorFactor);
		if (this.rightProduct != null && this.rightProduct.getRatios() != null) {
			for (RatioOfTwoCollinearSegments ratio : this.rightProduct.getRatios()) {
				sb.append("*(");
				sb.append(ratio.getNumeratorSegment().getDescription());
				sb.append("/");
				sb.append(ratio.getDenominatorSegment().getDescription());
				sb.append(")");
			}
		}
		return sb.toString();
	}

	@Override
	public AreaMethodTheoremStatement getAreaMethodStatement() {
		// TODO Auto-generated method stub
		return null;
	}

}
