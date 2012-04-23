/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.utilities.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.xml.sax.SAXException;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.prover_protocol.cp.OGPCP;
import com.ogprover.prover_protocol.cp.geoconstruction.*;
import com.ogprover.prover_protocol.cp.geoobject.*;


/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for handler of XML document used in parsing by usage of QDParser</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */
public class OGPDocHandler implements DocHandler {

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
	 * Static constants for type of current XML tag in processing.
	 */
	public static final int TAG_TYPE_UNKNOWN = -1; // used for errors - when unknown tag is encountered
	public static final int TAG_TYPE_NONE = 0; // this is mark for start of parsing
	public static final int TAG_TYPE_CONSTRUCTION = 1;
	public static final int TAG_TYPE_ELEMENT = 2;
	public static final int TAG_TYPE_COMMAND = 3;
	public static final int TAG_TYPE_SUB_ELEMENT = 4;
	public static final int TAG_TYPE_SUB_COMMAND = 5;
	public static final int TAG_TYPE_EXPRESSION = 6;
	
	/**
	 * Static constants for names of relevant tags.
	 */
	public static final String TAG_NAME_CONSTRUCTION = "construction";
	public static final String TAG_NAME_ELEMENT = "element";
	public static final String TAG_NAME_COMMAND = "command";
	public static final String TAG_NAME_INPUT = "input";
	public static final String TAG_NAME_OUTPUT = "output";
	public static final String TAG_NAME_EXPRESSION = "expression";
	
	/**
	 * Static constants for names of relevant attributes.
	 */
	public static final String ATTR_NAME_TITLE = "title";
	public static final String ATTR_NAME_LABEL = "label";
	public static final String ATTR_NAME_NAME = "name";
	public static final String ATTR_NAME_TYPE = "type";
	// generic arguments
	public static final String ATTR_NAME_GEN = "a"; // base name of all generic arguments: a0, a1, a2, a3, a4, ...
	
	/**
	 * Static constants for types of geometry objects described in <element> tag.
	 */
	public static final String ELEMENT_TYPE_NONE = "";
	public static final String ELEMENT_TYPE_POINT = "point";
	public static final String ELEMENT_TYPE_LINE = "line";
	public static final String ELEMENT_TYPE_CONIC = "conic";
	public static final String ELEMENT_TYPE_ANGLE = "angle";
	public static final String ELEMENT_TYPE_SEGMENT = "segment";
	public static final String ELEMENT_TYPE_VECTOR = "vector";
	public static final String ELEMENT_TYPE_POLYGON = "polygon";
	public static final String ELEMENT_TYPE_POLYLINE = "polyline";
	public static final String ELEMENT_TYPE_RAY = "ray";
	public static final String ELEMENT_TYPE_NUMERIC = "numeric"; // this is not a type of geometry object but definition of numeric constant
	
	/**
	 * Static constants for command names.
	 */
	// Points
	public static final String COMMAND_INTERSECT = "Intersect";
	public static final String COMMAND_MIDPOINT = "Midpoint";
	public static final String COMMAND_POINT = "Point"; // Random point on some set of points (line, circle, segment, polygon)
	public static final String COMMAND_POINT_IN = "PointIn"; // Random point inside polygon
	// Lines
	public static final String COMMAND_LINE = "Line"; // Line through two points or parallel line
	public static final String COMMAND_ORT_LINE = "OrthogonalLine";
	public static final String COMMAND_LINE_BISECTOR = "LineBisector"; // Perpendicular bisector of segment
	public static final String COMMAND_ANG_BISECTOR = "AngularBisector";
	public static final String COMMAND_TANGENT = "Tangent";
	public static final String COMMAND_POLAR = "Polar";
	// Conics
	public static final String COMMAND_CIRCLE = "Circle"; // all types of circles
	public static final String COMMAND_CONIC = "Conic"; // conic through five points
	public static final String COMMAND_ELLIPSE = "Ellipse";
	public static final String COMMAND_HYPERBOLA = "Hyperbola";
	public static final String COMMAND_PARABOLA = "Parabola";
	// Transformations
	public static final String COMMAND_MIRROR = "Mirror"; // all types of reflections (w.r.t line or point or circle - inversion)
	public static final String COMMAND_ROTATE = "Rotate";
	public static final String COMMAND_TRANSLATE = "Translate";
	public static final String COMMAND_DILATE = "Dilate";
	// Other auxiliary geometry objects
	public static final String COMMAND_SEGMENT = "Segment";
	public static final String COMMAND_POLYGON = "Polygon";
	public static final String COMMAND_POLYLINE = "PolyLine";
	public static final String COMMAND_RAY = "Ray";
	public static final String COMMAND_ANGLE = "Angle";
	public static final String COMMAND_VECTOR = "Vector";
	// TODO - add here other commands ...
	
	// ===== Storage objects for parsed data =====
	/**
	 * Map of geometry objects read from XML file.
	 */
	Map<String, GeoObject> geoObjMap = null;
	/**
	 * Construction Protocol for storage of parsed geometry constructions.
	 */
	private OGPCP consProtocol = null;
	
	// ===== Data members for processing of current tag =====
	/**
	 * The type of XML tag that is currently being processed.
	 */
	private int currentTagType;
	/**
	 * The name of last open XML tag.
	 */
	@SuppressWarnings("unused")
	private String currentTagName;
	
