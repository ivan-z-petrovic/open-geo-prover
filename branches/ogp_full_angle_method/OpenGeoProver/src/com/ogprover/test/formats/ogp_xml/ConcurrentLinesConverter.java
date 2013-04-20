/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import java.util.ArrayList;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.pp.tp.geoconstruction.GeoConstruction;
import com.ogprover.pp.tp.geoconstruction.Line;
import com.ogprover.pp.tp.thmstatement.ConcurrentLines;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of ConcurrentLines objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class ConcurrentLinesConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(ConcurrentLines.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		ConcurrentLines lines = (ConcurrentLines)obj;
		
		for (GeoConstruction geoCons : lines.getGeoObjects()) {
			Line line = (Line)geoCons;
			writer.startNode("line");
			writer.addAttribute("label", line.getGeoObjectLabel());
			writer.endNode();
		}
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		ArrayList<Line> lines = new ArrayList<Line>();
		
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			if ("line".equals(reader.getNodeName()))
				lines.add((Line) OpenGeoProver.settings.getParsedCP().getConstructionMap().get(reader.getAttribute("label")));
			else
				return null;
			reader.moveUp();
		}

		return new ConcurrentLines(lines);
	}
	
}