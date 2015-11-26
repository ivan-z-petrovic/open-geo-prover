/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.auxiliary;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import com.ogprover.pp.tp.geoconstruction.Point;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for operations with lists of points</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class PointListManager {
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
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method that creates list of all combinations of points from given list.
	 * E.g. if A, B and C are points that make passed in list of points,
	 * then this method returns following list with all combinations:
	 * <br>
	 * A<br>
	 * B<br>
	 * C<br>
	 * A B<br>
	 * A C<br>
	 * B C<br>
	 * A B C<br>
	 * 
	 * @param pointList		List of points - there are no duplicates in this list
	 * @return				List of all combinations of given points; null in case of error
	 */
	public static Vector<Vector<Point>> createListOfCombinations(Vector<Point> pointList) {
		if (pointList == null)
			return null;
		
		Vector<Vector<Point>> combinations = new Vector<Vector<Point>>();
		
		if (pointList.size() == 0)
			return combinations; // empty list
		
		int n = pointList.size(); // number of points
		
		/*
		 * Create all combinations of these points with various lengths
		 */
		for (int len = 1; len <= n; len++) {
			Vector<Integer> indexV = new Vector<Integer>(len);
			
			// initialize indices
			for (int ii = 0; ii < len; ii++)
				indexV.add(new Integer(-1));
			
			int iCurrIdx = 0;
			
			while (iCurrIdx >= 0) {
				int currVal = indexV.get(iCurrIdx).intValue();
				int newVal = currVal + 1;
				
				if (newVal >= n) { // no more options for current element - go one step back
					iCurrIdx--;
				}
				else {
					// check if combination of len elements can be completed
					if (len - iCurrIdx - 1 + newVal >= n) {
						// can't be completed - go one step back
						iCurrIdx--;
					}
					else {
						// update current value
						indexV.set(iCurrIdx, new Integer(newVal));
						
						if (iCurrIdx == len - 1) { // completed combination of len elements
							// found one good combination
							Vector<Point> ptsCombination = new Vector<Point>();
							
							for (int jj = 0; jj < len; jj++)
								ptsCombination.add(pointList.get(indexV.get(jj).intValue()));
							
							combinations.add(ptsCombination); // add this good combination to list
						}
						else { // combination not completed - go forward
							iCurrIdx++; // initialize next value
							indexV.set(iCurrIdx, new Integer(newVal));
						}
					}
				}
			}
		}
		
		return combinations;
	}
	
	/**
	 * Method that generates unique string key for list of points.
	 * Key is list of point labels ordered in ascending order, e.g.
	 * {B, A, D} has key "ABD".
	 * 
	 * @param pointList		List of points
	 * @return				String key of point list
	 */
	public static String getPointListKey(Vector<Point> pointList) {
		if (pointList == null)
			return null;
		
		if (pointList.size() == 0)
			return "";
		
		// Copy point list to tree
		Map<String, Point> pointTree = new TreeMap<String, Point>();
		
		for (Point pt : pointList)
			pointTree.put(pt.getGeoObjectLabel(), pt);
		
		// Generate list key by reading all tree nodes in ascending order
		StringBuilder sb = new StringBuilder();
		
		for (Point pt : pointTree.values())
			sb.append(pt.getGeoObjectLabel());
		
		return sb.toString();
	}
	
	/**
	 * Method that merges two lists of points
	 * 
	 * @param firstV	First vector of points
	 * @param secondV	Second vector of points
	 * @return			New destination vector of points with all points from 
	 * 					both vectors added into it; there will not be duplicate points.
	 */
	public static Vector<Point> mergePointLists(Vector<Point> firstV, Vector<Point> secondV) {
		Map<String, Point> destinationM = new HashMap<String, Point>(); // map for result of merge to ensure there will not be duplicates
		
		if (firstV != null) {
			// Add all points from first vector into destination
			for (Point P : firstV) {
				destinationM.put(P.getGeoObjectLabel(), P);
			}
		}
		
		if (secondV != null) {
			for (Point P : secondV) 
				destinationM.put(P.getGeoObjectLabel(), P);
		}
		
		Collection<Point> destinationC = destinationM.values();
		
		if (destinationC == null)
			return null;
		
		return new Vector<Point>(destinationC);
	}
	
	/**
	 * Method which takes two lists with various points combinations and merges pairs
	 * made of one combination from first and another from second list.
	 * 
	 * @param firstComb		First list of point combinations
	 * @param secondComb	Second list of point combinations
	 * @return				New list of point combinations which is result of 
	 *                      this merge operation
	 */
	public static Vector<Vector<Point>> mergePairsOfPointCombinations(Vector<Vector<Point>> firstComb, Vector<Vector<Point>> secondComb) {
		if (firstComb == null)
			return secondComb;
		
		if (secondComb == null)
			return firstComb;
		
		Map<String, Vector<Point>> destinationM = new HashMap<String, Vector<Point>>(); // map for result of merge to ensure there will not be duplicates
		
		for (Vector<Point> comb1 : firstComb) {
			for (Vector<Point> comb2 : secondComb) {
				Vector<Point> mergeComb = PointListManager.mergePointLists(comb1, comb2);
				String combKey = PointListManager.getPointListKey(mergeComb);
				destinationM.put(combKey, mergeComb);
			}
		}
		
		Collection<Vector<Point>> destinationC = destinationM.values();
		
		if (destinationC == null)
			return null;
		
		return new Vector<Vector<Point>>(destinationC);
	}
}
