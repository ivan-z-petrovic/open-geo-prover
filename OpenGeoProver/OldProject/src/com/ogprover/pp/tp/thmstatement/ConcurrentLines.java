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
import com.ogprover.pp.tp.expressions.AreaOfTriangle;
import com.ogprover.pp.tp.expressions.Difference;
import com.ogprover.pp.tp.expressions.AMExpression;
import com.ogprover.pp.tp.expressions.Product;
import com.ogprover.pp.tp.geoconstruction.GeoConstruction;
import com.ogprover.pp.tp.geoconstruction.IntersectionPoint;
import com.ogprover.pp.tp.geoconstruction.Line;
import com.ogprover.pp.tp.geoconstruction.Point;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for statement about n concurrent lines (n>=3)</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class ConcurrentLines extends PositionThmStatement {
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
	 * @param lineList	List of lines that make the statement about concurrent lines.
	 */
	public ConcurrentLines(ArrayList<Line> lineList) {
		if (lineList == null || lineList.size() < 3) {
			OpenGeoProver.settings.getLogger().error("There should be at least three lines for statement about concurrent lines.");
			return;
		}
		
		this.geoObjects = new Vector<GeoConstruction>();
		for (Line l : lineList)
			this.geoObjects.add(l);
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
		 *  that belong to at least two lines from list and for all 
		 *  other lines generate the condition about those points to 
		 *  belong to all these lines. 
		 */
		for (GeoConstruction geoCons : this.consProtocol.getConstructionSteps()) {
			if (!(geoCons instanceof Point))
				continue;
			
			Point P = (Point)geoCons;
			Vector<Line> linesHavingP = new Vector<Line>();
			PointSetRelationshipManager manager = null;
			tempCond = new XPolynomial();
			
			// Search for lines from this statement that contain point P
			for (GeoConstruction gc : this.geoObjects) {
				Line l = (Line)gc; // All objects of this statement are lines or validation would fail
				
				if (l.getPoints().indexOf(P) >= 0)
					linesHavingP.add(l);
			}
			
			// Process all lines if this point belongs to at least two lines from statement
			if (linesHavingP.size() >= 2) {
				int numLinesLeft = this.geoObjects.size() - linesHavingP.size(); // how many lines form statement don't contain P by construction
				
				// instantiate conditions for lines that don't contain P
				for (GeoConstruction gc : this.geoObjects) {
					Line l = (Line)gc; // All objects of statement are lines or validation would fail
					
					if (linesHavingP.indexOf(l) >= 0) // line l contains p by construction - skip it
						continue;
					
					manager = new PointSetRelationshipManager(l, P, PointSetRelationshipManager.MANAGER_TYPE_STATEMENT);
					tempAddend = manager.retrieveInstantiatedCondition(); // condition for p to belong to l
					
					// if only one line has been left, just add its condition
					// to temporary result and stop further search
					if (numLinesLeft == 1) {
						tempCond.addPolynomial(tempAddend);
						break;
					}
					
					// since we need conjunction of conditions for more then one line
					// we add squares of polynomials
					tempCond.addPolynomial(tempAddend.clone().multiplyByPolynomial(tempAddend));
					numLinesLeft--;
					
					// break the loop if no more lines left
					if (numLinesLeft == 0)
						break;
				}
				
				// Now check whether new condition for concurrent lines is better 
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
		 * Now take a pair of these lines and generate their intersection
		 * and instantiate condition for other lines to contain intersection point.
		 */
		boolean isOneLineLeft = (this.geoObjects.size() == 3); // when 2 lines are chosen, one is left iff there are exactly 3 lines in statement
		PointSetRelationshipManager manager = null;
		
		for (int ii1 = 0, jj1 = this.geoObjects.size(); ii1 < jj1; ii1++) {
			Line l1 = (Line)this.geoObjects.get(ii1);
			
			for (int ii2 = 0, jj2 = this.geoObjects.size(); ii2 < jj2; ii2++) {
				Line l2 = (Line)this.geoObjects.get(ii2);
				
				if (l2.getGeoObjectLabel().equals(l1.getGeoObjectLabel()))
					continue; // skip line l1 since we search for two different lines
				
				tempCond = new XPolynomial();
				
				// temporarily add new intersection point to CP
				int numOfPolynomialsInSystem = this.consProtocol.getAlgebraicGeoTheorem().getHypotheses().getPolynomials().size();
				IntersectionPoint P = new IntersectionPoint("intersectPoint-" + l1.getGeoObjectLabel() + "." + l2.getGeoObjectLabel(), l1, l2);
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
				
				// instantiate conditions for all other lines
				for (int ii = 0, jj = this.geoObjects.size(); ii < jj; ii++) {
					Line l = (Line)this.geoObjects.get(ii);
					
					if (l.getGeoObjectLabel().equals(l1.getGeoObjectLabel()) ||
					    l.getGeoObjectLabel().equals(l2.getGeoObjectLabel()))
						continue;
					
					manager = new PointSetRelationshipManager(l, P, PointSetRelationshipManager.MANAGER_TYPE_STATEMENT);
					XPolynomial instantiatedCondition = manager.retrieveInstantiatedCondition();
					
					if (instantiatedCondition == null) {
						OpenGeoProver.settings.getLogger().error("Failed to retrieve the condition for point " + P.getGeoObjectLabel() + " to belong to line " + l.getGeoObjectLabel());
						return null;
					}
					
					tempAddend = instantiatedCondition; // condition for P to belong to line l
					
					// if only one line has been left, add the condition and stop further search
					if (isOneLineLeft) {
						tempCond.addPolynomial(tempAddend);
						break;
					}
					
					// if there is more the one line we add squares of conditions
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
				
				// now remove point P from CP and lines and prepare CP for instantiation of new
				// intersection point
				this.consProtocol.getConstructionSteps().remove(P.getIndex());
				l1.getPoints().remove(P);
				l2.getPoints().remove(P);
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
			Line l1 = (Line)bestIntersectionPoint.getFirstPointSet();
			Line l2 = (Line)bestIntersectionPoint.getSecondPointSet();
			Point P = new IntersectionPoint("intersectPoint-" + l1.getGeoObjectLabel() + "." + l2.getGeoObjectLabel(), l1, l2);
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
		
		// there have to be at least three lines
		if (this.geoObjects.size() < 3) {
			OpenGeoProver.settings.getLogger().error("There should be at least three lines.");
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
		sb.append("Lines ");
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
		Vector<GeoConstruction> lines = this.getGeoObjects();
		
		Line line1 = (Line) lines.get(0);
		Line line2 = (Line) lines.get(1);
		
		Point a = line1.getPoints().get(0);
		Point b = line1.getPoints().get(1);
		Point c = line2.getPoints().get(0);
		Point d = line2.getPoints().get(1);
		
		Vector<AMExpression> statements = new Vector<AMExpression>();
		
		for (int i = 2 ; i<lines.size(); i++) {
			Point e = ((Line)lines.get(i)).getPoints().get(0);
			Point f = ((Line)lines.get(i)).getPoints().get(1);
			/*
			 * Let I be the intersection point between the lines (ab) and (cd).
			 * The three lines are concurrent iff I, e and f are collinear, so
			 *   iff S_efI = 0.
			 * Considering we cannot add new points to the construction at this step
			 *   of the algorithm, we eliminate the point I by hand. It gives us the
			 *   formula (S_ACD*S_EFB - S_BCD*S_EFA)/S_ABCD = 0, and we can simplify
			 *   by S_ABCD.
			 */
			AMExpression sacd = new AreaOfTriangle(a, c, d);
			AMExpression sefb = new AreaOfTriangle(e, f, b);
			AMExpression term1 = new Product(sacd, sefb);
			AMExpression sbcd = new AreaOfTriangle(b, c, d);
			AMExpression sefa = new AreaOfTriangle(e, f, a);
			AMExpression term2 = new Product(sbcd, sefa);
			statements.add(new Difference(term1, term2));
		}
		
	return new AreaMethodTheoremStatement(getStatementDesc(), statements);
	}
}
