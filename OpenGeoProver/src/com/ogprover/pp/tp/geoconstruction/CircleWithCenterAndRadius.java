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
* <dd>Class for construction of circle with given center and 
*     radius (represented by segment)</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class CircleWithCenterAndRadius extends Circle {
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
	 * Segment congruent to radius of circle
	 */
	private Segment radius = null;
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @param radius the radius to set
	 */
	public void setRadius(Segment radius) {
		this.radius = radius;
	}

	/**
	 * @return the radius
	 */
	public Segment getRadius() {
		return radius;
	}
	
	/**
	 * Method that gives the type of this construction
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getConstructionType()
	 */
	@Override
	public int getConstructionType() {
		return GeoConstruction.GEOCONS_TYPE_CIRCLE_WITH_CENTER_AND_RADIUS;
	}
	
	/**
	 * Method that retrieves the condition for a point that belongs to this circle
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.Line#getCondition()
	 */
	public SymbolicPolynomial getCondition() {
		return conditionForCircleWithCenterAndRadius;
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
	 * @param center		Circle center
	 * @param pointA		First end point of radius
	 * @param pointB		Second end point of radius
	 */
	public CircleWithCenterAndRadius(String circleLabel, Point center, Point pointA, Point pointB) {
		this.geoObjectLabel = circleLabel;
		this.points = new Vector<Point>();
		if (pointA != null && pointB != null)
			this.radius = new Segment(pointA, pointB);
		if (center != null)
			this.center = center;
		
		// if any of segment's end points is equals with center of circle, the second one 
		// is on circle
		if (pointA != null && this.center != null && pointA.getGeoObjectLabel().equals(this.center.getGeoObjectLabel()))
			this.points.add(pointB);
		if (pointB != null && this.center != null && pointB.getGeoObjectLabel().equals(this.center.getGeoObjectLabel()))
			this.points.add(pointA);
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
			// Construction of this circle is valid if its center and given points from radius are previously constructed
			Point pointA = this.radius.getFirstEndPoint();
			Point pointB = this.radius.getSecondEndPoint();
			Point center = this.center;
			int indexA, indexB, indexCenter;
		
			if (pointA == null || pointB == null || center == null) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Circle " + this.getGeoObjectLabel() + " can't be constructed because some of necessary points is not constructed");
				return false;
			}
		
			indexA = pointA.getIndex();
			indexB = pointB.getIndex();
			indexCenter = center.getIndex();
		
			if (indexA < 0 || indexB < 0 || indexCenter < 0) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Circle " + this.getGeoObjectLabel() + " can't be constructed because some of necessary points is not added to theorem protocol");
				return false; // some point is not in theorem protocol
			}
		
			boolean valid = this.index > indexA && this.index > indexB && this.index > indexCenter;
			
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
		sb.append(" with center ");
		sb.append(this.center.getGeoObjectLabel());
		sb.append(" and radius ");
		sb.append(this.radius.getDescription());
		return sb.toString();
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getInputLabels()
	 */
	@Override
	public String[] getInputLabels() {
		String[] inputLabels = new String[3];
		inputLabels[0] = this.center.getGeoObjectLabel();
		inputLabels[1] = this.radius.getFirstEndPoint().getGeoObjectLabel();
		inputLabels[2] = this.radius.getSecondEndPoint().getGeoObjectLabel();
		return inputLabels;
	}
	
}
