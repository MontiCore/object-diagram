// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

package de.monticore.dateliterals.prettyprinter;

import de.monticore.dateliterals._ast.ASTDate;
import de.monticore.dateliterals._visitor.DateLiteralsVisitor;
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

  private void printODDate(ASTDate a) {
    getPrinter().print(a.getDatePart().getYear().getValue());
    getPrinter().print("-");
    getPrinter().print(a.getDatePart().getMonth().getValue());
    getPrinter().print("-");
    getPrinter().print(a.getDatePart().getDay().getValue());
    getPrinter().print(" ");
    getPrinter().print(a.getTimePart().getHour().getValue());
    getPrinter().print(":");
    getPrinter().print(a.getTimePart().getMinute().getValue());
    getPrinter().print(":");
    getPrinter().print(a.getTimePart().getSecond().getValue());
  }

  private DateLiteralsVisitor realThis = this;

  /**
   * @see de.monticore.dateliterals._visitor.DateLiteralsVisitor#setRealThis(de.monticore.dateliterals._visitor.DateLiteralsVisitor)
   */
  @Override
  public void setRealThis(DateLiteralsVisitor realThis) {
    this.realThis = realThis;
  }

  /**
   * @see de.monticore.dateliterals._visitor.DateLiteralsVisitor#getRealThis()
   */
  @Override
  public DateLiteralsVisitor getRealThis() {
    return realThis;
  }

}
