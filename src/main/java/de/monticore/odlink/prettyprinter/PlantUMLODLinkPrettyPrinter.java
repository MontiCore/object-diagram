package de.monticore.odlink.prettyprinter;

import de.monticore.odbasis._visitor.ODBasisHandler;
import de.monticore.odbasis._visitor.ODBasisVisitor2;
import de.monticore.odlink._ast.*;
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
  
  /**
   * This method handles the link ast node by visiting the left and right side of the link.
   *
   * @param node link ast node
   */
  @Override
  public void handle(ASTODLink node) {
    String symbol = node.isAggregation() ? "o" : node.isComposition() ? "*" : "";
    String linkRepresentation = "--";
    ASTODLinkDirection linkDirection = node.getODLinkDirection();
    
    if (linkDirection instanceof ASTODLeftToRightDir) {
      linkRepresentation = linkRepresentation + (symbol.isEmpty() ? ">" : symbol);
    }
    else if (linkDirection instanceof ASTODRightToLeftDir) {
      linkRepresentation = (symbol.isEmpty() ? "<" : symbol) + linkRepresentation;
    }
    else if (linkDirection instanceof ASTODBiDir) {
      linkRepresentation = (symbol.isEmpty() ? "<" : symbol) + linkRepresentation +
          (symbol.isEmpty() ? ">" : symbol);
    }
    
    String finalLinkRepresentation = linkRepresentation;
    
    node.getODLinkLeftSide().getReferenceNamesList().forEach(leftRef -> {
      node.getODLinkRightSide().getReferenceNamesList().forEach(rightRef -> {
        leftRef.accept(getTraverser());
        
        node.getODLinkLeftSide().accept(getTraverser());
        printer.print(finalLinkRepresentation);
        node.getODLinkRightSide().accept(getTraverser());
        
        rightRef.accept(getTraverser());
        printer.println();
      });
    });
  }
  
  /**
   * This method handles the left side of ast node
   *
   * @param node left linked ast node
   */
  @Override
  public void handle(ASTODLinkLeftSide node) {
    if (node.isPresentRole()) {
      printer.print(" \"" + node.getRole() + "\" ");
    }
  }
  
  /**
   * This method handles the right side of ast node
   *
   * @param node right linked ast node
   */
  @Override
  public void handle(ASTODLinkRightSide node) {
    if (node.isPresentRole()) {
      printer.print(" \"" + node.getRole() + "\" ");
    }
  }
}
