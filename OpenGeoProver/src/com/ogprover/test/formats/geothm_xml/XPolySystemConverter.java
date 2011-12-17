/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.geothm_xml;

import com.ogprover.polynomials.XPolySystem;
import com.ogprover.polynomials.XPolynomial;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of XPolySystem objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class XPolySystemConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(XPolySystem.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		XPolySystem xs = (XPolySystem)obj;
		
		for (int ii = 0, jj = xs.getPolynomials().size(); ii < jj; ii++) {
			writer.startNode("xpoly");
			ctx.convertAnother(xs.getXPoly(ii));
			writer.endNode();
		}
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		XPolySystem xs = new XPolySystem();
		
		while (reader.hasMoreChildren()) {
			XPolynomial xp = null;
			reader.moveDown();
			
			if ("xpoly".equals(reader.getNodeName())) {
				xp = (XPolynomial)ctx.convertAnother(null, XPolynomial.class);
				if (xp != null)
					xs.addXPoly(xp);
			}
			reader.moveUp();
		}
		
		return xs;
	}
	
}