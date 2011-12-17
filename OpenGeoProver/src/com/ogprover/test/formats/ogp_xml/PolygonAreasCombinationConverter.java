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
public class PolygonAreasCombinationConverter implements Converter {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(LinearCombinationOfDoubleSignedPolygonAreas.class);
	}

	@Override
	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		LinearCombinationOfDoubleSignedPolygonAreas statement = (LinearCombinationOfDoubleSignedPolygonAreas)obj;
		Vector<Double> coeffs = statement.getCoefficients();
		
		// number of coefficients and number of polygons must be the same
		int coeffIdx = 0;
		
		for (Vector<Point> polygon : statement.getPolygons()) {
			writer.startNode("polygon");
			writer.addAttribute("coeff", coeffs.get(coeffIdx++) + "");
			for (Point pt : polygon) {
				writer.startNode("vertex");
				writer.addAttribute("label", pt.getGeoObjectLabel());
				writer.endNode();
			}
			writer.endNode();
		}
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		OGPCP consProtocol = OpenGeoProver.settings.getParsedCP();
		Vector<Double> coeffs = new Vector<Double>();
		Vector<Vector<Point>> polygons = new Vector<Vector<Point>>();
		
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			if (!"polygon".equals(reader.getNodeName()))
				return null;
			coeffs.add(Double.parseDouble(reader.getAttribute("coeff")));
			Vector<Point> vertices = new Vector<Point>();
			
			while (reader.hasMoreChildren()) {
				reader.moveDown();
				if (!"vertex".equals(reader.getNodeName()))
					return null;
				vertices.add((Point)consProtocol.getConstructionMap().get(reader.getAttribute("label")));
				reader.moveUp();
			}
			polygons.add(vertices);
			reader.moveUp();
		}

		return new LinearCombinationOfDoubleSignedPolygonAreas(polygons, coeffs);
	}
	
}