	// ===== Data members for processing of command tag =====
	/**
	 * The last processed geometry object defined by command tag.
	 */
	private GeoObject lastGeoObject;
	/**
	 * The type of last processed geometry object defined by command tag.
	 */
	private String lastGeoObjectType;
	/**
	 * Name of construction defined by command tag being processed.
	 */
	private String currConstructionName;
	/**
	 * Input arguments of current geometry object defined by command tag being processed.
	 */
	private ArrayList<String> currGeoObjInputArgs;
	/**
	 * Output arguments of current geometry object defined by command tag being processed.
	 */
	private ArrayList<String> currGeoObjOutputArgs;
	
	// ===== Parsing result =====
	/**
	 * Flag to determine whether parsing was successful - by default it is TRUE but if during parsing of XML tags
	 * an error is encountered, it is set to FALSE.
	 */
	private boolean bSuccess;
	
	// ===== Other auxiliary data members =====
	// ...
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @return the geoObjMap
	 */
	public Map<String, GeoObject> getGeoObjMap() {
		return this.geoObjMap;
	}
	
	/**
	 * @return the consProtocol
	 */
	public OGPCP getConsProtocol() {
		return this.consProtocol;
	}

	/**
	 * @param consProtocol the consProtocol to set
	 */
	public void setConsProtocol(OGPCP consProtocol) {
		this.consProtocol = consProtocol;
	}

