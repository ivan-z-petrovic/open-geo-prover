/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.utilities.io;

import java.io.IOException;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for log file</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class FileLogger extends Logger{
	// Hierarchy of log levels from lowest to highest:
	// ALL < TRACE < DEBUG < INFO < WARN < ERROR < FATAL < OFF
	// If log level is set to L then all messages with levels X >= L will be enabled
	// while others will be disabled.
	private static FileLoggerFactory loggerFactory = new FileLoggerFactory();
	private static final String DEFAULT_ROOT_DIR = "log/";
	private boolean verbose;
	private String rootDirectory;
	
	public FileLogger (String name) {
		super(name);
		this.setLevel(Level.INFO); // this will include info, warn, error and fatal messages 
		this.setVerbose(false);
		this.setRootDirectory(FileLogger.DEFAULT_ROOT_DIR);
	}

	/**
	 * @return the verbose
	 */
	public boolean isVerbose() {
		return this.verbose;
	}
	
	/**
	 * @param verbose the verbose to set
	 */
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	/**
	 * @param rootDirectory the rootDirectory to set
	 */
	public void setRootDirectory(String rootDirectory) {
		this.rootDirectory = rootDirectory;
	}

	/**
	 * @return the rootDirectory
	 */
	public String getRootDirectory() {
		return rootDirectory;
	}

	/**
     * This method overrides getLogger by supplying
     * its own factory type as a parameter.
     */
    public static FileLogger getLogger(String name, String rootDirectory) {
    	FileLogger fl = (FileLogger)Logger.getLogger(name, loggerFactory);
    	
    	if (rootDirectory != null)
    		fl.setRootDirectory(rootDirectory);
    	
    	/*
		 * The conversion pattern consists of date in ISO8601 format, level,
		 * thread name, logger name truncated to its rightmost two components
		 * and left justified to 17 characters, location information consisting
		 * of file name (padded to 20 characters) and line number, nested
		 * diagnostic context, the and the application supplied message.
		 * 
		 * WARNING: Location information is time consuming, but very useful.
		 */
		Layout layout = new PatternLayout("%d %-5p [%t] %-17c{2} (%20F:%L) %3x - %m%n");
		// Logger is associated with file
		try {
			String truncatedName = fl.getRootDirectory() + name.substring(name.lastIndexOf(".")+1, name.length()) + ".log";

			// this will open a file with specified name and truncate the previous contents
			fl.addAppender(new FileAppender(layout, truncatedName, false));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
    	return fl;
    }
    
    /**
     * This method overrides getLogger by supplying
     * its own factory type as a parameter.
     */
    public static FileLogger getLogger(String name) {
    	return FileLogger.getLogger(name, (String)null);
    }
    
    /* 
     * Base log functions - they are all made to be thread safe
     * since logger could be used in multi-threading environment
     * when multiplying two polynomials. 
     * 
     * Following comment is from book "Thinking in Java" by Bruce Eckel
     * (section with title "Resolving shared resource contention"):
     * "When you call any synchronized method, that object is locked 
     * and no other synchronized method of that object can be called 
     * until the first one finishes and releases the lock."
     */
    
    public synchronized void error(String msg) {
    	if (this.verbose)
    		System.out.println(msg);
    	this.log(FileLogger.class.getName(), Level.ERROR, msg, null);	
    }
    
    public synchronized void warn(String msg) {
    	if (this.verbose)
    		System.out.println(msg);
    	this.log(FileLogger.class.getName(), Level.WARN, msg, null);	
    }
    
    public synchronized void info(String msg) {
    	if (this.verbose)
    		System.out.println(msg);
    	this.log(FileLogger.class.getName(), Level.INFO, msg, null);	
    }
    
    public synchronized void debug(String msg) {
    	if (this.verbose)
    		System.out.println(msg);
    	this.log(FileLogger.class.getName(), Level.DEBUG, msg, null);	
    }
}