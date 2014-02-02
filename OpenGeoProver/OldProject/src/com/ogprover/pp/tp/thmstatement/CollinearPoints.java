/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.thmstatement;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.pp.tp.auxiliary.PointSetRelationshipManager;
import com.ogprover.pp.tp.expressions.AreaOfTriangle;
import com.ogprover.pp.tp.expressions.AMExpression;
import com.ogprover.pp.tp.geoconstruction.GeoConstruction;
import com.ogprover.pp.tp.geoconstruction.Line;
import com.ogprover.pp.tp.geoconstruction.LineThroughTwoPoints;
import com.ogprover.pp.tp.geoconstruction.Point;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for statement about n collinear points (n>=3)</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class CollinearPoints extends PositionThmStatement {
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
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method.
	 * 
	 * @param pointList	List of points that make the statement about collinear points.
	 */
	public CollinearPoints(ArrayList<Point> pointList) {
		if (pointList == null || pointList.size() < 3) {
			OpenGeoProver.settings.getLogger().error("There should be at least three points for statement about collinear points.");
			return;
		}
		
		this.geoObjects = new Vector<GeoConstruction>();
		for (Point p : pointList)
			this.geoObjects.add(p);
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * @see com.ogprover.pp.tp.thmstatement.ThmStatement#getAlgebraicForm()
	 */
	@Override
	public XPolynomial getAlgebraicForm() {
		// Initializations of polynomials used for calculation of 
		// algebraic form of theorem statement
		XPolynomial tempCond = null;
		XPolynomial tempAddend = null;
		XPolynomial bestCondition = null;
		int degreeOfBestCondition = 0;
		
		/*
		 *  First of all pass all lines from theorem protocol
		 *  that contain at least two points from list and for all 
		 *  other points generate the condition to belong to one of 
		 *  those lines. 
		 */
		for (GeoConstruction geoCons : this.consProtocol.getConstructionSteps()) {
			if (!(geoCons instanceof Line))
				continue;
			
			Line l = (Line)geoCons;
			Vector<Point> lPoints = l.getPoints();
			ArrayList<Point> pointsFromLine = new ArrayList<Point>();
			PointSetRelationshipManager manager = null;
			tempCond = new XPolynomial();
			
			// Search for points from this statement in collection of points from current line
			for (GeoConstruction gc : this.geoObjects) {
				Point p = (Point)gc; // All objects of this statement are points or validation would fail
				
				if (lPoints.indexOf(p) >= 0)
					pointsFromLine.add(p);
			}
			
			// Process all points if this line contains at least two points from statement
			if (pointsFromLine.size() >= 2) {
				int numPointsLeft = this.geoObjects.size() - pointsFromLine.size(); // how many points form statement are not on line l by construction
				
				// instantiate conditions for points that are not on line l
				for (GeoConstruction gc : this.geoObjects) {
					Point p = (Point)gc; // All objects of statement are points or validation would fail
					
					if (pointsFromLine.indexOf(p) >= 0) // point p is on line by construction - skip it
						continue;
					
					manager = new PointSetRelationshipManager(l, p, PointSetRelationshipManager.MANAGER_TYPE_STATEMENT);
					tempAddend = manager.retrieveInstantiatedCondition(); // condition for p to belong to l
					
					// if only one point has been left, just add its condition
					// to temporary result and stop further search
					if (numPointsLeft == 1) {
						tempCond.addPolynomial(tempAddend);
						break;
					}
					
					// since we need conjunction of conditions for more then one point
					// we add squares of polynomials
					tempCond.addPolynomial(tempAddend.clone().multiplyByPolynomial(tempAddend));
					numPointsLeft--;
					
					// break the loop if no more points left
					if (numPointsLeft == 0)
						break;
				}
				
				// Now check whether new condition for collinear points is better 
				// polynomial then current best one i.e. if it has less degree or 
				// less number of terms
				int tempDegree = tempCond.getPolynomialDegree();
				if (bestCondition == null ||
					tempDegree < degreeOfBestCondition ||
				    (tempDegree == degreeOfBestCondition &&
					 tempCond.getTerms().size() < bestCondition.getTerms().size())) {
					bestCondition = tempCond;
					degreeOfBestCondition = tempDegree;
				}
			}
		}
		
		/*
		 * Now take a pair of these points and generate the condition for other
		 * points to belong to line through two chosen points. We distinguish
		 * pairs (A, B) and (B, A).
		 */
		boolean isOnePointLeft = (this.geoObjects.size() == 3); // when 2 points are chosen, one is left iff there are exactly 3 points in statement
		for (int ii1 = 0, jj1 = this.geoObjects.size(); ii1 < jj1; ii1++) {
			Point p1 = (Point)this.geoObjects.get(ii1);
			
			for (int ii2 = 0, jj2 = this.geoObjects.size(); ii2 < jj2; ii2++) {
				Point p2 = (Point)this.geoObjects.get(ii2);
				
				if (p2.getGeoObjectLabel().equals(p1.getGeoObjectLabel()))
					continue; // skip point p1 since we search for two different points
				
				Line l = new LineThroughTwoPoints("tempLine", p1, p2);
				tempCond = new XPolynomial();
				
				// instantiate conditions for all other points
				for (int ii = 0, jj = this.geoObjects.size(); ii < jj; ii++) {
					Point p = (Point)this.geoObjects.get(ii);
					
					if (p.getGeoObjectLabel().equals(p1.getGeoObjectLabel()) ||
					    p.getGeoObjectLabel().equals(p2.getGeoObjectLabel()))
						continue;
					
					tempAddend = l.instantiateConditionFromBasicElements(p); // condition for p to belong to line through p1 and p2
					
					// if only one point has been left, add the condition and stop further search
					if (isOnePointLeft) {
						tempCond.addPolynomial(tempAddend);
						break;
					}
					
					// if there is more the one point we add squares of conditions
					tempCond.addPolynomial(tempAddend.clone().multiplyByPolynomial(tempAddend));
				}
					
				// Now check whether new condition is better polynomial then current best one
				// i.e. if it has less degree or less number of terms
				int tempDegree = tempCond.getPolynomialDegree();
				if (bestCondition == null ||
					tempDegree < degreeOfBestCondition ||
					(tempDegree == degreeOfBestCondition &&
				     tempCond.getTerms().size() < bestCondition.getTerms().size())) {
					bestCondition = tempCond;
					degreeOfBestCondition = tempDegree;
				}
			}
		}
		
		// return algebraic form of theorem statement
		return bestCondition;
	}
	
	/**
	 * @see com.ogprover.pp.tp.thmstatement.ElementaryThmStatement#isValid()
	 */
	@Override
	public boolean isValid() {
		// First of all call method from superclass
		if (!super.isValid())
			return false;
		
		// there have to be at least three points
		if (this.geoObjects.size() < 3) {
			OpenGeoProver.settings.getLogger().error("There should be at least three points.");
			return false;
		}
		
		return true;
	}

	/**
	 * @see com.ogprover.pp.tp.thmstatement.ThmStatement#getStatementDesc()
	 */
	@Override
	public String getStatementDesc() {
		StringBuilder sb = new StringBuilder();
		sb.append("Points ");
		boolean bFirst = true;
		for (GeoConstruction geoCons : this.geoObjects) {
			if (!bFirst)
				sb.append(", ");
			else
				bFirst = false;
			sb.append(geoCons.getGeoObjectLabel());
		}
		sb.append(" are collinear");
		return sb.toString();
	}


	@Override
	public AreaMethodTheoremStatement getAreaMethodStatement() {
		/*
		 * Three points are collinear iff the area of the corresponding triangle is zero.
		 * So, we take the first two points and for every other point, we add the corresponding statement.
		 */
		if (this.geoObjects.size() < 3) {
			OpenGeoProver.settings.getLogger().error("There should be at least three points.");
			return null;
		}
		
		Vector<AMExpression> statements = new Vector<AMExpression>();
		
		// If one of the cast fails then the validation will fail
		Point pt1 = (Point)this.geoObjects.get(0);
		Point pt2 = (Point)this.geoObjects.get(1);
		List<GeoConstruction> otherPoints = this.geoObjects.subList(2,this.geoObjects.size());
		for (GeoConstruction geoCons : otherPoints) {
			Point pt = (Point)geoCons;
			AMExpression expr = new AreaOfTriangle(pt1, pt2, pt);
			statements.add(expr);
		}
		
		return new AreaMethodTheoremStatement(this.getStatementDesc(), statements);
	}
}
