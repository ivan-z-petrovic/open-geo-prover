/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.junit;

import java.io.File;

import com.ogprover.main.OGPConfigurationSettings;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.utilities.logger.GeoGebraLogger;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for unit testing suit for Polynomials</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class JUTestPolynomialsSuite extends TestSuite {
	static public Test suite() {
		TestSuite suite = new TestSuite();
		OpenGeoProver.settings = new OGPConfigurationSettings("JUTestPolynomialsSuiteLogFile", ".." + File.separator + GeoGebraLogger.DEFAULT_LOG_DIR);
		
		// Add single test cases here
		suite.addTestSuite(JUTestPower.class);
		suite.addTestSuite(JUTestUTerm.class);
		// TODO - other tests for algebraic classes
		
		return suite;
	}
}

