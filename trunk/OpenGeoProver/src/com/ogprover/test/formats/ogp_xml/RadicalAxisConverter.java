/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.prover_protocol.cp.OGPCP;
import com.ogprover.prover_protocol.cp.geoconstruction.Circle;
import com.ogprover.prover_protocol.cp.geoconstruction.RadicalAxis;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of RadicalAxis objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class RadicalAxisConverter implements Converter {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(RadicalAxis.class);
	}

	@Override
	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		RadicalAxis line = (RadicalAxis)obj;
		writer.addAttribute("label", line.getGeoObjectLabel());
		writer.addAttribute("circle1", line.getFirstCircle().getGeoObjectLabel());
		writer.addAttribute("circle2", line.getSecondCircle().getGeoObjectLabel());
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		OGPCP consProtocol = OpenGeoProver.settings.getParsedCP();
		String label = reader.getAttribute("label");
		String circle1 = reader.getAttribute("circle1");
		String circle2 = reader.getAttribute("circle2");
		
		return new RadicalAxis(label, 
				               (Circle)consProtocol.getConstructionMap().get(circle1), 
				               (Circle)consProtocol.getConstructionMap().get(circle2));
	}
	
}