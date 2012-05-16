/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.api;

import java.util.ArrayList;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.prover_protocol.cp.OGPCP;
import com.ogprover.prover_protocol.cp.geoconstruction.*;
import com.ogprover.prover_protocol.cp.geogebra.*;
import com.ogprover.utilities.io.FileLogger;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for converter of GeoGebra's commands, used for constructions 
*     of geometry objects, to OGP's constructions in format suitable
*     for algebraic provers</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class GGConsConverterForAlgebraicProvers extends GeoGebraConstructionConverter {
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
	/**
	 * Constructor method.
	 * 
	 * @param inputCmdList	Input list with constructions in format of GeoGebra's commands
	 * @param ogpCP			OGP's construction protocol for storage of converted constructions
	 */
	public GGConsConverterForAlgebraicProvers(ArrayList<GeoGebraCommand> inputCmdList, OGPCP ogpCP) {
		super(inputCmdList, ogpCP);
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/*
	 * Methods for conversion of geometry objects
	 */
	
	/*
	 * POINTS
	 */
	
	/**
	 * @see com.ogprover.api.GeoGebraConstructionConverter#convertFreePointCmd(com.ogprover.prover_protocol.cp.geogebra.GeoGebraCommand)
	 */
	protected GeoConstruction convertFreePointCmd(GeoGebraCommand ggCmd) {
		FileLogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.validateCmdArguments(ggCmd, 0, 0, 1, 1) == false) {
			logger.error("Failed to validate command for construction of free point");
			return null;
		}
		
		return new FreePoint(ggCmd.getGeoObjectLabel());
	}
	
	/**
	 * @see com.ogprover.api.GeoGebraConstructionConverter#convertPointCmd(com.ogprover.prover_protocol.cp.geogebra.GeoGebraCommand)
	 */
	protected GeoConstruction convertPointCmd(GeoGebraCommand ggCmd) {
		// TODO
		return null;
	}
	
	/**
	 * @see com.ogprover.api.GeoGebraConstructionConverter#convertPointInCmd(com.ogprover.prover_protocol.cp.geogebra.GeoGebraCommand)
	 */
	protected GeoConstruction convertPointInCmd(GeoGebraCommand ggCmd) {
		// TODO
		return null;
	}
	
	/**
	 * @see com.ogprover.api.GeoGebraConstructionConverter#convertIntersectCmd(com.ogprover.prover_protocol.cp.geogebra.GeoGebraCommand)
	 */
	protected GeoConstruction convertIntersectCmd(GeoGebraCommand ggCmd) {
		// TODO
		return null;
	}
	
	/**
	 * @see com.ogprover.api.GeoGebraConstructionConverter#convertMidpointCmd(com.ogprover.prover_protocol.cp.geogebra.GeoGebraCommand)
	 */
	protected GeoConstruction convertMidpointCmd(GeoGebraCommand ggCmd) {
		// TODO
		return null;
	}
	
	/**
	 * @see com.ogprover.api.GeoGebraConstructionConverter#convertCenterCmd(com.ogprover.prover_protocol.cp.geogebra.GeoGebraCommand)
	 */
	protected GeoConstruction convertCenterCmd(GeoGebraCommand ggCmd) {
		// TODO
		return null;
	}
	
	/*
	 * LINES
	 */
	
	/**
	 * @see com.ogprover.api.GeoGebraConstructionConverter#convertLineCmd(com.ogprover.prover_protocol.cp.geogebra.GeoGebraCommand)
	 */
	protected GeoConstruction convertLineCmd(GeoGebraCommand ggCmd) {
		// TODO
		return null;
	}
	
	/**
	 * @see com.ogprover.api.GeoGebraConstructionConverter#convertOrthogonalLineCmd(com.ogprover.prover_protocol.cp.geogebra.GeoGebraCommand)
	 */
	protected GeoConstruction convertOrthogonalLineCmd(GeoGebraCommand ggCmd) {
		// TODO
		return null;
	}
	
	/**
	 * @see com.ogprover.api.GeoGebraConstructionConverter#convertLineBisectorCmd(com.ogprover.prover_protocol.cp.geogebra.GeoGebraCommand)
	 */
	protected GeoConstruction convertLineBisectorCmd(GeoGebraCommand ggCmd) {
		// TODO
		return null;
	}
	
	/**
	 * @see com.ogprover.api.GeoGebraConstructionConverter#convertAngularBisectorCmd(com.ogprover.prover_protocol.cp.geogebra.GeoGebraCommand)
	 */
	protected GeoConstruction convertAngularBisectorCmd(GeoGebraCommand ggCmd) {
		// TODO
		return null;
	}
	
	/**
	 * @see com.ogprover.api.GeoGebraConstructionConverter#convertTangentCmd(com.ogprover.prover_protocol.cp.geogebra.GeoGebraCommand)
	 */
	protected GeoConstruction convertTangentCmd(GeoGebraCommand ggCmd) {
		// TODO
		return null;
	}
	
	/**
	 * @see com.ogprover.api.GeoGebraConstructionConverter#convertPolarCmd(com.ogprover.prover_protocol.cp.geogebra.GeoGebraCommand)
	 */
	protected GeoConstruction convertPolarCmd(GeoGebraCommand ggCmd) {
		// TODO
		return null;
	}
	
	/**
	 * @see com.ogprover.api.GeoGebraConstructionConverter#convertDiameterCmd(com.ogprover.prover_protocol.cp.geogebra.GeoGebraCommand)
	 */
	protected GeoConstruction convertDiameterCmd(GeoGebraCommand ggCmd) {
		// TODO
		return null;
	}
	
	/*
	 * CONICS
	 */
	
	/**
	 * @see com.ogprover.api.GeoGebraConstructionConverter#convertCircleCmd(com.ogprover.prover_protocol.cp.geogebra.GeoGebraCommand)
	 */
	protected GeoConstruction convertCircleCmd(GeoGebraCommand ggCmd) {
		// TODO
		return null;
	}
	
	/**
	 * @see com.ogprover.api.GeoGebraConstructionConverter#convertConicCmd(com.ogprover.prover_protocol.cp.geogebra.GeoGebraCommand)
	 */
	protected GeoConstruction convertConicCmd(GeoGebraCommand ggCmd) {
		// TODO
		return null;
	}
	
	/**
	 * @see com.ogprover.api.GeoGebraConstructionConverter#convertEllipseCmd(com.ogprover.prover_protocol.cp.geogebra.GeoGebraCommand)
	 */
	protected GeoConstruction convertEllipseCmd(GeoGebraCommand ggCmd) {
		// TODO
		return null;
	}
	
	/**
	 * @see com.ogprover.api.GeoGebraConstructionConverter#convertHyperbolaCmd(com.ogprover.prover_protocol.cp.geogebra.GeoGebraCommand)
	 */
	protected GeoConstruction convertHyperbolaCmd(GeoGebraCommand ggCmd) {
		// TODO
		return null;
	}
	
	/**
	 * @see com.ogprover.api.GeoGebraConstructionConverter#convertParabolaCmd(com.ogprover.prover_protocol.cp.geogebra.GeoGebraCommand)
	 */
	protected GeoConstruction convertParabolaCmd(GeoGebraCommand ggCmd) {
		// TODO
		return null;
	}
	
	/*
	 * TRANSFORMATIONS
	 */
	
	/**
	 * @see com.ogprover.api.GeoGebraConstructionConverter#convertMirrorCmd(com.ogprover.prover_protocol.cp.geogebra.GeoGebraCommand)
	 */
	protected GeoConstruction convertMirrorCmd(GeoGebraCommand ggCmd) {
		// TODO
		return null;
	}
	
	/**
	 * @see com.ogprover.api.GeoGebraConstructionConverter#convertRotateCmd(com.ogprover.prover_protocol.cp.geogebra.GeoGebraCommand)
	 */
	protected GeoConstruction convertRotateCmd(GeoGebraCommand ggCmd) {
		// TODO
		return null;
	}
	
	/**
	 * @see com.ogprover.api.GeoGebraConstructionConverter#convertTranslateCmd(com.ogprover.prover_protocol.cp.geogebra.GeoGebraCommand)
	 */
	protected GeoConstruction convertTranslateCmd(GeoGebraCommand ggCmd) {
		// TODO
		return null;
	}
	
	/**
	 * @see com.ogprover.api.GeoGebraConstructionConverter#convertDilateCmd(com.ogprover.prover_protocol.cp.geogebra.GeoGebraCommand)
	 */
	protected GeoConstruction convertDilateCmd(GeoGebraCommand ggCmd) {
		// TODO
		return null;
	}
	
	/*
	 * PARTIAL AND AUXILIARY OBJECTS
	 */
	
	/**
	 * @see com.ogprover.api.GeoGebraConstructionConverter#convertSegmentCmd(com.ogprover.prover_protocol.cp.geogebra.GeoGebraCommand)
	 */
	protected GeoConstruction convertSegmentCmd(GeoGebraCommand ggCmd) {
		// TODO
		return null;
	}
	
	/**
	 * @see com.ogprover.api.GeoGebraConstructionConverter#convertVectorCmd(com.ogprover.prover_protocol.cp.geogebra.GeoGebraCommand)
	 */
	protected GeoConstruction convertVectorCmd(GeoGebraCommand ggCmd) {
		// TODO
		return null;
	}
	
	/**
	 * @see com.ogprover.api.GeoGebraConstructionConverter#convertRayCmd(com.ogprover.prover_protocol.cp.geogebra.GeoGebraCommand)
	 */
	protected GeoConstruction convertRayCmd(GeoGebraCommand ggCmd) {
		// TODO
		return null;
	}
	
	/**
	 * @see com.ogprover.api.GeoGebraConstructionConverter#convertCircleArcCmd(com.ogprover.prover_protocol.cp.geogebra.GeoGebraCommand)
	 */
	protected GeoConstruction convertCircleArcCmd(GeoGebraCommand ggCmd) {
		// TODO
		return null;
	}
	
	/**
	 * @see com.ogprover.api.GeoGebraConstructionConverter#convertCircumcircleArcCmd(com.ogprover.prover_protocol.cp.geogebra.GeoGebraCommand)
	 */
	protected GeoConstruction convertCircumcircleArcCmd(GeoGebraCommand ggCmd) {
		// TODO
		return null;
	}
	
	/**
	 * @see com.ogprover.api.GeoGebraConstructionConverter#convertCircleSectorCmd(com.ogprover.prover_protocol.cp.geogebra.GeoGebraCommand)
	 */
	protected GeoConstruction convertCircleSectorCmd(GeoGebraCommand ggCmd) {
		// TODO
		return null;
	}
	
	/**
	 * @see com.ogprover.api.GeoGebraConstructionConverter#convertCircumcircleSectorCmd(com.ogprover.prover_protocol.cp.geogebra.GeoGebraCommand)
	 */
	protected GeoConstruction convertCircumcircleSectorCmd(GeoGebraCommand ggCmd) {
		// TODO
		return null;
	}
	
	/**
	 * @see com.ogprover.api.GeoGebraConstructionConverter#convertSemicircleCmd(com.ogprover.prover_protocol.cp.geogebra.GeoGebraCommand)
	 */
	protected GeoConstruction convertSemicircleCmd(GeoGebraCommand ggCmd) {
		// TODO
		return null;
	}
}
