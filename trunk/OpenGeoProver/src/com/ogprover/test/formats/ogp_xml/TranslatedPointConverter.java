/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.prover_protocol.cp.OGPCP;
import com.ogprover.prover_protocol.cp.geoconstruction.Point;
import com.ogprover.prover_protocol.cp.geoconstruction.TranslatedPoint;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of TranslatedPoint objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class TranslatedPointConverter implements Converter {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(TranslatedPoint.class);
	}

	@Override
	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		TranslatedPoint point = (TranslatedPoint)obj;
		writer.addAttribute("label", point.getGeoObjectLabel());
		writer.addAttribute("point1", point.getPointA().getGeoObjectLabel());
		writer.addAttribute("point2", point.getPointB().getGeoObjectLabel());
		writer.addAttribute("origpt", point.getOriginalPoint().getGeoObjectLabel());
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		OGPCP consProtocol = OpenGeoProver.settings.getParsedCP();
		String label = reader.getAttribute("label");
		String origpt = reader.getAttribute("origpt");
		String pointA = reader.getAttribute("point1");
		String pointB = reader.getAttribute("point2");
		
		return new TranslatedPoint(label, 
				                   (Point)consProtocol.getConstructionMap().get(pointA), 
				                   (Point)consProtocol.getConstructionMap().get(pointB),
				                   (Point)consProtocol.getConstructionMap().get(origpt));
	}
	
}