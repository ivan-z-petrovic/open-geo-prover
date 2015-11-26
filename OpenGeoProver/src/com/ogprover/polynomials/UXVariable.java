/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.polynomials;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for plain variable of polynomial representation of geometric theorem</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */
public class UXVariable extends Variable {
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
	 * Type of variable (one of VAR_TYPE_UX_xxx values)
	 */
	private short varType;
	
	
	
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
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * 
	 * @param varType	Type of variable
	 * @param index		Index of variable
	 */
	public UXVariable(short varType, long index) {
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
		Variable copyVar = new UXVariable(this.varType, this.index);
		
		return copyVar;
	}

	
}
