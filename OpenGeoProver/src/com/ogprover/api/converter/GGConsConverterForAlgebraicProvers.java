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
*     for algebraic provers</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class GGConsConverterForAlgebraicProvers extends GeoGebraConstructionConverter {
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
	public GGConsConverterForAlgebraicProvers(GeoGebraTheoremConverter ggThmCnv) {
		super(ggThmCnv);
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/*
	 * Methods for conversion of geometry objects
	 */
	
	/*
	 * POINTS
	 */
	
	/**
	 * @see com.ogprover.api.converter.GeoGebraConstructionConverter#convertFreePointCmd(com.ogprover.geogebra.command.construction.GeoGebraConstructionCommand)
	 */
	protected GeoConstruction convertFreePointCmd(GeoGebraConstructionCommand ggCmd) {
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.validateCmdArguments(ggCmd, 0, 0, 1, 1) == false) {
			logger.error("Failed to validate command: " + FreePointCmd.cmdName);
			return null;
		}
		
		return new FreePoint(ggCmd.getGeoObjectLabel());
	}
	
	/**
	 * @see com.ogprover.api.converter.GeoGebraConstructionConverter#convertPointCmd(com.ogprover.geogebra.command.construction.GeoGebraConstructionCommand)
	 */
	protected GeoConstruction convertPointCmd(GeoGebraConstructionCommand ggCmd) {
		/*
		 * Point command has one input argument which is label of point set used for construction of
		 * random point from that point set or it can be in form "Circle[X, dist]" to express point
		 * which is on distance dist from point X (dist is number). Since algebraic provers don't
		 * work with constant numbers, the point on specified distance will be constructed as free point.
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
				return new FreePoint(oArgs.get(0));
			
			GeoConstruction gc = this.thmProtocol.getConstructionMap().get(iArgs.get(0));
			if (gc != null)
				return RandomPointFromSetOfPoints.createRandomPoint(oArgs.get(0), (SetOfPoints)gc);
			
			// If point is on polygon or polygon line, construct it as it is on first edge line
			GeoObject gobj = this.auxiliaryObjectsMap.get(iArgs.get(0));
			if (gobj != null && (gobj instanceof PolygonLine))
				return new RandomPointFromLine(oArgs.get(0), (Line)this.thmProtocol.getConstructionMap().get(((PolygonLine)gobj).getEdgesLabels().get(0)));
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
	 * @see com.ogprover.api.converter.GeoGebraConstructionConverter#convertPointInCmd(com.ogprover.geogebra.command.construction.GeoGebraConstructionCommand)
	 */
	protected GeoConstruction convertPointInCmd(GeoGebraConstructionCommand ggCmd) {
		/*
		 * PointIn command is used for construction of point inside some given geometry object.
		 * Since algebraic provers can't make a difference between interior and exterior of geometry
		 * objects, this point will be constructed as a plain free point.
		 */
		
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.validateCmdArguments(ggCmd, 1, 1, 1, 1) == false) {
			logger.error("Failed to validate command: " + PointInCmd.cmdName);
			return null;
		}
		
		ArrayList<String> oArgs = ggCmd.getOutputArgs();
		return new FreePoint(oArgs.get(0));
	}
	
	/**
	 * Method which find perpendicular line among constructed objects.
	 * 
	 * @param baseLine		Base line of perpendicular line.
	 * @param perpLinePt	Point from perpendicular line.
	 * @return				Line object which represents a perpendicular line from given point to given line; null if can't be found
	 */
	private Line findPerpLine(Line baseLine, Point perpLinePt) {
		// Assumption: baseLine != null && perpLinePt != null
		
		/*
		 * This method could be written as a recursive method to go as deeply as
		 * necessary to search for perpendicular line. The deep of recursion would depend on
		 * number of lines that are mutually subsequently perpendicular and parallel. 
		 * But instead of that, it will be limited to only 2 levels. That means following:
		 * 		1A. if given line is perpendicular to base line a which contains given point, that line a is returned;
		 * 		2A. if base line a of given perpendicular line is parallel to some base line b which contains given point, that line b is returned;
		 * 		1B. if given line is parallel to a perpendicular line whose baseline contains the given point, that base line is retrieved.
		 */
		
		Line perpLBaseLine = null;
		if (baseLine instanceof PerpendicularLine) {
			PerpendicularLine perpL = (PerpendicularLine)baseLine;
			perpLBaseLine = perpL.getBaseLine();
		}
		else if (baseLine instanceof PerpendicularBisector) {
			PerpendicularBisector perpL = (PerpendicularBisector)baseLine;
			Point ptA = perpL.getSegment().getFirstEndPoint();
			Point ptB = perpL.getSegment().getSecondEndPoint();
			
			for (GeoConstruction gc : this.thmProtocol.getConstructionSteps()) {
				if (gc instanceof Line) {
					Line l = (Line)gc;
					Vector<Point> pts = l.getPoints();
					
					if (pts.contains(ptA) && pts.contains(ptB)) {
						perpLBaseLine = l;
						break;
					}
				}
			}
		}
		
		if (perpLBaseLine != null) { // baseLine is some kind of perpendicular line
			if (perpLBaseLine.getPoints().contains(perpLinePt))
				return perpLBaseLine;
			if (perpLBaseLine instanceof ParallelLine) {
				ParallelLine paraL = (ParallelLine)perpLBaseLine;
				Line paraLBaseLine = paraL.getBaseLine();
				
				if (paraLBaseLine.getPoints().contains(perpLinePt))
					return paraLBaseLine;
			}
			else {
				for (GeoConstruction gc : this.thmProtocol.getConstructionSteps()) {
					if (gc instanceof ParallelLine) {
						ParallelLine pl = (ParallelLine)gc;
						Line plBaseLine = pl.getBaseLine();
						
						if (plBaseLine.getGeoObjectLabel() == perpLBaseLine.getGeoObjectLabel() && 
							pl.getPoints().contains(perpLinePt))
							return plBaseLine;
					}
				}
			}
		}
		else if (baseLine instanceof ParallelLine) {
			ParallelLine paraL = (ParallelLine)baseLine;
			Line paraLBaseLine = paraL.getBaseLine();
			
			if (paraLBaseLine instanceof PerpendicularLine) {
				PerpendicularLine perpL = (PerpendicularLine)paraLBaseLine;
				Line perpLBaseLine2 = perpL.getBaseLine();
				
				if (perpLBaseLine2.getPoints().contains(perpLinePt))
					return perpLBaseLine2;
			}
			else {
				for (GeoConstruction gc : this.thmProtocol.getConstructionSteps()) {
					if (gc instanceof PerpendicularLine) {
						PerpendicularLine pl = (PerpendicularLine)gc;
						Line plBaseLine = pl.getBaseLine();
						
						if (plBaseLine.getGeoObjectLabel() == paraLBaseLine.getGeoObjectLabel() && 
							pl.getPoints().contains(perpLinePt))
							return plBaseLine;
					}
				}
			}
		}
		else {
			for (GeoConstruction gc : this.thmProtocol.getConstructionSteps()) {
				if (gc instanceof PerpendicularLine) {
					PerpendicularLine pl = (PerpendicularLine)gc;
					Line plBaseLine = pl.getBaseLine();
					
					if (plBaseLine.getGeoObjectLabel() == baseLine.getGeoObjectLabel() && plBaseLine.getPoints().contains(perpLinePt))
						return plBaseLine;
				}
			}
		}
		
		return null;
	}
		
	/**
	 * Method which finds intersection point of two lines in theorem protocol, among constructed points.
	 * 
	 * @param line1		First line.
	 * @param line2		Second line.
	 * @return			Constructed intersection point of two lines or null if it is not found. 
	 */
	private Point findIntersectionPointOfTwoLines(Line line1, Line line2) {
		for (GeoConstruction gc : this.thmProtocol.getConstructionSteps()) {
			if (gc instanceof IntersectionPoint) {
				IntersectionPoint intPt = (IntersectionPoint)gc;
				if ((intPt.getFirstPointSet().getGeoObjectLabel() == line1.getGeoObjectLabel() && intPt.getSecondPointSet().getGeoObjectLabel() == line2.getGeoObjectLabel()) ||
					(intPt.getFirstPointSet().getGeoObjectLabel() == line2.getGeoObjectLabel() && intPt.getSecondPointSet().getGeoObjectLabel() == line1.getGeoObjectLabel()))
					return intPt;
			}
		}
		return null;
	}
	
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
			
			SetOfPoints firstSet = (SetOfPoints)this.thmProtocol.getConstructionMap().get(iArgs.get(0));
			SetOfPoints secondSet = (SetOfPoints)this.thmProtocol.getConstructionMap().get(iArgs.get(1));
			
			IntersectionPoint intPt1 = (oArgs.get(0) != null && oArgs.get(0).length() > 0) ? new IntersectionPoint(oArgs.get(0), firstSet, secondSet) : null;
			
			if (intPt1 == null) {
				logger.error("Failed to contruct first intersection point");
				return null;
			}
			
			if (oArgs.size() == 1)
				return intPt1;

			// oArgs.size() > 1
			Vector<GeoConstruction> consList = new Vector<GeoConstruction>();
			Vector<String> ptLabelList = new Vector<String>();
			
			for (String ptLabel : oArgs) {
				if (ptLabel != null && ptLabel.length() > 0) // skip empty labels - they appear when set of intersection points has less elements than it should in general case
					ptLabelList.add(ptLabel);
			}
			
			if (ptLabelList.size() == 1)
				return intPt1;
			
			consList.add(intPt1);
			if (ptLabelList.size() == 2) {
				String pt2Label = ptLabelList.get(1);
				
				if (firstSet instanceof Circle) {
					Circle c1 = (Circle)firstSet;
					Point center1 = c1.getCenter();
					
					if (center1 == null) {
						CenterOfCircle shCenter1 = new CenterOfCircle("center-" + c1.getGeoObjectLabel(), c1);
						consList.addAll(shCenter1.getShortcutListOfConstructions());
						center1 = shCenter1.getPoint();
					}

					if (secondSet instanceof Line) {
						Line l2 = (Line)secondSet;
						
						if (l2.getPoints().contains(center1))
							consList.add(new CentralSymmetricPoint(pt2Label, intPt1, center1));
						else {
							Line n12 = this.findPerpLine(l2, center1);
							if (n12 == null) {
								n12 = new PerpendicularLine("perpLine-" + center1.getGeoObjectLabel() + "-" + l2.getGeoObjectLabel(), l2, center1);
								consList.add(n12);
							}
							Point ip = this.findIntersectionPointOfTwoLines(l2, n12);
							if (ip == null) {
								ip = new IntersectionPoint("intPt-" + l2.getGeoObjectLabel() + "-" + n12.getGeoObjectLabel(), l2, n12);
								consList.add(ip);
							}
							consList.add(new CentralSymmetricPoint(pt2Label, intPt1, ip));
						}
					}
					else if (secondSet instanceof Circle) {
						Circle c2 = (Circle)secondSet;
						Point center2 = c2.getCenter();
						
						if (center2 == null) {
							CenterOfCircle shCenter2 = new CenterOfCircle("center-" + c2.getGeoObjectLabel(), c2);
							consList.addAll(shCenter2.getShortcutListOfConstructions());
							center2 = shCenter2.getPoint();
						}
						
						Line centerLine = new LineThroughTwoPoints("centerLine-" + center1.getGeoObjectLabel() + "-" + center2.getGeoObjectLabel(), center1, center2);
						consList.add(centerLine);
						Line n12 = this.findPerpLine(centerLine, intPt1);
						if (n12 != null) {
							Point ip = this.findIntersectionPointOfTwoLines(centerLine, n12);
							if (ip == null) {
								ip = new IntersectionPoint("intPt-" + centerLine.getGeoObjectLabel() + "-" + n12.getGeoObjectLabel(), centerLine, n12);
								consList.add(ip);
							}
							consList.add(new CentralSymmetricPoint(pt2Label, intPt1, ip));
						}
						else {
							ReflectedPoint shRP2 = new ReflectedPoint(pt2Label, intPt1, centerLine);
							consList.addAll(shRP2.getShortcutListOfConstructions());
						}
					}
					else {
						consList.add(new IntersectionPoint(pt2Label, firstSet, secondSet));
					}
				}
				else if (secondSet instanceof Circle) {
					Circle c2 = (Circle)secondSet;
					Point center2 = c2.getCenter();
					
					if (center2 == null) {
						CenterOfCircle shCenter2 = new CenterOfCircle("center-" + c2.getGeoObjectLabel(), c2);
						consList.addAll(shCenter2.getShortcutListOfConstructions());
						center2 = shCenter2.getPoint();
					}
					
					if (firstSet instanceof Line) {
						Line l1 = (Line)firstSet;
						
						if (l1.getPoints().contains(center2))
							consList.add(new CentralSymmetricPoint(pt2Label, intPt1, center2));
						else {
							Line n21 = this.findPerpLine(l1, center2);
							if (n21 == null) {
								n21 = new PerpendicularLine("perpLine-" + center2.getGeoObjectLabel() + "-" + l1.getGeoObjectLabel(), l1, center2);
								consList.add(n21);
							}
							Point ip = this.findIntersectionPointOfTwoLines(l1, n21);
							if (ip == null) {
								ip = new IntersectionPoint("intPt-" + l1.getGeoObjectLabel() + "-" + n21.getGeoObjectLabel(), l1, n21);
								consList.add(ip);
							}
							consList.add(new CentralSymmetricPoint(pt2Label, intPt1, ip));
						}
					}
					else { // firstSet is not circle and not line
						consList.add(new IntersectionPoint(pt2Label, firstSet, secondSet));
					}
				}
				else {
					consList.add(new IntersectionPoint(pt2Label, firstSet, secondSet));
				}
			}
			else { // > 2
				for (int ii = 1, jj = ptLabelList.size(); ii < jj; ii++)
					consList.add(new IntersectionPoint(ptLabelList.get(ii), firstSet, secondSet));
			}
			
			return new ListOfConstructions(consList);
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
			
			if (iArgs.size() == 1) {
				Line s = (Line)this.thmProtocol.getConstructionMap().get(iArgs.get(0)); // line representing a segment
				Vector<Point> points = s.getPoints();
				return new MidPoint(oArgs.get(0), points.get(0), points.get(1));
			}
			
			// iArgs.size() == 2
			Point pt1 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(0));
			Point pt2 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(1));
			return new MidPoint(oArgs.get(0), pt1, pt2);
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
		 * TODO - The construction of center of conic section will be implemented later.
		 * It exists in OGP for circle, but for conic section it doesn't exist - 
		 * the center of conic section can be retrieved only for special types of conic sections, 
		 * ellipses and hyperbolas, and it will be the origin of Cartesian coordinate system 
		 * in special coordinate assignments when doing instantiation of points, such that 
		 * these conics have canonical equations.
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
			
			if (obj2 instanceof Point)
				return new LineThroughTwoPoints(oArgs.get(0), pt1, (Point)obj2);
			return new ParallelLine(oArgs.get(0), (Line)obj2, pt1);
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
			Line line = (Line)this.thmProtocol.getConstructionMap().get(iArgs.get(1));
			
			return new PerpendicularLine(oArgs.get(0), line, pt);
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
			
			if (iArgs.size() == 1) {
				Line s = (Line)this.thmProtocol.getConstructionMap().get(iArgs.get(0)); // line representing a segment
				Vector<Point> points = s.getPoints();
				return new PerpendicularBisector(oArgs.get(0), points.get(0), points.get(1));
			}
			
			// iArgs.size() == 2
			Point pt1 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(0));
			Point pt2 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(1));
			return new PerpendicularBisector(oArgs.get(0), pt1, pt2);
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
			ArrayList<String> iArgs = ggCmd.getInputArgs();
			ArrayList<String> oArgs = ggCmd.getOutputArgs();
			
			Point ptRay1 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(0));
			Point ptVertex = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(1));
			Point ptRay2 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(2));
			
			return new AngleBisector(oArgs.get(0), ptRay1, ptVertex, ptRay2);
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
		 * It is not yet supported by OGP. Only tangents on conic through given point are currently supported.
		 *
		 * Construction of tangent line requires exactly two input arguments
		 * which are label of a point and label of circle or conic. It produces
		 * one or two tangent lines depending on whether the point is on 
		 * circle/conic or not, but always has two output labels - if has one
		 * tangent, the label of second is empty string.
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
			
			String tanLabel1 = oArgs.get(0);
			String tanLabel2 = oArgs.get(1);
			
			if (tanLabel2.length() == 0)
				return new TangentLine(tanLabel1, pt, conic);
			
			Vector<GeoConstruction> consList = new Vector<GeoConstruction>();
			consList.add(new TangentLine(tanLabel1, pt, conic));
			consList.add(new TangentLine(tanLabel2, pt, conic));
			
			return new ListOfConstructions(consList);
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
			ArrayList<String> iArgs = ggCmd.getInputArgs();
			ArrayList<String> oArgs = ggCmd.getOutputArgs();
			
			Point pt = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(0));
			SetOfPoints conic = (SetOfPoints)this.thmProtocol.getConstructionMap().get(iArgs.get(1));
			
			if (!(conic instanceof Circle) && !(conic instanceof ConicSection)) {
				logger.error("Can't construct polar on object which is not circle and conic section.");
				return null;
			}
			
			return new Polar(oArgs.get(0), pt, conic);
		} catch (ClassCastException ex) {
			logger.error(GeoGebraConstructionConverter.getClassCastExceptionMessage(ggCmd, ex));
			return null;
		}  catch (Exception ex) {
			logger.error("Unexpected exception caught: " + ex.toString());
			return null;
		}
	}
	
	/**
	 * @see com.ogprover.api.converter.GeoGebraConstructionConverter#convertDiameterCmd(com.ogprover.geogebra.command.construction.GeoGebraConstructionCommand)
	 */
	protected GeoConstruction convertDiameterCmd(GeoGebraConstructionCommand ggCmd) {
		/*
		 * TODO - In GeoGebra the "Diameter" construction for selected line and circle/conic gives a line
		 * which connects the center of circle/conic with the pole point of selected line w.r.t. given
		 * circle/conic. The construction of diameter line is not currently supported in OGP for conic sections.
		 * Note: in order to have a center of conic, it is firstly necessary to support special conic sections
		 * (ellipses, hyperbolas and parabolas) since in special choice of coordinate system for these
		 * conics, center of a conic is the origin of coordinate system for ellipses and hyperbolas, and is 
		 * the infinite far point of x-axis for parabola.
		 * 
		 * This construction can be currently done only for circles.
		 */
		
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.validateCmdArguments(ggCmd, 2, 2, 1, 1) == false) {
			logger.error("Failed to validate command: " + DiameterCmd.cmdName);
			return null;
		}
		
		try {
			ArrayList<String> iArgs = ggCmd.getInputArgs();
			ArrayList<String> oArgs = ggCmd.getOutputArgs();
			
			Line line = (Line)this.thmProtocol.getConstructionMap().get(iArgs.get(0));
			SetOfPoints conic = (SetOfPoints)this.thmProtocol.getConstructionMap().get(iArgs.get(1));
			
			if (!(conic instanceof Circle) /*&& !(conic instanceof ConicSection)*/) {
				logger.error("Can't construct diameter line on object which is not circle and conic section.");
				return null;
			}
			
			Circle c = (Circle)conic;
			ShortcutConstruction center = new CenterOfCircle(GeoGebraConstructionConverter.generateRandomLabel(c.getGeoObjectLabel() + "_center"), c);
			ShortcutConstruction pole = new Pole(GeoGebraConstructionConverter.generateRandomLabel(line.getGeoObjectLabel() + "_pole"), line, c);
			Vector<GeoConstruction> consList = new Vector<GeoConstruction>();
			
			for (GeoConstruction gc : center.getShortcutListOfConstructions())
				consList.add(gc);
			for (GeoConstruction gc : pole.getShortcutListOfConstructions())
				consList.add(gc);
			consList.add(new LineThroughTwoPoints(oArgs.get(0), center.getPoint(), pole.getPoint()));
			return new ListOfConstructions(consList);
		} catch (ClassCastException ex) {
			logger.error(GeoGebraConstructionConverter.getClassCastExceptionMessage(ggCmd, ex));
			return null;
		}  catch (Exception ex) {
			logger.error("Unexpected exception caught: " + ex.toString());
			return null;
		}
	}
	
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
				
				return new CircumscribedCircle(oArgs.get(0), pt1, pt2, pt3);
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
				
				return new CircleWithCenterAndRadius(oArgs.get(0), pt, pt1, pt2);
			}
			
			GeoObject geoObj = this.thmProtocol.getConstructionMap().get(label2);
			
			if (geoObj == null) {
				// Check whether second argument is a measure of radius
				try {
					Integer.parseInt(label2);
					
					/*
					 *  Since parsing of number was successful, and since constant numbers/measures
					 *  can't be used in algebraic provers (they deal with symbolic calculus), new
					 *  point will be introduced and the circle will be represented as a circle with
					 *  center and one point.
					 */
					Vector<GeoConstruction> consList = new Vector<GeoConstruction>();
					Point pt1 = new FreePoint(GeoGebraConstructionConverter.generateRandomLabel(oArgs.get(0) + "_pt"));
					consList.add(pt1);
					consList.add(new CircleWithCenterAndPoint(oArgs.get(0), pt, pt1));
					return new ListOfConstructions(consList);
				} catch (NumberFormatException ex) {
					logger.error("Incorrect second input argument for construction of circle.");
					return null;
				}
			}
			
			if (geoObj instanceof Line) { // line represent a segment
				Line l = (Line)geoObj;
				Vector<Point> points = l.getPoints(); 
				return new CircleWithCenterAndRadius(oArgs.get(0), pt, points.get(0), points.get(1));
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
		
		try {
			ArrayList<String> iArgs = ggCmd.getInputArgs();
			ArrayList<String> oArgs = ggCmd.getOutputArgs();
			
			Point pt1 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(0));
			Point pt2 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(1));
			Point pt3 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(2));
			Point pt4 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(3));
			Point pt5 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(4));
			
			return new ConicSectionWithFivePoints(oArgs.get(0), pt1, pt2, pt3, pt4, pt5);
		} catch (ClassCastException ex) {
			logger.error(GeoGebraConstructionConverter.getClassCastExceptionMessage(ggCmd, ex));
			return null;
		}  catch (Exception ex) {
			logger.error("Unexpected exception caught: " + ex.toString());
			return null;
		}
	}
	
	/**
	 * @see com.ogprover.api.converter.GeoGebraConstructionConverter#convertEllipseCmd(com.ogprover.geogebra.command.construction.GeoGebraConstructionCommand)
	 */
	protected GeoConstruction convertEllipseCmd(GeoGebraConstructionCommand ggCmd) {
		/*
		 * TODO - this construction is currently not supported by OGP, therefore a general
		 * conic section will be retrieved.
		 */
		
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.validateCmdArguments(ggCmd, 3, 3, 1, 1) == false) {
			logger.error("Failed to validate command: " + EllipseCmd.cmdName);
			return null;
		}
		
		try {
			ArrayList<String> iArgs = ggCmd.getInputArgs();
			ArrayList<String> oArgs = ggCmd.getOutputArgs();
			
			Point conicPt = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(2)); // third point is a point from conic
			// Remove the current point from CP since it will be redefined
			this.constructionsToRemove.add(conicPt);
			
			Vector<GeoConstruction> consList = new Vector<GeoConstruction>();
			GeneralConicSection conic = new GeneralConicSection(oArgs.get(0));
			consList.add(conic);
			consList.add(new RandomPointFromGeneralConic(conicPt.getGeoObjectLabel(), conic));
			return new ListOfConstructions(consList);
		} catch (ClassCastException ex) {
			logger.error(GeoGebraConstructionConverter.getClassCastExceptionMessage(ggCmd, ex));
			return null;
		}  catch (Exception ex) {
			logger.error("Unexpected exception caught: " + ex.toString());
			return null;
		}
	}
	
	/**
	 * @see com.ogprover.api.converter.GeoGebraConstructionConverter#convertHyperbolaCmd(com.ogprover.geogebra.command.construction.GeoGebraConstructionCommand)
	 */
	protected GeoConstruction convertHyperbolaCmd(GeoGebraConstructionCommand ggCmd) {
		/*
		 * TODO - this construction is currently not supported by OGP, therefore a general
		 * conic section will be retrieved.
		 */
		
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.validateCmdArguments(ggCmd, 3, 3, 1, 1) == false) {
			logger.error("Failed to validate command: " + HyperbolaCmd.cmdName);
			return null;
		}
		
		try {
			ArrayList<String> iArgs = ggCmd.getInputArgs();
			ArrayList<String> oArgs = ggCmd.getOutputArgs();
			
			Point conicPt = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(2)); // third point is a point from conic
			// Remove the current point from CP since it will be redefined
			this.constructionsToRemove.add(conicPt);
			
			Vector<GeoConstruction> consList = new Vector<GeoConstruction>();
			GeneralConicSection conic = new GeneralConicSection(oArgs.get(0));
			consList.add(conic);
			consList.add(new RandomPointFromGeneralConic(conicPt.getGeoObjectLabel(), conic));
			return new ListOfConstructions(consList);
		} catch (ClassCastException ex) {
			logger.error(GeoGebraConstructionConverter.getClassCastExceptionMessage(ggCmd, ex));
			return null;
		}  catch (Exception ex) {
			logger.error("Unexpected exception caught: " + ex.toString());
			return null;
		}
	}
	
	/**
	 * @see com.ogprover.api.converter.GeoGebraConstructionConverter#convertParabolaCmd(com.ogprover.geogebra.command.construction.GeoGebraConstructionCommand)
	 */
	protected GeoConstruction convertParabolaCmd(GeoGebraConstructionCommand ggCmd) {
		/*
		 * TODO - this construction is currently not supported by OGP, therefore a general
		 * conic section will be retrieved.
		 */
		
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.validateCmdArguments(ggCmd, 2, 2, 1, 1) == false) {
			logger.error("Failed to validate command: " + ParabolaCmd.cmdName);
			return null;
		}
		
		ArrayList<String> oArgs = ggCmd.getOutputArgs();
		
		return new GeneralConicSection(oArgs.get(0));
	}
	
	/*
	 * TRANSFORMATIONS
	 */
	
	/**
	 * Method which returns the point object from point construction object.
	 * 
	 * @param gc	Geometry construction of point
	 * @return		Point object or null if error
	 */
	private Point getPointFromConstruction(GeoConstruction gc) {
		if (gc == null)
			return null;
		if (gc instanceof Point)
			return (Point)gc;
		if (gc instanceof ShortcutConstruction)
			return ((ShortcutConstruction)gc).getPoint();
		return null;
	}
	
	/**
	 * Method which gives the list of all common points of two given sets of points.
	 *  
	 * @param set1	First set of points
	 * @param set2	Second set of points
	 * @return		List of common points if they exist (it can be an empty list).
	 */
	private Vector<Point> getCommonPointsOfTwoSets(SetOfPoints set1, SetOfPoints set2) {
		// note: two passed sets of points are not null
		Vector<Point> vpts = new Vector<Point>();
		String label1 = set1.getGeoObjectLabel();
		String label2 = set1.getGeoObjectLabel();
		
		for (GeoConstruction gc : this.thmProtocol.getConstructionSteps()) {
			if (gc instanceof IntersectionPoint) {
				IntersectionPoint intP = (IntersectionPoint)gc;
				String intPLabel1 = intP.getFirstPointSet().getGeoObjectLabel();
				String intPLabel2 = intP.getSecondPointSet().getGeoObjectLabel();
				
				if ((intPLabel1.equals(label1) && intPLabel2.equals(label2)) ||
					(intPLabel1.equals(label2) && intPLabel2.equals(label1)))
					vpts.add(intP);
			}
		}
		
		return vpts;
	}
	
	/**
	 * Method that returns construction object for point which corresponds to specified type of reflection.
	 * 
	 * @param mirroredPtLabel		Label of reflected point.
	 * @param originalPt			Point to be reflected (mirrored).
	 * @param mirrorCenter			Object to reflected given point about.
	 * @return						Corresponding geometry construction for reflected point or null in case of error. 
	 */
	private GeoConstruction getMirrorPointCons(String mirroredPtLabel, Point originalPt, GeoObject mirrorCenter) {
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (originalPt == null || mirrorCenter == null) {
			logger.error("Can't mirror - missing some input element");
			return null;
		}
		
		if (mirroredPtLabel == null || mirroredPtLabel.length() == 0) {
			logger.error("Can't mirror - label of result point is not provided");
			return null;
		}
		
		if (mirrorCenter instanceof Line) {
			return new ReflectedPoint(mirroredPtLabel, originalPt, (Line)mirrorCenter); // safe cast
		}
		
		if (mirrorCenter instanceof Point) {
			return new CentralSymmetricPoint(mirroredPtLabel, originalPt, (Point)mirrorCenter); // safe cast
		}

		if (mirrorCenter instanceof Circle) {
			return new InverseOfPoint(mirroredPtLabel, originalPt, (Circle)mirrorCenter); // safe cast
		}
		
		logger.error("Can't mirror about object which is not line, circle or point");
		return null; // cannot reflect about other objects (maybe later will be added reflection about conic if GG add support for that)
	}
	
	/**
	 * Method for creation of construction objects for mirror of point list.
	 * 
	 * @param ptList			List of points to be mirrored
	 * @param mirrorCenter		Center of reflection
	 * @return					List of new constructions, or null in case of error
	 */
	private Vector<GeoConstruction> mirrorPoints(Vector<Point> ptList, GeoObject mirrorCenter) {
		// note: it is assumed that point list is not null
		
		Vector<GeoConstruction> mptList = new Vector<GeoConstruction>();
		for (Point pt : ptList) {
			String ptLabel = GeoGebraConstructionConverter.generateRandomLabel(pt.getGeoObjectLabel() + "_m");
			GeoConstruction gc = this.getMirrorPointCons(ptLabel, pt, mirrorCenter);
			
			if (gc == null)
				return null;
			mptList.add(gc);
		}
		return mptList;
	}

	/**
	 * @see com.ogprover.api.converter.GeoGebraConstructionConverter#convertMirrorCmd(com.ogprover.geogebra.command.construction.GeoGebraConstructionCommand)
	 */
	protected GeoConstruction convertMirrorCmd(GeoGebraConstructionCommand ggCmd) {
		/*
		 * Mirror command has two input elements - the first is label of object
		 * to be mirrored, and the second is label of object which is center of
		 * reflection and can be line, point or circle.
		 * When the center of reflection is a line, the mirror operation is called
		 * line reflection, when the center is point it is called central symmetry
		 * and when the center is circle it is called circle inversion.
		 * 
		 * An object to be mirrored can be:
		 *  1. a point
		 *  2. a line
		 *  3. a regular polygon
		 *  4. a circle/conic or their part (arc or sector) 
		 *  5. a vector (in case of circle inversion the result is undefined)
		 *  (other objects are mirrored point by point like plain polygons or segments).
		 * 
		 * When the result of mirror operation is undefined, the output label is empty.
		 * Also, in case of circle inversion, when input argument is some conic section
		 * or polygon or polygon line, the result is curve that can't be constructed or
		 * easily expressed in terms of known geometry objects. These constructions are 
		 * simply skipped and empty list of geometry objects is retrieved (do not retrieve
		 * null since null is retrieved in case of some error).
		 * 
		 * Note: if some of point labels for construction of circle or some its part is in 
		 * format "Mirror[(Infinity, Infinity), c]" where c is the label of circle of inversion,
		 * this has to be replaced by the label of circle's center. This happens e.g. when
		 * mirroring a ray AB. Each point A and B is mirrored separately then infinity far
		 * point of that ray which produces undefined result. Then the image of whole ray is
		 * circumcircle arc defined by images of points A, B and the infinity far point (and
		 * that image is the center of circle of inversion).
		 */
		
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.validateCmdArguments(ggCmd, 2, 2, 1, 1) == false) {
			logger.error("Failed to validate command: " + MirrorCmd.cmdName);
			return null;
		}
		
		try {
			ArrayList<String> iArgs = ggCmd.getInputArgs();
			ArrayList<String> oArgs = ggCmd.getOutputArgs();
			Vector<GeoConstruction> consList = new Vector<GeoConstruction>();
			String oLabel = oArgs.get(0);
			
			/*
			 * If the output label is empty - the result of reflection is undefined so skip the object.
			 */
			if (oLabel.length() == 0) {
				logger.warn("Undefined result of mirror operation");
				return new ListOfConstructions(consList); // empty list of constructions
			}
			
			GeoObject objToMirror = this.getGeoObject(iArgs.get(0));
			// Object to be mirrored can be a point, line, circle, regular polygon, conic or vector (segment)
			if (!(objToMirror instanceof Point) && !(objToMirror instanceof Line) && !(objToMirror instanceof PolygonLine) &&
				!(objToMirror instanceof Circle) && !(objToMirror instanceof ConicSection) && !(objToMirror instanceof Segment)) {
				logger.error("Unsupported input object type for mirror operation");
				return null;
			}
			
			GeoObject mirrorCenter = this.getGeoObject(iArgs.get(1));
			// Center of reflection can be a point, line or a circle.
			if (!(mirrorCenter instanceof Line) && !(mirrorCenter instanceof Point) && !(mirrorCenter instanceof Circle)) {
				logger.error("Cannot reflect the object about another object which is not a line, point or circle");
				return null;
			}
			
			String objType = ggCmd.getObjectType();
			if (objType.equals(GeoGebraObject.OBJ_TYPE_CCURVE) ||
				objType.equals(GeoGebraObject.OBJ_TYPE_ICURVE) ||
				objType.equals(GeoGebraObject.OBJ_TYPE_NONE)) {
				logger.error("Unsupported output object type for mirror operation");
				return null;
			}
			
			// If mirror is circle inversion, find or create center of that circle
			Point invCCenter = null;
			if (mirrorCenter instanceof Circle) { // circle inversion
				Circle invC = (Circle)mirrorCenter;
				invCCenter = invC.getCenter();
				
				if (invCCenter == null) {
					ShortcutConstruction sc = new CenterOfCircle(GeoGebraConstructionConverter.generateRandomLabel(invC.getGeoObjectLabel() + "_c"), invC);
					consList.add(sc);
					invCCenter = sc.getPoint(); // must be non-null
				}
			}
			
			/*
			 * Mirror of point
			 */
			if (objToMirror instanceof Point) {
				consList.add(this.getMirrorPointCons(oLabel, (Point)objToMirror, mirrorCenter));
			}		
			/*
			 * Mirror of line
			 */
			else if ((objToMirror instanceof Line) && this.auxiliaryObjectsMap.get(objToMirror.getGeoObjectLabel()) == null) { // this is a line but not a segment or vector
				Line line = (Line)objToMirror;
				Vector<Point> ptsToMirror = this.getOrCreatePointsFromPointSet(line, 2, consList);
				
				if (ptsToMirror == null) {
					logger.error("Failed to get or create points from set of points " + line.getGeoObjectLabel());
					return null;
				}
				
				Vector<GeoConstruction> mpts = this.mirrorPoints(ptsToMirror, mirrorCenter);
				if (mpts == null) {
					logger.error("Failed to mirror point list");
					return null;
				}
				consList.addAll(mpts);
				
				Point mpt1 = this.getPointFromConstruction(mpts.get(0));
				Point mpt2 = this.getPointFromConstruction(mpts.get(1));
				
				if (mirrorCenter instanceof Circle) { // circle inversion
					if (ggCmd.getObjectType().equals(GeoGebraObject.OBJ_TYPE_LINE)) {
						Line mline = new LineThroughTwoPoints(oLabel, mpt1, mpt2);
						if (invCCenter != null)
							mline.addPointToSet(invCCenter); // center of circle of inversion must belong to this line
						consList.add(mline);
						
						// Original line and mirrored line are same sets of points in this case so they should have same list of points
						mline.getPoints().addAll(line.getPoints());
						Vector<Point> linePts = line.getPoints();
						linePts.add(mpt1);
						linePts.add(mpt2);
						if (linePts.indexOf(invCCenter) < 0)
							linePts.add(invCCenter);
					}
					else if (ggCmd.getObjectType().equals(GeoGebraObject.OBJ_TYPE_CONIC)) {
						Circle mcircle = new CircumscribedCircle(oLabel, mpt1, mpt2, invCCenter);
						consList.add(mcircle);
						
						// Mirrored line (i.e. resulting circle) must contain the common points of original line and the circle of inversion
						mcircle.getPoints().addAll(this.getCommonPointsOfTwoSets(line, (Circle)mirrorCenter));
					}
					else {
						logger.error("Incorrect output object type for circle inversion of line " + line.getGeoObjectLabel());
						return null;
					}
				}
				else { // other types of reflections
					consList.add(new LineThroughTwoPoints(oLabel, mpt1, mpt2));
				}
			}
			/*
			 * Mirror of circle
			 */
			else if (objToMirror instanceof Circle) {
				Circle circle = (Circle)objToMirror;
				Vector<Point> ptsToMirror = this.getOrCreatePointsFromPointSet(circle, 3, consList);
				
				if (ptsToMirror == null) {
					logger.error("Failed to get or create points from set of points " + circle.getGeoObjectLabel());
					return null;
				}
				
				Vector<GeoConstruction> mpts = this.mirrorPoints(ptsToMirror, mirrorCenter);
				if (mpts == null) {
					logger.error("Failed to mirror point list");
					return null;
				}
				consList.addAll(mpts);
				
				Point mpt1 = this.getPointFromConstruction(mpts.get(0));
				Point mpt2 = this.getPointFromConstruction(mpts.get(1));
				Point mpt3 = this.getPointFromConstruction(mpts.get(2));
				
				if (mirrorCenter instanceof Circle) { // circle inversion
					if (ggCmd.getObjectType().equals(GeoGebraObject.OBJ_TYPE_LINE)) {
						Line mline = new LineThroughTwoPoints(oLabel, mpt1, mpt2);
						mline.addPointToSet(mpt3);
						consList.add(mline);
						
						// Original circle must contain the center of circle of inversion in this case
						Vector<Point> circlePts = circle.getPoints();
						if (circlePts.indexOf(invCCenter) < 0)
							circlePts.add(invCCenter);
						
						// Mirrored circle (i.e. resulting line) must contain the common points of original circle and the circle of inversion
						mline.getPoints().addAll(this.getCommonPointsOfTwoSets(circle, (Circle)mirrorCenter));
					}
					else if (ggCmd.getObjectType().equals(GeoGebraObject.OBJ_TYPE_CONIC)) {
						Circle mcircle = new CircumscribedCircle(oLabel, mpt1, mpt2, invCCenter);
						// Mirrored circle (i.e. resulting circle) must contain the common points of original circle and the circle of inversion
						mcircle.getPoints().addAll(this.getCommonPointsOfTwoSets(circle, (Circle)mirrorCenter));
						consList.add(mcircle);
					}
					else {
						logger.error("Incorrect output object type for circle inversion of circle " + circle.getGeoObjectLabel());
						return null;
					}
				}
				else { // other types of reflections
					consList.add(new LineThroughTwoPoints(oLabel, mpt1, mpt2));
				}
			}
			/*
			 * Mirror of polygon (note: will be used only for regular polygons)
			 */
			else if (objToMirror instanceof PolygonLine) {
				PolygonLine p = (PolygonLine)objToMirror;
				
				Vector<GeoConstruction> mpts = this.mirrorPoints(p.getPoints(), mirrorCenter);
				if (mpts == null) {
					logger.error("Failed to mirror point list");
					return null;
				}
				consList.addAll(mpts);
				
				Vector<Point> vertices = new Vector<Point>();
				Vector<String> edges = new Vector<String>();
				for (int ii = 0, jj = 1, siz = mpts.size(); ii < siz; ii++, jj++) {
					GeoConstruction gc1 = mpts.get(ii);
					GeoConstruction gc2 = mpts.get(jj % siz);
					
					Point pt1 = this.getPointFromConstruction(gc1);
					if (pt1 == null) {
						logger.error("Failed to get mirrored point");
						return null;
					}
					vertices.add(pt1);
					
					Point pt2 = this.getPointFromConstruction(gc2);
					if (pt2 == null) {
						logger.error("Failed to get mirrored point");
						return null;
					}
						
					StringBuilder sb = new StringBuilder();
					sb.append(pt1.getGeoObjectLabel());
					sb.append(pt2.getGeoObjectLabel());
					sb.append("_l");
					String lineLabel = GeoGebraConstructionConverter.generateRandomLabel(sb.toString());
					consList.add(new LineThroughTwoPoints(lineLabel, pt1, pt2));
					edges.add(lineLabel);
				}
				
				this.auxiliaryObjectsMap.put(oLabel, new PolygonLine(vertices, edges, oLabel));
			}
			/*
			 * Mirror of conic section
			 */
			else if (objToMirror instanceof ConicSection) {
				// Since algebraic prover cannot distinguish among different types of conics, the result of mirror
				// operation for conic section will be general conic section.
				consList.add(new GeneralConicSection(oLabel));
			}
			/*
			 * Mirror of vector
			 */
			else if ((objToMirror instanceof Line) && this.auxiliaryObjectsMap.get(objToMirror.getGeoObjectLabel()) != null) {
				Line line = (Line)objToMirror;
				Vector<Point> pts = line.getPoints();
				Point pt1 = pts.get(0);
				Point pt2 = pts.get(1);
				GeoObject centerObj = null;
				
				// If mirror is central symmetry, simply return the opposite vector i.e. the second end point will be
				// reflected about the first end point.
				if (mirrorCenter instanceof Point) {
					centerObj = pt1;
				}
				// If mirror is line reflection, reflect the second end point about the line through the first end point
				// which is parallel to the given center of reflection.
				else if (mirrorCenter instanceof Line) {
					Line rline = (Line)mirrorCenter;
					Line pline = new ParallelLine(GeoGebraConstructionConverter.generateRandomLabel(rline.getGeoObjectLabel() + "_l"), 
												  rline, pt1);
					consList.add(pline);
					centerObj = pline;
				}
				else {
					logger.error("Unable to mirror vector about object which is not point and line");
					return null;
				}
				
				GeoConstruction mpt2cons = this.getMirrorPointCons(GeoGebraConstructionConverter.generateRandomLabel(pt2.getGeoObjectLabel() + "_m"), 
																   pt2, centerObj);
				consList.add(mpt2cons);
				Point mpt2 = this.getPointFromConstruction(mpt2cons);
				if (mpt2 == null) {
					logger.error("Failed to get mirrored point" + mpt2cons.getGeoObjectLabel());
					return null;
				}
				
				consList.add(new LineThroughTwoPoints(oLabel, pt1, mpt2));
				this.auxiliaryObjectsMap.put(oLabel, new Segment(pt1, mpt2, oLabel)); // represent a vector
			}
			/*
			 * Mirror of other types of objects
			 */
			else {
				logger.error("Incorrect type of input object for mirror operation");
				return null;
			}
			
			return new ListOfConstructions(consList);
		}  catch (ClassCastException ex) {
			logger.error(GeoGebraConstructionConverter.getClassCastExceptionMessage(ggCmd, ex));
			return null;
		}  catch (Exception ex) {
			logger.error("Unexpected exception caught: " + ex.toString());
			return null;
		}
	}
	
	/**
	 * @see com.ogprover.api.converter.GeoGebraConstructionConverter#convertRotateCmd(com.ogprover.geogebra.command.construction.GeoGebraConstructionCommand)
	 */
	protected GeoConstruction convertRotateCmd(GeoGebraConstructionCommand ggCmd) {
		// TODO
		return null;
	}
	
	/**
	 * @see com.ogprover.api.converter.GeoGebraConstructionConverter#convertTranslateCmd(com.ogprover.geogebra.command.construction.GeoGebraConstructionCommand)
	 */
	protected GeoConstruction convertTranslateCmd(GeoGebraConstructionCommand ggCmd) {
		// TODO
		return null;
	}
	
	/**
	 * @see com.ogprover.api.converter.GeoGebraConstructionConverter#convertDilateCmd(com.ogprover.geogebra.command.construction.GeoGebraConstructionCommand)
	 */
	protected GeoConstruction convertDilateCmd(GeoGebraConstructionCommand ggCmd) {
		// TODO
		return null;
	}
	
	/*
	 * PARTIAL AND AUXILIARY OBJECTS
	 */
	
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
		
		try {
			ArrayList<String> iArgs = ggCmd.getInputArgs();
			ArrayList<String> oArgs = ggCmd.getOutputArgs();
			Vector<GeoConstruction> consList = new Vector<GeoConstruction>();
			
			if (iArgs.size() == 3) {
				Point ptRay1 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(0));
				Point ptVertex = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(1));
				Point ptRay2 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(2));
				
				Angle ang = new Angle(ptRay1, ptVertex, ptRay2, oArgs.get(0));
				this.auxiliaryObjectsMap.put(ang.getGeoObjectLabel(), ang);
				return new ListOfConstructions(consList); // return empty list of constructions
			}
			
			// iArgs.size() == 2
			Line line1 = (Line)this.thmProtocol.getConstructionMap().get(iArgs.get(0));
			Line line2 = (Line)this.thmProtocol.getConstructionMap().get(iArgs.get(1));
			
			// Find intersection point of these two lines in CP
			Point intersecPt = null;
			for (GeoConstruction gc : this.thmProtocol.getConstructionSteps()) {
				if (gc instanceof IntersectionPoint) {
					IntersectionPoint intPt = (IntersectionPoint)gc; // safe cast
					
					GeoObject set1 = (GeoObject)intPt.getFirstPointSet();
					GeoObject set2 = (GeoObject)intPt.getSecondPointSet();
						
					if ((set1.getGeoObjectLabel().equals(line1.getGeoObjectLabel()) && set2.getGeoObjectLabel().equals(line2.getGeoObjectLabel())) ||
						(set1.getGeoObjectLabel().equals(line2.getGeoObjectLabel()) && set2.getGeoObjectLabel().equals(line1.getGeoObjectLabel()))) {
							intersecPt = intPt;
							break;
						}
				}
			}
			// If intersection point has not been found create it.
			// TODO - later consider a case when two lines are parallel and create zero angle - OGP currently doesn't support zero angle.  
			if (intersecPt == null) {
				StringBuilder sb = new StringBuilder();
				sb.append(line1.getGeoObjectLabel());
				sb.append(line2.getGeoObjectLabel());
				sb.append("_pt");
				consList.add(new IntersectionPoint(GeoGebraConstructionConverter.generateRandomLabel(sb.toString()), line1, line2));
			}
			
			// Find or create point from first line (different from vertex i.e. the intersection point)
			Point pt1 = null;
			for (Point pt : line1.getPoints()) {
				if (!pt.getGeoObjectLabel().equals(intersecPt.getGeoObjectLabel())) {
					pt1 = pt;
					break;
				}
			}
			if (pt1 == null)
				consList.add(new RandomPointFromLine(GeoGebraConstructionConverter.generateRandomLabel(line1.getGeoObjectLabel() + "_pt"), line1));
			
			// Find or create point from second line (different from vertex i.e. the intersection point)
			Point pt2 = null;
			for (Point pt : line2.getPoints()) {
				if (!pt.getGeoObjectLabel().equals(intersecPt.getGeoObjectLabel())) {
					pt2 = pt;
					break;
				}
			}
			if (pt2 == null)
				consList.add(new RandomPointFromLine(GeoGebraConstructionConverter.generateRandomLabel(line2.getGeoObjectLabel() + "_pt"), line2));

			Angle ang = new Angle(pt1, intersecPt, pt2, oArgs.get(0));
			this.auxiliaryObjectsMap.put(ang.getGeoObjectLabel(), ang);
			return new ListOfConstructions(consList);
		} catch (ClassCastException ex) {
			logger.error(GeoGebraConstructionConverter.getClassCastExceptionMessage(ggCmd, ex));
			return null;
		}  catch (Exception ex) {
			logger.error("Unexpected exception caught: " + ex.toString());
			return null;
		}
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
		logger.info("launching convertPolygonCmd");
		
		if (this.validateCmdArguments(ggCmd, 3, -1, 1, -1) == false) {
			logger.error("Failed to validate command: " + PolygonCmd.cmdName);
			return null;
		}
		
		try {
			ArrayList<String> iArgs = ggCmd.getInputArgs();
			ArrayList<String> oArgs = ggCmd.getOutputArgs();
			Vector<GeoConstruction> consList = new Vector<GeoConstruction>();
			
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
				Point originalPt = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(0)); // point to rotate
				Point center = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(1)); // center of rotation
				vertices.add(originalPt);
				vertices.add(center);
				double angMeasureInDegrees = -(numVertices - 2) * 180.0 / numVertices; // take negative measure i.e. rotate clockwise to order vertices counterclockwise 
				for (int ii = numVertices + 1; ii < oArgs.size(); ii++) { // new vertices are in output array after all edges
					Point vPt = new RotatedPoint(oArgs.get(ii), originalPt, center, angMeasureInDegrees);
					vertices.add(vPt);
					consList.add(vPt);
					// prepare for next rotation
					originalPt = center;
					center = vPt;
				}
			}
			
			// Add lines of polygon edges
			for (int ii = 0, jj = 1; ii < numVertices; ii++, jj++) {
				Point pt1 = vertices.get(ii);
				Point pt2 = vertices.get(jj % numVertices);
				String lineLabel = oArgs.get(ii + 1); 
				consList.add(new LineThroughTwoPoints(lineLabel, pt1, pt2));
				edges.add(lineLabel);
			}
			
			PolygonLine poly = new PolygonLine(vertices, edges, oLabel);
			this.auxiliaryObjectsMap.put(oLabel, poly);
			return new ListOfConstructions(consList);
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
			ArrayList<String> oArgs = ggCmd.getOutputArgs();
			Vector<GeoConstruction> consList = new Vector<GeoConstruction>();
			
			String oLabel = oArgs.get(0);
			Vector<Point> vertices = new Vector<Point>();
			Vector<String> edges = new Vector<String>();
			
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
				consList.add(new LineThroughTwoPoints(lineLabel, pt1, pt2));
				edges.add(lineLabel);
			}
			
			PolygonLine poly = new PolygonLine(vertices, edges, oLabel);
			this.auxiliaryObjectsMap.put(oLabel, poly);
			return new ListOfConstructions(consList);
		} catch (ClassCastException ex) {
			logger.error(GeoGebraConstructionConverter.getClassCastExceptionMessage(ggCmd, ex));
			return null;
		}  catch (Exception ex) {
			logger.error("Unexpected exception caught: " + ex.toString());
			return null;
		}
	}
	
	/**
	 * @see com.ogprover.api.converter.GeoGebraConstructionConverter#convertCircleArcCmd(com.ogprover.geogebra.command.construction.GeoGebraConstructionCommand)
	 */
	protected GeoConstruction convertCircleArcCmd(GeoGebraConstructionCommand ggCmd) {
		/*
		 * Circle arc is defined by three points - the first is center; the third is on ray that connects
		 * center with second end of an arc.
		 * It will be stored as a circle since algebraic provers can't distinguish circle arcs. 
		 */
		
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.validateCmdArguments(ggCmd, 3, 3, 1, 1) == false) {
			logger.error("Failed to validate command: " + CircleArcCmd.cmdName);
			return null;
		}
		
		try {
			ArrayList<String> iArgs = ggCmd.getInputArgs();
			ArrayList<String> oArgs = ggCmd.getOutputArgs();
			Point pt1 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(0));
			Point pt2 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(1));
			return new CircleWithCenterAndPoint(oArgs.get(0), pt1, pt2);
		} catch (ClassCastException ex) {
			logger.error(GeoGebraConstructionConverter.getClassCastExceptionMessage(ggCmd, ex));
			return null;
		}  catch (Exception ex) {
			logger.error("Unexpected exception caught: " + ex.toString());
			return null;
		}
	}
	
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
			ArrayList<String> iArgs = ggCmd.getInputArgs();
			ArrayList<String> oArgs = ggCmd.getOutputArgs();
			Point pt1 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(0));
			Point pt2 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(1));
			Point pt3 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(2));
			return new CircumscribedCircle(oArgs.get(0), pt1, pt2, pt3);
		} catch (ClassCastException ex) {
			logger.error(GeoGebraConstructionConverter.getClassCastExceptionMessage(ggCmd, ex));
			return null;
		}  catch (Exception ex) {
			logger.error("Unexpected exception caught: " + ex.toString());
			return null;
		}
	}
	
	/**
	 * @see com.ogprover.api.converter.GeoGebraConstructionConverter#convertCircleSectorCmd(com.ogprover.geogebra.command.construction.GeoGebraConstructionCommand)
	 */
	protected GeoConstruction convertCircleSectorCmd(GeoGebraConstructionCommand ggCmd) {
		/*
		 * Circle sector is defined by three points - the first is center; the third is on ray that connects
		 * center with second end of a sector arc.
		 * It will be stored as a circle since algebraic provers can't distinguish circle arcs. 
		 */
		
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.validateCmdArguments(ggCmd, 3, 3, 1, 1) == false) {
			logger.error("Failed to validate command: " + CircleSectorCmd.cmdName);
			return null;
		}
		
		try {
			ArrayList<String> iArgs = ggCmd.getInputArgs();
			ArrayList<String> oArgs = ggCmd.getOutputArgs();
			Point pt1 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(0));
			Point pt2 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(1));
			return new CircleWithCenterAndPoint(oArgs.get(0), pt1, pt2);
		} catch (ClassCastException ex) {
			logger.error(GeoGebraConstructionConverter.getClassCastExceptionMessage(ggCmd, ex));
			return null;
		}  catch (Exception ex) {
			logger.error("Unexpected exception caught: " + ex.toString());
			return null;
		}
	}
	
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
			ArrayList<String> iArgs = ggCmd.getInputArgs();
			ArrayList<String> oArgs = ggCmd.getOutputArgs();
			Point pt1 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(0));
			Point pt2 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(1));
			Point pt3 = (Point)this.thmProtocol.getConstructionMap().get(iArgs.get(2));
			return new CircumscribedCircle(oArgs.get(0), pt1, pt2, pt3);
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
		 * It will be stored as a circle since algebraic provers can't distinguish circle arcs. 
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
			return new CircleWithDiameter(oArgs.get(0), pt1, pt2);
		} catch (ClassCastException ex) {
			logger.error(GeoGebraConstructionConverter.getClassCastExceptionMessage(ggCmd, ex));
			return null;
		}  catch (Exception ex) {
			logger.error("Unexpected exception caught: " + ex.toString());
			return null;
		}
	}
}
