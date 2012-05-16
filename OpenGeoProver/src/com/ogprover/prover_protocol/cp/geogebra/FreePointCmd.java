/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.prover_protocol.cp.geogebra;

import java.util.ArrayList;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for construction of GeoGebra's free point</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class FreePointCmd extends GeoGebraCommand {
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
	 * <i><b>Name of this command</b></i>
	 */
	public static final String cmdName = GeoGebraCommand.COMMAND_FREE_POINT; // note: this is not the real GeoGebra command but the artificial one
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @see com.ogprover.prover_protocol.cp.geogebra.GeoGebraCommand#getCommandName()
	 */
	@Override
	public String getCommandName() {
		return FreePointCmd.cmdName;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method.
	 * 
	 * @param pointLabel	Label of new free point.
	 */
	public FreePointCmd(String pointLabel) {
		super(null, null, GeoGebraObject.OBJ_TYPE_POINT);
		this.outputArgs = new ArrayList<String>();
		this.outputArgs.add(pointLabel);
	}
}