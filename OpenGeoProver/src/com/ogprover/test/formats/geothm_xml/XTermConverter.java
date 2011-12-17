/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.geothm_xml;

import com.ogprover.polynomials.Power;
import com.ogprover.polynomials.UFraction;
import com.ogprover.polynomials.XTerm;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of XTerm objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class XTermConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(XTerm.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		XTerm xt = (XTerm)obj;
		writer.startNode("ufrac");
		ctx.convertAnother(xt.getUCoeff());
		writer.endNode();
		
		for (int ii = 0, jj = xt.getPowers().size(); ii < jj; ii++) {
			writer.startNode("pow");
			ctx.convertAnother(xt.getPowers().get(ii));
			writer.endNode();
		}
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		UFraction uf = null;
		reader.moveDown();
		uf = (UFraction)ctx.convertAnother(null, UFraction.class);
		reader.moveUp();
		XTerm xt = new XTerm(uf);
		
		while (reader.hasMoreChildren()) {
			Power currPow = null;
			reader.moveDown();
			
			if ("pow".equals(reader.getNodeName())) {
				currPow = (Power)ctx.convertAnother(null, Power.class);
				if (currPow != null)
					xt.addPower(currPow);
			}
			reader.moveUp();
		}
		
		return xt;
	}
	
}