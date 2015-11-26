/* 
 * DISCLAIMER PLACEHOLDER 
 */
package com.ogprover.pp.tp.printing;

import com.ogprover.pp.tp.expressions.AMExpression;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for representing the description of an area method proof.</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Damien Desfontaines
 */
public abstract class ProofStep {
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
	 * Expression before the computation step
	 */
	protected AMExpression previousExpression;
	
	/**
	 * Expression after the computation step
	 */
	protected AMExpression nextExpression;
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	public AMExpression getPreviousExpression() {
		return previousExpression;
	}
	
	public void setPreviousExpression(AMExpression previousExpression) {
		this.previousExpression = previousExpression;
	}
	
	public AMExpression getNextExpression() {
		return nextExpression;
	}
	
	public void setNextExpression(AMExpression nextExpression) {
		this.nextExpression = nextExpression;
	}

	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	public ProofStep(AMExpression previousExpression, AMExpression nextExpression) {
		this.previousExpression = previousExpression;
		this.nextExpression = nextExpression;
	}
}