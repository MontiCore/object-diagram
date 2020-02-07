// (c) https://github.com/MontiCore/monticore

package de.monticore.lang.odbasics.prettyprinter;

import de.monticore.MCCommonLiteralsPrettyPrinter;
import de.monticore.MCJavaLiteralsPrettyPrinter;
import de.monticore.lang.odbasics._ast.ASTODBasicsNode;
import de.monticore.lang.odbasics._visitor.ODBasicsDelegatorVisitor;
import de.monticore.literals.mccommonliterals._ast.ASTMCCommonLiteralsNode;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.prettyprint.JavaLightPrettyPrinter;
import de.monticore.prettyprint.UMLModifierPrettyPrinter;
import de.monticore.prettyprint.UMLStereotypePrettyPrinter;
import de.monticore.types.mcbasictypes._ast.ASTMCBasicTypesNode;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;
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

  public String prettyprint(ASTMCBasicTypesNode astmcBasicTypesNode) {
    getPrinter().clearBuffer();
    astmcBasicTypesNode.accept(getRealThis());
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

  private void init() {
    realThis = this;
    setODBasicsVisitor(new ODBasicsPrettyPrinter(printer));
    setMCBasicTypesVisitor(new MCBasicTypesPrettyPrinter(printer));
    setUMLStereotypeVisitor(new UMLStereotypePrettyPrinter(printer));
    setUMLModifierVisitor(new UMLModifierPrettyPrinter(printer));
    setMCCommonLiteralsVisitor(new MCCommonLiteralsPrettyPrinter(printer));
    setMCJavaLiteralsVisitor(new MCJavaLiteralsPrettyPrinter(printer));
  }

}
