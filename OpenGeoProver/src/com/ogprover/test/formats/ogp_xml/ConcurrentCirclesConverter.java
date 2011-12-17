/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import java.util.ArrayList;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.prover_protocol.cp.geoconstruction.Circle;
import com.ogprover.prover_protocol.cp.geoconstruction.GeoConstruction;
import com.ogprover.prover_protocol.cp.thmstatement.ConcurrentCircles;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of ConcurrentCircles objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class ConcurrentCirclesConverter implements Converter {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(ConcurrentCircles.class);
	}

	@Override
	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		ConcurrentCircles circles = (ConcurrentCircles)obj;
		
		for (GeoConstruction geoCons : circles.getGeoObjects()) {
			Circle circle = (Circle)geoCons;
			writer.startNode("circle");
			writer.addAttribute("label", circle.getGeoObjectLabel());
			writer.endNode();
		}
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		ArrayList<Circle> circles = new ArrayList<Circle>();
		
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			if ("circle".equals(reader.getNodeName()))
				circles.add((Circle) OpenGeoProver.settings.getParsedCP().getConstructionMap().get(reader.getAttribute("label")));
			else
				return null;
			reader.moveUp();
		}

		return new ConcurrentCircles(circles);
	}
	
}