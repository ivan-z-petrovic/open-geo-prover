/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.polynomials;

import com.ogprover.main.OpenGeoProver;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for powers of variables</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 *
 */
public class Power implements Comparable<Power>, Cloneable, RationalAlgebraicExpression {
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
	 * Variable of this power
	 */
	protected Variable variable;
	/**
	 * Exponent of power (e.g. (x_2)^7 has exponent 7; (u_5)^3 has exponent 3 etc.);
	 * this value theoretically can be any integer, but only objects with positive 
	 * exponents will be kept in memory
	 */
	protected int exponent;
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * Method that sets the variable to power
	 * 
	 * @param variable The variable to set
	 */
	public void setVariable(Variable variable) {
		this.variable = variable;
	}
	/**
	 * Method that retrieves power's variable
	 * 
	 * @return The variable
	 */
	public Variable getVariable() {
		return variable;
	}
	/**
	 * Method that sets exponent
	 * 
	 * @param exponent	Exponent to set
	 */
	public void setExponent(int exponent) {
		this.exponent = exponent;
	}
	/**
	 * Method that gets exponent
	 * 
	 * @return	Exponent
	 */
	public int getExponent() {
		return exponent;
	}
	/**
	 * Method that retrieves the index of variable from power
	 * 
	 * @return	The variable's index
	 */
	public long getIndex() {
		return this.variable.getIndex();
	}
	/**
	 * Method that retrieves the type of variable from power
	 * 
	 * @return	The variable's type
	 */
	public short getVarType() {
		return this.variable.getVariableType();
	}
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * 
	 * @param var		Variable of power
	 * @param exponent	Exponent of power
	 */
	public Power(Variable var, int exponent) {
		this.variable = var;
		this.exponent = exponent;
	}
	
	/** 
	 * Constructor method
	 * 
	 * @param varType	Type of variable from power
	 * @param index		Index of variable from power
	 * @param exponent	Exponent of power
	 */
	public Power(short varType, long index, int exponent) {
		if (varType == Variable.VAR_TYPE_UX_U || varType == Variable.VAR_TYPE_UX_X)
			this.variable = new UXVariable(varType, index);
		else if (varType == Variable.VAR_TYPE_SYMB_X || varType == Variable.VAR_TYPE_SYMB_Y)
			this.variable = new SymbolicVariable(varType, index);
		this.exponent = exponent;
	}
	
	
	/*
	 * ======================================================================
	 * ======================= COMMON OBJECT METHODS ========================
	 * ======================================================================
	 */
	/**
	 * Clone method
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Power clone(){
		Power p = new Power(this.variable.clone(), this.exponent);
		
		return p;
	}
	
	/**
	 * Method equals
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (!(obj instanceof Power))
			return false;
		Power p = (Power)obj;
		return (this.variable.equals(p.getVariable()) &&
				this.exponent == p.getExponent());
	}
	
	/**
	 * Method toString()
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		short varType = this.getVarType();
		
		if (varType == Variable.VAR_TYPE_SYMB_X ||
			varType == Variable.VAR_TYPE_SYMB_Y) {
			SymbolicVariable sv = (SymbolicVariable)this.variable;
			return "[Power object: variable type = " + varType +
			       " label = " + sv.getPointLabel() + 
			       " exponent = " + this.exponent + "]";
		}
		
		return "[Power object: variable type = " + varType +
			   " index = " + this.getIndex() + 
			   " exponent = " + this.exponent + "]";
	}
	
	/**
	 * CompareTo method
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Power p) {
		int result = 0;
		
		if (p == null) {
			OpenGeoProver.settings.getLogger().error("Null power passed in.");
			return -2; // error
		}
		
		// Only powers of same variable type can be compared
		// e.g. (x_1)^a < (x_3)^b and (x_1)^2 < (x_1)^4
		if (this.getVarType() == p.getVarType()){
			// e.g. x_1 < x_3
			if (this.getIndex() < p.getIndex())
				result = -1;
			// e.g. x_3 > x_1
			else if (this.getIndex() > p.getIndex())
				result = 1;
			// e.g. (x_1)^2 < (x_1)^4
			else if (this.exponent < p.getExponent())
				result = -1;
			// e.g. (x_1)^4 > (x_1)^2
			else if (this.exponent > p.getExponent())
				result = 1;
			// e.g. x_1 == x_1
			else
				result = 0;
		}
		else { // default comparison result (e.g. (u_5)^4 < x_1
			result = this.getVarType() - p.getVarType();
		}
		return result;
	}
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Multiplication with another power
	 * if two powers are of same variable (same type and index)
	 * resulting power will have total exponent equals to the sum 
	 * of exponents of two multipliers
	 * 
	 * @param p		The Power multiplier
	 * @return		Current power object after multiplication	
	 */
	public Power mul(Power p){
		if (p == null) {
			OpenGeoProver.settings.getLogger().error("Null pointer passed in.");
			return null;
		}
		
		if (this.getVarType() == p.getVarType() && this.getIndex() == p.getIndex()) {
			this.exponent += p.getExponent();
		}
		
		return this;
	}
	
	/**
	 * Increments exponent of power;
	 * value of argument can be any integer, but
	 * special care is taken in caller functions
	 * when resulting exponent is not positive
	 * 
	 * @param exp	Exponent increment
	 */
	public void addToExponent(int exp) {
		this.exponent += exp;
	}
	
	/**
	 * @see com.ogprover.polynomials.RationalAlgebraicExpression#printToLaTeX()
	 */
	public String printToLaTeX() {
		StringBuilder sb = new StringBuilder();
		
		// Power(varType, index, exponent) will be printed in LaTex format as:
		// varType_{index}^{exponent}
		// (i.e. superscript of subscript)
		sb.append(this.variable.printToLaTeX());
		
		if (this.exponent > 1) {
			sb.append("^{");
			sb.append(this.exponent+"");
			sb.append("}");
		}
		
		return sb.toString();
	}
	
	/**
	 * @see com.ogprover.polynomials.RationalAlgebraicExpression#printToXML()
	 */
	public String printToXML() {
		StringBuilder sb = new StringBuilder();
		
		/* Power(varType, index, exponent) will be printed in XML format as:
		 * 
		 * <proof_pow>
		 * 		<proof_var>
		 * 			<proof_varType>varType</proof_varType>
		 * 			<proof_index>index</proof_index>
		 * 		</proof_var>
		 * 		<proof_exp>exponent</proof_exp>
		 * </proof_pow>
		 * 
		 * which will be translated in HTML as 
		 * 
		 * varType<sub>index</sub><sup>exponent</sup>
		 * 
		 * (i.e. superscript of subscript)
		 */
		short varType = this.variable.getVariableType();
		String closingTag;
		
		if (varType == Variable.VAR_TYPE_UX_U) {
			sb.append("<proof_upow>");
			closingTag = "</proof_upow>";
		}
		else if (varType == Variable.VAR_TYPE_UX_X) {
			sb.append("<proof_xpow>");
			closingTag = "</proof_xpow>";
		}
		else {
			sb.append("<proof_pow>");
			closingTag = "</proof_pow>";
		}
		sb.append(this.variable.printToXML());
		sb.append("<proof_exp>");
		if (this.exponent > 1)
			sb.append(this.exponent + "");
		sb.append("</proof_exp>");
		sb.append(closingTag);
		
		return sb.toString();
	}
	
	/**
	 * @see com.ogprover.polynomials.RationalAlgebraicExpression#print()
	 */
	public String print() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.variable.print());
		if (this.exponent > 1) {
			sb.append("^");
			sb.append(this.exponent);
		}
		return sb.toString();
	}
}