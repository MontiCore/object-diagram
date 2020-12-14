// (c) https://github.com/MontiCore/monticore

package de.monticore.odlink._symboltable;

import java.util.Deque;

public class ODLinkSymbolTableCreator extends ODLinkSymbolTableCreatorTOP {

  public ODLinkSymbolTableCreator() { super(); }

  public ODLinkSymbolTableCreator(IODLinkScope enclosingScope) {
    super(enclosingScope);
  }

  public ODLinkSymbolTableCreator(Deque<? extends IODLinkScope> scopeStack) {
    super(scopeStack);
  }

}
