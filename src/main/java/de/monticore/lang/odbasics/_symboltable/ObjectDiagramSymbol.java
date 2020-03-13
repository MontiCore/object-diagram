/*
 * Copyright (c) 2017, MontiCore. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
package de.monticore.lang.odbasics._symboltable;

import de.monticore.symboltable.ISymbol;

import java.util.Collection;

public class ObjectDiagramSymbol extends ObjectDiagramSymbolTOP {

  public ObjectDiagramSymbol(String name) {
    super(name);
  }

  public Collection<ODObjectSymbol> getObjects() {
    return ISymbol.sortSymbolsByPosition(getSpannedScope().getLocalODObjectSymbols());
  }

}
