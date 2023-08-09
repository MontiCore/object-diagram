package de.monticore;

import de.monticore.expressions.expressionsbasis._ast.ASTLiteralExpression;
import de.monticore.expressions.expressionsbasis._ast.ASTNameExpression;
import de.monticore.od4report._ast.ASTODDate;
import de.monticore.odbasis._ast.*;
import de.monticore.odbasis._visitor.ODBasisHandler;
import de.monticore.odbasis._visitor.ODBasisTraverser;
import de.monticore.odbasis._visitor.ODBasisVisitor2;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Driver class for the basis pretty printer implementing the {@link ODBasisVisitor2}, {@link ODBasisHandler}.
 */
public class PlantUMLODBasisPrettyPrinter implements ODBasisVisitor2, ODBasisHandler {
    private final IndentPrinter printer;
    private ODBasisTraverser traverser;
    private Map<ASTODAnonymousObject, UUID> anonymousObjectsNameCache = new HashMap<>();

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
     * @param node ast OD node
     */
    @Override
    public void visit(ASTObjectDiagram node) {
        printer.println("@startuml");
        printer.println("note \"OD\" as tag #white");

    }
    
    /**
     * This method ends visiting the ast.
     * @param node ast OD node
     */
    @Override
    public void endVisit(ASTObjectDiagram node) {
        printer.print("@enduml");
    }
    
    /**
     * This method handles the named ast node by identifying the class types and printing them.
     * @param node named ast node
     */
    @Override
    public void handle(ASTODNamedObject node) {
        List<String> classTypes = ((ASTMCQualifiedType) node.getMCObjectType()).getNameList();
        if(classTypes == null || classTypes.isEmpty()) {
            printer.println(String.format("object \"__%1$s__\" {", node.getName()));
        }
        else {
            printer.println(String.format("object \"__%1$s:%2$s__\" as %1$s {", node.getName(), classTypes.get(0)));
        }
        handleODValuePrettyPrintingByASTType(node.getODAttributeList(), node.getName());
    }

    private void handleODValuePrettyPrintingByASTType(List<ASTODAttribute> odAttributeList, String name) {
        odAttributeList.stream().filter(attr -> attr.getODValue() instanceof ASTODDate ||
                        (attr.getODValue() instanceof ASTODSimpleAttributeValue
                        && ((ASTODSimpleAttributeValue)attr.getODValue()).getExpression() instanceof ASTLiteralExpression))
                .forEach(attr -> attr.accept(getTraverser()));
        printer.println("}");
        odAttributeList.stream().filter(attr -> attr.getODValue() instanceof ASTODSimpleAttributeValue
                        && ((ASTODSimpleAttributeValue)attr.getODValue()).getExpression() instanceof ASTNameExpression)
                .forEach(attr -> printer.println(name + " \"" + attr.getName() + "\" " + ((ASTNameExpression) ((ASTODSimpleAttributeValue) attr.getODValue()).getExpression()).getName()));

        odAttributeList.stream().filter(attr -> attr.getODValue() instanceof ASTODObject)
                .forEach(attr -> {
                    attr.accept(getTraverser());
                    if(attr.getODValue() instanceof ASTODAnonymousObject) {
                        printer.println(name + " \"" + attr.getName() + "\" " + anonymousObjectsNameCache.get(attr.getODValue()));
                    }
                    else {
                        printer.println(name + " \"" + attr.getName() + "\" " + ((ASTODObject) attr.getODValue()).getName());
                    }
                });
    }
    
    /**
     * This method handles the anonymous ast node by identifying the class types and printing them.
     * @param node anonymous ast node
     */
    @Override
    public void handle(ASTODAnonymousObject node) {
        List<String> classTypes = ((ASTMCQualifiedType) node.getMCObjectType()).getNameList();
        if(classTypes != null && !classTypes.isEmpty()){
            throw new RuntimeException("Anonymous objects must have a class type in ODs");
        }
        anonymousObjectsNameCache.putIfAbsent(node, UUID.randomUUID());
        printer.println(String.format("object \"__:%1$s__\" as %2$s {", classTypes.get(0), anonymousObjectsNameCache.get(node)));
        handleODValuePrettyPrintingByASTType(node.getODAttributeList(), node.getName());
    }
    
    /**
     * This method ends visiting the anonymous ast node.
     * @param node anonymous ast node
     */
    @Override
    public void endVisit(ASTODAnonymousObject node) {
        printer.println("}");
    }
    
    /**
     * This method starts visiting the attribute ast node.
     * @param node attribute ast node
     */
    @Override
    public void visit(ASTODAttribute node) {
        if(node.getODValue() instanceof ASTODSimpleAttributeValue || node.getODValue() instanceof ASTODDate) {
            printer.indent();
            printer.print(node.getName());
            if (node.isPresentODValue()) {
                printer.print(" = ");
            }
        }
    }
    
    /**
     * This method ends visiting the attribute ast node.
     * @param node attribute ast node
     */
    @Override
    public void endVisit(ASTODAttribute node) {
        if(node.getODValue() instanceof ASTODSimpleAttributeValue || node.getODValue() instanceof ASTODDate) {
            printer.println();
            printer.unindent();
        }
    }
    
    /**
     * This method starts visiting the named ast node.
     * @param node named ast node
     */
    @Override
    public void visit(ASTODName node) {
        printer.print(node.getName());
    }
}
