/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.geogebra.command;


/**
* <dl>
* <dt><b>Interface description:</b></dt>
* <dd>Interface for all GeoGebra commands</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public interface GeoGebraCommand {
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
	public static final String VERSION_NUM = "1.00"; // this should match the version number from interface comment
	
	/**
	 * Static constants for command names.
	 */
	public static final String COMMAND_PROVE = "Prove";
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method that retrieves the name of this command.
	 * 
	 * @return	Command name.
	 */
	public String getCommandName();
	/**
	 * Method which retrieves the brief string description of GeoGebra command.
	 * This method should be used when it is necessary to easily identify the 
	 * command object e.g. in log messages.
	 * 
	 * @return	String representing the description of GeoGebra command.
	 */
	public String getDescription();
	/**
	 * Method which gives the result type of this command (one of GeoGebraObject types).
	 * 
	 * @return	The command's result type.
	 */
	public String getResultType();
}
