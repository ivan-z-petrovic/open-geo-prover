/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.geoconstruction.*;
import com.ogprover.pp.tp.thmstatement.*;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for XML converter of OGPCP objects</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class OGPCPXMLConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return clazz.equals(OGPTP.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		OGPTP consProtocol = (OGPTP)obj;
		
		if (consProtocol == null)
			return;
		
		String thmName = consProtocol.getTheoremName();
		if (thmName != null)
			writer.addAttribute("name", thmName);
		
		writer.startNode("constructions");
		for (GeoConstruction geoCons : consProtocol.getConstructionSteps()) {
			if (geoCons instanceof AngleBisector)
				writer.startNode("langbis");
			else if (geoCons instanceof AngleOf60Deg)
				writer.startNode("ang60deg");
			else if (geoCons instanceof AngleRay)
				writer.startNode("langray");
			else if (geoCons instanceof AngleRayOfThirdAngleTo60Deg)
				writer.startNode("langrayto60deg");
			else if (geoCons instanceof AngleTrisector)
				writer.startNode("langtris");
			else if (geoCons instanceof CenterOfCircle)
				writer.startNode("pcirclecent");
			else if (geoCons instanceof CentralSymmetricPoint)
				writer.startNode("pcentralsymm");
			else if (geoCons instanceof CircleWithCenterAndPoint)
				writer.startNode("ccenterpt");
			else if (geoCons instanceof CircleWithCenterAndRadius)
				writer.startNode("ccenterradi");
			else if (geoCons instanceof CircleWithDiameter)
				writer.startNode("cdiam");
			else if (geoCons instanceof CircumscribedCircle)
				writer.startNode("ccircumscribed");
			else if (geoCons instanceof ConicSectionWithFivePoints)
				writer.startNode("psconic5");
			else if (geoCons instanceof FootPoint)
				writer.startNode("pfoot");
			else if (geoCons instanceof FreePoint)
				writer.startNode("pfree");
			else if (geoCons instanceof GeneralConicSection)
				writer.startNode("psgenconic");
			else if (geoCons instanceof GeneralizedSegmentDivisionPoint)
				writer.startNode("pgensegdiv");
			else if (geoCons instanceof HarmonicConjugatePoint)
				writer.startNode("pharmonicconj");
			else if (geoCons instanceof IntersectionPoint)
				writer.startNode("pintersect");
			else if (geoCons instanceof InverseOfPoint)
				writer.startNode("pinverse");
			else if (geoCons instanceof LineThroughTwoPoints)
				writer.startNode("ltwopts");
			else if (geoCons instanceof MidPoint)
				writer.startNode("pmid");
			else if (geoCons instanceof ParallelLine)
				writer.startNode("lparallel");
			else if (geoCons instanceof PerpendicularBisector)
				writer.startNode("lperpbis");
			else if (geoCons instanceof PerpendicularLine)
				writer.startNode("lperp");
			else if (geoCons instanceof Polar)
				writer.startNode("lpolar");
			else if (geoCons instanceof Pole)
				writer.startNode("ppole");
			else if (geoCons instanceof RadicalAxis)
				writer.startNode("lradical");
			else if (geoCons instanceof RandomPointFromCircle)
				writer.startNode("prandcircle");
			else if (geoCons instanceof RandomPointFromGeneralConic)
				writer.startNode("prandgenconic");
			else if (geoCons instanceof RandomPointFromLine)
				writer.startNode("prandline");
			else if (geoCons instanceof ReflectedPoint)
				writer.startNode("preflexive");
			else if (geoCons instanceof RotatedPoint)
				writer.startNode("protated");
			else if (geoCons instanceof SegmentDivisionPoint)
				writer.startNode("psegdiv");
			else if (geoCons instanceof TangentLine)
				writer.startNode("ltangent");
			else if (geoCons instanceof TranslatedPoint)
				writer.startNode("ptranslated");
			else if (geoCons instanceof TripleAngleRay)
				writer.startNode("ltangray");
			ctx.convertAnother(geoCons);
			writer.endNode();
		}
		writer.endNode();
		writer.startNode("statement");
		ThmStatement statement = consProtocol.getTheoremStatement();
		if (statement instanceof AlgebraicSumOfThreeAngles)
			writer.startNode("algsumangles");
		else if (statement instanceof AlgebraicSumOfThreeSegments)
			writer.startNode("algsumsegs");
		else if (statement instanceof AngleEqualToSpecialConstantAngle)
			writer.startNode("angeqspecconsang");
		else if (statement instanceof CollinearPoints)
			writer.startNode("collinearpts");
		else if (statement instanceof ConcurrentCircles)
			writer.startNode("concurrentcircles");
		else if (statement instanceof ConcurrentLines)
			writer.startNode("concurrentlines");
		else if (statement instanceof ConcyclicPoints)
			writer.startNode("concyclicpts");
		else if (statement instanceof CongruentTriangles)
			writer.startNode("congrtriangles");
		else if (statement instanceof EqualAngles)
			writer.startNode("eqangles");
		else if (statement instanceof EqualityOfRatioProducts)
			writer.startNode("eqratioprods");
		else if (statement instanceof EqualityOfTwoRatios)
			writer.startNode("eqratios");
		else if (statement instanceof EquilateralTriangle)
			writer.startNode("eqtriangle");
		else if (statement instanceof FourHarmonicConjugatePoints)
			writer.startNode("harmonicconj");
		else if (statement instanceof IdenticalPoints)
			writer.startNode("identicpts");
		else if (statement instanceof LinearCombinationOfDoubleSignedPolygonAreas)
			writer.startNode("polyareas");
		else if (statement instanceof LinearCombinationOfOrientedSegments)
			writer.startNode("segmentscomb");
		else if (statement instanceof LinearCombinationOfSquaresOfSegments)
			writer.startNode("sqsegmentscomb");
		else if (statement instanceof PointOnSetOfPoints)
			writer.startNode("pointonset");
		else if (statement instanceof RatioOfOrientedSegments)
			writer.startNode("ratioorisegs");
		else if (statement instanceof RatioOfTwoSegments)
			writer.startNode("segratio");
		else if (statement instanceof SegmentsOfEqualLengths)
			writer.startNode("eqsegs");
		else if (statement instanceof SimilarTriangles)
			writer.startNode("simrtriangles");
		else if (statement instanceof TouchingCircles)
			writer.startNode("touchcircles");
		else if (statement instanceof TwoInversePoints)
			writer.startNode("inversepts");
		else if (statement instanceof TwoParallelLines)
			writer.startNode("parallellines");
		else if (statement instanceof TwoPerpendicularLines)
			writer.startNode("perplines");
		ctx.convertAnother(statement);
		writer.endNode();
		writer.endNode();
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext ctx) {
		OGPTP consProtocol = new OGPTP();
		OpenGeoProver.settings.setParsedCP(consProtocol); // this CP will be visible while parsing inner tags
		
		consProtocol.setTheoremName(reader.getAttribute("name"));
		
		reader.moveDown();
		if ("constructions".equals(reader.getNodeName())) {
			GeoConstruction geoCons = null;
			
			while (reader.hasMoreChildren()) {
				reader.moveDown();
				String nodeName = reader.getNodeName();
				
				if ("langbis".equals(nodeName))
					geoCons = (AngleBisector)ctx.convertAnother(null, AngleBisector.class);
				else if ("ang60deg".equals(nodeName))
					geoCons = (AngleOf60Deg)ctx.convertAnother(null, AngleOf60Deg.class);
				else if ("langray".equals(nodeName))
					geoCons = (AngleRay)ctx.convertAnother(null, AngleRay.class);
				else if ("langrayto60deg".equals(nodeName))
					geoCons = (AngleRayOfThirdAngleTo60Deg)ctx.convertAnother(null, AngleRayOfThirdAngleTo60Deg.class);
				else if ("langtris".equals(nodeName))
					geoCons = (AngleTrisector)ctx.convertAnother(null, AngleTrisector.class);
				else if ("pcirclecent".equals(nodeName))
					geoCons = (CenterOfCircle)ctx.convertAnother(null, CenterOfCircle.class);
				else if ("pcentralsymm".equals(nodeName))
					geoCons = (CentralSymmetricPoint)ctx.convertAnother(null, CentralSymmetricPoint.class);
				else if ("ccenterpt".equals(nodeName))
					geoCons = (CircleWithCenterAndPoint)ctx.convertAnother(null, CircleWithCenterAndPoint.class);
				else if ("ccenterradi".equals(nodeName))
					geoCons = (CircleWithCenterAndRadius)ctx.convertAnother(null, CircleWithCenterAndRadius.class);
				else if ("cdiam".equals(nodeName))
					geoCons = (CircleWithDiameter)ctx.convertAnother(null, CircleWithDiameter.class);
				else if ("ccircumscribed".equals(nodeName))
					geoCons = (CircumscribedCircle)ctx.convertAnother(null, CircumscribedCircle.class);
				else if ("psconic5".equals(nodeName)) 
					geoCons = (ConicSectionWithFivePoints)ctx.convertAnother(null, ConicSectionWithFivePoints.class);
				else if ("pfoot".equals(nodeName))
					geoCons = (FootPoint)ctx.convertAnother(null, FootPoint.class);
				else if ("pfree".equals(nodeName))
					geoCons = (FreePoint)ctx.convertAnother(null, FreePoint.class);
				else if ("psgenconic".equals(nodeName)) 
					geoCons = (GeneralConicSection)ctx.convertAnother(null, GeneralConicSection.class);
				else if ("pgensegdiv".equals(nodeName)) 
					geoCons = (GeneralizedSegmentDivisionPoint)ctx.convertAnother(null, GeneralizedSegmentDivisionPoint.class);
				else if ("pharmonicconj".equals(nodeName)) 
					geoCons = (HarmonicConjugatePoint)ctx.convertAnother(null, HarmonicConjugatePoint.class);
				else if ("pintersect".equals(nodeName))
					geoCons = (IntersectionPoint)ctx.convertAnother(null, IntersectionPoint.class);
				else if ("pinverse".equals(nodeName))
					geoCons = (InverseOfPoint)ctx.convertAnother(null, InverseOfPoint.class);
				else if ("ltwopts".equals(nodeName))
					geoCons = (LineThroughTwoPoints)ctx.convertAnother(null, LineThroughTwoPoints.class);
				else if ("pmid".equals(nodeName))
					geoCons = (MidPoint)ctx.convertAnother(null, MidPoint.class);
				else if ("lparallel".equals(nodeName)) 
					geoCons = (ParallelLine)ctx.convertAnother(null, ParallelLine.class);
				else if ("lperpbis".equals(nodeName)) 
					geoCons = (PerpendicularBisector)ctx.convertAnother(null, PerpendicularBisector.class);
				else if ("lperp".equals(nodeName)) 
					geoCons = (PerpendicularLine)ctx.convertAnother(null, PerpendicularLine.class);
				else if ("lpolar".equals(nodeName)) 
					geoCons = (Polar)ctx.convertAnother(null, Polar.class);
				else if ("ppole".equals(nodeName)) 
					geoCons = (Pole)ctx.convertAnother(null, Pole.class);
				else if ("lradical".equals(nodeName)) 
					geoCons = (RadicalAxis)ctx.convertAnother(null, RadicalAxis.class);
				else if ("prandcircle".equals(nodeName))
					geoCons = (RandomPointFromCircle)ctx.convertAnother(null, RandomPointFromCircle.class);
				else if ("prandgenconic".equals(nodeName))
					geoCons = (RandomPointFromGeneralConic)ctx.convertAnother(null, RandomPointFromGeneralConic.class);
				else if ("prandline".equals(nodeName))
					geoCons = (RandomPointFromLine)ctx.convertAnother(null, RandomPointFromLine.class);
				else if ("preflexive".equals(nodeName))
					geoCons = (ReflectedPoint)ctx.convertAnother(null, ReflectedPoint.class);
				else if ("protated".equals(nodeName)) 
					geoCons = (RotatedPoint)ctx.convertAnother(null, RotatedPoint.class);
				else if ("psegdiv".equals(nodeName)) 
					geoCons = (SegmentDivisionPoint)ctx.convertAnother(null, SegmentDivisionPoint.class);
				else if ("ltangent".equals(nodeName)) 
					geoCons = (TangentLine)ctx.convertAnother(null, TangentLine.class);
				else if ("ptranslated".equals(nodeName)) 
					geoCons = (TranslatedPoint)ctx.convertAnother(null, TranslatedPoint.class);
				else if ("ltangray".equals(nodeName))
					geoCons = (TripleAngleRay)ctx.convertAnother(null, TripleAngleRay.class);
				if (geoCons != null)
					consProtocol.addGeoConstruction(geoCons);
				
				reader.moveUp();
			}
		}
		reader.moveUp();
		reader.moveDown();
		if ("statement".equals(reader.getNodeName())) {
			reader.moveDown();
			String nodeName = reader.getNodeName();
			
			if ("algsumangles".equals(nodeName))
				consProtocol.addThmStatement((ThmStatement)ctx.convertAnother(null, AlgebraicSumOfThreeAngles.class));
			else if ("algsumsegs".equals(nodeName))
				consProtocol.addThmStatement((ThmStatement)ctx.convertAnother(null, AlgebraicSumOfThreeSegments.class));
			else if ("angeqspecconsang".equals(nodeName))
				consProtocol.addThmStatement((ThmStatement)ctx.convertAnother(null, AngleEqualToSpecialConstantAngle.class));
			else if ("collinearpts".equals(nodeName))
				consProtocol.addThmStatement((ThmStatement)ctx.convertAnother(null, CollinearPoints.class));
			else if ("concurrentcircles".equals(nodeName))
				consProtocol.addThmStatement((ThmStatement)ctx.convertAnother(null, ConcurrentCircles.class));
			else if ("concurrentlines".equals(nodeName))
				consProtocol.addThmStatement((ThmStatement)ctx.convertAnother(null, ConcurrentLines.class));
			else if ("concyclicpts".equals(nodeName))
				consProtocol.addThmStatement((ThmStatement)ctx.convertAnother(null, ConcyclicPoints.class));
			else if ("congrtriangles".equals(nodeName))
				consProtocol.addThmStatement((ThmStatement)ctx.convertAnother(null, CongruentTriangles.class));
			else if ("eqangles".equals(nodeName))
				consProtocol.addThmStatement((ThmStatement)ctx.convertAnother(null, EqualAngles.class));
			else if ("eqratioprods".equals(nodeName))
				consProtocol.addThmStatement((ThmStatement)ctx.convertAnother(null, EqualityOfRatioProducts.class));
			else if ("eqratios".equals(nodeName))
				consProtocol.addThmStatement((ThmStatement)ctx.convertAnother(null, EqualityOfTwoRatios.class));
			else if ("eqtriangle".equals(nodeName))
				consProtocol.addThmStatement((ThmStatement)ctx.convertAnother(null, EquilateralTriangle.class));
			else if ("harmonicconj".equals(nodeName))
				consProtocol.addThmStatement((ThmStatement)ctx.convertAnother(null, FourHarmonicConjugatePoints.class));
			else if ("identicpts".equals(nodeName))
				consProtocol.addThmStatement((ThmStatement)ctx.convertAnother(null, IdenticalPoints.class));
			else if ("polyareas".equals(nodeName))
				consProtocol.addThmStatement((ThmStatement)ctx.convertAnother(null, LinearCombinationOfDoubleSignedPolygonAreas.class));
			else if ("segmentscomb".equals(nodeName))
				consProtocol.addThmStatement((ThmStatement)ctx.convertAnother(null, LinearCombinationOfOrientedSegments.class));
			else if ("sqsegmentscomb".equals(nodeName))
				consProtocol.addThmStatement((ThmStatement)ctx.convertAnother(null, LinearCombinationOfSquaresOfSegments.class));
			else if ("pointonset".equals(nodeName))
				consProtocol.addThmStatement((ThmStatement)ctx.convertAnother(null, PointOnSetOfPoints.class));
			else if ("ratioorisegs".equals(nodeName))
				consProtocol.addThmStatement((ThmStatement)ctx.convertAnother(null, RatioOfOrientedSegments.class));
			else if ("segratio".equals(nodeName))
				consProtocol.addThmStatement((ThmStatement)ctx.convertAnother(null, RatioOfTwoSegments.class));
			else if ("eqsegs".equals(nodeName))
				consProtocol.addThmStatement((ThmStatement)ctx.convertAnother(null, SegmentsOfEqualLengths.class));
			else if ("simrtriangles".equals(nodeName))
				consProtocol.addThmStatement((ThmStatement)ctx.convertAnother(null, SimilarTriangles.class));
			else if ("touchcircles".equals(nodeName))
				consProtocol.addThmStatement((ThmStatement)ctx.convertAnother(null, TouchingCircles.class));
			else if ("inversepts".equals(nodeName))
				consProtocol.addThmStatement((ThmStatement)ctx.convertAnother(null, TwoInversePoints.class));
			else if ("parallellines".equals(nodeName))
				consProtocol.addThmStatement((ThmStatement)ctx.convertAnother(null, TwoParallelLines.class));
			else if ("perplines".equals(nodeName))
				consProtocol.addThmStatement((ThmStatement)ctx.convertAnother(null, TwoPerpendicularLines.class));
			reader.moveUp();
		}
		reader.moveUp();
		
		return consProtocol;
	}
	
}