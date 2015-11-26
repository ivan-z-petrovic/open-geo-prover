/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.manual;

import java.io.IOException;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.utilities.io.CustomFile;
import com.ogprover.utilities.io.CustomFileWriter;
import com.ogprover.utilities.io.LaTeXFileWriter;
import com.ogprover.utilities.io.SpecialFileFormatting;
import com.ogprover.utilities.io.XMLFileWriter;
import com.ogprover.utilities.logger.ILogger;

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
		ILogger logger = OpenGeoProver.settings.getLogger();
		
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
		
		System.out.println(CustomFile.getFileAbsolutePath(cfw.getOutputFile()));
		
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
	
	public static void main (String args[]) {
		// MTestUtilities.testCustomFileWriter();
		// MTestUtilities.testFormattedWriters();
	}
}