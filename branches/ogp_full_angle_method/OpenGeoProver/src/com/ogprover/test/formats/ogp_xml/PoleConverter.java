/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.geoconstruction.GeoConstruction;
import com.ogprover.pp.tp.geoconstruction.Line;
import com.ogprover.pp.tp.geoconstruction.Pole;
import com.ogprover.pp.tp.geoconstruction.SetOfPoints;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of Pole objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class PoleConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(Pole.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		Pole pole = (Pole)obj;
		writer.addAttribute("label", pole.getGeoObjectLabel());
		writer.addAttribute("polar", pole.getPolar().getGeoObjectLabel());
		writer.addAttribute("set", ((GeoConstruction)pole.getBaseSet()).getGeoObjectLabel());
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		OGPTP consProtocol = OpenGeoProver.settings.getParsedCP();
		String label = reader.getAttribute("label");
		String polar = reader.getAttribute("polar");
		String set = reader.getAttribute("set");
		
		return new Pole(label,
						(Line)consProtocol.getConstructionMap().get(polar),
						(SetOfPoints)consProtocol.getConstructionMap().get(set));
	}
	
}