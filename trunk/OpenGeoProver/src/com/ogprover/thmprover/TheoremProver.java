/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.thmprover;


/**
 * <dl>
 * <dt><b>Interface description:</b></dt>
 * <dd>Main interface for theorem provers.</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */
public interface TheoremProver {
	// valid return codes for prover
	/**
	 * theorem's statement is disproved
	 */
	public static final int THEO_PROVE_RET_CODE_FALSE = 0; 
	/**
	 * therem's statement is proved
	 */
	public static final int THEO_PROVE_RET_CODE_TRUE = 1; 
	/**
	 * it is not clear whether theorem is true or false (it cannot be proved)
	 */
	public static final int THEO_PROVE_RET_CODE_UNKNOWN = 2;
	
	// general types of provers
	/**
	 * algebraic (coordinate-based) method
	 */
	public static final int THEO_PROVE_TYPE_ALG = 0; 
	/**
	 * coordinate-free method
	 */
	public static final int THEO_PROVE_TYPE_NONALG = 1;
	
	// specific types of provers
	public static final int TP_TYPE_WU = 0;
	public static final int TP_TYPE_GROEBNER = 1;
	public static final int TP_TYPE_AREA = 2;
	public static final int TP_TYPE_FREE_ANGLE = 3;
	
	// 
	/**
	 * Method for proving geometry theorems;
	 * it returns some of TheoremProver.THEO_PROVE_RET_CODE_xxx codes,
	 * or specific error code (one of GCLCProverConstants.ERR_CODE_xxx values).
	 * 
	 * @return		Success or error code
	 */
	public int prove();
}