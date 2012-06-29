/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.ogprover.main.OGPConstants;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.*;
import com.ogprover.pp.tp.auxiliary.*;
import com.ogprover.pp.tp.geoconstruction.*;
import com.ogprover.pp.tp.ndgcondition.AlgebraicNDGCondition;
import com.ogprover.pp.tp.ndgcondition.SimpleNDGCondition;
import com.ogprover.pp.tp.thmstatement.*;
import com.ogprover.utilities.io.OGPOutput;
import com.ogprover.utilities.io.SpecialFileFormatting;
import com.ogprover.utilities.logger.ILogger;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for theorem protocol - collection of 
 *     construction steps and theorem statement</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */
public class OGPTP {
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
	 * Name of geometry theorem
	 */
	private String theoremName = null;
	/**
	 * Array of construction steps
	 */
	private Vector<GeoConstruction> constructionSteps = null;
	/**
	 * Map of construction steps - for easy search by label of 
	 * constructed geometric object
	 */
	private Map<String, GeoConstruction> constructionMap = null;
	/**
	 * Theorem statement about objects constructed by 
	 * construction steps of this Construction Protocol 
	 */
	private ThmStatement theoremStatement = null;
	/**
	 * List of NDG conditions associated with algebraic prover for this theorem.
	 */
	private Vector<AlgebraicNDGCondition> algebraicNDGConditions = null;
	/**
	 * List of NDG conditions associated with the area method prover for this theorem.
	 */
	private Vector<SimpleNDGCondition> simpleNDGConditions = null;
	/**
	 * Geometry theorem from this Construction Protocol in algebraic form
	 */
	private GeoTheorem algebraicGeoTheorem = new GeoTheorem(); // empty theorem
	/**
	 * List with all combinations of points with zero for some coordinate
	 */
	private Vector<Vector<Point>> zeroPoints = null;
	/* 
	 * Members necessary for instantiation of points in algebraic form - BEGIN
	 */
	/**
	 * Next index of u-variable
	 */
	private int uIndex = 1;
	/**
	 * Next index of x-variable
	 */
	private int xIndex = 1;
	/**
	 * Number of zero indices used instead of u indices when instantiating points.
	 */
	private int numZeroIndices = 0;
	/**
	 * Flag that determines whether CP contains some free parametric set or not
	 */
	private boolean hasFreeParametricSet = false;
	/* 
	 * Members necessary for instantiation of points in algebraic form - END
	 */
	

	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * Method that sets theorem name
	 * 
	 * @param theoremName The name of theorem to set
	 */
	public void setTheoremName(String theoremName) {
		this.theoremName = theoremName;
		if (this.algebraicGeoTheorem != null)
			this.algebraicGeoTheorem.setName(theoremName);
	}
	
	/**
	 * Method that retrieves theorem name
	 * 
	 * @return The theorem name
	 */
	public String getTheoremName() {
		return theoremName;
	}	

	/**
	 * Method that sets array of construction steps
	 * 
	 * @param constructionSteps The construction steps to set
	 */
	public void setConstructionSteps(Vector<GeoConstruction> constructionSteps) {
		this.constructionSteps = constructionSteps;
	}

	/**
	 * Method that retrieves the array of construction steps
	 * 
	 * @return The construction steps
	 */
	public Vector<GeoConstruction> getConstructionSteps() {
		return constructionSteps;
	}

	/**
	 * Method that sets the map of construction steps
	 * 
	 * @param constructionMap The map to set
	 */
	public void setConstructionMap(Map<String, GeoConstruction> constructionMap) {
		this.constructionMap = constructionMap;
	}
	
	/**
	 * Method that retrieves the map of construction steps
	 * 
	 * @return The map of construction steps
	 */
	public Map<String, GeoConstruction> getConstructionMap() {
		return constructionMap;
	}

	/**
	 * Method that sets theorem statement
	 * 
	 * @param theoremStatement The theorem statement to set
	 */
	public void setTheoremStatement(ThmStatement theoremStatement) {
		this.theoremStatement = theoremStatement;
	}

	/**
	 * Method that retrieves theorem statement
	 * 
	 * @return The theorem statement
	 */
	public ThmStatement getTheoremStatement() {
		return theoremStatement;
	}
	
	/**
	 * @param ndgConditions the ndgConditions to set
	 */
	public void setAlgebraicNDGConditions(Vector<AlgebraicNDGCondition> ndgConditions) {
		this.algebraicNDGConditions = ndgConditions;
	}

	/**
	 * @return the ndgConditions
	 */
	public Vector<AlgebraicNDGCondition> getAlgebraicNDGConditions() {
		return algebraicNDGConditions;
	}
	
	/**
	 * @param ndgConditions the ndgConditions to set
	 */
	public void setSimpleNDGConditions(Vector<SimpleNDGCondition> ndgConditions) {
		this.simpleNDGConditions = ndgConditions;
	}

	/**
	 * @return the ndgConditions
	 */
	public Vector<SimpleNDGCondition> getSimpleNDGConditions() {
		return simpleNDGConditions;
	}
	/**
	 * Method that sets the algebraic form of geometry theorem
	 * 
	 * @param algebraicGeoTheorem The theorem in algebraic form to set
	 */
	public void setAlgebraicGeoTheorem(GeoTheorem algebraicGeoTheorem) {
		this.algebraicGeoTheorem = algebraicGeoTheorem;
	}
	
