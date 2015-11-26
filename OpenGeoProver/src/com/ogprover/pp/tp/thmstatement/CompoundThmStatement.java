/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.thmstatement;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.ogprover.main.OpenGeoProver;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Abstract class for compound geometry theorem statements</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public abstract class CompoundThmStatement extends ThmStatement {
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
	 * List of all theorem statements that make this compound statement
	 */
	protected Vector<ThmStatement> particleThmStatements = null;
	
	
	
	/*
	 * ======================================================================
	 * ========================= ABSTRACT METHODS ===========================
	 * ======================================================================
	 */
	
	
	

	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @return the particleThmStatements
	 */
	public Vector<ThmStatement> getParticleThmStatements() {
		return particleThmStatements;
	}

	/**
	 * @param particleThmStatements the particleThmStatements to set
	 */
	public void setParticleThmStatements(Vector<ThmStatement> particleThmStatements) {
		this.particleThmStatements = particleThmStatements;
	}

	
	

	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * @see com.ogprover.pp.tp.thmstatement.ThmStatement#isValid()
	 */
	@Override
	public boolean isValid() {
		// Compound geometry theorem statement is valid iff all its particle
		// statements that appear in it are valid within same theorem protocol.
		
		// First of all call method from superclass
		if (!super.isValid())
			return false;
		
		if (this.particleThmStatements == null || this.particleThmStatements.size() == 0) {
			OpenGeoProver.settings.getLogger().error("Compound theorem statement doesn't contain particle statements");
			return false;
		}
		
		for (ThmStatement statement : this.particleThmStatements) {
			if (this.consProtocol != statement.getConsProtocol()) {
				OpenGeoProver.settings.getLogger().error("Found particle statement not related to theorem protocol of this compound statement.");
				return false;
			}
			
			if (!statement.isValid())
				return false;
		}
		
		return true;
	}
	
	/**
	 * @see com.ogprover.pp.tp.thmstatement.ThmStatement#getInputLabels()
	 */
	@Override
	public String[] getInputLabels() {
		String[] inputLabels;
		
		if (this.particleThmStatements == null || this.particleThmStatements.size() == 0)
			return null;
		
		Map<String, String> labelMap = new HashMap<String, String>();
		for (ThmStatement ts : this.particleThmStatements) {
			for (String label : ts.getInputLabels()) // recursion
				labelMap.put(label, label);
		}
		
		if (labelMap.size() == 0)
			return null;
		
		inputLabels = new String[labelMap.size()];
		int ii = 0;
		for (String label : labelMap.keySet())
			inputLabels[ii++] = label;
		return inputLabels;
	}
	
}
