// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

package de.monticore.odbasis.prettyprinter;

import de.monticore.MCCommonLiteralsPrettyPrinter;
import de.monticore.expressions.prettyprint.CommonExpressionsPrettyPrinter;
import de.monticore.literals.mccommonliterals._ast.ASTMCCommonLiteralsNode;
import de.monticore.odbasis._ast.ASTODBasisNode;
import de.monticore.odbasis._visitor.ODBasisDelegatorVisitor;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.prettyprint.UMLModifierPrettyPrinter;
import de.monticore.prettyprint.UMLStereotypePrettyPrinter;
import de.monticore.types.mcbasictypes._ast.ASTMCType;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;
import de.monticore.umlmodifier._ast.ASTUMLModifierNode;
import de.monticore.umlstereotype._ast.ASTUMLStereotypeNode;

public class ODBasisPrettyPrinterDelegator extends ODBasisDelegatorVisitor {

  protected ODBasisDelegatorVisitor realThis = this;

  protected IndentPrinter printer;

  public ODBasisPrettyPrinterDelegator() {
    this.printer = new IndentPrinter();
    init();
  }

  public ODBasisPrettyPrinterDelegator(IndentPrinter indentPrinter) {
    this.printer = indentPrinter;
    init();
  }

  protected IndentPrinter getPrinter() {
    return this.printer;
  }

  public String prettyprint(ASTODBasisNode astodBasicsNode) {
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

  private void init() {
    realThis = this;
    setODBasisVisitor(new ODBasisPrettyPrinter(printer));
    setUMLStereotypeVisitor(new UMLStereotypePrettyPrinter(printer));
    setUMLModifierVisitor(new UMLModifierPrettyPrinter(printer));
    setMCCommonLiteralsVisitor(new MCCommonLiteralsPrettyPrinter(printer));
    setExpressionsBasisVisitor(new CommonExpressionsPrettyPrinter(printer));
    setMCBasicTypesVisitor(new MCBasicTypesPrettyPrinter(printer));
  }

}
