/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.prover_protocol.cp.auxiliary.Segment;
import com.ogprover.prover_protocol.cp.thmstatement.EqualityOfTwoRatios;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of EqualityOfTwoRatios objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class EqualityOfTwoRatiosConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(EqualityOfTwoRatios.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		EqualityOfTwoRatios statement = (EqualityOfTwoRatios)obj;
		
		writer.addAttribute("coeff", statement.getMultiplicatorCoefficient() + "");
		writer.startNode("rationum");
		ctx.convertAnother(statement.getNumeratorSegmentOfFirstRatio());
		writer.endNode();
		writer.startNode("ratioden");
		ctx.convertAnother(statement.getDenominatorSegmentOfFirstRatio());
		writer.endNode();
		writer.startNode("rationum");
		ctx.convertAnother(statement.getNumeratorSegmentOfFirstRatio());
		writer.endNode();
		writer.startNode("ratioden");
		ctx.convertAnother(statement.getDenominatorSegmentOfFirstRatio());
		writer.endNode();
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		double coeff = Double.parseDouble(reader.getAttribute("coeff"));
		Segment num1 = null, den1 = null, num2 = null, den2 = null;
		
		if (!reader.hasMoreChildren())
			return null;
		reader.moveDown();
		if (!"rationum".equals(reader.getNodeName()))
			return null;
		num1 = (Segment)ctx.convertAnother(null, Segment.class);
		reader.moveUp();
		
		if (!reader.hasMoreChildren())
			return null;
		reader.moveDown();
		if (!"ratioden".equals(reader.getNodeName()))
			return null;
		den1 = (Segment)ctx.convertAnother(null, Segment.class);
		reader.moveUp();
		
		if (!reader.hasMoreChildren())
			return null;
		reader.moveDown();
		if (!"rationum".equals(reader.getNodeName()))
			return null;
		num2 = (Segment)ctx.convertAnother(null, Segment.class);
		reader.moveUp();
		
		if (!reader.hasMoreChildren())
			return null;
		reader.moveDown();
		if (!"ratioden".equals(reader.getNodeName()))
			return null;
		den2 = (Segment)ctx.convertAnother(null, Segment.class);
		reader.moveUp();
		
		if (reader.hasMoreChildren())
			return null;

		return new EqualityOfTwoRatios(num1, den1, num2, den2, coeff);
	}
	
}