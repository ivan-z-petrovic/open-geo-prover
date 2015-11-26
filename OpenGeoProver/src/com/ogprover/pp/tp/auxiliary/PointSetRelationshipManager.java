/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.auxiliary;

import java.util.HashMap;
import java.util.Map;

import com.ogprover.main.OGPConstants;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.SymbolicPolynomial;
import com.ogprover.polynomials.UXVariable;
import com.ogprover.polynomials.Variable;
import com.ogprover.polynomials.XPolySystem;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.ogprover.pp.tp.geoconstruction.SetOfPoints;
import com.ogprover.utilities.logger.ILogger;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class that connects a point and a set of points</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class PointSetRelationshipManager {
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
	 * Set of points that is connected to the single point from this relationship manager.
	 */
	private SetOfPoints set = null;
	/**
	 * Single point that is connected to the set of points from this relationship manager.
	 */
	private Point point = null;
	/**
	 * Error indicator - true means error happened, false is otherwise
	 */
	private boolean errorFlag = false;
	/**
	 * Symbolic polynomial for the set of points that is instantiated (sometimes a set 
	 * of points can have several conditions for a single point to belong to that 
	 * set of points)
	 */
	private SymbolicPolynomial condition = null;
	/**
	 * Map of points assigned to symbolic labels that are the best points for instantiation 
	 * of condition from set of points
	 */
	private Map<String, Point> bestPointsForInstantiation = null;
	/**
	 * Best instantiated polynomial (with lowest degree and smallest number of terms)
	 */
	private XPolynomial bestInstantiatedPolynomial = null;
	/**
	 * The degree of best instantiated polynomial
	 */
	private int degreeOfBestInstantiatedPolynomial = 0;
	/**
	 * Variable used to temporarily hold the X-coordinate of point
	 * so it can be restored after point has been renamed 
	 */
	private Variable xCoord = null;
	/**
	 * Variable used to temporarily hold the Y-coordinate of point
	 * so it can be restored after point has been renamed 
	 */
	private Variable yCoord = null;
	/**
	 * Type of manager - determines whether it is for transformation of
	 * geometry construction or theorem statement in algebraic form
	 */
	private int managerType;
	// Constants for manger types
	/**
	 * <i><b>
	 * Type of manager for transformation of geometry construction
	 * </b></i>
	 */
	public static final int MANAGER_TYPE_CONSTRUCTION = 0;
	/**
	 * <i><b>
	 * Type of manager for transformation of theorem statement
	 * </b></i>
	 */
	public static final int MANAGER_TYPE_STATEMENT = 1;
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @param set the set to set
	 */
	public void setSet(SetOfPoints set) {
		this.set = set;
	}

	/**
	 * @return the set
	 */
	public SetOfPoints getSet() {
		return set;
	}
	
	/**
	 * @param point the point to set
	 */
	public void setPoint(Point point) {
		this.point = point;
	}

	/**
	 * @return the point
	 */
	public Point getPoint() {
		return point;
	}

	/**
	 * @param errorFlag the errorFlag to set
	 */
	public void setErrorFlag(boolean errorFlag) {
		this.errorFlag = errorFlag;
	}

	/**
	 * @return the errorFlag
	 */
	public boolean isErrorFlag() {
		return errorFlag;
	}

	/**
	 * @param condition the condition to set
	 */
	public void setCondition(SymbolicPolynomial condition) {
		this.condition = condition;
	}

	/**
	 * @return the condition
	 */
	public SymbolicPolynomial getCondition() {
		return condition;
	}

	/**
	 * @param bestPointsForInstantiation the bestPointsForInstantiation to set
	 */
	public void setBestPointsForInstantiation(
			Map<String, Point> bestPointsForInstantiation) {
		this.bestPointsForInstantiation = bestPointsForInstantiation;
	}

	/**
	 * @return the bestPointsForInstantiation
	 */
	public Map<String, Point> getBestPointsForInstantiation() {
		return bestPointsForInstantiation;
	}

	/**
	 * @param bestInstantiatedPolynomial the bestInstantiatedPolynomial to set
	 */
	public void setBestInstantiatedPolynomial(XPolynomial bestInstantiatedPolynomial) {
		this.bestInstantiatedPolynomial = bestInstantiatedPolynomial;
	}

	/**
	 * @return the bestInstantiatedPolynomial
	 */
	public XPolynomial getBestInstantiatedPolynomial() {
		return bestInstantiatedPolynomial;
	}

	/**
	 * @param degreeOfBestInstantiatedPolynomial the degreeOfBestInstantiatedPolynomial to set
	 */
	public void setDegreeOfBestInstantiatedPolynomial(
			int degreeOfBestInstantiatedPolynomial) {
		this.degreeOfBestInstantiatedPolynomial = degreeOfBestInstantiatedPolynomial;
	}

	/**
	 * @return the degreeOfBestInstantiatedPolynomial
	 */
	public int getDegreeOfBestInstantiatedPolynomial() {
		return degreeOfBestInstantiatedPolynomial;
	}
	
	/**
	 * @param xCoord the xCoord to set
	 */
	public void setxCoord(Variable xCoord) {
		this.xCoord = xCoord;
	}

	/**
	 * @return the xCoord
	 */
	public Variable getxCoord() {
		return xCoord;
	}

	/**
	 * @param yCoord the yCoord to set
	 */
	public void setyCoord(Variable yCoord) {
		this.yCoord = yCoord;
	}

	/**
	 * @return the yCoord
	 */
	public Variable getyCoord() {
		return yCoord;
	}

	/**
	 * @param managerType the managerType to set
	 */
	public void setManagerType(int managerType) {
		this.managerType = managerType;
	}

	/**
	 * @return the managerType
	 */
	public int getManagerType() {
		return managerType;
	}
	
	

	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * 
	 * @param set		Set of points
	 * @param point		Single point
	 */
	public PointSetRelationshipManager(SetOfPoints set, Point point) {
		this.set = set;
		this.setPoint(point);
		this.managerType = PointSetRelationshipManager.MANAGER_TYPE_CONSTRUCTION; // default type
	}
	
	/**
	 * Constructor method
	 * 
	 * @param set			Set of points
	 * @param point			Single point
	 * @param managerType	Type of manager
	 */
	public PointSetRelationshipManager(SetOfPoints set, Point point, int managerType) {
		this.set = set;
		this.setPoint(point);
		this.managerType = managerType;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method for clean of this object before finding new best points for
	 * instantiation of condition.
	 */
	public void clear() {
		this.bestInstantiatedPolynomial = null;
		this.degreeOfBestInstantiatedPolynomial = 0;
		this.bestPointsForInstantiation = null;
		this.condition = null;
		this.errorFlag = false;
	}
	
	/**
	 * Method that retrieves instantiated condition for the point to
	 * belong to the set of points from this manager.
	 * 
	 * @return	XPolynomial object representing the instantiated condition
	 */
	public XPolynomial retrieveInstantiatedCondition() {
		int oldIndex = -1; //  index of point in CP
		
		/* For manager used for statements we temporarily change
		 * the index of point:
		 * It will be necessary to instantiate a condition for point
		 * to belong to point set. Unlike constructions where the order
		 * of constructions has to be respected when instantiating
		 * conditions for points (which is done by considering indices of
		 * objects in theorem protocol), for statements all constructed
		 * objects are of same importance. Therefore it would be necessary
		 * to provide mechanism to check all objects from theorem 
		 * protocol when instantiating a condition for theorem statement.
		 * This could be achieved by setting index of point temporarily 
		 * to some enough high value so all other objects from protocol 
		 * could have indices less then that index. This way we would be
		 * able to use same methods as for constructions but with slightly
		 * different results.
		 */
		if (this.managerType == PointSetRelationshipManager.MANAGER_TYPE_STATEMENT) {
			oldIndex = this.point.getIndex(); // save old index of object from CP
			this.point.setIndex(1000000);     // set new index to some enough high value;
			                                  // notice that index in CP is actually not changed
			                                  // just the value of that index in object
		}
		
		
		// This will find best points for instantiation 
        // and set the best polynomial which is instantiated condition;
		// no output will be written from this function since
		// it will test various combinations of points from set 
		// for instantiation of condition
		this.set.findBestPointsForInstantation(this);
		
		// after instantiation, restore point's index
		if (this.managerType == PointSetRelationshipManager.MANAGER_TYPE_STATEMENT) {
			this.point.setIndex(oldIndex);
		}
		
		return this.bestInstantiatedPolynomial;
	}
	
	/**
	 * Preparation method for finding best points for instantiation of condition
	 */
	public void prepareForFirstInstantiation() {
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		// check if point has already been instantiated
		if (this.point.getPointState() == Point.POINT_STATE_INITIALIZED) {
			logger.error("Point not yet instantiated");
			this.setErrorFlag(true);
			return;
		}
		
		// Coordinates of point P could be renamed during this test.
		// In order to be able to repeat the action of renaming in real
		// processing of that point, we should save here its coordinates
		// and return to them at the end.
		this.xCoord = this.point.getX().clone();
		this.yCoord = this.point.getY().clone();
		
		
		// turn the state of point P to UNCHANGED so it can be reset 
		// according to actions taken on it in this method
		this.point.setPointState(Point.POINT_STATE_UNCHANGED);
		
		this.clear();
	}
	
	/**
	 * Core method that is invoked from within method for finding 
	 * best points for instantiation. It processes condition for
	 * current passed in map of points and checks whether these points
	 * make new best points for instantiation (by comparing obtained
	 * instantiated polynomial). Since it just checks one possible 
	 * collection of points and results will be reset, no text will
	 * be written to output file from this function.
	 * 
	 * @param pointsMap		Passed in map of points for instantiation
	 */
	public void processPointsAndCondition(Map<String, Point> pointsMap) {
		if (this.managerType == PointSetRelationshipManager.MANAGER_TYPE_CONSTRUCTION)
			this.processPointsAndConditionForConstruction(pointsMap);
		else if (this.managerType == PointSetRelationshipManager.MANAGER_TYPE_STATEMENT)
			this.processPointsAndConditionForStatement(pointsMap);
		else
			OpenGeoProver.settings.getLogger().error("Wrong manager type!");
	}
	
	/**
	 * Method that processes the condition for geometry construction.
	 * 
	 * @param pointsMap		Passed in map of points for instantiation
	 */
	public void processPointsAndConditionForConstruction(Map<String, Point> pointsMap) {
		ILogger logger = OpenGeoProver.settings.getLogger();
		Map<String, Point> bestElements = this.getBestPointsForInstantiation();
		XPolynomial bestPolynomial = this.getBestInstantiatedPolynomial();
		
		// instantiate the condition and simplify it
		XPolynomial insCondition1 = OGPTP.instantiateCondition(this.condition, pointsMap).reduceByUTermDivision();
		// process the condition (check for special forms of instantiated polynomial);
		// since this is test processing, do not print result of processing to output file 
		int retCode = this.point.processConstructionPolynomial(insCondition1, false); // this can change the state of point from manager
		
		switch (retCode) {
		case Point.PROCESSPOLY_RETCODE_BAD_POLYNOMIAL:
		case OGPConstants.ERR_CODE_GENERAL:
			logger.error("Error or bad polynomial instantiated when was searching for best elements");
			this.setErrorFlag(true);
			return;
		case Point.PROCESSPOLY_RETCODE_TRY_AGAIN:
			logger.info("Wrong polynomial but will try again");
			// re-instantiate half-dependent point by switching its coordinates
			this.point.getConsProtocol().decrementUIndex();
			this.point.getConsProtocol().decrementXIndex();
			if (this.point.getInstanceType() == Point.POINT_TYPE_X_INDEPENDENT)
				this.point.getConsProtocol().instantiatePoint(this.point, Point.POINT_TYPE_Y_INDEPENDENT); // this will change the state of point P
			else if (this.point.getInstanceType() == Point.POINT_TYPE_Y_INDEPENDENT)
				this.point.getConsProtocol().instantiatePoint(this.point, Point.POINT_TYPE_X_INDEPENDENT); // this will change the state of point P
			else {
				logger.error("Wrong instance type of point");
				this.setErrorFlag(true);
				return;
			}
			
			//save coordinates
			this.xCoord = this.point.getX().clone();
			this.yCoord = this.point.getY().clone();
			
			// again instantiate the condition and process it
			XPolynomial insCondition2 = OGPTP.instantiateCondition(this.condition, pointsMap).reduceByUTermDivision();
			int newRetCode = this.point.processConstructionPolynomial(insCondition2, false); // this can change the state of point P
			
			switch (newRetCode) {
			case Point.PROCESSPOLY_RETCODE_BAD_POLYNOMIAL:
			case OGPConstants.ERR_CODE_GENERAL:
			case Point.PROCESSPOLY_RETCODE_TRY_AGAIN:
				logger.error("Error or bad polynomial instantiated when was searching for best elements in second attempt");
				this.setErrorFlag(true);
				return;
			case Point.PROCESSPOLY_RETCODE_COORDINATES_RENAMED:
				logger.info("Found best elements in second attempt that will rename coordinates of point " + this.point.getGeoObjectLabel());
				
				if (bestElements == null) {
					bestElements = new HashMap<String, Point>();
					this.setBestPointsForInstantiation(bestElements);
				}
				
				bestPolynomial = insCondition2;
				this.setBestInstantiatedPolynomial(bestPolynomial);
				this.setDegreeOfBestInstantiatedPolynomial(bestPolynomial.getPolynomialDegree());
				
				for (String key : pointsMap.keySet()) {
					String newKey = new String(key);
					bestElements.put(newKey, pointsMap.get(key).clone());
				}
				
				// return old coordinates to point P and increment x index since renaming is undo
				this.point.setX((UXVariable)this.xCoord.clone());
				this.point.setY((UXVariable)this.yCoord.clone());
				this.point.getConsProtocol().incrementXIndex();
				return;
			case Point.PROCESSPOLY_RETCODE_ADDED_TO_SYSTEM:
				logger.info("Found elements in second attempt that will generate new polynomial that will be added to system of hypotheses");
				// since new polynomial has been already added to CP, remove it now since this
				// is just test processing
				XPolySystem system = this.point.getConsProtocol().getAlgebraicGeoTheorem().getHypotheses();
				system.removePoly(system.getPolynomials().size() - 1);
				
				if (bestElements == null) {
					bestElements = new HashMap<String, Point>();
					this.setBestPointsForInstantiation(bestElements);
				}
				
				// save these current elements as best if no one has been found so far
				if (bestPolynomial == null) {
					bestPolynomial = insCondition2;
					this.setBestInstantiatedPolynomial(bestPolynomial);
					this.setDegreeOfBestInstantiatedPolynomial(bestPolynomial.getPolynomialDegree());
					for (String key : pointsMap.keySet()) {
						String newKey = new String(key);
						bestElements.put(newKey, pointsMap.get(key).clone());
					}
				}
				else {
					int condDegree = insCondition2.getPolynomialDegree();
					
					// these elements are better if polynomial degree is lower or
					// if it is equals with current best degree but new polynomial has less terms;
					// i.e. these elements are better if new polynomial is simpler 
					if (condDegree < this.getDegreeOfBestInstantiatedPolynomial() || 
						(condDegree == this.getDegreeOfBestInstantiatedPolynomial() && insCondition2.getTerms().size() < bestPolynomial.getTerms().size())) {
						bestPolynomial = insCondition2;
						this.setBestInstantiatedPolynomial(bestPolynomial);
						this.setDegreeOfBestInstantiatedPolynomial(condDegree);
						for (String key : pointsMap.keySet()) {
							String newKey = new String(key);
							bestElements.put(newKey, pointsMap.get(key).clone());
						}
					}
				}
				break;
			default:
				logger.error("Unknown return result from processing of condition in second attempt");
				this.setErrorFlag(true);
				return;
			}
			break;
		case Point.PROCESSPOLY_RETCODE_COORDINATES_RENAMED:
			logger.info("Found best elements that will rename coordinates of point " + this.point.getGeoObjectLabel());
			if (bestElements == null) {
				bestElements = new HashMap<String, Point>();
				this.setBestPointsForInstantiation(bestElements);
			}
			
			bestPolynomial = insCondition1;
			this.setBestInstantiatedPolynomial(bestPolynomial);
			this.setDegreeOfBestInstantiatedPolynomial(bestPolynomial.getPolynomialDegree());
			
			for (String key : pointsMap.keySet()) {
				String newKey = new String(key);
				bestElements.put(newKey, pointsMap.get(key).clone());
			}
			// return old coordinates to point P and increment x index since renaming is undo
			this.point.setX((UXVariable)this.xCoord.clone());
			this.point.setY((UXVariable)this.yCoord.clone());
			this.point.getConsProtocol().incrementXIndex();
			return;
		case Point.PROCESSPOLY_RETCODE_ADDED_TO_SYSTEM:
			logger.info("Found elements that will generate new polynomial that will be added to system of hypotheses");
			// since new polynomial has been already added to CP, remove it now since this
			// is just test processing
			XPolySystem system = this.point.getConsProtocol().getAlgebraicGeoTheorem().getHypotheses();
			system.removePoly(system.getPolynomials().size() - 1);
			
			if (bestElements == null) {
				bestElements = new HashMap<String, Point>();
				this.setBestPointsForInstantiation(bestElements);
			}
			
			// save these current elements as best if no one has been found so far
			if (bestPolynomial == null) {
				bestPolynomial = insCondition1;
				this.setBestInstantiatedPolynomial(bestPolynomial);
				this.setDegreeOfBestInstantiatedPolynomial(bestPolynomial.getPolynomialDegree());
				for (String key : pointsMap.keySet()) {
					String newKey = new String(key);
					bestElements.put(newKey, pointsMap.get(key).clone());
				}
			}
			else {
				int condDegree = insCondition1.getPolynomialDegree();
				
				// these elements are better if polynomial degree is lower or
				// if it is equals with current best degree but new polynomial has less terms;
				// i.e. these elements are better if new polynomial is simpler 
				if (condDegree < this.getDegreeOfBestInstantiatedPolynomial() || 
					(condDegree == this.getDegreeOfBestInstantiatedPolynomial() && insCondition1.getTerms().size() < bestPolynomial.getTerms().size())) {
					bestPolynomial = insCondition1;
					this.setBestInstantiatedPolynomial(bestPolynomial);
					this.setDegreeOfBestInstantiatedPolynomial(condDegree);
					for (String key : pointsMap.keySet()) {
						String newKey = new String(key);
						bestElements.put(newKey, pointsMap.get(key).clone());
					}
				}
			}
			break;
		default:
			logger.error("Unknown return result from processing of condition");
			this.setErrorFlag(true);
			return;
		}
	}
	
	/**
	 * Method that processes the condition for theorem statement.
	 * 
	 * @param pointsMap		Passed in map of points for instantiation
	 */
	public void processPointsAndConditionForStatement(Map<String, Point> pointsMap) {
		Map<String, Point> bestElements = this.getBestPointsForInstantiation();
		XPolynomial bestPolynomial = this.getBestInstantiatedPolynomial();
		
		/*
		// instantiate the condition and simplify it
		XPolynomial insCondition = OGPCP.instantiateCondition(this.condition, pointsMap).reduceByUTermDivision();
		*/
		
		// instantiate the condition without simplification
		XPolynomial insCondition = OGPTP.instantiateCondition(this.condition, pointsMap);
		
		
		if (bestElements == null) {
			bestElements = new HashMap<String, Point>();
			this.setBestPointsForInstantiation(bestElements);
		}
		
		// save these current elements as best if no one has been found so far
		if (bestPolynomial == null) {
			this.setBestInstantiatedPolynomial(insCondition);
			this.setDegreeOfBestInstantiatedPolynomial(insCondition.getPolynomialDegree());
			for (String key : pointsMap.keySet()) {
				String newKey = new String(key);
				bestElements.put(newKey, pointsMap.get(key).clone());
			}
		}
		else {
			int condDegree = insCondition.getPolynomialDegree();
			
			// these elements are better if polynomial degree is lower or
			// if it is equals with current best degree but new polynomial has less terms;
			// i.e. these elements are better if new polynomial is simpler 
			if (condDegree < this.getDegreeOfBestInstantiatedPolynomial() || 
				(condDegree == this.getDegreeOfBestInstantiatedPolynomial() && insCondition.getTerms().size() < bestPolynomial.getTerms().size())) {
				this.setBestInstantiatedPolynomial(insCondition);
				this.setDegreeOfBestInstantiatedPolynomial(condDegree);
				for (String key : pointsMap.keySet()) {
					String newKey = new String(key);
					bestElements.put(newKey, pointsMap.get(key).clone());
				}
			}
		}
	}
}