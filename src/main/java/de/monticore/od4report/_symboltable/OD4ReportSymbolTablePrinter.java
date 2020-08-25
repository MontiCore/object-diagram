// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report._symboltable;

import de.monticore.odbasis._symboltable.IODBasisScope;
import de.monticore.symboltable.serialization.JsonPrinter;

public class OD4ReportSymbolTablePrinter extends OD4ReportSymbolTablePrinterTOP {

  public OD4ReportSymbolTablePrinter() {
    super();
  }

  public OD4ReportSymbolTablePrinter(JsonPrinter printer) {
    super(printer);
  }

  /*
  Override the traverse method to avoid a duplication of symbols in the printed symboltable
  caused by the symboltableprinter of the super grammars.
  */
  @Override
  public void traverse(IOD4ReportScope node) {
    getRealThis().traverse((IODBasisScope) node);
  }

}
