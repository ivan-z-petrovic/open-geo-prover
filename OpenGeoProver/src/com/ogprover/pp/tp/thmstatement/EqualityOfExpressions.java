/* 
 * DISCLAIMER PLACEHOLDER 
 */
package com.ogprover.pp.tp.thmstatement;

import java.util.HashSet;
import java.util.Vector;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.pp.tp.expressions.AMExpression;
import com.ogprover.pp.tp.geoconstruction.GeoConstruction;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>The</dd>
* </dl>
* 
* @version 1.00
* @author Damien Desfontaines
*/
public class EqualityOfExpressions extends ElementaryThmStatement {
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
	 * The two expressions which have to be equal
	 */
	private AMExpression expr1, expr2;

	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method.
	 * 
	 * @param expr1 	First expression
	 * @param expr2		Second expression
	 */
	public EqualityOfExpressions(AMExpression expr1, AMExpression expr2) {
		this.expr1 = expr1;
		this.expr2 = expr2;
		HashSet<GeoConstruction> points = new HashSet<GeoConstruction>();
		points.addAll(expr1.getPoints());
		points.addAll(expr2.getPoints());
		this.geoObjects = new Vector<GeoConstruction>(points);
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	@Override
	public XPolynomial getAlgebraicForm() {
		// TODO Write the transformation to algebraic form
		OpenGeoProver.settings.getLogger().error("Transformation to algebraic form is not yet implemented for this theorem statement."); 
		return null;
	}

	@Override
	public AreaMethodTheoremStatement getAreaMethodStatement() {
		AMExpression difference = new com.ogprover.pp.tp.expressions.Difference(expr1, expr2);
		Vector<AMExpression> statements = new Vector<AMExpression>();
		statements.add(difference);
		return new AreaMethodTheoremStatement(getStatementDesc(), statements);
	}

	@Override
	public String getStatementDesc() {
		StringBuilder sb = new StringBuilder();
		sb.append("Expressions ");
		sb.append(expr1.print());
		sb.append(" and ");
		sb.append(expr2.print());
		sb.append(" are equal.");
		return sb.toString();
	}
}
