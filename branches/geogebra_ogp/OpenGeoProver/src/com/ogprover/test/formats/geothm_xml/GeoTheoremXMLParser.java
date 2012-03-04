/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.geothm_xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.GeoTheorem;
import com.ogprover.utilities.io.CustomFileReader;
import com.ogprover.utilities.io.CustomFileWriter;
import com.ogprover.utilities.io.FileLogger;

import com.thoughtworks.xstream.XStream;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML parser for geometric theorems in GeoTheorem XML format</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class GeoTheoremXMLParser {
	public static final String XML_DIR_NAME = "xml_files"; // name of directory for xml files storage
	XStream xstream = null; // XML parser
	
	/**
	 * Constructor method.
	 */
	public GeoTheoremXMLParser() {
		xstream = new XStream();
		// configuring xstream
		xstream.alias("geothm", GeoTheorem.class); // main tag is associated with GeoTheorem class
		// registering custom converters
		xstream.registerConverter(new PowerConverter());
		xstream.registerConverter(new UTermConverter());
		xstream.registerConverter(new UPolynomialConverter());
		xstream.registerConverter(new UFractionConverter());
		xstream.registerConverter(new XTermConverter());
		xstream.registerConverter(new XPolynomialConverter());
		xstream.registerConverter(new XPolySystemConverter());
		xstream.registerConverter(new GeoTheoremConverter());
	}
	
	private String getAbsoluteFileName(String fileName) {
		String xstreamFileName = null;
		
		// extract only base file name
		int index = fileName.lastIndexOf('/');
		String basenameWithExt = null;
		String basename = null;
		
		if (index > -1)
			basenameWithExt = fileName.substring(index+1);
		else
			basenameWithExt = fileName;
		
		if (basenameWithExt.length() > 0) {
			index = basenameWithExt.lastIndexOf(".");
			
			if (index > -1)
				basename = basenameWithExt.substring(0, index);
			else
				basename = basenameWithExt;
		}
		
		xstreamFileName = CustomFileReader.INPUT_DIR_NAME + "/" + basename + ".xml";
		
		return xstreamFileName;
	}
	
	/**
	 * Method for reading GeoTheorem object from XML file.
	 * 
	 * @param fileName		Name of input xml file
	 * @return				GeoTheorem object if successfully read from xml file, 
	 * 						null otherwise
	 */
	public GeoTheorem readGeoTheoremFromXML(String fileName) {
		FileLogger logger = OpenGeoProver.settings.getLogger();
		
		if (fileName == null) {
			logger.error("Null file name passed to readGeoTheoremFromXML() method.");
			return null;
		}
		
		GeoTheorem theorem = null;
		InputStream input = null;
		String xstreamFileName = this.getAbsoluteFileName(fileName);
		
		// Check whether input directory with XML files exists
		File inDir = new File(CustomFileReader.INPUT_DIR_NAME);
		if (inDir.exists() == false || inDir.isDirectory() == false) {
			logger.error("Input directory for xml files with name " + CustomFileReader.INPUT_DIR_NAME + " doesn't exist.");
			return null;
		}
		
		try {
			input = new FileInputStream(xstreamFileName);
			
			// reading and parsing xml file
			Object obj = this.xstream.fromXML(input);
			
			if (obj != null)
				theorem = (GeoTheorem)obj;
		} catch (FileNotFoundException e) {
			logger.error("Error happend when reading file " + xstreamFileName);			
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					// disregard this exception
				}
			}
		}
		
		return theorem;
	}
	
	/**
	 * Method for writing passed in GeoTheorem object to XML file.
	 * 
	 * @param theorem		GeoTheorem object
	 * @param fileName		name of output xml file
	 */
	public void writeGeoTheoremToXML(GeoTheorem theorem, String fileName) {
		CustomFileWriter writer = null;
		try {
			writer = new CustomFileWriter(GeoTheoremXMLParser.XML_DIR_NAME, fileName, "xml");
			writer.write(this.xstream.toXML(theorem));
		} catch (IOException e) {
			if (writer != null)
				writer.close();
		}
	}
}