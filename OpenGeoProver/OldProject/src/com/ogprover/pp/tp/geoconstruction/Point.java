/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.geoconstruction;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.ogprover.main.OGPConstants;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.Power;
import com.ogprover.polynomials.Term;
import com.ogprover.polynomials.UFraction;
import com.ogprover.polynomials.UPolynomial;
import com.ogprover.polynomials.UTerm;
import com.ogprover.polynomials.UXVariable;
import com.ogprover.polynomials.Variable;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.polynomials.XTerm;
import com.ogprover.utilities.io.OGPOutput;
import com.ogprover.utilities.io.SpecialFileFormatting;
import com.ogprover.utilities.logger.ILogger;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Abstract class for point</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public abstract class Point extends GeoConstruction implements Cloneable {
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
     * X or U variable that is instance of X coordinate of this point
     */
    protected UXVariable X = null;
    /**
     * X or U variable that is instance of Y coordinate of this point
     */
    protected UXVariable Y = null;
    
    // Constants for types of points to be instantiated in algebraic form
    /**
	 * <i><b>
	 * Non-instantiated point
	 * </b></i>
	 */
	public static final int POINT_TYPE_NONE = -1;
	/**
	 * <i><b>
	 * Free or independent point by both variables
	 * </b></i>
	 */
	public static final int POINT_TYPE_FREE = 0;
	/**
	 * <i><b>
	 * Independent point by x variable (like some random point from line)
	 * </b></i>
	 */
	public static final int POINT_TYPE_X_INDEPENDENT = 1;
	/**
	 * <i><b>
	 * Independent point by y variable (like some random point from line)
	 * </b></i>
	 */
	public static final int POINT_TYPE_Y_INDEPENDENT = 2;
	/**
	 * <i><b>
	 * Dependent point (like intersection of two lines)
	 * </b></i>
	 */
	public static final int POINT_TYPE_DEPENDENT = 3;
	/**
	 * Type of instance of this point
	 */
	protected int instanceType = Point.POINT_TYPE_NONE;

	// Return codes for method for processing the polynomial
    /**
     * <i><b>
     * Polynomial is bad instance of condition of this point, and that
     * cannot be fixed.
     * </b></i>
     */
    public static final int PROCESSPOLY_RETCODE_BAD_POLYNOMIAL = 0;
    /**
     * <i><b>
     * Polynomial is bad instance of condition of this point, but
     * this might be fixed by re-instancing the point which is
     * half dependent(one coordinate is u, another is x).
     * </b></i>
     */
    public static final int PROCESSPOLY_RETCODE_TRY_AGAIN = 1;
    /**
     * <i><b>
     * Polynomial is good and is not some special case and therefore
     * it is added to the system of polynomials representing the constructions.
     * </b></i>
     */
    public static final int PROCESSPOLY_RETCODE_ADDED_TO_SYSTEM = 2;
    /**
     * <i><b>
     * Polynomial is good but it is special case when one dependent variable x
     * could be renamed by another variable.
     * </b></i>
     */
    public static final int PROCESSPOLY_RETCODE_COORDINATES_RENAMED = 3;
    
    // Types for states of this point
    // 1. Initial state is INITIALIZED. From this state point can go only to INSTANTIATED,
    //    and then can never return to initial state. 
    // 2. It can turn to state REINSTANTIATED, RENAMED and UNCHANGED from state INSTANTIATED. 
    // 3. If point is REINSTANTIATED it can further stay UNCHANGED or be RENAMED.
    // 4. If point is RENAMED it can stay UNCHANGED or be RENAMED again.
    // 5. If point was UNCHANGED it can turn to REINSTANTIATED, RENAMED or stay UNCHANGED.
    /**
     * <i><b>
     * Point has been initialized.
     * </b></i>
     */
    public static final int POINT_STATE_INITIALIZED = 0;
    /**
     * <i><b>
     * Point has been instantiated.
     * </b></i>
     */
    public static final int POINT_STATE_INSTANTIATED = 1;
    /**
     * <i><b>
     * Point has been re-instantiated.
     * </b></i>
     */
    public static final int POINT_STATE_REINSTANTIATED = 2;
    /**
     * <i><b>
     * Point's coordinates have been renamed.
     * </b></i>
     */
    public static final int POINT_STATE_RENAMED = 3;
    /**
     * <i><b>
     * Point unchanged.
     * </b></i>
     */
    public static final int POINT_STATE_UNCHANGED = 4;
    /**
     * Current state of this point.
     */
    protected int pointState = Point.POINT_STATE_INITIALIZED;
    
    
	/*
     * ======================================================================
     * ========================= ABSTRACT METHODS ===========================
     * ======================================================================
     */
    /**
     * Method that performs necessary steps to transform the construction of this 
     * point into algebraic form (i.e. it is generating algebraic conditions
     * that coordinates of this point in algebraic form satisfy).  The result of
     * transformation is/are x-polynomial(s) stored in theorem protocol or
     * renamed coordinates of this point.
     * 
     * @return	Returns SUCCESS if successful or general error otherwise 
     */
    public abstract int transformToAlgebraicForm();
    /**
     * Clone method
     * 
     * @see java.lang.Object#clone()
     */
    public abstract Point clone();
    
    /**
     * Method to replace the points used in a construction by another equivalent.
     */
    public abstract Point replace(HashMap<Point, Point> replacementMap);
	
	
	
    /*
     * ======================================================================
     * ========================== GETTERS/SETTERS ===========================
     * ======================================================================
     */
	/**
	 * Method that sets the instance of X coordinate
	 * 
	 * @param x The variable to set
	 */
	public void setX(UXVariable x) {
		X = x;
	}
	
	/**
	 * Method that retrieves the instance of X coordinate
	 * 
	 * @return The variable representing the instance of X coordinate
	 */
	public UXVariable getX() {
		return X;
	}
	
	/**
	 * Method that sets the instance of Y coordinate
	 * 
	 * @param y The variable to set
	 */
	public void setY(UXVariable y) {
		Y = y;
	}
	
	/**
	 * Method that retrieves the instance of Y coordinate
	 * 
	 * @return The variable representing the instance of Y coordinate
	 */
	public UXVariable getY() {
		return Y;
	}
	
	/**
	 * Method that sets the instance type
	 * 
	 * @param instanceType the instanceType to set
	 */
	public void setInstanceType(int instanceType) {
		this.instanceType = instanceType;
	}
	
	/**
	 * Method that retrieves the instance type
	 * 
	 * @return the instanceType
	 */
	public int getInstanceType() {
		return instanceType;
	}
	
	/**
	 * Method that sets the point state
	 * 
	 * @param pointState the pointState to set
	 */
	public void setPointState(int pointState) {
		this.pointState = pointState;
	}
	
	/**
	 * Method that retrieves the point state
	 * 
	 * @return the pointState
	 */
	public int getPointState() {
		return pointState;
	}
	
	
	
	/*
	 * ======================================================================
	 * ======================= COMMON OBJECT METHODS ========================
	 * ======================================================================
	 */
	/**
	 * Method equals
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Point))
			return false;
		
		return this.getGeoObjectLabel().equals(((Point)obj).getGeoObjectLabel());
	}
	
	/**
	 * Method compare - a total order on points, we do not care about its properties
	 */
	public boolean compare(Point pt) {
		return (this.getGeoObjectLabel().compareTo(pt.getGeoObjectLabel())) < 0;
	}
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method that renames coordinate of this point.
	 * <p>
	 * Assumption is that coordinate will not be renamed by a variable of same type
	 * and higher index, because in the moment of calling this method, this point
	 * will be the one that is currently processed i.e. their conditions instantiated
	 * from construction steps will not contain variables of same type and with higher
	 * indices or in other words - in the moment of calling this method this point
	 * will have the highest coordinates since it is currently being processed and their
	 * coordinates are still not used in any previous conditions so renaming is safe and
	 * will not break already transformed construction.
	 * </p>
	 * 
	 * <p>
	 * Only dependent variables can be renamed.
	 * </p>
	 * 
	 * @param coordinateType	The type of coordinate to rename - can be X or Y
	 * 							(expressed by one of two VAR_TYPE_SYMB_... values)
	 * @param newVarType		One of two VAR_TYPE_UX_... values (dependent or independent variable)
	 * @param newIndex			New variable index
	 * @param writeToOutput		True if results of processing have to be written to output reports. 
	 * 							False otherwise - it will be false when this method is called
	 * 							just for testing of some polynomial to see if it will be chosen
	 * 							as condition instance for this point.
	 * @return					PROCESSPOLY_RETCODE_COORDINATES_RENAMED if renaming was successful
	 * 							or ERR_CODE_GENERAL in case of error
	 */
	private int renameCoordinate(short coordinateType, short newVarType, long newIndex, boolean writeToOutput) {
		ILogger logger = OpenGeoProver.settings.getLogger();
		OGPOutput output = OpenGeoProver.settings.getOutput();
		String messageForOutput = "";
		
		// check input arguments - type and index of new variable
		if ((newVarType != Variable.VAR_TYPE_UX_U && newVarType != Variable.VAR_TYPE_UX_X) ||
			(newVarType == Variable.VAR_TYPE_UX_U && newIndex < 0) ||
			(newVarType == Variable.VAR_TYPE_UX_X && newIndex <= 0)) {
			logger.error("Wrong variable for coordinate of point");
			if (writeToOutput) {
				try {
					output.openItemWithDesc("Error: ");
					output.closeItemWithDesc("Wrong variable for coordinate of point " + this.geoObjectLabel);
				} catch (IOException e) {
					logger.error("Failed to write to output file(s).");
					output.close();
				}
			}
			return OGPConstants.ERR_CODE_GENERAL;
		}
		
		// set proper output message
		if (newVarType == Variable.VAR_TYPE_UX_X)
			messageForOutput = "dependent variable $x_{" + newIndex + "}$";
		else { // u-variable
			if (newIndex == 0)
				messageForOutput = "zero";
			else
				messageForOutput = "independent variable $u_{" + newIndex + "}$";
		}
		
		// apply rename logic
		if (coordinateType == Variable.VAR_TYPE_SYMB_X) {
			// X will be renamed
			
			logger.info("Will try to rename X coordinate");
			if (writeToOutput) {
				try {
					output.openItemWithDesc("Info: ");
					output.closeItemWithDesc("Will try to rename X coordinate of point " + this.geoObjectLabel);
				} catch (IOException e) {
					logger.error("Failed to write to output file(s).");
					output.close();
					return OGPConstants.ERR_CODE_GENERAL;
				}
			}
			
			if (this.X.getVariableType() != Variable.VAR_TYPE_UX_X) {
				logger.error("Attempt to rename independent variable");
				if (writeToOutput) {
					try {
						output.openItemWithDesc("Error: ");
						output.closeItemWithDesc("Failed to rename X coordinate of point " + this.geoObjectLabel + " because it is independent variable");
					} catch (IOException e) {
						logger.error("Failed to write to output file(s).");
						output.close();
					}
				}
				return OGPConstants.ERR_CODE_GENERAL;
			}
			
			// then check Y - if it is of same variable type
			// rename it too if its index is not smaller than of X 
			boolean renameY = false;
			if (this.Y.getVariableType() == Variable.VAR_TYPE_UX_X) {
				if (this.Y.getIndex() > this.X.getIndex()) {
					logger.info("Will rename Y coordinate by variable of X coordinate");
					if (writeToOutput) {
						try {
							output.openItemWithDesc("Info: ");
							output.closeItemWithDesc("Y coordinate of point " + this.geoObjectLabel + " will be replaced by X coordinate");
						} catch (IOException e) {
							logger.error("Failed to write to output file(s).");
							output.close();
							return OGPConstants.ERR_CODE_GENERAL;
						}
					}
					this.Y.setIndex(this.X.getIndex()); // reuse old index
				}
				else if (this.Y.getIndex() == this.X.getIndex()) {
					logger.info("Another coordinate of point will be renamed too");
					if (writeToOutput) {
						try {
							output.openItemWithDesc("Info: ");
							output.closeItemWithDesc("Y coordinate of point " + this.geoObjectLabel + " will be renamed too");
						} catch (IOException e) {
							logger.error("Failed to write to output file(s).");
							output.close();
							return OGPConstants.ERR_CODE_GENERAL;
						}
					}
					renameY = true;
				}
			}
			
			// rename X
			this.X.setVariableType(newVarType);
			this.X.setIndex(newIndex);
			this.consProtocol.decrementXIndex(); // decrement next index for instantiation
			
			if (renameY) {
				// rename Y too
				this.Y.setVariableType(newVarType);
				this.Y.setIndex(newIndex);
			}
			
			this.pointState = Point.POINT_STATE_RENAMED;
			
			logger.info("X coordinate of this point has been renamed");
			if (writeToOutput) {
				try {
					output.openItemWithDesc("Info: ");
					StringBuilder sb = new StringBuilder();
					sb.append("X coordinate of point ");
					sb.append(this.geoObjectLabel);
					sb.append(" renamed by ");
					sb.append(messageForOutput);
					output.closeItemWithDesc(sb.toString());
				} catch (IOException e) {
					logger.error("Failed to write to output file(s).");
					output.close();
					return OGPConstants.ERR_CODE_GENERAL;
				}
			}
			this.pointState = Point.POINT_STATE_RENAMED;
			return Point.PROCESSPOLY_RETCODE_COORDINATES_RENAMED;
		}
		
		if (coordinateType == Variable.VAR_TYPE_SYMB_Y) {
			// Y will be renamed
			
			logger.info("Will try to rename Y coordinate");
			if (writeToOutput) {
				try {
					output.openItemWithDesc("Info: ");
					output.closeItemWithDesc("Will try to rename Y coordinate of point " + this.geoObjectLabel);
				} catch (IOException e) {
					logger.error("Failed to write to output file(s).");
					output.close();
					return OGPConstants.ERR_CODE_GENERAL;
				}
			}
			
			if (this.Y.getVariableType() != Variable.VAR_TYPE_UX_X) {
				logger.error("Attempt to rename independent variable");
				if (writeToOutput) {
					try {
						output.openItemWithDesc("Error: ");
						output.closeItemWithDesc("Failed to rename Y coordinate of point " + this.geoObjectLabel + " because it is independent variable");
					} catch (IOException e) {
						logger.error("Failed to write to output file(s).");
						output.close();
					}
				}
				return OGPConstants.ERR_CODE_GENERAL;
			}
			
			// then check X - if it is of same variable type
			// rename it too if its index is not smaller than of Y 
			boolean renameX = false;
			if (this.X.getVariableType() == Variable.VAR_TYPE_UX_X) {
				if (this.X.getIndex() > this.Y.getIndex()) {
					logger.info("Will rename X coordinate by variable of Y coordinate");
					if (writeToOutput) {
						try {
							output.openItemWithDesc("Info: ");
							output.closeItemWithDesc("X coordinate of point " + this.geoObjectLabel + " will be replaced by Y coordinate");
						} catch (IOException e) {
							logger.error("Failed to write to output file(s).");
							output.close();
							return OGPConstants.ERR_CODE_GENERAL;
						}
					}
					this.X.setIndex(this.Y.getIndex()); // reuse old index
				}
				else if (this.X.getIndex() == this.Y.getIndex()) {
					logger.info("Another coordinate of point will be renamed too");
					if (writeToOutput) {
						try {
							output.openItemWithDesc("Info: ");
							output.closeItemWithDesc("X coordinate of point " + this.geoObjectLabel + " will be renamed too");
						} catch (IOException e) {
							logger.error("Failed to write to output file(s).");
							output.close();
							return OGPConstants.ERR_CODE_GENERAL;
						}
					}
					renameX = true;
				}
			}
			
			// rename Y
			this.Y.setVariableType(newVarType);
			this.Y.setIndex(newIndex);
			this.consProtocol.decrementXIndex(); // decrement next index for instantiation
			
			if (renameX) {
				// rename X too
				this.X.setVariableType(newVarType);
				this.X.setIndex(newIndex);
			}
			
			logger.info("Y coordinate of this point has been renamed");
			if (writeToOutput) {
				try {
					output.openItemWithDesc("Info: ");
					StringBuilder sb = new StringBuilder();
					sb.append("Y coordinate of point ");
					sb.append(this.geoObjectLabel);
					sb.append(" renamed by ");
					sb.append(messageForOutput);
					output.closeItemWithDesc(sb.toString());
				} catch (IOException e) {
					logger.error("Failed to write to output file(s).");
					output.close();
					return OGPConstants.ERR_CODE_GENERAL;
				}
			}
			this.pointState = Point.POINT_STATE_RENAMED;
			return Point.PROCESSPOLY_RETCODE_COORDINATES_RENAMED;
		}
		
		logger.error("Wrong type of coordinate to rename");
		if (writeToOutput) {
			try {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Attempt to rename unknown coordinate of point " + this.geoObjectLabel);
			} catch (IOException e) {
				logger.error("Failed to write to output file(s).");
				output.close();
			}
		}
		return OGPConstants.ERR_CODE_GENERAL;
	}
	
	/**
	 * Method for adding polynomial to the system of polynomials of 
	 * theorem protocol
	 * 
	 * @param xPoly			Polynomial to be added
	 * @param writeToOutput	True if results of processing have to be written to output reports. 
	 * 						False otherwise - it will be false when this method is called
	 * 						just for testing of some polynomial to see if it will be chosen
	 * 						as condition instance for this point.
	 * @return				PROCESSPOLY_RETCODE_ADDED_TO_SYSTEM if polynomial is added
	 * 						successfully, or ERR_CODE_GENERAL in case of error
	 */
	private int addPolynomialToSystem(XPolynomial xPoly, boolean writeToOutput) {
		ILogger logger = OpenGeoProver.settings.getLogger();
		OGPOutput output = OpenGeoProver.settings.getOutput();
		
		// When polynomial is added to the system, it is polynomial
		// with new variables. There could exist at the most only one
		// polynomial (and that could be only the last one) which contains
		// these new variables (e.g. when we are adding a polynomial for
		// one condition and have already added polynomial for another condition
		// for the same point). Therefore, we will not check if this polynomial
		// already exists in the system (it could be eventually at the last place
		// but that should not happen - in this case it will be reported in output 
		// file and algebraic prover will not be able to complete the proof).
		
		logger.info("Adding polynomial to the system");
		if (writeToOutput) {
			try {
				output.openItemWithDesc("Info: ");
				output.writePlainText("Polynomial ");
				output.writePolynomial(xPoly);
				output.closeItemWithDesc(" added to system of polynomials that represents the constructions");
			} catch (IOException e) {
				logger.error("Failed to write to output file(s).");
				output.close();
				return OGPConstants.ERR_CODE_GENERAL;
			}
		}
		this.consProtocol.getAlgebraicGeoTheorem().getHypotheses().addXPoly(xPoly);
		logger.debug(xPoly.printToLaTeX());
		return Point.PROCESSPOLY_RETCODE_ADDED_TO_SYSTEM;
	}
	
	/**
	 * <b>[final method]</b><br>
	 * Method that processes the polynomial obtained as instance of condition
	 * of this point from one construction step (e.g. point lies on given line etc).
	 * This method will check whether polynomial is good instance or not. 
	 * If it is good, the method will add it into system of polynomials representing
	 * the constructions, but before that it will be checked whether it is in some 
	 * special form that will lead to the ability of just renaming the variables.
	 * In that case the polynomial will not be added into the system of polynomials but
	 * another instance of coordinates of this point will keep the condition's instance.
	 * The effect will be the same but the dimension of the polynomial system will be 
	 * smaller which is a benefit - algebraic methods work faster with smaller
	 * polynomial systems.
	 * <p> 
	 * Very important note: only dependent variable could be renamed
	 * by either another previously introduced dependent or independent variable,
	 * including zero (a special case of independent variables). This is so because
	 * independent (parametric) variables are free and should not satisfy any
	 * special condition (as equality). They only could be a part of discussion 
	 * of non-degenerative conditions (NDG) that have to be satisfied in some common case
	 * of the theorem that is being proved. NDGs are given as inequalities.
	 * </p>
	 *  
	 * @param xPoly			XPolynomail representing the instance of condition of this point
	 * 						obtained from some construction step
	 * @param writeToOutput	True if results of processing has to be written to output reports. 
	 * 						False otherwise - it will be false when this method is called
	 * 						just for testing of some polynomial to see if it will be chosen
	 * 						as condition instance for this point.
	 * @return				Method returns the code of action that has been taken or 
	 * 						validity mark of correctness of passed in polynomial. These values
	 * 						are represented by one of PROCESSPOLY_RETCODE_xxx constants.
	 * 						When any other sort of error is obtained, then some ERR_CODE_xxx
	 * 						value is returned. 
	 */
	public final int processConstructionPolynomial(XPolynomial xPoly, boolean writeToOutput) {
		ILogger logger = OpenGeoProver.settings.getLogger();
		OGPOutput output = OpenGeoProver.settings.getOutput();
		
		logger.debug("Starting processing of polynomial...");
		if (writeToOutput) {
			try {
				output.writePlainText("Processing of polynomial ");
				output.writePolynomial(xPoly);
				output.openEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
			} catch (IOException e) {
				logger.error("Failed to write to output file(s).");
				output.close();
				return OGPConstants.ERR_CODE_GENERAL;
			}
		}
		
		int retCode = this.processConstructionPolynomialLogic(xPoly, writeToOutput);
		
		logger.debug("Finished processing of polynomial");
		if (writeToOutput) {
			try {
				output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
			} catch (IOException e) {
				logger.error("Failed to write to output file(s).");
				output.close();
				return OGPConstants.ERR_CODE_GENERAL;
			}
		}
		
		return retCode;
	}
	
	/**
	 * Method that contains the logic of processing polynomial
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.Point#processConstructionPolynomial(XPolynomial xPoly, boolean writeToOutput)
	 */
	private int processConstructionPolynomialLogic(XPolynomial xPoly, boolean writeToOutput) {
		ILogger logger = OpenGeoProver.settings.getLogger();
		OGPOutput output = OpenGeoProver.settings.getOutput();
		
		// If this point is free, there should no be any condition for it
		if (this.X.getVariableType() == Variable.VAR_TYPE_UX_U &&
		    this.Y.getVariableType() == Variable.VAR_TYPE_UX_U) {
			logger.error("Attempt to assign a condition to free point");
			if (writeToOutput) {
				try {
					output.openItemWithDesc("Error: ");
					output.closeItemWithDesc("Attempt to assign a condition to free point " + this.geoObjectLabel);
				} catch (IOException e) {
					logger.error("Failed to write to output file(s).");
					output.close();
					return OGPConstants.ERR_CODE_GENERAL;
				}
			}
			return Point.PROCESSPOLY_RETCODE_BAD_POLYNOMIAL;
		}
		
		ArrayList<Term> terms = xPoly.getTermsAsDescList();
		
		// If polynomial doesn't contain at least one dependent coordinate of
		// this point it is bad. In this case if point is half-dependent i.e.
		// one its coordinate is u-variable, it will be tried again with another
		// point instance if it is possible.
		boolean found = false;
		for (Term t : terms) {
			if ((this.X.getVariableType() == Variable.VAR_TYPE_UX_X && t.getVectorIndexOfVarIndex(this.X.getIndex()) > -1) ||
				(this.Y.getVariableType() == Variable.VAR_TYPE_UX_X && t.getVectorIndexOfVarIndex(this.Y.getIndex()) > -1)) {
				found = true;
				break;
			}
		}
		if (!found) {
			if (this.instanceType == Point.POINT_TYPE_X_INDEPENDENT ||
				this.instanceType == Point.POINT_TYPE_Y_INDEPENDENT) {
				logger.warn("Bad polynomial for this point - will try again");
				if (writeToOutput) {
					try {
						output.openItemWithDesc("Warning: ");
						output.writePlainText("Polynomial ");
						output.writePolynomial(xPoly);
						StringBuilder sb = new StringBuilder();
						sb.append(" is bad for point ");
						sb.append(this.geoObjectLabel);
						sb.append(" since it doesn't contain its coordinates - will try to re-instantiate the point's coordinates");
						output.closeItemWithDesc(sb.toString());
					} catch (IOException e) {
						logger.error("Failed to write to output file(s).");
						output.close();
						return OGPConstants.ERR_CODE_GENERAL;
					}
				}
				return Point.PROCESSPOLY_RETCODE_TRY_AGAIN;
			}
			
			logger.error("Bad polynomial for this point");
			if (writeToOutput) {
				try {
					output.openItemWithDesc("Error: ");
					output.writePlainText("Polynomial ");
					output.writePolynomial(xPoly);
					StringBuilder sb = new StringBuilder();
					sb.append(" is bad for point ");
					sb.append(this.geoObjectLabel);
					sb.append(" since it doesn't contain its coordinates");
					output.closeItemWithDesc(sb.toString());
				} catch (IOException e) {
					logger.error("Failed to write to output file(s).");
					output.close();
					return OGPConstants.ERR_CODE_GENERAL;
				}
			}
			return Point.PROCESSPOLY_RETCODE_BAD_POLYNOMIAL;
		}
		
		/*
		 * First special case - there is only one term in passed in polynomial:
		 * If there is single x power it will be replaced by zero, otherwise
		 * polynomial will be added to system
		 */
		if (terms.size() == 1) {
			Term singleTerm = terms.get(0);
			int powersSize = singleTerm.getPowers().size();
			
			if (powersSize == 1) { // single power in this term
				// Rename the variable by zero
				
				long varIndex = singleTerm.getPowers().get(0).getIndex();
				
				if (this.X.getVariableType() == Variable.VAR_TYPE_UX_X && this.X.getIndex() == varIndex)
					return this.renameCoordinate(Variable.VAR_TYPE_SYMB_X, Variable.VAR_TYPE_UX_U, 0, writeToOutput);
				
				if (this.Y.getVariableType() == Variable.VAR_TYPE_UX_X && this.Y.getIndex() == varIndex)
					return this.renameCoordinate(Variable.VAR_TYPE_SYMB_Y, Variable.VAR_TYPE_UX_U, 0, writeToOutput);
				
				// this is some old x-variable and this is an error, since
				// passed in polynomial must contain at least one dependent
				// variable of this point and that case is already processed
				logger.error("Bad polynomial obtained when expected correct one");
				if (writeToOutput) {
					try {
						output.openItemWithDesc("Error: ");
						output.closeItemWithDesc("Unexpected error during transformation of point " + this.geoObjectLabel + " into algebraic form");
					} catch (IOException e) {
						logger.error("Failed to write to output file(s).");
						output.close();
					}
				}
				return OGPConstants.ERR_CODE_GENERAL;
			}
			else { 
				if (powersSize == 0) {
					// this means there is no x-power in this single term
					// and here it is an error since this case has to be
					// already processed above - because passed in polynomial 
					// has to contain at least one dependent coordinate of this point
					logger.error("Bad polynomial obtained when expected correct one");
					if (writeToOutput) {
						try {
							output.openItemWithDesc("Error: ");
							output.closeItemWithDesc("Unexpected error during transformation of point " + this.geoObjectLabel + " into algebraic form");
						} catch (IOException e) {
							logger.error("Failed to write to output file(s).");
							output.close();
						}
					}
					return OGPConstants.ERR_CODE_GENERAL;
				}
				
				// more than one x-power in this term
				// Add polynomial to system
				return this.addPolynomialToSystem(xPoly, writeToOutput);
			}
		}
		
		/*
		 * Second special case - there are only two terms in passed in polynomial
		 */
		else if (terms.size() == 2) {
			XTerm firstTerm = (XTerm)terms.get(0); // leading term (since 'terms' is collection of terms in descending order)
			XTerm secondTerm = (XTerm)terms.get(1);
			
			if (firstTerm.getPowers().size() > 1 || secondTerm.getPowers().size() > 1) {
				// Add polynomial to system
				return this.addPolynomialToSystem(xPoly, writeToOutput);
			}
			
			if (firstTerm.getPowers().size() == 0) {
				// error since leading term must have x-powers
				logger.error("Bad polynomial obtained when expected correct one");
				if (writeToOutput) {
					try {
						output.openItemWithDesc("Error: ");
						output.closeItemWithDesc("Unexpected error during transformation of point " + this.geoObjectLabel + " into algebraic form");
					} catch (IOException e) {
						logger.error("Failed to write to output file(s).");
						output.close();
					}
				}
				return OGPConstants.ERR_CODE_GENERAL;
			}
			
			// here first term has only one x-power and second term one ore zero x-powers
			long firstVarIndex = firstTerm.getPowers().get(0).getIndex();
				
			// polynomial is of form:
			//   uf1 * x1 + uf2 * z2 = 0, where z2 is some x2 or 1 and uf1 and uf2 are non-zero u-fractions;
			// we reduce it to form:
			//   x1 = -uf2 * z2 / uf1 = (-uf2/uf1) * z2 = reducedUF * z2
			// divide second coefficient uf2 by first one (uf1) and change the sign
			UFraction reducedUF = secondTerm.getUCoeff().clone().mul(firstTerm.getUCoeff().clone().invertFraction()).reduce().invertSign();
				
			// to be able to rename x-variable x1 from first term by a variable from second term,
			// this calculated fraction has to be 1 when z2 is of type x or 
			// fraction has to be u_n (for some n > 0) when z2 is 1
				
			if (secondTerm.getPowers().size() == 1) { // there is x-power in second term
				long secondVarIndex = secondTerm.getPowers().get(0).getIndex();
					
				// check if fraction is equals to 1
				if (reducedUF.getNumerator().clone().subtractPolynomial(reducedUF.getDenominator()).getTerms().size() == 0) {
					// in this case numerator and denominator are equals polynomials which means fraction is 1
					
					// always rename variable with higher index by a variable with smaller one;
					// since first term is greater than the second, its variable is with
					// higher index
					
					if (this.X.getVariableType() == Variable.VAR_TYPE_UX_X &&
						this.X.getIndex() == firstVarIndex)
						return this.renameCoordinate(Variable.VAR_TYPE_SYMB_X, Variable.VAR_TYPE_UX_X, secondVarIndex, writeToOutput);
					
					if (this.Y.getVariableType() == Variable.VAR_TYPE_UX_X &&
						this.Y.getIndex() == firstVarIndex)
						return this.renameCoordinate(Variable.VAR_TYPE_SYMB_Y, Variable.VAR_TYPE_UX_X, secondVarIndex, writeToOutput);
					
					// error - attempt to rename some old x-variable, not the one from this point coordinates
					// (at least one x-variable of point must exist in polynomial but it is not the case here - therefore error)
					logger.error("Bad polynomial obtained when expected correct one");
					if (writeToOutput) {
						try {
							output.openItemWithDesc("Error: ");
							output.closeItemWithDesc("Unexpected error during transformation of point " + this.geoObjectLabel + " into algebraic form");
						} catch (IOException e) {
							logger.error("Failed to write to output file(s).");
							output.close();
						}
					}
					return OGPConstants.ERR_CODE_GENERAL;
				}
				
				// not in special form - then add into polynomial system
				// Add polynomial to system
				return this.addPolynomialToSystem(xPoly, writeToOutput);
			}
			
			// second term has no x-powers
			// check if fraction is ui for some i > 0
					
			// first of all calculate gcd of all terms from numerator
			boolean onFirst = true;
			UTerm gcdTerm = null;
			for (Term t : reducedUF.getNumerator().getTermsAsDescList()) {
				if (onFirst) {
					gcdTerm = (UTerm)((UTerm)t).clone();
					gcdTerm.setCoeff(1);
					onFirst = false;
				}
				else {
					gcdTerm.gcd(t);
				}
			}
					
			if (gcdTerm == null)
				gcdTerm = new UTerm(1);
					
			// pass all powers of gcdTerm:
			// gcdTerm = u1 * u2 * ... * un
			// check if for some ui it is satisfied that numerator/ui == denominator
			// numerator will be divisible by each ui since they are powers of gcd
			// of all terms from numerator
			long uIndex = -1;
			for (Power p: gcdTerm.getPowers()) {
				UTerm divisorTerm = new UTerm(1);
				Power newPower = p.clone();
				newPower.setExponent(1);
				divisorTerm.addPower(newPower);
						
				UPolynomial reducedNumerator = (UPolynomial)reducedUF.getNumerator().clone();
						
				if (reducedNumerator.divideByTerm(divisorTerm) == null) { // error in division
					logger.error("Error in division of UPolynomial by UTerm");
					if (writeToOutput) {
						try {
							output.openItemWithDesc("Error: ");
							output.closeItemWithDesc("Unexpected error during transformation of point " + this.geoObjectLabel + " into algebraic form");
						} catch (IOException e) {
							logger.error("Failed to write to output file(s).");
							output.close();
						}
					}
					return OGPConstants.ERR_CODE_GENERAL;
				}
						
				if (reducedNumerator.subtractPolynomial(reducedUF.getDenominator()).getTerms().size() == 0) {
					// found ui that satisfies the required condition from above comment
					uIndex = p.getIndex();
					break;
				}	
			}
					
			// check if found some ui
			if (uIndex > 0){ 
				// rename x-variable of first term by this found u-variable of second term
				if (this.X.getVariableType() == Variable.VAR_TYPE_UX_X &&
					this.X.getIndex() == firstVarIndex)
					return this.renameCoordinate(Variable.VAR_TYPE_SYMB_X, Variable.VAR_TYPE_UX_U, uIndex, writeToOutput);
					
				if (this.Y.getVariableType() == Variable.VAR_TYPE_UX_X &&
					this.Y.getIndex() == firstVarIndex)
					return this.renameCoordinate(Variable.VAR_TYPE_SYMB_Y, Variable.VAR_TYPE_UX_U, uIndex, writeToOutput);
					
				// error - attempt to rename some old x-variable, not the one from this point coordinates 
				// (at least one x-variable of point must exist in polynomial but it is not the case here - therefore error)
				logger.error("Bad polynomial obtained when expected correct one");
				if (writeToOutput) {
					try {
						output.openItemWithDesc("Error: ");
						output.closeItemWithDesc("Unexpected error during transformation of point " + this.geoObjectLabel + " into algebraic form");
					} catch (IOException e) {
						logger.error("Failed to write to output file(s).");
						output.close();
					}
				}
				return OGPConstants.ERR_CODE_GENERAL;
			}
				
			// not in special form - then add into polynomial system
			// Add polynomial to system
			return this.addPolynomialToSystem(xPoly, writeToOutput);
		}
		
		/*
		 * Passed in polynomial is valid and is not in any special form - therefore 
		 * add it to the polynomial system
		 */
		else if (terms.size() > 2) {
			// Add polynomial to system
			return this.addPolynomialToSystem(xPoly, writeToOutput);
		}
		
		// If nothing from above conditions was satisfied that is execution error 
		// since all code from above should cover all cases including empty polynomial
		logger.error("Unknown error in processing polynomial for point's condition");
		if (writeToOutput) {
			try {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Unknown error during transformation of point " + this.geoObjectLabel + " into algebraic form");
			} catch (IOException e) {
				logger.error("Failed to write to output file(s).");
				output.close();
			}
		}
		return OGPConstants.ERR_CODE_GENERAL;
	}
}
