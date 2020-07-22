// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

/* (c) https://github.com/MontiCore/monticore */
package de.monticore.odbasis._symboltable;

import de.monticore.symboltable.ISymbol;

import java.util.List;

public class ObjectDiagramSymbol extends ObjectDiagramSymbolTOP {

  public ObjectDiagramSymbol(String name) {
    super(name);
  }

  public List<ODNamedObjectSymbol> getObjects() {
    return ISymbol.sortSymbolsByPosition(getSpannedScope().getLocalODNamedObjectSymbols());
  }

}
