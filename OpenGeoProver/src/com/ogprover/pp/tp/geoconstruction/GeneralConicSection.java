/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.geoconstruction;

import java.io.IOException;
import java.util.ArrayList;
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
import com.ogprover.polynomials.UFraction;
import com.ogprover.polynomials.UPolynomial;
import com.ogprover.polynomials.UTerm;
import com.ogprover.polynomials.UXVariable;
import com.ogprover.polynomials.Variable;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.polynomials.XTerm;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.auxiliary.PointSetRelationshipManager;
import com.ogprover.pp.tp.ndgcondition.AlgebraicNDGCondition;
import com.ogprover.utilities.io.OGPOutput;
import com.ogprover.utilities.io.SpecialFileFormatting;
import com.ogprover.utilities.logger.ILogger;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for general conic section</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class GeneralConicSection extends FreeParametricSet implements ConicSection {
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
	 * List of all constructed points that belong to this conic section
	 */
	private Vector<Point> points;
	
	/**
	 * <i>
	 * Symbolic polynomial representing the condition for point 
	 * that belongs to this conic section
	 * </i>
	 */
	public static SymbolicPolynomial conditionForConicSection = null;
	
	/**
	 * <i>
	 * Symbolic polynomial representing the condition for point 
	 * that belongs to this conic section when it contains the origin 
	 * of Cartesian coordinate system
	 * </i>
	 */
	public static SymbolicPolynomial conditionForConicSectionWithOrigin = null;
	
	
	// Static initializer

	static {
		if (conditionForConicSectionWithOrigin == null) {
			/*
			 * Equation of general conic section is:
			 * 
			 * A*x^2 + B*x*y + C*y^2 + D*x + E*y + F = 0, where at least one of parameters A,B,C is different from zero.
			 * 
			 * When points are instantiated parametric set gets priority over construction of other points.
			 * Cartesian coordinate system can be chosen in such way that the origin belongs
			 * to this parametric set and at least one point from x-axis. If the origin is on
			 * this parametric set, then F = 0.
			 * 
			 * Point M0(x0, y0) belongs to conic section iff its coordinates satisfy the above equation.
			 *
			 */
			
			// Instances of symbolic variables
			SymbolicVariable x0 = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, M0Label);
			SymbolicVariable y0 = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, M0Label);
			SymbolicVariable xA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, ALabel);
			SymbolicVariable xB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, BLabel);
			SymbolicVariable xC = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, CLabel);
			SymbolicVariable xD = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, DLabel);
			SymbolicVariable xE = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, ELabel);
			
			conditionForConicSectionWithOrigin = new SymbolicPolynomial();
			
			// term A*x0^2
			Term t = new SymbolicTerm(1);
			t.addPower(new Power(xA, 1));
			t.addPower(new Power(x0, 2));
			conditionForConicSectionWithOrigin.addTerm(t);
			
			// term B*x0*y0
			t = new SymbolicTerm(1);
			t.addPower(new Power(xB, 1));
			t.addPower(new Power(x0, 1));
			t.addPower(new Power(y0, 1));
			conditionForConicSectionWithOrigin.addTerm(t);
			
			// term C*y0^2
			t = new SymbolicTerm(1);
			t.addPower(new Power(xC, 1));
			t.addPower(new Power(y0, 2));
			conditionForConicSectionWithOrigin.addTerm(t);
			
			// term D*x0
			t = new SymbolicTerm(1);
			t.addPower(new Power(xD, 1));
			t.addPower(new Power(x0, 1));
			conditionForConicSectionWithOrigin.addTerm(t);
			
			// term E*y0
			t = new SymbolicTerm(1);
			t.addPower(new Power(xE, 1));
			t.addPower(new Power(y0, 1));
			conditionForConicSectionWithOrigin.addTerm(t);
		}
		
		if (conditionForConicSection == null) {
			conditionForConicSection = (SymbolicPolynomial) conditionForConicSectionWithOrigin.clone();
			
			// term F
			Term t = new SymbolicTerm(1);
			SymbolicVariable xF = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, FLabel);
			t.addPower(new Power(xF, 1));
			conditionForConicSection.addTerm(t);
		}
		
		// NOTE: In both symbolic conditions parameters are free and are instantiated by u-variables.
		//       Therefore, number of parameters is not important for instantiation (there could be
		//       any number of polynomials instantiated from these symbolic conditions).
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * Method that sets the list of all points belonging to this conic section
	 * 
	 * @param points The points to set
	 */
	public void setPoints(Vector<Point> points) {
		this.points = points;
	}

	/**
	 * Method that retrieves the list of all points belonging to this conic section
	 * 
	 * @return the points
	 */
	public Vector<Point> getPoints() {
		return points;
	}
	
	
	

	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * 
	 * @param conicLabel	Label of this conic section
	 */
	public GeneralConicSection(String conicLabel) {
		this.geoObjectLabel = conicLabel;
		this.points = new Vector<Point>();
		this.parametricPoints = new Vector<Point>();
		
		this.parametricPoints.add(new FreePoint(ALabel + conicLabel));
		this.parametricPoints.add(new FreePoint(BLabel + conicLabel));
		this.parametricPoints.add(new FreePoint(CLabel + conicLabel));
		this.parametricPoints.add(new FreePoint(DLabel + conicLabel));
		this.parametricPoints.add(new FreePoint(ELabel + conicLabel));
		this.parametricPoints.add(new FreePoint(FLabel + conicLabel));
		// indication whether this conic section contains origin or not is set when it is
		// added to a theorem protocol object
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Finding best points for instantiation.
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.SetOfPoints#findBestPointsForInstantation(com.ogprover.pp.tp.auxiliary.PointSetRelationshipManager)
	 */
	public int findBestPointsForInstantation(PointSetRelationshipManager manager) {
		/*
		 * General conic section can't be constructed. Therefore no need to search for
		 * points from conic to find best elements for the instantiation. All that is 
		 * necessary is to use parametric points.
		 */

		ILogger logger = OpenGeoProver.settings.getLogger();
		
		// NOTE: This method is used to find best elements for process of transformation
		// in algebraic form. It is implemented by calling same methods like in
		// final transformation, but without printing of results to output reports.
		// Also, in order to be able to repeat the good transformation later in
		// final processing, it is necessary to cancel any result i.e. if a polynomial
		// is added to system of polynomials, it has to be removed, and if coordinates
		// are renamed, they have to be back to previous values.
		
		manager.prepareForFirstInstantiation();
		
		if (manager.isErrorFlag()) {
			logger.error("Failed in preparation method");
			return OGPConstants.ERR_CODE_GENERAL;
		}
		
		Map<String, Point> pointsMap = new HashMap<String, Point>(); // collection with current elements
		Point P = manager.getPoint();
		pointsMap.put(M0Label, P);
		manager.setCondition(this.getCondition());
		
		pointsMap.put(ALabel, this.parametricPoints.get(0));
		pointsMap.put(BLabel, this.parametricPoints.get(1));
		pointsMap.put(CLabel, this.parametricPoints.get(2));
		pointsMap.put(DLabel, this.parametricPoints.get(3));
		pointsMap.put(ELabel, this.parametricPoints.get(4));
		if (!this.containsOrigin)
			pointsMap.put(FLabel, this.parametricPoints.get(6));
		
		manager.processPointsAndCondition(pointsMap);
		
		if (manager.isErrorFlag()) {
			logger.error("Failed in processing specific polynomial for circle with three points on it.");
			return OGPConstants.ERR_CODE_GENERAL;
		}
		
		return OGPConstants.RET_CODE_SUCCESS;
	}
	
	/**
	 * @see com.ogprover.pp.tp.geoconstruction.SetOfPoints#instantiateConditionFromBasicElements(com.ogprover.pp.tp.geoconstruction.Point)
	 */
	public XPolynomial instantiateConditionFromBasicElements(Point P) {
		Map<String, Point> pointsMap = new HashMap<String, Point>();
		pointsMap.put(M0Label, P);
		
		pointsMap.put(ALabel, this.parametricPoints.get(0));
		pointsMap.put(BLabel, this.parametricPoints.get(1));
		pointsMap.put(CLabel, this.parametricPoints.get(2));
		pointsMap.put(DLabel, this.parametricPoints.get(3));
		pointsMap.put(ELabel, this.parametricPoints.get(4));
		if (!this.containsOrigin)
			pointsMap.put(FLabel, this.parametricPoints.get(5));
		
		return OGPTP.instantiateCondition(this.getCondition(), pointsMap); // don't reduce polynomial
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.SetOfPoints#getAllPossibleConditionsWithMappings()
	 */
	public Map<SymbolicPolynomial, ArrayList<Map<String, Point>>> getAllPossibleConditionsWithMappings() {
		Map<SymbolicPolynomial, ArrayList<Map<String, Point>>> retMap = new HashMap<SymbolicPolynomial, ArrayList<Map<String, Point>>>();
		
		ArrayList<Map<String, Point>> allMappings = new ArrayList<Map<String, Point>>();
		Map<String, Point> pointsMap = new HashMap<String, Point>();
		 
		// only add parametric points
		pointsMap.put(ALabel, this.parametricPoints.get(0));
		pointsMap.put(BLabel, this.parametricPoints.get(1));
		pointsMap.put(CLabel, this.parametricPoints.get(2));
		pointsMap.put(DLabel, this.parametricPoints.get(3));
		pointsMap.put(ELabel, this.parametricPoints.get(4));
		if (!this.containsOrigin)
			pointsMap.put(FLabel, this.parametricPoints.get(5));
		
		allMappings.add(pointsMap);
		
		retMap.put((SymbolicPolynomial) this.getCondition().clone(), allMappings);
		
		return retMap;
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.SetOfPoints#addPointToSet(com.ogprover.pp.tp.geoconstruction.Point)
	 */
	public void addPointToSet(Point P) {
		this.points.add(P);
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.SetOfPoints#getCondition()
	 */
	public SymbolicPolynomial getCondition() {
		if (!this.containsOrigin)
			return conditionForConicSection;
		
		return conditionForConicSectionWithOrigin;
	}

	/**
	 * Method that gives the type of this construction
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getConstructionType()
	 */
	@Override
	public int getConstructionType() {
		return GeoConstruction.GEOCONS_TYPE_CONIC_SECTION;
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getConstructionDesc()
	 */
	@Override
	public String getConstructionDesc() {
		StringBuilder sb = new StringBuilder();
		sb.append("General Conic Section ");
		sb.append(this.geoObjectLabel);
		return sb.toString();
	}
	
	/**
	 * Method that instantiates the parametric point
	 * 
	 * @param pointIndex	Index of point from collection of parametric points 
	 * 						of this conic section 
	 * @throws IOException	Exception when writing to output stream 
	 * 						will be processed by caller method
	 */
	private void instantiateParametricPoint(int pointIndex) throws IOException {
		OGPOutput output = OpenGeoProver.settings.getOutput();
		Point point = this.parametricPoints.get(pointIndex);
		
		point.setX(new UXVariable(Variable.VAR_TYPE_UX_U, this.consProtocol.getUIndex()));
		point.setY(new UXVariable(Variable.VAR_TYPE_UX_U, 0));
    	this.consProtocol.incrementUIndex();
    	if (point.getPointState() == Point.POINT_STATE_INITIALIZED)
    		point.setPointState(Point.POINT_STATE_INSTANTIATED);
		else
			point.setPointState(Point.POINT_STATE_REINSTANTIATED);
    	output.openItem();
		output.writePointCoordinatesAssignment(point);
		output.closeItem();
	}
	
	/**
     * Method that transforms general conic section in algebraic form
     * by instantiating parametric points.
     * 
     * @return	Returns SUCCESS if successful or general error otherwise 
     */
    public int transformToAlgebraicForm() {
    	OGPOutput output = OpenGeoProver.settings.getOutput();
    	ILogger logger = OpenGeoProver.settings.getLogger();
		
		try {
			output.openSubSection("Transformation of general conic section " + this.geoObjectLabel + ": ", true);
			output.writePlainText("List of parametric points");
			output.openEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
	    	
	    	this.instantiateParametricPoint(0); // parametric point A
	    	this.instantiateParametricPoint(1); // parametric point B
	    	this.instantiateParametricPoint(2); // parametric point C
	    	this.instantiateParametricPoint(3); // parametric point D
	    	this.instantiateParametricPoint(4); // parametric point E
	    	if (!this.containsOrigin)
	    		this.instantiateParametricPoint(5); // parametric point F
	    	
	    	// Write equation of this parametric set
	    	Point X = new FreePoint("X");
	    	X.setX(new UXVariable(Variable.VAR_TYPE_UX_X, 1));
	    	X.setY(new UXVariable(Variable.VAR_TYPE_UX_X, 2));
	    	XPolynomial equation = this.instantiateConditionFromBasicElements(X);
	    	output.openItem();
	    	output.writePlainText("Condition for point ");
	    	output.writePointWithCoordinates(X);
	    	output.writePlainText(" to belong to this conic section is following equation: ");
			output.writePolynomial(equation);
			output.closeItem();
	    	
			output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
			output.closeSubSection();
		} catch (IOException e) {
			logger.error("Failed to write to output file(s).");
			output.close();
			return OGPConstants.ERR_CODE_GENERAL;
		}
		
		return OGPConstants.RET_CODE_SUCCESS;
    	
    }
    
    public void processNDGCondition(AlgebraicNDGCondition ndgCond) {
    	// Create three polynomial conditions for non-degenerate conic section - parameters A, B and C from 
    	// equation of general conic section can't all be equal to zero.
    	
    	UTerm utA = new UTerm(1);
    	utA.addPower(new Power(this.parametricPoints.get(0).getX().clone(), 1));
    	UPolynomial upA = new UPolynomial();
    	upA.addTerm(utA);
    	UFraction ufA = new UFraction(upA);
    	XTerm xtA = new XTerm(ufA);
    	XPolynomial xpA = new XPolynomial();
    	xpA.addTerm(xtA);
    	
    	UTerm utB = new UTerm(1);
    	utB.addPower(new Power(this.parametricPoints.get(1).getX().clone(), 1));
    	UPolynomial upB = new UPolynomial();
    	upB.addTerm(utB);
    	UFraction ufB = new UFraction(upB);
    	XTerm xtB = new XTerm(ufB);
    	XPolynomial xpB = new XPolynomial();
    	xpB.addTerm(xtB);
    	
    	UTerm utC = new UTerm(1);
    	utC.addPower(new Power(this.parametricPoints.get(2).getX().clone(), 1));
    	UPolynomial upC = new UPolynomial();
    	upC.addTerm(utC);
    	UFraction ufC = new UFraction(upC);
    	XTerm xtC = new XTerm(ufC);
    	XPolynomial xpC = new XPolynomial();
    	xpC.addTerm(xtC);
    	
    	XPolynomial ndgPoly = ndgCond.getPolynomial();
    	
    	if (ndgPoly.equals(xpA) || ndgPoly.equals(xpB) || ndgPoly.equals(xpC)) {
    		ndgCond.setBestDescription("Conic Section is not in degenerate form");
    	}
    }

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.FreeParametricSet#getNumberOfZeroCoordinates()
	 */
	public int getNumberOfZeroCoordinates() {
		// Coordinate system can be set that origin and one point from x-axis
		// belong to this general conic section
		if (this.containsOrigin)
			return 3;
		return 0;
	}
	
	/**
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getInputLabels()
	 */
	@Override
	public String[] getInputLabels() {
		return null;
	}
	
}
