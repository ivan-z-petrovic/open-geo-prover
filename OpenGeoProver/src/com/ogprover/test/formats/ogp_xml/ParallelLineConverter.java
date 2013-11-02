/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.geoconstruction.Line;
import com.ogprover.pp.tp.geoconstruction.ParallelLine;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of ParallelLine objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class ParallelLineConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(ParallelLine.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		ParallelLine line = (ParallelLine)obj;
		writer.addAttribute("label", line.getGeoObjectLabel());
		writer.addAttribute("point", line.getPoints().get(0).getGeoObjectLabel());
		writer.addAttribute("baseline", line.getBaseLine().getGeoObjectLabel());
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		OGPTP consProtocol = OpenGeoProver.settings.getParsedTP();
		String label = reader.getAttribute("label");
		String point = reader.getAttribute("point");
		String baseline = reader.getAttribute("baseline");
		
		return new ParallelLine(label,  
				                (Line)consProtocol.getConstructionMap().get(baseline),
				                (Point)consProtocol.getConstructionMap().get(point));
	}
	
}