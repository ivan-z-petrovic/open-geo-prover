/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.utilities.logger;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;

import com.ogprover.utilities.io.CustomFile;

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
	/*
	 * ======================================================================
	 * ========================== VARIABLES =================================
	 * ======================================================================
	 */
	/**
	 * <i><b>
	 * Version number of class in form xx.yy where
	 * xx is major version/release number and yy is minor
	 * release number.
	 * </b></i>
	 */
	public static final String VERSION_NUM = "1.00"; // this should match the version number from class comment
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * @see org.apache.log4j.spi.LoggerFactory#makeNewLoggerInstance(java.lang.String)
	 */
	public Logger makeNewLoggerInstance(String logFilePath) {
		// Assumption: logFilePath is not null
		
		int separatorIdx = logFilePath.lastIndexOf(File.separatorChar);
		
		if (separatorIdx == -1) // the passed in string is not full path but only a base file name
			return new FileLogger(null, logFilePath);
		if (separatorIdx == 0) // file is in root directory
			return new FileLogger(CustomFile.getRootDirPath(), logFilePath);
		return new FileLogger(logFilePath.substring(0, separatorIdx), (separatorIdx == logFilePath.length() - 1) ? null : logFilePath.substring(separatorIdx + 1));
	}
	
}