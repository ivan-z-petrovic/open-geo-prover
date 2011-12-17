/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.prover_protocol.cp.OGPCP;
import com.ogprover.prover_protocol.cp.geoconstruction.GeoConstruction;
import com.ogprover.prover_protocol.cp.geoconstruction.Point;
import com.ogprover.prover_protocol.cp.geoconstruction.SetOfPoints;
import com.ogprover.prover_protocol.cp.geoconstruction.TangentLine;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of TangentLine objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class TangentLineConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(TangentLine.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		TangentLine line = (TangentLine)obj;
		writer.addAttribute("label", line.getGeoObjectLabel());
		writer.addAttribute("basept", line.getPoints().get(0).getGeoObjectLabel());
		writer.addAttribute("pointset", ((GeoConstruction)line.getUnderlyingPointsSet()).getGeoObjectLabel());
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		OGPCP consProtocol = OpenGeoProver.settings.getParsedCP();
		String label = reader.getAttribute("label");
		String basept = reader.getAttribute("basept");
		String pointset = reader.getAttribute("pointset");
		
		return new TangentLine(label, 
				               (Point)consProtocol.getConstructionMap().get(basept), 
				               (SetOfPoints)consProtocol.getConstructionMap().get(pointset));
	}
	
}