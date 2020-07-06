// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

package de.monticore.odbasics.prettyprinter;

import de.monticore.MCCommonLiteralsPrettyPrinter;
import de.monticore.literals.mccommonliterals._ast.ASTMCCommonLiteralsNode;
import de.monticore.odbasics._ast.ASTODBasicsNode;
import de.monticore.odbasics._visitor.ODBasicsDelegatorVisitor;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.prettyprint.UMLModifierPrettyPrinter;
import de.monticore.prettyprint.UMLStereotypePrettyPrinter;
import de.monticore.types.mcbasictypes._ast.ASTMCType;
import de.monticore.types.mcsimplegenerictypes._ast.ASTMCSimpleGenericTypesNode;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;
import de.monticore.types.prettyprint.MCCollectionTypesPrettyPrinter;
import de.monticore.types.prettyprint.MCFullGenericTypesPrettyPrinter;
import de.monticore.types.prettyprint.MCSimpleGenericTypesPrettyPrinter;
import de.monticore.umlmodifier._ast.ASTUMLModifierNode;
import de.monticore.umlstereotype._ast.ASTUMLStereotypeNode;

public class ODPrettyPrinterDelegator extends ODBasicsDelegatorVisitor {

  protected ODBasicsDelegatorVisitor realThis = this;

  protected IndentPrinter printer;

  public ODPrettyPrinterDelegator() {
    this.printer = new IndentPrinter();
    init();
  }

  public ODPrettyPrinterDelegator(IndentPrinter indentPrinter) {
    this.printer = indentPrinter;
    init();
  }

  protected IndentPrinter getPrinter() {
    return this.printer;
  }

  public String prettyprint(ASTODBasicsNode astodBasicsNode) {
    getPrinter().clearBuffer();
    astodBasicsNode.accept(getRealThis());
    return getPrinter().getContent();
  }

  public String prettyprint(ASTMCType astmcType) {
    getPrinter().clearBuffer();
    astmcType.accept(getRealThis());
    return getPrinter().getContent();
  }

  public String prettyprint(ASTUMLStereotypeNode astumlStereotypeNode) {
    getPrinter().clearBuffer();
    astumlStereotypeNode.accept(getRealThis());
    return getPrinter().getContent();
  }

  public String prettyprint(ASTUMLModifierNode astumlModifierNode) {
    getPrinter().clearBuffer();
    astumlModifierNode.accept(getRealThis());
    return getPrinter().getContent();
  }

  public String prettyprint(ASTMCCommonLiteralsNode astmcCommonLiteralsNode) {
    getPrinter().clearBuffer();
    astmcCommonLiteralsNode.accept(getRealThis());
    return getPrinter().getContent();
  }

  public String prettyprint(ASTMCSimpleGenericTypesNode astmcSimpleGenericTypesNode) {
    getPrinter().clearBuffer();
    astmcSimpleGenericTypesNode.accept(getRealThis());
    return getPrinter().getContent();
  }

  private void init() {
    realThis = this;
    setODBasicsVisitor(new ODBasicsPrettyPrinter(printer));
    setUMLStereotypeVisitor(new UMLStereotypePrettyPrinter(printer));
    setUMLModifierVisitor(new UMLModifierPrettyPrinter(printer));
    setMCCommonLiteralsVisitor(new MCCommonLiteralsPrettyPrinter(printer));
    setMCSimpleGenericTypesVisitor(new MCSimpleGenericTypesPrettyPrinter(printer));
    setMCBasicTypesVisitor(new MCBasicTypesPrettyPrinter(printer));
    setMCFullGenericTypesVisitor(new MCFullGenericTypesPrettyPrinter(printer));
    setMCCollectionTypesVisitor(new MCCollectionTypesPrettyPrinter(printer));
  }

}
