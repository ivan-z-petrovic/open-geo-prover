/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.geoconstruction;

import java.io.IOException;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.geoobject.GeoObject;
import com.ogprover.utilities.io.OGPOutput;
import com.ogprover.utilities.logger.ILogger;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Abstract class for geometry construction step</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public abstract class GeoConstruction implements GeoObject {
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
	 * Class constants for types of geometric constructions - BEGIN 
	 */
	/**
	 * <i><b>
	 * Ignored construction
	 * </b></i>
	 */
	public static final int GEOCONS_TYPE_IGNORED = -2;
	/**
	 * <i><b>
	 * Undefined construction
	 * </b></i>
	 */
	public static final int GEOCONS_TYPE_UNDEFINED = -1;
	// ======= Points' constructions =======
	// --- Special points ---
	/**
	 * <i><b>
	 * Construction of free point
	 * </b></i>
	 */
	public static final int GEOCONS_TYPE_FREE_POINT = 0;
	/**
	 * <i><b>
	 * Construction of midpoint
	 * </b></i>
	 */
	public static final int GEOCONS_TYPE_MIDPOINT = 1;
	/**
	 * <i><b>
	 * Construction of point that divides segment in given ratio
	 * </b></i>
	 */
	public static final int GEOCONS_TYPE_SEGMENT_RATIO = 2;
	/**
	 * <i><b>
	 * Construction of fourth harmonic point
	 * </b></i>
	 */
	public static final int GEOCONS_TYPE_HARMONIC_POINT = 3;
	/**
	 * <i><b>
	 * Construction of central symmetric point
	 * </b></i>
	 */
	public static final int GEOCONS_TYPE_CENTAL_SYMMETRIC_POINT = 4;
	/**
	 * <i><b>
	 * Construction of translated point
	 * </b></i>
	 */
	public static final int GEOCONS_TYPE_TRANSLATED_POINT = 5;
	/**
	 * <i><b>
	 * Construction of rotated point
	 * </b></i>
	 */
	public static final int GEOCONS_TYPE_ROTATED_POINT = 6;
	/**
	 * <i><b>
	 * Construction of inverse of point with respect to circle
	 * </b></i>
	 */
	public static final int GEOCONS_TYPE_INVERSE_POINT = 7;
	/**
	 * <i><b>
	 * Construction of generalized point that divides segment in given ratio
	 * </b></i>
	 */
	public static final int GEOCONS_TYPE_GEN_SEGMENT_RATIO = 8;
	/**
	 * <i><b>
	 * Construction of a foot from a point to a line, where the line is 
	 * given by two points, for the area method
	 * </b></i>
	 */
	public static final int GEOCONS_TYPE_AM_FOOT_POINT = 9;
	/**
	 * <i><b>
	 * TRATIO construction of a point -
	 * See http://hal.inria.fr/hal-00426563/PDF/areaMethodRecapV2.pdf
	 */
	public static final int GEOCONS_TYPE_TRATIO_POINT = 10;
	/**
	 * <i><b>
	 * PRATIO construction of a point -
	 * See http://hal.inria.fr/hal-00426563/PDF/areaMethodRecapV2.pdf
	 */
	public static final int GEOCONS_TYPE_PRATIO_POINT = 11;
	// put other special point constructions here => [12 .. 19]
	// --- Intersection points ---
	/**
	 * <i><b>
	 * Construction of intersection of two lines/segments or 
	 * two conics or line/segment and conic
	 * </b></i>
	 */
	public static final int GEOCONS_TYPE_INTERSECTION = 20;
	/**
	 * <i><b>
	 * Construction of intersection of two lines given by 
	 * two points each, for the area method
	 * </b></i>
	 */
	public static final int GEOCONS_TYPE_AM_INTERSECTION_POINT = 21;
	// put other intersection point constructions here => [22 .. 29]
	// --- Random points ---
	/**
	 * <i><b>
	 * Construction of random point on line
	 * </b></i>
	 */
	public static final int GEOCONS_TYPE_RAND_POINT_ON_LINE = 30;
	/**
	 * <i><b>
	 * Construction of random point on circle
	 * </b></i>
	 */
	public static final int GEOCONS_TYPE_RAND_POINT_ON_CIRCLE = 31;
	/**
	 * <i><b>
	 * Construction of random point on general conic
	 * </b></i>
	 */
	public static final int GEOCONS_TYPE_RAND_POINT_ON_CONIC = 32;
	// put other random point constructions here => [33 .. 39]
	// ======= Lines' constructions =======
	/**
	 * <i><b>
	 * Construction of line through two points
	 * </b></i>
	 */
	public static final int GEOCONS_TYPE_LINE_THROUGH_TWO_POINTS = 40;
	/**
	 * <i><b>
	 * Construction of parallel line
	 * </b></i>
	 */
	public static final int GEOCONS_TYPE_PARALLEL = 41;
	/**
	 * <i><b>
	 * Construction of perpendicular line
	 * </b></i>
	 */
	public static final int GEOCONS_TYPE_PERPENDICULAR = 42;
	/**
	 * <i><b>
	 * Construction of perpendicular bisector of segment
	 * </b></i>
	 */
	public static final int GEOCONS_TYPE_PERPENDICULAR_BISECTOR = 43;
	/**
	 * <i><b>
	 * Construction of angle bisector
	 * </b></i>
	 */
	public static final int GEOCONS_TYPE_ANGLE_BISECTOR = 44;
	/**
	 * <i><b>
	 * Construction of tangent to conic
	 * </b></i>
	 */
	public static final int GEOCONS_TYPE_TANGENT = 45;
	/**
	 * <i><b>
	 * Construction of radical axis of two circles
	 * </b></i>
	 */
	public static final int GEOCONS_TYPE_RADICAL_AXIS = 46;
	/**
	 * <i><b>
	 * Construction of angle ray
	 * </b></i>
	 */
	public static final int GEOCONS_TYPE_ANGLE_RAY = 47;
	/**
	 * <i><b>
	 * Construction of angle trisector
	 * </b></i>
	 */
	public static final int GEOCONS_TYPE_ANGLE_TRISECTOR = 48;
	/**
	 * <i><b>
	 * Construction of triple angle ray
	 * </b></i>
	 */
	public static final int GEOCONS_TYPE_TRIPLE_ANGLE_RAY = 49;
	/**
	 * <i><b>
	 * Construction of angle ray of third angle to 60 degrees
	 * </b></i>
	 */
	public static final int GEOCONS_TYPE_ANGLE_RAY_TO_60_DEG = 50;
	// put other line constructions here => [51 .. 59]
	// ======= Conics' constructions =======
	/**
	 * <i><b>
	 * Construction of circle with center and point on it
	 * </b></i>
	 */
	public static final int GEOCONS_TYPE_CIRCLE_WITH_CENTER_AND_POINT = 60;
	/**
	 * <i><b>
	 * Construction of circle with center and radius
	 * </b></i>
	 */
	public static final int GEOCONS_TYPE_CIRCLE_WITH_CENTER_AND_RADIUS = 61;
	/**
	 * <i><b>
	 * Construction of circle with diameter
	 * </b></i>
	 */
	public static final int GEOCONS_TYPE_CIRCLE_WITH_DIAMETER = 62;
	/**
	 * <i><b>
	 * Construction of circle with three points on it
	 * </b></i>
	 */
	public static final int GEOCONS_TYPE_CIRCUMSCRIBED_CIRCLE = 63;
	/**
	 * <i><b>
	 * Construction of general conic section
	 * </b></i>
	 */
	public static final int GEOCONS_TYPE_CONIC_SECTION = 64;
	/**
	 * <i><b>
	 * Construction of general conic section with five points
	 * </b></i>
	 */
	public static final int GEOCONS_TYPE_CONIC_SECTION_WITH_FIVE_PTS = 65;
	// put other conic constructions here => [66 .. 69]
	// ======= Various constructions =======
	/**
	 * <i><b>
	 * Construction of angle of 60 degrees
	 * </b></i>
	 */
	public static final int GEOCONS_TYPE_ANGLE_OF_60_DEG = 70;
	// put other constructions here => [71 .. 79]
	/*
	 * The rest of construction types are listed here, but some of them 
	 * could not be implemented with equalities like those that depend 
	 * on order of points on line (marked with *).
	 * 
	 * [points]
	 * public static final int GEOCONS_TYPE_FOOT = 1; // foot of perpendicular line
	 * public static final int GEOCONS_TYPE_CENTER = 1; // conic's center
	 * public static final int GEOCONS_TYPE_FOCUS = 1; // conic's focus
	 * public static final int GEOCONS_TYPE_POLE = 1; // conic's pole
	 * public static final int GEOCONS_TYPE_RAND_POINT_ON_SEGMENT = 1; (*)
	 * public static final int GEOCONS_TYPE_RAND_POINT_ON_RAY = 1; (*)
	 * public static final int GEOCONS_TYPE_RAND_POINT_ON_CIRCLE_ARC = 1; (*)
	 * public static final int GEOCONS_TYPE_RAND_POINT_ON_CONIC_ARC = 1; (*)
	 * 
	 * [lines/segments]
	 * public static final int GEOCONS_TYPE_SEGMENT = 1; (*)
	 * public static final int GEOCONS_TYPE_RAY = 1; (*)
	 * public static final int GEOCONS_TYPE_POLAR = 1; // conic's polar
	 * 
	 * [conics/segments]
	 * public static final int GEOCONS_TYPE_CIRCLE_ARC = 1; (*)
	 * public static final int GEOCONS_TYPE_CONIC_ARC = 1; (*)
	 */
	/* 
	 * Class constants for types of geometric constructions - END 
	 */
	
	// Other class data members
	/**
	 * Label/name of constructed geometric object
	 */
	protected String geoObjectLabel = null;
	/**
	 * Theorem protocol that contains this construction
	 */
	protected OGPTP consProtocol = null;
	/**
	 * Index of this constructed geometric object in theorem protocol
	 */
	protected int index = -1;
	
	
	

	/*
	 * ======================================================================
	 * ========================= ABSTRACT METHODS ===========================
	 * ======================================================================
	 */
	/**
	 * Method that retrieves the type of this geometric construction
	 * 
	 * @return	One of GEOCONS_TYPE_xxx constants
	 */
	public abstract int getConstructionType();
	/**
	 * Method that retrieves text description of geometric construction.
	 * 
	 * @return	Textual description of geometric construction
	 */
	public abstract String getConstructionDesc();
	/**
	 * Method which retrieves a string array with labels of input elements for this construction.
	 * The retrieved array doesn't contain duplicate labels.
	 * 
	 * @return	Array with labels of input elements.
	 */
	public abstract String[] getInputLabels();
	
	

	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * Method that sets the label of constructed geometric object 
	 * (e.g. name of point or line)
	 * 
	 * @param geoObjectLabel The label to set
	 */
	public void setGeoObjectLabel(String geoObjectLabel) {
		this.geoObjectLabel = geoObjectLabel;
	}
	/**
	 * Method that retrieves the label of constructed geometric object
	 * 
	 * @return The label of geometric object
	 * 
	 * @see com.ogprover.pp.tp.geoobject.GeoObject#getGeoObjectLabel()
	 */
	public String getGeoObjectLabel() {
		return geoObjectLabel;
	}
	
	/**
	 * Method that sets theorem protocol that contains this construction
	 * 
	 * @param consProtocol The theorem protocol to set
	 */
	public void setConsProtocol(OGPTP consProtocol) {
		this.consProtocol = consProtocol;
	}

	/**
	 * Method that retrieves theorem protocol that contains this construction
	 * 
	 * @return The theorem protocol
	 */
	public OGPTP getConsProtocol() {
		return consProtocol;
	}

	/**
	 * Method that sets the index of this object in theorem protocol
	 * 
	 * @param index The index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * Method that retrieves the index of this object in theorem protocol
	 * 
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
	
	

	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method that checks whether this construction step is valid i.e.
	 * if it can be executed from previously constructed elements of
	 * its theorem protocol.
	 * 
	 * @return	True if step is valid, false otherwise
	 */
	public boolean isValidConstructionStep() {
		// This base method will only check if object is 
		// assigned to theorem protocol; there will be
		// only one mapping in theorem protocol for the
		// label of this object, so duplicate objects will be checked
		// when validating whole theorem protocol.
		
		OGPOutput output = OpenGeoProver.settings.getOutput();
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		try {
			if (this.consProtocol == null) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Object " + this.geoObjectLabel + " not assigned to theorem protocol.");
				return false; // object not assigned to any theorem protocol
			}
		
			return true;
		} catch (IOException e) {
			logger.error("Failed to write to output file(s).");
			output.close();
			return false;
		}
	}
	
}
