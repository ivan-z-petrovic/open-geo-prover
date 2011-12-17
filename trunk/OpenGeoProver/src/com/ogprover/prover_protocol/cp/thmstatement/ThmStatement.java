/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.prover_protocol.cp.thmstatement;

import java.io.IOException;

import com.ogprover.main.OGPConstants;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.prover_protocol.cp.OGPCP;
import com.ogprover.utilities.io.FileLogger;
import com.ogprover.utilities.io.OGPOutput;
import com.ogprover.utilities.io.SpecialFileFormatting;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Base abstract class for all geometry theorem statements</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public abstract class ThmStatement {
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
	 * Construction protocol that contains this theorem statement
	 */
	protected OGPCP consProtocol = null;
	
	
	
	/*
	 * ======================================================================
	 * ========================= ABSTRACT METHODS ===========================
	 * ======================================================================
	 */
	/**
     * Transformation of theorem statement to polynomial form.
     * 
     * @return	Returns non-reduced polynomial for this statement 
     *          if successful or null otherwise
     */
    public abstract XPolynomial getAlgebraicForm();
    /**
	 * Method that retrieves text description of theorem statement.
	 * 
	 * @return	Textual description of theorem statement
	 */
	public abstract String getStatementDesc();
	
	

	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
    /**
	 * @return the consProtocol
	 */
	public OGPCP getConsProtocol() {
		return consProtocol;
	}
	/**
	 * @param consProtocol the consProtocol to set
	 */
	public void setConsProtocol(OGPCP consProtocol) {
		this.consProtocol = consProtocol;
	}
	
	

	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method that checks the validity of theorem statement.
	 * 
	 * @return	True if theorem statement is valid, false otherwise
	 */
	public boolean isValid() {
		// In base class this method will only check if theorem statement
		// has assigned construction protocol.
		boolean valid = (this.consProtocol != null);
		
		if (!valid)
			OpenGeoProver.settings.getLogger().error("Theorem statement is invalid because it is not added to construction protocol.");
		
		return valid;
	}
    
	/**
     * Method that performs necessary steps to transform the theorem statement
     * into algebraic form (i.e. it is generating algebraic condition
     * that coordinates of constructed points in algebraic form must satisfy).
     * The result of transformation is x-polynomial stored in construction protocol.
     * 
     * @return	Returns SUCCESS if successful or general error otherwise 
     */
    public int transformToAlgebraicForm() {
    	XPolynomial statementPoly = this.getAlgebraicForm();
    	
    	if (statementPoly == null)
    		return OGPConstants.ERR_CODE_GENERAL;
    	
    	this.consProtocol.getAlgebraicGeoTheorem().setStatement(statementPoly.reduceByUTermDivision());
    	
    	OGPOutput output = OpenGeoProver.settings.getOutput();
    	FileLogger logger = OpenGeoProver.settings.getLogger();
    	
    	try {
    		output.openEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
    		output.openItem();
    		output.writePlainText("Polynomial for theorem statement:");
    		output.writePolynomial(statementPoly);
    		output.closeItem();
    		output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
    	} catch (IOException e) {
			logger.error("Failed to write to output file(s).");
			output.close();
			return OGPConstants.ERR_CODE_GENERAL;
		}
		
		return OGPConstants.RET_CODE_SUCCESS;
    }

	
}
