/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.api;

import java.util.ArrayList;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.prover_protocol.cp.geoconstruction.*;
import com.ogprover.prover_protocol.cp.geoobject.*;
import com.ogprover.prover_protocol.cp.thmstatement.*;
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
	 * @param statText		The text of statement from GeoGebra
	 * @param ggConsConv	Construction converter with geometry objects
	 */
	public GGStatConverterForAlgebraicProvers(String statText, GeoGebraConstructionConverter ggConsConv) {
		super(statText, ggConsConv);
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * @see com.ogprover.api.GeoGebraStatementConverter#convertCollinear()
	 */
	@Override
	public ThmStatement convertCollinear() {
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		// This statement requires three input points.
		if (this.statementArgs.size() != 3) {
			logger.error("Failed to convert statement - incorrect number of arguments");
			this.bSuccess = false;
			return null;
		}
		
		try {
			Point p1 = (Point)this.consConverter.getConsProtocol().getConstructionMap().get(this.statementArgs.get(0));
			Point p2 = (Point)this.consConverter.getConsProtocol().getConstructionMap().get(this.statementArgs.get(1));
			Point p3 = (Point)this.consConverter.getConsProtocol().getConstructionMap().get(this.statementArgs.get(2));
			
			if (p1 == null || p2 == null || p3 == null) {
				logger.error("Failed to convert statement - missing input argument");
				this.bSuccess = false;
				return null;
			}
			
			ArrayList<Point> alp = new ArrayList<Point>();
			alp.add(p1);
			alp.add(p2);
			alp.add(p3);
			this.ogpStatement = new CollinearPoints(alp);
			return this.ogpStatement;
		} catch (ClassCastException ex) {
			logger.error("Failed to convert statement due to following reason: " + ex.toString());
			this.bSuccess = false;
			return null;
		}  catch (Exception ex) {
			logger.error("Failed to convert statement - unexpected exception caught: " + ex.toString());
			this.bSuccess = false;
			return null;
		}
	}

	/**
	 * @see com.ogprover.api.GeoGebraStatementConverter#convertConcurrent()
	 */
	@Override
	public ThmStatement convertConcurrent() {
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		// This statement requires three input lines or circles.
		if (this.statementArgs.size() != 3) {
			logger.error("Failed to convert statement - incorrect number of arguments");
			this.bSuccess = false;
			return null;
		}
		
		try {
			GeoObject gobj1 = this.consConverter.getConsProtocol().getConstructionMap().get(this.statementArgs.get(0));
			GeoObject gobj2 = this.consConverter.getConsProtocol().getConstructionMap().get(this.statementArgs.get(1));
			GeoObject gobj3 = this.consConverter.getConsProtocol().getConstructionMap().get(this.statementArgs.get(2));
			
			if (gobj1 == null || gobj2 == null || gobj3 == null) {
				logger.error("Failed to convert statement - missing input argument");
				this.bSuccess = false;
				return null;
			}
			
			if (gobj1 instanceof Line) {
				ArrayList<Line> all = new ArrayList<Line>();
				all.add((Line)gobj1);
				all.add((Line)gobj2);
				all.add((Line)gobj3);
				this.ogpStatement = new ConcurrentLines(all);
				return this.ogpStatement;
			}
			
			if (gobj1 instanceof Circle) {
				ArrayList<Circle> alc = new ArrayList<Circle>();
				alc.add((Circle)gobj1);
				alc.add((Circle)gobj2);
				alc.add((Circle)gobj3);
				this.ogpStatement = new ConcurrentCircles(alc);
				return this.ogpStatement;
			}
			
			logger.error("Failed to convert statement - unsupported input argument type");
			this.bSuccess = false;
			return null;
		} catch (ClassCastException ex) {
			logger.error("Failed to convert statement due to following reason: " + ex.toString());
			this.bSuccess = false;
			return null;
		}  catch (Exception ex) {
			logger.error("Failed to convert statement - unexpected exception caught: " + ex.toString());
			this.bSuccess = false;
			return null;
		}
	}

	/**
	 * @see com.ogprover.api.GeoGebraStatementConverter#convertEqual()
	 */
	@Override
	public ThmStatement convertEqual() {
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		// This statement requires two input points or segments or angles.
		if (this.statementArgs.size() != 2) {
			logger.error("Failed to convert statement - incorrect number of arguments");
			this.bSuccess = false;
			return null;
		}
		
		try {
			GeoObject gobj1 = this.consConverter.getConsProtocol().getConstructionMap().get(this.statementArgs.get(0));
			GeoObject gobj2 = this.consConverter.getConsProtocol().getConstructionMap().get(this.statementArgs.get(1));
			
			if (gobj1 == null) {
				// try with auxiliary geometry objects
				gobj1 = this.consConverter.getAuxiliaryObjectsMap().get(this.statementArgs.get(0));
				gobj2 = this.consConverter.getAuxiliaryObjectsMap().get(this.statementArgs.get(1));
			}
			
			if (gobj1 == null || gobj2 == null) {
				logger.error("Failed to convert statement - missing input argument");
				this.bSuccess = false;
				return null;
			}
			
			if (gobj1 instanceof Point) {
				this.ogpStatement = new IdenticalPoints((Point)gobj1, (Point)gobj2);
				return this.ogpStatement;
			}
			
			if (gobj1 instanceof Segment) {
				this.ogpStatement = new SegmentsOfEqualLengths((Segment)gobj1, (Segment)gobj2);
				return this.ogpStatement;
			}
			
			if (gobj1 instanceof Angle) {
				this.ogpStatement = new EqualAngles((Angle)gobj1, (Angle)gobj2);
				return this.ogpStatement;
			}
			
			logger.error("Failed to convert statement - unsupported input argument type");
			this.bSuccess = false;
			return null;
		} catch (ClassCastException ex) {
			logger.error("Failed to convert statement due to following reason: " + ex.toString());
			this.bSuccess = false;
			return null;
		}  catch (Exception ex) {
			logger.error("Failed to convert statement - unexpected exception caught: " + ex.toString());
			this.bSuccess = false;
			return null;
		}
	}

	/**
	 * @see com.ogprover.api.GeoGebraStatementConverter#convertParallel()
	 */
	@Override
	public ThmStatement convertParallel() {
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		// This statement requires two input lines.
		if (this.statementArgs.size() != 2) {
			logger.error("Failed to convert statement - incorrect number of arguments");
			this.bSuccess = false;
			return null;
		}
		
		try {
			Line l1 = (Line)this.consConverter.getConsProtocol().getConstructionMap().get(this.statementArgs.get(0));
			Line l2 = (Line)this.consConverter.getConsProtocol().getConstructionMap().get(this.statementArgs.get(1));
			
			if (l1 == null || l2 == null) {
				logger.error("Failed to convert statement - missing input argument");
				this.bSuccess = false;
				return null;
			}
			
			this.ogpStatement = new TwoParallelLines(l1, l2);
			return this.ogpStatement;
		} catch (ClassCastException ex) {
			logger.error("Failed to convert statement due to following reason: " + ex.toString());
			this.bSuccess = false;
			return null;
		}  catch (Exception ex) {
			logger.error("Failed to convert statement - unexpected exception caught: " + ex.toString());
			this.bSuccess = false;
			return null;
		}
	}

	/**
	 * @see com.ogprover.api.GeoGebraStatementConverter#convertPerpendicular()
	 */
	@Override
	public ThmStatement convertPerpendicular() {
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		// This statement requires two input lines.
		if (this.statementArgs.size() != 2) {
			logger.error("Failed to convert statement - incorrect number of arguments");
			this.bSuccess = false;
			return null;
		}
		
		try {
			Line l1 = (Line)this.consConverter.getConsProtocol().getConstructionMap().get(this.statementArgs.get(0));
			Line l2 = (Line)this.consConverter.getConsProtocol().getConstructionMap().get(this.statementArgs.get(1));
			
			if (l1 == null || l2 == null) {
				logger.error("Failed to convert statement - missing input argument");
				this.bSuccess = false;
				return null;
			}
			
			this.ogpStatement = new TwoPerpendicularLines(l1, l2);
			return this.ogpStatement;
		} catch (ClassCastException ex) {
			logger.error("Failed to convert statement due to following reason: " + ex.toString());
			this.bSuccess = false;
			return null;
		}  catch (Exception ex) {
			logger.error("Failed to convert statement - unexpected exception caught: " + ex.toString());
			this.bSuccess = false;
			return null;
		}
	}
}