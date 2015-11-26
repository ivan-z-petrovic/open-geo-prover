/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.thmprover;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.auxiliary.FloatCoordinates;
import com.ogprover.pp.tp.auxiliary.UnknownStatementException;
import com.ogprover.pp.tp.expressions.*;
import com.ogprover.pp.tp.geoconstruction.AMFootPoint;
import com.ogprover.pp.tp.geoconstruction.AMIntersectionPoint;
import com.ogprover.pp.tp.geoconstruction.FreePoint;
import com.ogprover.pp.tp.geoconstruction.GeoConstruction;
import com.ogprover.pp.tp.geoconstruction.PRatioPoint;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.ogprover.pp.tp.geoconstruction.TRatioPoint;
import com.ogprover.pp.tp.ndgcondition.SimpleNDGCondition;
import com.ogprover.pp.tp.printing.EliminationStep;
import com.ogprover.pp.tp.printing.ProofDescription;
import com.ogprover.pp.tp.printing.ProofStep;
import com.ogprover.pp.tp.printing.ReduceToSingleFractionStep;
import com.ogprover.pp.tp.printing.SimplificationStep;
import com.ogprover.pp.tp.printing.ToIndependantVariablesStep;
import com.ogprover.pp.tp.printing.UniformizationStep;
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
	public static boolean debugMode = true;
	
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
	 * Arbitrary coordinates of each point, for fast verification and debugging
	 */
	protected static HashMap<String, FloatCoordinates> coords;
	
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
	 * Description of proof
	 */
	protected ProofDescription description;
	
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
	
	public static void setDebugMode(boolean debugMode) {
		AreaMethodProver.debugMode = debugMode;
	}

	public static void setOptimizeCouplesOfPoints(boolean optimizeCouplesOfPoints) {
		AreaMethodProver.optimizeCouplesOfPoints = optimizeCouplesOfPoints;
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
		Vector<ProofStep> steps = new Vector<ProofStep>();
		
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
		
		if (debugMode) {
			debug("Generation of random coordinates for all points...");
			initCoords();
			for (GeoConstruction cons : constructions) {
				if (cons instanceof Point) {
					String label = cons.getGeoObjectLabel();
					debug("Point " + label + " : (" + coords.get(label).x + ", " + coords.get(label).y + ")");
				}
			}
		}
		
		debug("Statement to prove " + statement.getName());
		
		if (statement == null) {
			logger.error("Statement is null");
			return TheoremProver.THEO_PROVE_RET_CODE_UNKNOWN;
		}
		
		debug("Number of expressions in the statement : " + Integer.toString(statement.getStatements().size()));
		
		for (AMExpression expr : statement.getStatements()) {
			debug("We must prove that : " + expr.print() + " = 0");
			AMExpression current = expr;
			AMExpression next = null;
			if (optimizeCouplesOfPoints) {
				debug("After couples of point deletion : ", expr);
				current = current.replace(replacementMap);
			}
			computeNextPointToEliminate();
			while (nextPointToEliminate >=0  && !current.isZero()) {
				
				next = current.uniformize(knownCollinearPoints);
				steps.add(new UniformizationStep(current, next));
				current = next;
				
				next = current.simplify();
				steps.add(new SimplificationStep(current, next));
				current = next;
				
				Vector<Boolean> isLemmaUsed = new Vector<Boolean>(17);
				for (int i = 0 ; i < 17 ; i++)
					isLemmaUsed.set(i, false);
				try {
					next = current.eliminate((Point)constructions.get(nextPointToEliminate), isLemmaUsed, this); //safe cast
				} catch (UnknownStatementException e) {
					logger.error("The point elimination required a intermediary lemma to be proved, and the sub-process crashed.");
					logger.error("It occured on : " + e.getMessage());
					return TheoremProver.THEO_PROVE_RET_CODE_UNKNOWN;
				}
				steps.add(new EliminationStep(current, next, (Point)constructions.get(nextPointToEliminate)));
				nextPointToEliminate--;
				computeNextPointToEliminate();
				current = next;
				
				next = current.uniformize(knownCollinearPoints);
				next = next.simplify();
				steps.add(new SimplificationStep(current, next));
				current = next;
				
				next = current.reduceToSingleFraction();
				if (next instanceof Fraction)
					next = ((Fraction) next).getNumerator();
				steps.add(new ReduceToSingleFractionStep(current, next));
				current = next;
				
				next = current.simplify();
				steps.add(new SimplificationStep(current, next));
				current = next;
				
				next = current.toSumOfProducts();
			}
			if (next == null)
				next = current;
			next = next.reduceToSingleFraction();
			if (next instanceof Fraction)
				next = ((Fraction) next).getNumerator();
			steps.add(new ReduceToSingleFractionStep(current, next));
			current = next;
			
			next = current.simplify();
			next = next.toSumOfProducts();
			next = next.simplify();
			steps.add(new SimplificationStep(current, next));
			current = next;
			
			if (!(current.isZero())) {
				if (!transformToIndependantVariables) {
					debug("The expression is non-null and transformToIndependantVariable is set to false : aborting.");
					return THEO_PROVE_RET_CODE_UNKNOWN;
				}
				
				try {
					next = current.toIndependantVariables(this);
				} catch (UnknownStatementException e) {
					logger.error("The transformation to a formula with independant variables" +
							" required a intermediary lemma to be proved, and the sub-process crashed.");
					logger.error("It occured on : " + e.getMessage());
					return TheoremProver.THEO_PROVE_RET_CODE_UNKNOWN;
				}
				steps.add(new ToIndependantVariablesStep(current, next));
				
				next = current.uniformize(knownCollinearPoints);
				steps.add(new UniformizationStep(current, next));
				current = next;
				
				next = current.simplify();
				steps.add(new SimplificationStep(current, next));
				current = next;
				
				next = current.reduceToSingleFraction();
				if (next instanceof Fraction)
					next = ((Fraction) next).getNumerator();
				steps.add(new ReduceToSingleFractionStep(current, next));
				current = next;
				
				next = current.simplify();
				current = current.toSumOfProducts();
				steps.add(new SimplificationStep(current, next));
				current = next;
				
				next = current.uniformize(knownCollinearPoints);
				steps.add(new UniformizationStep(current, next));
				current = next;
				
				next = current.simplify();
				steps.add(new SimplificationStep(current, next));
				current = next;
				
				description = new ProofDescription(steps, statement, ndgConditions);
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
			verify(expr);
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
	
	/**
	 * Initializes the coords HashMap.
	 */
	private void initCoords() { 
		coords = new HashMap<String, FloatCoordinates>();
		coords.put("iO", new FloatCoordinates(42, 17));
		coords.put("iU", new FloatCoordinates(13, -66));
		coords.put("iV", new FloatCoordinates(2, 34));
		double norm = 10000000;
		for (GeoConstruction cons : constructions) {
			if (cons instanceof Point) {
				if (cons instanceof FreePoint) {
					// TODO put real randomness here
					double x = (double) cons.hashCode() + 1;
					double y = (double) coords.hashCode() + 3;
					// TODO debug
					/*
					boolean cp = false;
					if (cons.getGeoObjectLabel().equals("a")) {
						cp = true;
						x = 0.1;
						y = 0.1;
					} else if (cons.getGeoObjectLabel().equals("b")) {
						cp = true;
						x = 10.1;
						y = 10.1;
					} else if (cons.getGeoObjectLabel().equals("c")) {
						cp = true;
						x = 0.1;
						y = 20;
					} else if (cons.getGeoObjectLabel().equals("d")) {
						cp = true;
						x = 15;
						y = 5;
					}
					if (cp) {
						x *= norm;
						y *= norm;
					}
					*/
					coords.put(cons.getGeoObjectLabel(), new FloatCoordinates(x/norm, y/norm));
				} else if (cons instanceof AMIntersectionPoint) {
					AMIntersectionPoint pt = (AMIntersectionPoint) cons;
					String p = pt.getP().getGeoObjectLabel();
					String q = pt.getQ().getGeoObjectLabel();
					String u = pt.getU().getGeoObjectLabel();
					String v = pt.getV().getGeoObjectLabel();
					double xp = coords.get(p).x;
					double xq = coords.get(q).x;
					double xu = coords.get(u).x;
					double xv = coords.get(v).x;
					double yp = coords.get(p).y;
					double yq = coords.get(q).y;
					double yu = coords.get(u).y;
					double yv = coords.get(v).y;
					double x = (yu - yp + xp*((yq-yp)/(xq-xp)) - xu*((yv-yu)/(xv-xu)))/((yq-yp)/(xq-xp) - (yv-yu)/(xv-xu));
					double y = yp + (x-xp)*((yq-yp)/(xq-xp));
					coords.put(cons.getGeoObjectLabel(), new FloatCoordinates(x, y));
				} else if (cons instanceof AMFootPoint) {
					AMFootPoint pt = (AMFootPoint) cons;
					String p = pt.getP().getGeoObjectLabel();
					String u = pt.getU().getGeoObjectLabel();
					String v = pt.getV().getGeoObjectLabel();
					double xp = coords.get(p).x;
					double xu = coords.get(u).x;
					double xv = coords.get(v).x;
					double yp = coords.get(p).y;
					double yu = coords.get(u).y;
					double yv = coords.get(v).y;
					double x = (xp*(xv-xu) + (yp - yu + xu*((yv-yu)/(xv-xu)))*(yv-yu))/(xv - xu + ((yv-yu)*(yv-yu)/(xv-xu)));
					double y = yu + (x-xu)*((yv-yu)/(xv-xu));
					coords.put(cons.getGeoObjectLabel(), new FloatCoordinates(x, y));
				} else if (cons instanceof PRatioPoint) {
					PRatioPoint pt = (PRatioPoint) cons;
					String w = pt.getW().getGeoObjectLabel();
					String u = pt.getU().getGeoObjectLabel();
					String v = pt.getV().getGeoObjectLabel();
					double r = pt.getR().testValue(coords);
					double xw = coords.get(w).x;
					double xu = coords.get(u).x;
					double xv = coords.get(v).x;
					double yw = coords.get(w).y;
					double yu = coords.get(u).y;
					double yv = coords.get(v).y;
					double x = xw + r*(xv-xu);
					double y = yw + r*(yv-yu);
					coords.put(cons.getGeoObjectLabel(), new FloatCoordinates(x, y));
				} else if (cons instanceof TRatioPoint) {
					TRatioPoint pt = (TRatioPoint) cons;
					String u = pt.getU().getGeoObjectLabel();
					String v = pt.getV().getGeoObjectLabel();
					double r = pt.getR().testValue(coords);
					double xu = coords.get(u).x;
					double xv = coords.get(v).x;
					double yu = coords.get(u).y;
					double yv = coords.get(v).y;
					double x = xu - r*(yv-yu);
					double y = yu + r*(xv-xu);
					coords.put(cons.getGeoObjectLabel(), new FloatCoordinates(x, y));
				}
			}
		}
	}
	
	/**
	 * Verifies that a given formula is zero, by approximating its actual value.
	 */
	private static void verify(AMExpression expr) {
		double margin = 1;
		double value = expr.testValue(coords);
		if (value < margin && value > -margin)
			OpenGeoProver.settings.getLogger().debug(" (Verification OK : value = " + value + ")");
		else
			OpenGeoProver.settings.getLogger().debug(" (Verification failed : value = " + value + ")");
	}
	
	/**
	 * Debug
	 * @throws UnknownStatementException 
	 */
	/*
	private void grosDebug() {
		Vector<GeoConstruction> steps = new Vector<GeoConstruction>(this.constructions);
		debug("===========================");
		constructions = new Vector<GeoConstruction>();
		Point a = new FreePoint("a"); // (0,0)
		Point b = new FreePoint("b"); // (10,10)
		Point m = new PRatioPoint("m", a, a, b, new Fraction(new BasicNumber(1), new BasicNumber(2))); // (5, 5)
		constructions.add(a);
		constructions.add(b);
		constructions.add(m);
		initCoords();
		for (GeoConstruction cons : constructions) {
			String label = cons.getGeoObjectLabel();
			debug("Point " + label + " : (" + coords.get(label).x + ", " + coords.get(label).y + ")");
		}
		AMExpression segment = new PythagorasDifference(a, m, a);
		debug("P_ama = " + segment.testValue(coords) + " // ", segment); // 25
		debug("Elimination..."); // 25
		try {
			segment = segment.eliminate(m, isLemmaUsed, this);
			debug("P_ama = " + segment.testValue(coords) + " // ", segment); // 25
		} catch (UnknownStatementException e) {}
		debug("Simplification..."); // 25
		segment = segment.simplify().uniformize(knownCollinearPoints);
		debug("P_ama = " + segment.testValue(coords) + " // ", segment); // 25
		segment = segment.reduceToSingleFraction();
		debug("Reducing into single fraction..."); // 25
		debug("P_ama = " + segment.testValue(coords) + " // ", segment); // 25
		constructions = steps;
		if (segment instanceof Fraction) {
			debug("Removing of the denominator...");
			segment = ((Fraction) segment).getNumerator();
			debug("P_ama = " + segment.testValue(coords) + " // ", segment); // 25
		}
		debug("Simplification...");
		segment = segment.simplify().uniformize(knownCollinearPoints);
		debug("P_ama = " + segment.testValue(coords) + " // ", segment); // 25
		debug("Transforming into a sum of products of geometrical quantities...");
		segment = segment.toSumOfProducts();
		debug("P_ama = " + segment.testValue(coords) + " // ", segment); // 25
		debug("===========================");
	}
	*/
}