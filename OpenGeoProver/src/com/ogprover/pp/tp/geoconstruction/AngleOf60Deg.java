/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.geoconstruction;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.ogprover.main.OGPConstants;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.Power;
import com.ogprover.polynomials.SymbolicPolynomial;
import com.ogprover.polynomials.SymbolicTerm;
import com.ogprover.polynomials.SymbolicVariable;
import com.ogprover.polynomials.Term;
import com.ogprover.polynomials.UXVariable;
import com.ogprover.polynomials.Variable;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.polynomials.XTerm;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.ndgcondition.AlgebraicNDGCondition;
import com.ogprover.utilities.io.OGPOutput;
import com.ogprover.utilities.io.SpecialFileFormatting;
import com.ogprover.utilities.logger.ILogger;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for angle of 60 degrees (for its tangent)</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class AngleOf60Deg extends GeoConstruction implements SpecialConstantAngle {
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
	 * Parametric point used for algebraic expression for tangent of this angle
	 */
	private Point parametricPoint;
	/**
	 * Label of parametric point
	 */
	public static String parametricPointLabel = "A";
	
	/**
	 * <i>
	 * Symbolic polynomial representing the condition for
	 * tangent of angle of 60 degrees.
	 * </i>
	 */
	public static SymbolicPolynomial conditionForAngleOf60Deg = null;
	
	
	// Static initializer

	static {
		if (conditionForAngleOf60Deg == null) {
			/*
			 * tan60 = sqrt(3) => tan60^2 = 3
			 * Symbolic polynomial is xA^2 - 3 = 0.
			 */
			
			// Instances of symbolic variables
			SymbolicVariable xA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, parametricPointLabel);
			
			conditionForAngleOf60Deg = new SymbolicPolynomial();
			
			// term xA^2
			Term t = new SymbolicTerm(1);
			t.addPower(new Power(xA, 2));
			conditionForAngleOf60Deg.addTerm(t);
			
			// term -3
			t = new SymbolicTerm(-3);
			conditionForAngleOf60Deg.addTerm(t);
		}
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * Method that sets the parametric point
	 * 
	 * @param point The parametric point to set
	 */
	public void setParametricPoint(Point point) {
		this.parametricPoint = point;
	}

	/**
	 * Method that retrieves the parametric point
	 * 
	 * @return the parametric point
	 */
	public Point getParametricPoint() {
		return parametricPoint;
	}
	
	
	

	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * 
	 * @param angleLabel	Label of this angle
	 */
	public AngleOf60Deg(String angleLabel) {
		this.geoObjectLabel = angleLabel;
		this.parametricPoint = new FreePoint(parametricPointLabel + angleLabel);
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method that gives the type of this construction
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getConstructionType()
	 */
	@Override
	public int getConstructionType() {
		return GeoConstruction.GEOCONS_TYPE_ANGLE_OF_60_DEG;
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getConstructionDesc()
	 */
	@Override
	public String getConstructionDesc() {
		StringBuilder sb = new StringBuilder();
		sb.append("Angle ");
		sb.append(this.geoObjectLabel);
		sb.append(" of 60 degrees");
		return sb.toString();
	}
	
	/**
	 * Method that instantiates the parametric point
	 * 
	 * @throws IOException	Exception when writing to output stream 
	 * 						will be processed by caller method
	 */
	private void instantiateParametricPoint() throws IOException {
		OGPOutput output = OpenGeoProver.settings.getOutput();
		Point point = this.parametricPoint;
		
		point.setX(new UXVariable(Variable.VAR_TYPE_UX_X, this.consProtocol.getXIndex()));
		point.setY(new UXVariable(Variable.VAR_TYPE_UX_U, 0));
    	this.consProtocol.incrementXIndex();
    	if (point.getPointState() == Point.POINT_STATE_INITIALIZED)
    		point.setPointState(Point.POINT_STATE_INSTANTIATED);
		else
			point.setPointState(Point.POINT_STATE_REINSTANTIATED);
    	output.openItem();
    	output.writePlainText("Parametric point");
		output.writePointCoordinatesAssignment(point);
		output.closeItem();
	}
	
	/**
     * Method that transforms this angle to algebraic form.
     * 
     * @return	Returns SUCCESS if successful or general error otherwise 
     * 
     * @see com.ogprover.pp.tp.geoconstruction.SpecialConstantAngle#transformToAlgebraicForm()
     */
	public int transformToAlgebraicForm() {
    	OGPOutput output = OpenGeoProver.settings.getOutput();
    	ILogger logger = OpenGeoProver.settings.getLogger();
		
		try {
			output.openSubSection("Transformation of angle " + this.geoObjectLabel + " of 60 degrees: ", true);
			output.openEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
	    	
	    	this.instantiateParametricPoint();
	    	
	    	Map<String, Point> pointsMap = new HashMap<String, Point>();
	    	pointsMap.put(parametricPointLabel, this.parametricPoint);
	    	XPolynomial condition = OGPTP.instantiateCondition(conditionForAngleOf60Deg, pointsMap);
	    	this.consProtocol.getAlgebraicGeoTheorem().getHypotheses().addXPoly(condition);
	    	
	    	output.openItem();
	    	output.writePlainText("Polynomial ");
	    	output.writePolynomial(condition);
	    	output.writePlainText(" added to system of hypotheses.");
			output.closeItem();
	    	
	    	output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
			output.closeSubSection();
		} catch (IOException e) {
			logger.error("Failed to write to output file(s).");
			output.close();
			return OGPConstants.ERR_CODE_GENERAL;
		}
		
		return OGPConstants.RET_CODE_SUCCESS;
    	
    }
    
    /**
     * @see com.ogprover.pp.tp.geoconstruction.SpecialConstantAngle#processNDGCondition(com.ogprover.pp.tp.ndgcondition.AlgebraicNDGCondition)
     */
    public void processNDGCondition(AlgebraicNDGCondition ndgCond) {
    	XTerm xtA = new XTerm(1);
    	xtA.addPower(new Power(this.parametricPoint.getX().clone(), 1));
    	XPolynomial xpA = new XPolynomial();
    	xpA.addTerm(xtA);
    	
    	XPolynomial ndgPoly = ndgCond.getPolynomial();
    	
    	if (ndgPoly.equals(xpA)) {
    		StringBuilder sb = new StringBuilder();
    		sb.append("Angle ");
    		sb.append(this.geoObjectLabel);
    		sb.append(" is not zero angle");
    		ndgCond.setBestDescription(sb.toString());
    	}
    }

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getInputLabels()
	 */
	@Override
	public String[] getInputLabels() {
		return null;
	}
}
