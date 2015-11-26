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
import com.ogprover.pp.tp.geoobject.Segment;
import com.ogprover.utilities.io.OGPOutput;
import com.ogprover.utilities.logger.ILogger;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for division point of line segment (not necessarily belongs
*     to the segment, but to the line of that segment)</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class SegmentDivisionPoint extends SelfConditionalPoint {
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
	 * <i><b>Symbolic label for segment division point</b></i>
	 */
	private static final String M0Label = "0";
	/**
	 * <i><b>Symbolic label for first point of line segment</b></i>
	 */
	private static final String ALabel = "A";
	/**
	 * <i><b>Symbolic label for second point of line segment</b></i>
	 */
	private static final String BLabel = "B";
	/**
	 * Segment whose division point is this point
	 */
	private Segment segment = null;
	/**
	 * Coefficient of division - determines how this point divides the segment
	 */
	private double divisionCoefficient = 0.0;
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * Method to set the segment
	 * 
	 * @param seg The segment to set
	 */
	public void setSegment(Segment seg) {
		this.segment = seg;
	}

	/**
	 * Method that retrieves the segment
	 * 
	 * @return The segment
	 */
	public Segment getSegment() {
		return segment;
	}
	
	/**
	 * @param divisionCoefficient the divisionCoefficient to set
	 */
	public void setDivisionCoefficient(double divisionCoefficient) {
		this.divisionCoefficient = divisionCoefficient;
	}

	/**
	 * @return the divisionCoefficient
	 */
	public double getDivisionCoefficient() {
		return divisionCoefficient;
	}

	/**
	 * Method that retrieves the type of construction
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getConstructionType()
	 */
	@Override
	public int getConstructionType() {
		return GeoConstruction.GEOCONS_TYPE_SEGMENT_RATIO;
	}
	
	/**
	 * Method that gives the condition for x coordinate 
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.SelfConditionalPoint#getXCondition()
	 */
	@Override
	public SymbolicPolynomial getXCondition() {
		/*
		 * For division point M0 of segment AB it is satisfied:
		 * 	vector(A,M0) : vector(M0, B) = k (where k != -1)
		 * Therefore, x coordinate of segment division point satisfies following equation:
		 * 		(1 + k)*x0 - xA - k*xB = 0, where k != -1
		 */
		
		SymbolicPolynomial xConditionForSDPoint = new SymbolicPolynomial();
			
		// Instances of symbolic variables
		SymbolicVariable x0 = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, M0Label);
		SymbolicVariable xA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, ALabel);
		SymbolicVariable xB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, BLabel);
			
		// term (1 + k)*x0
		Term t = new SymbolicTerm(this.divisionCoefficient + 1);
		t.addPower(new Power(x0, 1));
		xConditionForSDPoint.addTerm(t);
			
		// term -xA
		t = new SymbolicTerm(-1);
		t.addPower(new Power(xA, 1));
		xConditionForSDPoint.addTerm(t);
			
		// term -k*xB
		t = new SymbolicTerm(-this.divisionCoefficient);
		t.addPower(new Power(xB, 1));
		xConditionForSDPoint.addTerm(t);
		
		return xConditionForSDPoint;
	}

	@Override
	/**
	 * Method that gives the condition for y coordinate 
	 * 
	 * @see com.ogp.pp.tp.geoconstruction.Point#getYCondition()
	 */
	public SymbolicPolynomial getYCondition() {
		/*
		 * For division point M0 of segment AB it is satisfied:
		 * 	vector(A,M0) : vector(M0, B) = k (where k != -1)
		 * Therefore, y coordinate of segment division point satisfies following equation:
		 * 		(1 + k)*y0 - yA - k*yB = 0, where k != -1
		 */
		
		SymbolicPolynomial yConditionForSDPoint = new SymbolicPolynomial();
			
		// Instances of symbolic variables
		SymbolicVariable y0 = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, M0Label);
		SymbolicVariable yA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, ALabel);
		SymbolicVariable yB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, BLabel);
			
		// term (1 + k)*y0
		Term t = new SymbolicTerm(this.divisionCoefficient + 1);
		t.addPower(new Power(y0, 1));
		yConditionForSDPoint.addTerm(t);
			
		// term -yA
		t = new SymbolicTerm(-1);
		t.addPower(new Power(yA, 1));
		yConditionForSDPoint.addTerm(t);
			
		// term -k*yB
		t = new SymbolicTerm(-this.divisionCoefficient);
		t.addPower(new Power(yB, 1));
		yConditionForSDPoint.addTerm(t);
		
		return yConditionForSDPoint;
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
	 * @param A				First segment's point
	 * @param B				Second segment's point
	 * @param divCoeff		Division coefficient
	 */
	public SegmentDivisionPoint(String pointLabel, Point A, Point B, double divCoeff) {
		this.geoObjectLabel = pointLabel;
		if (A != null && B != null)
			this.segment = new Segment(A, B);
		this.divisionCoefficient = divCoeff;
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
		// we do not perform deep copy so segment's end points will not be cloned
		Point p;
		
		if (this.segment != null)
			p = new SegmentDivisionPoint(this.geoObjectLabel, this.segment.getFirstEndPoint(), this.segment.getSecondEndPoint(), this.divisionCoefficient);
		else
			p = new SegmentDivisionPoint(this.geoObjectLabel, null, null, this.divisionCoefficient);
		
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
			int indexA, indexB;
			
			// check if coefficient is equals to -1
			if (this.divisionCoefficient == -1) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Segment division point " + this.getGeoObjectLabel() + " can't be constructed since coefficient is equals to -1 which is illegal.");
				return false;
			}
		
			if (this.segment == null || this.segment.getFirstEndPoint() == null || this.segment.getSecondEndPoint() == null) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Segment division point " + this.getGeoObjectLabel() + " can't be constructed since one or two segment's end points are not constructed");
				return false;
			}
		
			indexA = this.segment.getFirstEndPoint().getIndex();
			indexB = this.segment.getSecondEndPoint().getIndex();
		
			if (indexA < 0 || indexB < 0) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Segment division point " + this.getGeoObjectLabel() + " can't be constructed since some of segment's end ponts is not added to theorem protocol");
				return false; // some point not in theorem protocol
			}
		
			boolean valid = this.index > indexA && this.index > indexB;
			
			if (!valid) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Segment division point " + this.getGeoObjectLabel() + " can't be constructed since some of segment's end points is not yet constructed");
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
		sb.append("Segment division point ");
		sb.append(this.geoObjectLabel);
		sb.append(" of segment ");
		sb.append(this.segment.getDescription());
		sb.append(" with division coefficient ");
		sb.append(this.divisionCoefficient);
		return sb.toString();
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getInputLabels()
	 */
	@Override
	public String[] getInputLabels() {
		String[] inputLabels = new String[2];
		inputLabels[0] = this.segment.getFirstEndPoint().getGeoObjectLabel();
		inputLabels[1] = this.segment.getSecondEndPoint().getGeoObjectLabel();
		return inputLabels;
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.SelfConditionalPoint#getPointsForInstantiation()
	 */
	@Override
	public Map<String, Point> getPointsForInstantiation() {
		Map<String, Point> pointsMap = new HashMap<String, Point>();
		pointsMap.put(M0Label, this);
		pointsMap.put(ALabel, this.segment.getFirstEndPoint());
		pointsMap.put(BLabel, this.segment.getSecondEndPoint());
		return pointsMap;
	}

	@Override
	public Point replace(HashMap<Point, Point> replacementMap) {
		OpenGeoProver.settings.getLogger().error("This method should not be called on this class.");
		return null;
	}
}

