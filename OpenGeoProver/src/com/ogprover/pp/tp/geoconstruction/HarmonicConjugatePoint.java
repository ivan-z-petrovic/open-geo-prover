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
* <dd>Class for harmonic conjugate point of triple of points</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class HarmonicConjugatePoint extends SelfConditionalPoint {
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
	private static SymbolicPolynomial xConditionForHarmonicPointGeneral = null;
	/**
	 * <i>
	 * Symbolic polynomial representing the condition for y coordinate
	 * when line is not vertical to y-axis
	 * </i>
	 */
	private static SymbolicPolynomial yConditionForHarmonicPointGeneral = null;
	/**
	 * <i><b>Symbolic label for harmonic conjugate point</b></i>
	 */
	private static final String M0Label = "0";
	/**
	 * <i><b>Symbolic label for first point of triple of points</b></i>
	 */
	private static final String ALabel = "A";
	/**
	 * <i><b>Symbolic label for second point of triple of points</b></i>
	 */
	private static final String BLabel = "B";
	/**
	 * <i><b>Symbolic label for third point of triple of points</b></i>
	 */
	private static final String CLabel = "C";
	/**
	 * First point of triple of points
	 */
	private Point pointA = null;
	/**
	 * Second point of triple of points
	 */
	private Point pointB = null;
	/**
	 * Third point of triple of points
	 */
	private Point pointC = null;
	
	// Static initializer of condition members
	static {
		/*
		 * Pairs of points (A, B) and (C, M0) are in harmonic conjunction, which is 
		 * noted H(A, B; C, M0), iff they are collinear and their cross-ratio is equals -1:
		 * 
		 * vector(AC)/vector(CB) : vector(AM0)/vector(M0B) = -1
		 * 
		 * this is equivalent to:
		 * 
		 * vector(AC)/vector(CB) = -vector(AM0)/vector(M0B)
		 * and hence:
		 * vector(AC)/vector(CB) + vector(AM0)/vector(M0B) = 0
		 * 
		 * Points A, B, C and M0 must be collinear. For collinear points A, B and C
		 * it is satisfied:
		 * 
		 * vector(AC)/vector(CB) = (xC - xA)/(xB - xC) if line AB is not perpendicular to x-axis
		 * or
		 * vector(AC)/vector(CB) = (yC - yA)/(yB - yC) if line AB is not perpendicular to y-axis
		 * 
		 */
		
		/*
		 * General condition for x-coordinate of harmonic conjugate point can be applied
		 * if line AB is not perpendicular to x-axis:
		 * 
		 * vector(AC)/vector(CB) + vector(AM0)/vector(M0B) = 0
		 * (xC - xA)/(xB - xC) + (x0 - xA)/(xB - x0) = 0
		 * 
		 * after multiplying by denominators this becomes:
		 * (xC - xA)(xB - x0) + (x0 - xA)(xB - xC) = 0
		 * xC*xB - xC*x0 - xA*xB + xA*x0 + x0*xB - x0*xC - xA*xB + xA*xC = 0
		 * and finally:
		 * 
		 * xA*x0 + xB*x0 - 2xC*x0 + xA*xC + xB*xC -2xA*xB = 0
		 */
		if (xConditionForHarmonicPointGeneral == null) {
			xConditionForHarmonicPointGeneral = new SymbolicPolynomial();
			
			// Instances of symbolic variables
			SymbolicVariable x0 = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, M0Label);
			SymbolicVariable xA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, ALabel);
			SymbolicVariable xB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, BLabel);
			SymbolicVariable xC = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, CLabel);
			
			// term xA*x0
			Term t = new SymbolicTerm(1);
			t.addPower(new Power(xA, 1));
			t.addPower(new Power(x0, 1));
			xConditionForHarmonicPointGeneral.addTerm(t);
			
			// term xB*x0
			t = new SymbolicTerm(1);
			t.addPower(new Power(xB, 1));
			t.addPower(new Power(x0, 1));
			xConditionForHarmonicPointGeneral.addTerm(t);
			
			// term -2xC*x0
			t = new SymbolicTerm(-2);
			t.addPower(new Power(xC, 1));
			t.addPower(new Power(x0, 1));
			xConditionForHarmonicPointGeneral.addTerm(t);
			
			// term xA*xC
			t = new SymbolicTerm(1);
			t.addPower(new Power(xA, 1));
			t.addPower(new Power(xC, 1));
			xConditionForHarmonicPointGeneral.addTerm(t);
			
			// term xB*xC
			t = new SymbolicTerm(1);
			t.addPower(new Power(xB, 1));
			t.addPower(new Power(xC, 1));
			xConditionForHarmonicPointGeneral.addTerm(t);
			
			// term -2xA*xB
			t = new SymbolicTerm(-2);
			t.addPower(new Power(xA, 1));
			t.addPower(new Power(xB, 1));
			xConditionForHarmonicPointGeneral.addTerm(t);
		}
		
		/*
		 * General condition for y-coordinate of harmonic conjugate point can be applied
		 * if line AB is not perpendicular to y-axis:
		 * 
		 * vector(AC)/vector(CB) + vector(AM0)/vector(M0B) = 0
		 * (yC - yA)/(yB - yC) + (y0 - yA)/(yB - y0) = 0
		 * 
		 * after multiplying by denominators this becomes:
		 * (yC - yA)(yB - y0) + (y0 - yA)(yB - yC) = 0
		 * yC*yB - yC*y0 - yA*yB + yA*y0 + y0*yB - y0*yC - yA*yB + yA*yC = 0
		 * and finally:
		 * 
		 * yA*y0 + yB*y0 - 2yC*y0 + yA*yC + yB*yC -2yA*yB = 0
		 */
		if (yConditionForHarmonicPointGeneral == null) {
			yConditionForHarmonicPointGeneral = new SymbolicPolynomial();
			
			// Instances of symbolic variables
			SymbolicVariable y0 = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, M0Label);
			SymbolicVariable yA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, ALabel);
			SymbolicVariable yB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, BLabel);
			SymbolicVariable yC = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, CLabel);
			
			// term yA*y0
			Term t = new SymbolicTerm(1);
			t.addPower(new Power(yA, 1));
			t.addPower(new Power(y0, 1));
			yConditionForHarmonicPointGeneral.addTerm(t);
			
			// term yB*y0
			t = new SymbolicTerm(1);
			t.addPower(new Power(yB, 1));
			t.addPower(new Power(y0, 1));
			yConditionForHarmonicPointGeneral.addTerm(t);
			
			// term -2yC*y0
			t = new SymbolicTerm(-2);
			t.addPower(new Power(yC, 1));
			t.addPower(new Power(y0, 1));
			yConditionForHarmonicPointGeneral.addTerm(t);
			
			// term yA*yC
			t = new SymbolicTerm(1);
			t.addPower(new Power(yA, 1));
			t.addPower(new Power(yC, 1));
			yConditionForHarmonicPointGeneral.addTerm(t);
			
			// term yB*yC
			t = new SymbolicTerm(1);
			t.addPower(new Power(yB, 1));
			t.addPower(new Power(yC, 1));
			yConditionForHarmonicPointGeneral.addTerm(t);
			
			// term -2yA*yB
			t = new SymbolicTerm(-2);
			t.addPower(new Power(yA, 1));
			t.addPower(new Power(yB, 1));
			yConditionForHarmonicPointGeneral.addTerm(t);
		}
	}

	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * Method to set the first point of triple of points
	 * 
	 * @param A The first point of triple of points to set
	 */
	public void setPointA(Point A) {
		this.pointA = A;
	}

	/**
	 * Method that retrieves the first point of triple of points
	 * 
	 * @return The first point of triple of points
	 */
	public Point getPointA() {
		return pointA;
	}
	
	/**
	 * Method to set the second point of triple of points
	 * 
	 * @param B The second point of triple of points to set
	 */
	public void setPointB(Point B) {
		this.pointB = B;
	}

	/**
	 * Method that retrieves the second point of triple of points
	 * 
	 * @return The second point of triple of points
	 */
	public Point getPointB() {
		return pointB;
	}
	
	/**
	 * Method to set the third point of triple of points
	 * 
	 * @param C The third point of triple of points to set
	 */
	public void setPointC(Point C) {
		this.pointC = C;
	}

	/**
	 * Method that retrieves the third point of triple of points
	 * 
	 * @return The third point of triple of points
	 */
	public Point getPointC() {
		return pointC;
	}
	
	/**
	 * Method that retrieves the type of construction
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getConstructionType()
	 */
	@Override
	public int getConstructionType() {
		return GeoConstruction.GEOCONS_TYPE_HARMONIC_POINT;
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
		 * points from triple of points. If points A and B have same instances
		 * for x-coordinate that means the line AB is perpendicular to x-axis
		 * and therefore harmonic conjugate point must have same x-coordinate.
		 * Otherwise (general case) we apply general condition for x-coordinate
		 * of harmonic conjugate point.
		 */
		if (this.pointA.getX().equals(this.pointB.getX())) {
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
		
		return xConditionForHarmonicPointGeneral;
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
		 * points from triple of points. If points A and B have same instances
		 * for y-coordinate that means the line AB is perpendicular to y-axis
		 * and therefore harmonic conjugate point must have same y-coordinate.
		 * Otherwise (general case) we apply general condition for y-coordinate
		 * of harmonic conjugate point.
		 */
		if (this.pointA.getY().equals(this.pointB.getY())) {
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
		
		return yConditionForHarmonicPointGeneral;
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
	 * @param A				First point of triple of points
	 * @param B				Second point of triple of points
	 * @param C				Third point of triple of points
	 */
	public HarmonicConjugatePoint(String pointLabel, Point A, Point B, Point C) {
		this.geoObjectLabel = pointLabel;
		this.pointA = A;
		this.pointB = B;
		this.pointC = C;
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
		Point p = new HarmonicConjugatePoint(this.geoObjectLabel, this.pointA, this.pointB, this.pointC);
		
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
			int indexA, indexB, indexC;
		
			if (this.pointA == null || this.pointB == null || this.pointC == null) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Harmonic conjugate point " + this.getGeoObjectLabel() + " can't be constructed since some point from triple of points is not constructed");
				return false;
			}
		
			indexA = this.pointA.getIndex();
			indexB = this.pointB.getIndex();
			indexC = this.pointC.getIndex();
		
			if (indexA < 0 || indexB < 0 || indexC < 0) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Harmonic conjugate point " + this.getGeoObjectLabel() + " can't be constructed since some point from triple of points is not added to theorem protocol");
				return false; // some point not in theorem protocol
			}
		
			boolean valid = this.index > indexA && this.index > indexB && this.index > indexC;
			
			if (!valid) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Harmonic conjugate point " + this.getGeoObjectLabel() + " can't be constructed since some point from triple of points is not yet constructed");
			}
			
			/*
			 * At the end check if points A, B and C are collinear.
			 * To check this it would be necessary to prove this as
			 * small theorem, but it will not be done. It should be the
			 * responsibility of construction maker to provide collinear
			 * points A, B, C. Therefore, this check will be performed
			 * only by searching current system of polynomials and if
			 * condition for collienarity can't be found, only a warning
			 * will be written in log file - therefore this check will not
			 * affect the validation result.
			 */
			/*
			 * Cannot do it here since points are not yet instantiated
			 *
			if (valid) {
				Line testLine = new LineThroughTwoPoints("testLine" + Math.round(Math.random()*1000), this.pointA, this.pointB);
				XPolynomial xPolyCond = testLine.instantiateConditionFromBasicElements(this.pointC);
			
				if (this.consProtocol.isPolynomialConsequenceOfConstructions(xPolyCond) == false) {
					StringBuilder sb = new StringBuilder();
					sb.append("It is not safe to use points ");
					sb.append(this.pointA.getGeoObjectLabel());
					sb.append(", ");
					sb.append(this.pointB.getGeoObjectLabel());
					sb.append("and ");
					sb.append(this.pointC.getGeoObjectLabel());
					sb.append(" since it is not found that they are collinear on the basis of previous constructions.");
					logger.warn(sb.toString());
				}
			}
			*
			*
			*/
			
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
		sb.append("Fourth harmonic conjugate point ");
		sb.append(this.geoObjectLabel);
		sb.append(" of tripple of points ");
		sb.append(this.pointA.getGeoObjectLabel());
		sb.append(", ");
		sb.append(this.pointB.getGeoObjectLabel());
		sb.append(" and ");
		sb.append(this.pointC.getGeoObjectLabel());
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
		inputLabels[2] = this.pointC.getGeoObjectLabel();
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
		pointsMap.put(CLabel, this.pointC);
		return pointsMap;
	}

	@Override
	public Point replace(HashMap<Point, Point> replacementMap) {
		OpenGeoProver.settings.getLogger().error("This method should not be called on this class.");
		return null;
	}
}

