/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.ogprover.pp.tp.geoconstruction.RotatedPoint;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of RotatedPoint objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class RotatedPointConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(RotatedPoint.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		RotatedPoint point = (RotatedPoint)obj;
		writer.addAttribute("label", point.getGeoObjectLabel());
		writer.addAttribute("origpt", point.getOriginalPoint().getGeoObjectLabel());
		writer.addAttribute("center", point.getCenterOfRotation().getGeoObjectLabel());
		writer.addAttribute("angmeasure", point.getDegAngleMeasure() + "");
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		OGPTP consProtocol = OpenGeoProver.settings.getParsedCP();
		String label = reader.getAttribute("label");
		String origpt = reader.getAttribute("origpt");
		String center = reader.getAttribute("center");
		double angMeasure = Double.parseDouble(reader.getAttribute("angmeasure"));
		
		return new RotatedPoint(label, 
				                (Point)consProtocol.getConstructionMap().get(origpt), 
				                (Point)consProtocol.getConstructionMap().get(center),
				                angMeasure);
	}
	
}