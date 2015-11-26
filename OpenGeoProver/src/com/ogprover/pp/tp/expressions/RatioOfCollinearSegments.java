/* 
 * DISCLAIMER PLACEHOLDER 
 */
package com.ogprover.pp.tp.expressions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.pp.tp.auxiliary.FloatCoordinates;
import com.ogprover.pp.tp.auxiliary.UnknownStatementException;
import com.ogprover.pp.tp.geoconstruction.AMFootPoint;
import com.ogprover.pp.tp.geoconstruction.AMIntersectionPoint;
import com.ogprover.pp.tp.geoconstruction.FreePoint;
import com.ogprover.pp.tp.geoconstruction.PRatioPoint;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.ogprover.pp.tp.geoconstruction.TRatioPoint;
import com.ogprover.pp.tp.thmstatement.AreaMethodTheoremStatement;
import com.ogprover.pp.tp.thmstatement.CollinearPoints;
import com.ogprover.pp.tp.thmstatement.ThmStatement;
import com.ogprover.thmprover.AreaMethodProver;
import com.ogprover.thmprover.TheoremProver;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for representing the ratio between two collinear segments,
 * 		for the area method algorithm.</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Damien Desfontaines
 */
public class RatioOfCollinearSegments extends GeometricQuantity {
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
	 * Four Point variables A, B, C and D, to represent AB/CD.
	 */
	protected Point a,b,c,d; 

	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	public Point getA() {
		return a;
	}
	public Point getB() {
		return b;
	}
	public Point getC() {
		return c;
	}
	public Point getD() {
		return d;
	}
	
	
	/**
	 * @see com.ogprover.pp.tp.expressions.AMExpression#getPoints()
	 */
	public HashSet<Point> getPoints() {
		HashSet<Point> points = new HashSet<Point>();
		points.add(a);
		points.add(b);
		points.add(c);
		points.add(d);
		return points;
	}
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method, the ratio is then [ab]/[cd], where [ab] and [cd] are oriented parallel segments.
	 * @param a		Point
	 * @param b		Point
	 * @param c		Point
	 * @param d		Point
	 */
	public RatioOfCollinearSegments(Point a, Point b, Point c, Point d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}
	
	
	/*
	 * ======================================================================
	 * ======================= COMMON OBJECT METHODS ========================
	 * ======================================================================
	 */
	/**
	 * @see com.ogprover.pp.tp.expressions.AMExpression#toString()
	 */
	@Override
	public String print() {
		StringBuilder s = new StringBuilder();
		s.append(a.getGeoObjectLabel());
		s.append(b.getGeoObjectLabel());
		s.append("/");
		s.append(c.getGeoObjectLabel());
		s.append(d.getGeoObjectLabel());
		return s.toString();
	}
	
	@Override
	public boolean equals(Object expr) {
		if (!(expr instanceof RatioOfCollinearSegments))
			return false;
		RatioOfCollinearSegments ratio = (RatioOfCollinearSegments)expr;
		return (a.equals(ratio.getA()) && b.equals(ratio.getB()) && c.equals(ratio.getC()) && d.equals(ratio.getD()));
	}
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	@Override
	public boolean containsOnlyFreePoints() {
		if (a instanceof FreePoint && b instanceof FreePoint && c instanceof FreePoint && d instanceof FreePoint) {
			return true;
		}
		return false;
	}
	
	@Override
	public AMExpression uniformize(HashSet<HashSet<Point>> knownCollinearPoints) {
		if (a.equals(b))
			return new BasicNumber(0); // AA/CD -> 0
		if (a.equals(c) && b.equals(d))
			return new BasicNumber(1);
		if (a.equals(d) && b.equals(c))
			return new BasicNumber(-1);
		if (a.compare(b)) {
			if (c.compare(d)) {
				return this;
			}
			return new AdditiveInverse(new RatioOfCollinearSegments(a,b,d,c));
		}
		if (c.compare(d)) {
			return new AdditiveInverse(new RatioOfCollinearSegments(b,a,c,d));
		}
		return new RatioOfCollinearSegments(b,a,d,c);
	}
	
	@Override
	public AMExpression simplifyInOneStep() {
		// We assume that the verifications that the expression is non-zero have already been done.
		return this;
	}
	
