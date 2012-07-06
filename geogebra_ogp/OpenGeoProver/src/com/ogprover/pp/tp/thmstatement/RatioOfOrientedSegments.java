/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.thmstatement;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.Power;
import com.ogprover.polynomials.SymbolicPolynomial;
import com.ogprover.polynomials.SymbolicTerm;
import com.ogprover.polynomials.SymbolicVariable;
import com.ogprover.polynomials.Term;
import com.ogprover.polynomials.Variable;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.geoconstruction.GeoConstruction;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.ogprover.pp.tp.geoobject.Segment;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for statement about ratio of oriented segments.</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class RatioOfOrientedSegments extends DimensionThmStatement {
	// Segments are considered collinear i.e. they lie on same line or on parallel lines.
	
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
	 * <i><b>Symbolic label for first point of first line segment</b></i>
	 */
	private static final String ALabel = "A";
	/**
	 * <i><b>Symbolic label for second point of first line segment</b></i>
	 */
	private static final String BLabel = "B";
	/**
	 * <i><b>Symbolic label for first point of second line segment</b></i>
	 */
	private static final String CLabel = "C";
	/**
	 * <i><b>Symbolic label for second point of second line segment</b></i>
	 */
	private static final String DLabel = "D";
	
	/**
	 * First oriented segment
	 */
	private Segment firstSegment = null;
	/**
	 * Second oriented segment
	 */
	private Segment secondSegment = null;
	/**
	 * Ratio coefficient - determines quantity of ratio of two segments
	 */
	private double ratioCoefficient = 0.0;
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @param firstSegment the firstSegment to set
	 */
	public void setFirstSegment(Segment firstSegment) {
		this.firstSegment = firstSegment;
	}

	/**
	 * @return the firstSegment
	 */
	public Segment getFirstSegment() {
		return firstSegment;
	}

	/**
	 * @param secondSegment the secondSegment to set
	 */
	public void setSecondSegment(Segment secondSegment) {
		this.secondSegment = secondSegment;
	}

	/**
	 * @return the secondSegment
	 */
	public Segment getSecondSegment() {
		return secondSegment;
	}

	/**
	 * @param ratioCoefficient the ratioCoefficient to set
	 */
	public void setRatioCoefficient(double ratioCoefficient) {
		this.ratioCoefficient = ratioCoefficient;
	}

	/**
	 * @return the ratioCoefficient
	 */
	public double getRatioCoefficient() {
		return ratioCoefficient;
	}

	
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method.
	 * 
	 * @param firstSegment	First segment
	 * @param secondSegment	Second segment
	 * @param ratioCoeff	Ratio coefficient
	 */
	public RatioOfOrientedSegments(Segment firstSegment, Segment secondSegment, double ratioCoeff) {
		this.geoObjects = new Vector<GeoConstruction>();
		this.geoObjects.add(firstSegment.getFirstEndPoint());
		this.geoObjects.add(firstSegment.getSecondEndPoint());
		this.geoObjects.add(secondSegment.getFirstEndPoint());
		this.geoObjects.add(secondSegment.getSecondEndPoint());
		
		this.firstSegment = firstSegment;
		this.secondSegment = secondSegment;
		this.ratioCoefficient = ratioCoeff;
	}
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/*
	 * vector(AB) = k * vector(CD) ==> (xB - xA, yB - yA) = k * (xD - xC, yD - yC)
	 */
	
	/**
	 * Method that gives condition for x coordinates of segments' end points
	 * so that given ratio is satisfied.
	 * 
	 * @return	Symbolic polynomial representing the condition for ratio 
	 * 			expressed by x coordinates
	 */
	private SymbolicPolynomial getXCondition() {
		// xA - xB - k*xC + k*xD = 0
		
		SymbolicPolynomial xCondition = new SymbolicPolynomial();
		
		// Instances of symbolic variables
		SymbolicVariable xA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, ALabel);
		SymbolicVariable xB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, BLabel);
		SymbolicVariable xC = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, CLabel);
		SymbolicVariable xD = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, DLabel);
			
		// term xA
		Term t = new SymbolicTerm(1);
		t.addPower(new Power(xA, 1));
		xCondition.addTerm(t);
		
		// term -xB
		t = new SymbolicTerm(-1);
		t.addPower(new Power(xB, 1));
		xCondition.addTerm(t);
		
		// term -k*xC
		t = new SymbolicTerm(-this.ratioCoefficient);
		t.addPower(new Power(xC, 1));
		xCondition.addTerm(t);
		
		// term k*xD
		t = new SymbolicTerm(this.ratioCoefficient);
		t.addPower(new Power(xD, 1));
		xCondition.addTerm(t);
		
		return xCondition;
	}
	
	/**
	 * Method that gives condition for y coordinates of segments' end points
	 * so that given ratio is satisfied.
	 * 
	 * @return	Symbolic polynomial representing the condition for ratio 
	 * 			expressed by y coordinates
	 */
	private SymbolicPolynomial getYCondition() {
		// yA - yB - k*yC + k*yD = 0
		
		SymbolicPolynomial yCondition = new SymbolicPolynomial();
		
		// Instances of symbolic variables
		SymbolicVariable yA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, ALabel);
		SymbolicVariable yB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, BLabel);
		SymbolicVariable yC = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, CLabel);
		SymbolicVariable yD = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, DLabel);
			
		// term yA
		Term t = new SymbolicTerm(1);
		t.addPower(new Power(yA, 1));
		yCondition.addTerm(t);
		
		// term -yB
		t = new SymbolicTerm(-1);
		t.addPower(new Power(yB, 1));
		yCondition.addTerm(t);
		
		// term -k*yC
		t = new SymbolicTerm(-this.ratioCoefficient);
		t.addPower(new Power(yC, 1));
		yCondition.addTerm(t);
		
		// term k*yD
		t = new SymbolicTerm(this.ratioCoefficient);
		t.addPower(new Power(yD, 1));
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
		
		// There must be four points
		if (this.geoObjects.size() < 4) {
			OpenGeoProver.settings.getLogger().error("There must be 4 points.");
			return false;
		}
		
		// Position of segments with regards to x and y axis cannot be examined now
		// since points are still not instantiated.
		
		return true;
	}
	
	/**
	 * Method that retrieves the polynomial form of this statement expressed by x-coordinates.
	 * 
	 * @return	Polynomial form of this statement
	 */
	public XPolynomial getXAlgebraicForm() {
		Map<String, Point> pointsMap = new HashMap<String, Point>();
		pointsMap.put(ALabel, (Point) this.geoObjects.get(0));
		pointsMap.put(BLabel, (Point) this.geoObjects.get(1));
		pointsMap.put(CLabel, (Point) this.geoObjects.get(2));
		pointsMap.put(DLabel, (Point) this.geoObjects.get(3));
		
		return OGPTP.instantiateCondition(this.getXCondition(), pointsMap);
	}
	
	/**
	 * Method that retrieves the polynomial form of this statement expressed by y-coordinates.
	 * 
	 * @return	Polynomial form of this statement
	 */
	public XPolynomial getYAlgebraicForm() {
		Map<String, Point> pointsMap = new HashMap<String, Point>();
		pointsMap.put(ALabel, (Point) this.geoObjects.get(0));
		pointsMap.put(BLabel, (Point) this.geoObjects.get(1));
		pointsMap.put(CLabel, (Point) this.geoObjects.get(2));
		pointsMap.put(DLabel, (Point) this.geoObjects.get(3));
		
		return OGPTP.instantiateCondition(this.getYCondition(), pointsMap);
	}
	
	/**
	 * @see com.ogprover.pp.tp.thmstatement.ThmStatement#getAlgebraicForm()
	 */
	@Override
	public XPolynomial getAlgebraicForm() {
		// Post-validation - after points are instantiated position of segments can be tested
		/*
		 *  It will not be checked whether segments are collinear 
		 *  (it will be the responsibility of user) but if one is 
		 *  perpendicular to x/y axis and another is not.
		 */
		Variable xA = this.firstSegment.getFirstEndPoint().getX();
		Variable xB = this.firstSegment.getSecondEndPoint().getX();
		Variable xC = this.secondSegment.getFirstEndPoint().getX();
		Variable xD = this.secondSegment.getSecondEndPoint().getX();
		Variable yA = this.firstSegment.getFirstEndPoint().getY();
		Variable yB = this.firstSegment.getSecondEndPoint().getY();
		Variable yC = this.secondSegment.getFirstEndPoint().getY();
		Variable yD = this.secondSegment.getSecondEndPoint().getY();
		
		if (xA.getVariableType() == xB.getVariableType() && xA.getIndex() == xB.getIndex()) {
			// AB is perpendicular to x-axis
			
			if (xC.getVariableType() != xD.getVariableType() || xC.getIndex() != xD.getIndex()) {
				OpenGeoProver.settings.getLogger().error("Second segment is not perpendicular to x-axis while first is - they have to be collinear segments.");
				return null;
			}	
		}
		else if (xC.getVariableType() == xD.getVariableType() && xC.getIndex() == xD.getIndex()) {
			// CD is perpendicular to x-axis
			
			OpenGeoProver.settings.getLogger().error("First segment is not perpendicular to x-axis while second is - they have to be collinear segments.");
			return null;
		}
		
		if (yA.getVariableType() == yB.getVariableType() && yA.getIndex() == yB.getIndex()) {
			// AB is perpendicular to y-axis
			
			if (yC.getVariableType() != yD.getVariableType() || yC.getIndex() != yD.getIndex()) {
				OpenGeoProver.settings.getLogger().error("Second segment is not perpendicular to y-axis while first is - they have to be collinear segments.");
				return null;
			}	
		}
		else if (yC.getVariableType() == yD.getVariableType() && yC.getIndex() == yD.getIndex()) {
			// CD is perpendicular to y-axis
			
			OpenGeoProver.settings.getLogger().error("First segment is not perpendicular to y-axis while second is - they have to be collinear segments.");
			return null;
		}
		
		if (xA.getVariableType() == xB.getVariableType() && xA.getIndex() == xB.getIndex()) {
			// AB is perpendicular to x-axis (then CD is perpendicular as well or validation would fail)
			// - use y coordinates
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
		sb.append("Ratio of oriented segments ");
		sb.append(this.firstSegment.getDescription());
		sb.append("/");
		sb.append(this.secondSegment.getDescription());
		sb.append(" equals ");
		sb.append(this.ratioCoefficient);
		return sb.toString();
	}

	@Override
	public AreaMethodTheoremStatement getAreaMethodStatement() {
		OpenGeoProver.settings.getLogger().error("The area method does not currently use floating-point calculus.");
		return null;
	}
}
