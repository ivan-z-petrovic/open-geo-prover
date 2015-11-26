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
import com.ogprover.pp.tp.expressions.AreaOfTriangle;
import com.ogprover.pp.tp.expressions.Difference;
import com.ogprover.pp.tp.expressions.AMExpression;
import com.ogprover.pp.tp.expressions.RatioOfCollinearSegments;
import com.ogprover.pp.tp.geoconstruction.GeoConstruction;
import com.ogprover.pp.tp.geoconstruction.Point;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for statement about four harmonic conjugate points.</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class FourHarmonicConjugatePoints extends PositionThmStatement {
	/* 
	 * Pair of points (A, B) is in harmonic conjunction with pair of points (C, D)
	 * iff they are four collinear points and 
	 * [vector(AC)/vector(CB)]/[vector(AD)/vector(DB)] = -1 i.e. their cross-ratio
	 * equals -1.
	 */
	
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
	 * <i><b>Symbolic label for first point of first pair</b></i>
	 */
	private static final String ALabel = "A";
	/**
	 * <i><b>Symbolic label for second point of first pair</b></i>
	 */
	private static final String BLabel = "B";
	/**
	 * <i><b>Symbolic label for first point of second pair</b></i>
	 */
	private static final String CLabel = "C";
	/**
	 * <i><b>Symbolic label for second point of second pair</b></i>
	 */
	private static final String DLabel = "D";
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method.
	 * 
	 * @param pointA	First point of first pair
	 * @param pointB	Second point of first pair
	 * @param pointC	First point of second pair
	 * @param pointD	Second point of second pair
	 */
	public FourHarmonicConjugatePoints(Point pointA, Point pointB, Point pointC, Point pointD) {
		this.geoObjects = new Vector<GeoConstruction>();
		this.geoObjects.add(pointA);
		this.geoObjects.add(pointB);
		this.geoObjects.add(pointC);
		this.geoObjects.add(pointD);
	}
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/*
	 * [vector(AC)/vector(CB)]/[vector(AD)/vector(DB)] = -1
	 * 
	 * vector(AC)/vector(CB) = - vector(AD)/vector(DB)
	 * vector(AC)/vector(CB) + vector(AD)/vector(DB) = 0
	 * 
	 * vector_x(AC) * vector_x(DB) + vector_x(AD) * vector_x(CB) = 0
	 * or
	 * vector_y(AC) * vector_y(DB) + vector_y(AD) * vector_y(CB) = 0
	 */
	
	/**
	 * Method that gives condition for x coordinates of given points
	 * so that they are harmonic conjugate points.
	 * 
	 * @return	Symbolic polynomial representing the condition for harmonic 
	 * 			conjugate points expressed by x coordinates
	 */
	private SymbolicPolynomial getXCondition() {
		/*
		 * (xC - xA)*(xB - xD) + (xD - xA)*(xB - xC) = 0
		 * xC*xB - xD*xC - xB*xA + xD*xA + xD*xB - xD*xC - xB*xA + xC*xA = 0
		 * -2xD*xC + xD*xB + xD*xA + xC*xB + xC*xA - 2xB*xA = 0
		 */
		
		SymbolicPolynomial xCondition = new SymbolicPolynomial();
		
		// Instances of symbolic variables
		SymbolicVariable xA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, ALabel);
		SymbolicVariable xB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, BLabel);
		SymbolicVariable xC = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, CLabel);
		SymbolicVariable xD = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, DLabel);
			
		// term -2xD*xC
		Term t = new SymbolicTerm(-2);
		t.addPower(new Power(xD, 1));
		t.addPower(new Power(xC, 1));
		xCondition.addTerm(t);
		
		// term xD*xB
		t = new SymbolicTerm(1);
		t.addPower(new Power(xD, 1));
		t.addPower(new Power(xB, 1));
		xCondition.addTerm(t);
		
		// term xD*xA
		t = new SymbolicTerm(1);
		t.addPower(new Power(xD, 1));
		t.addPower(new Power(xA, 1));
		xCondition.addTerm(t);
		
		// term xC*xB
		t = new SymbolicTerm(1);
		t.addPower(new Power(xC, 1));
		t.addPower(new Power(xB, 1));
		xCondition.addTerm(t);
		
		// term xC*xA
		t = new SymbolicTerm(1);
		t.addPower(new Power(xC, 1));
		t.addPower(new Power(xA, 1));
		xCondition.addTerm(t);
		
		// term -2xB*xA
		t = new SymbolicTerm(-2);
		t.addPower(new Power(xB, 1));
		t.addPower(new Power(xA, 1));
		xCondition.addTerm(t);
		
		return xCondition;
	}
	
	/**
	 * Method that gives condition for y coordinates of given points
	 * so that they are harmonic conjugate points.
	 * 
	 * @return	Symbolic polynomial representing the condition for harmonic 
	 * 			conjugate points expressed by y coordinates
	 */
	private SymbolicPolynomial getYCondition() {
		/*
		 * (yC - yA)*(yB - yD) + (yD - yA)*(yB - yC) = 0
		 * yC*yB - yD*yC - yB*yA + yD*yA + yD*yB - yD*yC - yB*yA + yC*yA = 0
		 * -2yD*yC + yD*yB + yD*yA + yC*yB + yC*yA - 2yB*yA = 0
		 */
		
		SymbolicPolynomial yCondition = new SymbolicPolynomial();
		
		// Instances of symbolic variables
		SymbolicVariable yA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, ALabel);
		SymbolicVariable yB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, BLabel);
		SymbolicVariable yC = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, CLabel);
		SymbolicVariable yD = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, DLabel);
			
		// term -2yD*yC
		Term t = new SymbolicTerm(-2);
		t.addPower(new Power(yD, 1));
		t.addPower(new Power(yC, 1));
		yCondition.addTerm(t);
		
		// term yD*yB
		t = new SymbolicTerm(1);
		t.addPower(new Power(yD, 1));
		t.addPower(new Power(yB, 1));
		yCondition.addTerm(t);
		
		// term yD*yA
		t = new SymbolicTerm(1);
		t.addPower(new Power(yD, 1));
		t.addPower(new Power(yA, 1));
		yCondition.addTerm(t);
		
		// term yC*yB
		t = new SymbolicTerm(1);
		t.addPower(new Power(yC, 1));
		t.addPower(new Power(yB, 1));
		yCondition.addTerm(t);
		
		// term yC*yA
		t = new SymbolicTerm(1);
		t.addPower(new Power(yC, 1));
		t.addPower(new Power(yA, 1));
		yCondition.addTerm(t);
		
		// term -2yB*yA
		t = new SymbolicTerm(-2);
		t.addPower(new Power(yB, 1));
		t.addPower(new Power(yA, 1));
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
			OpenGeoProver.settings.getLogger().error("There must be four points.");
			return false;
		}
		
		// Four given points must be collinear 
		// (that is responsibility of user who performs the construction).
		
		return true;
	}
	
	/**
	 * Method that gives algebraic form of this statement expressed by x-coordinates
	 * 
	 * @return	Polynomial for this statement
	 */
	public XPolynomial getXAlgebraicForm() {
		// Points A, B, C and D must be collinear and that is the responsibility of user
		Point A = (Point)this.getGeoObjects().get(0);
		Point B = (Point)this.getGeoObjects().get(1);
		Point C = (Point)this.getGeoObjects().get(2);
		Point D = (Point)this.getGeoObjects().get(3);
		
		Map<String, Point> pointsMap = new HashMap<String, Point>();
		pointsMap.put(ALabel, A);
		pointsMap.put(BLabel, B);
		pointsMap.put(CLabel, C);
		pointsMap.put(DLabel, D);
		
		return OGPTP.instantiateCondition(this.getXCondition(), pointsMap);
	}
	
	/**
	 * Method that gives algebraic form of this statement expressed by y-coordinates
	 * 
	 * @return	Polynomial for this statement
	 */
	public XPolynomial getYAlgebraicForm() {
		// Points A, B, C and D must be collinear and that is the responsibility of user
		Point A = (Point)this.getGeoObjects().get(0);
		Point B = (Point)this.getGeoObjects().get(1);
		Point C = (Point)this.getGeoObjects().get(2);
		Point D = (Point)this.getGeoObjects().get(3);
		
		Map<String, Point> pointsMap = new HashMap<String, Point>();
		pointsMap.put(ALabel, A);
		pointsMap.put(BLabel, B);
		pointsMap.put(CLabel, C);
		pointsMap.put(DLabel, D);
		
		return OGPTP.instantiateCondition(this.getYCondition(), pointsMap);
	}
	
	/**
	 * @see com.ogprover.pp.tp.thmstatement.ThmStatement#getAlgebraicForm()
	 */
	@Override
	public XPolynomial getAlgebraicForm() {
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
		sb.append("Pair of points ");
		sb.append(this.getGeoObjects().get(0).getGeoObjectLabel());
		sb.append(" and ");
		sb.append(this.getGeoObjects().get(1).getGeoObjectLabel());
		sb.append(" is in harmonic conjunction with pair of points ");
		sb.append(this.getGeoObjects().get(2).getGeoObjectLabel());
		sb.append(" and ");
		sb.append(this.getGeoObjects().get(3).getGeoObjectLabel());
		return sb.toString();
	}




	@Override
	public AreaMethodTheoremStatement getAreaMethodStatement() {
		/*
		 * First, we verify that the points are collinear, and then we verify that the
		 * ratio condition is verified.
		 */
		Point a = (Point)this.geoObjects.get(0);
		Point b = (Point)this.geoObjects.get(1);
		Point c = (Point)this.geoObjects.get(2);
		Point d = (Point)this.geoObjects.get(3);
		
		AMExpression areaOfABC = new AreaOfTriangle(a, b, c);
		AMExpression areaOfABD = new AreaOfTriangle(a, b, d);
		
		AMExpression firstRatio = new RatioOfCollinearSegments(a,c,c,b);
		AMExpression secondRatio = new RatioOfCollinearSegments(d,a,d,b);
		Difference difference = new Difference(firstRatio, secondRatio);
		
		Vector<AMExpression> statements = new Vector<AMExpression>();
		statements.add(areaOfABC);
		statements.add(areaOfABD);
		statements.add(difference);
		
		return new AreaMethodTheoremStatement(getStatementDesc(), statements);
	}
}
