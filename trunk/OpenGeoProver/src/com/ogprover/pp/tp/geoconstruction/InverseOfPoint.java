/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.geoconstruction;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

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
* <dd>Class for inverse of point with respect to given circle</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class InverseOfPoint extends SelfConditionalPoint {
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
	 * <i>
	 * Symbolic polynomial representing the condition for x coordinate
	 * when line is not vertical to x-axis
	 * </i>
	 */
	private static SymbolicPolynomial xConditionForInverseOfPointGeneral = null;
	/**
	 * <i>
	 * Symbolic polynomial representing the condition for y coordinate
	 * when line is not vertical to y-axis
	 * </i>
	 */
	private static SymbolicPolynomial yConditionForInverseOfPointGeneral = null;
	/**
	 * <i><b>Symbolic label for inverse of given point</b></i>
	 */
	private static final String M0Label = "0"; // zero
	/**
	 * <i><b>Symbolic label for given point</b></i>
	 */
	private static final String ALabel = "A";
	/**
	 * <i><b>Symbolic label for center of circle</b></i>
	 */
	private static final String OLabel = "O"; // letter "O"
	/**
	 * <i><b>
	 * Symbolic label for intersection point of 
	 * circle of inversion with ray from center towards
	 * original point
	 * </b></i>
	 */
	private static final String BLabel = "B";
	/**
	 * Original point
	 */
	private Point originalPoint = null;
	/**
	 * Circle of inversion
	 */
	private Circle circleOfInversion = null;
	/**
	 * Intersection point of circle and line through center and original point
	 */
	private Point secantPoint = null;
	
	// Static initializer of condition members
	static {
		/*
		 * If A is outside circle k (whose center is O),
		 * then let T be the touch point of tangent from A to k and then M0 is the foot
		 * of perpendicular line from T to secant OA. Then M0 is inverse of point A with
		 * respect to circle k.
		 * 
		 * Another case: let A is inside circle k and let T be intersection point of 
		 * line, perpendicular to secant OA in point A, with circle k. Then let M0 be the
		 * intersection point of tangent line on k in T with secant OA. Again M0 is inverse
		 * of point A with respect to circle k.
		 * 
		 * In both cases following triangles are similar: OAT ~ OTM0
		 * therefore:
		 * 
		 * OA/OT = OT/OM0
		 * 
		 * Instead of OT we can use radius OB of circle k where B is on secant OA
		 * and therefore we can use vectors:
		 * 
		 * vector(OA)/vector(OB) - vector(OB)/vector(OM0) = 0
		 * 
		 * Points O, A, B and M0 must be collinear. 
		 * 
		 * For collinear points A, B and C
		 * it is satisfied:
		 * 
		 * vector(AC)/vector(CB) = (xC - xA)/(xB - xC) if line AB is not perpendicular to x-axis
		 * or
		 * vector(AC)/vector(CB) = (yC - yA)/(yB - yC) if line AB is not perpendicular to y-axis
		 * 
		 */
		
		/*
		 * General condition for x-coordinate of inverse of point A 
		 * with respect to circle with center O can be applied
		 * if line OA is not perpendicular to x-axis:
		 * 
		 * vector(OA)/vector(OB) - vector(OB)/vector(OM0) = 0
		 * (xA - xO)/(xB - xO) - (xB - xO)/(x0 - xO) = 0
		 * 
		 * after multiplying by denominators this becomes:
		 * (xA - xO)(x0 - xO) - (xB - xO)^2 = 0
		 * xA*x0 - xA*xO - xO*x0 + xO^2 - xB^2 + 2xB*xO - xO^2 = 0
		 * and finally:
		 * 
		 * xA*x0 - xO*x0 - xA*xO - xB^2 + 2xB*xO = 0
		 */
		if (xConditionForInverseOfPointGeneral == null) {
			xConditionForInverseOfPointGeneral = new SymbolicPolynomial();
			
			// Instances of symbolic variables
			SymbolicVariable x0 = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, M0Label);
			SymbolicVariable xO = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, OLabel);
			SymbolicVariable xA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, ALabel);
			SymbolicVariable xB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, BLabel);
			
			// term xA*x0
			Term t = new SymbolicTerm(1);
			t.addPower(new Power(xA, 1));
			t.addPower(new Power(x0, 1));
			xConditionForInverseOfPointGeneral.addTerm(t);
			
			// term -xO*x0
			t = new SymbolicTerm(-1);
			t.addPower(new Power(xO, 1));
			t.addPower(new Power(x0, 1));
			xConditionForInverseOfPointGeneral.addTerm(t);
			
			// term -xA*xO
			t = new SymbolicTerm(-1);
			t.addPower(new Power(xA, 1));
			t.addPower(new Power(xO, 1));
			xConditionForInverseOfPointGeneral.addTerm(t);
			
			// term -xB^2
			t = new SymbolicTerm(-1);
			t.addPower(new Power(xB, 2));
			xConditionForInverseOfPointGeneral.addTerm(t);
			
			// term 2xB*xO
			t = new SymbolicTerm(2);
			t.addPower(new Power(xB, 1));
			t.addPower(new Power(xO, 1));
			xConditionForInverseOfPointGeneral.addTerm(t);
		}
		
		/*
		 * General condition for y-coordinate of inverse of point A 
		 * with respect to circle with center O can be applied
		 * if line OA is not perpendicular to y-axis:
		 * 
		 * vector(OA)/vector(OB) - vector(OB)/vector(OM0) = 0
		 * (yA - yO)/(yB - yO) - (yB - yO)/(y0 - yO) = 0
		 * 
		 * after multiplying by denominators this becomes:
		 * (yA - yO)(y0 - yO) - (yB - yO)^2 = 0
		 * yA*y0 - yA*yO - yO*y0 + yO^2 - yB^2 + 2yB*yO - yO^2 = 0
		 * and finally:
		 * 
		 * yA*y0 - yO*y0 - yA*yO - yB^2 + 2yB*yO = 0
		 */
		if (yConditionForInverseOfPointGeneral == null) {
			yConditionForInverseOfPointGeneral = new SymbolicPolynomial();
			
			// Instances of symbolic variables
			SymbolicVariable y0 = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, M0Label);
			SymbolicVariable yO = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, OLabel);
			SymbolicVariable yA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, ALabel);
			SymbolicVariable yB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, BLabel);
			
			// term yA*y0
			Term t = new SymbolicTerm(1);
			t.addPower(new Power(yA, 1));
			t.addPower(new Power(y0, 1));
			yConditionForInverseOfPointGeneral.addTerm(t);
			
			// term -yO*y0
			t = new SymbolicTerm(-1);
			t.addPower(new Power(yO, 1));
			t.addPower(new Power(y0, 1));
			yConditionForInverseOfPointGeneral.addTerm(t);
			
			// term -yA*yO
			t = new SymbolicTerm(-1);
			t.addPower(new Power(yA, 1));
			t.addPower(new Power(yO, 1));
			yConditionForInverseOfPointGeneral.addTerm(t);
			
			// term -yB^2
			t = new SymbolicTerm(-1);
			t.addPower(new Power(yB, 2));
			yConditionForInverseOfPointGeneral.addTerm(t);
			
			// term 2yB*yO
			t = new SymbolicTerm(2);
			t.addPower(new Power(yB, 1));
			t.addPower(new Power(yO, 1));
			yConditionForInverseOfPointGeneral.addTerm(t);
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
	 * @param origP The original point to set
	 */
	public void setOriginalPoint(Point origP) {
		this.originalPoint = origP;
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
	 * Method to set the circle of inversion
	 * 
	 * @param circle The circle of inversion to set
	 */
	public void setCircleOfInversion(Circle circle) {
		this.circleOfInversion = circle;
	}

	/**
	 * Method that retrieves the circle of inversion
	 * 
	 * @return The circle of inversion
	 */
	public Circle getCircleOfInversion() {
		return circleOfInversion;
	}
	
	/**
	 * Method to set the intersection point
	 * of circle and line through center and original point
	 * 
	 * @param intersectP The intersection point to set
	 */
	public void setSecantPoint(Point intersectP) {
		this.secantPoint = intersectP;
	}

	/**
	 * Method that retrieves the intersection point
	 * of circle and line through center and original point
	 * 
	 * @return The intersection point
	 */
	public Point getSecantPoint() {
		return secantPoint;
	}
	
	/**
	 * Method that retrieves the type of construction
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getConstructionType()
	 */
	@Override
	public int getConstructionType() {
		return GeoConstruction.GEOCONS_TYPE_INVERSE_POINT;
	}
	
	/**
	 * Method that gives the condition for x coordinate 
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.SelfConditionalPoint#getXCondition()
	 */
	@Override
	public SymbolicPolynomial getXCondition() {
		/*
		 * The condition for x-coordinate depends on current instances of
		 * points O - center of circle, and A - original point. 
		 * If points O and A have same instances
		 * for x-coordinate that means the line OA is perpendicular to x-axis
		 * and therefore inverse point must have same x-coordinate.
		 * Otherwise (general case) we apply general condition for x-coordinate
		 * of inverse point.
		 */
		// center must exist at this moment since validation would fail otherwise
		if (this.circleOfInversion.getCenter().getX().equals(this.originalPoint.getX())) {
			SymbolicPolynomial xCond = new SymbolicPolynomial();
			
			// we retrieve condition "xA - x0 = 0"
			Term t = new SymbolicTerm(1);
			t.addPower(new Power(new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, ALabel), 1));
			xCond.addTerm(t);
			t = new SymbolicTerm(-1);
			t.addPower(new Power(new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, M0Label), 1));
			xCond.addTerm(t);
			return xCond;
		}
		
		return xConditionForInverseOfPointGeneral;
	}

	@Override
	/**
	 * Method that gives the condition for y coordinate 
	 * 
	 * @see com.ogp.pp.tp.geoconstruction.Point#getYCondition()
	 */
	public SymbolicPolynomial getYCondition() {
		/*
		 * The condition for y-coordinate depends on current instances of
		 * points O - center of circle, and A - original point. 
		 * If points O and A have same instances
		 * for y-coordinate that means the line OA is perpendicular to y-axis
		 * and therefore inverse point must have same y-coordinate.
		 * Otherwise (general case) we apply general condition for y-coordinate
		 * of inverse point.
		 */
		// center must exist at this moment since validation would fail otherwise
		if (this.circleOfInversion.getCenter().getY().equals(this.originalPoint.getY())) {
			SymbolicPolynomial yCond = new SymbolicPolynomial();
			
			// we retrieve condition "yA - y0 = 0"
			Term t = new SymbolicTerm(1);
			t.addPower(new Power(new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, ALabel), 1));
			yCond.addTerm(t);
			t = new SymbolicTerm(-1);
			t.addPower(new Power(new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, M0Label), 1));
			yCond.addTerm(t);
			return yCond;
		}
		
		return yConditionForInverseOfPointGeneral;
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
	 * @param A				Original point
	 * @param invC			Circle of inversion
	 */
	public InverseOfPoint(String pointLabel, Point A, Circle invC) {
		this.geoObjectLabel = pointLabel;
		this.originalPoint = A;
		this.circleOfInversion = invC;
		// secant point will be set in validation method since that method
		// deals with theorem protocol
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
		Point p = new InverseOfPoint(this.geoObjectLabel, this.originalPoint, this.circleOfInversion);
		
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
			int indexA, indexC;
		
			if (this.originalPoint == null || this.circleOfInversion == null) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Inverse point " + this.getGeoObjectLabel() + " can't be constructed since original point or circle of inversion are not constructed");
				return false;
			}
		
			indexA = this.originalPoint.getIndex();
			indexC = this.circleOfInversion.getIndex();
		
			if (indexA < 0 || indexC < 0) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Inverse point " + this.getGeoObjectLabel() + " can't be constructed since original point or circle of inversion are not added to theorem protocol");
				return false; // some point not in theorem protocol
			}
			
			if (this.index <= indexA || this.index <= indexC) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Inverse point " + this.getGeoObjectLabel() + " can't be constructed since original point or circle of inversion are not yet constructed");
				return false;
			}
			
			// Now check the center of circle - it must exist and must be
			// constructed before this point
			Point centerP = this.circleOfInversion.getCenter();
			
			if (centerP == null || centerP.getIndex() < 0 || centerP.getIndex() >= this.index) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Inverse point " + this.getGeoObjectLabel() + " can't be constructed since circle of inversion still doesn't have constructed center");
				return false;
			}
			
			// And finally provide secant point
			
			/* 
			 * First of all check if secant point is already constructed - pass all lines
			 * that pass through center and original point and for each check if it has
			 * common point with circle of inversion.
			 */
			Vector<Point> circlePoints = this.circleOfInversion.getPoints();
			for (GeoConstruction geoCons : this.consProtocol.getConstructionSteps()) {
				if (geoCons instanceof Line) {
					Line l = (Line)geoCons;
					Vector<Point> lPoints = l.getPoints();
					
					if (lPoints.indexOf(centerP) > 0 && lPoints.indexOf(this.originalPoint) > 0) {
						for (Point p : lPoints) {
							if (p.getIndex() < this.index && circlePoints.indexOf(p) > 0) {
								this.secantPoint = p;
								break;
							}
						}
					}
				}
			}
			// if secant point not found among constructed points, we have to construct it now
			if (this.secantPoint == null) {
				Line secantLine = new LineThroughTwoPoints("secantLine" + Math.round(Math.random()*1000), this.circleOfInversion.getCenter(), this.originalPoint);
				Point secantP = new IntersectionPoint("secantPoint" + Math.round(Math.random()*1000), this.circleOfInversion, secantLine);
				
				// put these two constructions in CP right before construction of this inverse point
				this.consProtocol.addGeoConstruction(this.getIndex(), secantLine);
				if (!secantLine.isValidConstructionStep()) {
					output.openItemWithDesc("Error: ");
					output.closeItemWithDesc("Inverse point " + this.getGeoObjectLabel() + " can't be constructed since construction of secant point is invalid");
					return false;
				}
				this.consProtocol.addGeoConstruction(secantLine.getIndex() + 1, secantP);
				if (!secantP.isValidConstructionStep()) {
					output.openItemWithDesc("Error: ");
					output.closeItemWithDesc("Inverse point " + this.getGeoObjectLabel() + " can't be constructed since construction of secant point is invalid");
					return false;
				}
				
				this.secantPoint = secantP;
			}
			
			return true;
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
		sb.append("Inverse point ");
		sb.append(this.geoObjectLabel);
		sb.append(" of point ");
		sb.append(this.originalPoint.getGeoObjectLabel());
		sb.append(" with respect to circle ");
		sb.append(this.circleOfInversion.getGeoObjectLabel());
		return sb.toString();
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getInputLabels()
	 */
	@Override
	public String[] getInputLabels() {
		String[] inputLabels = new String[2];
		inputLabels[0] = this.originalPoint.getGeoObjectLabel();
		inputLabels[1] = this.circleOfInversion.getGeoObjectLabel();
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
		pointsMap.put(BLabel, this.secantPoint);
		pointsMap.put(OLabel, this.circleOfInversion.getCenter());
		return pointsMap;
	}

	@Override
	public Point replace(HashMap<Point, Point> replacementMap) {
		OpenGeoProver.settings.getLogger().error("This method should not be called on this class.");
		return null;
	}
}

