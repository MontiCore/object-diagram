/* (c) https://github.com/MontiCore/monticore */
package de.monticore.od2plantuml.prettyprinter;

import de.monticore.odattribute._ast.ASTODList;
import de.monticore.odattribute._ast.ASTODMap;
import de.monticore.odattribute._ast.ASTODMapElement;
import de.monticore.odattribute._visitor.ODAttributeHandler;
import de.monticore.odattribute._visitor.ODAttributeTraverser;
import de.monticore.odbasis._ast.ASTODValue;
import de.monticore.prettyprint.IndentPrinter;

import java.util.List;

/**
 * Pretty prints lists and maps as attribute values, e.g., {@code [1, 2]} and {@code [1 -> 2]}.
 */
public class PlantUMLODAttributePrettyPrinter implements ODAttributeHandler {

  private final IndentPrinter printer;
  private ODAttributeTraverser traverser;

  public PlantUMLODAttributePrettyPrinter(IndentPrinter printer) {
    this.printer = printer;
  }

  @Override
  public ODAttributeTraverser getTraverser() {
    return traverser;
  }

  @Override
  public void setTraverser(ODAttributeTraverser traverser) {
    this.traverser = traverser;
  }

  @Override
  public void handle(ASTODList node) {
    List<ASTODValue> values = node.getODValueList();
    printer.print("[");
    for (int i = 0; i < values.size(); i++) {
      if (i > 0) {
        printer.stripTrailing();
        printer.print(", ");
      }
      values.get(i).accept(getTraverser());
    }
    printer.stripTrailing();
    printer.print("]");
  }

  @Override
  public void handle(ASTODMap node) {
    List<ASTODMapElement> elements = node.getODMapElementList();
    printer.print("[");
    for (int i = 0; i < elements.size(); i++) {
      if (i > 0) {
        printer.stripTrailing();
        printer.print(", ");
      }
      elements.get(i).accept(getTraverser());
    }
    printer.stripTrailing();
    printer.print("]");
  }

  @Override
  public void handle(ASTODMapElement node) {
    node.getKey().accept(getTraverser());
    printer.stripTrailing();
    printer.print(" -> ");
    node.getVal().accept(getTraverser());
  }
}
