/* 
 * This file has been taken from GeoGebra code:
 * http://dev.geogebra.org/svn/trunk/geogebra/common/src/geogebra/common/io/DocHandler.java
 * 
 */

/*
 * Quick and dirty XML parser. Java Tip 128
 * http://www.javaworld.com/javaworld/javatips/jw-javatip128.html
 */

package com.ogprover.utilities.io;

import java.util.LinkedHashMap;

/**
 * <dl>
 * <dt><b>Interface description:</b></dt>
 * <dd>Event listener for XML I/O files</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */
public interface DocHandler {
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
	 * Method called when start of XML element is encountered.
	 * 
	 * @param tag			element name
	 * @param h 			attributes
	 * @throws Exception 	if invalid
	 */
	public void startElement(String tag,LinkedHashMap<String, String> h) throws Exception;
	
	/**
	 * Method called when end of XML element is encountered.
	 * 
	 * @param tag 			element name
	 * @throws Exception 	if invalid
	 */
	public void endElement(String tag) throws Exception;
	
	/**
	 * Method called when starting processing of XML document.
	 * 
	 * @throws Exception if invalid
	 */
	public void startDocument() throws Exception;
	
	/**
	 * Method called when finishing processing of XML document.
	 * 
	 * @throws Exception if invalid
	 */
	public void endDocument() throws Exception;
	
	/**
	 * Method called when untagged text is encountered between two subsequent 
	 * start and end tags of same name.
	 * 
	 * @param str 			string between start and end tags
	 * @throws Exception 	if invalid
	 */
	public void text(String str) throws Exception;
	
	/**
	 * Method that retrieves the index of current construction step
	 * (For navigation bar)
	 * 
	 * @return current construction step
	 */
	public int getConsStep();
	
}
