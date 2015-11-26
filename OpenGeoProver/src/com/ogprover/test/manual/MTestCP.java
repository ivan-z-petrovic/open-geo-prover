/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.manual;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import com.ogprover.main.OGPConfigurationSettings;
import com.ogprover.main.OGPConstants;
import com.ogprover.main.OGPParameters;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.SymbolicPolynomial;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.auxiliary.ProductOfTwoSegments;
import com.ogprover.pp.tp.auxiliary.RatioOfTwoCollinearSegments;
import com.ogprover.pp.tp.auxiliary.RatioProduct;
import com.ogprover.pp.tp.geoconstruction.AngleBisector;
import com.ogprover.pp.tp.geoconstruction.CentralSymmetricPoint;
import com.ogprover.pp.tp.geoconstruction.Circle;
import com.ogprover.pp.tp.geoconstruction.CircleWithCenterAndPoint;
import com.ogprover.pp.tp.geoconstruction.CircleWithCenterAndRadius;
import com.ogprover.pp.tp.geoconstruction.CircleWithDiameter;
import com.ogprover.pp.tp.geoconstruction.CircumscribedCircle;
import com.ogprover.pp.tp.geoconstruction.FootPoint;
import com.ogprover.pp.tp.geoconstruction.FreePoint;
import com.ogprover.pp.tp.geoconstruction.GeneralConicSection;
import com.ogprover.pp.tp.geoconstruction.GeneralizedSegmentDivisionPoint;
import com.ogprover.pp.tp.geoconstruction.GeoConstruction;
import com.ogprover.pp.tp.geoconstruction.HarmonicConjugatePoint;
import com.ogprover.pp.tp.geoconstruction.IntersectionPoint;
import com.ogprover.pp.tp.geoconstruction.InverseOfPoint;
import com.ogprover.pp.tp.geoconstruction.Line;
import com.ogprover.pp.tp.geoconstruction.LineThroughTwoPoints;
import com.ogprover.pp.tp.geoconstruction.MidPoint;
import com.ogprover.pp.tp.geoconstruction.ParallelLine;
import com.ogprover.pp.tp.geoconstruction.PerpendicularBisector;
import com.ogprover.pp.tp.geoconstruction.PerpendicularLine;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.ogprover.pp.tp.geoconstruction.Polar;
import com.ogprover.pp.tp.geoconstruction.Pole;
import com.ogprover.pp.tp.geoconstruction.RadicalAxis;
import com.ogprover.pp.tp.geoconstruction.RandomPointFromCircle;
import com.ogprover.pp.tp.geoconstruction.RandomPointFromGeneralConic;
import com.ogprover.pp.tp.geoconstruction.RandomPointFromLine;
import com.ogprover.pp.tp.geoconstruction.ReflectedPoint;
import com.ogprover.pp.tp.geoconstruction.RotatedPoint;
import com.ogprover.pp.tp.geoconstruction.ShortcutConstruction;
import com.ogprover.pp.tp.geoconstruction.TangentLine;
import com.ogprover.pp.tp.geoconstruction.TranslatedPoint;
import com.ogprover.pp.tp.geoobject.Angle;
import com.ogprover.pp.tp.geoobject.Segment;
import com.ogprover.pp.tp.ndgcondition.AlgebraicNDGCondition;
import com.ogprover.pp.tp.thmstatement.AlgebraicSumOfThreeAngles;
import com.ogprover.pp.tp.thmstatement.AlgebraicSumOfThreeSegments;
import com.ogprover.pp.tp.thmstatement.CollinearPoints;
import com.ogprover.pp.tp.thmstatement.ConcurrentCircles;
import com.ogprover.pp.tp.thmstatement.ConcurrentLines;
import com.ogprover.pp.tp.thmstatement.ConcyclicPoints;
import com.ogprover.pp.tp.thmstatement.CongruentTriangles;
import com.ogprover.pp.tp.thmstatement.EqualAngles;
import com.ogprover.pp.tp.thmstatement.EqualityOfRatioProducts;
import com.ogprover.pp.tp.thmstatement.EqualityOfTwoRatios;
import com.ogprover.pp.tp.thmstatement.False;
import com.ogprover.pp.tp.thmstatement.FourHarmonicConjugatePoints;
import com.ogprover.pp.tp.thmstatement.IdenticalPoints;
import com.ogprover.pp.tp.thmstatement.LinearCombinationOfDoubleSignedPolygonAreas;
import com.ogprover.pp.tp.thmstatement.LinearCombinationOfOrientedSegments;
import com.ogprover.pp.tp.thmstatement.PointOnSetOfPoints;
import com.ogprover.pp.tp.thmstatement.RatioOfOrientedSegments;
import com.ogprover.pp.tp.thmstatement.RatioOfTwoSegments;
import com.ogprover.pp.tp.thmstatement.SegmentsOfEqualLengths;
import com.ogprover.pp.tp.thmstatement.SimilarTriangles;
import com.ogprover.pp.tp.thmstatement.TouchingCircles;
import com.ogprover.pp.tp.thmstatement.True;
import com.ogprover.pp.tp.thmstatement.TwoInversePoints;
import com.ogprover.pp.tp.thmstatement.TwoParallelLines;
import com.ogprover.pp.tp.thmstatement.TwoPerpendicularLines;
import com.ogprover.thmprover.AlgebraicMethodProver;
import com.ogprover.thmprover.TheoremProver;
import com.ogprover.thmprover.WuMethodProver;
import com.ogprover.utilities.OGPTimer;
import com.ogprover.utilities.OGPUtilities;
import com.ogprover.utilities.Stopwatch;
import com.ogprover.utilities.io.LaTeXFileWriter;
import com.ogprover.utilities.io.OGPOutput;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for manual testing of Construction Protocol</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class MTestCP {
	
	/*
	 * BASE METHODS - BEGIN
	 */
	
	/**
	 * Testing transformation of constructions to algebraic form.
	 * 
	 * @param cp	Construction Protocol
	 */
	public static void testConstructions(OGPTP cp) {
		// Here statement is not important, therefore set it to default statement
		cp.addThmStatement(new True(cp));
		
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("===========================================");
		System.out.println("===========================================");
		System.out.println();
		System.out.println("Constructions from CP for " + cp.getTheoremName() + " thm ...");
		System.out.println();
		
		/*
		 * Validation of CP
		 */
		System.out.println();
		System.out.println("Validation of CP for " + cp.getTheoremName() + " thm ...");
		if (!cp.isValid()) {
			System.out.println("CP is not valid!");
			return;
		}
		System.out.println("CP is valid");
		System.out.println();
		
		System.out.println("Transformation of CP for " + cp.getTheoremName() + " thm to algebraic form (will start measuring the time) ...");
		OpenGeoProver.settings.getStopwacth().startMeasureTime();
		int retCode = cp.convertToAlgebraicForm();
		OpenGeoProver.settings.getStopwacth().endMeasureTime();
		System.out.println();
		System.out.println("Time in seconds spent for transformation is: " + OGPUtilities.roundUpToPrecision(OpenGeoProver.settings.getStopwacth().getTimeIntSec()) + " sec");
		
		System.out.println();
		if (retCode != OGPConstants.RET_CODE_SUCCESS) {
			System.out.println("Transformation was unsuccessful");
			return;
		}
		System.out.println("Points with assigned coordinates");
		for (GeoConstruction cons : cp.getConstructionSteps()) {
			if (cons instanceof Point) {
				Point P = (Point)cons;
				System.out.println(cons.getGeoObjectLabel() + "(" + P.getX().printToLaTeX() + ", " + P.getY().printToLaTeX() + ")");
			}
		}
		System.out.println();
		System.out.println("System of polynomials for hypotheses is: ");
		int ii=1;
		for (XPolynomial xp : cp.getAlgebraicGeoTheorem().getHypotheses().getPolynomials()) {
			System.out.println("Polynomial #" + ii);
			String[] strArr = xp.printToLaTeX().split("\\$\\$");
			for (String str : strArr)
				System.out.println(str);
			System.out.println();
			ii++;
		}
		System.out.println();
	}
	
	/**
	 * Method for testing theorem proving.
	 * 
	 * @param cp	Construction Protocol
	 */
	public static void testTheorem(OGPTP cp) {
		String[] strArr;
		
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("===========================================");
		System.out.println("===========================================");
		System.out.println();
		System.out.println("Construction of CP for " + cp.getTheoremName() + " thm ...");
		System.out.println();
		
		/*
		 * Validation of CP
		 */
		System.out.println();
		System.out.println("Validation of CP for " + cp.getTheoremName() + " thm ...");
		if (!cp.isValid()) {
			System.out.println("CP is not valid!");
			return;
		}
		System.out.println("CP is valid");
		System.out.println();
		
		System.out.println("Transformation of CP for " + cp.getTheoremName() + " thm to algebraic form (will start measuring the time) ...");
		OpenGeoProver.settings.getStopwacth().startMeasureTime();
		int retCode = cp.convertToAlgebraicForm();
		OpenGeoProver.settings.getStopwacth().endMeasureTime();
		System.out.println();
		System.out.println("Time in seconds spent for transformation is: " + OGPUtilities.roundUpToPrecision(OpenGeoProver.settings.getStopwacth().getTimeIntSec()) + " sec");
		
		System.out.println();
		if (retCode != OGPConstants.RET_CODE_SUCCESS) {
			System.out.println("Transformation was unsuccessful");
			return;
		}
		System.out.println("Points with assigned coordinates");
		for (GeoConstruction cons : cp.getConstructionSteps()) {
			if (cons instanceof Point) {
				Point P = (Point)cons;
				System.out.println(cons.getGeoObjectLabel() + "(" + P.getX().printToLaTeX() + ", " + P.getY().printToLaTeX() + ")");
			}
		}
		System.out.println();
		System.out.println("System of polynomials for hypotheses is: ");
		int ii=1;
		for (XPolynomial xp : cp.getAlgebraicGeoTheorem().getHypotheses().getPolynomials()) {
			System.out.println("Polynomial #" + ii);
			strArr = xp.printToLaTeX().split("\\$\\$");
			for (String str : strArr)
				System.out.println(str);
			System.out.println();
			ii++;
		}
		System.out.println();
		System.out.println("Polynomial for theorem statement is: ");
		strArr = cp.getAlgebraicGeoTheorem().getStatement().printToLaTeX().split("\\$\\$");
		for (String str : strArr)
			System.out.println(str);
		System.out.println();
		
		// Invoking Wu's prover
		System.out.println();
		System.out.println();
		System.out.println("===========================================");
		System.out.println();
		System.out.println("--------- Invoking Wu's prover ------------");
		System.out.println();
		
		AlgebraicMethodProver proverMethod = new WuMethodProver(cp.getAlgebraicGeoTheorem());
		OGPTimer timer = OpenGeoProver.settings.getTimer();
		OGPParameters parameters = OpenGeoProver.settings.getParameters();
		Stopwatch stopwatch = OpenGeoProver.settings.getStopwacth();
		
		timer.setTimer(parameters.getTimeLimit()); // setting timer
		stopwatch.startMeasureTime();
		retCode = proverMethod.prove();
		stopwatch.endMeasureTime();
		timer.cancel(); // cancel the timer
		// set new timer
		OpenGeoProver.settings.setTimer(new OGPTimer());
		
		switch (retCode) {
		case TheoremProver.THEO_PROVE_RET_CODE_FALSE:
			System.out.println("Theorem has been disproved.");
			break;
		case TheoremProver.THEO_PROVE_RET_CODE_TRUE:
			System.out.println("Theorem has been proved.");
			break;
		case TheoremProver.THEO_PROVE_RET_CODE_UNKNOWN:
			System.out.println("Theorem can't be neither proved nor disproved.");
			break;
		case OGPConstants.ERR_CODE_GENERAL:
			System.out.println("Error happened - general error.");
			break;
		case OGPConstants.ERR_CODE_NULL:
			System.out.println("Error happened - null pointer error.");
			break;
		case OGPConstants.ERR_CODE_SPACE:
			System.out.println("Error happened - huge polynomial has been obtained.");
			break;
		case OGPConstants.ERR_CODE_TIME:
			System.out.println("Error happened - time has expired.");
			break;
		}
		System.out.println();
		System.out.println("Time spent by the prover is ");
		System.out.print(OGPUtilities.roundUpToPrecision(stopwatch.getTimeIntSec()));
		System.out.print(" seconds.");
		System.out.println();
		System.out.println("The biggest polynomial obtained during prover execution contains ");
		System.out.print(OpenGeoProver.settings.getMaxNumOfTerms());
		System.out.print(" terms.");
		System.out.println();
		
		// NDG Conditions in algebraic form
		System.out.println();
		System.out.println();
		System.out.println("-- NDG Conditions --");
		System.out.println();
		stopwatch.startMeasureTime();
		ii = 1;
		for (XPolynomial xp : cp.getAlgebraicGeoTheorem().getNDGConditions().getPolynomials()) {
			System.out.println("NDG Condition #" + ii);
			strArr = xp.printToLaTeX().split("\\$\\$");
			for (String str : strArr)
				System.out.println(str);
			System.out.println();
			ii++;
		}
		System.out.println();
		System.out.println();
		
		// NDG Conditions in readable form
		System.out.println();
		System.out.println();
		System.out.println("-- NDG Conditions as text --");
		System.out.println();
		for (XPolynomial xp : cp.getAlgebraicGeoTheorem().getNDGConditions().getPolynomials()) {
			cp.addAlgebraicNDGCondition(new AlgebraicNDGCondition(xp));
		}
		ii = 1;
		cp.translateNDGConditionsToUserReadableForm();
		for (AlgebraicNDGCondition ndgc : cp.getAlgebraicNDGConditions()) {
			System.out.println("Text NDG Condition #" + ii);
			
			// Printing all descriptions
			/*
			Vector<String> ndgcText = ndgc.getText();
			if (ndgcText == null || ndgcText.size() == 0) {
				strArr = ndgc.getPolynomial().printToLaTeX().split("\\$\\$");
				for (String str : strArr)
					System.out.println(str);
			}
			else {
				for (String ndgcStr : ndgcText)
					System.out.println(ndgcStr);
			}
			*/
			
			//Printing just best descriptions
			String ndgcText = ndgc.getBestDescription();
			if (ndgcText == null || ndgcText.length() == 0) {
				strArr = ndgc.getPolynomial().printToLaTeX().split("\\$\\$");
				for (String str : strArr)
					System.out.println(str);
			}
			else {
				System.out.println(ndgcText);
			}
			
			System.out.println();
			ii++;
			
			/*
			 * Temp - START
			 */
			/*
			for (Vector<Point> points : ndgc.getPointLists()) {
				for (Point p : points)
					System.out.print(p.getGeoObjectLabel() + " ");
				System.out.println();
			}
			System.out.println();
			*/
			/*
			 * Temp - END
			 */
		}
		stopwatch.endMeasureTime();
		System.out.println();
		System.out.println();
		
		System.out.println();
		System.out.println("Time spent for processing NDG conditions is ");
		System.out.print(OGPUtilities.roundUpToPrecision(stopwatch.getTimeIntSec()));
		System.out.print(" seconds.");
		System.out.println();
		
		// reset maximal number of terms so it could be prepared for next execution
		OpenGeoProver.settings.setMaxNumOfTerms(0);
	}
	/*
	 * BASE METHODS - END
	 */
	
	
	/**
	 * Testing calculation of first derivative of symbolic polynomials
	 * for conditions for some point sets.
	 */
	public static void testDerivation() {
		String strCond;
		String[] strArr;
		
		System.out.println();
		System.out.println();
		System.out.println("================ TESTING SYMBOLIC DERIVATION =================");
		System.out.println();
		System.out.println("Condition for circumscribed circle is: ");
		strCond = Circle.conditionForCircumscribedCircle.printToLaTeX();
		strArr = strCond.split("\\$\\$");
		for (String str : strArr)
			System.out.println(str);
		System.out.println("List of point labels is:");
		for (String pointLabel : Circle.conditionForCircumscribedCircle.getAllPointLabels())
			System.out.print(pointLabel + " ");
		System.out.println();
		System.out.println("its first derivative by M0 is: ");
		ArrayList<SymbolicPolynomial> derivativeFraction = ((SymbolicPolynomial)Circle.conditionForCircumscribedCircle.clone()).calcFirstDerivativeByPoint("0");
		System.out.println("Numerator = ");
		strCond = derivativeFraction.get(SymbolicPolynomial.FIRST_DERIVATIVE_NUMERATOR).printToLaTeX();
		strArr = strCond.split("\\$\\$");
		for (String str : strArr)
			System.out.println(str);
		System.out.println("Denominator = ");
		strCond = derivativeFraction.get(SymbolicPolynomial.FIRST_DERIVATIVE_DENOMINATOR).printToLaTeX();
		strArr = strCond.split("\\$\\$");
		for (String str : strArr)
			System.out.println(str);
		System.out.println();
		System.out.println("Condition for plain circle is: ");
		System.out.println(Circle.conditionForCircleWithCenterAndPoint.printToLaTeX());
		System.out.println("List of point labels is:");
		for (String pointLabel : Circle.conditionForCircleWithCenterAndPoint.getAllPointLabels())
			System.out.print(pointLabel + " ");
		System.out.println();
		System.out.println("its first derivative by M0 is: ");
		derivativeFraction = ((SymbolicPolynomial)Circle.conditionForCircleWithCenterAndPoint.clone()).calcFirstDerivativeByPoint("0");
		System.out.println("Numerator = " + derivativeFraction.get(SymbolicPolynomial.FIRST_DERIVATIVE_NUMERATOR).printToLaTeX());
		System.out.println("Denominator = " + derivativeFraction.get(SymbolicPolynomial.FIRST_DERIVATIVE_DENOMINATOR).printToLaTeX());
		System.out.println();
		System.out.println("Condition for plain line is: ");
		System.out.println(LineThroughTwoPoints.conditionForPlainLine.printToLaTeX());
		System.out.println("List of point labels is:");
		for (String pointLabel : LineThroughTwoPoints.conditionForPlainLine.getAllPointLabels())
			System.out.print(pointLabel + " ");
		System.out.println();
		System.out.println("its first derivative by M0 is: ");
		derivativeFraction = ((SymbolicPolynomial)LineThroughTwoPoints.conditionForPlainLine.clone()).calcFirstDerivativeByPoint("0");
		System.out.println("Numerator = " + derivativeFraction.get(SymbolicPolynomial.FIRST_DERIVATIVE_NUMERATOR).printToLaTeX());
		System.out.println("Denominator = " + derivativeFraction.get(SymbolicPolynomial.FIRST_DERIVATIVE_DENOMINATOR).printToLaTeX());
	}
	
	
	
	/*
	 * TESTING TRANSFORMATIONS OF CONSTRUCTIONS TO ALGEBRAIC FORM - BEGIN
	 */
	// example of Butterfly theorem
	public static void testConstructionTransformationToAlgebraicForm1() {
		OGPTP cp = new OGPTP();
		cp.setTheoremName("Butterfly");
		
		// Constructions for the Butterfly theorem
		/*
		 * A, B, C and D are four points on one circle whose center is O.
		 * E is intersection of AC and BD. Through E draw line perpendicular
		 * to OE, meeting AD at F and BC at G. Show that FE = GE.
		 */
		Point pointO = new FreePoint("O");
		cp.addGeoConstruction(pointO);
		Point pointA = new FreePoint("A");
		cp.addGeoConstruction(pointA);
		Circle circlek = new CircleWithCenterAndPoint("k", pointO, pointA);
		cp.addGeoConstruction(circlek);
		Point pointB = new RandomPointFromCircle("B", circlek);
		cp.addGeoConstruction(pointB);
		Point pointC = new RandomPointFromCircle("C", circlek);
		cp.addGeoConstruction(pointC);
		Point pointD = new RandomPointFromCircle("D", circlek);
		cp.addGeoConstruction(pointD);
		Line ac = new LineThroughTwoPoints("ac", pointA, pointC);
		cp.addGeoConstruction(ac);
		Line bd = new LineThroughTwoPoints("bd", pointB, pointD);
		cp.addGeoConstruction(bd);
		Point pointE = new IntersectionPoint("E", ac, bd);
		cp.addGeoConstruction(pointE);
		Line oe = new LineThroughTwoPoints("oe", pointO, pointE);
		cp.addGeoConstruction(oe);
		Line ne = new PerpendicularLine("ne", oe, pointE);
		cp.addGeoConstruction(ne);
		Line ad = new LineThroughTwoPoints("ad", pointA, pointD);
		cp.addGeoConstruction(ad);
		Point pointF = new IntersectionPoint("F", ad, ne);
		cp.addGeoConstruction(pointF);
		Line bc = new LineThroughTwoPoints("bc", pointB, pointC);
		cp.addGeoConstruction(bc);
		Point pointG = new IntersectionPoint("G", bc, ne);
		cp.addGeoConstruction(pointG);
		
		MTestCP.testConstructions(cp);
	}
	
	// custom example
	public static void testConstructionTransformationToAlgebraicForm2() {
		OGPTP cp = new OGPTP();
		cp.setTheoremName("Square, circle and tangent line");
		
		/*
		 * ABCD is square and E is midpoint of edge AD. 
		 * Let k be the circle with center A and that 
		 * contains point E, and t is tangent line from 
		 * vertex C to circle k. Let F be the point of 
		 * intersection of tangent t and line AD.
		 */
		Point pointA = new FreePoint("A");
		cp.addGeoConstruction(pointA);
		Point pointB = new FreePoint("B");
		cp.addGeoConstruction(pointB);
		Point pointC = new RotatedPoint("C", pointA, pointB, 90);
		cp.addGeoConstruction(pointC);
		Line AB = new LineThroughTwoPoints("AB", pointA, pointB);
		cp.addGeoConstruction(AB);
		Line BC = new LineThroughTwoPoints("BC", pointB, pointC);
		cp.addGeoConstruction(BC);
		Line nA = new PerpendicularLine("nA", AB, pointA);
		cp.addGeoConstruction(nA);
		Line nC = new PerpendicularLine("nC", BC, pointC);
		cp.addGeoConstruction(nC);
		Point pointD = new IntersectionPoint("D", nA, nC);
		cp.addGeoConstruction(pointD);
		Point pointE = new MidPoint("E", pointA, pointD);
		cp.addGeoConstruction(pointE);
		Circle k = new CircleWithCenterAndPoint("k", pointA, pointE);
		cp.addGeoConstruction(k);
		Line tC = new TangentLine("tC", pointC, k);
		cp.addGeoConstruction(tC);
		Line AD = new LineThroughTwoPoints("AD", pointA, pointD);
		cp.addGeoConstruction(AD);
		Point pointF = new IntersectionPoint("F", tC, AD);
		cp.addGeoConstruction(pointF);
		
		MTestCP.testConstructions(cp);
	}
	
	// example for inverse of point
	public static void testConstructionTransformationToAlgebraicForm3() {
		OGPTP cp = new OGPTP();
		cp.setTheoremName("Inverse point");
		
		/*
		 * k is circle with center O which passes through point A.
		 * B is random point from circle k and C is random point from 
		 * line AB. D is inverse point of point C with respect to circle k.
		 */
		Point pointO = new FreePoint("O");
		cp.addGeoConstruction(pointO);
		Point pointA = new FreePoint("A");
		cp.addGeoConstruction(pointA);
		Circle k = new CircleWithCenterAndPoint("k", pointO, pointA);
		cp.addGeoConstruction(k);
		Point pointB = new RandomPointFromCircle("B", k);
		cp.addGeoConstruction(pointB);
		Line ab = new LineThroughTwoPoints("ab", pointA, pointB);
		cp.addGeoConstruction(ab);
		Point pointC = new RandomPointFromLine("C", ab);
		cp.addGeoConstruction(pointC);
		Point pointD = new InverseOfPoint("D", pointC, k);
		cp.addGeoConstruction(pointD);
		
		MTestCP.testConstructions(cp);
	}
	
	// example of square theorem
	public static void testConstructionTransformationToAlgebraicForm4() {
		OGPTP cp = new OGPTP();
		cp.setTheoremName("Square with reflexion about line");
		
		/*
		 * Construction of square ABCD in following way:
		 * A and B are arbitrary points, C is rotated point A
		 * around B for right angle and D is point symmetric
		 * to B with respect to line AC.
		 */
		Point pointA = new FreePoint("A");
		cp.addGeoConstruction(pointA);
		Point pointB = new FreePoint("B");
		cp.addGeoConstruction(pointB);
		Point pointC = new RotatedPoint("C", pointA, pointB, 90);
		cp.addGeoConstruction(pointC);
		Line AC = new LineThroughTwoPoints("AC", pointA, pointC);
		cp.addGeoConstruction(AC);
		ShortcutConstruction pointD = new ReflectedPoint("D", pointB, AC);
		cp.addGeoConstruction(pointD);
		
		MTestCP.testConstructions(cp);
	}
	/*
	 * TESTING TRANSFORMATIONS OF CONSTRUCTIONS TO ALGEBRAIC FORM - END
	 */
	
	
	
	/*
	 * TESTING PROVING OF SOME THEOREMS - BEGIN
	 */
	/**
	 * Method for proving of a theorem.
	 * 
	 * @param theoNum	Index of theorem in list of examples from bellow method
	 * 					that gets prepared CP for proving 
	 */
	public static void testTheoremProving(int theoNum) {
		if (theoNum == 27) {
			OpenGeoProver.settings.getParameters().putTimeLimit(200000);
			OpenGeoProver.settings.getParameters().putSpaceLimit(20000);
			
			MTestCP.testTheorem(MTestCP.getPreparedCPForTheorem(27));
			
			OpenGeoProver.settings.getParameters().putTimeLimit(Long.parseLong(OGPConstants.DEF_VAL_PARAM_TIME_LIMIT));
			OpenGeoProver.settings.getParameters().putSpaceLimit(Integer.parseInt(OGPConstants.DEF_VAL_PARAM_SPACE_LIMIT));
		}
		else 
			MTestCP.testTheorem(MTestCP.getPreparedCPForTheorem(theoNum));
	}
	
	/**
	 * Returns Construction Protocol prepared with all construction steps
	 * and theorem statement of the theorem to be proved.
	 * 
	 * @param theoNum	Index of a theorem in list of theorems inside this method
	 * @return			Prepared CP
	 */
	public static OGPTP getPreparedCPForTheorem(int theoNum) {
		OGPTP cp = new OGPTP();
		
		switch(theoNum) {
		case 1:
			{
				cp.setTheoremName("Desargues");
			
				/*
				 * Desargues theorem: Let A1B1C1 and A2B2C2 are two perspective triangles,
				 * i.e. lines A1A2, B1B2 and C1C2 are concurrent and meet at point S.
				 * Let have following intersections: M = A1B1 x A2B2, N = B1C1 x B2C2 and
				 * P = C1A1 x C2A2. Then points M, N and P are collinear.  
				 */
				// constructions
				Point pointS = new FreePoint("S");
				cp.addGeoConstruction(pointS);
				Point pointA1 = new FreePoint("A1");
				cp.addGeoConstruction(pointA1);
				Point pointB1 = new FreePoint("B1");
				cp.addGeoConstruction(pointB1);
				Point pointC1 = new FreePoint("C1");
				cp.addGeoConstruction(pointC1);
				Line a = new LineThroughTwoPoints("a", pointS, pointA1);
				cp.addGeoConstruction(a);
				Line b = new LineThroughTwoPoints("b", pointS, pointB1);
				cp.addGeoConstruction(b);
				Line c = new LineThroughTwoPoints("c", pointS, pointC1);
				cp.addGeoConstruction(c);
				Point pointA2 = new RandomPointFromLine("A2", a);
				cp.addGeoConstruction(pointA2);
				Point pointB2 = new RandomPointFromLine("B2", b);
				cp.addGeoConstruction(pointB2);
				Point pointC2 = new RandomPointFromLine("C2", c);
				cp.addGeoConstruction(pointC2);
				Line c1 = new LineThroughTwoPoints("c1", pointA1, pointB1);
				cp.addGeoConstruction(c1);
				Line c2 = new LineThroughTwoPoints("c2", pointA2, pointB2);
				cp.addGeoConstruction(c2);
				Point pointM = new IntersectionPoint("M", c1, c2);
				cp.addGeoConstruction(pointM);
				Line a1 = new LineThroughTwoPoints("a1", pointB1, pointC1);
				cp.addGeoConstruction(a1);
				Line a2 = new LineThroughTwoPoints("a2", pointB2, pointC2);
				cp.addGeoConstruction(a2);
				Point pointN = new IntersectionPoint("N", a1, a2);
				cp.addGeoConstruction(pointN);
				Line b1 = new LineThroughTwoPoints("b1", pointC1, pointA1);
				cp.addGeoConstruction(b1);
				Line b2 = new LineThroughTwoPoints("b2", pointC2, pointA2);
				cp.addGeoConstruction(b2);
				Point pointP = new IntersectionPoint("P", b1, b2);
				cp.addGeoConstruction(pointP);
				// statement
				ArrayList<Point> collPointsList = new ArrayList<Point>();
				collPointsList.add(pointM);
				collPointsList.add(pointN);
				collPointsList.add(pointP);
				cp.addThmStatement(new CollinearPoints(collPointsList));
			}
			break;
		case 2:
			{
				cp.setTheoremName("Honsberger's");
			
				/*
				 * Honsberger's theorem (http://mathworld.wolfram.com/Collinear.html)
				 * 
				 * Let M, N and P are three collinear points each lies on different line 
				 * determined by edges of triangle ABC: M in AB, N in BC and P in CA. 
				 * Let M', N' and P' are points central symmetric to M, N and P respectively 
				 * with respect to midpoints of corresponding triangle's edges. 
				 * Then these three points are collinear too.  
				 */
				// constructions
				Point pointA = new FreePoint("A");
				cp.addGeoConstruction(pointA);
				Point pointB = new FreePoint("B");
				cp.addGeoConstruction(pointB);
				Point pointC = new FreePoint("C");
				cp.addGeoConstruction(pointC);
				Line c = new LineThroughTwoPoints("c", pointA, pointB);
				cp.addGeoConstruction(c);
				Line a = new LineThroughTwoPoints("a", pointB, pointC);
				cp.addGeoConstruction(a);
				Line b = new LineThroughTwoPoints("b", pointC, pointA);
				cp.addGeoConstruction(b);
				Point pointM = new RandomPointFromLine("M", c);
				cp.addGeoConstruction(pointM);
				Point pointN = new RandomPointFromLine("N", a);
				cp.addGeoConstruction(pointN);
				Line l = new LineThroughTwoPoints("l", pointM, pointN);
				cp.addGeoConstruction(l);
				Point pointP = new IntersectionPoint("P", l, b);
				cp.addGeoConstruction(pointP);
				Point pointC1 = new MidPoint("C1", pointA, pointB);
				cp.addGeoConstruction(pointC1);
				Point pointA1 = new MidPoint("A1", pointB, pointC);
				cp.addGeoConstruction(pointA1);
				Point pointB1 = new MidPoint("B1", pointC, pointA);
				cp.addGeoConstruction(pointB1);
				Point pointMp = new CentralSymmetricPoint("M'", pointM, pointC1);
				cp.addGeoConstruction(pointMp);
				Point pointNp = new CentralSymmetricPoint("N'", pointN, pointA1);
				cp.addGeoConstruction(pointNp);
				Point pointPp = new CentralSymmetricPoint("P'", pointP, pointB1);
				cp.addGeoConstruction(pointPp);
				// statement
				ArrayList<Point> collPointsList = new ArrayList<Point>();
				collPointsList.add(pointMp);
				collPointsList.add(pointNp);
				collPointsList.add(pointPp);
				cp.addThmStatement(new CollinearPoints(collPointsList));
			}
			break;
		case 3:
			{
				cp.setTheoremName("Circumscribed circle");
				
				/*
				 * Let k be the circle with center O and point A and 
				 * let B, C and D are some points on that circle.
				 * Then D belongs to circumscribed circle of triangle ABC.
				 */
				// constructions
				Point pointO = new FreePoint("O");
				cp.addGeoConstruction(pointO);
				Point pointA = new FreePoint("A");
				cp.addGeoConstruction(pointA);
				Circle k = new CircleWithCenterAndPoint("k", pointO, pointA);
				cp.addGeoConstruction(k);
				Point pointB = new RandomPointFromCircle("B", k);
				cp.addGeoConstruction(pointB);
				Point pointC = new RandomPointFromCircle("C", k);
				cp.addGeoConstruction(pointC);
				Point pointD = new RandomPointFromCircle("D", k);
				cp.addGeoConstruction(pointD);
				// statement
				Circle l = new CircumscribedCircle("l", pointA, pointB, pointC);
				cp.addGeoConstruction(l);
				cp.addThmStatement(new PointOnSetOfPoints(l, pointD));
			}
			break;
		case 4:
			{
				cp.setTheoremName("Simson's Line (1) - usage of circumscribed circle");
				
				/*
				 * Simson's Line:
				 * 
				 * Let M be any point of circumcircle of triangle ABC.
				 * Let A', B' and C' are respectively foots of perpendiculars
				 * from M to lines determined by following edges of triangle:
				 * BC, CA and AB. Then points A', B' and C' are collinear.
				 */
				// constructions
				Point pointA = new FreePoint("A");
				cp.addGeoConstruction(pointA);
				Point pointB = new FreePoint("B");
				cp.addGeoConstruction(pointB);
				Point pointC = new FreePoint("C");
				cp.addGeoConstruction(pointC);
				Line c = new LineThroughTwoPoints("c", pointA, pointB);
				cp.addGeoConstruction(c);
				Line a = new LineThroughTwoPoints("a", pointB, pointC);
				cp.addGeoConstruction(a);
				Line b = new LineThroughTwoPoints("b", pointC, pointA);
				cp.addGeoConstruction(b);
				Circle k = new CircumscribedCircle("k", pointA, pointB, pointC);
				cp.addGeoConstruction(k);
				Point pointM = new RandomPointFromCircle("M", k);
				cp.addGeoConstruction(pointM);
				ShortcutConstruction pointAp = new FootPoint("A'", pointM, a);
				cp.addGeoConstruction(pointAp);
				ShortcutConstruction pointBp = new FootPoint("B'", pointM, b);
				cp.addGeoConstruction(pointBp);
				ShortcutConstruction pointCp = new FootPoint("C'", pointM, c);
				cp.addGeoConstruction(pointCp);
				// statement
				ArrayList<Point> collPointsList = new ArrayList<Point>();
				collPointsList.add(pointAp.getPoint());
				collPointsList.add(pointBp.getPoint());
				collPointsList.add(pointCp.getPoint());
				cp.addThmStatement(new CollinearPoints(collPointsList));
			}
			break;
		case 5:
			{
				cp.setTheoremName("Simson's Line (2) - usage of circle with center and point");
				
				/*
				 * Simson's Line:
				 * 
				 * Let M be any point of circumcircle of triangle ABC.
				 * Let A', B' and C' are respectively foots of perpendiculars
				 * from M to lines determined by following edges of triangle:
				 * BC, CA and AB. Then points A', B' and C' are collinear.
				 */
				// constructions
				Point pointO = new FreePoint("O");
				cp.addGeoConstruction(pointO);
				Point pointA = new FreePoint("A");
				cp.addGeoConstruction(pointA);
				Circle k = new CircleWithCenterAndPoint("k", pointO, pointA);
				cp.addGeoConstruction(k);
				Point pointB = new RandomPointFromCircle("B", k);
				cp.addGeoConstruction(pointB);
				Point pointC = new RandomPointFromCircle("C", k);
				cp.addGeoConstruction(pointC);
				Line c = new LineThroughTwoPoints("c", pointA, pointB);
				cp.addGeoConstruction(c);
				Line a = new LineThroughTwoPoints("a", pointB, pointC);
				cp.addGeoConstruction(a);
				Line b = new LineThroughTwoPoints("b", pointC, pointA);
				cp.addGeoConstruction(b);
				Point pointM = new RandomPointFromCircle("M", k);
				cp.addGeoConstruction(pointM);
				ShortcutConstruction pointAp = new FootPoint("A'", pointM, a);
				cp.addGeoConstruction(pointAp);
				ShortcutConstruction pointBp = new FootPoint("B'", pointM, b);
				cp.addGeoConstruction(pointBp);
				ShortcutConstruction pointCp = new FootPoint("C'", pointM, c);
				cp.addGeoConstruction(pointCp);
				// statement
				ArrayList<Point> collPointsList = new ArrayList<Point>();
				collPointsList.add(pointAp.getPoint());
				collPointsList.add(pointBp.getPoint());
				collPointsList.add(pointCp.getPoint());
				cp.addThmStatement(new CollinearPoints(collPointsList));
			}
			break;
		case 6:
			{
				cp.setTheoremName("Pascal's theorem for ellipse/hyperbola");
				
				/*
				 * Pascal's theorem:
				 * 
				 * Let Eps is ellipse and A, B, C, D, E and F are 6 points lying
				 * on that ellipse. Then intersections of opposite edges of hexagon
				 * ABCDEF are 3 collinear points. In other words:
				 * Let M, N and P are respectively intersection points of following lines:
				 * M = AB x DE, N = BC x EF, P = CD x FA. Then M, N and P are collinear points.
				 */
				// There is example of this theorem for general conic section - example 57.
			}
			break;
		case 7:
			{
				cp.setTheoremName("Pascal's theorem for circle");
				
				/*
				 * Pascal's theorem:
				 * 
				 * Let k is circle and A, B, C, D, E and F are 6 points lying
				 * on that circle. Then intersections of opposite edges of hexagon
				 * ABCDEF are 3 collinear points. In other words:
				 * Let M, N and P are respectively intersection points of following lines:
				 * M = AB x DE, N = BC x EF, P = CD x FA. Then M, N and P are collinear points.
				 */
				// constructions
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Point C = new FreePoint("C");
				cp.addGeoConstruction(C);
				Circle k = new CircumscribedCircle("k", A, B, C);
				cp.addGeoConstruction(k);
				Point D = new RandomPointFromCircle("D", k);
				cp.addGeoConstruction(D);
				Point E = new RandomPointFromCircle("E", k);
				cp.addGeoConstruction(E);
				Point F = new RandomPointFromCircle("F", k);
				cp.addGeoConstruction(F);
				Line ab = new LineThroughTwoPoints("AB", A, B);
				cp.addGeoConstruction(ab);
				Line de = new LineThroughTwoPoints("DE", D, E);
				cp.addGeoConstruction(de);
				Line bc = new LineThroughTwoPoints("BC", B, C);
				cp.addGeoConstruction(bc);
				Line ef = new LineThroughTwoPoints("EF", E, F);
				cp.addGeoConstruction(ef);
				Line cd = new LineThroughTwoPoints("CD", C, D);
				cp.addGeoConstruction(cd);
				Line fa = new LineThroughTwoPoints("FA", F, A);
				cp.addGeoConstruction(fa);
				Point M = new IntersectionPoint("M", ab, de);
				cp.addGeoConstruction(M);
				Point N = new IntersectionPoint("N", bc, ef);
				cp.addGeoConstruction(N);
				Point P = new IntersectionPoint("P", cd, fa);
				cp.addGeoConstruction(P);
				// statement
				ArrayList<Point> collPointsList = new ArrayList<Point>();
				collPointsList.add(M);
				collPointsList.add(N);
				collPointsList.add(P);
				cp.addThmStatement(new CollinearPoints(collPointsList));
			}
			break;
		case 8:
			{
				cp.setTheoremName("Euler's theorem about nine points circle");
				
				/*
				 * Euler's theorem:
				 * 
				 * Let ABC be a triangle and A', B' and C' are foots of triangle's altitudes
				 * (perpendicular lines from triangle vertices to opposite edges).
				 * Let H be the orthocenter of that triangle and A2, B2 and C2
				 * are midpoints of segments AH, BH and CH respectively,
				 * while A1, B1 and C1 are midpoints of triangle's edges
				 * BC, CA and AB. Then points A', B', C', A1, B1, C1, A2, B2 and C2
				 * are all in same circle (Euler's circle or circle of nine points 
				 * of triangle).
				 * 
				 * (Note: it is sufficient to show that points A' and A2 are on
				 * circumscribed circle of triangle A1B1C1, i.e. that points 
				 * A1, B1, C1, A' and A2 are concyclic points).
				 */
				// constructions
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Point C = new FreePoint("C");
				cp.addGeoConstruction(C);
				Point A1 = new MidPoint("A1", B, C);
				cp.addGeoConstruction(A1);
				Point B1 = new MidPoint("B1", C, A);
				cp.addGeoConstruction(B1);
				Point C1 = new MidPoint("C1", A, B);
				cp.addGeoConstruction(C1);
				Line a = new LineThroughTwoPoints("a", B, C);
				cp.addGeoConstruction(a);
				Line b = new LineThroughTwoPoints("b", C, A);
				cp.addGeoConstruction(b);
				Line ha = new PerpendicularLine("ha", a, A);
				cp.addGeoConstruction(ha);
				Line hb = new PerpendicularLine("hb", b, B);
				cp.addGeoConstruction(hb);
				Point H = new IntersectionPoint("H", ha, hb);
				cp.addGeoConstruction(H);
				Point Ap = new IntersectionPoint("A'", ha, a);
				cp.addGeoConstruction(Ap);
				Point A2 = new MidPoint("A2", A, H);
				cp.addGeoConstruction(A2);
				// statement
				ArrayList<Point> collPointsList = new ArrayList<Point>();
				collPointsList.add(A1);
				collPointsList.add(B1);
				collPointsList.add(C1);
				collPointsList.add(Ap);
				collPointsList.add(A2);
				cp.addThmStatement(new ConcyclicPoints(collPointsList));
			}
			break;
		case 9:
			{
				cp.setTheoremName("Butterfly");
				
				// Constructions for the Butterfly theorem
				/*
				 * A, B, C and D are four points on one circle whose center is O.
				 * E is intersection of AC and BD. Through E draw line perpendicular
				 * to OE, meeting AD at F and BC at G. Show that FE = GE.
				 */
				Point pointO = new FreePoint("O");
				cp.addGeoConstruction(pointO);
				Point pointA = new FreePoint("A");
				cp.addGeoConstruction(pointA);
				Circle circlek = new CircleWithCenterAndPoint("k", pointO, pointA);
				cp.addGeoConstruction(circlek);
				Point pointB = new RandomPointFromCircle("B", circlek);
				cp.addGeoConstruction(pointB);
				Point pointC = new RandomPointFromCircle("C", circlek);
				cp.addGeoConstruction(pointC);
				Point pointD = new RandomPointFromCircle("D", circlek);
				cp.addGeoConstruction(pointD);
				Line ac = new LineThroughTwoPoints("ac", pointA, pointC);
				cp.addGeoConstruction(ac);
				Line bd = new LineThroughTwoPoints("bd", pointB, pointD);
				cp.addGeoConstruction(bd);
				Point pointE = new IntersectionPoint("E", ac, bd);
				cp.addGeoConstruction(pointE);
				Line oe = new LineThroughTwoPoints("oe", pointO, pointE);
				cp.addGeoConstruction(oe);
				Line ne = new PerpendicularLine("ne", oe, pointE);
				cp.addGeoConstruction(ne);
				Line ad = new LineThroughTwoPoints("ad", pointA, pointD);
				cp.addGeoConstruction(ad);
				Point pointF = new IntersectionPoint("F", ad, ne);
				cp.addGeoConstruction(pointF);
				Line bc = new LineThroughTwoPoints("bc", pointB, pointC);
				cp.addGeoConstruction(bc);
				Point pointG = new IntersectionPoint("G", bc, ne);
				cp.addGeoConstruction(pointG);
				// statement
				cp.addThmStatement(new SegmentsOfEqualLengths(pointE, pointF, pointE, pointG));
			}
			break;
		case 10:
			{
				cp.setTheoremName("Similar triangles - example");
				
				/*
				 * Let ABC be some triangle and B' and C'; are foots of 
				 * altitudes from vertices B and C respectively to 
				 * opposite edges. Then triangle ABC is similar to triangle
				 * AB'C'.
				 */
				//construction
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Point C = new FreePoint("C");
				cp.addGeoConstruction(C);
				Line b = new LineThroughTwoPoints("b", C, A);
				cp.addGeoConstruction(b);
				Line c = new LineThroughTwoPoints("c", A, B);
				cp.addGeoConstruction(c);
				ShortcutConstruction Bp = new FootPoint("B'", B, b);
				cp.addGeoConstruction(Bp);
				ShortcutConstruction Cp = new FootPoint("C'", C, c);
				cp.addGeoConstruction(Cp);
				// statement
				cp.addThmStatement(new SimilarTriangles(A, B, C, A, Bp.getPoint(), Cp.getPoint(), false));
			}
			break;
		case 11:
			{
				cp.setTheoremName("Center of inscribed circle of triangle");
				
				/*
				 * Let ABC be some triangle and sa, sb and sc are
				 * angle bisector lines of angles <A, <B and <C 
				 * respectively. Then they all meet in one point S - 
				 * - center of inscribed circle of triangle ABC.
				 */
				//construction
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Point C = new FreePoint("C");
				cp.addGeoConstruction(C);
				Line sa = new AngleBisector("sa", B, A, C);
				cp.addGeoConstruction(sa);
				Line sb = new AngleBisector("sb", C, B, A);
				cp.addGeoConstruction(sb);
				Line sc = new AngleBisector("sc", B, C, A);
				cp.addGeoConstruction(sc);
				/* Following piece cannot be proved/disproved
				 * 
				 */
				//Point S1 = new IntersectionPoint("S1", sa, sb);
				//cp.addGeoConstruction(S1);
				//Point S2 = new IntersectionPoint("S2", sb, sc);
				//cp.addGeoConstruction(S2);
				//// statement
				//cp.addThmStatement(new IdenticalPoints(S1, S2));
				/* 
				 * Following piece can be proved
				 */
				Point S = new IntersectionPoint("S", sa, sb);
				cp.addGeoConstruction(S);
				// statement
				cp.addThmStatement(new PointOnSetOfPoints(sc, S));
			}
			break;
		case 12:
			{
				cp.setTheoremName("Center of circumscribed circle of triangle");
				
				/*
				 * Let ABC be some triangle and ma, mb and mc are
				 * perpendicular bisector lines of edges a, b and c 
				 * respectively. Then they all meet in one point O - 
				 * - center of circumscribed circle of triangle ABC.
				 */
				//construction
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Point C = new FreePoint("C");
				cp.addGeoConstruction(C);
				Line ma = new PerpendicularBisector("ma", B, C);
				cp.addGeoConstruction(ma);
				Line mb = new PerpendicularBisector("mb", C, A);
				cp.addGeoConstruction(mb);
				Line mc = new PerpendicularBisector("mc", B, A);
				cp.addGeoConstruction(mc);
				Point O1 = new IntersectionPoint("O1", ma, mb);
				cp.addGeoConstruction(O1);
				Point O2 = new IntersectionPoint("O2", mb, mc);
				cp.addGeoConstruction(O2);
				// statement
				cp.addThmStatement(new IdenticalPoints(O1, O2));
			}
			break;
		case 13:
			{
				cp.setTheoremName("Angle bisector");
				
				/*
				 * Each point of angle bisector is equally distant from
				 * rays of angle.
				 */
				//construction
				Point O = new FreePoint("O");
				cp.addGeoConstruction(O);
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Line l = new AngleBisector("l", A, O, B);
				cp.addGeoConstruction(l);
				Point C = new RandomPointFromLine("C", l);
				cp.addGeoConstruction(C);
				Line a = new LineThroughTwoPoints("a", O, A);
				cp.addGeoConstruction(a);
				Line b = new LineThroughTwoPoints("b", O, B);
				cp.addGeoConstruction(b);
				ShortcutConstruction Ca = new FootPoint("Ca", C, a);
				cp.addGeoConstruction(Ca);
				ShortcutConstruction Cb = new FootPoint("Cb", C, b);
				cp.addGeoConstruction(Cb);
				// statement
				cp.addThmStatement(new SegmentsOfEqualLengths(C, Ca.getPoint(), C, Cb.getPoint()));
			}
			break;
		case 14:
			{
				cp.setTheoremName("Angle and perpendicular bisector of triangle");
				
				/*
				 * Angle bisector of an angle of triangle and perpendicular
				 * bisector of opposite edge meet at circumscribed circle
				 * of that triangle.
				 */
				//construction
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Point C = new FreePoint("C");
				cp.addGeoConstruction(C);
				Line sa = new AngleBisector("sa", B, A, C);
				cp.addGeoConstruction(sa);
				Line ma = new PerpendicularBisector("ma", B, C);
				cp.addGeoConstruction(ma);
				Point D = new IntersectionPoint("D", sa, ma);
				cp.addGeoConstruction(D);
				Circle k = new CircumscribedCircle("k", A, B, C);
				cp.addGeoConstruction(k);
				// statement
				cp.addThmStatement(new PointOnSetOfPoints(k, D));
			}
			break;
		case 15:
			{
				cp.setTheoremName("Isoscales triangle");
				
				/*
				 * Angles on base edge of isosceles triangle 
				 * are two equal angles.
				 */
				//construction
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Line m = new PerpendicularBisector("m", A, B);
				cp.addGeoConstruction(m);
				Point C = new RandomPointFromLine("C", m);
				cp.addGeoConstruction(C);
				// statement
				cp.addThmStatement(new EqualAngles(C, A, B, A, B, C));
			}
			break;
		case 16:
			{
				cp.setTheoremName("False");
				
				/*
				 * False
				 */
				//construction
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				// statement
				cp.addThmStatement(new False(cp));
			}
			break;
		case 17:
			{
				cp.setTheoremName("Two disjunctive circles");
				
				/*
				 * Although we construct 2 disjunctive circles,
				 * we succeed to show that if there is intersection
				 * point of first circle and radical axis, then it lies
				 * on second circle as well. (But that intersection
				 * doesn't exist).
				 */
				//construction
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Point C = new MidPoint("C", A, B);
				cp.addGeoConstruction(C);
				Point D = new MidPoint("D", A, C);
				cp.addGeoConstruction(D);
				Point E = new MidPoint("E", C, B);
				cp.addGeoConstruction(E);
				Circle k1 = new CircleWithCenterAndPoint("k1", A, D);
				cp.addGeoConstruction(k1);
				Circle k2 = new CircleWithCenterAndPoint("k2", B, E);
				cp.addGeoConstruction(k2);
				Line r = new RadicalAxis("r", k1, k2);
				cp.addGeoConstruction(r);
				Point P = new IntersectionPoint("P", k1, r);
				cp.addGeoConstruction(P);
				// statement
				cp.addThmStatement(new PointOnSetOfPoints(k2, P));
			}
			break;
		case 18:
			{
				cp.setTheoremName("Two disjunctive lines");
				
				/*
				 * Two parallel lines doesn't have common point
				 */
				
				/*
				 * Version 1
				 */
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Point C = new FreePoint("C");
				cp.addGeoConstruction(C);
				Point D = new FreePoint("D");
				cp.addGeoConstruction(D);
				Line AC = new LineThroughTwoPoints("AC", A, C);
				cp.addGeoConstruction(AC);
				Line p = new ParallelLine("p", AC, D);
				cp.addGeoConstruction(p);
				Point E = new RandomPointFromLine("E", p);
				cp.addGeoConstruction(E);
				// statement
				cp.addThmStatement(new PointOnSetOfPoints(AC, E)); // this will be disproved since two parallel lines cannot have common point
				/*
				*
				*/
				
				/*
				 * Version 2
				 *
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Point C = new FreePoint("C");
				cp.addGeoConstruction(C);
				Point D = new FreePoint("D");
				cp.addGeoConstruction(D);
				Line AC = new LineThroughTwoPoints("AC", A, C);
				cp.addGeoConstruction(AC);
				Line p = new ParallelLine("p", AC, D);
				cp.addGeoConstruction(p);
				Point E = new IntersectionPoint("E", AC, p); // with this system will fail in triangulation
				cp.addGeoConstruction(E);
				// statement
				cp.addThmStatement(new True(cp));
				 *
				 * 
				 */
				
				/*
				 * Version 3
				 * 
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Point C = new FreePoint("C");
				cp.addGeoConstruction(C);
				Line AB = new LineThroughTwoPoints("AB", A, B);
				cp.addGeoConstruction(AB);
				Line p = new ParallelLine("p", AB, C);
				cp.addGeoConstruction(p);
				Point D = new RandomPointFromLine("D", p);
				cp.addGeoConstruction(D);
				// statement
				cp.addThmStatement(new PointOnSetOfPoints(AB, D)); // this will give unsuccessful transformation since 2 parallel lines cannot have common point
				*
				*
				*/
			}
			break;
		case 19:
			{
				cp.setTheoremName("Orthocenter of triangle");
				
				/*
				 * Triangle's altitudes meet at single point - orthocenter of triangle
				 */
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Point C = new FreePoint("C");
				cp.addGeoConstruction(C);
				Line AB = new LineThroughTwoPoints("AB", A, B);
				cp.addGeoConstruction(AB);
				Line BC = new LineThroughTwoPoints("BC", B, C);
				cp.addGeoConstruction(BC);
				Line CA = new LineThroughTwoPoints("CA", C, A);
				cp.addGeoConstruction(CA);
				Line ha = new PerpendicularLine("ha", BC, A);
				cp.addGeoConstruction(ha);
				Line hb = new PerpendicularLine("hb", CA, B);
				cp.addGeoConstruction(hb);
				Line hc = new PerpendicularLine("hc", AB, C);
				cp.addGeoConstruction(hc);
				// statement
				ArrayList<Line> concurrentLines = new ArrayList<Line>();
				concurrentLines.add(ha);
				concurrentLines.add(hb);
				concurrentLines.add(hc);
				cp.addThmStatement(new ConcurrentLines(concurrentLines));
			}
			break;
		case 20:
			{
				cp.setTheoremName("Right Triangle - example (1)");
				
				/*
				 * Let abc be a triangle with right angle <abc and d be the midpoint of the
				 * diagonal side ac. Let f be the root of the perpendicular from d to side ab, and e
				 * the point on the extended perpendicular such that ad = de. If we connect e with a
				 * respectively c by line segments, then prove <ace = <ecb.
				 * 
				 * [paper]
				 * Geometric theorem proving by integrated logical and algebraic reasoning *
				 * Takashi Matsuyama *, Tomoaki Nitta
				 */
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Line AB = new LineThroughTwoPoints("AB", A, B);
				cp.addGeoConstruction(AB);
				Line nB = new PerpendicularLine("nB", AB, B);
				cp.addGeoConstruction(nB);
				Point C = new RandomPointFromLine("C", nB);
				cp.addGeoConstruction(C);
				Point D = new MidPoint("D", A, C);
				cp.addGeoConstruction(D);
				Line nD = new PerpendicularLine("nD", AB, D);
				cp.addGeoConstruction(nD);
				Circle k = new CircleWithCenterAndPoint("k", D, A);
				cp.addGeoConstruction(k);
				Point E = new IntersectionPoint("E", k, nD);
				cp.addGeoConstruction(E);
				// statement
				cp.addThmStatement(new EqualAngles(A, C, E, E, C, B));
			}
			break;
		case 21:
			{
				cp.setTheoremName("Concurrent circles - example");
				
				/*
				 * Let ABCD be a square. Let E be a midpoint of edge AB and
				 * k1 circle with center A through E and k2 circle with center
				 * B and through E. Let F be midpoint of edge CD and k3 circle
				 * with center F and radius AB. Then k1, k2 and k3 are concurrent
				 * circles (all contain E).
				 */
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Point C = new RotatedPoint("C", A, B, 90);
				cp.addGeoConstruction(C);
				Line AB = new LineThroughTwoPoints("AB", A, B);
				cp.addGeoConstruction(AB);
				Line BC = new LineThroughTwoPoints("BC", B, C);
				cp.addGeoConstruction(BC);
				Line nA = new PerpendicularLine("nA", AB, A);
				cp.addGeoConstruction(nA);
				Line nC = new PerpendicularLine("nC", BC, C);
				cp.addGeoConstruction(nC);
				Point D = new IntersectionPoint("D", nA, nC);
				cp.addGeoConstruction(D);
				Point E = new MidPoint("E", A, B);
				cp.addGeoConstruction(E);
				Point F = new MidPoint("F", C, D);
				cp.addGeoConstruction(F);
				Circle k1 = new CircleWithCenterAndPoint("k1", A, E);
				cp.addGeoConstruction(k1);
				Circle k2 = new CircleWithCenterAndPoint("k2", B, E);
				cp.addGeoConstruction(k2);
				Circle k3 = new CircleWithCenterAndRadius("k3", F, A, B);
				cp.addGeoConstruction(k3);
				// statement
				ArrayList<Circle> concurrentCircles = new ArrayList<Circle>();
				concurrentCircles.add(k1);
				concurrentCircles.add(k2);
				concurrentCircles.add(k3);
				cp.addThmStatement(new ConcurrentCircles(concurrentCircles));
			}
			break;
		case 22:
			{
				cp.setTheoremName("Angle bisectors of parallelogram's angles");
				
				/*
				 * Let ABCD be a parallelogram and sA and sC are angle
				 * bisectors of angles <A and <C. Then these lines are parallel.
				 * Also halves of angles <A and <C are equal.
				 */
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Point C = new FreePoint("C");
				cp.addGeoConstruction(C);
				Point D = new TranslatedPoint("D", B, A, C);
				cp.addGeoConstruction(D);
				Line sA = new AngleBisector("sA", B, A, D);
				cp.addGeoConstruction(sA);
				Line sC = new AngleBisector("sC", D, C, B);
				cp.addGeoConstruction(sC);
				
				/* Version 1 - can't be proved/disproved that lines are parallel
				 * 
				 *
				// statement
				cp.addThmStatement(new TwoParallelLines(sA, sC));
				 *
				 *
				 */
				
				/*
				 * Version 2 - can't be proved/disproved that angles' halves are equal 
				 * 
				Point F = new RandomPointFromLine("F", sA);
				cp.addGeoConstruction(F);
				Point S = new RandomPointFromLine("S", sC);
				cp.addGeoConstruction(S);
				// statement
				cp.addThmStatement(new EqualAngles(B, A, F, D, C, S));
				 * 
				 * 
				 */
				
				/*
				 * Version 3 - can't be proved/disproved that angles' halves are equal
				 *
				Line CD = new LineThroughTwoPoints("CD", C, D);
				cp.addGeoConstruction(CD);
				Line AB = new LineThroughTwoPoints("AB", A, B);
				cp.addGeoConstruction(AB);
				Point F = new IntersectionPoint("F", sA, CD);
				cp.addGeoConstruction(F);
				Point S = new IntersectionPoint("S", sC, AB);
				cp.addGeoConstruction(S);
				// statement
				cp.addThmStatement(new EqualAngles(B, A, F, D, C, S));
				//cp.addThmStatement(new SegmentsOfEqualLengths(A, F, C, S)); // also can't prove that segments are equal
				 * 
				 * 
				 */
				
				/*
				 * Version 4 - can't be proved/disproved that lines are parallel
				 *
				Line CD = new LineThroughTwoPoints("CD", C, D);
				cp.addGeoConstruction(CD);
				Line AB = new LineThroughTwoPoints("AB", A, B);
				cp.addGeoConstruction(AB);
				Point F = new IntersectionPoint("F", sA, CD);
				cp.addGeoConstruction(F);
				Point S = new IntersectionPoint("S", sC, AB);
				cp.addGeoConstruction(S);
				// statement
				cp.addThmStatement(new TwoParallelLines(sA, sC));
				 *
				 *
				 */
				
				/*
				 * Version 5 - can't be proved/disproved that angles are equal
				 *
				Line BD = new LineThroughTwoPoints("BD", B, D);
				cp.addGeoConstruction(BD);
				Point F = new IntersectionPoint("F", sA, BD);
				cp.addGeoConstruction(F);
				Point S = new IntersectionPoint("S", sC, BD);
				cp.addGeoConstruction(S);
				// statement
				cp.addThmStatement(new EqualAngles(B, A, F, D, C, S));
				*
				*
				*/
			}
			break;
		case 23:
			{
				cp.setTheoremName("Perpendicular bisectors of parallelogram's opposite edges - example");
				
				/*
				 * Let ABCD be a parallelogram and ma and mb are perpendicular
				 * bisectors of opposite edges AB and CD. Then these lines are parallel.
				 */
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Point C = new FreePoint("C");
				cp.addGeoConstruction(C);
				Point D = new TranslatedPoint("D", B, A, C);
				cp.addGeoConstruction(D);
				Line ma = new PerpendicularBisector("ma", A, B);
				cp.addGeoConstruction(ma);
				Line mb = new PerpendicularBisector("mb", C, D);
				cp.addGeoConstruction(mb);
				// statement
				cp.addThmStatement(new TwoParallelLines(ma, mb));
			}
			break;
		case 24:
			{
				cp.setTheoremName("Triangle's altitudes meet at single point - orthocenter of triangle (2)");
				
				/*
				 * Let ABC be a triangle. Let ha and hb are altitude lines from A and B respectively.
				 * They meet at point H and then line CH is perpendicular to AB.
				 */
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Point C = new FreePoint("C");
				cp.addGeoConstruction(C);
				Line AB = new LineThroughTwoPoints("AB", A, B);
				cp.addGeoConstruction(AB);
				Line BC = new LineThroughTwoPoints("BC", B, C);
				cp.addGeoConstruction(BC);
				Line CA = new LineThroughTwoPoints("CA", C, A);
				cp.addGeoConstruction(CA);
				Line ha = new PerpendicularLine("ha", BC, A);
				cp.addGeoConstruction(ha);
				Line hb = new PerpendicularLine("hb", CA, B);
				cp.addGeoConstruction(hb);
				Point H = new IntersectionPoint("H", ha, hb);
				cp.addGeoConstruction(H);
				Line hc = new LineThroughTwoPoints("hc", C, H);
				cp.addGeoConstruction(hc);
				// statement
				cp.addThmStatement(new TwoPerpendicularLines(hc, AB));
			}
			break;
		case 25:
			{
				cp.setTheoremName("Intersection of rhombus diagonals");
				
				/*
				 * Let ABCD be a rhombus and O is intersection point of diagonals
				 * AC and BD. Then O lies on circle with diameter AB (i.e. diagonals
				 * of rhombus are perpendicular).
				 */
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Circle cTemp = new CircleWithCenterAndPoint("cTemp", B, A);
				cp.addGeoConstruction(cTemp);
				Point C = new RandomPointFromCircle("C", cTemp);
				cp.addGeoConstruction(C);
				Point D = new TranslatedPoint("D", B, A, C);
				cp.addGeoConstruction(D);
				Line AC = new LineThroughTwoPoints("AC", A, C);
				cp.addGeoConstruction(AC);
				Line BD = new LineThroughTwoPoints("BD", B, D);
				cp.addGeoConstruction(BD);
				Point O = new IntersectionPoint("O", AC, BD);
				cp.addGeoConstruction(O);
				Circle k = new CircleWithDiameter("k", A, B);
				cp.addGeoConstruction(k);
				// statement
				cp.addThmStatement(new PointOnSetOfPoints(k, O));
			}
			break;
		case 26:
			{
				cp.setTheoremName("Example for algebraic sum of segments - (1)");
				
				/*
				 * [part of Great Problem]
				 * Let ABC be some triangle and S, Sa, Sb and Sc are centers of
				 * inscribed and three externally inscribed circles of this triangle
				 * respectively. Let P, Pa, Pb and Pc are respectively orthogonal 
				 * projections of these points on line BC. Then prove that
				 * PPa = |AB - AC|.
				 * 
				 */
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Point C = new FreePoint("C");
				cp.addGeoConstruction(C);
				//Point A = new FreePoint("A");
				//cp.addGeoConstruction(A);
				Line sa = new AngleBisector("sa", B, A, C);
				cp.addGeoConstruction(sa);
				Line sa_ex = new PerpendicularLine("sa_ex", sa, A);
				cp.addGeoConstruction(sa_ex);
				Line sb = new AngleBisector("sb", C, B, A);
				cp.addGeoConstruction(sb);
				Line sb_ex = new PerpendicularLine("sb_ex", sb, B);
				cp.addGeoConstruction(sb_ex);
				Point S = new IntersectionPoint("S", sa, sb);
				cp.addGeoConstruction(S);
				Point Sa = new IntersectionPoint("Sa", sa, sb_ex);
				cp.addGeoConstruction(Sa);
				/*
				Point Sb = new IntersectionPoint("Sb", sa_ex, sb);
				cp.addGeoConstruction(Sb);
				Point Sc = new IntersectionPoint("Sc", sa_ex, sb_ex);
				cp.addGeoConstruction(Sc);
				*/
				Line BC = new LineThroughTwoPoints("BC", B, C);
				cp.addGeoConstruction(BC);
				ShortcutConstruction P = new FootPoint("P", S, BC);
				cp.addGeoConstruction(P);
				ShortcutConstruction Pa = new FootPoint("Pa", Sa, BC);
				cp.addGeoConstruction(Pa);
				/*
				ShortcutConstruction Pb = new FootPoint("Pb", Sb, BC);
				cp.addGeoConstruction(Pb);
				ShortcutConstruction Pc = new FootPoint("Pc", Sc, BC);
				cp.addGeoConstruction(Pc);
				*/
				// statement
				cp.addThmStatement(new AlgebraicSumOfThreeSegments(P.getPoint(), Pa.getPoint(), A, B, A, C)); // proved in around 8 sec /with 110 terms
				
				/*
				 * Result is worse if A is third free point although the system and statement
				 * are much more simpler. Prover spends much more time and space to prove the theorem.
				 */
			}
			break;
		case 27:
			{
				cp.setTheoremName("Example for algebraic sum of segments - (2)");
				
				/*
				 * [part of Great Problem]
				 * Let ABC be some triangle and S, Sa, Sb and Sc are centers of
				 * inscribed and three externally inscribed circles of this triangle
				 * respectively. Let P, Pa, Pb and Pc are respectively orthogonal 
				 * projections of these points on line BC. Then prove that
				 * PbPc = AB + AC.
				 * 
				 */
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Point C = new FreePoint("C");
				cp.addGeoConstruction(C);
				//Point A = new FreePoint("A");
				//cp.addGeoConstruction(A);
				Line sa = new AngleBisector("sa", B, A, C);
				cp.addGeoConstruction(sa);
				Line sa_ex = new PerpendicularLine("sa_ex", sa, A);
				cp.addGeoConstruction(sa_ex);
				Line sb = new AngleBisector("sb", C, B, A);
				cp.addGeoConstruction(sb);
				Line sb_ex = new PerpendicularLine("sb_ex", sb, B);
				cp.addGeoConstruction(sb_ex);
				//Point S = new IntersectionPoint("S", sa, sb);
				//cp.addGeoConstruction(S);
				//Point Sa = new IntersectionPoint("Sa", sa, sb_ex);
				//cp.addGeoConstruction(Sa);
				Point Sb = new IntersectionPoint("Sb", sa_ex, sb);
				cp.addGeoConstruction(Sb);
				Point Sc = new IntersectionPoint("Sc", sa_ex, sb_ex);
				cp.addGeoConstruction(Sc);
				Line BC = new LineThroughTwoPoints("BC", B, C);
				cp.addGeoConstruction(BC);
				//ShortcutConstruction P = new FootPoint("P", S, BC);
				//cp.addGeoConstruction(P);
				//ShortcutConstruction Pa = new FootPoint("Pa", Sa, BC);
				//cp.addGeoConstruction(Pa);
				ShortcutConstruction Pb = new FootPoint("Pb", Sb, BC);
				cp.addGeoConstruction(Pb);
				ShortcutConstruction Pc = new FootPoint("Pc", Sc, BC);
				cp.addGeoConstruction(Pc);
				// statement
				cp.addThmStatement(new AlgebraicSumOfThreeSegments(Pb.getPoint(), Pc.getPoint(), A, B, A, C)); // can't be proved/disproved after 78 sec / with 420 terms
				
				/*
				 * Result is even worse if A is third free point although the system and statement
				 * are much more simpler. Prover spends much more time and space and still cannot
				 * prove/disprove the theorem.
				 */
			}
			break;
		case 28:
			{
				cp.setTheoremName("Sum of angle's halves is equal to whole angle");
				
				/*
				 * Sum of angle's halves is equal to whole angle.
				 * 
				 */
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Point C = new FreePoint("C");
				cp.addGeoConstruction(C);
				Line sa = new AngleBisector("sa", B, A, C);
				cp.addGeoConstruction(sa);
				Point D = new RandomPointFromLine("D", sa);
				cp.addGeoConstruction(D);
				// statement
				Angle alpha = new Angle(B, A, D);
				Angle beta = new Angle(D, A, C);
				Angle gamma = new Angle(B, A, C);
				cp.addThmStatement(new AlgebraicSumOfThreeAngles(alpha, beta, gamma));
			}
			break;
		case 29:
			{
				cp.setTheoremName("Three squares");
				
				/*
				 * Let ABCD, BEFC and EGHF be three concatenated squares.
				 * Then <BDC = <EDF + <GDH
				 */
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Point C = new RotatedPoint("C", A, B, -90);
				cp.addGeoConstruction(C);
				Point D = new TranslatedPoint("D", B, A, C);
				cp.addGeoConstruction(D);
				Point E = new TranslatedPoint("E", A, B, B);
				cp.addGeoConstruction(E);
				Point F = new TranslatedPoint("F", D, C, C);
				cp.addGeoConstruction(F);
				Point G = new TranslatedPoint("G", B, E, E);
				cp.addGeoConstruction(G);
				Point H = new TranslatedPoint("H", C, F, F);
				cp.addGeoConstruction(H);
				// statement
				Angle gamma = new Angle(B, D, C);
				Angle alpha = new Angle(E, D, F);
				Angle beta = new Angle(G, D, H);
				cp.addThmStatement(new AlgebraicSumOfThreeAngles(alpha, beta, gamma));
			}
			break;
		case 30:
			{
				cp.setTheoremName("Touching circles example");
				
				/*
				 * Let ABCD be a square and O is its center while
				 * E  and F are midpoints of edges AB and CD respectively.
				 * Then circles k(E, EA) and l(F, FC) are touching 
				 * each other (at point O).
				 */
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Point C = new RotatedPoint("C", A, B, -90);
				cp.addGeoConstruction(C);
				Point D = new TranslatedPoint("D", B, A, C);
				cp.addGeoConstruction(D);
				Point E = new MidPoint("E", A, B);
				cp.addGeoConstruction(E);
				Point F = new MidPoint("F", C, D);
				cp.addGeoConstruction(F);
				Circle k = new CircleWithCenterAndPoint("k", E, A);
				cp.addGeoConstruction(k);
				Circle l = new CircleWithCenterAndPoint("l", F, C);
				cp.addGeoConstruction(l);
				// statement
				cp.addThmStatement(new TouchingCircles(k, l));
			}
			break;
		case 31:
			{
				cp.setTheoremName("Centroid of triangle");
				
				/*
				 * Let ABC be a triangle and A1 midpoint of edge BC and
				 * B1 midpoint of edge CA and C1 midpoint of edge AB.
				 * Let medians AA1 and BB1 intersect at point T (centroid
				 * of triangle ABC). Then it divides medians with ratio 2:
				 * e.g. CT : TC1 = 2 : 1.
				 */
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Point C = new FreePoint("C");
				cp.addGeoConstruction(C);
				Point A1 = new MidPoint("A1", B, C);
				cp.addGeoConstruction(A1);
				Point B1 = new MidPoint("B1", C, A);
				cp.addGeoConstruction(B1);
				Point C1 = new MidPoint("C1", A, B);
				cp.addGeoConstruction(C1);
				Line ta = new LineThroughTwoPoints("ta", A, A1);
				cp.addGeoConstruction(ta);
				Line tb = new LineThroughTwoPoints("tb", B, B1);
				cp.addGeoConstruction(tb);
				Point T = new IntersectionPoint("T", ta, tb);
				cp.addGeoConstruction(T);
				// statement
				Segment CT = new Segment(C, T);
				Segment TC1 = new Segment(T, C1);
				cp.addThmStatement(new RatioOfOrientedSegments(CT, TC1, 2));
			}
			break;
		case 32:
			{
				cp.setTheoremName("Butterfly (2)");
				
				// Constructions for the Butterfly theorem
				/*
				 * A, B, C and D are four points on one circle whose center is O.
				 * E is intersection of AC and BD. Through E draw line perpendicular
				 * to OE, meeting AD at F and BC at G. Show that FE = GE.
				 * (Since E, F and G are collinear we will use that FE : EG = 1.)
				 */
				Point pointO = new FreePoint("O");
				cp.addGeoConstruction(pointO);
				Point pointA = new FreePoint("A");
				cp.addGeoConstruction(pointA);
				Circle circlek = new CircleWithCenterAndPoint("k", pointO, pointA);
				cp.addGeoConstruction(circlek);
				Point pointB = new RandomPointFromCircle("B", circlek);
				cp.addGeoConstruction(pointB);
				Point pointC = new RandomPointFromCircle("C", circlek);
				cp.addGeoConstruction(pointC);
				Point pointD = new RandomPointFromCircle("D", circlek);
				cp.addGeoConstruction(pointD);
				Line ac = new LineThroughTwoPoints("ac", pointA, pointC);
				cp.addGeoConstruction(ac);
				Line bd = new LineThroughTwoPoints("bd", pointB, pointD);
				cp.addGeoConstruction(bd);
				Point pointE = new IntersectionPoint("E", ac, bd);
				cp.addGeoConstruction(pointE);
				Line oe = new LineThroughTwoPoints("oe", pointO, pointE);
				cp.addGeoConstruction(oe);
				Line ne = new PerpendicularLine("ne", oe, pointE);
				cp.addGeoConstruction(ne);
				Line ad = new LineThroughTwoPoints("ad", pointA, pointD);
				cp.addGeoConstruction(ad);
				Point pointF = new IntersectionPoint("F", ad, ne);
				cp.addGeoConstruction(pointF);
				Line bc = new LineThroughTwoPoints("bc", pointB, pointC);
				cp.addGeoConstruction(bc);
				Point pointG = new IntersectionPoint("G", bc, ne);
				cp.addGeoConstruction(pointG);
				// statement
				Segment FE = new Segment(pointF, pointE);
				Segment EG = new Segment(pointE, pointG);
				cp.addThmStatement(new RatioOfOrientedSegments(FE, EG, 1));
			}
			break;
		case 33:
			{
				cp.setTheoremName("Parallelogram example");
				
				/*
				 * Let P, Q and R are midpoints of edges AB, BC and CD of
				 * parallelogram ABCD. Let lines DP and BR intersect segment
				 * AQ at points K and L respectively. Prove that KL = (2/5)*AQ.
				 */
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Point C = new FreePoint("C");
				cp.addGeoConstruction(C);
				Point D = new TranslatedPoint("D", B, A, C);
				cp.addGeoConstruction(D);
				Point P = new MidPoint("P", A, B);
				cp.addGeoConstruction(P);
				Point Q = new MidPoint("Q", B, C);
				cp.addGeoConstruction(Q);
				Point R = new MidPoint("R", C, D);
				cp.addGeoConstruction(R);
				Line DP = new LineThroughTwoPoints("DP", D, P);
				cp.addGeoConstruction(DP);
				Line BR = new LineThroughTwoPoints("BR", B, R);
				cp.addGeoConstruction(BR);
				Line AQ = new LineThroughTwoPoints("AQ", A, Q);
				cp.addGeoConstruction(AQ);
				Point K = new IntersectionPoint("K", DP, AQ);
				cp.addGeoConstruction(K);
				Point L = new IntersectionPoint("L", BR, AQ);
				cp.addGeoConstruction(L);
				// statement
				Segment segKL = new Segment(K, L);
				Segment segAQ = new Segment(A, Q);
				cp.addThmStatement(new RatioOfOrientedSegments(segKL, segAQ, 0.4));
			}
			break;
		case 34:
			{
				cp.setTheoremName("Inverse points example");
				
				/*
				 * Let k be a circle with center O and A and B
				 * are two points from that circle. Let l be circle
				 * that contains points A, B and O. Let C be
				 * some point from circle l and D is intersection
				 * point of lines AB and OC. Then D is inverse point
				 * of C with respect to circle k.
				 */
				Point O = new FreePoint("O");
				cp.addGeoConstruction(O);
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Circle k = new CircleWithCenterAndPoint("k", O, A);
				cp.addGeoConstruction(k);
				Point B = new RandomPointFromCircle("B", k);
				cp.addGeoConstruction(B);
				Circle l = new CircumscribedCircle("l", A, B, O);
				cp.addGeoConstruction(l);
				Line AB = new LineThroughTwoPoints("AB", A, B);
				cp.addGeoConstruction(AB);
				Point C = new RandomPointFromCircle("C", l);
				cp.addGeoConstruction(C);
				Line OC = new LineThroughTwoPoints("OC", O, C);
				cp.addGeoConstruction(OC);
				Point D = new IntersectionPoint("D", OC, AB);
				cp.addGeoConstruction(D);
				// statement
				/*
				// first version - with identical points
				Point E = new InverseOfPoint("E", C, k);
				cp.addGeoConstruction(E);
				cp.addThmStatement(new IdenticalPoints(D, E));
				*/
				// second version - with direct condition - better
				cp.addThmStatement(new TwoInversePoints(C, D, k));
			}
			break;
		case 35:
			{
				cp.setTheoremName("Harmonic conjugates example - (1)");
				
				/*
				 * Let A, B and C are three collinear points and L is
				 * some point not lying on line AB. Let p be some line
				 * through C, and M and N are intersection points of p
				 * with LA and LB respectively. Let K be intersection
				 * point of AN and BM and D is intersection of LK and AB.
				 * Then pair of points (C, D) is in harmonic conjunction
				 * with pair (A, B).
				 */
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Line AB = new LineThroughTwoPoints("AB", A, B);
				cp.addGeoConstruction(AB);
				Point C = new RandomPointFromLine("C", AB);
				cp.addGeoConstruction(C);
				Point L = new FreePoint("L");
				cp.addGeoConstruction(L);
				Line LA = new LineThroughTwoPoints("LA", L, A);
				cp.addGeoConstruction(LA);
				Line LB = new LineThroughTwoPoints("LB", L, B);
				cp.addGeoConstruction(LB);
				Point M = new RandomPointFromLine("M", LA);
				cp.addGeoConstruction(M);
				Line CM = new LineThroughTwoPoints("CM", C, M);
				cp.addGeoConstruction(CM);
				Point N = new IntersectionPoint("N", CM, LB);
				cp.addGeoConstruction(N);
				Line AN = new LineThroughTwoPoints("AN", A, N);
				cp.addGeoConstruction(AN);
				Line BM = new LineThroughTwoPoints("BM", B, M);
				cp.addGeoConstruction(BM);
				Point K = new IntersectionPoint("K", AN, BM);
				cp.addGeoConstruction(K);
				Line LK = new LineThroughTwoPoints("LK", L, K);
				cp.addGeoConstruction(LK);
				Point D = new IntersectionPoint("D", AB, LK);
				cp.addGeoConstruction(D);
				// statement
				// first version - with identical points
				Point E = new HarmonicConjugatePoint("E", A, B, C);
				cp.addGeoConstruction(E);
				cp.addThmStatement(new IdenticalPoints(D, E));
			}
			break;
		case 36:
			{
				cp.setTheoremName("Harmonic conjugates example - (2)");
				
				/*
				 * Let A, B and C are three collinear points and L is
				 * some point not lying on line AB. Let p be some line
				 * through C, and M and N are intersection points of p
				 * with LA and LB respectively. Let K be intersection
				 * point of AN and BM and D is intersection of LK and AB.
				 * Then pair of points (C, D) is in harmonic conjunction
				 * with pair (A, B).
				 */
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Line AB = new LineThroughTwoPoints("AB", A, B);
				cp.addGeoConstruction(AB);
				Point C = new RandomPointFromLine("C", AB);
				cp.addGeoConstruction(C);
				Point L = new FreePoint("L");
				cp.addGeoConstruction(L);
				Line LA = new LineThroughTwoPoints("LA", L, A);
				cp.addGeoConstruction(LA);
				Line LB = new LineThroughTwoPoints("LB", L, B);
				cp.addGeoConstruction(LB);
				Point M = new RandomPointFromLine("M", LA);
				cp.addGeoConstruction(M);
				Line CM = new LineThroughTwoPoints("CM", C, M);
				cp.addGeoConstruction(CM);
				Point N = new IntersectionPoint("N", CM, LB);
				cp.addGeoConstruction(N);
				Line AN = new LineThroughTwoPoints("AN", A, N);
				cp.addGeoConstruction(AN);
				Line BM = new LineThroughTwoPoints("BM", B, M);
				cp.addGeoConstruction(BM);
				Point K = new IntersectionPoint("K", AN, BM);
				cp.addGeoConstruction(K);
				Line LK = new LineThroughTwoPoints("LK", L, K);
				cp.addGeoConstruction(LK);
				Point D = new IntersectionPoint("D", AB, LK);
				cp.addGeoConstruction(D);
				// statement
				// second version - with direct condition - better
				cp.addThmStatement(new FourHarmonicConjugatePoints(A, B, C, D));
			}
			break;
		case 37:
			{
				cp.setTheoremName("Menelaus theorem - first part");
				
				/*
				 * Let ABC is triangle and D and E are points on lines BC and CA.
				 * Let F be a point on line AB such that (BD/DC)*(CE/EA)*(AF/FB) = -1
				 * (here segments of each ratio are oriented).
				 * Then points D, E and F are collinear.
				 */
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Point C = new FreePoint("C");
				cp.addGeoConstruction(C);
				Line AB = new LineThroughTwoPoints("AB", A, B);
				cp.addGeoConstruction(AB);
				Line BC = new LineThroughTwoPoints("BC", B, C);
				cp.addGeoConstruction(BC);
				Line CA = new LineThroughTwoPoints("CA", C, A);
				cp.addGeoConstruction(CA);
				Point D = new RandomPointFromLine("D", BC);
				cp.addGeoConstruction(D);
				Point E = new RandomPointFromLine("E", CA);
				cp.addGeoConstruction(E);
				RatioOfTwoCollinearSegments rCBD = new RatioOfTwoCollinearSegments(C, B, D);
				RatioOfTwoCollinearSegments rACE = new RatioOfTwoCollinearSegments(A, C, E);
				Vector<RatioOfTwoCollinearSegments> ratios = new Vector<RatioOfTwoCollinearSegments>();
				ratios.add(rCBD);
				ratios.add(rACE);
				RatioProduct ratioProd = new RatioProduct(ratios);
				Point F = new GeneralizedSegmentDivisionPoint("F", A, B, ratioProd, -1);
				cp.addGeoConstruction(F);
				// statement
				ArrayList<Point> points = new ArrayList<Point>();
				points.add(D);
				points.add(E);
				points.add(F);
				cp.addThmStatement(new CollinearPoints(points));
			}
			break;
		case 38:
			{
				cp.setTheoremName("Ceva's theorem - first part");
				
				/*
				 * Let ABC is triangle and D and E are points on lines BC and CA.
				 * Let F be a point on line AB such that (BD/DC)*(CE/EA)*(AF/FB) = 1
				 * (here segments of each ratio are oriented).
				 * Then lines AD, BE and CF are concurrent.
				 */
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Point C = new FreePoint("C");
				cp.addGeoConstruction(C);
				Line AB = new LineThroughTwoPoints("AB", A, B);
				cp.addGeoConstruction(AB);
				Line BC = new LineThroughTwoPoints("BC", B, C);
				cp.addGeoConstruction(BC);
				Line CA = new LineThroughTwoPoints("CA", C, A);
				cp.addGeoConstruction(CA);
				Point D = new RandomPointFromLine("D", BC);
				cp.addGeoConstruction(D);
				Point E = new RandomPointFromLine("E", CA);
				cp.addGeoConstruction(E);
				RatioOfTwoCollinearSegments rCBD = new RatioOfTwoCollinearSegments(C, B, D);
				RatioOfTwoCollinearSegments rACE = new RatioOfTwoCollinearSegments(A, C, E);
				Vector<RatioOfTwoCollinearSegments> ratios = new Vector<RatioOfTwoCollinearSegments>();
				ratios.add(rCBD);
				ratios.add(rACE);
				RatioProduct ratioProd = new RatioProduct(ratios);
				Point F = new GeneralizedSegmentDivisionPoint("F", A, B, ratioProd, 1);
				cp.addGeoConstruction(F);
				Line AD = new LineThroughTwoPoints("AD", A, D);
				cp.addGeoConstruction(AD);
				Line BE = new LineThroughTwoPoints("BE", B, E);
				cp.addGeoConstruction(BE);
				Line CF = new LineThroughTwoPoints("CF", C, F);
				cp.addGeoConstruction(CF);
				// statement
				ArrayList<Line> lines = new ArrayList<Line>();
				lines.add(AD);
				lines.add(BE);
				lines.add(CF);
				cp.addThmStatement(new ConcurrentLines(lines));
			}
			break;
		case 39:
			{
				cp.setTheoremName("Thales theorem - first part");
				
				/*
				 * Let SAD be a triangle and B is point on line SA.
				 * Then let C be the point on line SD such that 
				 * SC/CD = SA/AB. Then Lines CA and DB are parallel. 
				 */
				Point S = new FreePoint("S");
				cp.addGeoConstruction(S);
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point D = new FreePoint("D");
				cp.addGeoConstruction(D);
				Line SA = new LineThroughTwoPoints("SA", S, A);
				cp.addGeoConstruction(SA);
				Point B = new RandomPointFromLine("B", SA);
				cp.addGeoConstruction(B);
				RatioOfTwoCollinearSegments rSBA = new RatioOfTwoCollinearSegments(S, B, A);
				Vector<RatioOfTwoCollinearSegments> ratios = new Vector<RatioOfTwoCollinearSegments>();
				ratios.add(rSBA);
				RatioProduct ratioProd = new RatioProduct(ratios);
				Point C = new GeneralizedSegmentDivisionPoint("C", S, D, ratioProd, 1);
				cp.addGeoConstruction(C);
				Line AC = new LineThroughTwoPoints("AC", A, C);
				cp.addGeoConstruction(AC);
				Line BD = new LineThroughTwoPoints("BD", B, D);
				cp.addGeoConstruction(BD);
				// statement
				cp.addThmStatement(new TwoParallelLines(AC, BD));
			}
			break;
		case 40:
			{
				cp.setTheoremName("Menelaus theorem - second part");
				
				/*
				 * Let ABC be a triangle and some line l intersects
				 * lines BC, CA and AB at points D, E and F respectively.
				 * Then following equation holds:
				 * (BD/DC)*(CE/EA)*(AF/FB) = -1
				 * (these segments are oriented).
				 */
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Point C = new FreePoint("C");
				cp.addGeoConstruction(C);
				Line AB = new LineThroughTwoPoints("AB", A, B);
				cp.addGeoConstruction(AB);
				Line BC = new LineThroughTwoPoints("BC", B, C);
				cp.addGeoConstruction(BC);
				Line CA = new LineThroughTwoPoints("CA", C, A);
				cp.addGeoConstruction(CA);
				Point D = new RandomPointFromLine("D", BC);
				cp.addGeoConstruction(D);
				Point E = new RandomPointFromLine("E", CA);
				cp.addGeoConstruction(E);
				Line DE = new LineThroughTwoPoints("DE", D, E);
				cp.addGeoConstruction(DE);
				Point F = new IntersectionPoint("F", AB, DE);
				cp.addGeoConstruction(F);
				// statement
				Vector<RatioOfTwoCollinearSegments> ratios = new Vector<RatioOfTwoCollinearSegments>();
				RatioOfTwoCollinearSegments rBCD = new RatioOfTwoCollinearSegments(B, C, D);
				ratios.add(rBCD);
				RatioOfTwoCollinearSegments rCAE = new RatioOfTwoCollinearSegments(C, A, E);
				ratios.add(rCAE);
				RatioOfTwoCollinearSegments rABF = new RatioOfTwoCollinearSegments(A, B, F);
				ratios.add(rABF);
				RatioProduct ratioProd = new RatioProduct(ratios);
				cp.addThmStatement(new EqualityOfRatioProducts(ratioProd, null, -1));
			}
			break;
		case 41:
			{
				cp.setTheoremName("Ceva's theorem - second part");
				
				/*
				 * Let ABC be a triangle and D, E and F are points on lines 
				 * BC, CA and AB respectively, such that following lines are
				 * concurrent: AD, BE and CF.
				 * Then following equation holds:
				 * (BD/DC)*(CE/EA)*(AF/FB) = 1
				 * (these segments are oriented).
				 */
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Point C = new FreePoint("C");
				cp.addGeoConstruction(C);
				Line AB = new LineThroughTwoPoints("AB", A, B);
				cp.addGeoConstruction(AB);
				Line BC = new LineThroughTwoPoints("BC", B, C);
				cp.addGeoConstruction(BC);
				Line CA = new LineThroughTwoPoints("CA", C, A);
				cp.addGeoConstruction(CA);
				Point D = new RandomPointFromLine("D", BC);
				cp.addGeoConstruction(D);
				Point E = new RandomPointFromLine("E", CA);
				cp.addGeoConstruction(E);
				Line AD = new LineThroughTwoPoints("AD", A, D);
				cp.addGeoConstruction(AD);
				Line BE = new LineThroughTwoPoints("BE", B, E);
				cp.addGeoConstruction(BE);
				Point S = new IntersectionPoint("S", AD, BE);
				cp.addGeoConstruction(S);
				Line CS = new LineThroughTwoPoints("CS", C, S);
				cp.addGeoConstruction(CS);
				Point F = new IntersectionPoint("F", AB, CS);
				cp.addGeoConstruction(F);
				// statement
				Vector<RatioOfTwoCollinearSegments> ratios = new Vector<RatioOfTwoCollinearSegments>();
				RatioOfTwoCollinearSegments rBCD = new RatioOfTwoCollinearSegments(B, C, D);
				ratios.add(rBCD);
				RatioOfTwoCollinearSegments rCAE = new RatioOfTwoCollinearSegments(C, A, E);
				ratios.add(rCAE);
				RatioOfTwoCollinearSegments rABF = new RatioOfTwoCollinearSegments(A, B, F);
				ratios.add(rABF);
				RatioProduct ratioProd = new RatioProduct(ratios);
				cp.addThmStatement(new EqualityOfRatioProducts(ratioProd, null, 1));
			}
			break;
		case 42:
			{
				cp.setTheoremName("Thales theorem - second part");
				
				/*
				 * Let SAB be a triangle and C point on SA and D
				 * on SB such that CD is parallel to line AB. 
				 * Then following equation for oriented segments holds:
				 * SA/SB = SC/SD.
				 */
				Point S = new FreePoint("S");
				cp.addGeoConstruction(S);
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Line SA = new LineThroughTwoPoints("SA", S, A);
				cp.addGeoConstruction(SA);
				Line SB = new LineThroughTwoPoints("SB", S, B);
				cp.addGeoConstruction(SB);
				Point C = new RandomPointFromLine("C", SA);
				cp.addGeoConstruction(C);
				Line AB = new LineThroughTwoPoints("AB", A, B);
				cp.addGeoConstruction(AB);
				Line l = new ParallelLine("l", AB, C);
				cp.addGeoConstruction(l);
				Point D = new IntersectionPoint("D", l, SB);
				cp.addGeoConstruction(D);
				// statement
				Vector<RatioOfTwoCollinearSegments> leftRatios = new Vector<RatioOfTwoCollinearSegments>();
				RatioOfTwoCollinearSegments rSASB = new RatioOfTwoCollinearSegments(S, A, S, B);
				leftRatios.add(rSASB);
				RatioProduct leftRatioProd = new RatioProduct(leftRatios);
				Vector<RatioOfTwoCollinearSegments> rightRatios = new Vector<RatioOfTwoCollinearSegments>();
				RatioOfTwoCollinearSegments rSCSD = new RatioOfTwoCollinearSegments(S, C, S, D);
				rightRatios.add(rSCSD);
				RatioProduct rightRatioProd = new RatioProduct(rightRatios);
				cp.addThmStatement(new EqualityOfRatioProducts(leftRatioProd, rightRatioProd, 1));
			}
			break;
		case 43:
			{
				cp.setTheoremName("Combination of oriented segments - example");
				
				/*
				 * ABCD is rectangle and O is intersection of diagonals and E
				 * is midpoint of segment AO. Then following equation for
				 * oriented segments holds: AE + EO - OC = 0.
				 */
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Line AB = new LineThroughTwoPoints("AB", A, B);
				cp.addGeoConstruction(AB);
				Line nB = new PerpendicularLine("nB", AB, B);
				cp.addGeoConstruction(nB);
				Point C = new RandomPointFromLine("C", nB);
				cp.addGeoConstruction(C);
				Point D = new TranslatedPoint("D", B, A, C);
				cp.addGeoConstruction(D);
				Line AC = new LineThroughTwoPoints("AC", A, C);
				cp.addGeoConstruction(AC);
				Line BD = new LineThroughTwoPoints("BD", B, D);
				cp.addGeoConstruction(BD);
				Point O = new IntersectionPoint("O", AC, BD);
				cp.addGeoConstruction(O);
				Point E = new MidPoint("E", A, O);
				cp.addGeoConstruction(E);
				// statement
				Segment segAE = new Segment(A, E);
				Segment segEO = new Segment(E, O);
				Segment segOC = new Segment(O, C);
				Vector<Segment> segments = new Vector<Segment>();
				segments.add(segAE);
				segments.add(segEO);
				segments.add(segOC);
				Vector<Double> coefficients = new Vector<Double>();
				coefficients.add(new Double(1));
				coefficients.add(new Double(1));
				coefficients.add(new Double(-1));
				cp.addThmStatement(new LinearCombinationOfOrientedSegments(segments, coefficients));
			}
			break;
		case 44:
			{
				cp.setTheoremName("Ratio of side and altitude in equilateral triangle");
				
				/*
				 * Let ABC be an equilateral triangle. Then ratio of its edge
				 * and altitude is h = a*sqrt(3)/2.
				 */
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Point C = new RotatedPoint("C", A, B, -60);
				cp.addGeoConstruction(C);
				Line AB = new LineThroughTwoPoints("AB", A, B);
				cp.addGeoConstruction(AB);
				ShortcutConstruction C1 = new FootPoint("C1", C, AB);
				cp.addGeoConstruction(C1);
				// statement
				Segment segAB = new Segment(A, B);
				Segment segCC1 = new Segment(C, C1.getPoint());
				cp.addThmStatement(new RatioOfTwoSegments(segCC1, segAB, Math.sqrt(3)/2.0));
			}
			break;
		case 45:
			{
				cp.setTheoremName("Ratio of side and diagonal in pentagon");
				
				/*
				 * Let ABCDE be a pentagon. Then ratio of its edge a 
				 * and diagonal d is d = a * (1 + sqrt(5))/2.
				 */
				Point O = new FreePoint("O");
				cp.addGeoConstruction(O);
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new RotatedPoint("B", A, O, 72);
				cp.addGeoConstruction(B);
				Point C = new RotatedPoint("C", B, O, 72);
				cp.addGeoConstruction(C);
				// statement
				Segment segAB = new Segment(A, B);
				Segment segAC = new Segment(A, C);
				cp.addThmStatement(new RatioOfTwoSegments(segAC, segAB, (1 + Math.sqrt(5))/2.0));
			}
			break;
		case 46:
			{
				cp.setTheoremName("Equality of two ratios - example");
				
				/*
				 * Let ABC be a triangle and D is some point from its circumscribed circle.
				 * Let M be a foot of perpendicular line from B to AC and N be a foot of
				 * perpendicular line from C to BD. Then following equation for segments holds:
				 * AB/BM = DC/CN (since triangles ABM and DCN are similar).
				 */
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Point C = new FreePoint("C");
				cp.addGeoConstruction(C);
				Circle k = new CircumscribedCircle("k", A, B, C);
				cp.addGeoConstruction(k);
				Point D = new RandomPointFromCircle("D", k);
				cp.addGeoConstruction(D);
				Line AC = new LineThroughTwoPoints("AC", A, C);
				cp.addGeoConstruction(AC);
				Line BD = new LineThroughTwoPoints("BD", B, D);
				cp.addGeoConstruction(BD);
				ShortcutConstruction M = new FootPoint("M", B, AC);
				cp.addGeoConstruction(M);
				ShortcutConstruction N = new FootPoint("N", C, BD);
				cp.addGeoConstruction(N);
				// statement
				Segment segAB = new Segment(A, B);
				Segment segBM = new Segment(B, M.getPoint());
				Segment segDC = new Segment(D, C);
				Segment segCN = new Segment(C, N.getPoint());
				cp.addThmStatement(new EqualityOfTwoRatios(segAB, segBM, segDC, segCN, 1));
			}
			break;
		case 47:
			{
				cp.setTheoremName("Power of point with respect to circle - example");
				
				/*
				 * Let k be a circle and P some point out of it.
				 * Let PT be the tangent segment of circle k and let
				 * some line l through P intersects circle k at two
				 * points A and B. Then following equation for segments holds:
				 * PT^2 = PA*PB (or PT/PB = PA/PT).
				 */
				Point O = new FreePoint("O");
				cp.addGeoConstruction(O);
				Point T = new FreePoint("T");
				cp.addGeoConstruction(T);
				Circle k = new CircleWithCenterAndPoint("k", O, T);
				cp.addGeoConstruction(k);
				Line t = new TangentLine("t", T, k);
				cp.addGeoConstruction(t);
				Point P = new RandomPointFromLine("P", t);
				cp.addGeoConstruction(P);
				Point A = new RandomPointFromCircle("A", k);
				cp.addGeoConstruction(A);
				Line PA = new LineThroughTwoPoints("PA", P, A);
				cp.addGeoConstruction(PA);
				Point B = new IntersectionPoint("B", PA, k);
				cp.addGeoConstruction(B);
				// statement
				Segment segPT = new Segment(P, T);
				Segment segPA = new Segment(P, A);
				Segment segPB = new Segment(P, B);
				cp.addThmStatement(new EqualityOfTwoRatios(segPT, segPB, segPA, segPT, 1));
			}
			break;
		case 48:
			{
				cp.setTheoremName("Power of point with respect to circle - example (2)");
				
				/*
				 * Let k be a circle and P some point out of it.
				 * Let PT be the tangent segment of circle k and let
				 * some line l through P intersects circle k at two
				 * points A and B. Then following equation for segments holds:
				 * PT^2 = PA*PB (or PT/PB = PA/PT).
				 */
				Point P = new FreePoint("P");
				cp.addGeoConstruction(P);
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Line mAB = new PerpendicularBisector("mAB", A, B);
				cp.addGeoConstruction(mAB);
				Point O = new RandomPointFromLine("O", mAB);
				cp.addGeoConstruction(O);
				Circle k = new CircleWithCenterAndPoint("k", O, A);
				cp.addGeoConstruction(k);
				Line t = new TangentLine("t", P, k);
				cp.addGeoConstruction(t);
				Point T = new IntersectionPoint("T", t, k);
				cp.addGeoConstruction(T);
				// statement
				Segment segPT = new Segment(P, T);
				Segment segPA = new Segment(P, A);
				Segment segPB = new Segment(P, B);
				cp.addThmStatement(new EqualityOfTwoRatios(segPT, segPB, segPA, segPT, 1));
			}
			break;
		case 49:
			{
				cp.setTheoremName("Power of point with respect to circle - example (3)");
				
				/*
				 * Let k be a circle and P some point out of it.
				 * Let PT be the tangent segment of circle k and let
				 * some line l through P intersects circle k at two
				 * points A and B. Then following equation for segments holds:
				 * PT^2 = PA*PB (or PT/PB = PA/PT).
				 */
				Point P = new FreePoint("P");
				cp.addGeoConstruction(P);
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Point C = new FreePoint("C");
				cp.addGeoConstruction(C);
				Circle k = new CircumscribedCircle("k", A, B, C);
				cp.addGeoConstruction(k);
				Line t = new TangentLine("t", P, k);
				cp.addGeoConstruction(t);
				Point T = new IntersectionPoint("T", t, k);
				cp.addGeoConstruction(T);
				// statement
				Segment segPT = new Segment(P, T);
				Segment segPA = new Segment(P, A);
				Segment segPB = new Segment(P, B);
				cp.addThmStatement(new EqualityOfTwoRatios(segPT, segPB, segPA, segPT, 1));
			}
			break;
		case 50:
			{
				cp.setTheoremName("Power of point with respect to circle - example (4)");
				
				/*
				 * Let k be a circle and P some point out of it.
				 * Let PT be the tangent segment of circle k and let
				 * some line l through P intersects circle k at two
				 * points A and B. Then following equation for segments holds:
				 * PT^2 = PA*PB (or PT/PB = PA/PT).
				 */
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Point T = new FreePoint("T");
				cp.addGeoConstruction(T);
				Circle k = new CircumscribedCircle("k", A, B, T);
				cp.addGeoConstruction(k);
				Line t = new TangentLine("t", T, k);
				cp.addGeoConstruction(t);
				Line AB = new LineThroughTwoPoints("AB", A, B);
				cp.addGeoConstruction(AB);
				Point P = new IntersectionPoint("P", t, AB);
				cp.addGeoConstruction(P);
				// statement
				Segment segPT = new Segment(P, T);
				Segment segPA = new Segment(P, A);
				Segment segPB = new Segment(P, B);
				cp.addThmStatement(new EqualityOfTwoRatios(segPT, segPB, segPA, segPT, 1));
			}
			break;
		case 51:
			{
				cp.setTheoremName("Ptolemy theorem");
				
				/*
				 * Let ABCD be a cyclic quadrilateral.
				 * If AB=a, BC=b, CD=c, DA=d, AC=d1 and BD=d2 then 
				 * following equation holds: d1*d2 = a*c + b*d.
				 */
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Point C = new FreePoint("C");
				cp.addGeoConstruction(C);
				Circle k = new CircumscribedCircle("k", A, B, C);
				cp.addGeoConstruction(k);
				Point D = new RandomPointFromCircle("D", k);
				cp.addGeoConstruction(D);
				// statement
				Segment segAB = new Segment(A, B); // a
				Segment segBC = new Segment(B, C); // b
				Segment segCD = new Segment(C, D); // c
				Segment segDA = new Segment(D, A); // d
				Segment segAC = new Segment(A, C); // d1
				Segment segBD = new Segment(B, D); // d2
				ProductOfTwoSegments prodac = new ProductOfTwoSegments(segAB, segCD); // a*c
				ProductOfTwoSegments prodbd = new ProductOfTwoSegments(segBC, segDA); // b*d
				ProductOfTwoSegments prodd1d2 = new ProductOfTwoSegments(segAC, segBD); // d1*d2
				cp.addThmStatement(new AlgebraicSumOfThreeSegments(prodac, prodbd, prodd1d2));
			}
			break;
		case 52:
			{
				cp.setTheoremName("Pappus' theorem");
				
				/*
				 * Let A1, B1 and C1 be three collinear points and
				 * A2, B2 and C2 be another three collinear points.
				 * Let have following intersection points:
				 * P = B1C2 x B2C1, Q = A1C2 x A2C1 and R = A1B2 x A2B1.
				 * Then points P, Q and R are also collinear. 
				 */
				Point A1 = new FreePoint("A1");
				cp.addGeoConstruction(A1);
				Point B1 = new FreePoint("B1");
				cp.addGeoConstruction(B1);
				Line l1 = new LineThroughTwoPoints("l1", A1, B1);
				cp.addGeoConstruction(l1);
				Point C1 = new RandomPointFromLine("C1", l1);
				cp.addGeoConstruction(C1);
				Point A2 = new FreePoint("A2");
				cp.addGeoConstruction(A2);
				Point B2 = new FreePoint("B2");
				cp.addGeoConstruction(B2);
				Line l2 = new LineThroughTwoPoints("l2", A2, B2);
				cp.addGeoConstruction(l2);
				Point C2 = new RandomPointFromLine("C2", l2);
				cp.addGeoConstruction(C2);
				// point P
				Line B1C2 = new LineThroughTwoPoints("B1C2", B1, C2);
				cp.addGeoConstruction(B1C2);
				Line B2C1 = new LineThroughTwoPoints("B2C1", B2, C1);
				cp.addGeoConstruction(B2C1);
				Point P = new IntersectionPoint("P", B1C2, B2C1);
				cp.addGeoConstruction(P);
				// point Q
				Line A1C2 = new LineThroughTwoPoints("A1C2", A1, C2);
				cp.addGeoConstruction(A1C2);
				Line A2C1 = new LineThroughTwoPoints("A2C1", A2, C1);
				cp.addGeoConstruction(A2C1);
				Point Q = new IntersectionPoint("Q", A1C2, A2C1);
				cp.addGeoConstruction(Q);
				// point R
				Line A1B2 = new LineThroughTwoPoints("A1B2", A1, B2);
				cp.addGeoConstruction(A1B2);
				Line A2B1 = new LineThroughTwoPoints("A2B1", A2, B1);
				cp.addGeoConstruction(A2B1);
				Point R = new IntersectionPoint("R", A1B2, A2B1);
				cp.addGeoConstruction(R);
				// statement
				ArrayList<Point> collPointsList = new ArrayList<Point>();
				collPointsList.add(P);
				collPointsList.add(Q);
				collPointsList.add(R);
				cp.addThmStatement(new CollinearPoints(collPointsList));
			}
			break;
		case 53:
			{
				cp.setTheoremName("Sum of areas - example");
				
				/*
				 * Let ABCD be a rectangle and O is intersection of diagonals.
				 * Then area of rectangle is sum of areas of following triangles:
				 * ABO, BCO, CDO and DAO.
				 */
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Line AB = new LineThroughTwoPoints("AB", A, B);
				cp.addGeoConstruction(AB);
				Line nB = new PerpendicularLine("nB", AB, B);
				cp.addGeoConstruction(nB);
				Point C = new RandomPointFromLine("C", nB);
				cp.addGeoConstruction(C);
				Point D = new TranslatedPoint("D", B, A, C);
				cp.addGeoConstruction(D);
				Line AC = new LineThroughTwoPoints("AC", A, C);
				cp.addGeoConstruction(AC);
				Line BD = new LineThroughTwoPoints("BD", B, D);
				cp.addGeoConstruction(BD);
				Point O = new IntersectionPoint("O", AC, BD);
				cp.addGeoConstruction(O);
				// statement
				Vector<Point> tri_ABO = new Vector<Point>();
				tri_ABO.add(A);
				tri_ABO.add(B);
				tri_ABO.add(O);
				Vector<Point> tri_BCO = new Vector<Point>();
				tri_BCO.add(B);
				tri_BCO.add(C);
				tri_BCO.add(O);
				Vector<Point> tri_CDO = new Vector<Point>();
				tri_CDO.add(C);
				tri_CDO.add(D);
				tri_CDO.add(O);
				Vector<Point> tri_DAO = new Vector<Point>();
				tri_DAO.add(D);
				tri_DAO.add(A);
				tri_DAO.add(O);
				Vector<Point> rec_ABCD = new Vector<Point>();
				rec_ABCD.add(A);
				rec_ABCD.add(B);
				rec_ABCD.add(C);
				rec_ABCD.add(D);
				Vector<Vector<Point>> polygons = new Vector<Vector<Point>>();
				polygons.add(tri_ABO);
				polygons.add(tri_BCO);
				polygons.add(tri_CDO);
				polygons.add(tri_DAO);
				polygons.add(rec_ABCD);
				Vector<Double> coefficients = new Vector<Double>();
				coefficients.add(new Double(1));
				coefficients.add(new Double(1));
				coefficients.add(new Double(1));
				coefficients.add(new Double(1));
				coefficients.add(new Double(-1));
				cp.addThmStatement(new LinearCombinationOfDoubleSignedPolygonAreas(polygons, coefficients));
			}
			break;
		case 54:
			{
				cp.setTheoremName("Gergonne's point");
				
				/*
				 * If P, Q and R are touch points of inscribed circle of triangle ABC
				 * with sides AB, BC and CA respectively, then lines CP, AQ and BR are
				 * concurrent - they all meet at one same point G - called Gergonne's point.
				 */
				Point O = new FreePoint("O");
				cp.addGeoConstruction(O);
				Point P = new FreePoint("P");
				cp.addGeoConstruction(P);
				Circle k = new CircleWithCenterAndPoint("k", O, P);
				cp.addGeoConstruction(k);
				Point Q = new RandomPointFromCircle("Q", k);
				cp.addGeoConstruction(Q);
				Point R = new RandomPointFromCircle("R", k);
				cp.addGeoConstruction(R);
				Line OP = new LineThroughTwoPoints("OP", O, P);
				cp.addGeoConstruction(OP);
				Line OQ = new LineThroughTwoPoints("OQ", O, Q);
				cp.addGeoConstruction(OQ);
				Line OR = new LineThroughTwoPoints("OR", O, R);
				cp.addGeoConstruction(OR);
				Line c = new PerpendicularLine("c", OP, P);
				cp.addGeoConstruction(c);
				Line a = new PerpendicularLine("a", OQ, Q);
				cp.addGeoConstruction(a);
				Line b = new PerpendicularLine("b", OR, R);
				cp.addGeoConstruction(b);
				Point A = new IntersectionPoint("A", b, c);
				cp.addGeoConstruction(A);
				Point B = new IntersectionPoint("B", a, c);
				cp.addGeoConstruction(B);
				Point C = new IntersectionPoint("C", a, b);
				cp.addGeoConstruction(C);
				Line CP = new LineThroughTwoPoints("CP", C, P);
				cp.addGeoConstruction(CP);
				Line AQ = new LineThroughTwoPoints("AQ", A, Q);
				cp.addGeoConstruction(AQ);
				Line BR = new LineThroughTwoPoints("BR", B, R);
				cp.addGeoConstruction(BR);
				// statement 
				ArrayList<Line> arrLines = new ArrayList<Line>();
				arrLines.add(CP);
				arrLines.add(AQ);
				arrLines.add(BR);
				cp.addThmStatement(new ConcurrentLines(arrLines));
			}
			break;
		case 55:
			{
				cp.setTheoremName("Two congruent triangles - example");
				
				/*
				 * Let ABCD be a kite (deltoid) whose axis is diagonal AC.
				 * Triangles ABC and ACD are congruent.
				 */
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Point C = new FreePoint("C");
				cp.addGeoConstruction(C);
				Line AC = new LineThroughTwoPoints("AC", A, C);
				cp.addGeoConstruction(AC);
				ShortcutConstruction D = new ReflectedPoint("D", B, AC);
				cp.addGeoConstruction(D);
				// statement
				cp.addThmStatement(new CongruentTriangles(A, B, C, A, D.getPoint(), C));
			}
			break;
		case 56: // related to example#22
			{
				cp.setTheoremName("Angle bisectors of parallelogram's angles - (2)");
				
				/*
				 * Let ABCD be a parallelogram and sA and sC are angle
				 * bisectors of angles <A and <C. (Then these lines are parallel.
				 * Also halves of angles <A and <C are equal.)
				 * Let E and F are intersection points of these bisector lines with
				 * opposite sides of parallelogram. Then triangles AED and CFB are congruent.
				 */
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Point C = new FreePoint("C");
				cp.addGeoConstruction(C);
				Point D = new TranslatedPoint("D", B, A, C);
				cp.addGeoConstruction(D);
				Line sA = new AngleBisector("sA", B, A, D);
				cp.addGeoConstruction(sA);
				Line sC = new AngleBisector("sC", D, C, B);
				cp.addGeoConstruction(sC);
				Line CD = new LineThroughTwoPoints("CD", C, D);
				cp.addGeoConstruction(CD);
				Line AB = new LineThroughTwoPoints("AB", A, B);
				cp.addGeoConstruction(AB);
				Point E = new IntersectionPoint("E", sA, CD);
				cp.addGeoConstruction(E);
				Point F = new IntersectionPoint("F", sC, AB);
				cp.addGeoConstruction(F);
				// statement
				cp.addThmStatement(new CongruentTriangles(A, E, D, C, F, B));
			}
			break;
		case 57:
			{
				cp.setTheoremName("Pascal's theorem for general conic");
			
				/*
				 * Let A, B, C, D, E and F be six points from random general conic section.
				 * Let P be an intersection of lines AB and DE, Q intersection of lines
				 * BC and EF and R intersection of lines CD and FA. Then points P, Q and R
				 * are collinear.
				 */
				GeneralConicSection c = new GeneralConicSection("c");
				cp.addGeoConstruction(c);
				Point A = new RandomPointFromGeneralConic("A", c);
				cp.addGeoConstruction(A);
				Point B = new RandomPointFromGeneralConic("B", c);
				cp.addGeoConstruction(B);
				Point C = new RandomPointFromGeneralConic("C", c);
				cp.addGeoConstruction(C);
				Point D = new RandomPointFromGeneralConic("D", c);
				cp.addGeoConstruction(D);
				Point E = new RandomPointFromGeneralConic("E", c);
				cp.addGeoConstruction(E);
				Point F = new RandomPointFromGeneralConic("F", c);
				cp.addGeoConstruction(F);
				Line AB = new LineThroughTwoPoints("AB", A, B);
				cp.addGeoConstruction(AB);
				Line DE = new LineThroughTwoPoints("DE", D, E);
				cp.addGeoConstruction(DE);
				Point P = new IntersectionPoint("P", AB, DE);
				cp.addGeoConstruction(P);
				Line BC = new LineThroughTwoPoints("BC", B, C);
				cp.addGeoConstruction(BC);
				Line EF = new LineThroughTwoPoints("EF", E, F);
				cp.addGeoConstruction(EF);
				Point Q = new IntersectionPoint("Q", BC, EF);
				cp.addGeoConstruction(Q);
				Line CD = new LineThroughTwoPoints("CD", C, D);
				cp.addGeoConstruction(CD);
				Line FA = new LineThroughTwoPoints("FA", F, A);
				cp.addGeoConstruction(FA);
				Point R = new IntersectionPoint("R", CD, FA);
				cp.addGeoConstruction(R);
				// statement
				ArrayList<Point> collPointsList = new ArrayList<Point>();
				collPointsList.add(P);
				collPointsList.add(Q);
				collPointsList.add(R);
				cp.addThmStatement(new CollinearPoints(collPointsList));
			}
			break;
		case 58:
			{
				cp.setTheoremName("Converse of Pascal's theorem for general conic");
		
				/*
				 * Let A, B, C, D and E are five points from random general conic section.
				 * Let P be the intersection of lines AB and DE, Q is random point from line BC
				 * and R is the intersection of lines PQ and CD. Let F be the intersection of
				 * lines EQ and RA. Then F is on same conic section like first five points A-E.
				 */
				GeneralConicSection c = new GeneralConicSection("c");
				cp.addGeoConstruction(c);
				Point A = new RandomPointFromGeneralConic("A", c);
				cp.addGeoConstruction(A);
				Point B = new RandomPointFromGeneralConic("B", c);
				cp.addGeoConstruction(B);
				Point C = new RandomPointFromGeneralConic("C", c);
				cp.addGeoConstruction(C);
				Point D = new RandomPointFromGeneralConic("D", c);
				cp.addGeoConstruction(D);
				Point E = new RandomPointFromGeneralConic("E", c);
				cp.addGeoConstruction(E);
				Line AB = new LineThroughTwoPoints("AB", A, B);
				cp.addGeoConstruction(AB);
				Line DE = new LineThroughTwoPoints("DE", D, E);
				cp.addGeoConstruction(DE);
				Point P = new IntersectionPoint("P", AB, DE);
				cp.addGeoConstruction(P);
				Line BC = new LineThroughTwoPoints("BC", B, C);
				cp.addGeoConstruction(BC);
				Point Q = new RandomPointFromLine("Q", BC);
				cp.addGeoConstruction(Q);
				Line PQ = new LineThroughTwoPoints("PQ", P, Q);
				cp.addGeoConstruction(PQ);
				Line CD = new LineThroughTwoPoints("CD", C, D);
				cp.addGeoConstruction(CD);
				Point R = new IntersectionPoint("R", PQ, CD);
				cp.addGeoConstruction(R);
				Line EQ = new LineThroughTwoPoints("EQ", E, Q);
				cp.addGeoConstruction(EQ);
				Line RA = new LineThroughTwoPoints("RA", R, A);
				cp.addGeoConstruction(RA);
				Point F = new IntersectionPoint("F", EQ, RA);
				cp.addGeoConstruction(F);
				// statement
				cp.addThmStatement(new PointOnSetOfPoints(c, F));
			}
			break;
		case 59: // same thm as in #58
			{
				cp.setTheoremName("Converse of Pascal's theorem for general conic");
	
				/*
				 * Let A, B, C, D and E are five points from random general conic section.
				 * Let P be the intersection of lines AB and DE, Q is random point from line BC
				 * and R is the intersection of lines PQ and CD. Let F be the intersection of
				 * lines EQ and RA. Then F is on same conic section like first five points A-E.
				 */
				Point O = new FreePoint("O");
				cp.addGeoConstruction(O);
				Point M = new FreePoint("M");
				cp.addGeoConstruction(M);
				Line OM = new LineThroughTwoPoints("OM", O, M); // y-axis
				cp.addGeoConstruction(OM);
				Line n = new PerpendicularLine("n", OM, O); // x-axis
				cp.addGeoConstruction(n);
				GeneralConicSection c = new GeneralConicSection("c");
				cp.addGeoConstruction(c);
				Point A = new RandomPointFromGeneralConic("A", c);
				cp.addGeoConstruction(A);
				Point B = new IntersectionPoint("B", c, OM);
				cp.addGeoConstruction(B);
				Point C = new IntersectionPoint("C", c, OM);
				cp.addGeoConstruction(C);
				Point D = new IntersectionPoint("D", c, n);
				cp.addGeoConstruction(D);
				Point E = new IntersectionPoint("E", c, n);
				cp.addGeoConstruction(E);
				Line AB = new LineThroughTwoPoints("AB", A, B);
				cp.addGeoConstruction(AB);
				Line DE = new LineThroughTwoPoints("DE", D, E);
				cp.addGeoConstruction(DE);
				Point P = new IntersectionPoint("P", AB, DE);
				cp.addGeoConstruction(P);
				Line BC = new LineThroughTwoPoints("BC", B, C);
				cp.addGeoConstruction(BC);
				Point Q = new RandomPointFromLine("Q", BC);
				cp.addGeoConstruction(Q);
				Line PQ = new LineThroughTwoPoints("PQ", P, Q);
				cp.addGeoConstruction(PQ);
				Line CD = new LineThroughTwoPoints("CD", C, D);
				cp.addGeoConstruction(CD);
				Point R = new IntersectionPoint("R", PQ, CD);
				cp.addGeoConstruction(R);
				Line EQ = new LineThroughTwoPoints("EQ", E, Q);
				cp.addGeoConstruction(EQ);
				Line RA = new LineThroughTwoPoints("RA", R, A);
				cp.addGeoConstruction(RA);
				Point F = new IntersectionPoint("F", EQ, RA);
				cp.addGeoConstruction(F);
				// statement
				cp.addThmStatement(new PointOnSetOfPoints(c, F));
			}
			break;
		case 60: 
			{
				cp.setTheoremName("Brianchon");

				/*
				 * Let A, B, C, D, E and F are six points from random general conic section.
				 * Let ta, tb, tc, td, te and tf be tangent lines on given conic section in these
				 * points. Let they intersect each other in following points:
				 * A1=taXtb, B1=tbXtc, C1=tcXtd, D1=tdXte, E1=teXtf and F1=tfXta.
				 * Then following lines are concurrent: A1D1, B1E1 and C1F1.
				 */
				GeneralConicSection c = new GeneralConicSection("c");
				cp.addGeoConstruction(c);
				Point A = new RandomPointFromGeneralConic("A", c);
				cp.addGeoConstruction(A);
				Point B = new RandomPointFromGeneralConic("B", c);
				cp.addGeoConstruction(B);
				Point C = new RandomPointFromGeneralConic("C", c);
				cp.addGeoConstruction(C);
				Point D = new RandomPointFromGeneralConic("D", c);
				cp.addGeoConstruction(D);
				Point E = new RandomPointFromGeneralConic("E", c);
				cp.addGeoConstruction(E);
				Point F = new RandomPointFromGeneralConic("F", c);
				cp.addGeoConstruction(F);
				Line ta = new TangentLine("ta", A, c);
				cp.addGeoConstruction(ta);
				Line tb = new TangentLine("tb", B, c);
				cp.addGeoConstruction(tb);
				Line tc = new TangentLine("tc", C, c);
				cp.addGeoConstruction(tc);
				Line td = new TangentLine("td", D, c);
				cp.addGeoConstruction(td);
				Line te = new TangentLine("te", E, c);
				cp.addGeoConstruction(te);
				Line tf = new TangentLine("tf", F, c);
				cp.addGeoConstruction(tf);
				Point A1 = new IntersectionPoint("A1", ta, tb);
				cp.addGeoConstruction(A1);
				Point B1 = new IntersectionPoint("B1", tb, tc);
				cp.addGeoConstruction(B1);
				Point C1 = new IntersectionPoint("C1", tc, td);
				cp.addGeoConstruction(C1);
				Point D1 = new IntersectionPoint("D1", td, te);
				cp.addGeoConstruction(D1);
				Point E1 = new IntersectionPoint("E1", te, tf);
				cp.addGeoConstruction(E1);
				Point F1 = new IntersectionPoint("F1", tf, ta);
				cp.addGeoConstruction(F1);
				Line p = new LineThroughTwoPoints("p", A1, D1);
				cp.addGeoConstruction(p);
				Line q = new LineThroughTwoPoints("q", B1, E1);
				cp.addGeoConstruction(q);
				Line r = new LineThroughTwoPoints("r", C1, F1);
				cp.addGeoConstruction(r);
				Point S = new IntersectionPoint("S", p, q);
				cp.addGeoConstruction(S);
				// statement
				cp.addThmStatement(new PointOnSetOfPoints(r, S));
			}
			break;
		case 61: 
			{
				cp.setTheoremName("Pole and Polar");

				/*
				 * Let A be some random point and c is general conic.
				 * If a is polar of A w.r.t. conic c, then pole P of line a
				 * w.r.t. conic c is equal to point A.
				 */
				GeneralConicSection c = new GeneralConicSection("c");
				cp.addGeoConstruction(c);
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				ShortcutConstruction a = new Polar("a", A, c);
				cp.addGeoConstruction(a);
				ShortcutConstruction P = new Pole("P", a.getLine(), c);
				cp.addGeoConstruction(P);
				// statement
				cp.addThmStatement(new IdenticalPoints(A, P.getPoint()));
			}
			break;
		case 62: 
			{
				cp.setTheoremName("Pole and Polar for circle");
	
				/*
				 * Let A be some random point and c is circle with center O and point B.
				 * If a is polar of A w.r.t. circle c, then pole P of line a
				 * w.r.t. circle c is equal to point A.
				 */
				Point O = new FreePoint("O");
				cp.addGeoConstruction(O);
				Point B = new FreePoint("B");
				cp.addGeoConstruction(B);
				Point A = new FreePoint("A");
				cp.addGeoConstruction(A);
				Circle c = new CircleWithCenterAndPoint("c", O, B);
				cp.addGeoConstruction(c);
				ShortcutConstruction a = new Polar("a", A, c);
				cp.addGeoConstruction(a);
				ShortcutConstruction P = new Pole("P", a.getLine(), c);
				cp.addGeoConstruction(P);
				// statement
				cp.addThmStatement(new IdenticalPoints(A, P.getPoint()));
			}
			break;
		default:
			break;
		}
		
		return cp;
	}
	/*
	 * TESTING PROVING OF SOME THEOREMS - END
	 */
	
	
	
	
	
	/*
	 * MAIN METHOD
	 */
	public static void main (String[] args) {
		/*
		 * Prepare settings
		 */
		OpenGeoProver.settings = new OGPConfigurationSettings();
		//OpenGeoProver.settings.getParameters().putTimeLimit(900000); // 15 min
		//OpenGeoProver.settings.getParameters().putTimeLimit(50400000); // 14 h
		//OpenGeoProver.settings.getParameters().putSpaceLimit(10000000);
		//OpenGeoProver.settings.setOutput(new OGPOutput(null, null)); // no output files
		try {
			OpenGeoProver.settings.setOutput(new OGPOutput(new LaTeXFileWriter("my_latex_test"), null));
			OpenGeoProver.settings.getOutput().openDocument("article", "MTestCP", "Ivan.Petrovic");
		} catch (IOException e) {
			e.printStackTrace();
		} 
		// turn on debug log level
		//((FileLogger) OpenGeoProver.settings.getLogger()).setLevel(Level.DEBUG);
		
		/*
		 * Testing specific methods - not transformations 
		 * to algebraic form and theorem proving 
		 */
		//MTestCP.testConicConditions();
		//MTestCP.testDerivation();
		
		/*
		 * Testing constructions transformations
		 */
		/*
		MTestCP.testConstructionTransformationToAlgebraicForm1();
		MTestCP.testConstructionTransformationToAlgebraicForm2();
		MTestCP.testConstructionTransformationToAlgebraicForm3();
		MTestCP.testConstructionTransformationToAlgebraicForm4();
		MTestCP.testConstructionTransformationToAlgebraicForm5();
		*/
		
		
		/*
		 * *****************************
		 * Testing theorem proving
		 * *****************************
		 */
		/*
		 * Proved
		 */
		/*
		MTestCP.testTheoremProving(1);
		MTestCP.testTheoremProving(2);
		MTestCP.testTheoremProving(3);
		MTestCP.testTheoremProving(4);
		MTestCP.testTheoremProving(5);
		MTestCP.testTheoremProving(7);
		MTestCP.testTheoremProving(8);
		MTestCP.testTheoremProving(9); // too much time for proving because we do not use the fact that E, F and G are collinear
		MTestCP.testTheoremProving(10);
		MTestCP.testTheoremProving(11);
		MTestCP.testTheoremProving(12);
		MTestCP.testTheoremProving(13);
		MTestCP.testTheoremProving(14);
		MTestCP.testTheoremProving(15);
		MTestCP.testTheoremProving(17);
		MTestCP.testTheoremProving(19);
		MTestCP.testTheoremProving(20);
		MTestCP.testTheoremProving(21);
		MTestCP.testTheoremProving(23);
		MTestCP.testTheoremProving(24);
		MTestCP.testTheoremProving(25);
		MTestCP.testTheoremProving(26);
		MTestCP.testTheoremProving(28);
		MTestCP.testTheoremProving(29);
		MTestCP.testTheoremProving(30);
		MTestCP.testTheoremProving(31);
		MTestCP.testTheoremProving(32);
		MTestCP.testTheoremProving(33);
		MTestCP.testTheoremProving(34);
		MTestCP.testTheoremProving(35);
		MTestCP.testTheoremProving(36);
		MTestCP.testTheoremProving(37);
		MTestCP.testTheoremProving(38);
		MTestCP.testTheoremProving(39);
		MTestCP.testTheoremProving(40);
		MTestCP.testTheoremProving(41);
		MTestCP.testTheoremProving(42);
		MTestCP.testTheoremProving(43);
		MTestCP.testTheoremProving(44);
		MTestCP.testTheoremProving(45);
		MTestCP.testTheoremProving(46);
		MTestCP.testTheoremProving(50);
		MTestCP.testTheoremProving(51);
		MTestCP.testTheoremProving(52);
		MTestCP.testTheoremProving(53);
		MTestCP.testTheoremProving(54);
		MTestCP.testTheoremProving(55);
		MTestCP.testTheoremProving(57);
		MTestCP.testTheoremProving(58);
		MTestCP.testTheoremProving(59);
		*/
	   /*
		* Disproved (intentionally or not) or can't be proved/disproved or problems
		*/
	   /*
		MTestCP.testTheoremProving(6);  // can't be proved at this moment !!! (Pascal's thm for conics)
		MTestCP.testTheoremProving(16); // 'false' theorem
		MTestCP.testTheoremProving(18); // testing two disjunctive lines
		MTestCP.testTheoremProving(22); // can't be proved !!! (angle bisectors of paralllelogram's angles)
		MTestCP.testTheoremProving(27); // can't be proved !!! (part of Great problem for triangle: PbPc = AC + AB)
		MTestCP.testTheoremProving(47); // can't be proved !!! (power of point w.r.t. circle)
		MTestCP.testTheoremProving(48); // can't be proved !!! (power of point w.r.t. circle - 2)
		MTestCP.testTheoremProving(49); // can't be proved !!! (power of point w.r.t. circle - 3) - huge complexity
		MTestCP.testTheoremProving(56); // can't be proved !!! (angle bisectors of paralllelogram's angles)
		MTestCP.testTheoremProving(60); // can't be proved !!! (memory and time exceed all limits per process)
		MTestCP.testTheoremProving(61); // can't be proved !!! (memory and time exceed all limits per process)
		MTestCP.testTheoremProving(62); // can't be proved !!! (memory and time exceed all limits per process)
		*/
		
		// put current theorem(s) here
		MTestCP.testTheoremProving(55);
		
		OpenGeoProver.settings.getTimer().cancel(); // cancel timer thread
		
		try {
			OpenGeoProver.settings.getOutput().closeDocument();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}