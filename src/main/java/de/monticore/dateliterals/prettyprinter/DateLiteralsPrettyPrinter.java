// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

package de.monticore.dateliterals.prettyprinter;

import de.monticore.dateliterals._ast.*;
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
    a.getDatePart().accept(getRealThis());
    getPrinter().print(" ");
    a.getTimePart().accept(getRealThis());
  }

  @Override
  public void handle(ASTDatePartHyphen datePartHyphen) {
    getPrinter().print(datePartHyphen.getYear().getValue());
    getPrinter().print("-");
    getPrinter().print(datePartHyphen.getMonth().getValue());
    getPrinter().print("-");
    getPrinter().print(datePartHyphen.getDay().getValue());
  }

  @Override
  public void handle(ASTDatePartDot datePartDot) {
    getPrinter().print(datePartDot.getYear().getValue());
    getPrinter().print(".");
    getPrinter().print(datePartDot.getMonth().getValue());
    getPrinter().print(".");
    getPrinter().print(datePartDot.getDay().getValue());
  }

  @Override
  public void handle(ASTDatePartSlash datePartSlash) {
    getPrinter().print(datePartSlash.getYear().getValue());
    getPrinter().print("/");
    getPrinter().print(datePartSlash.getMonth().getValue());
    getPrinter().print("/");
    getPrinter().print(datePartSlash.getDay().getValue());
  }

  @Override
  public void handle(ASTTimePartColon timePartColon) {
    getPrinter().print(timePartColon.getHour().getValue());
    getPrinter().print(":");
    getPrinter().print(timePartColon.getMinute().getValue());
    getPrinter().print(":");
    getPrinter().print(timePartColon.getSecond().getValue());
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
