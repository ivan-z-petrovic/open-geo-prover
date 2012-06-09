/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.geoconstruction.Circle;
import com.ogprover.pp.tp.geoconstruction.InverseOfPoint;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of InverseOfPoint objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class InversePointConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(InverseOfPoint.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		InverseOfPoint point = (InverseOfPoint)obj;
		writer.addAttribute("label", point.getGeoObjectLabel());
		writer.addAttribute("point", point.getOriginalPoint().getGeoObjectLabel());
		writer.addAttribute("circle", point.getCircleOfInversion().getGeoObjectLabel());
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		OGPTP consProtocol = OpenGeoProver.settings.getParsedCP();
		String label = reader.getAttribute("label");
		String point = reader.getAttribute("point");
		String circle = reader.getAttribute("circle");
		
		return new InverseOfPoint(label, 
				                  (Point)consProtocol.getConstructionMap().get(point), 
				                  (Circle)consProtocol.getConstructionMap().get(circle));
	}
	
}