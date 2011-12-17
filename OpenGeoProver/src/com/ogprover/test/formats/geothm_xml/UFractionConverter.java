/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.geothm_xml;

import com.ogprover.polynomials.UFraction;
import com.ogprover.polynomials.UPolynomial;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of UFraction objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class UFractionConverter implements Converter {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(UFraction.class);
	}

	@Override
	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		UFraction uf = (UFraction)obj;
		
		writer.startNode("upoly");
		ctx.convertAnother(uf.getNumerator());
		writer.endNode();
		writer.startNode("upoly");
		ctx.convertAnother(uf.getDenominator());
		writer.endNode();
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		UFraction uf = new UFraction(null, null);
		
		reader.moveDown();
		uf.setNumerator((UPolynomial)ctx.convertAnother(null, UPolynomial.class));
		reader.moveUp();
		if (reader.hasMoreChildren()) {
			reader.moveDown();
			uf.setDenominator((UPolynomial)ctx.convertAnother(null, UPolynomial.class));
			reader.moveUp();
		}
		else {
			uf.setDenominator(new UPolynomial(1));
		}
		
		return uf;
	}
	
}