package de.monticore.lang.od.report;

import de.monticore.generating.templateengine.reporting.commons.ReportingRepository;
import de.monticore.literals.literals._ast.*;
import de.monticore.literals.literals._visitor.LiteralsVisitor;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.symboltable.Scope;
import de.monticore.symboltable.Symbol;
import de.se_rwth.commons.Names;
import de.se_rwth.commons.StringTransformations;

public class Literals2OD implements LiteralsVisitor {
  private LiteralsVisitor realThis = this;

  protected IndentPrinter pp;

  protected ReportingRepository reporting;

  protected boolean printEmptyOptional = false;

  protected boolean printEmptyList = false;

  public Literals2OD(IndentPrinter printer, ReportingRepository reporting) {
    this.reporting = reporting;
    this.pp = printer;
  }

  public void handle(ASTNullLiteral node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.literals.literals._ast.ASTNullLiteral");
    this.pp.indent();
    String scopeName;
    if (node.getSymbol().isPresent()) {
      scopeName = StringTransformations
          .uncapitalize(this.reporting.getSymbolNameFormatted((Symbol) node.getSymbol().get()));
      this.pp.println("symbol = " + scopeName + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("symbol = absent;");
    }

    if (node.getEnclosingScope().isPresent()) {
      scopeName = StringTransformations.uncapitalize(
          this.reporting.getScopeNameFormatted((Scope) node.getEnclosingScope().get()));
      this.pp.println("enclosingScope = " + scopeName + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("enclosingScope = absent;");
    }

    if (node.getSpannedScope().isPresent()) {
      scopeName = StringTransformations
          .uncapitalize(this.reporting.getScopeNameFormatted((Scope) node.getSpannedScope().get()));
      this.pp.println("spanningScope = " + scopeName + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("spannedScope = absent;");
    }

    this.pp.unindent();
    this.pp.print("}");
  }

  public void handle(ASTBooleanLiteral node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.literals.literals._ast.ASTBooleanLiteral");
    this.pp.indent();
    String scopeName;
    if (node.getSymbol().isPresent()) {
      scopeName = StringTransformations
          .uncapitalize(this.reporting.getSymbolNameFormatted((Symbol) node.getSymbol().get()));
      this.pp.println("symbol = " + scopeName + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("symbol = absent;");
    }

    if (node.getEnclosingScope().isPresent()) {
      scopeName = StringTransformations.uncapitalize(
          this.reporting.getScopeNameFormatted((Scope) node.getEnclosingScope().get()));
      this.pp.println("enclosingScope = " + scopeName + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("enclosingScope = absent;");
    }

    if (node.getSpannedScope().isPresent()) {
      scopeName = StringTransformations
          .uncapitalize(this.reporting.getScopeNameFormatted((Scope) node.getSpannedScope().get()));
      this.pp.println("spanningScope = " + scopeName + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("spannedScope = absent;");
    }

    this.printAttribute("source", String.valueOf(node.getSource()));
    this.pp.unindent();
    this.pp.print("}");
  }

  public void handle(ASTCharLiteral node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.literals.literals._ast.ASTCharLiteral");
    this.pp.indent();
    String scopeName;
    if (node.getSymbol().isPresent()) {
      scopeName = StringTransformations
          .uncapitalize(this.reporting.getSymbolNameFormatted((Symbol) node.getSymbol().get()));
      this.pp.println("symbol = " + scopeName + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("symbol = absent;");
    }

    if (node.getEnclosingScope().isPresent()) {
      scopeName = StringTransformations.uncapitalize(
          this.reporting.getScopeNameFormatted((Scope) node.getEnclosingScope().get()));
      this.pp.println("enclosingScope = " + scopeName + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("enclosingScope = absent;");
    }

    if (node.getSpannedScope().isPresent()) {
      scopeName = StringTransformations
          .uncapitalize(this.reporting.getScopeNameFormatted((Scope) node.getSpannedScope().get()));
      this.pp.println("spanningScope = " + scopeName + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("spannedScope = absent;");
    }

    this.printAttribute("source", "\"" + node.getSource() + "\"");
    this.pp.unindent();
    this.pp.print("}");
  }

  public void handle(ASTStringLiteral node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.literals.literals._ast.ASTStringLiteral");
    this.pp.indent();
    String scopeName;
    if (node.getSymbol().isPresent()) {
      scopeName = StringTransformations
          .uncapitalize(this.reporting.getSymbolNameFormatted((Symbol) node.getSymbol().get()));
      this.pp.println("symbol = " + scopeName + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("symbol = absent;");
    }

    if (node.getEnclosingScope().isPresent()) {
      scopeName = StringTransformations.uncapitalize(
          this.reporting.getScopeNameFormatted((Scope) node.getEnclosingScope().get()));
      this.pp.println("enclosingScope = " + scopeName + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("enclosingScope = absent;");
    }

    if (node.getSpannedScope().isPresent()) {
      scopeName = StringTransformations
          .uncapitalize(this.reporting.getScopeNameFormatted((Scope) node.getSpannedScope().get()));
      this.pp.println("spanningScope = " + scopeName + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("spannedScope = absent;");
    }

    this.printAttribute("source", "\"" + node.getSource() + "\"");
    this.pp.unindent();
    this.pp.print("}");
  }

  public void handle(ASTIntLiteral node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.literals.literals._ast.ASTIntLiteral");
    this.pp.indent();
    String scopeName;
    if (node.getSymbol().isPresent()) {
      scopeName = StringTransformations
          .uncapitalize(this.reporting.getSymbolNameFormatted((Symbol) node.getSymbol().get()));
      this.pp.println("symbol = " + scopeName + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("symbol = absent;");
    }

    if (node.getEnclosingScope().isPresent()) {
      scopeName = StringTransformations.uncapitalize(
          this.reporting.getScopeNameFormatted((Scope) node.getEnclosingScope().get()));
      this.pp.println("enclosingScope = " + scopeName + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("enclosingScope = absent;");
    }

    if (node.getSpannedScope().isPresent()) {
      scopeName = StringTransformations
          .uncapitalize(this.reporting.getScopeNameFormatted((Scope) node.getSpannedScope().get()));
      this.pp.println("spanningScope = " + scopeName + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("spannedScope = absent;");
    }

    this.printAttribute("source", "\"" + node.getSource() + "\"");
    this.pp.unindent();
    this.pp.print("}");
  }

  public void handle(ASTSignedIntLiteral node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.literals.literals._ast.ASTSignedIntLiteral");
    this.pp.indent();
    String scopeName;
    if (node.getSymbol().isPresent()) {
      scopeName = StringTransformations
          .uncapitalize(this.reporting.getSymbolNameFormatted((Symbol) node.getSymbol().get()));
      this.pp.println("symbol = " + scopeName + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("symbol = absent;");
    }

    if (node.getEnclosingScope().isPresent()) {
      scopeName = StringTransformations.uncapitalize(
          this.reporting.getScopeNameFormatted((Scope) node.getEnclosingScope().get()));
      this.pp.println("enclosingScope = " + scopeName + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("enclosingScope = absent;");
    }

    if (node.getSpannedScope().isPresent()) {
      scopeName = StringTransformations
          .uncapitalize(this.reporting.getScopeNameFormatted((Scope) node.getSpannedScope().get()));
      this.pp.println("spanningScope = " + scopeName + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("spannedScope = absent;");
    }

    this.printAttribute("source", "\"" + node.getSource() + "\"");
    this.printAttribute("negative", String.valueOf(node.isNegative()));
    this.pp.unindent();
    this.pp.print("}");
  }

  public void handle(ASTLongLiteral node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.literals.literals._ast.ASTLongLiteral");
    this.pp.indent();
    String scopeName;
    if (node.getSymbol().isPresent()) {
      scopeName = StringTransformations
          .uncapitalize(this.reporting.getSymbolNameFormatted((Symbol) node.getSymbol().get()));
      this.pp.println("symbol = " + scopeName + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("symbol = absent;");
    }

    if (node.getEnclosingScope().isPresent()) {
      scopeName = StringTransformations.uncapitalize(
          this.reporting.getScopeNameFormatted((Scope) node.getEnclosingScope().get()));
      this.pp.println("enclosingScope = " + scopeName + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("enclosingScope = absent;");
    }

    if (node.getSpannedScope().isPresent()) {
      scopeName = StringTransformations
          .uncapitalize(this.reporting.getScopeNameFormatted((Scope) node.getSpannedScope().get()));
      this.pp.println("spanningScope = " + scopeName + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("spannedScope = absent;");
    }

    this.printAttribute("source", "\"" + node.getSource() + "\"");
    this.pp.unindent();
    this.pp.print("}");
  }

  public void handle(ASTSignedLongLiteral node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.literals.literals._ast.ASTSignedLongLiteral");
    this.pp.indent();
    String scopeName;
    if (node.getSymbol().isPresent()) {
      scopeName = StringTransformations
          .uncapitalize(this.reporting.getSymbolNameFormatted((Symbol) node.getSymbol().get()));
      this.pp.println("symbol = " + scopeName + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("symbol = absent;");
    }

    if (node.getEnclosingScope().isPresent()) {
      scopeName = StringTransformations.uncapitalize(
          this.reporting.getScopeNameFormatted((Scope) node.getEnclosingScope().get()));
      this.pp.println("enclosingScope = " + scopeName + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("enclosingScope = absent;");
    }

    if (node.getSpannedScope().isPresent()) {
      scopeName = StringTransformations
          .uncapitalize(this.reporting.getScopeNameFormatted((Scope) node.getSpannedScope().get()));
      this.pp.println("spanningScope = " + scopeName + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("spannedScope = absent;");
    }

    this.printAttribute("source", "\"" + node.getSource() + "\"");
    this.printAttribute("negative", String.valueOf(node.isNegative()));
    this.pp.unindent();
    this.pp.print("}");
  }

  public void handle(ASTFloatLiteral node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.literals.literals._ast.ASTFloatLiteral");
    this.pp.indent();
    String scopeName;
    if (node.getSymbol().isPresent()) {
      scopeName = StringTransformations
          .uncapitalize(this.reporting.getSymbolNameFormatted((Symbol) node.getSymbol().get()));
      this.pp.println("symbol = " + scopeName + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("symbol = absent;");
    }

    if (node.getEnclosingScope().isPresent()) {
      scopeName = StringTransformations.uncapitalize(
          this.reporting.getScopeNameFormatted((Scope) node.getEnclosingScope().get()));
      this.pp.println("enclosingScope = " + scopeName + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("enclosingScope = absent;");
    }

    if (node.getSpannedScope().isPresent()) {
      scopeName = StringTransformations
          .uncapitalize(this.reporting.getScopeNameFormatted((Scope) node.getSpannedScope().get()));
      this.pp.println("spanningScope = " + scopeName + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("spannedScope = absent;");
    }

    this.printAttribute("source", "\"" + node.getSource() + "\"");
    this.pp.unindent();
    this.pp.print("}");
  }

  public void handle(ASTSignedFloatLiteral node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.literals.literals._ast.ASTSignedFloatLiteral");
    this.pp.indent();
    String scopeName;
    if (node.getSymbol().isPresent()) {
      scopeName = StringTransformations
          .uncapitalize(this.reporting.getSymbolNameFormatted((Symbol) node.getSymbol().get()));
      this.pp.println("symbol = " + scopeName + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("symbol = absent;");
    }

    if (node.getEnclosingScope().isPresent()) {
      scopeName = StringTransformations.uncapitalize(
          this.reporting.getScopeNameFormatted((Scope) node.getEnclosingScope().get()));
      this.pp.println("enclosingScope = " + scopeName + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("enclosingScope = absent;");
    }

    if (node.getSpannedScope().isPresent()) {
      scopeName = StringTransformations
          .uncapitalize(this.reporting.getScopeNameFormatted((Scope) node.getSpannedScope().get()));
      this.pp.println("spanningScope = " + scopeName + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("spannedScope = absent;");
    }

    this.printAttribute("source", "\"" + node.getSource() + "\"");
    this.printAttribute("negative", String.valueOf(node.isNegative()));
    this.pp.unindent();
    this.pp.print("}");
  }

  public void handle(ASTDoubleLiteral node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.literals.literals._ast.ASTDoubleLiteral");
    this.pp.indent();
    String scopeName;
    if (node.getSymbol().isPresent()) {
      scopeName = StringTransformations
          .uncapitalize(this.reporting.getSymbolNameFormatted((Symbol) node.getSymbol().get()));
      this.pp.println("symbol = " + scopeName + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("symbol = absent;");
    }

    if (node.getEnclosingScope().isPresent()) {
      scopeName = StringTransformations.uncapitalize(
          this.reporting.getScopeNameFormatted((Scope) node.getEnclosingScope().get()));
      this.pp.println("enclosingScope = " + scopeName + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("enclosingScope = absent;");
    }

    if (node.getSpannedScope().isPresent()) {
      scopeName = StringTransformations
          .uncapitalize(this.reporting.getScopeNameFormatted((Scope) node.getSpannedScope().get()));
      this.pp.println("spanningScope = " + scopeName + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("spannedScope = absent;");
    }

    this.printAttribute("source", "\"" + node.getSource() + "\"");
    this.pp.unindent();
    this.pp.print("}");
  }

  public void handle(ASTSignedDoubleLiteral node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.literals.literals._ast.ASTSignedDoubleLiteral");
    this.pp.indent();
    String scopeName;
    if (node.getSymbol().isPresent()) {
      scopeName = StringTransformations
          .uncapitalize(this.reporting.getSymbolNameFormatted((Symbol) node.getSymbol().get()));
      this.pp.println("symbol = " + scopeName + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("symbol = absent;");
    }

    if (node.getEnclosingScope().isPresent()) {
      scopeName = StringTransformations.uncapitalize(
          this.reporting.getScopeNameFormatted((Scope) node.getEnclosingScope().get()));
      this.pp.println("enclosingScope = " + scopeName + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("enclosingScope = absent;");
    }

    if (node.getSpannedScope().isPresent()) {
      scopeName = StringTransformations
          .uncapitalize(this.reporting.getScopeNameFormatted((Scope) node.getSpannedScope().get()));
      this.pp.println("spanningScope = " + scopeName + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("spannedScope = absent;");
    }

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

  public String printObjectDiagram(String modelName, ASTLiteralsNode node) {
    this.pp.clearBuffer();
    this.pp.setIndentLength(2);
    this.pp.print("objectdiagram ");
    this.pp.print(modelName);
    this.pp.println(" {");
    this.pp.indent();
    node.accept(this.getRealThis());
    this.pp.unindent();
    this.pp.println("}");
    return this.pp.getContent();
  }

  public void setRealThis(LiteralsVisitor realThis) {
    this.realThis = realThis;
  }

  public LiteralsVisitor getRealThis() {
    return this.realThis;
  }

  public boolean isPrintEmptyOptional() {
    return this.printEmptyOptional;
  }

  public void setPrintEmptyOptional(boolean printEmptyOptional) {
    this.printEmptyOptional = printEmptyOptional;
  }

  public boolean isPrintEmptyList() {
    return this.printEmptyList;
  }

  public void setPrintEmptyList(boolean printEmptyList) {
    this.printEmptyList = printEmptyList;
  }
}
