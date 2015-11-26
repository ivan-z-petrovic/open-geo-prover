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
public class ReduceToSingleFractionStep extends ProofStep {
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

	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * @param previousExpression
	 * @param nextExpression
	 */
	public ReduceToSingleFractionStep(AMExpression previousExpression, AMExpression nextExpression) {
		super(previousExpression, nextExpression);
	}
}