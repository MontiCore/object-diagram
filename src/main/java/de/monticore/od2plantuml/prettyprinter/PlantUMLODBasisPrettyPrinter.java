package de.monticore.od2plantuml.prettyprinter;

import de.monticore.odbasis._ast.*;
import de.monticore.odbasis._visitor.ODBasisHandler;
import de.monticore.odbasis._visitor.ODBasisTraverser;
import de.monticore.odbasis._visitor.ODBasisVisitor2;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.mcbasictypes._prettyprint.MCBasicTypesFullPrettyPrinter;

import java.util.*;

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
  
  @Override
  public void visit(ASTObjectDiagram node) {
    printer.println("@startuml");
    
    // prints the model tag
    printer.println("note \"OD\" as tag #white");
  }
  
  @Override
  public void visit(ASTODNamedObject node) {
    var typesPrinter = new MCBasicTypesFullPrettyPrinter(new IndentPrinter());
    var printedType = typesPrinter.prettyprint(node.getMCObjectType());
    printer.println(
        String.format("object \"__%1$s:%2$s__\" as %1$s {", node.getName(), printedType));
  }
  
  @Override
  public void visit(ASTODAnonymousObject node) {
    var typesPrinter = new MCBasicTypesFullPrettyPrinter(new IndentPrinter());
    var printedType = typesPrinter.prettyprint(node.getMCObjectType());
    this.anonymousObjectsNameCache.putIfAbsent(node, UUID.randomUUID());
    String nodeName = this.anonymousObjectsNameCache.get(node).toString();
    printer.println(
        String.format("object \"__%1$s:%2$s__\" as %1$s {", nodeName, printedType));
  }
  
  @Override
  public void endVisit(ASTODNamedObject node) {
    printer.println("}");
  }
  
  @Override
  public void endVisit(ASTODAnonymousObject node) {
    printer.println("}");
  }
  
  @Override
  public void visit(ASTODAttribute node) {
      printer.indent();
      printer.print(node.getName());
      if (node.isPresentODValue()) {
        printer.print(" = ");
      }
  }

  @Override
  public void endVisit(ASTODAttribute node) {
      printer.println();
      printer.unindent();
  }
  
  @Override
  public void endVisit(ASTObjectDiagram node) {
    printer.print("@enduml");
  }
  
  @Override
  public void visit(ASTODName node) {
    printer.print(node.getName());
  }
}
