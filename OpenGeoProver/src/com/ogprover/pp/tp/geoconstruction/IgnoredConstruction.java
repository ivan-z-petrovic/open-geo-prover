/* 
 * DISCLAIMER PLACEHOLDER 
 */
package com.ogprover.pp.tp.geoconstruction;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class to describe a construction to ignore during the conversion part of the algorithm.
*     It is useful to simply drop useless constructions such as polygons, for the area method.</dd>
* </dl>
* 
* @version 1.00
* @author Damien Desfontaines
*/
public class IgnoredConstruction extends GeoConstruction {
	@Override
	public int getConstructionType() {
		return GEOCONS_TYPE_IGNORED;
	}

	@Override
	public String getConstructionDesc() {
		return "Construction ignored by the conversion step";
	}

	@Override
	public String[] getInputLabels() {
		return null;
	}
}
