/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.prover_protocol.cp.OGPCP;
import com.ogprover.prover_protocol.cp.geoconstruction.Point;
import com.ogprover.prover_protocol.cp.geoconstruction.SetOfPoints;
import com.ogprover.prover_protocol.cp.thmstatement.PointOnSetOfPoints;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of PointOnSetOfPoints objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class PointOnSetOfPointsConverter implements Converter {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(PointOnSetOfPoints.class);
	}

	@Override
	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		PointOnSetOfPoints statement = (PointOnSetOfPoints)obj;
		
		writer.addAttribute("set", statement.getGeoObjects().get(0).getGeoObjectLabel());
		writer.addAttribute("point", statement.getGeoObjects().get(1).getGeoObjectLabel());
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		OGPCP consProtocol = OpenGeoProver.settings.getParsedCP();
		String set = reader.getAttribute("set");
		String point = reader.getAttribute("point");
		
		return new PointOnSetOfPoints((SetOfPoints)consProtocol.getConstructionMap().get(set),
				                      (Point)consProtocol.getConstructionMap().get(point));
	}
	
}