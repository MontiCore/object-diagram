// (c) https://github.com/MontiCore/monticore

package de.monticore.odbasis.prettyprinter;

import de.monticore.odbasis._ast.*;
import de.monticore.odbasis._visitor.ODBasisInheritanceVisitor;
import de.monticore.odbasis._visitor.ODBasisVisitor;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.prettyprint.MCBasicsPrettyPrinter;
import de.monticore.types.mcbasictypes._ast.ASTMCImportStatement;

import java.util.Iterator;

/**
 * This class is responsible for pretty-printing object diagrams. It is implemented using the
 * Visitor pattern. The Visitor pattern traverses a tree in depth first, the visit and
 * ownVisit-methods are called when a node is traversed, the endVisit methods are called when the
 * whole subtree of a node has been traversed. The ownVisit-Methods stop the automatic traversal
 * order and allow to explicitly visit subtrees by calling getVisitor().startVisit(ASTNode)
 */
public class ODBasisPrettyPrinter extends MCBasicsPrettyPrinter
    implements ODBasisInheritanceVisitor {

  // printer to use
  protected IndentPrinter printer;

  /**
   * Constructor.
   *
   * @param printer the printer to write to.
   */
  public ODBasisPrettyPrinter(IndentPrinter printer) {
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
    if (unit.isPresentMCPackageDeclaration() && !unit.getMCPackageDeclaration().getMCQualifiedName()
        .getQName().isEmpty()) {
      getPrinter().println("package " + unit.getMCPackageDeclaration().getMCQualifiedName().getQName() + ";\n");
    }

    if (unit.getMCImportStatementList() != null && !unit.getMCImportStatementList().isEmpty()) {
      for (ASTMCImportStatement s : unit.getMCImportStatementList()) {
        s.accept(getRealThis());
      }
      getPrinter().println();
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

    // print elements
    for (Iterator<ASTODElement> it = a.getODElementList().iterator(); it.hasNext(); ) {
      it.next().accept(getRealThis());
      getPrinter().println(";");
      if (it.hasNext()) {
        getPrinter().println();
      }
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
  public void visit(ASTODObject a) {

    if (a.isPresentModifier()) {
      a.getModifier().accept(getRealThis());
    }
    // print object name and type
    getPrinter().print(a.getName());
    if (a.getMCObjectType() != null) {
      getPrinter().print(":");
      a.getMCObjectType().accept(getRealThis());
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

  @Override
  public void traverse(ASTODNamedObject a) {
  }

  @Override
  public void traverse(ASTODAnonymousObject a) {
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

    if (a.isPresentComplete() || a.isPresentODValue()) {
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
    }

    getPrinter().println(";");
  }

  /**
   * @see de.monticore.odbasis._visitor.ODBasisVisitor#handle(de.monticore.odbasis._ast.ASTODValue)
   */
  @Override
  public void handle(ASTODValue a) {
    a.accept(getRealThis());
  }

  /**
   * @see de.monticore.odbasis._visitor.ODBasisVisitor#handle(de.monticore.odbasis._ast.ASTODName)
   */
  @Override
  public void handle(ASTODName a) {
    getPrinter().print(a.getName());
  }

  /**
   * @see de.monticore.odbasis._visitor.ODBasisVisitor#handle(de.monticore.odbasis._ast.ASTODSimpleAttributeValue)
   */
  @Override
  public void handle(ASTODSimpleAttributeValue astodSimpleAttributeValue) {
    astodSimpleAttributeValue.getExpression().accept(getRealThis());
  }

  /**
   * @see de.monticore.odbasis._visitor.ODBasisVisitor#handle(de.monticore.odbasis._ast.ASTODAbsent)
   */
  @Override
  public void handle(ASTODAbsent a) {
    getPrinter().print("...");
  }

  private ODBasisVisitor realThis = this;

  /**
   * @see de.monticore.odbasis._visitor.ODBasisVisitor#setRealThis(de.monticore.odbasis._visitor.ODBasisVisitor)
   */
  @Override
  public void setRealThis(ODBasisVisitor realThis) {
    this.realThis = realThis;
  }

  /**
   * @see de.monticore.odbasis._visitor.ODBasisVisitor#getRealThis()
   */
  @Override
  public ODBasisVisitor getRealThis() {
    return realThis;
  }

}
