/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.prover_protocol.cp.OGPCP;
import com.ogprover.prover_protocol.cp.geoconstruction.AngleTrisector;
import com.ogprover.prover_protocol.cp.geoconstruction.Point;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of AngleTrisector objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class AngleTrisectorConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(AngleTrisector.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		AngleTrisector angTris = (AngleTrisector)obj;
		writer.addAttribute("label", angTris.getGeoObjectLabel());
		writer.addAttribute("firstraypt", angTris.getAngle().getFirstRayPoint().getGeoObjectLabel());
		writer.addAttribute("vertex", angTris.getAngle().getVertex().getGeoObjectLabel());
		writer.addAttribute("secondraypt", angTris.getAngle().getSecondRayPoint().getGeoObjectLabel());
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		OGPCP consProtocol = OpenGeoProver.settings.getParsedCP();
		String label = reader.getAttribute("label");
		String firstraypt = reader.getAttribute("firstraypt");
		String vertex = reader.getAttribute("vertex");
		String secondraypt = reader.getAttribute("secondraypt");
		
		return new AngleTrisector(label, 
				                 (Point)consProtocol.getConstructionMap().get(firstraypt), 
				                 (Point)consProtocol.getConstructionMap().get(vertex),
				                 (Point)consProtocol.getConstructionMap().get(secondraypt));
	}
	
}