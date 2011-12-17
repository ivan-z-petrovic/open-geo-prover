/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.manual;

import com.ogprover.main.OGPConstants;
import com.ogprover.polynomials.GeoTheorem;
import com.ogprover.test.formats.geothm_xml.GeoTheoremXMLParser;
import com.ogprover.thmprover.TheoremProver;
import com.ogprover.thmprover.WuMethodProver;
import com.ogprover.utilities.Stopwatch;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for manual testing of Theorem Prover</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class MTestTheoremProver {
	public static void main (String[] args) {
		Stopwatch stopwatch = new Stopwatch();
		GeoTheoremXMLParser parser = new GeoTheoremXMLParser();
		GeoTheorem theorem = parser.readGeoTheoremFromXML("geothm02_test");
		
		WuMethodProver prover = new WuMethodProver(theorem);
		stopwatch.startMeasureTime();
		int retCode = prover.prove();
		stopwatch.endMeasureTime();
		
		switch (retCode) {
		case TheoremProver.THEO_PROVE_RET_CODE_FALSE:
			System.out.println("Theorem has been disproved.");
			break;
		case TheoremProver.THEO_PROVE_RET_CODE_TRUE:
			System.out.println("Theorem has been proved.");
			break;
		case TheoremProver.THEO_PROVE_RET_CODE_UNKNOWN:
			System.out.println("Theorem can't be neither proved nor disproved.");
			break;
		case OGPConstants.ERR_CODE_GENERAL:
		case OGPConstants.ERR_CODE_NULL:
		case OGPConstants.ERR_CODE_SPACE:
		case OGPConstants.ERR_CODE_TIME:
			System.out.println("Error happened.");
			break;
		}
		
		System.out.println("Prover took " + stopwatch.getTimeIntMillisec() + " milliSec for proving.");
	}
}
