/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.geoconstruction;

import java.io.IOException;

import com.ogprover.main.OGPConstants;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.pp.tp.auxiliary.PointSetRelationshipManager;
import com.ogprover.utilities.io.OGPOutput;
import com.ogprover.utilities.io.SpecialFileFormatting;
import com.ogprover.utilities.logger.ILogger;



/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for random point from some set of points (line, circle, conic section)</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public abstract class RandomPointFromSetOfPoints extends Point {
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
	 * Base set of points that this random point belongs to
	 */
	protected SetOfPoints baseSetOfPoints;
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * Method that sets the base set of points
	 * 
	 * @param baseSetOfPoints The base set of points to set
	 */
	public void setBaseSetOfPoints(SetOfPoints baseSetOfPoints) {
		this.baseSetOfPoints = baseSetOfPoints;
	}
	
	/**
	 * Method that retrieves the base set of points
	 * 
	 * @return The base set of points
	 */
	public SetOfPoints getBaseSetOfPoints() {
		return baseSetOfPoints;
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
		ILogger logger = OpenGeoProver.settings.getLogger();
		OGPOutput output = OpenGeoProver.settings.getOutput();
		
		try {
			output.openSubSection("Transformation of point " + this.geoObjectLabel + ": ", true);
			output.openEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
		
			// instantiate half dependent point - start with independent x coordinate
			// (if necessary it will be changed when best elements are being searched for
			// during instantiation of condition for this point)
			this.consProtocol.instantiatePoint(this, Point.POINT_TYPE_X_INDEPENDENT);
			output.openItem();
			output.writePointCoordinatesAssignment(this);
			output.closeItem();
		
			// create manager object for relationship between this point and base set of points
			PointSetRelationshipManager manager = new PointSetRelationshipManager(this.baseSetOfPoints, this);
		
			// instantiate the condition and simplify it
			XPolynomial condition = manager.retrieveInstantiatedCondition();
			output.openItem();
			output.writePlainText("Polynomial that point " + this.geoObjectLabel + " has to satisfy is:");
			output.writePolynomial(condition);
			output.closeItem();
		
			if (condition == null) {
				logger.error("Failed to instantiate the condition for this point");
				output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
				output.closeSubSection();
				return OGPConstants.ERR_CODE_GENERAL;
			}
		
			// process the condition (check for special forms of instantiated polynomial) and log result in output file
			output.openItem();
			int retCode = this.processConstructionPolynomial(condition, true);
			output.closeItem();
		
			switch (retCode) {
			case Point.PROCESSPOLY_RETCODE_BAD_POLYNOMIAL:
			case OGPConstants.ERR_CODE_GENERAL:
			case Point.PROCESSPOLY_RETCODE_TRY_AGAIN: // since best elements are found, then processing should not return bad polynomial  
				logger.error("Error or bad polynomial instantiated when transforming to algebraic form");
				output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
				output.openEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
				output.openItemWithDesc("Error:");
				output.closeItemWithDesc("Failed to process the condition");
				output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
				output.closeSubSection();
				return OGPConstants.ERR_CODE_GENERAL;
			case Point.PROCESSPOLY_RETCODE_COORDINATES_RENAMED:
				logger.info("Renamed coordinates of point " + this.getGeoObjectLabel());
				output.openItem();
				output.writePlainText("Point " + this.geoObjectLabel + " has been renamed. ");
				output.writePointCoordinatesAssignment(this);
				output.closeItem();
				break;
			case Point.PROCESSPOLY_RETCODE_ADDED_TO_SYSTEM:
				logger.info("New polynomial condition added to system of hypotheses");
				output.openItem();
				output.writePlainText("New polynomial added to system of hypotheses");
				output.closeItem();
				break;
			default:
				logger.error("Unknown return result from processing of condition");
				output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
				output.openEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
				output.openItemWithDesc("Error:");
				output.closeItemWithDesc("Failed to process the condition");
				output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
				output.closeSubSection();
				return OGPConstants.ERR_CODE_GENERAL;
			}
		
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
	 * <i>
	 * Method for creation of new random point from point set.
	 * </i>
	 * 
	 * @param ptLabel	Label of new random point.
	 * @param ptSet		Set of points where new point belongs to.
	 * @return			New point or null if unable to create (unknown set of point type).
	 */
	public static Point createRandomPoint(String ptLabel, SetOfPoints ptSet) {
		if (ptSet instanceof Line)
			return new RandomPointFromLine(ptLabel, (Line)ptSet);
		
		if (ptSet instanceof Circle)
			return new RandomPointFromCircle(ptLabel, (Circle)ptSet);
		
		if (ptSet instanceof GeneralConicSection)
			return new RandomPointFromGeneralConic(ptLabel, (GeneralConicSection)ptSet);
		
		// TODO - if new type of point set is defined, add corresponding piece of code for it here.
		
		return null; // unknown type of point set
	}
	
	/**
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getInputLabels()
	 */
	@Override
	public String[] getInputLabels() {
		String[] inputLabels = new String[1];
		inputLabels[0] = ((GeoConstruction)this.baseSetOfPoints).getGeoObjectLabel();
		return inputLabels;
	}

}

