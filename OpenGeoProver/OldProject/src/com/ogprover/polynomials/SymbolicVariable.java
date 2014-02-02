/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.polynomials;

import com.ogprover.utilities.OGPUtilities;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for symbolic variable</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */
public class SymbolicVariable extends Variable {
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
	 * Type of variable (one of VAR_TYPE_SYMB_xxx values)
	 */
	private short varType;
	/**
	 * Label of point whose this coordinate/variable is; acts as index
	 */
	private String pointLabel = "";
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * Method that sets variable type
	 * 
	 * @param varType The variable type to set
	 */
	public void setVariableType(short varType) {
		this.varType = varType;
	}
	
	/**
	 * Method that retrieves the type of this variable
	 * 
	 * @see com.ogprover.polynomials.Variable#getVariableType()
	 */
	@Override
	public short getVariableType() {
		return varType;
	}
	
	/**
	 * Method that sets point label
	 * 
	 * @param pointLabel The point label to set
	 */
	public void setPointLabel(String pointLabel) {
		this.pointLabel = pointLabel;
		this.index = OGPUtilities.getSymbolicVariableIndex(this);
	}

	/**
	 * Method that retrieves the point label of this symbolic variable
	 * 
	 * @return The point label
	 */
	public String getPointLabel() {
		return pointLabel;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * 
	 * @param varType	Type of symbolic variable
	 * @param label		Point label
	 */
	public SymbolicVariable(short varType, String label) {
		this.varType = varType;
		this.pointLabel = label;
		this.index = OGPUtilities.getSymbolicVariableIndex(this);
	}
	
	/**
	 * Constructor method
	 * 
	 * @param varType	Type of symbolic variable
	 * @param index		Index of variable
	 */
	public SymbolicVariable(short varType, long index) {
		this.varType = varType;
		this.index = index;
	}
	
	
	/*
	 * ======================================================================
	 * ======================= COMMON OBJECT METHODS ========================
	 * ======================================================================
	 */
	/**
	 * Clone method
	 * 
	 * @see com.ogprover.polynomials.Variable#clone()
	 */
	@Override
	public Variable clone() {
		Variable copyVar = new SymbolicVariable(this.varType, this.index);
		((SymbolicVariable)copyVar).setPointLabel(this.pointLabel);
		
		return copyVar;
	}

	
}
