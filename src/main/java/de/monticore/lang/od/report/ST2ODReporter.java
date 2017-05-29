package de.monticore.lang.od.report;

import de.monticore.generating.templateengine.reporting.commons.ReportingRepository;
import de.monticore.generating.templateengine.reporting.reporter.SymbolTableReporter;
import de.monticore.lang.od._symboltable.ODObjectSymbol;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.symboltable.Symbol;

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

}
