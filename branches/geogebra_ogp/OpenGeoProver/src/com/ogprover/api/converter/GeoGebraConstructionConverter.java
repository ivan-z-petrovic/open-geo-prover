/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.api.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.ogprover.geogebra.command.GeoGebraCommand;
import com.ogprover.geogebra.command.construction.GeoGebraConstructionCommand;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.geoconstruction.*;
import com.ogprover.pp.tp.geoobject.*;
import com.ogprover.utilities.logger.ILogger;

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
	protected Vector<GeoGebraConstructionCommand> ggCmdList;
	/**
	 * OGP's Theorem Protocol for storage of converted construction.
	 */
	protected OGPTP thmProtocol;
	/**
	 * List of constructions to be removed from Construction Protocol after each
	 * step of conversion. 
	 */
	protected Vector<GeoConstruction> constructionsToRemove;
	/**
	 * Map with auxiliary geometry objects (angles, segments, vectors, polygons etc.), used in constructions 
	 * of other geometry objects.
	 */
	protected Map<String, GeoObject> auxiliaryObjectsMap;
	/**
	 * Flag which is used for success or failure of conversion operation.
	 */
	protected boolean bSuccess;
	
	
	
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
	
	protected abstract GeoConstruction convertFreePointCmd(GeoGebraConstructionCommand ggCmd);
	/**
	 * Conversion of construction 'PointCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertPointCmd(GeoGebraConstructionCommand ggCmd);
	
	/**
	 * Conversion of construction 'PointInCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertPointInCmd(GeoGebraConstructionCommand ggCmd);
	
	/**
	 * Conversion of construction 'IntersectCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertIntersectCmd(GeoGebraConstructionCommand ggCmd);
	
	/**
	 * Conversion of construction 'MidpointCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertMidpointCmd(GeoGebraConstructionCommand ggCmd);
	
	/**
	 * Conversion of construction 'CenterCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertCenterCmd(GeoGebraConstructionCommand ggCmd);
	
	/*
	 * LINES
	 */
	
	/**
	 * Conversion of construction 'LineCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertLineCmd(GeoGebraConstructionCommand ggCmd);
	
	/**
	 * Conversion of construction 'OrthogonalLineCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertOrthogonalLineCmd(GeoGebraConstructionCommand ggCmd);
	
	/**
	 * Conversion of construction 'LineBisectorCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertLineBisectorCmd(GeoGebraConstructionCommand ggCmd);
	
	/**
	 * Conversion of construction 'AngularBisectorCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertAngularBisectorCmd(GeoGebraConstructionCommand ggCmd);
	
	/**
	 * Conversion of construction 'TangentCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertTangentCmd(GeoGebraConstructionCommand ggCmd);
	
	/**
	 * Conversion of construction 'PolarCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertPolarCmd(GeoGebraConstructionCommand ggCmd);
	
	/**
	 * Conversion of construction 'DiameterCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertDiameterCmd(GeoGebraConstructionCommand ggCmd);
	
	/*
	 * CONICS
	 */
	
	/**
	 * Conversion of construction 'CircleCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertCircleCmd(GeoGebraConstructionCommand ggCmd);
	
	/**
	 * Conversion of construction 'ConicCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertConicCmd(GeoGebraConstructionCommand ggCmd);
	
	/**
	 * Conversion of construction 'EllipseCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertEllipseCmd(GeoGebraConstructionCommand ggCmd);
	
	/**
	 * Conversion of construction 'HyperbolaCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertHyperbolaCmd(GeoGebraConstructionCommand ggCmd);
	
	/**
	 * Conversion of construction 'ParabolaCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertParabolaCmd(GeoGebraConstructionCommand ggCmd);
	
	/*
	 * TRANSFORMATIONS
	 */
	
	/**
	 * Conversion of construction 'MirrorCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertMirrorCmd(GeoGebraConstructionCommand ggCmd);
	
	/**
	 * Conversion of construction 'RotateCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertRotateCmd(GeoGebraConstructionCommand ggCmd);
	
	/**
	 * Conversion of construction 'TranslateCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertTranslateCmd(GeoGebraConstructionCommand ggCmd);
	
	/**
	 * Conversion of construction 'DilateCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertDilateCmd(GeoGebraConstructionCommand ggCmd);
	
	/*
	 * PARTIAL AND AUXILIARY OBJECTS
	 */
	
	/**
	 * Conversion of construction 'SegmentCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertSegmentCmd(GeoGebraConstructionCommand ggCmd);
	
	/**
	 * Conversion of construction 'PolygonCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertPolygonCmd(GeoGebraConstructionCommand ggCmd);
	
	/**
	 * Conversion of construction 'PolyLineCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertPolyLineCmd(GeoGebraConstructionCommand ggCmd);
	
	/**
	 * Conversion of construction 'RayCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertRayCmd(GeoGebraConstructionCommand ggCmd);
	
	/**
	 * Conversion of construction 'AngleCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertAngleCmd(GeoGebraConstructionCommand ggCmd);
	
	/**
	 * Conversion of construction 'VectorCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertVectorCmd(GeoGebraConstructionCommand ggCmd);
	
	/**
	 * Conversion of construction 'SemicircleCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertSemicircleCmd(GeoGebraConstructionCommand ggCmd);
	
	/**
	 * Conversion of construction 'CircleArcCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertCircleArcCmd(GeoGebraConstructionCommand ggCmd);
	
	/**
	 * Conversion of construction 'CircumcircleArcCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertCircumcircleArcCmd(GeoGebraConstructionCommand ggCmd);
	
	/**
	 * Conversion of construction 'CircleSectorCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertCircleSectorCmd(GeoGebraConstructionCommand ggCmd);
	
	/**
	 * Conversion of construction 'CircumcircleSectorCmd'.
	 * 
	 * @param ggCmd		Construction command in GeoGebra format.
	 * @return			Converted construction in OGP format or null in case of error.
	 */
	protected abstract GeoConstruction convertCircumcircleSectorCmd(GeoGebraConstructionCommand ggCmd);
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @return the ggCmdList
	 */
	public Vector<GeoGebraConstructionCommand> getConstructionCmdList() {
		return ggCmdList;
	}
	/**
	 * @return the thmProtocol
	 */
	public OGPTP getThmProtocol() {
		return this.thmProtocol;
	}
	/**
	 * @return the auxiliaryObjectsMap
	 */
	public Map<String, GeoObject> getAuxiliaryObjectsMap() {
		return this.auxiliaryObjectsMap;
	}
	/**
	 * @return the bSuccess
	 */
	public boolean isbSuccess() {
		return this.bSuccess;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * 
	 * @param ggThmCnv		GeoGebra theorem converter
	 */
	public GeoGebraConstructionConverter(GeoGebraTheoremConverter ggThmCnv) {
		this.ggCmdList = ggThmCnv.getTheorem().getConstructionList();
		this.thmProtocol = ggThmCnv.getThmProtocol();
		this.constructionsToRemove = new Vector<GeoConstruction>();
		this.auxiliaryObjectsMap = new HashMap<String, GeoObject>();
		this.bSuccess = true;
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
		ILogger logger = OpenGeoProver.settings.getLogger();
		this.bSuccess = true;
		
		if (this.ggCmdList == null) {
			logger.error("Can't convert constructions - Missing input list with GeoGebra's commands");
			this.bSuccess = false;
			return false;
		}
		
		if (this.thmProtocol == null) {
			logger.error("Can't convert constructions - Missing storage for converted constructions");
			this.bSuccess = false;
			return false;
		}
		
		for (GeoGebraConstructionCommand ggCmd : this.ggCmdList) {
			this.constructionsToRemove.clear();
			
			GeoConstruction geoCons = this.convertConstruction(ggCmd);
			if (geoCons == null) {
				logger.error("Failed to convert GeoGebra command " + ((ggCmd != null) ? ggCmd.getDescription() : ""));
				this.bSuccess = false;
				return false;
			}
			
			if (this.constructionsToRemove.size() > 0) {
				for (GeoConstruction gc : this.constructionsToRemove)
					this.thmProtocol.removeGeoConstruction(gc);
			}
			if (!(geoCons instanceof IgnoredConstruction))
				this.thmProtocol.addGeoConstruction(geoCons);
		}
		
		return true;
	}
	
	/**
	 * Method for conversion of single construction from GeoGebra's command
	 * to OGP's format for construction.
	 * 
	 * @return	Construction object if conversion was successful, null otherwise.
	 */
	protected GeoConstruction convertConstruction(GeoGebraConstructionCommand ggCmd) {
		/*
		 * Note: This method must match the implementation of GeoGebraCommandFactory#createGeoGebraCommand() method.
		 * 		Also note that conversion logic cannot be moved to GG command classes since single command can't
		 *  convert itself since it can't be validated and transformed without other constructions that were
		 *  generated before that particular single construction.
		 */
		
		ILogger logger = OpenGeoProver.settings.getLogger();
		String consName = ggCmd.getCommandName();
		
		// Points
		if (consName.equals(GeoGebraConstructionCommand.COMMAND_FREE_POINT))
			return convertFreePointCmd(ggCmd);
		if (consName.equals(GeoGebraConstructionCommand.COMMAND_POINT))
			return convertPointCmd(ggCmd);
		if (consName.equals(GeoGebraConstructionCommand.COMMAND_POINT_IN)) 
			return convertPointInCmd(ggCmd);
		if (consName.equals(GeoGebraConstructionCommand.COMMAND_INTERSECT)) 
			return convertIntersectCmd(ggCmd);
		if (consName.equals(GeoGebraConstructionCommand.COMMAND_MIDPOINT)) 
			return convertMidpointCmd(ggCmd);
		if (consName.equals(GeoGebraConstructionCommand.COMMAND_CENTER)) 
			return convertCenterCmd(ggCmd);
		
		// Lines
		if (consName.equals(GeoGebraConstructionCommand.COMMAND_LINE)) 
			return convertLineCmd(ggCmd);
		if (consName.equals(GeoGebraConstructionCommand.COMMAND_ORT_LINE)) 
			return convertOrthogonalLineCmd(ggCmd);
		if (consName.equals(GeoGebraConstructionCommand.COMMAND_LINE_BISECTOR)) 
			return convertLineBisectorCmd(ggCmd);
		if (consName.equals(GeoGebraConstructionCommand.COMMAND_ANG_BISECTOR)) 
			return convertAngularBisectorCmd(ggCmd);
		if (consName.equals(GeoGebraConstructionCommand.COMMAND_TANGENT)) 
			return convertTangentCmd(ggCmd);
		if (consName.equals(GeoGebraConstructionCommand.COMMAND_POLAR)) 
			return convertPolarCmd(ggCmd);
		if (consName.equals(GeoGebraConstructionCommand.COMMAND_DIAMETER)) 
			return convertDiameterCmd(ggCmd);
		
		// Conics
		if (consName.equals(GeoGebraConstructionCommand.COMMAND_CIRCLE)) 
			return convertCircleCmd(ggCmd);
		if (consName.equals(GeoGebraConstructionCommand.COMMAND_CONIC)) 
			return convertConicCmd(ggCmd);
		if (consName.equals(GeoGebraConstructionCommand.COMMAND_ELLIPSE)) 
			return convertEllipseCmd(ggCmd);
		if (consName.equals(GeoGebraConstructionCommand.COMMAND_HYPERBOLA)) 
			return convertHyperbolaCmd(ggCmd);
		if (consName.equals(GeoGebraConstructionCommand.COMMAND_PARABOLA)) 
			return convertParabolaCmd(ggCmd);
		
		// Transformations
		if (consName.equals(GeoGebraConstructionCommand.COMMAND_MIRROR)) 
			return convertMirrorCmd(ggCmd);
		if (consName.equals(GeoGebraConstructionCommand.COMMAND_ROTATE)) 
			return convertRotateCmd(ggCmd);
		if (consName.equals(GeoGebraConstructionCommand.COMMAND_TRANSLATE)) 
			return convertTranslateCmd(ggCmd);
		if (consName.equals(GeoGebraConstructionCommand.COMMAND_DILATE)) 
			return convertDilateCmd(ggCmd);
		
		// Partial and auxiliary objects
		if (consName.equals(GeoGebraConstructionCommand.COMMAND_SEGMENT)) 
			return convertSegmentCmd(ggCmd);
		if (consName.equals(GeoGebraConstructionCommand.COMMAND_VECTOR)) 
			return convertVectorCmd(ggCmd);
		if (consName.equals(GeoGebraConstructionCommand.COMMAND_RAY)) 
			return convertRayCmd(ggCmd);
		if (consName.equals(GeoGebraConstructionCommand.COMMAND_ANGLE)) 
			return convertAngleCmd(ggCmd);
		if (consName.equals(GeoGebraConstructionCommand.COMMAND_POLYGON)) 
			return convertPolygonCmd(ggCmd);
		if (consName.equals(GeoGebraConstructionCommand.COMMAND_POLYLINE)) 
			return convertPolyLineCmd(ggCmd);
		if (consName.equals(GeoGebraConstructionCommand.COMMAND_CIRCLE_ARC)) 
			return convertCircleArcCmd(ggCmd);
		if (consName.equals(GeoGebraConstructionCommand.COMMAND_CCIRCLE_ARC)) 
			return convertCircumcircleArcCmd(ggCmd);
		if (consName.equals(GeoGebraConstructionCommand.COMMAND_CIRCLE_SECTOR)) 
			return convertCircleSectorCmd(ggCmd);
		if (consName.equals(GeoGebraConstructionCommand.COMMAND_CCIRCLE_SECTOR)) 
			return convertCircumcircleSectorCmd(ggCmd);
		if (consName.equals(GeoGebraConstructionCommand.COMMAND_SEMICIRCLE)) 
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
	 * @param maxInputSize		Maximal number of input arguments, if equals -1 that means no upper limit for number of arguments (infinity)
	 * @param minOutputSize		Minimal number of output arguments
	 * @param maxOutputSize		Maximal number of output arguments, if equals -1 that means no upper limit for number of arguments (infinity)
	 * @return					TRUE if validation passed, FALSE otherwise.
	 */
	protected boolean validateCmdArguments(GeoGebraConstructionCommand ggCmd, int minInputSize, int maxInputSize, int minOutputSize, int maxOutputSize) {
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (ggCmd == null) {
			logger.error("Cannot convert null command");
			return false;
		}
		
		/*
		 * Validation of input argument list
		 */
		ArrayList<String> iArgs = ggCmd.getInputArgs();
		if (iArgs == null && minInputSize > 0) {
			logger.error("List of input arguments is null for command " + ggCmd.getDescription());
			return false;
		}
		if (iArgs != null) {
			int iSize = iArgs.size();
			if (iSize < minInputSize || (maxInputSize != -1 && iSize > maxInputSize)) {
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
				if (this.getGeoObject(iLabel) == null &&
						(!iLabel.contains("[") || !iLabel.contains("]"))) {
					try {
						Double.parseDouble(iLabel);
					} catch (NumberFormatException ex) {
						logger.error("The format of input label is incorrect for command " + ggCmd.getDescription());
						return false;
					}
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
		if (oSize < minOutputSize || (maxOutputSize != -1 && oSize > maxOutputSize)) {
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
			 * in theorem protocol among object constructed previously.
			 */
			if (this.getGeoObject(oLabel) != null) { // note: this condition covers the case of empty label as well
				logger.error("Bad output label (already used in previous construction) for command " + ggCmd.getDescription());
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Method which retrieves geometry object by its label.
	 * It searches first among constructions, then among auxiliary objects.
	 * 
	 * @param objLabel	The label of object which is searched for.
	 * @return			The object found by label or null if not found.
	 */
	protected GeoObject getGeoObject(String objLabel) {
		GeoConstruction gc = this.thmProtocol.getConstructionMap().get(objLabel);
		
		if (gc != null)
			return gc;
		return this.auxiliaryObjectsMap.get(objLabel);
	}
	
	/**
	 * Method which returns the list of <b>numOfPts</b> points from point set.
	 * If point set doesn't have necessary number of points, new points are created.
	 * 
	 * @param ptSet			Set of points whose points are retrieved
	 * @param numOfPts		Number of points
	 * @param consList		List of all constructions where only new points are added
	 * @return				List of points from point set, or null in case of error
	 */
	protected Vector<Point> getOrCreatePointsFromPointSet(SetOfPoints ptSet, int numOfPts, Vector<GeoConstruction> consList) {
		if (ptSet == null)
			return null;
		
		Vector<Point> vpts = new Vector<Point>();
		Vector<Point> setPts = ptSet.getPoints();
		int numSetPts = (setPts == null) ? 0 : setPts.size();
		
		for (int ii = 0; ii < numOfPts; ii++) {
			Point pt = (numSetPts > ii) ? setPts.get(ii) : null;
			
			if (pt == null) {
				StringBuilder sb = new StringBuilder();
				sb.append(ptSet.getGeoObjectLabel());
				sb.append("_pt");
				sb.append(ii+1);
				sb.append("_");
				sb.append(Math.round(Math.random()*1000));
				String ptLabel = GeoGebraConstructionConverter.generateRandomLabel(sb.toString());
				pt = RandomPointFromSetOfPoints.createRandomPoint(ptLabel, ptSet);
				if (pt == null)
					return null; // failed to create a point (i.e. unknown set of points passed)
				consList.add(pt);
			}
			
			vpts.add(pt);
		}
		
		return vpts;
	}
	
	/**
	 * <i>
	 * Method which retrieves the random label to be used in some geometry object.
	 * </i>
	 * 
	 * @param prefix	The prefix of new random label
	 * @return			Random label
	 */
	protected static String generateRandomLabel(String prefix) {
		StringBuilder sb = new StringBuilder();
		sb.append(prefix);
		sb.append(Math.round(Math.random()*1000));
		return sb.toString();
	}
	
	/**
	 * <i>
	 * Method which retrieves the message for class cast exception to be used for logging.
	 * </i>
	 * 
	 * @param ggCmd		The command on which an exception has occurred.
	 * @param ex		The exception caught.
	 * @return			Exception message.
	 */
	protected static String getClassCastExceptionMessage(GeoGebraCommand ggCmd, Exception ex) {
		StringBuilder sb = new StringBuilder();
		sb.append("Can't cast input argument(s) for command ");
		sb.append(ggCmd.getCommandName());
		sb.append(" exception caught: ");
		sb.append(ex.toString());
		return sb.toString();
	}
}
