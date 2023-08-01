package de.monticore;

import de.monticore.od4report._ast.ASTODName;
import de.monticore.od4report._visitor.OD4ReportHandler;
import de.monticore.od4report._visitor.OD4ReportTraverser;
import de.monticore.od4report._visitor.OD4ReportVisitor2;
import de.monticore.prettyprint.IndentPrinter;

public class PlantUMLODReportPrettyPrinter implements OD4ReportVisitor2, OD4ReportHandler {
    private final IndentPrinter printer;
    private OD4ReportTraverser traverser;

    public PlantUMLODReportPrettyPrinter(IndentPrinter printer) {
        this.printer = printer;
    }


    @Override
    public OD4ReportTraverser getTraverser() {
        return traverser;
    }

    @Override
    public void setTraverser(OD4ReportTraverser traverser) {
        this.traverser = traverser;
    }

    @Override
    public void visit(ASTODName node){
        printer.print(node.isPresentName() ? node.getName() : node.getODSpecialName());
    }
}
