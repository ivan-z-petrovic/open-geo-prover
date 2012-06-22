/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.thmprover;

import java.util.Vector;

import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.auxiliary.AMExpression;
import com.ogprover.pp.tp.auxiliary.AreaMethodTheoremStatement;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for the area method prover</dd>
* </dl>
* 
* @version 1.00
* @author Damien Desfontaines
*/
public class AreaMethodProver implements TheoremProver {
	/**
	 * Statement to be proved
	 */
	protected AreaMethodTheoremStatement statement;
	
	/**
	 * Steps of the computation (we store them for future printing)
	 */
	protected Vector<AMExpression> steps;
	
	
	/**
	 * Constructor method
	 */
	public AreaMethodProver(OGPTP thmProtocol) {
		steps = new Vector<AMExpression>();
		this.statement = thmProtocol.getTheoremStatement().getAreaMethodStatement();
	}

	public int prove() {
		System.out.println("Number of expressions in the statement : " + Integer.toString(statement.getStatements().size()));
		
		for (AMExpression expr : statement.getStatements()) {
			System.out.println("We must prove that : " + expr.print() + " = 0");
			steps.add(expr);
			AMExpression current = expr;
			while (!current.isZero()) {
				if (current.containsOnlyFreePoints()) {
					/*
					 * The current expression, after uniformization and simplification, is non-zero 
					 * but contains only free points : it is maybe false, but it can also be an 
					 * expression such as S_ABC = S_ABD + S_ADC + S_DBC (which is always true). In this
					 * case, we have to transform it with the area coordinates. 
					 */
					// TODO implement this
					return TheoremProver.THEO_PROVE_RET_CODE_UNKNOWN;
				}
				System.out.println("Uniformization of : " + current.print());
				current = current.uniformize();
				System.out.println("Simplification of : " + current.print());
				current = current.simplify();
				System.out.println("We finally got : " + current.print());
			}
		}
		
		return TheoremProver.THEO_PROVE_RET_CODE_TRUE;
	}
}
