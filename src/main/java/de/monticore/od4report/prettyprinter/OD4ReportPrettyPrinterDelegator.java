// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report.prettyprinter;

import de.monticore.MCCommonLiteralsPrettyPrinter;
import de.monticore.dateliterals._ast.ASTDateLiteralsNode;
import de.monticore.dateliterals.prettyprinter.DateLiteralsPrettyPrinter;
import de.monticore.literals.mccommonliterals._ast.ASTMCCommonLiteralsNode;
import de.monticore.od4report._ast.ASTOD4ReportNode;
import de.monticore.od4report._visitor.OD4ReportDelegatorVisitor;
import de.monticore.odbasics._ast.ASTODBasicsNode;
import de.monticore.odbasics.prettyprinter.ODBasicsPrettyPrinter;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.prettyprint.UMLModifierPrettyPrinter;
import de.monticore.prettyprint.UMLStereotypePrettyPrinter;
import de.monticore.types.mcbasictypes._ast.ASTMCType;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;
import de.monticore.types.prettyprint.MCCollectionTypesPrettyPrinter;
import de.monticore.types.prettyprint.MCSimpleGenericTypesPrettyPrinter;
import de.monticore.umlmodifier._ast.ASTUMLModifierNode;
import de.monticore.umlstereotype._ast.ASTUMLStereotypeNode;

public class OD4ReportPrettyPrinterDelegator extends OD4ReportDelegatorVisitor {

  protected OD4ReportDelegatorVisitor realThis = this;

  protected IndentPrinter printer;

  public OD4ReportPrettyPrinterDelegator() {
    this.printer = new IndentPrinter();
    init();
  }

  public OD4ReportPrettyPrinterDelegator(IndentPrinter indentPrinter) {
    this.printer = indentPrinter;
    init();
  }

  protected IndentPrinter getPrinter() {
    return this.printer;
  }

  public String prettyprint(ASTOD4ReportNode astod4ReportNode) {
    getPrinter().clearBuffer();
    astod4ReportNode.accept(getRealThis());
    return getPrinter().getContent();
  }

  public String prettyprint(ASTDateLiteralsNode astDateLiteralsNode) {
    getPrinter().clearBuffer();
    astDateLiteralsNode.accept(getRealThis());
    return getPrinter().getContent();
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

  private void init() {
    realThis = this;
    setOD4ReportVisitor(new OD4ReportPrettyPrinter(printer));
    setODBasicsVisitor(new ODBasicsPrettyPrinter(printer));
    setDateLiteralsVisitor(new DateLiteralsPrettyPrinter(printer));
    setMCBasicTypesVisitor(new MCBasicTypesPrettyPrinter(printer));
    setUMLStereotypeVisitor(new UMLStereotypePrettyPrinter(printer));
    setUMLModifierVisitor(new UMLModifierPrettyPrinter(printer));
    setMCCommonLiteralsVisitor(new MCCommonLiteralsPrettyPrinter(printer));
    setMCCollectionTypesVisitor(new MCCollectionTypesPrettyPrinter(printer));
    setMCSimpleGenericTypesVisitor(new MCSimpleGenericTypesPrettyPrinter(printer));
  }

}
