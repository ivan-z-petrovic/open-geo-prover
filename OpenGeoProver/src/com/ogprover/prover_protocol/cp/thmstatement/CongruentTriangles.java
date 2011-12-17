/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.prover_protocol.cp.thmstatement;

import java.util.Vector;

import com.ogprover.prover_protocol.cp.geoconstruction.Point;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for statement about two congruent triangles</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
/**
 * @author ipetrov
 *
 */
public class CongruentTriangles extends ConjunctionThmStatement {
	/*
	 * Congruence is proved by proving equality of three pairs of
	 * corresponding triangles' sides. 
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
	// Vertices of triangle
	private Point firstTrianglePointA = null;
	private Point firstTrianglePointB = null;
	private Point firstTrianglePointC = null;
	private Point secondTrianglePointA = null;
	private Point secondTrianglePointB = null;
	private Point secondTrianglePointC = null;
	
	
	

	/*
	 * ======================================================================
	 * ========================= ABSTRACT METHODS ===========================
	 * ======================================================================
	 */
	
	
	

	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @return the firstTrianglePointA
	 */
	public Point getFirstTrianglePointA() {
		return firstTrianglePointA;
	}
	
	/**
	 * @return the firstTrianglePointB
	 */
	public Point getFirstTrianglePointB() {
		return firstTrianglePointB;
	}
	
	/**
	 * @return the firstTrianglePointC
	 */
	public Point getFirstTrianglePointC() {
		return firstTrianglePointC;
	}
	
	/**
	 * @return the secondTrianglePointA
	 */
	public Point getSecondTrianglePointA() {
		return secondTrianglePointA;
	}
	
	/**
	 * @return the secondTrianglePointB
	 */
	public Point getSecondTrianglePointB() {
		return secondTrianglePointB;
	}
	
	/**
	 * @return the secondTrianglePointC
	 */
	public Point getSecondTrianglePointC() {
		return secondTrianglePointC;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * 
	 * @param A1	First point of first triangle
	 * @param B1	Second point of first triangle
	 * @param C1	Third point of first triangle
	 * @param A2	First point of second triangle
	 * @param B2	Second point of second triangle
	 * @param C2	Third point of second triangle
	 */
	public CongruentTriangles(Point A1, Point B1, Point C1, Point A2, Point B2, Point C2) {
		this.particleThmStatements = new Vector<ThmStatement>();
		this.particleThmStatements.add(new SegmentsOfEqualLengths(A1, B1, A2, B2));
		this.particleThmStatements.add(new SegmentsOfEqualLengths(A1, C1, A2, C2));
		this.particleThmStatements.add(new SegmentsOfEqualLengths(B1, C1, B2, C2));
		
		this.firstTrianglePointA = A1;
		this.firstTrianglePointB = B1;
		this.firstTrianglePointC = C1;
		this.secondTrianglePointA = A2;
		this.secondTrianglePointB = B2;
		this.secondTrianglePointC = C2;
	}
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * @see com.ogprover.prover_protocol.cp.thmstatement.ThmStatement#getStatementDesc()
	 */
	@Override
	public String getStatementDesc() {
		StringBuilder sb = new StringBuilder();
		sb.append("Triangles ");
		sb.append(this.firstTrianglePointA.getGeoObjectLabel());
		sb.append(this.firstTrianglePointB.getGeoObjectLabel());
		sb.append(this.firstTrianglePointC.getGeoObjectLabel());
		sb.append(" and ");
		sb.append(this.secondTrianglePointA.getGeoObjectLabel());
		sb.append(this.secondTrianglePointB.getGeoObjectLabel());
		sb.append(this.secondTrianglePointC.getGeoObjectLabel());
		sb.append(" are congruent");
		return sb.toString();
	}
}
