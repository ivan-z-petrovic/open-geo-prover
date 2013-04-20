/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import java.util.Vector;

import com.ogprover.pp.tp.auxiliary.GeneralizedSegment;
import com.ogprover.pp.tp.auxiliary.ProductOfTwoSegments;
import com.ogprover.pp.tp.auxiliary.Segment;
import com.ogprover.pp.tp.thmstatement.LinearCombinationOfSquaresOfSegments;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of LinearCombinationOfSquaresOfSegments objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class LinearCombinationOfSquaresOfSegmentsConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(LinearCombinationOfSquaresOfSegments.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		LinearCombinationOfSquaresOfSegments statement = (LinearCombinationOfSquaresOfSegments)obj;
		
		int ii = 0;
		for (GeneralizedSegment genseg : statement.getSegments()) {
			if (genseg instanceof Segment) {
				Segment seg = (Segment)genseg;
				writer.startNode("segment");
				ctx.convertAnother(seg);
				writer.endNode();
			}
			else if (genseg instanceof ProductOfTwoSegments) {
				ProductOfTwoSegments prodseg = (ProductOfTwoSegments)genseg;
				writer.startNode("segprod");
				ctx.convertAnother(prodseg);
				writer.endNode();
			}
			writer.startNode("coeff");
			writer.addAttribute("value", statement.getCoefficients().get(ii).toString());
			writer.endNode();
			ii++;
		}
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		Vector<GeneralizedSegment> segments = new Vector<GeneralizedSegment>();
		Vector<Double> coefficients = new Vector<Double>();
		GeneralizedSegment genseg;
		Double coeff;
		
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			if ("segment".equals(reader.getNodeName())) {
				genseg = (GeneralizedSegment)ctx.convertAnother(null, Segment.class);
				segments.add(genseg);
			}
			else if ("segprod".equals(reader.getNodeName())) {
				genseg = (GeneralizedSegment)ctx.convertAnother(null, ProductOfTwoSegments.class);
				segments.add(genseg);
			}
			else if ("coeff".equals(reader.getNodeName())) {
				coeff = new Double(reader.getAttribute("value"));
				coefficients.add(coeff);
			}
			reader.moveUp();
		}
		
		return new LinearCombinationOfSquaresOfSegments(segments, coefficients);
	}
	
}