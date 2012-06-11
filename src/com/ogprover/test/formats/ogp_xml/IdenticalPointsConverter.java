/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.prover_protocol.cp.OGPCP;
import com.ogprover.prover_protocol.cp.geoconstruction.Point;
import com.ogprover.prover_protocol.cp.thmstatement.IdenticalPoints;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of IdenticalPoints objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class IdenticalPointsConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(IdenticalPoints.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		IdenticalPoints statement = (IdenticalPoints)obj;
		Point A = (Point) statement.getGeoObjects().get(0);
		Point B = (Point) statement.getGeoObjects().get(1);
		
		writer.addAttribute("point1", A.getGeoObjectLabel());
		writer.addAttribute("point2", B.getGeoObjectLabel());
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		OGPCP consProtocol = OpenGeoProver.settings.getParsedCP();
		String point1 = reader.getAttribute("point1");
		String point2 = reader.getAttribute("point2");
		
		return new IdenticalPoints((Point)consProtocol.getConstructionMap().get(point1),
				                   (Point)consProtocol.getConstructionMap().get(point2));
	}
	
}