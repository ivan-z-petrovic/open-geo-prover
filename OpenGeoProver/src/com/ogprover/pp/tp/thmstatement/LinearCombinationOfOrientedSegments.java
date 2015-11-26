/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.thmstatement;

import java.util.Vector;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.Variable;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.pp.tp.geoconstruction.GeoConstruction;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.ogprover.pp.tp.geoobject.Segment;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for statement about linear combination of oriented segments.</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class LinearCombinationOfOrientedSegments extends DimensionThmStatement {
	// k1*A1B1 + k2*A2B2 + ... + kn*AnBn = 0; 
	// segments are collinear (lie on same or parallel lines)
	
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
	 * Segments of linear combination
	 */
	private Vector<Segment> segments = null;
	/**
	 * Coefficients of linear combination
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
	public void setSegments(Vector<Segment> segments) {
		this.segments = segments;
	}

	/**
	 * @return the segments
	 */
	public Vector<Segment> getSegments() {
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
	public LinearCombinationOfOrientedSegments(Vector<Segment> segments, Vector<Double> coefficients) {
		Point tempP = null;
		
		this.segments = segments;
		this.coefficients = coefficients;
		
		this.geoObjects = new Vector<GeoConstruction>();
		
		if (this.segments != null) {
			for (Segment seg : this.segments) {
				tempP = seg.getFirstEndPoint();
				if (this.geoObjects.indexOf(tempP) < 0)
					this.geoObjects.add(tempP);
				
				tempP = seg.getSecondEndPoint();
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
	 * @see com.ogprover.pp.tp.thmstatement.ElementaryThmStatement#isValid()
	 */
	@Override
	public boolean isValid() {
		// First of all call method from superclass
		if (!super.isValid())
			return false;
		
		// There must be equal number of segments and coefficients
		if (this.segments == null || this.coefficients == null || this.segments.size() != this.coefficients.size()) {
			OpenGeoProver.settings.getLogger().error("There must be equal number of segments and coefficients.");
			return false;
		}
		
		// Segments must be collinear and that is the responsibility of user 
		// who instantiates the object of this class
		
		return true;
	}
	
	/**
	 * Method that gives algebraic form of this statement expressed by x-coordinates.
	 * 
	 * @return	Polynomial for this statement
	 */
	public XPolynomial getXAlgebraicForm() {
		XPolynomial result = new XPolynomial();
		
		for (int ii = 0, jj = this.segments.size(); ii < jj; ii++) {
			Segment currSeg = this.segments.get(ii);
			double currCoeff = this.coefficients.get(ii).doubleValue();
			
			result.addPolynomial(currSeg.getInstantiatedXCoordinateOfOrientedSegment().multiplyByRealConstant(currCoeff));
		}
		
		return result;
	}
	
	/**
	 * Method that gives algebraic form of this statement expressed by y-coordinates.
	 * 
	 * @return	Polynomial for this statement
	 */
	public XPolynomial getYAlgebraicForm() {
		XPolynomial result = new XPolynomial();
		
		for (int ii = 0, jj = this.segments.size(); ii < jj; ii++) {
			Segment currSeg = this.segments.get(ii);
			double currCoeff = this.coefficients.get(ii).doubleValue();
			
			result.addPolynomial(currSeg.getInstantiatedYCoordinateOfOrientedSegment().multiplyByRealConstant(currCoeff));
		}
		
		return result;
	}
	
	/**
	 * @see com.ogprover.pp.tp.thmstatement.ThmStatement#getAlgebraicForm()
	 */
	@Override
	public XPolynomial getAlgebraicForm() {
		// Check whether segments are perpendicular to x-axis by checking only first segment
		Segment seg = this.segments.get(0);
		Point A = seg.getFirstEndPoint();
		Point B = seg.getSecondEndPoint();
		Variable xA = A.getX();
		Variable xB = B.getX();
		
		if (xA.getVariableType() == xB.getVariableType() && xA.getIndex() == xB.getIndex()) {
			// Segment AB is perpendicular to x-axis, use y coordinates
			return this.getYAlgebraicForm();
		}
		
		return this.getXAlgebraicForm();
	}

	/**
	 * @see com.ogprover.pp.tp.thmstatement.ThmStatement#getStatementDesc()
	 */
	@Override
	public String getStatementDesc() {
		StringBuilder sb = new StringBuilder();
		sb.append("Linear combination of oriented segments: ");
		for (int ii = 0; ii < this.coefficients.size(); ii++) {
			if (ii > 0)
				sb.append("+");
			sb.append(this.coefficients.get(ii));
			sb.append("*");
			sb.append(this.segments.get(ii).getDescription());
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
