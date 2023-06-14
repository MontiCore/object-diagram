package de.monticore;

import de.monticore.odbasis._ast.ASTODAnonymousObject;
import de.monticore.odbasis._ast.ASTODAttribute;
import de.monticore.odbasis._ast.ASTODNamedObject;
import de.monticore.odbasis._ast.ASTObjectDiagram;
import de.monticore.odbasis._visitor.ODBasisHandler;
import de.monticore.odbasis._visitor.ODBasisTraverser;
import de.monticore.odbasis._visitor.ODBasisVisitor2;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedType;

import java.util.List;

public class PlantUMLODBasisPrettyPrinter implements ODBasisVisitor2, ODBasisHandler {
    private final IndentPrinter printer;
    private ODBasisTraverser traverser;

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
        printer.println("note \"OD\" as tag #white");

    }

    @Override
    public void endVisit(ASTObjectDiagram node) {
        printer.print("@enduml");
    }

    @Override
    public void visit(ASTODNamedObject node) {
        List<String> classTypes = ((ASTMCQualifiedType) node.getMCObjectType()).getNameList();
        if(classTypes == null || classTypes.isEmpty()) {
            printer.println(String.format("object \"__%1$s__\" {", node.getName()));
        }
        else {
            printer.println(String.format("object \"__%1$s:%2$s__\" as %1$s {", node.getName(), classTypes.get(0)));
        }
    }

    @Override
    public void endVisit(ASTODNamedObject node) {
        printer.println("}");
    }

    @Override
    public void visit(ASTODAnonymousObject node) {
        List<String> classTypes = ((ASTMCQualifiedType) node.getMCObjectType()).getNameList();
        assert !(classTypes == null || classTypes.isEmpty()); // Anonymous objects must have a class type in ODs
        printer.println(String.format("object \"__:%1$s__\" as %1$s {", classTypes.get(0)));
    }

    @Override
    public void endVisit(ASTODAnonymousObject node) {
        printer.println("}");
    }

    @Override
    public void visit(ASTODAttribute node) {
        printer.indent();
        printer.print(node.getName());
        if(node.isPresentODValue()) {
            printer.print(" = ");
        }
    }

    @Override
    public void endVisit(ASTODAttribute node) {
        printer.println();
        printer.unindent();
    }
}
