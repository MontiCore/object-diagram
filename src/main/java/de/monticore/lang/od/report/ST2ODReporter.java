package de.monticore.lang.od.report;

import de.monticore.generating.templateengine.reporting.commons.ReportingRepository;
import de.monticore.generating.templateengine.reporting.reporter.SymbolTableReporter;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.symboltable.Symbol;

public class ST2ODReporter extends SymbolTableReporter {


  public ST2ODReporter(String outputDir, String modelName, ReportingRepository repository) {
    super(outputDir, modelName, repository);
  }

  /**
   * @see de.monticore.generating.templateengine.reporting.reporter.SymbolTableReporter#reportAttributes(de.monticore.symboltable.Symbol, de.monticore.prettyprint.IndentPrinter)
   */
  @Override
  protected void reportAttributes(Symbol sym, IndentPrinter printer) {
    super.reportAttributes(sym, printer);
    // TODO Report additional attributes
  }

  

}
