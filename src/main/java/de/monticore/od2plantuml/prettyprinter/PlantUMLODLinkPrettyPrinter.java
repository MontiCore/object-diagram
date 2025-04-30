package de.monticore.od2plantuml.prettyprinter;

import de.monticore.odbasis._visitor.ODBasisHandler;
import de.monticore.odbasis._visitor.ODBasisVisitor2;
import de.monticore.odlink.ODLinkMill;
import de.monticore.odlink._ast.*;
import de.monticore.odlink._util.IODLinkTypeDispatcher;
import de.monticore.odlink._visitor.ODLinkHandler;
import de.monticore.odlink._visitor.ODLinkTraverser;
import de.monticore.odlink._visitor.ODLinkVisitor2;
import de.monticore.prettyprint.IndentPrinter;

/**
 * Driver class for the link pretty printer implementing the {@link ODBasisVisitor2},
 * {@link ODBasisHandler}.
 */
public class PlantUMLODLinkPrettyPrinter implements ODLinkVisitor2, ODLinkHandler {
  
  private final IndentPrinter printer;
  private ODLinkTraverser traverser;
  
  public PlantUMLODLinkPrettyPrinter(IndentPrinter printer) {
    this.printer = printer;
  }
  
  @Override
  public ODLinkTraverser getTraverser() {
    return traverser;
  }
  
  @Override
  public void setTraverser(ODLinkTraverser traverser) {
    this.traverser = traverser;
  }
  
  @Override
  public void handle(ASTODLink node) {
    String linkRepresentation = getLinkRepresentation(node);
    
    node.getODLinkLeftSide().getReferenceNamesList().forEach(leftRef -> {
      node.getODLinkRightSide().getReferenceNamesList().forEach(rightRef -> {
        leftRef.accept(getTraverser());
        
        node.getODLinkLeftSide().accept(getTraverser());
        printer.print(linkRepresentation);
        node.getODLinkRightSide().accept(getTraverser());
        
        rightRef.accept(getTraverser());
        
        if (node.isPresentName()) {
          printer.print(" : " + node.getName());
        }
        
        printer.println();
      });
    });
  }
  
  protected String getLinkRepresentation(ASTODLink node) {
    String symbol = node.isAggregation() ? "o" : node.isComposition() ? "*" : "";
    String linkRepresentation = "--";
    ASTODLinkDirection linkDirection = node.getODLinkDirection();
    
    IODLinkTypeDispatcher typeDispatcher = ODLinkMill.typeDispatcher();
    
    if (typeDispatcher.isODLinkASTODLeftToRightDir(linkDirection)) {
      if (!symbol.isEmpty()) {
        linkRepresentation = symbol + linkRepresentation;
      }
      linkRepresentation = linkRepresentation + ">";
    }
    else if (typeDispatcher.isODLinkASTODRightToLeftDir(linkDirection)) {
      if (!symbol.isEmpty()) {
        linkRepresentation = linkRepresentation + symbol;
      }
      linkRepresentation = "<" + linkRepresentation;
    }
    else if (typeDispatcher.isODLinkASTODBiDir(linkDirection)) {
      linkRepresentation =
          (symbol.isEmpty() ? "<" : symbol) + linkRepresentation + (symbol.isEmpty() ? ">"
                                                                                     : symbol);
    }
    return " " + linkRepresentation + " ";
  }
  
  @Override
  public void handle(ASTODLinkLeftSide node) {
    if (node.isPresentRole()) {
      printer.print(" \"" + node.getRole() + "\" ");
    }
  }
  
  @Override
  public void handle(ASTODLinkRightSide node) {
    if (node.isPresentRole()) {
      printer.print(" \"" + node.getRole() + "\" ");
    }
  }
}