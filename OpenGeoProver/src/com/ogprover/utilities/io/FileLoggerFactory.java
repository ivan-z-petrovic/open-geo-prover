/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.utilities.io;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for log file factory</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class FileLoggerFactory implements LoggerFactory {

	@Override
	public Logger makeNewLoggerInstance(String name) {
		return new FileLogger(name);
	}
	
}