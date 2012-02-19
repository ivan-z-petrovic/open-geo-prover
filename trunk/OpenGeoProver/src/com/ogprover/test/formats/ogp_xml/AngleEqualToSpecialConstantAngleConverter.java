/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.prover_protocol.cp.OGPCP;
import com.ogprover.prover_protocol.cp.auxiliary.Angle;
import com.ogprover.prover_protocol.cp.geoconstruction.GeoConstruction;
import com.ogprover.prover_protocol.cp.geoconstruction.SpecialConstantAngle;
import com.ogprover.prover_protocol.cp.thmstatement.AngleEqualToSpecialConstantAngle;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of AngleEqualToSpecialConstantAngle objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class AngleEqualToSpecialConstantAngleConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(AngleEqualToSpecialConstantAngle.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		AngleEqualToSpecialConstantAngle statement = (AngleEqualToSpecialConstantAngle)obj;
		Angle varAng = statement.getVarAngle();
		SpecialConstantAngle consAng = statement.getConsAngle();
		
		writer.addAttribute("consAngle", ((GeoConstruction)consAng).getGeoObjectLabel());
		writer.startNode("angle");
		ctx.convertAnother(varAng);
		writer.endNode();
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		OGPCP consProtocol = OpenGeoProver.settings.getParsedCP();
		String consAngLabel = reader.getAttribute("consAngle");
		
		if (!reader.hasMoreChildren())
			return null;
		
		reader.moveDown();
		if (!"angle".equals(reader.getNodeName()))
			return null;
		Angle varAng = (Angle)ctx.convertAnother(null, Angle.class);
		reader.moveUp();
		
		if (reader.hasMoreChildren())
			return null;

		return new AngleEqualToSpecialConstantAngle(varAng, (SpecialConstantAngle)consProtocol.getConstructionMap().get(consAngLabel));
	}
	
}