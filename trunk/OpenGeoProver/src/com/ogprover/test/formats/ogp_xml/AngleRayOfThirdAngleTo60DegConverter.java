/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.prover_protocol.cp.OGPCP;
import com.ogprover.prover_protocol.cp.geoconstruction.AngleOf60Deg;
import com.ogprover.prover_protocol.cp.geoconstruction.AngleRayOfThirdAngleTo60Deg;
import com.ogprover.prover_protocol.cp.geoconstruction.Point;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of AngleRayOfThirdAngleTo60Deg objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class AngleRayOfThirdAngleTo60DegConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(AngleRayOfThirdAngleTo60Deg.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		AngleRayOfThirdAngleTo60Deg angRay = (AngleRayOfThirdAngleTo60Deg)obj;
		writer.addAttribute("label", angRay.getGeoObjectLabel());
		writer.addAttribute("firstraypt", angRay.getFirstRayPoint().getGeoObjectLabel());
		writer.addAttribute("vertex", angRay.getPoints().get(0).getGeoObjectLabel());
		writer.addAttribute("firstangfrp", angRay.getFirstAngle().getFirstRayPoint().getGeoObjectLabel());
		writer.addAttribute("firstangv", angRay.getFirstAngle().getVertex().getGeoObjectLabel());
		writer.addAttribute("firstangsrp", angRay.getFirstAngle().getSecondRayPoint().getGeoObjectLabel());
		writer.addAttribute("secondangfrp", angRay.getSecondAngle().getFirstRayPoint().getGeoObjectLabel());
		writer.addAttribute("secondangv", angRay.getSecondAngle().getVertex().getGeoObjectLabel());
		writer.addAttribute("secondangsrp", angRay.getSecondAngle().getSecondRayPoint().getGeoObjectLabel());
		writer.addAttribute("ang60deg", angRay.getAngle60Deg().getGeoObjectLabel());
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		OGPCP consProtocol = OpenGeoProver.settings.getParsedCP();
		String label = reader.getAttribute("label");
		String firstraypt = reader.getAttribute("firstraypt");
		String vertex = reader.getAttribute("vertex");
		String firstangfrp = reader.getAttribute("firstangfrp");
		String firstangv = reader.getAttribute("firstangv");
		String firstangsrp = reader.getAttribute("firstangsrp");
		String secondangfrp = reader.getAttribute("secondangfrp");
		String secondangv = reader.getAttribute("secondangv");
		String secondangsrp = reader.getAttribute("secondangsrp");
		String ang60deg = reader.getAttribute("ang60deg");
		
		return new AngleRayOfThirdAngleTo60Deg(label, 
				            				  (Point)consProtocol.getConstructionMap().get(firstraypt), 
				            				  (Point)consProtocol.getConstructionMap().get(vertex),
				            				  (Point)consProtocol.getConstructionMap().get(firstangfrp), 
				            				  (Point)consProtocol.getConstructionMap().get(firstangv),
				            				  (Point)consProtocol.getConstructionMap().get(firstangsrp),
				            				  (Point)consProtocol.getConstructionMap().get(secondangfrp), 
				            				  (Point)consProtocol.getConstructionMap().get(secondangv),
				            				  (Point)consProtocol.getConstructionMap().get(secondangsrp),
				            				  (AngleOf60Deg)consProtocol.getConstructionMap().get(ang60deg));
	}
	
}