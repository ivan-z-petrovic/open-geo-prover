/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.utilities.logger;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.ogprover.utilities.io.CustomFile;



/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for log file</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class FileLogger extends Logger implements ILogger {
	/*
	 *  Hierarchy of log levels from lowest to highest:
	 *  ALL < TRACE < DEBUG < INFO < WARN < ERROR < FATAL < OFF
	 *  
	 *  If log level is set to some value L then all messages with levels 
	 *  X >= L will be enabled while others will be disabled.
	 */
	
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
	
	public static final String DEFAULT_LOG_DIR = "log"; // default name of destination directory for all log files (from current working directory, whatever it is)
	public static final String DEFAULT_LOG_FILE_EXTENSION = "log"; // default extension for log files
	public static final String DEFAULT_LOG_FILE_NAME = "tempOGPLogFile";
	
	/**
	 * <i>
	 * Creator of log file instances
	 * </i>
	 */
	private static FileLoggerFactory loggerFactory = new FileLoggerFactory();
	/**
	 * Flag for writing to standard output
	 */
	private boolean verbose;
	/**
	 * Full path of log directory
	 */
	private String logDirectoryPath;
	/**
	 * Base name of log file with extension (default extension is ".log")
	 */
	private String logBaseFileName;
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @return the loggerFactory
	 */
	public FileLoggerFactory getLoggerFactory() {
		return FileLogger.loggerFactory;
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
	 * @return the logDirectoryPath
	 */
	public String getLogDirectoryPath() {
		return this.logDirectoryPath;
	}

	/**
	 * @param logDirectoryPath the logDirectoryPath to set
	 */
	public void setLogDirectoryPath(String logDirectoryPath) {
		this.logDirectoryPath = logDirectoryPath;
	}
	
	/**
	 * @return the logBaseFileName
	 */
	public String getLogBaseFileName() {
		return this.logBaseFileName;
	}

	/**
	 * @param logBaseFileName the logBaseFileName to set
	 */
	public void setLogBaseFileName(String logBaseFileName) {
		this.logBaseFileName = logBaseFileName;
	}

	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method.
	 * 
	 * @param loggerDestDirPath		Absolute or relative (with respect to current working directory) path 
	 * 								of destination directory where log file is stored (optional)
	 * @param loggerBaseFileName	Base name of log file (with or without extension - default extension is ".log")
	 */
	public FileLogger(String loggerDestDirPath, String loggerBaseFileName) {
		super((loggerBaseFileName != null) ? loggerBaseFileName : FileLogger.DEFAULT_LOG_FILE_NAME);
		this.setLevel(Level.INFO); // this will include info, warn, error and fatal messages
		
		this.verbose = false;
		
		if (loggerDestDirPath == null)
			this.logDirectoryPath = CustomFile.buildAbsolutePath(FileLogger.DEFAULT_LOG_DIR);
		else
			this.logDirectoryPath = CustomFile.buildAbsolutePath(loggerDestDirPath);
		
		String initialBaseFileName = (loggerBaseFileName != null) ? loggerBaseFileName : FileLogger.DEFAULT_LOG_FILE_NAME;
		String baseFileName = (initialBaseFileName.indexOf(File.separatorChar) == -1) ? initialBaseFileName : FileLogger.DEFAULT_LOG_FILE_NAME; // disregard file names that are not base file names i.e. contain file separator
		int lastDotIdx = baseFileName.lastIndexOf('.');
		if (lastDotIdx == -1) // extension is not set
			this.logBaseFileName = CustomFile.buildBaseFileName(baseFileName, FileLogger.DEFAULT_LOG_FILE_EXTENSION);
		else
			this.logBaseFileName = baseFileName;
	}
	
	/**
	 * Constructor method.
	 * 
	 * @param loggerBaseFileName	Base name of log file (with or without extension - default extension is ".log")
	 */
	public FileLogger(String loggerBaseFileName) {
		this(null, loggerBaseFileName); // logger file will be created in current working directory
	}

	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
     * This method is new method in FileLogger that uses custom destination directory
     * for log file and custom logger factory object.
     * 
     * @param loggerDestDirPath		Absolute or relative path of destination directory where log file is stored (optional)
     * @param loggerBaseFileName	Base name of log file (with or without extension - default extension is ".log")
     */
    public static FileLogger getLogger(String loggerDestDirPath, String loggerBaseFileName) {
    	if (loggerBaseFileName == null || loggerBaseFileName.indexOf(File.separatorChar) != -1)
    		return null; // incorrect base file name
    	
    	String absoluteFileLoggerPath = CustomFile.buildAbsoluteFilePath((loggerDestDirPath != null) ? loggerDestDirPath : FileLogger.DEFAULT_LOG_DIR, loggerBaseFileName);
    	FileLogger fl = (FileLogger)Logger.getLogger(absoluteFileLoggerPath, FileLogger.loggerFactory); // safe cast since FileLoggerFactory retrieves FileLogger object
    	
    	// Setting layout for log file
    	
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
			fl.addAppender(new FileAppender(layout, absoluteFileLoggerPath, false)); // This will open a file with specified name and truncate the previous contents
		} catch (IOException e) {
			e.printStackTrace();
		}
		
    	return fl;
    }
    
    /**
     * This method overrides Logger.getLogger by supplying its own factory type as a parameter.
     * 
     * @param loggerBaseFileName	Base name of log file (with or without extension - default extension is ".log")
     * 
     * @see org.apache.log4j.Logger#getLogger(java.lang.String)
     */
    public static FileLogger getLogger(String loggerBaseFileName) {
    	return FileLogger.getLogger(FileLogger.DEFAULT_LOG_DIR, loggerBaseFileName);
    }
    
    
    /*
     * Implementation methods from ILogger interface
     */
    
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
    
    public synchronized void fatal(String msg) {
    	if (this.verbose)
    		System.out.println(msg);
    	this.log(FileLogger.class.getName(), Level.FATAL, msg, null);
	}
    
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
    
	public synchronized void trace(String msg) {
		if (this.verbose)
    		System.out.println(msg);
    	this.log(FileLogger.class.getName(), Level.TRACE, msg, null);
	}
}