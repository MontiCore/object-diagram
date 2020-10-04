// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report.util;

import de.monticore.od4report._ast.ASTODReportObject;
import de.monticore.od4report._visitor.OD4ReportVisitor;
import de.monticore.odbasis._ast.ASTODNamedObject;

import java.util.ArrayList;
import java.util.List;

public class OD4ReportObjectCollector implements OD4ReportVisitor {

  private List<ASTODNamedObject> namedObjects = new ArrayList<>();

  @Override
  public void visit(ASTODReportObject reportObject) {
    namedObjects.add(reportObject);
  }

  public List<ASTODNamedObject> getNamedObjects() {
    return namedObjects;
  }

  private OD4ReportVisitor realThis = this;

  @Override
  public void setRealThis(OD4ReportVisitor realThis) {
    this.realThis = realThis;
  }

  @Override
  public OD4ReportVisitor getRealThis() {
    return realThis;
  }

}
