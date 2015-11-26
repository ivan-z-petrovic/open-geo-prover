/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.junit;

import java.io.File;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.ogprover.main.OGPConfigurationSettings;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.utilities.logger.GeoGebraLogger;

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
		OpenGeoProver.settings = new OGPConfigurationSettings("JUTestCPSuiteLogFile", ".." + File.separator + GeoGebraLogger.DEFAULT_LOG_DIR);
		
		// Add single test cases here
		suite.addTestSuite(JUTestPoint.class);
		// TODO - other tests for geometry classes
		
		return suite;
	}
}

