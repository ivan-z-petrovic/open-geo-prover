/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import java.util.ArrayList;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.prover_protocol.cp.geoconstruction.GeoConstruction;
import com.ogprover.prover_protocol.cp.geoconstruction.Point;
import com.ogprover.prover_protocol.cp.thmstatement.CollinearPoints;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of CollinearPoints objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class CollinearPointsConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(CollinearPoints.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		CollinearPoints points = (CollinearPoints)obj;
		
		for (GeoConstruction geoCons : points.getGeoObjects()) {
			Point point = (Point)geoCons;
			writer.startNode("point");
			writer.addAttribute("label", point.getGeoObjectLabel());
			writer.endNode();
		}
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		ArrayList<Point> points = new ArrayList<Point>();
		
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			if ("point".equals(reader.getNodeName()))
				points.add((Point) OpenGeoProver.settings.getParsedCP().getConstructionMap().get(reader.getAttribute("label")));
			else
				return null;
			reader.moveUp();
		}

		return new CollinearPoints(points);
	}
	
}