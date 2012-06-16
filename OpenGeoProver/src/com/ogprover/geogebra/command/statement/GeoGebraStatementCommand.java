/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.geogebra.command.statement;

import java.util.ArrayList;

import com.ogprover.geogebra.GeoGebraObject;
import com.ogprover.geogebra.command.GeoGebraCommand;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Abstract class for GeoGebra's commands used for statements of geometry theorems</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public abstract class GeoGebraStatementCommand implements GeoGebraCommand {
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
	 * Static constants for command names.
	 */
	public static final String COMMAND_BOOLEAN = "boolean"; // note: this is not the name of command from GeoGebra
	public static final String COMMAND_COLLINEAR = "AreCollinear";
	public static final String COMMAND_CONCYCLIC = "AreConcyclic";
	public static final String COMMAND_CONCURRENT = "AreConcurrent";
	public static final String COMMAND_PARALLEL = "AreParallel";
	public static final String COMMAND_PERPENDICULAR = "ArePerpendicular";
	public static final String COMMAND_EQUAL = "AreEqual";
	// TODO - add here other commands ...
	
	/*
	 * Data members
	 */
	/**
	 * List of input arguments which are labels of existing objects. 
	 */
	protected ArrayList<String> inputArgs;
	/**
	 * Label of output boolean result.
	 */
	protected String outputArg;
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @return the inputArgs
	 */
	public ArrayList<String> getInputArgs() {
		return inputArgs;
	}

	/**
	 * @param inputArgs the inputArgs to set
	 */
	public void setInputArgs(ArrayList<String> inputArgs) {
		this.inputArgs = inputArgs;
	}
	
	/**
	 * @return the outputArg
	 */
	public String getOutputArg() {
		return outputArg;
	}

	/**
	 * @param outputArg the outputArg to set
	 */
	public void setOutputArg(String outputArg) {
		this.outputArg = outputArg;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Default (implicit) constructor method.
	 */
	public GeoGebraStatementCommand() {
		this.inputArgs = null;
		this.outputArg = null;
	}
	
	/**
	 * Constructor method.
	 * 
	 * @param inputArgs		List of input arguments - labels of existing objects.
	 * @param outputArg		Output argument - labels of boolean result.
	 */
	public GeoGebraStatementCommand(ArrayList<String> inputArgs, String outputArg) {
		this.inputArgs = inputArgs;
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
