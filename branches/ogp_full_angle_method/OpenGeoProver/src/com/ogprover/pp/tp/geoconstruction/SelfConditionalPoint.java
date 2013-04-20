/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.geoconstruction;

import java.io.IOException;
import java.util.Map;

import com.ogprover.main.OGPConstants;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.SymbolicPolynomial;
import com.ogprover.polynomials.XPolySystem;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.utilities.io.OGPOutput;
import com.ogprover.utilities.io.SpecialFileFormatting;
import com.ogprover.utilities.logger.ILogger;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Abstract class for self-conditional points i.e for points 
*     that contain conditions for their own coordinates</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public abstract class SelfConditionalPoint extends Point {
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
	 * ========================= ABSTRACT METHODS ===========================
	 * ======================================================================
	 */
	/**
	 * Method that retrieves the condition for X coordinate of this point
	 * 
	 * @return The condition
	 */
	public abstract SymbolicPolynomial getXCondition();
	/**
	 * Method that retrieves the condition for Y coordinate of this point
	 * 
	 * @return The condition
	 */
	public abstract SymbolicPolynomial getYCondition();
	/**
	 * Method which returns map of points used to instantiate conditions.
	 * 		
	 * @return	Map of points.
	 */
	public abstract Map<String, Point> getPointsForInstantiation();
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method that retrieves the instance of condition for X coordinate of this
	 * point expressed as x-polynomial (after symbolic coordinates of this point
	 * have been substituted by UX variables)
	 *  
	 * @param pointsMap		Map of points assigned to labels of common points from
	 * 						symbolic polynomial representing the condition,
	 * 						used for instantiation of that condition
	 * @return	X-polynomial representing the condition for X coordinate of this 
	 * 			point in algebraic form
	 */
	public XPolynomial instantiateXCondition(Map<String, Point> pointsMap) {
		return OGPTP.instantiateCondition(this.getXCondition(), pointsMap);
	}

	/**
	 * Method that retrieves the instance of condition for Y coordinate of this
	 * point expressed as x-polynomial (after symbolic coordinates of this point
	 * have been substituted by UX variables)
	 * 
	 * @param pointsMap		Map of points assigned to labels of common points from
	 * 						symbolic polynomial representing the condition,
	 * 						used for instantiation of that condition 
	 * @return	X-polynomial representing the condition for Y coordinate of this 
	 * 			point in algebraic form
	 */
	public XPolynomial instantiateYCondition(Map<String, Point> pointsMap) {
		return OGPTP.instantiateCondition(this.getYCondition(), pointsMap);
	}
	
	/**
	 * Method that transforms the construction of this point into algebraic form
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.Point#transformToAlgebraicForm()
	 */
	@Override
	public int transformToAlgebraicForm() {
		return this.transformToAlgebraicForm(this.getPointsForInstantiation());
	}
	
	/**
	 * Method that transforms the construction of this point into algebraic form
	 * 
	 * @param pointsMap		Map of points assigned to generic labels, used for instantiation
	 * 						of conditions
	 * @return				SUCCESS if transformation is successful, general error otherwise 
	 */
	public int transformToAlgebraicForm(Map<String, Point> pointsMap) {
		OGPOutput output = OpenGeoProver.settings.getOutput();
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		try {
			output.openSubSection("Transformation of point " + this.geoObjectLabel + ": ", true);
			output.openEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
			
			// instantiate the coordinates of this point
			this.consProtocol.instantiatePoint(this, Point.POINT_TYPE_DEPENDENT);
			output.openItem();
			output.writePointCoordinatesAssignment(this);
			output.closeItem();
		
			// instantiate the first condition
			XPolynomial xCondInstance = this.instantiateXCondition(pointsMap).reduceByUTermDivision();
			
			if (xCondInstance == null) {
				output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
				output.openEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
				output.openItemWithDesc("Error:");
				output.closeItemWithDesc("Failed to instantiate the condition");
				output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
				output.closeSubSection();
				return OGPConstants.ERR_CODE_GENERAL;
			}
		
			// process the polynomial
			output.openItem();
			output.writePlainText("Instantiating condition for X-coordinate of this point");
			output.closeItem();
			output.openItem();
			int xRetCode = this.processConstructionPolynomial(xCondInstance, true);
			output.closeItem();
		
			switch (xRetCode) {
			case Point.PROCESSPOLY_RETCODE_BAD_POLYNOMIAL:
			case OGPConstants.ERR_CODE_GENERAL:
				logger.error("Error or bad polynomial obtained during processing");
				output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
				output.openEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
				output.openItemWithDesc("Error:");
				output.closeItemWithDesc("Failed to process the condition");
				output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
				output.closeSubSection();
				return OGPConstants.ERR_CODE_GENERAL; // leave the method
			case Point.PROCESSPOLY_RETCODE_TRY_AGAIN:
				// this return code is invalid for dependent point - therefore error
				logger.error("Bad polynomial obtained during processing");
				output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
				output.openEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
				output.openItemWithDesc("Error:");
				output.closeItemWithDesc("Failed to process the condition");
				output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
				output.closeSubSection();
				return OGPConstants.ERR_CODE_GENERAL; // leave the method
			case Point.PROCESSPOLY_RETCODE_COORDINATES_RENAMED:
				logger.info("Renamed coordinates of point " + this.geoObjectLabel);
				output.openItem();
				output.writePlainText("Point " + this.geoObjectLabel + " has been renamed. ");
				output.writePointCoordinatesAssignment(this);
				output.closeItem();
				break;
			case Point.PROCESSPOLY_RETCODE_ADDED_TO_SYSTEM:
				output.openItem();
				output.writePlainText("Instantiated condition ");
				output.writePolynomial(xCondInstance);
				output.writePlainText(" is added to polynomial system\n");
				output.closeItem();
				break;
			default:
				logger.error("Unknown error during processing");
				output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
				output.openEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
				output.openItemWithDesc("Error:");
				output.closeItemWithDesc("Failed to process the condition");
				output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
				output.closeSubSection();
				return OGPConstants.ERR_CODE_GENERAL; // leave the method
			}
		
			// instantiate the second condition
			output.openItem();
			output.writePlainText("Instantiating condition for Y-coordinate of this point");
			output.closeItem();
			XPolynomial yCondInstance = this.instantiateYCondition(pointsMap).reduceByUTermDivision();
		
			// process the polynomial
			output.openItem();
			int yRetCode = this.processConstructionPolynomial(yCondInstance, true);
			output.closeItem();
		
			switch (yRetCode) {
			case Point.PROCESSPOLY_RETCODE_BAD_POLYNOMIAL:
			case OGPConstants.ERR_CODE_GENERAL:
				logger.error("Error or bad polynomial obtained during processing");
				output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
				output.openEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
				output.openItemWithDesc("Error:");
				output.closeItemWithDesc("Failed to process the condition");
				output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
				output.closeSubSection();
				return OGPConstants.ERR_CODE_GENERAL; // leave the method
			case Point.PROCESSPOLY_RETCODE_TRY_AGAIN:
				// this return code is invalid for dependent point - therefore error
				logger.error("Bad polynomial obtained during processing");
				output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
				output.openEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
				output.openItemWithDesc("Error:");
				output.closeItemWithDesc("Failed to process the condition");
				output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
				output.closeSubSection();
				return OGPConstants.ERR_CODE_GENERAL; // leave the method
			case Point.PROCESSPOLY_RETCODE_COORDINATES_RENAMED:
				logger.info("Renamed coordinates of point " + this.geoObjectLabel);
				output.openItem();
				output.writePlainText("Point " + this.geoObjectLabel + " has been renamed. ");
				output.writePointCoordinatesAssignment(this);
				output.closeItem();
				break;
			case Point.PROCESSPOLY_RETCODE_ADDED_TO_SYSTEM:
				output.openItem();
				output.writePlainText("Instantiated condition ");
				output.writePolynomial(yCondInstance);
				output.writePlainText(" is added to polynomial system\n");
				output.closeItem();
				break;
			default:
				logger.error("Unknown error during processing");
				output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
				output.openEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
				output.openItemWithDesc("Error:");
				output.closeItemWithDesc("Failed to process the condition");
				output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
				output.closeSubSection();
				return OGPConstants.ERR_CODE_GENERAL; // leave the method
			}
		
			// If coordinates were renamed second time and not renamed first time
			// then re-instantiate and re-process the first condition
			if (yRetCode == Point.PROCESSPOLY_RETCODE_COORDINATES_RENAMED &&
				xRetCode == Point.PROCESSPOLY_RETCODE_ADDED_TO_SYSTEM) {
				// first of all remove first polynomial from the system - it is on last place
				XPolySystem system = this.consProtocol.getAlgebraicGeoTheorem().getHypotheses();
				system.removePoly(system.getPolynomials().size() - 1);
			
				output.openItem();
				output.writePlainText("Repeating instantiation of condition for X-coordinate of this point, after it has been renamed");
				output.closeItem();
				xCondInstance = this.instantiateXCondition(pointsMap).reduceByUTermDivision();
				output.openItem();
				xRetCode = this.processConstructionPolynomial(xCondInstance, true);
				output.closeItem();
			
				switch (xRetCode) {
				case Point.PROCESSPOLY_RETCODE_BAD_POLYNOMIAL:
				case OGPConstants.ERR_CODE_GENERAL:
					logger.error("Error or bad polynomial obtained during processing");
					output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
					output.openEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
					output.openItemWithDesc("Error:");
					output.closeItemWithDesc("Failed to process the condition");
					output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
					output.closeSubSection();
					return OGPConstants.ERR_CODE_GENERAL; // leave the method
				case Point.PROCESSPOLY_RETCODE_TRY_AGAIN:
					// this return code is invalid for dependent point - therefore error
					logger.error("Bad polynomial obtained during processing");
					output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
					output.openEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
					output.openItemWithDesc("Error:");
					output.closeItemWithDesc("Failed to process the condition");
					output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
					output.closeSubSection();
					return OGPConstants.ERR_CODE_GENERAL; // leave the method
				case Point.PROCESSPOLY_RETCODE_COORDINATES_RENAMED:
					logger.info("Renamed coordinates of point " + this.geoObjectLabel);
					output.openItem();
					output.writePlainText("Point " + this.geoObjectLabel + " has been renamed. ");
					output.writePointCoordinatesAssignment(this);
					output.closeItem();
					break;
				case Point.PROCESSPOLY_RETCODE_ADDED_TO_SYSTEM:
					output.openItem();
					output.writePlainText("Instantiated condition ");
					output.writePolynomial(xCondInstance);
					output.writePlainText(" is added to polynomial system\n");
					output.closeItem();
					break;
				default:
					logger.error("Unknown error during processing");
					output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
					output.openEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
					output.openItemWithDesc("Error:");
					output.closeItemWithDesc("Failed to process the condition");
					output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
					output.closeSubSection();
					return OGPConstants.ERR_CODE_GENERAL; // leave the method
				}
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

	