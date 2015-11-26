/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.thmstatement;

import java.util.Vector;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.pp.tp.geoconstruction.GeoConstruction;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Abstract class for elementary geometry theorem statements</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public abstract class ElementaryThmStatement extends ThmStatement {
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
	 * List of all geometry objects that appear in this theorem statement.
	 */
	protected Vector<GeoConstruction> geoObjects = null;
	
	
	
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
	 * @return the geoObjects
	 */
	public Vector<GeoConstruction> getGeoObjects() {
		return geoObjects;
	}

	/**
	 * @param geoObjects the geoObjects to set
	 */
	public void setGeoObjects(Vector<GeoConstruction> geoObjects) {
		this.geoObjects = geoObjects;
	}
	
	

	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * @see com.ogprover.pp.tp.thmstatement.ThmStatement#isValid()
	 */
	@Override
	public boolean isValid() {
		// Elementary geometry theorem statement is valid iff all objects
		// that appear in it are constructed within same theorem protocol.
		
		// First of all call method from superclass
		if (!super.isValid())
			return false;
		
		if (this.geoObjects == null || this.geoObjects.size() == 0) {
			OpenGeoProver.settings.getLogger().error("Theorem statement doesn't contain geometry objects");
			return false;
		}
		
		for (GeoConstruction geoCons : this.geoObjects) {
			if (this.consProtocol.getConstructionSteps().indexOf(geoCons) < 0) {
				OpenGeoProver.settings.getLogger().error("Geometry object " + geoCons.getGeoObjectLabel() + " not found in list of all constructed objects.");
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * @see com.ogprover.pp.tp.thmstatement.ThmStatement#getInputLabels()
	 */
	@Override
	public String[] getInputLabels() {
		String[] inputLabels;
		
		if (this.geoObjects == null || this.geoObjects.size() == 0)
			return null;
		
		inputLabels = new String[this.geoObjects.size()];
		int ii = 0;
		for (GeoConstruction gc : this.geoObjects)
			inputLabels[ii++] = gc.getGeoObjectLabel();
		return inputLabels;
	}
}