/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.utilities.io.CustomFileReader;
import com.ogprover.utilities.io.CustomFileWriter;
import com.ogprover.utilities.logger.ILogger;
import com.thoughtworks.xstream.XStream;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML parser for geometric theorems in OGP XML format</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class OGPCPXMLParser {
	public static final String XML_DIR_NAME = "xml_files"; // name of directory for xml files storage
	XStream xstream = null; // XML parser
	
	/**
	 * Constructor method.
	 */
	public OGPCPXMLParser() {
		xstream = new XStream();
		// configuring xstream
		xstream.alias("geothm", OGPTP.class); // main tag is associated with GeoTheorem class
		/*
		 *  registering custom converters
		 */
		xstream.registerConverter(new OGPCPXMLConverter());
		// constructions' converters - BEGIN
		xstream.registerConverter(new AngleBisectorConverter());
		xstream.registerConverter(new AngleOf60DegConverter());
		xstream.registerConverter(new AngleRayConverter());
		xstream.registerConverter(new AngleRayOfThirdAngleTo60DegConverter());
		xstream.registerConverter(new AngleTrisectorConverter());
		xstream.registerConverter(new CenterOfCircleConverter());
		xstream.registerConverter(new CentralSymmetricPointConverter());
		xstream.registerConverter(new CircleWithCenterAndPointConverter());
		xstream.registerConverter(new CircleWithCenterAndRadiusConverter());
		xstream.registerConverter(new CircleWithDiameterConverter());
		xstream.registerConverter(new CircumscribedCircleConverter());
		xstream.registerConverter(new ConicSectionWithFivePointsConverter());
		xstream.registerConverter(new FootPointConverter());
		xstream.registerConverter(new FreePointConverter());
		xstream.registerConverter(new GeneralConicSectionConverter());
		xstream.registerConverter(new GeneralizedSegmentDivisionPointConverter());
		xstream.registerConverter(new HarmonicConjugatePointConverter());
		xstream.registerConverter(new IntersectionPointConverter());
		xstream.registerConverter(new InversePointConverter());
		xstream.registerConverter(new LineThroughTwoPointsConverter());
		xstream.registerConverter(new MidPointConverter());
		xstream.registerConverter(new ParallelLineConverter());
		xstream.registerConverter(new PerpendicularBisectorConverter());
		xstream.registerConverter(new PerpendicularLineConverter());
		xstream.registerConverter(new PolarConverter());
		xstream.registerConverter(new PoleConverter());
		xstream.registerConverter(new RadicalAxisConverter());
		xstream.registerConverter(new RandomPointFromCircleConverter());
		xstream.registerConverter(new RandomPointFromGeneralConicConverter());
		xstream.registerConverter(new RandomPointFromLineConverter());
		xstream.registerConverter(new ReflectedPointConverter());
		xstream.registerConverter(new RotatedPointConverter());
		xstream.registerConverter(new SegmentDivisionPointConverter());
		xstream.registerConverter(new TangentLineConverter());
		xstream.registerConverter(new TranslatedPointConverter());
		xstream.registerConverter(new TripleAngleRayConverter());
		// constructions' converters - END
		// statements' converters - BEGIN
		xstream.registerConverter(new AlgSumOfAnglesConverter());
		xstream.registerConverter(new AlgSumOfSegmentsConverter());
		xstream.registerConverter(new AngleEqualToSpecialConstantAngleConverter());
		xstream.registerConverter(new CollinearPointsConverter());
		xstream.registerConverter(new ConcurrentCirclesConverter());
		xstream.registerConverter(new ConcurrentLinesConverter());
		xstream.registerConverter(new ConcyclicPointsConverter());
		xstream.registerConverter(new CongruentTrianglesConverter());
		xstream.registerConverter(new EqualAnglesConverter());
		xstream.registerConverter(new EqualityOfRatioProductsConverter());
		xstream.registerConverter(new EqualityOfTwoRatiosConverter());
		xstream.registerConverter(new EquilateralTriangleConverter());
		xstream.registerConverter(new HarmonicConjugatesConverter());
		xstream.registerConverter(new IdenticalPointsConverter());
		xstream.registerConverter(new LinearCombinationOfDoubleSignedPolygonAreasConverter());
		xstream.registerConverter(new LinearCombinationOfSquaresOfSegmentsConverter());
		xstream.registerConverter(new PolygonAreasCombinationConverter());
		xstream.registerConverter(new OrientedSegmentsCombinationConverter());
		xstream.registerConverter(new PointOnSetOfPointsConverter());
		xstream.registerConverter(new RatioOfOrientedSegmentsConverter());
		xstream.registerConverter(new RatioOfTwoSegmentsConverter());
		xstream.registerConverter(new SegmentsOfEqualLengthsConverter());
		xstream.registerConverter(new SimilarTrianglesConverter());
		xstream.registerConverter(new TouchingCirclesConverter());
		xstream.registerConverter(new TwoInversePointsConverter());
		xstream.registerConverter(new TwoParallelLinesConverter());
		xstream.registerConverter(new TwoPerpendicularLinesConverter());
		// statements' converters - END
		// other classes' converters - BEGIN
		xstream.registerConverter(new AngleConverter());
		xstream.registerConverter(new PointListConverter());
		xstream.registerConverter(new ProductOfTwoSegmentsConverter());
		xstream.registerConverter(new RatioOfTwoCollinearSegmentsConverter());
		xstream.registerConverter(new RatioProductConverter());
		xstream.registerConverter(new SegmentConverter());
		// other classes' converters - END
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
	public OGPTP readGeoTheoremFromXML(String fileName) {
		// TODO - Re-factor this code to use CustomFileReader instead of making File object.
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (fileName == null) {
			logger.error("Null file name passed to readGeoTheoremFromXML() method.");
			return null;
		}
		
		OGPTP consProtocol = null;
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
				consProtocol = (OGPTP)obj;
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
		
		return consProtocol;
	}
	
	/**
	 * Method for writing passed in GeoTheorem object to XML file.
	 * 
	 * @param consProtocol		Theorem protocol to be converted to XML format
	 * @param fileName			Name of output xml file
	 */
	public void writeGeoTheoremToXML(OGPTP consProtocol, String fileName) {
		CustomFileWriter writer = null;
		try {
			writer = new CustomFileWriter(OGPCPXMLParser.XML_DIR_NAME, fileName, "xml");
			writer.write(this.xstream.toXML(consProtocol));
		} catch (IOException e) {
			if (writer != null)
				writer.close();
		}
	}
}