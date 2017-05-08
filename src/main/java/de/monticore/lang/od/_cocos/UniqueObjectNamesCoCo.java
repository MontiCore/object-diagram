/*
 * Copyright (c)  RWTH Aachen. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
package de.monticore.lang.od._cocos;

import de.monticore.lang.od._ast.ASTODObject;
import de.monticore.lang.od._symboltable.ODObjectSymbol;
import de.monticore.symboltable.Symbol;
import de.se_rwth.commons.logging.Log;

import java.util.Collection;
import java.util.Optional;

public class UniqueObjectNamesCoCo implements ODASTODObjectCoCo {

  @Override public void check(ASTODObject node) {
    Optional<? extends Symbol> symbol = node.getSymbol();
    if (symbol.isPresent() && symbol.get() instanceof ODObjectSymbol) {
      ODObjectSymbol objectSymbol = (ODObjectSymbol) symbol.get();
      Collection<ODObjectSymbol> symbols = objectSymbol.getEnclosingScope()
          .resolveMany(objectSymbol.getName(), ODObjectSymbol.KIND);
      if (symbols.size() != 1) {
        Log.error("Violation of CoCo 'UniqueObjectNamesCoCo'", node.get_SourcePositionStart());
      }
    }

  }
}
