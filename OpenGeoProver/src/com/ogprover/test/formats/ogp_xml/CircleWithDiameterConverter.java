/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.prover_protocol.cp.OGPCP;
import com.ogprover.prover_protocol.cp.geoconstruction.CircleWithDiameter;
import com.ogprover.prover_protocol.cp.geoconstruction.Point;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of CircleWithDiameter objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class CircleWithDiameterConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(CircleWithDiameter.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		CircleWithDiameter circle = (CircleWithDiameter)obj;
		writer.addAttribute("label", circle.getGeoObjectLabel());
		writer.addAttribute("diampt1", circle.getDiameter().getFirstEndPoint().getGeoObjectLabel());
		writer.addAttribute("diampt2", circle.getDiameter().getSecondEndPoint().getGeoObjectLabel());
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		OGPCP consProtocol = OpenGeoProver.settings.getParsedCP();
		String label = reader.getAttribute("label");
		String diampt1 = reader.getAttribute("diampt1");
		String diampt2 = reader.getAttribute("diampt2");
		
		return new CircleWithDiameter(label,
				                      (Point)consProtocol.getConstructionMap().get(diampt1),
				                      (Point)consProtocol.getConstructionMap().get(diampt2));
	}
	
}