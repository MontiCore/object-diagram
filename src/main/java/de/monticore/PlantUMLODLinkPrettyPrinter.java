package de.monticore;

import de.monticore.od4report._ast.ASTODName;
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
    public void handle(ASTODLink node) {
        String symbol = node.isAggregation() ? "o" : node.isComposition() ? "*" : "";
        String linkRepresentation = "--";
        ASTODLinkDirection linkDirection = node.getODLinkDirection();

        if(linkDirection instanceof ASTODLeftToRightDir){
            linkRepresentation = linkRepresentation + (symbol.isEmpty() ? ">" : symbol);
        }
        else if(linkDirection instanceof ASTODRightToLeftDir) {
            linkRepresentation = (symbol.isEmpty() ? "<" : symbol) + linkRepresentation;
        }
        else if(linkDirection instanceof ASTODBiDir) {
            linkRepresentation = (symbol.isEmpty() ? "<" : symbol) + linkRepresentation + (symbol.isEmpty() ? ">" : symbol);
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

    @Override
    public void handle(ASTODLinkLeftSide node) {
        if(node.isPresentRole()) {
            printer.print(" \"" + node.getRole() + "\" ");
        }
    }

    @Override
    public void handle(ASTODLinkRightSide node) {
        if(node.isPresentRole()) {
            printer.print(" \"" + node.getRole() + "\" ");
        }
    }
}
