/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.geoconstruction;

import java.io.IOException;
import java.util.HashMap;

import com.ogprover.main.OGPConstants;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.UXVariable;
import com.ogprover.utilities.io.OGPOutput;
import com.ogprover.utilities.logger.ILogger;



/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for point constructed as an intersection between two lines,
* 		the two lines being given with two couples of points.</dd>
* </dl>
* 
* @version 1.00
* @author Damien Desfontaines
*/
public class AMIntersectionPoint extends IntersectionPoint {
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
	 * The point is the intersection point between the lines (uv) and (pq).
	 */
	protected Point u,v,p,q;


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
		return GeoConstruction.GEOCONS_TYPE_AM_INTERSECTION_POINT;
	}
	
	public Point getU() {
		return u;
	}
	
	public Point getV() {
		return v;
	}
	
	public Point getP() {
		return p;
	}
	
	public Point getQ() {
		return q;
	}
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	 /**
	 * Constructor method
	 * Returns the point with a given label, intersection between the two lines (uv) and (pq)
	 * 
	 * @param pointLabel 	Label of point
	 * @param u				Previously constructed point
	 * @param v 			Previously constructed point
	 * @param p				Previously constructed point
	 * @param q 			Previously constructed point
	 */
	public AMIntersectionPoint(String pointLabel, Point u, Point v, Point p, Point q) {
		super(pointLabel, new LineThroughTwoPoints("TODO", u, v), new LineThroughTwoPoints("TODO", p, q));
		this.u = u;
		this.v = v;
		this.p = p;
		this.q = q;
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
		Point pp = new AMIntersectionPoint(this.geoObjectLabel, this.u, this.v, this.p, this.q);
		
		// TODO I'm not sure which following line is useful and which isn't
		if (this.getX() != null)
			pp.setX((UXVariable) this.getX().clone());
		if (this.getY() != null)
			pp.setY((UXVariable) this.getY().clone());
		pp.setInstanceType(this.instanceType);
		pp.setPointState(this.pointState);
		pp.setConsProtocol(this.consProtocol);
		pp.setIndex(this.index);
		
		return pp;
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
	
	/**
	 * Method to check the validity of this construction step
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#isValidConstructionStep()
	 */
	@Override
	public boolean isValidConstructionStep() {
		OGPOutput output = OpenGeoProver.settings.getOutput();
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (!super.isValidConstructionStep())
			return false;
		
		try {
			if (this.u == null || this.v == null || this.p == null || this.q == null) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Intersection point " + this.getGeoObjectLabel() + " can't be constructed since some base points' set is not constructed");
				return false;
			}
			
			if (((GeoConstruction)this.u).getIndex() < 0 || ((GeoConstruction)this.u).getIndex() >= this.index ||
				((GeoConstruction)this.v).getIndex() < 0 || ((GeoConstruction)this.v).getIndex() >= this.index ||
				((GeoConstruction)this.p).getIndex() < 0 || ((GeoConstruction)this.p).getIndex() >= this.index ||
				((GeoConstruction)this.q).getIndex() < 0 || ((GeoConstruction)this.q).getIndex() >= this.index)  {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Intersection point " + this.getGeoObjectLabel() + " can't be constructed since some base points' set is not yet constructed or not added to theorem protocol");
				return false;
			}
		} catch (IOException e) {
			logger.error("Failed to write to output file(s).");
			output.close();
			return false;
		}
		
		return true;
	}
	
	@Override
	public int transformToAlgebraicForm() {
		// We don't do anything here - we never transform this type of point to algebraic form.
		return OGPConstants.ERR_CODE_NULL;
	}

	/** 
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getConstructionDesc()
	 */
	@Override
	public String getConstructionDesc() {
		StringBuilder sb = new StringBuilder();
		sb.append("Intersection point ");
		sb.append(this.geoObjectLabel);
		sb.append(" of line (");
		sb.append(((GeoConstruction)this.u).geoObjectLabel);
		sb.append(((GeoConstruction)this.v).geoObjectLabel);
		sb.append(") and line (");
		sb.append(((GeoConstruction)this.p).geoObjectLabel);
		sb.append(((GeoConstruction)this.q).geoObjectLabel);
		sb.append(")");
		return sb.toString();
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getInputLabels()
	 */
	@Override
	public String[] getInputLabels() {
		String[] inputLabels = new String[4];
		inputLabels[0] = ((GeoConstruction)this.u).getGeoObjectLabel();
		inputLabels[1] = ((GeoConstruction)this.v).getGeoObjectLabel();
		inputLabels[2] = ((GeoConstruction)this.p).getGeoObjectLabel();
		inputLabels[3] = ((GeoConstruction)this.q).getGeoObjectLabel();
		return inputLabels;
	}

	@Override
	public Point replace(HashMap<Point, Point> replacementMap) {
		Point p2 = p;
		Point q2 = q;
		Point u2 = u;
		Point v2 = v;
		if (replacementMap.containsKey(p))
			p2 = replacementMap.get(p);
		if (replacementMap.containsKey(q))
			q2 = replacementMap.get(q);
		if (replacementMap.containsKey(u))
			u2 = replacementMap.get(u);
		if (replacementMap.containsKey(v))
			v2 = replacementMap.get(v);
		return new AMIntersectionPoint(geoObjectLabel, p2, q2, u2, v2);
	}
}
