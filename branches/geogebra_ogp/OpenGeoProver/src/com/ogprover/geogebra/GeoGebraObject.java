/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.geogebra;

import com.ogprover.pp.tp.geoobject.GeoObject;

/**
 * <dl>
 * <dt><b>Interface description:</b></dt>
 * <dd>
 * 	Interface for all objects that could appear in GeoGebra's Construction Protocol.
 * They are various geometry objects but also can be some numeric constants.
 * </dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */

public interface GeoGebraObject extends GeoObject {
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
	
	
	/**
	 * Static constants for types of geometry objects described in <element> tag.
	 */
	public static final String OBJ_TYPE_NONE = "";
	public static final String OBJ_TYPE_POINT = "point";
	public static final String OBJ_TYPE_LINE = "line";
	public static final String OBJ_TYPE_CONIC = "conic";
	public static final String OBJ_TYPE_CONIC_PART = "conicpart";
	public static final String OBJ_TYPE_ANGLE = "angle";
	public static final String OBJ_TYPE_SEGMENT = "segment";
	public static final String OBJ_TYPE_VECTOR = "vector";
	public static final String OBJ_TYPE_POLYGON = "polygon";
	public static final String OBJ_TYPE_POLYLINE = "polyline";
	public static final String OBJ_TYPE_RAY = "ray";
	public static final String OBJ_TYPE_CCURVE = "curvecartesian";
	public static final String OBJ_TYPE_ICURVE = "implicitpoly";
	public static final String OBJ_TYPE_NUMERIC = "numeric"; // this is not a type of geometry object but definition of numeric constant
	public static final String OBJ_TYPE_BOOL = "boolean"; // this is not a type of geometry object but definition of boolean constant
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method that retrieves the type of object
	 * 
	 * @return The type of GeoGebra's object
	 */
	public String getObjectType();
}