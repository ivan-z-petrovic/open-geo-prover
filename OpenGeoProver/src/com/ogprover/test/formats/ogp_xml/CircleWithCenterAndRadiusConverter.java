/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.prover_protocol.cp.OGPCP;
import com.ogprover.prover_protocol.cp.geoconstruction.CircleWithCenterAndRadius;
import com.ogprover.prover_protocol.cp.geoconstruction.Point;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of CircleWithCenterAndRadius objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class CircleWithCenterAndRadiusConverter implements Converter {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(CircleWithCenterAndRadius.class);
	}

	@Override
	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		CircleWithCenterAndRadius circle = (CircleWithCenterAndRadius)obj;
		writer.addAttribute("label", circle.getGeoObjectLabel());
		writer.addAttribute("center", circle.getCenter().getGeoObjectLabel());
		writer.addAttribute("radipt1", circle.getRadius().getFirstEndPoint().getGeoObjectLabel());
		writer.addAttribute("radipt2", circle.getRadius().getSecondEndPoint().getGeoObjectLabel());
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		OGPCP consProtocol = OpenGeoProver.settings.getParsedCP();
		String label = reader.getAttribute("label");
		String center = reader.getAttribute("center");
		String radipoint1 = reader.getAttribute("radipt1");
		String radipoint2 = reader.getAttribute("radipt2");
		
		return new CircleWithCenterAndRadius(label, 
				                             (Point)consProtocol.getConstructionMap().get(center), 
				                             (Point)consProtocol.getConstructionMap().get(radipoint1),
				                             (Point)consProtocol.getConstructionMap().get(radipoint2));
	}
	
}