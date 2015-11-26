/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.geoconstruction;

import java.io.IOException;
import java.util.HashMap;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.UXVariable;
import com.ogprover.utilities.io.OGPOutput;
import com.ogprover.utilities.logger.ILogger;



/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for random point from general conic section</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class RandomPointFromGeneralConic extends RandomPointFromParametricSet {
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
		return GeoConstruction.GEOCONS_TYPE_RAND_POINT_ON_CONIC;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * 
	 * @param pointLabel	Label of this point
	 * @param conic			Base conic which this point belongs to
	 */
	public RandomPointFromGeneralConic(String pointLabel, ConicSection conic) {
		this.geoObjectLabel = pointLabel;
		this.baseSetOfPoints = (SetOfPoints) conic;
		// add point to point set
		if (this.baseSetOfPoints != null)
			this.baseSetOfPoints.addPointToSet(this);
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
		// call constructor with null set of points to avoid adding cloned point to
		// that set of points; later just set the base set of points reference
		RandomPointFromGeneralConic p = new RandomPointFromGeneralConic(this.geoObjectLabel, null);
		p.setBaseSetOfPoints(this.baseSetOfPoints);
		
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
	 * Method to check the validity of this construction step
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#isValidConstructionStep()
	 */
	@Override
	public boolean isValidConstructionStep() {
		OGPOutput output = OpenGeoProver.settings.getOutput();
		ILogger logger = OpenGeoProver.settings.getLogger();
		ConicSection baseConic = (ConicSection) this.baseSetOfPoints;
		
		if (!super.isValidConstructionStep())
			return false;
		
		try {
			if (baseConic == null) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Random point " + this.getGeoObjectLabel() + " can't be constructed since its base conic is not constructed");
				return false;
			}
			
			if (((GeoConstruction)baseConic).getIndex() < 0 || ((GeoConstruction)baseConic).getIndex() >= this.index) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Random point " + this.getGeoObjectLabel() + " can't be constructed since its base conic is not yet constructed or not added to theorem protocol");
				return false;
			}
		} catch (IOException e) {
			logger.error("Failed to write to output file(s).");
			output.close();
			return false;
		}
		
		return true;
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getConstructionDesc()
	 */
	@Override
	public String getConstructionDesc() {
		StringBuilder sb = new StringBuilder();
		sb.append("Random point ");
		sb.append(this.geoObjectLabel);
		sb.append(" from general conic ");
		sb.append(((GeoConstruction)this.baseSetOfPoints).getGeoObjectLabel());
		return sb.toString();
	}

	@Override
	public Point replace(HashMap<Point, Point> replacementMap) {
		OpenGeoProver.settings.getLogger().error("This method should not be called on this class.");
		return null;
	}
}

