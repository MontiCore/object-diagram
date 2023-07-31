// (c) https://github.com/MontiCore/monticore

package de.monticore.dateliterals._prettyprint;

import de.monticore.dateliterals._ast.*;
import de.monticore.prettyprint.IndentPrinter;

public class DateLiteralsPrettyPrinter extends DateLiteralsPrettyPrinterTOP {
  
  public DateLiteralsPrettyPrinter(IndentPrinter printer, boolean printComments) {
    super(printer, printComments);
  }
  
  @Override
  public void handle(ASTDate a) {
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
  
}
