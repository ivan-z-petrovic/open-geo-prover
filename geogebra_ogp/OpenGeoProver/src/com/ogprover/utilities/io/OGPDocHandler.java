/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.utilities.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import org.xml.sax.SAXException;

import com.ogprover.geogebra.GeoGebraObject;
import com.ogprover.geogebra.GeoGebraTheorem;
import com.ogprover.geogebra.command.GeoGebraCommand;
import com.ogprover.geogebra.command.GeoGebraCommandFactory;
import com.ogprover.geogebra.command.ProveCmd;
import com.ogprover.geogebra.command.construction.FreePointCmd;
import com.ogprover.geogebra.command.construction.GeoGebraConstructionCommand;
import com.ogprover.geogebra.command.statement.BooleanCmd;
import com.ogprover.geogebra.command.statement.GeoGebraStatementCommand;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.utilities.logger.ILogger;


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
	 * 		This XML document handler is used for parsing input geometry theorem
	 * from GeoGebra to OGP. Currently OGP implements only algebraic provers so GeoGebra's
	 * theorems are converted in OGP to the format suitable for algebraic provers
	 * (i.e. to constructions and statements that algebraic provers can deal with).
	 * Later, when other prover methods are added, if they need different conversion of
	 * GeoGebra's theorems, suitable converter class has to be created.
	 * 		Conversion logic is not in this class - this is just for low level XML parsing.
	 * The result of parsing is GeoGebra's theorem object which enters the conversion method 
	 * of converter class which transforms it to the OGP theorem.
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
	public static final String ATTR_NAME_EXP = "exp";
	// generic arguments
	public static final String ATTR_NAME_GEN = "a"; // base name of all generic arguments: a0, a1, a2, a3, a4, ...	
	
	/**
	 * Special characters
	 */
	public static final char CH_QUESTION_EQ = '\u225f';			// special Unicode character for question equality
	public static final char CH_PARALLEL_TO = '\u2225';			// special Unicode character for parallel sign
	public static final char CH_PERPENDICULAR = '\u22a5';		// special Unicode character for perpendicular sign
	// TODO - add other characters here
	
	
	// ===== Storage objects for parsed data =====
	/**
	 * GeoGebra theorem
	 */
	private GeoGebraTheorem ggThm;
	/**
	 * Map of GeoGebra objects read from XML file. Each object is associated
	 * by the GeoGebra command that introduces it in output argument list.
	 */
	private Map<String, GeoGebraCommand> ggObjMap;
	
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
	 * Name of GG command from command tag being processed.
	 */
	private String currCmdName;
	/**
	 * Input arguments of current GG command defined by command tag being processed.
	 */
	private ArrayList<String> currCmdInputArgs;
	/**
	 * Output arguments of current GG command defined by command tag being processed.
	 */
	private ArrayList<String> currCmdOutputArgs;
	
	// ===== Data members for processing of expression tag =====
	private Map<String, String> expMap; // map of expressions - key is label and value is expression text
	
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
	 * @return the ggThm
	 */
	public GeoGebraTheorem getTheorem() {
		return ggThm;
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
		this.ggThm = null; // this is instantiated when XML parsing starts
		this.ggObjMap = new HashMap<String, GeoGebraCommand>();
		this.bSuccess = true;
		this.expMap = new HashMap<String, String>();
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
		
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		// Save the name of current open tag
		this.currentTagName = tag;
		
		// === <construction> ===
		if (tag.equalsIgnoreCase(OGPDocHandler.TAG_NAME_CONSTRUCTION)) {
			if (this.currentTagType != OGPDocHandler.TAG_TYPE_NONE) {
				logger.error("Construction tag encountered when not expected.");
				this.bSuccess = false;
				return;
			}
			
			String theoremName = h.get(OGPDocHandler.ATTR_NAME_TITLE); // Construction title will hold the name of theorem to be proved
			if (theoremName == null)
				theoremName = "";
			this.ggThm.setTheoremName(theoremName);
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
			
			// Find command or expression for this element
			GeoGebraCommand ggCmd = this.ggObjMap.get(elementLabel);
			
			if (ggCmd == null) {
				if (this.expMap.get(elementLabel) == null) { // not an expression either
					// Object could not be found and this is allowed only for points
					if (elementType.equalsIgnoreCase(GeoGebraObject.OBJ_TYPE_POINT)) {
						// create new free point
						GeoGebraConstructionCommand newGGCmd = new FreePointCmd(elementLabel);
						if (this.ggThm.getStatement() != null || this.ggThm.getProveCmd() != null) {
							logger.error("Construction command read after statement or prove command");
							this.bSuccess = false;
							return;
						}
						this.ggThm.getConstructionList().add(newGGCmd);
						this.ggObjMap.put(elementLabel, newGGCmd);
					}
					else {
						logger.error("Definition of object which is not introduced by command");
						this.bSuccess = false;
						return;
					}
				}
			}
			else {
				// If command is transformation, set the object type if not already set, otherwise check type
				if (ggCmd instanceof GeoGebraConstructionCommand) {
					String cmdName = ggCmd.getCommandName();
					GeoGebraConstructionCommand consCmd = (GeoGebraConstructionCommand)ggCmd;
					if (cmdName.equals(GeoGebraConstructionCommand.COMMAND_MIRROR) || cmdName.equals(GeoGebraConstructionCommand.COMMAND_ROTATE) ||
						cmdName.equals(GeoGebraConstructionCommand.COMMAND_TRANSLATE) || cmdName.equals(GeoGebraConstructionCommand.COMMAND_DILATE)) {
						if (consCmd.getObjectType().equals(GeoGebraObject.OBJ_TYPE_NONE)) {
							consCmd.setObjectType(elementType);
						} 
					}
				}
				
				// Note: we won't check if the element type matches the type of object from command since it will be
				// provided by GeoGebra. If we do check it, we must process special cases when there are multiple 
				// output objects - this is not a problem for Intersection Points or Tangent Lines since all output objects
				// are of same type, but it is more complicated for polygons which can define polygon, segments and points. 
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
			
			this.currCmdName = commandName;
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
			this.currCmdInputArgs = new ArrayList<String>();
			String genArg = null;
			Integer argNum = new Integer(0);
			while ((genArg = h.get(OGPDocHandler.ATTR_NAME_GEN + argNum.toString())) != null) {
				this.currCmdInputArgs.add(genArg);
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
			this.currCmdOutputArgs = new ArrayList<String>();
			String genArg = null;
			Integer argNum = new Integer(0);
			while ((genArg = h.get(OGPDocHandler.ATTR_NAME_GEN + argNum.toString())) != null) {
				this.currCmdOutputArgs.add(genArg);
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
			
			// store expression to map
			this.expMap.put(h.get(OGPDocHandler.ATTR_NAME_LABEL), h.get(OGPDocHandler.ATTR_NAME_EXP));
			
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
		
		ILogger logger = OpenGeoProver.settings.getLogger();
		
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
			
			GeoGebraCommand ggCmd = GeoGebraCommandFactory.createGeoGebraCommand(this.currCmdName, this.currCmdInputArgs, this.currCmdOutputArgs, GeoGebraObject.OBJ_TYPE_NONE);
			
			if (ggCmd == null) {
				logger.error("Failed to create command object for current command tag");
				this.bSuccess = false;
				return;
			}	
			
			// Add this command to theorem object
			if (ggCmd instanceof GeoGebraConstructionCommand) {
				if (this.ggThm.getStatement() != null || this.ggThm.getProveCmd() != null) {
					logger.error("Construction command read after statement or prove command");
					this.bSuccess = false;
					return;
				}
				this.ggThm.getConstructionList().add((GeoGebraConstructionCommand)ggCmd);
			}
			else if (ggCmd instanceof GeoGebraStatementCommand) {
				if (this.ggThm.getStatement() != null || this.ggThm.getProveCmd() != null) {
					logger.error("Statament command read after statement or prove command");
					this.bSuccess = false;
					return;
				}
				this.ggThm.setStatement((GeoGebraStatementCommand)ggCmd);
			}
			else if (ggCmd instanceof ProveCmd) {
				if (this.ggThm.getProveCmd() != null) {
					logger.error("Prove command read after prove command");
					this.bSuccess = false;
					return;
				}
				this.ggThm.setProveCmd((ProveCmd)ggCmd);
			}
			else {
				logger.error("Unknown GeoGebra command type");
				this.bSuccess = false;
				return;
			}
			
			/*
			 * Store this command object in collection of GeoGebra objects obtained by parsing.
			 */
			for (String outLabel : this.currCmdOutputArgs) {
				if (outLabel.length() > 0) // "not empty label"
					this.ggObjMap.put(outLabel, ggCmd);
			}
			
			// reset elements used for processing of command tag - prepare for next <command> tag
			this.currCmdName = null;
			this.currCmdInputArgs = null;
			this.currCmdOutputArgs = null;
			
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
		this.ggThm = new GeoGebraTheorem();
		this.ggObjMap.clear();
		
		// ===== Data members for processing of current tag =====
		this.currentTagType = OGPDocHandler.TAG_TYPE_NONE;
		this.currentTagName = null;
		
		// ===== Data members for processing of command tag =====
		this.currCmdName = null;
		this.currCmdInputArgs = null;
		this.currCmdOutputArgs = null;
		
		// ===== Data members for processing of expression tag =====
		this.expMap.clear();
		
		// ===== Parsing result =====
		this.bSuccess = true; // reset the flag which determines whether parsing was successful
	}

	/**
	 * @see com.ogprover.utilities.io.DocHandler#endDocument()
	 */
	public void endDocument() throws Exception {
		String message;
		ILogger logger = OpenGeoProver.settings.getLogger();
		
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
		
		// Validate parsed GG theorem
		boolean bValidThm = true;
		if (this.ggThm == null)
			bValidThm = false;
		else {
			Vector<GeoGebraConstructionCommand> consList = this.ggThm.getConstructionList();
			if (consList == null)
				bValidThm = false;
			else if (this.ggThm.getProveCmd() == null)
				bValidThm = false;
			else if (this.ggThm.getStatement() == null) {
				GeoGebraStatementCommand ggStmCmd = this.createStatementFromProveCmd();
				
				if (ggStmCmd == null)
					bValidThm = false;
				else
					this.ggThm.setStatement(ggStmCmd);
			}
		}
		if (bValidThm == false) {
			message = "Parsed theorem is in incorrect format";
			logger.error(message);
			this.bSuccess = false;
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
	
	/**
	 * Method which parses statement text and creates corresponding statement object.
	 * 
	 * @return	TRUE if operation was successful, FALSE otherwise
	 */
	private GeoGebraStatementCommand createStatementFromProveCmd() {
		ILogger logger = OpenGeoProver.settings.getLogger();
		String proveCmdInputText = this.ggThm.getProveCmd().getInputArg();
		
		if (proveCmdInputText == null){
			logger.error("Statement in bad format - missing statement text");
			return null;
		}
		
		String statementName;
		ArrayList<String> statementArgs = new ArrayList<String>();
		String statResultLabel;
		
		// Check if input text of prove command is "true" or "false
		String uppperCaseStatText = (new String(proveCmdInputText)).toUpperCase();
		if (uppperCaseStatText.equals(BooleanCmd.CMD_TEXT_TRUE) || uppperCaseStatText.equals(BooleanCmd.CMD_TEXT_FALSE)) {
			statementName = GeoGebraStatementCommand.COMMAND_BOOLEAN;
			statementArgs.add(uppperCaseStatText);
		}
		else {
			// Then check if statement text is in expression map - passed in input text of prove command is the label of expression
			String statementText = this.expMap.get(proveCmdInputText);
			if (statementText == null)
				statementText = proveCmdInputText; // if not in map, then use original input text as statement text
			
			// First of all check special infix formats of theorem statement
			if (statementText.indexOf(OGPDocHandler.CH_QUESTION_EQ) != -1) {
				String[] strArr = statementText.split((new Character(OGPDocHandler.CH_QUESTION_EQ)).toString());
			
				if (strArr.length != 2) {
					logger.error("Statement in bad format - incorrect number of statement arguments in infix notation");
					return null;
				}
			
				statementName = GeoGebraStatementCommand.COMMAND_EQUAL;
				statementArgs.add(strArr[0].trim());
				statementArgs.add(strArr[1].trim());
			}
			else if (statementText.indexOf(OGPDocHandler.CH_PARALLEL_TO) != -1) {
				String[] strArr = statementText.split((new Character(OGPDocHandler.CH_PARALLEL_TO)).toString());
			
				if (strArr.length != 2) {
					logger.error("Statement in bad format - incorrect number of statement arguments in infix notation");
					return null;
				}
			
				statementName = GeoGebraStatementCommand.COMMAND_PARALLEL;
				statementArgs.add(strArr[0].trim());
				statementArgs.add(strArr[1].trim());
			}
			else if (statementText.indexOf(OGPDocHandler.CH_PERPENDICULAR) != -1) {
				String[] strArr = statementText.split((new Character(OGPDocHandler.CH_PERPENDICULAR)).toString());
			
				if (strArr.length != 2) {
					logger.error("Statement in bad format - incorrect number of statement arguments in infix notation");
					return null;
				}
			
				statementName = GeoGebraStatementCommand.COMMAND_PERPENDICULAR;
				statementArgs.add(strArr[0].trim());
				statementArgs.add(strArr[1].trim());
			}
			// TODO - "else if" statements for other special infix notations
			else { // prefix notation
				// === Brackets ===
				int length = statementText.length();
				int lbracIdx = statementText.indexOf('[');
				int rbracIdx = statementText.lastIndexOf(']');
				if (lbracIdx == -1 || rbracIdx == -1) {
					logger.error("Statement in bad format - missing bracket");
					return null;
				}
				if (length != rbracIdx + 1) {
					logger.error("Statement in bad format - statement text doesn't end with right bracket");
					return null;
				}
		
				// === Statement name ===
				statementName = statementText.substring(0, lbracIdx);
				if (statementName.length() == 0) {
					logger.error("Statement in bad format - missing statement name");
					return null;
				}
		
				// === Argument list ===
				String args = statementText.substring(lbracIdx + 1, rbracIdx);
				OpenGeoProver.settings.getLogger().debug("Args before parsing : " + args);
				int openBrackets = 0;
				int beginningOfArgument = 0;
				for (int i = 0, s = args.length() ; i < s ; i++) {
					if (args.charAt(i) == ',' && openBrackets == 0) {
						statementArgs.add(args.substring(beginningOfArgument, i).trim());
						beginningOfArgument = i+1;
					}
					else if (args.charAt(i) == '(' || args.charAt(i) == '[')
						openBrackets++;
					else if (args.charAt(i) == ')' || args.charAt(i) == ']')
						openBrackets--;
				}
				statementArgs.add(args.substring(beginningOfArgument, args.length()).trim());
			}
		}
		
		// === Create statement command ===
		Long rndLabelNum = new Long(Math.round(Math.random() * 1000));
		statResultLabel = "statRes_" + rndLabelNum.toString();
		ArrayList<String> outputArgs = new ArrayList<String>();
		outputArgs.add(statResultLabel);
		GeoGebraCommand ggCmd = GeoGebraCommandFactory.createGeoGebraCommand(statementName, statementArgs, outputArgs, GeoGebraObject.OBJ_TYPE_NONE);
		if (ggCmd == null || !(ggCmd instanceof GeoGebraStatementCommand))
			return null;
		return (GeoGebraStatementCommand)ggCmd;
	}
}
