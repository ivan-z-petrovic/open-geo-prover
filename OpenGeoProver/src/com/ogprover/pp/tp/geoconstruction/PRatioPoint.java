/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.geoconstruction;

import java.util.HashMap;

import com.ogprover.main.OGPConstants;
import com.ogprover.polynomials.UXVariable;
import com.ogprover.pp.tp.expressions.AMExpression;



/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for point constructed with the PRatio method :
*      PRatio(W,U,V,r) is the point Y such as WY = rUV, 
*      where WY and UV are oriented parallel segments.
*      See http://hal.inria.fr/hal-00426563/PDF/areaMethodRecapV2.pdf</dd>
* </dl>
* 
* @version 1.00
* @author Damien Desfontaines
*/
public class PRatioPoint extends Point {
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
	 */
	protected Point w,u,v;
	/**
	 * Ratio used to construct this point
	 */
	protected AMExpression r;

	
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
		return GeoConstruction.GEOCONS_TYPE_PRATIO_POINT;
	}
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	 /**
	 * Constructor method
	 * Returns the point with a given label, constructed as PRATIO(y,w,u,v,r)
	 * See http://hal.inria.fr/hal-00426563/PDF/areaMethodRecapV2.pdf
	 * 
	 * @param pointLabel 	Label of point
	 * @param w				Previously constructed point
	 * @param u				Previously constructed point
	 * @param v 			Previously constructed point
	 * @param r				Ratio
	 */
	public PRatioPoint(String pointLabel, Point w, Point u, Point v, AMExpression r) {
		this.geoObjectLabel = pointLabel;
		this.w = w;
		this.u = u;
		this.v = v;
		this.r = r;
	}
	
	public Point getW() {
		return w;
	}
	
	public Point getU() {
		return u;
	}
	
	public Point getV() {
		return v;
	}
	
	public AMExpression getR() {
		return r;
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
		Point p = new PRatioPoint(this.geoObjectLabel, this.w, this.u, this.v, this.r);
		
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
		sb.append("Point ");
		sb.append(this.geoObjectLabel);
		sb.append(" verifying [");
		sb.append(((GeoConstruction)this.w).geoObjectLabel);
		sb.append(this.geoObjectLabel);
		sb.append("] = r[");
		sb.append(((GeoConstruction)this.u).geoObjectLabel);
		sb.append(((GeoConstruction)this.v).geoObjectLabel);
		sb.append("], where ");
		sb.append(((GeoConstruction)this.w).geoObjectLabel);
		sb.append(this.geoObjectLabel);
		sb.append(" and ");
		sb.append(((GeoConstruction)this.u).geoObjectLabel);
		sb.append(((GeoConstruction)this.v).geoObjectLabel);
		sb.append(" are parallel, and where r = ");
		int size = r.size();
		if (size >= 200)
			sb.append("[too large to be printed : size = " + Integer.toString(size) + "]");
		else
			sb.append(this.r.print());
		return sb.toString();
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getInputLabels()
	 */
	@Override
	public String[] getInputLabels() {
		// TODO Discuss if we need to return the name of points appearing in the ratio r or not
		String[] inputLabels = new String[3];
		inputLabels[0] = ((GeoConstruction)this.w).getGeoObjectLabel();
		inputLabels[1] = ((GeoConstruction)this.u).getGeoObjectLabel();
		inputLabels[2] = ((GeoConstruction)this.v).getGeoObjectLabel();
		return inputLabels;
	}

	@Override
	public Point replace(HashMap<Point, Point> replacementMap) {
		Point w2 = w;
		Point u2 = u;
		Point v2 = v;
		if (replacementMap.containsKey(w))
			w2 = replacementMap.get(w);
		if (replacementMap.containsKey(u))
			u2 = replacementMap.get(u);
		if (replacementMap.containsKey(v))
			v2 = replacementMap.get(v);
		return new PRatioPoint(geoObjectLabel, w2, u2, v2, r);
	}
}
