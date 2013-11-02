/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.geoconstruction.Circle;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.ogprover.pp.tp.thmstatement.TwoInversePoints;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of TwoInversePoints objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class TwoInversePointsConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(TwoInversePoints.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		TwoInversePoints statement = (TwoInversePoints)obj;
		writer.addAttribute("point1", statement.getGeoObjects().get(0).getGeoObjectLabel());
		writer.addAttribute("point2", statement.getGeoObjects().get(1).getGeoObjectLabel());
		writer.addAttribute("circle", statement.getGeoObjects().get(2).getGeoObjectLabel());
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		OGPTP consProtocol = OpenGeoProver.settings.getParsedTP();
		String point1 = reader.getAttribute("point1");
		String point2 = reader.getAttribute("point2");
		String circle = reader.getAttribute("circle");
		
		return new TwoInversePoints((Point)consProtocol.getConstructionMap().get(point1), 
				                    (Point)consProtocol.getConstructionMap().get(point2),
				                    (Circle)consProtocol.getConstructionMap().get(circle));
	}
	
}