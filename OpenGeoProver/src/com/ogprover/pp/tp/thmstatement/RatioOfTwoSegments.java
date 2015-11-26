/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.thmstatement;

import java.util.Vector;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.pp.tp.geoconstruction.GeoConstruction;
import com.ogprover.pp.tp.geoobject.Segment;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for statement about ratio of two segments.</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class RatioOfTwoSegments extends DimensionThmStatement {
	// Segments need not be collinear (on same or parallel lines).
	
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
	 * First segment
	 */
	private Segment firstSegment = null;
	/**
	 * Second segment
	 */
	private Segment secondSegment = null;
	/**
	 * Ratio coefficient - determines quantity of ratio of two segments
	 */
	private double ratioCoefficient = 0.0;
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @param firstSegment the firstSegment to set
	 */
	public void setFirstSegment(Segment firstSegment) {
		this.firstSegment = firstSegment;
	}

	/**
	 * @return the firstSegment
	 */
	public Segment getFirstSegment() {
		return firstSegment;
	}

	/**
	 * @param secondSegment the secondSegment to set
	 */
	public void setSecondSegment(Segment secondSegment) {
		this.secondSegment = secondSegment;
	}

	/**
	 * @return the secondSegment
	 */
	public Segment getSecondSegment() {
		return secondSegment;
	}

	/**
	 * @param ratioCoefficient the ratioCoefficient to set
	 */
	public void setRatioCoefficient(double ratioCoefficient) {
		this.ratioCoefficient = ratioCoefficient;
	}

	/**
	 * @return the ratioCoefficient
	 */
	public double getRatioCoefficient() {
		return ratioCoefficient;
	}

	
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method.
	 * 
	 * @param firstSegment	First segment
	 * @param secondSegment	Second segment
	 * @param ratioCoeff	Ratio coefficient
	 */
	public RatioOfTwoSegments(Segment firstSegment, Segment secondSegment, double ratioCoeff) {
		this.geoObjects = new Vector<GeoConstruction>();
		this.geoObjects.add(firstSegment.getFirstEndPoint());
		this.geoObjects.add(firstSegment.getSecondEndPoint());
		this.geoObjects.add(secondSegment.getFirstEndPoint());
		this.geoObjects.add(secondSegment.getSecondEndPoint());
		
		this.firstSegment = firstSegment;
		this.secondSegment = secondSegment;
		this.ratioCoefficient = ratioCoeff;
	}
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * @see com.ogprover.pp.tp.thmstatement.ElementaryThmStatement#isValid()
	 */
	@Override
	public boolean isValid() {
		// First of all call method from superclass
		if (!super.isValid())
			return false;
		
		// There must be four points
		if (this.geoObjects.size() < 4) {
			OpenGeoProver.settings.getLogger().error("There must be 4 points.");
			return false;
		}
		
		return true;
	}
	
	/**
	 * @see com.ogprover.pp.tp.thmstatement.ThmStatement#getAlgebraicForm()
	 */
	@Override
	public XPolynomial getAlgebraicForm() {
		/*
		 * AB/CD = k ==> AB^2 - k^2*CD^2 = 0
		 */
		return (XPolynomial) this.firstSegment.getInstantiatedConditionForSquareOfSegment()
				                                                    .subtractPolynomial(this.secondSegment.getInstantiatedConditionForSquareOfSegment()
				                                                                                          .multiplyByRealConstant(this.ratioCoefficient * this.ratioCoefficient));
	}
	
	/**
	 * @see com.ogprover.pp.tp.thmstatement.ThmStatement#getStatementDesc()
	 */
	@Override
	public String getStatementDesc() {
		StringBuilder sb = new StringBuilder();
		sb.append("Ratio of two segments ");
		sb.append(this.firstSegment.getDescription());
		sb.append("/");
		sb.append(this.secondSegment.getDescription());
		sb.append(" equals ");
		sb.append(this.ratioCoefficient);
		return sb.toString();
	}

	@Override
	public AreaMethodTheoremStatement getAreaMethodStatement() {
		OpenGeoProver.settings.getLogger().error("The area method does not currently use floating-point calculus.");
		return null;
	}
}
