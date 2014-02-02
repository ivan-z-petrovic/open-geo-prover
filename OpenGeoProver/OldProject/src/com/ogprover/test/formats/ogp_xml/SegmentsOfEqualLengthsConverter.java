/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.ogprover.pp.tp.thmstatement.SegmentsOfEqualLengths;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of SegmentsOfEqualLengths objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class SegmentsOfEqualLengthsConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(SegmentsOfEqualLengths.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		SegmentsOfEqualLengths statement = (SegmentsOfEqualLengths)obj;
		writer.addAttribute("point11", statement.getGeoObjects().get(0).getGeoObjectLabel());
		writer.addAttribute("point12", statement.getGeoObjects().get(1).getGeoObjectLabel());
		writer.addAttribute("point21", statement.getGeoObjects().get(2).getGeoObjectLabel());
		writer.addAttribute("point22", statement.getGeoObjects().get(3).getGeoObjectLabel());
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		OGPTP consProtocol = OpenGeoProver.settings.getParsedTP();
		String point11 = reader.getAttribute("point11");
		String point12 = reader.getAttribute("point12");
		String point21 = reader.getAttribute("point21");
		String point22 = reader.getAttribute("point22");
		
		return new SegmentsOfEqualLengths((Point)consProtocol.getConstructionMap().get(point11), 
				                          (Point)consProtocol.getConstructionMap().get(point12),
				                          (Point)consProtocol.getConstructionMap().get(point21),
				                          (Point)consProtocol.getConstructionMap().get(point22));
	}
	
}