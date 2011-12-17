/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.geothm_xml;

import com.ogprover.polynomials.Power;
import com.ogprover.polynomials.Variable;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of Power objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class PowerConverter implements Converter {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(Power.class);
	}

	@Override
	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		Power power = (Power)obj;
		writer.addAttribute("vtype", ((power.getVarType() == Variable.VAR_TYPE_UX_U) ? "u" : "x"));
		writer.addAttribute("ind", Long.toString(power.getIndex()));
		writer.addAttribute("exp", Integer.toString(power.getExponent()));
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		short vtype = 0;
		int ind = 0, exp = 0;
		
		vtype = ("u".equals(reader.getAttribute("vtype")) ? Variable.VAR_TYPE_UX_U : Variable.VAR_TYPE_UX_X);
		ind = Integer.valueOf(reader.getAttribute("ind")).intValue();
		exp = Integer.valueOf(reader.getAttribute("exp")).intValue();
		
		return new Power(vtype, ind, exp);
	}
	
}