/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.geoconstruction.CircleWithCenterAndPoint;
import com.ogprover.pp.tp.geoconstruction.Point;
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
	public boolean canConvert(Class clazz) {
		return clazz.equals(CircleWithCenterAndPoint.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		CircleWithCenterAndPoint circle = (CircleWithCenterAndPoint)obj;
		writer.addAttribute("label", circle.getGeoObjectLabel());
		writer.addAttribute("center", circle.getCenter().getGeoObjectLabel());
		writer.addAttribute("point", circle.getPoints().get(0).getGeoObjectLabel());
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		OGPTP consProtocol = OpenGeoProver.settings.getParsedCP();
		String label = reader.getAttribute("label");
		String center = reader.getAttribute("center");
		String point = reader.getAttribute("point");
		
		return new CircleWithCenterAndPoint(label, 
				                           (Point)consProtocol.getConstructionMap().get(center), 
				                           (Point)consProtocol.getConstructionMap().get(point));
	}
	
}