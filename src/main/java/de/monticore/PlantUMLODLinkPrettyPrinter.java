package de.monticore;

import de.monticore.odlink._ast.*;
import de.monticore.odlink._visitor.ODLinkHandler;
import de.monticore.odlink._visitor.ODLinkTraverser;
import de.monticore.odlink._visitor.ODLinkVisitor2;
import de.monticore.prettyprint.IndentPrinter;

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
    public void visit(ASTODLink node) {
        node.getODLinkLeftSide().accept(getTraverser());
        String symbol = node.isAggregation() ? "o" : node.isComposition() ? "*" : "";
        String linkRepresentation = "--";
        ASTODLinkDirection linkDirection = node.getODLinkDirection();

        if(linkDirection instanceof ASTODLeftToRightDir){
            linkRepresentation = linkRepresentation + symbol;
        }
        else if(linkDirection instanceof ASTODRightToLeftDir) {
            linkRepresentation = symbol + linkRepresentation;
        }
        else if(linkDirection instanceof ASTODBiDir) {
            linkRepresentation = symbol + linkRepresentation + symbol;
        }

        printer.print(linkRepresentation);

        node.getODLinkRightSide().accept(getTraverser());
    }

    @Override
    public void visit(ASTODLinkLeftSide node) {
        printer.print(node.getReferenceNamesList());
    }

    @Override
    public void visit(ASTODLinkRightSide node) {
        printer.print(node.getReferenceNamesList());
    }
}
