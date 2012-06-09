/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.geoconstruction.AngleBisector;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of AngleBisector objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class AngleBisectorConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(AngleBisector.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		AngleBisector angBis = (AngleBisector)obj;
		writer.addAttribute("label", angBis.getGeoObjectLabel());
		writer.addAttribute("firstraypt", angBis.getAngle().getFirstRayPoint().getGeoObjectLabel());
		writer.addAttribute("vertex", angBis.getAngle().getVertex().getGeoObjectLabel());
		writer.addAttribute("secondraypt", angBis.getAngle().getSecondRayPoint().getGeoObjectLabel());
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		OGPTP consProtocol = OpenGeoProver.settings.getParsedCP();
		String label = reader.getAttribute("label");
		String firstraypt = reader.getAttribute("firstraypt");
		String vertex = reader.getAttribute("vertex");
		String secondraypt = reader.getAttribute("secondraypt");
		
		return new AngleBisector(label, 
				                (Point)consProtocol.getConstructionMap().get(firstraypt), 
				                (Point)consProtocol.getConstructionMap().get(vertex),
				                (Point)consProtocol.getConstructionMap().get(secondraypt));
	}
	
}