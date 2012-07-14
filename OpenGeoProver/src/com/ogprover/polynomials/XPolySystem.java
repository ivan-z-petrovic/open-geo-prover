/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.polynomials;

import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Vector;

import com.ogprover.main.OGPConstants;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.utilities.io.OGPOutput;
import com.ogprover.utilities.io.SpecialFileFormatting;
import com.ogprover.utilities.logger.ILogger;


/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for system of x-polynomials</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */
public class XPolySystem {
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
	 * Collection of polynomials that make the system
	 */
	private Vector<XPolynomial> polynomials;
	/**
	 * List of variable indices as they were introduced in triangular system 
	 */
	private Vector<Integer> variableList;
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * Method that retrieves collection of polynomials
	 * 
	 * @return The polynomials
	 */
	public Vector<XPolynomial> getPolynomials() {
		return polynomials;
	}

	/**
	 * Method that sets collection of polynomials
	 * 
	 * @param polynomials The polynomials to set
	 */
	public void setPolynomials(Vector<XPolynomial> polynomials) {
		this.polynomials = polynomials;
	}

	/**
	 * Method that retrieves variable list
	 * 
	 * @return The variableList
	 */
	public Vector<Integer> getVariableList() {
		return variableList;
	}

	/**
	 * Method that sets variable list
	 * 
	 * @param variableList The variableList to set
	 */
	public void setVariableList(Vector<Integer> variableList) {
		this.variableList = variableList;
	}

	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 */
	public XPolySystem() {
		this.polynomials = new Vector<XPolynomial>(); // empty collection of polynomials
		this.variableList = null; // this is going to be populated in triangulation method
	}

	/*
	 * ======================================================================
	 * ======================= COMMON OBJECT METHODS ========================
	 * ======================================================================
	 */

	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	// Wrapper methods from java.util.Vector class
	/**
	 * Gives XPolynomial object with specified index
	 * 
	 * @param index		Index of element
	 * @return			XPolynomial with specified index from collection
	 */
	public XPolynomial getXPoly(int index) {
		return this.polynomials.get(index);
	}
	
	/**
	 * Updates XPolynomial object in collection at specified index
	 * with passed in XPolynomial
	 * 
	 * @param index		Index of polynomial in collection
	 * @param xPoly		New value of polynomial to be set in collection
	 */
	public void setXPoly(int index, XPolynomial xPoly) {
		this.polynomials.set(index, xPoly);
	}
	
	/**
	 * Adds new polynomial to collection at specified index and
	 * shifts all polynomials in collection from and after that 
	 * position for one place to right
	 * 
	 * @param index		Index of polynomial in collection
	 * @param xPoly		Polynomial to be added to collection
	 */
	public void addXPoly(int index, XPolynomial xPoly) {
		this.polynomials.add(index, xPoly);
	}
	
	/**
	 * Adds new polynomial to the end of collection
	 * 
	 * @param xPoly		Polynomial to be added to collection
	 */
	public void addXPoly(XPolynomial xPoly) {
		this.polynomials.add(xPoly);
	}
	
	/**
	 * Removes polynomial with specified index from collection
	 * and shifts all polynomials after that position to the
	 * left for one place
	 *  
	 * @param index		Index of polynomial in collection
	 */
	public void removePoly(int index) {
		this.polynomials.remove(index);
	}
	
	/**
	 * Gets number of polynomials that make system
	 * 
	 * @return	Number of polynomials in system
	 */
	public int numOfPols() {
		return this.polynomials.size();
	}
	
	/**
	 * Check whether system is valid.
	 * It is valid if polynomials have no other
	 * x variables but those with indices from 1 to n
	 * where n is number of polynomials in system.
	 * Also, all these variables must appear in system.
	 * 
	 * @return	True if system is valid, false otherwise
	 */
	public boolean isValid() {
		int n = this.polynomials.size();
		int[] usedIndices = new int[n]; // i-th element is zero if variable x[i+1] is never
		                                // used in system, and it is 1 otherwise
		
		for (int ii = 0; ii < n; ii++)
			usedIndices[ii] = 0;
		
		for (XPolynomial xp : this.polynomials) {
			for (Term xt : xp.getTermsAsDescList()) {
				for (Power pow : xt.getPowers()) {
					int currIndex = (int)pow.getIndex(); // for power of x-polynomial it is safe to case index to int
					
					if (currIndex <= 0 || currIndex > n)
						return false; // system is invalid because it contains disallowed variable
					usedIndices[currIndex-1] = 1;
				}
			}
		}
		
		for (int ii = 0; ii < n; ii++) {
			if (usedIndices[ii] == 0)
				return false; // system is invalid because it does not contain variable x[ii+1]
		}
		
		return true;
	}
	
