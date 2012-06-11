/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import java.util.Vector;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.prover_protocol.cp.OGPCP;
import com.ogprover.prover_protocol.cp.geoconstruction.Point;
import com.ogprover.prover_protocol.cp.thmstatement.LinearCombinationOfDoubleSignedPolygonAreas;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of LinearCombinationOfDoubleSignedPolygonAreas objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class LinearCombinationOfDoubleSignedPolygonAreasConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(LinearCombinationOfDoubleSignedPolygonAreas.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		LinearCombinationOfDoubleSignedPolygonAreas statement = (LinearCombinationOfDoubleSignedPolygonAreas)obj;
		
		int ii = 0;
		Vector<Double> coefficients = statement.getCoefficients();
		
		for (Vector<Point> polygon : statement.getPolygons()) {
			writer.startNode("polygon");
			writer.addAttribute("coeff", coefficients.get(ii).toString());
			for (Point vertex : polygon) {
				writer.startNode("vertex");
				writer.addAttribute("label", vertex.getGeoObjectLabel());
				writer.endNode();
			}
			writer.endNode();
			ii++;
		}
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		OGPCP consProtocol = OpenGeoProver.settings.getParsedCP();
		Vector<Vector<Point>> polygons = new Vector<Vector<Point>>();
		Vector<Point> polygon = null;
		Vector<Double> coefficients = new Vector<Double>();
		Double coeff = null;
		
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			
			if (!"polygon".equals(reader.getNodeName()))
				return null;
			
			coeff = new Double(reader.getAttribute("coeff"));
			coefficients.add(coeff);
			
			polygon = new Vector<Point>();
			
			while (reader.hasMoreChildren()) {
				reader.moveDown();
				
				if (!"vertex".equals(reader.getNodeName()))
					return null;
				
				polygon.add((Point) consProtocol.getConstructionMap().get(reader.getAttribute("label")));
				
				reader.moveUp();
			}
			
			polygons.add(polygon);
			
			reader.moveUp();
		}
		
		return new LinearCombinationOfDoubleSignedPolygonAreas(polygons, coefficients);
	}
	
}