// (c) https://github.com/MontiCore/monticore

package de.monticore.odattribute._symboltable;

import de.monticore.odattribute.typescalculator.DeriveSymTypeOfODAttribute;
import de.monticore.odbasis.typescalculator.ODTypesCalculator;

import java.util.Deque;

public class ODAttributeSymbolTableCreator extends ODAttributeSymbolTableCreatorTOP {

  private ODTypesCalculator typechecker = new DeriveSymTypeOfODAttribute();

  public ODAttributeSymbolTableCreator() { super(); }

  public ODAttributeSymbolTableCreator(IODAttributeScope enclosingScope) {
    super(enclosingScope);
  }

  public ODAttributeSymbolTableCreator(Deque<? extends IODAttributeScope> scopeStack) {
    super(scopeStack);
  }

  public void setTypechecker(ODTypesCalculator typechecker) {
    this.typechecker = typechecker;
  }

}
