/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.auxiliary;

import java.util.Vector;

import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.ogprover.pp.tp.ndgcondition.AlgebraicNDGCondition;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Abstract class for checking whether certain points' positions 
*     correspond to specified NDG condition</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public abstract class PointsPositionChecker {
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
	 * Attached NDG condition
	 */
	protected AlgebraicNDGCondition ndgCond = null;
	/**
	 * Theorem protocol used for process of translation of 
	 * NDG condition to readable form.
	 */
	protected OGPTP auxiliaryCP = null;
	/**
	 * Initial value of u-index in auxiliary CP.
	 */
	protected int acpUIndex = 1;
	/**
	 * Initial value of x-index in auxiliary CP.
	 */
	protected int acpXIndex = 1;
	
	
	
	
	/*
	 * ======================================================================
	 * ========================= ABSTRACT METHODS ===========================
	 * ======================================================================
	 */
	/**
	 * Method that checks points positions
	 * 
	 * @return	True if some position generates attached NDG condition,
	 * 		    false otherwise
	 */
	public abstract boolean checkPositions(Vector<Point> pointList);
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @param ndgCond the ndgCond to set
	 */
	public void setNDGCond(AlgebraicNDGCondition ndgCond) {
		this.ndgCond = ndgCond;
	}
	
	/**
	 * @return the ndgCond
	 */
	public AlgebraicNDGCondition getNDGCond() {
		return ndgCond;
	}
	
	/**
	 * @param auxiliaryCP the auxiliaryCP to set
	 */
	public void setAuxiliaryCP(OGPTP auxiliaryCP) {
		this.auxiliaryCP = auxiliaryCP;
	}

	/**
	 * @return the auxiliaryCP
	 */
	public OGPTP getAuxiliaryCP() {
		return auxiliaryCP;
	}
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method for clearing auxiliary CP and setting initial values for indices.
	 */
	public void clearAuxCP() {
		this.auxiliaryCP.clear();
		this.auxiliaryCP.setUIndex(this.acpUIndex);
		this.auxiliaryCP.setXIndex(this.acpXIndex);
	}
	
	/**
	 * Method for initialization of auxiliary CP that must be called
	 * within constructors of derived classes.
	 */
	protected void initializeAuxiliaryCP() {
		this.auxiliaryCP = new OGPTP();
		/* 
		 * Set u and x indices from CP of NDG condition for the case 
		 * when new points are generated within this new CP. This is
		 * because this new CP will contain points from NDGC's CP and
		 * new points should be generated with coordinates that follow
		 * already added points.
		 */
		this.acpUIndex = this.ndgCond.getConsProtocol().getUIndex();
		this.acpXIndex = this.ndgCond.getConsProtocol().getXIndex();
		this.auxiliaryCP.setUIndex(this.acpUIndex);
		this.auxiliaryCP.setXIndex(this.acpXIndex);
	}
	
	/**
	 * Main method that must be called within each constructor of derived classes.
	 * 
	 * @param ndgCond	NDG condition associated to this points position checker
	 */
	protected void initializePointsPositionChecker(AlgebraicNDGCondition ndgCond) {
		this.ndgCond = ndgCond;
		this.initializeAuxiliaryCP();
	}
}