	/**
	 * Method that checks whether polynomial system is in triangular form 
	 * and re-orders it so that there is polynomial with only
	 * one variable at first place in system, then polynomial with
	 * only two variables (one from previous polynomial) at second place 
	 * in system, and so on.
	 * System of polynomials is triangular iff each next polynomial
	 * introduces exactly one new x variable.
	 *  
	 * @return	True if system is in triangular form, false otherwise
	 */
	public boolean checkAndReOrderTriangularSystem() {
		// empty system of polynomials is trivially triangular
		if (this.getPolynomials().size() == 0)
			return true;
		
		int n = this.polynomials.size();
		Vector<Integer> polysByNumOfVars = new Vector<Integer>(n+1); // i-th element is index of polynomial with exactly i number of x variables 
		Vector<BitSet> varsInPolys = new Vector<BitSet>(n); 		 // each bit set will be used to keep which variables are used in certain polynomial
		
		// initializations
		for (int ii = 0; ii < n; ii++) {
			polysByNumOfVars.add(new Integer(-1)); // initial value, but will be populated later, except for ii = 0
												   // because none polynomial should have zero variables in itself
			BitSet bs = new BitSet(n); // each bit stands for variable with same index as that bit has in bit set
			bs.clear();
			varsInPolys.add(bs);
		}
		polysByNumOfVars.add(new Integer(-1)); // (n+1)-th i.e. last initial value
		// prepare list of variables introduced by triangular system
		this.variableList = new Vector<Integer>(n);
		
		// pass system of polynomials and check if it is triangular
		for (int ii = 0; ii < n; ii++) {
			int counter = 0; // number of different x variables
			ArrayList<Term> terms = this.polynomials.get(ii).getTermsAsDescList();
			
			for (int jj = 0, size = terms.size(); jj < size; jj++) {
				Vector<Power> powers = terms.get(jj).getPowers();
				
				for (int kk = 0, psize = powers.size(); kk < psize; kk++) {
					int varIndex = (int)powers.get(kk).getIndex() - 1; // for x-polynomial it is safe to cast index to integer
					BitSet bs = varsInPolys.get(ii);
					
					if (!bs.get(varIndex)) { // found new variable for this polynomial
						counter++;
						bs.set(varIndex);
					}
				}
			}
			
			if (counter <= 0 || counter > n || polysByNumOfVars.get(counter).intValue() >= 0)
				return false; // polynomial system is not triangular because
							  // current polynomial has no variables or has
							  // more then n variables (more then a size of the system) 
							  // or has same number of variables like some other polynomial
			
			polysByNumOfVars.set(counter, new Integer(ii));
		}
		
		// after previous loop all polynomials have mutually different number of variables
		
		// variable introduced by single variable polynomial (first polynomial in triangular system)
		this.variableList.add(0, new Integer(varsInPolys.get(polysByNumOfVars.get(1)).nextSetBit(0) + 1));
		
		for (int ii = 1, jj = 2; jj <= n; ii++, jj++) { // loop by number of variables
			BitSet bsi = varsInPolys.get(polysByNumOfVars.get(ii));
			BitSet bsiCopy = new BitSet(n);
			bsiCopy.clear();
			bsiCopy.or(bsi); // bsiCopy is now copy of bsi
			BitSet bsj = varsInPolys.get(polysByNumOfVars.get(jj));
			bsiCopy.and(bsj);
			// bsi and bsj must differ in exactly one bit which means bsj introduced exactly
			// one new variable (and therefore set its bit to true)
			if (!bsi.equals(bsiCopy))
				return false; // next polynomial jj is not introducing exactly one new 
			                  // variable into set of variables used by polynomial ii;
							  // therefore, system is not triangular
			
			// new variable introduced by polynomial jj
			BitSet bsiNewCopy = new BitSet(n);
			bsiNewCopy.clear();
			bsiNewCopy.or(bsi);	 // bsiNewCopy is now copy of bsi
			bsiNewCopy.xor(bsj); // leave only one new bit introduced by bsj
			this.variableList.add(jj - 1, new Integer(bsiNewCopy.nextSetBit(0) + 1));
		}
		
		// system is triangular, then reorganize it
		Vector<XPolynomial> triangularSystem = new Vector<XPolynomial>(n);
		
		for (int ii = 0; ii < n; ii++)
			triangularSystem.add(ii, this.polynomials.get(polysByNumOfVars.get(ii + 1)));
		
		this.polynomials = triangularSystem;
		return true; // system is triangular
	}
	
