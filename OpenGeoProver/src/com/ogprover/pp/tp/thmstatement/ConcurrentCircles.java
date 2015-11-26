/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.thmstatement;

import java.util.ArrayList;
import java.util.Vector;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.Variable;
import com.ogprover.polynomials.XPolySystem;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.pp.tp.auxiliary.PointSetRelationshipManager;
import com.ogprover.pp.tp.geoconstruction.Circle;
import com.ogprover.pp.tp.geoconstruction.GeoConstruction;
import com.ogprover.pp.tp.geoconstruction.IntersectionPoint;
import com.ogprover.pp.tp.geoconstruction.Point;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for statement about n concurrent circles (n>=3)</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
/**
 * @author ipetrov
 *
 */
public class ConcurrentCircles extends PositionThmStatement {
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
	 * @param circleList	List of circles that make the statement about concurrent circles.
	 */
	public ConcurrentCircles(ArrayList<Circle> circleList) {
		if (circleList == null || circleList.size() < 3) {
			OpenGeoProver.settings.getLogger().error("There should be at least three circles for statement about concurrent circles.");
			return;
		}
		
		this.geoObjects = new Vector<GeoConstruction>();
		for (Circle c : circleList)
			this.geoObjects.add(c);
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
		IntersectionPoint bestIntersectionPoint = null;
		
		/*
		 *  First of all pass all points from theorem protocol
		 *  that belong to at least two circles from list and for all 
		 *  other circles generate the condition about those points to 
		 *  belong to all these circles. 
		 */
		for (GeoConstruction geoCons : this.consProtocol.getConstructionSteps()) {
			if (!(geoCons instanceof Point))
				continue;
			
			Point P = (Point)geoCons;
			Vector<Circle> circlesHavingP = new Vector<Circle>();
			PointSetRelationshipManager manager = null;
			tempCond = new XPolynomial();
			
			// Search for circles from this statement that contain point P
			for (GeoConstruction gc : this.geoObjects) {
				Circle c = (Circle)gc; // All objects of this statement are circles or validation would fail
				
				if (c.getPoints().indexOf(P) >= 0)
					circlesHavingP.add(c);
			}
			
			// Process all circles if this point belongs to at least two circles from statement
			if (circlesHavingP.size() >= 2) {
				int numCirclesLeft = this.geoObjects.size() - circlesHavingP.size(); // how many circles form statement don't contain P by construction
				
				// instantiate conditions for circles that don't contain P
				for (GeoConstruction gc : this.geoObjects) {
					Circle c = (Circle)gc; // All objects of statement are circles or validation would fail
					
					if (circlesHavingP.indexOf(c) >= 0) // circle c contains p by construction - skip it
						continue;
					
					manager = new PointSetRelationshipManager(c, P, PointSetRelationshipManager.MANAGER_TYPE_STATEMENT);
					tempAddend = manager.retrieveInstantiatedCondition(); // condition for p to belong to c
					
					// if only one circle has been left, just add its condition
					// to temporary result and stop further search
					if (numCirclesLeft == 1) {
						tempCond.addPolynomial(tempAddend);
						break;
					}
					
					// since we need conjunction of conditions for more then one circle
					// we add squares of polynomials
					tempCond.addPolynomial(tempAddend.clone().multiplyByPolynomial(tempAddend));
					numCirclesLeft--;
					
					// break the loop if no more circles left
					if (numCirclesLeft == 0)
						break;
				}
				
				// Now check whether new condition for concurrent circles is better 
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
		 * Now take a pair of these circles and generate their intersection
		 * and instantiate condition for other circles to contain intersection point.
		 */
		boolean isOneCircleLeft = (this.geoObjects.size() == 3); // when 2 circles are chosen, one is left iff there are exactly 3 circles in statement
		PointSetRelationshipManager manager = null;
		
		for (int ii1 = 0, jj1 = this.geoObjects.size(); ii1 < jj1; ii1++) {
			Circle c1 = (Circle)this.geoObjects.get(ii1);
			
			for (int ii2 = 0, jj2 = this.geoObjects.size(); ii2 < jj2; ii2++) {
				Circle c2 = (Circle)this.geoObjects.get(ii2);
				
				if (c2.getGeoObjectLabel().equals(c1.getGeoObjectLabel()))
					continue; // skip circle c1 since we search for two different circles
				
				tempCond = new XPolynomial();
				
				// temporarily add new intersection point to CP
				int numOfPolynomialsInSystem = this.consProtocol.getAlgebraicGeoTheorem().getHypotheses().getPolynomials().size();
				IntersectionPoint P = new IntersectionPoint("intersectPoint-" + c1.getGeoObjectLabel() + "." + c2.getGeoObjectLabel(), c1, c2);
				this.consProtocol.addGeoConstruction(P); // add to the end of CP
				if (P.isValidConstructionStep() == false) {
					OpenGeoProver.settings.getLogger().error("Failed to validate the construction of intersection point " + P.getGeoObjectLabel());
					return null;
				}
				P.transformToAlgebraicFormWithOutputPrintFlag(false);
				int numOfDependentCoordinates = 0;
				
				if (P.getX().getVariableType() == Variable.VAR_TYPE_UX_X)
					numOfDependentCoordinates++;
				if (P.getY().getVariableType() == Variable.VAR_TYPE_UX_X)
					numOfDependentCoordinates++;
				
				// instantiate conditions for all other circles
				for (int ii = 0, jj = this.geoObjects.size(); ii < jj; ii++) {
					Circle c = (Circle)this.geoObjects.get(ii);
					
					if (c.getGeoObjectLabel().equals(c1.getGeoObjectLabel()) ||
					    c.getGeoObjectLabel().equals(c2.getGeoObjectLabel()))
						continue;
					
					manager = new PointSetRelationshipManager(c, P, PointSetRelationshipManager.MANAGER_TYPE_STATEMENT);
					XPolynomial instantiatedCondition = manager.retrieveInstantiatedCondition();
					
					if (instantiatedCondition == null) {
						OpenGeoProver.settings.getLogger().error("Failed to retrieve the condition for point " + P.getGeoObjectLabel() + " to belong to circle " + c.getGeoObjectLabel());
						return null;
					}
					
					tempAddend = instantiatedCondition; // condition for P to belong to circle c
					
					// if only one circle has been left, add the condition and stop further search
					if (isOneCircleLeft) {
						tempCond.addPolynomial(tempAddend);
						break;
					}
					
					// if there is more the one circle we add squares of conditions
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
					// save best point
					bestIntersectionPoint = P;
				}
				
				// now remove point P from CP and prepare CP for instantiation of new
				// intersection point
				this.consProtocol.getConstructionSteps().remove(P.getIndex());
				XPolySystem system = this.consProtocol.getAlgebraicGeoTheorem().getHypotheses();
				for (int ii = 0, jj = system.getPolynomials().size() - numOfPolynomialsInSystem; ii < jj; ii++)
					system.removePoly(system.getPolynomials().size() - 1); // remove last polynomial
				for (int ii = 0; ii < numOfDependentCoordinates; ii++)
					this.consProtocol.decrementXIndex();
			}
		}
		
		// put best point into CP
		if (bestIntersectionPoint != null) {
			// reconstruct best intersection point - we must do like this sine previous point
			// has eventually renamed its coordinates during transformation to algebraic form
			Circle c1 = (Circle)bestIntersectionPoint.getFirstPointSet();
			Circle c2 = (Circle)bestIntersectionPoint.getSecondPointSet();
			Point P = new IntersectionPoint("intersectPoint-" + c1.getGeoObjectLabel() + "." + c2.getGeoObjectLabel(), c1, c2);
			this.consProtocol.addGeoConstruction(P);
			
			if (P.isValidConstructionStep() == false) {
				OpenGeoProver.settings.getLogger().error("Failed to validate the construction of intersection point " + bestIntersectionPoint.getGeoObjectLabel());
				return null;
			}
			
			// transform that construction into algebraic form
			P.transformToAlgebraicForm();
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
		
		// there have to be at least three circles
		if (this.geoObjects.size() < 3) {
			OpenGeoProver.settings.getLogger().error("There should be at least three circles.");
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
		sb.append("Circles ");
		boolean bFirst = true;
		for (GeoConstruction geoCons : this.geoObjects) {
			if (!bFirst)
				sb.append(", ");
			else
				bFirst = false;
			sb.append(geoCons.getGeoObjectLabel());
		}
		sb.append(" are concurrent");
		return sb.toString();
	}



	@Override
	public AreaMethodTheoremStatement getAreaMethodStatement() {
		OpenGeoProver.settings.getLogger().error("Statement not currently supported by the area method.");
		return null;
	}
}
