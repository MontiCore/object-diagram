package de.monticore.lang.od.report;

import de.monticore.ast.ASTNode;
import de.monticore.generating.templateengine.reporting.commons.AReporter;
import de.monticore.generating.templateengine.reporting.commons.ReportingRepository;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.symboltable.*;
import de.se_rwth.commons.Names;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Collectors;

public class ST2ODReporter extends AReporter {
  protected final String outputDir;

  protected final String modelName;

  protected final ReportingRepository repository;

  protected boolean printEmptyOptional = false;

  protected boolean printAllFieldsCommented = false;

  protected boolean printEmptyList = false;

  public ST2ODReporter(String outputDir, String modelName, ReportingRepository repository) {
    super(outputDir + File.separator + "reports" + File.separator + modelName,
        Names.getSimpleName(modelName) + "_ST", "od");
    this.outputDir = outputDir;
    this.modelName = modelName;
    this.repository = repository;
  }

  @Override
  protected void writeHeader() {
    writeLine("/*");
    writeLine(
        " * ========================================================== ST for ObjectDiagram");
    writeLine(" */");
  }

  private void writeFooter() {
    writeLine("/*");
    writeLine(" * ========================================================== Explanation");
    writeLine(" * Shows the ST after finishing the generation process.");
    writeLine(" */");
  }

  public void flush(ASTNode ast) {
    if (ast != null) {
      Optional scope;
      for (scope = ast.getSpannedScope();
           scope.isPresent() && !(scope.get() instanceof GlobalScope); scope = ((Scope) scope.get())
          .getEnclosingScope()) {
        ;
      }

      if (scope.isPresent()) {
        this.reportSymbolTableScope((Scope) scope.get());
      }
    }

    this.writeFooter();
    super.flush(ast);
  }

  protected void reportScope(Scope scope, IndentPrinter printer) {
    Collection<Symbol> symbols = Scopes.getLocalSymbolsAsCollection(scope);
    String scopeName = this.repository.getScopeNameFormatted(scope);
    String type = Names.getSimpleName(scope.getClass().getName());
    printer.println(scopeName + ": " + type + "{");
    printer.indent();
    this.reportAllFields(scope.getClass(), printer);
    if (scope.getName().isPresent()) {
      printer.println("name = \"" + (String) scope.getName().get() + "\";");
    }
    else if (this.printEmptyOptional) {
      printer.println("name = absent;");
    }

    String sep;
    Iterator var8;
    if (scope instanceof ArtifactScope) {
      ArtifactScope artifactScope = (ArtifactScope) scope;
      if (artifactScope.getImports().isEmpty()) {
        if (this.printEmptyList) {
          printer.println("imports = [];");
        }
      }
      else {
        printer.print("imports = [\"");
        sep = "";
        var8 = artifactScope.getImports().iterator();

        while (var8.hasNext()) {
          ImportStatement imp = (ImportStatement) var8.next();
          printer.print(sep);
          sep = ", ";
          printer.print(imp.getStatement());
          printer.print("\"");
        }

        printer.println("];");
      }

      printer.println("packageName = \"" + artifactScope.getPackageName() + "\";");
    }

    printer.println("isShadowingScope = " + scope.isShadowingScope() + ";");
    printer.println("exportsSymbols = " + scope.exportsSymbols() + ";");
    if (scope.getAstNode().isPresent()) {
      printer.print("astNode = ");
      printer.print(this.repository.getASTNodeNameFormatted((ASTNode) scope.getAstNode().get()));
      printer.println(";");
    }
    else if (this.printEmptyOptional) {
      printer.println("astNode = absent;");
    }

    if (scope.getSpanningSymbol().isPresent()) {
      printer.print("spanningSymbol = ");
      this.reportSymbol((Symbol) scope.getSpanningSymbol().get(), printer);
      printer.println(";");
    }
    else if (this.printEmptyOptional) {
      printer.println("spanningSymbol = absent;");
    }

    if (scope.getEnclosingScope().isPresent()) {
      printer.print("enclosingScope = ");
      printer.print(this.repository.getScopeNameFormatted((Scope) scope.getEnclosingScope().get()));
      printer.println(";");
    }
    else if (this.printEmptyOptional) {
      printer.println("enclosingScope = absent;");
    }

    Collection<Symbol> reportedSymbols = (Collection) symbols.stream().filter((sym) -> {
      return !(sym instanceof ScopeSpanningSymbol);
    }).collect(Collectors.toList());
    if (!reportedSymbols.isEmpty()) {
      printer.print("symbols = [");
      printer.println("// *size: " + reportedSymbols.size());
      printer.indent();
    }
    else if (this.printEmptyList) {
      printer.println("symbols = [];");
    }

    sep = "";
    var8 = reportedSymbols.iterator();

    while (var8.hasNext()) {
      Symbol symbol = (Symbol) var8.next();
      if (!(symbol instanceof ScopeSpanningSymbol)) {
        printer.print(sep);
        sep = ",\n";
        this.reportSymbol(symbol, printer);
      }
    }

    if (!reportedSymbols.isEmpty()) {
      printer.println("];");
      printer.unindent();
    }

    if (!scope.getSubScopes().isEmpty()) {
      printer.print("subScopes = [");
      printer.println("// *size: " + scope.getSubScopes().size());
      printer.indent();
    }
    else if (this.printEmptyList) {
      printer.println("subScopes = [];");
    }

    sep = "";
    var8 = scope.getSubScopes().iterator();

    while (var8.hasNext()) {
      Scope subScope = (Scope) var8.next();
      printer.print(sep);
      sep = ",\n";
      this.reportScope(subScope, printer);
    }

    if (!scope.getSubScopes().isEmpty()) {
      printer.println("];");
      printer.unindent();
    }

    if (scope instanceof GlobalScope) {
      printer.unindent();
      printer.print("};");
    }
    else {
      printer.unindent();
      printer.print("}");
    }
  }

