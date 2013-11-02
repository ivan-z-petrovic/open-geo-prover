/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.geoconstruction.ConicSection;
import com.ogprover.pp.tp.geoconstruction.GeoConstruction;
import com.ogprover.pp.tp.geoconstruction.RandomPointFromGeneralConic;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of RandomPointFromGeneralConic objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class RandomPointFromGeneralConicConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(RandomPointFromGeneralConic.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		RandomPointFromGeneralConic point = (RandomPointFromGeneralConic)obj;
		writer.addAttribute("label", point.getGeoObjectLabel());
		writer.addAttribute("conic", ((GeoConstruction)point.getBaseSetOfPoints()).getGeoObjectLabel());
	}
	
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		OGPTP consProtocol = OpenGeoProver.settings.getParsedTP();
		String label = reader.getAttribute("label");
		String conic = reader.getAttribute("conic");
		
		return new RandomPointFromGeneralConic(label, (ConicSection)consProtocol.getConstructionMap().get(conic));
	}
	
}