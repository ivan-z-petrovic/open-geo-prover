/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.prover_protocol.cp.OGPCP;
import com.ogprover.prover_protocol.cp.geoconstruction.CircleWithCenterAndPoint;
import com.ogprover.prover_protocol.cp.geoconstruction.Point;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of CircleWithCenterAndPoint objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class CircleWithCenterAndPointConverter implements Converter {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(CircleWithCenterAndPoint.class);
	}

	@Override
	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		CircleWithCenterAndPoint circle = (CircleWithCenterAndPoint)obj;
		writer.addAttribute("label", circle.getGeoObjectLabel());
		writer.addAttribute("center", circle.getCenter().getGeoObjectLabel());
		writer.addAttribute("point", circle.getPoints().get(0).getGeoObjectLabel());
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		OGPCP consProtocol = OpenGeoProver.settings.getParsedCP();
		String label = reader.getAttribute("label");
		String center = reader.getAttribute("center");
		String point = reader.getAttribute("point");
		
		return new CircleWithCenterAndPoint(label, 
				                           (Point)consProtocol.getConstructionMap().get(center), 
				                           (Point)consProtocol.getConstructionMap().get(point));
	}
	
}