/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.utilities.io;

import java.io.IOException;
import java.util.Vector;

import com.ogprover.main.OGPConstants;
import com.ogprover.polynomials.XPolySystem;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.pp.tp.geoconstruction.Point;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for LaTeX output files</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class LaTeXFileWriter extends CustomFileWriter implements SpecialFileFormatting {
	private boolean documentOpened;
	private boolean sectionOpened;
	private boolean subSectionOpened;
	private boolean paragraphOpened;
	private Vector<String> lastEnumCommandList = null;
	
	public LaTeXFileWriter(String fileName) throws IOException{
		super(fileName, "tex");
		this.documentOpened = false;
		this.sectionOpened = false;
		this.subSectionOpened = false;
		this.paragraphOpened = false;
		this.lastEnumCommandList = new Vector<String>(); // empty list
	}

	public void openDocument(String documentType, String title, String author) throws IOException {
		if (this.documentOpened)
			return;
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("\\documentclass[a4paper]{");
		if (documentType != null)
			sb.append(documentType);
		else
			sb.append("article"); // default
		sb.append("}\n\\usepackage{gclc_proof}\n\n\n\\begin{document}\n\n\\title{");
		if (title != null)
			sb.append(title);
		else
			sb.append("[theorem title not provided]");
		sb.append("}\n\n\\author{");
		if (author != null)
			sb.append(author);
		else
			sb.append("[author name not provided]");
		sb.append("}\n\n\\maketitle\n\n\n\n\n\n");
		
		this.write(sb.toString());
		
		this.documentOpened = true;
	}
	
	public void closeDocument() throws IOException {
		if (!this.documentOpened)
			return;
		
		if (this.lastEnumCommandList != null) {
			while (this.lastEnumCommandList.size() > 0)
				closeEnum(this.lastEnumCommandList.get(0));
			this.lastEnumCommandList.clear();
		}
		if (this.paragraphOpened)
			closeParagraph();
		if (this.subSectionOpened)
			closeSubSection();
		if (this.sectionOpened)
			closeSection();
		
		this.write("\n\\end{document}\n");
		
		this.documentOpened = false;
	}
	
	public void openSection(String title) throws IOException{
		if (this.lastEnumCommandList != null) {
			while (this.lastEnumCommandList.size() > 0)
				closeEnum(this.lastEnumCommandList.get(0));
			this.lastEnumCommandList.clear();
		}
		if (this.paragraphOpened)
			closeParagraph();
		if (this.subSectionOpened)
			closeSubSection();
		if (this.sectionOpened)
			closeSection();
		
		this.sectionOpened = true;
		
		StringBuilder sb = new StringBuilder();
		sb.append("\n\n\n\n\\section{");
		sb.append(title);
		sb.append("}\n\n");
		this.write(sb.toString());
	}

	public void closeSection() throws IOException {
		this.sectionOpened = false;		
	}

	public void openSubSection(String title, boolean hasNumeration) throws IOException {
		if (this.lastEnumCommandList != null) {
			while (this.lastEnumCommandList.size() > 0)
				closeEnum(this.lastEnumCommandList.get(0));
			this.lastEnumCommandList.clear();
		}
		if (this.paragraphOpened)
			closeParagraph();
		if (this.subSectionOpened)
			closeSubSection();
		this.subSectionOpened = true;
		
		StringBuilder sb = new StringBuilder();
		sb.append("\n\n\n\n\\subsection");
		if (hasNumeration)
			sb.append("{");
		else
			sb.append("*{");
		sb.append(title);
		sb.append("}\n\n");
		this.write(sb.toString());
	}

	public void closeSubSection() throws IOException {
		this.subSectionOpened = false;
	}

	public void openParagraph() throws IOException {
		if (this.lastEnumCommandList != null) {
			while (this.lastEnumCommandList.size() > 0)
				closeEnum(this.lastEnumCommandList.get(0));
			this.lastEnumCommandList.clear();
		}
		if (this.paragraphOpened)
			closeParagraph();
		this.paragraphOpened = true;
		this.write("\n\n");
	}

	public void closeParagraph() throws IOException {
		this.write("\n");
		this.paragraphOpened = false;	
	}

	public void openEnum(String enumCommand) throws IOException {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\\begin{");
		sb.append(enumCommand);
		sb.append("}\n\n");
		this.write(sb.toString());
		if (this.lastEnumCommandList != null)
			this.lastEnumCommandList.add(0, new String(enumCommand)); // add newest command at the beginning so it will be closed before existing commands
	}

	public void closeEnum(String enumCommand) throws IOException {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\\end{");
		sb.append(enumCommand);
		sb.append("}\n\n");
		this.write(sb.toString());
		if (this.lastEnumCommandList != null)
			this.lastEnumCommandList.remove(0);
	}

	public void openItem() throws IOException {
		this.write("\\item ");		
	}

	public void closeItem() throws IOException {
		// nothing for LaTeX		
	}

	public void openItemWithDesc(String desc) throws IOException {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\\item [");
		sb.append(this.processTextWithIndices(desc));
		sb.append("] ");
		this.write(sb.toString());
	}

	public void closeItemWithDesc(String desc) throws IOException {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" ");
		sb.append(this.processTextWithIndices(desc));
		this.write(sb.toString());
	}
	
	private String processTextWithIndices(String text) {
		StringBuilder sb = new StringBuilder();
		int indexOfOpenIndTextTag = -1, indexOfCloseIndTextTag = -1, startIndexOfResidualPart = 0;
		int indexOfOpenLabelTag = -1, indexOfCloseLabelTag = -1, indexOfOpenIndTag = -1, indexOfCloseIndTag = -1;
		String firstPart = null;
		String tagPart = null;
		String residualPart = text;
		
		boolean endOfLoop = false;
		do {
			indexOfOpenIndTextTag = residualPart.indexOf("<ind_text>");
			indexOfCloseIndTextTag = residualPart.indexOf("</ind_text>");
			startIndexOfResidualPart = indexOfCloseIndTextTag + 11;
		
			if (indexOfOpenIndTextTag == -1 || indexOfCloseIndTextTag == -1 || indexOfCloseIndTextTag <= indexOfOpenIndTextTag) {
				sb.append(residualPart);
				endOfLoop = true;
			}
			else {
				// extract one indexed text
				firstPart = residualPart.substring(0, indexOfOpenIndTextTag);
				tagPart = residualPart.substring(indexOfOpenIndTextTag, startIndexOfResidualPart);
				residualPart = residualPart.substring(startIndexOfResidualPart);
				
				sb.append(firstPart);
				
				// extract parts of indexed text
				indexOfOpenLabelTag = tagPart.indexOf("<label>");
				indexOfCloseLabelTag = tagPart.indexOf("</label>");
				indexOfOpenIndTag = tagPart.indexOf("<ind>");
				indexOfCloseIndTag = tagPart.indexOf("</ind>");
				
				if (indexOfOpenLabelTag == -1 || indexOfCloseLabelTag == -1 || indexOfCloseLabelTag <= indexOfOpenLabelTag ||
					indexOfOpenIndTag == -1 || indexOfCloseIndTag == -1 || indexOfCloseIndTag <= indexOfOpenIndTag)
					sb.append(tagPart);
				else {
					sb.append("$");
					sb.append(tagPart.substring(indexOfOpenLabelTag + 7, indexOfCloseLabelTag));
					sb.append("_{");
					sb.append(tagPart.substring(indexOfOpenIndTag + 5, indexOfCloseIndTag));
					sb.append("}$");
				}
			}
		} while(!endOfLoop);
		
		return sb.toString();
	}

	public void writePlainText(String text) throws IOException {
		this.write(this.processTextWithIndices(text));
	}

	public void writeFormattedText(String text, int formatType) throws IOException {
		StringBuilder sb = new StringBuilder();
		
		switch(formatType){
		case SpecialFileFormatting.FMT_TYPE_PROOF_TEXT:
			sb.append(text);
			break;
		case SpecialFileFormatting.FMT_TYPE_PROOF_LINE:
			sb.append(text);
			sb.append("\n");
			break;
		case SpecialFileFormatting.FMT_TYPE_PROOF_BOLD:
			sb.append("\\textbf{");
			sb.append(text);
			sb.append("}\n\n");
			break;
		default:
			sb.append(text);
			break;
		}
		
		this.write(sb.toString());
	}
	
	public void writeProofText(String text) throws IOException {
		writeFormattedText(text, SpecialFileFormatting.FMT_TYPE_PROOF_TEXT);
	}
	
	public void writeSingleLine(String text) throws IOException {
		writeFormattedText(text, SpecialFileFormatting.FMT_TYPE_PROOF_LINE);
	}

	public void writeBoldText(String text) throws IOException {
		writeFormattedText(text, SpecialFileFormatting.FMT_TYPE_PROOF_BOLD);
		
	}

	public void writeEnumDescription(String desc) throws IOException {
		StringBuilder sb = new StringBuilder();
		
		sb.append("[");
		sb.append(desc);
		sb.append("] ");
		this.write(sb.toString());
	}

	public void writeEnumItem(String text) throws IOException {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\\item ");
		sb.append(text);
		sb.append("\n\n");
		this.write(sb.toString());
	}
	
	public void writePointCoordinatesAssignment(Point P)
			throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("Point ");
		sb.append(P.getGeoObjectLabel());
		sb.append(" has been assigned following coordinates: ($");
		sb.append(P.getX().printToLaTeX());
		sb.append("$, $");
		sb.append(P.getY().printToLaTeX());
		sb.append("$)\n");
		this.write(sb.toString());
	}
	
	public void writePointWithCoordinates(Point P)
			throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append(P.getGeoObjectLabel());
		sb.append("($");
		sb.append(P.getX().printToLaTeX());
		sb.append("$, $");
		sb.append(P.getY().printToLaTeX());
		sb.append("$)");
		this.write(sb.toString());
	}
	
	public void writePolynomial(int index, XPolynomial xpoly)
			throws IOException {
		StringBuilder sb = new StringBuilder();
		int outIndex = index + 1;
		int numOfTerms = xpoly.getTerms().size();
		
		if (numOfTerms > OGPConstants.MAX_OUTPUT_POLY_TERMS_NUM) {
			sb.append("\n\\hspace*{2em} \\parbox{0.65\\textwidth}{\\textit{Polynomial too big for output (number of terms is $");
			sb.append(numOfTerms);
			sb.append("$)}}\n\n");
		}
		else {
			String latexXPoly = xpoly.printToLaTeX();
			int len = latexXPoly.length();
			
			if (len > OGPConstants.MAX_OUTPUT_POLY_CHARS_NUM ||
				latexXPoly.startsWith("??")) {
				sb.append("\n\\hspace*{2em} \\parbox{0.65\\textwidth}{\\textit{Polynomial too big for output (text size is ");
				if (latexXPoly.startsWith("??")) {
					sb.append("greater than $");
					sb.append(OGPConstants.MAX_OUTPUT_POLY_CHARS_NUM);
				}
				else {
					sb.append("$");
					sb.append(len);
				}
				sb.append("$ characters, number of terms is $");
				sb.append(numOfTerms);
				sb.append("$)}}\n\n");
			}
			else {
				sb.append("\n\\begin{eqnarray*}\n");
				if (index < -1)
					sb.append("p & = & ");
				else if (index <= -1) // polynomial represents statement
					sb.append("g & = & ");
				else
					sb.append("p_{" + outIndex + "} & = & ");
				
				int startIndex = 0;
				String tempPolyOutput = latexXPoly;
				int endIndex = tempPolyOutput.indexOf("$$"); // "$$" is used to separate chunks
				boolean firstChunk = true;
				
				while (endIndex >= 0) {
					if (!firstChunk)
						sb.append("\\\\ \n & & ");
					else
						firstChunk = false;
					
					sb.append(tempPolyOutput.substring(startIndex, endIndex));
					startIndex = endIndex + 2; // skip "$$"
					if (startIndex < tempPolyOutput.length()) {
						tempPolyOutput = tempPolyOutput.substring(startIndex);
						endIndex = tempPolyOutput.indexOf("$$");
						startIndex = 0;
					}
					else {
						tempPolyOutput = null;
						endIndex = -1;
					}
				}
				
				if (tempPolyOutput != null && tempPolyOutput.length() > 0) {
					if (!firstChunk)
						sb.append("\\\\ \n & & ");
					sb.append(tempPolyOutput);
				}
				sb.append("\n\\end{eqnarray*}\n");
			}
		}
		
		this.write(sb.toString());
	}
	
	public void writePolynomial(XPolynomial xpoly)
			throws IOException {
		this.writePolynomial(-2, xpoly);
	}
	
	public void writePolySystem(XPolySystem xpolySys) throws IOException {
		if (xpolySys == null || xpolySys.getPolynomials().size() == 0)
			return;
		
		StringBuilder sb = new StringBuilder();
		int ii = 1;
		boolean firstPoly = true;
		
		sb.append("\n\\begin{eqnarray*}\n");
		
		for (XPolynomial xp : xpolySys.getPolynomials()) {
			int numOfTerms = xp.getTerms().size();
			
			if (!firstPoly)
				sb.append("\\\\ \n");
			else
				firstPoly = false;
			
			sb.append("p_{" + ii + "} & = & ");
			if (numOfTerms > OGPConstants.MAX_OUTPUT_POLY_TERMS_NUM)
				sb.append("\\ldots");
			else {
				String latexXPoly = xp.printToLaTeX();
				int len = latexXPoly.length();
				
				if (len > OGPConstants.MAX_OUTPUT_POLY_CHARS_NUM ||
					latexXPoly.startsWith("??"))
					sb.append("\\ldots");
				else {
					int startIndex = 0;
					String tempPolyOutput = latexXPoly;
					int endIndex = tempPolyOutput.indexOf("$$"); // "$$" is used to separate chunks
					boolean firstChunk = true;
					
					while (endIndex >= 0) {
						if (!firstChunk)
							sb.append("\\\\ \n & & ");
						else
							firstChunk = false;
						
						sb.append(tempPolyOutput.substring(startIndex, endIndex));
						startIndex = endIndex + 2; // skip "$$"
						if (startIndex < tempPolyOutput.length()) {
							tempPolyOutput = tempPolyOutput.substring(startIndex);
							endIndex = tempPolyOutput.indexOf("$$");
							startIndex = 0;
						}
						else {
							tempPolyOutput = null;
							endIndex = -1;
						}
					}
					
					if (tempPolyOutput != null && tempPolyOutput.length() > 0) {
						if (!firstChunk)
							sb.append("\\\\ \n & & ");
						sb.append(tempPolyOutput);
					}
				}
			}
			ii++;
		}
		
		sb.append("\n\\end{eqnarray*}\n");
		
		this.write(sb.toString());
	}
	
	public boolean isDocumentOpened() {
		return this.documentOpened;
	}

	public boolean isSectionOpened() {
		return this.sectionOpened;
	}

	public boolean isSubSectionOpened() {
		return this.subSectionOpened;
	}

	public boolean isParagraphOpened() {
		return this.paragraphOpened;
	}

}