	/**
	 * Method that retrieves the algebraic form of geometry theorem
	 * 
	 * @return The theorem in algebraic form
	 */
	public GeoTheorem getAlgebraicGeoTheorem() {
		return algebraicGeoTheorem;
	}

	/**
	 * @return the zeroPoints
	 */
	public Vector<Vector<Point>> getZeroPoints() {
		if (this.zeroPoints == null)
			this.populateZeroPoints();
		
		return this.zeroPoints;
	}
	
	/**
	 * @param uIndex the uIndex to set
	 */
	public void setUIndex(int uIndex) {
		this.uIndex = uIndex;
	}
	
	/**
	 * @return the uIndex
	 */
	public int getUIndex() {
		return uIndex;
	}

	/**
	 * @param xIndex the xIndex to set
	 */
	public void setXIndex(int xIndex) {
		this.xIndex = xIndex;
	}

	/**
	 * @return the xIndex
	 */
	public int getXIndex() {
		return xIndex;
	}
	
	/**
	 * @param numZeroIndices the numZeroIndices to set
	 */
	public void setNumZeroIndices(int numZeroIndices) {
		this.numZeroIndices = numZeroIndices;
	}

	/**
	 * @return the numZeroIndices
	 */
	public int getNumZeroIndices() {
		return numZeroIndices;
	}
	
	/**
	 * @param hasFreeParametricSet the hasFreeParametricSet to set
	 */
	public void setHasFreeParametricSet(boolean hasFreeParametricSet) {
		this.hasFreeParametricSet = hasFreeParametricSet;
	}

