// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report.prettyprinter;

import de.monticore.od4report._ast.ASTODDate;
import de.monticore.od4report._ast.ASTODName;
import de.monticore.od4report._ast.ASTODReportObject;
import de.monticore.od4report._visitor.OD4ReportInheritanceVisitor;
import de.monticore.od4report._visitor.OD4ReportVisitor;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.prettyprint.MCBasicsPrettyPrinter;

public class OD4ReportPrettyPrinter extends MCBasicsPrettyPrinter implements
    OD4ReportInheritanceVisitor {

  /**
   * Constructor.
   *
   * @param printer the printer to write to.
   */
  public OD4ReportPrettyPrinter(IndentPrinter printer) {
    super(printer);
  }

  @Override
  public void handle(ASTODName a) {
    getPrinter().print(a.getName());
  }

  @Override
  public void handle(ASTODDate astodDate) {
    astodDate.getDate().accept(getRealThis());
  }

  @Override
  public void traverse(ASTODReportObject a) {
  }

  private OD4ReportVisitor realThis = this;

  /**
   * @see de.monticore.od4report._visitor.OD4ReportVisitor#setRealThis(de.monticore.od4report._visitor.OD4ReportVisitor)
   */
  @Override
  public void setRealThis(OD4ReportVisitor realThis) {
    this.realThis = realThis;
  }

  /**
   * @see de.monticore.od4report._visitor.OD4ReportVisitor#getRealThis()
   */
  @Override
  public OD4ReportVisitor getRealThis() {
    return realThis;
  }

}
