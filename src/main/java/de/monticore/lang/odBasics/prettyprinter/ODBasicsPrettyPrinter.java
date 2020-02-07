// (c) https://github.com/MontiCore/monticore

package de.monticore.lang.odbasics.prettyprinter;

import de.monticore.lang.odbasics._ast.*;
import de.monticore.lang.odbasics._visitor.ODBasicsVisitor;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.mcbasictypes._ast.ASTMCImportStatement;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;
import de.se_rwth.commons.Names;

import java.util.Iterator;

/**
 * This class is responsible for pretty-printing object diagrams. It is implemented using the
 * Visitor pattern. The Visitor pattern traverses a tree in depth first, the visit and
 * ownVisit-methods are called when a node is traversed, the endVisit methods are called when the
 * whole subtree of a node has been traversed. The ownVisit-Methods stop the automatic traversal
 * order and allow to explictly visit subtrees by calling getVisitor().startVisit(ASTNode)
 *
 * @author Martin Schindler, Robert Heim, Steffen Hillemacher
 */
public class ODBasicsPrettyPrinter extends MCBasicTypesPrettyPrinter implements ODBasicsVisitor {

  // printer to use
  protected IndentPrinter printer;

  /**
   * Constructor.
   *
   * @param printer the printer to write to.
   */
  public ODBasicsPrettyPrinter(IndentPrinter printer) {
    super(printer);
    this.printer = printer;
  }

  /**
   * Return current {@link IndentPrinter}.
   *
   * @return current printer
   */
  public IndentPrinter getPrinter() {
    return printer;
  }

  /**
   * Prints the compilation unit of an object diagram (start of the pretty print)
   *
   * @param unit a OD compilation unit
   */
  @Override
  public void handle(ASTODArtifact unit) {
    if (unit.getPackageList() != null && !unit.getPackageList().isEmpty()) {
      getPrinter().println("package " + Names.getQualifiedName(unit.getPackageList()) + ";\n");
    }
    if (unit.getMCImportStatementList() != null && !unit.getMCImportStatementList().isEmpty()) {
      for (ASTMCImportStatement s : unit.getMCImportStatementList()) {
        s.accept(getRealThis());
      }
      getPrinter().println();
    }
    if (unit.getODConformsStatementList() != null && !unit.getODConformsStatementList().isEmpty()) {
      for (ASTODConformsStatement s : unit.getODConformsStatementList()) {
        getPrinter().print("conformsTo " + s.getMCQualifiedName().toString() + ";");
      }
    }
    unit.getObjectDiagram().accept(getRealThis());
  }

  /**
   * Prints the object diagram definition
   *
   * @param a object diagram definition
   */
  @Override
  public void handle(ASTObjectDiagram a) {
    // print stereotype
    if (a.isPresentStereotype()) {
      a.getStereotype().accept(getRealThis());
      getPrinter().println();
    }
    // print object diagram name and parameters
    getPrinter().print("objectdiagram " + a.getName());
    // print body
    getPrinter().println(" {\n");
    getPrinter().indent();
    // print Objects

    for (Iterator<ASTODObject> it = a.getODObjectList().iterator(); it.hasNext(); ) {
      it.next().accept(getRealThis());
      getPrinter().println(";");
      if (it.hasNext()) {
        getPrinter().println();
      }
    }
    for (ASTODLink l : a.getODLinkList()) {
      l.accept(getRealThis());
      getPrinter().print(";");
    }
    getPrinter().println();
    getPrinter().unindent();
    getPrinter().println("}");
  }

  /**
   * Prints an object in an object diagram
   *
   * @param a object
   */
  @Override
  public void handle(ASTODObject a) {

    if (a.isPresentModifier()) {
      a.getModifier().accept(getRealThis());
    }
    // print object name and type
    if (a.isPresentODName()) {
      a.getODName().accept(getRealThis());
    }
    if (a.getMCQualifiedType() != null) {
      getPrinter().print(":");
      a.getMCQualifiedType().accept(getRealThis());
    }
    getPrinter().print("{");

    // print object body
    if (!a.getODAttributeList().isEmpty()) {
      getPrinter().println();
      getPrinter().indent();
      for (ASTODAttribute ast : a.getODAttributeList()) {
        ast.accept(getRealThis());
      }
      getPrinter().unindent();
    }
    getPrinter().print("}");
  }

