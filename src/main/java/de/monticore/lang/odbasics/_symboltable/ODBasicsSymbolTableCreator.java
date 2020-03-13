/*
 * Copyright (c) 2017, MontiCore. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
package de.monticore.lang.odbasics._symboltable;

import de.monticore.lang.odbasics._ast.ASTODObject;

import java.util.Deque;

public class ODBasicsSymbolTableCreator extends ODBasicsSymbolTableCreatorTOP {

  public ODBasicsSymbolTableCreator(IODBasicsScope enclosingScope) {
    super(enclosingScope);
  }

  public ODBasicsSymbolTableCreator(Deque<? extends IODBasicsScope> scopeStack) {
    super(scopeStack);
  }

  public void visit(ASTODObject node) {
    // no symbols for anonymous objects
    if ("".equals(node.getName())) {
      return;
    }

    super.visit(node);
  }

}
