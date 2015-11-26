/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.thmstatement;

import java.util.Vector;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.pp.tp.auxiliary.PointSetRelationshipManager;
import com.ogprover.pp.tp.expressions.Difference;
import com.ogprover.pp.tp.expressions.AMExpression;
import com.ogprover.pp.tp.expressions.PythagorasDifference;
import com.ogprover.pp.tp.geoconstruction.GeoConstruction;
import com.ogprover.pp.tp.geoconstruction.Line;
import com.ogprover.pp.tp.geoconstruction.LineThroughTwoPoints;
import com.ogprover.pp.tp.geoconstruction.PerpendicularLine;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.ogprover.pp.tp.geoconstruction.RandomPointFromLine;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for statement about two perpendicular lines</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class TwoPerpendicularLines extends PositionThmStatement {
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
	 * @param firstLine		First line of pair of perpendicular lines
	 * @param secondLine	Second line of pair of perpendicular lines
	 */
	public TwoPerpendicularLines(Line firstLine, Line secondLine) {
		this.geoObjects = new Vector<GeoConstruction>();
		this.geoObjects.add(firstLine);
		this.geoObjects.add(secondLine);
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
		XPolynomial bestCondition = null;
		int degreeOfBestCondition = 0;
		
		/*
		 * Each of two lines has to have at least 
		 * two points since we will construct perpendicular
		 * line through one point and calculate the 
		 * condition for second point to belong to 
		 * that perpendicular line, and base line of that 
		 * perpendicular will be second line and it also
		 * has to have at least two points in order to
		 * have valid construction of perpendicular line.
		 */
		Line firstLine = (Line) this.geoObjects.get(0);
		Line secondLine = (Line) this.geoObjects.get(1);
		
		while (firstLine.getPoints().size() < 2) {
			Point firstTempPoint = new RandomPointFromLine("tempPoint-" + firstLine.getPoints().size() + firstLine.getGeoObjectLabel(), firstLine);
			this.consProtocol.addGeoConstruction(firstTempPoint);
			if (firstTempPoint.isValidConstructionStep() == false) {
				OpenGeoProver.settings.getLogger().error("Failed to validate the construction of random point from line " + firstLine.getGeoObjectLabel());
				return null;
			}
			firstTempPoint.transformToAlgebraicForm();
		}
		
		while (secondLine.getPoints().size() < 2) {
			Point secondTempPoint = new RandomPointFromLine("tempPoint-" + secondLine.getPoints().size() + secondLine.getGeoObjectLabel(), secondLine);
			this.consProtocol.addGeoConstruction(secondTempPoint);
			if (secondTempPoint.isValidConstructionStep() == false) {
				OpenGeoProver.settings.getLogger().error("Failed to validate the construction of random point from line " + secondLine.getGeoObjectLabel());
				return null;
			}
			secondTempPoint.transformToAlgebraicForm();
		}
		
		
		/*
		 * We pass all points from first line and chose pairs of them:
		 * we construct line through first point perpendicular to second 
		 * line and then calculate the condition for second point to
		 * belong to constructed perpendicular line.
		 */
		Vector<Point> firstLinePoints = firstLine.getPoints();
		
		for (int ii1 = 0, jj1 = firstLinePoints.size(); ii1 < jj1; ii1++) {
			Point A = firstLinePoints.get(ii1);
			
			for (int ii2 = 0, jj2 = firstLinePoints.size(); ii2 < jj2; ii2++) {
				Point B = firstLinePoints.get(ii2);
				
				if (B.getGeoObjectLabel().equals(A.getGeoObjectLabel()))
					continue;
				
				Line p = new PerpendicularLine("tempParallelLine", secondLine, A);
				
				// temporarily add this new line to CP in order to search all
				// points of base line when calculating condition
				this.consProtocol.addGeoConstruction(p);
				
				PointSetRelationshipManager manager = new PointSetRelationshipManager(p, B, PointSetRelationshipManager.MANAGER_TYPE_STATEMENT);
				XPolynomial instantiatedCondition = manager.retrieveInstantiatedCondition();
				
				// Now check whether new condition for perpendicular lines is better 
				// polynomial then current best one i.e. if it has less degree or 
				// less number of terms
				int tempDegree = instantiatedCondition.getPolynomialDegree();
				if (bestCondition == null ||
					tempDegree < degreeOfBestCondition ||
				    (tempDegree == degreeOfBestCondition &&
				     instantiatedCondition.getTerms().size() < bestCondition.getTerms().size())) {
					bestCondition = instantiatedCondition;
					degreeOfBestCondition = tempDegree;
				}
				
				// remove constructed line from CP - it is at last place
				this.consProtocol.getConstructionSteps().remove(this.consProtocol.getConstructionSteps().size() - 1);
			}
		}
		
		/*
		 * Now repeat previous procedure but when first and second lines 
		 * interchange their roles.
		 */
		Vector<Point> secondLinePoints = secondLine.getPoints();
		
		for (int ii1 = 0, jj1 = secondLinePoints.size(); ii1 < jj1; ii1++) {
			Point A = secondLinePoints.get(ii1);
			
			for (int ii2 = 0, jj2 = secondLinePoints.size(); ii2 < jj2; ii2++) {
				Point B = secondLinePoints.get(ii2);
				
				if (B.getGeoObjectLabel().equals(A.getGeoObjectLabel()))
					continue;
				
				Line p = new PerpendicularLine("tempParallelLine", firstLine, A);
				
				// temporarily add this new line to CP in order to search all
				// points of base line when calculating condition
				this.consProtocol.addGeoConstruction(p);
				
				PointSetRelationshipManager manager = new PointSetRelationshipManager(p, B, PointSetRelationshipManager.MANAGER_TYPE_STATEMENT);
				XPolynomial instantiatedCondition = manager.retrieveInstantiatedCondition();
				
				// Now check whether new condition for perpendicular lines is better 
				// polynomial then current best one i.e. if it has less degree or 
				// less number of terms
				int tempDegree = instantiatedCondition.getPolynomialDegree();
				if (bestCondition == null ||
					tempDegree < degreeOfBestCondition ||
				    (tempDegree == degreeOfBestCondition &&
				     instantiatedCondition.getTerms().size() < bestCondition.getTerms().size())) {
					bestCondition = instantiatedCondition;
					degreeOfBestCondition = tempDegree;
				}
				
				// remove constructed line from CP - it is at last place
				this.consProtocol.getConstructionSteps().remove(this.consProtocol.getConstructionSteps().size() - 1);
			}
		}
		
		// add calculated condition to theorem statement;
		// set algebraic form of theorem statement
		return bestCondition;
	}
	
	/**
	 * @see com.ogprover.pp.tp.thmstatement.ThmStatement#getStatementDesc()
	 */
	@Override
	public String getStatementDesc() {
		StringBuilder sb = new StringBuilder();
		sb.append("Line ");
		sb.append(this.geoObjects.get(0).getGeoObjectLabel());
		sb.append(" is perpendicular to line ");
		sb.append(this.geoObjects.get(1).getGeoObjectLabel());
		return sb.toString();
	}



	@Override
	public AreaMethodTheoremStatement getAreaMethodStatement() {
		/*
		 *  The lines (AB) and (CD) are perpendicular iff AC² + BD² = BC² + AD², which
		 *  can be rewritten with the Pythagoras difference : P_ACD = P_BCD.
		 */
		LineThroughTwoPoints firstLine = (LineThroughTwoPoints)this.geoObjects.get(0);
		LineThroughTwoPoints secondLine = (LineThroughTwoPoints)this.geoObjects.get(1);
		Point a = firstLine.getPoints().get(0);
		Point b = firstLine.getPoints().get(1);
		Point c = secondLine.getPoints().get(0);
		Point d = secondLine.getPoints().get(1);
		
		AMExpression areaOfABC = new PythagorasDifference(a, c, d);
		AMExpression areaOfABD = new PythagorasDifference(b, c, d);
		
		Vector<AMExpression> statements = new Vector<AMExpression>();
		statements.add(new Difference(areaOfABC, areaOfABD));
		return new AreaMethodTheoremStatement(getStatementDesc(), statements);
	}
}
