/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.formats.ogp_xml;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.prover_protocol.cp.OGPCP;
import com.ogprover.prover_protocol.cp.geoconstruction.AngleBisector;
import com.ogprover.prover_protocol.cp.geoconstruction.CenterOfCircle;
import com.ogprover.prover_protocol.cp.geoconstruction.CentralSymmetricPoint;
import com.ogprover.prover_protocol.cp.geoconstruction.CircleWithCenterAndPoint;
import com.ogprover.prover_protocol.cp.geoconstruction.CircleWithCenterAndRadius;
import com.ogprover.prover_protocol.cp.geoconstruction.CircleWithDiameter;
import com.ogprover.prover_protocol.cp.geoconstruction.CircumscribedCircle;
import com.ogprover.prover_protocol.cp.geoconstruction.FootPoint;
import com.ogprover.prover_protocol.cp.geoconstruction.FreePoint;
import com.ogprover.prover_protocol.cp.geoconstruction.GeneralizedSegmentDivisionPoint;
import com.ogprover.prover_protocol.cp.geoconstruction.GeoConstruction;
import com.ogprover.prover_protocol.cp.geoconstruction.HarmonicConjugatePoint;
import com.ogprover.prover_protocol.cp.geoconstruction.IntersectionPoint;
import com.ogprover.prover_protocol.cp.geoconstruction.InverseOfPoint;
import com.ogprover.prover_protocol.cp.geoconstruction.LineThroughTwoPoints;
import com.ogprover.prover_protocol.cp.geoconstruction.MidPoint;
import com.ogprover.prover_protocol.cp.geoconstruction.ParallelLine;
import com.ogprover.prover_protocol.cp.geoconstruction.PerpendicularBisector;
import com.ogprover.prover_protocol.cp.geoconstruction.PerpendicularLine;
import com.ogprover.prover_protocol.cp.geoconstruction.RadicalAxis;
import com.ogprover.prover_protocol.cp.geoconstruction.RandomPointFromCircle;
import com.ogprover.prover_protocol.cp.geoconstruction.RandomPointFromLine;
import com.ogprover.prover_protocol.cp.geoconstruction.ReflexivePoint;
import com.ogprover.prover_protocol.cp.geoconstruction.RotatedPoint;
import com.ogprover.prover_protocol.cp.geoconstruction.SegmentDivisionPoint;
import com.ogprover.prover_protocol.cp.geoconstruction.TangentLine;
import com.ogprover.prover_protocol.cp.geoconstruction.TranslatedPoint;
import com.ogprover.prover_protocol.cp.thmstatement.AlgebraicSumOfThreeAngles;
import com.ogprover.prover_protocol.cp.thmstatement.AlgebraicSumOfThreeSegments;
import com.ogprover.prover_protocol.cp.thmstatement.CollinearPoints;
import com.ogprover.prover_protocol.cp.thmstatement.ConcurrentCircles;
import com.ogprover.prover_protocol.cp.thmstatement.ConcurrentLines;
import com.ogprover.prover_protocol.cp.thmstatement.ConcyclicPoints;
import com.ogprover.prover_protocol.cp.thmstatement.CongruentTriangles;
import com.ogprover.prover_protocol.cp.thmstatement.EqualAngles;
import com.ogprover.prover_protocol.cp.thmstatement.EqualityOfRatioProducts;
import com.ogprover.prover_protocol.cp.thmstatement.EqualityOfTwoRatios;
import com.ogprover.prover_protocol.cp.thmstatement.FourHarmonicConjugatePoints;
import com.ogprover.prover_protocol.cp.thmstatement.IdenticalPoints;
import com.ogprover.prover_protocol.cp.thmstatement.LinearCombinationOfDoubleSignedPolygonAreas;
import com.ogprover.prover_protocol.cp.thmstatement.LinearCombinationOfOrientedSegments;
import com.ogprover.prover_protocol.cp.thmstatement.PointOnSetOfPoints;
import com.ogprover.prover_protocol.cp.thmstatement.RatioOfOrientedSegments;
import com.ogprover.prover_protocol.cp.thmstatement.RatioOfTwoSegments;
import com.ogprover.prover_protocol.cp.thmstatement.SegmentsOfEqualLengths;
import com.ogprover.prover_protocol.cp.thmstatement.SimilarTriangles;
import com.ogprover.prover_protocol.cp.thmstatement.ThmStatement;
import com.ogprover.prover_protocol.cp.thmstatement.TouchingCircles;
import com.ogprover.prover_protocol.cp.thmstatement.TwoInversePoints;
import com.ogprover.prover_protocol.cp.thmstatement.TwoParallelLines;
import com.ogprover.prover_protocol.cp.thmstatement.TwoPerpendicularLines;
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
		return clazz.equals(OGPCP.class);
	}

	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext ctx) {
		OGPCP consProtocol = (OGPCP)obj;
		
		if (consProtocol == null)
			return;
		
		String thmName = consProtocol.getTheoremName();
		if (thmName != null)
			writer.addAttribute("name", thmName);
		
		writer.startNode("constructions");
		for (GeoConstruction geoCons : consProtocol.getConstructionSteps()) {
			if (geoCons instanceof AngleBisector)
				writer.startNode("langbis");
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
			else if (geoCons instanceof FootPoint)
				writer.startNode("pfoot");
			else if (geoCons instanceof FreePoint)
				writer.startNode("pfree");
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
			else if (geoCons instanceof RadicalAxis)
				writer.startNode("lradical");
			else if (geoCons instanceof RandomPointFromCircle)
				writer.startNode("prandcircle");
			else if (geoCons instanceof RandomPointFromLine)
				writer.startNode("prandline");
			else if (geoCons instanceof ReflexivePoint)
				writer.startNode("preflexive");
			else if (geoCons instanceof RotatedPoint)
				writer.startNode("protated");
			else if (geoCons instanceof SegmentDivisionPoint)
				writer.startNode("psegdiv");
			else if (geoCons instanceof TangentLine)
				writer.startNode("ltangent");
			else if (geoCons instanceof TranslatedPoint)
				writer.startNode("ptranslated");
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
		else if (statement instanceof FourHarmonicConjugatePoints)
			writer.startNode("harmonicconj");
		else if (statement instanceof IdenticalPoints)
			writer.startNode("identicpts");
		else if (statement instanceof LinearCombinationOfDoubleSignedPolygonAreas)
			writer.startNode("polyareas");
		else if (statement instanceof LinearCombinationOfOrientedSegments)
			writer.startNode("segmentscomb");
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
		OGPCP consProtocol = new OGPCP();
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
				else if ("pfoot".equals(nodeName))
					geoCons = (FootPoint)ctx.convertAnother(null, FootPoint.class);
				else if ("pfree".equals(nodeName))
					geoCons = (FreePoint)ctx.convertAnother(null, FreePoint.class);
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
				else if ("lradical".equals(nodeName)) 
					geoCons = (RadicalAxis)ctx.convertAnother(null, RadicalAxis.class);
				else if ("prandcircle".equals(nodeName))
					geoCons = (RandomPointFromCircle)ctx.convertAnother(null, RandomPointFromCircle.class);
				else if ("prandline".equals(nodeName))
					geoCons = (RandomPointFromLine)ctx.convertAnother(null, RandomPointFromLine.class);
				else if ("preflexive".equals(nodeName))
					geoCons = (ReflexivePoint)ctx.convertAnother(null, ReflexivePoint.class);
				else if ("protated".equals(nodeName)) 
					geoCons = (RotatedPoint)ctx.convertAnother(null, RotatedPoint.class);
				else if ("psegdiv".equals(nodeName)) 
					geoCons = (SegmentDivisionPoint)ctx.convertAnother(null, SegmentDivisionPoint.class);
				else if ("ltangent".equals(nodeName)) 
					geoCons = (TangentLine)ctx.convertAnother(null, TangentLine.class);
				else if ("ptranslated".equals(nodeName)) 
					geoCons = (TranslatedPoint)ctx.convertAnother(null, TranslatedPoint.class);
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
			else if ("harmonicconj".equals(nodeName))
				consProtocol.addThmStatement((ThmStatement)ctx.convertAnother(null, FourHarmonicConjugatePoints.class));
			else if ("identicpts".equals(nodeName))
				consProtocol.addThmStatement((ThmStatement)ctx.convertAnother(null, IdenticalPoints.class));
			else if ("polyareas".equals(nodeName))
				consProtocol.addThmStatement((ThmStatement)ctx.convertAnother(null, LinearCombinationOfDoubleSignedPolygonAreas.class));
			else if ("segmentscomb".equals(nodeName))
				consProtocol.addThmStatement((ThmStatement)ctx.convertAnother(null, LinearCombinationOfOrientedSegments.class));
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