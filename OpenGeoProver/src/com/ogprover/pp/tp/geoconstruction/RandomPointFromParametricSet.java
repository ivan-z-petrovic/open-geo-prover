/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.geoconstruction;

import java.io.IOException;

import com.ogprover.main.OGPConstants;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.Variable;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.pp.tp.auxiliary.PointSetRelationshipManager;
import com.ogprover.utilities.io.OGPOutput;
import com.ogprover.utilities.io.SpecialFileFormatting;
import com.ogprover.utilities.logger.ILogger;




/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Abstract class for random point from parametric set of points</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public abstract class RandomPointFromParametricSet extends RandomPointFromSetOfPoints {
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
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method that transforms the construction of this point into algebraic form
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.RandomPointFromSetOfPoints#transformToAlgebraicForm()
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
			
			// if point has been instantiated as the origin - exit the method
			if (this.getX().getVariableType() == Variable.VAR_TYPE_UX_U && this.getX().getIndex() == 0 &&
				this.getY().getVariableType() == Variable.VAR_TYPE_UX_U && this.getY().getIndex() == 0) {
				output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
				output.closeSubSection();
				return OGPConstants.RET_CODE_SUCCESS;
			}
		
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
}

