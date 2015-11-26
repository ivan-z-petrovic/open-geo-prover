/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.geoconstruction;

import java.io.IOException;
import java.util.ArrayList;
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
import com.ogprover.pp.tp.auxiliary.PointSetRelationshipManager;
import com.ogprover.utilities.io.OGPOutput;
import com.ogprover.utilities.logger.ILogger;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for construction of line through two points</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class LineThroughTwoPoints extends Line {
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
	 * <i><b>Symbolic label for generic point from line</b></i>
	 */
	private static final String M0Label = "0";
	/**
	 * <i><b>Symbolic label for first point that determines the line</b></i>
	 */
	private static final String ALabel = "A";
	/**
	 * <i><b>Symbolic label for second point that determines the line</b></i>
	 */
	private static final String BLabel = "B";
	/**
	 * <i>Symbolic polynomial representing the condition for point that belongs to this line</i>
	 */
	public static SymbolicPolynomial conditionForPlainLine = null;
	
	// Static initializer of condition member 
	static {
		/* 
		 * M0 = (x0, y0) is the point on line for which the condition is calculated;
		 * A = (xA, yA) and B = (xB, yB) are two different points of given line.
		 * 
		 * Condition is:
		 *    vector AM0 is collinear with vector AB, which yields the polynomial
		 *    (x0 - xA)(yB - yA) - (xB - xA)(y0 - yA) = 0
		 * 
		 */
		if (conditionForPlainLine == null) {
			conditionForPlainLine = new SymbolicPolynomial();
			
			// Instances of symbolic variables
			SymbolicVariable x0 = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, M0Label);
			SymbolicVariable y0 = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, M0Label);
			SymbolicVariable xA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, ALabel);
			SymbolicVariable yA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, ALabel);
			SymbolicVariable xB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, BLabel);
			SymbolicVariable yB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, BLabel);
			
			// term x0 * yB
			Term t = new SymbolicTerm(1);
			t.addPower(new Power(x0, 1));
			t.addPower(new Power(yB, 1));
			conditionForPlainLine.addTerm(t);
			
			// term -x0 * yA
			t = new SymbolicTerm(-1);
			t.addPower(new Power(x0, 1));
			t.addPower(new Power(yA, 1));
			conditionForPlainLine.addTerm(t);
			
			// term -xA * yB
			t = new SymbolicTerm(-1);
			t.addPower(new Power(xA, 1));
			t.addPower(new Power(yB, 1));
			conditionForPlainLine.addTerm(t);
			
			// term -xB * y0
			t = new SymbolicTerm(-1);
			t.addPower(new Power(xB, 1));
			t.addPower(new Power(y0, 1));
			conditionForPlainLine.addTerm(t);
			
			// term xB * yA
			t = new SymbolicTerm(1);
			t.addPower(new Power(xB, 1));
			t.addPower(new Power(yA, 1));
			conditionForPlainLine.addTerm(t);
			
			// term xA * y0
			t = new SymbolicTerm(1);
			t.addPower(new Power(xA, 1));
			t.addPower(new Power(y0, 1));
			conditionForPlainLine.addTerm(t);
		}
	}
	
	
	
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
		return GeoConstruction.GEOCONS_TYPE_LINE_THROUGH_TWO_POINTS;
	}
	
	/**
	 * Method that retrieves the condition for a point that belongs to this line
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.Line#getCondition()
	 */
	public SymbolicPolynomial getCondition() {
		return conditionForPlainLine;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * 
	 * @param lineLabel		Label of this line
	 * @param pointA		First point from line
	 * @param pointB		Second point from line
	 */
	public LineThroughTwoPoints(String lineLabel, Point pointA, Point pointB) {
		this.geoObjectLabel = lineLabel;
		this.points = new Vector<Point>();
		if (pointA != null)
			this.points.add(pointA); // add at the end - first point
		if (pointB != null)
			this.points.add(pointB); // add at the end - second point
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
			// Construction of plain line is valid if both its points are previously constructed
			Point pointA = this.points.get(0);
			Point pointB = this.points.get(1);
			int indexA, indexB;
		
			if (pointA == null || pointB == null) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Line " + this.getGeoObjectLabel() + " can't be constructed because some of its base points is not constructed");
				return false;
			}
		
			indexA = pointA.getIndex();
			indexB = pointB.getIndex();
		
			if (indexA < 0 || indexB < 0) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Line " + this.getGeoObjectLabel() + " can't be constructed because some of its base points is not added to theorem protocol");
				return false; // some point is not in theorem protocol
			}
		
			boolean valid = this.index > indexA && this.index > indexB;
			
			if (!valid) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Line " + this.getGeoObjectLabel() + " can't be constructed because some of its base points is not yet constructed");
			}
			
			return valid;
		} catch (IOException e) {
			logger.error("Failed to write to output file(s).");
			output.close();
			return false;
		}
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.Line#findBestPointsForInstantation(com.ogprover.pp.tp.auxiliary.PointSetRelationshipManager)
	 */
	@Override
	public int findBestPointsForInstantation(PointSetRelationshipManager manager) {
		// just call method from superclass since it has logic for plain line
		return super.findBestPointsForInstantation(manager);
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.SetOfPoints#instantiateConditionFromBasicElements(com.ogprover.pp.tp.geoconstruction.Point)
	 */
	public XPolynomial instantiateConditionFromBasicElements(Point P) {
		Map<String, Point> pointsMap = new HashMap<String, Point>();
		pointsMap.put(M0Label, P);
		pointsMap.put(ALabel, this.points.get(0));
		pointsMap.put(BLabel, this.points.get(1));
		
		//return this.instantiateCondition(pointsMap).reduceByUTermDivision();
		return this.instantiateCondition(pointsMap); // don't reduce polynomial
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.Line#getAllPossibleConditionsWithMappings()
	 */
	@Override
	public Map<SymbolicPolynomial, ArrayList<Map<String, Point>>> getAllPossibleConditionsWithMappings() {
		// just call method from superclass
		return super.getAllPossibleConditionsWithMappings();
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getConstructionDesc()
	 */
	@Override
	public String getConstructionDesc() {
		StringBuilder sb = new StringBuilder();
		sb.append("Line ");
		sb.append(this.geoObjectLabel);
		sb.append(" through two points ");
		sb.append(this.points.get(0).getGeoObjectLabel());
		sb.append(" and ");
		sb.append(this.points.get(1).getGeoObjectLabel());
		return sb.toString();
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getInputLabels()
	 */
	@Override
	public String[] getInputLabels() {
		String[] inputLabels = new String[2];
		inputLabels[0] = this.points.get(0).getGeoObjectLabel();
		inputLabels[1] = this.points.get(1).getGeoObjectLabel();
		return inputLabels;
	}

	
}
