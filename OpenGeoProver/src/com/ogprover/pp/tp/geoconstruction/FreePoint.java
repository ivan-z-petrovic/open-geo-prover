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
import com.ogprover.utilities.io.SpecialFileFormatting;
import com.ogprover.utilities.logger.ILogger;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for free points</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class FreePoint extends Point {
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
		return GeoConstruction.GEOCONS_TYPE_FREE_POINT;
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
	 */
	public FreePoint(String pointLabel) {
		this.geoObjectLabel = pointLabel;
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
		Point p = new FreePoint(this.geoObjectLabel);
		
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
		if (!super.isValidConstructionStep())
			return false;
		
		// Construction of free point is always valid
		return true;
	}

	/**
	 * Method to transform construction of free point into algebraic form
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.Point#transformToAlgebraicForm()
	 */
	@Override
	public int transformToAlgebraicForm() {
		OGPOutput output = OpenGeoProver.settings.getOutput();
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		try {
			output.openSubSection("Transformation of point " + this.geoObjectLabel + ": ", true);
			output.openEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
			// Let Construction Protocol assign UX variables to coordinates of this free point
			this.consProtocol.instantiatePoint(this, Point.POINT_TYPE_FREE);
			output.openItem();
			output.writePointCoordinatesAssignment(this);
			output.closeItem();
			output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
			output.closeSubSection();
		} catch (IOException e) {
			logger.error("Failed to write to output file(s).");
			output.close();
			return OGPConstants.ERR_CODE_GENERAL;
		}
		
		return OGPConstants.RET_CODE_SUCCESS;
	}



	/**
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getConstructionDesc()
	 */
	@Override
	public String getConstructionDesc() {
		StringBuilder sb = new StringBuilder();
		sb.append("Free point ");
		sb.append(this.geoObjectLabel);
		return sb.toString();
	}
	
	/**
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getInputLabels()
	 */
	@Override
	public String[] getInputLabels() {
		return null;
	}

	@Override
	public Point replace(HashMap<Point, Point> replacementMap) {
		return this;
	}
}

