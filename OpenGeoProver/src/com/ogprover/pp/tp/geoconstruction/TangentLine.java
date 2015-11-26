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
import com.ogprover.polynomials.Variable;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.pp.tp.auxiliary.PointSetRelationshipManager;
import com.ogprover.utilities.io.OGPOutput;
import com.ogprover.utilities.logger.ILogger;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for construction of tangent line from given point
*     to some set of points</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class TangentLine extends Line {
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
	 * <i><b>Symbolic label for generic point from this line</b></i>
	 */
	private static final String M0Label = "0"; // zero
	/**
	 * <i><b>Symbolic label for common (touch) point of tangent line and 
	 *       the underlying set of points</b></i>
	 */
	private static final String T0Label = "T0";
	/**
	 * Underlying set of points whose tangent is this line
	 */
	private SetOfPoints underlyingPointsSet = null;
	/**
	 * Index of touch point of this tangent line and its underlying
	 * set of points, in list of all points that belong to this line;
	 * if equals to -1 that means the touch point is not yet constructed/added
	 * to this line.
	 */
	private int indexOfTouchPoint = -1;
	/**
	 * Symbolic polynomial representing the condition for underlying
	 * set of points - used for calculation of condition for tangent line
	 */
	private SymbolicPolynomial conditionForUnderlyingSetOfPoints = null;
	
	
	
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
		return GeoConstruction.GEOCONS_TYPE_TANGENT;
	}
	
	/**
	 * @param pointsSet Underlying set of points to set
	 */
	public void setUnderlyingPointsSet(SetOfPoints pointsSet) {
		this.underlyingPointsSet = pointsSet;
	}

	/**
	 * @return the underlying set of points
	 */
	public SetOfPoints getUnderlyingPointsSet() {
		return this.underlyingPointsSet;
	}
	
	/**
	 * @param indexOfTouchPoint the indexOfTouchPoint to set
	 */
	public void setIndexOfTouchPoint(int indexOfTouchPoint) {
		this.indexOfTouchPoint = indexOfTouchPoint;
	}

	/**
	 * @return the indexOfTouchPoint
	 */
	public int getIndexOfTouchPoint() {
		return indexOfTouchPoint;
	}
	
	/**
	 * Method that retrieves the touch point of this tangent line and 
	 * its underlying set of points
	 * 
	 * @return	Touch point
	 */
	public Point getTouchPoint() {
		return this.points.get(this.indexOfTouchPoint);
	}

	/**
	 * Method that retrieves symbolic polynomial that represents the condition
	 * that some point belongs to this tangent line. There are multiple 
	 * conditions for same tangent line, depending on condition from underlying
	 * set of points that is chosen.
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.Line#getCondition()
	 */
	public SymbolicPolynomial getCondition() {
		/* 
		 * M0=(x0, y0) is some arbitrary point from tangent line;
		 * T0=(xT, yT) is common point of tangent line and set of points whose 
		 * tangent is that line (touch point).
		 * 
		 *  Let kn and kd be respectively numerator and denominator of first derivative 
		 *  by point T0 of symbolic polynomial which represents the condition for a point 
		 *  to belong to set of points assigned to this tangent line.
		 *  
		 *  Then equation of tangent line is 
		 *  	y = (kn/kd)*x + m 
		 *  or 
		 *  	kn*x - kd*y + kd*m = 0.
		 *  
		 *  Point T0 belongs to this line, hence:
		 *     kn*xT - kd*yT + kd*m = 0 => kd*m = kd*yT - kn*xT
		 *     
		 *  Therefore we obtain the equation of tangent line (condition for M0 to belong
		 *  to this line):
		 *     kn*(x0 - xT) - kd*(y0 - yT) = 0
		 *
		 */
		SymbolicPolynomial conditionForTangentLine = new SymbolicPolynomial();
			
		// Instances of symbolic variables
		SymbolicVariable x0 = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, M0Label);
		SymbolicVariable y0 = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, M0Label);
		SymbolicVariable xT = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, T0Label);
		SymbolicVariable yT = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, T0Label);
			
		// first of all calculate first derivative of condition for set of points
		if (this.conditionForUnderlyingSetOfPoints == null) {
			OpenGeoProver.settings.getLogger().error("Cannot calculate condition for tangent line because condition for underlying set of points is null");
			return null;
		}
		ArrayList<SymbolicPolynomial> derivative = ((SymbolicPolynomial)(this.conditionForUnderlyingSetOfPoints.clone())).calcFirstDerivativeByPoint(M0Label);
		// now replace all occurrences of label M0 in first derivative by label T0 of touch point
		// because M0 is general label for some point that belongs to a set of points, and here 
		// it has different special meaning - a point that belongs to this line; and also
		// T0 has to be a touch point of tangent and underlying points set so we have to 
		// calculate the derivative in T0.
		if (derivative.get(SymbolicPolynomial.FIRST_DERIVATIVE_NUMERATOR) == null ||
			derivative.get(SymbolicPolynomial.FIRST_DERIVATIVE_DENOMINATOR) == null) {
			StringBuilder sb = new StringBuilder();
			sb.append("First derivative of set of points ");
			sb.append(((GeoConstruction)this).getGeoObjectLabel());
			sb.append(" isn't calculated correcly. ");
			OpenGeoProver.settings.getLogger().error(sb.toString());
			return null;
		}
			
		derivative.get(SymbolicPolynomial.FIRST_DERIVATIVE_NUMERATOR).substitute(T0Label, M0Label);
		derivative.get(SymbolicPolynomial.FIRST_DERIVATIVE_DENOMINATOR).substitute(T0Label, M0Label);
			
		SymbolicPolynomial tempPoly = new SymbolicPolynomial();
		Term tempTerm = new SymbolicTerm(1);
		tempTerm.addPower(new Power(x0, 1));
		tempPoly.addTerm(tempTerm);
		tempTerm = new SymbolicTerm(-1);
		tempTerm.addPower(new Power(xT, 1));
		tempPoly.addTerm(tempTerm);
		conditionForTangentLine = (SymbolicPolynomial) derivative.get(SymbolicPolynomial.FIRST_DERIVATIVE_NUMERATOR).clone().multiplyByPolynomial(tempPoly); // kn*(x0 - xT)
		tempPoly = new SymbolicPolynomial();
		tempTerm = new SymbolicTerm(1);
		tempTerm.addPower(new Power(y0, 1));
		tempPoly.addTerm(tempTerm);
		tempTerm = new SymbolicTerm(-1);
		tempTerm.addPower(new Power(yT, 1));
		tempPoly.addTerm(tempTerm);
		conditionForTangentLine.subtractPolynomial((SymbolicPolynomial) derivative.get(SymbolicPolynomial.FIRST_DERIVATIVE_DENOMINATOR).clone().multiplyByPolynomial(tempPoly)); // kn*(x0 - xT) - kd*(y0 - yT)
		
		return conditionForTangentLine;
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
	 * @param basePoint		Base point from tangent line (one given point that this
	 * 						line is passing through)
	 * @param pointsSet		Set of points whose tangent is this line
	 */
	public TangentLine(String lineLabel, Point basePoint, SetOfPoints pointsSet) {
		this.geoObjectLabel = lineLabel;
		this.points = new Vector<Point>();
		if (basePoint != null)
			this.points.add(basePoint); // add at the end - basePoint is the first point of this line (index is zero)
		// although theoretically possible, it will be forbidden here to construct
		// tangent to line
		if (pointsSet instanceof Line) {
			OpenGeoProver.settings.getLogger().error("Attempt to set line as tangent's underlying set of points");
			this.underlyingPointsSet = null;
		}
		else
			this.underlyingPointsSet = pointsSet;
		
		if (this.underlyingPointsSet != null) {
			// if base point is also in passed in underlying set of points, it is touch point
			if (this.underlyingPointsSet.getPoints().indexOf(basePoint) > -1)
				this.indexOfTouchPoint = 0;
		}
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
			// Angle bisector is valid if vertex and points from
			// both rays have been already constructed 
			Point basePoint = this.points.get(0);
			int indexBP, indexPS;
		
			// All elements must exist
			if (basePoint == null || this.underlyingPointsSet == null) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Cannot construct tangent line " + this.getGeoObjectLabel() + " because some necessary elements are not constructed");
				return false;
			}
		
			indexBP = basePoint.getIndex();
			indexPS = ((GeoConstruction)this.underlyingPointsSet).getIndex();
		
			if (indexBP < 0 || indexPS < 0) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Cannot construct tangent line " + this.getGeoObjectLabel() + " because some necessary elements are not added to theorem protocol");
				return false; // some object is not in theorem protocol
			}
		
			if (indexBP >= this.index || indexPS >= this.index) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Cannot construct tangent line " + this.getGeoObjectLabel() + " because some necessary elements are not yet constructed");
				return false; // some object is not constructed before this line
			}
		
			// Validating touch point (check if index is -1 and if so provide some point);
			// touch point must be defined because it is necessary for instantiation of
			// condition for some point to belong to this tangent line
			if (this.indexOfTouchPoint == -1) {
				// check whether construction of touch point is in CP
				for (Point tempP : this.points) {
					int indexP = this.underlyingPointsSet.getPoints().indexOf(tempP);
					
					if (indexP > -1) {
						this.indexOfTouchPoint = this.points.indexOf(tempP);
						break;
					}
				}
				
				// if nothing has been found add construction of touch point right after the
				// construction of this tangent line
				if (this.indexOfTouchPoint == -1) {
					// new touch point with random label starting with GP - general point
					IntersectionPoint touchPoint = new IntersectionPoint("GP#" + Math.round(Math.random()*1000), this, this.underlyingPointsSet);
					output.openItemWithDesc("Info: ");
					output.closeItemWithDesc("Attempting to add the construction of touch point " + 
							                 touchPoint.getGeoObjectLabel() + 
							                 " of tangent line " + 
							                 this.geoObjectLabel + 
							                 " and set of points " + 
							                 ((GeoConstruction)this.underlyingPointsSet).getGeoObjectLabel());
					this.consProtocol.addGeoConstruction(this.index + 1, touchPoint);
					// validate new construction separately
					if (touchPoint.isValidConstructionStep() == false)
						return false;
					this.indexOfTouchPoint = this.points.indexOf(touchPoint);
					
					
					output.openItemWithDesc("Warrning: ");
					StringBuilder sb = new StringBuilder();
					sb.append("Created touch point ");
					sb.append(touchPoint.getGeoObjectLabel());
					sb.append(" of tangent line ");
					sb.append(this.getGeoObjectLabel());
					sb.append(" and set of points ");
					sb.append(((GeoConstruction)this.underlyingPointsSet).getGeoObjectLabel());
					output.closeItemWithDesc(sb.toString());
				}
			}
			return true;
		} catch (IOException e) {
			logger.error("Failed to write to output file(s).");
			output.close();
			return false;
		}
	}

	/**
	 * Method for finding best points for instantiation of condition
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.Line#findBestPointsForInstantation(com.ogprover.pp.tp.auxiliary.PointSetRelationshipManager)
	 */
	@Override
	public int findBestPointsForInstantation(PointSetRelationshipManager manager) {
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		// First call method from superclass - it implements default behavior considering
		// this line as plain line through two points
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
		Point P = manager.getPoint();
		P.setPointState(Point.POINT_STATE_UNCHANGED);
		
		// instantiate the condition from this line using its specific condition
		
		// first of all determine the M0 point instance
		Point m0Point = null;
		int limitPointCPIndex = -1; // this will be set to maximal index that some point, 
		                            // which can be accepted for instantiation, can have in CP
		
		if (P.getGeoObjectLabel().equals(this.getTouchPoint().getGeoObjectLabel())) {
			// we are instancing the condition for touch point - therefore use first point
			// as instance of M0 - that point must not be a touch point since touch point
			// can be constructed as intersection point of this tangent and underlying 
			// set of points only after construction of this tangent - therefore tangent
			// and its first point must be constructed before P
			if (this.indexOfTouchPoint == 0) {
				logger.error("findBestPointsForInstantation() method failed in processing condition for tangent line");
				return OGPConstants.ERR_CODE_GENERAL;
			}
			
			m0Point = this.points.get(0);
			limitPointCPIndex = P.getIndex(); // all points constructed before touch point + touch point can be used for instantiation
			// notice that we allow here any point for instantiation 
			// of condition from underlying set of points
			// to be same as touch point in case when P == touchPoint -
			// therefore e.g. it can be one of circle's points;
			// this is because conditions for touch point to belong to
			// this tangent line and to underlying set of points are independent
			// and when instantiating the condition for point set it will
			// not be allowed to choose touch point as one of given points 
			// since in that case we are calculating the condition for it to
			// be on that set of points
		}
		else { // use point from manager
			m0Point = P;
			limitPointCPIndex = P.getIndex() - 1; // all points constructed before point P can be used for instantiation
		}

		// then loop through various conditions and mappings
		Map<SymbolicPolynomial, ArrayList<Map<String, Point>>> allConditions = this.getAllPossibleConditionsWithMappings();
		
		// pass all possible conditions that could be applied to this line
		for (SymbolicPolynomial symbCond : allConditions.keySet()) {
			manager.setCondition((SymbolicPolynomial) symbCond.clone());
			
			boolean isCondForPlainLine = symbCond.equals(LineThroughTwoPoints.conditionForPlainLine);
			ArrayList<String> listOfLabels = symbCond.getAllPointLabels();
			ArrayList<Map<String, Point>> allMappings = allConditions.get(symbCond);
			Map<String, Point> pointsMap = null;
			
			for (Map<String, Point> map : allMappings) {
				boolean badMap = false;
				
				pointsMap = new HashMap<String, Point>();
				pointsMap.put(M0Label, m0Point.clone());
				
				// special logic if tangent line is considered as plain line through two points;
				// this is because in this case we do not use touch point T0 so it is not allowed 
				// to instantiate the condition for M0 with points constructed after it
				if (isCondForPlainLine) {
					// all points from map must be constructed before m0Point
					for (String label : listOfLabels) {
						// skip label M0Label since it is already put in map
						if (label.equals(M0Label))
							continue;
					
						Point match = map.get(label);
					
						// all points from map must be constructed before m0Point
						if (match == null || match.getIndex() >= m0Point.getIndex()) {
							badMap = true;
							break;
						}
					
						pointsMap.put(label, match.clone());
					}
				}
				else { // specific conditions only for tangent line
					for (String label : listOfLabels) {
						// skip label M0Label since it is already put in map
						if (label.equals(M0Label))
							continue;
					
						Point match = map.get(label);
					
						// for all labels except T0Label check index from CP of point for instantiation;
						// we skip T0Label since it is clear that always touch point instantiates it
						if (match == null || (!label.equals(T0Label) && match.getIndex() > limitPointCPIndex)) {
							badMap = true;
							break;
						}
					
						pointsMap.put(label, match.clone());
					}
				}
				
				if (badMap)
					continue;
				
				// found one good map - process the condition
				manager.processPointsAndCondition(pointsMap);
				
				if (manager.isErrorFlag()) {
					logger.error("findBestPointsForInstantation() method failed in processing condition for tangent line");
					return OGPConstants.ERR_CODE_GENERAL;
				}
				
				// if polynomial that renames coordinates of point is found, stop further search
				if (manager.getPoint().getPointState() == Point.POINT_STATE_RENAMED)
					return OGPConstants.RET_CODE_SUCCESS;
				
				P.setPointState(Point.POINT_STATE_UNCHANGED); // prepare for next loop step
			}
		}

		return OGPConstants.RET_CODE_SUCCESS;
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.SetOfPoints#instantiateConditionFromBasicElements(com.ogprover.pp.tp.geoconstruction.Point)
	 */
	public XPolynomial instantiateConditionFromBasicElements(Point P) {
		Map<SymbolicPolynomial, ArrayList<Map<String, Point>>> undMap = this.underlyingPointsSet.getAllPossibleConditionsWithMappings();
		SymbolicPolynomial undCond = this.underlyingPointsSet.getCondition(); // get basic condition for underlying point set
		
		this.conditionForUnderlyingSetOfPoints = undCond;
		SymbolicPolynomial tangCond = this.getCondition(); // tangent line will not be considered as plain line through two points but only
		                                                   // with its specific condition
		ArrayList<String> listOfLabels = tangCond.getAllPointLabels();
		
		ArrayList<Map<String, Point>> allMappings = undMap.get(undCond); 
		Point touchPoint = this.points.get(this.indexOfTouchPoint);
		Map<String, Point> pointsMap = null;
		
		// first of all determine the M0 point instance
		Point m0Point = null;
		int limitPointCPIndex = -1; // this will be set to maximal index that some point, 
        							// which can be accepted for instantiation, can have in CP
		
		if (P.getGeoObjectLabel().equals(touchPoint.getGeoObjectLabel())) {
			// we are instancing the condition for touch point - therefore use first point
			// as instance of M0 - that point must not be a touch point since touch point
			// can be constructed as intersection point of this tangent and underlying 
			// set of points only after construction of this tangent - therefore tangent
			// and its first point must be constructed before P
			if (this.indexOfTouchPoint == 0) {
				OpenGeoProver.settings.getLogger().error("instantiateConditionFromBasicElements() method failed in processing condition for tangent line");
				return null;
			}
			
			m0Point = this.points.get(0);
			limitPointCPIndex = P.getIndex(); // all points constructed before touch point + touch point can be used for instantiation
		}
		else { // use point from manager
			m0Point = P;
			limitPointCPIndex = P.getIndex() - 1; // all points constructed before point P can be used for instantiation
		}
		
		for (Map<String, Point> map : allMappings) {
			boolean badMap = false;
			
			pointsMap = new HashMap<String, Point>();
			pointsMap.put(M0Label, m0Point);
			pointsMap.put(T0Label, touchPoint);
			
			for (String label : listOfLabels) {
				// skip labels M0Label and T0Label 
				// since these points are already put in map
				if (label.equals(M0Label) || label.equals(T0Label))
					continue;
				
				Point match = map.get(label);
				
				if (match == null || match.getIndex() > limitPointCPIndex) {
					badMap = true;
					break;
				}
				
				pointsMap.put(label, match.clone());
			}
			
			if (badMap)
				continue;
			
			// found one good map - leave the loop
			break;
		}
		
		//return this.instantiateCondition(pointsMap).reduceByUTermDivision();
		return this.instantiateCondition(pointsMap); // don't reduce polynomial
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.Line#getAllPossibleConditionsWithMappings()
	 */
	@Override
	public Map<SymbolicPolynomial, ArrayList<Map<String, Point>>> getAllPossibleConditionsWithMappings() {
		Map<SymbolicPolynomial, ArrayList<Map<String, Point>>> retMap = super.getAllPossibleConditionsWithMappings();
		// take all mappings of underlying set of points - we can use it since the method
		// always creates new instance of this map
		Map<SymbolicPolynomial, ArrayList<Map<String, Point>>> undMap = this.underlyingPointsSet.getAllPossibleConditionsWithMappings();
		
		for (SymbolicPolynomial sp : undMap.keySet()) {
			// For each condition of underlying set of points
			// calculate condition for this tangent line and put in
			// collection
			this.conditionForUnderlyingSetOfPoints = sp; // first of all prepare condition of underlying set of points
			ArrayList<Map<String, Point>> allUndMappings = undMap.get(sp);
			
			for (Map<String, Point> map : allUndMappings)
				map.put(T0Label, this.points.get(this.indexOfTouchPoint)); // put touch point to the map
			
			retMap.put((SymbolicPolynomial) this.getCondition().clone(), allUndMappings);
		}
		
		return retMap;
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getConstructionDesc()
	 */
	@Override
	public String getConstructionDesc() {
		StringBuilder sb = new StringBuilder();
		sb.append("Tangent line ");
		sb.append(this.geoObjectLabel);
		sb.append(" through point ");
		sb.append(this.points.get(0).getGeoObjectLabel());
		sb.append(" of set of points ");
		sb.append(((GeoConstruction) this.underlyingPointsSet).getGeoObjectLabel());
		return sb.toString();
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getInputLabels()
	 */
	@Override
	public String[] getInputLabels() {
		String[] inputLabels = new String[2];
		inputLabels[0] = this.points.get(0).getGeoObjectLabel();
		inputLabels[1] = ((GeoConstruction)this.underlyingPointsSet).getGeoObjectLabel();
		return inputLabels;
	}
}


