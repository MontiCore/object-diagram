/*
 * ******************************************************************************
 * MontiCore Language Workbench, www.monticore.de
 * Copyright (c) 2017, MontiCore, All rights reserved.
 *
 * This project is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this project. If not, see <http://www.gnu.org/licenses/>.
 * ******************************************************************************
 */

package de.monticore.lang.od.prettyprint;

import de.monticore.common.prettyprint.CommonPrettyPrinterConcreteVisitor;
import de.monticore.lang.od._ast.*;
import de.monticore.lang.od._visitor.ODVisitor;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.types._ast.ASTImportStatement;
import de.se_rwth.commons.Names;

import java.util.Iterator;

/**
 * This class is responsible for pretty-printing object diagrams. It is implemented using the
 * Visitor pattern. The Visitor pattern traverses a tree in depth first, the visit and
 * ownVisit-methods are called when a node is traversed, the endVisit methods are called when the
 * whole subtree of a node has been traversed. The ownVisit-Methods stop the automatic traversal
 * order and allow to explictly visit subtrees by calling getVisitor().startVisit(ASTNode)
 *
 * @author Martin Schindler, Robert Heim
 */
public class ODPrettyPrinterConcreteVisitor extends CommonPrettyPrinterConcreteVisitor implements
    ODVisitor {

  /**
   * Constructor.
   *
   * @param printer the printer to write to.
   */
  public ODPrettyPrinterConcreteVisitor(IndentPrinter printer) {
    super(printer);
  }

  /**
   * Prints the compilation unit of an object diagram (start of the pretty print)
   *
   * @param unit a OD compilation unit
   */
  @Override
  public void handle(ASTODArtifact unit) {
    if (unit.getPackage() != null && !unit.getPackage().isEmpty()) {
      getPrinter()
          .println("package " + Names.getQualifiedName(unit.getPackage()) + ";\n");
    }
    if (unit.getImportStatements() != null && !unit.getImportStatements().isEmpty()) {
      for (ASTImportStatement s : unit.getImportStatements()) {
        getPrinter().print("import " + Names.getQualifiedName(s.getImportList()));
        if (s.isStar()) {
          getPrinter().println(".*;");
        }
        else {
          getPrinter().println(";");
        }
      }
      getPrinter().println();
    }
    if (unit.getODConformsStatements() != null && !unit.getODConformsStatements().isEmpty()) {
      for (ASTODConformsStatement s : unit.getODConformsStatements()) {
        getPrinter()
            .print("conformsTo " + s.getQualifiedName().toString() + ";");
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
    if (a.getStereotype().isPresent()) {
      a.getStereotype().get().accept(getRealThis());
      getPrinter().println();
    }
    // print object diagram name and parameters
    getPrinter().print("objectdiagram " + a.getName());
    // print body
    getPrinter().println(" {");
    getPrinter().indent();
    // print Objects

    for (Iterator<ASTODObject> it = a.getODObjects().iterator(); it.hasNext(); ) {
      it.next().accept(getRealThis());
      getPrinter().print(";");
      if (it.hasNext()) {
        getPrinter().println();
      }
    }
    for (Iterator<ASTODLink> it = a.getODLinks().iterator(); it.hasNext(); ) {
      it.next().accept(getRealThis());
      getPrinter().print(";");
      if (it.hasNext()) {
        getPrinter().println();
      }
    }
    getPrinter().unindent();
    getPrinter().println();
    getPrinter().println("}");
  }

  /**
   * Prints an object in an object diagram
   *
   * @param a object
   */
  @Override
  public void handle(ASTODObject a) {

    if (a.getModifier().isPresent()) {
      a.getModifier().get().accept(getRealThis());
    }
    // print object name and type
    if (a.getODName().isPresent()) {
      a.getODName().get().accept(getRealThis());
    }
    if (a.getReferenceType() != null) {
      getPrinter().print(":");
      a.getReferenceType().accept(getRealThis());
    }
    getPrinter().println("{");
    getPrinter().indent();

    // print object body
    if (!a.getODAttributes().isEmpty()) {
      for (Iterator<ASTODAttribute> it = a.getODAttributes().iterator(); it.hasNext(); ) {
        it.next().accept(getRealThis());
        if (it.hasNext()) {
          getPrinter().println();
        }
      }
    }

    getPrinter().unindent();
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
    if (a.getModifier().isPresent())
      a.getModifier().get().accept(getRealThis());
    // print type
    if (a.getType().isPresent()) {
      a.getType().get().accept(getRealThis());
      getPrinter().print(" ");
    }
    // print name
    getPrinter().print(a.getName());

    if (a.completeIsPresent() || a.getODValue().isPresent() || a.getODMap().isPresent() || a
        .getODList().isPresent()) {
      if (a.completeIsPresent()) {
        getPrinter().print(" = ");
      }
      else {
        getPrinter().print(" -> ");
      }

      // print value
      if (a.getODValue().isPresent()) {
        a.getODValue().get().accept(getRealThis());
      }
      // print value collection
      else if (a.getODList().isPresent()) {
        a.getODList().get().accept(getRealThis());
      }
      // print value map
      else if (a.getODMap().isPresent()) {
        a.getODMap().get().accept(getRealThis());
      }

    }

    getPrinter().print(";");
  }

  /**
   * Prints an ODValueList in an object diagram
   *
   * @param a ASTODValueList
   */
  @Override
  public void handle(ASTODList a) {
    getPrinter().print("[");
    for (Iterator<ASTODValue> it = a.getODValues().iterator(); it.hasNext(); ) {
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
    for (Iterator<ASTODMapElement> it = a.getODMapElements().iterator(); it.hasNext(); ) {
      it.next().accept(getRealThis());
      if (it.hasNext()) {
        getPrinter().print(",");
      }
    }
    getPrinter().print("]");
  }

  @Override public void handle(ASTODMapElement a) {
    a.getKey().accept(getRealThis());
    getPrinter().print(" -> ");
    a.getVal().accept(getRealThis());
  }

  /**
   * @see de.monticore.lang.od._visitor.ODVisitor#handle(de.monticore.lang.od._ast.ASTODValue)
   */
  @Override
  public void handle(ASTODValue a) {
    a.accept(getRealThis());
  }

  @Override
  public void handle(ASTODName a) {
    if (a.getName().isPresent()) {
      getPrinter().print(a.getName().get());
    }
    else if (a.getODSpecialName().isPresent()) {
      getPrinter().print(a.getODSpecialName().get());
    }
  }

  @Override
  public void handle(ASTODDate a) {
    printODDate(a);
  }

  @Override
  public void handle(ASTODDateV1 a) {
    printODDate(a);
  }

  @Override
  public void handle(ASTODDateV2 a) {
    printODDate(a);
  }

  @Override
  public void handle(ASTODDateV3 a) {
    printODDate(a);
  }

  private void printODDate(ASTODDate a){
    getPrinter().print(a.getYear().getValue());
    getPrinter().print("-");
    getPrinter().print(a.getMonth().getValue());
    getPrinter().print("-");
    getPrinter().print(a.getDay().getValue());
    getPrinter().print(" ");
    getPrinter().print(a.getHour().getValue());
    getPrinter().print(":");
    getPrinter().print(a.getMinute().getValue());
    getPrinter().print(":");
    getPrinter().print(a.getSecond().getValue());
  }

  /**
   * @see de.monticore.lang.od._visitor.ODVisitor#handle(de.monticore.lang.od._ast.ASTODAbsent)
   */
  @Override public void handle(ASTODAbsent a) {
    if (a.isAbsent()) {
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
    if (a.getName().isPresent()) {
      getPrinter().print("[[");
      getPrinter().print(a.getName().get());
      getPrinter().print("]]");
    }
    else if (a.getODValue().isPresent()) {
      getPrinter().print("[");
      a.getODValue().get().accept(getRealThis());
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
    if (a.getStereotype().isPresent()) {
      a.getStereotype().get().accept(getRealThis());
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
    if (a.getName().isPresent()) {
      getPrinter().print(a.getName().get() + " ");
    }
    // print left modifier
    if (a.getLeftModifier().isPresent()) {
      a.getLeftModifier().get().accept(getRealThis());
    }
    // print objects referenced on the left side of the link
    Iterator<ASTODName> refNames = a.getLeftReferenceNames().iterator();
    while (refNames.hasNext()) {
      refNames.next().accept(getRealThis());
      if (refNames.hasNext()) {
        getPrinter().print(", ");
      }
    }
    getPrinter().print(" ");
    // print left qualifier
    if (a.getLeftQualifier().isPresent()) {
      a.getLeftQualifier().get().accept(getRealThis());
    }
    // print left role
    if (a.getLeftRole().isPresent()) {
      getPrinter().print("(");
      getPrinter().print(a.getLeftRole().get());
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
    if (a.getRightRole().isPresent()) {
      getPrinter().print(" (");
      getPrinter().print(a.getRightRole().get());
      getPrinter().print(")");
    }
    // print right qualifier
    if (a.getRightQualifier().isPresent()) {
      a.getRightQualifier().get().accept(getRealThis());
    }
    // print objects referenced on the right side of the link
    getPrinter().print(" ");
    refNames = a.getRightReferenceNames().iterator();
    while (refNames.hasNext()) {
      refNames.next().accept(getRealThis());
      if (refNames.hasNext()) {
        getPrinter().print(", ");
      }
    }
    // print right modifier
    if (a.getRightModifier().isPresent()) {
      a.getRightModifier().get().accept(getRealThis());
    }
  }

  private ODVisitor realThis = this;

  /**
   * @see de.monticore.lang.od._visitor.ODVisitor#setRealThis(de.monticore.lang.od._visitor.ODVisitor)
   */
  @Override
  public void setRealThis(ODVisitor realThis) {
    this.realThis = realThis;
  }

  /**
   * @see de.monticore.common.prettyprint.CommonPrettyPrinterConcreteVisitor#getRealThis()
   */
  @Override
  public ODVisitor getRealThis() {
    return realThis;
  }
}
