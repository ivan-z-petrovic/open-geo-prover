/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.thmstatement;

import java.util.Vector;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.pp.tp.auxiliary.PointSetRelationshipManager;
import com.ogprover.pp.tp.expressions.AreaOfTriangle;
import com.ogprover.pp.tp.expressions.Difference;
import com.ogprover.pp.tp.expressions.AMExpression;
import com.ogprover.pp.tp.expressions.PythagorasDifference;
import com.ogprover.pp.tp.geoconstruction.Circle;
import com.ogprover.pp.tp.geoconstruction.CircleWithCenterAndPoint;
import com.ogprover.pp.tp.geoconstruction.GeoConstruction;
import com.ogprover.pp.tp.geoconstruction.Line;
import com.ogprover.pp.tp.geoconstruction.LineThroughTwoPoints;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.ogprover.pp.tp.geoconstruction.SetOfPoints;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for statement about point on point set</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class PointOnSetOfPoints extends PositionThmStatement {
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
	 * @param set 	Set of points (line, circle or conic)
	 * @param P		Point whose condition of belonging to 
	 * 				passed in set of points represents this statement
	 */
	public PointOnSetOfPoints(SetOfPoints set, Point P) {
		this.geoObjects = new Vector<GeoConstruction>();
		this.geoObjects.add((GeoConstruction)set);
		this.geoObjects.add(P);
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
		// prepare manager for relationship between specified point and set of points
		SetOfPoints set = (SetOfPoints) this.geoObjects.get(0);
		Point P = (Point) this.geoObjects.get(1);
		PointSetRelationshipManager manager = new PointSetRelationshipManager(set, P, PointSetRelationshipManager.MANAGER_TYPE_STATEMENT);
		
		// instantiate the condition for point P to belong to set of points
		XPolynomial instantiatedCondition = manager.retrieveInstantiatedCondition();
		
		if (instantiatedCondition == null) {
			OpenGeoProver.settings.getLogger().error("Failed to instantiate the condition");
			return null;
		}
		
		return instantiatedCondition;
	}

	/**
	 * @see com.ogprover.pp.tp.thmstatement.ThmStatement#getStatementDesc()
	 */
	@Override
	public String getStatementDesc() {
		StringBuilder sb = new StringBuilder();
		sb.append("Point ");
		sb.append(this.geoObjects.get(1).getGeoObjectLabel());
		sb.append(" lies on set of points ");
		sb.append(this.geoObjects.get(0).getGeoObjectLabel());
		return sb.toString();
	}



	@Override
	public AreaMethodTheoremStatement getAreaMethodStatement() {
		SetOfPoints set = (SetOfPoints)this.geoObjects.get(0);
		Point p = (Point)this.geoObjects.get(1);
		
		if (set instanceof Line) {
			Point a = ((LineThroughTwoPoints)set).getPoints().get(0);
			Point b = ((LineThroughTwoPoints)set).getPoints().get(1);
			
			AMExpression areaOfABP = new AreaOfTriangle(a, b, p);
			
			Vector<AMExpression> statements = new Vector<AMExpression>();
			statements.add(areaOfABP);
			
			return new AreaMethodTheoremStatement(getStatementDesc(), statements);
		}
		if (set instanceof Circle) {
			Point center = ((CircleWithCenterAndPoint)set).getCenter();
			Point pointOnCircle = ((CircleWithCenterAndPoint)set).getPoints().get(0);
			
			AMExpression squareOfRadius = new PythagorasDifference(center, pointOnCircle, center);
			AMExpression squareOfDistanceToP = new PythagorasDifference(center, p, center);
			AMExpression difference = new Difference(squareOfDistanceToP, squareOfRadius);
			
			Vector<AMExpression> statements = new Vector<AMExpression>();
			statements.add(difference);
			
			return new AreaMethodTheoremStatement(getStatementDesc(), statements);
		}
		return null;
	}
}
