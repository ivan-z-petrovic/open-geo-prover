/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.api;

import java.util.ArrayList;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.prover_protocol.cp.OGPCP;
import com.ogprover.prover_protocol.cp.geoconstruction.GeoConstruction;
import com.ogprover.prover_protocol.cp.geogebra.GeoGebraCommand;
import com.ogprover.utilities.io.FileLogger;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Abstract class for converter of GeoGebra's commands, used for constructions 
*     of geometry objects, to OGP's constructions</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public abstract class GeoGebraConstructionConverter {
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
	 * Input list of constructions in format of GeoGebra's commands.
	 */
	protected ArrayList<GeoGebraCommand> ggCmdList;
	/**
	 * OGP's Construction Protocol for storage of converted construction.
	 */
	protected OGPCP consProtocol;
	
	
	
	/*
	 * ======================================================================
	 * ========================= ABSTRACT METHODS ===========================
	 * ======================================================================
	 */
	/*
	 * Methods for conversion of geometry objects 
	 */
	/*
	 * POINTS
	 */
	
	/**
	 * Conversion of construction 'FreePointCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	
	protected abstract GeoConstruction convertFreePointCmd(GeoGebraCommand ggCmd);
	/**
	 * Conversion of construction 'PointCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertPointCmd(GeoGebraCommand ggCmd);
	
	/**
	 * Conversion of construction 'PointInCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertPointInCmd(GeoGebraCommand ggCmd);
	
	/**
	 * Conversion of construction 'IntersectCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertIntersectCmd(GeoGebraCommand ggCmd);
	
	/**
	 * Conversion of construction 'MidpointCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertMidpointCmd(GeoGebraCommand ggCmd);
	
	/**
	 * Conversion of construction 'CenterCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertCenterCmd(GeoGebraCommand ggCmd);
	
	/*
	 * LINES
	 */
	
	/**
	 * Conversion of construction 'LineCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertLineCmd(GeoGebraCommand ggCmd);
	
	/**
	 * Conversion of construction 'OrthogonalLineCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertOrthogonalLineCmd(GeoGebraCommand ggCmd);
	
	/**
	 * Conversion of construction 'LineBisectorCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertLineBisectorCmd(GeoGebraCommand ggCmd);
	
	/**
	 * Conversion of construction 'AngularBisectorCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertAngularBisectorCmd(GeoGebraCommand ggCmd);
	
	/**
	 * Conversion of construction 'TangentCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertTangentCmd(GeoGebraCommand ggCmd);
	
	/**
	 * Conversion of construction 'PolarCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertPolarCmd(GeoGebraCommand ggCmd);
	
	/**
	 * Conversion of construction 'DiameterCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertDiameterCmd(GeoGebraCommand ggCmd);
	
	/*
	 * CONICS
	 */
	
	/**
	 * Conversion of construction 'CircleCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertCircleCmd(GeoGebraCommand ggCmd);
	
	/**
	 * Conversion of construction 'ConicCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertConicCmd(GeoGebraCommand ggCmd);
	
	/**
	 * Conversion of construction 'EllipseCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertEllipseCmd(GeoGebraCommand ggCmd);
	
	/**
	 * Conversion of construction 'HyperbolaCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertHyperbolaCmd(GeoGebraCommand ggCmd);
	
	/**
	 * Conversion of construction 'ParabolaCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertParabolaCmd(GeoGebraCommand ggCmd);
	
	/*
	 * TRANSFORMATIONS
	 */
	
	/**
	 * Conversion of construction 'MirrorCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertMirrorCmd(GeoGebraCommand ggCmd);
	
	/**
	 * Conversion of construction 'RotateCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertRotateCmd(GeoGebraCommand ggCmd);
	
	/**
	 * Conversion of construction 'TranslateCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertTranslateCmd(GeoGebraCommand ggCmd);
	
	/**
	 * Conversion of construction 'DilateCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertDilateCmd(GeoGebraCommand ggCmd);
	
	/*
	 * PARTIAL AND AUXILIARY OBJECTS
	 */
	
	/**
	 * Conversion of construction 'SegmentCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertSegmentCmd(GeoGebraCommand ggCmd);
	
	/**
	 * Conversion of construction 'VectorCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertVectorCmd(GeoGebraCommand ggCmd);
	
	/**
	 * Conversion of construction 'RayCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertRayCmd(GeoGebraCommand ggCmd);
	
	/**
	 * Conversion of construction 'CircleArcCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertCircleArcCmd(GeoGebraCommand ggCmd);
	
	/**
	 * Conversion of construction 'CircumcircleArcCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertCircumcircleArcCmd(GeoGebraCommand ggCmd);
	
	/**
	 * Conversion of construction 'CircleSectorCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertCircleSectorCmd(GeoGebraCommand ggCmd);
	
	/**
	 * Conversion of construction 'CircumcircleSectorCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertCircumcircleSectorCmd(GeoGebraCommand ggCmd);
	
	/**
	 * Conversion of construction 'SemicircleCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertSemicircleCmd(GeoGebraCommand ggCmd);
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Default constructor.
	 */
	public GeoGebraConstructionConverter() {
		this.ggCmdList = null;
		this.consProtocol = null;
	}
	
	/**
	 * Constructor method.
	 * 
	 * @param inputCmdList	Input list with constructions in format of GeoGebra's commands
	 * @param ogpCP			OGP's construction protocol for storage of converted constructions
	 */
	public GeoGebraConstructionConverter(ArrayList<GeoGebraCommand> inputCmdList, OGPCP ogpCP) {
		this.ggCmdList = inputCmdList;
		this.consProtocol = ogpCP;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method which converts input list with GeoGebra commands to OGP's 
	 * constructions which are stored in provided storage Construction Protocol.
	 *  
	 * @return	TRUE if conversion was successful, FALSE otherwise.
	 */
	public boolean convert() {
		FileLogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.ggCmdList == null) {
			logger.error("Can't convert constructions - Missing input list with GeoGebra's commands");
			return false;
		}
		
		if (this.consProtocol == null) {
			logger.error("Can't convert constructions - Missing storage for converted constructions");
			return false;
		}
		
		for (GeoGebraCommand ggCmd : this.ggCmdList) {
			GeoConstruction geoCons = this.convertConstruction(ggCmd);
			
			if (geoCons == null) {
				logger.error("Failed to convert GeoGebra command " + ggCmd.getDescription());
				return false;
			}
			
			this.consProtocol.addGeoConstruction(geoCons);
		}
		
		return true;
	}
	
	/**
	 * Method for conversion of single construction from GeoGebra's command
	 * to OGP's format for construction.
	 * 
	 * @return	Construction object if conversion was successful, null otherwise.
	 */
	protected GeoConstruction convertConstruction(GeoGebraCommand ggCmd) {
		/*
		 * Note: This method must match the implementation of GeoGebraCommandFactory#createGeoGebraCommand() method.
		 * 		Also note that conversion logic cannot be moved to GG command classes since single command can't
		 *  convert itself since it can't be validated and transformed without other constructions that were
		 *  generated before that particular single construction.
		 */
		
		FileLogger logger = OpenGeoProver.settings.getLogger();
		
		// Points
		if (ggCmd.getCommandName().equals(GeoGebraCommand.COMMAND_FREE_POINT))
			return convertFreePointCmd(ggCmd);
		if (ggCmd.getCommandName().equals(GeoGebraCommand.COMMAND_POINT))
			return convertPointCmd(ggCmd);
		if (ggCmd.getCommandName().equals(GeoGebraCommand.COMMAND_POINT_IN)) 
			return convertPointInCmd(ggCmd);
		if (ggCmd.getCommandName().equals(GeoGebraCommand.COMMAND_INTERSECT)) 
			return convertIntersectCmd(ggCmd);
		if (ggCmd.getCommandName().equals(GeoGebraCommand.COMMAND_MIDPOINT)) 
			return convertMidpointCmd(ggCmd);
		if (ggCmd.getCommandName().equals(GeoGebraCommand.COMMAND_CENTER)) 
			return convertCenterCmd(ggCmd);
		
		// Lines
		if (ggCmd.getCommandName().equals(GeoGebraCommand.COMMAND_LINE)) 
			return convertLineCmd(ggCmd);
		if (ggCmd.getCommandName().equals(GeoGebraCommand.COMMAND_ORT_LINE)) 
			return convertOrthogonalLineCmd(ggCmd);
		if (ggCmd.getCommandName().equals(GeoGebraCommand.COMMAND_LINE_BISECTOR)) 
			return convertLineBisectorCmd(ggCmd);
		if (ggCmd.getCommandName().equals(GeoGebraCommand.COMMAND_ANG_BISECTOR)) 
			return convertAngularBisectorCmd(ggCmd);
		if (ggCmd.getCommandName().equals(GeoGebraCommand.COMMAND_TANGENT)) 
			return convertTangentCmd(ggCmd);
		if (ggCmd.getCommandName().equals(GeoGebraCommand.COMMAND_POLAR)) 
			return convertPolarCmd(ggCmd);
		if (ggCmd.getCommandName().equals(GeoGebraCommand.COMMAND_DIAMETER)) 
			return convertDiameterCmd(ggCmd);
		
		// Conics
		if (ggCmd.getCommandName().equals(GeoGebraCommand.COMMAND_CIRCLE)) 
			return convertCircleCmd(ggCmd);
		if (ggCmd.getCommandName().equals(GeoGebraCommand.COMMAND_CONIC)) 
			return convertConicCmd(ggCmd);
		if (ggCmd.getCommandName().equals(GeoGebraCommand.COMMAND_ELLIPSE)) 
			return convertEllipseCmd(ggCmd);
		if (ggCmd.getCommandName().equals(GeoGebraCommand.COMMAND_HYPERBOLA)) 
			return convertHyperbolaCmd(ggCmd);
		if (ggCmd.getCommandName().equals(GeoGebraCommand.COMMAND_PARABOLA)) 
			return convertParabolaCmd(ggCmd);
		
		// Transformations
		if (ggCmd.getCommandName().equals(GeoGebraCommand.COMMAND_MIRROR)) 
			return convertMirrorCmd(ggCmd);
		if (ggCmd.getCommandName().equals(GeoGebraCommand.COMMAND_ROTATE)) 
			return convertRotateCmd(ggCmd);
		if (ggCmd.getCommandName().equals(GeoGebraCommand.COMMAND_TRANSLATE)) 
			return convertTranslateCmd(ggCmd);
		if (ggCmd.getCommandName().equals(GeoGebraCommand.COMMAND_DILATE)) 
			return convertDilateCmd(ggCmd);
		
		// Partial and auxiliary objects
		if (ggCmd.getCommandName().equals(GeoGebraCommand.COMMAND_SEGMENT)) 
			return convertSegmentCmd(ggCmd);
		if (ggCmd.getCommandName().equals(GeoGebraCommand.COMMAND_VECTOR)) 
			return convertVectorCmd(ggCmd);
		if (ggCmd.getCommandName().equals(GeoGebraCommand.COMMAND_RAY)) 
			return convertRayCmd(ggCmd);
		if (ggCmd.getCommandName().equals(GeoGebraCommand.COMMAND_CIRCLE_ARC)) 
			return convertCircleArcCmd(ggCmd);
		if (ggCmd.getCommandName().equals(GeoGebraCommand.COMMAND_CCIRCLE_ARC)) 
			return convertCircumcircleArcCmd(ggCmd);
		if (ggCmd.getCommandName().equals(GeoGebraCommand.COMMAND_CIRCLE_SECTOR)) 
			return convertCircleSectorCmd(ggCmd);
		if (ggCmd.getCommandName().equals(GeoGebraCommand.COMMAND_CCIRCLE_SECTOR)) 
			return convertCircumcircleSectorCmd(ggCmd);
		if (ggCmd.getCommandName().equals(GeoGebraCommand.COMMAND_SEMICIRCLE)) 
			return convertSemicircleCmd(ggCmd);
		
		// TODO - add other converters if necessary
		
		logger.error("Unknown GeoGebra command - cannot convert.");
		return null;
	}
	
	/**
	 * Method which performs the basic validation of input and output arguments
	 * of some GeoGebra command that has to be converted to OGP construction.
	 * 
	 * @param ggCmd				The command to be validated
	 * @param minInputSize		Minimal number of input arguments
	 * @param maxInputSize		Maximal number of input arguments
	 * @param minOutputSize		Minimal number of output arguments
	 * @param maxOutputSize		Maximal number of output arguments
	 * @return					TRUE if validation passed, FALSE otherwise.
	 */
	protected boolean validateCmdArguments(GeoGebraCommand ggCmd, int minInputSize, int maxInputSize, int minOutputSize, int maxOutputSize) {
		FileLogger logger = OpenGeoProver.settings.getLogger();
		
		if (ggCmd == null) {
			logger.error("Cannot convert null command");
			return false;
		}
		
		/*
		 * Validation of input argument list
		 */
		ArrayList<String> iArgs = ggCmd.getInputArgs();
		if (iArgs == null) {
			logger.error("List of input arguments is null for command " + ggCmd.getDescription());
			return false;
		}
		int iSize = iArgs.size();
		if (iSize < minInputSize || iSize > maxInputSize) {
			logger.error("List of input arguments is with incorrect number of elements for command " + ggCmd.getDescription());
			return false;
		}
		for (int ii = 0; ii < iSize; ii++) {
			String iLabel = iArgs.get(ii);
			
			if (iLabel == null) {
				logger.error("Some of input arguments is null for command " + ggCmd.getDescription());
				return false;
			}
			
			/*
			 * Format of input label - it can be one of following:
			 * 	1. label of existing geometry object
			 * 	2. number
			 * 	3. GeoGebra command in form: "cmdName[arg1, arg2, ..., argn]"
			 */
			if (iLabel.length() == 0) {
				logger.error("Empty label is not allowed for input argument in command " + ggCmd.getDescription());
				return false;
			}
			if (this.consProtocol.getConstructionMap().get(iLabel) == null &&
				(!iLabel.contains("[") || !iLabel.contains("]"))) {
				try {
					Double.parseDouble(iLabel);
				} catch (NumberFormatException ex) {
					logger.error("The format of input label is incorrect for command " + ggCmd.getDescription());
					return false;
				}
			}
		}
		
		/*
		 * Validation of output argument list
		 */
		ArrayList<String> oArgs = ggCmd.getOutputArgs();
		if (oArgs == null) {
			logger.error("List of output arguments is null for command " + ggCmd.getDescription());
			return false;
		}
		int oSize = oArgs.size();
		if (oSize < minOutputSize || oSize > maxOutputSize) {
			logger.error("List of output arguments is with incorrect number of elements for command " + ggCmd.getDescription());
			return false;
		}
		for (int ii = 0; ii < oSize; ii++) {
			String oLabel = oArgs.get(ii);
			
			if (oLabel == null) {
				logger.error("Some of output arguments is null for command " + ggCmd.getDescription());
				return false;
			}
			
			/*
			 * Format of output label - it can be empty string in some cases
			 * and can be regular label of geometry object which must not exist
			 * in construction protocol among object constructed previously.
			 */
			if (this.consProtocol.getConstructionMap().get(oLabel) != null) { // this condition covers the case of empty label as well
				logger.error("Bad output label (already used in previous construction) for command " + ggCmd.getDescription());
				return false;
			}
		}
		
		return true;
	}
}
