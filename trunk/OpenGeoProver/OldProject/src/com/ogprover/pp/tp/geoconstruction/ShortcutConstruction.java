/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.geoconstruction;


import java.util.Vector;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Abstract class for shortcut construction</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
/*
 * Note:
 * 	User should be careful with usage of shortcut constructions because
 *  they could increase the complexity of algebraic form of geometric 
 *  constructions - it is not error to have multiple labels for same object
 *  but that will lead to new coordinates and to new variables in system
 *  of polynomials. E.g. one can construct the foot of perpendicular line
 *  from given point to given line and later to construct the perpendicular
 *  line. It is responsibility of construction maker to provide minimal set of
 *  objects that will construct the scene and to avoid repetitions.
 */
public abstract class ShortcutConstruction extends GeoConstruction {
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
     * Vector of elementary constructions that make this shortcut
     */
    protected Vector<GeoConstruction> shortcutListOfConstructions = null;
    
    
	/*
     * ======================================================================
     * ========================= ABSTRACT METHODS ===========================
     * ======================================================================
     */
	/**
	 * Method that retrieves the point constructed by this shortcut construction.
	 * 
	 * @return	Constructed point
	 */
	public abstract Point getPoint();
	/**
	 * Method that retrieves the line constructed by this shortcut construction.
	 * 
	 * @return	Constructed line
	 */
	public abstract Line getLine();
	/**
	 * Method that retrieves the circle constructed by this shortcut construction.
	 * 
	 * @return	Constructed circle
	 */
	public abstract Circle getCircle();
	/**
	 * Method that retrieves the conic constructed by this shortcut construction.
	 * 
	 * @return	Constructed conic
	 */
	public abstract ConicSection getConic();
	
	
    /*
     * ======================================================================
     * ========================== GETTERS/SETTERS ===========================
     * ======================================================================
     */
    /**
	 * Method that retrieves the type of construction
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getConstructionType()
	 */
	@Override
	public int getConstructionType() {
		/* 
		 * This construction will never be examined like single object but
		 * list of construction steps that make it will be added to theorem
		 * protocol and their construction types will be examined.
		 */
		
		return GeoConstruction.GEOCONS_TYPE_UNDEFINED;
	}
	
	/**
	 * @param shortcutListOfConstructions the shortcutListOfConstructions to set
	 */
	public void setShortcutListOfConstructions(
			Vector<GeoConstruction> shortcutListOfConstructions) {
		this.shortcutListOfConstructions = shortcutListOfConstructions;
	}

	/**
	 * @return the shortcutListOfConstructions
	 */
	public Vector<GeoConstruction> getShortcutListOfConstructions() {
		return shortcutListOfConstructions;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method to check the validity of this construction step
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#isValidConstructionStep()
	 */
	@Override
	public boolean isValidConstructionStep() {
		/*
		 * This type of construction should never be added to theorem protocol.
		 * Instead of it, its vector of elementary constructions is added and therefore
		 * these elementary constructions are validated. Therefore, validation of this
		 * type of construction should always fail.
		 */
		return false;
	}
	
	/**
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getInputLabels()
	 */
	@Override
	public String[] getInputLabels() {
		return null; // this is shortcut construction and will be expanded in CP, so partial constructions will return input labels
	}
}
