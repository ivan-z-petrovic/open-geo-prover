/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.prover_protocol.cp.geogebra;

import java.util.ArrayList;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.utilities.io.FileLogger;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for instantiation of GeoGebra's command objects.</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class GeoGebraCommandFactory {
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
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	// The default constructor will be used.
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * <i>
	 * Method for instantiation of objects for GeoGebra commands.
	 * </i>
	 * 
	 * @param ggCmdName		Name of GeoGebra command
	 * @param inputArgs		List of input arguments - labels of previously constructed objects
	 * @param outputArgs	List of output arguments - labels of new objects
	 * @param objType		The GeoGebra's type of geometry object.
	 * @return
	 */
	public static GeoGebraCommand createGeoGebraCommand(String ggCmdName, ArrayList<String> inputArgs, ArrayList<String> outputArgs, String objType) {
		/*
		 * Note: This method must match the implementation of GeoGebraConstructionConverter#convertConstruction() method.
		 */
		
		FileLogger logger = OpenGeoProver.settings.getLogger();
		
		// Points
		if (ggCmdName.equals(GeoGebraCommand.COMMAND_FREE_POINT))
			return new FreePointCmd(outputArgs.get(0));
		if (ggCmdName.equals(GeoGebraCommand.COMMAND_POINT))
			return new PointCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraCommand.COMMAND_POINT_IN)) 
			return new PointInCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraCommand.COMMAND_INTERSECT)) 
			return new IntersectCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraCommand.COMMAND_MIDPOINT)) 
			return new MidpointCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraCommand.COMMAND_CENTER)) 
			return new CenterCmd(inputArgs, outputArgs);
		
		// Lines
		if (ggCmdName.equals(GeoGebraCommand.COMMAND_LINE)) 
			return new LineCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraCommand.COMMAND_ORT_LINE)) 
			return new OrthogonalLineCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraCommand.COMMAND_LINE_BISECTOR)) 
			return new LineBisectorCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraCommand.COMMAND_ANG_BISECTOR)) 
			return new AngularBisectorCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraCommand.COMMAND_TANGENT)) 
			return new TangentCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraCommand.COMMAND_POLAR)) 
			return new PolarCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraCommand.COMMAND_DIAMETER)) 
			return new DiameterCmd(inputArgs, outputArgs);
		
		// Conics
		if (ggCmdName.equals(GeoGebraCommand.COMMAND_CIRCLE)) 
			return new CircleCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraCommand.COMMAND_CONIC)) 
			return new ConicCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraCommand.COMMAND_ELLIPSE)) 
			return new EllipseCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraCommand.COMMAND_HYPERBOLA)) 
			return new HyperbolaCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraCommand.COMMAND_PARABOLA)) 
			return new ParabolaCmd(inputArgs, outputArgs);
		
		// Transformations
		if (ggCmdName.equals(GeoGebraCommand.COMMAND_MIRROR)) 
			return new MirrorCmd(inputArgs, outputArgs, objType);
		if (ggCmdName.equals(GeoGebraCommand.COMMAND_ROTATE)) 
			return new RotateCmd(inputArgs, outputArgs, objType);
		if (ggCmdName.equals(GeoGebraCommand.COMMAND_TRANSLATE)) 
			return new TranslateCmd(inputArgs, outputArgs, objType);
		if (ggCmdName.equals(GeoGebraCommand.COMMAND_DILATE)) 
			return new DilateCmd(inputArgs, outputArgs, objType);
		
		// Partial and auxiliary objects
		if (ggCmdName.equals(GeoGebraCommand.COMMAND_SEGMENT)) 
			return new SegmentCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraCommand.COMMAND_VECTOR)) 
			return new VectorCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraCommand.COMMAND_RAY)) 
			return new RayCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraCommand.COMMAND_CIRCLE_ARC)) 
			return new CircleArcCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraCommand.COMMAND_CCIRCLE_ARC)) 
			return new CircumcircleArcCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraCommand.COMMAND_CIRCLE_SECTOR)) 
			return new CircleSectorCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraCommand.COMMAND_CCIRCLE_SECTOR)) 
			return new CircumcircleSectorCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraCommand.COMMAND_SEMICIRCLE)) 
			return new SemicircleCmd(inputArgs, outputArgs);
		
		// TODO - add other commands if necessary
		
		logger.error("Unknown GeoGebra command");
		return null;
	}
}
