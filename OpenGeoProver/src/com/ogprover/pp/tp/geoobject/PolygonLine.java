/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.geoobject;

import java.util.Vector;

import com.ogprover.pp.tp.geoconstruction.FreePoint;
import com.ogprover.pp.tp.geoconstruction.Point;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for polygons and other polygon lines</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class PolygonLine implements PointList {
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
	 * Vector of vertices
	 */
	private Vector<Point> vertices;
	/**
	 * The label of polygon line.
	 */
	private String label;
	/**
	 * Vector of labels of polygon edges.
	 * First edge is segment from first to second point etc., the last edge is
	 * segment from last to first point.
	 */
	private Vector<String> edgesLabels;
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @param vertices the vertices to set
	 */
	public void setVertices(Vector<Point> vertices) {
		this.vertices = vertices;
	}

	/**
	 * @return the vertices
	 */
	public Vector<Point> getVertices() {
		return vertices;
	}
	
	/**
	 * @see com.ogprover.pp.tp.geoobject.GeoObject#getGeoObjectLabel()
	 */
	public String getGeoObjectLabel() {
		return this.label;
	}
	
	/**
	 * @param edgesLabels the edgesLabels to set
	 */
	public void setEdgesLabels(Vector<String> edgesLabels) {
		this.edgesLabels = edgesLabels;
	}

	/**
	 * @return the edgesLabels
	 */
	public Vector<String> getEdgesLabels() {
		return edgesLabels;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	public PolygonLine(Vector<Point> vertices, Vector<String> edges) {
		this.vertices = vertices;
		this.edgesLabels = edges;
		StringBuilder sb = new StringBuilder();
		sb.append("|");
		for (Point pt: vertices)
			sb.append(pt.getGeoObjectLabel());
		sb.append("|");
		this.label = sb.toString();
	}
	
	public PolygonLine(Vector<Point> vertices, Vector<String> edges, String label) {
		this.vertices = vertices;
		this.edgesLabels = edges;
		this.label = label;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method that checks whether passed in point is vertex of this polygon line.
	 * 
	 * @param pt	Point which is examined.
	 * @return		TRUE if point is vertex of this polygon line, FALSE otherwise.
	 */
	public boolean containsPointAsVertex(Point pt) {
		return this.vertices.contains(pt); // uses equals() from Point which compares by label
	}
	
	/**
	 * Method that checks whether passed in point is vertex of this polygon line.
	 * 
	 * @param ptLabel	Label of point which is examined.
	 * @return			TRUE if point is vertex of this polygon line, FALSE otherwise.
	 */
	public boolean containsPointAsVertex(String ptLabel) {
		Point tempPt = new FreePoint(ptLabel);
		return this.containsPointAsVertex(tempPt);
	}

	/**
	 * @see com.ogprover.pp.tp.geoobject.PointList#getPoints()
	 */
	public Vector<Point> getPoints() {
		return this.vertices;
	}
}


