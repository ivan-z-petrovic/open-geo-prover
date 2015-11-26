/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.manual;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import com.ogprover.main.OGPConfigurationSettings;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.Polynomial;
import com.ogprover.polynomials.Power;
import com.ogprover.polynomials.Term;
import com.ogprover.polynomials.UFraction;
import com.ogprover.polynomials.UPolynomial;
import com.ogprover.polynomials.UTerm;
import com.ogprover.polynomials.Variable;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.polynomials.XTerm;
import com.ogprover.utilities.OGPUtilities;
import com.ogprover.utilities.Stopwatch;
import com.ogprover.utilities.logger.ILogger;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for manual testing of algebraic primitives</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class MTestPolynomials {
	
	/**
	 * Testing operations over powers.
	 */
	public static void testPowers() {
		System.out.println();
		System.out.println("	TEST FOR POWERS");
		System.out.println();
		
		// Instances of several powers
		Power p1 = new Power(Variable.VAR_TYPE_UX_U, 3, 4); // (u_3)^4
		Power p2 = new Power(Variable.VAR_TYPE_UX_U, 1, 2); // (u_1)^2
		Power p2a = new Power(Variable.VAR_TYPE_UX_U, 1, 5); // (u_1)^5
		Power q1 = new Power(Variable.VAR_TYPE_UX_X, 5, 2); // (x_5)^2
		Power q2 = new Power(Variable.VAR_TYPE_UX_X, 4, 4); // (x_4)^4
		Power q2a = new Power(Variable.VAR_TYPE_UX_X, 4, 1); // x_4
		
		// Comparison of powers
		int result = 0;
		if ((result = p1.compareTo(p2)) > 0)
			System.out.println("Power " + p1.toString() + " is greater than " + p2.toString());
		else if (result < 0)
			System.out.println("Power " + p1.toString() + " is less than " + p2.toString());
		else
			System.out.println("Powers " + p1.toString() + " and " + p2.toString() + " are equals.");
		
		if ((result = q1.compareTo(q2)) > 0)
			System.out.println("Power " + q1.toString() + " is greater than " + q2.toString());
		else if (result < 0)
			System.out.println("Power " + q1.toString() + " is less than " + q2.toString());
		else
			System.out.println("Powers " + q1.toString() + " and " + q2.toString() + " are equals.");
		
		// Multiplication of powers
		System.out.println("Result of multiplication of power " + p2.toString() + " and power " + p2a.toString() + " is power " + p2.mul(p2a).toString());
		System.out.println("Result of multiplication of power " + q2.toString() + " and power " + q2a.toString() + " is power " + q2.mul(q2a).toString());
		System.out.println("Result of multiplication of power " + p1.toString() + " and power " + p2a.toString() + " is power " + p1.mul(p2a).toString()); // nothing is changed in p1
		
		// LaTeX format
		System.out.println("LaTeX format of power " + p1.toString() + " is: " + p1.printToLaTeX());
		System.out.println("LaTeX format of power " + q1.toString() + " is: " + q1.printToLaTeX());
	}
	
	/**
	 * Testing printing vectors of powers.
	 */
	public static void printVectors(Vector<Power> v1, Vector<Power> v2) {
		// Print first vector
		System.out.println("First vector");
		for (int ii = 0; ii < v1.size(); ii++) {
			Power pi = v1.get(ii);
			System.out.println(pi.toString());
		}
		System.out.println();
		
		// Print second vector
		System.out.println("Second vector");
		for (int jj = 0; jj < v2.size(); jj++) {
			Power pj = v2.get(jj);
			System.out.println(pj.toString());
		}
		System.out.println();
	}
	
	/**
	 * Testing operations over vectors of powers.
	 */
	public static void testVectorOfPowers() {
		Vector<Power> v1 = new Vector<Power>();
		Vector<Power> v2 = new Vector<Power>();
		
		// Instances of several powers
		Power p1 = new Power(Variable.VAR_TYPE_UX_U, 7, 2); // (u_7)^2
		Power p2 = new Power(Variable.VAR_TYPE_UX_U, 3, 3); // (u_3)^3
		Power p3 = new Power(Variable.VAR_TYPE_UX_U, 4, 1); // u_4
		Power q1 = new Power(Variable.VAR_TYPE_UX_X, 3, 5); // (x_3)^5
		Power q2 = new Power(Variable.VAR_TYPE_UX_X, 8, 9); // (x_8)^9
		Power q3 = new Power(Variable.VAR_TYPE_UX_X, 1, 3); // (x_1)^3
		
		v1.add(p1);
		v1.add(p2);
		v1.add(p3);
		
		v2.add(q1);
		v2.add(q2);
		v2.add(q3);
		
		// Print vectors
		System.out.println();
		System.out.println("	TEST FOR VECTOR OF POWERS");
		System.out.println();
		
		// printing vectors (1)
		System.out.println(" ===== Initial print ===== ");
		System.out.println();
		printVectors(v1, v2);
		
		// Add third element from second vector at second position of first vector
		Power temp = v2.get(2);
		v1.add(1, temp);
		
		// printing vectors (2)
		System.out.println(" ===== Print after adding ===== ");
		System.out.println();
		printVectors(v1, v2);
		
		// Update third element from second vector - this will reflect to first vector as well
		Power pt = v2.get(2);
		pt.setExponent(5);
		
		// printing vectors (3)
		System.out.println(" ===== Print after update ===== ");
		System.out.println();
		printVectors(v1, v2);
		
		// Remove third element from second vector
		v2.remove(2);
		
		// printing vectors (4)
		System.out.println(" ===== Print after deleting ===== ");
		System.out.println();
		printVectors(v1, v2);
	}
	
	/**
	 * Testing operations over u-terms.
	 */
	public static void testUTerms() {
		System.out.println();
		System.out.println("	TEST FOR U-TERMS");
		System.out.println();
		
		// Several UTerm objects
		Term u1 = new UTerm(7.5);
		Term u2 = new UTerm(-0.23);
		Term u3 = new UTerm(0);
		Term u4 = new UTerm(3);
		
		// u1 = 7.5(u_7)(u_5)^4(u_2)^8
		u1.addPower(new Power(Variable.VAR_TYPE_UX_U, 5, 4)); // adding (u_5)^4
		u1.addPower(new Power(Variable.VAR_TYPE_UX_U, 2, 8)); // adding (u_2)^8
		u1.addPower(new Power(Variable.VAR_TYPE_UX_U, 7, 1)); // adding u_7
		
		// u2 = -0.23(u_12)^2(u_10)^3(u_9)^2(u_7)^5(u_5)(u_3)^3
		u2.addPower(new Power(Variable.VAR_TYPE_UX_U, 5, 1)); // adding u_5
		u2.addPower(new Power(Variable.VAR_TYPE_UX_U, 10, 3)); // adding (u_10)^3
		u2.addPower(new Power(Variable.VAR_TYPE_UX_U, 3, 3)); // adding (u_3)^3
		u2.addPower(new Power(Variable.VAR_TYPE_UX_U, 12, 2)); // adding (u_12)^2
		u2.addPower(new Power(Variable.VAR_TYPE_UX_U, 9, 2)); // adding (u_9)^2
		u2.addPower(new Power(Variable.VAR_TYPE_UX_U, 7, 5)); // adding (u_7)^5
		
		// u3 = 0 (= 0(u_6)(u_4))
		u3.addPower(new Power(Variable.VAR_TYPE_UX_U, 6, 1)); // adding u_6
		u3.addPower(new Power(Variable.VAR_TYPE_UX_U, 4, 1)); // adding u_4
		
		// u4 = 3(u_8)^8
		u4.addPower(new Power(Variable.VAR_TYPE_UX_U, 8, 8)); // adding (u_8)^8
		
		// test now printing with various log levels
		ILogger logger = OpenGeoProver.settings.getLogger();
		System.out.println(u1.toString());
		logger.info("u1 term printed");
		System.out.println(u2.toString());
		logger.debug("u2 term printed");
		System.out.println(u3.toString());
		logger.info("u3 term printed");
		System.out.println(u4.toString());
		logger.error("u4 term printed");
	}
	
	/**
	 * Testing printing collections of terms.
	 */
	public static void printCollectionOfTerms(Collection<Term> col) {
		Iterator<Term> termIT = col.iterator();
		
		System.out.println("List of terms = {");
		 
		if (col.isEmpty() == true)
			System.out.println("Nil");
		else {
			while (termIT.hasNext()) {
				System.out.println(termIT.next().toString());
			}
		}
		System.out.println("}");
	}
	
	/**
	 * Testing operations over tree of terms.
	 */
	public static void testTreeMapOfTerms() {
		TreeMap<Term, Term> terms = new TreeMap<Term, Term>();
		
		System.out.println();
		System.out.println("	TEST FOR TREE MAP OF TERMS");
		System.out.println();
		
		// Several UTerm objects
		Term u1 = new UTerm(2);
		Term u2 = new UTerm(4);
		Term u3 = new UTerm(9.8);
		Term u4 = new UTerm(-3);
		Term u5 = new UTerm(-5.32457);
		Term u6 = new UTerm(Math.PI);
		Term u7 = new UTerm(0);
		Term u8 = new UTerm(2*Math.E);
		Term u9 = new UTerm(11);
		Term u10 = new UTerm(-12);
		Term u11 = new UTerm(0.21);
		Term u12 = new UTerm(45.876);
		
		// u1 = 2(u_9)^5(u_8)(u_7)^7
		u1.addPower(new Power(Variable.VAR_TYPE_UX_U, 9, 5));
		u1.addPower(new Power(Variable.VAR_TYPE_UX_U, 8, 1));
		u1.addPower(new Power(Variable.VAR_TYPE_UX_U, 7, 7));
		// u2 = 4(u_3)^6(u_2)^6(u_1)^3
		u2.addPower(new Power(Variable.VAR_TYPE_UX_U, 1, 3));
		u2.addPower(new Power(Variable.VAR_TYPE_UX_U, 2, 6));
		u2.addPower(new Power(Variable.VAR_TYPE_UX_U, 3, 6));
		// u3 = 9.8(u_9)^4(u_8)^3(u_7)
		u3.addPower(new Power(Variable.VAR_TYPE_UX_U, 8, 3));
		u3.addPower(new Power(Variable.VAR_TYPE_UX_U, 9, 4));
		u3.addPower(new Power(Variable.VAR_TYPE_UX_U, 7, 1));
		// u4 = -3(u_12)^4(u_8)^5
		u4.addPower(new Power(Variable.VAR_TYPE_UX_U, 12, 4));
		u4.addPower(new Power(Variable.VAR_TYPE_UX_U, 8, 5));
		// u5 = -5.32457(u_11)(u_4)^6(u_3)^5
		u5.addPower(new Power(Variable.VAR_TYPE_UX_U, 4, 6));
		u5.addPower(new Power(Variable.VAR_TYPE_UX_U, 3, 5));
		u5.addPower(new Power(Variable.VAR_TYPE_UX_U, 11, 1));
		// u6 = Math.PI(u_9)(u_8)^5(u_7)
		u6.addPower(new Power(Variable.VAR_TYPE_UX_U, 9, 1));
		u6.addPower(new Power(Variable.VAR_TYPE_UX_U, 8, 5));
		u6.addPower(new Power(Variable.VAR_TYPE_UX_U, 7, 1));
		// u7 = 0 = 0(u_2)^2(u_1)^2
		u7.addPower(new Power(Variable.VAR_TYPE_UX_U, 2, 2));
		u7.addPower(new Power(Variable.VAR_TYPE_UX_U, 1, 2));
		// u8 = 2*Math.E(u_10)^9(u_3)^3(u_2)^2
		u8.addPower(new Power(Variable.VAR_TYPE_UX_U, 2, 2));
		u8.addPower(new Power(Variable.VAR_TYPE_UX_U, 10, 9));
		u8.addPower(new Power(Variable.VAR_TYPE_UX_U, 3, 3));
		// u9 = 11(u_4)^9(u_2)(u_1)
		u9.addPower(new Power(Variable.VAR_TYPE_UX_U, 4, 9));
		u9.addPower(new Power(Variable.VAR_TYPE_UX_U, 2, 1));
		u9.addPower(new Power(Variable.VAR_TYPE_UX_U, 1, 1));
		// u10 = -12(u_11)^8(u_4)^2(u_3)^3
		u10.addPower(new Power(Variable.VAR_TYPE_UX_U, 11, 8));
		u10.addPower(new Power(Variable.VAR_TYPE_UX_U, 4, 2));
		u10.addPower(new Power(Variable.VAR_TYPE_UX_U, 3, 3));
		// u11 = 0.21(u_3)^6(u_2)^6(u_1)^3
		u11.addPower(new Power(Variable.VAR_TYPE_UX_U, 3, 6));
		u11.addPower(new Power(Variable.VAR_TYPE_UX_U, 2, 6));
		u11.addPower(new Power(Variable.VAR_TYPE_UX_U, 1, 3));
		// u12 = 45.876(u_9)^5(u_8)(u_7)^7
		u12.addPower(new Power(Variable.VAR_TYPE_UX_U, 7, 7));
		u12.addPower(new Power(Variable.VAR_TYPE_UX_U, 8, 1));
		u12.addPower(new Power(Variable.VAR_TYPE_UX_U, 9, 5));
		
		// Adding terms to tree
		// and printing in 'inorder tree pass (from left to right)' manner 
		// i.e. in ascending order
		System.out.println("Initial tree contains ");
		printCollectionOfTerms(terms.values());
		System.out.println("===============================");
		System.out.println();
		// u1
		terms.put(u1, u1);
		System.out.println("Added term u1 = " + u1.toString());
		printCollectionOfTerms(terms.values());
		System.out.println("===============================");
		System.out.println();
		// u2
		terms.put(u2, u2);
		System.out.println("Added term u2 = " + u2.toString());
		printCollectionOfTerms(terms.values());
		System.out.println("===============================");
		System.out.println();
		// u3
		terms.put(u3, u3);
		System.out.println("Added term u3 = " + u3.toString());
		printCollectionOfTerms(terms.values());
		System.out.println("===============================");
		System.out.println();
		// u4
		terms.put(u4, u4);
		System.out.println("Added term u4 = " + u4.toString());
		printCollectionOfTerms(terms.values());
		System.out.println("===============================");
		System.out.println();
		// u5
		terms.put(u5, u5);
		System.out.println("Added term u5 = " + u5.toString());
		printCollectionOfTerms(terms.values());
		System.out.println("===============================");
		System.out.println();
		// u6
		terms.put(u6, u6);
		System.out.println("Added term u6 = " + u6.toString());
		printCollectionOfTerms(terms.values());
		System.out.println("===============================");
		System.out.println();
		// u7
		terms.put(u7, u7);
		System.out.println("Added term u7 = " + u7.toString());
		printCollectionOfTerms(terms.values());
		System.out.println("===============================");
		System.out.println();
		// u8
		terms.put(u8, u8);
		System.out.println("Added term u8 = " + u8.toString());
		printCollectionOfTerms(terms.values());
		System.out.println("===============================");
		System.out.println();
		// u9
		terms.put(u9, u9);
		System.out.println("Added term u9 = " + u9.toString());
		printCollectionOfTerms(terms.values());
		System.out.println("===============================");
		System.out.println();
		// u10
		terms.put(u10, u10);
		System.out.println("Added term u10 = " + u10.toString());
		printCollectionOfTerms(terms.values());
		System.out.println("===============================");
		System.out.println();
		// u11 - updates previously added u2
		terms.put(u11, u11);
		System.out.println("Added term u11 = " + u11.toString());
		printCollectionOfTerms(terms.values());
		System.out.println("===============================");
		System.out.println();
		// u12 - updates previously added u1
		terms.put(u12, u12);
		System.out.println("Added term u12 = " + u12.toString());
		printCollectionOfTerms(terms.values());
		System.out.println("===============================");
		System.out.println();
		
		// go to second element (smallest non-zero term) and multiply by term (u_13);
		// this will break the order
		Collection<Term> col = terms.values();
		Iterator<Term> itCol = col.iterator();
		Term second = null;
		int ii = 0;
		
		while (itCol.hasNext() && ii < 2) {
			second = itCol.next();
			ii++;
		}
		Term ut = new UTerm(1);
		ut.addPower(new Power(Variable.VAR_TYPE_UX_U, 13, 1));
		second.mul(ut);
		System.out.println("Second term multiplied by {u_13}");
		printCollectionOfTerms(terms.values());
		System.out.println("===============================");
		System.out.println();
		// Now try to get this term from tree - this will fail since order is broken
		System.out.println("Try to find term "+ second.toString() + " in tree:");
		Term secondFromTree = terms.get(second);
		if (secondFromTree == null)
			System.out.println("Not found.");
		else
			System.out.println("Found term " + secondFromTree.toString());
	}
	
	/**
	 * Testing operations over u-polynomials.
	 */
	public static void testUPolynomials() {
		System.out.println();
		System.out.println("	TEST FOR U-POLYNOMIALS");
		System.out.println();
		
		// Several UPolynomial objects
		Polynomial up1 = new UPolynomial();
		Polynomial up2 = new UPolynomial();
		Polynomial up3 = new UPolynomial(Math.sqrt(2)); // constant polynomial: up3 = sqrt(2)
		Polynomial up4 = new UPolynomial();
		Term t = null;
		
		// up1 = 7(u_12)^5(u_11)^6 + 4.5(u_2)(u_1)^5 - 5(u_1)
		t = new UTerm(7);
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 12, 5));
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 11, 6));
		up1.addTerm(t);
		t = new UTerm(-5);
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 1, 1));
		up1.addTerm(t);
		t = new UTerm(4.5);
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 2, 1));
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 1, 5));
		up1.addTerm(t);
		// up2 = 13(u_7)(u_6)(u_5) - 10(u_2)(u_1)
		t = new UTerm(-10);
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 2, 1));
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 1, 1));
		up2.addTerm(t);
		t = new UTerm(13);
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 7, 1));
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 6, 1));
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 5, 1));
		up2.addTerm(t);
		// up4 = -0.9(u_2)^5(u_1)^5 - 12(u_2)(u_1) + 15.43
		t = new UTerm(-12);
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 2, 1));
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 1, 1));
		up4.addTerm(t);
		t = new UTerm(15.43);
		up4.addTerm(t);
		t = new UTerm(-0.9);
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 2, 5));
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 1, 5));
		up4.addTerm(t);
		
		System.out.println("Polynomials in LaTeX format");
		System.out.println();
		System.out.print("up1 = ");
		System.out.println(up1.printToLaTeX());
		System.out.println("==================");
		System.out.print("up2 = ");
		System.out.println(up2.printToLaTeX());
		System.out.println("==================");
		System.out.print("up3 = ");
		System.out.println(up3.printToLaTeX());
		System.out.println("==================");
		System.out.print("up4 = ");
		System.out.println(up4.printToLaTeX());
		System.out.println("==================");
		System.out.println();
		
		Polynomial upmul24 = up2.clone();
		upmul24.multiplyByPolynomial(up4);
		System.out.print("up2 * up4 = ");
		System.out.println(upmul24.printToLaTeX());
		System.out.println("==================");
		Polynomial upmul42 = up4.clone();
		upmul42.multiplyByPolynomial(up2);
		System.out.print("up4 * up2 = ");
		System.out.println(upmul42.printToLaTeX());
		System.out.println("==================");
		
		Term ut = new UTerm(1);
		ut.addPower(new Power(Variable.VAR_TYPE_UX_U, 2, 1));
		Polynomial upmul2t = up2.clone();
		upmul2t.multiplyByTerm(ut);
		System.out.print("up2 * (u_2) = ");
		System.out.println(upmul2t.printToLaTeX());
		System.out.println("==================");
	}
	
	/**
	 * Testing printing of decimals.
	 */
	public static void testPrintingDecimals(){
		System.out.println();
		System.out.println(Math.round(-11.78934) + "");
		System.out.println();
		System.out.println(OGPUtilities.roundUpToPrecision(-11.786548976) + "");
		System.out.println();
		System.out.println(OGPUtilities.roundUpToPrecision(-11.700000000000001) + "");
	}
	
	/**
	 * Testing operations over u-fractions.
	 */
	public static void testUFractions() {
		System.out.println();
		System.out.println("	TEST FOR U-FRACTIONS");
		System.out.println();
		
		Term t = null;
		
		UPolynomial up1 = new UPolynomial();
		UPolynomial up2 = new UPolynomial();
		// up1 = 7(u_12)^5(u_11)^6 + 4.5(u_2)(u_1)^5 - 5(u_1)
		t = new UTerm(7);
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 12, 5));
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 11, 6));
		up1.addTerm(t);
		t = new UTerm(-5);
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 1, 1));
		up1.addTerm(t);
		t = new UTerm(4.5);
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 2, 1));
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 1, 5));
		up1.addTerm(t);
		// up2 = 13(u_7)(u_6)(u_5) - 10(u_2)(u_1)
		t = new UTerm(-10);
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 2, 1));
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 1, 1));
		up2.addTerm(t);
		t = new UTerm(13);
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 7, 1));
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 6, 1));
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 5, 1));
		up2.addTerm(t);
		
		UFraction uf12 = new UFraction(up1, up2);
		
		UPolynomial up3 = new UPolynomial(Math.sqrt(2)); // constant polynomial: up3 = sqrt(2)
		UPolynomial up4 = new UPolynomial();
		// up4 = -0.9(u_2)^5(u_1)^5 - 12(u_2)(u_1) + 15.43
		t = new UTerm(-12);
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 2, 1));
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 1, 1));
		up4.addTerm(t);
		t = new UTerm(15.43);
		up4.addTerm(t);
		t = new UTerm(-0.9);
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 2, 5));
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 1, 5));
		up4.addTerm(t);
		
		UFraction uf34 = new UFraction(up3, up4);
		
		UPolynomial up5 = new UPolynomial();
		UPolynomial up6 = new UPolynomial();
		// up5 = (u_7)^5(u_2)^3(u_1) - 3(u_8)(u_6)(u_2)(u_1)
		t = new UTerm(1);
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 7, 5));
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 2, 3));
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 1, 1));
		up5.addTerm(t);
		t = new UTerm(-3);
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 8, 1));
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 6, 1));
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 2, 1));
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 1, 1));
		up5.addTerm(t);
		// up6 = 2.8(u_7)^2(u_6)(u_2)^5(u_1)^3 + 0.4(u_4)^4(u_2)^2(u_1)^2
		t = new UTerm(2.8);
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 7, 2));
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 6, 1));
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 2, 5));
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 1, 3));
		up6.addTerm(t);
		t = new UTerm(0.4);
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 4, 4));
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 2, 2));
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 1, 2));
		up6.addTerm(t);
		
		UFraction uf56 = new UFraction(up5, up6);
		
		System.out.println("up1 = " + up1.printToLaTeX() + "; up2 = " + up2.printToLaTeX() + ";");
		System.out.println();
		System.out.println("uf12 = up1/up2 = " + uf12.printToLaTeX());
		System.out.println();
		System.out.println("up3 = " + up3.printToLaTeX() + "; up4 = " + up4.printToLaTeX() + ";");
		System.out.println();
		System.out.println("uf34 = up3/up4 = " + uf34.printToLaTeX());
		System.out.println();
		System.out.println("up5 = " + up5.printToLaTeX() + "; up6 = " + up6.printToLaTeX() + ";");
		System.out.println();
		System.out.println("uf56 = up5/up6 = " + uf56.printToLaTeX());
		System.out.println();
		System.out.println("===== Operations =====");
		System.out.println();
		System.out.println("2 * uf12 = " + uf12.clone().mul(2).printToLaTeX());
		System.out.println();
		System.out.println("1/uf34 = " + uf34.clone().invertFraction().printToLaTeX());
		System.out.println();
		System.out.println("1/uf34 after reduction is " + uf34.clone().invertFraction().reduce().printToLaTeX());
		System.out.println();
		System.out.println("uf12 - uf34 = " + uf12.clone().subtract(uf34).printToLaTeX());
		System.out.println();
		System.out.println("uf34 * uf12 = " + uf34.clone().mul(uf12).printToLaTeX());
		System.out.println();
		System.out.println("uf56 after reduction = " + uf56.clone().reduce().printToLaTeX());
		System.out.println();
	}
	
	/**
	 * Testing operations over x-terms.
	 */
	public static void testXTerms() {
		System.out.println();
		System.out.println("	TEST FOR XTERMS");
		System.out.println();
		
		Term t;
		Polynomial p;
		
		// xt1 = (2(u_2)^2 + (u_1))(x_2)(x_1)^2
		Term xt1;
		p = new UPolynomial();
		t = new UTerm(2);
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 2, 2));
		p.addTerm(t);
		t = new UTerm(1);
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 1, 1));
		p.addTerm(t);
		xt1 = new XTerm(new UFraction((UPolynomial)p));
		xt1.addPower(new Power(Variable.VAR_TYPE_UX_X, 2, 1));
		xt1.addPower(new Power(Variable.VAR_TYPE_UX_X, 1, 2));
		// xt2 = ((u_2) + 2(u_1)^2)(x_2)(x_1)^2
		Term xt2;
		p = new UPolynomial();
		t = new UTerm(1);
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 2, 1));
		p.addTerm(t);
		t = new UTerm(2);
		t.addPower(new Power(Variable.VAR_TYPE_UX_U, 1, 2));
		p.addTerm(t);
		xt2 = new XTerm(new UFraction((UPolynomial)p));
		xt2.addPower(new Power(Variable.VAR_TYPE_UX_X, 2, 1));
		xt2.addPower(new Power(Variable.VAR_TYPE_UX_X, 1, 2));
		// xt3 = merge(xt1, xt2)
		Term xt3 = xt1.clone().merge(xt2);
		// xt4 = mul(xt1, xt2)
		Term xt4 = xt1.clone().mul(xt2);
		System.out.println("xt1 = " + xt1.printToLaTeX());
		System.out.println();
		System.out.println("xt2 = " + xt2.printToLaTeX());
		System.out.println();
		System.out.println("xt3 = merge(xt1, xt2) = " + xt3.printToLaTeX());
		System.out.println();
		System.out.println("xt4 = mul(xt1, xt2) = " + xt4.printToLaTeX());
	}
	
	/**
	 * Testing operations over x-polynomials.
	 */
	public static void testXPolynomials() {
		OpenGeoProver.settings.getLogger().info("Starting XPolynomials test");
		System.out.println();
		System.out.println("	TEST FOR X - POLYNOMIALS");
		System.out.println();
		
		System.out.println();
		System.out.println();
		System.out.println("	First example:");
		System.out.println();
		Stopwatch s = new Stopwatch();
		XPolynomial p, q, r;
		XTerm xt;
		UTerm ut;
		UPolynomial up;
		
		p = new XPolynomial();
		// ...
		xt = new XTerm(new UFraction(-1));
		xt.addPower(new Power(Variable.VAR_TYPE_UX_X, 3, 2));
		p.addTerm(xt);
		// ...
		up = new UPolynomial();
		ut = new UTerm(2);
		ut.addPower(new Power(Variable.VAR_TYPE_UX_U, 1, 1));
		up.addTerm(ut);
		xt = new XTerm(new UFraction((UPolynomial)up));
		xt.addPower(new Power(Variable.VAR_TYPE_UX_X, 3, 1));
		p.addTerm(xt);
		// ...
		xt = new XTerm(new UFraction(-1));
		xt.addPower(new Power(Variable.VAR_TYPE_UX_X, 2, 2));
		p.addTerm(xt);
		// ...
		up = new UPolynomial();
		ut = new UTerm(1);
		ut.addPower(new Power(Variable.VAR_TYPE_UX_U, 3, 2));
		up.addTerm(ut);
		ut = new UTerm(1);
		ut.addPower(new Power(Variable.VAR_TYPE_UX_U, 2, 2));
		up.addTerm(ut);
		ut = new UTerm(-2);
		ut.addPower(new Power(Variable.VAR_TYPE_UX_U, 2, 1));
		ut.addPower(new Power(Variable.VAR_TYPE_UX_U, 1, 1));
		up.addTerm(ut);
		xt = new XTerm(new UFraction((UPolynomial)up));
		p.addTerm(xt);
		
		q = new XPolynomial();
		// ...
		up = new UPolynomial();
		ut = new UTerm(-1);
		ut.addPower(new Power(Variable.VAR_TYPE_UX_U, 3, 1));
		up.addTerm(ut);
		xt = new XTerm(new UFraction((UPolynomial)up));
		xt.addPower(new Power(Variable.VAR_TYPE_UX_X, 3, 1));
		q.addTerm(xt);
		// ...
		up = new UPolynomial();
		ut = new UTerm(1);
		ut.addPower(new Power(Variable.VAR_TYPE_UX_U, 2, 1));
		up.addTerm(ut);
		xt = new XTerm(new UFraction((UPolynomial)up));
		xt.addPower(new Power(Variable.VAR_TYPE_UX_X, 2, 1));
		q.addTerm(xt);
		
		r = (XPolynomial)p.clone();
		s.startMeasureTime();
		r = r.pseudoReminder(q, 3);
		s.endMeasureTime();
		
		System.out.println("p = " + p.print());
		System.out.println();
		System.out.println("q = " + q.print());
		System.out.println();
		System.out.println("==================================");
		System.out.println();
		System.out.println("prem(p, q, x_3) = " + r.print());
		System.out.println();
		System.out.println("Reminder calculated in " + s.getTimeIntMillisec() + " millisec.");
		
		
		System.out.println();
		System.out.println();
		System.out.println("	Second example:");
		System.out.println();
		XPolynomial p1, q1, r1;
		
		p1 = new XPolynomial();
		// ...
		up = new UPolynomial();
		ut = new UTerm(-1);
		ut.addPower(new Power(Variable.VAR_TYPE_UX_U, 3, 4));
		up.addTerm(ut);
		ut = new UTerm(-1);
		ut.addPower(new Power(Variable.VAR_TYPE_UX_U, 3, 2));
		ut.addPower(new Power(Variable.VAR_TYPE_UX_U, 2, 2));
		up.addTerm(ut);
		xt = new XTerm(new UFraction((UPolynomial)up));
		xt.addPower(new Power(Variable.VAR_TYPE_UX_X, 2, 1));
		p1.addTerm(xt);
		// ...
		up = new UPolynomial();
		ut = new UTerm(1);
		ut.addPower(new Power(Variable.VAR_TYPE_UX_U, 3, 3));
		ut.addPower(new Power(Variable.VAR_TYPE_UX_U, 2, 1));
		up.addTerm(ut);
		ut = new UTerm(-2);
		ut.addPower(new Power(Variable.VAR_TYPE_UX_U, 3, 3));
		ut.addPower(new Power(Variable.VAR_TYPE_UX_U, 1, 1));
		up.addTerm(ut);
		ut = new UTerm(1);
		ut.addPower(new Power(Variable.VAR_TYPE_UX_U, 3, 1));
		ut.addPower(new Power(Variable.VAR_TYPE_UX_U, 2, 3));
		up.addTerm(ut);
		ut = new UTerm(-2);
		ut.addPower(new Power(Variable.VAR_TYPE_UX_U, 3, 1));
		ut.addPower(new Power(Variable.VAR_TYPE_UX_U, 2, 2));
		ut.addPower(new Power(Variable.VAR_TYPE_UX_U, 1, 1));
		up.addTerm(ut);
		xt = new XTerm(new UFraction((UPolynomial)up));
		xt.addPower(new Power(Variable.VAR_TYPE_UX_X, 1, 1));
		p1.addTerm(xt);
		// ...
		up = new UPolynomial();
		ut = new UTerm(1);
		ut.addPower(new Power(Variable.VAR_TYPE_UX_U, 3, 4));
		ut.addPower(new Power(Variable.VAR_TYPE_UX_U, 1, 1));
		up.addTerm(ut);
		ut = new UTerm(1);
		ut.addPower(new Power(Variable.VAR_TYPE_UX_U, 3, 2));
		ut.addPower(new Power(Variable.VAR_TYPE_UX_U, 2, 2));
		ut.addPower(new Power(Variable.VAR_TYPE_UX_U, 1, 1));
		up.addTerm(ut);
		xt = new XTerm(new UFraction((UPolynomial)up));
		p1.addTerm(xt);
		
		q1 = new XPolynomial();
		// ...
		up = new UPolynomial();
		ut = new UTerm(1);
		ut.addPower(new Power(Variable.VAR_TYPE_UX_U, 3, 1));
		up.addTerm(ut);
		xt = new XTerm(new UFraction((UPolynomial)up));
		xt.addPower(new Power(Variable.VAR_TYPE_UX_X, 2, 1));
		q1.addTerm(xt);
		// ...
		up = new UPolynomial();
		ut = new UTerm(-1);
		ut.addPower(new Power(Variable.VAR_TYPE_UX_U, 2, 1));
		up.addTerm(ut);
		ut = new UTerm(1);
		ut.addPower(new Power(Variable.VAR_TYPE_UX_U, 1, 1));
		up.addTerm(ut);
		xt = new XTerm(new UFraction((UPolynomial)up));
		xt.addPower(new Power(Variable.VAR_TYPE_UX_X, 1, 1));
		q1.addTerm(xt);
		
		r1 = (XPolynomial)p1.clone();
		s.startMeasureTime();
		r1 = r1.pseudoReminder(q1, 2);
		s.endMeasureTime();
		
		System.out.println("p1 = " + p1.print());
		System.out.println();
		System.out.println("q1 = " + q1.print());
		System.out.println();
		System.out.println("==================================");
		System.out.println();
		System.out.println("prem(p1, q1, x_2) = " + r1.print());
		System.out.println();
		System.out.println("Reminder calculated in " + s.getTimeIntMillisec() + " millisec.");
		
		// reduction
		UPolynomial num, den;
		UTerm tempUT;
		UFraction tempUF;
		XTerm tempXT;
		XPolynomial testXP = new XPolynomial();
		
		num = new UPolynomial();
		den = new UPolynomial();
		tempUT = new UTerm(1);
		tempUT.addPower(new Power(Variable.VAR_TYPE_UX_U, 2, 1));
		tempUT.addPower(new Power(Variable.VAR_TYPE_UX_U, 1, 1));
		num.addTerm(tempUT);
		tempUT = new UTerm(1);
		tempUT.addPower(new Power(Variable.VAR_TYPE_UX_U, 1, 1));
		num.addTerm(tempUT);
		tempUT = new UTerm(1);
		tempUT.addPower(new Power(Variable.VAR_TYPE_UX_U, 3, 2));
		den.addTerm(tempUT);
		tempUT = new UTerm(1);
		tempUT.addPower(new Power(Variable.VAR_TYPE_UX_U, 3, 1));
		tempUT.addPower(new Power(Variable.VAR_TYPE_UX_U, 2, 1));
		den.addTerm(tempUT);
		tempUF = new UFraction(num, den);
		tempXT = new XTerm(tempUF);
		tempXT.addPower(new Power(Variable.VAR_TYPE_UX_X, 2, 1));
		testXP.addTerm(tempXT);
		
		num = new UPolynomial();
		den = new UPolynomial();
		tempUT = new UTerm(1);
		tempUT.addPower(new Power(Variable.VAR_TYPE_UX_U, 1, 2));
		num.addTerm(tempUT);
		tempUT = new UTerm(1);
		tempUT.addPower(new Power(Variable.VAR_TYPE_UX_U, 1, 1));
		num.addTerm(tempUT);
		tempUT = new UTerm(1);
		tempUT.addPower(new Power(Variable.VAR_TYPE_UX_U, 4, 1));
		tempUT.addPower(new Power(Variable.VAR_TYPE_UX_U, 3, 1));
		den.addTerm(tempUT);
		tempUT = new UTerm(-1);
		tempUT.addPower(new Power(Variable.VAR_TYPE_UX_U, 3, 1));
		den.addTerm(tempUT);
		tempUF = new UFraction(num, den);
		tempXT = new XTerm(tempUF);
		tempXT.addPower(new Power(Variable.VAR_TYPE_UX_X, 1, 1));
		testXP.addTerm(tempXT);
		
		
		System.out.println();
		System.out.println();
		System.out.println("Reducing polynomial:" + testXP.print());
		System.out.println();
		System.out.println("Result of reduction:" + testXP.reduceByUTermDivision().print());
		System.out.println();
		
	}
	
	public static void main (String[] args) {
		OpenGeoProver.settings = new OGPConfigurationSettings();
		// MTestPolynomials.testPrintingDecimals();
		// MTestPolynomials.testPowers();
		// MTestPolynomials.testVectorOfPowers();
		// MTestPolynomials.testUTerms();
		// MTestPolynomials.testTreeMapOfTerms();
		// MTestPolynomials.testUPolynomials();
		// MTestPolynomials.testPolynomialsMultiplying();
		// MTestPolynomials.testPolynomialsMultiplyingInLoop(30);
		// MTestPolynomials.testUFractions();
		// MTestPolynomials.testXTerms();
		 MTestPolynomials.testXPolynomials();
		 
		OpenGeoProver.settings.getTimer().cancel(); // cancel timer thread
	}
}
