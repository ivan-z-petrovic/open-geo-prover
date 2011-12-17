/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.geothm_xml;

import java.util.Collection;
import java.util.Iterator;

import com.ogprover.polynomials.Term;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.polynomials.XTerm;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of XPolynomial objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class XPolynomialConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(XPolynomial.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		XPolynomial xp = (XPolynomial)obj;
		Collection<Term> col = xp.getTerms().values();
		Iterator<Term> it = col.iterator();
		
	    while (it.hasNext()) {
	    	writer.startNode("xterm");
	    	ctx.convertAnother((XTerm)it.next());
	    	writer.endNode();
	    }
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		XPolynomial xp = new XPolynomial();
		XTerm xt = null;
		
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			if ("xterm".equals(reader.getNodeName()))
				xt = (XTerm)ctx.convertAnother(null, XTerm.class);
			if (xt != null)
				xp.addTerm(xt);
			reader.moveUp();
		}
		
		return xp;
	}
	
}