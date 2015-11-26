/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.thmstatement;

import java.util.Map;
import java.util.Vector;

import com.ogprover.polynomials.XPolynomial;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.auxiliary.GeneralizedAngleTangent;
import com.ogprover.pp.tp.expressions.AreaOfTriangle;
import com.ogprover.pp.tp.expressions.Difference;
import com.ogprover.pp.tp.expressions.AMExpression;
import com.ogprover.pp.tp.expressions.Product;
import com.ogprover.pp.tp.expressions.PythagorasDifference;
import com.ogprover.pp.tp.geoconstruction.GeoConstruction;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.ogprover.pp.tp.geoobject.Angle;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for statement about equal angles</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class EqualAngles extends DimensionThmStatement {
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
	 * First angle from pair of equal angles
	 */
	private Angle firstAngle = null;
	/**
	 * Second angle from pair of equal angles
	 */
	private Angle secondAngle = null;
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @param firstAngle the firstAngle to set
	 */
	public void setFirstAngle(Angle firstAngle) {
		this.firstAngle = firstAngle;
	}

	/**
	 * @return the firstAngle
	 */
	public Angle getFirstAngle() {
		return firstAngle;
	}

	/**
	 * @param secondAngle the secondAngle to set
	 */
	public void setSecondAngle(Angle secondAngle) {
		this.secondAngle = secondAngle;
	}

	/**
	 * @return the secondAngle
	 */
	public Angle getSecondAngle() {
		return secondAngle;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method.
	 * 
	 * @param A1	Point from first ray of first angle
	 * @param O1	Vertex of first angle
	 * @param B1	Point from second ray of first angle
	 * @param A2	Point from first ray of second angle
	 * @param O2	Vertex of second angle
	 * @param B2	Point from second ray of second angle
	 */
	public EqualAngles(Point A1, Point O1, Point B1, Point A2, Point O2, Point B2) {
		this.geoObjects = new Vector<GeoConstruction>();
		this.geoObjects.add(A1);
		this.geoObjects.add(O1);
		this.geoObjects.add(B1);
		this.geoObjects.add(A2);
		this.geoObjects.add(O2);
		this.geoObjects.add(B2);
		
		this.firstAngle = new Angle(A1, O1, B1);
		this.secondAngle = new Angle(A2, O2, B2);
	}
	
	/**
	 * Constructor method
	 * 
	 * @param firstAngle	First angle from pair of equal angles
	 * @param secondAngle	Second angle from pair of equal angles
	 */
	public EqualAngles(Angle firstAngle, Angle secondAngle) {
		this.firstAngle = firstAngle;
		this.secondAngle = secondAngle;
		
		this.geoObjects = new Vector<GeoConstruction>();
		this.geoObjects.add(firstAngle.getFirstRayPoint());
		this.geoObjects.add(firstAngle.getVertex());
		this.geoObjects.add(firstAngle.getSecondRayPoint());
		this.geoObjects.add(secondAngle.getFirstRayPoint());
		this.geoObjects.add(secondAngle.getVertex());
		this.geoObjects.add(secondAngle.getSecondRayPoint());
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
		Angle alpha = new Angle(((Point)this.geoObjects.get(0)).clone(), ((Point)this.geoObjects.get(1)).clone(), ((Point)this.geoObjects.get(2)).clone());
		Angle beta  = new Angle(((Point)this.geoObjects.get(3)).clone(), ((Point)this.geoObjects.get(4)).clone(), ((Point)this.geoObjects.get(5)).clone());
		
		Map<String, Point> pointsMap = GeneralizedAngleTangent.getPointsMapForTwoAngles(alpha, beta);
		
		return OGPTP.instantiateCondition(GeneralizedAngleTangent.getConditionForEqualsConvexAngles(), pointsMap);
	}

	/**
	 * @see com.ogprover.pp.tp.thmstatement.ThmStatement#getStatementDesc()
	 */
	@Override
	public String getStatementDesc() {
		Angle alpha = new Angle((Point)this.geoObjects.get(0), (Point)this.geoObjects.get(1), (Point)this.geoObjects.get(2));
		Angle beta = new Angle((Point)this.geoObjects.get(3), (Point)this.geoObjects.get(4), (Point)this.geoObjects.get(5));
		StringBuilder sb = new StringBuilder();
		sb.append("Angles ");
		sb.append(alpha.getDescription());
		sb.append(" and ");
		sb.append(beta.getDescription());
		sb.append(" are equal");
		return sb.toString();
	}

	@Override
	public AreaMethodTheoremStatement getAreaMethodStatement() {
		/*
		 * Let S_ABC be the area of the triangle ABC and P_ABC be defined as AB²+BC²-AC².
		 * Then two angles ABC and DEF are equal iff S_ABC*P_DEF = S_DEF*P_ABC.
		 * 
		 * This can be proved as followed :
		 * 
		 * If ABC is a right angle, then P_ABC = 0 by the Pythagoras theorem, so the result is trivial.
		 * Else, we can remark that S_ABC/P_ABC = tan(ABC)/4. Indeed, by the cosine rule :
		 * 		S_ABC = AB*BC*sin(ABC)/2
		 * 		P_ABC = AB² + BC² - (AB² + BC² - 2*AB*BC*cos(ABC))
		 * 			  = 2*AB*BC*cos(ABC)
		 */
		Point a = (Point)this.geoObjects.get(0);
		Point b = (Point)this.geoObjects.get(1);
		Point c = (Point)this.geoObjects.get(2);
		Point d = (Point)this.geoObjects.get(3);
		Point e = (Point)this.geoObjects.get(4);
		Point f = (Point)this.geoObjects.get(5);
		
		AMExpression sabc = new AreaOfTriangle(a, b, c);
		AMExpression sdef = new AreaOfTriangle(d, e, f);
		AMExpression pabc = new PythagorasDifference(a, b, c);
		AMExpression pdef = new PythagorasDifference(d, e, f);
		AMExpression firstProduct = new Product(sabc,pdef);
		AMExpression secondProduct = new Product(sdef,pabc);
		AMExpression difference = new Difference(firstProduct, secondProduct);
		
		Vector<AMExpression> statements = new Vector<AMExpression>();
		statements.add(difference);
		
		return new AreaMethodTheoremStatement(getStatementDesc(), statements);
	}

}
