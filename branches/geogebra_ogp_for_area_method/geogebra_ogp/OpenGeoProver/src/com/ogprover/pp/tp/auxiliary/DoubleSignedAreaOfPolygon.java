/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.auxiliary;

import java.util.Vector;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.ogprover.pp.tp.geoobject.Segment;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for double signed area of polygon (n-gon where n >= 3)</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class DoubleSignedAreaOfPolygon {
	/*
	 * Polygons can be any triangle and any simple (no self-intersection points) 
	 * quadrilateral, but for higher number of vertices we consider only convex polygons.
	 */
	
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
	 * Ordered vertices of polygon.
	 */
	private Vector<Point> vertices = null;
	/**
	 * Polynomial that represents the double signed area of polygon.
	 */
	private XPolynomial areaPolynomial = null;
	
	
	
	
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

	// we don't have set method for area polynomial
	
	/**
	 * @return the areaPolynomial
	 */
	public XPolynomial getAreaPolynomial() {
		this.calculateAreaPolynomial();
		return areaPolynomial;
	}
	
	
	

	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * 
	 * @param vertices	Vector of ordered vertices of polygon
	 */
	public DoubleSignedAreaOfPolygon(Vector<Point> vertices) {
		this.vertices = vertices;
	}
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method that calculates the polynomial that represents the value 
	 * of double signed area of polygon.
	 */
	private void calculateAreaPolynomial() {
		// skip calculation if polynomial is already calculated
		if (this.areaPolynomial != null)
			return;
		
		// Check if it is possible to do a calculation
		if (this.vertices == null || this.vertices.size() < 3) {
			OpenGeoProver.settings.getLogger().error("There are no enough vertices in polygon.");
			return;
		}
		
		/*
		 * If ABC is a triangle, then double value of its area (or area of parallelogram
		 * with edges AB and AC) is equal to magnitude of cross-product of vectors
		 * AB and AC:
		 *    S(ABC) = |vector(AB) X vector(AC)|
		 *    
		 * Coordinates of vectors AB and AC in 3D Euclidean space are:
		 *  vector(AB) = (xB - xA, yB - yA, 0), vector(AC) = (xC - xA, yC - yA, 0)
		 *  
		 *  then if i, j and k are unit vectors of x, y and z-axes respectively then
		 *  cross-ratio of vectors AB and AC equals following determinant:
		 *  
		 *                               (   i        j     k)
		 *  vector(AB) X vector(AC) = det(xB - xA  yB - yA  0) = [(xB-xA)(yC-yA) - (xC-xA)(yB-yA)]k
		 *                               (xC - xA  yC - yA  0)
		 *                               
		 *  then double area of triangle ABC is:
		 *  
		 *  S(ABC) = |(xB-xA)(yC-yA) - (xC-xA)(yB-yA)|
		 *  
		 *  If we remove absolute value we get expression for double signed area of triangle:
		 *  S(ABC) = (xB-xA)(yC-yA) - (xC-xA)(yB-yA)
		 *  
		 *  If points B and C replace their order, then double signed area changes the sign.
		 *  Therefore, equally oriented triangles have same value of double signed area, while
		 *  oppositely oriented triangles have values with opposite signs.
		 *  
		 *  If A1A2...An (where n >= 3) is polygon such that it is simple for n = 4 and convex 
		 *  for n > 4, then double signed area of that polygon is sum of double signed areas
		 *  of following equally oriented triangles:
		 *     A1A2A3
		 *     A1A3A4
		 *     A1A4A5
		 *     ...
		 *     A1A(n-1)An, n >= 3                             
		 */
		
		Point A1 = this.vertices.get(0);
		Point secondPoint = this.vertices.get(1);
		Point thirdPoint = null;
		Segment AB = null;
		Segment AC = null;
		
		this.areaPolynomial = new XPolynomial();
		
		for (int ii = 2, jj = this.vertices.size(); ii < jj; ii++) {
			thirdPoint = this.vertices.get(ii);
			
			AB = new Segment(A1, secondPoint);
			AC = new Segment(A1, thirdPoint);
			
			this.areaPolynomial.addPolynomial(AB.getInstantiatedXCoordinateOfOrientedSegment()
					                            .multiplyByPolynomial(AC.getInstantiatedYCoordinateOfOrientedSegment())
					                            .subtractPolynomial(AB.getInstantiatedYCoordinateOfOrientedSegment()
					                                                  .multiplyByPolynomial(AC.getInstantiatedXCoordinateOfOrientedSegment())));
			
			secondPoint = thirdPoint;
		}
		
		// Note: do not reduce this polynomial by UTerm division now since it could be  a part of greater expression!
	}
	
}


