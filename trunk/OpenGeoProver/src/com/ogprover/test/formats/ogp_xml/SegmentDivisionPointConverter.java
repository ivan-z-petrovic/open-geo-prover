/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.prover_protocol.cp.OGPCP;
import com.ogprover.prover_protocol.cp.geoconstruction.Point;
import com.ogprover.prover_protocol.cp.geoconstruction.SegmentDivisionPoint;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of SegmentDivisionPoint objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class SegmentDivisionPointConverter implements Converter {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(SegmentDivisionPoint.class);
	}

	@Override
	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		SegmentDivisionPoint point = (SegmentDivisionPoint)obj;
		
		writer.addAttribute("label", point.getGeoObjectLabel());
		writer.addAttribute("point1", point.getSegment().getFirstEndPoint().getGeoObjectLabel());
		writer.addAttribute("point2", point.getSegment().getSecondEndPoint().getGeoObjectLabel());
		writer.addAttribute("coeff", point.getDivisionCoefficient() + "");
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		OGPCP consProtocol = OpenGeoProver.settings.getParsedCP();
		String label = reader.getAttribute("label");
		String point1 = reader.getAttribute("point1");
		String point2 = reader.getAttribute("point2");
		double coeff = Double.parseDouble(reader.getAttribute("coeff"));

		return new SegmentDivisionPoint(label, 
				                        (Point)consProtocol.getConstructionMap().get(point1), 
				                        (Point)consProtocol.getConstructionMap().get(point2), 
				                        coeff);
	}
	
}