// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

package de.monticore.od4data.prettyprinter;

import de.monticore.MCCommonLiteralsPrettyPrinter;
import de.monticore.expressions.prettyprint.CommonExpressionsPrettyPrinter;
import de.monticore.expressions.prettyprint.ExpressionsBasisPrettyPrinter;
import de.monticore.od4data._ast.ASTOD4DataNode;
import de.monticore.od4data._visitor.OD4DataDelegatorVisitor;
import de.monticore.odattribute.prettyprinter.ODAttributePrettyPrinter;
import de.monticore.odbasis._ast.ASTODBasisNode;
import de.monticore.odbasis.prettyprinter.ODBasisPrettyPrinter;
import de.monticore.odlink.prettyprinter.ODLinkPrettyPrinter;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.prettyprint.UMLModifierPrettyPrinter;
import de.monticore.prettyprint.UMLStereotypePrettyPrinter;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;
import de.monticore.types.prettyprint.MCCollectionTypesPrettyPrinter;
import de.monticore.types.prettyprint.MCFullGenericTypesPrettyPrinter;
import de.monticore.types.prettyprint.MCSimpleGenericTypesPrettyPrinter;

public class OD4DataPrettyPrinterDelegator extends OD4DataDelegatorVisitor {

  protected OD4DataPrettyPrinterDelegator realThis = this;

  protected IndentPrinter printer;

  public OD4DataPrettyPrinterDelegator() {
    this.printer = new IndentPrinter();
    init();
  }

  public OD4DataPrettyPrinterDelegator(IndentPrinter indentPrinter) {
    this.printer = indentPrinter;
    init();
  }

  public String prettyprint(ASTOD4DataNode astod4DataNode) {
    getPrinter().clearBuffer();
    astod4DataNode.accept(getRealThis());
    return getPrinter().getContent();
  }

  public String prettyprint(ASTODBasisNode astodBasisNode) {
    getPrinter().clearBuffer();
    astodBasisNode.accept(getRealThis());
    return getPrinter().getContent();
  }

  protected IndentPrinter getPrinter() {
    return this.printer;
  }

  private void init() {
    realThis = this;

    setODBasisVisitor(new ODBasisPrettyPrinter(printer));
    setODLinkVisitor(new ODLinkPrettyPrinter(printer));
    setODAttributeVisitor(new ODAttributePrettyPrinter(printer));

    setMCBasicTypesVisitor(new MCBasicTypesPrettyPrinter(printer));
    setMCCommonLiteralsVisitor(new MCCommonLiteralsPrettyPrinter(printer));
    setUMLStereotypeVisitor(new UMLStereotypePrettyPrinter(printer));
    setUMLModifierVisitor(new UMLModifierPrettyPrinter(printer));
    setCommonExpressionsVisitor(new CommonExpressionsPrettyPrinter(printer));
    setExpressionsBasisVisitor(new ExpressionsBasisPrettyPrinter(printer));

    setMCSimpleGenericTypesVisitor(new MCSimpleGenericTypesPrettyPrinter(printer));
    setMCCollectionTypesVisitor(new MCCollectionTypesPrettyPrinter(printer));
    setMCFullGenericTypesVisitor(new MCFullGenericTypesPrettyPrinter(printer));
  }

}
