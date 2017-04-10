package de.monticore.lang.od.report;

import de.monticore.generating.templateengine.reporting.commons.ReportingRepository;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.symboltable.Scope;
import de.monticore.symboltable.Symbol;
import de.monticore.types.types._ast.*;
import de.monticore.types.types._visitor.TypesVisitor;
import de.se_rwth.commons.Names;
import de.se_rwth.commons.StringTransformations;

import java.util.Iterator;

public class Types2OD implements TypesVisitor {
  private TypesVisitor realThis = this;

  protected IndentPrinter pp;

  protected ReportingRepository reporting;

  protected boolean printEmptyOptional = false;

  protected boolean printEmptyList = false;

  public Types2OD(IndentPrinter printer, ReportingRepository reporting) {
    this.reporting = reporting;
    this.pp = printer;
  }

  public void handle(ASTQualifiedName node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.types.types._ast.ASTQualifiedName");
    this.pp.indent();
    String sep;
    if (node.getSymbol().isPresent()) {
      sep = StringTransformations
          .uncapitalize(this.reporting.getSymbolNameFormatted((Symbol) node.getSymbol().get()));
      this.pp.println("symbol = " + sep + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("symbol = absent;");
    }

    if (node.getEnclosingScope().isPresent()) {
      sep = StringTransformations.uncapitalize(
          this.reporting.getScopeNameFormatted((Scope) node.getEnclosingScope().get()));
      this.pp.println("enclosingScope = " + sep + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("enclosingScope = absent;");
    }

    if (node.getSpannedScope().isPresent()) {
      sep = StringTransformations
          .uncapitalize(this.reporting.getScopeNameFormatted((Scope) node.getSpannedScope().get()));
      this.pp.println("spanningScope = " + sep + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("spannedScope = absent;");
    }

    sep = "";
    String str = "\"";
    Iterator<?> it = node.getParts().iterator();
    boolean isEmpty = true;
    if (it.hasNext() || this.printEmptyList) {
      this.pp.print("parts = [");
      isEmpty = false;
    }

    while (it.hasNext()) {
      this.pp.print(sep);
      this.pp.print(str + String.valueOf(it.next()) + str);
      sep = ", ";
    }

    if (!isEmpty) {
      this.pp.println("];");
    }

    this.pp.unindent();
    this.pp.print("}");
  }

  public void handle(ASTComplexArrayType node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.types.types._ast.ASTComplexArrayType");
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

    if (null != node.getComponentType()) {
      this.pp.print("componentType");
      this.pp.print(" = ");
      node.getComponentType().accept(this.getRealThis());
      this.pp.println(";");
    }

    this.printAttribute("dimensions", String.valueOf(node.getDimensions()));
    this.pp.unindent();
    this.pp.print("}");
  }

  public void handle(ASTPrimitiveArrayType node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.types.types._ast.ASTPrimitiveArrayType");
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

    if (null != node.getComponentType()) {
      this.pp.print("componentType");
      this.pp.print(" = ");
      node.getComponentType().accept(this.getRealThis());
      this.pp.println(";");
    }

    this.printAttribute("dimensions", String.valueOf(node.getDimensions()));
    this.pp.unindent();
    this.pp.print("}");
  }

  public void handle(ASTVoidType node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.types.types._ast.ASTVoidType");
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

  public void handle(ASTPrimitiveType node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.types.types._ast.ASTPrimitiveType");
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

    this.printAttribute("primitive", String.valueOf(node.getPrimitive()));
    this.pp.unindent();
    this.pp.print("}");
  }

  public void handle(ASTSimpleReferenceType node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.types.types._ast.ASTSimpleReferenceType");
    this.pp.indent();
    String sep;
    if (node.getSymbol().isPresent()) {
      sep = StringTransformations
          .uncapitalize(this.reporting.getSymbolNameFormatted((Symbol) node.getSymbol().get()));
      this.pp.println("symbol = " + sep + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("symbol = absent;");
    }

    if (node.getEnclosingScope().isPresent()) {
      sep = StringTransformations.uncapitalize(
          this.reporting.getScopeNameFormatted((Scope) node.getEnclosingScope().get()));
      this.pp.println("enclosingScope = " + sep + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("enclosingScope = absent;");
    }

    if (node.getSpannedScope().isPresent()) {
      sep = StringTransformations
          .uncapitalize(this.reporting.getScopeNameFormatted((Scope) node.getSpannedScope().get()));
      this.pp.println("spanningScope = " + sep + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("spannedScope = absent;");
    }

    sep = "";
    String str = "\"";
    Iterator<?> it = node.getNames().iterator();
    boolean isEmpty = true;
    if (it.hasNext() || this.printEmptyList) {
      this.pp.print("names = [");
      isEmpty = false;
    }

    while (it.hasNext()) {
      this.pp.print(sep);
      this.pp.print(str + String.valueOf(it.next()) + str);
      sep = ", ";
    }

    if (!isEmpty) {
      this.pp.println("];");
    }

    if (node.getTypeArguments().isPresent()) {
      this.pp.print("typeArguments");
      this.pp.print(" = ");
      ((ASTTypeArguments) node.getTypeArguments().get()).accept(this.getRealThis());
      this.pp.println(";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("typeArguments = absent;");
    }

    this.pp.unindent();
    this.pp.print("}");
  }

  public void handle(ASTComplexReferenceType node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.types.types._ast.ASTComplexReferenceType");
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

    Iterator<ASTSimpleReferenceType> iter_simpleReferenceTypes = node.getSimpleReferenceTypes()
        .iterator();
    boolean isEmpty = true;
    if (iter_simpleReferenceTypes.hasNext()) {
      this.pp.print("simpleReferenceTypes");
      this.pp.print(" = [");
      this.pp.println("// *size: " + node.getSimpleReferenceTypes().size());
      this.pp.indent();
      isEmpty = false;
    }
    else if (this.printEmptyList) {
      this.pp.print("simpleReferenceTypes");
      this.pp.println(" = []; // *size: 0");
    }

    boolean isFirst = true;

    while (iter_simpleReferenceTypes.hasNext()) {
      if (!isFirst) {
        this.pp.println(",");
      }

      isFirst = false;
      ((ASTSimpleReferenceType) iter_simpleReferenceTypes.next()).accept(this.getRealThis());
    }

    if (!isEmpty) {
      this.pp.println("];");
      this.pp.unindent();
    }

    this.pp.unindent();
    this.pp.print("}");
  }

  public void handle(ASTTypeArguments node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.types.types._ast.ASTTypeArguments");
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

    Iterator<ASTTypeArgument> iter_typeArguments = node.getTypeArguments().iterator();
    boolean isEmpty = true;
    if (iter_typeArguments.hasNext()) {
      this.pp.print("typeArguments");
      this.pp.print(" = [");
      this.pp.println("// *size: " + node.getTypeArguments().size());
      this.pp.indent();
      isEmpty = false;
    }
    else if (this.printEmptyList) {
      this.pp.print("typeArguments");
      this.pp.println(" = []; // *size: 0");
    }

    boolean isFirst = true;

    while (iter_typeArguments.hasNext()) {
      if (!isFirst) {
        this.pp.println(",");
      }

      isFirst = false;
      ((ASTTypeArgument) iter_typeArguments.next()).accept(this.getRealThis());
    }

    if (!isEmpty) {
      this.pp.println("];");
      this.pp.unindent();
    }

    this.pp.unindent();
    this.pp.print("}");
  }

  public void handle(ASTWildcardType node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.types.types._ast.ASTWildcardType");
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

    if (node.getUpperBound().isPresent()) {
      this.pp.print("upperBound");
      this.pp.print(" = ");
      ((ASTType) node.getUpperBound().get()).accept(this.getRealThis());
      this.pp.println(";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("upperBound = absent;");
    }

    if (node.getLowerBound().isPresent()) {
      this.pp.print("lowerBound");
      this.pp.print(" = ");
      ((ASTType) node.getLowerBound().get()).accept(this.getRealThis());
      this.pp.println(";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("lowerBound = absent;");
    }

    this.pp.unindent();
    this.pp.print("}");
  }

  public void handle(ASTTypeParameters node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.types.types._ast.ASTTypeParameters");
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

    Iterator<ASTTypeVariableDeclaration> iter_typeVariableDeclarations = node
        .getTypeVariableDeclarations().iterator();
    boolean isEmpty = true;
    if (iter_typeVariableDeclarations.hasNext()) {
      this.pp.print("typeVariableDeclarations");
      this.pp.print(" = [");
      this.pp.println("// *size: " + node.getTypeVariableDeclarations().size());
      this.pp.indent();
      isEmpty = false;
    }
    else if (this.printEmptyList) {
      this.pp.print("typeVariableDeclarations");
      this.pp.println(" = []; // *size: 0");
    }

    boolean isFirst = true;

    while (iter_typeVariableDeclarations.hasNext()) {
      if (!isFirst) {
        this.pp.println(",");
      }

      isFirst = false;
      ((ASTTypeVariableDeclaration) iter_typeVariableDeclarations.next())
          .accept(this.getRealThis());
    }

    if (!isEmpty) {
      this.pp.println("];");
      this.pp.unindent();
    }

    this.pp.unindent();
    this.pp.print("}");
  }

  public void handle(ASTTypeVariableDeclaration node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.types.types._ast.ASTTypeVariableDeclaration");
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
    Iterator<ASTComplexReferenceType> iter_upperBounds = node.getUpperBounds().iterator();
    boolean isEmpty = true;
    if (iter_upperBounds.hasNext()) {
      this.pp.print("upperBounds");
      this.pp.print(" = [");
      this.pp.println("// *size: " + node.getUpperBounds().size());
      this.pp.indent();
      isEmpty = false;
    }
    else if (this.printEmptyList) {
      this.pp.print("upperBounds");
      this.pp.println(" = []; // *size: 0");
    }

    boolean isFirst = true;

    while (iter_upperBounds.hasNext()) {
      if (!isFirst) {
        this.pp.println(",");
      }

      isFirst = false;
      ((ASTComplexReferenceType) iter_upperBounds.next()).accept(this.getRealThis());
    }

    if (!isEmpty) {
      this.pp.println("];");
      this.pp.unindent();
    }

    this.pp.unindent();
    this.pp.print("}");
  }

  public void handle(ASTImportStatement node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.types.types._ast.ASTImportStatement");
    this.pp.indent();
    String sep;
    if (node.getSymbol().isPresent()) {
      sep = StringTransformations
          .uncapitalize(this.reporting.getSymbolNameFormatted((Symbol) node.getSymbol().get()));
      this.pp.println("symbol = " + sep + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("symbol = absent;");
    }

    if (node.getEnclosingScope().isPresent()) {
      sep = StringTransformations.uncapitalize(
          this.reporting.getScopeNameFormatted((Scope) node.getEnclosingScope().get()));
      this.pp.println("enclosingScope = " + sep + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("enclosingScope = absent;");
    }

    if (node.getSpannedScope().isPresent()) {
      sep = StringTransformations
          .uncapitalize(this.reporting.getScopeNameFormatted((Scope) node.getSpannedScope().get()));
      this.pp.println("spanningScope = " + sep + ";");
    }
    else if (this.printEmptyOptional) {
      this.pp.println("spannedScope = absent;");
    }

    sep = "";
    String str = "\"";
    Iterator<?> it = node.getImportList().iterator();
    boolean isEmpty = true;
    if (it.hasNext() || this.printEmptyList) {
      this.pp.print("importList = [");
      isEmpty = false;
    }

    while (it.hasNext()) {
      this.pp.print(sep);
      this.pp.print(str + String.valueOf(it.next()) + str);
      sep = ", ";
    }

    if (!isEmpty) {
      this.pp.println("];");
    }

    this.printAttribute("star", String.valueOf(node.isStar()));
    this.pp.unindent();
    this.pp.print("}");
  }

  public void handle(ASTArrayType node) {
    String name = StringTransformations.uncapitalize(this.reporting.getASTNodeNameFormatted(node));
    this.printObject(name, "de.monticore.types.types._ast.ASTArrayType");
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

    if (null != node.getComponentType()) {
      this.pp.print("componentType");
      this.pp.print(" = ");
      node.getComponentType().accept(this.getRealThis());
      this.pp.println(";");
    }

    this.printAttribute("dimensions", String.valueOf(node.getDimensions()));
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

  public String printObjectDiagram(String modelName, ASTTypesNode node) {
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

  public void setRealThis(TypesVisitor realThis) {
    this.realThis = realThis;
  }

  public TypesVisitor getRealThis() {
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
