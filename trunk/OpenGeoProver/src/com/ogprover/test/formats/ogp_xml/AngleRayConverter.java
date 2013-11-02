/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.geoconstruction.AngleRay;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of AngleRay objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class AngleRayConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(AngleRay.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		AngleRay angRay = (AngleRay)obj;
		writer.addAttribute("label", angRay.getGeoObjectLabel());
		writer.addAttribute("firstraypt", angRay.getFirstRayPoint().getGeoObjectLabel());
		writer.addAttribute("vertex", angRay.getPoints().get(0).getGeoObjectLabel());
		writer.addAttribute("congangfrp", angRay.getCongAngle().getFirstRayPoint().getGeoObjectLabel());
		writer.addAttribute("congangv", angRay.getCongAngle().getVertex().getGeoObjectLabel());
		writer.addAttribute("congangsrp", angRay.getCongAngle().getSecondRayPoint().getGeoObjectLabel());
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		OGPTP consProtocol = OpenGeoProver.settings.getParsedTP();
		String label = reader.getAttribute("label");
		String firstraypt = reader.getAttribute("firstraypt");
		String vertex = reader.getAttribute("vertex");
		String congangfrp = reader.getAttribute("congangfrp");
		String congangv = reader.getAttribute("congangv");
		String congangsrp = reader.getAttribute("congangsrp");
		
		return new AngleRay(label, 
				            (Point)consProtocol.getConstructionMap().get(firstraypt), 
				            (Point)consProtocol.getConstructionMap().get(vertex),
				            (Point)consProtocol.getConstructionMap().get(congangfrp), 
				            (Point)consProtocol.getConstructionMap().get(congangv),
				            (Point)consProtocol.getConstructionMap().get(congangsrp));
	}
	
}