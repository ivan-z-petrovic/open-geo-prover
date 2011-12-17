/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.prover_protocol.cp.geoconstruction;

import java.io.IOException;
import java.util.Vector;

//import com.ogp.polynomials.XPolynomial;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.SymbolicPolynomial;
import com.ogprover.utilities.io.FileLogger;
import com.ogprover.utilities.io.OGPOutput;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for construction of circle through three points</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class CircumscribedCircle extends Circle {
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
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * Method that gives the type of this construction
	 * 
	 * @see com.ogprover.prover_protocol.cp.geoconstruction.GeoConstruction#getConstructionType()
	 */
	@Override
	public int getConstructionType() {
		return GeoConstruction.GEOCONS_TYPE_CIRCUMSCRIBED_CIRCLE;
	}
	
	/**
	 * Method that retrieves the condition for a point that belongs to this circle
	 * 
	 * @see com.ogprover.prover_protocol.cp.geoconstruction.Line#getCondition()
	 */
	public SymbolicPolynomial getCondition() {
		return conditionForCircumscribedCircle;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	public CircumscribedCircle(String circleLabel, Point pointA, Point pointB, Point pointC) {
		this.geoObjectLabel = circleLabel;
		this.points = new Vector<Point>();
		if (pointA != null)
			this.points.add(pointA); // add at the end - first point
		if (pointB != null)
			this.points.add(pointB); // add at the end - second point
		if (pointC != null)
			this.points.add(pointC); // add at the end - third point
		this.center = null;
	}
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method to check the validity of this construction step
	 * 
	 * @see com.ogprover.prover_protocol.cp.geoconstruction.GeoConstruction#isValidConstructionStep()
	 */
	@Override
	public boolean isValidConstructionStep() {
		OGPOutput output = OpenGeoProver.settings.getOutput();
		FileLogger logger = OpenGeoProver.settings.getLogger();
		
		if (!super.isValidConstructionStep())
			return false;
		
		try {
			// Construction of circumscribed circle is valid if all three points are previously constructed
			Point pointA = this.points.get(0);
			Point pointB = this.points.get(1);
			Point pointC = this.points.get(2);
			int indexA, indexB, indexC;
		
			if (pointA == null || pointB == null || pointC == null) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Circle " + this.getGeoObjectLabel() + " can't be constructed because some of necessary points is not constructed");
				return false;
			}
		
			indexA = pointA.getIndex();
			indexB = pointB.getIndex();
			indexC = pointC.getIndex();
		
			if (indexA < 0 || indexB < 0 || indexC < 0) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Circle " + this.getGeoObjectLabel() + " can't be constructed because some of necessary points is not added to construction protocol");
				return false; // some point is not in construction protocol
			}
		
			boolean valid = this.index > indexA && this.index > indexB && this.index > indexC;
			
			if (!valid) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Circle " + this.getGeoObjectLabel() + " can't be constructed because some of necessary points is not yet constructed");
				return false;
			}
			
			// finally check if points are collinear
			/* TODO 
			 * Doesn't work since points are not yet instantiated at this moment
			 * 
			 
			LineThroughTwoPoints tempLine = new LineThroughTwoPoints("tempLine", pointA, pointB);
			XPolynomial checkCollinearity = tempLine.instantiateConditionFromBasicElements(pointC);
			
			if (this.consProtocol.isPolynomialConsequenceOfConstructions(checkCollinearity)) {
				valid = false;
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Circle " + this.getGeoObjectLabel() + " can't be constructed because the three given points are collinear");
			}
			
			*
			*
			*/
			
			return valid;
		} catch (IOException e) {
			logger.error("Failed to write to output file(s).");
			output.close();
			return false;
		}
	}

	/**
	 * @see com.ogprover.prover_protocol.cp.geoconstruction.GeoConstruction#getConstructionDesc()
	 */
	@Override
	public String getConstructionDesc() {
		StringBuilder sb = new StringBuilder();
		sb.append("Circumscribed circle ");
		sb.append(this.geoObjectLabel);
		sb.append(" around triangle ");
		sb.append(this.points.get(0).getGeoObjectLabel());
		sb.append(this.points.get(1).getGeoObjectLabel());
		sb.append(this.points.get(2).getGeoObjectLabel());
		return sb.toString();
	}
}
