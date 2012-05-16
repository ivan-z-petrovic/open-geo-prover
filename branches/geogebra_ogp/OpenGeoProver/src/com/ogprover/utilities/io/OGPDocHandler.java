/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.utilities.io;

/*
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import org.xml.sax.SAXException;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.prover_protocol.cp.OGPCP;
import com.ogprover.prover_protocol.cp.geoconstruction.*;
import com.ogprover.prover_protocol.cp.geogebra.*;
import com.ogprover.prover_protocol.cp.geoobject.*;
*/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.xml.sax.SAXException;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.prover_protocol.cp.geogebra.GeoGebraCommand;
import com.ogprover.prover_protocol.cp.geogebra.GeoGebraCommandFactory;
import com.ogprover.prover_protocol.cp.geogebra.GeoGebraObject;


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
	 * Note:
	 * 
	 * 		This XML document handler is used for parsing input geometry constructions
	 * from GeoGebra to OGP. Currently OGP implements only algebraic provers so GeoGebra's
	 * constructions are converted in OGP to the format suitable for algebraic provers
	 * (i.e. to constructions that algebraic provers can deal with).
	 * Later, when other prover methods are added, if they need different conversion of
	 * GeoGebra's constructions, suitable converter class has to be created.
	 * 		Conversion logic is not in this class - this is just for low level XML parsing.
	 * The result of parsing is list of GeoGebra's construction commands which enters the
	 * conversion method of converter class which transforms it to the OGP constructions.
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
	
	
	// ===== Storage objects for parsed data =====
	/**
	 * Map of geometry objects read from XML file.
	 */
	private Map<String, GeoGebraCommand> geoObjMap = null;
	/**
	 * The name of geometry theorem (will be read from 'title' attribute of <construction> tag).
	 */
	private String theoremName;
	
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
	@SuppressWarnings("unused")
	private GeoGebraCommand lastGeoObject;
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
	public Map<String, GeoGebraCommand> getGeoObjMap() {
		return this.geoObjMap;
	}

	/**
	 * @return the theoremName
	 */
	public String getTheoremName() {
		return theoremName;
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
	 */
	public OGPDocHandler() {
		this.geoObjMap = new HashMap<String, GeoGebraCommand>();
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
			
			this.theoremName = h.get(OGPDocHandler.ATTR_NAME_TITLE); // Construction title will hold the name of theorem to be proved
			if (this.theoremName == null)
				this.theoremName = "";
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
			
			/* TODO
			 * 
			
			boolean bDoDefaultCheck = false;
			
			if (elementType.equals(GeoGebraObject.OBJ_TYPE_POINT)) {
				boolean bIsFreePt = false;
				boolean bIsFromPoly = false;
				
				if (this.lastGeoObject == null)
					bIsFreePt = true;
				else {
					if ((this.lastGeoObject instanceof PolygonCmd) || (this.lastGeoObject instanceof PolyLineCmd)) { // polygon or open polygon line
						PolygonLine polyLine = (PolygonLine)(this.lastGeoObject); // safe cast
						if (!polyLine.containsPointAsVertex(elementLabel))
							bIsFreePt = true;
						else
							bIsFromPoly = true;
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
				else if (!bIsFromPoly)
					bDoDefaultCheck = true;
			}
			else if (elementType.equals(OGPDocHandler.ELEMENT_TYPE_SEGMENT)) {
				// Skip checking segments introduced as edges of previously constructed polygon
				if (!(this.lastGeoObject instanceof PolygonLine))
					bDoDefaultCheck = true;
			}
			else if (elementType.equals(OGPDocHandler.ELEMENT_TYPE_CCURVE) || elementType.equals(OGPDocHandler.ELEMENT_TYPE_ICURVE)) {
				// Skip curves other than conic sections
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
			
			*
			*/
			
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
			while ((genArg = h.get(OGPDocHandler.ATTR_NAME_GEN + argNum.toString())) != null) {
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
			while ((genArg = h.get(OGPDocHandler.ATTR_NAME_GEN + argNum.toString())) != null) {
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
			
			GeoGebraCommand ggCmd = GeoGebraCommandFactory.createGeoGebraCommand(this.currConstructionName, this.currGeoObjInputArgs, this.currGeoObjOutputArgs, GeoGebraObject.OBJ_TYPE_NONE);
			
			if (ggCmd == null) {
				logger.error("Failed to create geometry object for current command tag.");
				this.bSuccess = false;
				return;
			}
			
			this.currentTagType = OGPDocHandler.TAG_TYPE_CONSTRUCTION;
			
			/*
			 * Store this geometry object in collections of geometry objects obtained by parsing.
			 */
			this.lastGeoObject = ggCmd;
			// Put objects that are not already added
			String objLabel = ggCmd.getGeoObjectLabel();
			if (this.geoObjMap.get(objLabel) == null)
				this.geoObjMap.put(objLabel, ggCmd);
			
			// reset elements used for processing of command tag
			this.currConstructionName = null;
			this.currGeoObjInputArgs = null;
			this.currGeoObjOutputArgs = null;
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
		
		// ===== Data members for processing of current tag =====
		this.currentTagType = OGPDocHandler.TAG_TYPE_NONE;
		this.currentTagName = null;
		
		// ===== Data members for processing of command tag =====
		this.lastGeoObject = null; // the geometry construction defined by last current command found
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
}
