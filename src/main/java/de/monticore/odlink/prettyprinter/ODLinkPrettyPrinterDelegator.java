// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

package de.monticore.odlink.prettyprinter;

import de.monticore.odbasis._ast.ASTODBasisNode;
import de.monticore.odbasis.prettyprinter.ODBasisPrettyPrinterDelegator;
import de.monticore.odlink._ast.ASTODLinkNode;
import de.monticore.odlink._visitor.ODLinkDelegatorVisitor;
import de.monticore.prettyprint.IndentPrinter;

public class ODLinkPrettyPrinterDelegator extends ODLinkDelegatorVisitor {

  protected ODLinkDelegatorVisitor realThis = this;

  protected IndentPrinter printer;

  public ODLinkPrettyPrinterDelegator() {
    this.printer = new IndentPrinter();
    init();
  }

  public ODLinkPrettyPrinterDelegator(IndentPrinter indentPrinter) {
    this.printer = indentPrinter;
    init();
  }

  public String prettyprint(ASTODLinkNode astodLinkNode) {
    getPrinter().clearBuffer();
    astodLinkNode.accept(getRealThis());
    return getPrinter().getContent();
  }

  public String prettyprint(ASTODBasisNode astodBasicsNode) {
    getPrinter().clearBuffer();
    astodBasicsNode.accept(getRealThis());
    return getPrinter().getContent();
  }

  protected IndentPrinter getPrinter() {
    return this.printer;
  }

  private void init() {
    realThis = this;
    setODLinkVisitor(new ODLinkPrettyPrinter(printer));
    setODBasisVisitor(new ODBasisPrettyPrinterDelegator(printer));
  }

}
