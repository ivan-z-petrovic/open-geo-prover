/* 
 * DISCLAIMER PLACEHOLDER 
 */

/*
 * This file contains code from GeoGebra code from repository
 * location http://dev.geogebra.org/trac/browser/trunk/geogebra.
 * Following files from GeoGebra code have been used to incorporate
 * into single GeoGebra-like logger in OGP's branch for GeoGebra's
 * prover which implements OGP's logger facility:
 * 
 *  http://dev.geogebra.org/trac/browser/trunk/geogebra/common/src/geogebra/common/util/GeoGebraLogger.java
 *  http://dev.geogebra.org/trac/browser/trunk/geogebra/desktop/geogebra/util/GeoGebraLogger.java
 *  http://dev.geogebra.org/trac/browser/trunk/geogebra/common/src/geogebra/common/main/AbstractApplication.java
 *  http://dev.geogebra.org/trac/browser/trunk/geogebra//desktop/geogebra/main/Application.java
 *  
 *  Common logging class
 *  @author Zoltan Kovacs <zoltan@geogebra.org>
 *  
 *  Modified in OGP code by Ivan Petrovic.
 */

package com.ogprover.utilities.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.TreeSet;

import com.ogprover.utilities.io.CustomFile;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for implementation of GeoGebra logger</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */
public class GeoGebraLogger implements ILogger {
	/*
	 * Logging level
	 */
	static public class Level {
		/**
		 * Log level priority
		 */ 
		int priority;
		/**
		 * Category text
		 */
		String text;
		               
		/**
		 * Creates a logging level
		 * @param priority Log level priority
		 * @param text Category text
         */
		Level(int priority, String text) {
			this.priority = priority;
			this.text = text;
		}
	
        /**
         * Message priority, the lower the more problematic.
         * @return the priority
	     */
	     public int getPriority() {
	    	 return priority;
	     }
	}
		
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

	public static final String DEFAULT_LOG_DIR = "log";
	private static Set<String> reportedImplementationNeeded = new TreeSet<String>();
    
    public static Level EMERGENCY = new Level(0, "EMERGENCY");
    public static Level ALERT = new Level(1, "ALERT");
    public static Level CRITICAL = new Level(2, "CRITICAL");
    public static Level ERROR = new Level(3, "ERROR");
    public static Level WARN = new Level(4, "WARN");
    public static Level NOTICE = new Level(5, "NOTICE");
    public static Level INFO = new Level(7, "INFO");
    public static Level DEBUG = new Level(8, "DEBUG");
    public static Level TRACE = new Level(9, "TRACE"); 
    /**
     * Logging destinations
     */
    public enum LogDestination {/**
     * Send logging to file. A file name must also be set by using the setLogFile() method.
     */
    FILE, /**
     * Sends logging to console. Messages <= ERROR will be written to STDERR, others to STDOUT
     * in desktop mode; sends log messages via GWT.log to the Eclipse console in web
     * development mode.
     */
    CONSOLE, /**
     * Sends logging to the web console (available by pressing CTRL-SHIFT-J in Google Chrome,
     * or CTRL-SHIFT-K in Firefox) in web development or production mode. Not available
     * in desktop mode.
     */
    WEB_CONSOLE, /**
     * Sends logging to CONSOLE and WEB_CONSOLE as well (if available).
     */
    CONSOLES}
   
    private Level logLevel = DEBUG; // default
    private LogDestination logDestination = LogDestination.CONSOLES; // default;
    private boolean timeShown = true; // default
    private boolean callerShown = true; // default
    private File logFile = null; // default
    private FileWriter logFileWriter = null;
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/*
	 * Implementation of ILogger interface API.
	 */
	/**
	 * @see com.ogprover.utilities.logger.ILogger#fatal(java.lang.String)
	 */
	public synchronized void fatal(String msg) {
		log(EMERGENCY, msg);
	}

	/**
	 * @see com.ogprover.utilities.logger.ILogger#error(java.lang.String)
	 */
	public synchronized void error(String msg) {
		log(ERROR, msg);
	}

	/**
	 * @see com.ogprover.utilities.logger.ILogger#warn(java.lang.String)
	 */
	public synchronized void warn(String msg) {
		log(WARN, msg);
	}

	/**
	 * @see com.ogprover.utilities.logger.ILogger#info(java.lang.String)
	 */
	public synchronized void info(String msg) {
		log(INFO, msg);
	}

	/**
	 * @see com.ogprover.utilities.logger.ILogger#debug(java.lang.String)
	 */
	public synchronized void debug(String msg) {
		log(DEBUG, msg);
	}

