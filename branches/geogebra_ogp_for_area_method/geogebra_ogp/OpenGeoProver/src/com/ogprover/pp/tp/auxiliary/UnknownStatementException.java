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
     public UnknownStatementException(String s)
     {
          super(s);
     }
}