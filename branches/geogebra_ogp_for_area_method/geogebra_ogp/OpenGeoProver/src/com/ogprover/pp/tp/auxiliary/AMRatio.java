/* 
 * DISCLAIMER PLACEHOLDER 
 */
package com.ogprover.pp.tp.auxiliary;

import java.util.ArrayList;
import java.util.HashSet;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.pp.tp.geoconstruction.AMFootPoint;
import com.ogprover.pp.tp.geoconstruction.AMIntersectionPoint;
import com.ogprover.pp.tp.geoconstruction.FreePoint;
import com.ogprover.pp.tp.geoconstruction.PRatioPoint;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.ogprover.pp.tp.geoconstruction.TRatioPoint;
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
public class AMRatio extends AMExpression {
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
	 * @see com.ogprover.pp.tp.auxiliary.AMExpression#getPoints()
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
	public AMRatio(Point a, Point b, Point c, Point d) {
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
	 * @see com.ogprover.pp.tp.auxiliary.AMExpression#toString()
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
		if (!(expr instanceof AMRatio))
			return false;
		AMRatio ratio = (AMRatio)expr;
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
	public AMExpression uniformize() {
		if (a.compare(b)) {
			if (c.compare(d)) {
				return this;
			}
			return new AMAdditiveInverse(new AMRatio(a,b,d,c));
		}
		if (c.compare(d)) {
			return new AMAdditiveInverse(new AMRatio(b,a,c,d));
		}
		return new AMRatio(b,a,d,c);
	}
	
	@Override
	public AMExpression simplifyInOneStep() {
		if (a.equals(b))
			return new AMNumber(0); // AA/CD -> 0
		if (a.equals(c) && b.equals(d))
			return new AMNumber(1);
		if (a.equals(d) && b.equals(c))
			return new AMNumber(-1);
		return this;
	}
	
	@Override
	public AMExpression eliminate(Point pt, AreaMethodProver prover) throws UnknownStatementException {
		// We want to eliminate the point Y in the fraction ab/cd
		if (!(a.equals(pt) || b.equals(pt) || c.equals(pt) || d.equals(pt)))
			return this; // ab/cd -> ab/cd
		if (a.equals(b))
			return new AMNumber(0); // aa/cd -> 0
		if (b.equals(pt) && b.equals(c))
			return (new AMAdditiveInverse(new AMRatio(a, b, d, c))).eliminate(pt, prover); // ay/yd -> -ay/dy
		if (a.equals(pt) && a.equals(d))
			return (new AMAdditiveInverse(new AMRatio(b, a, c, d))).eliminate(pt, prover); // ya/cy -> -ay/cy
		if (a.equals(pt) && a.equals(c))
			return (new AMAdditiveInverse(new AMRatio(b, a, d, c))).eliminate(pt, prover); // ya/yd -> ay/dy
		if (a.equals(pt))
			return (new AMAdditiveInverse(new AMRatio(b, a, c, d))).eliminate(pt, prover); // ya/cd -> -ay/cd
		if (d.equals(pt))
			return (new AMFraction(new AMNumber(1), new AMRatio(c, d, a, b))).eliminate(pt, prover); // ab/cy -> 1/(cy/ab)
		if (c.equals(pt))
			return (new AMFraction(new AMNumber(1), new AMRatio(c, d, b, a))).eliminate(pt, prover); // ab/yd -> 1/(yd/ba)
		/*
		 * See http://hal.inria.fr/hal-00426563/PDF/areaMethodRecapV2.pdf for an explanation of the formulas above
		 */
		if (b.equals(pt) && b.equals(d)) {
			if (pt instanceof AMIntersectionPoint) {
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
					AMExpression sapq = new AMAreaOfTriangle(a, p, q);
					AMExpression scpq = new AMAreaOfTriangle(c, p, q);
					return new AMFraction(sapq, scpq);
				}
				if (retCode == TheoremProver.THEO_PROVE_RET_CODE_FALSE) { // a, u and v are not collinear
					AMExpression sauv = new AMAreaOfTriangle(a, u, v);
					AMExpression scuv = new AMAreaOfTriangle(c, u, v);
					return new AMFraction(sauv, scuv);
				}
				// If the prover crashed
				throw new UnknownStatementException("Elimination of the point " + pt.getGeoObjectLabel() + " in the ratio " + this.print());
			}
			
			if (pt instanceof AMFootPoint) {
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
					AMExpression ppuv = new AMPythagorasDifference(p, u, v);
					AMExpression ppcv = new AMPythagorasDifference(p, c, v);
					AMExpression pacv = new AMPythagorasDifference(a, c, v);
					AMExpression ppcav = new AMDifference(ppcv, pacv);
					AMExpression ppvu = new AMPythagorasDifference(p, v, u);
					AMExpression ppcu = new AMPythagorasDifference(p, c, u);
					AMExpression pacu = new AMPythagorasDifference(a, c, u);
					AMExpression ppcau = new AMDifference(ppcu, pacu);
					AMExpression numerator = new AMSum(new AMProduct(ppuv, ppcav), new AMProduct(ppvu, ppcau));
					
					AMExpression pcvc = new AMPythagorasDifference(c, v, c);
					AMExpression term1 = new AMProduct(ppuv, pcvc);
					AMExpression pcuc = new AMPythagorasDifference(c, u, c);
					AMExpression term2 = new AMProduct(ppvu, pcuc);
					AMExpression term3 = new AMProduct(ppuv, ppvu);
					AMExpression denominator = new AMSum(term1, new AMSum(term2, term3));
					
					return new AMFraction(numerator, denominator);
				}
				if (retCode == TheoremProver.THEO_PROVE_RET_CODE_FALSE) { // a, u and v are not collinear
					AMExpression sauv = new AMAreaOfTriangle(a, u, v);
					AMExpression scuv = new AMAreaOfTriangle(c, u, v);
					return new AMFraction(sauv, scuv);
				}
				// If the prover crashed
				throw new UnknownStatementException("Elimination of the point " + pt.getGeoObjectLabel() + " in the ratio " + this.print());
			}
			
			if (pt instanceof PRatioPoint) {
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
					AMExpression ratioarpq = new AMRatio(a, r, p, q);
					AMExpression ratiocrpq = new AMRatio(c, r, p, q);
					return new AMFraction(new AMSum(ratioarpq, coeff), new AMSum(ratiocrpq, coeff));
				}
				if (retCode == TheoremProver.THEO_PROVE_RET_CODE_FALSE) { // a, w and y are not collinear
					AMExpression sapr = new AMAreaOfTriangle(a, p, r); 
					AMExpression sarq = new AMAreaOfTriangle(a, r, q); 
					AMExpression scpr = new AMAreaOfTriangle(c, p, r); 
					AMExpression scrq = new AMAreaOfTriangle(c, r, q); 
					return new AMFraction(new AMSum(sapr, sarq), new AMSum(scpr, scrq));
				}
				// If the prover crashed
				throw new UnknownStatementException("Elimination of the point " + pt.getGeoObjectLabel() + " in the ratio " + this.print());
			}
			if (pt instanceof TRatioPoint) {
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
					AMExpression coeff = new AMFraction(r, new AMNumber(4));
					AMExpression sapq = new AMAreaOfTriangle(a, p, q);
					AMExpression ppqp = new AMPythagorasDifference(p, q, p);
					AMExpression numerator = new AMDifference(sapq, new AMProduct(coeff, ppqp));
					AMExpression scpq = new AMAreaOfTriangle(c, p, q);
					AMExpression denominator = new AMDifference(scpq, new AMProduct(coeff, ppqp));
					return new AMFraction(numerator, denominator);
				}
				if (retCode == TheoremProver.THEO_PROVE_RET_CODE_FALSE) { // a, p and y are not collinear
					AMExpression papq = new AMPythagorasDifference(a, p, q);
					AMExpression pcpq = new AMPythagorasDifference(a, p, q);
					return new AMFraction(papq, pcpq);
				}
				// If the prover crashed
				throw new UnknownStatementException("Elimination of the point " + pt.getGeoObjectLabel() + " in the ratio " + this.print());
			}
		} // Wow, this is the least fun code I've ever written - I am sorry you have to read it
		if (b.equals(pt)) {
			if (pt instanceof AMIntersectionPoint) {
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
					AMExpression sapq = new AMAreaOfTriangle(a, p, q);
					AMExpression scpd = new AMAreaOfTriangle(c, p, d);
					AMExpression scdq = new AMAreaOfTriangle(c, d, q);
					return new AMFraction(sapq, new AMSum(scpd, scdq));
				}
				if (retCode == TheoremProver.THEO_PROVE_RET_CODE_FALSE) { // a, u and v are not collinear
					AMExpression sauv = new AMAreaOfTriangle(a, u, v);
					AMExpression scud = new AMAreaOfTriangle(c, u, d);
					AMExpression scdv = new AMAreaOfTriangle(c, d, v);
					return new AMFraction(sauv, new AMSum(scud, scdv));
				}
				// If the prover crashed
				throw new UnknownStatementException("Elimination of the point " + pt.getGeoObjectLabel() + " in the ratio " + this.print());
			}
			
			if (pt instanceof AMFootPoint) {
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
					AMExpression ppcd = new AMPythagorasDifference(p, c, d);
					AMExpression pacd = new AMPythagorasDifference(a, c, d);
					AMExpression pcdc = new AMPythagorasDifference(c, d, c);
					return new AMFraction(new AMDifference(ppcd, pacd), pcdc);
				}
				if (retCode == TheoremProver.THEO_PROVE_RET_CODE_FALSE) { // a, u and v are not collinear
					AMExpression sauv = new AMAreaOfTriangle(a, u, v);
					AMExpression scud = new AMAreaOfTriangle(c, u, d);
					AMExpression scdv = new AMAreaOfTriangle(c, d, v);
					return new AMFraction(sauv, new AMSum(scud, scdv));
				}
				// If the prover crashed
				throw new UnknownStatementException("Elimination of the point " + pt.getGeoObjectLabel() + " in the ratio " + this.print());
			}
			
			if (pt instanceof PRatioPoint) {
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
					AMExpression ratioarpq = new AMRatio(a, r, p, q);
					AMExpression ratiocdpq = new AMRatio(c, d, p, q);
					return new AMFraction(new AMSum(ratioarpq, coeff), ratiocdpq);
				}
				if (retCode == TheoremProver.THEO_PROVE_RET_CODE_FALSE) { // a, w and y are not collinear
					AMExpression sapr = new AMAreaOfTriangle(a, p, r);
					AMExpression sarq = new AMAreaOfTriangle(a, r, q);
					AMExpression scpd = new AMAreaOfTriangle(c, p, d);
					AMExpression scdq = new AMAreaOfTriangle(c, d, q);
					return new AMFraction(new AMSum(sapr, sarq), new AMSum(scpd, scdq));
				}
				// If the prover crashed
				throw new UnknownStatementException("Elimination of the point " + pt.getGeoObjectLabel() + " in the ratio " + this.print());
			}
			
			if (pt instanceof TRatioPoint) {
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
					AMExpression coeff = new AMFraction(r, new AMNumber(4));
					AMExpression sapq = new AMAreaOfTriangle(a, p, q);
					AMExpression ppqp = new AMPythagorasDifference(p, q, p);
					AMExpression scpd = new AMAreaOfTriangle(c, p, d);
					AMExpression scdq = new AMAreaOfTriangle(c, d, q);
					AMExpression numerator = new AMDifference(sapq, new AMProduct(coeff, ppqp));
					AMExpression denominator = new AMSum(scpd, scdq);
					return new AMFraction(numerator, denominator);
				}
				if (retCode == TheoremProver.THEO_PROVE_RET_CODE_FALSE) { // a, p and y are not collinear
					AMExpression papq = new AMPythagorasDifference(a, p, q);
					AMExpression pcpq = new AMPythagorasDifference(c, p, q);
					AMExpression pdpq = new AMPythagorasDifference(d, p, q);
					return new AMFraction(papq, new AMDifference(pcpq, pdpq));
				}
				// If the prover crashed
				throw new UnknownStatementException("Elimination of the point " + pt.getGeoObjectLabel() + " in the ratio " + this.print());
			}
		}
		
		OpenGeoProver.settings.getLogger().error("Unexpected form of fraction : " + this.print() +
				"where the point to eliminate is " + pt.getGeoObjectLabel());
		return null;
	}
	
	@Override
	public AMExpression reduceToSingleFraction() {
		return this;
	}
	
	@Override
	public AMExpression reductToRightAssociativeForm() {
		return this;
	}
	
	@Override
	public AMExpression toIndependantVariables() {
		// TODO write the transformation to independant variables for the ratios
		System.out.println("Not yet implemented");
		return null;
	}
	
	@Override
	public int size() {
		return 1;
	}
}
