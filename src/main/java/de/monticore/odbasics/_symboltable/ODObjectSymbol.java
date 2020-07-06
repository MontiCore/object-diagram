// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

package de.monticore.odbasics._symboltable;

import de.monticore.types.check.SymTypeExpression;

public class ODObjectSymbol extends ODObjectSymbolTOP {

  private SymTypeExpression objectType;

  public SymTypeExpression getObjectType() {
    return objectType;
  }

  public ODObjectSymbol(String name, SymTypeExpression objectType) {
    super(name);
    this.objectType = objectType;
  }

  public void setObjectType(SymTypeExpression objectType) {
    this.objectType = objectType;
  }

  public ODObjectSymbol(String name) {
    super(name);
  }

}
