/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.thmprover;

import java.util.Vector;

import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.auxiliary.AMExpression;
import com.ogprover.pp.tp.auxiliary.AMFraction;
import com.ogprover.pp.tp.auxiliary.AMNumber;
import com.ogprover.pp.tp.auxiliary.AMProduct;
import com.ogprover.pp.tp.auxiliary.AreaMethodTheoremStatement;
import com.ogprover.pp.tp.geoconstruction.FreePoint;
import com.ogprover.pp.tp.geoconstruction.GeoConstruction;
import com.ogprover.pp.tp.geoconstruction.Point;

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
	 * Steps of the construction
	 */
	protected Vector<GeoConstruction> constructions;
	
	/**
	 * Index of the next point to eliminate
	 */
	protected int nextPointToEliminate;
	
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
		this.constructions = thmProtocol.getConstructionSteps();
		nextPointToEliminate = constructions.size()-1;
		computeNextPointToEliminate();
	}

	public int prove() {
		System.out.println("Number of expressions in the statement : " + Integer.toString(statement.getStatements().size()));
		
		for (AMExpression expr : statement.getStatements()) {
			System.out.println("We must prove that : " + expr.print() + " = 0");
			steps.add(expr);
			AMExpression current = expr;
			computeNextPointToEliminate();
			while (nextPointToEliminate >=0  && !current.isZero()) {
				//if (current.containsOnlyFreePoints()) {
					/*
					 * The current expression, after uniformization and simplification, is non-zero 
					 * but contains only free points : it is maybe false, but it can also be an 
					 * expression such as S_ABC = S_ABD + S_ADC + S_DBC (which is always true). In this
					 * case, we have to transform it with the area coordinates. 
					 */
					// TODO implement this
				//	return TheoremProver.THEO_PROVE_RET_CODE_UNKNOWN;
				//}
				System.out.println("Uniformization of : " + current.print());
				System.out.println("  (size = " + Integer.toString(current.size()) + ")");
				current = current.uniformize();
				System.out.println("Simplification of : " + current.print());
				System.out.println("  (size = " + Integer.toString(current.size()) + ")");
				current = current.simplify();
				System.out.println("Removing of the point " 
									+ constructions.get(nextPointToEliminate).getGeoObjectLabel() 
									+ " of the formula : "
									+ current.print());
				System.out.println("  (size = " + Integer.toString(current.size()) + ")");
				current = current.eliminate((Point)constructions.get(nextPointToEliminate)); //safe cast
				nextPointToEliminate--;
				computeNextPointToEliminate();
				System.out.println("Second uniformization of : " + current.print());
				System.out.println("  (size = " + Integer.toString(current.size()) + ")");
				current = current.uniformize();
				System.out.println("Second simplification of : " + current.print());
				System.out.println("  (size = " + Integer.toString(current.size()) + ")");
				current = current.simplify();
				System.out.println("Reducing into a single fraction of : " + current.print());
				System.out.println("  (size = " + Integer.toString(current.size()) + ")");
				current = current.reduceToSingleFraction();
				if (current instanceof AMFraction) {
					System.out.println("Removing of the denominator of : " + current.print());
					System.out.println("  (size = " + Integer.toString(current.size()) + ")");
					current = ((AMFraction) current).getNumerator();
				}
				System.out.println("Last simplification of : " + current.print());
				System.out.println("  (size = " + Integer.toString(current.size()) + ")");
				current = current.simplify();
				System.out.println("Reducing into a right associative form of : " + current.print());
				System.out.println("  (size = " + Integer.toString(current.size()) + ")");
			}
			System.out.println("Reducing into a single fraction of : " + current.print());
			System.out.println("  (size = " + Integer.toString(current.size()) + ")");
			current = current.reduceToSingleFraction();
			if (current instanceof AMFraction) {
				System.out.println("Removing of the denominator of : " + current.print());
				System.out.println("  (size = " + Integer.toString(current.size()) + ")");
				current = ((AMFraction) current).getNumerator();
			}
			System.out.println("Last simplification of : " + current.print());
			System.out.println("  (size = " + Integer.toString(current.size()) + ")");
			current = current.simplify();
			System.out.println("Reducing into a right associative form of : " + current.print());
			System.out.println("  (size = " + Integer.toString(current.size()) + ")");
			current = (new AMProduct(new AMNumber(1), current)).reductToRightAssociativeForm();
			System.out.println("Grouping of : " + current.print());
			System.out.println("  (size = " + Integer.toString(current.size()) + ")");
			current = current.groupSumOfProducts();
			System.out.println("Simplification of : " + current.print());
			System.out.println("  (size = " + Integer.toString(current.size()) + ")");
			current = current.simplify();
			if (!(current.isZero())) {
				System.out.println("Transformation to a formula with only independant variables of : " + current.print());
				System.out.println("  (size = " + Integer.toString(current.size()) + ")");
				current = current.toIndependantVariables();
				System.out.println("Reducing into a single fraction of : " + current.print());
				System.out.println("  (size = " + Integer.toString(current.size()) + ")");
				current = current.reduceToSingleFraction();
				if (current instanceof AMFraction) {
					System.out.println("Removing of the denominator of : " + current.print());
					System.out.println("  (size = " + Integer.toString(current.size()) + ")");
					current = ((AMFraction) current).getNumerator();
				}
				System.out.println("Simplification of : " + current.print());
				System.out.println("  (size = " + Integer.toString(current.size()) + ")");
				current = current.simplify();
				System.out.println("Reducing into a right associative form of : " + current.print());
				System.out.println("  (size = " + Integer.toString(current.size()) + ")");
				current = (new AMProduct(new AMNumber(1), current)).reductToRightAssociativeForm();
				System.out.println("Grouping of : " + current.print());
				System.out.println("  (size = " + Integer.toString(current.size()) + ")");
				current = current.groupSumOfProducts();
				System.out.println("Simplification of : " + current.print());
				System.out.println("  (size = " + Integer.toString(current.size()) + ")");
				current = current.simplify();
				System.out.println("Result : " + current.print());
				System.out.println("  (size = " + Integer.toString(current.size()) + ")");
				if (!(current.isZero()))
					return TheoremProver.THEO_PROVE_RET_CODE_FALSE;
			}
		}
		
		return TheoremProver.THEO_PROVE_RET_CODE_TRUE;
	}
	
	private void computeNextPointToEliminate() {
		while (nextPointToEliminate >= 0 
				&& (!(constructions.get(nextPointToEliminate) instanceof Point) 
						|| constructions.get(nextPointToEliminate) instanceof FreePoint))
			nextPointToEliminate--;
	}
}
