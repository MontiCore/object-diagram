/* (c) https://github.com/MontiCore/monticore */

package de.monticore.odbasis._cocos.object;

import de.monticore.odbasis._ast.ASTODObject;
import de.monticore.odbasis._cocos.ODBasisASTODObjectCoCo;
import de.monticore.symbols.basicsymbols._symboltable.TypeSymbol;
import de.se_rwth.commons.logging.Log;

import java.util.List;

/**
 * Checks that every object type used in an object diagram can be resolved to exactly one
 * available type symbol in the enclosing scope.
 * <p>
 * For each {@link ASTODObject} in the artifact, this CoCo resolves the object's declared type by
 * combining the current package, the import statements, and the declared type name. If no type
 * symbol can be found, the object type is considered undefined. If more than one type symbol can
 * be resolved, the type usage is considered ambiguous.
 */
public class ValidObjectTypeCoCo implements ODBasisASTODObjectCoCo {
  
  public static final String ERROR_TYPE_DEFINED_MULTIPLE_TIMES =
      "0xB0032: Type '%s' is defined more than once.";
  
  public static final String ERROR_TYPE_USED_BUT_UNDEFINED =
      "0xB0035: Type '%s' is used but not defined.";
  
  @Override
  public void check(ASTODObject node) {
    String typeName = node.getMCObjectType().printType();
    List<TypeSymbol> typeSymbols = node.getEnclosingScope().resolveTypeMany(typeName);
    
    if (typeSymbols.isEmpty()) {
      Log.error(String.format(ERROR_TYPE_USED_BUT_UNDEFINED, typeName),
          node.get_SourcePositionStart());
    }
    else if (typeSymbols.size() > 1) {
      Log.error(String.format(ERROR_TYPE_DEFINED_MULTIPLE_TIMES, typeName),
          node.get_SourcePositionStart());
    }
  }
}
