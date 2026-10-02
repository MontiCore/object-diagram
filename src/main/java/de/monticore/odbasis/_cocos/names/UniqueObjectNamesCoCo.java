/* (c) https://github.com/MontiCore/monticore */

package de.monticore.odbasis._cocos.names;

import de.monticore.odbasis._ast.ASTODNamedObject;
import de.monticore.odbasis._cocos.ODBasisASTODNamedObjectCoCo;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.se_rwth.commons.logging.Log;

import java.util.List;

/**
 * Checks that each named object in an object diagram has a unique name in the enclosing scope.
 * <p>
 * The CoCo is only applied to named objects. For each named object, the symbol of the object is
 * resolved in its enclosing scope by name. If the name is not unique in that scope, the object
 * name is considered invalid and an error is reported at the start position of the object.
 */
public class UniqueObjectNamesCoCo implements ODBasisASTODNamedObjectCoCo {

  public static final String ERROR_OBJECT_NAME_NOT_UNIQUE =
      "0x0D002: The object name '%s' is not unique in the enclosing scope.";

  @Override
  public void check(ASTODNamedObject node) {
    VariableSymbol symbol = node.getSymbol();
    List<VariableSymbol> symbols = symbol.getEnclosingScope().resolveVariableMany(symbol.getName());
    if (symbols.size() != 1) {
      Log.error(String.format(ERROR_OBJECT_NAME_NOT_UNIQUE, symbol.getName()),
          node.get_SourcePositionStart());
    }
  }

}
