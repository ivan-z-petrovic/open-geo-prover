/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.prover_protocol.cp.ndgcondition;

import java.util.Collection;
import java.util.Map;
import java.util.Vector;

import com.ogprover.main.OGPConstants;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.UXVariable;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.prover_protocol.cp.OGPCP;
import com.ogprover.prover_protocol.cp.auxiliary.FourPointsPositionChecker;
import com.ogprover.prover_protocol.cp.auxiliary.PointListManager;
import com.ogprover.prover_protocol.cp.auxiliary.PointsPositionChecker;
import com.ogprover.prover_protocol.cp.auxiliary.ThreePointsPositionChecker;
import com.ogprover.prover_protocol.cp.auxiliary.TwoPointsPositionChecker;
import com.ogprover.prover_protocol.cp.geoconstruction.FreePoint;
import com.ogprover.prover_protocol.cp.geoconstruction.GeoConstruction;
import com.ogprover.prover_protocol.cp.geoconstruction.ParametricSet;
import com.ogprover.prover_protocol.cp.geoconstruction.Point;
import com.ogprover.prover_protocol.cp.geoconstruction.RandomPointFromSetOfPoints;
import com.ogprover.utilities.io.FileLogger;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for NDG (Non-degenerative) condition</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class NDGCondition {
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
	 * Construction protocol that contains this NDG condition
	 */
	private OGPCP consProtocol = null;
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
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @param consProtocol the consProtocol to set
	 */
	public void setConsProtocol(OGPCP consProtocol) {
		this.consProtocol = consProtocol;
	}
	
	/**
	 * @return the consProtocol
	 */
	public OGPCP getConsProtocol() {
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

	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Default Constructor method
	 */
	public NDGCondition(XPolynomial ndgPoly){
		this.polynomial = ((XPolynomial) ndgPoly.clone()).reduceUTerms(false); // partial reduction of u-terms
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
	 * @param pointList	List of points whose special position generates this NDG condition.
	 * @param textStr	Text for description of this NDG condition.
	 */
	public void addNewText(Vector<Point> pointList, String textStr) {
		if (pointList== null || textStr == null)
			return;
		
		if (this.textList == null)
			this.textList = new Vector<String>();
		
		this.textList.add(textStr);
		
		// Update elements for best description
		if (this.bestDescription == null) {
			this.bestDescription = textStr;
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
				this.bestDescription = textStr;
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
		FileLogger logger = OpenGeoProver.settings.getLogger();
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
		FileLogger logger = OpenGeoProver.settings.getLogger();
		
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
}
