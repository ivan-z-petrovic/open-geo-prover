/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.utilities.logger;


/**
 * <dl>
 * <dt><b>Interface description:</b></dt>
 * <dd>
 * 	Basic interface for OGP logger facility. If additional specific
 *  functionality is required, the best would be to derive a new
 *  logger interface from this basic interface.
 * </dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */

public interface ILogger {
	/*
	 * ======================================================================
	 * ========================== VARIABLES =================================
	 * ======================================================================
	 */
	/**
	 * <i><b>
	 * Version number of interface in form xx.yy where
	 * xx is major version/release number and yy is minor
	 * release number.
	 * </b></i>
	 */
	public static final String VERSION_NUM = "1.00"; // this should match the version number from interface comment
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method for logging fatal error.
	 * 
	 * @param msg	Message to log.
	 */
	public void fatal(String msg);
	/**
	 * Method for logging error.
	 * 
	 * @param msg	Message to log.
	 */
	public void error(String msg);
	/**
	 * Method for logging warning.
	 * 
	 * @param msg	Message to log.
	 */
    public void warn(String msg);
    /**
	 * Method for logging information.
	 * 
	 * @param msg	Message to log.
	 */
    public void info(String msg);
    /**
	 * Method for logging debugger message.
	 * 
	 * @param msg	Message to log.
	 */
    public void debug(String msg);
    /**
	 * Method for logging trace message.
	 * 
	 * @param msg	Message to log.
	 */
    public void trace(String msg);
}