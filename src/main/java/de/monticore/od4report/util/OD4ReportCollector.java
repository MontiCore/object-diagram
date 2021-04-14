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
import de.monticore.prettyprint.IndentPrinter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OD4ReportCollector {

  protected OD4ReportTraverser traverser = OD4ReportMill.traverser();

  protected IndentPrinter printer;

  private OD4ReportObjectCollector od4ReportObjectCollector = new OD4ReportObjectCollector();

  private ODBasisObjectCollector odBasisObjectCollector = new ODBasisObjectCollector();

  private ODLinkCollector odLinkCollector = new ODLinkCollector();

  public OD4ReportCollector() {
    init();
  }

  public List<ASTODReportObject> getReportObjects(ASTObjectDiagram objectDiagram) {
    objectDiagram.accept(traverser);
    List<ASTODReportObject> result = od4ReportObjectCollector.getNamedObjects();
    reset();
    return result;
  }

  public List<ASTODNamedObject> getNamedObjects(ASTObjectDiagram objectDiagram) {
    objectDiagram.accept(traverser);
    List<ASTODNamedObject> result = Stream.concat(odBasisObjectCollector.getNamedObjects().stream(),
        od4ReportObjectCollector.getNamedObjects().stream()).collect(Collectors.toList());
    reset();
    return result;
  }

  public List<ASTODAnonymousObject> getAnonymousObjects(ASTObjectDiagram objectDiagram) {
    objectDiagram.accept(traverser);
    List<ASTODAnonymousObject> result = odBasisObjectCollector.getAnonymousObjects();
    reset();
    return result;
  }

  public List<ASTODObject> getODObjects(ASTObjectDiagram objectDiagram) {
    objectDiagram.accept(traverser);
    List<ASTODObject> result = Stream.concat(odBasisObjectCollector.getODObjects().stream(),
        od4ReportObjectCollector.getNamedObjects().stream()).collect(Collectors.toList());
    reset();
    return result;
  }

  public List<ASTODLink> getODLinks(ASTObjectDiagram objectDiagram) {
    objectDiagram.accept(traverser);
    List<ASTODLink> result = odLinkCollector.getLinks();
    reset();
    return result;
  }

  private void init() {
    traverser.add4ODBasis(odBasisObjectCollector);
    traverser.add4ODLink(odLinkCollector);
    traverser.add4OD4Report(od4ReportObjectCollector);
  }

  private void reset() {
    od4ReportObjectCollector = new OD4ReportObjectCollector();
    odBasisObjectCollector = new ODBasisObjectCollector();
    odLinkCollector = new ODLinkCollector();
  }

}
