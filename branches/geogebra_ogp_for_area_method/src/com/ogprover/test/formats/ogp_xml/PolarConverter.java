/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.prover_protocol.cp.OGPCP;
import com.ogprover.prover_protocol.cp.geoconstruction.GeoConstruction;
import com.ogprover.prover_protocol.cp.geoconstruction.Point;
import com.ogprover.prover_protocol.cp.geoconstruction.Polar;
import com.ogprover.prover_protocol.cp.geoconstruction.SetOfPoints;
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
		OGPCP consProtocol = OpenGeoProver.settings.getParsedCP();
		String label = reader.getAttribute("label");
		String pole = reader.getAttribute("pole");
		String set = reader.getAttribute("set");
		
		return new Polar(label,
						 (Point)consProtocol.getConstructionMap().get(pole),
						 (SetOfPoints)consProtocol.getConstructionMap().get(set));
	}
	
}