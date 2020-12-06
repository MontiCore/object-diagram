// (c) https://github.com/MontiCore/monticore

package de.monticore.odbasis.prettyprinter;

import de.monticore.odbasis._ast.*;
import de.monticore.odbasis._visitor.ODBasisHandler;
import de.monticore.odbasis._visitor.ODBasisTraverser;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.mcbasictypes._ast.ASTMCImportStatement;

import java.util.Iterator;

/**
 * This class is responsible for pretty-printing object diagrams. It is implemented using the
 * Visitor pattern. The Visitor pattern traverses a tree in depth first, the visit and
 * ownVisit-methods are called when a node is traversed, the endVisit methods are called when the
 * whole subtree of a node has been traversed. The ownVisit-Methods stop the automatic traversal
 * order and allow to explicitly visit subtrees by calling getVisitor().startVisit(ASTNode)
 */
public class ODBasisPrettyPrinter implements ODBasisHandler {

  protected IndentPrinter printer;

  protected ODBasisTraverser traverser;

  public ODBasisPrettyPrinter(IndentPrinter printer) {
    this.printer = printer;
  }

  public IndentPrinter getPrinter() {
    return printer;
  }

  @Override
  public void handle(ASTODArtifact unit) {
    if (unit.isPresentMCPackageDeclaration() && !unit.getMCPackageDeclaration().getMCQualifiedName()
        .getQName().isEmpty()) {
      getPrinter().println("package " + unit.getMCPackageDeclaration().getMCQualifiedName().getQName() + ";\n");
    }

    if (unit.getMCImportStatementList() != null && !unit.getMCImportStatementList().isEmpty()) {
      for (ASTMCImportStatement s : unit.getMCImportStatementList()) {
        s.accept(getTraverser());
      }
      getPrinter().println();
    }

    unit.getObjectDiagram().accept(getTraverser());
  }

  @Override
  public void handle(ASTObjectDiagram a) {
    // print stereotype
    if (a.isPresentStereotype()) {
      a.getStereotype().accept(getTraverser());
      getPrinter().println();
    }
    // print object diagram name and parameters
    getPrinter().print("objectdiagram " + a.getName());
    // print body
    getPrinter().println(" {\n");
    getPrinter().indent();

    // print elements
    for (Iterator<ASTODElement> it = a.getODElementList().iterator(); it.hasNext(); ) {
      it.next().accept(getTraverser());
      getPrinter().println(";");
      if (it.hasNext()) {
        getPrinter().println();
      }
    }
    getPrinter().println();
    getPrinter().unindent();
    getPrinter().println("}");
  }

  @Override
  public void handle(ASTODObject a) {

    if (a.isPresentModifier()) {
      a.getModifier().accept(getTraverser());
    }
    // print object name and type
    getPrinter().print(a.getName());
    if (a.getMCObjectType() != null) {
      getPrinter().print(":");
      a.getMCObjectType().accept(getTraverser());
    }
    getPrinter().print("{");

    // print object body
    if (!a.getODAttributeList().isEmpty()) {
      getPrinter().println();
      getPrinter().indent();
      for (ASTODAttribute ast : a.getODAttributeList()) {
        ast.accept(getTraverser());
      }
      getPrinter().unindent();
    }
    getPrinter().print("}");
  }

  @Override
  public void handle(ASTODNamedObject a) {
    getTraverser().handle((ASTODObject) a);
  }

  @Override
  public void handle(ASTODAnonymousObject a) {
    getTraverser().handle((ASTODObject) a);
  }

  @Override
  public void handle(ASTODAttribute a) {
    // print modifier
    if (a.isPresentModifier())
      a.getModifier().accept(getTraverser());
    // print type
    if (a.isPresentMCType()) {
      a.getMCType().accept(getTraverser());
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
        a.getODValue().accept(getTraverser());
      }
    }

    getPrinter().println(";");
  }

  @Override
  public void handle(ASTODValue a) {
    a.accept(getTraverser());
  }

  @Override
  public void handle(ASTODName a) {
    getPrinter().print(a.getName());
  }

  @Override
  public void handle(ASTODSimpleAttributeValue astodSimpleAttributeValue) {
    astodSimpleAttributeValue.getExpression().accept(getTraverser());
  }

  @Override
  public void handle(ASTODAbsent a) {
    getPrinter().print("...");
  }

  @Override
  public ODBasisTraverser getTraverser() {
    return traverser;
  }

  @Override
  public void setTraverser(ODBasisTraverser traverser) {
    this.traverser = traverser;
  }

}
