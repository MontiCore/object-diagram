// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report.util;

import de.monticore.od4report.OD4ReportMill;
import de.monticore.od4report._ast.ASTODReportObject;
import de.monticore.od4report._visitor.OD4ReportTraverser;
import de.monticore.odbasis._ast.ASTODAnonymousObject;
import de.monticore.odbasis._ast.ASTODNamedObject;
import de.monticore.odbasis._ast.ASTODObject;
import de.monticore.odbasis._ast.ASTObjectDiagram;
import de.monticore.odbasis.utils.ODBasisObjectCollector;
import de.monticore.odlink._ast.ASTODLink;
import de.monticore.odlink.utils.ODLinkCollector;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Collects the objects and links of an OD4Report object diagram. Each query traverses the
 * diagram with fresh collectors, so a collector instance can be reused for any number of queries.
 */
public class OD4ReportCollector {

  public List<ASTODReportObject> getReportObjects(ASTObjectDiagram objectDiagram) {
    return collect(objectDiagram).reportObjects().getNamedObjects();
  }

  public List<ASTODNamedObject> getNamedObjects(ASTObjectDiagram objectDiagram) {
    Collected collected = collect(objectDiagram);
    return Stream.concat(collected.basisObjects().getNamedObjects().stream(),
        collected.reportObjects().getNamedObjects().stream()).collect(Collectors.toList());
  }

  public List<ASTODAnonymousObject> getAnonymousObjects(ASTObjectDiagram objectDiagram) {
    return collect(objectDiagram).basisObjects().getAnonymousObjects();
  }

  public List<ASTODObject> getODObjects(ASTObjectDiagram objectDiagram) {
    Collected collected = collect(objectDiagram);
    return Stream.concat(collected.basisObjects().getODObjects().stream(),
        collected.reportObjects().getNamedObjects().stream()).collect(Collectors.toList());
  }

  public List<ASTODLink> getODLinks(ASTObjectDiagram objectDiagram) {
    return collect(objectDiagram).links().getLinks();
  }

  protected Collected collect(ASTObjectDiagram objectDiagram) {
    Collected collected = new Collected(new OD4ReportObjectCollector(),
        new ODBasisObjectCollector(), new ODLinkCollector());

    OD4ReportTraverser traverser = OD4ReportMill.traverser();
    traverser.add4OD4Report(collected.reportObjects());
    traverser.add4ODBasis(collected.basisObjects());
    traverser.add4ODLink(collected.links());
    objectDiagram.accept(traverser);

    return collected;
  }

  protected record Collected(OD4ReportObjectCollector reportObjects,
                             ODBasisObjectCollector basisObjects, ODLinkCollector links) {
  }

}
