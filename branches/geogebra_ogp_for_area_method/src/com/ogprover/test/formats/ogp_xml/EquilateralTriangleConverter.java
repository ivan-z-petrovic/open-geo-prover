/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.prover_protocol.cp.OGPCP;
import com.ogprover.prover_protocol.cp.geoconstruction.Point;
import com.ogprover.prover_protocol.cp.thmstatement.EquilateralTriangle;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of EquilateralTriangle objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class EquilateralTriangleConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(EquilateralTriangle.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		EquilateralTriangle statement = (EquilateralTriangle)obj;
		
		writer.addAttribute("pointA", statement.getGeoObjects().get(0).getGeoObjectLabel());
		writer.addAttribute("pointB", statement.getGeoObjects().get(1).getGeoObjectLabel());
		writer.addAttribute("pointC", statement.getGeoObjects().get(2).getGeoObjectLabel());
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		OGPCP consProtocol = OpenGeoProver.settings.getParsedCP();
		String pointA = reader.getAttribute("pointA");
		String pointB = reader.getAttribute("pointB");
		String pointC = reader.getAttribute("pointC");

		return new EquilateralTriangle((Point)consProtocol.getConstructionMap().get(pointA),
				                       (Point)consProtocol.getConstructionMap().get(pointB),
				                       (Point)consProtocol.getConstructionMap().get(pointC));
	}
	
}