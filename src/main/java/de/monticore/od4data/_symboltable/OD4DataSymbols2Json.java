// (c) https://github.com/MontiCore/monticore

package de.monticore.od4data._symboltable;

import de.monticore.odbasis._symboltable.IODBasisScope;
import de.monticore.symboltable.serialization.JsonPrinter;

public class OD4DataSymbols2Json extends OD4DataSymbols2JsonTOP {

  public OD4DataSymbols2Json() {
    super();
  }

  public OD4DataSymbols2Json(JsonPrinter printer) {
    super(printer);
  }

  /*
  Override the traverse method to avoid a duplication of symbols in the printed symboltable
  caused by the symboltableprinter of the super grammars.
   */
  @Override
  public void traverse(IOD4DataScope node) {
    getRealThis().traverse((IODBasisScope) node);
  }

}
