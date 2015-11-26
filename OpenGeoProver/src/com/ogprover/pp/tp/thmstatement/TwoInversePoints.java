/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.thmstatement;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.ogprover.main.OGPConstants;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.Power;
import com.ogprover.polynomials.SymbolicPolynomial;
import com.ogprover.polynomials.SymbolicTerm;
import com.ogprover.polynomials.SymbolicVariable;
import com.ogprover.polynomials.Term;
import com.ogprover.polynomials.Variable;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.geoconstruction.Circle;
import com.ogprover.pp.tp.geoconstruction.GeoConstruction;
import com.ogprover.pp.tp.geoconstruction.IntersectionPoint;
import com.ogprover.pp.tp.geoconstruction.Line;
import com.ogprover.pp.tp.geoconstruction.LineThroughTwoPoints;
import com.ogprover.pp.tp.geoconstruction.Point;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for statement about two inverse points.</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class TwoInversePoints extends PositionThmStatement {
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
	 * <i><b>Symbolic label for first point of pair</b></i>
	 */
	private static final String ALabel = "A";
	/**
	 * <i><b>Symbolic label for second point of pair</b></i>
	 */
	private static final String BLabel = "B";
	/**
	 * <i><b>Symbolic label for center of circle of inversion</b></i>
	 */
	private static final String OLabel = "O";
	/**
	 * <i><b>
	 * Symbolic label for intersection point of circle of 
	 * inversion and secant through two given points A and B
	 * </b></i>
	 */
	private static final String SLabel = "S";
	/**
	 * Intersection point of circle of inversion and 
	 * secant through two given points A and B
	 */
	private Point intersectionPointS = null;
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @return the intersectionPointS
	 */
	public Point getIntersectionPointS() {
		return intersectionPointS;
	}
	
	/**
	 * @param intersectionPointS the intersectionPointS to set
	 */
	public void setIntersectionPointS(Point intersectionPointS) {
		this.intersectionPointS = intersectionPointS;
	}




	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method.
	 * 
	 * @param firstPoint	First point
	 * @param secondPoint	Second point
	 * @param circle		Circle of inversion
	 */
	public TwoInversePoints(Point firstPoint, Point secondPoint, Circle circle) {
		this.geoObjects = new Vector<GeoConstruction>();
		this.geoObjects.add(firstPoint);
		this.geoObjects.add(secondPoint);
		// points are equally used - so first one is not more important than second one
		this.geoObjects.add(circle);
	}
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/*
	 * Points A and B are inverse points with respect to circle k whose center is O.
	 * Points A, B and O must be collinear and let S be a intersection point of circle
	 * k and ray OA. Then following equation is satisfied:
	 * 
	 * vector(OA)/vector(OS) = vector(OS)/vector(OB)
	 * 
	 * vector_x(OA)*vector_x(OB) - vector_x(OS)^2 = 0
	 * or
	 * vector_y(OA)*vector_y(OB) - vector_y(OS)^2 = 0
	 */
	
	/**
	 * Method that gives condition for x coordinates of given points
	 * so that they are inverse points with respect to given circle.
	 * 
	 * @return	Symbolic polynomial representing the condition for inverse 
	 * 			points expressed by x coordinates
	 */
	private SymbolicPolynomial getXCondition() {
		/*
		 * (xA - xO)*(xB - xO) - (xS - xO)^2 = 0
		 * xA*xB - xA*xO - xB*xO - xS^2 + 2*xS*xO = 0
		 */
		
		SymbolicPolynomial xCondition = new SymbolicPolynomial();
		
		// Instances of symbolic variables
		SymbolicVariable xA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, ALabel);
		SymbolicVariable xB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, BLabel);
		SymbolicVariable xO = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, OLabel);
		SymbolicVariable xS = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, SLabel);
			
		// term xA*xB
		Term t = new SymbolicTerm(1);
		t.addPower(new Power(xA, 1));
		t.addPower(new Power(xB, 1));
		xCondition.addTerm(t);
		
		// term -xA*xO
		t = new SymbolicTerm(-1);
		t.addPower(new Power(xA, 1));
		t.addPower(new Power(xO, 1));
		xCondition.addTerm(t);
		
		// term -xB*xO
		t = new SymbolicTerm(-1);
		t.addPower(new Power(xB, 1));
		t.addPower(new Power(xO, 1));
		xCondition.addTerm(t);
		
		// term -xS^2
		t = new SymbolicTerm(-1);
		t.addPower(new Power(xS, 2));
		xCondition.addTerm(t);
		
		// term 2*xS*xO
		t = new SymbolicTerm(2);
		t.addPower(new Power(xS, 1));
		t.addPower(new Power(xO, 1));
		xCondition.addTerm(t);
		
		return xCondition;
	}
	
	/**
	 * Method that gives condition for y coordinates of given points
	 * so that they are inverse points with respect to given circle.
	 * 
	 * @return	Symbolic polynomial representing the condition for inverse 
	 * 			points expressed by y coordinates
	 */
	private SymbolicPolynomial getYCondition() {
		/*
		 * (yA - yO)*(yB - yO) - (yS - yO)^2 = 0
		 * yA*yB - yA*yO - yB*yO - yS^2 + 2*yS*yO = 0
		 */
		
		SymbolicPolynomial yCondition = new SymbolicPolynomial();
		
		// Instances of symbolic variables
		SymbolicVariable yA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, ALabel);
		SymbolicVariable yB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, BLabel);
		SymbolicVariable yO = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, OLabel);
		SymbolicVariable yS = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, SLabel);
			
		// term yA*yB
		Term t = new SymbolicTerm(1);
		t.addPower(new Power(yA, 1));
		t.addPower(new Power(yB, 1));
		yCondition.addTerm(t);
		
		// term -yA*yO
		t = new SymbolicTerm(-1);
		t.addPower(new Power(yA, 1));
		t.addPower(new Power(yO, 1));
		yCondition.addTerm(t);
		
		// term -yB*yO
		t = new SymbolicTerm(-1);
		t.addPower(new Power(yB, 1));
		t.addPower(new Power(yO, 1));
		yCondition.addTerm(t);
		
		// term -yS^2
		t = new SymbolicTerm(-1);
		t.addPower(new Power(yS, 2));
		yCondition.addTerm(t);
		
		// term 2*yS*yO
		t = new SymbolicTerm(2);
		t.addPower(new Power(yS, 1));
		t.addPower(new Power(yO, 1));
		yCondition.addTerm(t);
		
		return yCondition;
	}
	
	/**
	 * @see com.ogprover.pp.tp.thmstatement.ElementaryThmStatement#isValid()
	 */
	@Override
	public boolean isValid() {
		// First of all call method from superclass
		if (!super.isValid())
			return false;
		
		// There must be two points and one circle
		if (this.geoObjects.size() < 3) {
			OpenGeoProver.settings.getLogger().error("There must be two points and one circle.");
			return false;
		}
		
		// Circle must have constructed center
		if (((Circle)this.getGeoObjects().get(2)).getCenter() == null) {
			OpenGeoProver.settings.getLogger().error("Circle must have constructed center.");
			return false;
		}
		
		// Two given points and center of circle of inversion must be collinear 
		// (that is responsibility of user who performs the construction).
		
		return true;
	}
	
	/**
	 * Method that sets the intersection point of inversion circle and secant through 
	 * two given points A and B.
	 * 
	 * @return	SUCCESS if successful or general error code
	 */
	private int setIntersectionPoint() {
		Point A = (Point)this.getGeoObjects().get(0);
		Point B = (Point)this.getGeoObjects().get(1);
		// circle of inversion and its center
		Circle k = (Circle)this.getGeoObjects().get(2);
		Point O = (Point)k.getCenter();
		
		if (this.intersectionPointS == null) {
			/*
			 *  determine intersection point S
			 */
			// first of all find line AB
			Line AB = null;
			boolean isExisting = false;
		
			for (GeoConstruction geoCons : this.consProtocol.getConstructionSteps()) {
				if (geoCons instanceof Line) {
					Vector<Point> linePoints = ((Line)geoCons).getPoints();
				
					if (linePoints.indexOf(A) >= 0 && linePoints.indexOf(B) >= 0) {
						AB = (Line)geoCons;
						isExisting = true;
						break;
					}
				}
			}
		
			if (AB == null) {
				AB = new LineThroughTwoPoints("SecantOfInversePoints", A, B);
				this.consProtocol.addGeoConstruction(AB);
				if (AB.isValidConstructionStep() == false) {
					OpenGeoProver.settings.getLogger().error("Failed to validate the construction of secant of inverse points.");
					return OGPConstants.ERR_CODE_GENERAL;
				}
				AB.addPointToSet(O); // points A, B and O are collinear
			}
		
			// then determine common point of line AB and circle of inversion, k
			if (isExisting) {
				Vector<Point> kPoints = k.getPoints();
			
				for (Point P : AB.getPoints()) {
					if (kPoints.indexOf(P) >= 0) {
						this.intersectionPointS = P;
						break;
					}
				}
			}
		
			if (this.intersectionPointS == null) {
				this.intersectionPointS = new IntersectionPoint("tempPoint-S", AB, k);
				this.consProtocol.addGeoConstruction(this.intersectionPointS);
				if (this.intersectionPointS.isValidConstructionStep() == false) {
					OpenGeoProver.settings.getLogger().error("Failed to validate the construction of intersection point of circle and line through two given points.");
					return OGPConstants.ERR_CODE_GENERAL;
				}
				
				if (((IntersectionPoint) this.intersectionPointS).transformToAlgebraicFormWithOutputPrintFlag(false) != OGPConstants.RET_CODE_SUCCESS)
					return OGPConstants.ERR_CODE_GENERAL;
			}
		}
		
		return OGPConstants.RET_CODE_SUCCESS;
	}
	
	/**
	 * Method that retrieves condition for this statement expressed by x-coordinates.
	 * 
	 * @return	Polynomial for this statement
	 */
	public XPolynomial getXAlgebraicForm() {
		if (this.setIntersectionPoint() != OGPConstants.RET_CODE_SUCCESS)
			return null;
		
		// Points O, A and B must be collinear and that is the responsibility of user
		Map<String, Point> pointsMap = new HashMap<String, Point>();
		pointsMap.put(ALabel, (Point)this.getGeoObjects().get(0));
		pointsMap.put(BLabel, (Point)this.getGeoObjects().get(1));
		pointsMap.put(OLabel, (Point)((Circle)this.getGeoObjects().get(2)).getCenter());
		pointsMap.put(SLabel, this.intersectionPointS);
		
		return OGPTP.instantiateCondition(this.getXCondition(), pointsMap);
	}
	
	/**
	 * Method that retrieves condition for this statement expressed by y-coordinates.
	 * 
	 * @return	Polynomial for this statement
	 */
	public XPolynomial getYAlgebraicForm() {
		if (this.setIntersectionPoint() != OGPConstants.RET_CODE_SUCCESS)
			return null;
		
		// Points O, A and B must be collinear and that is the responsibility of user
		Map<String, Point> pointsMap = new HashMap<String, Point>();
		pointsMap.put(ALabel, (Point)this.getGeoObjects().get(0));
		pointsMap.put(BLabel, (Point)this.getGeoObjects().get(1));
		pointsMap.put(OLabel, (Point)((Circle)this.getGeoObjects().get(2)).getCenter());
		pointsMap.put(SLabel, this.intersectionPointS);
		
		return OGPTP.instantiateCondition(this.getYCondition(), pointsMap);
	}
	
	/**
	 * @see com.ogprover.pp.tp.thmstatement.ThmStatement#getAlgebraicForm()
	 */
	@Override
	public XPolynomial getAlgebraicForm() {
		// Points A and B are equally used (they are of same importance) - this can be seen from symbolic conditions as well
		Point A = (Point)this.getGeoObjects().get(0);
		Point B = (Point)this.getGeoObjects().get(1);
		Variable xA = A.getX();
		Variable xB = B.getX();
		
		if (xA.getVariableType() == xB.getVariableType() && xA.getIndex() == xB.getIndex()) {
			// AB is perpendicular to x-axis - use y coordinates
			return this.getYAlgebraicForm();
		}
		
		// use x coordinates
		return this.getXAlgebraicForm();
	}

	/**
	 * @see com.ogprover.pp.tp.thmstatement.ThmStatement#getStatementDesc()
	 */
	@Override
	public String getStatementDesc() {
		StringBuilder sb = new StringBuilder();
		sb.append("Point ");
		sb.append(this.geoObjects.get(0).getGeoObjectLabel());
		sb.append(" is inverse to point ");
		sb.append(this.geoObjects.get(1).getGeoObjectLabel());
		sb.append(" with respect to circle ");
		sb.append(this.geoObjects.get(2).getGeoObjectLabel());
		return sb.toString();
	}

	@Override
	public AreaMethodTheoremStatement getAreaMethodStatement() {
		// The area method cannot deal with such statement.
		OpenGeoProver.settings.getLogger().error("The area method cannot deal with circle inversion.");
		return null;
	}
}
