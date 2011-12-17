/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.prover_protocol.cp.OGPCP;
import com.ogprover.prover_protocol.cp.geoconstruction.HarmonicConjugatePoint;
import com.ogprover.prover_protocol.cp.geoconstruction.Point;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of HarmonicConjugatePoint objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class HarmonicConjugatePointConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(HarmonicConjugatePoint.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		HarmonicConjugatePoint point = (HarmonicConjugatePoint)obj;
		writer.addAttribute("label", point.getGeoObjectLabel());
		writer.addAttribute("point1", point.getPointA().getGeoObjectLabel());
		writer.addAttribute("point2", point.getPointB().getGeoObjectLabel());
		writer.addAttribute("point3", point.getPointC().getGeoObjectLabel());
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		OGPCP consProtocol = OpenGeoProver.settings.getParsedCP();
		String label = reader.getAttribute("label");
		String point1 = reader.getAttribute("point1");
		String point2 = reader.getAttribute("point2");
		String point3 = reader.getAttribute("point3");
		
		return new HarmonicConjugatePoint(label, 
				                          (Point)consProtocol.getConstructionMap().get(point1), 
				                          (Point)consProtocol.getConstructionMap().get(point2),
				                          (Point)consProtocol.getConstructionMap().get(point3));
	}
	
}