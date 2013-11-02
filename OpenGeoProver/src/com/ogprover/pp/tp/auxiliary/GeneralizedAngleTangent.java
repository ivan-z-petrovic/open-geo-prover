/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.auxiliary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.Power;
import com.ogprover.polynomials.SymbolicPolynomial;
import com.ogprover.polynomials.SymbolicTerm;
import com.ogprover.polynomials.SymbolicVariable;
import com.ogprover.polynomials.Term;
import com.ogprover.polynomials.Variable;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.ogprover.pp.tp.geoobject.Angle;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for generalized angle tangent</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class GeneralizedAngleTangent {
	/*
	 * Note: 
	 * 
	 * This class describes generalized tangent of some angle.
	 * It is 'generalized' because its argument is oriented angle.
	 * The function that describes the generalized angle tangent
	 * comes from trigonometry identity for the tangent of the difference
	 * of two angles, but it is sensitive to orientation hence its value
	 * could be positive or negative depending on orientation of angle 
	 * but in absolute value it is always equals to the absolute value of 
	 * real tangent of angle. As it will be seen later bellow in formulae,
	 * this value is calculated via slopes of angle rays. Therefore, while
	 * tan<AOB = tan<BOA for real tangent in geometry, here is tg<AOB = -tg<BOA 
	 * for generalized angle tangent. 
	 * 
	 * Two oriented angles of same orientation are equals (or geometrically 
	 * congruent) iff their generalized tangents are equals i.e their difference
	 * is equals to zero. Two oriented angles of same orientation are supplementary
	 * iff their generalized tangents have same absolute values but different
	 * signs, i.e. their sum is equals to zero.
	 * 
	 * When using in statements it is responsibility of user to provide angles
	 * of same orientation in statements that deal with angles. E.g. it is not
	 * the same to say that angles <AOB and <CDE are congruent (or supplementary)
	 * and that angles <BOA and <CDE are congruent (or supplementary).
	 */
	
	
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
	
	// This is angle tangent of angle <AOB
	/**
	 * <i><b>Symbolic label for angle vertex</b></i>
	 */
	private static final String OLabel = "O";
	/**
	 * <i><b>Symbolic label for point from first ray of angle</b></i>
	 */
	private static final String ALabel = "A";
	/**
	 * <i><b>Symbolic label for point from second ray of angle</b></i>
	 */
	private static final String BLabel = "B";
	
	// Other labels for symbolic points
	/**
	 * <i><b>Symbolic label for angle vertex</b></i>
	 */
	private static final String O1Label = "O1";
	/**
	 * <i><b>Symbolic label for point from first ray of angle</b></i>
	 */
	private static final String A1Label = "A1";
	/**
	 * <i><b>Symbolic label for point from second ray of angle</b></i>
	 */
	private static final String B1Label = "B1";
	/**
	 * <i><b>Symbolic label for angle vertex</b></i>
	 */
	private static final String O2Label = "O2";
	/**
	 * <i><b>Symbolic label for point from first ray of angle</b></i>
	 */
	private static final String A2Label = "A2";
	/**
	 * <i><b>Symbolic label for point from second ray of angle</b></i>
	 */
	private static final String B2Label = "B2";
	/**
	 * <i><b>Symbolic label for angle vertex</b></i>
	 */
	private static final String O3Label = "O3";
	/**
	 * <i><b>Symbolic label for point from first ray of angle</b></i>
	 */
	private static final String A3Label = "A3";
	/**
	 * <i><b>Symbolic label for point from second ray of angle</b></i>
	 */
	private static final String B3Label = "B3";
	
	// Constants for parts of tangent fraction
	/**
	 * <i><b> Numerator of tangent of angle </b></i>
	 */
	public static final int TANGENT_NUMERATOR = 0;
	/**
	 * <i><b> Denominator of tangent of angle </b></i>
	 */
	public static final int TANGENT_DENOMINATOR = 1;
	
	/**
	 * <i>
	 * Symbolic polynomials representing the tangent of angle between 
	 * two lines (numerator and denominator)
	 * </i>
	 */
	private static ArrayList<SymbolicPolynomial> tangentCondition = null;
	
	// Static initializer of condition member 
	static {
		/*
		 * Condition for generalized tangent of angle <AOB:
		 * 
		 * 	Let O=(xO, yO), A=(xA, yA) and B=(xB, yB). Angle <AOB is equals to
		 *	difference of angles <(BO, x) and <(AO, x). Therefore its generalized tangent
		 *	is equals to following value (formula for tangent of difference of angles):
		 *		tg<AOB = (tg<(BO, x) - tg<(AO, x))/(1 + tg<(AO, x) * tg<(BO, x))
		 *
		 *	If k1 is slope of line AO and k2 slope of line BO then this formula becomes:
		 *		(*) tg<AOB = (k2 - k1)/(1 + k1 * k2)
		 *
		 *  Special cases:
		 *  1. Ray AO is perpendicular to X-axis (slope k1 is infinity)
		 *     tg<AOB = tg(<(BO, x) - pi/2) 
		 *            = -tg(pi/2 - <(BO, x)) 
		 *            = -ctg<(BO, x) 
		 *            = -1/tg<(BO, x) 
		 *            = -1/k2
		 *  2. Ray BO is perpendicular to X-axis (slope k2 is infinity)
		 *     tg<AOB = tg(pi/2 - <(AO, x))
		 *            = ctg<(AO, x)
		 *            = 1/tg<(AO, x)
		 *            = 1/k1
		 *  3. Both rays AO and BO are perpendicular to X-axis (k1 and k2 are infinities)
		 *     tg<AOB = tg(pi/2 - pi/2)
		 *            = tg 0
		 *            = 0   
		 *
		 *	Slopes can be calculated easily this way:
		 *		k1 = (yA - yO)/(xA - xO); k2 = (yB - yO)/(xB - xO)
		 *
		 *  From equality (*) by substituting these expressions for slopes
		 *  following equality for generalized tangent of angle is obtained:
		 *  
		 *  	(**) tg<AOB = ((yB - yO)(xA - xO) - (yA - yO)(xB - xO))/((xA - xO)(xB - xO) + (yA - yO)(yB - yO))
		 *  This can be calculated this way only if denominators of both slopes are not zero
		 *  i.e. neither first nor second ray is perpendicular to X-axis. But in these special cases
		 *  equation (**) becomes one of these:
		 *  
		 *    1. If AO is perpendicular to X-axis that means xO = xA and equation becomes:
		 *       tg<AOB = -(yA - yO)(xB - xO)/(yA - yO)(yB - yO) which after reduction
		 *       by (yA - yO) leads to -1/k2.
		 *       
		 *    2. If BO is perpendicular to X-axis that means xO = xB and equation becomes:
		 *       tg<AOB = (yB - yO)(xA - xO)/(yA - yO)(yB - yO) which after reduction
		 *       by (yB - yO) leads to 1/k1.
		 *       
		 *    3. If both AO and BO are perpendicular to X-axis that means xO = xA = xB and 
		 *       equation becomes:
		 *       tg<AOB = 0.
		 *       
		 *  From equation (**) tangent is infinity if denominator is zero and that is when
		 *  (xA - xO)(xB - xO) + (yA - yO)(yB - yO) = 0.
		 *  Rays AO i BO are perpendicular iff dot product of vectors OA and OB is zero:
		 *  (xA - xO)(xB - xO) + (yA - yO)(yB - yO) = 0 but this is the same condition as
		 *  for infinity of tg<AOB. Therefore, formula (**) is good also when rays are 
		 *  perpendicular. 
		 *       
		 */
		if (tangentCondition == null) {
			tangentCondition = new ArrayList<SymbolicPolynomial>();
			SymbolicPolynomial numerator = new SymbolicPolynomial();
			SymbolicPolynomial denominator = new SymbolicPolynomial();
			
			// Instances of symbolic variables
			SymbolicVariable xO = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, OLabel);
			SymbolicVariable yO = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, OLabel);
			SymbolicVariable xA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, ALabel);
			SymbolicVariable yA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, ALabel);
			SymbolicVariable xB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, BLabel);
			SymbolicVariable yB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, BLabel);
			
			/*
			 * numerator
			 */
			// term xA * yB
			Term t = new SymbolicTerm(1);
			t.addPower(new Power(xA, 1));
			t.addPower(new Power(yB, 1));
			numerator.addTerm(t);
			
			// term - xO * yB
			t = new SymbolicTerm(-1);
			t.addPower(new Power(xO, 1));
			t.addPower(new Power(yB, 1));
			numerator.addTerm(t);
			
			// term - xA * yO
			t = new SymbolicTerm(-1);
			t.addPower(new Power(xA, 1));
			t.addPower(new Power(yO, 1));
			numerator.addTerm(t);
			
			// term - xB * yA
			t = new SymbolicTerm(-1);
			t.addPower(new Power(xB, 1));
			t.addPower(new Power(yA, 1));
			numerator.addTerm(t);
			
			// term xO * yA
			t = new SymbolicTerm(1);
			t.addPower(new Power(xO, 1));
			t.addPower(new Power(yA, 1));
			numerator.addTerm(t);
			
			// term xB * yO
			t = new SymbolicTerm(1);
			t.addPower(new Power(xB, 1));
			t.addPower(new Power(yO, 1));
			numerator.addTerm(t);
			
			tangentCondition.add(numerator);
			
			/*
			 * denominator
			 */
			// term xA * xB
			t = new SymbolicTerm(1);
			t.addPower(new Power(xA, 1));
			t.addPower(new Power(xB, 1));
			denominator.addTerm(t);
			
			// term - xA * xO
			t = new SymbolicTerm(-1);
			t.addPower(new Power(xA, 1));
			t.addPower(new Power(xO, 1));
			denominator.addTerm(t);
			
			// term - xB * xO
			t = new SymbolicTerm(-1);
			t.addPower(new Power(xB, 1));
			t.addPower(new Power(xO, 1));
			denominator.addTerm(t);
			
			// term xO^2
			t = new SymbolicTerm(1);
			t.addPower(new Power(xO, 2));
			denominator.addTerm(t);
			
			// term yA * yB
			t = new SymbolicTerm(1);
			t.addPower(new Power(yA, 1));
			t.addPower(new Power(yB, 1));
			denominator.addTerm(t);
			
			// term - yA * yO
			t = new SymbolicTerm(-1);
			t.addPower(new Power(yA, 1));
			t.addPower(new Power(yO, 1));
			denominator.addTerm(t);
			
			// term - yB * yO
			t = new SymbolicTerm(-1);
			t.addPower(new Power(yB, 1));
			t.addPower(new Power(yO, 1));
			denominator.addTerm(t);
			
			// term yO^2
			t = new SymbolicTerm(1);
			t.addPower(new Power(yO, 2));
			denominator.addTerm(t);
			
			tangentCondition.add(denominator);
		}
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * <i>
	 * Method to populate map of points for condition about angles' tangents of two
	 * angles.
	 * </i>
	 * 
	 * @param alpha		First angle
	 * @param beta		Second angle
	 * @return			Map of points with assigned points to symbolic labels
	 */
	public static Map<String, Point> getPointsMapForTwoAngles(Angle alpha, Angle beta) {
		Map<String, Point> pointsMap = new HashMap<String, Point>();
		
		pointsMap.put(O1Label, alpha.getVertex());
		pointsMap.put(A1Label, alpha.getFirstRayPoint());
		pointsMap.put(B1Label, alpha.getSecondRayPoint());
		
		pointsMap.put(O2Label, beta.getVertex());
		pointsMap.put(A2Label, beta.getFirstRayPoint());
		pointsMap.put(B2Label, beta.getSecondRayPoint());
		
		return pointsMap;
	}
	
	/**
	 * <i>
	 * Method to populate map of points for condition about algebraic sum of three angles.
	 * </i>
	 * 
	 * @param alpha		First angle
	 * @param beta		Second angle
	 * @param gamma		Third angle
	 * @return			Map of points with assigned points to symbolic labels
	 */
	public static Map<String, Point> getPointsMapForAlgebraicSumOfThreeAngles(Angle alpha, Angle beta, Angle gamma) {
		Map<String, Point> pointsMap = new HashMap<String, Point>();
		
		pointsMap.put(O1Label, alpha.getVertex());
		pointsMap.put(A1Label, alpha.getFirstRayPoint());
		pointsMap.put(B1Label, alpha.getSecondRayPoint());
		
		pointsMap.put(O2Label, beta.getVertex());
		pointsMap.put(A2Label, beta.getFirstRayPoint());
		pointsMap.put(B2Label, beta.getSecondRayPoint());
		
		pointsMap.put(O3Label, gamma.getVertex());
		pointsMap.put(A3Label, gamma.getFirstRayPoint());
		pointsMap.put(B3Label, gamma.getSecondRayPoint());
		
		return pointsMap;
	}
	
	/**
	 * <i>
	 * Method to populate map of points' labels for condition about 
	 * angles' tangents of two angles.
	 * </i>
	 * 
	 * @param labelA1	Label of point from first ray of first angle
	 * @param labelO1	Label of vertex of first angle
	 * @param labelB1	Label of point from second ray of first angle
	 * @param labelA2	Label of point from first ray of second angle
	 * @param labelO2	Label of vertex of second angle
	 * @param labelB2	Label of point from second ray of second angle
	 * @return	Map of points' labels with assigned new labels to symbolic labels
	 */
	public static Map<String, String> getLabelsMapForTwoAngles(String labelA1, String labelO1, String labelB1,
			                                                   String labelA2, String labelO2, String labelB2) {
		Map<String, String> labelsMap = new HashMap<String, String>();
		
		labelsMap.put(O1Label, labelO1);
		labelsMap.put(A1Label, labelA1);
		labelsMap.put(B1Label, labelB1);
		
		labelsMap.put(O2Label, labelO2);
		labelsMap.put(A2Label, labelA2);
		labelsMap.put(B2Label, labelB2);
		
		return labelsMap;
	}
	
	/**
	 * <i>
	 * Method that substitutes common points' labels with
	 * passed in labels in given symbolic polynomial representing
	 * some condition for two angles.
	 * </i>
	 * 
	 * @param symbPoly	Symbolic polynomial to be changed
	 * @param labelA1	Label of point from first ray of first angle
	 * @param labelO1	Label of vertex of first angle
	 * @param labelB1	Label of point from second ray of first angle
	 * @param labelA2	Label of point from first ray of second angle
	 * @param labelO2	Label of vertex of second angle
	 * @param labelB2	Label of point from second ray of second angle
	 * @return			Symbolic polynomial with substituted labels
	 */
	public static SymbolicPolynomial substitutePointLabelsForTwoAngles(SymbolicPolynomial symbPoly, String labelA1, String labelO1, String labelB1,
                                                                                                    String labelA2, String labelO2, String labelB2) {
		Map<String, String> labelsMap = getLabelsMapForTwoAngles(labelA1, labelO1, labelB1, labelA2, labelO2, labelB2);
		
		return symbPoly.substitute(labelsMap);
	}
	
	/**
	 * <i>
	 * Method that returns symbolic polynomial that constitutes the algebraic 
	 * condition for generalized tangent of an angle.
	 * </i>
	 *  
	 * @return	Array of two symbolic polynomials representing the condition
	 * 			for tangent of given angle (numerator and denominator of tangent)
	 */
	public static ArrayList<SymbolicPolynomial> getConditionForTangent() {
		ArrayList<SymbolicPolynomial> tan = new ArrayList<SymbolicPolynomial>();
		
		tan.add((SymbolicPolynomial) GeneralizedAngleTangent.tangentCondition.get(GeneralizedAngleTangent.TANGENT_NUMERATOR).clone());
		tan.add((SymbolicPolynomial) GeneralizedAngleTangent.tangentCondition.get(GeneralizedAngleTangent.TANGENT_DENOMINATOR).clone());
		
		return tan;
	}
	
	/**
	 * <i>
	 * Method that returns symbolic polynomial that constitutes the algebraic 
	 * condition for generalized tangent of an angle.
	 * </i>
	 *  
	 * @param labelA	Label of point from first angle's ray.
	 * @param labelO	Label of angle's vertex.
	 * @param labelB	Label of point from second angle's ray.
	 * @return			Array of two symbolic polynomials representing the condition
	 * 					for tangent of given angle (numerator and denominator of tangent)
	 */
	public static ArrayList<SymbolicPolynomial> getSubstitutedConditionForTangent(String labelA, String labelO, String labelB) {
		ArrayList<SymbolicPolynomial> tan = new ArrayList<SymbolicPolynomial>();
		Map<String, String> labelsMap = new HashMap<String, String>();
		
		labelsMap.put(OLabel, labelO);
		labelsMap.put(ALabel, labelA);
		labelsMap.put(BLabel, labelB);
		
		tan.add(((SymbolicPolynomial) GeneralizedAngleTangent.tangentCondition.get(GeneralizedAngleTangent.TANGENT_NUMERATOR).clone()).substitute(labelsMap));
		tan.add(((SymbolicPolynomial) GeneralizedAngleTangent.tangentCondition.get(GeneralizedAngleTangent.TANGENT_DENOMINATOR).clone()).substitute(labelsMap));
		
		return tan;
	}
	
	/**
	 * <i>
	 * Method that returns the condition for generalized tangent of first angle <A1O1B1
	 * </i>
	 * 
	 * @return	Array of two symbolic polynomials representing the condition
	 * 			for tangent of an angle (numerator and denominator of tangent)
	 */
	private static ArrayList<SymbolicPolynomial> getConditionForTangentOfFirstAngle() {
		ArrayList<SymbolicPolynomial> tan = GeneralizedAngleTangent.getConditionForTangent();
		
		for (Term t: tan.get(GeneralizedAngleTangent.TANGENT_NUMERATOR).getTermsAsDescList()) {
			for (Power p : t.getPowers()) {
				SymbolicVariable var = (SymbolicVariable)(p.getVariable());
				if (var.getPointLabel().equals(OLabel)) {
					var.setPointLabel(O1Label);
				}
				else if (var.getPointLabel().equals(ALabel)) {
					var.setPointLabel(A1Label);
				}
				else if (var.getPointLabel().equals(BLabel)) {
					var.setPointLabel(B1Label);
				}
			}
		}
		for (Term t: tan.get(GeneralizedAngleTangent.TANGENT_DENOMINATOR).getTermsAsDescList()) {
			for (Power p : t.getPowers()) {
				SymbolicVariable var = (SymbolicVariable)(p.getVariable());
				if (var.getPointLabel().equals(OLabel)) {
					var.setPointLabel(O1Label);
				}
				else if (var.getPointLabel().equals(ALabel)) {
					var.setPointLabel(A1Label);
				}
				else if (var.getPointLabel().equals(BLabel)) {
					var.setPointLabel(B1Label);
				}
			}
		}
		
		OpenGeoProver.settings.getLogger().debug("Condition for tangent of first angle:");
		OpenGeoProver.settings.getLogger().debug("Numerator:");
		OpenGeoProver.settings.getLogger().debug(tan.get(TANGENT_NUMERATOR).printToLaTeX());
		OpenGeoProver.settings.getLogger().debug("Denominator:");
		OpenGeoProver.settings.getLogger().debug(tan.get(TANGENT_DENOMINATOR).printToLaTeX());
		return tan;
	}
	
	/**
	 * <i>
	 * Method that returns the condition for tangent of second angle <A2O2B2
	 * </i>
	 * 
	 * @return	Array of two symbolic polynomials representing the condition
	 * 			for tangent of an angle (numerator and denominator of tangent)
	 */
	private static ArrayList<SymbolicPolynomial> getConditionForTangentOfSecondAngle() {
		ArrayList<SymbolicPolynomial> tan = GeneralizedAngleTangent.getConditionForTangent();
		
		for (Term t: tan.get(GeneralizedAngleTangent.TANGENT_NUMERATOR).getTermsAsDescList()) {
			for (Power p : t.getPowers()) {
				SymbolicVariable var = (SymbolicVariable)(p.getVariable());
				if (var.getPointLabel().equals(OLabel)) {
					var.setPointLabel(O2Label);
				}
				else if (var.getPointLabel().equals(ALabel)) {
					var.setPointLabel(A2Label);
				}
				else if (var.getPointLabel().equals(BLabel)) {
					var.setPointLabel(B2Label);
				}
			}
		}
		for (Term t: tan.get(GeneralizedAngleTangent.TANGENT_DENOMINATOR).getTermsAsDescList()) {
			for (Power p : t.getPowers()) {
				SymbolicVariable var = (SymbolicVariable)(p.getVariable());
				if (var.getPointLabel().equals(OLabel)) {
					var.setPointLabel(O2Label);
				}
				else if (var.getPointLabel().equals(ALabel)) {
					var.setPointLabel(A2Label);
				}
				else if (var.getPointLabel().equals(BLabel)) {
					var.setPointLabel(B2Label);
				}
			}
		}
		
		OpenGeoProver.settings.getLogger().debug("Condition for tangent of second angle:");
		OpenGeoProver.settings.getLogger().debug("Numerator:");
		OpenGeoProver.settings.getLogger().debug(tan.get(TANGENT_NUMERATOR).printToLaTeX());
		OpenGeoProver.settings.getLogger().debug("Denominator:");
		OpenGeoProver.settings.getLogger().debug(tan.get(TANGENT_DENOMINATOR).printToLaTeX());
		
		return tan;
	}
	
	/**
	 * <i>
	 * Method that returns the condition for tangent of third angle <A3O3B3
	 * </i>
	 * 
	 * @return	Array of two symbolic polynomials representing the condition
	 * 			for tangent of an angle (numerator and denominator of tangent)
	 */
	private static ArrayList<SymbolicPolynomial> getConditionForTangentOfThirdAngle() {
		ArrayList<SymbolicPolynomial> tan = GeneralizedAngleTangent.getConditionForTangent();
		
		for (Term t: tan.get(GeneralizedAngleTangent.TANGENT_NUMERATOR).getTermsAsDescList()) {
			for (Power p : t.getPowers()) {
				SymbolicVariable var = (SymbolicVariable)(p.getVariable());
				if (var.getPointLabel().equals(OLabel)) {
					var.setPointLabel(O3Label);
				}
				else if (var.getPointLabel().equals(ALabel)) {
					var.setPointLabel(A3Label);
				}
				else if (var.getPointLabel().equals(BLabel)) {
					var.setPointLabel(B3Label);
				}
			}
		}
		for (Term t: tan.get(GeneralizedAngleTangent.TANGENT_DENOMINATOR).getTermsAsDescList()) {
			for (Power p : t.getPowers()) {
				SymbolicVariable var = (SymbolicVariable)(p.getVariable());
				if (var.getPointLabel().equals(OLabel)) {
					var.setPointLabel(O3Label);
				}
				else if (var.getPointLabel().equals(ALabel)) {
					var.setPointLabel(A3Label);
				}
				else if (var.getPointLabel().equals(BLabel)) {
					var.setPointLabel(B3Label);
				}
			}
		}
		
		OpenGeoProver.settings.getLogger().debug("Condition for tangent of third angle:");
		OpenGeoProver.settings.getLogger().debug("Numerator:");
		OpenGeoProver.settings.getLogger().debug(tan.get(TANGENT_NUMERATOR).printToLaTeX());
		OpenGeoProver.settings.getLogger().debug("Denominator:");
		OpenGeoProver.settings.getLogger().debug(tan.get(TANGENT_DENOMINATOR).printToLaTeX());
		
		return tan;
	}
	
	/**
	 * <i>
	 * Method for condition for difference of two generalized tangents.
	 * </i>
	 * 
	 * @return	Symbolic polynomial representing the condition for difference of tangents
	 */
	public static SymbolicPolynomial getConditionForTangentsDifference() {
		// Tangent of angle <A1O1B1
		ArrayList<SymbolicPolynomial> tanA1O1B1 = GeneralizedAngleTangent.getConditionForTangentOfFirstAngle();
		
		// Tangent of angle <A2O2B2
		ArrayList<SymbolicPolynomial> tanA2O2B2 = GeneralizedAngleTangent.getConditionForTangentOfSecondAngle();
		
		// Resulting condition is tg<A1O1B1 = tg<A2O2B2 i.e. tg<A1O1B1 - tg<A2O2B2 = 0 and
		// will be used to express the condition when angles are congruent in geometric
		// sense. But this condition is wider since tg(x) = tg(pi + x). But since
		// we are talking here about convex angles, no one of them can be greater then
		// pi, so condition of equal tangents here is same as congruence of angles in
		// geometric sense.
		// In order to be able to calculate this value these tangents must exist i.e.
		// angles are not right angles i.e. denominators of tangents are not zero.
		// Another case when angles are equals is when both angles are right angles i.e.
		// both tangents' denominators are zero.
		
		// tanA1O1B1 = n1/d1, tanA2O2B2 = n2/d2 => tanA1O1B1 - tanA2O2B2 = (n1 * d2 - n2 * d1)/(d1 * d2)
		// from this expression we see that angles are equals if n1*d2 - n2*d1 = 0 and
		// this is satisfied when d1,d2 <> 0. But from above discussion, angles are equals
		// also when they are both right angles i.e. d1=d2=0. But in this case condition 
		// n1*d2 - n2*d1 = 0  is also met. So this condition can be used for all cases.
		return (SymbolicPolynomial) tanA1O1B1.get(GeneralizedAngleTangent.TANGENT_NUMERATOR).clone().
		       multiplyByPolynomial(tanA2O2B2.get(GeneralizedAngleTangent.TANGENT_DENOMINATOR)).
		       subtractPolynomial(tanA2O2B2.get(GeneralizedAngleTangent.TANGENT_NUMERATOR).clone().
		       multiplyByPolynomial(tanA1O1B1.get(GeneralizedAngleTangent.TANGENT_DENOMINATOR)));
	}
	
	/**
	 * <i>
	 * Method for condition for sum of two generalized tangents.
	 * </i>
	 * 
	 * @return	Symbolic polynomial representing the condition for sum of tangents
	 */
	public static SymbolicPolynomial getConditionForTangentsSum() {
		// Tangent of angle <A1O1B1
		ArrayList<SymbolicPolynomial> tanA1O1B1 = GeneralizedAngleTangent.getConditionForTangentOfFirstAngle();
		
		// Tangent of angle <A2O2B2
		ArrayList<SymbolicPolynomial> tanA2O2B2 = GeneralizedAngleTangent.getConditionForTangentOfSecondAngle();
		
		// Resulting condition is tg<A1O1B1 = -tg<A2O2B2 i.e. tg<A1O1B1 + tg<A2O2B2 = 0 and
		// will be used to express the condition when angles are supplementary each to other.
		// In order to be able to calculate this value these tangents must exist i.e.
		// angles are not right angles i.e. denominators of tangents are not zero.
		// Another case when angles are mutually supplementary is when both angles are right 
		// angles i.e. both tangents' denominators are zero.
		
		// tanA1O1B1 = n1/d1, tanA2O2B2 = n2/d2 => tanA1O1B1 + tanA2O2B2 = (n1 * d2 + n2 * d1)/(d1 * d2)
		// from this expression we see that angles are supplementary if n1*d2 + n2*d1 = 0 and
		// this is satisfied when d1,d2 <> 0. But from above discussion, angles are supplementary
		// also when they are both right angles i.e. d1=d2=0. But in this case condition 
		// n1*d2 + n2*d1 = 0  is also met. So this condition can be used for all cases.
		return (SymbolicPolynomial) tanA1O1B1.get(GeneralizedAngleTangent.TANGENT_NUMERATOR).clone().
		       multiplyByPolynomial(tanA2O2B2.get(GeneralizedAngleTangent.TANGENT_DENOMINATOR)).
		       addPolynomial(tanA2O2B2.get(GeneralizedAngleTangent.TANGENT_NUMERATOR).clone().
		       multiplyByPolynomial(tanA1O1B1.get(GeneralizedAngleTangent.TANGENT_DENOMINATOR)));
	}
	
	/**
	 * <i>
	 * Method for condition for generalized tangent of angles' sum.
	 * </i>
	 * 
	 * @return	Symbolic polynomial representing the condition for generalized tangent of angles' sum
	 */
	public static ArrayList<SymbolicPolynomial> getConditionForTangentOfSum() {
		ArrayList<SymbolicPolynomial> tanSum = new ArrayList<SymbolicPolynomial>();
		
		// Tangent of angle <A1O1B1
		ArrayList<SymbolicPolynomial> tanA1O1B1 = GeneralizedAngleTangent.getConditionForTangentOfFirstAngle();
		
		// Tangent of angle <A2O2B2
		ArrayList<SymbolicPolynomial> tanA2O2B2 = GeneralizedAngleTangent.getConditionForTangentOfSecondAngle();
		
		// Resulting condition is tg(<A1O1B1 + <A2O2B2) = (tg<A1O1B1 + tg<A2O2B2)/(1-tg<A1O1B1*tg<A2O2B2)
		// tanA1O1B1 = n1/d1, tanA2O2B2 = n2/d2 => 
		// tg(<A1O1B1 + <A2O2B2) = (n1 * d2 + n2 * d1)/(d1 * d2 - n1 * n2)
		SymbolicPolynomial numerator = (SymbolicPolynomial) tanA1O1B1.get(GeneralizedAngleTangent.TANGENT_NUMERATOR).clone().
		       												multiplyByPolynomial(tanA2O2B2.get(GeneralizedAngleTangent.TANGENT_DENOMINATOR)).
		       												addPolynomial(tanA2O2B2.get(GeneralizedAngleTangent.TANGENT_NUMERATOR).clone().
		       												multiplyByPolynomial(tanA1O1B1.get(GeneralizedAngleTangent.TANGENT_DENOMINATOR)));
		
		SymbolicPolynomial denominator = (SymbolicPolynomial) tanA1O1B1.get(GeneralizedAngleTangent.TANGENT_DENOMINATOR).clone().
															  multiplyByPolynomial(tanA2O2B2.get(GeneralizedAngleTangent.TANGENT_DENOMINATOR)).
															  subtractPolynomial(tanA1O1B1.get(GeneralizedAngleTangent.TANGENT_NUMERATOR).clone().
															  multiplyByPolynomial(tanA2O2B2.get(GeneralizedAngleTangent.TANGENT_NUMERATOR)));
		
		tanSum.add(numerator);
		tanSum.add(denominator);
		
		return tanSum;
	}
	
	/**
	 * <i>
	 * Method for condition for generalized tangent of sum of three angles.
	 * </i>
	 * 
	 * @param	labelA1		Label of point from first ray of first angle
	 * @param	labelO1		Label of vertex of first angle
	 * @param	labelB1		Label of point from second ray of first angle
	 * @param	labelA2		Label of point from first ray of second angle
	 * @param	labelO2		Label of vertex of second angle
	 * @param	labelB2		Label of point from second ray of second angle
	 * @param	labelA3		Label of point from first ray of third angle
	 * @param	labelO3		Label of vertex of third angle
	 * @param	labelB3		Label of point from second ray of third angle
	 * @return				Symbolic polynomials representing the condition for generalized tangent of sum of three angles
	 */
	public static ArrayList<SymbolicPolynomial> getSubstitutedConditionForTangentOfSumOfThreeAngles(String labelA1, String labelO1, String labelB1,
																									String labelA2, String labelO2, String labelB2,
																									String labelA3, String labelO3, String labelB3) {
		ArrayList<SymbolicPolynomial> tanSum = new ArrayList<SymbolicPolynomial>();
		
		// Tangent of angle <A1O1B1 = x
		ArrayList<SymbolicPolynomial> tanA1O1B1 = GeneralizedAngleTangent.getConditionForTangentOfFirstAngle();
		
		// Tangent of angle <A2O2B2 = y
		ArrayList<SymbolicPolynomial> tanA2O2B2 = GeneralizedAngleTangent.getConditionForTangentOfSecondAngle();
		
		// Tangent of angle <A3O3B3 = z
		ArrayList<SymbolicPolynomial> tanA3O3B3 = GeneralizedAngleTangent.getConditionForTangentOfThirdAngle();
		
		// tan(x+y+z) = tan((x+y)+z)=(tan(x+y)+tanz)/(1-tan(x+y)tanz)=(tanx+tany+tanz-tanx*tany*tanz)/(1-tanx*tany-tanx*tanz-tany*tanz)
		// tanx = nx/dx, tany = ny/dy, tanz = nz/dz
		// tan(x+y+z) = N/D
		// N = nx*dy*dz + dx*ny*dz + dx*dy*nz - nx*ny*nz
		// D = dx*dy*dz - nx*ny*dz - nx*dy*nz - dx*ny*nz
		
		SymbolicPolynomial one = new SymbolicPolynomial(1);
		SymbolicPolynomial temp;
		SymbolicPolynomial nx = tanA1O1B1.get(GeneralizedAngleTangent.TANGENT_NUMERATOR);
		SymbolicPolynomial dx = tanA1O1B1.get(GeneralizedAngleTangent.TANGENT_DENOMINATOR);
		SymbolicPolynomial ny = tanA2O2B2.get(GeneralizedAngleTangent.TANGENT_NUMERATOR);
		SymbolicPolynomial dy = tanA2O2B2.get(GeneralizedAngleTangent.TANGENT_DENOMINATOR);
		SymbolicPolynomial nz = tanA3O3B3.get(GeneralizedAngleTangent.TANGENT_NUMERATOR);
		SymbolicPolynomial dz = tanA3O3B3.get(GeneralizedAngleTangent.TANGENT_DENOMINATOR);
		
		SymbolicPolynomial N = new SymbolicPolynomial();
		temp = (SymbolicPolynomial) one.clone().multiplyByPolynomial(nx).multiplyByPolynomial(dy).multiplyByPolynomial(dz);
		N.addPolynomial(temp);
		temp = (SymbolicPolynomial) one.clone().multiplyByPolynomial(dx).multiplyByPolynomial(ny).multiplyByPolynomial(dz);
		N.addPolynomial(temp);
		temp = (SymbolicPolynomial) one.clone().multiplyByPolynomial(dx).multiplyByPolynomial(dy).multiplyByPolynomial(nz);
		N.addPolynomial(temp);
		temp = (SymbolicPolynomial) one.clone().multiplyByPolynomial(nx).multiplyByPolynomial(ny).multiplyByPolynomial(nz);
		N.subtractPolynomial(temp);
		
		SymbolicPolynomial D = new SymbolicPolynomial();
		temp = (SymbolicPolynomial) one.clone().multiplyByPolynomial(dx).multiplyByPolynomial(dy).multiplyByPolynomial(dz);
		D.addPolynomial(temp);
		temp = (SymbolicPolynomial) one.clone().multiplyByPolynomial(nx).multiplyByPolynomial(ny).multiplyByPolynomial(dz);
		D.subtractPolynomial(temp);
		temp = (SymbolicPolynomial) one.clone().multiplyByPolynomial(nx).multiplyByPolynomial(dy).multiplyByPolynomial(nz);
		D.subtractPolynomial(temp);
		temp = (SymbolicPolynomial) one.clone().multiplyByPolynomial(dx).multiplyByPolynomial(ny).multiplyByPolynomial(nz);
		D.subtractPolynomial(temp);
		
		Map<String, String> labelsMap = new HashMap<String, String>();
		
		labelsMap.put(A1Label, labelA1);
		labelsMap.put(O1Label, labelO1);
		labelsMap.put(B1Label, labelB1);
		labelsMap.put(A2Label, labelA2);
		labelsMap.put(O2Label, labelO2);
		labelsMap.put(B2Label, labelB2);
		labelsMap.put(A3Label, labelA3);
		labelsMap.put(O3Label, labelO3);
		labelsMap.put(B3Label, labelB3);
		
		tanSum.add(N.substitute(labelsMap));
		tanSum.add(D.substitute(labelsMap));
		
		return tanSum;
	}
	
	/**
	 * <i>
	 * Method for condition for angle trisector.
	 * </i>
	 * 
	 * @return	Symbolic polynomial representing the condition for triple angle
	 */
	public static SymbolicPolynomial getConditionForTripleAngle() {
		/*
		 * <A1O1B1 = 3<A2O2B2
		 * tg<A1O1B1 = tg(3<A2O2B2) = (3tg<A2O2B2 - (tg<A2O2B2)^3)/(1-3(tg<A2O2B2)^2) (*)
		 * 
		 * tg<A1O1B1 = n1/d1; tg<A2O2B2 = n2/d2
		 * 
		 * (*) => n1/d1 = (3n2/d2 - n2^3/d2^3)/(1 - 3n2^2/d2^2)
		 *        n1/d1 = (3n2d2^2 - n2^3)/(d2^3 - 3n2^2d2)
		 *        n1*(d2^3 - 3*n2^2*d2) - d1*(3*n2*d2^2 - n2^3) = 0
		 */
		// Tangent of angle <A1O1B1
		ArrayList<SymbolicPolynomial> tanA1O1B1 = GeneralizedAngleTangent.getConditionForTangentOfFirstAngle();
		// Tangent of angle <A2O2B2
		ArrayList<SymbolicPolynomial> tanA2O2B2 = GeneralizedAngleTangent.getConditionForTangentOfSecondAngle();
		
		SymbolicPolynomial n1 = tanA1O1B1.get(GeneralizedAngleTangent.TANGENT_NUMERATOR);
		SymbolicPolynomial d1 = tanA1O1B1.get(GeneralizedAngleTangent.TANGENT_DENOMINATOR);
		SymbolicPolynomial n2 = tanA2O2B2.get(GeneralizedAngleTangent.TANGENT_NUMERATOR);
		SymbolicPolynomial d2 = tanA2O2B2.get(GeneralizedAngleTangent.TANGENT_DENOMINATOR);
		SymbolicTerm coeff3 = new SymbolicTerm(3);
		
		return (SymbolicPolynomial) n1.multiplyByPolynomial(d2.clone().multiplyByPolynomial(d2.clone().multiplyByPolynomial(d2)).
				                       subtractPolynomial(n2.clone().multiplyByPolynomial(n2).multiplyByPolynomial(d2).multiplyByTerm(coeff3))).
		         subtractPolynomial(d1.multiplyByPolynomial(n2.clone().multiplyByPolynomial(d2.clone().multiplyByPolynomial(d2)).multiplyByTerm(coeff3).
		    		                   subtractPolynomial(n2.clone().multiplyByPolynomial(n2.clone().multiplyByPolynomial(n2)))));
		          
	}
	
	/**
	 * <i>
	 * Method for condition for algebraic sum of three angles.
	 * </i>
	 * 
	 * @return	Symbolic polynomial representing the condition for algebraic sum of three angles
	 */
	public static SymbolicPolynomial getConditionForAlgebraicSumOfThreeAngles() {
		// The condition is: tg(<A1O1B1 + <A2O2B2) - tg<A3O3B3 = 0
		ArrayList<SymbolicPolynomial> tanSum = GeneralizedAngleTangent.getConditionForTangentOfSum();
		ArrayList<SymbolicPolynomial> tan3 = GeneralizedAngleTangent.getConditionForTangentOfThirdAngle();
		
		// return the condition for difference
		return (SymbolicPolynomial) tanSum.get(GeneralizedAngleTangent.TANGENT_NUMERATOR).clone().
	       							multiplyByPolynomial(tan3.get(GeneralizedAngleTangent.TANGENT_DENOMINATOR)).
	       							subtractPolynomial(tan3.get(GeneralizedAngleTangent.TANGENT_NUMERATOR).clone().
	       						    multiplyByPolynomial(tanSum.get(GeneralizedAngleTangent.TANGENT_DENOMINATOR)));
	}
	
	/**
	 * <i>
	 * Method that gives the condition for equals convex angles.
	 * These angles are of same orientation.
	 * </i>
	 * 
	 * @return		Symbolic polynomial representing the condition
	 */
	public static SymbolicPolynomial getConditionForEqualsConvexAngles() {
		// Convex angles of same orientation are equals iff ("if and only if") 
		// their generalized tangents are equals i.e their difference is zero.
		// tg<A1O1B1 - tg<A2O2B2 = 0
		return GeneralizedAngleTangent.getConditionForTangentsDifference();
	}
	
	/**
	 * <i>
	 * Method that gives the condition for supplementary convex angles (whose sum is pi).
	 * These angles are of same orientation. If angles are of opposite orientation
	 * then this method gives the condition for them to be equals (as plain geometric angles).
	 * </i>
	 * 
	 * @return		Symbolic polynomial representing the condition
	 */
	public static SymbolicPolynomial getConditionForSupplementaryConvexAngles() {
		// Convex angles of same orientation are supplementary iff ("if and only if") 
		// their generalized tangents are of opposite signs i.e.their sum is zero.
		// tg<A1O1B1 + tg<A2O2B2 = 0
		return GeneralizedAngleTangent.getConditionForTangentsSum();
	}
	
	/**
	 * <i>
	 * Method that gives the condition for equals or supplementary convex angles.
	 * </i>
	 * 
	 * @return			Symbolic polynomial representing the condition
	 */
	public static SymbolicPolynomial getConditionForEqualsOrSupplementaryConvexAngles() {
		// Convex angles are equals iff difference of their tangents is zero, a
		// and they are supplementary iff sum of their tangents is zero.
		// "Or" of these two condition means multiplying.
		return (SymbolicPolynomial) GeneralizedAngleTangent.getConditionForTangentsDifference().
		       multiplyByPolynomial(GeneralizedAngleTangent.getConditionForTangentsSum());
	}

}