	/**
	 * @see com.ogprover.utilities.logger.ILogger#trace(java.lang.String)
	 */
	public synchronized void trace(String msg) {
		log(TRACE, msg);
	}
	
	
    /**
     * Sets the current logging level
     * @param logLevel the logging level to set
     */
    public void setLogLevel(Level logLevel) {
            this.logLevel = logLevel;
    }
    /**
     * Sets the current logging level
     * @param logLevel the logging level to set
     */
    public void setLogLevel(String logLevel) {
            if (logLevel == null)
                    return;
            if ("ALERT".equals(logLevel))
                    this.logLevel = ALERT;
            if ("EMERGENCY".equals(logLevel))
                    this.logLevel = EMERGENCY;
            if ("CRITICAL".equals(logLevel))
                    this.logLevel = CRITICAL;
            if ("ERROR".equals(logLevel))
                    this.logLevel = ERROR;
            if ("WARN".equals(logLevel))
                    this.logLevel = WARN;
            if ("INFO".equals(logLevel))
                    this.logLevel = INFO;
            if ("NOTICE".equals(logLevel))
                    this.logLevel = NOTICE;
            if ("DEBUG".equals(logLevel))
                    this.logLevel = DEBUG;
            if ("TRACE".equals(logLevel))
                    this.logLevel = TRACE;
    }
   
    /**
     * Returns the current logging level
     * @return the current level
     */
    public Level getLogLevel() {
    	return logLevel;
    }
    /**
     * Sets the logger destination (FILE, CONSOLE, WEB_CONSOLE, CONSOLES)
     * @param logDestination the destination
     */
    public void setLogDestination(LogDestination logDestination) {
    	this.logDestination = logDestination;
    }
    /**
     * Returns the logger destination (FILE, CONSOLE, WEB_CONSOLE, CONSOLES)
     * @return the destination
     */
    public LogDestination getLogDestination() {
    	return logDestination;
    }
    /**
     * Reports if the timestamp is printed for logging
     * @return if the names are printed
     */
    public boolean isTimeShown() {
    	return timeShown;
    }
    /**
     * Sets if the report time of the log message should be printed for logging.
     * May not be available for all platforms (returns empty string when not).
     * @param timeShown if the timestamp should be printed
     */
    public void setTimeShown(boolean timeShown) {
    	this.timeShown = timeShown;
    }
    /**
     * Reports if the caller class and method names are printed for logging
     * @return if the names are printed
     */
    public boolean isCallerShown() {
    	return callerShown;
    }
    /**
     * Sets if the caller class and method names should be printed for logging
     * @param callerShown if the names should be printed
     */
    public void setCallerShown(boolean callerShown) {
    	this.callerShown = callerShown;
    }
    /**
     * Prints a log message if the logLevel is set to <= level
     * and stores those classes which have no implementation
     * (simply checks if the message starts with "implementation needed")
     * @param level logging level
     * @param message the log message
     */
    public void log(Level level, String message) {
    	if (message == null) {
    		message = "*null*";
    	}
           
    	if (logLevel.getPriority() >= level.getPriority()) {
    		String caller = "";
    		if (callerShown) {
    			caller = getCaller();
    			if (message.length() >= 21) {
    				if (message.toLowerCase().substring(0, 21).equals("implementation needed")) {
    					if (!reportedImplementationNeeded.contains(caller))
    						reportedImplementationNeeded.add(caller);
    				}
    			}
    			caller += ": ";
    		}
    		String timeInfo = "";
    		if (timeShown) {
    			timeInfo = getTimeInfo();
    			if (timeInfo != "") {
    				timeInfo += " ";
    			}
    		}
    		String logEntry = timeInfo + level.text + ": " + caller + message;
    		print(logEntry, level);
    	}
    }
   
    /**
     * Prints the log entry, which is usually the full message with timestamp,
     * the logging level and the caller class
     * @param logEntry the full log entry
     * @param level logging level
     */
    protected void print(String logEntry, Level level) {
    	if (getLogDestination() == LogDestination.WEB_CONSOLE || getLogDestination() == LogDestination.CONSOLES) {
    		// This is not supported in desktop.
    		// Falling back to use CONSOLE instead:
    		setLogDestination(LogDestination.CONSOLE);
    		log(WARN, "WEB_CONSOLE logging is not supported in desktop, falling back to use CONSOLE instead");
    	}
    	if (getLogDestination() == LogDestination.FILE) {
    		if (logFileWriter != null) {
    			try {
    				logFileWriter.append(logEntry + "\n");
    				logFileWriter.flush();
    				return;
    			} catch (IOException e) {
    				// Falling back to use CONSOLE instead:
    				setLogDestination(LogDestination.CONSOLE);
    				log(WARN, "Error writing log file");
    				// Trying again (recursive call):
    				print(logEntry, level);
    				return;
    			}
    		}
    		setLogDestination(LogDestination.CONSOLE);
    	}
        if (getLogDestination() == LogDestination.CONSOLE) {
        	if (level.getPriority() <= ERROR.getPriority()) {
        		System.err.println(logEntry);
        	}
        	else {
        		System.out.println(logEntry);
        	}
        	return;
        }
    }
   
