/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.geogebra;

import java.util.Vector;

import com.ogprover.geogebra.command.ProveCmd;
import com.ogprover.geogebra.command.construction.GeoGebraConstructionCommand;
import com.ogprover.geogebra.command.statement.GeoGebraStatementCommand;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for GeoGebra's format of geometry theorem</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class GeoGebraTheorem {
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
	 * Name of geometry theorem
	 */
	private String theoremName;
	/**
	 * List with constructions of objects that enter theorem statement
	 */
	private Vector<GeoGebraConstructionCommand> constructionList;
	/**
	 * The theorem statement
	 */
	private GeoGebraStatementCommand statement;
	/**
	 * The prove command
	 */
	private ProveCmd proveCmd;
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @return the theoremName
	 */
	public String getTheoremName() {
		return this.theoremName;
	}
	/**
	 * @param theoremName the theoremName to set
	 */
	public void setTheoremName(String theoremName) {
		this.theoremName = theoremName;
	}
	/**
	 * @return the constructionList
	 */
	public Vector<GeoGebraConstructionCommand> getConstructionList() {
		return this.constructionList;
	}
	/**
	 * @param constructionList the constructionList to set
	 */
	public void setConstructionList(Vector<GeoGebraConstructionCommand> constructionList) {
		this.constructionList = constructionList;
	}
	/**
	 * @return the statement
	 */
	public GeoGebraStatementCommand getStatement() {
		return this.statement;
	}
	/**
	 * @param statement the statement to set
	 */
	public void setStatement(GeoGebraStatementCommand statement) {
		this.statement = statement;
	}
	/**
	 * @return the proveCmd
	 */
	public ProveCmd getProveCmd() {
		return this.proveCmd;
	}
	/**
	 * @param proveCmd the proveCmd to set
	 */
	public void setProveCmd(ProveCmd proveCmd) {
		this.proveCmd = proveCmd;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method.
	 */
	public GeoGebraTheorem() {
		this.theoremName = null;
		this.constructionList = new Vector<GeoGebraConstructionCommand>();
		this.statement = null;
		this.proveCmd = null;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
}
