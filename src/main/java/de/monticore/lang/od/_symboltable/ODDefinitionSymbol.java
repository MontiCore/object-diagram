/*
 * Copyright (c) 2015 RWTH Aachen. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
package de.monticore.lang.od._symboltable;

import java.util.Optional;

public class ODDefinitionSymbol extends ODDefinitionSymbolTOP {

  public ODDefinitionSymbol(final String name) {
    super(name);
  }

  @Override
  protected ODDefinitionScope createSpannedScope() {
    return new ODDefinitionScope(Optional.empty());
  }

  public Optional<ODObjectSymbol> getODObject(final String name) {
    return getSpannedScope().resolveLocally(name, ODObjectSymbol.KIND);
  }

}
