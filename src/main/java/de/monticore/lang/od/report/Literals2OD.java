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

package de.monticore.lang.od.report;

import de.monticore.ast.ASTNode;
import de.monticore.generating.templateengine.reporting.commons.ReportingRepository;
import de.monticore.literals.literals._ast.*;
import de.monticore.literals.literals._visitor.LiteralsVisitor;
import de.monticore.prettyprint.IndentPrinter;
import de.se_rwth.commons.Names;
import de.se_rwth.commons.StringTransformations;

public class Literals2OD implements LiteralsVisitor {
  private LiteralsVisitor realThis = this;

  private IndentPrinter pp;

  protected ReportingRepository reporting;
  
  private boolean printEmptyList = false;
  
  private boolean printEmptyOptional = false;

  public Literals2OD(IndentPrinter printer, ReportingRepository reporting) {
    this.reporting = reporting;
    this.pp = printer;
  }

  public void handle(ASTNullLiteral node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.literals.literals._ast.ASTNullLiteral");
    this.pp.indent();
    this.printSymbol(node);
    this.printEnclosingScope(node);
    this.printSpannedScope(node);
    this.pp.unindent();
    this.pp.print("}");
  }

  public void handle(ASTBooleanLiteral node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.literals.literals._ast.ASTBooleanLiteral");
    this.pp.indent();
    this.printSymbol(node);
    this.printEnclosingScope(node);
    this.printSpannedScope(node);
    this.printAttribute("source", String.valueOf(node.getSource()));
    this.pp.unindent();
    this.pp.print("}");
  }

  public void handle(ASTCharLiteral node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.literals.literals._ast.ASTCharLiteral");
    this.pp.indent();
    this.printSymbol(node);
    this.printEnclosingScope(node);
    this.printSpannedScope(node);
    this.printAttribute("source", "\"" + node.getSource() + "\"");
    this.pp.unindent();
    this.pp.print("}");
  }

  public void handle(ASTStringLiteral node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.literals.literals._ast.ASTStringLiteral");
    this.pp.indent();
    this.printSymbol(node);
    this.printEnclosingScope(node);
    this.printSpannedScope(node);
    this.printAttribute("source", "\"" + node.getSource() + "\"");
    this.pp.unindent();
    this.pp.print("}");
  }

  public void handle(ASTIntLiteral node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.literals.literals._ast.ASTIntLiteral");
    this.pp.indent();
    this.printSymbol(node);
    this.printEnclosingScope(node);
    this.printSpannedScope(node);
    this.printAttribute("source", "\"" + node.getSource() + "\"");
    this.pp.unindent();
    this.pp.print("}");
  }

  public void handle(ASTSignedIntLiteral node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.literals.literals._ast.ASTSignedIntLiteral");
    this.pp.indent();
    this.printSymbol(node);
    this.printEnclosingScope(node);
    this.printSpannedScope(node);
    this.printAttribute("source", "\"" + node.getSource() + "\"");
    this.printAttribute("negative", String.valueOf(node.isNegative()));
    this.pp.unindent();
    this.pp.print("}");
  }

  public void handle(ASTLongLiteral node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.literals.literals._ast.ASTLongLiteral");
    this.pp.indent();
    this.printSymbol(node);
    this.printEnclosingScope(node);
    this.printSpannedScope(node);
    this.printAttribute("source", "\"" + node.getSource() + "\"");
    this.pp.unindent();
    this.pp.print("}");
  }

  public void handle(ASTSignedLongLiteral node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.literals.literals._ast.ASTSignedLongLiteral");
    this.pp.indent();
    this.printSymbol(node);
    this.printEnclosingScope(node);
    this.printSpannedScope(node);
    this.printAttribute("source", "\"" + node.getSource() + "\"");
    this.printAttribute("negative", String.valueOf(node.isNegative()));
    this.pp.unindent();
    this.pp.print("}");
  }

  public void handle(ASTFloatLiteral node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.literals.literals._ast.ASTFloatLiteral");
    this.pp.indent();
    this.printSymbol(node);
    this.printEnclosingScope(node);
    this.printSpannedScope(node);
    this.printAttribute("source", "\"" + node.getSource() + "\"");
    this.pp.unindent();
    this.pp.print("}");
  }

  public void handle(ASTSignedFloatLiteral node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.literals.literals._ast.ASTSignedFloatLiteral");
    this.pp.indent();
    this.printSymbol(node);
    this.printEnclosingScope(node);
    this.printSpannedScope(node);
    this.printAttribute("source", "\"" + node.getSource() + "\"");
    this.printAttribute("negative", String.valueOf(node.isNegative()));
    this.pp.unindent();
    this.pp.print("}");
  }

  public void handle(ASTDoubleLiteral node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.literals.literals._ast.ASTDoubleLiteral");
    this.pp.indent();
    this.printSymbol(node);
    this.printEnclosingScope(node);
    this.printSpannedScope(node);
    this.printAttribute("source", "\"" + node.getSource() + "\"");
    this.pp.unindent();
    this.pp.print("}");
  }

  public void handle(ASTSignedDoubleLiteral node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.literals.literals._ast.ASTSignedDoubleLiteral");
    this.pp.indent();
    this.printSymbol(node);
    this.printEnclosingScope(node);
    this.printSpannedScope(node);
    this.printAttribute("source", "\"" + node.getSource() + "\"");
    this.printAttribute("negative", String.valueOf(node.isNegative()));
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

  public void setRealThis(LiteralsVisitor realThis) {
    this.realThis = realThis;
  }

  public LiteralsVisitor getRealThis() {
    return this.realThis;
  }

  public void setPrintEmptyOptional(boolean printEmptyOptional) {
    this.printEmptyOptional = printEmptyOptional;
  }

  public void setPrintEmptyList(boolean printEmptyList) {
    this.printEmptyList = printEmptyList;
  }
}
