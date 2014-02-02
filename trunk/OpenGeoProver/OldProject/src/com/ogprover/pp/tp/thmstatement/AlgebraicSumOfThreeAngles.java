/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.thmstatement;

import java.util.Vector;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.auxiliary.GeneralizedAngleTangent;
import com.ogprover.pp.tp.geoconstruction.GeoConstruction;
import com.ogprover.pp.tp.geoobject.Angle;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for statement about algebraic sum of three angles.</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class AlgebraicSumOfThreeAngles extends DimensionThmStatement {
	// Angles are of same orientation and need not have same vertex and common rays.
	
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
	 * First addend
	 */
	private Angle firstAngle = null;
	/**
	 * Second addend
	 */
	private Angle secondAngle = null;
	/**
	 * Sum
	 */
	private Angle thirdAngle = null;
	
	
	
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

	/**
	 * @param thirdAngle the thirdAngle to set
	 */
	public void setThirdAngle(Angle thirdAngle) {
		this.thirdAngle = thirdAngle;
	}

	/**
	 * @return the thirdAngle
	 */
	public Angle getThirdAngle() {
		return thirdAngle;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method.
	 * 
	 * @param alpha	First angle
	 * @param beta	Second angle
	 * @param gamma	Third angle
	 */
	public AlgebraicSumOfThreeAngles(Angle alpha, Angle beta, Angle gamma) {
		this.geoObjects = new Vector<GeoConstruction>();
		this.geoObjects.add(alpha.getVertex());
		this.geoObjects.add(alpha.getFirstRayPoint());
		this.geoObjects.add(alpha.getSecondRayPoint());
		this.geoObjects.add(beta.getVertex());
		this.geoObjects.add(beta.getFirstRayPoint());
		this.geoObjects.add(beta.getSecondRayPoint());
		this.geoObjects.add(gamma.getVertex());
		this.geoObjects.add(gamma.getFirstRayPoint());
		this.geoObjects.add(gamma.getSecondRayPoint());
		
		this.firstAngle = alpha;
		this.secondAngle = beta;
		this.thirdAngle = gamma;
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
		return OGPTP.instantiateCondition(GeneralizedAngleTangent.getConditionForAlgebraicSumOfThreeAngles(), 
				                          GeneralizedAngleTangent.getPointsMapForAlgebraicSumOfThreeAngles(this.firstAngle, this.secondAngle, this.thirdAngle));
	}

	/**
	 * @see com.ogprover.pp.tp.thmstatement.ThmStatement#getStatementDesc()
	 */
	@Override
	public String getStatementDesc() {
		StringBuilder sb = new StringBuilder();
		sb.append("Algebraic sum of angles ");
		sb.append(this.firstAngle.getDescription());
		sb.append(", ");
		sb.append(this.secondAngle.getDescription());
		sb.append(" and ");
		sb.append(this.thirdAngle.getDescription());
		sb.append(" is zero");
		return sb.toString();
	}

	@Override
	public AreaMethodTheoremStatement getAreaMethodStatement() {
		OpenGeoProver.settings.getLogger().error("Statement not currently supported by the area method.");
		return null;
	}

}