	/**
	 * @return the hasFreeParametricSet
	 */
	public boolean isHasFreeParametricSet() {
		return hasFreeParametricSet;
	}
	
	

	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Default constructor method that creates empty Construction Protocol object
	 */
	public OGPTP() {
		this.constructionSteps = new Vector<GeoConstruction>();
		this.constructionMap = new HashMap<String, GeoConstruction>();
		this.theoremStatement = null;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method for clearing of this CP.
	 */
	public void clear() {
		this.constructionSteps = new Vector<GeoConstruction>();
		this.constructionMap = new HashMap<String, GeoConstruction>();
		this.theoremStatement = null;
		this.theoremName = null;
		this.algebraicNDGConditions = null;
		this.algebraicGeoTheorem = new GeoTheorem();
		this.zeroPoints = null;
		this.uIndex = 1;
		this.xIndex = 1;
		this.numZeroIndices = 0;
		this.hasFreeParametricSet = false;
	}
	
	/**
	 * Method for adding one construction step to this theorem protocol at the end
	 * 
	 * @param gc	Geometry construction to add into this protocol
	 */
	public void addGeoConstruction(GeoConstruction gc) {
		if (gc == null) {
			OpenGeoProver.settings.getLogger().warn("Attempt to add null object to theorem protocol");
			return; // skip null objects
		}
		
		if (this.constructionSteps == null) {
			this.constructionSteps = new Vector<GeoConstruction>();
			this.constructionMap = new HashMap<String, GeoConstruction>();
		}
		
		// We make difference between plain and shortcut constructions
		if (gc instanceof ShortcutConstruction) {
			// add each single construction to the end of CP
			for (GeoConstruction singleCons : ((ShortcutConstruction)gc).getShortcutListOfConstructions()) {
				this.constructionSteps.add(singleCons); // add construction to the end of list of constructions
				this.constructionMap.put(singleCons.getGeoObjectLabel(), singleCons);
				singleCons.setConsProtocol(this);
				singleCons.setIndex(this.constructionSteps.size() - 1);
			}
		}
		else { // add this construction as single object
			this.constructionSteps.add(gc); // add construction to the end of list of constructions
			this.constructionMap.put(gc.getGeoObjectLabel(), gc);
			gc.setConsProtocol(this);
			gc.setIndex(this.constructionSteps.size() - 1);
			
			if (gc instanceof FreeParametricSet) {
				if (!this.hasFreeParametricSet) { // this is first free parametric set and therefore will contain the origin
					((FreeParametricSet)gc).setContainsOrigin(true);
					this.hasFreeParametricSet = true;
				}
				else
					((FreeParametricSet)gc).setContainsOrigin(false);
			}
		}
	}
	
	/**
	 * Method for adding one construction step to this theorem protocol 
	 * at the specified position
	 * 
	 * @param index	Index in theorem protocol where to put new construction
	 * @param gc	Geometry construction to add into this protocol
	 */
	public void addGeoConstruction(int index, GeoConstruction gc) {
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (gc == null) {
			logger.warn("Attempt to add null object to theorem protocol");
			return; // skip null objects
		}
		
		if (index < 0 || index > this.getConstructionSteps().size()) {
			logger.error("Bad index to put object in theorem protocol - index out of boundaries");
			return;
		}
		
		if (this.constructionSteps == null) {
			this.constructionSteps = new Vector<GeoConstruction>();
			this.constructionMap = new HashMap<String, GeoConstruction>();
		}
		
		// We make difference between plain and shortcut constructions
		if (gc instanceof ShortcutConstruction) {
			ShortcutConstruction scCons = (ShortcutConstruction)gc;
			
			// Shift indices of all constructed objects from specified index till the end
			for (int ii = index, jj = this.constructionSteps.size(), kk = ii + scCons.getShortcutListOfConstructions().size(); ii < jj; ii = kk, kk++)
				this.constructionSteps.get(ii).setIndex(kk);
			
			// Now add each single construction to CP
			int currIndex = index;
			for (GeoConstruction currGC : scCons.getShortcutListOfConstructions()) {
				this.constructionSteps.add(currIndex, currGC); // add construction to the specified place - old elements
			                                                   // starting from position "index" till the end will be 
		                                                       // shifted to the right for one place
				this.constructionMap.put(currGC.getGeoObjectLabel(), currGC);
				currGC.setConsProtocol(this);
				currGC.setIndex(currIndex);
				currIndex++;
			}
		}
		else { // add this construction as single object
			// Shift indices of all constructed objects from specified index till the end
			for (int ii = index, jj = this.constructionSteps.size(), kk = ii + 1; ii < jj; ii = kk, kk++)
				this.constructionSteps.get(ii).setIndex(kk);
			this.constructionSteps.add(index, gc); // add construction to the specified place - old elements
			                                       // starting from position "index" till the end will be 
		                                           // shifted to the right for one place
			this.constructionMap.put(gc.getGeoObjectLabel(), gc);
			gc.setConsProtocol(this);
			gc.setIndex(index);
			
			if (gc instanceof FreeParametricSet) {
				if (!this.hasFreeParametricSet) { // this is first free parametric set and therefore will contain the origin
					((FreeParametricSet)gc).setContainsOrigin(true);
					this.hasFreeParametricSet = true;
				}
				else
					((FreeParametricSet)gc).setContainsOrigin(false);
			}
		}
	}
	
	/**
	 * Method which removes the construction from theorem protocol.
	 * 
	 * @param gc	The construction to be removed. Note: the construction from
	 * 				CP has to be provided here, not a copy of it, since with 
	 * 				non-overridden equals() it won't be found in CP.
	 */
	public void removeGeoConstruction(GeoConstruction gc) {
		if (gc == null || (gc instanceof ShortcutConstruction))
			return;
		
		int consInd = this.constructionSteps.indexOf(gc); // uses equals()
		this.constructionSteps.remove(consInd); // this also shifts all following objects to the left for one place
		this.constructionMap.remove(gc.getGeoObjectLabel());
		gc.setConsProtocol(null);
		gc.setIndex(-1);
		
		if (gc instanceof IntersectionPoint) {
			IntersectionPoint pt = (IntersectionPoint)gc;
			SetOfPoints set1 = pt.getFirstPointSet();
			SetOfPoints set2 = pt.getSecondPointSet();
			int idx1 = set1.getPoints().indexOf(pt);
			int idx2 = set2.getPoints().indexOf(pt);
			
			if (idx1 != -1)
				set1.getPoints().remove(idx1);
			if (idx2 != -1)
				set2.getPoints().remove(idx2);
		}
		else if (gc instanceof RandomPointFromSetOfPoints) {
			RandomPointFromSetOfPoints pt = (RandomPointFromSetOfPoints)gc;
			SetOfPoints set = pt.getBaseSetOfPoints();
			int idx = set.getPoints().indexOf(pt);
			
			if (idx != -1)
				set.getPoints().remove(idx);
		}
		
		// Shift indices of all constructed objects from object's index till the end
		for (int ii = consInd, jj = this.constructionSteps.size(); ii < jj; ii++)
			this.constructionSteps.get(ii).setIndex(ii);
	}
	
	/**
	 * Method to add theorem statement to theorem protocol.
	 * 
	 * @param statement	Theorem statement to add to this theorem protocol.
	 */
	public void addThmStatement(ThmStatement statement) {
		if (statement == null) {
			OpenGeoProver.settings.getLogger().error("Attempt to add null theorem statement to theorem protocol.");
			return;
		}
		
		this.theoremStatement = statement;
		statement.setConsProtocol(this);
		
		// for compound statements - assign CP to each single statement
		if (statement instanceof CompoundThmStatement) {
			for (ThmStatement singleStatement : ((CompoundThmStatement)statement).getParticleThmStatements())
				singleStatement.setConsProtocol(this);
		}
	}
	
	/**
	 * Method to add NDG condition to theorem protocol.
	 * 
	 * @param ndgCond	NDG condition to add to this theorem protocol.
	 */
	public void addAlgebraicNDGCondition(AlgebraicNDGCondition ndgCond) {
		if (ndgCond == null) {
			OpenGeoProver.settings.getLogger().error("Attempt to add null NDG condition to theorem protocol.");
			return;
		}
		
		if (this.algebraicNDGConditions == null)
			this.algebraicNDGConditions = new Vector<AlgebraicNDGCondition>();
		
		this.algebraicNDGConditions.add(ndgCond);
		ndgCond.setConsProtocol(this);
	}
	
	/**
	 * Method to add a simple NDG condition to theorem protocol.
	 *
	 * @param ndgCond	NDG condition to add to this theorem p
	 */
	public void addSimpleNDGCondition(SimpleNDGCondition ndgCond) {
		if (ndgCond == null) {
			OpenGeoProver.settings.getLogger().error("Attempt to add null NDG condition to theorem protocol.");
			return;
		}
		
		if (this.simpleNDGConditions == null)
			this.simpleNDGConditions = new Vector<SimpleNDGCondition>();
		
		this.simpleNDGConditions.add(ndgCond);
	}
	
	/**
	 * Method that checks the validity of this theorem protocol.
	 * Construction Protocol is considered valid if all points can be
	 * constructed from all other previously constructed points of that 
	 * protocol. In short, each construction step must be valid.
	 * Also, theorem statement must be only about constructed objects.
	 * 
	 * @return	True if this protocol is valid, false otherwise
	 */
	public boolean isValid() {
		OGPOutput output = OpenGeoProver.settings.getOutput();
		ILogger logger = OpenGeoProver.settings.getLogger();
		boolean valid = true;
		
		try {
			output.openSection("Validation of Construction Protocol");
			
			output.openSubSection("Construction steps: ", false);
			output.openEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
			for (GeoConstruction geoCons : this.constructionSteps) {
				output.writeEnumItem(geoCons.getConstructionDesc());
			}
			output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
			output.closeSubSection();
			
			output.openSubSection("Theorem statement: ", false);
			output.openEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
			output.writeEnumItem(this.theoremStatement.getStatementDesc());
			output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
			output.closeSubSection();
			
			output.openEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
			
			// Empty Construction Protocol is valid so don't check number of constructions
			// Check if statement is set
			if (this.theoremStatement == null) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("There is no theorem statement");
				valid = false;
			}
			
			if (this.constructionSteps.size() > 0) {
				if (valid) {
					// Check if there are objects with same labels:
					// each object must appear in map of this CP exactly once,
					// therefore, if mapping object is null or not that object
					// that's an error.
					for (GeoConstruction geoCons : this.constructionSteps) {
						String label = geoCons.getGeoObjectLabel();
						GeoConstruction mapCons = this.constructionMap.get(label);
					
						if (mapCons == null || mapCons != geoCons) {
							output.openItemWithDesc("Error: ");
							if (mapCons == null)
								output.closeItemWithDesc("Object with name " + label + " not correctly mapped in this protocol with its name");
							else
								output.closeItemWithDesc("Name " + label + " is used more than once for definition of new constructed object");
							valid = false;
							break;
						}
					}
				}
		
				if (valid) {
					// Check the validity of each construction step
					GeoConstruction currGeoCons = this.constructionSteps.get(0); // first construction from list
		
					while (currGeoCons != null && (valid = currGeoCons.isValidConstructionStep())) {
						int nextIndex = currGeoCons.getIndex() + 1;
					
						if (nextIndex < this.constructionSteps.size())
							currGeoCons = this.constructionSteps.get(nextIndex);	// get next construction step for validation this way
																					// because isValidConstructionStep() method can add new constructions to list
						else
							currGeoCons = null;
					}
				}
			}
			
			if (this.theoremStatement.isValid() == false) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Theorem statement is not valid");
				valid = false;
			}
			
			output.openItemWithDesc("Validation result: ");
			if (valid)
				output.closeItemWithDesc("Theorem protocol is valid.");
			else
				output.closeItemWithDesc("Theorem protocol is not valid - cannot proceed.");
			
			output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
			output.closeSection();
		} catch (IOException e) {
			logger.error("Failed to write to output file(s).");
			output.close();
			return false;
		}
		
