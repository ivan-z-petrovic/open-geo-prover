/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.prover_protocol.cp.OGPCP;
import com.ogprover.prover_protocol.cp.geoconstruction.ConicSectionWithFivePoints;
import com.ogprover.prover_protocol.cp.geoconstruction.Point;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of ConicSectionWithFivePoints objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class ConicSectionWithFivePointsConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(ConicSectionWithFivePoints.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		ConicSectionWithFivePoints conic = (ConicSectionWithFivePoints)obj;
		writer.addAttribute("label", conic.getGeoObjectLabel());
		writer.addAttribute("pointA", conic.getPoints().get(0).getGeoObjectLabel());
		writer.addAttribute("pointB", conic.getPoints().get(1).getGeoObjectLabel());
		writer.addAttribute("pointC", conic.getPoints().get(2).getGeoObjectLabel());
		writer.addAttribute("pointD", conic.getPoints().get(3).getGeoObjectLabel());
		writer.addAttribute("pointE", conic.getPoints().get(4).getGeoObjectLabel());
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		OGPCP consProtocol = OpenGeoProver.settings.getParsedCP();
		String label = reader.getAttribute("label");
		String pointA = reader.getAttribute("pointA");
		String pointB = reader.getAttribute("pointB");
		String pointC = reader.getAttribute("pointC");
		String pointD = reader.getAttribute("pointD");
		String pointE = reader.getAttribute("pointE");
		
		return new ConicSectionWithFivePoints(label,
											  (Point)consProtocol.getConstructionMap().get(pointA),
											  (Point)consProtocol.getConstructionMap().get(pointB),
											  (Point)consProtocol.getConstructionMap().get(pointC),
											  (Point)consProtocol.getConstructionMap().get(pointD),
											  (Point)consProtocol.getConstructionMap().get(pointE));
	}
	
}