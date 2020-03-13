/*
 * Copyright (c) 2017, MontiCore. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
package de.monticore.lang.odbasics.cocos.names;

import de.monticore.lang.odbasics._ast.ASTODObject;
import de.monticore.lang.odbasics._cocos.ODBasicsASTODObjectCoCo;
import de.monticore.lang.odbasics._symboltable.ODObjectSymbol;
import de.se_rwth.commons.logging.Log;

import java.util.Collection;

public class UniqueObjectNamesCoCo implements ODBasicsASTODObjectCoCo {

  @Override
  public void check(ASTODObject node) {
    ODObjectSymbol symbol = node.getSymbol();
    Collection<ODObjectSymbol> symbols = symbol.getEnclosingScope()
        .resolveODObjectMany(symbol.getName());
    if (symbols.size() != 1) {
      Log.error("Violation of CoCo 'UniqueObjectNamesCoCo'", node.get_SourcePositionStart());
    }
  }

}
