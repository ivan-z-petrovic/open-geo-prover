/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.geoconstruction.GeoConstruction;
import com.ogprover.pp.tp.geoconstruction.IntersectionPoint;
import com.ogprover.pp.tp.geoconstruction.SetOfPoints;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of IntersectionPoint objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class IntersectionPointConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(IntersectionPoint.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		IntersectionPoint point = (IntersectionPoint)obj;
		writer.addAttribute("label", point.getGeoObjectLabel());
		writer.addAttribute("set1", ((GeoConstruction)point.getFirstPointSet()).getGeoObjectLabel());
		writer.addAttribute("set2", ((GeoConstruction)point.getSecondPointSet()).getGeoObjectLabel());
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		OGPTP consProtocol = OpenGeoProver.settings.getParsedTP();
		String label = reader.getAttribute("label");
		String set1 = reader.getAttribute("set1");
		String set2 = reader.getAttribute("set2");
		
		return new IntersectionPoint(label, 
				                     (SetOfPoints)consProtocol.getConstructionMap().get(set1), 
				                     (SetOfPoints)consProtocol.getConstructionMap().get(set2));
	}
	
}