	@Override
	public AMExpression eliminate(Point pt, Vector<Boolean> isLemmaUsed, AreaMethodProver prover) throws UnknownStatementException {
		// We want to eliminate the point Y in the fraction ab/cd
		if (!(a.equals(pt) || b.equals(pt) || c.equals(pt) || d.equals(pt)))
			return this; // ab/cd -> ab/cd
		if (a.equals(b))
			return new BasicNumber(0); // aa/cd -> 0
		if (b.equals(pt) && b.equals(c))
			return (new AdditiveInverse(new RatioOfCollinearSegments(a, b, d, c))).eliminate(pt, isLemmaUsed, prover); // ay/yd -> -ay/dy
		if (a.equals(pt) && a.equals(d))
			return (new AdditiveInverse(new RatioOfCollinearSegments(b, a, c, d))).eliminate(pt, isLemmaUsed, prover); // ya/cy -> -ay/cy
		if (a.equals(pt) && a.equals(c))
			return (new AdditiveInverse(new RatioOfCollinearSegments(b, a, d, c))).eliminate(pt, isLemmaUsed, prover); // ya/yd -> ay/dy
		if (a.equals(pt))
			return (new AdditiveInverse(new RatioOfCollinearSegments(b, a, c, d))).eliminate(pt, isLemmaUsed, prover); // ya/cd -> -ay/cd
		if (c.equals(pt))
			return (new Fraction(new BasicNumber(1), new RatioOfCollinearSegments(d, c, b, a))).eliminate(pt, isLemmaUsed, prover); // ab/yd -> 1/(dy/ba)
		/*
		 * See http://hal.inria.fr/hal-00426563/PDF/areaMethodRecapV2.pdf for an explanation of the formulas above
		 */
		if (b.equals(pt) && b.equals(d)) {
			if (pt instanceof AMIntersectionPoint) {
				isLemmaUsed.set(1, true);
				Point u = ((AMIntersectionPoint)pt).getU();
				Point v = ((AMIntersectionPoint)pt).getV();
				Point p = ((AMIntersectionPoint)pt).getP();
				Point q = ((AMIntersectionPoint)pt).getQ();
				
				ArrayList<Point> points = new ArrayList<Point>();
				points.add(a);
				points.add(u);
				points.add(v);
				ThmStatement statementToVerify = new CollinearPoints(points);
				AreaMethodTheoremStatement areaMethodStatement = statementToVerify.getAreaMethodStatement();
				AreaMethodProver verifier = new AreaMethodProver(areaMethodStatement, prover.getConstructions(), prover.getNDGConditions());
				int retCode = verifier.prove();
				if (retCode == TheoremProver.THEO_PROVE_RET_CODE_TRUE) { // a, u and v are collinear
					AMExpression sapq = new AreaOfTriangle(a, p, q);
					AMExpression scpq = new AreaOfTriangle(c, p, q);
					return new Fraction(sapq, scpq);
				}
				if (retCode == TheoremProver.THEO_PROVE_RET_CODE_FALSE) { // a, u and v are not collinear
					AMExpression sauv = new AreaOfTriangle(a, u, v);
					AMExpression scuv = new AreaOfTriangle(c, u, v);
					return new Fraction(sauv, scuv);
				}
				// If the prover crashed
				throw new UnknownStatementException("Elimination of the point " + pt.getGeoObjectLabel() + " in the ratio " + this.print());
			}
			
			if (pt instanceof AMFootPoint) {
				isLemmaUsed.set(2, true);
				Point p = ((AMFootPoint)pt).getP();
				Point u = ((AMFootPoint)pt).getU();
				Point v = ((AMFootPoint)pt).getV();
			
				ArrayList<Point> points = new ArrayList<Point>();
				points.add(a);
				points.add(u);
				points.add(v);
				ThmStatement statementToVerify = new CollinearPoints(points);
				AreaMethodTheoremStatement areaMethodStatement = statementToVerify.getAreaMethodStatement();
				AreaMethodProver verifier = new AreaMethodProver(areaMethodStatement, prover.getConstructions(), prover.getNDGConditions());
				int retCode = verifier.prove();
				if (retCode == TheoremProver.THEO_PROVE_RET_CODE_TRUE) { // a, u and v are collinear
					AMExpression ppuv = new PythagorasDifference(p, u, v);
					AMExpression ppcv = new PythagorasDifference(p, c, v);
					AMExpression pacv = new PythagorasDifference(a, c, v);
					AMExpression ppcav = new Difference(ppcv, pacv);
					AMExpression ppvu = new PythagorasDifference(p, v, u);
					AMExpression ppcu = new PythagorasDifference(p, c, u);
					AMExpression pacu = new PythagorasDifference(a, c, u);
					AMExpression ppcau = new Difference(ppcu, pacu);
					AMExpression numerator = new Sum(new Product(ppuv, ppcav), new Product(ppvu, ppcau));
					
					AMExpression pcvc = new PythagorasDifference(c, v, c);
					AMExpression term1 = new Product(ppuv, pcvc);
					AMExpression pcuc = new PythagorasDifference(c, u, c);
					AMExpression term2 = new Product(ppvu, pcuc);
					AMExpression term3 = new Product(ppuv, ppvu);
					AMExpression denominator = new Sum(term1, new Sum(term2, term3));
					
					return new Fraction(numerator, denominator);
				}
				if (retCode == TheoremProver.THEO_PROVE_RET_CODE_FALSE) { // a, u and v are not collinear
					AMExpression sauv = new AreaOfTriangle(a, u, v);
					AMExpression scuv = new AreaOfTriangle(c, u, v);
					return new Fraction(sauv, scuv);
				}
				// If the prover crashed
				throw new UnknownStatementException("Elimination of the point " + pt.getGeoObjectLabel() + " in the ratio " + this.print());
			}
			
			if (pt instanceof PRatioPoint) {
				isLemmaUsed.set(3, true);
				Point r = ((PRatioPoint)pt).getW();
				Point p = ((PRatioPoint)pt).getU();
				Point q = ((PRatioPoint)pt).getV();
				AMExpression coeff = ((PRatioPoint)pt).getR();
				
				ArrayList<Point> points = new ArrayList<Point>();
				points.add(a);
				points.add(r);
				points.add(pt);
				ThmStatement statementToVerify = new CollinearPoints(points);
				AreaMethodTheoremStatement areaMethodStatement = statementToVerify.getAreaMethodStatement();
				AreaMethodProver verifier = new AreaMethodProver(areaMethodStatement, prover.getConstructions(), prover.getNDGConditions());
				int retCode = verifier.prove();
				if (retCode == TheoremProver.THEO_PROVE_RET_CODE_TRUE) { // a, w and y are collinear
					AMExpression ratioarpq = new RatioOfCollinearSegments(a, r, p, q);
					AMExpression ratiocrpq = new RatioOfCollinearSegments(c, r, p, q);
					return new Fraction(new Sum(ratioarpq, coeff), new Sum(ratiocrpq, coeff));
				}
				if (retCode == TheoremProver.THEO_PROVE_RET_CODE_FALSE) { // a, w and y are not collinear
					AMExpression sapr = new AreaOfTriangle(a, p, r); 
					AMExpression sarq = new AreaOfTriangle(a, r, q); 
					AMExpression scpr = new AreaOfTriangle(c, p, r); 
					AMExpression scrq = new AreaOfTriangle(c, r, q); 
					return new Fraction(new Sum(sapr, sarq), new Sum(scpr, scrq));
				}
				// If the prover crashed
				throw new UnknownStatementException("Elimination of the point " + pt.getGeoObjectLabel() + " in the ratio " + this.print());
			}
			if (pt instanceof TRatioPoint) {
				isLemmaUsed.set(4, true);
				Point p = ((TRatioPoint)pt).getU();
				Point q = ((TRatioPoint)pt).getV();
				AMExpression r = ((TRatioPoint)pt).getR();
				
				ArrayList<Point> points = new ArrayList<Point>();
				points.add(a);
				points.add(p);
				points.add(pt);
				ThmStatement statementToVerify = new CollinearPoints(points);
				AreaMethodTheoremStatement areaMethodStatement = statementToVerify.getAreaMethodStatement();
				AreaMethodProver verifier = new AreaMethodProver(areaMethodStatement, prover.getConstructions(), prover.getNDGConditions());
				int retCode = verifier.prove();
				if (retCode == TheoremProver.THEO_PROVE_RET_CODE_TRUE) { // a, p and y are collinear
					AMExpression coeff = new Fraction(r, new BasicNumber(4));
					AMExpression sapq = new AreaOfTriangle(a, p, q);
					AMExpression ppqp = new PythagorasDifference(p, q, p);
					AMExpression numerator = new Difference(sapq, new Product(coeff, ppqp));
					AMExpression scpq = new AreaOfTriangle(c, p, q);
					AMExpression denominator = new Difference(scpq, new Product(coeff, ppqp));
					return new Fraction(numerator, denominator);
				}
				if (retCode == TheoremProver.THEO_PROVE_RET_CODE_FALSE) { // a, p and y are not collinear
					AMExpression papq = new PythagorasDifference(a, p, q);
					AMExpression pcpq = new PythagorasDifference(a, p, q);
					return new Fraction(papq, pcpq);
				}
				// If the prover crashed
				throw new UnknownStatementException("Elimination of the point " + pt.getGeoObjectLabel() + " in the ratio " + this.print());
			}
		} // Wow, this is the least fun code I've ever written - I am sorry you have to read it
		if (d.equals(pt))
			return (new Fraction(new BasicNumber(1), new RatioOfCollinearSegments(c, d, a, b))).eliminate(pt, isLemmaUsed, prover); // ab/cy -> 1/(cy/ab)
		if (b.equals(pt)) {
			if (pt instanceof AMIntersectionPoint) {
				isLemmaUsed.set(1, true);
				Point u = ((AMIntersectionPoint)pt).getU();
				Point v = ((AMIntersectionPoint)pt).getV();
				Point p = ((AMIntersectionPoint)pt).getP();
				Point q = ((AMIntersectionPoint)pt).getQ();
				
				ArrayList<Point> points = new ArrayList<Point>();
				points.add(a);
				points.add(u);
				points.add(v);
				ThmStatement statementToVerify = new CollinearPoints(points);
				AreaMethodTheoremStatement areaMethodStatement = statementToVerify.getAreaMethodStatement();
				AreaMethodProver verifier = new AreaMethodProver(areaMethodStatement, prover.getConstructions(), prover.getNDGConditions());
				int retCode = verifier.prove();
				if (retCode == TheoremProver.THEO_PROVE_RET_CODE_TRUE) { // a, u and v are collinear
					AMExpression sapq = new AreaOfTriangle(a, p, q);
					AMExpression scpd = new AreaOfTriangle(c, p, d);
					AMExpression scdq = new AreaOfTriangle(c, d, q);
					return new Fraction(sapq, new Sum(scpd, scdq));
				}
				if (retCode == TheoremProver.THEO_PROVE_RET_CODE_FALSE) { // a, u and v are not collinear
					AMExpression sauv = new AreaOfTriangle(a, u, v);
					AMExpression scud = new AreaOfTriangle(c, u, d);
					AMExpression scdv = new AreaOfTriangle(c, d, v);
					return new Fraction(sauv, new Sum(scud, scdv));
				}
				// If the prover crashed
				throw new UnknownStatementException("Elimination of the point " + pt.getGeoObjectLabel() + " in the ratio " + this.print());
			}
			
			if (pt instanceof AMFootPoint) {
				isLemmaUsed.set(2, true);
				Point p = ((AMFootPoint)pt).getP();
				Point u = ((AMFootPoint)pt).getU();
				Point v = ((AMFootPoint)pt).getV();
			
				ArrayList<Point> points = new ArrayList<Point>();
				points.add(a);
				points.add(u);
				points.add(v);
				ThmStatement statementToVerify = new CollinearPoints(points);
				AreaMethodTheoremStatement areaMethodStatement = statementToVerify.getAreaMethodStatement();
				AreaMethodProver verifier = new AreaMethodProver(areaMethodStatement, prover.getConstructions(), prover.getNDGConditions());
				int retCode = verifier.prove();
				if (retCode == TheoremProver.THEO_PROVE_RET_CODE_TRUE) { // a, u and v are collinear
					AMExpression ppcd = new PythagorasDifference(p, c, d);
					AMExpression pacd = new PythagorasDifference(a, c, d);
					AMExpression pcdc = new PythagorasDifference(c, d, c);
					return new Fraction(new Difference(ppcd, pacd), pcdc);
				}
				if (retCode == TheoremProver.THEO_PROVE_RET_CODE_FALSE) { // a, u and v are not collinear
					AMExpression sauv = new AreaOfTriangle(a, u, v);
					AMExpression scud = new AreaOfTriangle(c, u, d);
					AMExpression scdv = new AreaOfTriangle(c, d, v);
					return new Fraction(sauv, new Sum(scud, scdv));
				}
				// If the prover crashed
				throw new UnknownStatementException("Elimination of the point " + pt.getGeoObjectLabel() + " in the ratio " + this.print());
			}
			
			if (pt instanceof PRatioPoint) {
				isLemmaUsed.set(3, true);
				Point r = ((PRatioPoint)pt).getW();
				Point p = ((PRatioPoint)pt).getU();
				Point q = ((PRatioPoint)pt).getV();
				AMExpression coeff = ((PRatioPoint)pt).getR();
				
				ArrayList<Point> points = new ArrayList<Point>();
				points.add(a);
				points.add(r);
				points.add(pt);
				ThmStatement statementToVerify = new CollinearPoints(points);
				AreaMethodTheoremStatement areaMethodStatement = statementToVerify.getAreaMethodStatement();
				AreaMethodProver verifier = new AreaMethodProver(areaMethodStatement, prover.getConstructions(), prover.getNDGConditions());
				int retCode = verifier.prove();
				if (retCode == TheoremProver.THEO_PROVE_RET_CODE_TRUE) { // a, w and y are collinear
					AMExpression ratioarpq = new RatioOfCollinearSegments(a, r, p, q);
					AMExpression ratiocdpq = new RatioOfCollinearSegments(c, d, p, q);
					return new Fraction(new Sum(ratioarpq, coeff), ratiocdpq);
				}
				if (retCode == TheoremProver.THEO_PROVE_RET_CODE_FALSE) { // a, w and y are not collinear
					AMExpression sapr = new AreaOfTriangle(a, p, r);
					AMExpression sarq = new AreaOfTriangle(a, r, q);
					AMExpression scpd = new AreaOfTriangle(c, p, d);
					AMExpression scdq = new AreaOfTriangle(c, d, q);
					return new Fraction(new Sum(sapr, sarq), new Sum(scpd, scdq));
				}
				// If the prover crashed
				throw new UnknownStatementException("Elimination of the point " + pt.getGeoObjectLabel() + " in the ratio " + this.print());
			}
			
			if (pt instanceof TRatioPoint) {
				isLemmaUsed.set(4, true);
				Point p = ((TRatioPoint)pt).getU();
				Point q = ((TRatioPoint)pt).getV();
				AMExpression r = ((TRatioPoint)pt).getR();
				
				ArrayList<Point> points = new ArrayList<Point>();
				points.add(a);
				points.add(p);
				points.add(pt);
				ThmStatement statementToVerify = new CollinearPoints(points);
				AreaMethodTheoremStatement areaMethodStatement = statementToVerify.getAreaMethodStatement();
				AreaMethodProver verifier = new AreaMethodProver(areaMethodStatement, prover.getConstructions(), prover.getNDGConditions());
				int retCode = verifier.prove();
				if (retCode == TheoremProver.THEO_PROVE_RET_CODE_TRUE) { // a, p and y are collinear
					AMExpression coeff = new Fraction(r, new BasicNumber(4));
					AMExpression sapq = new AreaOfTriangle(a, p, q);
					AMExpression ppqp = new PythagorasDifference(p, q, p);
					AMExpression scpd = new AreaOfTriangle(c, p, d);
					AMExpression scdq = new AreaOfTriangle(c, d, q);
					AMExpression numerator = new Difference(sapq, new Product(coeff, ppqp));
					AMExpression denominator = new Sum(scpd, scdq);
					return new Fraction(numerator, denominator);
				}
				if (retCode == TheoremProver.THEO_PROVE_RET_CODE_FALSE) { // a, p and y are not collinear
					AMExpression papq = new PythagorasDifference(a, p, q);
					AMExpression pcpq = new PythagorasDifference(c, p, q);
					AMExpression pdpq = new PythagorasDifference(d, p, q);
					return new Fraction(papq, new Difference(pcpq, pdpq));
				}
				// If the prover crashed
				throw new UnknownStatementException("Elimination of the point " + pt.getGeoObjectLabel() + " in the ratio " + this.print());
			}
		}
		
		OpenGeoProver.settings.getLogger().error("Unexpected form of fraction : " + this.print() +
				" where the point to eliminate is " + pt.getGeoObjectLabel());
		return null;
	}
	
