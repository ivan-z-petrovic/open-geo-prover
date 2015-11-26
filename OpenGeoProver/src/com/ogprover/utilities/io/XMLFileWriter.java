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
* <dd>Class for XML output files</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class XMLFileWriter extends CustomFileWriter implements SpecialFileFormatting {
	private boolean documentOpened;
	private boolean sectionOpened;
	private boolean subSectionOpened;
	private boolean paragraphOpened;
	private Vector<String> lastEnumCommandList = null;
	// numbers of section and subsection to be opened
	private int nextSectionNum = 0;
	private int nextSubSectionNum = 0;
	
	public XMLFileWriter(String fileName) throws IOException{
		super(fileName, "xml");
		this.documentOpened = false;
		this.sectionOpened = false;
		this.subSectionOpened = false;
		this.paragraphOpened = false;
		this.lastEnumCommandList = new Vector<String>();
	}
	
	public void openDocument(String documentType, String title, String author) throws IOException {
		if (this.documentOpened)
			return;
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append("<!DOCTYPE main_proof SYSTEM \"geocons_proof.dtd\">\n");
		sb.append("<?xml-stylesheet href=\"GeoCons_proof.xsl\" type=\"text/xsl\"?>\n\n");
		sb.append("<main_proof>\n");
		// documentType for XML not relevant.
		
		sb.append("<proof_title>");
		if (title != null)
			sb.append(title);
		else
			sb.append("[theorem title not provided]");
		sb.append(" (");
		if (author != null)
			sb.append(author);
		else
			sb.append("[author name not provided]");
		sb.append(") </proof_title>\n");
		
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
		
		this.write("\n\n</main_proof>\n\n");
		
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
		
		this.nextSectionNum++;
		this.nextSubSectionNum = 0;
		this.sectionOpened = true;
		
		StringBuilder sb = new StringBuilder();
		sb.append("\n\n<proof_section secnum=\"");
		sb.append(this.nextSectionNum);
		sb.append("\">\n<caption>");
		sb.append(title);
		sb.append("</caption>\n");
		this.write(sb.toString());
	}

	public void closeSection() throws IOException {
		if (this.isSectionOpened()) {
			this.write("\n</proof_section>\n\n");
			this.sectionOpened = false;
		}
	}

	public void openSubSection(String title, boolean hasNumeration) throws IOException {
		// hasNumeration is not used for XML output - it is always numerated
		
		if (this.lastEnumCommandList != null) {
			while (this.lastEnumCommandList.size() > 0)
				closeEnum(this.lastEnumCommandList.get(0));
			this.lastEnumCommandList.clear();
		}
		if (this.paragraphOpened)
			closeParagraph();
		if (this.subSectionOpened)
			closeSubSection();
		
		this.nextSubSectionNum++;
		this.subSectionOpened = true;
		
		StringBuilder sb = new StringBuilder();
		sb.append("\n\n<proof_subsection subsecnum=\"");
		sb.append(this.nextSectionNum);
		sb.append(".");
		sb.append(this.nextSubSectionNum);
		sb.append("\">\n<caption>");
		sb.append(title);
		sb.append("</caption>\n");
		this.write(sb.toString());
	}

	public void closeSubSection() throws IOException {
		if (this.isSubSectionOpened()) {
			this.write("\n</proof_subsection>\n\n");
			this.subSectionOpened = false;	
		}
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
		this.write("\n<proof_line>\n");
	}

	public void closeParagraph() throws IOException {
		if (this.paragraphOpened) {
			this.write("\n</proof_line>\n\n");
			this.paragraphOpened = false;
		}
	}

	public void openEnum(String enumCommand) throws IOException {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\n<proof_enum_");
		sb.append(enumCommand);
		sb.append(">\n");
		this.write(sb.toString());
		if (this.lastEnumCommandList != null)
			this.lastEnumCommandList.add(0, new String(enumCommand)); // add newest command at the beginning so it will be closed before existing commands
	}

	public void closeEnum(String enumCommand) throws IOException {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\n</proof_enum_");
		sb.append(enumCommand);
		sb.append(">\n");
		this.write(sb.toString());
		if (this.lastEnumCommandList != null)
			this.lastEnumCommandList.remove(0);
	}

	public void openItem() throws IOException {
		this.write("\n<proof_enum_item>");		
	}

	public void closeItem() throws IOException {
		this.write("</proof_enum_item>");	
	}

	public void openItemWithDesc(String desc) throws IOException {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\n<proof_enum_item><proof_text_bold>");
		sb.append(XMLFileWriter.formatTextInXML(this.processTextWithIndices(desc)));
		sb.append("</proof_text_bold>\n");
		this.write(sb.toString());
	}

	public void closeItemWithDesc(String desc) throws IOException {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\n ");
		sb.append(XMLFileWriter.formatTextInXML(this.processTextWithIndices(desc)));
		sb.append("</proof_enum_item>\n");
		this.write(sb.toString());
	}
	
	private String processTextWithIndices(String text) {
		String newText = text.replace("<ind_text>", "<poly_label>");
		newText = newText.replace("<label>", "<poly_label_text>");
		newText = newText.replace("<ind>", "<proof_index>");
		newText = newText.replace("</ind_text>", "</poly_label>");
		newText = newText.replace("</label>", "</poly_label_text>");
		newText = newText.replace("</ind>", "</proof_index>");
		
		return newText;
	}

	public void writePlainText(String text) throws IOException {
		this.write(XMLFileWriter.formatTextInXML(this.processTextWithIndices(text)));
	}

	public void writeFormattedText(String text, int formatType) throws IOException {
		StringBuilder sb = new StringBuilder();
		
		switch(formatType){
		case SpecialFileFormatting.FMT_TYPE_PROOF_TEXT:
			sb.append("<proof_text>");
			sb.append(XMLFileWriter.formatTextInXML(text));
			sb.append("</proof_text>");
			break;
		case SpecialFileFormatting.FMT_TYPE_PROOF_LINE:
			sb.append("<proof_line>");
			sb.append(XMLFileWriter.formatTextInXML(text));
			sb.append("</proof_line>");
			break;
		case SpecialFileFormatting.FMT_TYPE_PROOF_BOLD:
			sb.append("\n<proof_bold>");
			sb.append(XMLFileWriter.formatTextInXML(text));
			sb.append("</proof_bold>\n");
			break;
		default:
			sb.append(XMLFileWriter.formatTextInXML(text));
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
		
		sb.append("\n<proof_bold>");
		sb.append(XMLFileWriter.formatTextInXML(desc));
		sb.append("</proof_bold>\n");
		this.write(sb.toString());
	}

	public void writeEnumItem(String text) throws IOException {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\n<proof_enum_item>");
		sb.append(XMLFileWriter.formatTextInXML(text));
		sb.append("</proof_enum_item>\n");
		this.write(sb.toString());
	}
	
	public void writePointCoordinatesAssignment(Point P)
			throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("Point ");
		sb.append(P.getGeoObjectLabel());
		sb.append(" has been assigned following coordinates: (");
		sb.append(P.getX().printToXML());
		sb.append(", ");
		sb.append(P.getY().printToXML());
		sb.append(")\n");
		this.write(sb.toString());
	}
	
	public void writePointWithCoordinates(Point P)
			throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append(P.getGeoObjectLabel());
		sb.append("(");
		sb.append(P.getX().printToXML());
		sb.append(", ");
		sb.append(P.getY().printToXML());
		sb.append(")");
		this.write(sb.toString());
	}
	
	private String getPolynomialText(int index, XPolynomial xpoly) {
		StringBuilder sb = new StringBuilder();
		int outIndex = index + 1;
		int numOfTerms = xpoly.getTerms().size();
		
		if (numOfTerms > OGPConstants.MAX_OUTPUT_POLY_TERMS_NUM) {
			sb.append("\n<proof_line>\n");
			sb.append("Polynomial too big for output (number of terms is ");
			sb.append(numOfTerms);
			sb.append(")\n</proof_line>\n");
		}
		else {
			String xmlXPoly = xpoly.printToXML();
			int len = xmlXPoly.length();
			
			if (len > OGPConstants.MAX_XML_OUTPUT_POLY_CHARS_NUM ||
				xmlXPoly.startsWith("??")) {
				sb.append("\n<proof_line>\n");
				sb.append("Polynomial too big for output (text size is ");
				if (xmlXPoly.startsWith("??")) {
					sb.append("greater than ");
					sb.append(OGPConstants.MAX_XML_OUTPUT_POLY_CHARS_NUM);
				}
				else
					sb.append(len);
				sb.append(" characters, number of terms is ");
				sb.append(numOfTerms);
				sb.append(")\n</proof_line>\n");
			}
			else {
				sb.append("\n<polynomial>\n");
				sb.append("<poly_label><poly_label_text>");
				if (index < -1)
					sb.append("p</poly_label_text>");
				else if (index <= -1) // polynomial represents statement
					sb.append("g</poly_label_text>");
				else {
					sb.append("p</poly_label_text><proof_index>");
					sb.append(outIndex);
					sb.append("</proof_index>");
				}
				sb.append("</poly_label>");
				
				sb.append(xmlXPoly);
				sb.append("\n</polynomial>\n");
			}
		}
		
		return sb.toString();
	}
	
	public void writePolynomial(int index, XPolynomial xpoly)
			throws IOException {
		this.write(getPolynomialText(index, xpoly));
	}
	
	public void writePolynomial(XPolynomial xpoly)
			throws IOException {
		this.writePolynomial(-2, xpoly);
	}
	
	public void writePolySystem(XPolySystem xpolySys) throws IOException {
		if (xpolySys == null || xpolySys.getPolynomials().size() == 0)
			return;
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("\n<polynomial_system>\n");
		
		int ii = 0;
		for (XPolynomial xp : xpolySys.getPolynomials()) {
			sb.append(this.getPolynomialText(ii, xp));
			sb.append("\n");
			ii++;
		}
		
		sb.append("\n</polynomial_system>\n");
		
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
	
	public static String formatTextInXML(String text) {
		String formattedText = new String(text);
		
		formattedText = formattedText.replaceAll("\\$", ""); // each $ sign is deleted
		formattedText = formattedText.replaceAll("\\\\\\{", "{"); // each sequence '\{' is replaced by '{'
		formattedText = formattedText.replaceAll("\\\\\\}", "}"); // each sequence '\}' is replaced by '}'
		formattedText = formattedText.replaceAll("\\\\ni", "&#8715;"); // each sequence '\ni' is replaced by specified Unicode character
		formattedText = formattedText.replaceAll("\\\\emptyset", "&#8709;"); // each sequence '\emptyset' is replaced by specified Unicode character
		
		return formattedText;
	}
}