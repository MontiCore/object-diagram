/*
 * Copyright (c) 2017, MontiCore. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
package de.monticore.lang.od._symboltable;

import de.monticore.symboltable.Scope;

public class ObjectDiagramSymbolReference extends ObjectDiagramSymbolReferenceTOP {

  public ObjectDiagramSymbolReference(String name,
      Scope enclosingScopeOfReference) {
    super(name, enclosingScopeOfReference);
  }

  @Override public Scope getSpannedScope() {
    return getReferencedSymbol().getSpannedScope();
  }
}
