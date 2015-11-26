/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.api.converter;

import java.util.ArrayList;

import com.ogprover.geogebra.command.statement.BooleanCmd;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.pp.tp.geoconstruction.*;
import com.ogprover.pp.tp.geoobject.*;
import com.ogprover.pp.tp.thmstatement.*;
import com.ogprover.utilities.logger.ILogger;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for converter of GeoGebra's statements, used for
*     algebraic provers.</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class GGStatConverterForAlgebraicProvers extends GeoGebraStatementConverter {
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
	public GGStatConverterForAlgebraicProvers(GeoGebraTheoremConverter ggThmCnv) {
		super(ggThmCnv);
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * @see com.ogprover.api.converter.GeoGebraStatementConverter#convertBoolean()
	 */
	@Override
	public ThmStatement convertBoolean() {
		ILogger logger = OpenGeoProver.settings.getLogger();
		ArrayList<String> statementArgs = this.ggStatCmd.getInputArgs();
		
		// This statement requires one input argument - the boolean text - true or false.
		if (statementArgs.size() != 1) {
			logger.error("Failed to convert statement - incorrect number of arguments");
			return null;
		}
		
		String statementText = (new String(statementArgs.get(0))).toUpperCase();
		if (statementText.equals(BooleanCmd.CMD_TEXT_TRUE))
			return new True(this.thmProtocol);
		if (statementText.equals(BooleanCmd.CMD_TEXT_FALSE))
			return new False(this.thmProtocol);
		logger.error("Failed to convert statement - incorrect boolean statement");
		return null;
	}
	
	/**
	 * @see com.ogprover.api.converter.GeoGebraStatementConverter#convertCollinear()
	 */
	@Override
	public ThmStatement convertCollinear() {
		ILogger logger = OpenGeoProver.settings.getLogger();
		ArrayList<String> statementArgs = this.ggStatCmd.getInputArgs();
		
		// This statement requires three input points.
		if (statementArgs.size() != 3) {
			logger.error("Failed to convert statement - incorrect number of arguments");
			return null;
		}
		
		try {
			Point p1 = (Point)this.thmProtocol.getConstructionMap().get(statementArgs.get(0));
			Point p2 = (Point)this.thmProtocol.getConstructionMap().get(statementArgs.get(1));
			Point p3 = (Point)this.thmProtocol.getConstructionMap().get(statementArgs.get(2));
			
			if (p1 == null || p2 == null || p3 == null) {
				logger.error("Failed to convert statement - missing input argument");
				return null;
			}
			
			ArrayList<Point> alp = new ArrayList<Point>();
			alp.add(p1);
			alp.add(p2);
			alp.add(p3);
			return new CollinearPoints(alp);
		} catch (ClassCastException ex) {
			logger.error("Failed to convert statement due to following reason: " + ex.toString());
			return null;
		}  catch (Exception ex) {
			logger.error("Failed to convert statement - unexpected exception caught: " + ex.toString());
			return null;
		}
	}
	
	/**
	 * @see com.ogprover.api.converter.GeoGebraStatementConverter#convertConcyclic()
	 */
	@Override
	public ThmStatement convertConcyclic() {
		ILogger logger = OpenGeoProver.settings.getLogger();
		ArrayList<String> statementArgs = this.ggStatCmd.getInputArgs();
		
		// This statement requires four input points.
		if (statementArgs.size() != 4) {
			logger.error("Failed to convert statement - incorrect number of arguments");
			return null;
		}
		
		try {
			Point p1 = (Point)this.thmProtocol.getConstructionMap().get(statementArgs.get(0));
			Point p2 = (Point)this.thmProtocol.getConstructionMap().get(statementArgs.get(1));
			Point p3 = (Point)this.thmProtocol.getConstructionMap().get(statementArgs.get(2));
			Point p4 = (Point)this.thmProtocol.getConstructionMap().get(statementArgs.get(3));
			
			if (p1 == null || p2 == null || p3 == null || p4 == null) {
				logger.error("Failed to convert statement - missing input argument");
				return null;
			}
			
			ArrayList<Point> alp = new ArrayList<Point>();
			alp.add(p1);
			alp.add(p2);
			alp.add(p3);
			alp.add(p4);
			return new ConcyclicPoints(alp);
		} catch (ClassCastException ex) {
			logger.error("Failed to convert statement due to following reason: " + ex.toString());
			return null;
		}  catch (Exception ex) {
			logger.error("Failed to convert statement - unexpected exception caught: " + ex.toString());
			return null;
		}
	}

	/**
	 * @see com.ogprover.api.converter.GeoGebraStatementConverter#convertConcurrent()
	 */
	@Override
	public ThmStatement convertConcurrent() {
		ILogger logger = OpenGeoProver.settings.getLogger();
		ArrayList<String> statementArgs = this.ggStatCmd.getInputArgs();
		
		// This statement requires three input lines or circles.
		if (statementArgs.size() != 3) {
			logger.error("Failed to convert statement - incorrect number of arguments");
			return null;
		}
		
		try {
			GeoObject gobj1 = this.thmProtocol.getConstructionMap().get(statementArgs.get(0));
			GeoObject gobj2 = this.thmProtocol.getConstructionMap().get(statementArgs.get(1));
			GeoObject gobj3 = this.thmProtocol.getConstructionMap().get(statementArgs.get(2));
			
			if (gobj1 == null || gobj2 == null || gobj3 == null) {
				logger.error("Failed to convert statement - missing input argument");
				return null;
			}
			
			if (gobj1 instanceof Line) {
				ArrayList<Line> all = new ArrayList<Line>();
				all.add((Line)gobj1);
				all.add((Line)gobj2);
				all.add((Line)gobj3);
				return new ConcurrentLines(all);
			}
			
			if (gobj1 instanceof Circle) {
				ArrayList<Circle> alc = new ArrayList<Circle>();
				alc.add((Circle)gobj1);
				alc.add((Circle)gobj2);
				alc.add((Circle)gobj3);
				return new ConcurrentCircles(alc);
			}
			
			logger.error("Failed to convert statement - unsupported input argument type");
			return null;
		} catch (ClassCastException ex) {
			logger.error("Failed to convert statement due to following reason: " + ex.toString());
			return null;
		}  catch (Exception ex) {
			logger.error("Failed to convert statement - unexpected exception caught: " + ex.toString());
			return null;
		}
	}

	/**
	 * @see com.ogprover.api.converter.GeoGebraStatementConverter#convertEqual()
	 */
	@Override
	public ThmStatement convertEqual() {
		ILogger logger = OpenGeoProver.settings.getLogger();
		ArrayList<String> statementArgs = this.ggStatCmd.getInputArgs();
		
		// This statement requires two input points or segments or angles.
		if (statementArgs.size() != 2) {
			logger.error("Failed to convert statement - incorrect number of arguments");
			return null;
		}
		
		try {
			// First search among auxiliary objects since segments are added in CP as lines
			GeoObject gobj1 = this.auxiliaryObjectsMap.get(statementArgs.get(0));
			GeoObject gobj2 = this.auxiliaryObjectsMap.get(statementArgs.get(1));
			
			if (gobj1 == null || gobj2 == null) {
				// try with constructed geometry objects
				gobj1 = this.thmProtocol.getConstructionMap().get(statementArgs.get(0));
				gobj2 = this.thmProtocol.getConstructionMap().get(statementArgs.get(1));
			}
			
			if (gobj1 == null || gobj2 == null) {
				logger.error("Failed to convert statement - missing input argument");
				return null;
			}
			
			if (gobj1 instanceof Point) {
				return new IdenticalPoints((Point)gobj1, (Point)gobj2);
			}
			
			if (gobj1 instanceof Segment) {
				return new SegmentsOfEqualLengths((Segment)gobj1, (Segment)gobj2);
			}
			
			if (gobj1 instanceof Angle) {
				return new EqualAngles((Angle)gobj1, (Angle)gobj2);
			}
			
			logger.error("Failed to convert statement - unsupported input argument type");
			return null;
		} catch (ClassCastException ex) {
			logger.error("Failed to convert statement due to following reason: " + ex.toString());
			return null;
		}  catch (Exception ex) {
			logger.error("Failed to convert statement - unexpected exception caught: " + ex.toString());
			return null;
		}
	}

	/**
	 * @see com.ogprover.api.converter.GeoGebraStatementConverter#convertParallel()
	 */
	@Override
	public ThmStatement convertParallel() {
		ILogger logger = OpenGeoProver.settings.getLogger();
		ArrayList<String> statementArgs = this.ggStatCmd.getInputArgs();
		
		// This statement requires two input lines.
		if (statementArgs.size() != 2) {
			logger.error("Failed to convert statement - incorrect number of arguments");
			return null;
		}
		
		try {
			Line l1 = (Line)this.thmProtocol.getConstructionMap().get(statementArgs.get(0));
			Line l2 = (Line)this.thmProtocol.getConstructionMap().get(statementArgs.get(1));
			
			if (l1 == null || l2 == null) {
				logger.error("Failed to convert statement - missing input argument");
				return null;
			}
			return new TwoParallelLines(l1, l2);
		} catch (ClassCastException ex) {
			logger.error("Failed to convert statement due to following reason: " + ex.toString());
			return null;
		}  catch (Exception ex) {
			logger.error("Failed to convert statement - unexpected exception caught: " + ex.toString());
			return null;
		}
	}

	/**
	 * @see com.ogprover.api.converter.GeoGebraStatementConverter#convertPerpendicular()
	 */
	@Override
	public ThmStatement convertPerpendicular() {
		ILogger logger = OpenGeoProver.settings.getLogger();
		ArrayList<String> statementArgs = this.ggStatCmd.getInputArgs();
		
		// This statement requires two input lines.
		if (statementArgs.size() != 2) {
			logger.error("Failed to convert statement - incorrect number of arguments");
			return null;
		}
		
		try {
			Line l1 = (Line)this.thmProtocol.getConstructionMap().get(statementArgs.get(0));
			Line l2 = (Line)this.thmProtocol.getConstructionMap().get(statementArgs.get(1));
			
			if (l1 == null || l2 == null) {
				logger.error("Failed to convert statement - missing input argument");
				return null;
			}
			return new TwoPerpendicularLines(l1, l2);
		} catch (ClassCastException ex) {
			logger.error("Failed to convert statement due to following reason: " + ex.toString());
			return null;
		}  catch (Exception ex) {
			logger.error("Failed to convert statement - unexpected exception caught: " + ex.toString());
			return null;
		}
	}
}