	/**
	 * @return the bSuccess
	 */
	public boolean isSuccess() {
		return bSuccess;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method.
	 * 
	 * @param consProtocol	The construction protocol object where 
	 * 						parsed geometry construction objects are stored.
	 */
	public OGPDocHandler(OGPCP consProtocol) {
		this.consProtocol = consProtocol;
		this.geoObjMap = new HashMap<String, GeoObject>();
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	
	/*
	 * Overridden methods of DocHandler interface.
	 */
	
	/**
	 * @see com.ogprover.utilities.io.DocHandler#startElement(java.lang.String, java.util.LinkedHashMap)
	 * 
	 * In case of error flag for success of parsing is set to FALSE.
	 */
	public void startElement(String tag, LinkedHashMap<String, String> h)
			throws Exception {
		if (!this.bSuccess)
			return; // skip the tag if an error has already been encountered
		
		FileLogger logger = OpenGeoProver.settings.getLogger();
		
		// Save the name of current open tag
		this.currentTagName = tag;
		
		// === <construction> ===
		if (tag.equalsIgnoreCase(OGPDocHandler.TAG_NAME_CONSTRUCTION)) {
			if (this.currentTagType != OGPDocHandler.TAG_TYPE_NONE) {
				logger.error("Construction tag encountered when not expected.");
				this.bSuccess = false;
				return;
			}
			
			String constructionTitle = h.get(OGPDocHandler.ATTR_NAME_TITLE);
			if (constructionTitle != null)
				this.consProtocol.setTheoremName(constructionTitle); // Construction title will hold the name of theorem to be proved
			this.currentTagType = OGPDocHandler.TAG_TYPE_CONSTRUCTION;
		}
		// === <element> ===
		else if (tag.equalsIgnoreCase(OGPDocHandler.TAG_NAME_ELEMENT)) {
			if (this.currentTagType != OGPDocHandler.TAG_TYPE_CONSTRUCTION) {
				logger.error("Element tag encountered when not expected.");
				this.bSuccess = false;
				return;
			}
			
			String elementType = h.get(OGPDocHandler.ATTR_NAME_TYPE);
			if (elementType == null) {
				logger.error("Not found \"type\" attribute of element tag.");
				this.bSuccess = false;
				return;
			}
			
			String elementLabel = h.get(OGPDocHandler.ATTR_NAME_LABEL);
			if (elementLabel == null) {
				logger.error("Not found \"label\" attribute of element tag.");
				this.bSuccess = false;
				return;
			}
			
			/*
			 * Check previously constructed geometry object - <element> tags
			 * usually follow the <command> tag: <element> describes the object
			 * introduced with <command>. 
			 * 
			 * For polygons there is one <command> and then set of <element> tags 
			 * for definition of polygon and its edges. For free points there is 
			 * just <element> tag (without previous <command> tag). Also a point
			 * can be defined after <expression> tag but these points are vertices
			 * of special polygons so they will be considered as free points for 
			 * proving purposes if not required differently.
			 */
			
			boolean bDoDefaultCheck = false;
			
			if (elementType.equals(OGPDocHandler.ELEMENT_TYPE_POINT)) {
				boolean bIsFreePt = false;
				
				if (this.lastGeoObject == null)
					bIsFreePt = true;
				else {
					if (this.lastGeoObject instanceof PolygonLine) { // polygon or open polygon line
						PolygonLine polyLine = (PolygonLine)(this.lastGeoObject); // safe cast
						if (!polyLine.containsPointAsVertex(elementLabel))
							bIsFreePt = true;
					}
					else if (!(this.lastGeoObject instanceof Point))
						bIsFreePt = true;
					else if (!this.lastGeoObject.getGeoObjectLabel().equals(elementLabel))
						bIsFreePt = true;
				}
				
				if (bIsFreePt) {
					Point freePt = new FreePoint(elementLabel);
					this.lastGeoObject = freePt;
					this.lastGeoObjectType = OGPDocHandler.ELEMENT_TYPE_POINT;
					this.consProtocol.addGeoConstruction(freePt);
					this.geoObjMap.put(freePt.getGeoObjectLabel(), freePt);
				}
				else
					bDoDefaultCheck = true;
			}
			else if (elementType.equals(OGPDocHandler.ELEMENT_TYPE_SEGMENT)) {
				// Skip checking segments introduced as edges of previously constructed polygon
				if (!(this.lastGeoObject instanceof PolygonLine))
					bDoDefaultCheck = true;
			}
			else if (elementType.equals(OGPDocHandler.ELEMENT_TYPE_NUMERIC)) {
				// numeric is not for geometry objects so skip it
			}
			
			if (bDoDefaultCheck) {
				if (this.lastGeoObject == null) {
					StringBuilder sb = new StringBuilder();
					sb.append("Element tag for definition of geometry object of type ");
					sb.append(elementType);
					sb.append(" encountered before corresponding command tag for that geometry object.");
					logger.error(sb.toString());
					this.bSuccess = false;
					return;
				}
				
				if (!this.lastGeoObject.getGeoObjectLabel().equals(elementLabel)) {
					StringBuilder sb = new StringBuilder();
					sb.append("Element tag for definition of geometry object of type ");
					sb.append(elementType);
					sb.append(" doesn't match the label of last geometry construction command.");
					logger.error(sb.toString());
					this.bSuccess = false;
					return;
				}
				
				if (!this.lastGeoObjectType.equals(elementType)) {
					StringBuilder sb = new StringBuilder();
					sb.append("Element tag for definition of geometry object of type ");
					sb.append(elementType);
					sb.append(" doesn't match the type of last geometry construction command.");
					logger.error(sb.toString());
					this.bSuccess = false;
					return;
				}
			}
			
			this.currentTagType = OGPDocHandler.TAG_TYPE_ELEMENT;
		}
		// === <command> ===
		else if (tag.equalsIgnoreCase(OGPDocHandler.TAG_NAME_COMMAND)) {
			if (this.currentTagType != OGPDocHandler.TAG_TYPE_CONSTRUCTION) {
				logger.error("Command tag encountered when not expected.");
				this.bSuccess = false;
				return;
			}
			
			String commandName = h.get(OGPDocHandler.ATTR_NAME_NAME);
			
			if (commandName == null) {
				logger.error("Failed to find name attribute of command tag.");
				this.bSuccess = false;
				return;
			}
			
			this.currConstructionName = commandName;
			this.currentTagType = OGPDocHandler.TAG_TYPE_COMMAND;
		}
		// === <input> ===
		else if (tag.equalsIgnoreCase(OGPDocHandler.TAG_NAME_INPUT)) {
			if (this.currentTagType != OGPDocHandler.TAG_TYPE_COMMAND) {
				logger.error("Input tag encountered when not expected.");
				this.bSuccess = false;
				return;
			}
			
			// Populate list of input arguments
			this.currGeoObjInputArgs = new ArrayList<String>();
			String genArg = null;
			Integer argNum = new Integer(0);
			while((genArg = h.get(OGPDocHandler.ATTR_NAME_GEN + argNum.toString())) != null) {
				this.currGeoObjInputArgs.add(genArg);
				argNum++;
			}
			
			this.currentTagType = OGPDocHandler.TAG_TYPE_SUB_COMMAND;
		}
		// === <output> ===
		else if (tag.equalsIgnoreCase(OGPDocHandler.TAG_NAME_OUTPUT)) {
			if (this.currentTagType != OGPDocHandler.TAG_TYPE_COMMAND) {
				logger.error("Output tag encountered when not expected.");
				this.bSuccess = false;
				return;
			}
			
			// Populate list of output arguments
			this.currGeoObjOutputArgs = new ArrayList<String>();
			String genArg = null;
			Integer argNum = new Integer(0);
			while((genArg = h.get(OGPDocHandler.ATTR_NAME_GEN + argNum.toString())) != null) {
				this.currGeoObjOutputArgs.add(genArg);
				argNum++;
			}
			
			this.currentTagType = OGPDocHandler.TAG_TYPE_SUB_COMMAND;
		}
		// === <expression> ===
		else if (tag.equalsIgnoreCase(OGPDocHandler.TAG_NAME_EXPRESSION)) {
			if (this.currentTagType != OGPDocHandler.TAG_TYPE_CONSTRUCTION) {
				logger.error("Expression tag encountered when not expected.");
				this.bSuccess = false;
				return;
			}
			
			this.currentTagType = OGPDocHandler.TAG_TYPE_EXPRESSION;
		}
		// === other tags ===
		else {
			// Other tags are allowed only within <element> tag and they are disregarded.
			if (this.currentTagType != OGPDocHandler.TAG_TYPE_ELEMENT) {
				logger.error("Unknown tag encountered.");
				this.currentTagType = OGPDocHandler.TAG_TYPE_UNKNOWN;
				this.bSuccess = false;
				return;
			}
			
			this.currentTagType = OGPDocHandler.TAG_TYPE_SUB_ELEMENT;
		}
	}

	/**
	 * @see com.ogprover.utilities.io.DocHandler#endElement(java.lang.String)
	 * 
	 * In case of error flag for success of parsing is set to FALSE.
	 */
	public void endElement(String tag) throws Exception {
		if (!this.bSuccess)
			return; // skip the tag if an error has already been encountered
		
		FileLogger logger = OpenGeoProver.settings.getLogger();
		
		// === </construction> ===
		if (tag.equalsIgnoreCase(OGPDocHandler.TAG_NAME_CONSTRUCTION)) {
			if (this.currentTagType != OGPDocHandler.TAG_TYPE_CONSTRUCTION) {
				logger.error("Trying to close construction tag when not expected.");
				this.bSuccess = false;
				return;
			}
			
			this.currentTagType = OGPDocHandler.TAG_TYPE_NONE;
		}
		// === </element> ===
		else if (tag.equalsIgnoreCase(OGPDocHandler.TAG_NAME_ELEMENT)) {
			if (this.currentTagType != OGPDocHandler.TAG_TYPE_ELEMENT) {
				logger.error("Trying to close element tag when not expected.");
				this.bSuccess = false;
				return;
			}
			
			this.currentTagType = OGPDocHandler.TAG_TYPE_CONSTRUCTION;
		}
		// === </command> ===
		else if (tag.equalsIgnoreCase(OGPDocHandler.TAG_NAME_COMMAND)) {
			if (this.currentTagType != OGPDocHandler.TAG_TYPE_COMMAND) {
				logger.error("Trying to close command tag when not expected.");
				this.bSuccess = false;
				return;
			}
			
			GeoObject geoObj = this.createGeoObjectFromCurrentCommand(); // this can create some auxiliary geometry objects and store them in collections
			
			if (geoObj == null) {
				logger.error("Failed to create geometry object for current command tag.");
				this.bSuccess = false;
				return;
			}
			
			// store this geometry object
			this.lastGeoObject = geoObj;
			this.lastGeoObjectType = this.getElementTypeOfCurrentConstruction(geoObj);
			this.geoObjMap.put(geoObj.getGeoObjectLabel(), geoObj);
			if (geoObj instanceof GeoConstruction) {
				this.consProtocol.addGeoConstruction((GeoConstruction)geoObj); // safe cast
			}
			// reset elements used for processing of command tag
			this.currConstructionName = null;
			this.currGeoObjInputArgs = null;
			this.currGeoObjOutputArgs = null;
			
			this.currentTagType = OGPDocHandler.TAG_TYPE_CONSTRUCTION;
		}
		// === </input> ===
		else if (tag.equalsIgnoreCase(OGPDocHandler.TAG_NAME_INPUT)) {
			if (this.currentTagType != OGPDocHandler.TAG_TYPE_SUB_COMMAND) {
				logger.error("Trying to close input tag when not expected.");
				this.bSuccess = false;
				return;
			}
			
			this.currentTagType = OGPDocHandler.TAG_TYPE_COMMAND;
		}
		// === </output> ===
		else if (tag.equalsIgnoreCase(OGPDocHandler.TAG_NAME_OUTPUT)) {
			if (this.currentTagType != OGPDocHandler.TAG_TYPE_SUB_COMMAND) {
				logger.error("Trying to close output tag when not expected.");
				this.bSuccess = false;
				return;
			}
			
			this.currentTagType = OGPDocHandler.TAG_TYPE_COMMAND;
		}
		// === </expression> ===
		else if (tag.equalsIgnoreCase(OGPDocHandler.TAG_NAME_EXPRESSION)) {
			if (this.currentTagType != OGPDocHandler.TAG_TYPE_EXPRESSION) {
				logger.error("Trying to close expression tag when not expected.");
				this.bSuccess = false;
				return;
			}
			
			this.currentTagType = OGPDocHandler.TAG_TYPE_CONSTRUCTION;
		}
		// === other tags ===
		else {
			// Other tags are allowed only within <element> tag and they are disregarded.
			if (this.currentTagType != OGPDocHandler.TAG_TYPE_SUB_ELEMENT) {
				logger.error("Trying to close unknown tag.");
				this.currentTagType = OGPDocHandler.TAG_TYPE_UNKNOWN;
				this.bSuccess = false;
				return;
			}
			
			this.currentTagType = OGPDocHandler.TAG_TYPE_ELEMENT;
		}
	}

	/**
	 * @see com.ogprover.utilities.io.DocHandler#startDocument()
	 */
	public void startDocument() throws Exception {
		// Put initializations for parsing process here ...
		
		// ===== Storage objects for parsed data =====
		this.geoObjMap.clear();
		this.consProtocol.clear();
		
		// ===== Data members for processing of current tag =====
		this.currentTagType = OGPDocHandler.TAG_TYPE_NONE;
		this.currentTagName = null;
		
		// ===== Data members for processing of command tag =====
		this.lastGeoObject = null; // the geometry construction defined by last current command found
		this.lastGeoObjectType = OGPDocHandler.ELEMENT_TYPE_NONE;
		this.currConstructionName = null;
		this.currGeoObjInputArgs = null;
		this.currGeoObjOutputArgs = null;
		
		// ===== Parsing result =====
		this.bSuccess = true; // reset the flag which determines whether parsing was successful
	}

	/**
	 * @see com.ogprover.utilities.io.DocHandler#endDocument()
	 */
	public void endDocument() throws Exception {
		String message;
		FileLogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.currentTagType != TAG_TYPE_NONE) {
			message = "XML input not valid.";
			this.bSuccess = false;
			logger.error(message);
			throw new SAXException(message);
		}
		else if (!this.bSuccess) {
			message = "Failed parsing";
			logger.error(message);
			throw new SAXException(message);
		}
	}

	/**
	 * @see com.ogprover.utilities.io.DocHandler#text(java.lang.String)
	 */
	public void text(String str) throws Exception {
		// Not required for XML parsing in OGP
	}

	/**
	 * @see com.ogprover.utilities.io.DocHandler#getConsStep()
	 */
	public int getConsStep() {
		// Default implementation - not required for XML parsing in OGP
		return 0;
	}
	
	
	/*
	 * Auxiliary methods
	 */
	/**
	 * Method that retrieves the element type which corresponds to passed in geometry object.
	 * It is called while processing <command> tag.
	 * 
	 * @param geoObj	The geometry object introduced by <command> tag
	 * @return			The element type of passed in constructed geometry object - if object is null
	 * 					then null is retrieved, otherwise the element type of that object is retrieved
	 * 					(it is empty string if the type of object is unknown).
	 */
	private String getElementTypeOfCurrentConstruction(GeoObject geoObj) {
		if (geoObj == null)
			return null;
		
		if (geoObj instanceof Point)
			return OGPDocHandler.ELEMENT_TYPE_POINT;
		
		if (geoObj instanceof Line) {
			if (this.currConstructionName.equals(OGPDocHandler.COMMAND_RAY))
				return OGPDocHandler.ELEMENT_TYPE_RAY;
			return OGPDocHandler.ELEMENT_TYPE_LINE;
		}
		
		if ((geoObj instanceof Circle) || (geoObj instanceof ConicSection))
			return OGPDocHandler.ELEMENT_TYPE_CONIC;
		
		if (geoObj instanceof Segment) {
			if (this.currConstructionName.equals(OGPDocHandler.COMMAND_VECTOR))
				return OGPDocHandler.ELEMENT_TYPE_VECTOR;
			return OGPDocHandler.ELEMENT_TYPE_SEGMENT;
		}
		
		if (geoObj instanceof Angle)
			return OGPDocHandler.ELEMENT_TYPE_ANGLE;
		
		if (geoObj instanceof PolygonLine) {
			if (this.currConstructionName.equals(OGPDocHandler.COMMAND_POLYLINE))
				return OGPDocHandler.ELEMENT_TYPE_POLYLINE;
			return OGPDocHandler.ELEMENT_TYPE_POLYGON;
		}
		
		return OGPDocHandler.ELEMENT_TYPE_NONE;
	}
	
	
	
	/*
	 * Methods for generating geometry objects.
	 */
	
	// === Points ===
	/**
	 * Method for creation of intersection point from attributes of command tag (and its sub-tags) 
	 * that is currently being processed.
	 *  
	 * @return NULL object in case of error, non-NULL geometry object that corresponds to current command 
	 * 		   tag if successful.
	 */
	private IntersectionPoint createIntersectionPoint() {
		FileLogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.currGeoObjInputArgs == null) {
			logger.error("Empty list of input arguments.");
			return null;
		}
			
		if (this.currGeoObjInputArgs.size() != 2) {
			logger.error("Incorrect number of input arguments.");
			return null;
		}
		
		String s1Label = this.currGeoObjInputArgs.get(0); // label of first set
		String s2Label = this.currGeoObjInputArgs.get(1); // label of second set
		
		if (s1Label == null || s2Label == null) {
			logger.error("Some input argument is missing.");
			return null;
		}
		
		SetOfPoints set1 = null;
		SetOfPoints set2 = null;
		try {
			set1 = (SetOfPoints) this.consProtocol.getConstructionMap().get(s1Label);
			set2 = (SetOfPoints) this.consProtocol.getConstructionMap().get(s2Label);
		
			if (set1 == null || set2 == null) {
				logger.error("Some input argument is missing in construction protocol.");
				return null;
			}
		} catch (ClassCastException e) {
			logger.error("Some input argument is of wrong type. Exception caught: " + e.toString());
			return null;
		}
		
		return new IntersectionPoint(this.currGeoObjOutputArgs.get(0), set1, set2);
	}
	
