/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.geoobject;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.ogprover.polynomials.UXVariable;
import com.ogprover.polynomials.Variable;
import com.ogprover.pp.tp.geoconstruction.Point;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for list of points</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class RCConsPointList implements PointList {
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
	 * Point list
	 */
	private Vector<Point> points;
	/**
	 * Map with double values for x-coordinates of points.
	 * Key is point label.
	 */
	private Map<String, Double> xCoordinateInstances;
	/**
	 * Map with double values for y-coordinates of points.
	 * Key is point label.
	 */
	private Map<String, Double> yCoordinateInstances;
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @param points the points to set
	 */
	public void setPoints(Vector<Point> points) {
		this.points = points;
	}

	/**
	 * @return the points
	 */
	public Vector<Point> getPoints() {
		return points;
	}
	
	/**
	 * @param xCoordinateInstances the xCoordinateInstances to set
	 */
	public void setXCoordinateInstances(Map<String, Double> xCoordinateInstances) {
		this.xCoordinateInstances = xCoordinateInstances;
	}

	/**
	 * @return the xCoordinateInstances
	 */
	public Map<String, Double> getXCoordinateInstances() {
		return xCoordinateInstances;
	}

	/**
	 * @param yCoordinateInstances the yCoordinateInstances to set
	 */
	public void setYCoordinateInstances(Map<String, Double> yCoordinateInstances) {
		this.yCoordinateInstances = yCoordinateInstances;
	}

	/**
	 * @return the yCoordinateInstances
	 */
	public Map<String, Double> getYCoordinateInstances() {
		return yCoordinateInstances;
	}
	
	

	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method.
	 */
	public RCConsPointList() {
		this.points = new Vector<Point>();
		this.xCoordinateInstances = new HashMap<String, Double>();
		this.yCoordinateInstances = new HashMap<String, Double>();
	}
	
	/**
	 * Constructor method.
	 * 
	 * @param points	List of points
	 */
	public RCConsPointList(Vector<Point> points) {
		this.points = points;
		this.xCoordinateInstances = new HashMap<String, Double>();
		this.yCoordinateInstances = new HashMap<String, Double>();
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method for clearing point list.
	 */
	public void clear() {
		this.points.clear();
		this.xCoordinateInstances.clear();
		this.yCoordinateInstances.clear();
		
	}
	
	/**
	 * Method for adding new point to point list.
	 * 
	 * @param pt	Point to add.
	 */
	public void addPoint(Point pt) {
		this.points.add(pt);
	}
	
	/**
	 * Method for adding value of x-coordinate of specified point.
	 * 
	 * @param ptLabel	Point label
	 * @param xVal		Double value of x-coordinate
	 */
	public void addXCoordinateValue(String ptLabel, double xVal) {
		this.xCoordinateInstances.put(ptLabel, xVal);
	}
	
	/**
	 * Method which retrieves double (as object) value of x-coordinate assigned to specified point.
	 * 
	 * @param ptLabel	Point label.
	 * @return			Double value of x-coordinate.
	 */
	public Double getXCoordinateValue(String ptLabel) {
		return this.xCoordinateInstances.get(ptLabel);
	}
	
	/**
	 * Method for adding value of y-coordinate of specified point.
	 * 
	 * @param ptLabel	Point label
	 * @param yVal		Double value of y-coordinate
	 */
	public void addYCoordinateValue(String ptLabel, double yVal) {
		this.yCoordinateInstances.put(ptLabel, yVal);
	}
	
	/**
	 * Method which retrieves double (as object) value of y-coordinate assigned to specified point.
	 * 
	 * @param ptLabel	Point label.
	 * @return			Double value of y-coordinate.
	 */
	public Double getYCoordinateValue(String ptLabel) {
		return this.yCoordinateInstances.get(ptLabel);
	}
	
	/**
	 * Method which retrieves map of symbolic variables with their double values.
	 * It is based on how points from this list have been assigned double values for their coordinates.
	 * 
	 * @return	Map with double values of symbolic variables.
	 */
	public Map<UXVariable, Double> getMapWithVariableInstances() {
		Map<UXVariable, Double> varInstanceMap = new HashMap<UXVariable, Double>();
		
		for (Point pt : this.points) {
			if (pt.getPointState() != Point.POINT_STATE_INITIALIZED) { // point has been only instantiated by symbolic variables
				UXVariable xVar = pt.getX();
				if (xVar.getVariableType() != Variable.VAR_TYPE_UX_U || xVar.getIndex() != 0) {
					Double xVal = this.xCoordinateInstances.get(pt.getGeoObjectLabel());
					if (xVal != null)
						varInstanceMap.put(xVar, xVal);
				}
				
				UXVariable yVar = pt.getY();
				if (yVar.getVariableType() != Variable.VAR_TYPE_UX_U || yVar.getIndex() != 0) {
					Double yVal = this.yCoordinateInstances.get(pt.getGeoObjectLabel());
					if (yVal != null)
						varInstanceMap.put(yVar, yVal);
				}
			}
		}
		
		return varInstanceMap;
	}

	public String getGeoObjectLabel() {
		StringBuilder sb = new StringBuilder("Point list for RC constructibility: ");
		boolean bFirst = true;
		for (Point pt : this.points) {
			if (bFirst) {
				bFirst = false;
			}
			else {
				sb.append(", ");
			}
			if (pt != null) {
				sb.append(pt.getGeoObjectLabel());
			}
		}
		return sb.toString();
	}
}
