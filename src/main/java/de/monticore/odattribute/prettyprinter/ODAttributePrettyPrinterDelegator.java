// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

package de.monticore.odattribute.prettyprinter;

import de.monticore.odattribute._ast.ASTODAttributeNode;
import de.monticore.odattribute._visitor.ODAttributeDelegatorVisitor;
import de.monticore.odbasis._ast.ASTODBasisNode;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.prettyprint.MCCollectionTypesPrettyPrinter;
import de.monticore.types.prettyprint.MCFullGenericTypesPrettyPrinter;
import de.monticore.types.prettyprint.MCSimpleGenericTypesPrettyPrinter;

public class ODAttributePrettyPrinterDelegator extends ODAttributeDelegatorVisitor {

  protected ODAttributeDelegatorVisitor realThis = this;

  protected IndentPrinter printer;

  public ODAttributePrettyPrinterDelegator() {
    this.printer = new IndentPrinter();
    init();
  }

  public ODAttributePrettyPrinterDelegator(IndentPrinter indentPrinter) {
    this.printer = indentPrinter;
    init();
  }

  public String prettyprint(ASTODAttributeNode astodAttributeNode) {
    getPrinter().clearBuffer();
    astodAttributeNode.accept(getRealThis());
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
    setODAttributeVisitor(new ODAttributePrettyPrinter(printer));

    setMCSimpleGenericTypesVisitor(new MCSimpleGenericTypesPrettyPrinter(printer));
    setMCCollectionTypesVisitor(new MCCollectionTypesPrettyPrinter(printer));
    setMCFullGenericTypesVisitor(new MCFullGenericTypesPrettyPrinter(printer));
  }

}