	/**
	 * Method for creation of midpoint from attributes of command tag (and its sub-tags) 
	 * that is currently being processed.
	 *  
	 * @return NULL object in case of error, non-NULL geometry object that corresponds to current command 
	 * 		   tag if successful.
	 */
	private Point createMidPoint() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Method for creation of general point from attributes of command tag (and its sub-tags) 
	 * that is currently being processed.
	 *  
	 * @return NULL object in case of error, non-NULL geometry object that corresponds to current command 
	 * 		   tag if successful.
	 */
	private GeoObject createPoint() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Method for creation of interior point from attributes of command tag (and its sub-tags) 
	 * that is currently being processed.
	 *  
	 * @return NULL object in case of error, non-NULL geometry object that corresponds to current command 
	 * 		   tag if successful.
	 */
	private GeoObject createPointIn() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Method for creation of mirrored point from attributes of command tag (and its sub-tags) 
	 * that is currently being processed.
	 *  
	 * @return NULL object in case of error, non-NULL geometry object that corresponds to current command 
	 * 		   tag if successful.
	 */
	private Point createMirroredPoint() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Method for creation of rotated point or angle ray from attributes of command tag (and its sub-tags) 
	 * that is currently being processed.
	 *  
	 * @return NULL object in case of error, non-NULL geometry object that corresponds to current command 
	 * 		   tag if successful.
	 */
	private GeoObject createRotatedPoint() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Method for creation of translated point from attributes of command tag (and its sub-tags) 
	 * that is currently being processed.
	 *  
	 * @return NULL object in case of error, non-NULL geometry object that corresponds to current command 
	 * 		   tag if successful.
	 */
	private Point createTranslatedPoint() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Method for creation of dilated point from attributes of command tag (and its sub-tags) 
	 * that is currently being processed.
	 *  
	 * @return NULL object in case of error, non-NULL geometry object that corresponds to current command 
	 * 		   tag if successful.
	 */
	private Point createDilatedPoint() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	// === Lines ===
	/**
	 * Method for creation of line through two points or parallel line, from attributes 
	 * of command tag (and its sub-tags) that is currently being processed.
	 *  
	 * @return NULL object in case of error, non-NULL geometry object that corresponds to current command 
	 * 		   tag if successful.
	 */
	private Line createLine() {
		FileLogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.currGeoObjInputArgs == null) {
			logger.error("Empty list of input arguments.");
			return null;
		}
			
