/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.junit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.Power;
import com.ogprover.polynomials.Variable;

import junit.framework.TestCase;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for unit testing of Power</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class JUTestPower extends TestCase {
	private Power pow;
	
	public JUTestPower(String name) {
		super(name);
	}
	
	@Before
	public void setUp() throws Exception{
		pow = new Power(Variable.VAR_TYPE_UX_U, 1, 3);
		OpenGeoProver.settings.getLogger().info("Starting new power test");
	}
	
	@After
	public void tearDown() {
		pow = null;
		OpenGeoProver.settings.getLogger().info("Finished power test");
	}
	
	@Test
	public void testPowerMultiplication() {
		OpenGeoProver.settings.getLogger().info("Testing power multiplication");
		
		Power p = new Power(Variable.VAR_TYPE_UX_U, 1, 4);
		
		pow.mul(p);
		
		assertEquals(Variable.VAR_TYPE_UX_U, pow.getVarType());
		assertEquals(1, pow.getIndex());
		assertEquals(7, pow.getExponent());
	} 
}