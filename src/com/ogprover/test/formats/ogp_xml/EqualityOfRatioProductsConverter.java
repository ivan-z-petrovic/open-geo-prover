/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.prover_protocol.cp.auxiliary.RatioProduct;
import com.ogprover.prover_protocol.cp.thmstatement.EqualityOfRatioProducts;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of EqualityOfRatioProducts objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class EqualityOfRatioProductsConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(EqualityOfRatioProducts.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		EqualityOfRatioProducts statement = (EqualityOfRatioProducts)obj;
		
		writer.addAttribute("coeff", statement.getMultiplicatorFactor() + "");
		writer.startNode("ratioprod");
		ctx.convertAnother(statement.getLeftProduct());
		writer.endNode();
		writer.startNode("ratioprod");
		ctx.convertAnother(statement.getRightProduct());
		writer.endNode();
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		double coeff = Double.parseDouble(reader.getAttribute("coeff"));
		RatioProduct lprod = null, rprod = null;
		
		if (!reader.hasMoreChildren())
			return null;
		reader.moveDown();
		if (!"ratioprod".equals(reader.getNodeName()))
			return null;
		lprod = (RatioProduct)ctx.convertAnother(null, RatioProduct.class);
		reader.moveUp();
		
		if (!reader.hasMoreChildren())
			return null;
		reader.moveDown();
		if (!"ratioprod".equals(reader.getNodeName()))
			return null;
		rprod = (RatioProduct)ctx.convertAnother(null, RatioProduct.class);
		reader.moveUp();
		
		if (reader.hasMoreChildren())
			return null;

		return new EqualityOfRatioProducts(lprod, rprod, coeff);
	}
	
}