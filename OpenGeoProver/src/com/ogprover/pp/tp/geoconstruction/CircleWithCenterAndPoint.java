/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.geoconstruction;

import java.io.IOException;
import java.util.Vector;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.SymbolicPolynomial;
import com.ogprover.utilities.io.OGPOutput;
import com.ogprover.utilities.logger.ILogger;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for construction of circle with given center and one point</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class CircleWithCenterAndPoint extends Circle {
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
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getConstructionType()
	 */
	@Override
	public int getConstructionType() {
		return GeoConstruction.GEOCONS_TYPE_CIRCLE_WITH_CENTER_AND_POINT;
	}
	
	/**
	 * Method that retrieves the condition for a point that belongs to this circle
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.Line#getCondition()
	 */
	public SymbolicPolynomial getCondition() {
		return conditionForCircleWithCenterAndPoint;
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
	 * @param pointA		Point from circle
	 */
	public CircleWithCenterAndPoint(String circleLabel, Point center, Point pointA) {
		this.geoObjectLabel = circleLabel;
		this.points = new Vector<Point>();
		if (pointA != null)
			this.points.add(pointA); // add at the end - first point
		if (center != null)
			this.center = center;
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
			// Construction of plain circle is valid if both its center and given point are previously constructed
			Point pointA = this.points.get(0);
			Point center = this.center;
			int indexA, indexCenter;
		
			if (pointA == null || center == null) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Circle " + this.getGeoObjectLabel() + " can't be constructed because some of necessary points is not constructed");
				return false;
			}
		
			indexA = pointA.getIndex();
			indexCenter = center.getIndex();
		
			if (indexA < 0 || indexCenter < 0) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Circle " + this.getGeoObjectLabel() + " can't be constructed because some of necessary points is not added to theorem protocol");
				return false; // some point is not in theorem protocol
			}
		
			boolean valid = this.index > indexA && this.index > indexCenter;
			
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
		sb.append(" and one point ");
		sb.append(this.points.get(0).getGeoObjectLabel());
		return sb.toString();
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getInputLabels()
	 */
	@Override
	public String[] getInputLabels() {
		String[] inputLabels = new String[2];
		inputLabels[0] = this.center.getGeoObjectLabel();
		inputLabels[1] = this.points.get(0).getGeoObjectLabel();
		return inputLabels;
	}
}
