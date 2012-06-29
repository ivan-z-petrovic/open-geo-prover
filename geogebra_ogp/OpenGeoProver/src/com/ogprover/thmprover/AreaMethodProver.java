/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.thmprover;

import java.util.Vector;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.auxiliary.AMExpression;
import com.ogprover.pp.tp.auxiliary.AMFraction;
import com.ogprover.pp.tp.auxiliary.AMNumber;
import com.ogprover.pp.tp.auxiliary.AMProduct;
import com.ogprover.pp.tp.auxiliary.AreaMethodTheoremStatement;
import com.ogprover.pp.tp.geoconstruction.FreePoint;
import com.ogprover.pp.tp.geoconstruction.GeoConstruction;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.ogprover.pp.tp.ndgcondition.SimpleNDGCondition;
import com.ogprover.utilities.logger.ILogger;

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
	 * NDGs conditions of the theorem
	 */
	protected Vector<SimpleNDGCondition> ndgConditions;
	
	
	/**
	 * Constructor method
	 */
	public AreaMethodProver(OGPTP thmProtocol) {
		this.steps = new Vector<AMExpression>();
		this.statement = thmProtocol.getTheoremStatement().getAreaMethodStatement();
		this.constructions = thmProtocol.getConstructionSteps();
		this.nextPointToEliminate = constructions.size()-1;
		this.ndgConditions = thmProtocol.getSimpleNDGConditions();
		computeNextPointToEliminate();
	}

	public int prove() {
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		logger.debug("Description of the intern representation of the construction :");
		for (GeoConstruction cons : constructions) {
			logger.debug("  " + cons.getConstructionDesc());
		}
		
		logger.debug("Description of the NDGs conditions :");
		for (SimpleNDGCondition ndgCons : ndgConditions) {
			logger.debug("  " + ndgCons.print());
		}
		
		logger.debug("Number of expressions in the statement : " + Integer.toString(statement.getStatements().size()));
		
		for (AMExpression expr : statement.getStatements()) {
			logger.debug("We must prove that : " + expr.print() + " = 0");
			steps.add(expr);
			AMExpression current = expr;
			computeNextPointToEliminate();
			while (nextPointToEliminate >=0  && !current.isZero()) {
				//if (current.containsOnlyFreePoints()) {
					//
					// The current expression, after uniformization and simplification, is non-zero 
					// but contains only free points : it is maybe false, but it can also be an 
					// expression such as S_ABC = S_ABD + S_ADC + S_DBC (which is always true). In this
					// case, we have to transform it with the area coordinates. 
					//
					// TODO implement this
				//	return TheoremProver.THEO_PROVE_RET_CODE_UNKNOWN;
				//}
				debug("Uniformization of : ", current);
				current = current.uniformize();
				debug("Simplification of : ", current);
				current = current.simplify();
				String label = constructions.get(nextPointToEliminate).getGeoObjectLabel();
				debug("Removing of the point " + label + " of the formula : ", current);
				current = current.eliminate((Point)constructions.get(nextPointToEliminate)); //safe cast
				nextPointToEliminate--;
				computeNextPointToEliminate();
				debug("Second uniformization of : ", current);
				current = current.uniformize();
				debug("Second simplification of : ", current);
				current = current.simplify();
				debug("Reducing into a single fraction of : ", current);
				current = current.reduceToSingleFraction();
				if (current instanceof AMFraction) {
					debug("Removing of the denominator of : ", current);
					current = ((AMFraction) current).getNumerator();
				}
				debug("Last simplification of : ", current);
				current = current.simplify();
				debug("Reducing into a right associative form of : ", current);
			}
			debug("Reducing into a single fraction of : ", current);
			current = current.reduceToSingleFraction();
			if (current instanceof AMFraction) {
				debug("Removing of the denominator of : ", current);
				current = ((AMFraction) current).getNumerator();
			}
			debug("Last simplification of : ", current);
			current = current.simplify();
			debug("Reducing into a right associative form of : ", current);
			current = (new AMProduct(new AMNumber(1), current)).reductToRightAssociativeForm();
			debug("Grouping of : ", current);
			current = current.groupSumOfProducts();
			debug("Simplification of : ", current);
			current = current.simplify();
			if (!(current.isZero())) {
				debug("Transformation to a formula with only independant variables of : ", current);
				current = current.toIndependantVariables();
				debug("Uniformization of : ", current);
				current = current.uniformize();
				debug("Simplification of : ", current);
				current = current.simplify();
				debug("Reducing into a single fraction of : ", current);
				current = current.reduceToSingleFraction();
				if (current instanceof AMFraction) {
					debug("Removing of the denominator of : ", current);
					current = ((AMFraction) current).getNumerator();
				}
				debug("Simplification of : ", current);
				current = current.simplify();
				debug("Reducing into a right associative form of : ", current);
				current = (new AMProduct(new AMNumber(1), current)).reductToRightAssociativeForm();
				debug("Grouping of : ", current);
				current = current.groupSumOfProducts();
				debug("Simplification of : ", current);;
				current = current.simplify();
				debug("Result : ", current);
				if (!(current.isZero()))
					return TheoremProver.THEO_PROVE_RET_CODE_FALSE;
				logger.debug("The formula equals zero : the statement is then proved");
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
	
	private static void debug(String str, AMExpression expr) {
		ILogger logger = OpenGeoProver.settings.getLogger();
		int size = expr.size();
		int MAX_SIZE = 200;
		if (size >= MAX_SIZE)
			logger.debug(str + "Too large to be printed");
		else
			logger.debug(str + expr.print());
		logger.debug("  (Size = " + Integer.toString(size) + ")");
	}
}