		if (this.currGeoObjInputArgs.size() != 2) {
			logger.error("Incorrect number of input arguments.");
			return null;
		}
		
		String label1 = this.currGeoObjInputArgs.get(0); // label of first point from line
		String label2 = this.currGeoObjInputArgs.get(1); // label of second point from line or label of basic line
		
		if (label1 == null || label2 == null) {
			logger.error("Some input argument is missing.");
			return null;
		}
		
		Point pointA = null;
		Point pointB = null;
		Line baseLine = null;
		try {
			pointA = (Point)this.consProtocol.getConstructionMap().get(label1);
			if (pointA == null) {
				logger.error("First input argument is missing in construction protocol.");
				return null;
			}
			
			GeoConstruction gc2 = this.consProtocol.getConstructionMap().get(label2);
			if (gc2 == null) {
				logger.error("Second input argument is missing in construction protocol.");
				return null;
			}
			if (gc2 instanceof Point) {
				pointB = (Point)gc2;
			}
			else if (gc2 instanceof Line) {
				baseLine = (Line)gc2;
			}
			else {
				logger.error("Second input argument is for object of incorrect type.");
				return null;
			}
		} catch (ClassCastException e) {
			logger.error("Some input argument is of wrong type. Exception caught: " + e.toString());
			return null;
		}
		
