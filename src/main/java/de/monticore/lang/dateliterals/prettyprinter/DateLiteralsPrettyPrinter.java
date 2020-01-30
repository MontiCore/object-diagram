// (c) https://github.com/MontiCore/monticore

package de.monticore.lang.dateliterals.prettyprinter;

import de.monticore.lang.dateliterals._ast.ASTDate;
import de.monticore.lang.dateliterals._ast.ASTDateV1;
import de.monticore.lang.dateliterals._ast.ASTDateV2;
import de.monticore.lang.dateliterals._ast.ASTDateV3;
import de.monticore.lang.dateliterals._visitor.DateLiteralsVisitor;
import de.monticore.prettyprint.IndentPrinter;

public class DateLiteralsPrettyPrinter implements DateLiteralsVisitor {

  // printer to use
  protected IndentPrinter printer;

  /**
   * Constructor.
   *
   * @param printer the printer to write to.
   */
  public DateLiteralsPrettyPrinter(IndentPrinter printer) {
    this.printer = printer;
  }

  /**
   * Return current {@link IndentPrinter}.
   *
   * @return current printer
   */
  public IndentPrinter getPrinter() {
    return printer;
  }

  @Override
  public void handle(ASTDate a) {
    printODDate(a);
  }

  @Override
  public void handle(ASTDateV1 a) {
    printODDate(a);
  }

  @Override
  public void handle(ASTDateV2 a) {
    printODDate(a);
  }

  @Override
  public void handle(ASTDateV3 a) {
    printODDate(a);
  }

  private void printODDate(ASTDate a) {
    getPrinter().print(a.getYear().getValue());
    getPrinter().print("-");
    getPrinter().print(a.getMonth().getValue());
    getPrinter().print("-");
    getPrinter().print(a.getDay().getValue());
    getPrinter().print(" ");
    getPrinter().print(a.getHour().getValue());
    getPrinter().print(":");
    getPrinter().print(a.getMinute().getValue());
    getPrinter().print(":");
    getPrinter().print(a.getSecond().getValue());
  }

  private DateLiteralsVisitor realThis = this;

  /**
   * @see de.monticore.lang.dateliterals._visitor.DateLiteralsVisitor#setRealThis(de.monticore.lang.dateliterals._visitor.DateLiteralsVisitor)
   */
  @Override
  public void setRealThis(DateLiteralsVisitor realThis) {
    this.realThis = realThis;
  }

  /**
   * @see de.monticore.lang.dateliterals._visitor.DateLiteralsVisitor#getRealThis()
   */
  @Override
  public DateLiteralsVisitor getRealThis() {
    return realThis;
  }

}
