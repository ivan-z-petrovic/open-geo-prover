/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.geoconstruction;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.SymbolicPolynomial;
import com.ogprover.polynomials.UXVariable;
import com.ogprover.polynomials.Variable;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.pp.tp.auxiliary.RatioOfTwoCollinearSegments;
import com.ogprover.pp.tp.auxiliary.RatioProduct;
import com.ogprover.pp.tp.geoobject.Segment;
import com.ogprover.utilities.io.OGPOutput;
import com.ogprover.utilities.logger.ILogger;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for generalized division point of line segment (not necessarily belongs
*     to the segment, but to the line of that segment)</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class GeneralizedSegmentDivisionPoint extends SelfConditionalPoint {
	/*
	 * Point M0 divides segment AB in ratio determined by ratio product 
	 * of other collinear segments and by multiplicator coefficient:
	 * 
	 * AM0 : M0B = k*(A1B1/C1D1)*(A2B2/C2D2)*(A3B3/C3D3)*...*(AnBn/CnDn)
	 */
	
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
	 * <i><b>Symbolic label for segment division point</b></i>
	 */
	private static final String M0Label = "0";
	/**
	 * <i><b>Symbolic label for first point of line segment</b></i>
	 */
	private static final String ALabel = "A";
	/**
	 * <i><b>Symbolic label for second point of line segment</b></i>
	 */
	private static final String BLabel = "B";
	/**
	 * Segment whose division point is this point
	 */
	private Segment segment = null;
	/**
	 * Product of n (n >= 0) ratios of collinear segments
	 * that determine the ratio in which this point divides
	 * its segment.
	 */
	private RatioProduct ratioProduct = null;
	/**
	 * Multiplicator coefficient of division - determines how this point divides the segment
	 */
	private double multiplicatorCoefficient = 0.0;
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * Method to set the segment
	 * 
	 * @param seg The segment to set
	 */
	public void setSegment(Segment seg) {
		this.segment = seg;
	}

	/**
	 * Method that retrieves the segment
	 * 
	 * @return The segment
	 */
	public Segment getSegment() {
		return segment;
	}
	
	/**
	 * @param ratioProduct the ratioProduct to set
	 */
	public void setRatioProduct(RatioProduct ratioProduct) {
		this.ratioProduct = ratioProduct;
	}

	/**
	 * @return the ratioProduct
	 */
	public RatioProduct getRatioProduct() {
		return ratioProduct;
	}
	
	/**
	 * @param multiplicatorCoefficient the multiplicatorCoefficient to set
	 */
	public void setMultiplicatorCoefficient(double multiplicatorCoefficient) {
		this.multiplicatorCoefficient = multiplicatorCoefficient;
	}

	/**
	 * @return the multiplicatorCoefficient
	 */
	public double getMultiplicatorCoefficient() {
		return multiplicatorCoefficient;
	}

	/**
	 * Method that retrieves the type of construction
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getConstructionType()
	 */
	@Override
	public int getConstructionType() {
		return GeoConstruction.GEOCONS_TYPE_GEN_SEGMENT_RATIO;
	}

	/**
	 * Method that gives the condition for x coordinate 
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.SelfConditionalPoint#getXCondition()
	 */
	@Override
	public SymbolicPolynomial getXCondition() {
		// Just return null - we don't need this method for this class
		return null;
	}

	@Override
	/**
	 * Method that gives the condition for y coordinate 
	 * 
	 * @see com.ogp.pp.tp.geoconstruction.Point#getYCondition()
	 */
	public SymbolicPolynomial getYCondition() {
		// Just return null - we don't need this method for this class
		return null;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * 
	 * @param pointLabel	Label of this point
	 * @param A				First segment's point
	 * @param B				Second segment's point
	 * @param ratioProd		Ratio product that determines the ratio of this segment division
	 * @param mulCoeff		Multiplicator factor that determines the ratio of this segment division
	 */
	public GeneralizedSegmentDivisionPoint(String pointLabel, Point A, Point B, RatioProduct ratioProd, double mulCoeff) {
		this.geoObjectLabel = pointLabel;
		if (A != null && B != null)
			this.segment = new Segment(A, B);
		if (ratioProd != null)
			this.ratioProduct = ratioProd;
		else
			this.ratioProduct = new RatioProduct(); // default empty product
		this.multiplicatorCoefficient = mulCoeff;
	}
	
	
	
	/*
	 * ======================================================================
	 * ======================= COMMON OBJECT METHODS ========================
	 * ======================================================================
	 */
	/**
	 * Clone method
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.Point#clone()
	 */
	@Override
	public Point clone() {
		// we do not perform deep copy so segment's end points will not be cloned
		Point p;
		
		if (this.segment != null)
			p = new GeneralizedSegmentDivisionPoint(this.geoObjectLabel, this.segment.getFirstEndPoint(), this.segment.getSecondEndPoint(), this.ratioProduct, this.multiplicatorCoefficient);
		else
			p = new GeneralizedSegmentDivisionPoint(this.geoObjectLabel, null, null, this.ratioProduct, this.multiplicatorCoefficient);
		
		if (this.getX() != null)
			p.setX((UXVariable) this.getX().clone());
		if (this.getY() != null)
			p.setY((UXVariable) this.getY().clone());
		p.setInstanceType(this.instanceType);
		p.setPointState(this.pointState);
		p.setConsProtocol(this.consProtocol);
		p.setIndex(this.index);
		
		return p;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method that retrieves the instance of condition for X coordinate.
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.SelfConditionalPoint#instantiateXCondition(java.util.Map)
	 */
	@Override
	public XPolynomial instantiateXCondition(Map<String, Point> pointsMap) {
		/*
		 * AM0 : M0B = k*ratioP_num/ratioP_den
		 * 
		 * ratioP_den*AM0 - k*ratioP_num*M0B = 0
		 */
		Point A = this.segment.getFirstEndPoint();
		Point B = this.segment.getSecondEndPoint();
		Segment AM0Seg = new Segment(A, this);
		Segment M0BSeg = new Segment(this, B);
		
		// Note: do not reduce polynomials AM0Poly and MoBPoly by UTerm devision now !!!
		XPolynomial AM0Poly = AM0Seg.getInstantiatedXCoordinateOfOrientedSegment();
		XPolynomial M0BPoly = M0BSeg.getInstantiatedXCoordinateOfOrientedSegment();
		
		// Check whether segment is perpendicular to x-axis
		
		Variable x_A = A.getX();
		Variable x_B = B.getX();
		
		if (x_A.getVariableType() == x_B.getVariableType() && x_A.getIndex() == x_B.getIndex()) {
			// Segment AB is perpendicular to x-axis, therefore the condition is x0 - xA = 0
			
			return AM0Poly.reduceByUTermDivision();
		}
		
		this.ratioProduct.transformToAlgebraicForm();
		
		return ((XPolynomial) AM0Poly.multiplyByPolynomial(this.ratioProduct.getDenominator())
		                             .subtractPolynomial(M0BPoly.multiplyByPolynomial(this.ratioProduct.getNumerator())
				                                                .multiplyByRealConstant(this.multiplicatorCoefficient))
			   ).reduceByUTermDivision();
	}

	/**
	 * Method that retrieves the instance of condition for Y coordinate.
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.SelfConditionalPoint#instantiateYCondition(java.util.Map)
	 */
	@Override
	public XPolynomial instantiateYCondition(Map<String, Point> pointsMap) {
		/*
		 * AM0 : M0B = k*ratioP_num/ratioP_den
		 * 
		 * ratioP_den*AM0 - k*ratioP_num*M0B = 0
		 */
		Point A = this.segment.getFirstEndPoint();
		Point B = this.segment.getSecondEndPoint();
		Segment AM0Seg = new Segment(A, this);
		Segment M0BSeg = new Segment(this, B);
		
		// Note: do not reduce polynomials AM0Poly and MoBPoly by UTerm devision now !!!
		XPolynomial AM0Poly = AM0Seg.getInstantiatedYCoordinateOfOrientedSegment();
		XPolynomial M0BPoly = M0BSeg.getInstantiatedYCoordinateOfOrientedSegment();
		
		// Check whether segment is perpendicular to y-axis
		Variable y_A = A.getY();
		Variable y_B = B.getY();
		
		if (y_A.getVariableType() == y_B.getVariableType() && y_A.getIndex() == y_B.getIndex()) {
			// Segment AB is perpendicular to y-axis, therefore the condition is y0 - yA = 0
			
			return AM0Poly.reduceByUTermDivision();
		}
		
		this.ratioProduct.transformToAlgebraicForm();
		
		return ((XPolynomial) AM0Poly.multiplyByPolynomial(this.ratioProduct.getDenominator())
		                             .subtractPolynomial(M0BPoly.multiplyByPolynomial(this.ratioProduct.getNumerator())
				                                                .multiplyByRealConstant(this.multiplicatorCoefficient))
			   ).reduceByUTermDivision();
	}
	
	/**
	 * Method to check the validity of this construction step
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#isValidConstructionStep()
	 */
	@Override
	public boolean isValidConstructionStep() {
		OGPOutput output = OpenGeoProver.settings.getOutput();
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (!super.isValidConstructionStep())
			return false;
		
		try {
			int indexA, indexB;
		
			if (this.segment == null || this.segment.getFirstEndPoint() == null || this.segment.getSecondEndPoint() == null) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Segment division point " + this.getGeoObjectLabel() + " can't be constructed since one or two segment's end points are not constructed");
				return false;
			}
		
			indexA = this.segment.getFirstEndPoint().getIndex();
			indexB = this.segment.getSecondEndPoint().getIndex();
		
			if (indexA < 0 || indexB < 0) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Segment division point " + this.getGeoObjectLabel() + " can't be constructed since some of segment's end ponts is not added to theorem protocol");
				return false; // some point not in theorem protocol
			}
		
			boolean valid = this.index > indexA && this.index > indexB;
			
			if (!valid) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Segment division point " + this.getGeoObjectLabel() + " can't be constructed since some of segment's end points is not yet constructed");
			}
			
			return valid;
		} catch (IOException e) {
			logger.error("Failed to write to output file(s).");
			output.close();
			return false;
		}
	}
	
	/**
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getConstructionDesc()
	 */
	@Override
	public String getConstructionDesc() {
		StringBuilder sb = new StringBuilder();
		sb.append("Generalized segment division point ");
		sb.append(this.geoObjectLabel);
		sb.append(" of segment ");
		sb.append(this.segment.getDescription());
		sb.append(" with respect to ratio product ");
		boolean bFirst = true;
		for (RatioOfTwoCollinearSegments ratio : this.ratioProduct.getRatios()) {
			if (!bFirst)
				sb.append("*");
			else
				bFirst = false;
			sb.append("(");
			sb.append(ratio.getNumeratorSegment().getDescription());
			sb.append("/");
			sb.append(ratio.getDenominatorSegment().getDescription());
			sb.append(")");
		}
		sb.append(" and coefficient ");
		sb.append(this.multiplicatorCoefficient);
		return sb.toString();
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getInputLabels()
	 */
	@Override
	public String[] getInputLabels() {
		Map<String, String> labels = new HashMap<String, String>(); // map is used to avoid duplicate labels
		String Alabel = this.segment.getFirstEndPoint().getGeoObjectLabel();
		String Blabel = this.segment.getSecondEndPoint().getGeoObjectLabel();
		labels.put(Alabel, Alabel);
		labels.put(Blabel, Blabel);
		for (RatioOfTwoCollinearSegments ratio : this.ratioProduct.getRatios()) {
			Segment numSeg = ratio.getNumeratorSegment();
			Segment denSeg = ratio.getDenominatorSegment();
			String A1label = numSeg.getFirstEndPoint().getGeoObjectLabel();
			String B1label = numSeg.getSecondEndPoint().getGeoObjectLabel();
			String A2label = denSeg.getFirstEndPoint().getGeoObjectLabel();
			String B2label = denSeg.getSecondEndPoint().getGeoObjectLabel();
			labels.put(A1label, A1label);
			labels.put(B1label, B1label);
			labels.put(A2label, A2label);
			labels.put(B2label, B2label);
		}
		
		String[] inputLabels = new String[labels.size()];
		int ii = 0;
		for (String label : labels.keySet())
			inputLabels[ii++] = label;
		return inputLabels;
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.SelfConditionalPoint#getPointsForInstantiation()
	 */
	@Override
	public Map<String, Point> getPointsForInstantiation() {
		Map<String, Point> pointsMap = new HashMap<String, Point>();
		pointsMap.put(M0Label, this);
		pointsMap.put(ALabel, this.segment.getFirstEndPoint());
		pointsMap.put(BLabel, this.segment.getSecondEndPoint());
		return pointsMap;
	}

	@Override
	public Point replace(HashMap<Point, Point> replacementMap) {
		OpenGeoProver.settings.getLogger().error("This method should not be called on this class.");
		return null;
	}
}

