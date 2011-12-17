/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.geothm_xml;

import java.util.Collection;
import java.util.Iterator;

import com.ogprover.polynomials.Term;
import com.ogprover.polynomials.UPolynomial;
import com.ogprover.polynomials.UTerm;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of UPolynomial objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class UPolynomialConverter implements Converter {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(UPolynomial.class);
	}

	@Override
	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		UPolynomial up = (UPolynomial)obj;
		Collection<Term> col = up.getTerms().values();
		Iterator<Term> it = col.iterator();
		
	    while (it.hasNext()) {
	    	writer.startNode("uterm");
	    	ctx.convertAnother((UTerm)it.next());
	    	writer.endNode();
	    }
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		UPolynomial up = new UPolynomial();
		UTerm ut = null;
		
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			if ("uterm".equals(reader.getNodeName()))
				ut = (UTerm)ctx.convertAnother(null, UTerm.class);
			if (ut != null)
				up.addTerm(ut);
			reader.moveUp();
		}
		
		return up;
	}
	
}