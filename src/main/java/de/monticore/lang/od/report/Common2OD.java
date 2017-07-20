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

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package de.monticore.lang.od.report;

import de.monticore.ast.ASTNode;
import de.monticore.common.common._ast.*;
import de.monticore.common.common._visitor.CommonVisitor;
import de.monticore.generating.templateengine.reporting.commons.ReportingRepository;
import de.monticore.prettyprint.IndentPrinter;
import de.se_rwth.commons.Names;
import de.se_rwth.commons.StringTransformations;

import java.util.Iterator;

public class Common2OD implements CommonVisitor {
  private CommonVisitor realThis = this;

  private IndentPrinter pp;

  protected ReportingRepository reporting;

  private boolean printEmptyOptional = false;

  private boolean printEmptyList = false;

  public Common2OD(IndentPrinter printer, ReportingRepository reporting) {
    this.reporting = reporting;
    this.pp = printer;
  }

  public void handle(ASTStereotype node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.common.common._ast.ASTStereotype");
    this.pp.indent();
    this.printSymbol(node);
    this.printEnclosingScope(node);
    this.printSpannedScope(node);
    Iterator<ASTStereoValue> iter_values = node.getValues().iterator();
    boolean isEmpty = true;
    if (iter_values.hasNext()) {
      this.pp.print("values");
      this.pp.print(" = [");
      this.pp.println("// *size: " + node.getValues().size());
      this.pp.indent();
      isEmpty = false;
    }
    else if (this.printEmptyList) {
      this.pp.print("values");
      this.pp.println(" = []; // *size: 0");
    }

    boolean isFirst = true;

    while (iter_values.hasNext()) {
      if (!isFirst) {
        this.pp.println(",");
      }

      isFirst = false;
      iter_values.next().accept(this.getRealThis());
    }

    if (!isEmpty) {
      this.pp.println("];");
      this.pp.unindent();
    }

    this.pp.unindent();
    this.pp.print("}");
  }

  public void handle(ASTStereoValue node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.common.common._ast.ASTStereoValue");
    this.pp.indent();
    this.printSymbol(node);
    this.printEnclosingScope(node);
    this.printSpannedScope(node);
    this.printAttribute("name", "\"" + node.getName() + "\"");
    if (node.getSource().isPresent()) {
      this.printAttribute("source", "\"" + String.valueOf(node.getSource().get()) + "\"");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("source = absent;");
    }

    this.pp.unindent();
    this.pp.print("}");
  }

  public void handle(ASTCardinality node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.common.common._ast.ASTCardinality");
    this.pp.indent();
    this.printSymbol(node);
    this.printEnclosingScope(node);
    this.printSpannedScope(node);
    this.printAttribute("lowerBound", String.valueOf(node.getLowerBound()));
    this.printAttribute("upperBound", String.valueOf(node.getUpperBound()));
    if (node.getLowerBoundLit().isPresent()) {
      this.pp.print("lowerBoundLit");
      this.pp.print(" = ");
      node.getLowerBoundLit().get().accept(this.getRealThis());
      this.pp.println(";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("lowerBoundLit = absent;");
    }

    if (node.getUpperBoundLit().isPresent()) {
      this.pp.print("upperBoundLit");
      this.pp.print(" = ");
      node.getUpperBoundLit().get().accept(this.getRealThis());
      this.pp.println(";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("upperBoundLit = absent;");
    }

    this.printAttribute("noUpperLimit", String.valueOf(node.isNoUpperLimit()));
    this.printAttribute("many", String.valueOf(node.isMany()));
    this.pp.unindent();
    this.pp.print("}");
  }

  public void handle(ASTCompleteness node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.common.common._ast.ASTCompleteness");
    this.pp.indent();
    this.printSymbol(node);
    this.printEnclosingScope(node);
    this.printSpannedScope(node);
    this.printAttribute("leftComplete", String.valueOf(node.isLeftComplete()));
    this.printAttribute("rightComplete", String.valueOf(node.isRightComplete()));
    this.printAttribute("complete", String.valueOf(node.isComplete()));
    this.printAttribute("incomplete", String.valueOf(node.isIncomplete()));
    this.pp.unindent();
    this.pp.print("}");
  }

  public void handle(ASTModifier node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.common.common._ast.ASTModifier");
    this.pp.indent();
    this.printSymbol(node);
    this.printEnclosingScope(node);
    this.printSpannedScope(node);

    if (node.getStereotype().isPresent()) {
      this.pp.print("stereotype");
      this.pp.print(" = ");
      node.getStereotype().get().accept(this.getRealThis());
      this.pp.println(";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("stereotype = absent;");
    }

    this.printAttribute("r__static", String.valueOf(node.isStatic()));
    this.printAttribute("r__readonly", String.valueOf(node.isReadonly()));
    this.printAttribute("r__derived", String.valueOf(node.isDerived()));
    this.printAttribute("r__local", String.valueOf(node.isLocal()));
    this.printAttribute("r__abstract", String.valueOf(node.isAbstract()));
    this.printAttribute("r__final", String.valueOf(node.isFinal()));
    this.printAttribute("r__protected", String.valueOf(node.isProtected()));
    this.printAttribute("r__private", String.valueOf(node.isPrivate()));
    this.printAttribute("r__public", String.valueOf(node.isPublic()));
    this.pp.unindent();
    this.pp.print("}");
  }

  private void printAttribute(String name, String value) {
    this.pp.print(name);
    this.pp.print(" = ");
    this.pp.print(value);
    this.pp.println(";");
  }

  private void printObject(String objName, String objType) {
    this.pp.print(objName);
    this.pp.print(":");
    this.pp.print(Names.getSimpleName(objType));
    this.pp.println(" {");
  }

  private void printSymbol(ASTNode node) {
    if (node.getSymbol().isPresent()) {
      String scopeName = StringTransformations
          .uncapitalize(this.reporting.getSymbolNameFormatted(node.getSymbol().get()));
      this.pp.println("symbol = " + scopeName + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("symbol = absent;");
    }
  }

  private void printEnclosingScope(ASTNode node) {
    if (node.getEnclosingScope().isPresent()) {
      String scopeName = StringTransformations.uncapitalize(
          this.reporting.getScopeNameFormatted(node.getEnclosingScope().get()));
      this.pp.println("enclosingScope = " + scopeName + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("enclosingScope = absent;");
    }
  }

  private void printSpannedScope(ASTNode node) {
    if (node.getSpannedScope().isPresent()) {
      String scopeName = StringTransformations
          .uncapitalize(this.reporting.getScopeNameFormatted(node.getSpannedScope().get()));
      this.pp.println("spanningScope = " + scopeName + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("spannedScope = absent;");
    }
  }

  public void setRealThis(CommonVisitor realThis) {
    this.realThis = realThis;
  }

  public CommonVisitor getRealThis() {
    return this.realThis;
  }

  public void setPrintEmptyOptional(boolean printEmptyOptional) {
    this.printEmptyOptional = printEmptyOptional;
  }

  public void setPrintEmptyList(boolean printEmptyList) {
    this.printEmptyList = printEmptyList;
  }
}
