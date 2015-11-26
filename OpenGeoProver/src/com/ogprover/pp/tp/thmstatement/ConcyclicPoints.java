/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.thmstatement;

import java.util.ArrayList;
import java.util.Vector;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.pp.tp.auxiliary.PointSetRelationshipManager;
import com.ogprover.pp.tp.expressions.AreaOfTriangle;
import com.ogprover.pp.tp.expressions.Difference;
import com.ogprover.pp.tp.expressions.AMExpression;
import com.ogprover.pp.tp.expressions.Product;
import com.ogprover.pp.tp.expressions.PythagorasDifference;
import com.ogprover.pp.tp.geoconstruction.Circle;
import com.ogprover.pp.tp.geoconstruction.CircumscribedCircle;
import com.ogprover.pp.tp.geoconstruction.GeoConstruction;
import com.ogprover.pp.tp.geoconstruction.Point;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for statement about n concyclic points (n>=4)</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class ConcyclicPoints extends PositionThmStatement {
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
	 * @param pointList	List of points that make the statement about concyclic points.
	 */
	public ConcyclicPoints(ArrayList<Point> pointList) {
		if (pointList == null || pointList.size() < 4) {
			OpenGeoProver.settings.getLogger().error("There should be at least four points for statement about concyclic points.");
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
		 *  First of all pass all circles from theorem protocol
		 *  that contain at least three points from list and for all 
		 *  other points generate the condition to belong to one of 
		 *  those circles. 
		 */
		for (GeoConstruction geoCons : this.consProtocol.getConstructionSteps()) {
			if (!(geoCons instanceof Circle))
				continue;
			
			Circle c = (Circle)geoCons;
			Vector<Point> cPoints = c.getPoints();
			ArrayList<Point> pointsFromCircle = new ArrayList<Point>();
			PointSetRelationshipManager manager = null;
			tempCond = new XPolynomial();
			
			// Search for points from this statement in collection of points from current circle
			for (GeoConstruction gc : this.geoObjects) {
				Point p = (Point)gc; // All objects of this statement are points or validation would fail
				
				if (cPoints.indexOf(p) >= 0)
					pointsFromCircle.add(p);
			}
			
			// Process all points if this circle contains at least three points from statement
			if (pointsFromCircle.size() >= 3) {
				int numPointsLeft = this.geoObjects.size() - pointsFromCircle.size(); // how many points form statement are not on circle c by construction
				
				// instantiate conditions for points that are not on circle c
				for (GeoConstruction gc : this.geoObjects) {
					Point p = (Point)gc; // All objects of statement are points or validation would fail
					
					if (pointsFromCircle.indexOf(p) >= 0) // point p is on circle by construction - skip it
						continue;
					
					manager = new PointSetRelationshipManager(c, p, PointSetRelationshipManager.MANAGER_TYPE_STATEMENT);
					tempAddend = manager.retrieveInstantiatedCondition(); // condition for p to belong to c
					
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
				
				// Now check whether new condition for concyclic points is better 
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
		 * Now take a triple of these points and generate the condition for other
		 * points to belong to circle through three chosen points. We distinguish
		 * the order of points inside triples.
		 */
		boolean isOnePointLeft = (this.geoObjects.size() == 4); // when 3 points are chosen, one is left iff there are exactly 4 points in statement
		for (int ii1 = 0, jj1 = this.geoObjects.size(); ii1 < jj1; ii1++) {
			Point p1 = (Point)this.geoObjects.get(ii1);
			
			for (int ii2 = 0, jj2 = this.geoObjects.size(); ii2 < jj2; ii2++) {
				Point p2 = (Point)this.geoObjects.get(ii2);
				
				if (p2.getGeoObjectLabel().equals(p1.getGeoObjectLabel()))
					continue; // skip point p1 since we search for three different points
				
				for (int ii3 = 0, jj3 = this.geoObjects.size(); ii3 < jj3; ii3++) {
					Point p3 = (Point)this.geoObjects.get(ii3);
					
					if (p3.getGeoObjectLabel().equals(p1.getGeoObjectLabel()) || p3.getGeoObjectLabel().equals(p2.getGeoObjectLabel()))
						continue; // skip points p1 and p2 since we search for three different points
					
					Circle c = new CircumscribedCircle("tempCircle", p1, p2, p3);
					tempCond = new XPolynomial();
				
					// instantiate conditions for all other points
					for (int ii = 0, jj = this.geoObjects.size(); ii < jj; ii++) {
						Point p = (Point)this.geoObjects.get(ii);
					
						if (p.getGeoObjectLabel().equals(p1.getGeoObjectLabel()) ||
							p.getGeoObjectLabel().equals(p2.getGeoObjectLabel()) ||
							p.getGeoObjectLabel().equals(p3.getGeoObjectLabel()))
							continue;
					
						tempAddend = c.instantiateConditionFromBasicElements(p); // condition for p to belong to circle through p1, p2 and p3
					
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
		}
		
		// set algebraic form of theorem statement
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
		
		// there have to be at least four points
		if (this.geoObjects.size() < 4) {
			OpenGeoProver.settings.getLogger().error("There should be at least four points.");
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
		sb.append(" are concyclic");
		return sb.toString();
	}



	@Override
	public AreaMethodTheoremStatement getAreaMethodStatement() {
		/*
		 * Let S_ABC be the area of the triangle ABC and P_ABC be defined as P_ABC = AB²+BC²-AC².
		 * 
		 * Then, A and B belong to the same circle arc CD iff S_CAD*P_CBD = S_CBD*P_CAD.
		 */
		Vector<GeoConstruction> pointList = this.geoObjects;
		Vector<AMExpression> statements = new Vector<AMExpression>();
		
		Point a = (Point)pointList.get(0);
		Point b = (Point)pointList.get(1);
		for (int i=4 ; i<pointList.size() ; i++) {
			Point c = (Point)pointList.get(i-1);
			Point d = (Point)pointList.get(i);
			
			AMExpression scad = new AreaOfTriangle(c,a,d);
			AMExpression scbd = new AreaOfTriangle(c,b,d);
			AMExpression pcad = new PythagorasDifference(c,a,d);
			AMExpression pcbd = new PythagorasDifference(c,b,d);
			AMExpression product1 = new Product(scad, pcbd);
			AMExpression product2 = new Product(scbd, pcad);
			AMExpression difference = new Difference(product1, product2);
			
			statements.add(difference);
		}
		return new AreaMethodTheoremStatement(getStatementDesc(), statements);
	}
}
