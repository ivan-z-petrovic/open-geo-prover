/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.pp.tp.auxiliary.ProductOfTwoSegments;
import com.ogprover.pp.tp.auxiliary.Segment;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of ProductOfTwoSegments objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class ProductOfTwoSegmentsConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(ProductOfTwoSegments.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		ProductOfTwoSegments prod = (ProductOfTwoSegments)obj;
		
		writer.startNode("segment");
		ctx.convertAnother(prod.getFirstSegment());
		writer.endNode();
		writer.startNode("segment");
		ctx.convertAnother(prod.getSecondSegment());
		writer.endNode();
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		Segment firstSeg = null;
		Segment secondSeg = null;
		
		reader.moveDown();
		if ("segment".equals(reader.getNodeName()))
			firstSeg = (Segment)ctx.convertAnother(null, Segment.class);
		else
			return null;
		reader.moveUp();
		reader.moveDown();
		if ("segment".equals(reader.getNodeName()))
			secondSeg = (Segment)ctx.convertAnother(null, Segment.class);
		else
			return null;
		reader.moveUp();
		
		if (reader.hasMoreChildren())
			return null;

		return new ProductOfTwoSegments(firstSeg, secondSeg);
	}
	
}