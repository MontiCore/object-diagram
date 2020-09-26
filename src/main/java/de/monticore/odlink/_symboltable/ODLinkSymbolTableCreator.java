// (c) https://github.com/MontiCore/monticore

package de.monticore.odlink._symboltable;

import de.monticore.odbasis.typescalculator.ODTypesCalculator;

import java.util.Deque;

public class ODLinkSymbolTableCreator extends ODLinkSymbolTableCreatorTOP {

  private ODTypesCalculator typechecker;

  public ODLinkSymbolTableCreator(IODLinkScope enclosingScope) {
    super(enclosingScope);
  }

  public ODLinkSymbolTableCreator(Deque<? extends IODLinkScope> scopeStack) {
    super(scopeStack);
  }

  public void setTypechecker(ODTypesCalculator typechecker) {
    this.typechecker = typechecker;
  }

}
