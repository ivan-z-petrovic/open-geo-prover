/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.prover_protocol.cp.OGPCP;
import com.ogprover.prover_protocol.cp.geoconstruction.Point;
import com.ogprover.prover_protocol.cp.thmstatement.CongruentTriangles;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of CongruentTriangles objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class CongruentTrianglesConverter implements Converter {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(CongruentTriangles.class);
	}

	@Override
	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		CongruentTriangles statement = (CongruentTriangles)obj;
		
		writer.startNode("point");
		writer.addAttribute("label", statement.getFirstTrianglePointA().getGeoObjectLabel());
		writer.endNode();
		writer.startNode("point");
		writer.addAttribute("label", statement.getFirstTrianglePointB().getGeoObjectLabel());
		writer.endNode();
		writer.startNode("point");
		writer.addAttribute("label", statement.getFirstTrianglePointC().getGeoObjectLabel());
		writer.endNode();
		writer.startNode("point");
		writer.addAttribute("label", statement.getSecondTrianglePointA().getGeoObjectLabel());
		writer.endNode();
		writer.startNode("point");
		writer.addAttribute("label", statement.getSecondTrianglePointB().getGeoObjectLabel());
		writer.endNode();
		writer.startNode("point");
		writer.addAttribute("label", statement.getSecondTrianglePointC().getGeoObjectLabel());
		writer.endNode();
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		OGPCP consProtocol = OpenGeoProver.settings.getParsedCP();
		String firstTA = null, firstTB = null, firstTC = null;
		String secondTA = null, secondTB = null, secondTC = null;
		
		if (!reader.hasMoreChildren())
			return null;
		reader.moveDown();
		if (!"point".equals(reader.getNodeName()))
			return null;
		firstTA = reader.getAttribute("label");
		reader.moveUp();
		
		if (!reader.hasMoreChildren())
			return null;
		reader.moveDown();
		if (!"point".equals(reader.getNodeName()))
			return null;
		firstTB = reader.getAttribute("label");
		reader.moveUp();
		
		if (!reader.hasMoreChildren())
			return null;
		reader.moveDown();
		if (!"point".equals(reader.getNodeName()))
			return null;
		firstTC = reader.getAttribute("label");
		reader.moveUp();
		
		if (!reader.hasMoreChildren())
			return null;
		reader.moveDown();
		if (!"point".equals(reader.getNodeName()))
			return null;
		secondTA = reader.getAttribute("label");
		reader.moveUp();
		
		if (!reader.hasMoreChildren())
			return null;
		reader.moveDown();
		if (!"point".equals(reader.getNodeName()))
			return null;
		secondTB = reader.getAttribute("label");
		reader.moveUp();
		
		if (!reader.hasMoreChildren())
			return null;
		reader.moveDown();
		if (!"point".equals(reader.getNodeName()))
			return null;
		secondTC = reader.getAttribute("label");
		reader.moveUp();
		
		if (reader.hasMoreChildren())
			return null;

		return new CongruentTriangles((Point)consProtocol.getConstructionMap().get(firstTA),
				                      (Point)consProtocol.getConstructionMap().get(firstTB),
				                      (Point)consProtocol.getConstructionMap().get(firstTC),
				                      (Point)consProtocol.getConstructionMap().get(secondTA),
				                      (Point)consProtocol.getConstructionMap().get(secondTB),
				                      (Point)consProtocol.getConstructionMap().get(secondTC));
	}
	
}