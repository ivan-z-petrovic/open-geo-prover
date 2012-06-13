/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.api.converter;

import java.util.ArrayList;
import java.util.Vector;

import com.ogprover.geogebra.*;
import com.ogprover.geogebra.command.construction.*;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.pp.tp.geoconstruction.*;
import com.ogprover.pp.tp.geoobject.*;
import com.ogprover.utilities.logger.ILogger;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for converter of GeoGebra's commands, used for constructions 
*     of geometry objects, to OGP's constructions in format suitable
*     for the area method algorithm</dd>
* </dl>
* 
* @version 1.00
* @author Damien Desfontaines
*/
public class GGConsConverterForAreaMethod extends GGConsConverterForAlgebraicProvers {
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
	 * Number of the last intermediary number of variable used
	 */
	public static int lastUsedNumber = 0;
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * 
	 * @param ggThmCnv		GeoGebra theorem converter
	 */
	public GGConsConverterForAreaMethod(GeoGebraTheoremConverter ggThmCnv) {
		super(ggThmCnv);
	}

	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/*
	 * Methods for conversion of geometry objects
	 * We only override the classes which need to be changed.
	 * The following methods MUST return something which is an instance of :
	 * 	- FreePoint
	 * 	- AMFootPoint
	 *  - AMIntersectionPoint
	 *  - PRatioPoint
	 *  - TRatioPoint
	 *  - LineThroughTwoPoints
	 *  - CircleWithCenterAndPoint
	 * and add in the  
	 */
	
	/*
	 * POINTS
	 */
	
	// Free point - The superclass method is fine.

	/**
	 * @see com.ogprover.api.converter.GeoGebraConstructionConverter#convertPointCmd(com.ogprover.geogebra.command.construction.GeoGebraConstructionCommand)
	 */
	@Override
	protected GeoConstruction convertPointCmd(GeoGebraConstructionCommand ggCmd) {
		/*
		 * Point command has one input argument which is label of point set used for construction of
		 * random point from that point set or it can be in form "Circle[X, dist]" to express point
		 * which is on distance dist from point X (dist is number).
		 * In the first case, we do different things whether the point set is a line, a circle or otherwise
		 * In the second case, we store the information about the distance in the distances HashMap.
		 */
		
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.validateCmdArguments(ggCmd, 1, 1, 1, 1) == false) {
			logger.error("Failed to validate command: " + PointCmd.cmdName);
			return null;
		}
		
