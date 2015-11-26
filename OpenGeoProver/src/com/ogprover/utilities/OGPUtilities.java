/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.utilities;

import com.ogprover.main.OGPConstants;
import com.ogprover.polynomials.SymbolicVariable;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class with various utility methods for OGP</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class OGPUtilities {
	/**
	 * Rounding of number
	 * 
	 * @param d		Input number to be rounded
	 * @return		Rounded number up to precision defined for this prover
	 */
	public static double roundUpToPrecision(double d){
		return Math.round(d * OGPConstants.precPower)/(double)OGPConstants.precPower;
	}
	
	/** 
	 * Method that calculates the index of symbolic variable (e.g. x_A or y_B).
	 * <br><br>
	 * For symbolic variables we need types to be two prime numbers - 
	 * the smallest two - 2 and 3, since we are going to use these types' values
	 * for calculation of variable index of symbolic variable; e.g.:
	 * 
	 * 		x_A will have index 2^(code(A)), and y_A will have index 3^(code(A)).
	 * <br><br>
	 * Therefore there will not be two variables x_A and y_B with same index values
	 * (since point labels are always non-empty strings).
	 * Code of point label will be calculated as hash code of string that represents
	 * the point label. Then using this value as exponent for powers of 2 and 3
	 * could produce very huge numbers:  
	 * (code(A) = A[0]*31^(n-1) + A[1]*31^(n-2) + ... + A[n-1], where n=A.length()).
	 * <br><br>
	 * Instead of that we calculate log10(2^code(A)) = code(A)*log10(2) and 
	 * log10(3^code(A)) = code(A)*log10(3). We can do it since log10(x) is bijection.
	 * We then multiply these values by 10^precision (precision is usually 6) 
	 * and round that value - we assume that such value will be unique - 
	 * we use power of precision since we compare double values in this system
	 * up to set precision. This function is defined in utilities.
	 * <br><br>
	 * @param v		Symbolic variable whose index is calculated
	 * 
	 * @return		Index of symbolic variable passed in as argument
	 */
	public static long getSymbolicVariableIndex(SymbolicVariable v) {
		String label = v.getPointLabel();
		int varType = v.getVariableType();
		
		return Math.round(label.hashCode() * Math.log10(varType) * OGPConstants.precPower);
	}
}