    /**
     * Sets the log file name (if FILE logging is available)
     * @param logFileName the name of the log file
     */
    public void setLogFile(String logFileName) {
    	if (logFile != null && logFileWriter != null) {
    		try {
    			logFileWriter.close();
            } catch (IOException e) {
            	log(WARN, "Previous log file cannot be closed");
            }
    	}
    	logFile = new File(logFileName);
    	try {
    		logFileWriter = new FileWriter(logFile);
    	} catch (IOException e) {
    		log(WARN, "Log file " + logFileName + "cannot be opened");
    	}
    }
   
    /**
     * Returns the current time in human readable format (for debugging)
     * @return the timestamp
     */
    protected String getTimeInfo() {
    	Calendar calendar = new GregorianCalendar();
        int min = calendar.get(Calendar.MINUTE);
        String minS = (min < 10) ? "0" + min : "" + min;
        int sec = calendar.get(Calendar.SECOND);
        String secS = (sec < 10) ? "0" + sec : "" + sec;
        int msec = calendar.get(Calendar.MILLISECOND);
        String msecS = (msec < 100) ? "0" + msec : "" + msec;
        if (msec < 10) {
        	msecS = "0" + msecS;
        }
        return calendar.get(Calendar.HOUR_OF_DAY) + ":" + minS + ":" + secS + "." + msecS;
    }
   
    /**
     * Returns some memory related information (for debugging)
     * @return the memory info text
     */
    public String getMemoryInfo() {
    	// TODO: Not really sure why this is needed 4 times:
        System.gc();
        System.gc();
        System.gc();
        System.gc();
        long usedK = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024;
        return ("\n free memory: ") + Runtime.getRuntime().freeMemory()
        		+ " total memory: " + Runtime.getRuntime().totalMemory()
        		+ " max memory: " + Runtime.getRuntime().maxMemory()
        		+ "\n used memory (total-free): " + usedK + "K";
    }
   
    /**
     * Returns the caller class and method names
     * @return the full Java class and method name
     */
    public String getCaller() {
    	String callerMethodName = null;
    	String callerClassName = null;
    	int callerLineNumber;
    	try {
    		Throwable t = new Throwable();
    		StackTraceElement[] elements = t.getStackTrace();
    		// String calleeMethod = elements[0].getMethodName();
    		int ce = 3;
    		callerMethodName = elements[ce].getMethodName();
    		if ("debug".equals(callerMethodName)) {
    			// This means AbstractApplication.debug(Object) was called,
    			// so it is better to search for its caller instead
    			++ ce;
    			callerMethodName = elements[ce].getMethodName();
    		}
    		callerClassName = elements[ce].getClassName();
    		callerLineNumber = elements[ce].getLineNumber();
                   
    		if (callerClassName.equals("Unknown")) {
    			/* In web production mode the GWT compile rewrites the
    			 * code very thoroughly. We are doing some intuitive
    			 * hacking here to explode the method name; since
    			 * other information (class name, line number) is unavailable.
    			 */
                           
    			// PRETTY style
    			// safari:
    			if (callerMethodName.equals("$fillInStackTrace")) {
    				if (elements.length < 10) {
    					return "?"; 
    				}
    				return elements[9].getMethodName(); 
    			}
    			// gecko1_8
    			if (callerMethodName.equals("fillInStackTrace")) {
    				if (elements.length < 11) {
    					return "?"; 
    				}
    				return elements[10].getMethodName(); 
    			}
    			// TODO: Maybe other user agents could be supported.
                          
    			// OBFUSCATED style
    			return callerMethodName;
    		}
                   
    	} catch (Throwable t) {
    		// do nothing here; we are probably running Web in Opera
    		return "?";
    	}
    	return callerClassName + "." + callerMethodName + "[" + callerLineNumber + "]";
    }
    
    /**
     * Factory method for creation of new instances of file logger.
     * 
     * @param logFileName				Name of log file
     * @param logFileRootDirectory		Root directory of logger file
     * @return							New instance of file logger
     */
    public static GeoGebraLogger factory(String logFileName, String logFileRootDirectory) {
    	GeoGebraLogger logger = new GeoGebraLogger();
		logger.setLogLevel(GeoGebraLogger.INFO);
		logger.setLogDestination(GeoGebraLogger.LogDestination.FILE);
		
		String rootDirPath = (logFileRootDirectory == null || logFileRootDirectory.length() == 0) ? GeoGebraLogger.DEFAULT_LOG_DIR : logFileRootDirectory;
		String baseFileName;
		if (!logFileName.contains("."))
			baseFileName = CustomFile.buildBaseFileName(logFileName, "log");
		else
			baseFileName = logFileName;
		
		String logPath = CustomFile.buildAbsoluteFilePath(rootDirPath, baseFileName);
		logger.setLogFile(logPath);
    	return logger;
    }
}
