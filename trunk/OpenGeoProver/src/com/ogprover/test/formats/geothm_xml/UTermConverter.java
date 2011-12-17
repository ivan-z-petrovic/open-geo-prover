/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.geothm_xml;

import com.ogprover.polynomials.Power;
import com.ogprover.polynomials.UTerm;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of UTerm objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class UTermConverter implements Converter {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(UTerm.class);
	}

	@Override
	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		UTerm ut = (UTerm)obj;
		writer.addAttribute("coeff", Double.toString(ut.getCoeff()));
		for (int ii = 0, jj = ut.getPowers().size(); ii < jj; ii++) {
			writer.startNode("pow");
			ctx.convertAnother(ut.getPowers().get(ii));
			writer.endNode();
		}
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		double coeff = 0.0;
		UTerm ut = null;
		
		coeff = Double.valueOf(reader.getAttribute("coeff")).doubleValue();
		ut = new UTerm(coeff);
		
		while (reader.hasMoreChildren()) {
			Power currPow = null;
			reader.moveDown();
			
			if ("pow".equals(reader.getNodeName())) {
				currPow = (Power)ctx.convertAnother(null, Power.class);
				if (currPow != null)
					ut.addPower(currPow);
			}
			reader.moveUp();
		}
		
		return ut;
	}
	
}