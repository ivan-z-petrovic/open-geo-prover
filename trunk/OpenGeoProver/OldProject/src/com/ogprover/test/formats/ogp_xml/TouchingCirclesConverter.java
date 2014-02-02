/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.geoconstruction.Circle;
import com.ogprover.pp.tp.thmstatement.TouchingCircles;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of TouchingCircles objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class TouchingCirclesConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(TouchingCircles.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		TouchingCircles statement = (TouchingCircles)obj;
		writer.addAttribute("circle1", statement.getGeoObjects().get(0).getGeoObjectLabel());
		writer.addAttribute("circle2", statement.getGeoObjects().get(1).getGeoObjectLabel());
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		OGPTP consProtocol = OpenGeoProver.settings.getParsedTP();
		String circle1 = reader.getAttribute("circle1");
		String circle2 = reader.getAttribute("circle2");
		
		return new TouchingCircles((Circle)consProtocol.getConstructionMap().get(circle1), 
				                   (Circle)consProtocol.getConstructionMap().get(circle2));
	}
	
}