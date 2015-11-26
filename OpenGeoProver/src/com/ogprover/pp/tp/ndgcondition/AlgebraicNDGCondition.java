/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.ndgcondition;

import java.util.Collection;
import java.util.Map;
import java.util.Vector;

import com.ogprover.main.OGPConstants;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.UXVariable;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.auxiliary.FourPointsPositionChecker;
import com.ogprover.pp.tp.auxiliary.PointListManager;
import com.ogprover.pp.tp.auxiliary.PointsPositionChecker;
import com.ogprover.pp.tp.auxiliary.ThreePointsPositionChecker;
import com.ogprover.pp.tp.auxiliary.TwoPointsPositionChecker;
import com.ogprover.pp.tp.geoconstruction.FreePoint;
import com.ogprover.pp.tp.geoconstruction.GeoConstruction;
import com.ogprover.pp.tp.geoconstruction.ParametricSet;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.ogprover.pp.tp.geoconstruction.RandomPointFromSetOfPoints;
import com.ogprover.utilities.logger.ILogger;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for NDG (Non-degenerative) condition</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class AlgebraicNDGCondition {
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
	 * Theorem protocol that contains this NDG condition
	 */
	private OGPTP consProtocol = null;
	/**
	 * Polynomial form of this NDG condition
	 */
	private XPolynomial polynomial = null;
	/**
	 * Lists of point combinations that are candidates
	 * for making this NDG condition when they are in 
	 * some specific position.
	 */
	private Vector<Vector<Point>> pointLists = null;
	/**
	 * Text that describes position of points which generate
	 * this NDG condition. If this cannot be found it will remain
	 * empty and polynomial will be displayed as text by default.
	 * It is list of all possible descriptions.
	 */
	private Vector<String> textList = null;
	// Elements for best description of this NDG condition
	/**
	 * Text that best describes this NDG condition.
	 */
	private String bestDescription = null;
	/**
	 * List of points whose special position best describes this NDG condition.
	 */
	private Vector<Point> bestPointList = null;
	/**
	 * Number of free points in list of points whose special 
	 * position best describes this NDG condition.
	 */
	private int numOfFreePts = 0;
	/**
	 * Number of random points in list of points whose special 
	 * position best describes this NDG condition.
	 */
	private int numOfRndPts = 0;
	/**
	 * Number of dependent points in list of points whose special 
	 * position best describes this NDG condition.
	 */
	private int numOfDependentPts = 0;
	/**
	 * Number of all points in list of points whose special 
	 * position best describes this NDG condition. It is equal
	 * to sum of free, random and dependent points.
	 */
	private int numOfAllPts = 0;
	
	/*
	 * Types of NDG conditions
	 */
	public static final String NDG_TYPE_POLYNOMIAL			= "IsPolynomial";
	public static final String NDG_TYPE_2PT_IDENTICAL		= "AreEqual";
	public static final String NDG_TYPE_3PT_COLLINEAR		= "AreCollinear";
	public static final String NDG_TYPE_3PT_MIDPOINT		= "IsMidpointOf";
	public static final String NDG_TYPE_3PT_ON_PERP_BIS		= "IsOnBisector";
	public static final String NDG_TYPE_3PT_RIGHT_ANG		= "ArePerpendicular";
	public static final String NDG_TYPE_3PT_ON_CIRCLE		= "IsOnCircle";
	public static final String NDG_TYPE_3PT_SEG_SUM			= "IsSumOf";
	public static final String NDG_TYPE_4PT_COLLINEAR		= "AreCollinear";
	public static final String NDG_TYPE_4PT_CONCYCLIC		= "AreConcyclic";
	public static final String NDG_TYPE_4PT_EQ_SEG			= "AreEqual";
	public static final String NDG_TYPE_4PT_PARALLEL		= "AreParallel";
	public static final String NDG_TYPE_4PT_PERPENDICULAR	= "ArePerpendicular";
	public static final String NDG_TYPE_4PT_HARMONIC		= "AreHarmonic";
	public static final String NDG_TYPE_4PT_CONG_COLL_SEG	= "AreCollinearCongruentSegments"; // ?
	public static final String NDG_TYPE_4PT_ON_ANG_BIS		= "IsOnAngleBisector";
	public static final String NDG_TYPE_4PT_2_ON_CIRCLE		= "AreOnCircle";
	public static final String NDG_TYPE_4PT_ON_CIRCLE		= "AreOnCircle";
	public static final String NDG_TYPE_4PT_INVERSE			= "AreInverses";
	public static final String NDG_TYPE_4PT_2_ON_PERP_BIS	= "AreOnPerpendicularBisector"; // ??
	public static final String NDG_TYPE_4PT_TOUCH_CIRCLES	= "AreTouchingCircles"; // ??
	// TODO - other types of NDGs ...
	
	/**
	 * Type of this NDG condition - one of NDG_TYPE_xxx constants.
	 */
	private String ndgType;
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @param consProtocol the consProtocol to set
	 */
	public void setConsProtocol(OGPTP consProtocol) {
		this.consProtocol = consProtocol;
	}
	
	/**
	 * @return the consProtocol
	 */
	public OGPTP getConsProtocol() {
		return consProtocol;
	}

	/**
	 * @param polynomial the polynomial to set
	 */
	public void setPolynomial(XPolynomial polynomial) {
		this.polynomial = polynomial;
	}
	
	/**
	 * @return the polynomial
	 */
	public XPolynomial getPolynomial() {
		return polynomial;
	}

	/**
	 * @param pointLists the pointLists to set
	 */
	public void setPointLists(Vector<Vector<Point>> pointLists) {
		this.pointLists = pointLists;
	}
	
	/**
	 * @return the pointLists
	 */
	public Vector<Vector<Point>> getPointLists() {
		return pointLists;
	}
	
	/**
	 * @param textL the text list to set
	 */
	public void setText(Vector<String> textL) {
		this.textList = textL;
	}

	/**
	 * @return the text
	 */
	public Vector<String> getText() {
		return textList;
	}
	
	/**
	 * @param bestDescription the bestDescription to set
	 */
	public void setBestDescription(String bestDescription) {
		this.bestDescription = bestDescription;
	}

	/**
	 * @return the bestDescription
	 */
	public String getBestDescription() {
		return bestDescription;
	}

	/**
	 * @param bestPointList the bestPointList to set
	 */
	public void setBestPointList(Vector<Point> bestPointList) {
		this.bestPointList = bestPointList;
	}
	
	/**
	 * @return the bestPointList
	 */
	public Vector<Point> getBestPointList() {
		return bestPointList;
	}

	/**
	 * @param numOfFreePts the numOfFreePts to set
	 */
	public void setNumOfFreePts(int numOfFreePts) {
		this.numOfFreePts = numOfFreePts;
	}

	/**
	 * @return the numOfFreePts
	 */
	public int getNumOfFreePts() {
		return numOfFreePts;
	}

	/**
	 * @param numOfRndPts the numOfRndPts to set
	 */
	public void setNumOfRndPts(int numOfRndPts) {
		this.numOfRndPts = numOfRndPts;
	}

	/**
	 * @return the numOfRndPts
	 */
	public int getNumOfRndPts() {
		return numOfRndPts;
	}

	/**
	 * @param numOfDependentPts the numOfDependentPts to set
	 */
	public void setNumOfDependentPts(int numOfDependentPts) {
		this.numOfDependentPts = numOfDependentPts;
	}

	/**
	 * @return the numOfDependentPts
	 */
	public int getNumOfDependentPts() {
		return numOfDependentPts;
	}

	/**
	 * @param numOfAllPts the numOfAllPts to set
	 */
	public void setNumOfAllPts(int numOfAllPts) {
		this.numOfAllPts = numOfAllPts;
	}

	/**
	 * @return the numOfAllPts
	 */
	public int getNumOfAllPts() {
		return numOfAllPts;
	}
	
	/**
	 * @return the ndgType
	 */
	public String getNdgType() {
		return ndgType;
	}

	/**
	 * @param ndgType the ndgType to set
	 */
	public void setNdgType(String ndgType) {
		this.ndgType = ndgType;
	}
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Default Constructor method
	 */
	public AlgebraicNDGCondition(XPolynomial ndgPoly){
		this.polynomial = ((XPolynomial) ndgPoly.clone()).reduceUTerms(false); // partial reduction of u-terms
		this.ndgType = AlgebraicNDGCondition.NDG_TYPE_POLYNOMIAL;
	}
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method for adding new text in list of strings that describe positions
	 * of points that make this NDG condition. Also, best description is updated.
	 * 
	 * @param ndgType	Type of this NDG condition - one of NDG_TYPE_xxx constants.
	 * @param pointList	List of points whose special position generates this NDG condition.
	 */
	public void addNewTranslation(String ndgType, Vector<Point> pointList) {
		// Assumption: arguments are not null.
		
		if (this.textList == null)
			this.textList = new Vector<String>();
		
		String textStr = AlgebraicNDGCondition.getNDGConditionText(ndgType, pointList);
		this.textList.add(textStr);
		
		// Update elements for best description
		if (this.bestDescription == null) {
			this.ndgType = ndgType;
			this.bestDescription = textStr;
			this.bestPointList = pointList;
			this.numOfAllPts = pointList.size();
			this.numOfFreePts = 0;
			this.numOfRndPts = 0;
			this.numOfDependentPts = 0;
			
			for (Point pt : pointList) {
				if (pt instanceof FreePoint)
					this.numOfFreePts++;
				else if (pt instanceof RandomPointFromSetOfPoints)
					this.numOfRndPts++;
				else
					this.numOfDependentPts++;
			}
		}
		else {
			/* 
			 * New list is better choice if, first of all, it has less number
			 * of dependent points, then if it has more free points, than if
			 * it is of less size, and finally if text of position is shorter.
			 */
			int newNumOfAllPts = pointList.size();
			int newNumOfFreePts = 0, newNumOfRndPts = 0, newNumOfDependentPts = 0;
			
			for (Point pt : pointList) {
				if (pt instanceof FreePoint)
					newNumOfFreePts++;
				else if (pt instanceof RandomPointFromSetOfPoints)
					newNumOfRndPts++;
				else
					newNumOfDependentPts++;
			}
			
			if (this.numOfDependentPts > newNumOfDependentPts ||
				this.numOfFreePts < newNumOfFreePts ||
				this.numOfAllPts > newNumOfAllPts ||
				this.bestDescription.length() > textStr.length()) {
				this.numOfAllPts = newNumOfAllPts;
				this.numOfFreePts = newNumOfFreePts;
				this.numOfRndPts = newNumOfRndPts;
				this.numOfDependentPts = newNumOfDependentPts;
				this.ndgType = ndgType;
				this.bestDescription = textStr;
				this.bestPointList = pointList;
			}
		}
	}
	
	/**
	 * Method that populates lists of points of this NDG condition object
	 * with all combinations of points that could make this NDG condition.
	 * 
	 * @return	SUCCESS if successful, general error code otherwise
	 */
	private int populatePointLists() {
		ILogger logger = OpenGeoProver.settings.getLogger();
		Vector<UXVariable> varList = this.polynomial.extractAllVariables();
		
		if (varList == null) {
			logger.error("Failed to extract variables from polynomial of NDG condition");
			return OGPConstants.ERR_CODE_GENERAL;
		}
		
		Map<UXVariable, Vector<Point>> pointsOfVars = this.consProtocol.getPointsAssociatedWithVariables(varList);
		
		if (pointsOfVars == null) {
			logger.error("Failed to retrieve points from CP associated to list of extracted variables");
			return OGPConstants.ERR_CODE_GENERAL;
		}
		
		Collection<Vector<Point>> pointsOfVarsC = pointsOfVars.values();
		
		if (pointsOfVarsC == null) {
			logger.error("Failed to extract lists of points from map with variables and their associated points.");
			return OGPConstants.ERR_CODE_GENERAL;
		}
		
		Vector<Vector<Point>> pointsOfVarsV = new Vector<Vector<Point>>(pointsOfVarsC);
		
		// create points combinations of each list
		Vector<Vector<Vector<Point>>> combinationsV = new Vector<Vector<Vector<Point>>>();
		
		for (Vector<Point> comb : pointsOfVarsV)
			combinationsV.add(PointListManager.createListOfCombinations(comb));
		
		// merge all these points combinations
		boolean bFirst = true;
		Vector<Vector<Point>> mergedPoints = new Vector<Vector<Point>>();
		
		for (Vector<Vector<Point>> comb : combinationsV) {
			if (bFirst) {
				mergedPoints = comb;
				bFirst = false;
			}
			else {
				mergedPoints = PointListManager.mergePairsOfPointCombinations(mergedPoints, comb);
			}
		}
		
		// finally merge these points with zero points of CP
		mergedPoints = PointListManager.mergePairsOfPointCombinations(mergedPoints, this.consProtocol.getZeroPoints());
		
		// Now populate lists of points only with relevant lists - 
		// that are within specified boundaries
		this.pointLists = new Vector<Vector<Point>>();
		
		for (Vector<Point> vp : mergedPoints) {
			int vpsiz = vp.size();
			
			if (vpsiz >= OGPConstants.MIN_NUM_OF_NDGC_POINTS && 
				vpsiz <= OGPConstants.MAX_NUM_OF_NDGC_POINTS)
				this.pointLists.add(vp);
		}
		
		return OGPConstants.RET_CODE_SUCCESS;
	}
	
	/**
	 * Method that checks polynomial form of this NDG condition
	 * in other parametric objects.
	 *  
	 * @return TRUE if there is some parametric object, FALSE otherwise.
	 */
	public boolean checkInParametricObjects() {
		boolean result = false;
		
		for (GeoConstruction geoCons : this.consProtocol.getConstructionSteps()) {
			if (geoCons instanceof ParametricSet) {
				((ParametricSet)geoCons).processNDGCondition(this);
				result = true;
			}
		}
		
		return result;
	}
	/**
	 * Method that transforms polynomial form of this NDG condition
	 * to user readable form which is stored in text property.
	 * If transformation is unsuccessful or impossible, text remains
	 * empty and where necessary polynomial will be displayed instead
	 * of user readable text.
	 * 
	 * @return	Text associated to this NDG condition is stored in text property;
	 *          SUCCESS is return if method successfully completes and general
	 *          error is returned otherwise.
	 */
	public int transformToUserReadableForm() {
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.populatePointLists() != OGPConstants.RET_CODE_SUCCESS) {
			// Check if there are other objects with parameters
			if (checkInParametricObjects() == true)
				return OGPConstants.RET_CODE_SUCCESS;
			
			logger.error("Failed to populate points for NDG condition.");
			return OGPConstants.ERR_CODE_GENERAL;
		}
		
		PointsPositionChecker twoPtsPosChecker = new TwoPointsPositionChecker(this);
		PointsPositionChecker threePtsPosChecker = new ThreePointsPositionChecker(this);
		PointsPositionChecker fourPtsPosChecker = new FourPointsPositionChecker(this);
		
		for (Vector<Point> pointsV : this.pointLists) {
			switch (pointsV.size()) {
				case 2:
					// allow checking of various combinations of points
					twoPtsPosChecker.checkPositions(pointsV);
					
					break;
				case 3:
					// allow checking of various combinations of points
					threePtsPosChecker.checkPositions(pointsV);
					
					break;
				case 4:
					// allow checking of various combinations of points
					fourPtsPosChecker.checkPositions(pointsV);
					
					break;
				default:
					break;
			}
		}
		
		// Couldn't find readable form - text of this NDG condition remains empty
		// but this NDG condition will be printed as polynomial
		return OGPConstants.RET_CODE_SUCCESS;
	}
	
	/**
	 * <i>
	 * Method that retrieves the textual description of NDG condition based on its type and by using
	 * list of points that are arguments of NDG condition.
	 * </i>
	 * 
	 * @param ndgType		The type of some NDG condition
	 * @param points		List of points which make the NDG condition
	 * @return				Textual description of NDG condition, or null if not found
	 */
	public static String getNDGConditionText(String ndgType, Vector<Point> points) {
		// Note: it is assumed that passed in list of points isn't empty and has correct number of points.
		Point pt1 = points.get(0);
		Point pt2 = points.get(1);
		Point pt3 = (points.size() > 2) ? points.get(2) : null;
		Point pt4 = (points.size() > 3) ? points.get(3) : null;
		
		StringBuilder sb = new StringBuilder();
		
		if (ndgType == AlgebraicNDGCondition.NDG_TYPE_2PT_IDENTICAL) {
			sb.append("Points ");
			sb.append(pt1.getGeoObjectLabel());
			sb.append(" and ");
			sb.append(pt2.getGeoObjectLabel());
			sb.append(" are not identical");
			return sb.toString();
		}
		
		if (ndgType == AlgebraicNDGCondition.NDG_TYPE_3PT_COLLINEAR) {
			sb.append("Points ");
			sb.append(pt1.getGeoObjectLabel());
			sb.append(", ");
			sb.append(pt2.getGeoObjectLabel());
			sb.append(" and ");
			sb.append(pt3.getGeoObjectLabel());
			sb.append(" are not collinear");
			return sb.toString();
		}
		
		if (ndgType == AlgebraicNDGCondition.NDG_TYPE_3PT_MIDPOINT) {
			sb.append("Point ");
			sb.append(pt3.getGeoObjectLabel());
			sb.append(" is not the midpoint of segment with endpoints ");
			sb.append(pt1.getGeoObjectLabel());
			sb.append(" and ");
			sb.append(pt2.getGeoObjectLabel());
			return sb.toString();
		}
		
		if (ndgType == AlgebraicNDGCondition.NDG_TYPE_3PT_ON_PERP_BIS) {
			sb.append("Point ");
			sb.append(pt3.getGeoObjectLabel());
			sb.append(" is not on perpendicular bisector of segment with endpoints ");
			sb.append(pt1.getGeoObjectLabel());
			sb.append(" and ");
			sb.append(pt2.getGeoObjectLabel());
			return sb.toString();
		}
		
		if (ndgType == AlgebraicNDGCondition.NDG_TYPE_3PT_RIGHT_ANG) {
			sb.append("Line through points ");
			sb.append(pt1.getGeoObjectLabel());
			sb.append(" and ");
			sb.append(pt3.getGeoObjectLabel());
			sb.append(" is not perpendicular to line through points ");
			sb.append(pt3.getGeoObjectLabel());
			sb.append(" and ");
			sb.append(pt2.getGeoObjectLabel());
			return sb.toString();
		}
		
		if (ndgType == AlgebraicNDGCondition.NDG_TYPE_3PT_ON_CIRCLE) {
			sb.append("Point ");
			sb.append(pt3.getGeoObjectLabel());
			sb.append(" is not on circle with center ");
			sb.append(pt1.getGeoObjectLabel());
			sb.append(" and point from it ");
			sb.append(pt2.getGeoObjectLabel());
			return sb.toString();
		}
		
		if (ndgType == AlgebraicNDGCondition.NDG_TYPE_3PT_SEG_SUM) {
			sb.append("Segment with endpoints ");
			sb.append(pt1.getGeoObjectLabel());
			sb.append(" and ");
			sb.append(pt2.getGeoObjectLabel());
			sb.append(" is not sum of two segments: segment with endpoints ");
			sb.append(pt1.getGeoObjectLabel());
			sb.append(" and ");
			sb.append(pt3.getGeoObjectLabel());
			sb.append(" and segment with endpoints ");
			sb.append(pt3.getGeoObjectLabel());
			sb.append(" and ");
			sb.append(pt2.getGeoObjectLabel());
			return sb.toString();
		}
		
		if (ndgType == AlgebraicNDGCondition.NDG_TYPE_4PT_COLLINEAR) {
			sb.append("Points ");
			sb.append(pt1.getGeoObjectLabel());
			sb.append(", ");
			sb.append(pt2.getGeoObjectLabel());
			sb.append(", ");
			sb.append(pt3.getGeoObjectLabel());
			sb.append(" and ");
			sb.append(pt4.getGeoObjectLabel());
			sb.append(" are not collinear");
			return sb.toString();
		}
		
		if (ndgType == AlgebraicNDGCondition.NDG_TYPE_4PT_CONCYCLIC) {
			sb.append("Points ");
			sb.append(pt1.getGeoObjectLabel());
			sb.append(", ");
			sb.append(pt2.getGeoObjectLabel());
			sb.append(", ");
			sb.append(pt3.getGeoObjectLabel());
			sb.append(" and ");
			sb.append(pt4.getGeoObjectLabel());
			sb.append(" are not concyclic");
			return sb.toString();
		}
		
		if (ndgType == AlgebraicNDGCondition.NDG_TYPE_4PT_EQ_SEG) {
			sb.append("Segment with endpoints ");
			sb.append(pt1.getGeoObjectLabel());
			sb.append(" and ");
			sb.append(pt2.getGeoObjectLabel());
			sb.append(" and segment with endpoints ");
			sb.append(pt3.getGeoObjectLabel());
			sb.append(" and ");
			sb.append(pt4.getGeoObjectLabel());
			sb.append(" are not of same lengths");
			return sb.toString();
		}
		
		if (ndgType == AlgebraicNDGCondition.NDG_TYPE_4PT_PARALLEL) {
			sb.append("Line through points ");
			sb.append(pt1.getGeoObjectLabel());
			sb.append(" and ");
			sb.append(pt2.getGeoObjectLabel());
			sb.append(" is not parallel with line through points ");
			sb.append(pt3.getGeoObjectLabel());
			sb.append(" and ");
			sb.append(pt4.getGeoObjectLabel());
			return sb.toString();
		}
		
		if (ndgType == AlgebraicNDGCondition.NDG_TYPE_4PT_PERPENDICULAR) {
			sb.append("Line through points ");
			sb.append(pt1.getGeoObjectLabel());
			sb.append(" and ");
			sb.append(pt2.getGeoObjectLabel());
			sb.append(" is not perpendicular to line through points ");
			sb.append(pt3.getGeoObjectLabel());
			sb.append(" and ");
			sb.append(pt4.getGeoObjectLabel());
			return sb.toString();
		}
		
		if (ndgType == AlgebraicNDGCondition.NDG_TYPE_4PT_HARMONIC) {
			sb.append("Pair of points ");
			sb.append(pt1.getGeoObjectLabel());
			sb.append(" and ");
			sb.append(pt2.getGeoObjectLabel());
			sb.append(" is not in harmonic conjunction with pair of points ");
			sb.append(pt3.getGeoObjectLabel());
			sb.append(" and ");
			sb.append(pt4.getGeoObjectLabel());
			return sb.toString();
		}
		
		if (ndgType == AlgebraicNDGCondition.NDG_TYPE_4PT_CONG_COLL_SEG) {
			sb.append("Segment with endpoints ");
			sb.append(pt1.getGeoObjectLabel());
			sb.append(" and ");
			sb.append(pt2.getGeoObjectLabel());
			sb.append(" is not collinear and congruent with segment with endpoints ");
			sb.append(pt3.getGeoObjectLabel());
			sb.append(" and ");
			sb.append(pt4.getGeoObjectLabel());
			return sb.toString();
		}
		
		if (ndgType == AlgebraicNDGCondition.NDG_TYPE_4PT_ON_ANG_BIS) {
			sb.append("Point ");
			sb.append(pt4.getGeoObjectLabel());
			sb.append(" is not on angle bisector of angle with vertex ");
			sb.append(pt2.getGeoObjectLabel());
			sb.append(" and two points from different rays ");
			sb.append(pt1.getGeoObjectLabel());
			sb.append(" and ");
			sb.append(pt3.getGeoObjectLabel());
			return sb.toString();
		}
		
		if (ndgType == AlgebraicNDGCondition.NDG_TYPE_4PT_2_ON_CIRCLE) {
			sb.append("Points ");
			sb.append(pt3.getGeoObjectLabel());
			sb.append(" and ");
			sb.append(pt4.getGeoObjectLabel());
			sb.append(" are not together on circle with center ");
			sb.append(pt1.getGeoObjectLabel());
			sb.append(" and one point on it ");
			sb.append(pt2.getGeoObjectLabel());
			return sb.toString();
		}
		
		if (ndgType == AlgebraicNDGCondition.NDG_TYPE_4PT_ON_CIRCLE) {
			sb.append("Point ");
			sb.append(pt4.getGeoObjectLabel());
			sb.append(" is not on circle with center ");
			sb.append(pt1.getGeoObjectLabel());
			sb.append(" and radius equal to segment with endpoints ");
			sb.append(pt2.getGeoObjectLabel());
			sb.append(" and ");
			sb.append(pt3.getGeoObjectLabel());
			return sb.toString();
		}
		
		if (ndgType == AlgebraicNDGCondition.NDG_TYPE_4PT_INVERSE) {
			sb.append("Points ");
			sb.append(pt3.getGeoObjectLabel());
			sb.append(" and ");
			sb.append(pt4.getGeoObjectLabel());
			sb.append(" are not two inverse points with respect to circle with center ");
			sb.append(pt1.getGeoObjectLabel());
			sb.append(" and one point from it ");
			sb.append(pt2.getGeoObjectLabel());
			return sb.toString();
		}
		
		if (ndgType == AlgebraicNDGCondition.NDG_TYPE_4PT_2_ON_PERP_BIS) {
			// TODO
			return sb.toString();
		}
		
		if (ndgType == AlgebraicNDGCondition.NDG_TYPE_4PT_TOUCH_CIRCLES) {
			// TODO
			return sb.toString();
		}
		// TODO - other types of NDG conditions
		
		return null;
	}
}