		return valid;
	}
	
	/**
	 * Method that resets indices necessary for transformation of 
	 * geometry points to their algebraic form (x and u variables).
	 */
	public void resetIndices() {
		this.uIndex = 1;
		this.xIndex = 1;
		this.numZeroIndices = 0;
		this.hasFreeParametricSet = false;
	}
	
	/**
	 * Method that decreases the value of index for dependent variables by one;
	 * it is used when it is decided to rename some recently introduced 
	 * dependent variable so its index could be reused for the next
	 * dependent variable that is going to be introduced when instantiating
	 * some point.
	 */
	public void decrementXIndex() {
		this.xIndex--;
	}
	
	/**
	 * Method that increases the value of index for dependent variables by one;
	 * it is used when it is decided to undo renaming of some recently introduced 
	 * dependent variable.
	 */
	public void incrementXIndex() {
		this.xIndex++;
	}
	
	/**
	 * Method that decreases the value of index for independent variables by one;
	 * it is used when it is decided to re-instantiate some recently introduced 
	 * half-dependent point so same variable indices could be used.
	 */
	public void decrementUIndex() {
		this.uIndex--;
	}
	
	/**
	 * Method that increases the value of index for independent variables by one;
	 * it is used when it is decided to undo re-instantiation of some recently introduced 
	 * half-dependent point.
	 */
	public void incrementUIndex() {
		this.uIndex++;
	}
	
	private void setNumberOfZeroCoordinates() {
		// If there is a free parametric set of points, take the first
		// and set necessary number of zero coordinates; otherwise it will be 3
		// (first free point will be instantiated as (0,0) next free point will be
		// instantiated as (0,u1)).
		if (this.hasFreeParametricSet) {
			for (GeoConstruction geoCons : this.constructionSteps) {
				if (geoCons instanceof FreeParametricSet) {
					this.numZeroIndices = ((FreeParametricSet)geoCons).getNumberOfZeroCoordinates();
					return;
				}
			}
		}
		
		this.numZeroIndices = 3;
		return;
	}
	
	/**
	 * Method for conversion of this theorem protocol
	 * to algebraic form, used for algebraic prover methods
	 * 
	 * @return	SUCCESS on successful execution or general error otherwise
	 */
	public int convertToAlgebraicForm() {
		OGPOutput output = OpenGeoProver.settings.getOutput();
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		// First of all, set the number of zero coordinates used for instantiation of points
		this.setNumberOfZeroCoordinates();
		
		// Remove unnecessary geometry objects
		this.simplify();
		
		try {
			//output.openSection("Transformation of Construction Protocol to algebraic form");
			
			output.openSubSection("Transformation of Construction steps", false);
			for (GeoConstruction geoCons : this.constructionSteps) {
				if (geoCons instanceof Point) {
					int iRet = ((Point) geoCons).transformToAlgebraicForm();
					
					if (iRet != OGPConstants.RET_CODE_SUCCESS) {
						output.openEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
						output.openItemWithDesc("Error: ");
						output.closeItemWithDesc("Prover failed to transform construction of point " + geoCons.getGeoObjectLabel() + " to algebraic form.");
						return OGPConstants.ERR_CODE_GENERAL;
					}
				}
				else if (geoCons instanceof ParametricSet) {
					int iRet = ((ParametricSet) geoCons).transformToAlgebraicForm();
					
					if (iRet != OGPConstants.RET_CODE_SUCCESS) {
						output.openEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
						output.openItemWithDesc("Error: ");
						output.closeItemWithDesc("Prover failed to transform parametric set of points " + geoCons.getGeoObjectLabel() + " to algebraic form.");
						return OGPConstants.ERR_CODE_GENERAL;
					}
				}
				else if (geoCons instanceof SpecialConstantAngle) {
					int iRet = ((SpecialConstantAngle) geoCons).transformToAlgebraicForm();
					
					if (iRet != OGPConstants.RET_CODE_SUCCESS) {
						output.openEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
						output.openItemWithDesc("Error: ");
						output.closeItemWithDesc("Prover failed to transform special constant angle " + geoCons.getGeoObjectLabel() + " to algebraic form.");
						return OGPConstants.ERR_CODE_GENERAL;
					}
				}
			}
			output.closeSubSection();
			
			output.openSubSection("Transformation of Theorem statement", false);
			int iRet = this.theoremStatement.transformToAlgebraicForm();
			if (iRet != OGPConstants.RET_CODE_SUCCESS) {
				output.openEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Prover failed to transform theorem statement to algebraic form.");
				return OGPConstants.ERR_CODE_GENERAL;
			}
			output.closeSubSection();
			
			//output.closeSection();
		} catch (IOException e) {
			logger.error("Failed to write to output file(s).");
			output.close();
			return OGPConstants.ERR_CODE_GENERAL;
		}
		
		return OGPConstants.RET_CODE_SUCCESS;
	}
	
	/**
	 * Method which simplifies CP by removing all constructions that are not necessary for theorem statement.
	 */
	public void simplify() {
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.theoremStatement == null || this.constructionMap == null)
			return;
		
		Map<String, String> usedLabelsMap = new HashMap<String, String>();
		Vector<String> usedLabelsList = new Vector<String>();
		
		// labels from statement
		String[] statementInputLabels = this.theoremStatement.getInputLabels();
		if (statementInputLabels == null) {
			logger.warn("Statement doesn't have input arguments");
			return;
		}
		for (String label : statementInputLabels) {
			if (usedLabelsMap.get(label) == null) {
				usedLabelsMap.put(label, label);
				usedLabelsList.add(label);
			}
		}
		
		// labels from constructions
		for (int ii = 0; ii < usedLabelsList.size(); ii++) {
			String[] consLabels = this.constructionMap.get(usedLabelsList.get(ii)).getInputLabels();
			
			if (consLabels != null) {
				for (String label : consLabels) {
					if (usedLabelsMap.get(label) == null) {
						usedLabelsMap.put(label, label);
						usedLabelsList.add(label);
					}
				}
			}
 		}
		
		// remove unnecessary constructions
		for (int ii = 0; ii < this.constructionSteps.size(); ii++) {
			GeoConstruction gc = this.constructionSteps.get(ii);
			if (usedLabelsMap.get(gc.getGeoObjectLabel()) == null) {
				this.removeGeoConstruction(gc);
				ii--;
			}
		}
	}
	
	/**
	 * Method that assigns two UX variables to specified constructed point
	 * 
	 * @param P			The point that new variables will be assigned to its 
	 * 					symbolic x and y coordinates
	 * @param pointType	Type of instantiation: whether this point will be instantiated
	 * 					as free point (both coordinates will be u-variables) or as
	 * 					dependent point (both coordinates will be x-variables) or as
	 * 					half dependent point (only one coordinate is u-variable and
	 * 					another is x-variable); this value is one of POINT_TYPE_xxx values 
	 */
	public void instantiatePoint(Point P, int pointType) {
		if (P == null)
			return;
		
		UXVariable xVar = null, yVar = null;
		boolean instantiatedAsOrigin = false;
		
		switch (pointType) {
		case Point.POINT_TYPE_FREE:
			if (!this.hasFreeParametricSet && this.numZeroIndices > 0) {
				xVar = new UXVariable(Variable.VAR_TYPE_UX_U, 0);
				this.numZeroIndices--;
			}
			else {
				xVar = new UXVariable(Variable.VAR_TYPE_UX_U, this.uIndex);
				this.uIndex++;
			}
			
			if (!this.hasFreeParametricSet && this.numZeroIndices > 0) {
				yVar = new UXVariable(Variable.VAR_TYPE_UX_U, 0);
				this.numZeroIndices--;
			}
			else {
				yVar = new UXVariable(Variable.VAR_TYPE_UX_U, this.uIndex);
				this.uIndex++;
			}
			
			break;
		case Point.POINT_TYPE_X_INDEPENDENT:
			instantiatedAsOrigin = false;
			
			if (this.hasFreeParametricSet && (P instanceof RandomPointFromParametricSet)) {
				ParametricSet set = (ParametricSet) ((RandomPointFromParametricSet)P).getBaseSetOfPoints();
				Point firstPointFromParamSet = set.getPoints().get(0);
				
				if ((set instanceof FreeParametricSet) && ((FreeParametricSet)set).isContainsOrigin() && P.equals(firstPointFromParamSet) && this.numZeroIndices > 1) {
					// instantiate as origin
					xVar = new UXVariable(Variable.VAR_TYPE_UX_U, 0);
					yVar = new UXVariable(Variable.VAR_TYPE_UX_U, 0);
					this.numZeroIndices = this.numZeroIndices - 2;
					instantiatedAsOrigin = true;
				}
			}
			
			if (!instantiatedAsOrigin) {
				if ((!this.hasFreeParametricSet || (P instanceof RandomPointFromParametricSet)) &&
					this.numZeroIndices > 0) {
					xVar = new UXVariable(Variable.VAR_TYPE_UX_U, 0);
					this.numZeroIndices--;
				}
				else {
					xVar = new UXVariable(Variable.VAR_TYPE_UX_U, this.uIndex);
					this.uIndex++;
				}
			
				yVar = new UXVariable(Variable.VAR_TYPE_UX_X, this.xIndex);
				this.xIndex++;
			}
			
			break;
		case Point.POINT_TYPE_Y_INDEPENDENT:
			instantiatedAsOrigin = false;
			
			if (this.hasFreeParametricSet && (P instanceof RandomPointFromParametricSet)) {
				ParametricSet set = (ParametricSet) ((RandomPointFromParametricSet)P).getBaseSetOfPoints();
				Point firstPointFromParamSet = set.getPoints().get(0);
				
				if ((set instanceof FreeParametricSet) && ((FreeParametricSet)set).isContainsOrigin() && P.equals(firstPointFromParamSet) && this.numZeroIndices > 1) {
					// instantiate as origin
					xVar = new UXVariable(Variable.VAR_TYPE_UX_U, 0);
					yVar = new UXVariable(Variable.VAR_TYPE_UX_U, 0);
					this.numZeroIndices = this.numZeroIndices - 2;
					instantiatedAsOrigin = true;
				}
			}
			
			if (!instantiatedAsOrigin) {
				xVar = new UXVariable(Variable.VAR_TYPE_UX_X, this.xIndex);
				this.xIndex++;
			
				if ((!this.hasFreeParametricSet || (P instanceof RandomPointFromParametricSet)) &&
					this.numZeroIndices > 0) {
					yVar = new UXVariable(Variable.VAR_TYPE_UX_U, 0);
					this.numZeroIndices--;
				}
				else {
					yVar = new UXVariable(Variable.VAR_TYPE_UX_U, this.uIndex);
					this.uIndex++;
				}
			}
			
			break;
		case Point.POINT_TYPE_DEPENDENT:
			xVar = new UXVariable(Variable.VAR_TYPE_UX_X, this.xIndex);
			this.xIndex++;
			
			yVar = new UXVariable(Variable.VAR_TYPE_UX_X, this.xIndex);
			this.xIndex++;
			
			break;
		default:
			break;
		}
		
		P.setX(xVar);
		P.setY(yVar);
		P.setInstanceType(pointType);
		if (P.getPointState() == Point.POINT_STATE_INITIALIZED)
			P.setPointState(Point.POINT_STATE_INSTANTIATED);
		else
			P.setPointState(Point.POINT_STATE_REINSTANTIATED);
	}
	
	/**
	 * Method for instantiation of symbolic polynomial in x-polynomial.
	 * 
	 * @param condition		Symbolic polynomial representing the condition 
	 * 						for some point
	 * @param pointsMap		Map of points assigned to labels of common points from
	 * 						symbolic polynomial representing the condition,
	 * 						used for instantiation of that condition
	 * @return				XPolynomial representing the algebraic form of condition
	 * 						or null in case of error
	 */
	public static XPolynomial instantiateCondition(SymbolicPolynomial condition, Map<String, Point> pointsMap) {
		XPolynomial result = new XPolynomial(); // empty polynomial
		
		// Instantiate one by one term of symbolic polynomial representing the condition
		// and then add to the resulting XPolynomial
		for (Term st : condition.getTermsAsDescList()) {
			// instances of powers will be temporarily split into two maps
			// to provide merging of powers of same U or X variables;
			// e.g. term x0*xA could be instantiated with u1 * u1 = u1 ^2
			// Although method addPower() from Term class does the merging of degrees
			// of powers of same variable, we do extra merging here to make adding
			// powers to term faster and to save some memory here - but this solution
			// could be skipped
			Map<UXVariable, Power> uPowers = new HashMap<UXVariable, Power>();
			Map<UXVariable, Power> xPowers = new HashMap<UXVariable, Power>();
			
			boolean isZeroTerm = false;
			// Process powers of current symbolic term one by one
			for (Power pow : st.getPowers()) {
				Power powerInstance;
				SymbolicVariable sv = (SymbolicVariable)pow.getVariable();
				String pointLabel = sv.getPointLabel();
				short varType = sv.getVariableType();
				Point P = pointsMap.get(pointLabel);
				
				if (P == null) {
					OpenGeoProver.settings.getLogger().error("Failed to find point with specified label " + pointLabel + " during instantiation of condition");
					return null;
				}
				
				if (varType == Variable.VAR_TYPE_SYMB_X) {
					powerInstance = new Power(P.getX().clone(), pow.getExponent());
				}
				else if (varType == Variable.VAR_TYPE_SYMB_Y) {
					powerInstance = new Power(P.getY().clone(), pow.getExponent());
				}
				else {
					OpenGeoProver.settings.getLogger().error("Non-symbolic variable obtained when symbolic was excpected");
					return null;
				}
				
				// check the type of the power instance and add into proper place
				short newVarType = powerInstance.getVarType();
				
				if (newVarType == Variable.VAR_TYPE_UX_U) {
					// check whether power is zero - in that case whole term is zero
					// and will not be added to resulting XPolynomial
					if (powerInstance.getIndex() == 0) {
						isZeroTerm = true;
						break;
					}
					
					// check whether power for this u-variable has been already added
					Power currPower = uPowers.get(powerInstance.getVariable());
					
					if (currPower == null) {
						// if new variable, just add its power into map
						uPowers.put((UXVariable)powerInstance.getVariable(), powerInstance);
					}
					else {
						// if existing variable, then merge exponents of existing and current powers
						currPower.addToExponent(powerInstance.getExponent());
					}
				}
				else if (newVarType == Variable.VAR_TYPE_UX_X) {
					// check whether power for this x-variable has been already added
					Power currPower = xPowers.get(powerInstance.getVariable());
					
					if (currPower == null) {
						// if new variable, just add its power into map
						xPowers.put((UXVariable)powerInstance.getVariable(), powerInstance);
					}
					else {
						// if existing variable, then merge exponents of existing and current powers
						currPower.addToExponent(powerInstance.getExponent());
					}
				}
				else {
					OpenGeoProver.settings.getLogger().error("Power is not instantiated by UX variable");
					return null;
				}
			}
			
			// If zero term obtained as instance, skip it
			if (isZeroTerm)
				continue;
			
			// instantiate XTerm of this symbolic term
			UTerm ut = new UTerm(((SymbolicTerm)st).getCoeff());
			
			for (Power upow : uPowers.values()) {
				ut.addPower(upow);
			}
			
			UPolynomial up = new UPolynomial();
			up.addTerm(ut);
			UFraction uf = new UFraction(up);
			XTerm xt = new XTerm(uf);
			
			for (Power xpow : xPowers.values()) {
				xt.addPower(xpow);
			}
			
			// add current instantiated XTerm to resulting polynomial
			result.addTerm(xt);
		}
		
		return result;
	}
	
	public boolean isPolynomialConsequenceOfConstructions(XPolynomial xpoly) {
		// If given polynomial is zero polynomial, it is always satisfied and
		// therefore it is trivially consequence of constructions
		if (xpoly.isZero())
			return true;
		
		// If it is not zero, check whether it is already added to system of polynomials;
		// if that's the case then given polynomial is consequence of constructions;
		// in common case we should check other types of dependencies like 
		// complex forms of linear (or even other) dependencies of this polynomial
		// and other polynomials from system to see if this instantiated polynomial
		// is consequence of construction. It could be done like proving small theorem
		// by usage of some algebraic method (e.g. Wu's method). But we do not want
		// to increase the complexity of transformation to algebraic form so this
		// complex check will be skipped unless something urgent happens that will change
		// this decision.
		for (XPolynomial xp : this.getAlgebraicGeoTheorem().getHypotheses().getPolynomials()) {
			if (xpoly.clone().subtractPolynomial(xp).isZero())
				return true;
		}
		
		return false;
	}
	
	/**
	 * Method that transforms all polynomial NDG conditions to user
	 * readable form.
	 * 
	 * @return	SUCCESS if successful, or general error otherwise
	 */
	public int translateNDGConditionsToUserReadableForm() {
		// Fill in NDG conditions
		if (this.algebraicNDGConditions == null) {
			Vector<XPolynomial> ndgConditionsPolys = this.getAlgebraicGeoTheorem().getNDGConditions().getPolynomials();
			
			if (ndgConditionsPolys == null || ndgConditionsPolys.size() == 0)
				return OGPConstants.RET_CODE_SUCCESS; // there's nothing to translate
			
			for (XPolynomial ndgPoly : ndgConditionsPolys)
				this.addAlgebraicNDGCondition(new AlgebraicNDGCondition(ndgPoly));
		}
		
		if (this.algebraicNDGConditions == null) {
			OpenGeoProver.settings.getLogger().error("Failed to fill in objects for NDG conditions");
			return OGPConstants.ERR_CODE_GENERAL;
		}
		
		for (AlgebraicNDGCondition ndgCond : this.algebraicNDGConditions) {
			if (ndgCond.transformToUserReadableForm() != OGPConstants.RET_CODE_SUCCESS) {
				OpenGeoProver.settings.getLogger().error("Failed to translate NDG condition " + ndgCond.getPolynomial().print());
				return OGPConstants.ERR_CODE_GENERAL;
			}
		}
		
		return OGPConstants.RET_CODE_SUCCESS;
	}
	
	/**
	 * Method which creates and populates a vector with translated NDG conditions.
	 * First of all it calls translation of NDG conditions to user readable format.
	 * Elements of vector are strings in format:
	 *    <b>"ndgType[A1, A2, ..., An]"</b>
	 * where Ai are points which generate the special position for this NDG condition.
	 * If ndgType is polynomial representation, then string will be
	 *    <b>"ndgType[polynomial in textual format]"</b>.
	 * Generated list can be used outside of OGP.
	 *  
	 * @return	List with NDG conditions.
	 */
	public Vector<String> exportTranslatedNDGConditions() {
		if (this.translateNDGConditionsToUserReadableForm() != OGPConstants.RET_CODE_SUCCESS)
			return null;
		
		Map<String, String> ndgMap = new HashMap<String, String>();
		if (this.algebraicNDGConditions != null && this.algebraicNDGConditions.size() > 0) {
			for (AlgebraicNDGCondition ndgc : this.algebraicNDGConditions) {
				StringBuilder sbNdgText = new StringBuilder(ndgc.getNdgType());
				String ndgText = null;
				
				sbNdgText.append("[");
				if (ndgc.getNdgType().equals(AlgebraicNDGCondition.NDG_TYPE_POLYNOMIAL))
					sbNdgText.append(ndgc.getPolynomial().print());
				else {
					Vector<Point> ptList = ndgc.getBestPointList();
					for (int ii = 0, jj = ptList.size(); ii < jj; ii++) {
						if (ii > 0)
							sbNdgText.append(", ");
						sbNdgText.append(ptList.get(ii).getGeoObjectLabel());
					}
				}
				sbNdgText.append("]");
				ndgText = sbNdgText.toString();
				
				if (ndgMap.get(ndgText) == null) // use map to avoid duplicate values
					ndgMap.put(ndgText, ndgText);
			}
		}
		
		return new Vector<String>(ndgMap.values());
	}
	
	/**
	 * Method that searches for all combinations of points whose at least one
	 * coordinate equals zero. E.g. if A, B and C are points with zero coordinate(s)
	 * then this method populates zeroPoints list with all combinations:
	 * 
	 * A
	 * B
	 * C
	 * A B
	 * A C
	 * B C
	 * A B C
	 */
	private void populateZeroPoints() {
		Vector<Point> allZeroPoints = new Vector<Point>();
		
		for (GeoConstruction cons: this.constructionSteps) {
			if (cons instanceof Point) {
				Point P = (Point)cons;
				Variable x = P.getX();
				Variable y = P.getY();
				
				if ((x.getVariableType() == Variable.VAR_TYPE_UX_U && x.getIndex() == 0) ||
					(y.getVariableType() == Variable.VAR_TYPE_UX_U && y.getIndex() == 0))
					allZeroPoints.add(P);
			}
		}
		
		this.zeroPoints = PointListManager.createListOfCombinations(allZeroPoints);
		this.zeroPoints.add(new Vector<Point>()); // add empty list for merging without zero points
	}
	
	/**
	 * Method that retrieves lists of points associated to each of variables
	 * from passed in list. A point form this CP is associated to some variable
	 * if and only if it has been instantiated (i.e. variables have been assigned
	 * to its coordinates) and one its coordinate is equal to that specified variable.
	 * 
	 * @param varList	List of variables
	 * @return			Map with lists of points associated to passed in variables;
	 * 					null in case of error
	 */
	public Map<UXVariable, Vector<Point>> getPointsAssociatedWithVariables(Vector<UXVariable> varList) {
		Map<UXVariable, Vector<Point>> resultMap = new HashMap<UXVariable, Vector<Point>>();
		Map<String, UXVariable> varMap = new HashMap<String, UXVariable>();
		
		for (UXVariable uxV : varList)
			varMap.put(uxV.toString(), uxV);
		
		if (varList.size() != varMap.size()) {
			OpenGeoProver.settings.getLogger().error("Passed in list of variables contains duplicates.");
			return null;
		}
		
		for (GeoConstruction gc : this.constructionSteps) {
			if (!(gc instanceof Point))
				continue;
			
			Point p = (Point)gc; // safe cast
			
			if (p.getPointState() == Point.POINT_STATE_INITIALIZED) {
				OpenGeoProver.settings.getLogger().error("Point not yet instantiated.");
				return null;
			}
			
			UXVariable xVar = p.getX();
			UXVariable yVar = p.getY();
			
			if (xVar == null || yVar == null) {
				OpenGeoProver.settings.getLogger().error("Point doesn't have coordinates.");
				return null;
			}
			
			if (varMap.get(xVar.toString()) != null) {
				Vector<Point> xVarPts = resultMap.get(xVar);
				
				if (xVarPts == null) {
					xVarPts = new Vector<Point>();
					resultMap.put(xVar, xVarPts);
				}
				
				if (!xVarPts.contains(p))
					xVarPts.add(p);
			}
			
			if (varMap.get(yVar.toString()) != null) {
				Vector<Point> yVarPts = resultMap.get(yVar);
				
				if (yVarPts == null) {
					yVarPts = new Vector<Point>();
					resultMap.put(yVar, yVarPts);
				}
				
				if (!yVarPts.contains(p))
					yVarPts.add(p);
			}
		}
		
		// Check if all variables were processed
		if (resultMap.size() != varMap.size()) {
			OpenGeoProver.settings.getLogger().error("Not all variables were successfully processed.");
			return null;
		}
		
		return resultMap;
	}
}
