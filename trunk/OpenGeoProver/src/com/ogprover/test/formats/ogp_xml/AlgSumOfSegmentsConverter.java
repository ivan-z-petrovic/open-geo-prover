/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.prover_protocol.cp.auxiliary.GeneralizedSegment;
import com.ogprover.prover_protocol.cp.auxiliary.ProductOfTwoSegments;
import com.ogprover.prover_protocol.cp.auxiliary.Segment;
import com.ogprover.prover_protocol.cp.thmstatement.AlgebraicSumOfThreeSegments;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of AlgebraicSumOfThreeSegments objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class AlgSumOfSegmentsConverter implements Converter {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(AlgebraicSumOfThreeSegments.class);
	}

	@Override
	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		AlgebraicSumOfThreeSegments sum = (AlgebraicSumOfThreeSegments)obj;
		GeneralizedSegment firstSeg = sum.getFirstSegment();
		GeneralizedSegment secondSeg = sum.getSecondSegment();
		GeneralizedSegment thirdSeg = sum.getThirdSegment();
		
		if (firstSeg instanceof Segment) {
			writer.startNode("segment");
			ctx.convertAnother(firstSeg);
			writer.endNode();
			writer.startNode("segment");
			ctx.convertAnother(secondSeg);
			writer.endNode();
			writer.startNode("segment");
			ctx.convertAnother(thirdSeg);
			writer.endNode();
		}
		else if (firstSeg instanceof ProductOfTwoSegments) {
			writer.startNode("segprod");
			ctx.convertAnother(firstSeg);
			writer.endNode();
			writer.startNode("segprod");
			ctx.convertAnother(secondSeg);
			writer.endNode();
			writer.startNode("segprod");
			ctx.convertAnother(thirdSeg);
			writer.endNode();
		}
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		GeneralizedSegment firstSeg = null, secondSeg = null, thirdSeg = null;
		
		if (!reader.hasMoreChildren())
			return null;
		
		reader.moveDown();
		if ("segment".equals(reader.getNodeName())) {
			firstSeg = (Segment)ctx.convertAnother(null, Segment.class);
			reader.moveUp();
			
			if (!reader.hasMoreChildren())
				return null;
			
			reader.moveDown();
			if (!"segment".equals(reader.getNodeName()))
				return null;
			secondSeg = (Segment)ctx.convertAnother(null, Segment.class);
			reader.moveUp();
			
			if (!reader.hasMoreChildren())
				return null;
			
			reader.moveDown();
			if (!"segment".equals(reader.getNodeName()))
				return null;
			thirdSeg = (Segment)ctx.convertAnother(null, Segment.class);
			reader.moveUp();
		}
		else if ("segprod".equals(reader.getNodeName())) {
			firstSeg = (ProductOfTwoSegments)ctx.convertAnother(null, ProductOfTwoSegments.class);
			reader.moveUp();
			
			if (!reader.hasMoreChildren())
				return null;
			
			reader.moveDown();
			if (!"segprod".equals(reader.getNodeName()))
				return null;
			secondSeg = (ProductOfTwoSegments)ctx.convertAnother(null, ProductOfTwoSegments.class);
			reader.moveUp();
			
			if (!reader.hasMoreChildren())
				return null;
			
			reader.moveDown();
			if (!"segprod".equals(reader.getNodeName()))
				return null;
			thirdSeg = (ProductOfTwoSegments)ctx.convertAnother(null, ProductOfTwoSegments.class);
			reader.moveUp();
		}
		else
			return null;
		
		return new AlgebraicSumOfThreeSegments(firstSeg, secondSeg, thirdSeg);
	}
	
}