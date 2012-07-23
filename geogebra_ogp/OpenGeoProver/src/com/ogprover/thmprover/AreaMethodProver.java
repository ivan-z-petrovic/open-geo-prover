/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.thmprover;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.auxiliary.UnknownStatementException;
import com.ogprover.pp.tp.expressions.*;
import com.ogprover.pp.tp.geoconstruction.AMFootPoint;
import com.ogprover.pp.tp.geoconstruction.AMIntersectionPoint;
import com.ogprover.pp.tp.geoconstruction.FreePoint;
import com.ogprover.pp.tp.geoconstruction.GeoConstruction;
import com.ogprover.pp.tp.geoconstruction.PRatioPoint;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.ogprover.pp.tp.ndgcondition.SimpleNDGCondition;
import com.ogprover.pp.tp.thmstatement.AreaMethodTheoremStatement;
import com.ogprover.pp.tp.thmstatement.IdenticalPoints;
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
	/*
	 * ======================================================================
	 * ========================== VARIABLES =================================
	 * ======================================================================
	 */
	/**
	 * Equals true iff this is the first time that the prover is launched.
	 */
	private static boolean firstLaunch = true;
	
	/**
	 * Known results.
	 */
	private static HashMap<AreaMethodTheoremStatement, Boolean> alreadyProvedStatements;
	
	/**
	 * Replacement map for couples of same points
	 */
	private static HashMap<Point, Point> replacementMap;
	
	/**
	 * Triples of known collinear points.
	 */
	private static HashSet<HashSet<Point>> knownCollinearPoints;
	
	/**
	 * Whether or not we have to print debug messages.
	 */
	public static boolean debugMode = false;
	
	/**
	 * Whether of not we have to compute and eliminate the areas of three collinear points
	 * (this is an optimization - by default, it is set to false).
	 */
	public static boolean optimizeAreaOfCollinearPoints = false;
	
	/**
	 * Whether or not we have to search for couples of points which actually are the same
	 */
	public static boolean optimizeCouplesOfPoints = false;
	
	/**
	 * Whether or not we have to do the "transform to independant variables" step
	 */
	protected boolean transformToIndependantVariables = true;
	
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
	
	
	static {
		alreadyProvedStatements = new HashMap<AreaMethodTheoremStatement, Boolean>();
		replacementMap = new HashMap<Point, Point>();
	}
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	public AreaMethodTheoremStatement getStatement() {
		return statement;
	}
	
	public Vector<GeoConstruction> getConstructions() {
		return constructions;
	}
	
	public Vector<SimpleNDGCondition> getNDGConditions() {
		return ndgConditions;
	}
	
	public void setTransformToIndependantVariables(boolean b) {
		this.transformToIndependantVariables = b;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * @param thmProtocol	The details of the construction, with the type OGPTP.
	 */
	public AreaMethodProver(OGPTP thmProtocol) {
		this.steps = new Vector<AMExpression>();
		this.statement = thmProtocol.getTheoremStatement().getAreaMethodStatement();
		this.constructions = thmProtocol.getConstructionSteps();
		this.nextPointToEliminate = constructions.size()-1;
		this.ndgConditions = thmProtocol.getSimpleNDGConditions();
		computeNextPointToEliminate();
	}
	
	/**
	 * Constructor method
	 * @param statement			The statement to prove
	 * @param constructions		The details of the construction
	 * @param ndgConditions		The NDGs-conditions
	 */
	public AreaMethodProver(AreaMethodTheoremStatement statement, Vector<GeoConstruction> constructions, Vector<SimpleNDGCondition> ndgConditions) {
		this.steps = new Vector<AMExpression>();
		this.statement = statement;
		this.constructions = constructions;
		this.nextPointToEliminate = constructions.size()-1;
		this.ndgConditions = ndgConditions;
		computeNextPointToEliminate();
	}

	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	public int prove() {
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (firstLaunch) {
			firstLaunch = false;
			if (optimizeCouplesOfPoints)
				computeCoupleOfPoints();
			if (optimizeAreaOfCollinearPoints)
				computeCollinearPoints();
		}
		
		if (alreadyProvedStatements.containsKey(statement)) {
			debug("Statement to prove : " + statement.getName());
			if (alreadyProvedStatements.get(statement).booleanValue()) {
				debug("-> We have already proved that it was true");
				return THEO_PROVE_RET_CODE_TRUE;
			}
			debug("-> We have already proved that it was false");
			return THEO_PROVE_RET_CODE_FALSE;
		}
		
		debug("Description of the intern representation of the construction :");
		for (GeoConstruction cons : constructions) {
			debug("  " + cons.getConstructionDesc());
		}
		
		debug("Description of the NDGs conditions :");
		if (ndgConditions != null) {
			for (SimpleNDGCondition ndgCons : ndgConditions) {
				debug("  " + ndgCons.print());
			}
		}
		
		debug("Statement to prove " + statement.getName());
		
		if (statement == null) {
			logger.error("Statement is null");
			return TheoremProver.THEO_PROVE_RET_CODE_UNKNOWN;
		}
		
		debug("Number of expressions in the statement : " + Integer.toString(statement.getStatements().size()));
		
		grosDebug();
		
		for (AMExpression expr : statement.getStatements()) {
			debug("We must prove that : " + expr.print() + " = 0");
			steps.add(expr);
			AMExpression current = expr;
			if (optimizeCouplesOfPoints) {
				debug("After couples of point deletion : ", expr);
				current = current.replace(replacementMap);
			}
			computeNextPointToEliminate();
			while (nextPointToEliminate >=0  && !current.isZero()) {
				debug("Uniformization of : ", current);
				current = current.uniformize(knownCollinearPoints);
				debug("Simplification of : ", current);
				current = current.simplify();
				String label = constructions.get(nextPointToEliminate).getGeoObjectLabel();
				debug("Removing the point " + label + " of the formula : ", current);
				try {
					current = current.eliminate((Point)constructions.get(nextPointToEliminate), this); //safe cast
				} catch (UnknownStatementException e) {
					logger.error("The point elimination required a intermediary lemma to be proved, and the sub-process crashed.");
					logger.error("It occured on : " + e.getMessage());
					return TheoremProver.THEO_PROVE_RET_CODE_UNKNOWN;
				}
				nextPointToEliminate--;
				computeNextPointToEliminate();
				debug("Second uniformization of : ", current);
				current = current.uniformize(knownCollinearPoints);
				debug("Second simplification of : ", current);
				current = current.simplify();
				debug("Reducing into a single fraction of : ", current);
				current = current.reduceToSingleFraction();
				if (current instanceof Fraction) {
					debug("Removing of the denominator of : ", current);
					current = ((Fraction) current).getNumerator();
				}
				debug("Last simplification of : ", current);
				current = current.simplify();
				debug("Reducing into a right associative form of : ", current);
			}
			debug("Reducing into a single fraction of : ", current);
			current = current.reduceToSingleFraction();
			if (current instanceof Fraction) {
				debug("Removing of the denominator of : ", current);
				current = ((Fraction) current).getNumerator();
			}
			debug("Last simplification of : ", current);
			current = current.simplify();
			debug("Transforming into a sum of products of geometrical quantities of : ", current);
			current = current.toSumOfProducts();
			/*
			debug("Reducing into a right associative form of : ", current);
			current = (new Product(new BasicNumber(1), current)).reduceToRightAssociativeForm();
			debug("Grouping of : ", current);
			current = current.groupSumOfProducts();
			*/
			debug("Simplification of : ", current);
			current = current.simplify();
			if (!(current.isZero())) {
				if (!transformToIndependantVariables) {
					debug("The expression is non-null and transformToIndependantVariable is false : aborting.");
					if (current.size() > 20)
						return THEO_PROVE_RET_CODE_UNKNOWN;
					debug("Oh, well, actually, this expression is small, let's try to extend it anyway.");
				}
				debug("Transformation to a formula with only independant variables of : ", current);
				try {
					current = current.toIndependantVariables(this);
				} catch (UnknownStatementException e) {
					logger.error("The transformation to a formula with independant variables" +
							" required a intermediary lemma to be proved, and the sub-process crashed.");
					logger.error("It occured on : " + e.getMessage());
					return TheoremProver.THEO_PROVE_RET_CODE_UNKNOWN;
				}
				debug("Uniformization of : ", current);
				current = current.uniformize(knownCollinearPoints);
				debug("Simplification of : ", current);
				current = current.simplify();
				debug("Reducing into a single fraction of : ", current);
				current = current.reduceToSingleFraction();
				if (current instanceof Fraction) {
					debug("Removing of the denominator of : ", current);
					current = ((Fraction) current).getNumerator();
				}
				debug("Simplification of : ", current);
				current = current.simplify();
				/*
				debug("Reducing into a right associative form of : ", current);
				current = (new Product(new BasicNumber(1), current)).reduceToRightAssociativeForm();
				debug("Grouping of : ", current);
				current = current.groupSumOfProducts();
				*/
				debug("Transforming into a sum of products of geometrical quantities of : ", current);
				current = current.toSumOfProducts();
				debug("Uniformization of : ", current);
				current = current.uniformize(knownCollinearPoints);
				debug("Very last simplification of : ", current);
				current = current.simplify();
				debug("Result : ", current);
				if (current.isZero())
					debug("The formula equals zero : the statement is then proved");
				else
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
	
	public static void debug(String str, AMExpression expr) {
		if (debugMode) {
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
	
	public static void debug(String str)  {
		if (debugMode) {
			ILogger logger = OpenGeoProver.settings.getLogger();
			logger.debug(str);
		}
	}
	
	/**
	 * Fills the knownCollinearPoints set.
	 */
	private void computeCollinearPoints() {
		if (knownCollinearPoints != null)
			return;
		
		knownCollinearPoints = new HashSet<HashSet<Point>>();
		
		int numberOfConstructions = constructions.size();
		for (int i = 0 ; i < numberOfConstructions ; i++) {
			for (int j = 0 ; j < numberOfConstructions ; j++) {
				for (int k = 0 ; k < numberOfConstructions ; k++) {
					if (constructions.get(k) instanceof Point && !(constructions.get(k) instanceof FreePoint) ) {
						Point current = (Point)constructions.get(k);
						if (current instanceof AMIntersectionPoint) {
							Point u = ((AMIntersectionPoint) current).getU();
							Point v = ((AMIntersectionPoint) current).getV();
							addCollinearPoints(u, v, current);
							Point p = ((AMIntersectionPoint) current).getP();
							Point q = ((AMIntersectionPoint) current).getQ();
							addCollinearPoints(p, q, current);
						} else if (current instanceof AMFootPoint) {
							Point u = ((AMFootPoint) current).getU();
							Point v = ((AMFootPoint) current).getV();
							addCollinearPoints(u, v, current);
						} else if (current instanceof PRatioPoint) {
							Point w = ((PRatioPoint) current).getW();
							Point u = ((PRatioPoint) current).getU();
							Point v = ((PRatioPoint) current).getV();
							HashSet<Point> wuv = new HashSet<Point>();
							wuv.add(w); wuv.add(u); wuv.add(v);
							if (w.equals(u) || w.equals(v) || knownCollinearPoints.contains(wuv)) {
								addCollinearPoints(u, v, current);
								addCollinearPoints(u, w, current);
								addCollinearPoints(v, w, current);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Adds a triple of points to the knownCollinearPoints set.
	 * @param a	    The first point of the triple 
	 * @param b	    The second point of the triple 
	 * @param c 	The last point of the triple, which we are currently adding.
	 */
	private void addCollinearPoints(Point a, Point b, Point c) {
		HashSet<Point> set = new HashSet<Point>();
		set.add(a);
		set.add(b);
		set.add(c);
		Vector<HashSet<Point>> toAdd = new Vector<HashSet<Point>>();
		toAdd.add(set);
		for (HashSet<Point> s : knownCollinearPoints) {
			@SuppressWarnings("unchecked") // TODO change this into something less ugly
			HashSet<Point> points = (HashSet<Point>) s.clone();
			if (points.remove(a) && points.remove(b)) { // true iff a and b were in the set
				/*
				 * If (a,b,c) are collinear and (a,b,d) are known to be collinear, we must add
				 * (a,c,d) and (b,c,d) into the set of known-to-be-collinear triples.
				 */
				Point d = (Point)points.toArray()[0];
				HashSet<Point> newSet = new HashSet<Point>();
				newSet.add(a); newSet.add(c); newSet.add(d);
				toAdd.add(newSet);
				newSet = new HashSet<Point>();
				newSet.add(b); newSet.add(c); newSet.add(d);
				toAdd.add(newSet);
			}
		}
		knownCollinearPoints.addAll(toAdd);
	}
	
	/**
	 * For each couple of construction points, verify if the two points are the same.
	 * If so, replaces one of the two points by the other in the expression, and in the
	 *     constructions vector.
	 */
	private void computeCoupleOfPoints() {
		int size = constructions.size();
		for (int i = 0 ; i < size ; i++) {
			for (int j = 0 ; j < i ; j++) {
				GeoConstruction cons1 = constructions.get(i);
				GeoConstruction cons2 = constructions.get(j);
				if (cons1 instanceof Point && !(cons1 instanceof FreePoint)
					&& cons2 instanceof Point && !(cons2 instanceof FreePoint)) {
					Point pt1 = (Point) cons1;
					Point pt2 = (Point) cons2;
					AreaMethodTheoremStatement stat = (new IdenticalPoints(pt1, pt2)).getAreaMethodStatement();
					AreaMethodProver prover = new AreaMethodProver(stat, constructions, ndgConditions);
					prover.setTransformToIndependantVariables(false);
					debug("=========== Calling a prover for a sub theorem ============");
					if (prover.prove() == THEO_PROVE_RET_CODE_TRUE) { // If pt1 and pt2 are geometrically the same point
						debug("---> Couple of geometrically identical points found : " + pt1.getGeoObjectLabel() + " and " + pt2.getGeoObjectLabel() + " !");
						replacementMap.put(pt2, pt1);
						for (int k = j+1 ; k < j ; k++) {
							GeoConstruction pt = constructions.get(k);
							if (pt instanceof Point) {
								Point newPt = ((Point) pt).replace(replacementMap);
								constructions.set(k, newPt);
								replacementMap.put((Point) pt, newPt);
							}
						}
					}
					debug("=========== The sub-theorem prover just returned ============");
				}
			}
		}
	}
	
	private void grosDebug() {
		debug("===============DEBUG=================");
		Point a = new FreePoint("a");
		Point b = new FreePoint("b");
		Point c = new FreePoint("c");
		AMExpression pabc = new PythagorasDifference(a, b, c);
		AMExpression pacb = new PythagorasDifference(a, c, b);
		AMExpression pbac = new PythagorasDifference(b, a, c);
		AMExpression pbca = new PythagorasDifference(b, c, a);
		AMExpression pcab = new PythagorasDifference(c, a, b);
		AMExpression pcba = new PythagorasDifference(c, b, a);
		//AMExpression two = new BasicNumber(2);
		//AMExpression product = new Product(sabc, two);
		
		AMExpression expr = pabc;
		debug("pabc : ", expr);
		AMExpression uniformized = expr.uniformize(knownCollinearPoints);
		debug("uniformized : ", uniformized);
		
		expr = pacb;
		debug("pacb : ", expr);
		uniformized = expr.uniformize(knownCollinearPoints);
		debug("uniformized : ", uniformized);
		
		expr = pbac;
		debug("pbac : ", expr);
		uniformized = expr.uniformize(knownCollinearPoints);
		debug("uniformized : ", uniformized);
		
		expr = pbca;
		debug("pbca : ", expr);
		uniformized = expr.uniformize(knownCollinearPoints);
		debug("uniformized : ", uniformized);
		
		expr = pcab;
		debug("pcab : ", expr);
		uniformized = expr.uniformize(knownCollinearPoints);
		debug("uniformized : ", uniformized);
		
		expr = pcba;
		debug("pcba : ", expr);
		uniformized = expr.uniformize(knownCollinearPoints);
		debug("uniformized : ", uniformized);
		debug("=====================================");
	}
}