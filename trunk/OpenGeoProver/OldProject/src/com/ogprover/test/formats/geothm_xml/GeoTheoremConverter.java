/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.geothm_xml;

import com.ogprover.polynomials.GeoTheorem;
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
* <dd>Class for XML converter of GeoTheorem objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class GeoTheoremConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(GeoTheorem.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		GeoTheorem theorem = (GeoTheorem)obj;
		
		if (theorem.getName() != null)
			writer.addAttribute("name", theorem.getName());
		else
			writer.addAttribute("name", "");
		writer.startNode("xpolysys");
		ctx.convertAnother(theorem.getHypotheses());
		writer.endNode();
		writer.startNode("xpoly");
		ctx.convertAnother(theorem.getStatement());
		writer.endNode();
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		XPolySystem xs = null;
		XPolynomial xp = null;
		String name;
		
		name = reader.getAttribute("name");
		reader.moveDown();
		if ("xpolysys".equals(reader.getNodeName()))
			xs = (XPolySystem)ctx.convertAnother(null, XPolySystem.class);
		reader.moveUp();
		reader.moveDown();
		if ("xpoly".equals(reader.getNodeName()))
			xp = (XPolynomial)ctx.convertAnother(null, XPolynomial.class);
		reader.moveUp();
		
		return new GeoTheorem(name, xs, xp);
	}
	
}