/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.geoconstruction.GeoConstruction;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.ogprover.pp.tp.geoconstruction.Polar;
import com.ogprover.pp.tp.geoconstruction.SetOfPoints;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of Polar objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class PolarConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(Polar.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		Polar polar = (Polar)obj;
		writer.addAttribute("label", polar.getGeoObjectLabel());
		writer.addAttribute("pole", polar.getPole().getGeoObjectLabel());
		writer.addAttribute("set", ((GeoConstruction)polar.getBaseSet()).getGeoObjectLabel());
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		OGPTP consProtocol = OpenGeoProver.settings.getParsedTP();
		String label = reader.getAttribute("label");
		String pole = reader.getAttribute("pole");
		String set = reader.getAttribute("set");
		
		return new Polar(label,
						 (Point)consProtocol.getConstructionMap().get(pole),
						 (SetOfPoints)consProtocol.getConstructionMap().get(set));
	}
	
}