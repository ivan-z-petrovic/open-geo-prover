/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.geoconstruction;

import java.util.ArrayList;
import java.util.Map;
import com.ogprover.polynomials.SymbolicPolynomial;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.pp.tp.auxiliary.PointSetRelationshipManager;
import com.ogprover.pp.tp.geoobject.PointList;

/**
 * <dl>
 * <dt><b>Interface description:</b></dt>
 * <dd>Interface for all sets of points like lines, circles and ellipses</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */

public interface SetOfPoints extends PointList {
	/*
	 * ======================================================================
	 * ========================== VARIABLES =================================
	 * ======================================================================
	 */
	/**
	 * <i><b>
	 * Version number of interface in form xx.yy where
	 * xx is major version/release number and yy is minor
	 * release number.
	 * </b></i>
	 */
	public static final String VERSION_NUM = "1.00"; // this should match the version number from interface comment
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method that fills the map with best points for instantiation of a condition
	 * for the point to belong to the set of points from relationship manager.
	 * Also best instantiated polynomial will be set in manager object.
	 * In order to find best elements this method will check all possibilities;
	 * therefore there will be many test, so this method should not write any text
	 * to output files.
	 * 
	 * This method will be called only from PointSetRelationshipManager object on
	 * its set of points and that manager object will be passed in as an argument.
	 * That way only manager that this set of points belongs to could be passed in
	 * as an argument of this method.
	 * 
	 * @param manager	Object which contains the relationship between a point and a set
	 * @return			SUCCESS if execution was successful and ERR_CODE_GENERAL 
	 * 					in case of error
	 */
	public int findBestPointsForInstantation(PointSetRelationshipManager manager);
	/**
	 * Method that returns X-polynomial which is instance of symbolic condition 
	 * that some point belongs to this set of points. Instance is made for point
	 * passed in as argument and for other basic points that constitute this set of
	 * points.
	 * 
	 * @param P		Given point for which condition is instantiated
	 * @return		XPolynomial object that represents the instantiated condition
	 * 				or null in case of error
	 */
	public XPolynomial instantiateConditionFromBasicElements(Point P);
	/**
	 * Method to add passed in point to point set. It will be used when new point from
	 * point set is constructed, to add it to that point set.
	 * 
	 * @param P	Point to be added
	 */
	public void addPointToSet(Point P);
	/**
	 * Method that retrieves the condition for some point
	 * to belong to this set of points
	 * 
	 * @return The condition (it is null in case of error)
	 */
	public abstract SymbolicPolynomial getCondition();
	/**
	 * Method that retrieves map with all symbolic conditions that could be applied for
	 * this set of points, together with list of all mappings of points to symbolic
	 * labels necessary for instantiation except generic label M0 for some arbitrary
	 * point from this set - this mapping is added at the moment of instantiation when
	 * this point is known.
	 * 
	 * @return 	Map with symbolic polynomials and corresponding list of mappings of labels
	 * 			to points
	 */
	public Map<SymbolicPolynomial, ArrayList<Map<String, Point>>> getAllPossibleConditionsWithMappings();
}