/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.prover_protocol.cp.geoconstruction;

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
import com.ogprover.polynomials.Variable;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.prover_protocol.cp.OGPCP;
import com.ogprover.prover_protocol.cp.auxiliary.PointSetRelationshipManager;
import com.ogprover.prover_protocol.cp.auxiliary.Segment;
import com.ogprover.utilities.io.FileLogger;
import com.ogprover.utilities.io.OGPOutput;



/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for parabola</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class Parabola extends ConicSection {
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
	 * <i><b>Symbolic label for generic point from parabola</b></i>
	 */
	private static final String M0Label = "0";
	/**
	 * <i><b>Symbolic label for first point of parabola's directrix</b></i>
	 */
	private static final String ALabel = "A";
	/**
	 * <i><b>Symbolic label for second point of parabola's directrix</b></i>
	 */
	private static final String BLabel = "B";
	/**
	 * <i><b>Symbolic label for focus of parabola</b></i>
	 */
	private static final String FLabel = "F";
	/**
	 * List of all constructed points that belong to this parabola
	 */
	protected Vector<Point> points = new Vector<Point>();
	/**
	 * Focus of parabola
	 */
	protected Point focus = null;
	/**
	 * Directrix of parabola
	 */
	protected Line directrix = null;
	/**
	 * <i>
	 * Symbolic polynomial representing the condition for point 
	 * that belongs to this parabola
	 * </i>
	 */
	public static SymbolicPolynomial conditionForParabola = null;
	
	// Static initializer of condition members 
	static {
		/*
		 * Let M0 = (x0, y0) be some point from parabola whose given elements are:
		 * focus F = (xF, yF) and directrix with two points
		 * A = (xA, yA) and B = (xB, yB).
		 * 
		 * Geometry property of parabola is that the distance between any its point and focus
		 * of that parabola is same as the distance between that point and the directrix 
		 * of that parabola.
		 * 
		 * Let N=(xN, yN) be the foot of line through M0 perpendicular to directrix AB.
		 * Since N is on AB then its coordinates satisfy the equation:
		 * 	(xN - xA)(yB - yA) - (xB - xA)(yN - yA) = 0 [see PlainLine.java]
		 * From this we get:
		 * 	yN = (xN - xA)(yB - yA)/(xB - xA) + yA
		 * N is also on perpendicular line through M0 to directrix AB:
		 * 	(xN - x0)(xB - xA) + (yN - y0)(yB - yA) = 0 [see PerpendicularLine.java]
		 * In this equation we replace yN expressed by xN:
		 * 	(xN - x0)(xB - xA) + ((xN - xA)(yB - yA)/(xB - xA) + yA - y0)(yB - yA) = 0
		 * Multiply whole equation by (xB - xA):
		 * 	[(xB - xA)^2 + (yB - yA)^2]*xN = x0(xB - xA)^2 + xA(yB - yA)^2 - (yA - y0)(xB - xA)(yB - yA)
		 * from this we obtain numerator and denominator of x-coordinate of point N:
		 * 		xNum = x0(xB - xA)^2 + xA(yB - yA)^2 - (yA - y0)(xB - xA)(yB - yA)
		 * 		xDen = (xB - xA)^2 + (yB - yA)^2
		 * Now y-coordinate of point N becomes:
		 * 	xDen*(xB - xA)*yN = (xNum - xA*xDen)(yB - yA) + yA*xDen*(xB - xA)
		 * from this we obtain numerator and denominator of y-coordinate of point N:
		 * 		yNum = (xNum - xA*xDen)(yB - yA) + yA*xDen*(xB - xA)
		 * 		yDen = xDen*(xB - xA)
		 * 
		 * Then the distance between M0 and directrix AB is equals as distance between M0 and N.
		 * Square of this distance is:
		 * 	(x0 - xNum/xDen)^2 + (y0 - yNum/yDen)^2
		 * from this we get following equivalent expression:
		 * 	[(xDen*yDen*x0 - yDen*xNum)^2 + (xDen*yDen*y0 - xDen*yNum)^2]/(xDen*yDen)^2
		 * this has to be equals to square of distance between M0 and F, hence we obtain
		 * the condition for point to belong to parabola:
		 * 		(xDen*yDen*x0 - yDen*xNum)^2 + (xDen*yDen*y0 - xDen*yNum)^2 - (M0F*xDen*yDen)^2 = 0
		 * 
		 * Although we would expect to obtain a quadric equation by coordinates of point M0 as 
		 * condition for parabola, it will be the equation of fourth degree, due to squaring
		 * we had to apply in order to get free of square roots 
		 * (see manual test test MTestCP.testConicConditions()). But explanation of this
		 * equation of fourth degree is following: it represents the union of two parabolas -
		 * one with these given elements and another with same directrix but whose focus
		 * is symmetric to F about directrix, and whose given point is symmetric to M0
		 * about directrix. That is 2+2 = 4th degree in total.
		 *  
		 */
		if (conditionForParabola == null) {
			// square of distance M0F
			SymbolicPolynomial sdM0F = Segment.substitutePointLabelsForSquareOfDistance((SymbolicPolynomial) Segment.getConditionForSquareOfDistance().clone(), M0Label, FLabel);
			
			// Instances of symbolic variables
			SymbolicVariable x0 = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, M0Label);
			SymbolicVariable y0 = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, M0Label);
			SymbolicVariable xA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, ALabel);
			SymbolicVariable yA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, ALabel);
			SymbolicVariable xB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, BLabel);
			SymbolicVariable yB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, BLabel);
			
			SymbolicTerm t = null;
			SymbolicPolynomial tempAddendant = null;
			SymbolicPolynomial tempFactor = null;
			
			// xB - xA
			SymbolicPolynomial diffxBxA = new SymbolicPolynomial();
			t = new SymbolicTerm(1);
			t.addPower(new Power(xB, 1));
			diffxBxA.addTerm(t);
			t = new SymbolicTerm(1);
			t.addPower(new Power(xA, 1));
			tempAddendant = new SymbolicPolynomial();
			tempAddendant.addTerm(t);
			diffxBxA.subtractPolynomial(tempAddendant);
			
			// yB - yA
			SymbolicPolynomial diffyByA = new SymbolicPolynomial();
			t = new SymbolicTerm(1);
			t.addPower(new Power(yB, 1));
			diffyByA.addTerm(t);
			t = new SymbolicTerm(1);
			t.addPower(new Power(yA, 1));
			tempAddendant = new SymbolicPolynomial();
			tempAddendant.addTerm(t);
			diffyByA.subtractPolynomial(tempAddendant);
			
			// xNum = x0(xB - xA)^2 + xA(yB - yA)^2 - (yA - y0)(xB - xA)(yB - yA)
			SymbolicPolynomial xNum = new SymbolicPolynomial();
			tempAddendant = new SymbolicPolynomial();
			tempFactor = new SymbolicPolynomial();
			t = new SymbolicTerm(1);
			t.addPower(new Power(x0, 1));
			tempFactor.addTerm(t);
			tempAddendant.addPolynomial(tempFactor); // x0
			tempAddendant.multiplyByPolynomial(diffxBxA).multiplyByPolynomial(diffxBxA); // x0(xB - xA)^2
			xNum.addPolynomial(tempAddendant);
			tempAddendant = new SymbolicPolynomial();
			tempFactor = new SymbolicPolynomial();
			t = new SymbolicTerm(1);
			t.addPower(new Power(xA, 1));
			tempFactor.addTerm(t);
			tempAddendant.addPolynomial(tempFactor); // xA
			tempAddendant.multiplyByPolynomial(diffyByA).multiplyByPolynomial(diffyByA); // xA(yB - yA)^2
			xNum.addPolynomial(tempAddendant);
			tempAddendant = new SymbolicPolynomial();
			tempFactor = new SymbolicPolynomial();
			t = new SymbolicTerm(1);
			t.addPower(new Power(yA, 1));
			tempFactor.addTerm(t);
			tempAddendant.addPolynomial(tempFactor); // yA
			t = new SymbolicTerm(-1);
			t.addPower(new Power(y0, 1));
			tempFactor = new SymbolicPolynomial();
			tempFactor.addTerm(t);
			tempAddendant.addPolynomial(tempFactor); // yA - y0
			tempAddendant.multiplyByPolynomial(diffxBxA).multiplyByPolynomial(diffyByA); // (yA - y0)(xB - xA)(yB - yA)
			xNum.subtractPolynomial(tempAddendant);
			
			// xDen = (xB - xA)^2 + (yB - yA)^2
			SymbolicPolynomial xDen = (SymbolicPolynomial) diffxBxA.clone().multiplyByPolynomial(diffxBxA);
			xDen.addPolynomial((SymbolicPolynomial) diffyByA.clone().multiplyByPolynomial(diffyByA));
			
			// yNum = (xNum - xA*xDen)(yB - yA) + yA*xDen*(xB - xA)
			SymbolicPolynomial yNum = new SymbolicPolynomial();
			tempFactor = new SymbolicPolynomial();
			t = new SymbolicTerm(-1);
			t.addPower(new Power(xA, 1));
			tempFactor.addTerm(t); // -xA
			tempFactor.multiplyByPolynomial(xDen); // -xA*xDen
			yNum = (SymbolicPolynomial) xNum.clone().addPolynomial(tempFactor); // xNum - xA*xDen 
			yNum.multiplyByPolynomial(diffyByA); // (xNum - xA*xDen)(yB - yA)
			tempFactor = new SymbolicPolynomial();
			t = new SymbolicTerm(1);
			t.addPower(new Power(yA, 1));
			tempFactor.addTerm(t); // yA
			yNum.addPolynomial(tempFactor.multiplyByPolynomial(xDen).multiplyByPolynomial(diffxBxA)); // (xNum - xA*xDen)(yB - yA) + yA*xDen*(xB - xA)
			
			// yDen = xDen*(xB - xA)
			SymbolicPolynomial yDen = (SymbolicPolynomial) xDen.clone().multiplyByPolynomial(diffxBxA);
			
			// conditionForParabola = (xDen*yDen*x0 - yDen*xNum)^2 + (xDen*yDen*y0 - xDen*yNum)^2 - (M0F*xDen*yDen)^2
			tempAddendant = new SymbolicPolynomial();
			tempFactor = new SymbolicPolynomial();
			t = new SymbolicTerm(1);
			t.addPower(new Power(x0, 1));
			tempFactor.addTerm(t); // x0
			tempAddendant.addPolynomial(tempFactor.multiplyByPolynomial(xDen).multiplyByPolynomial(yDen)); // xDen*yDen*x0
			tempAddendant.subtractPolynomial(yDen.clone().multiplyByPolynomial(xNum)); // xDen*yDen*x0 - yDen*xNum
			conditionForParabola = (SymbolicPolynomial) tempAddendant.clone().multiplyByPolynomial(tempAddendant); // (xDen*yDen*x0 - yDen*xNum)^2
			tempAddendant = new SymbolicPolynomial();
			tempFactor = new SymbolicPolynomial();
			t = new SymbolicTerm(1);
			t.addPower(new Power(y0, 1));
			tempFactor.addTerm(t); // y0
			tempAddendant.addPolynomial(tempFactor.multiplyByPolynomial(xDen).multiplyByPolynomial(yDen)); // xDen*yDen*y0
			tempAddendant.subtractPolynomial(xDen.clone().multiplyByPolynomial(yNum)); // xDen*yDen*y0 - xDen*yNum
			conditionForParabola.addPolynomial((SymbolicPolynomial) tempAddendant.clone().multiplyByPolynomial(tempAddendant)); // (xDen*yDen*x0 - yDen*xNum)^2 + (xDen*yDen*y0 - xDen*yNum)^2
			conditionForParabola.subtractPolynomial(sdM0F.clone()
					            .multiplyByPolynomial(sdM0F)
					            .multiplyByPolynomial(xDen)
					            .multiplyByPolynomial(xDen)
					            .multiplyByPolynomial(yDen)
					            .multiplyByPolynomial(yDen)); // (xDen*yDen*x0 - yDen*xNum)^2 + (xDen*yDen*y0 - xDen*yNum)^2 - (M0F*xDen*yDen)^2
			
			/*
			 * IMPORTANT NOTE:
			 * 
			 *    This condition includes not only points from conic but other points from plane that
			 *    satisfy it. This is because we used squaring of equations to get free of square roots.
			 *    But condition like this is mostly useless for theorem proving since it doesn't
			 *    show the relationship among points from conic section but among points from wider
			 *    set of points. That is why some theorems can't be proved by using this condition. 
			 */
		}
	}
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * Method that sets the list of all points belonging to this parabola
	 * 
	 * @param points The points to set
	 */
	public void setPoints(Vector<Point> points) {
		this.points = points;
	}

	/**
	 * Method that retrieves the list of all points belonging to this parabola
	 * 
	 * @return the points
	 */
	public Vector<Point> getPoints() {
		return points;
	}
	
	/**
	 * Method that sets the focus of parabola
	 * 
	 * @param focus The focus of parabola to set
	 */
	public void setFocus(Point focus) {
		this.focus = focus;
	}

	/**
	 * Method that retrieves the focus of this parabola
	 * 
	 * @return the focus
	 */
	public Point getFocus() {
		return focus;
	}
	
	/**
	 * Method that sets the directrix of parabola
	 * 
	 * @param directrix The directrix of parabola to set
	 */
	public void setDirectrix(Line directrix) {
		this.directrix = directrix;
	}

	/**
	 * Method that retrieves the directrix of this parabola
	 * 
	 * @return the directrix
	 */
	public Line getDirectrix() {
		return directrix;
	}
	
	/**
	 * Method that retrieves the condition for some point
	 * to belong to this parabola
	 * 
	 * @return The condition
	 */
	public SymbolicPolynomial getCondition() {
		return conditionForParabola;
	}
	
	/**
	 * Method that gives the type of this construction
	 * 
	 * @see com.ogprover.prover_protocol.cp.geoconstruction.GeoConstruction#getConstructionType()
	 */
	@Override
	public int getConstructionType() {
		return GeoConstruction.GEOCONS_TYPE_PARABOLA;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	public Parabola(String parabolaLabel, Point focus, Line directrix) {
		this.geoObjectLabel = parabolaLabel;
		this.points = new Vector<Point>();
		this.focus = focus;
		this.directrix = directrix;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method that retrieves the instance of condition for points of this parabola
	 * expressed as x-polynomial (after symbolic coordinates of all points
	 * have been substituted by UX variables)
	 *  
	 * @param pointsMap		Map of points assigned to labels of common points from
	 * 						symbolic polynomial representing the condition,
	 * 						used for instantiation of that condition
	 * @return				X-polynomial representing the condition for points of this 
	 * 						conic in algebraic form or null in case of error
	 */
	public XPolynomial instantiateCondition(Map<String, Point> pointsMap) { 
		return OGPCP.instantiateCondition(this.getCondition(), pointsMap);
	}
	
	/**
	 * Method to check the validity of this construction step
	 * 
	 * @see com.ogprover.prover_protocol.cp.geoconstruction.GeoConstruction#isValidConstructionStep()
	 */
	@Override
	public boolean isValidConstructionStep() {
		OGPOutput output = OpenGeoProver.settings.getOutput();
		FileLogger logger = OpenGeoProver.settings.getLogger();
		
		if (!super.isValidConstructionStep())
			return false;
		
		try {
			// Construction of parabola is valid if both its focus and directrix are previously constructed
			Point focus = this.focus;
			Line directrix = this.directrix;
			int indexF, indexD;
		
			if (focus == null || directrix == null) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Parabola " + this.getGeoObjectLabel() + " can't be constructed because some of necessary elements is not constructed");
				return false;
			}
		
			indexF = focus.getIndex();
			indexD = directrix.getIndex();
		
			if (indexF < 0 || indexD < 0) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Parabola " + this.getGeoObjectLabel() + " can't be constructed because some of necessary elements is not added to construction protocol");
				return false; // some point is not in construction protocol
			}
		
			boolean valid = this.index > indexF && this.index > indexD;
			
			if (!valid) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Parabola " + this.getGeoObjectLabel() + " can't be constructed because some of necessary elements is not yet constructed");
			}
			
			return valid;
		} catch (IOException e) {
			logger.error("Failed to write to output file(s).");
			output.close();
			return false;
		}
	}
	
	/**
	 * Finding best points for instantiation.
	 * 
	 * @see com.ogprover.prover_protocol.cp.geoconstruction.SetOfPoints#findBestPointsForInstantation(com.ogprover.prover_protocol.cp.auxiliary.PointSetRelationshipManager)
	 */
	public int findBestPointsForInstantation(PointSetRelationshipManager manager) {
		FileLogger logger = OpenGeoProver.settings.getLogger();
		
		// First call method from superclass - it implements default behavior considering
		// this conic as conic section through five points
		super.findBestPointsForInstantation(manager);
		
		// in case of error exit
		if (manager.isErrorFlag()) {
			logger.error("Failed in findBestPointsForInstantation() method from superclass");
			return OGPConstants.ERR_CODE_GENERAL;
		}
		
		if (manager.getPoint().getPointState() == Point.POINT_STATE_RENAMED)
			return OGPConstants.RET_CODE_SUCCESS;
		
		// NOTE: This method is used to find best elements for process of transformation
		// in algebraic form. It is implemented by calling same methods like in
		// final transformation, but without printing of results to output reports.
		// Also, in order to be able to repeat the good transformation later in
		// final processing, it is necessary to cancel any result i.e. if a polynomial
		// is added to system of polynomials, it has to be removed, and if coordinates
		// are renamed, they have to be back to previous values.
		
		// turn the state of point P to UNCHANGED so it can be reset 
		// according to actions taken on it in this method
		manager.getPoint().setPointState(Point.POINT_STATE_UNCHANGED);
		manager.setCondition(Parabola.conditionForParabola);
		
		// instantiate the condition from this conic using its specific condition
		Point P = manager.getPoint();
		Map<String, Point> pointsMap = new HashMap<String, Point>(); // collection with current elements
		pointsMap.put(M0Label, P);
		pointsMap.put(FLabel, this.focus); // must be constructed before P since validation of CP would fail otherwise
		
		// Points A and B from symbolic condition for parabola are not used as equivalent, therefore we will
		// provide all possible variations of pairs.
		
		// Pass all points of parabola's directrix to search for first point from it
		for (int iA = 0, jA = this.points.size(); iA < jA; iA++) {
			Point pointA = this.directrix.getPoints().get(iA);
			// choose as given point one of those constructed before P
			if (pointA.getIndex() >= P.getIndex()) // A constructed after P or is P - skip it
				continue;
			
			// now pass points again to search for the second point
			for (int iB = 0, jB = this.points.size(); iB < jB; iB++) {
				Point pointB = this.points.get(iB);
				// skip point if constructed after P or is P or if it is point A 
				if (pointB.getIndex() >= P.getIndex() || pointB.getIndex() == pointA.getIndex())
					continue;
					
				pointsMap.put(ALabel, pointA);
				pointsMap.put(BLabel, pointB);
		
				manager.processPointsAndCondition(pointsMap);
		
				if (manager.isErrorFlag()) {
					logger.error("Failed in processing specific polynomial for conic with two foci and one point on it.");
					return OGPConstants.ERR_CODE_GENERAL;
				}
					
				// if polynomial that renames coordinates of point is found, stop further search
				if (manager.getPoint().getPointState() == Point.POINT_STATE_RENAMED)
					return OGPConstants.RET_CODE_SUCCESS;
			}
		}
		
		return OGPConstants.RET_CODE_SUCCESS;
	}
	
	/**
	 * @see com.ogprover.prover_protocol.cp.geoconstruction.SetOfPoints#instantiateConditionFromBasicElements(com.ogprover.prover_protocol.cp.geoconstruction.Point)
	 */
	@Override
	public XPolynomial instantiateConditionFromBasicElements(Point P) {
		Map<String, Point> pointsMap = new HashMap<String, Point>();
		pointsMap.put(M0Label, P);
		Point firstP = this.directrix.getPoints().get(0);
		Point secondP = this.directrix.getPoints().get(1);
		
		if (firstP == null || secondP == null) {
			OpenGeoProver.settings.getLogger().error("There are no two different points in parabola " + this.getGeoObjectLabel());
			return null;
		}
		pointsMap.put(ALabel, firstP);
		pointsMap.put(BLabel, secondP);
		pointsMap.put(FLabel, this.focus);
		
		//return this.instantiateCondition(pointsMap).reduceByUTermDivision();
		return this.instantiateCondition(pointsMap); // don't reduce polynomial
	}
	
	/**
	 * @see com.ogprover.prover_protocol.cp.geoconstruction.SetOfPoints#addPointToSet(com.ogprover.prover_protocol.cp.geoconstruction.Point)
	 */
	public void addPointToSet(Point P) {
		this.points.add(P);
	}
	
	/**
	 * Method that checks the validity of construction of some point from 
	 * parabola. It will be called from validation method for
	 * construction of some point from point set.
	 * 
	 * @param P	Point from parabola whose construction is being verified
	 * @return	True if construction is valid, false otherwise
	 */
	public boolean isParabolaPointConstructionValid(Point P) {
		FileLogger logger = OpenGeoProver.settings.getLogger();
		OGPOutput output = OpenGeoProver.settings.getOutput();
		
		try {
			// Construction of some point on parabola is valid if that conic has been 
			// already constructed and there are two points on directrix of that parabola;
			// if one point from parabola's directrix is missing, then new random point is chosen
		
			// check parabola's directrix
			Line directrix = this.getDirectrix();
			// since parabola has been already constructed, the validity
			// of that construction has already been verified; therefore its directrix
			// has been already constructed (it's not null and has at least one 
			// constructed point) and it is only required to check the second point
			Point secondPoint = directrix.getPoints().get(1); // get second point from parabola's directrix
			
			if (secondPoint == null || secondPoint.getIndex() < 0 || secondPoint.getIndex() >= P.getIndex()) {
				// there is no second point yet constructed - create random point with random label starting with GP - "General Point"
				RandomPointFromLine rndOnDirectrix = new RandomPointFromLine("GP#" + Math.round(Math.random()*1000), directrix);
				// add this construction to protocol right before this current construction
				output.openItemWithDesc("Info: ");
				output.closeItemWithDesc("Attempting to add the construction of new random point " + rndOnDirectrix.getGeoObjectLabel() + " necessary for completion of construction of point " + P.getGeoObjectLabel());
				this.consProtocol.addGeoConstruction(P.getIndex(), rndOnDirectrix); // here it will not be checked whether object with same name/label is already in CP since the probability for this event is zero
				// then validate this construction - it may generate new random point constructions
				if (rndOnDirectrix.isValidConstructionStep() == false)
					return false;
				
				output.openItemWithDesc("Warrning: ");
				StringBuilder sb = new StringBuilder();
				sb.append("Generated new random point ");
				sb.append(rndOnDirectrix.getGeoObjectLabel());
				sb.append(" on line ");
				sb.append(directrix.getGeoObjectLabel());
				sb.append(" in order to complete the construction of point ");
				sb.append(P.getGeoObjectLabel());
				sb.append(" on parabola ");
				sb.append(this.getGeoObjectLabel());
				output.closeItemWithDesc(sb.toString());
			}
		} catch (NullPointerException e) {
			logger.error("Parabola verified as correctly constructed, but some elements are null");
			try {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Unexpected error has occured during validity check for construction of point " + P.getGeoObjectLabel() + " on parabola " + this.getGeoObjectLabel());
			} catch (IOException e1) {
				logger.error("Failed to write to output file(s).");
				output.close();
			}
			return false;
		} catch (IOException e1) {
			logger.error("Failed to write to output file(s).");
			output.close();
			return false;
		}
		
		return true;
	}

	/**
	 * @see com.ogprover.prover_protocol.cp.geoconstruction.ConicSection#getAllPossibleConditionsWithMappings()
	 */
	@Override
	public Map<SymbolicPolynomial, ArrayList<Map<String, Point>>> getAllPossibleConditionsWithMappings() {
		Map<SymbolicPolynomial, ArrayList<Map<String, Point>>> retMap = super.getAllPossibleConditionsWithMappings();
		
		ArrayList<Map<String, Point>> allMappings = new ArrayList<Map<String, Point>>();
		Map<String, Point> pointsMap = null;
		
		// we must provide variations of two points A and B
		// since they are not equivalent in condition for parabola
		for (Point A : this.points) {
			for (Point B : this.points) {
				if (B.getGeoObjectLabel().equals(A.getGeoObjectLabel()))
					continue;
				
				pointsMap = new HashMap<String, Point>();
				pointsMap.put(ALabel, A);
				pointsMap.put(BLabel, B);
				pointsMap.put(FLabel, this.focus);
				allMappings.add(pointsMap);
			}
		}
		
		retMap.put((SymbolicPolynomial) Parabola.conditionForParabola.clone(), allMappings);
		return retMap;
	}

	/**
	 * @see com.ogprover.prover_protocol.cp.geoconstruction.GeoConstruction#getConstructionDesc()
	 */
	@Override
	public String getConstructionDesc() {
		StringBuilder sb = new StringBuilder();
		sb.append("Parabola ");
		sb.append(this.geoObjectLabel);
		sb.append(" with focus ");
		sb.append(this.focus.getGeoObjectLabel());
		sb.append(" and directrix ");
		sb.append(this.directrix.getGeoObjectLabel());
		return sb.toString();
	}

	
}