	@Override
	public AMExpression reduceToSingleFraction() {
		return this;
	}
	
	@Override
	public AMExpression toIndependantVariables(AreaMethodProver prover) throws UnknownStatementException {
		ArrayList<Point> points = new ArrayList<Point>();
		points.add(a);
		points.add(c);
		points.add(d);
		ThmStatement statementToVerify = new CollinearPoints(points);
		AreaMethodTheoremStatement areaMethodStatement = statementToVerify.getAreaMethodStatement();
		AreaMethodProver verifier = new AreaMethodProver(areaMethodStatement, prover.getConstructions(), prover.getNDGConditions());
		int retCode = verifier.prove();
		if (retCode == TheoremProver.THEO_PROVE_RET_CODE_TRUE) { // a, c and d are collinear
			AMExpression xcya = new Product(getX(c), getY(a));
			AMExpression xcyb = new Product(getX(c), getY(b));
			AMExpression yaxb = new Product(getY(a), getX(b));
			AMExpression ybxa = new Product(getY(b), getX(a));
			AMExpression ycxa = new Product(getY(c), getX(a));
			AMExpression ycxb = new Product(getY(c), getX(b));
			AMExpression xcyd = new Product(getX(c), getY(d));
			AMExpression yaxd = new Product(getY(a), getX(d));
			AMExpression ycxd = new Product(getY(c), getX(d));
			AMExpression xayd = new Product(getX(a), getY(d));
			AMExpression numeratorPart1 = new Difference(xcya, new Sum(xcyb, yaxb));
			AMExpression numeratorPart2 = new Sum(new Difference(ybxa, ycxa), ycxb);
			AMExpression numerator = new Sum(numeratorPart1, numeratorPart2);
			AMExpression denominatorPart1 = new Difference(xcya, new Sum(xcyd, yaxd));
			AMExpression denominatorPart2 = new Sum(ycxd, new Difference(xayd, ycxa));
			AMExpression denominator = new Sum(denominatorPart1, denominatorPart2);
			return new Fraction(numerator, denominator);
		}
		if (retCode == TheoremProver.THEO_PROVE_RET_CODE_FALSE) { // a, p and y are not collinear
			AMExpression xbya = new Product(getX(b), getY(a));
			AMExpression xayb = new Product(getX(a), getY(b));
			AMExpression xdyc = new Product(getX(d), getY(c));
			AMExpression xcyd = new Product(getX(c), getY(d));
			AMExpression numerator = new Difference(xbya, xayb);
			AMExpression denominator = new Difference(xdyc, xcyd);
			return new Fraction(numerator, denominator);
		}
		// If the prover crashed
		throw new UnknownStatementException("Reducing to independant variables of : " + this.print());
		
	}
	
