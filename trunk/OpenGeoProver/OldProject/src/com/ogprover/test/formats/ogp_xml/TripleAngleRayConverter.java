/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.ogprover.pp.tp.geoconstruction.TripleAngleRay;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of TripleAngleRay objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class TripleAngleRayConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(TripleAngleRay.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		TripleAngleRay angRay = (TripleAngleRay)obj;
		writer.addAttribute("label", angRay.getGeoObjectLabel());
		writer.addAttribute("firstraypt", angRay.getFirstRayPoint().getGeoObjectLabel());
		writer.addAttribute("vertex", angRay.getPoints().get(0).getGeoObjectLabel());
		writer.addAttribute("smallangfrp", angRay.getSmallAngle().getFirstRayPoint().getGeoObjectLabel());
		writer.addAttribute("smallangv", angRay.getSmallAngle().getVertex().getGeoObjectLabel());
		writer.addAttribute("smallangsrp", angRay.getSmallAngle().getSecondRayPoint().getGeoObjectLabel());
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		OGPTP consProtocol = OpenGeoProver.settings.getParsedTP();
		String label = reader.getAttribute("label");
		String firstraypt = reader.getAttribute("firstraypt");
		String vertex = reader.getAttribute("vertex");
		String smallangfrp = reader.getAttribute("smallangfrp");
		String smallangv = reader.getAttribute("smallangv");
		String smallangsrp = reader.getAttribute("smallangsrp");
		
		return new TripleAngleRay(label, 
				            	 (Point)consProtocol.getConstructionMap().get(firstraypt), 
				            	 (Point)consProtocol.getConstructionMap().get(vertex),
				            	 (Point)consProtocol.getConstructionMap().get(smallangfrp), 
				            	 (Point)consProtocol.getConstructionMap().get(smallangv),
				            	 (Point)consProtocol.getConstructionMap().get(smallangsrp));
	}
	
}