/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.junit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ogprover.main.OGPConstants;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.Power;
import com.ogprover.polynomials.UTerm;
import com.ogprover.polynomials.Variable;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for unit testing of UTerm</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class JUTestUTerm extends TestCase {
	private UTerm uterm;
	
	public JUTestUTerm(String name) {
		super(name);
	}
	
	@Before
	public void setUp() throws Exception{
		uterm = new UTerm(3);
		uterm.addPower(new Power(Variable.VAR_TYPE_UX_U, 1, 2));
		uterm.addPower(new Power(Variable.VAR_TYPE_UX_U, 2, 1));
		OpenGeoProver.settings.getLogger().info("Starting new uterm test");
	}
	
	@After
	public void tearDown() {
		uterm = null;
		OpenGeoProver.settings.getLogger().info("Finished uterm test");
	}
	
	@Test
	public void testUTermsMultiplication() {
		OpenGeoProver.settings.getLogger().info("Testing uterm multiplication");
		
		UTerm tempUT = new UTerm(0.4);
		tempUT.addPower(new Power(Variable.VAR_TYPE_UX_U, 1, 5));
		tempUT.addPower(new Power(Variable.VAR_TYPE_UX_U, 2, 3));
		
		uterm.mul(tempUT);
		
		tempUT = null;
		
		Assert.assertEquals(1.2, uterm.getCoeff(), OGPConstants.EPSILON);
		Assert.assertEquals(4, uterm.getPowers().get(0).getExponent());
		Assert.assertEquals(7, uterm.getPowers().get(1).getExponent());
	} 
}