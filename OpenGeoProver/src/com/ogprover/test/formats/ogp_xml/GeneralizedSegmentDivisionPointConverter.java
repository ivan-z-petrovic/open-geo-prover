/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.prover_protocol.cp.auxiliary.RatioProduct;
import com.ogprover.prover_protocol.cp.auxiliary.Segment;
import com.ogprover.prover_protocol.cp.geoconstruction.GeneralizedSegmentDivisionPoint;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of GeneralizedSegmentDivisionPoint objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class GeneralizedSegmentDivisionPointConverter implements Converter {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(GeneralizedSegmentDivisionPoint.class);
	}

	@Override
	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		GeneralizedSegmentDivisionPoint point = (GeneralizedSegmentDivisionPoint)obj;
		
		writer.addAttribute("label", point.getGeoObjectLabel());
		writer.addAttribute("coeff", point.getMultiplicatorCoefficient() + "");
		writer.startNode("segment");
		ctx.convertAnother(point.getSegment());
		writer.endNode();
		writer.startNode("ratioprod");
		ctx.convertAnother(point.getRatioProduct());
		writer.endNode();
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		String label = reader.getAttribute("label");
		double coeff = Double.parseDouble(reader.getAttribute("coeff"));
		int numOfChildren = 0;
		RatioProduct prod = null;
		Segment seg = null;
		
		while (reader.hasMoreChildren()) {
			numOfChildren++;
			
			if (numOfChildren > 2)
				return null;
			
			reader.moveDown();
			if ("segment".equals(reader.getNodeName()))
				seg = (Segment)ctx.convertAnother(null, Segment.class);
			else if ("ratioprod".equals(reader.getNodeName()))
				prod = (RatioProduct)ctx.convertAnother(null, RatioProduct.class);
			else
				return null;
			reader.moveUp();
		}
		
		if (numOfChildren < 2)
			return null;

		return new GeneralizedSegmentDivisionPoint(label, seg.getFirstEndPoint(), seg.getSecondEndPoint(), prod, coeff);
	}
	
}