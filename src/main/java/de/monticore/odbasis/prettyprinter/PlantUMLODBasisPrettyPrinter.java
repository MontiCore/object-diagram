package de.monticore.odbasis.prettyprinter;

import de.monticore.expressions.expressionsbasis._ast.ASTLiteralExpression;
import de.monticore.expressions.expressionsbasis._ast.ASTNameExpression;
import de.monticore.od4report._ast.ASTODDate;
import de.monticore.odbasis._ast.*;
import de.monticore.odbasis._visitor.ODBasisHandler;
import de.monticore.odbasis._visitor.ODBasisTraverser;
import de.monticore.odbasis._visitor.ODBasisVisitor2;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedType;
import de.monticore.types.mcbasictypes._prettyprint.MCBasicTypesFullPrettyPrinter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Driver class for the basis pretty printer implementing the {@link ODBasisVisitor2},
 * {@link ODBasisHandler}.
 */
public class PlantUMLODBasisPrettyPrinter implements ODBasisVisitor2, ODBasisHandler {
  
  private final IndentPrinter printer;
  private ODBasisTraverser traverser;
  private final Map<ASTODAnonymousObject, UUID> anonymousObjectsNameCache = new HashMap<>();
  
  public PlantUMLODBasisPrettyPrinter(IndentPrinter printer) {
    this.printer = printer;
  }
  
  @Override
  public ODBasisTraverser getTraverser() {
    return traverser;
  }
  
  @Override
  public void setTraverser(ODBasisTraverser traverser) {
    this.traverser = traverser;
  }
  
  /**
   * This method starts visiting the ast.
   *
   * @param node ast OD node
   */
  @Override
  public void visit(ASTObjectDiagram node) {
    printer.println("@startuml");
    
    // prints the model tag
    printer.println("note \"OD\" as tag #white");
  }
  
  /*public void visit(ASTODNamedObject node) {
    var typesPrinter = new MCBasicTypesFullPrettyPrinter(new IndentPrinter());
    var printedType = typesPrinter.prettyprint(node.getMCObjectType());
    printer.println(
        String.format("object \"__%1$s:%2$s__\" as %1$s {", node.getName(), printedType));
  }
  
  public void endVisit(ASTODNamedObject node) {
    printer.println("}");
  }*/
  
  /**
   * This method ends visiting the ast.
   *
   * @param node ast OD node
   */
  @Override
  public void endVisit(ASTObjectDiagram node) {
    printer.print("@enduml");
  }
  
  /**
   * This method handles the named ast node by identifying the class types and printing them.
   *
   * @param node named ast node
   */
  @Override
  public void handle(ASTODNamedObject node) {
    List<String> classTypes = ((ASTMCQualifiedType) node.getMCObjectType()).getNameList();
    if (classTypes == null || classTypes.isEmpty()) {
      printer.println(String.format("object \"__%1$s__\" {", node.getName()));
    }
    else {
      var typesPrinter = new MCBasicTypesFullPrettyPrinter(new IndentPrinter());
      var printedType = typesPrinter.prettyprint(node.getMCObjectType());
      printer.println(
          String.format("object \"__%1$s:%2$s__\" as %1$s {", node.getName(), printedType));
    }
    handleODValuePrettyPrintingByASTType(node.streamODAttributes(), node.getName());
  }
  
  private void handleODValuePrettyPrintingByASTType(Stream<ASTODAttribute> odAttributeList,
      String name) {
    var attributesWithValues =
        odAttributeList.filter(ASTODAttribute::isPresentODValue).collect(Collectors.toList());
    attributesWithValues.stream().filter(attr -> {
          return attr.getODValue() instanceof ASTODDate || (
              attr.getODValue() instanceof ASTODSimpleAttributeValue &&
                  ((ASTODSimpleAttributeValue) attr.getODValue()).getExpression() instanceof ASTLiteralExpression
          );
        })
        .forEach(attr -> attr.accept(getTraverser()));
    printer.println("}");
    attributesWithValues.stream().filter(attr -> attr.getODValue() instanceof ASTODSimpleAttributeValue
            &&
            ((ASTODSimpleAttributeValue) attr.getODValue()).getExpression() instanceof ASTNameExpression)
        .forEach(attr -> printer.println(name + " \"" + attr.getName() + "\" " +
            ((ASTNameExpression) ((ASTODSimpleAttributeValue) attr.getODValue()).getExpression()).getName()));
    
    attributesWithValues.stream().filter(attr -> attr.getODValue() instanceof ASTODObject)
        .forEach(attr -> {
          attr.accept(getTraverser());
          if (attr.getODValue() instanceof ASTODAnonymousObject) {
            printer.println(name + " \"" + attr.getName() + "\" " +
                anonymousObjectsNameCache.get(attr.getODValue()));
          }
          else {
            printer.println(name + " \"" + attr.getName() + "\" " +
                ((ASTODObject) attr.getODValue()).getName());
          }
        });
  }
  
  /**
   * This method handles the anonymous ast node by identifying the class types and printing them.
   *
   * @param node anonymous ast node
   */
  @Override
  public void handle(ASTODAnonymousObject node) {
    var typesPrinter = new MCBasicTypesFullPrettyPrinter(new IndentPrinter());
    var classType = typesPrinter.prettyprint(node.getMCObjectType());
    anonymousObjectsNameCache.putIfAbsent(node, UUID.randomUUID());
    printer.println(String.format("object \"__:%1$s__\" as %2$s {", classType,
        anonymousObjectsNameCache.get(node)));
    handleODValuePrettyPrintingByASTType(node.streamODAttributes(), node.getName());
  }
  
  /**
   * This method ends visiting the anonymous ast node.
   *
   * @param node anonymous ast node
   */
  @Override
  public void endVisit(ASTODAnonymousObject node) {
    printer.println("}");
  }
  
  /**
   * This method starts visiting the attribute ast node.
   *
   * @param node attribute ast node
   */
  @Override
  public void visit(ASTODAttribute node) {
    if (node.getODValue() instanceof ASTODSimpleAttributeValue ||
        node.getODValue() instanceof ASTODDate) {
      printer.indent();
      printer.print(node.getName());
      if (node.isPresentODValue()) {
        printer.print(" = ");
      }
    }
  }
  
  /**
   * This method ends visiting the attribute ast node.
   *
   * @param node attribute ast node
   */
  @Override
  public void endVisit(ASTODAttribute node) {
    if (node.getODValue() instanceof ASTODSimpleAttributeValue ||
        node.getODValue() instanceof ASTODDate) {
      printer.println();
      printer.unindent();
    }
  }
  
  /**
   * This method starts visiting the named ast node.
   *
   * @param node named ast node
   */
  @Override
  public void visit(ASTODName node) {
    printer.print(node.getName());
  }
}
