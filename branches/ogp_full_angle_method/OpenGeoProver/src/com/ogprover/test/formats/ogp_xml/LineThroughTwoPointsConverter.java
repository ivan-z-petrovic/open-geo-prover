/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.geoconstruction.LineThroughTwoPoints;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of LineThroughTwoPoints objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class LineThroughTwoPointsConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(LineThroughTwoPoints.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		LineThroughTwoPoints line = (LineThroughTwoPoints)obj;
		writer.addAttribute("label", line.getGeoObjectLabel());
		writer.addAttribute("point1", line.getPoints().get(0).getGeoObjectLabel());
		writer.addAttribute("point2", line.getPoints().get(1).getGeoObjectLabel());
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		OGPTP consProtocol = OpenGeoProver.settings.getParsedCP();
		String label = reader.getAttribute("label");
		String point1 = reader.getAttribute("point1");
		String point2 = reader.getAttribute("point2");
		
		return new LineThroughTwoPoints(label, 
				                        (Point)consProtocol.getConstructionMap().get(point1), 
				                        (Point)consProtocol.getConstructionMap().get(point2));
	}
	
}