package de.monticore.od4report.prettyprinter;

import de.monticore.od4report._ast.ASTODName;
import de.monticore.od4report._visitor.OD4ReportHandler;
import de.monticore.od4report._visitor.OD4ReportTraverser;
import de.monticore.od4report._visitor.OD4ReportVisitor2;
import de.monticore.odbasis._visitor.ODBasisHandler;
import de.monticore.odbasis._visitor.ODBasisVisitor2;
import de.monticore.prettyprint.IndentPrinter;

/**
 * Driver class for the report pretty printer implementing the {@link ODBasisVisitor2},
 * {@link ODBasisHandler}.
 */
public class PlantUMLODReportPrettyPrinter implements OD4ReportVisitor2, OD4ReportHandler {
  private final IndentPrinter printer;
  private OD4ReportTraverser traverser;
  
  public PlantUMLODReportPrettyPrinter(IndentPrinter printer) {
    this.printer = printer;
  }
  
  @Override
  public OD4ReportTraverser getTraverser() {
    return traverser;
  }
  
  @Override
  public void setTraverser(OD4ReportTraverser traverser) {
    this.traverser = traverser;
  }
  
  /**
   * This method visits the named ast node
   *
   * @param node named ast node
   */
  @Override
  public void visit(ASTODName node) {
    printer.print(node.isPresentName() ? node.getName() : node.getODSpecialName());
  }
}
