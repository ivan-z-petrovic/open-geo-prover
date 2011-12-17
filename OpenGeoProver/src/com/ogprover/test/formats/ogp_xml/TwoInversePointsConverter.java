/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.prover_protocol.cp.OGPCP;
import com.ogprover.prover_protocol.cp.geoconstruction.Circle;
import com.ogprover.prover_protocol.cp.geoconstruction.Point;
import com.ogprover.prover_protocol.cp.thmstatement.TwoInversePoints;
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
	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(TwoInversePoints.class);
	}

	@Override
	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		TwoInversePoints statement = (TwoInversePoints)obj;
		writer.addAttribute("point1", statement.getGeoObjects().get(0).getGeoObjectLabel());
		writer.addAttribute("point2", statement.getGeoObjects().get(1).getGeoObjectLabel());
		writer.addAttribute("circle", statement.getGeoObjects().get(2).getGeoObjectLabel());
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		OGPCP consProtocol = OpenGeoProver.settings.getParsedCP();
		String point1 = reader.getAttribute("point1");
		String point2 = reader.getAttribute("point2");
		String circle = reader.getAttribute("circle");
		
		return new TwoInversePoints((Point)consProtocol.getConstructionMap().get(point1), 
				                    (Point)consProtocol.getConstructionMap().get(point2),
				                    (Circle)consProtocol.getConstructionMap().get(circle));
	}
	
}