  public void reportSymbolTableScope(Scope scope) {
    IndentPrinter printer = new IndentPrinter();
    printer.println("objectdiagram " + Names.getSimpleName(this.modelName) + "_ST {");
    printer.indent();
    this.reportScope(scope, printer);
    printer.println();
    printer.unindent();
    printer.println("}");
    this.writeLine(printer.getContent());
  }

  protected void reportSymbol(Symbol sym, IndentPrinter printer) {
    String type = Names.getSimpleName(sym.getClass().getSimpleName());
    String symName = this.repository.getSymbolNameFormatted(sym);
    printer.println(symName + ": " + type + " {");
    printer.indent();
    this.reportAttributes(sym, printer);
    printer.unindent();
    printer.print("}");
  }

  protected void reportAttributes(Symbol sym, IndentPrinter printer) {
    this.reportAllFields(sym.getClass(), printer);
    printer.println("name = \"" + sym.getName() + "\";");
    printer.println("KIND = \"" + sym.getKind() + "\";");
    if (sym.getAstNode().isPresent()) {
      printer.print("astNode = ");
      printer.print(this.repository.getASTNodeNameFormatted((ASTNode) sym.getAstNode().get()));
      printer.println(";");
    }
    else if (this.printEmptyOptional) {
      printer.print("astNode = absent;");
    }

    if (sym instanceof ScopeSpanningSymbol) {
      ScopeSpanningSymbol spanningSym = (ScopeSpanningSymbol) sym;
      printer.println(
          "spannedScope = " + this.repository.getScopeNameFormatted(spanningSym.getSpannedScope())
              + ";");
    }

    printer.print("enclosingScope = ");
    printer.print(this.repository.getScopeNameFormatted(sym.getEnclosingScope()));
    printer.println(";");
  }

  public boolean isPrintAllFieldsCommented() {
    return this.printAllFieldsCommented;
  }

  protected void reportAllFields(Class<?> clazz, IndentPrinter printer) {
    if (this.isPrintAllFieldsCommented()) {
      printer.print("/* fields = ");
      Field[] var3 = clazz.getDeclaredFields();
      int var4 = var3.length;

      int var5;
      Field field;
      for (var5 = 0; var5 < var4; ++var5) {
        field = var3[var5];
        printer.print(field.getName() + " ");
      }

      while (clazz.getSuperclass() != null) {
        clazz = clazz.getSuperclass();
        var3 = clazz.getDeclaredFields();
        var4 = var3.length;

        for (var5 = 0; var5 < var4; ++var5) {
          field = var3[var5];
          printer.print(field.getName() + " ");
        }
      }

      printer.println("*/");
    }
  }
}
