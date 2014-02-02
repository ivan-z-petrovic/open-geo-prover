/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import java.util.Vector;

import com.ogprover.pp.tp.geoobject.Segment;
import com.ogprover.pp.tp.thmstatement.LinearCombinationOfOrientedSegments;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of LinearCombinationOfOrientedSegments objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class OrientedSegmentsCombinationConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(LinearCombinationOfOrientedSegments.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		LinearCombinationOfOrientedSegments statement = (LinearCombinationOfOrientedSegments)obj;
		
		for (Segment segment : statement.getSegments()) {
			writer.startNode("segment");
			ctx.convertAnother(segment);
			writer.endNode();
		}
		
		for (Double coeff : statement.getCoefficients()) {
			writer.startNode("coeff");
			writer.addAttribute("value", coeff + "");
			writer.endNode();
		}
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		Vector<Segment> segments = new Vector<Segment>();
		Vector<Double> coeffs = new Vector<Double>();
		
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			if ("segment".equals(reader.getNodeName()))
				segments.add((Segment)ctx.convertAnother(null, Segment.class));
			else if ("coeff".equals(reader.getNodeName()))
				coeffs.add(Double.parseDouble(reader.getAttribute("value")));
			else
				return null;
			reader.moveUp();
		}

		return new LinearCombinationOfOrientedSegments(segments, coeffs);
	}
	
}