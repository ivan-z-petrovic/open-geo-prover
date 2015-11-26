/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.geogebra.command.statement;

import java.util.ArrayList;



/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for GeoGebra's command used for statement
*     about two parallel lines. 
* </dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class ParallelCmd extends GeoGebraStatementCommand {
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
	public static final String cmdName = GeoGebraStatementCommand.COMMAND_PARALLEL;
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @see com.ogprover.geogebra.command.GeoGebraCommand#getCommandName()
	 */
	public String getCommandName() {
		return ParallelCmd.cmdName;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method.
	 * 
	 * @param inputArgs		List of input arguments - labels of existing objects.
	 * @param outputArg		Output argument - labels of boolean result.
	*/
	public ParallelCmd(ArrayList<String> inputArgs, String outputArg) {
		super(inputArgs, outputArg);
	}
}