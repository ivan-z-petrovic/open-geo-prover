/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.pp.tp.auxiliary.RatioOfTwoCollinearSegments;
import com.ogprover.pp.tp.auxiliary.Segment;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of RatioOfTwoCollinearSegments objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class RatioOfTwoCollinearSegmentsConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(RatioOfTwoCollinearSegments.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		RatioOfTwoCollinearSegments ratio = (RatioOfTwoCollinearSegments)obj;
		
		writer.startNode("segment");
		ctx.convertAnother(ratio.getNumeratorSegment());
		writer.endNode();
		writer.startNode("segment");
		ctx.convertAnother(ratio.getDenominatorSegment());
		writer.endNode();
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		Segment numeratorSeg = null;
		Segment denominatorSeg = null;
		
		reader.moveDown();
		if ("segment".equals(reader.getNodeName()))
			numeratorSeg = (Segment)ctx.convertAnother(null, Segment.class);
		else
			return null;
		reader.moveUp();
		reader.moveDown();
		if ("segment".equals(reader.getNodeName()))
			denominatorSeg = (Segment)ctx.convertAnother(null, Segment.class);
		else
			return null;
		reader.moveUp();
		
		if (reader.hasMoreChildren())
			return null;

		return new RatioOfTwoCollinearSegments(numeratorSeg, denominatorSeg);
	}
	
}