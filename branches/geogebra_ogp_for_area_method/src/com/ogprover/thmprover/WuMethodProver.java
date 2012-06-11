/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.thmprover;

import java.io.IOException;

import com.ogprover.main.OGPConstants;
import com.ogprover.main.OGPParameters;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.GeoTheorem;
import com.ogprover.polynomials.XPolySystem;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.utilities.io.OGPOutput;
import com.ogprover.utilities.io.SpecialFileFormatting;
import com.ogprover.utilities.logger.ILogger;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for Wu's prover</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class WuMethodProver extends AlgebraicMethodProver {

	/**
	 * Constructor method.
	 * 
	 * @param theorem	Theorem to be proved.
	 */
	public WuMethodProver(GeoTheorem theorem) {
		this.theorem = theorem;
	}
	
	
	/**
	 * @see com.ogprover.thmprover.TheoremProver#prove()
	 */
	public int prove() {
		int retCode;
		
		OGPParameters parameters = OpenGeoProver.settings.getParameters();
		OGPOutput output = OpenGeoProver.settings.getOutput();
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		XPolySystem hypotheses = this.theorem.getHypotheses();
		XPolynomial statement = this.theorem.getStatement();
		boolean isSystemLinear = hypotheses.isSystemLinear();
		boolean writeToReport = parameters.createReport();
		StringBuilder sb;
		
		
		// first step - triangulate the system
		logger.info("Triangulation of system...");
		if (writeToReport) {
			try {
				output.openSection("Invoking the theorem prover");
				sb = new StringBuilder();
				sb.append("The used proving method is ");
				if (parameters.getProver() == TheoremProver.TP_TYPE_WU)
					sb.append("Wu's method.\n\n");
				else if (parameters.getProver() == TheoremProver.TP_TYPE_GROEBNER)
					sb.append("Groebner basis method.\n\n");
				output.writePlainText(sb.toString());
			} catch (IOException e) {
				logger.error("Failed to write to output file(s).");
				output.close();
				return OGPConstants.ERR_CODE_GENERAL;
			}
		}
		retCode = hypotheses.triangulate();
		if (writeToReport) {
			try {
				if (retCode == OGPConstants.ERR_CODE_SPACE) {
					output.openParagraph();
					output.writePlainText("Triangulation has failed because large polynomial has been obtained during calculation.");
					output.closeParagraph();
				}
				else if (retCode == OGPConstants.ERR_CODE_TIME) {
					output.openParagraph();
					output.writePlainText("Triangulation has failed because time for execution has been expired.");
					output.closeParagraph();
				}
				output.closeSection();
			} catch (IOException e) {
				logger.error("Failed to write to output file(s).");
				output.close();
				return OGPConstants.ERR_CODE_GENERAL;
			}
		}
		
		if (retCode < 0) {
			logger.error("Wu's prove method has failed to triangulate the system of hypotheses.");
			return retCode;
		}
		
		// Fill in NDG conditions in algebraic form of theorem
		retCode = this.theorem.fillNDGConditionsForWuProver();
		if (retCode < 0 && writeToReport) {
			try {
				output.openParagraph();
				output.writePlainText("Failed reading of NDG Conditions.");
				output.closeParagraph();
			} catch (IOException e) {
				logger.error("Failed to write to output file(s).");
				output.close();
				return OGPConstants.ERR_CODE_GENERAL;
			}
		}
		
		// second step - calculation of final reminder
		logger.info("Calculation of final reminder...");
		
		if (writeToReport) {
			try {
				output.openSection("Final Remainder");
				output.openSubSection("Final remainder for conjecture " + this.theorem.getName(), true);
				output.writePlainText("Calculating final remainder of the conclusion:\n");
				output.writePolynomial(-1, statement);
				output.writePlainText("with respect to the triangular system.\n\n");
				if (hypotheses.numOfPols() > 0)
					output.openEnum(SpecialFileFormatting.ENUM_COMMAND_ENUMERATE);
			} catch (IOException e) {
				logger.error("Failed to write to output file(s).");
				output.close();
				return OGPConstants.ERR_CODE_GENERAL;
			}
		}
		
		XPolynomial finalReminder = (XPolynomial)statement.clone();
		
		for (int ii = hypotheses.numOfPols() - 1; ii >= 0; ii--) {
			int varIndex = hypotheses.getVariableList().get(ii).intValue();
			if (writeToReport) {
				try {
					output.openItem();
					sb = new StringBuilder();
					sb.append("Pseudo remainder with <ind_text><label>p</label><ind>");
					sb.append(ii + 1);
					sb.append("</ind></ind_text> over variable <ind_text><label>x</label><ind>");
					sb.append(varIndex);
					sb.append("</ind></ind_text>:\n");
					output.writePlainText(sb.toString());
					output.closeItem();
				} catch (IOException e) {
					logger.error("Failed to write to output file(s).");
					output.close();
					return OGPConstants.ERR_CODE_GENERAL;
				}
			}
			finalReminder = finalReminder.pseudoReminder(hypotheses.getXPoly(ii), varIndex);
			
			if (finalReminder == null) {
				logger.error("Wu's prove method has failed to calculate the final reminder due to error in pseudo division operation.");
				return OpenGeoProver.settings.getRetCodeOfPseudoDivision();
			}
			
			int numOfTerms = finalReminder.getTerms().size();
			
			if (numOfTerms > parameters.getSpaceLimit()) {
				logger.error("Polynomial exceeds maximal allowed number of terms.");
				if (writeToReport) {
					try {
						output.openParagraph();
						output.writePlainText("Calculation of final reminder has failed because large polynomial has been obtained during calculation.");
						output.closeParagraph();
					} catch (IOException e) {
						logger.error("Failed to write to output file(s).");
						output.close();
						return OGPConstants.ERR_CODE_GENERAL;
					}
				}
				return OGPConstants.ERR_CODE_SPACE;
			}
			
			if (numOfTerms > OpenGeoProver.settings.getMaxNumOfTerms()) {
				OpenGeoProver.settings.setMaxNumOfTerms(numOfTerms);
			}
			
			if (OpenGeoProver.settings.getTimer().isTimeIsUp()) {
				logger.error("Time for execution of prover has been expired.");
				if (writeToReport) {
					try {
						output.openParagraph();
						output.writePlainText("Calculation of final reminder has failed because time for prover execution has been expired.");
						output.closeParagraph();
					} catch (IOException e) {
						logger.error("Failed to write to output file(s).");
						output.close();
						return OGPConstants.ERR_CODE_GENERAL;
					}
				}
				return OGPConstants.ERR_CODE_TIME;
			}
			
			if (writeToReport) {
				try {
					output.writePolynomial(-1, finalReminder);
				} catch (IOException e) {
					logger.error("Failed to write to output file(s).");
					output.close();
					return OGPConstants.ERR_CODE_GENERAL;
				}
			}
		}
		
		if (writeToReport) {
			try {
				if (hypotheses.numOfPols() > 0)
					output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ENUMERATE);
				output.closeSubSection();
				output.closeSection();
			} catch (IOException e) {
				logger.error("Failed to write to output file(s).");
				output.close();
				return OGPConstants.ERR_CODE_GENERAL;
			}
		}
		
		if (finalReminder.isZero()) {
			return TheoremProver.THEO_PROVE_RET_CODE_TRUE; // theorem has been proved
		}
		
		if (isSystemLinear) {
			return TheoremProver.THEO_PROVE_RET_CODE_FALSE; // theorem has been disproved
		}
		
		return TheoremProver.THEO_PROVE_RET_CODE_UNKNOWN;
	}
	
}