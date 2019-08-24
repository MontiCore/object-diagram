/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.od._symboltable;

import de.monticore.symboltable.Symbols;

import java.util.Collection;

public class ObjectDiagramSymbol extends ObjectDiagramSymbolTOP {

  public ObjectDiagramSymbol(String name) {
    super(name);
  }

  public Collection<ODObjectSymbol> getODObjects() {
    return Symbols.sortSymbolsByPosition(getSpannedScope().resolveLocally(ODObjectSymbol.KIND));
  }
}
