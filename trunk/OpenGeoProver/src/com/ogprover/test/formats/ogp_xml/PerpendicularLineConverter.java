/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.prover_protocol.cp.OGPCP;
import com.ogprover.prover_protocol.cp.geoconstruction.Line;
import com.ogprover.prover_protocol.cp.geoconstruction.PerpendicularLine;
import com.ogprover.prover_protocol.cp.geoconstruction.Point;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of PerpendicularLine objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class PerpendicularLineConverter implements Converter {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(PerpendicularLine.class);
	}

	@Override
	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		PerpendicularLine line = (PerpendicularLine)obj;
		writer.addAttribute("label", line.getGeoObjectLabel());
		writer.addAttribute("point", line.getPoints().get(0).getGeoObjectLabel());
		writer.addAttribute("baseline", line.getBaseLine().getGeoObjectLabel());
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		OGPCP consProtocol = OpenGeoProver.settings.getParsedCP();
		String label = reader.getAttribute("label");
		String point = reader.getAttribute("point");
		String baseline = reader.getAttribute("baseline");
		
		return new PerpendicularLine(label,  
				                     (Line)consProtocol.getConstructionMap().get(baseline),
				                     (Point)consProtocol.getConstructionMap().get(point));
	}
	
}