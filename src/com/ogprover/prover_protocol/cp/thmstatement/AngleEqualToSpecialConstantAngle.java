/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.prover_protocol.cp.thmstatement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.ogprover.polynomials.Power;
import com.ogprover.polynomials.SymbolicPolynomial;
import com.ogprover.polynomials.SymbolicTerm;
import com.ogprover.polynomials.SymbolicVariable;
import com.ogprover.polynomials.Variable;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.prover_protocol.cp.OGPCP;
import com.ogprover.prover_protocol.cp.auxiliary.Angle;
import com.ogprover.prover_protocol.cp.auxiliary.GeneralizedAngleTangent;
import com.ogprover.prover_protocol.cp.geoconstruction.GeoConstruction;
import com.ogprover.prover_protocol.cp.geoconstruction.Point;
import com.ogprover.prover_protocol.cp.geoconstruction.SpecialConstantAngle;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for statement about angle of specific size (e.g. 60, 30 or 45 degrees)</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class AngleEqualToSpecialConstantAngle extends DimensionThmStatement {
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
	
	// Symbolic labels
	public static final String ALabel = "A";
	public static final String OLabel = "O";
	public static final String BLabel = "B";
	public static final String alphaLabel = "alpha";
	
	/**
	 * Given variable angle
	 */
	private Angle varAngle = null;
	/**
	 * Constant angle
	 */
	private SpecialConstantAngle consAngle = null;
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @param varAngle the varAngle to set
	 */
	public void setVarAngle(Angle varAngle) {
		this.varAngle = varAngle;
	}

	/**
	 * @return the varAngle
	 */
	public Angle getVarAngle() {
		return varAngle;
	}

	/**
	 * @param consAngle the consAngle to set
	 */
	public void setConsAngle(SpecialConstantAngle consAngle) {
		this.consAngle = consAngle;
	}

	/**
	 * @return the consAngle
	 */
	public SpecialConstantAngle getConsAngle() {
		return consAngle;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method.
	 * 
	 * @param A			Point from first ray of given variable angle
	 * @param O			Vertex of given variable angle
	 * @param B			Point from second ray of given variable angle
	 * @param alpha		Constant angle of known size (e.g. 30, 60 or 45 degrees)
	 */
	public AngleEqualToSpecialConstantAngle(Point A, Point O, Point B, SpecialConstantAngle alpha) {
		this.geoObjects = new Vector<GeoConstruction>();
		this.geoObjects.add(A);
		this.geoObjects.add(O);
		this.geoObjects.add(B);
		
		this.varAngle = new Angle(A, O, B);
		this.consAngle = alpha;
	}
	
	/**
	 * Constructor method
	 * 
	 * @param varAngle	Given variable angle
	 * @param alpha		Constant angle of known size (e.g. 30, 60 or 45 degrees)
	 */
	public AngleEqualToSpecialConstantAngle(Angle varAngle, SpecialConstantAngle alpha) {
		this.varAngle = varAngle;
		this.consAngle = alpha;
		
		this.geoObjects = new Vector<GeoConstruction>();
		this.geoObjects.add(varAngle.getFirstRayPoint());
		this.geoObjects.add(varAngle.getVertex());
		this.geoObjects.add(varAngle.getSecondRayPoint());
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * @see com.ogprover.prover_protocol.cp.thmstatement.ThmStatement#getAlgebraicForm()
	 */
	@Override
	public XPolynomial getAlgebraicForm() {
		Map<String, Point> pointsMap = new HashMap<String, Point>();
		pointsMap.put(ALabel, (Point)this.geoObjects.get(0));
		pointsMap.put(OLabel, (Point)this.geoObjects.get(1));
		pointsMap.put(BLabel, (Point)this.geoObjects.get(2));
		pointsMap.put(alphaLabel, this.consAngle.getParametricPoint());
		
		ArrayList<SymbolicPolynomial> tg = GeneralizedAngleTangent.getSubstitutedConditionForTangent(ALabel, OLabel, BLabel);
		SymbolicPolynomial condition = tg.get(GeneralizedAngleTangent.TANGENT_NUMERATOR);
		SymbolicTerm st = new SymbolicTerm(1);
		st.addPower(new Power(new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, alphaLabel), 1));
		condition.subtractPolynomial(tg.get(GeneralizedAngleTangent.TANGENT_DENOMINATOR).multiplyByTerm(st));
		
		return OGPCP.instantiateCondition(condition, pointsMap);
	}

	/**
	 * @see com.ogprover.prover_protocol.cp.thmstatement.ThmStatement#getStatementDesc()
	 */
	@Override
	public String getStatementDesc() {
		StringBuilder sb = new StringBuilder();
		sb.append("Angle ");
		sb.append(this.varAngle.getDescription());
		sb.append(" is equal to angle ");
		sb.append(((GeoConstruction)this.consAngle).getGeoObjectLabel());
		return sb.toString();
	}

}
