/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.odbasics._cocos.names;

import de.monticore.lang.odbasics._ast.ASTODObject;
import de.monticore.lang.odbasics._cocos.ODBasicsASTODObjectCoCo;
import de.monticore.lang.odbasics._symboltable.ODObjectSymbol;
import de.se_rwth.commons.logging.Log;

import java.util.Collection;

public class UniqueObjectNamesCoCo implements ODBasicsASTODObjectCoCo {

  @Override
  public void check(ASTODObject node) {

    // no symbols for anonymous objects
    if("".equals(node.getName())){
      return;
    }

    ODObjectSymbol symbol = node.getSymbol();
    Collection<ODObjectSymbol> symbols = symbol.getEnclosingScope()
        .resolveODObjectMany(symbol.getName());
    if (symbols.size() != 1) {
      Log.error("Violation of CoCo 'UniqueObjectNamesCoCo'", node.get_SourcePositionStart());
    }
  }

}
