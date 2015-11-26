/* 
 * DISCLAIMER PLACEHOLDER 
 */
package com.ogprover.pp.tp.expressions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import com.ogprover.pp.tp.auxiliary.UnknownStatementException;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.ogprover.thmprover.AreaMethodProver;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Abstract class for representing the (three) geometric quantities used in the area method.</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Damien Desfontaines
 */
public abstract class GeometricQuantity extends AMExpression {
	/*
	 * ======================================================================
	 * ========================= ABSTRACT METHODS ===========================
	 * ======================================================================
	 */
	@Override
	public abstract String print();

	@Override
	public abstract HashSet<Point> getPoints();

	@Override
	public abstract boolean containsOnlyFreePoints();

	@Override
	public abstract int size();

	@Override
	public abstract AMExpression uniformize(HashSet<HashSet<Point>> knownCollinearPoints);

	@Override
	public abstract AMExpression simplifyInOneStep();

	@Override
	public abstract AMExpression eliminate(Point pt, Vector<Boolean> isLemmaUsed, AreaMethodProver prover)
			throws UnknownStatementException;

	@Override
	public abstract AMExpression reduceToSingleFraction();

	@Override
	public abstract AMExpression toIndependantVariables(AreaMethodProver prover)
			throws UnknownStatementException;

	@Override
	public abstract AMExpression replace(HashMap<Point, Point> replacementMap);

	@Override
	public boolean equals(Object expr) {
		if (!(expr instanceof GeometricQuantity))
			return false;
		GeometricQuantity q = (GeometricQuantity)expr;
		return this.print().equals(q.print());
	}
}
