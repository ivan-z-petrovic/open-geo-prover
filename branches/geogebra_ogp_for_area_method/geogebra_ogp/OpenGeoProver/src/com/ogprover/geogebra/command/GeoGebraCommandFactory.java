/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.geogebra.command;

import java.util.ArrayList;

import com.ogprover.geogebra.command.construction.*;
import com.ogprover.geogebra.command.statement.*;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.utilities.logger.ILogger;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for instantiation of GeoGebra command objects.</dd>
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
	 * 						It can be GeoGebraObject.OBJ_TYPE_NONE if command object implicitly sets the type.  
	 * @return				GeoGebra command object or null in case of error.
	 */
	public static GeoGebraCommand createGeoGebraCommand(String ggCmdName, ArrayList<String> inputArgs, ArrayList<String> outputArgs, String objType) {
		/*
		 * Note: This method must match the implementation of GeoGebraConstructionConverter#convertConstruction() method.
		 */
		
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		/*
		 * Construction commands
		 */
		
		// Points
		if (ggCmdName.equals(GeoGebraConstructionCommand.COMMAND_FREE_POINT))
			return new FreePointCmd(outputArgs.get(0));
		if (ggCmdName.equals(GeoGebraConstructionCommand.COMMAND_POINT))
			return new PointCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraConstructionCommand.COMMAND_POINT_IN)) 
			return new PointInCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraConstructionCommand.COMMAND_INTERSECT)) 
			return new IntersectCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraConstructionCommand.COMMAND_MIDPOINT)) 
			return new MidpointCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraConstructionCommand.COMMAND_CENTER)) 
			return new CenterCmd(inputArgs, outputArgs);
		
		// Lines
		if (ggCmdName.equals(GeoGebraConstructionCommand.COMMAND_LINE)) 
			return new LineCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraConstructionCommand.COMMAND_ORT_LINE)) 
			return new OrthogonalLineCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraConstructionCommand.COMMAND_LINE_BISECTOR)) 
			return new LineBisectorCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraConstructionCommand.COMMAND_ANG_BISECTOR)) 
			return new AngularBisectorCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraConstructionCommand.COMMAND_TANGENT)) 
			return new TangentCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraConstructionCommand.COMMAND_POLAR)) 
			return new PolarCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraConstructionCommand.COMMAND_DIAMETER)) 
			return new DiameterCmd(inputArgs, outputArgs);
		
		// Conics
		if (ggCmdName.equals(GeoGebraConstructionCommand.COMMAND_CIRCLE)) 
			return new CircleCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraConstructionCommand.COMMAND_CONIC)) 
			return new ConicCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraConstructionCommand.COMMAND_ELLIPSE)) 
			return new EllipseCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraConstructionCommand.COMMAND_HYPERBOLA)) 
			return new HyperbolaCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraConstructionCommand.COMMAND_PARABOLA)) 
			return new ParabolaCmd(inputArgs, outputArgs);
		
		// Transformations
		if (ggCmdName.equals(GeoGebraConstructionCommand.COMMAND_MIRROR)) 
			return new MirrorCmd(inputArgs, outputArgs, objType);
		if (ggCmdName.equals(GeoGebraConstructionCommand.COMMAND_ROTATE)) 
			return new RotateCmd(inputArgs, outputArgs, objType);
		if (ggCmdName.equals(GeoGebraConstructionCommand.COMMAND_TRANSLATE)) 
			return new TranslateCmd(inputArgs, outputArgs, objType);
		if (ggCmdName.equals(GeoGebraConstructionCommand.COMMAND_DILATE)) 
			return new DilateCmd(inputArgs, outputArgs, objType);
		
		// Partial and auxiliary objects
		if (ggCmdName.equals(GeoGebraConstructionCommand.COMMAND_SEGMENT)) 
			return new SegmentCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraConstructionCommand.COMMAND_POLYGON)) 
			return new PolygonCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraConstructionCommand.COMMAND_POLYLINE)) 
			return new PolyLineCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraConstructionCommand.COMMAND_RAY)) 
			return new RayCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraConstructionCommand.COMMAND_ANGLE)) 
			return new AngleCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraConstructionCommand.COMMAND_VECTOR)) 
			return new VectorCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraConstructionCommand.COMMAND_SEMICIRCLE)) 
			return new SemicircleCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraConstructionCommand.COMMAND_CIRCLE_ARC)) 
			return new CircleArcCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraConstructionCommand.COMMAND_CCIRCLE_ARC)) 
			return new CircumcircleArcCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraConstructionCommand.COMMAND_CIRCLE_SECTOR)) 
			return new CircleSectorCmd(inputArgs, outputArgs);
		if (ggCmdName.equals(GeoGebraConstructionCommand.COMMAND_CCIRCLE_SECTOR)) 
			return new CircumcircleSectorCmd(inputArgs, outputArgs);
		
		/*
		 * Statement commands
		 */
		if (ggCmdName.equals(GeoGebraStatementCommand.COMMAND_BOOLEAN)) 
			return new BooleanCmd(inputArgs.get(0), outputArgs.get(0));
		else if (ggCmdName.equals(GeoGebraStatementCommand.COMMAND_COLLINEAR)) 
			return new CollinearCmd(inputArgs, outputArgs.get(0));
		if (ggCmdName.equals(GeoGebraStatementCommand.COMMAND_CONCYCLIC)) 
			return new ConcyclicCmd(inputArgs, outputArgs.get(0));
		if (ggCmdName.equals(GeoGebraStatementCommand.COMMAND_CONCURRENT)) 
			return new ConcurrentCmd(inputArgs, outputArgs.get(0));
		if (ggCmdName.equals(GeoGebraStatementCommand.COMMAND_PARALLEL)) 
			return new ParallelCmd(inputArgs, outputArgs.get(0));
		if (ggCmdName.equals(GeoGebraStatementCommand.COMMAND_PERPENDICULAR)) 
			return new PerpendicularCmd(inputArgs, outputArgs.get(0));
		if (ggCmdName.equals(GeoGebraStatementCommand.COMMAND_EQUAL)) 
			return new EqualCmd(inputArgs, outputArgs.get(0));
		
		/*
		 * Prove command
		 */
		if (ggCmdName.equals(GeoGebraCommand.COMMAND_PROVE)) 
			return new ProveCmd(inputArgs.get(0), outputArgs.get(0));
		
		// TODO - add other commands if necessary
		
		logger.error("Unknown GeoGebra command");
		return null;
	}
}
