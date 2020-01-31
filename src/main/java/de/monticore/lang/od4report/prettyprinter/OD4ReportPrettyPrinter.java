// (c) https://github.com/MontiCore/monticore

package de.monticore.lang.od4report.prettyprinter;

import de.monticore.lang.dateliterals.prettyprinter.DateLiteralsPrettyPrinter;
import de.monticore.lang.od4report._ast.ASTODDate;
import de.monticore.lang.od4report._ast.ASTODName;
import de.monticore.lang.od4report._visitor.OD4ReportVisitor;
import de.monticore.lang.odbasics.prettyprinter.ODBasicsPrettyPrinter;
import de.monticore.prettyprint.IndentPrinter;

public class OD4ReportPrettyPrinter extends ODBasicsPrettyPrinter implements OD4ReportVisitor {

  protected DateLiteralsPrettyPrinter dateLiteralsPrettyPrinter;

  /**
   * Constructor.
   *
   * @param printer the printer to write to.
   */
  public OD4ReportPrettyPrinter(IndentPrinter printer) {
    super(printer);
    this.dateLiteralsPrettyPrinter = new DateLiteralsPrettyPrinter(printer);
  }

  @Override
  public void handle(ASTODName a) {
    getPrinter().print(a.getName());
  }

  @Override
  public void handle(ASTODDate astodDate) {
    astodDate.getDate().accept(this.dateLiteralsPrettyPrinter);
  }

  private OD4ReportVisitor realThis = this;

  /**
   * @see de.monticore.lang.od4report._visitor.OD4ReportVisitor#setRealThis(de.monticore.lang.od4report._visitor.OD4ReportVisitor)
   */
  @Override
  public void setRealThis(OD4ReportVisitor realThis) {
    this.realThis = realThis;
  }

  /**
   * @see de.monticore.lang.od4report._visitor.OD4ReportVisitor#getRealThis()
   */
  @Override
  public OD4ReportVisitor getRealThis() {
    return realThis;
  }

}
