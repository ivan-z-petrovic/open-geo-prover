/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.ogprover.pp.tp.geoconstruction.SetOfPoints;
import com.ogprover.pp.tp.thmstatement.PointOnSetOfPoints;
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
	public boolean canConvert(Class clazz) {
		return clazz.equals(PointOnSetOfPoints.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		PointOnSetOfPoints statement = (PointOnSetOfPoints)obj;
		
		writer.addAttribute("set", statement.getGeoObjects().get(0).getGeoObjectLabel());
		writer.addAttribute("point", statement.getGeoObjects().get(1).getGeoObjectLabel());
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		OGPTP consProtocol = OpenGeoProver.settings.getParsedCP();
		String set = reader.getAttribute("set");
		String point = reader.getAttribute("point");
		
		return new PointOnSetOfPoints((SetOfPoints)consProtocol.getConstructionMap().get(set),
				                      (Point)consProtocol.getConstructionMap().get(point));
	}
	
}