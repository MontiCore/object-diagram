// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

package de.monticore.dateliterals.prettyprinter;

import de.monticore.dateliterals._ast.*;
import de.monticore.dateliterals._visitor.DateLiteralsHandler;
import de.monticore.dateliterals._visitor.DateLiteralsTraverser;
import de.monticore.prettyprint.IndentPrinter;

public class DateLiteralsPrettyPrinter implements DateLiteralsHandler {

  protected IndentPrinter printer;

  protected DateLiteralsTraverser traverser;

  public DateLiteralsPrettyPrinter(IndentPrinter printer) {
    this.printer = printer;
  }

  public IndentPrinter getPrinter() {
    return printer;
  }

  @Override
  public void handle(ASTDate a) {
    printODDate(a);
  }

  private void printODDate(ASTDate a) {
    a.getDatePart().accept(getTraverser());
    getPrinter().print(" ");
    a.getTimePart().accept(getTraverser());
  }

  @Override
  public void handle(ASTDatePartHyphen datePartHyphen) {
    getPrinter().print(datePartHyphen.getYear().getDigits());
    getPrinter().print("-");
    getPrinter().print(datePartHyphen.getMonth().getDigits());
    getPrinter().print("-");
    getPrinter().print(datePartHyphen.getDay().getDigits());
  }

  @Override
  public void handle(ASTDatePartDot datePartDot) {
    getPrinter().print(datePartDot.getYear().getDigits());
    getPrinter().print(".");
    getPrinter().print(datePartDot.getMonth().getDigits());
    getPrinter().print(".");
    getPrinter().print(datePartDot.getDay().getDigits());
  }

  @Override
  public void handle(ASTDatePartSlash datePartSlash) {
    getPrinter().print(datePartSlash.getYear().getDigits());
    getPrinter().print("/");
    getPrinter().print(datePartSlash.getMonth().getDigits());
    getPrinter().print("/");
    getPrinter().print(datePartSlash.getDay().getDigits());
  }

  @Override
  public void handle(ASTTimePartColon timePartColon) {
    getPrinter().print(timePartColon.getHour().getDigits());
    getPrinter().print(":");
    getPrinter().print(timePartColon.getMinute().getDigits());
    getPrinter().print(":");
    getPrinter().print(timePartColon.getSecond().getDigits());
  }

  @Override
  public DateLiteralsTraverser getTraverser() {
    return traverser;
  }

  @Override
  public void setTraverser(DateLiteralsTraverser traverser) {
    this.traverser = traverser;
  }

}
