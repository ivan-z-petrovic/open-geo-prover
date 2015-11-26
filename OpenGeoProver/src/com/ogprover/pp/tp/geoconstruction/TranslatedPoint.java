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
* <dd>Class for translated point</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class TranslatedPoint extends SelfConditionalPoint {
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
	private static SymbolicPolynomial xConditionForTranslatedPoint = null;
	/**
	 * <i>Symbolic polynomial representing the condition for y coordinate</i>
	 */
	private static SymbolicPolynomial yConditionForTranslatedPoint = null;
	/**
	 * <i><b>Symbolic label for translated point</b></i>
	 */
	private static final String M0Label = "0";
	/**
	 * <i><b>Symbolic label for first point of vector</b></i>
	 */
	private static final String ALabel = "A";
	/**
	 * <i><b>Symbolic label for second point of vector</b></i>
	 */
	private static final String BLabel = "B";
	/**
	 * <i><b>Symbolic label for original point</b></i>
	 */
	private static final String CLabel = "C";
	/**
	 * First vector's point
	 */
	private Point pointA = null;
	/**
	 * Second vector's point
	 */
	private Point pointB = null;
	/**
	 * Original point
	 */
	private Point originalPoint = null;
	
	// Static initializer of condition members
	static {
		/*
		 * Vector AB has coordinates (xB - xA, yB - yA), hence M0 as translated point C
		 * has following coordinates M0 = (xC + xB - xA, yC + yB - yA)
		 * 
		 * condition for x coordinate is: x0 + xA - xB - xC = 0
		 * condition for y coordinate is: y0 + yA - yB - yC = 0
		 */
		if (xConditionForTranslatedPoint == null) {
			xConditionForTranslatedPoint = new SymbolicPolynomial();
			
			// Instances of symbolic variables
			SymbolicVariable x0 = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, M0Label);
			SymbolicVariable xA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, ALabel);
			SymbolicVariable xB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, BLabel);
			SymbolicVariable xC = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, CLabel);
			
			// term x0
			Term t = new SymbolicTerm(1);
			t.addPower(new Power(x0, 1));
			xConditionForTranslatedPoint.addTerm(t);
			
			// term xA
			t = new SymbolicTerm(1);
			t.addPower(new Power(xA, 1));
			xConditionForTranslatedPoint.addTerm(t);
			
			// term -xB
			t = new SymbolicTerm(-1);
			t.addPower(new Power(xB, 1));
			xConditionForTranslatedPoint.addTerm(t);
			
			// term -xC
			t = new SymbolicTerm(-1);
			t.addPower(new Power(xC, 1));
			xConditionForTranslatedPoint.addTerm(t);
		}
		
		if (yConditionForTranslatedPoint == null) {
			yConditionForTranslatedPoint = new SymbolicPolynomial();
			
			// Instances of symbolic variables
			SymbolicVariable y0 = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, M0Label);
			SymbolicVariable yA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, ALabel);
			SymbolicVariable yB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, BLabel);
			SymbolicVariable yC = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, CLabel);
			
			// term y0
			Term t = new SymbolicTerm(1);
			t.addPower(new Power(y0, 1));
			yConditionForTranslatedPoint.addTerm(t);
			
			// term yA
			t = new SymbolicTerm(1);
			t.addPower(new Power(yA, 1));
			yConditionForTranslatedPoint.addTerm(t);
			
			// term -yB
			t = new SymbolicTerm(-1);
			t.addPower(new Power(yB, 1));
			yConditionForTranslatedPoint.addTerm(t);
			
			// term -yC
			t = new SymbolicTerm(-1);
			t.addPower(new Power(yC, 1));
			yConditionForTranslatedPoint.addTerm(t);
		}
	}

	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * Method to set the first point of vector
	 * 
	 * @param pointA The first vector's point to set
	 */
	public void setPointA(Point pointA) {
		this.pointA = pointA;
	}

	/**
	 * Method that retrieves the first point of vector
	 * 
	 * @return The first vector's point
	 */
	public Point getPointA() {
		return pointA;
	}
	
	/**
	 * Method to set the second point of vector
	 * 
	 * @param pointB The second vector's point to set
	 */
	public void setPointB(Point pointB) {
		this.pointB = pointB;
	}

	/**
	 * Method that retrieves the second point of vector
	 * 
	 * @return The second vector's point
	 */
	public Point getPointB() {
		return pointB;
	}
	
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
	 * Method that retrieves the type of construction
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getConstructionType()
	 */
	@Override
	public int getConstructionType() {
		return GeoConstruction.GEOCONS_TYPE_TRANSLATED_POINT;
	}
	
	/**
	 * Method that gives the condition for x coordinate 
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.SelfConditionalPoint#getXCondition()
	 */
	@Override
	public SymbolicPolynomial getXCondition() {
		return xConditionForTranslatedPoint;
	}

	@Override
	/**
	 * Method that gives the condition for y coordinate 
	 * 
	 * @see com.ogp.pp.tp.geoconstruction.Point#getYCondition()
	 */
	public SymbolicPolynomial getYCondition() {
		return yConditionForTranslatedPoint;
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
	 * @param A				First vector's point
	 * @param B				Second vector's point
	 * @param originalPoint	Original point
	 */
	public TranslatedPoint(String pointLabel, Point A, Point B, Point originalPoint) {
		this.geoObjectLabel = pointLabel;
		this.pointA = A;
		this.pointB = B;
		this.originalPoint = originalPoint;
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
		Point p = new TranslatedPoint(this.geoObjectLabel, this.pointA, this.pointB, this.originalPoint);
		
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
			int indexA, indexB, indexOP;
		
			if (pointA == null || pointB == null || originalPoint == null) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Translated point " + this.getGeoObjectLabel() + " can't be constructed since one or two vector's points or original point are not constructed");
				return false;
			}
		
			indexA = pointA.getIndex();
			indexB = pointB.getIndex();
			indexOP = originalPoint.getIndex();
		
			if (indexA < 0 || indexB < 0 || indexOP < 0) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Translated point " + this.getGeoObjectLabel() + " can't be constructed since some of vector's points or original point is not added to theorem protocol");
				return false; // some point not in theorem protocol
			}
		
			boolean valid = this.index > indexA && this.index > indexB && this.index > indexOP;
			
			if (!valid) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Translated point " + this.getGeoObjectLabel() + " can't be constructed since some of vector's points or original point is not yet constructed");
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
		sb.append("Translated point ");
		sb.append(this.geoObjectLabel);
		sb.append(" of point ");
		sb.append(this.originalPoint.getGeoObjectLabel());
		sb.append(" for vector ");
		sb.append(this.pointA.getGeoObjectLabel());
		sb.append(this.pointB.getGeoObjectLabel());
		return sb.toString();
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getInputLabels()
	 */
	@Override
	public String[] getInputLabels() {
		String[] inputLabels = new String[3];
		inputLabels[0] = this.pointA.getGeoObjectLabel();
		inputLabels[1] = this.pointB.getGeoObjectLabel();
		inputLabels[2] = this.originalPoint.getGeoObjectLabel();
		return inputLabels;
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.SelfConditionalPoint#getPointsForInstantiation()
	 */
	@Override
	public Map<String, Point> getPointsForInstantiation() {
		Map<String, Point> pointsMap = new HashMap<String, Point>();
		pointsMap.put(M0Label, this);
		pointsMap.put(ALabel, this.pointA);
		pointsMap.put(BLabel, this.pointB);
		pointsMap.put(CLabel, this.originalPoint);
		return pointsMap;
	}

	@Override
	public Point replace(HashMap<Point, Point> replacementMap) {
		OpenGeoProver.settings.getLogger().error("This method should not be called on this class.");
		return null;
	}
}