  /**
   * Prints an attribute of an object in an object diagram
   *
   * @param a attribute
   */
  @Override
  public void handle(ASTODAttribute a) {
    // print modifier
    if (a.isPresentModifier())
      a.getModifier().accept(getRealThis());
    // print type
    if (a.isPresentMCType()) {
      a.getMCType().accept(getRealThis());
      getPrinter().print(" ");
    }
    // print name
    getPrinter().print(a.getName());

    if (a.isPresentComplete() || a.isPresentODValue() || a.isPresentODMap() || a
        .isPresentODList()) {
      if (a.isPresentComplete()) {
        getPrinter().print(" = ");
      }
      else {
        getPrinter().print(" -> ");
      }

      // print value
      if (a.isPresentODValue()) {
        a.getODValue().accept(getRealThis());
      }
      // print value collection
      else if (a.isPresentODList()) {
        a.getODList().accept(getRealThis());
      }
      // print value map
      else if (a.isPresentODMap()) {
        a.getODMap().accept(getRealThis());
      }

    }

    getPrinter().println(";");
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

  /**
   * @see de.monticore.lang.odbasics._visitor.ODBasicsVisitor#handle(de.monticore.lang.odbasics._ast.ASTODValue)
   */
  @Override
  public void handle(ASTODValue a) {
    a.accept(getRealThis());
  }

  /**
   * @see de.monticore.lang.odbasics._visitor.ODBasicsVisitor#handle(de.monticore.lang.odbasics._ast.ASTODName)
   */
  @Override
  public void handle(ASTODName a) {
    getPrinter().print(a.getName());
  }

  /**
   * @see de.monticore.lang.odbasics._visitor.ODBasicsVisitor#handle(de.monticore.lang.odbasics._ast.ASTODLiteral)
   */
  @Override
  public void handle(ASTODLiteral astodSignedLiteral) {
    astodSignedLiteral.getLiteral().accept(getRealThis());
  }

  @Override
  public void handle(ASTODExpression astodExpression) {
    astodExpression.getExpression().accept(getRealThis());
  }

  /**
   * @see de.monticore.lang.odbasics._visitor.ODBasicsVisitor#handle(de.monticore.lang.odbasics._ast.ASTODAbsent)
   */
  @Override
  public void handle(ASTODAbsent a) {
    if (a.is_absent()) {
      getPrinter().print("absent");
    }
  }

  /**
   * Prints a qualifier of a link in an object diagram
   *
   * @param a qualifier of a link
   */
  @Override
  public void handle(ASTODLinkQualifier a) {
    if (a.isPresentName()) {
      getPrinter().print("[[");
      getPrinter().print(a.getName());
      getPrinter().print("]]");
    }
    else if (a.isPresentODValue()) {
      getPrinter().print("[");
      a.getODValue().accept(getRealThis());
      getPrinter().print("]");
    }
    getPrinter().print(" ");
  }

  /**
   * Prints an association, aggregation or composition in an object diagram
   *
   * @param a association, aggregation or composition
   */
  @Override
  public void handle(ASTODLink a) {
    getPrinter().println();
    // print stereotype
    if (a.isPresentStereotype()) {
      a.accept(getRealThis());
      getPrinter().print(" ");
    }
    // print type of the link
    if (a.getLinkForm() == ASTLinkForm.LINK) {
      getPrinter().print("link ");
    }
    else if (a.getLinkForm() == ASTLinkForm.AGGREGATION) {
      getPrinter().print("aggregation ");
    }
    else if (a.getLinkForm() == ASTLinkForm.COMPOSITION) {
      getPrinter().print("composition ");
    }
    // print name
    if (a.isDerived()) {
      getPrinter().print("/");
    }
    if (a.isPresentName()) {
      getPrinter().print(a.getName() + " ");
    }
    // print left modifier
    if (a.isPresentLeftModifier()) {
      a.getLeftModifier().accept(getRealThis());
    }
    // print objects referenced on the left side of the link
    Iterator<ASTODName> refNames = a.getLeftReferenceNameList().iterator();
    while (refNames.hasNext()) {
      refNames.next().accept(getRealThis());
      if (refNames.hasNext()) {
        getPrinter().print(", ");
      }
    }
    getPrinter().print(" ");
    // print left qualifier
    if (a.isPresentLeftQualifier()) {
      a.getLeftQualifier().accept(getRealThis());
    }
    // print left role
    if (a.isPresentLeftRole()) {
      getPrinter().print("(");
      getPrinter().print(a.getLeftRole());
      getPrinter().print(") ");
    }
    // print arrow
    if (a.getAssocDirection() == ASTAssocDirection.LEFTTORIGHT) {
      getPrinter().print("->");
    }
    else if (a.getAssocDirection() == ASTAssocDirection.RIGHTTOLEFT) {
      getPrinter().print("<-");
    }
    else if (a.getAssocDirection() == ASTAssocDirection.BIDIRECTIONAL) {
      getPrinter().print("<->");
    }
    else if (a.getAssocDirection() == ASTAssocDirection.UNSPECIFIED) {
      getPrinter().print("--");
    }
    // print right role
    if (a.isPresentRightRole()) {
      getPrinter().print(" (");
      getPrinter().print(a.getRightRole());
      getPrinter().print(")");
    }
    // print right qualifier
    if (a.isPresentRightQualifier()) {
      a.getRightQualifier().accept(getRealThis());
    }
    // print objects referenced on the right side of the link
    getPrinter().print(" ");
    refNames = a.getRightReferenceNameList().iterator();
    while (refNames.hasNext()) {
      refNames.next().accept(getRealThis());
      if (refNames.hasNext()) {
        getPrinter().print(", ");
      }
    }
    // print right modifier
    if (a.isPresentRightModifier()) {
      a.getRightModifier().accept(getRealThis());
    }
  }

  private ODBasicsVisitor realThis = this;

  /**
   * @see de.monticore.lang.odbasics._visitor.ODBasicsVisitor#setRealThis(de.monticore.lang.odbasics._visitor.ODBasicsVisitor)
   */
  @Override
  public void setRealThis(ODBasicsVisitor realThis) {
    this.realThis = realThis;
  }

  /**
   * @see de.monticore.lang.odbasics._visitor.ODBasicsVisitor#getRealThis()
   */
  @Override
  public ODBasicsVisitor getRealThis() {
    return realThis;
  }

}