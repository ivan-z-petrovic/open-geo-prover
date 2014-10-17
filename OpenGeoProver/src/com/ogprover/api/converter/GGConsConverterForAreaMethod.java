/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.api.converter;

import java.util.ArrayList;
import java.util.Vector;

import com.ogprover.geogebra.command.construction.*;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.pp.tp.expressions.Fraction;
import com.ogprover.pp.tp.expressions.BasicNumber;
import com.ogprover.pp.tp.expressions.RatioOfCollinearSegments;
import com.ogprover.pp.tp.geoconstruction.*;
import com.ogprover.pp.tp.geoobject.*;
import com.ogprover.pp.tp.ndgcondition.DistinctPoints;
import com.ogprover.pp.tp.ndgcondition.NonParallelLines;
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
	 * and add only constructions of these types in thmProtocol.
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
					RatioOfCollinearSegments ratio = new RatioOfCollinearSegments(center, pointOnCircle, center, footPoint);
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
					this.thmProtocol.addSimpleNDGCondition(new NonParallelLines(point1, point2, secondPoint1, secondPoint2));
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
						return new PRatioPoint(oArgs.get(0), footPoint, point1, footPoint, new BasicNumber(1));
					}
					if (point2.equals(pointOnCircle)) {
						Point footPoint = new AMFootPoint(nextAvailableName(), center, point1, point2);
						this.thmProtocol.addGeoConstruction(footPoint);
						return new PRatioPoint(oArgs.get(0), footPoint, point2, footPoint, new BasicNumber(1));
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
						return new PRatioPoint(oArgs.get(0), midPoint, pointOnCircle1, midPoint, new BasicNumber(1));
					}
					logger.error("To use the area method on the intersection between two circles, the two circles must" +
							" have been generated using the same point");
					return null;
				}
				if (firstSet instanceof Circle || firstSet instanceof Line) {
					// This shouldn't happen
					logger.error("Object " + firstSet.getGeoObjectLabel() + " has not been converted into an area method-compatible class.");
					return null;
				}
				if (secondSet instanceof Circle || secondSet instanceof Line) {
					// This shouldn't happen
					logger.error("Object " + secondSet.getGeoObjectLabel() + " has not been converted into an area method-compatible class.");
					return null;
				}
				logger.error("The area method cannot deal with intersection between general conics.");
			}

			// oArgs.size() > 1
			logger.error("The area method cannot deal with intersection between objects which " +
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
	 * @see com.ogprover.api.converter.GeoGebraConstructionConverter#convertMidpointCmd(com.ogprover.geogebra.command.construction.GeoGebraConstructionCommand)
	 */
	protected GeoConstruction convertMidpointCmd(GeoGebraConstructionCommand ggCmd) {
		/*
		 * Construction of segment's midpoint requires two input arguments
		 * which are labels of two points or one input argument which is label of segment.
		 */
		
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.validateCmdArguments(ggCmd, 1, 2, 1, 1) == false) {
			logger.error("Failed to validate command: " + MidpointCmd.cmdName);
			return null;
		}
		
		try {
			ArrayList<String> iArgs = ggCmd.getInputArgs();
			ArrayList<String> oArgs = ggCmd.getOutputArgs();
			
			Point pt1 = null;
			Point pt2 = null;
			
			if (iArgs.size() == 1) {
				Line s = (Line)this.thmProtocol.getConstructionMap().get(iArgs.get(0)); // line representing a segment
				Vector<Point> points = s.getPoints();
				pt1 = points.get(0);
				pt2 = points.get(1);
			}
			if (iArgs.size() == 2) {
				pt1 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(0));
				pt2 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(1));
			}
			return new PRatioPoint(oArgs.get(0), pt1, pt1, pt2, new Fraction(1,2));
			
		} catch (ClassCastException ex) {
			logger.error(GeoGebraConstructionConverter.getClassCastExceptionMessage(ggCmd, ex));
			return null;
		}  catch (Exception ex) {
			logger.error("Unexpected exception caught: " + ex.toString());
			return null;
		}
	}
	
	/**
	 * @see com.ogprover.api.converter.GeoGebraConstructionConverter#convertCenterCmd(com.ogprover.geogebra.command.construction.GeoGebraConstructionCommand)
	 */
	protected GeoConstruction convertCenterCmd(GeoGebraConstructionCommand ggCmd) {
		/*
		 * For the area method can construct the center of a conic only if this is a circle.
		 * 
		 * Construction of center of circle/conic requires exactly one input argument
		 * which is the label of a circle/conic. 
		 */
		
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.validateCmdArguments(ggCmd, 1, 1, 1, 1) == false) {
			logger.error("Failed to validate command: " + CenterCmd.cmdName);
			return null;
		}
		
		try {
			ArrayList<String> iArgs = ggCmd.getInputArgs();
			ArrayList<String> oArgs = ggCmd.getOutputArgs();
			
			SetOfPoints conic = (SetOfPoints)this.thmProtocol.getConstructionMap().get(iArgs.get(0));
			
			if (!(conic instanceof Circle) /*&& !(conic instanceof ConicSection)*/) {
				logger.error("Can't construct center on object which is not circle and conic section.");
				return null;
			}
			
			if (!(conic instanceof CircleWithCenterAndPoint)) {
				// This shouldn't happen
				logger.error("Object " + conic.getGeoObjectLabel() + " has not been converted into an area method-compatible class.");
				return null;
			}
			
			Circle c = (Circle)conic;
			return new CenterOfCircle(oArgs.get(0), c);
		} catch (ClassCastException ex) {
			logger.error(GeoGebraConstructionConverter.getClassCastExceptionMessage(ggCmd, ex));
			return null;
		}  catch (Exception ex) {
			logger.error("Unexpected exception caught: " + ex.toString());
			return null;
		}
	}
	
	
	/*
	 * LINES
	 * 
	 * When reading the following, remember that we always want to return a LineThroughTwoPoints,
	 * that's why the constructions may be a little complicated sometimes.
	 */
	
	/**
	 * @see com.ogprover.api.converter.GeoGebraConstructionConverter#convertLineCmd(com.ogprover.geogebra.command.construction.GeoGebraConstructionCommand)
	 */
	protected GeoConstruction convertLineCmd(GeoGebraConstructionCommand ggCmd) {
		/*
		 * Construction of line requires exactly two input arguments
		 * which are labels of either two points or a point and line.
		 */
		
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.validateCmdArguments(ggCmd, 2, 2, 1, 1) == false) {
			logger.error("Failed to validate command: " + LineCmd.cmdName);
			return null;
		}
		
		try {
			ArrayList<String> iArgs = ggCmd.getInputArgs();
			ArrayList<String> oArgs = ggCmd.getOutputArgs();
			
			Point pt1 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(0));
			GeoObject obj2 = this.thmProtocol.getConstructionMap().get(iArgs.get(1));
			
			if (obj2 instanceof Point) {
				this.thmProtocol.addSimpleNDGCondition(new DistinctPoints(pt1, (Point)obj2));
				return new LineThroughTwoPoints(oArgs.get(0), pt1, (Point)obj2);
			}
			
			// Else, obj2 is supposed to be a line
			LineThroughTwoPoints line = (LineThroughTwoPoints)obj2;
			Point pointOnLine1 = line.getPoints().get(0);
			Point pointOnLine2 = line.getPoints().get(1);
			Point aux = new PRatioPoint(nextAvailableName(), pt1, pointOnLine1, pointOnLine2, new BasicNumber(1));
			this.thmProtocol.addGeoConstruction(aux);
			this.thmProtocol.addSimpleNDGCondition(new DistinctPoints(aux, pt1));
			return new LineThroughTwoPoints(oArgs.get(0), aux, pt1);
		} catch (ClassCastException ex) {
			logger.error(GeoGebraConstructionConverter.getClassCastExceptionMessage(ggCmd, ex));
			return null;
		}  catch (Exception ex) {
			logger.error("Unexpected exception caught: " + ex.toString());
			return null;
		}
	}
	
	/**
	 * @see com.ogprover.api.converter.GeoGebraConstructionConverter#convertOrthogonalLineCmd(com.ogprover.geogebra.command.construction.GeoGebraConstructionCommand)
	 */
	protected GeoConstruction convertOrthogonalLineCmd(GeoGebraConstructionCommand ggCmd) {
		/*
		 * Construction of perpendicular line requires exactly two input arguments
		 * which are labels of a point and a line.
		 */
		
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.validateCmdArguments(ggCmd, 2, 2, 1, 1) == false) {
			logger.error("Failed to validate command: " + OrthogonalLineCmd.cmdName);
			return null;
		}
		
		try {
			ArrayList<String> iArgs = ggCmd.getInputArgs();
			ArrayList<String> oArgs = ggCmd.getOutputArgs();
			
			Point pt = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(0));
			LineThroughTwoPoints line = (LineThroughTwoPoints)this.thmProtocol.getConstructionMap().get(iArgs.get(1));
			
			Point pointOnLine1 = line.getPoints().get(0);
			Point pointOnLine2 = line.getPoints().get(1);
			Point footPoint = new AMFootPoint(nextAvailableName(), pt, pointOnLine1, pointOnLine2);
			this.thmProtocol.addGeoConstruction(footPoint);
			this.thmProtocol.addSimpleNDGCondition(new DistinctPoints(footPoint, pt));
			return new LineThroughTwoPoints(oArgs.get(0), footPoint, pt);
		} catch (ClassCastException ex) {
			logger.error(GeoGebraConstructionConverter.getClassCastExceptionMessage(ggCmd, ex));
			return null;
		}  catch (Exception ex) {
			logger.error("Unexpected exception caught: " + ex.toString());
			return null;
		}
	}
	
	/**
	 * @see com.ogprover.api.converter.GeoGebraConstructionConverter#convertLineBisectorCmd(com.ogprover.geogebra.command.construction.GeoGebraConstructionCommand)
	 */
	protected GeoConstruction convertLineBisectorCmd(GeoGebraConstructionCommand ggCmd) {
		/*
		 * Construction of perpendicular bisector requires two input arguments
		 * which are labels of points or one input argument which is label of segment.
		 */
		
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.validateCmdArguments(ggCmd, 1, 2, 1, 1) == false) {
			logger.error("Failed to validate command: " + LineBisectorCmd.cmdName);
			return null;
		}
		
		try {
			ArrayList<String> iArgs = ggCmd.getInputArgs();
			ArrayList<String> oArgs = ggCmd.getOutputArgs();
			
			Point pt1 = null;
			Point pt2 = null;
			
			if (iArgs.size() == 1) {
				LineThroughTwoPoints line = (LineThroughTwoPoints)this.thmProtocol.getConstructionMap().get(iArgs.get(0));
				pt1 = line.getPoints().get(0);
				pt2 = line.getPoints().get(1);
			}
			else {
				pt1 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(0));
				pt2 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(1));
			}
			
			Point midPoint = new PRatioPoint (nextAvailableName(), pt1, pt1, pt2, new Fraction(1,2));
			this.thmProtocol.addGeoConstruction(midPoint);
			
			Point aux = new TRatioPoint (nextAvailableName(), midPoint, pt2, new BasicNumber(1));
			this.thmProtocol.addGeoConstruction(aux);
			
			this.thmProtocol.addSimpleNDGCondition(new DistinctPoints(midPoint, aux));
			return new LineThroughTwoPoints(oArgs.get(0), midPoint, aux);
		} catch (ClassCastException ex) {
			logger.error(GeoGebraConstructionConverter.getClassCastExceptionMessage(ggCmd, ex));
			return null;
		}  catch (Exception ex) {
			logger.error("Unexpected exception caught: " + ex.toString());
			return null;
		}
	}
	
	/**
	 * @see com.ogprover.api.converter.GeoGebraConstructionConverter#convertAngularBisectorCmd(com.ogprover.geogebra.command.construction.GeoGebraConstructionCommand)
	 */
	protected GeoConstruction convertAngularBisectorCmd(GeoGebraConstructionCommand ggCmd) {
		/*
		 * Construction of angular bisector requires exactly three input arguments
		 * which are labels of three points. 
		 */
		
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.validateCmdArguments(ggCmd, 3, 3, 1, 1) == false) {
			logger.error("Failed to validate command: " + AngularBisectorCmd.cmdName);
			return null;
		}
		
		try {
			//ArrayList<String> iArgs = ggCmd.getInputArgs();
			//ArrayList<String> oArgs = ggCmd.getOutputArgs();
			
			//Point ptRay1 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(0));
			//Point ptVertex = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(1));
			//Point ptRay2 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(2));
			
			// TODO See if we can compute this using the area method
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
	 * @see com.ogprover.api.converter.GeoGebraConstructionConverter#convertTangentCmd(com.ogprover.geogebra.command.construction.GeoGebraConstructionCommand)
	 */
	protected GeoConstruction convertTangentCmd(GeoGebraConstructionCommand ggCmd) {
		/*
		 * TODO - In GeoGebra there is a construction of tangent lines to conic parallel with given line.
		 * It is not supported by OGP. Only tangents on conic through given point are currently supported.
		 *
		 * Construction of tangent line requires exactly two input arguments
		 * which are label of a point and label of circle or conic. It produces
		 * one or two tangent lines depending on whether the point is on 
		 * circle/conic or not, but always has two output labels - if has one
		 * tangent, the label of second is empty string.
		 * 
		 * With the area method, one can not produce more than one tangent line, and currently, one is not
		 * able to deal with conics.
		 */
		
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.validateCmdArguments(ggCmd, 2, 2, 2, 2) == false) {
			logger.error("Failed to validate command: " + TangentCmd.cmdName);
			return null;
		}
		
		try {
			ArrayList<String> iArgs = ggCmd.getInputArgs();
			ArrayList<String> oArgs = ggCmd.getOutputArgs();
			
			Point pt = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(0));
			SetOfPoints conic = (SetOfPoints)this.thmProtocol.getConstructionMap().get(iArgs.get(1));
			
			if (!(conic instanceof Circle) && !(conic instanceof ConicSection)) {
				logger.error("Can't construct tangent on object which is not circle and conic section.");
				return null;
			}
			
			if (!(conic instanceof Circle)) {
				logger.error("Currently, the area method cannot deal with conics.");
				return null;
			}
			
			String secondLabel = oArgs.get(1);
			
			if (secondLabel.length() != 0) {
				logger.error("The area method cannot deal with the construction of two tangents at the same time");
				return null;
			}
			
			CircleWithCenterAndPoint circle = (CircleWithCenterAndPoint) conic;
			Point center = circle.getCenter();
			
			if (circle.getPoints().contains(pt)) {
				Point aux = new TRatioPoint(nextAvailableName(), pt, center, new BasicNumber(1));
				this.thmProtocol.addGeoConstruction(aux);
				this.thmProtocol.addSimpleNDGCondition(new DistinctPoints(pt, aux));
				return new LineThroughTwoPoints(oArgs.get(0), pt, aux);
			}
			
			// Only one label but two tangents
			logger.error("Invalid input (the point is not on the circle, but only one label is given)");
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
	 * @see com.ogprover.api.converter.GeoGebraConstructionConverter#convertPolarCmd(com.ogprover.geogebra.command.construction.GeoGebraConstructionCommand)
	 */
	protected GeoConstruction convertPolarCmd(GeoGebraConstructionCommand ggCmd) {
		/*
		 * In GeoGebra the "Polar" construction for selected point and circle/conic gives the polar
		 * line of that selected point w.r.t. that circle/conic.
		 */
		
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.validateCmdArguments(ggCmd, 2, 2, 1, 1) == false) {
			logger.error("Failed to validate command: " + PolarCmd.cmdName);
			return null;
		}
		
		try {
			logger.error("convertPolarCmd() command is not compatible with the area method.");
			return null;
		} catch (ClassCastException ex) {
			logger.error(GeoGebraConstructionConverter.getClassCastExceptionMessage(ggCmd, ex));
			return null;
		}  catch (Exception ex) {
			logger.error("Unexpected exception caught: " + ex.toString());
			return null;
		}
	}
	
	// TODO handle convertDiameterCmd() for circles
	
	
	/*
	 * CONICS
	 */
	
	/**
	 * @see com.ogprover.api.converter.GeoGebraConstructionConverter#convertCircleCmd(com.ogprover.geogebra.command.construction.GeoGebraConstructionCommand)
	 */
	protected GeoConstruction convertCircleCmd(GeoGebraConstructionCommand ggCmd) {
		/*
		 * Construction of circle requires 2 or 3 arguments. When has two arguments,
		 * the first is always the center and the second can be: a point from circle, 
		 * a segment either by its label or in form of command "Segment[X,Y]" or 
		 * the measure of radius. Three arguments are used in form with three points 
		 * for construction of circumscribed circle.
		 */
		
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.validateCmdArguments(ggCmd, 2, 3, 1, 1) == false) {
			logger.error("Failed to validate command: " + CircleCmd.cmdName);
			return null;
		}
		
		try {
			ArrayList<String> iArgs = ggCmd.getInputArgs();
			ArrayList<String> oArgs = ggCmd.getOutputArgs();
			
			if (iArgs.size() == 3) {
				Point pt1 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(0));
				Point pt2 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(1));
				Point pt3 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(2));
				
				// The circumscribed circle is the intersection of two bisectors of the segments
				Point midPoint1 = new PRatioPoint(nextAvailableName(), pt1, pt1, pt2, new Fraction(1,2));
				this.thmProtocol.addGeoConstruction(midPoint1);
				Point aux1 = new TRatioPoint(nextAvailableName(), midPoint1, pt1, new BasicNumber(1));
				this.thmProtocol.addGeoConstruction(aux1);
				
				Point midPoint2 = new PRatioPoint(nextAvailableName(), pt2, pt2, pt3, new Fraction(1,2));
				this.thmProtocol.addGeoConstruction(midPoint2);
				Point aux2 = new TRatioPoint(nextAvailableName(), midPoint2, pt2, new BasicNumber(1));
				this.thmProtocol.addGeoConstruction(aux2);
				
				Point center = new AMIntersectionPoint(nextAvailableName(), midPoint1, aux1, midPoint2, aux2);
				this.thmProtocol.addGeoConstruction(center);
				return new CircleWithCenterAndPoint(oArgs.get(0), center, pt1);
			}
			
			// iArgs.size() == 2
			Point pt = (Point)(this.thmProtocol.getConstructionMap().get(iArgs.get(0)));
			String label2 = iArgs.get(1);
			
			if (label2.startsWith("Segment[")) {
				int pt1Idx = label2.indexOf("[") + 1;
				int commaIdx = label2.indexOf(",");
				int pt2Idx = commaIdx + 2; // skip one blank space character
				int rbracIdx = label2.indexOf("]");
				
				String pt1Label = label2.substring(pt1Idx, commaIdx);
				String pt2Label = label2.substring(pt2Idx, rbracIdx);
				
				Point pt1 = (Point)this.thmProtocol.getConstructionMap().get(pt1Label);
				Point pt2 = (Point)this.thmProtocol.getConstructionMap().get(pt2Label);
				
				Point pointOnCircle = new PRatioPoint(nextAvailableName(), pt, pt1, pt2, new BasicNumber(1));
				
				return new CircleWithCenterAndPoint(oArgs.get(0), pt, pointOnCircle);
			}
			
			GeoObject geoObj = this.thmProtocol.getConstructionMap().get(label2);
			
			if (geoObj == null) {
				// Check whether second argument is a measure of radius
				try {
					// TODO this need to be changed : label2 can be something else than an integer,
					// and the area method can deal with constant numbers.
					Integer.parseInt(label2);
					/*
					 *  Since parsing of number was successful, and since constant numbers/measures
					 *  can't be used in algebraic provers (they deal with symbolic calculus), new
					 *  point will be introduced and the circle will be represented as a circle with
					 *  center and one point.
					 */
					Point pt1 = new FreePoint(nextAvailableName());
					this.thmProtocol.addGeoConstruction(pt1);
					return new CircleWithCenterAndPoint(oArgs.get(0), pt, pt1);
				} catch (NumberFormatException ex) {
					logger.error("Incorrect second input argument for construction of circle.");
					return null;
				}
			}
			
			if (geoObj instanceof LineThroughTwoPoints) { // line represent a segment
				LineThroughTwoPoints l = (LineThroughTwoPoints)geoObj;
				Point pt1 = l.getPoints().get(0); 
				Point pt2 = l.getPoints().get(1); 
				Point pointOnCircle = new PRatioPoint(nextAvailableName(), pt, pt1, pt2, new BasicNumber(1));
				
				return new CircleWithCenterAndPoint(oArgs.get(0), pt, pointOnCircle);
			}
			
			if (geoObj instanceof Line) {
				// This shouldn't happen
				logger.error("Object " + geoObj.getGeoObjectLabel() + " has not been converted into an area method-compatible class.");
			}
			
			return new CircleWithCenterAndPoint(oArgs.get(0), pt, (Point)geoObj); // safe casts
		} catch (ClassCastException ex) {
			logger.error(GeoGebraConstructionConverter.getClassCastExceptionMessage(ggCmd, ex));
			return null;
		}  catch (Exception ex) {
			logger.error("Unexpected exception caught: " + ex.toString());
			return null;
		}
	}
	
	/**
	 * @see com.ogprover.api.converter.GeoGebraConstructionConverter#convertConicCmd(com.ogprover.geogebra.command.construction.GeoGebraConstructionCommand)
	 */
	protected GeoConstruction convertConicCmd(GeoGebraConstructionCommand ggCmd) {
		/*
		 * "Construction" of conic section through 5 given points.
		 */
		
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.validateCmdArguments(ggCmd, 5, 5, 1, 1) == false) {
			logger.error("Failed to validate command: " + ConicCmd.cmdName);
			return null;
		}
		
		logger.error("The use of general conics is impossible wih the area method.");
		return null;
	}
	
	/**
	 * @see com.ogprover.api.converter.GeoGebraConstructionConverter#convertEllipseCmd(com.ogprover.geogebra.command.construction.GeoGebraConstructionCommand)
	 */
	protected GeoConstruction convertEllipseCmd(GeoGebraConstructionCommand ggCmd) {
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.validateCmdArguments(ggCmd, 3, 3, 1, 1) == false) {
			logger.error("Failed to validate command: " + EllipseCmd.cmdName);
			return null;
		}
		
		logger.error("The use of general conics is impossible wih the area method.");
		return null;
	}
	
	/**
	 * @see com.ogprover.api.converter.GeoGebraConstructionConverter#convertHyperbolaCmd(com.ogprover.geogebra.command.construction.GeoGebraConstructionCommand)
	 */
	protected GeoConstruction convertHyperbolaCmd(GeoGebraConstructionCommand ggCmd) {
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.validateCmdArguments(ggCmd, 3, 3, 1, 1) == false) {
			logger.error("Failed to validate command: " + HyperbolaCmd.cmdName);
			return null;
		}
		
		logger.error("The use of general conics is impossible wih the area method.");
		return null;
	}
	
	/**
	 * @see com.ogprover.api.converter.GeoGebraConstructionConverter#convertParabolaCmd(com.ogprover.geogebra.command.construction.GeoGebraConstructionCommand)
	 */
	protected GeoConstruction convertParabolaCmd(GeoGebraConstructionCommand ggCmd) {
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.validateCmdArguments(ggCmd, 2, 2, 1, 1) == false) {
			logger.error("Failed to validate command: " + ParabolaCmd.cmdName);
			return null;
		}
		
		logger.error("The use of general conics is impossible wih the area method.");
		return null;
	}
	
	
	
	// TODO - Rotate command, Translate command, Dilate command.
	
	/*
	 * PARTIAL AND AUXILIARY OBJECTS
	 */
	
	// Segments, vectors, rays, etc. are seen as lines.
	
	/**
	 * @see com.ogprover.api.converter.GeoGebraConstructionConverter#convertSegmentCmd(com.ogprover.geogebra.command.construction.GeoGebraConstructionCommand)
	 */
	protected GeoConstruction convertSegmentCmd(GeoGebraConstructionCommand ggCmd) {
		/*
		 * Segment is always defined by two points.
		 */
		
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.validateCmdArguments(ggCmd, 2, 2, 1, 1) == false) {
			logger.error("Failed to validate command: " + SegmentCmd.cmdName);
			return null;
		}
		
		try {
			ArrayList<String> iArgs = ggCmd.getInputArgs();
			ArrayList<String> oArgs = ggCmd.getOutputArgs();
			
			Point pt1 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(0));
			Point pt2 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(1));
			this.thmProtocol.addSimpleNDGCondition(new DistinctPoints(pt1, pt2));
			Segment seg = new Segment(pt1, pt2, oArgs.get(0));
			this.auxiliaryObjectsMap.put(seg.getGeoObjectLabel(), seg);
			return new LineThroughTwoPoints(oArgs.get(0), pt1, pt2);
		} catch (ClassCastException ex) {
			logger.error(GeoGebraConstructionConverter.getClassCastExceptionMessage(ggCmd, ex));
			return null;
		}  catch (Exception ex) {
			logger.error("Unexpected exception caught: " + ex.toString());
			return null;
		}
	}
	
	/**
	 * @see com.ogprover.api.converter.GeoGebraConstructionConverter#convertVectorCmd(com.ogprover.geogebra.command.construction.GeoGebraConstructionCommand)
	 */
	protected GeoConstruction convertVectorCmd(GeoGebraConstructionCommand ggCmd) {
		/*
		 * Vector is always defined by two points.
		 */
		
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.validateCmdArguments(ggCmd, 2, 2, 1, 1) == false) {
			logger.error("Failed to validate command: " + VectorCmd.cmdName);
			return null;
		}
		
		try {
			ArrayList<String> iArgs = ggCmd.getInputArgs();
			ArrayList<String> oArgs = ggCmd.getOutputArgs();
			
			Point pt1 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(0));
			Point pt2 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(1));
			this.thmProtocol.addSimpleNDGCondition(new DistinctPoints(pt1, pt2));
			Segment seg = new Segment(pt1, pt2, oArgs.get(0));
			this.auxiliaryObjectsMap.put(seg.getGeoObjectLabel(), seg);
			return new LineThroughTwoPoints(oArgs.get(0), pt1, pt2);
		} catch (ClassCastException ex) {
			logger.error(GeoGebraConstructionConverter.getClassCastExceptionMessage(ggCmd, ex));
			return null;
		}  catch (Exception ex) {
			logger.error("Unexpected exception caught: " + ex.toString());
			return null;
		}
	}
	
	/**
	 * @see com.ogprover.api.converter.GeoGebraConstructionConverter#convertRayCmd(com.ogprover.geogebra.command.construction.GeoGebraConstructionCommand)
	 */
	protected GeoConstruction convertRayCmd(GeoGebraConstructionCommand ggCmd) {
		/*
		 * Ray is always defined by two points.
		 */
		
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.validateCmdArguments(ggCmd, 2, 2, 1, 1) == false) {
			logger.error("Failed to validate command: " + RayCmd.cmdName);
			return null;
		}
		
		try {
			ArrayList<String> iArgs = ggCmd.getInputArgs();
			ArrayList<String> oArgs = ggCmd.getOutputArgs();
			
			Point pt1 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(0));
			Point pt2 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(1));
			this.thmProtocol.addSimpleNDGCondition(new DistinctPoints(pt1, pt2));
			return new LineThroughTwoPoints(oArgs.get(0), pt1, pt2);
		} catch (ClassCastException ex) {
			logger.error(GeoGebraConstructionConverter.getClassCastExceptionMessage(ggCmd, ex));
			return null;
		}  catch (Exception ex) {
			logger.error("Unexpected exception caught: " + ex.toString());
			return null;
		}
	}
	
	/**
	 * @see com.ogprover.api.converter.GeoGebraConstructionConverter#convertAngleCmd(com.ogprover.geogebra.command.construction.GeoGebraConstructionCommand)
	 */
	protected GeoConstruction convertAngleCmd(GeoGebraConstructionCommand ggCmd) {
		/*
		 * Angle is defined by three points or by two lines.
		 */
		
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.validateCmdArguments(ggCmd, 2, 3, 1, 1) == false) {
			logger.error("Failed to validate command: " + AngleCmd.cmdName);
			return null;
		}
		
		logger.error("The use of angles is impossible wih the area method - " +
				"please describe you angles using lines.");
		return null;
	}
	
	/**
	 * @see com.ogprover.api.converter.GeoGebraConstructionConverter#convertPolygonCmd(com.ogprover.geogebra.command.construction.GeoGebraConstructionCommand)
	 */
	protected GeoConstruction convertPolygonCmd(GeoGebraConstructionCommand ggCmd) {
		/*
		 * General polygon is defined by the array of its vertices. Regular polygon is
		 * defined by two subsequent vertices and number of vertices.
		 */
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.validateCmdArguments(ggCmd, 3, -1, 1, -1) == false) {
			logger.error("Failed to validate command: " + PolygonCmd.cmdName);
			return null;
		}
		
		logger.info("The use of polygons is strongly not recommanded wih the area method - " +
				"you should describe your polygons using lines.");
		try {
			ArrayList<String> iArgs = ggCmd.getInputArgs();
			ArrayList<String> oArgs = ggCmd.getOutputArgs();
			
			String oLabel = oArgs.get(0);
			boolean bIsRegular = false;
			int numVertices = 0;
			Vector<Point> vertices = new Vector<Point>();
			Vector<String> edges = new Vector<String>();
			
			// Check if input is for regular polygon
			if (iArgs.size() == 3) {
				try {
					numVertices = Integer.parseInt(iArgs.get(2));
					
					if (numVertices < 3) {
						logger.error("Incorrect number of vertices passed in for construction of regular polygon");
						return null;
					}
					
					bIsRegular = true;
				} catch (NumberFormatException ex) {
					// Third argument is not a positive integer number
				}
			}
			
			/*
			 * Populate array of vertices
			 */
			// Plain polygon
			if (!bIsRegular) {
				numVertices = iArgs.size(); // number of points
				// Check number of output arguments
				if (oArgs.size() != numVertices + 1) { // number of edges + polygon
					logger.error("Incorrect number of output arguments for construction of polygon " + oLabel);
					return null;
				}
				
				for (String ptLabel : iArgs) {
					Point pt = (Point)this.thmProtocol.getConstructionMap().get(ptLabel);
					vertices.add(pt);
				}
			}
			else { // regular polygon
				// Check number of output arguments
				if (oArgs.size() != 2*numVertices -1) { // number of edges (numVertices) + new vertices (numVertices - 2) + polygon
					logger.error("Incorrect number of output arguments for construction of polygon " + oLabel);
					return null;
				}
				
				// Take labels of new vertices and create these points by rotation for the measure of inner angle
				logger.error("The area method does not currently deal with regular polygons. " +
						"Please construct the regular polygon by hand");
				return null;
			}
			
			// Add lines of polygon edges
			for (int ii = 0, jj = 1; ii < numVertices; ii++, jj++) {
				Point pt1 = vertices.get(ii);
				Point pt2 = vertices.get(jj % numVertices);
				String lineLabel = oArgs.get(ii + 1); 
				this.thmProtocol.addSimpleNDGCondition(new DistinctPoints(pt1, pt2));
				this.thmProtocol.addGeoConstruction(new LineThroughTwoPoints(lineLabel, pt1, pt2));
				edges.add(lineLabel);
			}
			return new IgnoredConstruction();
		} catch (ClassCastException ex) {
			logger.error(GeoGebraConstructionConverter.getClassCastExceptionMessage(ggCmd, ex));
			return null;
		}  catch (Exception ex) {
			logger.error("Unexpected exception caught: " + ex.toString());
			return null;
		}
	}
	
	/**
	 * @see com.ogprover.api.converter.GeoGebraConstructionConverter#convertPolyLineCmd(com.ogprover.geogebra.command.construction.GeoGebraConstructionCommand)
	 */
	protected GeoConstruction convertPolyLineCmd(GeoGebraConstructionCommand ggCmd) {
		/*
		 * Polygon line is defined by the array of its vertices.
		 */
		
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.validateCmdArguments(ggCmd, 3, -1, 1, 1) == false) {
			logger.error("Failed to validate command: " + PolyLineCmd.cmdName);
			return null;
		}
		
		try {
			ArrayList<String> iArgs = ggCmd.getInputArgs();
			Vector<GeoConstruction> consList = new Vector<GeoConstruction>();
			
			Vector<Point> vertices = new Vector<Point>();
			Vector<String> edges = new Vector<String>();
			
			logger.error("The use of polylines is not recommanded wih the area method - " +
					"you should describe your construction using simple lines.");
			
			// Populate vertices
			for (String ptLabel : iArgs) {
				Point pt = (Point)this.thmProtocol.getConstructionMap().get(ptLabel);
				vertices.add(pt);
				consList.add(pt);
			}
			
			// Add lines of polygon edges
			for (int ii = 0, jj = 1, numVertices = iArgs.size(); ii < numVertices; ii++, jj++) {
				Point pt1 = vertices.get(ii);
				Point pt2 = vertices.get(jj % numVertices);
				
				StringBuilder sb = new StringBuilder();
				sb.append(pt1.getGeoObjectLabel());
				sb.append(pt2.getGeoObjectLabel());
				sb.append("_l");
				String lineLabel = GeoGebraConstructionConverter.generateRandomLabel(sb.toString()); 
				this.thmProtocol.addSimpleNDGCondition(new DistinctPoints(pt1, pt2));
				this.thmProtocol.addGeoConstruction(new LineThroughTwoPoints(lineLabel, pt1, pt2));
				edges.add(lineLabel);
			}
			
			return new IgnoredConstruction();
		} catch (ClassCastException ex) {
			logger.error(GeoGebraConstructionConverter.getClassCastExceptionMessage(ggCmd, ex));
			return null;
		}  catch (Exception ex) {
			logger.error("Unexpected exception caught: " + ex.toString());
			return null;
		}
	}
	
	// convertCircleArc -- Superclass' behavior is fine.
	
	/**
	 * @see com.ogprover.api.converter.GeoGebraConstructionConverter#convertCircumcircleArcCmd(com.ogprover.geogebra.command.construction.GeoGebraConstructionCommand)
	 */
	protected GeoConstruction convertCircumcircleArcCmd(GeoGebraConstructionCommand ggCmd) {
		/*
		 * Circumcircle arc is defined by three points from that arc.
		 * It will be stored as a circle since algebraic provers can't distinguish circle arcs. 
		 */
		
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.validateCmdArguments(ggCmd, 3, 3, 1, 1) == false) {
			logger.error("Failed to validate command: " + CircumcircleArcCmd.cmdName);
			return null;
		}
		
		try {
			// The bisector of a segment formed by two points on a circle passes by the center of the circle.
			ArrayList<String> iArgs = ggCmd.getInputArgs();
			ArrayList<String> oArgs = ggCmd.getOutputArgs();
			
			Point pt1 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(0));
			Point pt2 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(1));
			Point pt3 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(2));
			
			Point midPoint1 = new PRatioPoint(nextAvailableName(), pt1, pt1, pt2, new Fraction(1,2));
			this.thmProtocol.addGeoConstruction(midPoint1);
			Point midPoint2 = new PRatioPoint(nextAvailableName(), pt1, pt1, pt3, new Fraction(1,2));
			this.thmProtocol.addGeoConstruction(midPoint2);
			
			Point pointOnLine1 = new TRatioPoint(nextAvailableName(), midPoint1, pt1, new BasicNumber(1));
			this.thmProtocol.addGeoConstruction(pointOnLine1);
			Point pointOnLine2 = new TRatioPoint(nextAvailableName(), midPoint2, pt1, new BasicNumber(1));
			this.thmProtocol.addGeoConstruction(pointOnLine2);
			
			return new AMIntersectionPoint(oArgs.get(0), midPoint1, pointOnLine1, midPoint2, pointOnLine2);
		} catch (ClassCastException ex) {
			logger.error(GeoGebraConstructionConverter.getClassCastExceptionMessage(ggCmd, ex));
			return null;
		}  catch (Exception ex) {
			logger.error("Unexpected exception caught: " + ex.toString());
			return null;
		}
	}
	
	// convertCircleSectorCmd -- Superclass' behavior is fine.
	
	/**
	 * @see com.ogprover.api.converter.GeoGebraConstructionConverter#convertCircumcircleSectorCmd(com.ogprover.geogebra.command.construction.GeoGebraConstructionCommand)
	 */
	protected GeoConstruction convertCircumcircleSectorCmd(GeoGebraConstructionCommand ggCmd) {
		/*
		 * Circumcircle sector is defined by three points from its arc.
		 * It will be stored as a circle since algebraic provers can't distinguish circle arcs. 
		 */
		
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.validateCmdArguments(ggCmd, 3, 3, 1, 1) == false) {
			logger.error("Failed to validate command: " + CircumcircleSectorCmd.cmdName);
			return null;
		}
		
		try {
			// The bisector of a segment formed by two points on a circle passes by the center of the circle.
			ArrayList<String> iArgs = ggCmd.getInputArgs();
			ArrayList<String> oArgs = ggCmd.getOutputArgs();
			
			Point pt1 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(0));
			Point pt2 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(1));
			Point pt3 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(2));
			
			Point midPoint1 = new PRatioPoint(nextAvailableName(), pt1, pt1, pt2, new Fraction(1,2));
			this.thmProtocol.addGeoConstruction(midPoint1);
			Point midPoint2 = new PRatioPoint(nextAvailableName(), pt1, pt1, pt3, new Fraction(1,2));
			this.thmProtocol.addGeoConstruction(midPoint2);
			
			Point pointOnLine1 = new TRatioPoint(nextAvailableName(), midPoint1, pt1, new BasicNumber(1));
			this.thmProtocol.addGeoConstruction(pointOnLine1);
			Point pointOnLine2 = new TRatioPoint(nextAvailableName(), midPoint2, pt1, new BasicNumber(1));
			this.thmProtocol.addGeoConstruction(pointOnLine2);
			
			return new AMIntersectionPoint(oArgs.get(0), midPoint1, pointOnLine1, midPoint2, pointOnLine2);
		} catch (ClassCastException ex) {
			logger.error(GeoGebraConstructionConverter.getClassCastExceptionMessage(ggCmd, ex));
			return null;
		}  catch (Exception ex) {
			logger.error("Unexpected exception caught: " + ex.toString());
			return null;
		}
	}
	
	/**
	 * @see com.ogprover.api.converter.GeoGebraConstructionConverter#convertSemicircleCmd(com.ogprover.geogebra.command.construction.GeoGebraConstructionCommand)
	 */
	protected GeoConstruction convertSemicircleCmd(GeoGebraConstructionCommand ggCmd) {
		/*
		 * Semicircle is defined by two diameter points.
		 * It will be stored as a circle since the area method can't distinguish circle arcs. 
		 */
		
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.validateCmdArguments(ggCmd, 2, 2, 1, 1) == false) {
			logger.error("Failed to validate command: " + SemicircleCmd.cmdName);
			return null;
		}
		
		try {
			ArrayList<String> iArgs = ggCmd.getInputArgs();
			ArrayList<String> oArgs = ggCmd.getOutputArgs();
			Point pt1 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(0));
			Point pt2 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(1));
			Point midPoint = new PRatioPoint(nextAvailableName(), pt1, pt1, pt2, new Fraction(1,2));
			this.thmProtocol.addGeoConstruction(midPoint);
			return new CircleWithCenterAndPoint(oArgs.get(0), midPoint, pt2);
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

