// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report.prettyprinter;

import de.monticore.od4report._ast.ASTODDate;
import de.monticore.od4report._ast.ASTODName;
import de.monticore.od4report._ast.ASTODReportObject;
import de.monticore.od4report._visitor.OD4ReportHandler;
import de.monticore.od4report._visitor.OD4ReportTraverser;
import de.monticore.od4report._visitor.OD4ReportVisitor2;
import de.monticore.prettyprint.IndentPrinter;

public class OD4ReportPrettyPrinter implements OD4ReportVisitor2, OD4ReportHandler {

  protected IndentPrinter printer;

  protected OD4ReportTraverser traverser;

  public OD4ReportPrettyPrinter(IndentPrinter printer) {
    this.printer = printer;
  }

  public IndentPrinter getPrinter() {
    return printer;
  }

  @Override
  public void handle(ASTODName a) {
    getPrinter().print(a.getName());
  }

  @Override
  public void handle(ASTODDate astodDate) {
    astodDate.getDate().accept(getTraverser());
  }

  @Override
  public void traverse(ASTODReportObject a) {
  }

  @Override
  public OD4ReportTraverser getTraverser() {
    return traverser;
  }

  @Override
  public void setTraverser(OD4ReportTraverser traverser) {
    this.traverser = traverser;
  }

}
