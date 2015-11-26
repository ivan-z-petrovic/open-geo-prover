/* 
 * DISCLAIMER PLACEHOLDER 
 */
package com.ogprover.pp.tp.auxiliary;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Exception thrown when a prover cannot decide if a statement is true or false.</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Damien Desfontaines
 */
public class UnknownStatementException extends Exception
{
	private static final long serialVersionUID = -5054776550450263992L;

	public UnknownStatementException(String s)
     {
          super(s);
     }
}