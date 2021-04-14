/*
 (c) https://github.com/MontiCore/monticore
 */

package de.monticore.odlink.prettyprinter;

import de.monticore.expressions.prettyprint.CommonExpressionsPrettyPrinter;
import de.monticore.expressions.prettyprint.ExpressionsBasisPrettyPrinter;
import de.monticore.literals.prettyprint.MCCommonLiteralsPrettyPrinter;
import de.monticore.od.prettyprinter.ODFullPrettyPrinter;
import de.monticore.odbasis.prettyprinter.ODBasisPrettyPrinter;
import de.monticore.odlink.ODLinkMill;
import de.monticore.odlink._ast.ASTODLink;
import de.monticore.odlink._ast.ASTODLinkNode;
import de.monticore.odlink._visitor.ODLinkTraverser;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.prettyprint.UMLModifierPrettyPrinter;
import de.monticore.prettyprint.UMLStereotypePrettyPrinter;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;

public class ODLinkFullPrettyPrinter extends ODFullPrettyPrinter {

  protected ODLinkTraverser traverser = ODLinkMill.traverser();

  public ODLinkFullPrettyPrinter() {
    super();
    init();
  }

  public ODLinkFullPrettyPrinter(IndentPrinter printer) {
    super(printer);
    init();
  }

  // has to be done due to ambiguity issues
  public String prettyprint(ASTODLink astodLink) {
    getPrinter().clearBuffer();
    astodLink.accept(traverser);
    return getPrinter().getContent();
  }

  public String prettyprint(ASTODLinkNode astodLinkNode) {
    getPrinter().clearBuffer();
    astodLinkNode.accept(traverser);
    return getPrinter().getContent();
  }

  protected void init() {
    // ods
    traverser.setODBasisHandler(new ODBasisPrettyPrinter(printer));
    traverser.setODLinkHandler(new ODLinkPrettyPrinter(printer));

    // mccommons
    MCCommonLiteralsPrettyPrinter commonLiterals = new MCCommonLiteralsPrettyPrinter(printer);
    traverser.add4MCCommonLiterals(commonLiterals);
    traverser.setMCCommonLiteralsHandler(commonLiterals);

    // uml modifier
    UMLModifierPrettyPrinter umlModifier = new UMLModifierPrettyPrinter(printer);
    traverser.add4UMLModifier(umlModifier);
    traverser.setUMLModifierHandler(umlModifier);

    // uml stereotype
    UMLStereotypePrettyPrinter umlStereotype = new UMLStereotypePrettyPrinter(printer);
    traverser.add4UMLStereotype(umlStereotype);
    traverser.setUMLStereotypeHandler(umlStereotype);

    // common expressions
    CommonExpressionsPrettyPrinter commonExpressions = new CommonExpressionsPrettyPrinter(printer);
    traverser.setCommonExpressionsHandler(commonExpressions);
    traverser.add4CommonExpressions(commonExpressions);
    ExpressionsBasisPrettyPrinter basicExpression = new ExpressionsBasisPrettyPrinter(printer);
    traverser.setExpressionsBasisHandler(basicExpression);
    traverser.add4ExpressionsBasis(basicExpression);

    // mc basictypes
    MCBasicTypesPrettyPrinter basicTypes = new MCBasicTypesPrettyPrinter(printer);
    traverser.setMCBasicTypesHandler(basicTypes);
    traverser.add4MCBasicTypes(basicTypes);
  }

}
