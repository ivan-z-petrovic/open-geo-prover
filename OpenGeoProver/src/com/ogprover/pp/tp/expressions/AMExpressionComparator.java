/* 
 * DISCLAIMER PLACEHOLDER 
 */
package com.ogprover.pp.tp.expressions;

import java.util.Comparator;
 
/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class to compare two area method expressions</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Damien Desfontaines
 */
public class AMExpressionComparator implements Comparator<AMExpression> {
 
    public int compare(AMExpression expr1, AMExpression expr2) {
 
    	String name1 = expr1.print();
    	String name2 = expr2.print();
 
    	return name1.compareTo(name2);
    }
}