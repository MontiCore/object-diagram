// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report.util;

import de.monticore.od4report._visitor.OD4ReportDelegatorVisitor;
import de.monticore.odattribute.utils.ODAttributeObjectCollector;
import de.monticore.odbasis._ast.ASTODAnonymousObject;
import de.monticore.odbasis._ast.ASTODNamedObject;
import de.monticore.odbasis._ast.ASTODObject;
import de.monticore.odbasis.utils.ODBasisObjectCollector;
import de.monticore.odlink._ast.ASTODLink;
import de.monticore.odlink.utils.ODLinkCollector;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OD4ReportCollector extends OD4ReportDelegatorVisitor {

  protected OD4ReportDelegatorVisitor realThis = this;

  private OD4ReportObjectCollector od4ReportObjectCollector = new OD4ReportObjectCollector();

  private ODBasisObjectCollector odBasisObjectCollector = new ODBasisObjectCollector();

  private ODLinkCollector odLinkCollector = new ODLinkCollector();

  private ODAttributeObjectCollector odAttributeObjectCollector = new ODAttributeObjectCollector();

  public OD4ReportCollector() {
    setOD4ReportVisitor(od4ReportObjectCollector);
    setODBasisVisitor(odBasisObjectCollector);
    setODLinkVisitor(odLinkCollector);
    setODAttributeVisitor(odAttributeObjectCollector);
  }

  public List<ASTODNamedObject> getNamedObjects() {
    return Stream.concat(Stream.concat(odBasisObjectCollector.getNamedObjects()
        .stream(), odAttributeObjectCollector.getNamedObjects()
        .stream()), od4ReportObjectCollector.getNamedObjects()
        .stream())
        .collect(Collectors.toList());
  }

  public List<ASTODAnonymousObject> getAnonymousObjects() {
    return odBasisObjectCollector.getAnonymousObjects();
  }

  public List<ASTODObject> getODObjects() {
    return Stream.concat(Stream.concat(odBasisObjectCollector.getODObjects()
        .stream(), odAttributeObjectCollector.getODObjects()
        .stream()), od4ReportObjectCollector.getNamedObjects()
        .stream())
        .collect(Collectors.toList());
  }

  public List<ASTODLink> getODLinks() {
    return odLinkCollector.getLinks();
  }

}
