// (c) https://github.com/MontiCore/monticore

package de.monticore.odattribute._symboltable;

import java.util.Deque;

public class ODAttributeSymbolTableCreator extends ODAttributeSymbolTableCreatorTOP {

  public ODAttributeSymbolTableCreator() {
    super();
  }

  public ODAttributeSymbolTableCreator(IODAttributeScope enclosingScope) {
    super(enclosingScope);
  }

  public ODAttributeSymbolTableCreator(Deque<? extends IODAttributeScope> scopeStack) {
    super(scopeStack);
  }

}
