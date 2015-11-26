/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.geoconstruction;

import java.io.IOException;
import java.util.HashMap;

import com.ogprover.main.OGPConstants;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.UXVariable;
import com.ogprover.polynomials.XPolySystem;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.pp.tp.auxiliary.PointSetRelationshipManager;
import com.ogprover.utilities.io.OGPOutput;
import com.ogprover.utilities.io.SpecialFileFormatting;
import com.ogprover.utilities.logger.ILogger;



/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for intersection point of two points' sets</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class IntersectionPoint extends Point {
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
	 * First set of points that this point belongs to
	 */
	private SetOfPoints firstPointSet = null;
	/**
	 * Second set of points that this point belongs to
	 */
	private SetOfPoints secondPointSet = null;
	

	
	
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
		return GeoConstruction.GEOCONS_TYPE_RAND_POINT_ON_LINE;
	}
	
	/**
	 * Method that sets the first set of points
	 * 
	 * @param firstSet	Set of points to set
	 */
	public void setFirstPointSet(SetOfPoints firstSet) {
		this.firstPointSet = firstSet;
	}
	
	/**
	 * Method that retrieves the first set of points
	 * 
	 * @return	First set of points
	 */
	public SetOfPoints getFirstPointSet() {
		return this.firstPointSet;
	}
	
	/**
	 * Method that sets the second set of points
	 * 
	 * @param secondSet	Set of points to set
	 */
	public void setSecondPointSet(SetOfPoints secondSet) {
		this.secondPointSet = secondSet;
	}
	
	/**
	 * Method that retrieves the second set of points
	 * 
	 * @return	Second set of points
	 */
	public SetOfPoints getSecondPointSet() {
		return this.secondPointSet;
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
	 * @param firstSet		First set of points that this point belongs to
	 * @param secondSet		Second set of points that this point belongs to
	 */
	public IntersectionPoint(String pointLabel, SetOfPoints firstSet, SetOfPoints secondSet) {
		this.geoObjectLabel = pointLabel;
		this.firstPointSet = firstSet;
		// add point to point set
		if (this.firstPointSet != null)
			this.firstPointSet.addPointToSet(this);
		this.secondPointSet = secondSet;
		// add point to point set
		if (this.secondPointSet != null)
			this.secondPointSet.addPointToSet(this);
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
		// clone this point with null point sets to avoid adding cloned point to these sets;
		// after that these sets can be set manually
		IntersectionPoint p = new IntersectionPoint(this.geoObjectLabel, null, null);
		p.setFirstPointSet(this.firstPointSet);
		p.setSecondPointSet(this.secondPointSet);
		
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
		
		if (!super.isValidConstructionStep())
			return false;
		
		try {
			if (this instanceof AMIntersectionPoint) {
				return true;
			}
			
			if (this.firstPointSet == null || this.secondPointSet == null) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Intersection point " + this.getGeoObjectLabel() + " can't be constructed since some base points' set is not constructed");
				return false;
			}
			
			if (((GeoConstruction)this.firstPointSet).getIndex() < 0 || ((GeoConstruction)this.firstPointSet).getIndex() >= this.index ||
				((GeoConstruction)this.secondPointSet).getIndex() < 0 || ((GeoConstruction)this.secondPointSet).getIndex() >= this.index) {
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
	
	/**
	 * Method that transforms the construction of this point into algebraic form
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.Point#transformToAlgebraicForm()
	 */
	@Override
	public int transformToAlgebraicForm() {
		return this.transformToAlgebraicFormWithOutputPrintFlag(true);
	}
	
	/**
	 * Method that transforms the construction of this point into algebraic form
	 * with specified flag that determines whether to write results to output
	 * files or not. It is necessary to have this method in situations when some
	 * temporary point is added to theorem protocol and it has to be transformed
	 * to algebraic form - we don't want to print results for temporary points.
	 * 
	 * @param outputPrintFlag	Flag that determines whether to print results of 
	 * 							transformation to algebraic form or not
	 * @return					Success or general error
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.Point#transformToAlgebraicForm()
	 */
	public int transformToAlgebraicFormWithOutputPrintFlag(boolean outputPrintFlag) {
		ILogger logger = OpenGeoProver.settings.getLogger();
		OGPOutput output = OpenGeoProver.settings.getOutput();
		
		try {
			if (outputPrintFlag) {
				output.openSubSection("Transformation of point " + this.geoObjectLabel + ": ", true);
				output.openEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
			}
		
			// instantiate fully dependent point 
			this.consProtocol.instantiatePoint(this, Point.POINT_TYPE_DEPENDENT);
			if (outputPrintFlag) {
				output.openItem();
				output.writePointCoordinatesAssignment(this);
				output.closeItem();
			}
		
			// create manager object for relationship between this point and first set of points
			PointSetRelationshipManager manager1 = new PointSetRelationshipManager(this.firstPointSet, this);
		
			// instantiate the condition and simplify it
			XPolynomial condition1 = manager1.retrieveInstantiatedCondition();
			
			if (condition1 == null) {
				logger.error("Failed to instantiate the first condition for this point");
				if (outputPrintFlag) {
					output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
					output.closeSubSection();
				}
				return OGPConstants.ERR_CODE_GENERAL;
			}
			
			if (outputPrintFlag) {
				output.openItem();
				output.writePlainText("Polynomial that point " + this.geoObjectLabel + " has to satisfy is:");
				output.writePolynomial(condition1);
				output.closeItem();
			}
		
			// process the condition (check for special forms of instantiated polynomial) and log result in output file
			if (outputPrintFlag)
				output.openItem();
			int retCode1 = this.processConstructionPolynomial(condition1, outputPrintFlag);
			if (outputPrintFlag)
				output.closeItem();
		
			switch (retCode1) {
			case Point.PROCESSPOLY_RETCODE_BAD_POLYNOMIAL:
			case OGPConstants.ERR_CODE_GENERAL:
			case Point.PROCESSPOLY_RETCODE_TRY_AGAIN: // since best elements are found, then processing should not return bad polynomial  
				logger.error("Error or bad polynomial instantiated when transforming to algebraic form");
				if (outputPrintFlag) {
					output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
					output.openEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
					output.openItemWithDesc("Error:");
					output.closeItemWithDesc("Failed to process the condition");
					output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
					output.closeSubSection();
				}
				return OGPConstants.ERR_CODE_GENERAL;
			case Point.PROCESSPOLY_RETCODE_COORDINATES_RENAMED:
				logger.info("Renamed coordinates of point " + this.getGeoObjectLabel());
				if (outputPrintFlag) {
					output.openItem();
					output.writePlainText("Point " + this.geoObjectLabel + " has been renamed. ");
					output.writePointCoordinatesAssignment(this);
					output.closeItem();
				}
				break;
			case Point.PROCESSPOLY_RETCODE_ADDED_TO_SYSTEM:
				logger.info("New polynomial condition added to system of hypotheses");
				if (outputPrintFlag) {
					output.openItem();
					output.writePlainText("New polynomial added to system of hypotheses");
					output.closeItem();
				}
				break;
			default:
				logger.error("Unknown return result from processing of condition");
				if (outputPrintFlag) {
					output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
					output.openEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
					output.openItemWithDesc("Error:");
					output.closeItemWithDesc("Failed to process the condition");
					output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
					output.closeSubSection();
				}
				return OGPConstants.ERR_CODE_GENERAL;
			}
			
			// create manager object for relationship between this point and second set of points
			PointSetRelationshipManager manager2 = new PointSetRelationshipManager(this.secondPointSet, this);
		
			// instantiate the condition and simplify it
			XPolynomial condition2 = manager2.retrieveInstantiatedCondition();
			
			if (condition2 == null) {
				logger.error("Failed to instantiate the second condition for this point");
				if (outputPrintFlag) {
					output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
					output.closeSubSection();
				}
				return OGPConstants.ERR_CODE_GENERAL;
			}
			
			if (outputPrintFlag) {
				output.openItem();
				output.writePlainText("Polynomial that point " + this.geoObjectLabel + " has to satisfy is:");
				output.writePolynomial(condition2);
				output.closeItem();
			}
		
			// process the condition (check for special forms of instantiated polynomial) and log result in output file
			if (outputPrintFlag)
				output.openItem();
			int retCode2 = this.processConstructionPolynomial(condition2, outputPrintFlag);
			if (outputPrintFlag)
				output.closeItem();
		
			switch (retCode2) {
			case Point.PROCESSPOLY_RETCODE_BAD_POLYNOMIAL:
			case OGPConstants.ERR_CODE_GENERAL:
			case Point.PROCESSPOLY_RETCODE_TRY_AGAIN: // since best elements are found, then processing should not return bad polynomial  
				logger.error("Error or bad polynomial instantiated when transforming to algebraic form");
				if (outputPrintFlag) {
					output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
					output.openEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
					output.openItemWithDesc("Error:");
					output.closeItemWithDesc("Failed to process the condition");
					output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
					output.closeSubSection();
				}
				return OGPConstants.ERR_CODE_GENERAL;
			case Point.PROCESSPOLY_RETCODE_COORDINATES_RENAMED:
				logger.info("Renamed coordinates of point " + this.getGeoObjectLabel());
				if (outputPrintFlag) {
					output.openItem();
					output.writePlainText("Point " + this.geoObjectLabel + " has been renamed. ");
					output.writePointCoordinatesAssignment(this);
					output.closeItem();
				}
				break;
			case Point.PROCESSPOLY_RETCODE_ADDED_TO_SYSTEM:
				logger.info("New polynomial condition added to system of hypotheses");
				if (outputPrintFlag) {
					output.openItem();
					output.writePlainText("New polynomial added to system of hypotheses");
					output.closeItem();
				}
				break;
			default:
				logger.error("Unknown return result from processing of condition");
				if (outputPrintFlag) {
					output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
					output.openEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
					output.openItemWithDesc("Error:");
					output.closeItemWithDesc("Failed to process the condition");
					output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
					output.closeSubSection();
				}
				return OGPConstants.ERR_CODE_GENERAL;
			}
			
			// If coordinates were renamed second time and not renamed first time
			// then re-instantiate and re-process the first condition
			if (retCode2 == Point.PROCESSPOLY_RETCODE_COORDINATES_RENAMED &&
				retCode1 == Point.PROCESSPOLY_RETCODE_ADDED_TO_SYSTEM) {
				// first of all remove first polynomial from the system - it is on last place
				XPolySystem system = this.consProtocol.getAlgebraicGeoTheorem().getHypotheses();
				system.removePoly(system.getPolynomials().size() - 1);
			
				if (outputPrintFlag) {
					output.openItem();
					output.writePlainText("Repeating instantiation of first condition of this point, after its coordinate has been renamed");
					output.closeItem();
				}
				// instantiate the condition and simplify it
				manager1.setPoint(this);
				condition1 = manager1.retrieveInstantiatedCondition();
				if (outputPrintFlag) {
					output.openItem();
					output.writePlainText("Polynomial that point " + this.geoObjectLabel + " has to satisfy is:");
					output.writePolynomial(condition1);
					output.closeItem();
				}
			
				if (condition1 == null) {
					logger.error("Failed to instantiate the first condition for this point");
					if (outputPrintFlag) {
						output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
						output.closeSubSection();
					}
					return OGPConstants.ERR_CODE_GENERAL;
				}
			
				// process the condition (check for special forms of instantiated polynomial) and log result in output file
				if (outputPrintFlag)
					output.openItem();
				retCode1 = this.processConstructionPolynomial(condition1, outputPrintFlag);
				if (outputPrintFlag)
					output.closeItem();
			
				switch (retCode1) {
				case Point.PROCESSPOLY_RETCODE_BAD_POLYNOMIAL:
				case OGPConstants.ERR_CODE_GENERAL:
				case Point.PROCESSPOLY_RETCODE_TRY_AGAIN: // since best elements are found, then processing should not return bad polynomial  
					logger.error("Error or bad polynomial instantiated when transforming to algebraic form");
					if (outputPrintFlag) {
						output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
						output.openEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
						output.openItemWithDesc("Error:");
						output.closeItemWithDesc("Failed to process the condition");
						output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
						output.closeSubSection();
					}
					return OGPConstants.ERR_CODE_GENERAL;
				case Point.PROCESSPOLY_RETCODE_COORDINATES_RENAMED:
					logger.info("Renamed coordinates of point " + this.getGeoObjectLabel());
					if (outputPrintFlag) {
						output.openItem();
						output.writePlainText("Point " + this.geoObjectLabel + " has been renamed. ");
						output.writePointCoordinatesAssignment(this);
						output.closeItem();
					}
					break;
				case Point.PROCESSPOLY_RETCODE_ADDED_TO_SYSTEM:
					logger.info("New polynomial condition added to system of hypotheses");
					if (outputPrintFlag) {
						output.openItem();
						output.writePlainText("New polynomial added to system of hypotheses");
						output.closeItem();
					}
					break;
				default:
					logger.error("Unknown return result from processing of condition");
					if (outputPrintFlag) {
						output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
						output.openEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
						output.openItemWithDesc("Error:");
						output.closeItemWithDesc("Failed to process the condition");
						output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
						output.closeSubSection();
					}
					return OGPConstants.ERR_CODE_GENERAL;
				}
			}
		
			if (outputPrintFlag) {
				output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
				output.closeSubSection();
			}
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
		sb.append("Intersection point ");
		sb.append(this.geoObjectLabel);
		sb.append(" of point sets ");
		sb.append(((GeoConstruction)this.firstPointSet).getGeoObjectLabel());
		sb.append(" and ");
		sb.append(((GeoConstruction)this.secondPointSet).getGeoObjectLabel());
		return sb.toString();
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getInputLabels()
	 */
	@Override
	public String[] getInputLabels() {
		String[] inputLabels = new String[2];
		inputLabels[0] = ((GeoConstruction)this.firstPointSet).getGeoObjectLabel();
		inputLabels[1] = ((GeoConstruction)this.secondPointSet).getGeoObjectLabel();
		return inputLabels;
	}

	@Override
	public Point replace(HashMap<Point, Point> replacementMap) {
		if (this instanceof AMIntersectionPoint)
			return ((AMIntersectionPoint)this).replace(replacementMap);
		OpenGeoProver.settings.getLogger().error("This method should not be called on this class.");
		return null;
	}
}

