// (c) https://github.com/MontiCore/monticore

package de.monticore.odattribute.prettyprinter;

import de.monticore.odattribute._ast.ASTODList;
import de.monticore.odattribute._ast.ASTODMap;
import de.monticore.odattribute._ast.ASTODMapElement;
import de.monticore.odattribute._visitor.ODAttributeHandler;
import de.monticore.odattribute._visitor.ODAttributeTraverser;
import de.monticore.odbasis._ast.ASTODValue;
import de.monticore.prettyprint.IndentPrinter;

import java.util.Iterator;

public class ODAttributePrettyPrinter implements ODAttributeHandler {

  protected IndentPrinter printer;

  protected ODAttributeTraverser traverser;

  public ODAttributePrettyPrinter(IndentPrinter printer) {
    this.printer = printer;
  }

  @Override
  public void handle(ASTODList a) {
    getPrinter().print("[");
    for (Iterator<ASTODValue> it = a.getODValueList().iterator(); it.hasNext(); ) {
      it.next().accept(getTraverser());
      if (it.hasNext()) {
        getPrinter().print(",");
      }
    }
    getPrinter().print("]");
  }

  @Override
  public void handle(ASTODMap a) {
    getPrinter().print("[");
    for (Iterator<ASTODMapElement> it = a.getODMapElementList().iterator(); it.hasNext(); ) {
      it.next().accept(getTraverser());
      if (it.hasNext()) {
        getPrinter().print(",");
      }
    }
    getPrinter().print("]");
  }

  @Override
  public void handle(ASTODMapElement a) {
    a.getKey().accept(getTraverser());
    getPrinter().print(" -> ");
    a.getVal().accept(getTraverser());
  }

  @Override
  public ODAttributeTraverser getTraverser() {
    return traverser;
  }

  @Override
  public void setTraverser(ODAttributeTraverser traverser) {
    this.traverser = traverser;
  }

  public IndentPrinter getPrinter() {
    return printer;
  }

}
