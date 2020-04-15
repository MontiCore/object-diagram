/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.odbasics.report;

import de.monticore.generating.templateengine.reporting.commons.ReportingConstants;
import de.monticore.generating.templateengine.reporting.commons.ReportingRepository;
import de.monticore.generating.templateengine.reporting.reporter.SymbolTableReporter2;
import de.monticore.lang.odbasics._symboltable.ODObjectSymbol;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.symboltable.ISymbol;

import java.io.File;

public class ST2ODReporter extends SymbolTableReporter2 {

  public ST2ODReporter(String outputDir, String modelName, ReportingRepository repository) {
    super(outputDir + File.separator + ReportingConstants.REPORTING_DIR + File.separator, modelName,
        repository);
    setPrintAllFieldsCommented(true);
    setPrintEmptyList(true);
    setPrintEmptyOptional(true);
  }

  /**
   * @see de.monticore.generating.templateengine.reporting.reporter.SymbolTableReporter2#reportAttributes(de.monticore.symboltable.ISymbol,
   * IndentPrinter)
   */
  @Override
  protected void reportAttributes(ISymbol sym, IndentPrinter printer) {
    super.reportAttributes(sym, printer);

    if (sym instanceof ODObjectSymbol) {
      // print object type
      printer.print("type = ");
      printer.print(repository.getASTNodeNameFormatted(((ODObjectSymbol) sym).getAstNode()));
      printer.println(";");
    }
  }

}
