// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report.prettyprinter;

import de.monticore.dateliterals._ast.ASTDateLiteralsNode;
import de.monticore.dateliterals.prettyprinter.DateLiteralsPrettyPrinter;
import de.monticore.expressions.prettyprint.CommonExpressionsPrettyPrinter;
import de.monticore.expressions.prettyprint.ExpressionsBasisPrettyPrinter;
import de.monticore.literals.mccommonliterals._ast.ASTMCCommonLiteralsNode;
import de.monticore.literals.prettyprint.MCCommonLiteralsPrettyPrinter;
import de.monticore.od4report.OD4ReportMill;
import de.monticore.od4report._ast.ASTOD4ReportNode;
import de.monticore.od4report._visitor.OD4ReportTraverser;
import de.monticore.odattribute._ast.ASTODAttributeNode;
import de.monticore.odattribute.prettyprinter.ODAttributePrettyPrinter;
import de.monticore.odbasis._ast.ASTODBasisNode;
import de.monticore.odbasis.prettyprinter.ODBasisPrettyPrinter;
import de.monticore.odlink._ast.ASTODLinkNode;
import de.monticore.odlink.prettyprinter.ODLinkPrettyPrinter;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.prettyprint.UMLModifierPrettyPrinter;
import de.monticore.prettyprint.UMLStereotypePrettyPrinter;
import de.monticore.types.mcbasictypes._ast.ASTMCType;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;
import de.monticore.types.prettyprint.MCCollectionTypesPrettyPrinter;
import de.monticore.types.prettyprint.MCFullGenericTypesPrettyPrinter;
import de.monticore.types.prettyprint.MCSimpleGenericTypesPrettyPrinter;
import de.monticore.umlmodifier._ast.ASTUMLModifierNode;
import de.monticore.umlstereotype._ast.ASTUMLStereotypeNode;

public class OD4ReportFullPrettyPrinter {
  
  protected OD4ReportTraverser traverser = OD4ReportMill.traverser();
  
  protected IndentPrinter printer;
  
  public OD4ReportFullPrettyPrinter() {
    this.printer = new IndentPrinter();
    init();
  }
  
  public OD4ReportFullPrettyPrinter(IndentPrinter indentPrinter) {
    this.printer = indentPrinter;
    init();
  }
  
  protected IndentPrinter getPrinter() {
    return this.printer;
  }
  
  public String prettyprint(ASTOD4ReportNode astod4ReportNode) {
    getPrinter().clearBuffer();
    astod4ReportNode.accept(traverser);
    return getPrinter().getContent();
  }
  
  public String prettyprint(ASTDateLiteralsNode astDateLiteralsNode) {
    getPrinter().clearBuffer();
    astDateLiteralsNode.accept(traverser);
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
  
  public String prettyprint(ASTODAttributeNode ASTODAttributeNode) {
    getPrinter().clearBuffer();
    ASTODAttributeNode.accept(traverser);
    return getPrinter().getContent();
  }
  
  public String prettyprint(ASTMCType astmcType) {
    getPrinter().clearBuffer();
    astmcType.accept(traverser);
    return getPrinter().getContent();
  }
  
  public String prettyprint(ASTUMLStereotypeNode astumlStereotypeNode) {
    getPrinter().clearBuffer();
    astumlStereotypeNode.accept(traverser);
    return getPrinter().getContent();
  }
  
  public String prettyprint(ASTUMLModifierNode astumlModifierNode) {
    getPrinter().clearBuffer();
    astumlModifierNode.accept(traverser);
    return getPrinter().getContent();
  }
  
  public String prettyprint(ASTMCCommonLiteralsNode astmcCommonLiteralsNode) {
    getPrinter().clearBuffer();
    astmcCommonLiteralsNode.accept(traverser);
    return getPrinter().getContent();
  }
  
  private void init() {
    // ods
    traverser.setODBasisHandler(new ODBasisPrettyPrinter(printer));
    traverser.setOD4ReportHandler(new OD4ReportPrettyPrinter(printer));
    traverser.setODLinkHandler(new ODLinkPrettyPrinter(printer));
    traverser.setODAttributeHandler(new ODAttributePrettyPrinter(printer));
    
    // date literals
    traverser.setDateLiteralsHandler(new DateLiteralsPrettyPrinter(printer));
    
    MCCommonLiteralsPrettyPrinter commonLiterals = new MCCommonLiteralsPrettyPrinter(printer);
    traverser.add4MCCommonLiterals(commonLiterals);
    traverser.setMCCommonLiteralsHandler(commonLiterals);
    
    // uml modifier
    UMLModifierPrettyPrinter umlModifier = new UMLModifierPrettyPrinter(printer);
    traverser.add4UMLModifier(umlModifier);
    traverser.setUMLModifierHandler(umlModifier);
    
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
    
    // full generic types
    MCFullGenericTypesPrettyPrinter fullGenericTypes = new MCFullGenericTypesPrettyPrinter(printer);
    traverser.setMCFullGenericTypesHandler(fullGenericTypes);
    traverser.add4MCFullGenericTypes(fullGenericTypes);
    
    MCSimpleGenericTypesPrettyPrinter simpleGenericTypes = new MCSimpleGenericTypesPrettyPrinter(printer);
    traverser.setMCSimpleGenericTypesHandler(simpleGenericTypes);
    traverser.add4MCSimpleGenericTypes(simpleGenericTypes);
    
    MCCollectionTypesPrettyPrinter collectionTypes = new MCCollectionTypesPrettyPrinter(printer);
    traverser.setMCCollectionTypesHandler(collectionTypes);
    traverser.add4MCCollectionTypes(collectionTypes);
    
    MCBasicTypesPrettyPrinter basicTypes = new MCBasicTypesPrettyPrinter(printer);
    traverser.setMCBasicTypesHandler(basicTypes);
    traverser.add4MCBasicTypes(basicTypes);
  }
  
}
