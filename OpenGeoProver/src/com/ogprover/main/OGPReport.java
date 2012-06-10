/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.main;

import java.io.IOException;

import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.ndgcondition.NDGCondition;
import com.ogprover.thmprover.TheoremProver;
import com.ogprover.utilities.OGPUtilities;
import com.ogprover.utilities.Stopwatch;
import com.ogprover.utilities.io.OGPOutput;
import com.ogprover.utilities.io.SpecialFileFormatting;
import com.ogprover.utilities.logger.ILogger;


/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for OpenGeoProver report logic</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */
public class OGPReport {
	/*
	 * ======================================================================
	 * ========================== VARIABLES =================================
	 * ======================================================================
	 */
	/**
	 * <i><b>
	 * Version number of class in form xx.yy where
	 * xx is major version/release number and yy is minor
	 * release number.
	 * </b></i>
	 */
	public static final String VERSION_NUM = "1.00"; // this should match the version number from class comment
	
	/**
	 * Theorem protocol assigned to this report
	 */
	private OGPTP thmProtocol = null;
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @param thmProtocol the thmProtocol to set
	 */
	public void setThmProtocol(OGPTP thmProtocol) {
		this.thmProtocol = thmProtocol;
	}

	/**
	 * @return the thmProtocol
	 */
	public OGPTP getThmProtocol() {
		return thmProtocol;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method.
	 * 
	 * @param tp	Theorem protocol assigned to this report
	 */
	public OGPReport(OGPTP tp) {
		this.thmProtocol = tp;
	}
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * <i>[static method]</i><br>
	 * Method for opening output report.
	 * 
	 * @return			SUCCESS if succeeded to open document, general error otherwise
	 */
	public int openReport() {
		OGPParameters parameters = OpenGeoProver.settings.getParameters();
		OGPOutput output = OpenGeoProver.settings.getOutput();
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (parameters.createReport()) {	
			String title = "OpenGeoProver Output for conjecture ``" + this.thmProtocol.getTheoremName() + "'' ";
			String author = null;
			
			if (parameters.getProver() == TheoremProver.TP_TYPE_WU)
				author = "Wu's method used";
			else if (parameters.getProver() == TheoremProver.TP_TYPE_GROEBNER)
				author = "Groebner basis method used";
			
			try {
				output.openDocument(null, title, author);
			} catch (IOException e) {
				logger.error("Failed to open document.");
				output.close();
				return OGPConstants.ERR_CODE_GENERAL;
			}
		}
		
		return OGPConstants.RET_CODE_SUCCESS;
	}
	
	/**
	 * <i>[static method]</i><br>
	 * Method for printing final report results.
	 * 
	 * @param proverRetCode		Return code of prover execution.
	 * @return					SUCCESS if succeeded to print results, general error otherwise
	 */
	public int printProverResults(int proverRetCode) {
		OGPParameters parameters = OpenGeoProver.settings.getParameters();
		OGPOutput output = OpenGeoProver.settings.getOutput();
		ILogger logger = OpenGeoProver.settings.getLogger();
		Stopwatch stopwatch = OpenGeoProver.settings.getStopwacth();
		int retCode = OGPConstants.RET_CODE_SUCCESS;
		
		if (parameters.createReport()) {
			try {
				output.openSection("Prover results");
				output.openEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
				output.openItemWithDesc("Status:");
			} catch (IOException e) {
				logger.error("Failed to write to output file(s).");
				output.close();
				return OGPConstants.ERR_CODE_GENERAL;
			}
		}
		
		String statusText;
		
		switch (proverRetCode) {
		case TheoremProver.THEO_PROVE_RET_CODE_FALSE:
			statusText = "Theorem has been disproved.";
			System.out.println(statusText);
			if (parameters.createReport()) {
				try {
					output.closeItemWithDesc(statusText);
				} catch (IOException e) {
					logger.error("Failed to write to output file(s).");
					output.close();
					return OGPConstants.ERR_CODE_GENERAL;
				}
			}
			break;
		case TheoremProver.THEO_PROVE_RET_CODE_TRUE:
			statusText = "Theorem has been proved.";
			System.out.println(statusText);
			if (parameters.createReport()) {
				try {
					output.closeItemWithDesc(statusText);
				} catch (IOException e) {
					logger.error("Failed to write to output file(s).");
					output.close();
					return OGPConstants.ERR_CODE_GENERAL;
				}
			}
			break;
		case TheoremProver.THEO_PROVE_RET_CODE_UNKNOWN:
			statusText = "Theorem can't be neither proved nor disproved.";
			System.out.println(statusText);
			if (parameters.createReport()) {
				try {
					output.closeItemWithDesc(statusText);
				} catch (IOException e) {
					logger.error("Failed to write to output file(s).");
					output.close();
					return OGPConstants.ERR_CODE_GENERAL;
				}
			}
			break;
		case OGPConstants.ERR_CODE_GENERAL:
			statusText = "Proving failed - general error occurred.";
			System.out.println(statusText);
			if (parameters.createReport()) {
				boolean exceptionCaught = false;
				try {
					output.closeItemWithDesc(statusText); 
				} catch (IOException e) {
					logger.error("Failed to write to output file(s).");
					exceptionCaught = true;
				} finally {
					output.close();
					if (exceptionCaught)
						return OGPConstants.ERR_CODE_GENERAL;
				}
			}
			break;
		case OGPConstants.ERR_CODE_NULL:
			statusText = "Proving failed - Found null object when expected non-null.";
			System.out.println(statusText);
			if (parameters.createReport()) {
				boolean exceptionCaught = false;
				try {
					output.closeItemWithDesc(statusText);
				} catch (IOException e) {
					logger.error("Failed to write to output file(s).");
					exceptionCaught = true;
				} finally {
					output.close();
					if (exceptionCaught)
						return OGPConstants.ERR_CODE_GENERAL;
				}
			}
			break;
		case OGPConstants.ERR_CODE_SPACE:
			statusText = "Proving failed - Space limit has been reached.";
			System.out.println(statusText);
			if (parameters.createReport()) {
				try {
					output.closeItemWithDesc(statusText);
				} catch (IOException e) {
					logger.error("Failed to write to output file(s).");
					output.close();
					return OGPConstants.ERR_CODE_GENERAL;
				}
			}
			break;
		case OGPConstants.ERR_CODE_TIME:
			statusText = "Proving failed - Time for prover execution has been expired.";
			System.out.println(statusText);
			if (parameters.createReport()) {
				try {
					output.closeItemWithDesc(statusText);
				} catch (IOException e) {
					logger.error("Failed to write to output file(s).");
					output.close();
					return OGPConstants.ERR_CODE_GENERAL;
				}
			}
			break;
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("Time spent by the prover is ");
		sb.append(OGPUtilities.roundUpToPrecision(stopwatch.getTimeIntSec()));
		sb.append(" seconds.");
		String timeReportSec = sb.toString();
		sb = new StringBuilder();
		sb.append("Time spent by the prover is ");
		sb.append(stopwatch.getTimeIntMillisec());
		sb.append(" milliseconds.");
		String timeReportMiliSec = sb.toString();
		sb = new StringBuilder();
		sb.append("The biggest polynomial obtained during prover execution contains ");
		sb.append(OpenGeoProver.settings.getMaxNumOfTerms());
		sb.append(" terms.");
		String spaceReport = sb.toString();
		
		System.out.println(timeReportMiliSec);
		System.out.println();
		System.out.println();
		System.out.println(spaceReport);
		System.out.println();
		System.out.println();
		
		if (parameters.createReport()) {
			try {
				output.openItemWithDesc("Space Complexity:");
				output.closeItemWithDesc(spaceReport);
				output.openItemWithDesc("Time Complexity:");
				output.closeItemWithDesc(timeReportSec);
				output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
				output.closeSection();
			} catch (IOException e) {
				logger.error("Failed to write to output file(s).");
				output.close();
				return OGPConstants.ERR_CODE_GENERAL;
			}
		}
		
		if (proverRetCode != TheoremProver.THEO_PROVE_RET_CODE_FALSE &&
			proverRetCode != TheoremProver.THEO_PROVE_RET_CODE_TRUE) {
			try {
				output.closeDocument();
			} catch (IOException e) {
				logger.error("Failed to write to output file(s).");
				output.close();
				return OGPConstants.ERR_CODE_GENERAL;
			}
			return retCode;
		}
		
		// NDG Conditions - they are processed only if theorem has been proved or disproved
		
		stopwatch.startMeasureTime();
		
		if (parameters.createReport()) {
			try {
				output.openSection("NDG Conditions");
				output.openSubSection("NDG Conditions in readable form", false);
				output.openEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
			} catch (IOException e) {
				logger.error("Failed to write to output file(s).");
				output.close();
				return OGPConstants.ERR_CODE_GENERAL;
			}
		}
		
		retCode = this.thmProtocol.translateNDGConditionsToUserReadableForm();
		
		if (retCode != OGPConstants.RET_CODE_SUCCESS) {
			boolean exceptionCaught = false;
			try {
				output.openItem();
				output.writePlainText("Failed to translate NDG Conditions to readable form");
				output.closeItem();
				output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
				output.closeSubSection();
				output.closeSection();
			} catch (IOException e) {
				logger.error("Failed to write to output file(s).");
				exceptionCaught = true;
			}
			finally {
				output.close();
				if (exceptionCaught)
					retCode =  OGPConstants.ERR_CODE_GENERAL;
			}
			
			return retCode;
		}
		
		if (this.thmProtocol.getNdgConditions() == null) {
			boolean exceptionCaught = false;
			try {
				output.openItem();
				output.writePlainText("There are no NDG conditions for this theorem");
				output.closeItem();
				output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
				output.closeSubSection();
				output.closeSection();
			} catch (IOException e) {
				logger.error("Failed to write to output file(s).");
				exceptionCaught = true;
			}
			finally {
				output.close();
				if (exceptionCaught)
					retCode =  OGPConstants.ERR_CODE_GENERAL;
			}
			
			return retCode;
		}
		
		for (NDGCondition ndgc : this.thmProtocol.getNdgConditions()) {
			String ndgcText = ndgc.getBestDescription();
				
			try {
				if (ndgcText == null || ndgcText.length() == 0) {
					output.openItem();
					output.writePolynomial(ndgc.getPolynomial());
					output.closeItem();
				}
				else {
					output.openItem();
					output.writePlainText(ndgcText);
					output.closeItem();
				}
			} catch (IOException e) {
				logger.error("Failed to write to output file(s).");
				output.close();
				return OGPConstants.ERR_CODE_GENERAL;
			}
		}
			
		stopwatch.endMeasureTime();
		
		try {
			output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
			output.closeSubSection();
			output.openSubSection("Time spent for processing NDG Conditions", false);
			output.openEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
			output.openItem();
			output.writePlainText(OGPUtilities.roundUpToPrecision(stopwatch.getTimeIntSec()) + " seconds");
			output.closeItem();
			output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
			output.closeSubSection();
			output.closeSection();
			output.closeDocument();
		} catch (IOException e) {
			logger.error("Failed to write to output file(s).");
			output.close();
			return OGPConstants.ERR_CODE_GENERAL;
		}
		
		return retCode;
	}
}