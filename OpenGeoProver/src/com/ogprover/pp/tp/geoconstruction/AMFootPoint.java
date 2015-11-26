/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.geoconstruction;

import java.util.HashMap;

import com.ogprover.main.OGPConstants;
import com.ogprover.polynomials.UXVariable;



/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for construction of foot of a point on a line given by two points </dd>
* </dl>
* 
* @version 1.00
* @author Damien Desfontaines
*/
public class AMFootPoint extends Point {
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
	 * Points used to construct this point
	 * The point is the foot of the point p on the line (uv).
	 */
	protected Point p,u,v;

	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * Method that retrieves the type of construction
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getConstructionType()
	 */
	@Override
	public int getConstructionType() {
		return GeoConstruction.GEOCONS_TYPE_AM_FOOT_POINT;
	}
	
	public Point getP() {
		return p;
	}
	
	public Point getU() {
		return u;
	}
	
	public Point getV() {
		return v;
	}
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	 /**
	 * Constructor method
	 * 
	 * @param pointLabel 	Label of point
	 * @param p				Previously constructed point
	 * @param u				Previously constructed point
	 * @param v 			Previously constructed point
	 */
	public AMFootPoint(String pointLabel, Point p, Point u, Point v) {
		this.geoObjectLabel = pointLabel;
		this.p = p;
		this.u = u;
		this.v = v;
	}

	
	/*
	 * ======================================================================
	 * ======================= COMMON OBJECT METHODS ========================
	 * ======================================================================
	 */
	/**
	 * Clone method
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.Point#clone()
	 */
	@Override
	public Point clone() {
		Point p = new AMFootPoint(this.geoObjectLabel, this.p, this.u, this.v);
		
		// TODO I'm not sure which following line is useful and which isn't
		if (this.getX() != null)
			p.setX((UXVariable) this.getX().clone());
		if (this.getY() != null)
			p.setY((UXVariable) this.getY().clone());
		p.setInstanceType(this.instanceType);
		p.setPointState(this.pointState);
		p.setConsProtocol(this.consProtocol);
		p.setIndex(this.index);
		
		return p;
	}


	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method that transforms the construction of this point into algebraic form
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.Point#transformToAlgebraicForm()
	 */
	@Override
	public int transformToAlgebraicForm() {
		// We don't do anything here - we never transform this type of point to algebraic form.
		return OGPConstants.RET_CODE_SUCCESS;
	}

	/** 
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getConstructionDesc()
	 */
	@Override
	public String getConstructionDesc() {
		StringBuilder sb = new StringBuilder();
		sb.append("Foot ");
		sb.append(this.geoObjectLabel);
		sb.append(" of the point ");
		sb.append(((GeoConstruction)this.p).geoObjectLabel);
		sb.append(" on the line (");
		sb.append(((GeoConstruction)this.u).geoObjectLabel);
		sb.append(((GeoConstruction)this.v).geoObjectLabel);
		sb.append(")");
		return sb.toString();
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getInputLabels()
	 */
	@Override
	public String[] getInputLabels() {
		// TODO Discuss if we need to return the name of points appearing in the ratio r or not
		String[] inputLabels = new String[3];
		inputLabels[0] = ((GeoConstruction)this.p).getGeoObjectLabel();
		inputLabels[1] = ((GeoConstruction)this.u).getGeoObjectLabel();
		inputLabels[2] = ((GeoConstruction)this.v).getGeoObjectLabel();
		return inputLabels;
	}

	@Override
	public Point replace(HashMap<Point, Point> replacementMap) {
		Point p2 = p;
		Point u2 = u;
		Point v2 = v;
		if (replacementMap.containsKey(p))
			p2 = replacementMap.get(p);
		if (replacementMap.containsKey(u))
			u2 = replacementMap.get(u);
		if (replacementMap.containsKey(v))
			v2 = replacementMap.get(v);
		return new AMFootPoint(geoObjectLabel, p2, u2, v2);
	}
}