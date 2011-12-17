/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.manual;

import java.io.File;
import java.io.IOException;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.GeoTheorem;
import com.ogprover.polynomials.XPolySystem;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.test.formats.geothm_xml.GeoTheoremXMLParser;
import com.ogprover.utilities.io.CustomFileReader;
import com.ogprover.utilities.io.CustomFileWriter;
import com.ogprover.utilities.io.FileLogger;
import com.ogprover.utilities.io.LaTeXFileWriter;
import com.ogprover.utilities.io.SpecialFileFormatting;
import com.ogprover.utilities.io.XMLFileWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for manual testing of utility classes</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class MTestUtilities {
	
	/**
	 * Testing manipulation with file for writing.
	 */
	public static void testCustomFileWriter() {
		CustomFileWriter cfw = null;
		boolean error = false;
		FileLogger logger = OpenGeoProver.settings.getLogger();
		
		try {
			cfw = new CustomFileWriter("firstExample", "txt");
		} catch (IOException e) {
			logger.error("Input/Output error happened when creating/openning file.");
			error = true;
		} finally {
			if (error && cfw != null) {
				cfw.close();
				return;
			}
		}
		
		System.out.println(cfw.getPath());
		
		String oneLineOfText = "This is one line of text inserted in file.";
		
		try {
			cfw.write(oneLineOfText);
		} catch (IOException e) {
			logger.error("Input/Output error happened when writing to file.");
		} finally {
			if (cfw != null)
				cfw.close();
		}
	}
	
	// auxiliary method to write the body of document
	public static void writeBody(SpecialFileFormatting sff) throws IOException {
		sff.openSection("Geometry figures");
			sff.openSubSection("Plane geometry figures", false);
				sff.openEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
					sff.writeEnumItem("triangle");
						sff.openEnum(SpecialFileFormatting.ENUM_COMMAND_ENUMERATE);
							sff.writeEnumItem("isoscales triangle");
							sff.writeEnumItem("equilateral triangle");
							sff.writeEnumItem("right triangle");
						sff.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ENUMERATE);
					sff.writeEnumItem("tetragon");
						sff.openEnum(SpecialFileFormatting.ENUM_COMMAND_ENUMERATE);
							sff.writeEnumItem("square");
							sff.writeEnumItem("rectangle");
							sff.writeEnumItem("parallelogram");
						sff.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ENUMERATE);
				sff.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
			sff.closeSubSection();
			sff.openSubSection("3D Space geometry figures", false);
				sff.openEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
					sff.writeEnumItem("ball");
					sff.writeEnumItem("cone");
					sff.writeEnumItem("cylinder");
				sff.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
			sff.closeSubSection();
		sff.closeSection();
		
		sff.openSection("Algebraic expressions");
			sff.openSubSection("Polynomials", true);
				sff.writeFormattedText("Here is one polynomial", SpecialFileFormatting.FMT_TYPE_PROOF_LINE);
				sff.writePlainText("$$ 0.5x^3y^3 - 7xy^2 + \\frac{1}{3}xy + 5x - 3y + 2 $$");
			sff.closeSubSection();
			sff.openSubSection("Equations", true);
				sff.writeFormattedText("Here is one equation", SpecialFileFormatting.FMT_TYPE_PROOF_LINE);
				sff.writePlainText("$$ (x - 1)^2 + (y - 2)^2 = 0 $$");
			sff.closeSubSection();
		sff.closeSection();
	}
	
	/**
	 * Testing manipulation with file for formatted writing.
	 */
	public static void testFormattedWriters() {
		LaTeXFileWriter latexWriter = null;
		XMLFileWriter xmlWriter = null;
		
		try {
			latexWriter = new LaTeXFileWriter("testLaTeX");
			latexWriter.openDocument("article", "Testing of LaTeX output", "Ivan Petrovic");
			writeBody(latexWriter);
			latexWriter.closeDocument();
		} catch (Exception e) {
			
		} finally {
			if (latexWriter != null)
				latexWriter.close();
		}
		
		try {
			xmlWriter = new XMLFileWriter("testXML");
			writeBody(xmlWriter);
			xmlWriter.writeFormattedText("\\{something inside brackets\\} \\emptyset \\ni", SpecialFileFormatting.FMT_TYPE_PROOF_LINE);
		} catch (Exception e) {
			
		} finally {
			if (xmlWriter != null)
				xmlWriter.close();
		}
	}
	
	/**
	 * Testing XML parser for theorems in algebraic form.
	 */
	public static void testGeoTheoremXMLParser() {
		System.out.println();
		System.out.println("========= TEST OF READING X-POLYNOMIALS FROM XML FILE =========");
		System.out.println();
		
		GeoTheoremXMLParser parser = new GeoTheoremXMLParser();
		GeoTheorem theorem = parser.readGeoTheoremFromXML("geothm01_src");
		XPolySystem system = null;
		XPolynomial statement = null;
		
		if (theorem != null) {
			system = theorem.getHypotheses();
			statement = theorem.getStatement();
		}
		
		if (system != null) {
			System.out.println();
			System.out.println("System of XPolynomials read from xml file \"geothm01_src.xml\" is:");
			System.out.println();
			for(int ii = 0, jj = system.getPolynomials().size(); ii < jj; ii++)
				System.out.println(system.getXPoly(ii).printToLaTeX());
		}
		else
			System.out.println("System is empty.");
		
		if (statement != null) {
			System.out.println();
			System.out.println("Single XPolynomial read from xml file \"geothm01_src.xml\" is:");
			System.out.println();
			System.out.println(statement.printToLaTeX());
		}
		else
			System.out.println("Single polynomial is empty.");
		
		if (theorem != null) {
			GeoTheorem newtheorem = null;
			XPolySystem newsystem = null;
			XPolynomial newstatement = null;
			CustomFileWriter destFile = null;
			CustomFileReader srcFile = null;
			
			parser.writeGeoTheoremToXML(theorem, "geothm01_dest");
			System.out.println();
			System.out.println("Geometry theorem is written to \"geothm01_dest.xml\" file.");
			System.out.println();
			
			// Copy "geothm01_dest.xml" to input directory
			try {
				destFile = new CustomFileWriter(CustomFileReader.INPUT_DIR_NAME, "geothm01_dest", "xml");
				srcFile = new CustomFileReader(GeoTheoremXMLParser.XML_DIR_NAME, "geothm01_dest", "xml");
				String line = null;
				
				while ((line = srcFile.readLine()) != null) {
					destFile.write(line);
					destFile.write("\n");
				}
			}
			catch (Exception e) {
				// do nothing
			}
			finally {
				if (destFile != null)
					destFile.close();
				if (srcFile != null)
					srcFile.close();
			}
			System.out.println();
			System.out.println("File \"geothm01_dest.xml\" has been copied to input directory.");
			System.out.println();
			
			newtheorem = parser.readGeoTheoremFromXML("geothm01_dest");
			
			if (newtheorem != null) {
				newsystem = newtheorem.getHypotheses();
				newstatement = newtheorem.getStatement();
			}
			
			if (newsystem != null) {
				System.out.println();
				System.out.println("System of XPolynomials read from xml file \"geothm01_dest.xml\" is:");
				System.out.println();
			
				for(int ii = 0, jj = newsystem.getPolynomials().size(); ii < jj; ii++)
					System.out.println(newsystem.getXPoly(ii).printToLaTeX());
			}
			else
				System.out.println("New system is empty");
			
			if (newstatement != null) {
				System.out.println();
				System.out.println("Single XPolynomial read from xml file \"geothm01_dest.xml\" is:");
				System.out.println();
				System.out.println(newstatement.printToLaTeX());
			}
			else
				System.out.println("New single polynomial is empty");
			
			// Delete "geothm01_dest.xml" from input directory
			try {
				srcFile = new CustomFileReader(CustomFileReader.INPUT_DIR_NAME, "geothm01_dest", "xml");
				File inputFile = (srcFile != null) ? srcFile.getInputFile() : null;
				boolean result = true;

				System.out.println();
				if (inputFile != null && inputFile.exists()) {
					if (srcFile != null) { // must close this first in order to successfully delete the file
						srcFile.close();
						srcFile = null;
					}
					result = inputFile.delete();
					if (!result)
						System.out.println("Failed to delete \"geothm01_dest.xml\" file from input directory.");
					else
						System.out.println("File \"geothm01_dest.xml\" has been deleted from input directory.");
				}
				else
					System.out.println("File \"geothm01_dest.xml\" doesn't exist.");
				System.out.println();
			}
			catch (Exception e) {
				// do nothing
			}
			finally {
				if (srcFile != null)
					srcFile.close();
			}
		}
	}
	
	public static void main (String args[]) {
		// MTestUtilities.testCustomFileWriter();
		// MTestUtilities.testFormattedWriters();
		MTestUtilities.testGeoTheoremXMLParser();
	}
}