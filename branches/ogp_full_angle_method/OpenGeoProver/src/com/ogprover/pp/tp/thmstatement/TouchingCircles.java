/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.thmstatement;

import java.util.Vector;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.pp.tp.geoconstruction.Circle;
import com.ogprover.pp.tp.geoconstruction.CircleWithCenterAndRadius;
import com.ogprover.pp.tp.geoconstruction.GeoConstruction;
import com.ogprover.pp.tp.geoconstruction.Point;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for statement about touching circles.</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class TouchingCircles extends PositionThmStatement {
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
	 * @param firstCircle	First circle
	 * @param secondCircle	Second circle
	 */
	public TouchingCircles(Circle firstCircle, Circle secondCircle) {
		this.geoObjects = new Vector<GeoConstruction>();
		this.geoObjects.add(firstCircle);
		this.geoObjects.add(secondCircle);
	}
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * @see com.ogprover.pp.tp.thmstatement.ElementaryThmStatement#isValid()
	 */
	@Override
	public boolean isValid() {
		// First of all call method from superclass
		if (!super.isValid())
			return false;
		
		if (this.geoObjects.size() < 2) {
			OpenGeoProver.settings.getLogger().error("There should be two circles.");
			return false;
		}
		
		// each circle must have center and defined radius
		Circle firstCircle = (Circle) this.geoObjects.get(0);
		Circle secondCircle = (Circle) this.geoObjects.get(1);
		
		if (firstCircle == null || secondCircle == null) {
			OpenGeoProver.settings.getLogger().error("Some circle is null.");
			return false;
		}
		
		if (firstCircle.getCenter() == null || secondCircle.getCenter() == null) {
			OpenGeoProver.settings.getLogger().error("Each circle must have constructed center.");
			return false;
		}
		
		if ((firstCircle.getPoints().size() == 0 && !(firstCircle instanceof CircleWithCenterAndRadius)) ||
			(secondCircle.getPoints().size() == 0 && !(secondCircle instanceof CircleWithCenterAndRadius)) ) {
			OpenGeoProver.settings.getLogger().error("Each circle must have defined radius.");
			return false;
		}
		
		return true;
	}
	
	/**
	 * @see com.ogprover.pp.tp.thmstatement.ThmStatement#getAlgebraicForm()
	 */
	@Override
	public XPolynomial getAlgebraicForm() {
		// Initializations of polynomials used for calculation of 
		// algebraic form of theorem statement
		XPolynomial bestCondition = null;
		int degreeOfBestCondition = 0;
		XPolynomial currCondition = null;
		int degreeOfCurrCondition = 0;
		
		Circle firstCircle = (Circle) this.geoObjects.get(0);
		Circle secondCircle = (Circle) this.geoObjects.get(1);
		Point O1 = firstCircle.getCenter();
		Point O2 = secondCircle.getCenter();
		AlgebraicSumOfThreeSegments segSum = null;
		
		/*
		 * Two circles k1(O1, r1) and k2(O2, r2) are touching each other 
		 * and one is inside interior of another iff O1O2 = |r1 - r2|.
		 * They are touching each other and each is in exterior of another
		 * iff O1O2 = r1 + r2.
		 * 
		 * Generally speaking, two circles are touching each other iff
		 * algebraic sum of segments O1O2, r1 and r2 equals zero.
		 */
		
		// All variants of radii of both circles
		if (firstCircle instanceof CircleWithCenterAndRadius) {
			Point A1 = ((CircleWithCenterAndRadius)firstCircle).getRadius().getFirstEndPoint();
			Point B1 = ((CircleWithCenterAndRadius)firstCircle).getRadius().getSecondEndPoint();
			
			if (secondCircle instanceof CircleWithCenterAndRadius) {
				Point A2 = ((CircleWithCenterAndRadius)secondCircle).getRadius().getFirstEndPoint();
				Point B2 = ((CircleWithCenterAndRadius)secondCircle).getRadius().getSecondEndPoint();
				
				segSum = new AlgebraicSumOfThreeSegments(O1, O2, A1, B1, A2, B2);
				currCondition = segSum.getAlgebraicForm();
				degreeOfCurrCondition = currCondition.getPolynomialDegree();
				
				// Now check whether new condition for touching circles is better 
				// polynomial then current best one i.e. if it has less degree or 
				// less number of terms
				if (bestCondition == null ||
					degreeOfCurrCondition < degreeOfBestCondition ||
					(degreeOfCurrCondition == degreeOfBestCondition &&
					 currCondition.getTerms().size() < bestCondition.getTerms().size())) {
					bestCondition = currCondition;
					degreeOfBestCondition = degreeOfCurrCondition;
				}
			}
			
			for (int ii2 = 0, jj2 = secondCircle.getPoints().size(); ii2 < jj2; ii2++) {
				segSum = new AlgebraicSumOfThreeSegments(O1, O2, A1, B1, O2, secondCircle.getPoints().get(ii2));
				currCondition = segSum.getAlgebraicForm();
				degreeOfCurrCondition = currCondition.getPolynomialDegree();
				
				// Now check whether new condition for touching circles is better 
				// polynomial then current best one i.e. if it has less degree or 
				// less number of terms
				if (bestCondition == null ||
					degreeOfCurrCondition < degreeOfBestCondition ||
					(degreeOfCurrCondition == degreeOfBestCondition &&
					 currCondition.getTerms().size() < bestCondition.getTerms().size())) {
					bestCondition = currCondition;
					degreeOfBestCondition = degreeOfCurrCondition;
				}
			}
		}
		
		for (int ii1 = 0, jj1 = firstCircle.getPoints().size(); ii1 < jj1; ii1++) {
			Point B1 = firstCircle.getPoints().get(ii1);
			
			if (secondCircle instanceof CircleWithCenterAndRadius) {
				Point A2 = ((CircleWithCenterAndRadius)secondCircle).getRadius().getFirstEndPoint();
				Point B2 = ((CircleWithCenterAndRadius)secondCircle).getRadius().getSecondEndPoint();
				
				segSum = new AlgebraicSumOfThreeSegments(O1, O2, O1, B1, A2, B2);
				currCondition = segSum.getAlgebraicForm();
				degreeOfCurrCondition = currCondition.getPolynomialDegree();
				
				// Now check whether new condition for touching circles is better 
				// polynomial then current best one i.e. if it has less degree or 
				// less number of terms
				if (bestCondition == null ||
					degreeOfCurrCondition < degreeOfBestCondition ||
					(degreeOfCurrCondition == degreeOfBestCondition &&
					 currCondition.getTerms().size() < bestCondition.getTerms().size())) {
					bestCondition = currCondition;
					degreeOfBestCondition = degreeOfCurrCondition;
				}
			}
			
			for (int ii2 = 0, jj2 = secondCircle.getPoints().size(); ii2 < jj2; ii2++) {
				segSum = new AlgebraicSumOfThreeSegments(O1, O2, O1, B1, O2, secondCircle.getPoints().get(ii2));
				currCondition = segSum.getAlgebraicForm();
				degreeOfCurrCondition = currCondition.getPolynomialDegree();
				
				// Now check whether new condition for touching circles is better 
				// polynomial then current best one i.e. if it has less degree or 
				// less number of terms
				if (bestCondition == null ||
					degreeOfCurrCondition < degreeOfBestCondition ||
					(degreeOfCurrCondition == degreeOfBestCondition &&
					 currCondition.getTerms().size() < bestCondition.getTerms().size())) {
					bestCondition = currCondition;
					degreeOfBestCondition = degreeOfCurrCondition;
				}
			}
		}
		
		// Put best found polynomial to theorem statement's algebraic form
		return bestCondition;
	}

	/**
	 * @see com.ogprover.pp.tp.thmstatement.ThmStatement#getStatementDesc()
	 */
	@Override
	public String getStatementDesc() {
		StringBuilder sb = new StringBuilder();
		sb.append("Circles ");
		sb.append(this.geoObjects.get(0).getGeoObjectLabel());
		sb.append(" and ");
		sb.append(this.geoObjects.get(1).getGeoObjectLabel());
		sb.append(" are touching each other");
		return sb.toString();
	}




	@Override
	public AreaMethodTheoremStatement getAreaMethodStatement() {
		OpenGeoProver.settings.getLogger().error("Area method cannot deal with such statement for now.");
		return null;
	}
}
