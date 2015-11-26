/* 
 * DISCLAIMER PLACEHOLDER 
 */
package com.ogprover.pp.tp.printing;

import java.util.Vector;

import com.ogprover.pp.tp.ndgcondition.SimpleNDGCondition;
import com.ogprover.pp.tp.thmstatement.AreaMethodTheoremStatement;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for representing the description of an area method proof.</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Damien Desfontaines
 */
public class ProofDescription {

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
	 * Elimination lemmas used in the proof.
	 * See http://hal.inria.fr/hal-00426563/PDF/areaMethodRecapV2.pdf "EL1 - EL13".
	 * Here, the lemmas 5 to 7 only concern expressions of the form S_ABY, while the
	 * lemmas 14 to 16 concern expressions of the form P_ABY.
	 */
	public static boolean isLemma1Used = false,
						  isLemma2Used = false,
						  isLemma3Used = false,
						  isLemma4Used = false,
						  isLemma5Used = false,
						  isLemma6Used = false,
						  isLemma7Used = false,
						  isLemma8Used = false,
						  isLemma9Used = false,
						  isLemma10Used = false,
						  isLemma11Used = false,
						  isLemma12Used = false,
						  isLemma13Used = false,
						  isLemma14Used = false,
						  isLemma15Used = false,
						  isLemma16Used = false;
	
	/**
	 * Steps of the computation
	 */
	protected Vector<ProofStep> steps; 
	
	/**
	 * Statement to prove
	 */
	protected AreaMethodTheoremStatement statement;
	
	/**
	 * NDGs-conditions associated with the theorem
	 */
	protected Vector<SimpleNDGCondition> ndgConditions;

	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	public AreaMethodTheoremStatement getStatement() {
		return statement;
	}

	public void setStatement(AreaMethodTheoremStatement statement) {
		this.statement = statement;
	}

	public Vector<SimpleNDGCondition> getNDGConditions() {
		return ndgConditions;
	}

	public void setNdgConditions(Vector<SimpleNDGCondition> ndgConditions) {
		this.ndgConditions = ndgConditions;
	}

	public Vector<ProofStep> getSteps() {
		return steps;
	}
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	public ProofDescription(Vector<ProofStep> steps, AreaMethodTheoremStatement statement, Vector<SimpleNDGCondition> ndgConditions) {
		this.steps = steps;
		this.statement = statement;
		this.ndgConditions = ndgConditions;
	}
}
