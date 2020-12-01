// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

package de.monticore.dateliterals.prettyprinter;

import de.monticore.dateliterals._ast.*;
import de.monticore.dateliterals._visitor.DateLiteralsHandler;
import de.monticore.dateliterals._visitor.DateLiteralsTraverser;
import de.monticore.dateliterals._visitor.DateLiteralsVisitor2;
import de.monticore.prettyprint.IndentPrinter;

public class DateLiteralsPrettyPrinter implements DateLiteralsVisitor2, DateLiteralsHandler {

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
  
  @Override 
  public DateLiteralsTraverser getTraverser() {
    return traverser;
  }
  
  @Override
  public void setTraverser(DateLiteralsTraverser traverser) {
    this.traverser = traverser;
  }

}
