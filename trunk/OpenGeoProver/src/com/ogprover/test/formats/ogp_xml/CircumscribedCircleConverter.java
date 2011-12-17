/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.prover_protocol.cp.OGPCP;
import com.ogprover.prover_protocol.cp.geoconstruction.CircumscribedCircle;
import com.ogprover.prover_protocol.cp.geoconstruction.Point;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of CircumscribedCircle objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class CircumscribedCircleConverter implements Converter {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(CircumscribedCircle.class);
	}

	@Override
	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		CircumscribedCircle circle = (CircumscribedCircle)obj;
		writer.addAttribute("label", circle.getGeoObjectLabel());
		writer.addAttribute("point1", circle.getPoints().get(0).getGeoObjectLabel());
		writer.addAttribute("point2", circle.getPoints().get(1).getGeoObjectLabel());
		writer.addAttribute("point3", circle.getPoints().get(2).getGeoObjectLabel());
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		OGPCP consProtocol = OpenGeoProver.settings.getParsedCP();
		String label = reader.getAttribute("label");
		String point1 = reader.getAttribute("point1");
		String point2 = reader.getAttribute("point2");
		String point3 = reader.getAttribute("point3");
		
		return new CircumscribedCircle(label,
				                       (Point)consProtocol.getConstructionMap().get(point1),
				                       (Point)consProtocol.getConstructionMap().get(point2),
				                       (Point)consProtocol.getConstructionMap().get(point3));
	}
	
}