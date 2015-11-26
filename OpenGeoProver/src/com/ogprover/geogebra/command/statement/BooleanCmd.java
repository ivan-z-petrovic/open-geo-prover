/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.geogebra.command.statement;

import java.util.ArrayList;



/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for GeoGebra's command used for true or false statement. 
* </dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class BooleanCmd extends GeoGebraStatementCommand {
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
	public static final String cmdName = GeoGebraStatementCommand.COMMAND_BOOLEAN;
	
	// Command text
	public static final String CMD_TEXT_TRUE = "TRUE";
	public static final String CMD_TEXT_FALSE = "FALSE";
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @see com.ogprover.geogebra.command.GeoGebraCommand#getCommandName()
	 */
	public String getCommandName() {
		return BooleanCmd.cmdName;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method.
	 * 
	 * @param inputArg		Input argument - single text of statement which is "true" or "false".
	 * @param outputArg		Output argument - label of boolean result.
	*/
	public BooleanCmd(String inputArg, String outputArg) {
		this.inputArgs = new ArrayList<String>();
		this.inputArgs.add(inputArg);
		this.outputArg = outputArg;
	}
}