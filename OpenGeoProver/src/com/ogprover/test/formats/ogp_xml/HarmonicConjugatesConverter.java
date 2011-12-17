/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.prover_protocol.cp.OGPCP;
import com.ogprover.prover_protocol.cp.geoconstruction.Point;
import com.ogprover.prover_protocol.cp.thmstatement.FourHarmonicConjugatePoints;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of FourHarmonicConjugatePoints objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class HarmonicConjugatesConverter implements Converter {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(FourHarmonicConjugatePoints.class);
	}

	@Override
	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		FourHarmonicConjugatePoints statement = (FourHarmonicConjugatePoints)obj;
		Point A = (Point) statement.getGeoObjects().get(0);
		Point B = (Point) statement.getGeoObjects().get(1);
		Point C = (Point) statement.getGeoObjects().get(2);
		Point D = (Point) statement.getGeoObjects().get(3);
		
		writer.addAttribute("point1", A.getGeoObjectLabel());
		writer.addAttribute("point2", B.getGeoObjectLabel());
		writer.addAttribute("point3", C.getGeoObjectLabel());
		writer.addAttribute("point4", D.getGeoObjectLabel());
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		OGPCP consProtocol = OpenGeoProver.settings.getParsedCP();
		String point1 = reader.getAttribute("point1");
		String point2 = reader.getAttribute("point2");
		String point3 = reader.getAttribute("point3");
		String point4 = reader.getAttribute("point4");
		
		return new FourHarmonicConjugatePoints((Point)consProtocol.getConstructionMap().get(point1),
				                               (Point)consProtocol.getConstructionMap().get(point2),
				                               (Point)consProtocol.getConstructionMap().get(point3),
				                               (Point)consProtocol.getConstructionMap().get(point4));
	}
	
}