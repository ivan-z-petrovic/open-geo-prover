/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.utilities.io;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.xml.sax.SAXException;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.prover_protocol.cp.OGPCP;
import com.ogprover.prover_protocol.cp.geoconstruction.*;


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
	
	/**
	 * Static constants for names of relevant tags.
	 */
	public static final String TAG_NAME_CONSTRUCTION = "construction";
	public static final String TAG_NAME_ELEMENT = "element";
	public static final String TAG_NAME_COMMAND = "command";
	public static final String TAG_NAME_INPUT = "input";
	public static final String TAG_NAME_OUTPUT = "output";
	
	/**
	 * Static constants for names of relevant attributes.
	 */
	public static final String ATTR_NAME_TITLE = "title";
	public static final String ATTR_NAME_LABEL = "label";
	public static final String ATTR_NAME_NAME = "name";
	public static final String ATTR_NAME_TYPE = "type";
	// generic arguments
	public static final String ATTR_NAME_A0 = "a0";
	public static final String ATTR_NAME_A1 = "a1";
	// TODO - add more generic arguments if necessary
	
	/**
	 * Static constants for types of geometry objects.
	 */
	public static final String ELEMENT_TYPE_POINT = "point";
	public static final String ELEMENT_TYPE_LINE = "line";
	public static final String ELEMENT_TYPE_CONIC = "conic";
	
	/**
	 * Static constants for command names.
	 */
	public static final String COMMAND_LINE = "Line";
	public static final String COMMAND_ORT_LINE = "OrthogonalLine";
	public static final String COMMAND_INTERSECT = "Intersect";
	public static final String COMMAND_CIRCLE = "Circle";
	// TODO - other commands
	
	/**
	 * The type of XML tag that is currently being processed.
	 */
	private int currentTagType;
	
	/**
	 * The name of last open XML tag.
	 */
	private String lastOpenTagName;
	
	/**
	 * Construction Protocol for storage of parsed geometry constructions.
	 */
	private OGPCP consProtocol = null;
	
	/**
	 * Flag to determine whether parsing was successful - by default it is TRUE but if during parsing of XML tags
	 * an error is encountered, it is set to FALSE.
	 */
	private boolean bSuccess;
	
	/**
	 * The last processed geometry construction defined by command tag.
	 */
	private GeoConstruction lastGeoConstruction;
	
	// Elements used for processing of command tag.
	/**
	 * Type of current geometry construction defined by command tag being processed.
	 */
	private String currGeoConsType;
	/**
	 * Input arguments of current geometry construction defined by command tag being processed.
	 */
	private ArrayList<String> currGeoConsInputArgs;
	/**
	 * Label of current geometry construction defined by command tag being processed.
	 */
	private String currGeoConsLabel;
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @return the currentTagType
	 */
	protected int getCurrentTagType() {
		return currentTagType;
	}

	/**
	 * @param currentTagType the currentTagType to set
	 */
	protected void setCurrentTagType(int currentTagType) {
		this.currentTagType = currentTagType;
	}
	
	/**
	 * @return the lastOpenTagName
	 */
	protected String getLastOpenTagName() {
		return lastOpenTagName;
	}

	/**
	 * @param lastOpenTagName the lastOpenTagName to set
	 */
	protected void setLastOpenTagName(String lastOpenTagName) {
		this.lastOpenTagName = lastOpenTagName;
	}

	/**
	 * @return the consProtocol
	 */
	public OGPCP getConsProtocol() {
		return consProtocol;
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
	
	/**
	 * @param bSuccess the bSuccess to set
	 */
	public void setSuccess(boolean bSuccess) {
		this.bSuccess = bSuccess;
	}
	
	/**
	 * @return the lastGeoConstruction
	 */
	protected GeoConstruction getLastGeoConstruction() {
		return lastGeoConstruction;
	}

	/**
	 * @param lastGeoConstruction the lastGeoConstruction to set
	 */
	protected void setLastGeoConstruction(GeoConstruction lastGeoConstruction) {
		this.lastGeoConstruction = lastGeoConstruction;
	}
	
	/**
	 * @return the currGeoConsType
	 */
	protected String getCurrGeoConsType() {
		return currGeoConsType;
	}

	/**
	 * @param currGeoConsType the currGeoConsType to set
	 */
	protected void setCurrGeoConsType(String currGeoConsType) {
		this.currGeoConsType = currGeoConsType;
	}

	/**
	 * @return the currGeoConsInputArgs
	 */
	protected ArrayList<String> getCurrGeoConsInputArgs() {
		return currGeoConsInputArgs;
	}

	/**
	 * @param currGeoConsInputArgs the currGeoConsInputArgs to set
	 */
	protected void setCurrGeoConsInputArgs(ArrayList<String> currGeoConsInputArgs) {
		this.currGeoConsInputArgs = currGeoConsInputArgs;
	}

	/**
	 * @return the currGeoConsLabel
	 */
	protected String getCurrGeoConsLabel() {
		return currGeoConsLabel;
	}

	/**
	 * @param currGeoConsLabel the currGeoConsLabel to set
	 */
	protected void setCurrGeoConsLabel(String currGeoConsLabel) {
		this.currGeoConsLabel = currGeoConsLabel;
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
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
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
		
		this.lastOpenTagName = tag;
		
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
			
			if (elementType.equals(OGPDocHandler.ELEMENT_TYPE_POINT)) {
				// Check the last construction ...
				
				if (this.lastGeoConstruction == null || !this.lastGeoConstruction.getGeoObjectLabel().equals(elementLabel)) {
					this.consProtocol.addGeoConstruction(new FreePoint(elementLabel));
				}
				else if (this.lastGeoConstruction.getGeoObjectLabel().equals(elementLabel)) {
					if (!(this.lastGeoConstruction instanceof Point)) {
						logger.error("Element tag for point follows construction of object with same label but which is not point.");
						this.bSuccess = false;
						return;
					}
				}
			}
			else if (elementType.equals(OGPDocHandler.ELEMENT_TYPE_LINE)) {
				// Check the last construction ...
				
				if (this.lastGeoConstruction == null) {
					logger.error("Element tag for line encountered before command tag for that line.");
					this.bSuccess = false;
					return;
				}
				
				if (!this.lastGeoConstruction.getGeoObjectLabel().equals(elementLabel)) {
					logger.error("Element tag for line doesn't match the label of last geometry construction command.");
					this.bSuccess = false;
					return;
				}
				
				if (!(this.lastGeoConstruction instanceof Line)) {
					logger.error("Element tag for line doesn't match the type of last geometry construction command - last construction is not a line.");
					this.bSuccess = false;
					return;
				}
			}
			else if (elementType.equals(OGPDocHandler.ELEMENT_TYPE_CONIC)) {
				// Check the last construction ...
				
				if (this.lastGeoConstruction == null) {
					logger.error("Element tag for conic section encountered before command tag for that conic section.");
					this.bSuccess = false;
					return;
				}
				
				if (!this.lastGeoConstruction.getGeoObjectLabel().equals(elementLabel)) {
					logger.error("Element tag for conic section doesn't match the label of last geometry construction command.");
					this.bSuccess = false;
					return;
				}
				
				if (!(this.lastGeoConstruction instanceof Circle) && !(this.lastGeoConstruction instanceof ConicSection)) {
					logger.error("Element tag for conic section doesn't match the type of last geometry construction command - last construction is not a conic section.");
					this.bSuccess = false;
					return;
				}
			}
			else {
				logger.error("Element tag of unknown type has been encountered.");
				this.bSuccess = false;
				return;
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
			
			this.currGeoConsType = commandName;
			this.currentTagType = OGPDocHandler.TAG_TYPE_COMMAND;
		}
		// === <input> ===
		else if (tag.equalsIgnoreCase(OGPDocHandler.TAG_NAME_INPUT)) {
			if (this.currentTagType != OGPDocHandler.TAG_TYPE_COMMAND) {
				logger.error("Input tag encountered when not expected.");
				this.bSuccess = false;
				return;
			}
			
			this.currGeoConsInputArgs = new ArrayList<String>();
			this.currGeoConsInputArgs.add(h.get(OGPDocHandler.ATTR_NAME_A0));
			this.currGeoConsInputArgs.add(h.get(OGPDocHandler.ATTR_NAME_A1));
			// TODO - add more generic arguments if necessary 
			this.currentTagType = OGPDocHandler.TAG_TYPE_SUB_COMMAND;
		}
		// === <output> ===
		else if (tag.equalsIgnoreCase(OGPDocHandler.TAG_NAME_OUTPUT)) {
			if (this.currentTagType != OGPDocHandler.TAG_TYPE_COMMAND) {
				logger.error("Output tag encountered when not expected.");
				this.bSuccess = false;
				return;
			}
			
			this.currGeoConsLabel = h.get(OGPDocHandler.ATTR_NAME_A0);
			this.currentTagType = OGPDocHandler.TAG_TYPE_SUB_COMMAND;
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
			
			GeoConstruction geoCons = this.createGeoConstructionFromCurrentCommand();
			
			if (geoCons == null) {
				logger.error("Failed to create geometry construction object for current command tag.");
				this.bSuccess = false;
				return;
			}
			
			this.lastGeoConstruction = geoCons;
			this.consProtocol.addGeoConstruction(geoCons);
			// reset elements used for processing of command tag
			this.currGeoConsType = null;
			this.currGeoConsInputArgs = null;
			this.currGeoConsLabel = null;
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
		// put initializations for parsing process here ...
		
		this.bSuccess = true; // reset the flag which determines whether parsing was successful
		this.lastGeoConstruction = null; // the geometry construction defined by last current command found
		this.currentTagType = OGPDocHandler.TAG_TYPE_NONE;
		// reset elements used for processing of command tag
		this.currGeoConsType = null;
		this.currGeoConsInputArgs = null;
		this.currGeoConsLabel = null;
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
	 * Methods for generating geometry construction objects.
	 */
	
	/**
	 * Method for creation of line through two points from attributes of command tag (and its sub-tags) 
	 * that is currently being processed.
	 *  
	 * @return NULL object in case of error, non-NULL geometry object that corresponds to current command 
	 * 		   tag if successful.
	 */
	private LineThroughTwoPoints createLineThroughTwoPoints() {
		FileLogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.currGeoConsInputArgs == null) {
			logger.error("Empty list of input arguments.");
			return null;
		}
			
		if (this.currGeoConsInputArgs.size() != 2) {
			logger.error("Incorrect number of input arguments.");
			return null;
		}
		
		String ALabel = this.currGeoConsInputArgs.get(0); // label of first point from line
		String BLabel = this.currGeoConsInputArgs.get(1); // label of second point from line
		
		if (ALabel == null || BLabel == null) {
			logger.error("Some input argument is missing.");
			return null;
		}
		
		Point pointA = null;
		Point pointB = null;
		try {
			pointA = (Point)this.consProtocol.getConstructionMap().get(ALabel);
			pointB = (Point)this.consProtocol.getConstructionMap().get(BLabel);
			
			if (pointA == null || pointB == null) {
				logger.error("Some input argument is missing in construction protocol.");
				return null;
			}
		} catch (ClassCastException e) {
			logger.error("Some input argument is of wrong type. Exception caught: " + e.toString());
			return null;
		}
		
		return new LineThroughTwoPoints(this.currGeoConsLabel, pointA, pointB);
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
		
		if (this.currGeoConsInputArgs == null) {
			logger.error("Empty list of input arguments.");
			return null;
		}
			
		if (this.currGeoConsInputArgs.size() != 2) {
			logger.error("Incorrect number of input arguments.");
			return null;
		}
		
		String M0Label = this.currGeoConsInputArgs.get(0); // base point label
		String lLabel = this.currGeoConsInputArgs.get(1); // base line label
		
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
		
		return new PerpendicularLine(this.currGeoConsLabel, linel, pointM0);
	}
	
	/**
	 * Method for creation of intersection point from attributes of command tag (and its sub-tags) 
	 * that is currently being processed.
	 *  
	 * @return NULL object in case of error, non-NULL geometry object that corresponds to current command 
	 * 		   tag if successful.
	 */
	private IntersectionPoint createIntersectionPoint() {
		FileLogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.currGeoConsInputArgs == null) {
			logger.error("Empty list of input arguments.");
			return null;
		}
			
		if (this.currGeoConsInputArgs.size() != 2) {
			logger.error("Incorrect number of input arguments.");
			return null;
		}
		
		String s1Label = this.currGeoConsInputArgs.get(0); // label of first set
		String s2Label = this.currGeoConsInputArgs.get(1); // label of second set
		
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
		
		return new IntersectionPoint(this.currGeoConsLabel, set1, set2);
	}
	
	/**
	 * Method for creation of circle with center and one point from attributes of command tag (and its sub-tags) 
	 * that is currently being processed.
	 *  
	 * @return NULL object in case of error, non-NULL geometry object that corresponds to current command 
	 * 		   tag if successful.
	 */
	private CircleWithCenterAndPoint createCircleWithCenterAndPoint() {
		FileLogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.currGeoConsInputArgs == null) {
			logger.error("Empty list of input arguments.");
			return null;
		}
			
		if (this.currGeoConsInputArgs.size() != 2) {
			logger.error("Incorrect number of input arguments.");
			return null;
		}
		
		String CLabel = this.currGeoConsInputArgs.get(0); // label of center
		String ALabel = this.currGeoConsInputArgs.get(1); // label of point from circle
		
		if (CLabel == null || ALabel == null) {
			logger.error("Some input argument is missing.");
			return null;
		}
		
		Point pointC = null;
		Point pointA = null;
		try {
			pointC = (Point) this.consProtocol.getConstructionMap().get(CLabel);
			pointA = (Point) this.consProtocol.getConstructionMap().get(ALabel);
		
			if (pointC == null || pointA == null) {
				logger.error("Some input argument is missing in construction protocol.");
				return null;
			}
		} catch (ClassCastException e) {
			logger.error("Some input argument is of wrong type. Exception caught: " + e.toString());
			return null;
		}
		
		return new CircleWithCenterAndPoint(this.currGeoConsLabel, pointC, pointA);
	}
	
	/**
	 * Method that takes attributes of command tag (and its sub-tags) that is currently being processed
	 * and creates corresponding geometry construction object.
	 *  
	 * @return NULL object in case of error, non-NULL geometry object that corresponds to current command 
	 * 		   tag if successful.
	 */
	private GeoConstruction createGeoConstructionFromCurrentCommand() {
		FileLogger logger = OpenGeoProver.settings.getLogger();
		
		if (this.currGeoConsType == null) {
			logger.error("Cannot create geometry construction object for null construction type.");
			return null;
		}
		
		if (this.currGeoConsLabel == null) {
			logger.error("Missing label for new geometry construction object.");
			return null;
		}
		
		/*
		 * Create specific geometry construction object.
		 */
		if (this.currGeoConsType.equals(OGPDocHandler.COMMAND_LINE)) {
			return this.createLineThroughTwoPoints();
		}
		else if (this.currGeoConsType.equals(OGPDocHandler.COMMAND_ORT_LINE)) {
			return this.createPerpendicularLine();
		}
		else if (this.currGeoConsType.equals(OGPDocHandler.COMMAND_INTERSECT)) {
			return this.createIntersectionPoint();
		}
		else if (this.currGeoConsType.equals(OGPDocHandler.COMMAND_CIRCLE)) {
			return this.createCircleWithCenterAndPoint();
		}
		// TODO - Update this method when new commands are added to class
		
		// else
		logger.error("Unknown type of construction");
		return null;
	}

}
