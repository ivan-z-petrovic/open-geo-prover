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
 * <dt><b>Interface description:</b></dt>
 * <dd>Interface for files with special format like LaTeX or XML.</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */
public interface SpecialFileFormatting {
	// text format types - begin
	public static final int FMT_TYPE_PROOF_TEXT = 0;
	public static final int FMT_TYPE_PROOF_LINE = 1;
	public static final int FMT_TYPE_PROOF_BOLD = 2;
	// text format types - end
	
	// types for enumerated section command
	public static final String ENUM_COMMAND_DESCRIPTION = "description";
	public static final String ENUM_COMMAND_ENUMERATE = "enumerate";
	public static final String ENUM_COMMAND_ITEMIZE = "itemize";
	
	// opening and closing of document
	public void openDocument(String documentType, String title, String author) throws IOException;
	public void closeDocument() throws IOException;
	// opening and closing of section
	public void openSection(String title) throws IOException;
	public void closeSection() throws IOException;
	// opening and closing of subsection
	public void openSubSection(String title, boolean hasNumeration) throws IOException;
	public void closeSubSection() throws IOException;
	// opening and closing of paragraph
	public void openParagraph() throws IOException;
	public void closeParagraph() throws IOException;
	// opening and closing of enumeration
	public void openEnum(String enumCommand) throws IOException;
	public void closeEnum(String enumCommand) throws IOException;
	// opening and closing of item
	public void openItem() throws IOException;
	public void closeItem() throws IOException;
	// opening and closing of item with description
	public void openItemWithDesc(String desc) throws IOException;
	public void closeItemWithDesc(String desc) throws IOException;
	
	// writing output text
	public void writePlainText(String text) throws IOException;
	public void writeFormattedText(String text, int formatType) throws IOException;
	public void writeProofText(String text) throws IOException;
	public void writeSingleLine(String text) throws IOException;
	public void writeBoldText(String text) throws IOException;
	public void writeEnumDescription(String desc) throws IOException;
	public void writeEnumItem(String text) throws IOException;
	public void writePointCoordinatesAssignment(Point P) throws IOException;
	public void writePointWithCoordinates(Point P) throws IOException;
	public void writePolynomial(int index, XPolynomial xpoly) throws IOException;
	public void writePolynomial(XPolynomial xpoly) throws IOException;
	public void writePolySystem(XPolySystem xpolySys) throws IOException;
	
	// checking status of output
	public boolean isDocumentOpened();
	public boolean isSectionOpened();
	public boolean isSubSectionOpened();
	public boolean isParagraphOpened();
}