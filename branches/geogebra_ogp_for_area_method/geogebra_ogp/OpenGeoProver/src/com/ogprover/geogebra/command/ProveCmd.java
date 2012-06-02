/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.geogebra.command;

import com.ogprover.geogebra.GeoGebraObject;



/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for GeoGebra's command used for statement
*     about three collinear points. 
* </dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class ProveCmd implements GeoGebraCommand {
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
	public static final String cmdName = GeoGebraCommand.COMMAND_PROVE;
	
	/**
	 * Input argument - label of statement result or statement command in string format
	 */
	private String inputArg;
	
	/**
	 * Output argument - label of boolean prove result
	 */
	private String outputArg;
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @param inputArg the inputArg to set
	 */
	public void setInputArg(String inputArg) {
		this.inputArg = inputArg;
	}

	/**
	 * @return the inputArg
	 */
	public String getInputArg() {
		return inputArg;
	}
	
	/**
	 * @param outputArg the outputArg to set
	 */
	public void setOutputArg(String outputArg) {
		this.outputArg = outputArg;
	}

	/**
	 * @return the outputArg
	 */
	public String getOutputArg() {
		return outputArg;
	}
	
	/**
	 * @see com.ogprover.geogebra.command.GeoGebraCommand#getCommandName()
	 */
	public String getCommandName() {
		return ProveCmd.cmdName;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method.
	 * 
	 * @param inputArg		Input argument - label of boolean statement result or statement command string.
	 * @param outputArg		Output argument - label of boolean prove result.
	*/
	public ProveCmd(String inputArg, String outputArg) {
		this.inputArg = inputArg;
		this.outputArg = outputArg;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * @see com.ogprover.geogebra.command.GeoGebraCommand#getDescription()
	 */
	public String getDescription() {
		StringBuilder sb = new StringBuilder();
		sb.append("GGCmd[CmdName:");
		sb.append(this.getCommandName());
		sb.append("]");
		
		return sb.toString();
	}
	
	/**
	 * @see com.ogprover.geogebra.command.GeoGebraCommand#getResultType()
	 */
	public String getResultType() {
		return GeoGebraObject.OBJ_TYPE_BOOL;
	}
}