		try {
			ArrayList<String> iArgs = ggCmd.getInputArgs();
			ArrayList<String> oArgs = ggCmd.getOutputArgs();
			
			if (iArgs.get(0).contains("Circle["))
				//TODO parse the command to add the distance to the point
				return new FreePoint(oArgs.get(0));
			
			GeoConstruction gc = this.thmProtocol.getConstructionMap().get(iArgs.get(0));
			if (gc != null) {
				if (gc instanceof LineThroughTwoPoints) {
					// We take the foot of a free point on this line 
					Point point1 = ((Line)gc).getPoints().get(0);
					Point point2 = ((Line)gc).getPoints().get(1);
					Point freePoint = new FreePoint(nextAvailableName());
					this.thmProtocol.addGeoConstruction(freePoint);
					return new AMFootPoint(oArgs.get(0), freePoint, point1, point2);
				}
				if (gc instanceof CircleWithCenterAndPoint) {
					/*
					 * If the center of the circle is O, and the point which defines the circle is P, 
					 * to generate a point A anywhere in the circle, we take a free point F, let G be 
					 * its foot on the line OP, then A is generated as PRatio(O,O,F,OP/OG) by the 
					 * intercept theorem.
					 */
					Point center = ((Circle)gc).getCenter();
					Point pointOnCircle = ((Circle)gc).getPoints().get(0);
					Point freePoint = new FreePoint(nextAvailableName());
					this.thmProtocol.addGeoConstruction(freePoint);
					Point footPoint = new AMFootPoint(nextAvailableName(), freePoint, center, pointOnCircle);
					this.thmProtocol.addGeoConstruction(footPoint);
					AMRatio ratio = new AMRatio(center, pointOnCircle, center, footPoint);
					return new PRatioPoint(oArgs.get(0), center, center, freePoint, ratio);
				}
				if (gc instanceof Line || gc instanceof Circle) {
					// This shouldn't happen
					logger.error("Object " + gc.getGeoObjectLabel() + " has not been converted into an area method-compatible class.");
					return null;
				}
			}
			// Else, gc is a conic or something we cannot deal with
			logger.error("Object " + gc.getGeoObjectLabel() + " can not be used with the area method.");
			return null;
			
		} catch (ClassCastException ex) {
			logger.error(GeoGebraConstructionConverter.getClassCastExceptionMessage(ggCmd, ex));
			return null;
		}  catch (Exception ex) {
			logger.error("Unexpected exception caught: " + ex.toString());
			return null;
		}
	}
	
	// convertPointInCmd method - the superclass' behavior is fine
	
	/**
	 * @see com.ogprover.api.converter.GeoGebraConstructionConverter#convertIntersectCmd(com.ogprover.geogebra.command.construction.GeoGebraConstructionCommand)
	 */
	protected GeoConstruction convertIntersectCmd(GeoGebraConstructionCommand ggCmd) {
		/*
		 * Intersection point is defined by two objects that intersects each other.
		 * Sometimes there could exist the third argument which is ordinal number
		 * of the intersection point but it is irrelevant for the prover.
		 * 
		 * Output can give all intersection points of two input objects. 
		 */
		
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.validateCmdArguments(ggCmd, 2, 3, 1, -1) == false) {
			logger.error("Failed to validate command: " + IntersectCmd.cmdName);
			return null;
		}
		
		try {
			ArrayList<String> iArgs = ggCmd.getInputArgs();
			ArrayList<String> oArgs = ggCmd.getOutputArgs();
			
			GeoConstruction firstSet = this.thmProtocol.getConstructionMap().get(iArgs.get(0));
			GeoConstruction secondSet = this.thmProtocol.getConstructionMap().get(iArgs.get(1));
			
			if (oArgs.size() == 1) {
				if (firstSet instanceof LineThroughTwoPoints && secondSet instanceof LineThroughTwoPoints) {
					Point point1 = ((Line)firstSet).getPoints().get(0);
					Point point2 = ((Line)firstSet).getPoints().get(1);
					Point secondPoint1 = ((Line)secondSet).getPoints().get(0);
					Point secondPoint2 = ((Line)secondSet).getPoints().get(1);
					return new AMIntersectionPoint(oArgs.get(0),point1,point2,secondPoint1,secondPoint2);
				}
				if (firstSet instanceof CircleWithCenterAndPoint && secondSet instanceof LineThroughTwoPoints) {
					GeoConstruction temp = firstSet;
					firstSet = secondSet;
					secondSet = temp;
				}
				if (firstSet instanceof LineThroughTwoPoints && secondSet instanceof CircleWithCenterAndPoint) {
					Point point1 = ((Line)firstSet).getPoints().get(0);
					Point point2 = ((Line)firstSet).getPoints().get(1);
					Point center = ((Line)secondSet).getPoints().get(0);
					Point pointOnCircle = ((Line)secondSet).getPoints().get(1);
					/*
					 * We can only compute the intersection between a line and a circle if one of the two points of
					 * the line is the point which defines the circle. If so, then the line which is perpendicular 
					 * to the line and which passes by the center of the circle is an axis of symmetric between the 
					 * two intersections.
					 */
					if (point1.equals(pointOnCircle)) {
						Point footPoint = new AMFootPoint(nextAvailableName(), center, point1, point2);
						this.thmProtocol.addGeoConstruction(footPoint);
						return new PRatioPoint(oArgs.get(0), footPoint, point1, footPoint, new AMRatio(1));
					}
					if (point2.equals(pointOnCircle)) {
						Point footPoint = new AMFootPoint(nextAvailableName(), center, point1, point2);
						this.thmProtocol.addGeoConstruction(footPoint);
						return new PRatioPoint(oArgs.get(0), footPoint, point2, footPoint, new AMRatio(1));
					}
					logger.error("To use the area method on the intersection between a line and a circle, the line " +
							"must have been generated using one of two intersection points.");
					return null;
				}
				if (firstSet instanceof CircleWithCenterAndPoint && secondSet instanceof CircleWithCenterAndPoint) {
					Point center1 = ((Line)firstSet).getPoints().get(0);
					Point pointOnCircle1 = ((Line)secondSet).getPoints().get(1);
					Point center2 = ((Line)firstSet).getPoints().get(0);
					Point pointOnCircle2 = ((Line)secondSet).getPoints().get(1);
					/*
					 * We can only compute the intersection between two circles if the two points generating the circles
					 * are the same : the intersection we look for is then the other intersection.
					 */
					if (pointOnCircle1.equals(pointOnCircle2)) {
						Point midPoint = new AMFootPoint(nextAvailableName(), pointOnCircle1, center1, center2);
						this.thmProtocol.addGeoConstruction(midPoint);
						return new PRatioPoint(oArgs.get(0), midPoint, pointOnCircle1, midPoint, new AMRatio(1));
					}
					logger.error("To use the area method on the intersection between two circles, the two circles must" +
							" have been generated using the same point");
					return null;
				}
			}

			// oArgs.size() > 1
			logger.error("The area method cannot handle intersection between objects which " +
					"intersect themselves several times");
			return null;
		} catch (ClassCastException ex) {
			logger.error(GeoGebraConstructionConverter.getClassCastExceptionMessage(ggCmd, ex));
			return null;
		}  catch (Exception ex) {
			logger.error("Unexpected exception caught: " + ex.toString());
			return null;
		}
	}
	
	/**
	 * @return the next name available for an intermediary point
	 */
	protected String nextAvailableName(){
		return "iP".concat(String.valueOf(lastUsedNumber++));
	}
}