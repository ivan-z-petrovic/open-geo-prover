/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.pp.tp.geoobject.Segment;
import com.ogprover.pp.tp.thmstatement.RatioOfTwoSegments;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of RatioOfTwoSegments objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class RatioOfTwoSegmentsConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(RatioOfTwoSegments.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		RatioOfTwoSegments statement = (RatioOfTwoSegments)obj;
		
		writer.addAttribute("coeff", statement.getRatioCoefficient() + "");
		writer.startNode("segment");
		ctx.convertAnother(statement.getFirstSegment());
		writer.endNode();
		writer.startNode("segment");
		ctx.convertAnother(statement.getSecondSegment());
		writer.endNode();
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		double coeff = Double.parseDouble(reader.getAttribute("coeff"));
		Segment firstSeg = null;
		Segment secondSeg = null;
		
		if (!reader.hasMoreChildren())
			return null;
		reader.moveDown();
		if (!"segment".equals(reader.getNodeName()))
			return null;
		firstSeg = (Segment)ctx.convertAnother(null, Segment.class);
		reader.moveUp();
		
		if (!reader.hasMoreChildren())
			return null;
		reader.moveDown();
		if (!"segment".equals(reader.getNodeName()))
			return null;
		secondSeg = (Segment)ctx.convertAnother(null, Segment.class);
		reader.moveUp();
		
		if (reader.hasMoreChildren())
			return null;

		return new RatioOfTwoSegments(firstSeg, secondSeg, coeff);
	}
	
}