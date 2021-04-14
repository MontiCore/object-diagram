// (c) https://github.com/MontiCore/monticore

package de.monticore.od4data.prettyprinter;

import de.monticore.expressions.prettyprint.CommonExpressionsPrettyPrinter;
import de.monticore.expressions.prettyprint.ExpressionsBasisPrettyPrinter;
import de.monticore.literals.prettyprint.MCCommonLiteralsPrettyPrinter;
import de.monticore.od.prettyprinter.ODFullPrettyPrinter;
import de.monticore.od4data.OD4DataMill;
import de.monticore.od4data._ast.ASTOD4DataNode;
import de.monticore.od4data._visitor.OD4DataTraverser;
import de.monticore.odattribute._ast.ASTODAttributeNode;
import de.monticore.odattribute.prettyprinter.ODAttributePrettyPrinter;
import de.monticore.odbasis._ast.ASTODBasisNode;
import de.monticore.odbasis.prettyprinter.ODBasisPrettyPrinter;
import de.monticore.odlink._ast.ASTODLink;
import de.monticore.odlink._ast.ASTODLinkNode;
import de.monticore.odlink.prettyprinter.ODLinkPrettyPrinter;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.prettyprint.UMLModifierPrettyPrinter;
import de.monticore.prettyprint.UMLStereotypePrettyPrinter;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;

public class OD4DataFullPrettyPrinter extends ODFullPrettyPrinter {

  protected OD4DataTraverser traverser = OD4DataMill.traverser();

  public OD4DataFullPrettyPrinter() {
    super();
    init();
  }

  public OD4DataFullPrettyPrinter(IndentPrinter printer) {
    super(printer);
    init();
  }

  public String prettyprint(ASTOD4DataNode astod4DataNode) {
    getPrinter().clearBuffer();
    astod4DataNode.accept(traverser);
    return getPrinter().getContent();
  }

  public String prettyprint(ASTODBasisNode astodBasisNode) {
    getPrinter().clearBuffer();
    astodBasisNode.accept(traverser);
    return getPrinter().getContent();
  }

  public String prettyprint(ASTODLinkNode astodLinkNode) {
    getPrinter().clearBuffer();
    astodLinkNode.accept(traverser);
    return getPrinter().getContent();
  }

  // has to be done due to ambiguity issues
  public String prettyprint(ASTODLink astodLink) {
    getPrinter().clearBuffer();
    astodLink.accept(traverser);
    return getPrinter().getContent();
  }

  public String prettyprint(ASTODAttributeNode ASTODAttributeNode) {
    getPrinter().clearBuffer();
    ASTODAttributeNode.accept(traverser);
    return getPrinter().getContent();
  }

  protected IndentPrinter getPrinter() {
    return this.printer;
  }

  protected void init() {
    // ods
    traverser.setODBasisHandler(new ODBasisPrettyPrinter(printer));
    traverser.setODLinkHandler(new ODLinkPrettyPrinter(printer));
    traverser.setODAttributeHandler(new ODAttributePrettyPrinter(printer));

    // uml modifier
    UMLModifierPrettyPrinter umlModifier = new UMLModifierPrettyPrinter(printer);
    traverser.add4UMLModifier(umlModifier);
    traverser.setUMLModifierHandler(umlModifier);

    MCCommonLiteralsPrettyPrinter commonLiterals = new MCCommonLiteralsPrettyPrinter(printer);
    traverser.add4MCCommonLiterals(commonLiterals);
    traverser.setMCCommonLiteralsHandler(commonLiterals);

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

    // basic types
    MCBasicTypesPrettyPrinter basicTypes = new MCBasicTypesPrettyPrinter(printer);
    traverser.setMCBasicTypesHandler(basicTypes);
    traverser.add4MCBasicTypes(basicTypes);
  }

}