		if (pointB != null)
			return new LineThroughTwoPoints(this.currGeoObjOutputArgs.get(0), pointA, pointB);
		else if (baseLine != null)
			return new ParallelLine(this.currGeoObjOutputArgs.get(0), baseLine, pointA);
		
		return null;
	}
	
	/**
	 * Method for creation of perpendicular line from attributes of command tag (and its sub-tags) 
	 * that is currently being processed.
	 *  
	 * @return NULL object in case of error, non-NULL geometry object that corresponds to current command 
	 * 		   tag if successful.
	 */
	private PerpendicularLine createPerpendicularLine() {
		FileLogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.currGeoObjInputArgs == null) {
			logger.error("Empty list of input arguments.");
			return null;
		}
			
		if (this.currGeoObjInputArgs.size() != 2) {
			logger.error("Incorrect number of input arguments.");
			return null;
		}
		
		String M0Label = this.currGeoObjInputArgs.get(0); // base point label
		String lLabel = this.currGeoObjInputArgs.get(1); // base line label
		
		if (M0Label == null || lLabel == null) {
			logger.error("Some input argument is missing.");
			return null;
		}
		
		Point pointM0 = null;
		Line linel = null;
		try {
			pointM0 = (Point) this.consProtocol.getConstructionMap().get(M0Label);
			linel = (Line) this.consProtocol.getConstructionMap().get(lLabel);
		
			if (pointM0 == null || linel == null) {
				logger.error("Some input argument is missing in construction protocol.");
				return null;
			}
		} catch (ClassCastException e) {
			logger.error("Some input argument is of wrong type. Exception caught: " + e.toString());
			return null;
		}
		
		return new PerpendicularLine(this.currGeoObjOutputArgs.get(0), linel, pointM0);
	}
	
	/**
	 * Method for creation of perpendicular bisector from attributes of command tag (and its sub-tags) 
	 * that is currently being processed.
	 *  
	 * @return NULL object in case of error, non-NULL geometry object that corresponds to current command 
	 * 		   tag if successful.
	 */
	private Line createPerpendicularBisector() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Method for creation of angle bisector from attributes of command tag (and its sub-tags) 
	 * that is currently being processed.
	 *  
	 * @return NULL object in case of error, non-NULL geometry object that corresponds to current command 
	 * 		   tag if successful.
	 */
	private Line createAngBisector() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Method for creation of tangent line from attributes of command tag (and its sub-tags) 
	 * that is currently being processed.
	 *  
	 * @return NULL object in case of error, non-NULL geometry object that corresponds to current command 
	 * 		   tag if successful.
	 */
	private Line createTangent() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Method for creation of polar from attributes of command tag (and its sub-tags) 
	 * that is currently being processed.
	 *  
	 * @return NULL object in case of error, non-NULL geometry object that corresponds to current command 
	 * 		   tag if successful.
	 */
	private Line createPolar() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	// === Conic Sections ===
	/**
	 * Method for creation of circle from attributes of command tag (and its sub-tags) 
	 * that is currently being processed.
	 *  
	 * @return NULL object in case of error, non-NULL geometry object that corresponds to current command 
	 * 		   tag if successful.
	 */
	private Circle createCircle() {
		FileLogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.currGeoObjInputArgs == null) {
			logger.error("Empty list of input arguments.");
			return null;
		}
			
		if (this.currGeoObjInputArgs.size() < 2 || this.currGeoObjInputArgs.size() > 3) {
			logger.error("Incorrect number of input arguments.");
			return null;
		}
		
		String CLabel = this.currGeoObjInputArgs.get(0); // label of center or label of first point from circumscribed circle
		String label2 = this.currGeoObjInputArgs.get(1); // label of second argument
		String label3 = null; // label of third argument (point)
		
		if (CLabel == null || label2 == null) {
			logger.error("Some input argument is missing.");
			return null;
		}
		
		if (this.currGeoObjInputArgs.size() == 3) {
			label3 = this.currGeoObjInputArgs.get(2);
			if (label3 == null) {
				logger.error("Third argument is missing.");
				return null;
			}
		}
		
		Point pointC = null;
		Point pointA0 = null;
		Point pointA1 = null;
		try {
			pointC = (Point) this.consProtocol.getConstructionMap().get(CLabel);
			if (pointC == null) {
				logger.error("First input argument is missing in construction protocol.");
				return null;
			}
			
			// parse second label
			if (label2.startsWith("Segment[")) {
				int firstPointIndex = label2.indexOf("[") + 1;
				int commaIndex = label2.indexOf(",");
				int secondPointIndex = commaIndex + 2; // skip one blank space character
				int rbracIndex = label2.indexOf("]");
				
				String ALabel = label2.substring(firstPointIndex, commaIndex);
				String BLabel = label2.substring(secondPointIndex, rbracIndex);
				
				pointA0 = (Point)this.consProtocol.getConstructionMap().get(ALabel);
				pointA1 = (Point)this.consProtocol.getConstructionMap().get(BLabel);
				
				if (pointA0 == null || pointA1 == null) {
					logger.error("Some input argument is missing in construction protocol.");
					return null;
				}
				
				return new CircleWithCenterAndRadius(this.currGeoObjOutputArgs.get(0), pointC, pointA0, pointA1);
			}
			else if (this.currGeoObjInputArgs.size() == 2) {
				GeoConstruction gc = this.consProtocol.getConstructionMap().get(label2);
				if (gc == null) {
					// check if last geometry object obtained from <command> tag is segment
					if (this.lastGeoObject != null && (this.lastGeoObject instanceof Segment) && label2.equals(this.lastGeoObject.getGeoObjectLabel())) {
						Segment seg = (Segment)this.lastGeoObject; // safe cast
						return new CircleWithCenterAndRadius(this.currGeoObjOutputArgs.get(0), pointC, seg.getFirstEndPoint(), seg.getSecondEndPoint());
					}
					
					logger.error("Second input argument is missing in construction protocol.");
					return null;
				}
				
				if (gc instanceof Point) {
					pointA0 = (Point)gc;
					return new CircleWithCenterAndPoint(this.currGeoObjOutputArgs.get(0), pointC, pointA0);
				}
				else {
					logger.error("Second input argument is of wrong type.");
					return null;
				}
			}
			else if (this.currGeoObjInputArgs.size() == 3) {
				pointA0 = (Point) this.consProtocol.getConstructionMap().get(label2);
				pointA1 = (Point) this.consProtocol.getConstructionMap().get(label3);
				
				if (pointA0 == null || pointA1 == null) {
					logger.error("Some input argument is missing in construction protocol.");
					return null;
				}
				
				return new CircumscribedCircle(this.currGeoObjOutputArgs.get(0), pointC, pointA0, pointA1);
			}
			else {
				logger.error("Incorrect input argument list for circle.");
				return null;
			}
		} catch (ClassCastException e) {
			logger.error("Some input argument is of wrong type. Exception caught: " + e.toString());
			return null;
		}
	}
	
	/**
	 * Method for creation of conic through five points from attributes of command tag (and its sub-tags) 
	 * that is currently being processed.
	 *  
	 * @return NULL object in case of error, non-NULL geometry object that corresponds to current command 
	 * 		   tag if successful.
	 */
	private ConicSection createConic() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Method for creation of ellipse from attributes of command tag (and its sub-tags) 
	 * that is currently being processed.
	 *  
	 * @return NULL object in case of error, non-NULL geometry object that corresponds to current command 
	 * 		   tag if successful.
	 */
	private ConicSection createEllipse() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Method for creation of hyperbola from attributes of command tag (and its sub-tags) 
	 * that is currently being processed.
	 *  
	 * @return NULL object in case of error, non-NULL geometry object that corresponds to current command 
	 * 		   tag if successful.
	 */
	private ConicSection createHyperbola() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Method for creation of parabola from attributes of command tag (and its sub-tags) 
	 * that is currently being processed.
	 *  
	 * @return NULL object in case of error, non-NULL geometry object that corresponds to current command 
	 * 		   tag if successful.
	 */
	private ConicSection createParabola() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	// == Other auxiliary geometry objects ===
	/**
	 * Method for creation of segment from attributes of command tag (and its sub-tags) 
	 * that is currently being processed.
	 *  
	 * @return NULL object in case of error, non-NULL geometry object that corresponds to current command 
	 * 		   tag if successful.
	 */
	private Segment createSegment() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Method for creation of polygon from attributes of command tag (and its sub-tags) 
	 * that is currently being processed.
	 *  
	 * @return NULL object in case of error, non-NULL geometry object that corresponds to current command 
	 * 		   tag if successful.
	 */
	private PolygonLine createPolygon() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Method for creation of polygon line from attributes of command tag (and its sub-tags) 
	 * that is currently being processed.
	 *  
	 * @return NULL object in case of error, non-NULL geometry object that corresponds to current command 
	 * 		   tag if successful.
	 */
	private PolygonLine createPolyLine() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Method for creation of ray from attributes of command tag (and its sub-tags) 
	 * that is currently being processed.
	 *  
	 * @return NULL object in case of error, non-NULL geometry object that corresponds to current command 
	 * 		   tag if successful.
	 */
	private Line createRay() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Method for creation of angle from attributes of command tag (and its sub-tags) 
	 * that is currently being processed.
	 *  
	 * @return NULL object in case of error, non-NULL geometry object that corresponds to current command 
	 * 		   tag if successful.
	 */
	private Angle createAngle() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Method for creation of vector from attributes of command tag (and its sub-tags) 
	 * that is currently being processed.
	 *  
	 * @return NULL object in case of error, non-NULL geometry object that corresponds to current command 
	 * 		   tag if successful.
	 */
	private Segment createVector() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	
	// ==================== Main method for processing <command> tag ====================
	/**
	 * Method that takes attributes of command tag (and its sub-tags) that is currently being processed
	 * and creates corresponding geometry object.
	 *  
	 * @return NULL object in case of error, non-NULL geometry object that corresponds to current command 
	 * 		   tag if successful.
	 */
	private GeoObject createGeoObjectFromCurrentCommand() {
		FileLogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.currConstructionName == null) {
			logger.error("Cannot create geometry construction object for null construction.");
			return null;
		}
		
		if (this.currGeoObjOutputArgs == null || 
			this.currGeoObjOutputArgs.size() == 0 || 
			this.currGeoObjOutputArgs.get(0).length() == 0) {
			logger.error("Missing label for new geometry construction object.");
			return null;
		}
		
		/*
		 * Create specific geometry construction object.
		 */
		
		// === Commands for points and transformations ===
		if (this.currConstructionName.equals(OGPDocHandler.COMMAND_INTERSECT)) {
			return this.createIntersectionPoint();
		}
		if (this.currConstructionName.equals(OGPDocHandler.COMMAND_MIDPOINT)) {
			return this.createMidPoint();
		}
		if (this.currConstructionName.equals(OGPDocHandler.COMMAND_POINT)) {
			return this.createPoint(); // Random point on some set of points (line, circle, segment, polygon)
		}
		if (this.currConstructionName.equals(OGPDocHandler.COMMAND_POINT_IN)) {
			return this.createPointIn(); // Random point inside polygon
		}
		if (this.currConstructionName.equals(OGPDocHandler.COMMAND_MIRROR)) {
			return this.createMirroredPoint(); // all types of reflections (w.r.t line or point or circle - inversion)
		}
		if (this.currConstructionName.equals(OGPDocHandler.COMMAND_ROTATE)) {
			return this.createRotatedPoint();
		}
		if (this.currConstructionName.equals(OGPDocHandler.COMMAND_TRANSLATE)) {
			return this.createTranslatedPoint();
		}
		if (this.currConstructionName.equals(OGPDocHandler.COMMAND_DILATE)) {
			return this.createDilatedPoint();
		}
		
		// === Commands for lines ===
		if (this.currConstructionName.equals(OGPDocHandler.COMMAND_LINE)) {
			return this.createLine(); // Line through two points or parallel line
		}
		if (this.currConstructionName.equals(OGPDocHandler.COMMAND_ORT_LINE)) {
			return this.createPerpendicularLine();
		}
		if (this.currConstructionName.equals(OGPDocHandler.COMMAND_LINE_BISECTOR)) {
			return this.createPerpendicularBisector(); // Perpendicular bisector of segment
		}
		if (this.currConstructionName.equals(OGPDocHandler.COMMAND_ANG_BISECTOR)) {
			return this.createAngBisector();
		}
		if (this.currConstructionName.equals(OGPDocHandler.COMMAND_TANGENT)) {
			return this.createTangent();
		}
		if (this.currConstructionName.equals(OGPDocHandler.COMMAND_POLAR)) {
			return this.createPolar();
		}
		
		// === Commands for conic sections ===
		if (this.currConstructionName.equals(OGPDocHandler.COMMAND_CIRCLE)) {
			return this.createCircle(); // all types of circles
		}
		if (this.currConstructionName.equals(OGPDocHandler.COMMAND_CONIC)) {
			return this.createConic(); // conic through five points
		}
		if (this.currConstructionName.equals(OGPDocHandler.COMMAND_ELLIPSE)) {
			return this.createEllipse();
		}
		if (this.currConstructionName.equals(OGPDocHandler.COMMAND_HYPERBOLA)) {
			return this.createHyperbola();
		}
		if (this.currConstructionName.equals(OGPDocHandler.COMMAND_PARABOLA)) {
			return this.createParabola();
		}
		
		// === Commands for other auxiliary geometry objects ===
		if (this.currConstructionName.equals(OGPDocHandler.COMMAND_SEGMENT)) {
			return this.createSegment();
		}
		if (this.currConstructionName.equals(OGPDocHandler.COMMAND_POLYGON)) {
			return this.createPolygon();
		}
		if (this.currConstructionName.equals(OGPDocHandler.COMMAND_POLYLINE)) {
			return this.createPolyLine();
		}
		if (this.currConstructionName.equals(OGPDocHandler.COMMAND_RAY)) {
			return this.createRay();
		}
		if (this.currConstructionName.equals(OGPDocHandler.COMMAND_ANGLE)) {
			return this.createAngle();
		}
		if (this.currConstructionName.equals(OGPDocHandler.COMMAND_VECTOR)) {
			return this.createVector();
		}
		
		// TODO - Update this method when new commands are added to class
		
		// else
		logger.error("Unknown type of construction");
		return null;
	}

}