	/**
	 * Method that performs triangulation over this system, i.e.
	 * transforms this system in triangular form
	 * 
	 * @return	Return code is zero when operation is successfully completed
	 * 			and negative with specific error code, if error happens
	 */
	public int triangulate() {
		StringBuilder sb;
		OGPOutput output = OpenGeoProver.settings.getOutput();
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		// if system is already triangular, only reorder it and exit
		if (this.checkAndReOrderTriangularSystem() == true) {
			try {
				output.writePlainText("The system is already triangular.\n\n");
				output.writePolySystem(this);
			} catch (IOException e) {
				logger.error("Failed to write to output file(s).");
				output.close();
				return OGPConstants.ERR_CODE_GENERAL;
			}
			return OGPConstants.RET_CODE_SUCCESS;
		}
		
		this.variableList = new Vector<Integer>(); // prepare list of variables
		
		Vector<XPolynomial> triangularSystem = new Vector<XPolynomial>(); // final triangular system of polynomials
		Vector<XPolynomial> freeSystem = null;   // system that contains polynomials free of certain variable;
											     // it is also updated in each loop step
		Vector<XPolynomial> nonFreeSystem = null; // system of polynomials to be freed from certain variable;
												  // it is also updated in each loop step
		Vector<XPolynomial> auxSystem = this.polynomials;  // auxiliary system used in each loop step for polynomials
		                                                   // that have to be processed; initially auxiliary system that 
		                                                   // is about to be processed is current system of polynomials
		Vector<XPolynomial> tempSystemForOutput = null;    // vector of polynomials for printing in report file after 
														   // each triangulation step.
		XPolySystem tempPolySystem = null;				   // temporary polynomial system for printing to output
		Vector<Integer> originalIndexes = null;            // vector of original indices from auxiliary system.
		boolean tempSystemChanged = true;
		
		try {
			output.writePlainText("The input system is:\n\n");
			output.writePolySystem(this);
		} catch (IOException e) {
			logger.error("Failed to write to output file(s).");
			output.close();
			return OGPConstants.ERR_CODE_GENERAL;
		}
		
		// main loop for triangulation of polynomial system
		for (int ii = this.polynomials.size(), istep = 1, isize = this.polynomials.size(); ii > 0; ii--, istep++){ // loop through x variables starting from greatest one
			// in this step auxiliary system has to be made free of variable with index ii
			
			try {
				output.openSubSection("Triangulation, step " + istep, true);
				output.openEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
				output.openItemWithDesc("Choosing variable:");
				sb = new StringBuilder();
				sb.append("Trying the variable with index ");
				sb.append(ii);
				sb.append(".\n\n");
				output.closeItemWithDesc(sb.toString());
			} catch (IOException e) {
				logger.error("Failed to write to output file(s).");
				output.close();
				return OGPConstants.ERR_CODE_GENERAL;
			}
			
			
			// first step - pass auxiliary system and split it into two parts:
			// one will be polynomials free of variable x[ii] and another consists
			// of polynomials that contain x[ii]
			freeSystem = new Vector<XPolynomial>();
			nonFreeSystem = new Vector<XPolynomial>();
			originalIndexes = new Vector<Integer>();
			tempSystemChanged = true;
			
			for (int jj = 0, kk = auxSystem.size(); jj < kk; jj++) {
				XPolynomial currXPoly = auxSystem.get(jj);
				int varExp = 0; // exponent of variable x[ii] in terms of current polynomial
				
				ArrayList<Term> termList = currXPoly.getTermsAsDescList();
				boolean allProcessed = false; // indicator whether search for variable x[ii] in terms of
				                              // this polynomial is over
				int numOfTerms = termList.size();
				int counter = 0;
				boolean found = false; // indicator whether variable x[ii] is found in polynomial
				
				while (counter < numOfTerms && !allProcessed && !found) {
					XTerm currTerm = (XTerm)termList.get(counter);
					
					if (currTerm == null) {
						String errMsg = "Found null term";
						logger.error(errMsg);
						try {
							output.openItemWithDesc("Error:");
							output.closeItemWithDesc(errMsg);
						} catch (IOException e) {
							logger.error("Failed to write to output file(s).");
							output.close();
							return OGPConstants.ERR_CODE_GENERAL;
						}
						return OGPConstants.ERR_CODE_NULL;
					}
					varExp = currTerm.getVariableExponent(ii);
					
					if (varExp > 0) // variable found in polynomial
						found = true;
					// if variable not found and greatest variable in term is less
					// than the one that is searched for (ii), that means no need
					// to search for it further
					else if (currTerm.getPowers().size() == 0 || currTerm.getPowers().get(0).getIndex() < ii)
						allProcessed = true;
					
					counter++;
				}
				
				// decide where to store polynomial
				if (found) {
					nonFreeSystem.add((XPolynomial)currXPoly.clone());
					originalIndexes.add(new Integer(jj));
				}
				else
					freeSystem.add((XPolynomial)currXPoly.clone());
			}
			
			// if no polynomial found with variable x[ii] that is error
			if (nonFreeSystem.size() == 0) {
				String errMsg = "Variable with index " + ii + " not found in polynomial system.";
				logger.error(errMsg);
				try {
					output.openItemWithDesc("Error:");
					output.closeItemWithDesc(errMsg);
				} catch (IOException e) {
					logger.error("Failed to write to output file(s).");
					output.close();
					return OGPConstants.ERR_CODE_GENERAL;
				}
				return OGPConstants.ERR_CODE_GENERAL;
			}
			
			try {
				sb = new StringBuilder();
				sb.append("Variable <ind_text><label>x</label><ind>");
				sb.append(ii);
				sb.append("</ind></ind_text> selected:");
				output.openItemWithDesc(sb.toString());
				sb = new StringBuilder();
				sb.append("The number of polynomials with this variable, with indexes from 1 to ");
				sb.append(isize - istep + 1);
				sb.append(", is ");
				sb.append(nonFreeSystem.size());
				sb.append(".\n\n");
				output.closeItemWithDesc(sb.toString());
			} catch (IOException e) {
				logger.error("Failed to write to output file(s).");
				output.close();
				return OGPConstants.ERR_CODE_GENERAL;
			}
			
			// if there is only one polynomial with variable x[ii], choose it for triangular system
			if (nonFreeSystem.size() == 1) {
				triangularSystem.add(0, nonFreeSystem.get(0)); // always add to beginning
				this.variableList.add(0, new Integer(ii));
				auxSystem = freeSystem;
				tempSystemChanged = false;
				try {
					output.openItemWithDesc("Single polynomial with chosen variable:");
					sb = new StringBuilder();
					sb.append("Chosen polynomial is <ind_text><label>p</label><ind>");
					sb.append(originalIndexes.get(0).intValue() + 1);
					sb.append("</ind></ind_text>. No reduction needed.\n\n");
					output.closeItemWithDesc(sb.toString());
					output.writeEnumItem("The triangular system has not been changed.\n\n");
				} catch (IOException e) {
					logger.error("Failed to write to output file(s).");
					output.close();
					return OGPConstants.ERR_CODE_GENERAL;
				}
			}
			else { // more than 1 polynomial with variable x[ii]
				boolean end = false;
				
				do { // remove x[ii] variable from notFreeSystem of polynomials
					 // find two polynomials with smallest exponent of variable x[ii]
					int first = 0, second = 1;
					int exp1 = nonFreeSystem.get(first).getLeadingExp(ii), 
						exp2 = nonFreeSystem.get(second).getLeadingExp(ii);
					int min1, min2, count1 = 1, count2 = 1;
					
					if (exp1 == 0 || exp2 == 0) {
						String errMsg = "Variable not found when expected to be found.";
						logger.error(errMsg);
						try {
							output.openItemWithDesc("Error:");
							output.closeItemWithDesc(errMsg);
						} catch (IOException e) {
							logger.error("Failed to write to output file(s).");
							output.close();
							return OGPConstants.ERR_CODE_GENERAL;
						}
						return OGPConstants.ERR_CODE_GENERAL;
					}
					// first is for smallest exponent
					if (exp1 <= exp2) {
						min1 = exp1;
						min2 = exp2;
					}
					else {
						first = 1;
						second = 0;
						min1 = exp2;
						min2 = exp1;
					}
					
					for (int ll = 2, mm = nonFreeSystem.size(); ll < mm; ll++) {
						int currExp = nonFreeSystem.get(ll).getLeadingExp(ii);
						
						if (currExp == 0) {
							String errMsg = "Variable not found when expected to be found.";
							logger.error(errMsg);
							try {
								output.openItemWithDesc("Error:");
								output.closeItemWithDesc(errMsg);
							} catch (IOException e) {
								logger.error("Failed to write to output file(s).");
								output.close();
								return OGPConstants.ERR_CODE_GENERAL;
							}
							return OGPConstants.ERR_CODE_GENERAL;
						} 
						if (currExp < min1) {
							first = ll;
							min1 = currExp;
							count1 = 1;
						}
						else if (currExp == min1) {
							count1++;
						}
						else if (currExp < min2) {
							second = ll;
							min2 = currExp;
							count2 = 1;
						}
						else if (currExp == min2) {
							count2++;
						}
					}
					
					try {
						output.openItemWithDesc("Minimal degrees:");
						sb = new StringBuilder();
						if (min1 < min2) {
							sb.append(count1);
							sb.append(" polynomial(s) with degree ");
							sb.append(min1);
							sb.append(" and ");
							sb.append(count2);
							sb.append(" polynomial(s) with degree ");
							sb.append(min2);
						}
						else if (min1 == min2) {
							sb.append(count1 + count2);
							sb.append(" polynomial(s) with degree ");
							sb.append(min1);
						}
						sb.append(".\n\n");
						output.closeItemWithDesc(sb.toString());
					} catch (IOException e) {
						logger.error("Failed to write to output file(s).");
						output.close();
						return OGPConstants.ERR_CODE_GENERAL;
					}
					
					// if minimal exponent is 1, choose that polynomial for triangular
					// system and all the rest will be cleaned of variable x[ii] by
					// pseudo reminder operation
					if (min1 == 1) {
						try {
							output.openItemWithDesc("Polynomial with linear degree:");
							sb = new StringBuilder();
							sb.append("Removing variable <ind_text><label>x</label><ind>");
							sb.append(ii);
							sb.append("</ind></ind_text> from all other polynomials by reducing them with polynomial <ind_text><label>p</label><ind>");
							sb.append(originalIndexes.get(first).intValue() + 1);
							sb.append("</ind></ind_text> from previous step.\n\n");
							output.closeItemWithDesc(sb.toString());
						} catch (IOException e) {
							logger.error("Failed to write to output file(s).");
							output.close();
							return OGPConstants.ERR_CODE_GENERAL;
						}
						
						XPolynomial currPoly = nonFreeSystem.get(first);
						triangularSystem.add(0, currPoly); // always add to beginning
						this.variableList.add(0, new Integer(ii));
						nonFreeSystem.remove(first);
						for (int ll = 0, mm = nonFreeSystem.size(); ll < mm; ll++) {
							XPolynomial tempXP = nonFreeSystem.get(ll).pseudoReminder(currPoly, ii);
							
							if (tempXP == null)
								return OpenGeoProver.settings.getRetCodeOfPseudoDivision();
							
							int numOfTerms = tempXP.getTerms().size();
							
							if (numOfTerms > OpenGeoProver.settings.getParameters().getSpaceLimit()) {
								String errMsg = "Polynomial exceeds maximal allowed number of terms.";
								logger.error(errMsg);
								try {
									output.openItemWithDesc("Error:");
									output.closeItemWithDesc(errMsg);
								} catch (IOException e) {
									logger.error("Failed to write to output file(s).");
									output.close();
									return OGPConstants.ERR_CODE_GENERAL;
								}
								return OGPConstants.ERR_CODE_SPACE;
							}
							if (numOfTerms > OpenGeoProver.settings.getMaxNumOfTerms()) {
								OpenGeoProver.settings.setMaxNumOfTerms(numOfTerms);
							}
							if (OpenGeoProver.settings.getTimer().isTimeIsUp()) {
								String errMsg = "Prover execution time has been expired.";
								logger.error(errMsg);
								try {
									output.openItemWithDesc("Error:");
									output.closeItemWithDesc(errMsg);
								} catch (IOException e) {
									logger.error("Failed to write to output file(s).");
									output.close();
									return OGPConstants.ERR_CODE_GENERAL;
								}
								return OGPConstants.ERR_CODE_TIME;
							}
							freeSystem.add(tempXP);
						}
						auxSystem = freeSystem; // prepare for the next step
						end = true;
					}
					else {
						// reduce two chosen polynomials
						XPolynomial r2 = nonFreeSystem.get(second);
						XPolynomial r1 = nonFreeSystem.get(first);
						int leadExp = 0;
						
						try {
							output.openItemWithDesc("No linear degree polynomials:");
							sb = new StringBuilder();
							sb.append("Reducing polynomial <ind_text><label>p</label><ind>");
							sb.append(second + 1);
							sb.append("</ind></ind_text> (of degree ");
							sb.append(min2);
							sb.append(") with <ind_text><label>p</label><ind>");
							sb.append(first + 1);
							sb.append("</ind></ind_text> (of degree ");
							sb.append(min1);
							sb.append(").\n\n");
							output.closeItemWithDesc(sb.toString());
						} catch (IOException e) {
							logger.error("Failed to write to output file(s).");
							output.close();
							return OGPConstants.ERR_CODE_GENERAL;
						}
						
						do {
							XPolynomial temp = r2.pseudoReminder(r1, ii);
							
							if (temp == null)
								return OpenGeoProver.settings.getRetCodeOfPseudoDivision();
							
							int numOfTerms = temp.getTerms().size();
							
							if (numOfTerms > OpenGeoProver.settings.getParameters().getSpaceLimit()) {
								String errMsg = "Polynomial exceeds maximal allowed number of terms.";
								logger.error(errMsg);
								try {
									output.openItemWithDesc("Error:");
									output.closeItemWithDesc(errMsg);
								} catch (IOException e) {
									logger.error("Failed to write to output file(s).");
									output.close();
									return OGPConstants.ERR_CODE_GENERAL;
								}
								return OGPConstants.ERR_CODE_SPACE;
							}
							if (numOfTerms > OpenGeoProver.settings.getMaxNumOfTerms()) {
								OpenGeoProver.settings.setMaxNumOfTerms(numOfTerms);
							}
							if (OpenGeoProver.settings.getTimer().isTimeIsUp()) {
								String errMsg = "Prover execution time has been expired.";
								logger.error(errMsg);
								try {
									output.openItemWithDesc("Error:");
									output.closeItemWithDesc(errMsg);
								} catch (IOException e) {
									logger.error("Failed to write to output file(s).");
									output.close();
									return OGPConstants.ERR_CODE_GENERAL;
								}
								return OGPConstants.ERR_CODE_TIME;
							}
							
							r2 = r1;
							r1 = temp;
							
							if (r1.isZero()) { // two chosen polynomials have common factor - this is treated as error
								String errMsg = "Two polynomials have common factor.";
								logger.error(errMsg);
								try {
									output.openItemWithDesc("Error:");
									output.closeItemWithDesc(errMsg);
								} catch (IOException e) {
									logger.error("Failed to write to output file(s).");
									output.close();
									return OGPConstants.ERR_CODE_GENERAL;
								}
								return OGPConstants.ERR_CODE_GENERAL;
							}
							
							leadExp = r1.getLeadingExp(ii);
						} while (leadExp > 1);
						
						// at the end notFreeSystem(second) contains r2 and
						// notFreeSystem(first) contains r1
						
						//update references in notFreeSystem
						nonFreeSystem.set(first, r1);
						nonFreeSystem.set(second, r2);
						
						// if r1 doesn't contain variable x[ii], add it to free polynomials
						if (leadExp == 0) {
							freeSystem.add(r1);
							nonFreeSystem.remove(first);
							if (nonFreeSystem.size() == 1) { // only one polynomial has left
								// add it into triangular system
								triangularSystem.add(0, r2); // always add to beginning
								this.variableList.add(0, new Integer(ii));
								auxSystem = freeSystem; // prepare for the next step
								end = true;
							}
							// else, back to beginning to find other two polynomials that contain x[ii]
						}
						else  { // leadExp == 1
							// add it into triangular system, and divide all other polynomials
							triangularSystem.add(0, r1); // always add to beginning
							this.variableList.add(0, new Integer(ii));
							nonFreeSystem.remove(first);
							for (int ll = 0, mm = nonFreeSystem.size(); ll < mm; ll++) {
								XPolynomial tempXP = nonFreeSystem.get(ll).pseudoReminder(r1, ii);
								
								if (tempXP == null)
									return OpenGeoProver.settings.getRetCodeOfPseudoDivision();
								
								int numOfTerms = tempXP.getTerms().size();
								
								if (numOfTerms > OpenGeoProver.settings.getParameters().getSpaceLimit()) {
									String errMsg = "Polynomial exceeds maximal allowed number of terms.";
									logger.error(errMsg);
									try {
										output.openItemWithDesc("Error:");
										output.closeItemWithDesc(errMsg);
									} catch (IOException e) {
										logger.error("Failed to write to output file(s).");
										output.close();
										return OGPConstants.ERR_CODE_GENERAL;
									}
									return OGPConstants.ERR_CODE_SPACE;
								}
								if (numOfTerms > OpenGeoProver.settings.getMaxNumOfTerms()) {
									OpenGeoProver.settings.setMaxNumOfTerms(numOfTerms);
								}
								if (OpenGeoProver.settings.getTimer().isTimeIsUp()) {
									String errMsg = "Prover execution time has been expired.";
									logger.error(errMsg);
									try {
										output.openItemWithDesc("Error:");
										output.closeItemWithDesc(errMsg);
									} catch (IOException e) {
										logger.error("Failed to write to output file(s).");
										output.close();
										return OGPConstants.ERR_CODE_GENERAL;
									}
									return OGPConstants.ERR_CODE_TIME;
								}
								freeSystem.add(tempXP);
							}
							auxSystem = freeSystem; // prepare for the next step
							end = true;
						}
					}
				} while (!end);
				
				// when exit the loop, no more polynomials with x[ii] and everything
				// has been prepared for the next step (variable x[ii+1])
			}
			
			tempSystemForOutput = new Vector<XPolynomial>();
			for (XPolynomial xp: auxSystem)
				tempSystemForOutput.add(xp);
			for (XPolynomial xp: triangularSystem)
				tempSystemForOutput.add(xp);
			tempPolySystem = new XPolySystem();
			tempPolySystem.setPolynomials(tempSystemForOutput);
			
			try {
				output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
				if (tempSystemChanged) {
					output.writePlainText("Finished a triangulation step, the current system is:\n\n");
					output.writePolySystem(tempPolySystem);
				}
				output.closeSubSection();
			} catch (IOException e) {
				logger.error("Failed to write to output file(s).");
				output.close();
				return OGPConstants.ERR_CODE_GENERAL;
			}
		}
		
		// at the end, set new system to triangular system
		this.polynomials = triangularSystem;
		
		try {
			output.writePlainText("\n\nThe triangular system is:\n\n");
			output.writePolySystem(this);
		} catch (IOException e) {
			logger.error("Failed to write to output file(s).");
			output.close();
			return OGPConstants.ERR_CODE_GENERAL;
		}
		
		return OGPConstants.RET_CODE_SUCCESS;
	}
	
	/**
	 * Method which examines whether this polynomial system is linear;
	 * i.e. if power of each dependent variable in each term of each
	 * polynomial of system is not greater than 1.
	 * 
	 * @return		True if system is linear, false otherwise
	 */
	public boolean isSystemLinear() {
		for (XPolynomial xp : this.polynomials) {
			for (Term xt : xp.getTermsAsDescList()) {
				for (Power pow : xt.getPowers()) {
					if (pow.getExponent() > 1)
						return false;
				}
			}
		}
		
		return true;
	}
}
