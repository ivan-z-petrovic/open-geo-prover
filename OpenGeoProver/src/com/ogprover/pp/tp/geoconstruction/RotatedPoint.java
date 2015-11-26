/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.geoconstruction;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.Power;
import com.ogprover.polynomials.SymbolicPolynomial;
import com.ogprover.polynomials.SymbolicTerm;
import com.ogprover.polynomials.SymbolicVariable;
import com.ogprover.polynomials.UXVariable;
import com.ogprover.polynomials.Variable;
import com.ogprover.utilities.io.OGPOutput;
import com.ogprover.utilities.logger.ILogger;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for rotated point</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class RotatedPoint extends SelfConditionalPoint {
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
	 * <i><b>Symbolic label for rotated point</b></i>
	 */
	private static final String M0Label = "0";
	/**
	 * <i><b>Symbolic label for original point</b></i>
	 */
	private static final String ALabel = "A";
	/**
	 * <i><b>Symbolic label for center of rotation</b></i>
	 */
	private static final String SLabel = "S";
	/**
	 * Original point
	 */
	private Point originalPoint = null;
	/**
	 * Center of rotation
	 */
	private Point centerOfRotation = null;
	/**
	 * Measure of angle of rotation in radians
	 */
	private double radAngleMeasure = 0.0;
	/**
	 * Measure of angle of rotation in degrees
	 */
	private double degAngleMeasure = 0.0;
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * Method to set the original point
	 * 
	 * @param originalPoint The original point to set
	 */
	public void setOriginalPoint(Point originalPoint) {
		this.originalPoint = originalPoint;
	}

	/**
	 * Method that retrieves the original point
	 * 
	 * @return The original point
	 */
	public Point getOriginalPoint() {
		return originalPoint;
	}
	
	/**
	 * Method to set the center of rotation
	 * 
	 * @param centerOfRotation The center of rotation to set
	 */
	public void setCenterOfRotation(Point centerOfRotation) {
		this.centerOfRotation = centerOfRotation;
	}

	/**
	 * Method that retrieves the center of rotation
	 * 
	 * @return The center of rotation
	 */
	public Point getCenterOfRotation() {
		return centerOfRotation;
	}
	
	/**
	 * @param radAngleMeasure the radAngleMeasure to set
	 */
	public void setRadAngleMeasure(double radAngleMeasure) {
		this.radAngleMeasure = radAngleMeasure;
	}

	/**
	 * @return the radAngleMeasure
	 */
	public double getRadAngleMeasure() {
		return radAngleMeasure;
	}
	
	/**
	 * @param degAngleMeasure the degAngleMeasure to set
	 */
	public void setDegAngleMeasure(double degAngleMeasure) {
		this.degAngleMeasure = degAngleMeasure;
	}

	/**
	 * @return the degAngleMeasure
	 */
	public double getDegAngleMeasure() {
		return degAngleMeasure;
	}

	/**
	 * Method that retrieves the type of construction
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getConstructionType()
	 */
	@Override
	public int getConstructionType() {
		return GeoConstruction.GEOCONS_TYPE_ROTATED_POINT;
	}
	
	// Methods for symbolic polynomials that represent the conditions by coordinates
	/*
	 * Let M0=(x0, y0) be the point that is rotated point A=(xA, yA) around
	 * central point S=(xS, yS) for angle whose measure is alpha.
	 * 
	 * To perform this rotation we have to:
	 * 	1. translate the center of rotation S to the origin of Cartesian coordinate system O
	 * 	2. perform the rotation around origin point
	 * 	3. translate the origin point back to S
	 * 
	 * Coordinates changes:
	 * 	1. translation by vector SO: (x, y) |--> (x - xS, y - yS)
	 * 	2. rotation around origin by angle alpha:
	 * 		original point (x, y) makes some angle alpha_0 with positive ray of x-axis and
	 * 		Cartesian coordinates satisfy equations:
	 * 			x = r * cos(alpha_0)
	 * 			y = r * sin(alpha_0), where r is distance between origin and original point
	 * 		After the rotation, new point makes angle (alpha + alpha_0) with positive ray 
	 * 		of x-axis, so new coordinates satisfy equations:
	 * 			x' = r * cos(alpha + alpha_0)
	 * 			y' = r * sin(alpha + alpha_0)
	 * 		we apply trigonometric addition formulae:
	 * 			x' = r * (cos(alpha)*cos(alpha_0) - sin(alpha)*sin(alpha_0))
	 * 			y' = r * (sin(alpha)*cos(alpha_0) + cos(alpha)*sin(alpha_0))
	 * 		so we get:
	 * 			x' = x*cos(alpha) - y*sin(alpha)
	 * 			y' = x*sin(alpha) + y*cos(alpha)
	 * 	3. translation by vector OS: (x, y) |--> (x + xS, y + yS)
	 * 
	 *  All in total:
	 *  		x' = (x - xS)*cos(alpha) - (y - yS)*sin(alpha) + xS
	 *  		y' = (x - xS)*sin(alpha) + (y - yS)*cos(alpha) + yS
	 *  
	 *  Finally, conditions for coordinates of point M0 are:
	 *  
	 *  		x0 = (xA - xS)*cos(alpha) - (yA - yS)*sin(alpha) + xS
	 *  		y0 = (xA - xS)*sin(alpha) + (yA - yS)*cos(alpha) + yS
	 */
	/**
	 * Method that gives the condition for x coordinate 
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.SelfConditionalPoint#getXCondition()
	 */
	@Override
	public SymbolicPolynomial getXCondition() {
		// x0 = (xA - xS)*cos(alpha) - (yA - yS)*sin(alpha) + xS
		// x0 - (xA - xS)*cos(alpha) + (yA - yS)*sin(alpha) - xS = 0
		SymbolicPolynomial xConditionForRotatedPoint = new SymbolicPolynomial();
		SymbolicVariable x0 = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, M0Label);
		SymbolicVariable xA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, ALabel);
		SymbolicVariable yA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, ALabel);
		SymbolicVariable xS = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, SLabel);
		SymbolicVariable yS = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, SLabel);
		
		SymbolicPolynomial m0Part = new SymbolicPolynomial();
		SymbolicPolynomial xPart = new SymbolicPolynomial();
		SymbolicPolynomial yPart = new SymbolicPolynomial();
		SymbolicPolynomial zPart = new SymbolicPolynomial();
		SymbolicTerm tempTerm = null;
		
		tempTerm = new SymbolicTerm(1);
		tempTerm.addPower(new Power(x0, 1));
		m0Part.addTerm(tempTerm);
		
		tempTerm = new SymbolicTerm(1);
		tempTerm.addPower(new Power(xA, 1));
		xPart.addTerm(tempTerm);
		tempTerm = new SymbolicTerm(-1);
		tempTerm.addPower(new Power(xS, 1));
		xPart.addTerm(tempTerm);
		xPart.multiplyByRealConstant(Math.cos(this.radAngleMeasure));
		
		tempTerm = new SymbolicTerm(1);
		tempTerm.addPower(new Power(yA, 1));
		yPart.addTerm(tempTerm);
		tempTerm = new SymbolicTerm(-1);
		tempTerm.addPower(new Power(yS, 1));
		yPart.addTerm(tempTerm);
		yPart.multiplyByRealConstant(Math.sin(this.radAngleMeasure));
		
		tempTerm = new SymbolicTerm(1);
		tempTerm.addPower(new Power(xS, 1));
		zPart.addTerm(tempTerm);
		
		xConditionForRotatedPoint.addPolynomial(m0Part);
		xConditionForRotatedPoint.subtractPolynomial(xPart);
		xConditionForRotatedPoint.addPolynomial(yPart);
		xConditionForRotatedPoint.subtractPolynomial(zPart);
		
		return xConditionForRotatedPoint;
	}

	@Override
	/**
	 * Method that gives the condition for y coordinate 
	 * 
	 * @see com.ogp.pp.tp.geoconstruction.Point#getYCondition()
	 */
	public SymbolicPolynomial getYCondition() {
		// y0 = (xA - xS)*sin(alpha) + (yA - yS)*cos(alpha) + yS
		// y0 - (xA - xS)*sin(alpha) - (yA - yS)*cos(alpha) - yS = 0
		SymbolicPolynomial yConditionForRotatedPoint = new SymbolicPolynomial();
		SymbolicVariable y0 = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, M0Label);
		SymbolicVariable xA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, ALabel);
		SymbolicVariable yA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, ALabel);
		SymbolicVariable xS = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, SLabel);
		SymbolicVariable yS = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, SLabel);
		
		SymbolicPolynomial m0Part = new SymbolicPolynomial();
		SymbolicPolynomial xPart = new SymbolicPolynomial();
		SymbolicPolynomial yPart = new SymbolicPolynomial();
		SymbolicPolynomial zPart = new SymbolicPolynomial();
		SymbolicTerm tempTerm = null;
		
		tempTerm = new SymbolicTerm(1);
		tempTerm.addPower(new Power(y0, 1));
		m0Part.addTerm(tempTerm);
		
		tempTerm = new SymbolicTerm(1);
		tempTerm.addPower(new Power(xA, 1));
		xPart.addTerm(tempTerm);
		tempTerm = new SymbolicTerm(-1);
		tempTerm.addPower(new Power(xS, 1));
		xPart.addTerm(tempTerm);
		xPart.multiplyByRealConstant(Math.sin(this.radAngleMeasure));
		
		tempTerm = new SymbolicTerm(1);
		tempTerm.addPower(new Power(yA, 1));
		yPart.addTerm(tempTerm);
		tempTerm = new SymbolicTerm(-1);
		tempTerm.addPower(new Power(yS, 1));
		yPart.addTerm(tempTerm);
		yPart.multiplyByRealConstant(Math.cos(this.radAngleMeasure));
		
		tempTerm = new SymbolicTerm(1);
		tempTerm.addPower(new Power(yS, 1));
		zPart.addTerm(tempTerm);
		
		yConditionForRotatedPoint.addPolynomial(m0Part);
		yConditionForRotatedPoint.subtractPolynomial(xPart);
		yConditionForRotatedPoint.subtractPolynomial(yPart);
		yConditionForRotatedPoint.subtractPolynomial(zPart);
		
		return yConditionForRotatedPoint;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * 
	 * @param pointLabel	Label of this point
	 * @param originalPoint	Original point
	 * @param center		Center of rotation
	 * @param degAngMeasure	Measure of angle of rotation in degrees (+/-)
	 */
	public RotatedPoint(String pointLabel, Point originalPoint, Point center, double degAngMeasure) {
		this.geoObjectLabel = pointLabel;
		this.originalPoint = originalPoint;
		this.centerOfRotation = center;
		this.degAngleMeasure = degAngMeasure;
		this.radAngleMeasure = this.degAngleMeasure * Math.PI / 180.0; // angle in radians
	}
	
	
	
	/*
	 * ======================================================================
	 * ======================= COMMON OBJECT METHODS ========================
	 * ======================================================================
	 */
	/**
	 * Clone method
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.Point#clone()
	 */
	@Override
	public Point clone() {
		Point p = new RotatedPoint(this.geoObjectLabel, this.originalPoint, this.centerOfRotation, this.degAngleMeasure);
		
		if (this.getX() != null)
			p.setX((UXVariable) this.getX().clone());
		if (this.getY() != null)
			p.setY((UXVariable) this.getY().clone());
		p.setInstanceType(this.instanceType);
		p.setPointState(this.pointState);
		p.setConsProtocol(this.consProtocol);
		p.setIndex(this.index);
		
		return p;
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
			int indexOP, indexC;
		
			if (this.originalPoint == null || this.centerOfRotation == null) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Rotated point " + this.getGeoObjectLabel() + " can't be constructed since original point or center of rotation are not constructed");
				return false;
			}

			indexOP = this.originalPoint.getIndex();
			indexC = this.centerOfRotation.getIndex();
		
			if (indexOP < 0 || indexC < 0) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Rotated point " + this.getGeoObjectLabel() + " can't be constructed since original point or center of rotation are not added to theorem protocol");
				return false; // some point not in theorem protocol
			}
		
			boolean valid = this.index > indexOP && this.index > indexC;
			
			if (!valid) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Rotated point " + this.getGeoObjectLabel() + " can't be constructed since original point or center of rotation are not yet constructed");
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
		sb.append("Rotated point ");
		sb.append(this.geoObjectLabel);
		sb.append(" of point ");
		sb.append(this.originalPoint.getGeoObjectLabel());
		sb.append(" around point ");
		sb.append(this.centerOfRotation.getGeoObjectLabel());
		sb.append(" for angle of ");
		sb.append(this.degAngleMeasure);
		sb.append(" degrees");
		return sb.toString();
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getInputLabels()
	 */
	@Override
	public String[] getInputLabels() {
		String[] inputLabels = new String[2];
		inputLabels[0] = this.originalPoint.getGeoObjectLabel();
		inputLabels[1] = this.centerOfRotation.getGeoObjectLabel();
		return inputLabels;
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.SelfConditionalPoint#getPointsForInstantiation()
	 */
	@Override
	public Map<String, Point> getPointsForInstantiation() {
		Map<String, Point> pointsMap = new HashMap<String, Point>();
		pointsMap.put(M0Label, this);
		pointsMap.put(ALabel, this.originalPoint);
		pointsMap.put(SLabel, this.centerOfRotation);
		return pointsMap;
	}

	@Override
	public Point replace(HashMap<Point, Point> replacementMap) {
		OpenGeoProver.settings.getLogger().error("This method should not be called on this class.");
		return null;
	}
}

