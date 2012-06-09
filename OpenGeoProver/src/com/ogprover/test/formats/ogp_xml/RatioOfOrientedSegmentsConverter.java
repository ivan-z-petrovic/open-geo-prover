/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.auxiliary.Segment;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.ogprover.pp.tp.thmstatement.RatioOfOrientedSegments;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of RatioOfOrientedSegments objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class RatioOfOrientedSegmentsConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(RatioOfOrientedSegments.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		RatioOfOrientedSegments statement = (RatioOfOrientedSegments)obj;
		writer.addAttribute("point11", statement.getFirstSegment().getFirstEndPoint().getGeoObjectLabel());
		writer.addAttribute("point12", statement.getFirstSegment().getSecondEndPoint().getGeoObjectLabel());
		writer.addAttribute("point21", statement.getSecondSegment().getFirstEndPoint().getGeoObjectLabel());
		writer.addAttribute("point22", statement.getSecondSegment().getSecondEndPoint().getGeoObjectLabel());
		writer.addAttribute("ratiocoeff", statement.getRatioCoefficient() + "");
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		OGPTP consProtocol = OpenGeoProver.settings.getParsedCP();
		String point11 = reader.getAttribute("point11");
		String point12 = reader.getAttribute("point12");
		String point21 = reader.getAttribute("point21");
		String point22 = reader.getAttribute("point22");
		double ratioCoeff = Double.parseDouble(reader.getAttribute("ratiocoeff"));
		
		Segment firstSegment = new Segment((Point)consProtocol.getConstructionMap().get(point11), (Point)consProtocol.getConstructionMap().get(point12));
		Segment secondSegment = new Segment((Point)consProtocol.getConstructionMap().get(point21), (Point)consProtocol.getConstructionMap().get(point22));
		
		return new RatioOfOrientedSegments(firstSegment, secondSegment, ratioCoeff);
	}
	
}