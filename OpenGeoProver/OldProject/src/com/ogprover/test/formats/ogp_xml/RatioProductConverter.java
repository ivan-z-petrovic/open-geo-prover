/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import java.util.Vector;

import com.ogprover.pp.tp.auxiliary.RatioOfTwoCollinearSegments;
import com.ogprover.pp.tp.auxiliary.RatioProduct;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of RatioProduct objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class RatioProductConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(RatioProduct.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		RatioProduct prod = (RatioProduct)obj;
		
		for (RatioOfTwoCollinearSegments ratio : prod.getRatios()) {
			writer.startNode("ratiocollinearsegs");
			ctx.convertAnother(ratio);
			writer.endNode();
		}
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		Vector<RatioOfTwoCollinearSegments> ratios = new Vector<RatioOfTwoCollinearSegments>();
		
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			if ("ratiocollinearsegs".equals(reader.getNodeName()))
				ratios.add((RatioOfTwoCollinearSegments)ctx.convertAnother(null, RatioOfTwoCollinearSegments.class));
			else
				return null;
			reader.moveUp();
		}

		return new RatioProduct(ratios);
	}
	
}