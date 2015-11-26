/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.polynomials;

import com.ogprover.main.OpenGeoProver;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Abstract class for variable</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */
public abstract class Variable implements Comparable<Variable>, Cloneable, RationalAlgebraicExpression {
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
	// Class constants for types of variables
	/**
	 * <i><b>Parameter or independent variable type</b></i>
	 */
	public static final short VAR_TYPE_UX_U = 0;
	/**
	 * <i><b>Plain variable (algebraically dependent on u's)</b></i>
	 */
	public static final short VAR_TYPE_UX_X = 1;
	
	// Types of symbolic variables must be 2 and 3 - see comments
	/**
	 * <i><b>Symbolic variable x</b></i>
	 * 
	 * @see com.ogprover.utilities.OGPUtilities#getSymbolicVariableIndex(SymbolicVariable)
	 */
	public static final short VAR_TYPE_SYMB_X = 2;
	/**
	 * <i><b>Symbolic variable y</b></i>
	 * 
	 * @see com.ogprover.utilities.OGPUtilities#getSymbolicVariableIndex(SymbolicVariable)
	 */
	public static final short VAR_TYPE_SYMB_Y = 3;
	
	// Other class data members
	/**
	 * Index of variable (e.g. x_2 has index 2; u_5 has index 5 etc.); 
	 * this value is positive integer number; special case is u_0 which 
	 * has meaning of zero value of u-variable
	 */
	protected long index;
	
	

	/*
	 * ======================================================================
	 * ========================= ABSTRACT METHODS ===========================
	 * ======================================================================
	 */
	/**
	 * Method that retrieves the type of variable
	 * 
	 * @return	Type of this variable
	 */
	public abstract short getVariableType();
	/**
	 * Method clone()
	 * 
	 * @see java.lang.Object#clone()
	 */
	public abstract Variable clone();
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * Method that returns the index of this variable
	 * 
	 * @return The variable's index
	 */
	public long getIndex() {
		return index;
	}

	/**
	 * Method that sets the index to this variable
	 * 
	 * @param index The index to set
	 */
	public void setIndex(long index) {
		this.index = index;
	}
	
	
	
	/*
	 * ======================================================================
	 * ======================= COMMON OBJECT METHODS ========================
	 * ======================================================================
	 */
	/**
	 * Method equals
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (!(obj instanceof Variable))
			return false;
		Variable v = (Variable)obj;
		return (this.getVariableType() == v.getVariableType() &&
				this.index == v.getIndex());
	}
	
	/**
	 * CompareTo method
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Variable v) {
		if (v == null) {
			OpenGeoProver.settings.getLogger().error("Null variable passed in.");
			return -2; // error
		}
		
		// Variables are ordered lexicographically i.e. they are first compared by
		// type then by index
		if (this.getVariableType() == v.getVariableType()) {
			if (this.index == v.getIndex())
				return 0;
			
			if (this.index > v.getIndex())
				return 1;
			
			return -1;
		}
		else if (this.getVariableType() > v.getVariableType())
			return 1;
		
		return -1;
	}
	
	/**
	 * Method toString()
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		short varType = this.getVariableType();
		
		if (varType == Variable.VAR_TYPE_SYMB_X ||
			varType == Variable.VAR_TYPE_SYMB_Y) {
			SymbolicVariable sv = (SymbolicVariable)this;
			return "[Variable object: variable type = " + varType +
			       " label = " + sv.getPointLabel() + "]";
		}
		
		return "[Variable object: variable type = " + varType +
			   " index = " + this.getIndex() + "]";
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	// Necessary for using these objects as keys of HashMaps
	public int hashCode() {
		return this.toString().hashCode();
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * @see com.ogprover.polynomials.RationalAlgebraicExpression#printToLaTeX()
	 */
	public String printToLaTeX() {
		StringBuilder sb = new StringBuilder();
		
		// Variable(varType, index) will be printed in LaTex format as:
		// varType_{index}
		// (i.e. as subscript)
		short varType = this.getVariableType();
		
		if (varType == Variable.VAR_TYPE_UX_U || varType == Variable.VAR_TYPE_UX_X) {
			if (varType == Variable.VAR_TYPE_UX_U && this.getIndex() == 0) { // zero
				sb.append("0");
				return sb.toString();
			}
			
			sb.append((varType == Variable.VAR_TYPE_UX_U) ? "u" : "x");
			sb.append("_{");
			sb.append(this.getIndex() + "");
			sb.append("}");
		}
		else if (varType == Variable.VAR_TYPE_SYMB_X || varType == Variable.VAR_TYPE_SYMB_Y) {
			SymbolicVariable sv = (SymbolicVariable)this;
			sb.append((varType == Variable.VAR_TYPE_SYMB_X) ? "x" : "y");
			sb.append("_{");
			sb.append(sv.getPointLabel() + "");
			sb.append("}");
		}
		
		return sb.toString();
	}
	
	/**
	 * @see com.ogprover.polynomials.RationalAlgebraicExpression#printToXML()
	 */
	public String printToXML() {
		StringBuilder sb = new StringBuilder();
		
		/* Variable(varType, index) will be printed in XML format as:
		 * 
		 * <proof_var>
		 * 		<proof_varType>varType</proof_varType>
		 * 		<proof_index>index</proof_index>
		 * </proof_var>
		 * 
		 * which will be translated in HTML as 
		 * 
		 * varType<sub>index</sub>
		 * 
		 * (i.e. as subscript).
		 */
		short varType = this.getVariableType();
		
		sb.append("<proof_var>");
		if (varType == Variable.VAR_TYPE_UX_U || varType == Variable.VAR_TYPE_UX_X) {
			if (varType == Variable.VAR_TYPE_UX_U && this.getIndex() == 0) { // zero
				sb.append("<proof_varType>");
				sb.append("0");
				sb.append("</proof_varType>");
				sb.append("<proof_index>");
				sb.append("</proof_index>");
				sb.append("</proof_var>");
				return sb.toString();
			}
			
			sb.append("<proof_varType>");
			sb.append((varType == Variable.VAR_TYPE_UX_U) ? "u" : "x");
			sb.append("</proof_varType>");
			sb.append("<proof_index>");
			sb.append(this.getIndex() + "");
			sb.append("</proof_index>");
		}
		else if (varType == Variable.VAR_TYPE_SYMB_X || varType == Variable.VAR_TYPE_SYMB_Y) {
			SymbolicVariable sv = (SymbolicVariable)this;
			sb.append("<proof_varType>");
			sb.append((varType == Variable.VAR_TYPE_SYMB_X) ? "x" : "y");
			sb.append("</proof_varType>");
			sb.append("<proof_index>");
			sb.append(sv.getPointLabel() + "");
			sb.append("</proof_index>");
		}
		sb.append("</proof_var>");
		
		return sb.toString();
	}
	
	/**
	 * @see com.ogprover.polynomials.RationalAlgebraicExpression#print()
	 */
	public String print() {
		short varType = this.getVariableType();
		StringBuilder sb = new StringBuilder();
		if (varType == Variable.VAR_TYPE_UX_U || varType == Variable.VAR_TYPE_UX_X) {
			sb.append((varType == Variable.VAR_TYPE_UX_U) ? "u" : "x");
			sb.append(this.getIndex());
		}
		else if (varType == Variable.VAR_TYPE_SYMB_X || varType == Variable.VAR_TYPE_SYMB_Y) {
			sb.append((varType == Variable.VAR_TYPE_SYMB_X) ? "x" : "y");
			sb.append(((SymbolicVariable)this).getPointLabel());
		}
		return sb.toString();
	}
}
