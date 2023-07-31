/* (c) https://github.com/MontiCore/monticore */

package de.monticore.odbasis._cocos.names;

import de.monticore.odbasis._ast.ASTODNamedObject;
import de.monticore.odbasis._cocos.ODBasisASTODNamedObjectCoCo;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.se_rwth.commons.logging.Log;

import java.util.List;

public class UniqueObjectNamesCoCo implements ODBasisASTODNamedObjectCoCo {
  
  @Override
  public void check(ASTODNamedObject node) {
    VariableSymbol symbol = node.getSymbol();
    List<VariableSymbol> symbols = symbol.getEnclosingScope().resolveVariableMany(symbol.getName());
    if (symbols.size() != 1) {
      Log.error("Violation of CoCo 'UniqueObjectNamesCoCo'", node.get_SourcePositionStart());
    }
  }
  
}
