/*
 (c) https://github.com/MontiCore/monticore
 */

package de.monticore.odbasis.prettyprinter;

import de.monticore.expressions.prettyprint.CommonExpressionsPrettyPrinter;
import de.monticore.expressions.prettyprint.ExpressionsBasisPrettyPrinter;
import de.monticore.literals.prettyprint.MCCommonLiteralsPrettyPrinter;
import de.monticore.od.prettyprinter.ODFullPrettyPrinter;
import de.monticore.odbasis.ODBasisMill;
import de.monticore.odbasis._ast.ASTODBasisNode;
import de.monticore.odbasis._visitor.ODBasisTraverser;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.prettyprint.UMLModifierPrettyPrinter;
import de.monticore.prettyprint.UMLStereotypePrettyPrinter;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;

public class ODBasisFullPrettyPrinter extends ODFullPrettyPrinter {

  protected ODBasisTraverser traverser = ODBasisMill.traverser();

  public ODBasisFullPrettyPrinter() {
    super();
    init();
  }

  public ODBasisFullPrettyPrinter(IndentPrinter printer) {
    super(printer);
    init();
  }

  public String prettyprint(ASTODBasisNode astodBasisNode) {
    getPrinter().clearBuffer();
    astodBasisNode.accept(traverser);
    return getPrinter().getContent();
  }

  protected void init() {
    // ods
    traverser.setODBasisHandler(new ODBasisPrettyPrinter(printer));

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
