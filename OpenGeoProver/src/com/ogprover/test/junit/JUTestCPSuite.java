/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.junit;

import com.ogprover.main.OGPConfigurationSettings;
import com.ogprover.main.OpenGeoProver;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for unit testing suit for Construction Protocol</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class JUTestCPSuite extends TestSuite {
	static public Test suite() {
		TestSuite suite = new TestSuite();
		OpenGeoProver.settings = new OGPConfigurationSettings("JUTestCPSuiteLogFile", "../log/");
		
		// Add single test cases here
		suite.addTestSuite(JUTestPoint.class);
		// TODO - other tests for geometry classes
		
		return suite;
	}
}

