// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

package de.monticore.odattribute.prettyprinter;

import de.monticore.odattribute._ast.ASTODList;
import de.monticore.odattribute._ast.ASTODMap;
import de.monticore.odattribute._ast.ASTODMapElement;
import de.monticore.odattribute._visitor.ODAttributeVisitor;
import de.monticore.odbasis._ast.ASTODValue;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.prettyprint.MCBasicsPrettyPrinter;

import java.util.Iterator;

public class ODAttributePrettyPrinter extends MCBasicsPrettyPrinter
    implements ODAttributeVisitor {

  public ODAttributePrettyPrinter(IndentPrinter printer) {
    super(printer);
  }

  /**
   * Prints an ODValueList in an object diagram
   *
   * @param a ASTODValueList
   */
  @Override
  public void handle(ASTODList a) {
    getPrinter().print("[");
    for (Iterator<ASTODValue> it = a.getODValueList().iterator(); it.hasNext(); ) {
      it.next().accept(getRealThis());
      if (it.hasNext()) {
        getPrinter().print(",");
      }
    }
    getPrinter().print("]");
  }

  /**
   * Prints an ODValueMap in an object diagram
   *
   * @param a ASTODValueMap
   */
  @Override
  public void handle(ASTODMap a) {
    getPrinter().print("[");
    for (Iterator<ASTODMapElement> it = a.getODMapElementList().iterator(); it.hasNext(); ) {
      it.next().accept(getRealThis());
      if (it.hasNext()) {
        getPrinter().print(",");
      }
    }
    getPrinter().print("]");
  }

  @Override
  public void handle(ASTODMapElement a) {
    a.getKey().accept(getRealThis());
    getPrinter().print(" -> ");
    a.getVal().accept(getRealThis());
  }

  private ODAttributeVisitor realThis = this;

  /**
   * @see de.monticore.odattribute._visitor.ODAttributeVisitor#setRealThis(de.monticore.odattribute._visitor.ODAttributeVisitor)
   */
  @Override
  public void setRealThis(ODAttributeVisitor realThis) {
    this.realThis = realThis;
  }

  /**
   * @see de.monticore.odlink._visitor.ODLinkVisitor#getRealThis()
   */
  @Override
  public ODAttributeVisitor getRealThis() {
    return realThis;
  }

}
