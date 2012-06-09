/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.geoconstruction;

import java.io.IOException;
import java.util.Vector;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.SymbolicPolynomial;
import com.ogprover.pp.tp.geoobject.Segment;
import com.ogprover.utilities.io.OGPOutput;
import com.ogprover.utilities.logger.ILogger;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for construction of circle with given 
*     diameter (represented by segment)</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class CircleWithDiameter extends Circle {
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
	 * Segment congruent to diameter of circle
	 */
	private Segment diameter = null;
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @param diameter the diameter to set
	 */
	public void setDiameter(Segment diameter) {
		this.diameter = diameter;
	}

	/**
	 * @return the diameter
	 */
	public Segment getDiameter() {
		return diameter;
	}
	
	/**
	 * Method that gives the type of this construction
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getConstructionType()
	 */
	@Override
	public int getConstructionType() {
		return GeoConstruction.GEOCONS_TYPE_CIRCLE_WITH_DIAMETER;
	}
	
	/**
	 * Method that retrieves the condition for a point that belongs to this circle
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.Line#getCondition()
	 */
	public SymbolicPolynomial getCondition() {
		return conditionForCircleWithDiameter;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * 
	 * @param circleLabel	Circle label
	 * @param pointA		First end point of diameter
	 * @param pointB		Second end point of diameter
	 */
	public CircleWithDiameter(String circleLabel, Point pointA, Point pointB) {
		this.geoObjectLabel = circleLabel;
		this.points = new Vector<Point>();
		if (pointA != null && pointB != null) {
			this.diameter = new Segment(pointA, pointB);
			this.points.add(pointA); // pointA belongs to circle
			this.points.add(pointB); // pointB belongs to circle
		}
	}
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method to check the validity of this construction step
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#isValidConstructionStep()
	 */
	@Override
	public boolean isValidConstructionStep() {
		OGPOutput output = OpenGeoProver.settings.getOutput();
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (!super.isValidConstructionStep())
			return false;
		
		try {
			// Construction of this circle is valid if its given points from diameter are previously constructed
			Point pointA = this.diameter.getFirstEndPoint();
			Point pointB = this.diameter.getSecondEndPoint();
			int indexA, indexB;
		
			if (pointA == null || pointB == null) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Circle " + this.getGeoObjectLabel() + " can't be constructed because some of necessary points is not constructed");
				return false;
			}
		
			indexA = pointA.getIndex();
			indexB = pointB.getIndex();
		
			if (indexA < 0 || indexB < 0) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Circle " + this.getGeoObjectLabel() + " can't be constructed because some of necessary points is not added to theorem protocol");
				return false; // some point is not in theorem protocol
			}
		
			boolean valid = this.index > indexA && this.index > indexB;
			
			if (!valid) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Circle " + this.getGeoObjectLabel() + " can't be constructed because some of necessary points is not yet constructed");
			}
			
			return valid;
		} catch (IOException e) {
			logger.error("Failed to write to output file(s).");
			output.close();
			return false;
		}
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getConstructionDesc()
	 */
	@Override
	public String getConstructionDesc() {
		StringBuilder sb = new StringBuilder();
		sb.append("Circle ");
		sb.append(this.geoObjectLabel);
		sb.append(" with diameter ");
		sb.append(this.diameter.getDescription());
		return sb.toString();
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getInputLabels()
	 */
	@Override
	public String[] getInputLabels() {
		String[] inputLabels = new String[2];
		inputLabels[0] = this.diameter.getFirstEndPoint().getGeoObjectLabel();
		inputLabels[1] = this.diameter.getSecondEndPoint().getGeoObjectLabel();
		return inputLabels;
	}
	
}
