package de.monticore.lang.od.report;

import de.monticore.ast.ASTNode;
import de.monticore.generating.templateengine.reporting.commons.ReportingRepository;
import de.monticore.generating.templateengine.reporting.reporter.SymbolTableReporter;
import de.monticore.lang.od._symboltable.ODObjectSymbol;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.symboltable.*;
import de.se_rwth.commons.Names;

import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;

public class ST2ODReporter extends SymbolTableReporter {

  public ST2ODReporter(String outputDir, String modelName, ReportingRepository repository) {
    super(outputDir, modelName, repository);
    setPrintAllFieldsCommented(true);
    setPrintEmptyList(true);
    setPrintEmptyOptional(true);
  }

  /**
   * @see de.monticore.generating.templateengine.reporting.reporter.SymbolTableReporter#reportAttributes(de.monticore.symboltable.Symbol, de.monticore.prettyprint.IndentPrinter)
   */
  @Override
  protected void reportAttributes(Symbol sym, IndentPrinter printer) {
    super.reportAttributes(sym, printer);

    if (sym instanceof ODObjectSymbol) {
      // print object type
      printer.print("type = ");
      printer.print(repository.getASTNodeNameFormatted(((ODObjectSymbol) sym).getType()));
      printer.println(";");
    }
  }

  @Override protected void reportScope(Scope scope, IndentPrinter printer) {
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

    printer.unindent();
    printer.print("}");
  }
}
