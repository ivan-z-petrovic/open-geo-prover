/* 
 * DISCLAIMER PLACEHOLDER 
 */
package com.ogprover.pp.tp.printing;

import java.util.Vector;

import com.ogprover.pp.tp.expressions.AMExpression;
import com.ogprover.pp.tp.geoconstruction.Point;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for representing the description of an area method proof.</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Damien Desfontaines
 */
public class EliminationStep extends ProofStep {
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
	 * Eliminated point.
	 */
	private Point eliminatedPoint; 

	/**
	 * Elimination lemmas used in the proof.
	 * See http://hal.inria.fr/hal-00426563/PDF/areaMethodRecapV2.pdf "EL1 - EL13".
	 * Here, the lemmas 5 to 7 only concern expressions of the form S_ABY, while the
	 * lemmas 14 to 16 concern expressions of the form P_ABY.
	 */
	private Vector<Boolean> isLemmaUsed;
	
	
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
	public EliminationStep(AMExpression previousExpression, AMExpression nextExpression, Point eliminatedPoint) {
		super(previousExpression, nextExpression);
		this.eliminatedPoint = eliminatedPoint;
		this.isLemmaUsed = new Vector<Boolean>(17);
	}
}