	@Override
	public int size() {
		return 1;
	}
	
	@Override
	public AMExpression replace(HashMap<Point, Point> replacementMap) {
		if (replacementMap.containsKey(a))
			return new RatioOfCollinearSegments(replacementMap.get(a), b, c, d).replace(replacementMap);
		if (replacementMap.containsKey(b))
			return new RatioOfCollinearSegments(a, replacementMap.get(b), c, d).replace(replacementMap);
		if (replacementMap.containsKey(c))
			return new RatioOfCollinearSegments(a, b, replacementMap.get(c), d).replace(replacementMap);
		if (replacementMap.containsKey(d))
			return new RatioOfCollinearSegments(a, b, c, replacementMap.get(d)).replace(replacementMap);
		return this;
	}
	
	@Override
	public AMExpression toSumOfProducts() {
		return new SumOfProducts(new BigProduct(this));
	}
	@Override
	public double testValue(HashMap<String, FloatCoordinates> coords) {
		FloatCoordinates coordsA = coords.get(a.getGeoObjectLabel());
		FloatCoordinates coordsB = coords.get(b.getGeoObjectLabel());
		FloatCoordinates coordsC = coords.get(c.getGeoObjectLabel());
		FloatCoordinates coordsD = coords.get(d.getGeoObjectLabel());
		double xa = coordsA.x;
		double xb = coordsB.x;
		double xc = coordsC.x;
		double xd = coordsD.x;
		return (xb-xa)/(xd-xc);
	}
}
