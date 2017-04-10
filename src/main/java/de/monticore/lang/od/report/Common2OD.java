//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package de.monticore.lang.od.report;

import de.monticore.common.common._ast.*;
import de.monticore.common.common._visitor.CommonVisitor;
import de.monticore.generating.templateengine.reporting.commons.ReportingRepository;
import de.monticore.literals.literals._ast.ASTIntLiteral;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.symboltable.Scope;
import de.monticore.symboltable.Symbol;
import de.se_rwth.commons.Names;
import de.se_rwth.commons.StringTransformations;

import java.util.Iterator;

public class Common2OD implements CommonVisitor {
  private CommonVisitor realThis = this;

  protected IndentPrinter pp;

  protected ReportingRepository reporting;

  protected boolean printEmptyOptional = false;

  protected boolean printEmptyList = false;

  public Common2OD(IndentPrinter printer, ReportingRepository reporting) {
    this.reporting = reporting;
    this.pp = printer;
  }

  public void handle(ASTStereotype node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.common.common._ast.ASTStereotype");
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
      ((ASTStereoValue) iter_values.next()).accept(this.getRealThis());
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

    this.printAttribute("lowerBound", String.valueOf(node.getLowerBound()));
    this.printAttribute("upperBound", String.valueOf(node.getUpperBound()));
    if (node.getLowerBoundLit().isPresent()) {
      this.pp.print("lowerBoundLit");
      this.pp.print(" = ");
      ((ASTIntLiteral) node.getLowerBoundLit().get()).accept(this.getRealThis());
      this.pp.println(";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("lowerBoundLit = absent;");
    }

    if (node.getUpperBoundLit().isPresent()) {
      this.pp.print("upperBoundLit");
      this.pp.print(" = ");
      ((ASTIntLiteral) node.getUpperBoundLit().get()).accept(this.getRealThis());
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

    if (node.getStereotype().isPresent()) {
      this.pp.print("stereotype");
      this.pp.print(" = ");
      ((ASTStereotype) node.getStereotype().get()).accept(this.getRealThis());
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

  public String printObjectDiagram(String modelName, ASTCommonNode node) {
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

  public void setRealThis(CommonVisitor realThis) {
    this.realThis = realThis;
  }

  public CommonVisitor getRealThis() {
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
