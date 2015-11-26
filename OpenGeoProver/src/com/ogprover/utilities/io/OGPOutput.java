/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.utilities.io;

import java.io.IOException;

import com.ogprover.polynomials.XPolySystem;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.pp.tp.geoconstruction.Point;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for output report files of the OGP prover</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class OGPOutput implements SpecialFileFormatting {
	private LaTeXFileWriter latexFile;
	private XMLFileWriter xmlFile;
	private boolean closed; // it is true if output is closed for writing 
	
	public OGPOutput() {
		this.setLatexFile(null);
		this.setXmlFile(null);
		this.setClosed(false);
	}
	
	public OGPOutput(LaTeXFileWriter latexFile, XMLFileWriter xmlFile) {
		this.setLatexFile(latexFile);
		this.setXmlFile(xmlFile);
	}

	public void setLatexFile(LaTeXFileWriter latexFile) {
		this.latexFile = latexFile;
	}

	public LaTeXFileWriter getLatexFile() {
		return latexFile;
	}

	public void setXmlFile(XMLFileWriter xmlFile) {
		this.xmlFile = xmlFile;
	}

	public XMLFileWriter getXmlFile() {
		return xmlFile;
	}
	
	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	public boolean isClosed() {
		return closed;
	}

	public void openDocument(String documentType, String title, String author)
			throws IOException {
		if (this.closed)
			return;
		
		if (this.latexFile != null)
			this.latexFile.openDocument(documentType, title, author);
		if (this.xmlFile != null)
			this.xmlFile.openDocument(documentType, title, author);
	}

	public void closeDocument() throws IOException {
		if (this.closed)
			return;
		
		if (this.latexFile != null)
			this.latexFile.closeDocument();
		if (this.xmlFile != null)
			this.xmlFile.closeDocument();
	}

	public void openSection(String title) throws IOException {
		if (this.closed)
			return;
		
		if (this.latexFile != null)
			this.latexFile.openSection(title);
		if (this.xmlFile != null)
			this.xmlFile.openSection(title);
	}

	public void closeSection() throws IOException {
		if (this.closed)
			return;
		
		if (this.latexFile != null)
			this.latexFile.closeSection();
		if (this.xmlFile != null)
			this.xmlFile.closeSection();
	}

	public void openSubSection(String title, boolean hasNumeration)
			throws IOException {
		if (this.closed)
			return;
		
		if (this.latexFile != null)
			this.latexFile.openSubSection(title, hasNumeration);
		if (this.xmlFile != null)
			this.xmlFile.openSubSection(title, hasNumeration);
	}

	public void closeSubSection() throws IOException {
		if (this.closed)
			return;
		
		if (this.latexFile != null)
			this.latexFile.closeSubSection();
		if (this.xmlFile != null)
			this.xmlFile.closeSubSection();
	}

	public void openParagraph() throws IOException {
		if (this.closed)
			return;
		
		if (this.latexFile != null)
			this.latexFile.openParagraph();
		if (this.xmlFile != null)
			this.xmlFile.openParagraph();
	}

	public void closeParagraph() throws IOException {
		if (this.closed)
			return;
		
		if (this.latexFile != null)
			this.latexFile.closeParagraph();
		if (this.xmlFile != null)
			this.xmlFile.closeParagraph();
	}

	public void openEnum(String enumCommand) throws IOException {
		if (this.closed)
			return;
		
		if (this.latexFile != null)
			this.latexFile.openEnum(enumCommand);
		if (this.xmlFile != null)
			this.xmlFile.openEnum(enumCommand);
	}

	public void closeEnum(String enumCommand) throws IOException {
		if (this.closed)
			return;
		
		if (this.latexFile != null)
			this.latexFile.closeEnum(enumCommand);
		if (this.xmlFile != null)
			this.xmlFile.closeEnum(enumCommand);
	}

	public void openItem() throws IOException {
		if (this.closed)
			return;
		
		if (this.latexFile != null)
			this.latexFile.openItem();
		if (this.xmlFile != null)
			this.xmlFile.openItem();
	}

	public void closeItem() throws IOException {
		if (this.closed)
			return;
		
		if (this.latexFile != null)
			this.latexFile.closeItem();
		if (this.xmlFile != null)
			this.xmlFile.closeItem();
	}

	public void openItemWithDesc(String desc) throws IOException {
		if (this.closed)
			return;
		
		if (this.latexFile != null)
			this.latexFile.openItemWithDesc(desc);
		if (this.xmlFile != null)
			this.xmlFile.openItemWithDesc(desc);
	}

	public void closeItemWithDesc(String desc) throws IOException {
		if (this.closed)
			return;
		
		if (this.latexFile != null)
			this.latexFile.closeItemWithDesc(desc);
		if (this.xmlFile != null)
			this.xmlFile.closeItemWithDesc(desc);
	}

	public void writePlainText(String text) throws IOException {
		if (this.closed)
			return;
		
		if (this.latexFile != null)
			this.latexFile.writePlainText(text);
		if (this.xmlFile != null)
			this.xmlFile.writePlainText(text);
	}

	public void writeFormattedText(String text, int formatType)
			throws IOException {
		if (this.closed)
			return;
		
		if (this.latexFile != null)
			this.latexFile.writeFormattedText(text, formatType);
		if (this.xmlFile != null)
			this.xmlFile.writeFormattedText(text, formatType);
	}

	public void writeProofText(String text) throws IOException {
		if (this.closed)
			return;
		
		if (this.latexFile != null)
			this.latexFile.writeProofText(text);
		if (this.xmlFile != null)
			this.xmlFile.writeProofText(text);
	}

	public void writeSingleLine(String text) throws IOException {
		if (this.closed)
			return;
		
		if (this.latexFile != null)
			this.latexFile.writeSingleLine(text);
		if (this.xmlFile != null)
			this.xmlFile.writeSingleLine(text);
	}

	public void writeBoldText(String text) throws IOException {
		if (this.closed)
			return;
		
		if (this.latexFile != null)
			this.latexFile.writeBoldText(text);
		if (this.xmlFile != null)
			this.xmlFile.writeBoldText(text);
	}

	public void writeEnumDescription(String desc) throws IOException {
		if (this.closed)
			return;
		
		if (this.latexFile != null)
			this.latexFile.writeEnumDescription(desc);
		if (this.xmlFile != null)
			this.xmlFile.writeEnumDescription(desc);
	}

	public void writeEnumItem(String text) throws IOException {
		if (this.closed)
			return;
		
		if (this.latexFile != null)
			this.latexFile.writeEnumItem(text);
		if (this.xmlFile != null)
			this.xmlFile.writeEnumItem(text);
	}
	
	public void writePointCoordinatesAssignment(Point P)
			throws IOException {
		if (this.closed)
			return;
		
		if (this.latexFile != null)
			this.latexFile.writePointCoordinatesAssignment(P);
		if (this.xmlFile != null)
			this.xmlFile.writePointCoordinatesAssignment(P);
	}
	
	public void writePointWithCoordinates(Point P)
			throws IOException {
		if (this.closed)
			return;

		if (this.latexFile != null)
			this.latexFile.writePointWithCoordinates(P);
		if (this.xmlFile != null)
			this.xmlFile.writePointWithCoordinates(P);
	}
	
	public void writePolynomial(int index, XPolynomial xpoly)
			throws IOException {
		if (this.closed)
			return;
		
		if (this.latexFile != null)
			this.latexFile.writePolynomial(index, xpoly);
		if (this.xmlFile != null)
			this.xmlFile.writePolynomial(index, xpoly);
	}
	
	public void writePolynomial(XPolynomial xpoly)
			throws IOException {
		if (this.closed)
			return;
		
		if (this.latexFile != null)
			this.latexFile.writePolynomial(xpoly);
		if (this.xmlFile != null)
			this.xmlFile.writePolynomial(xpoly);
	}
	
	public void writePolySystem(XPolySystem xpolySys) throws IOException {
		if (this.closed)
			return;
		
		if (this.latexFile != null)
			this.latexFile.writePolySystem(xpolySys);
		if (this.xmlFile != null)
			this.xmlFile.writePolySystem(xpolySys);
	}
	
	public boolean isDocumentOpened() {
		if (this.closed)
			return false;
		
		// since both files are written synchronously no matter which one is used
		if (this.latexFile != null)
			return this.latexFile.isDocumentOpened();
		
		if (this.xmlFile != null)
			return this.xmlFile.isDocumentOpened();
		
		return false;
	}

	public boolean isSectionOpened() {
		if (this.closed)
			return false;
		
		// since both files are written synchronously no matter which one is used
		if (this.latexFile != null)
			return this.latexFile.isSectionOpened();
		
		if (this.xmlFile != null)
			return this.xmlFile.isSectionOpened();
		
		return false;
	}

	public boolean isSubSectionOpened() {
		if (this.closed)
			return false;
		
		// since both files are written synchronously no matter which one is used
		if (this.latexFile != null)
			return this.latexFile.isSubSectionOpened();
		
		if (this.xmlFile != null)
			return this.xmlFile.isSubSectionOpened();
		
		return false;
	}

	public boolean isParagraphOpened() {
		if (this.closed)
			return false;
		
		// since both files are written synchronously no matter which one is used
		if (this.latexFile != null)
			return this.latexFile.isParagraphOpened();
		
		if (this.xmlFile != null)
			this.xmlFile.isParagraphOpened();
		
		return false;
	}
	
	/**
	 * Method for closing output files.
	 */
	public void close() {
		try {
			this.closeDocument(); // finish formated text output 
		} catch (IOException e) {
			// disregard this exception
		}
		if (this.latexFile != null)
			this.latexFile.close();
		if (this.xmlFile != null)
			this.xmlFile.close();
		this.closed = true; // forbid further write actions to this output
	}
}