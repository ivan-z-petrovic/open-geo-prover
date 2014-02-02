/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.pp.tp.geoobject.RCConsPointList;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of list of points</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class RCConsPointListConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(RCConsPointList.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		RCConsPointList ptList = (RCConsPointList)obj;
		
		for (Point pt : ptList.getPoints()) {
			writer.startNode("point");
			String ptLabel = pt.getGeoObjectLabel();
			writer.addAttribute("label", ptLabel);
			Double xVal = ptList.getXCoordinateValue(ptLabel);
			if (xVal != null)
				writer.addAttribute("x", xVal.toString());
			Double yVal = ptList.getYCoordinateValue(ptLabel);
			if (yVal != null)
				writer.addAttribute("y", yVal.toString());
			writer.endNode();
		}
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		RCConsPointList ptList = new RCConsPointList();
		
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			if ("point".equals(reader.getNodeName())) {
				String ptLabel = reader.getAttribute("label");
				Point pt = (Point)OpenGeoProver.settings.getParsedTP().getConstructionMap().get(ptLabel);
				if (pt == null)
					return null;
				ptList.addPoint(pt);
				String xValStr = reader.getAttribute("x");
				String yValStr = reader.getAttribute("y");
				try {
					if (xValStr != null)
						ptList.addXCoordinateValue(ptLabel, Double.parseDouble(xValStr));
					if (yValStr != null)
						ptList.addYCoordinateValue(ptLabel, Double.parseDouble(yValStr));
				} catch (NumberFormatException e) {
					return null;
				}
			}
			else
				return null;
			reader.moveUp();
		}

		return ptList;
	}
}
