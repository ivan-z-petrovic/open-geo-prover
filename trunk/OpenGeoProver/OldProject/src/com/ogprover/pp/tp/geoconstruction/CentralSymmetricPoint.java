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
import com.ogprover.polynomials.Term;
import com.ogprover.polynomials.UXVariable;
import com.ogprover.polynomials.Variable;
import com.ogprover.utilities.io.OGPOutput;
import com.ogprover.utilities.logger.ILogger;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for central symmetric</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class CentralSymmetricPoint extends SelfConditionalPoint {
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
	 * <i>Symbolic polynomial representing the condition for x coordinate</i>
	 */
	private static SymbolicPolynomial xConditionForCentralSymmetricPoint = null;
	/**
	 * <i>Symbolic polynomial representing the condition for y coordinate</i>
	 */
	private static SymbolicPolynomial yConditionForCentralSymmetricPoint = null;
	/**
	 * <i><b>Symbolic label for central symmetric point</b></i>
	 */
	private static final String M0Label = "0";
	/**
	 * <i><b>Symbolic label for original point</b></i>
	 */
	private static final String ALabel = "A";
	/**
	 * <i><b>Symbolic label for center of symmetry</b></i>
	 */
	private static final String SLabel = "S";
	/**
	 * Original point
	 */
	private Point originalPoint = null;
	/**
	 * Center of symmetry
	 */
	private Point center = null;
	
	// Static initializer of condition members
	static {
		/*
		 * x coordinate of central point is the arithmetic mean of x coordinates of 
		 * original and symmetric points:
		 * 		xS = (xA + x0)/2 which gives polynomial 2xS - xA - x0 = 0
		 */
		if (xConditionForCentralSymmetricPoint == null) {
			xConditionForCentralSymmetricPoint = new SymbolicPolynomial();
			
			// Instances of symbolic variables
			SymbolicVariable x0 = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, M0Label);
			SymbolicVariable xA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, ALabel);
			SymbolicVariable xS = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, SLabel);
			
			// term 2*xS
			Term t = new SymbolicTerm(2);
			t.addPower(new Power(xS, 1));
			xConditionForCentralSymmetricPoint.addTerm(t);
			
			// term -xA
			t = new SymbolicTerm(-1);
			t.addPower(new Power(xA, 1));
			xConditionForCentralSymmetricPoint.addTerm(t);
			
			// term -x0
			t = new SymbolicTerm(-1);
			t.addPower(new Power(x0, 1));
			xConditionForCentralSymmetricPoint.addTerm(t);
		}
		
		/*
		 * y coordinate of central point is the arithmetic mean of y coordinates of 
		 * original and symmetric points:
		 * 		yS = (yA + y0)/2 which gives polynomial 2yS - yA - y0 = 0
		 */
		if (yConditionForCentralSymmetricPoint == null) {
			yConditionForCentralSymmetricPoint = new SymbolicPolynomial();
			
			// Instances of symbolic variables
			SymbolicVariable y0 = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, M0Label);
			SymbolicVariable yA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, ALabel);
			SymbolicVariable yS = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, SLabel);
			
			// term 2*yS
			Term t = new SymbolicTerm(2);
			t.addPower(new Power(yS, 1));
			yConditionForCentralSymmetricPoint.addTerm(t);
			
			// term -yA
			t = new SymbolicTerm(-1);
			t.addPower(new Power(yA, 1));
			yConditionForCentralSymmetricPoint.addTerm(t);
			
			// term -y0
			t = new SymbolicTerm(-1);
			t.addPower(new Power(y0, 1));
			yConditionForCentralSymmetricPoint.addTerm(t);
		}
	}

	
	
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
	 * Method to set the center of symmetry
	 * 
	 * @param center The center of symmetry to set
	 */
	public void setCenter(Point center) {
		this.center = center;
	}

	/**
	 * Method that retrieves the center of symmetry
	 * 
	 * @return The center of symmetry
	 */
	public Point getCenter() {
		return center;
	}
	
	/**
	 * Method that retrieves the type of construction
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getConstructionType()
	 */
	@Override
	public int getConstructionType() {
		return GeoConstruction.GEOCONS_TYPE_CENTAL_SYMMETRIC_POINT;
	}
	
	/**
	 * Method that gives the condition for x coordinate 
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.SelfConditionalPoint#getXCondition()
	 */
	@Override
	public SymbolicPolynomial getXCondition() {
		return xConditionForCentralSymmetricPoint;
	}

	@Override
	/**
	 * Method that gives the condition for y coordinate 
	 * 
	 * @see com.ogp.pp.tp.geoconstruction.Point#getYCondition()
	 */
	public SymbolicPolynomial getYCondition() {
		return yConditionForCentralSymmetricPoint;
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
	 * @param center		Center of symmetry
	 */
	public CentralSymmetricPoint(String pointLabel, Point originalPoint, Point center) {
		this.geoObjectLabel = pointLabel;
		this.originalPoint = originalPoint;
		this.center = center;
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
		Point p = new CentralSymmetricPoint(this.geoObjectLabel, this.originalPoint, this.center);
		
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
		
			if (originalPoint == null || center == null) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Central symmetric point " + this.getGeoObjectLabel() + " can't be constructed since original point or center is not constructed");
				return false;
			}
		
			indexOP = originalPoint.getIndex();
			indexC = center.getIndex();
		
			if (indexOP < 0 || indexC < 0) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Central symmetric point " + this.getGeoObjectLabel() + " can't be constructed since original pont or center is not added to theorem protocol");
				return false; // some point not in theorem protocol
			}
		
			boolean valid = this.index > indexOP && this.index > indexC;
			
			if (!valid) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Central symmetric point " + this.getGeoObjectLabel() + " can't be constructed since original pont or center is not yet constructed");
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
		sb.append("Cental symmetric point ");
		sb.append(this.geoObjectLabel);
		sb.append(" of point ");
		sb.append(this.originalPoint.getGeoObjectLabel());
		sb.append(" with respect to center of symmetry ");
		sb.append(this.center.getGeoObjectLabel());
		return sb.toString();
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getInputLabels()
	 */
	@Override
	public String[] getInputLabels() {
		String[] inputLabels = new String[2];
		inputLabels[0] = this.originalPoint.getGeoObjectLabel();
		inputLabels[1] = this.center.getGeoObjectLabel();
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
		pointsMap.put(SLabel, this.center);
		return pointsMap;
	}

	@Override
	public Point replace(HashMap<Point, Point> replacementMap) {
		OpenGeoProver.settings.getLogger().error("This method should not be called on this class.");
		return null;
	}
}

