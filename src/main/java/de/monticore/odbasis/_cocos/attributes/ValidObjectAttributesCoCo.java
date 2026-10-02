package de.monticore.odbasis._cocos.attributes;

import de.monticore.odbasis._ast.ASTODAttribute;
import de.monticore.odbasis._ast.ASTODObject;
import de.monticore.odbasis._cocos.ODBasisASTODObjectCoCo;
import de.monticore.symbols.basicsymbols._symboltable.TypeSymbol;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.monticore.symbols.oosymbols._symboltable.FieldSymbol;
import de.se_rwth.commons.logging.Log;

import java.util.Optional;

/**
 * Checks that every attribute declared in an {@link ASTODObject} is defined as a field in the
 * referenced type of the object.
 * <p>
 * For each attribute of an object, this CoCo attempts to resolve the attribute name to a
 * {@link FieldSymbol} in the enclosing scope. If an attribute name cannot be resolved, it is
 * considered undefined and an error is reported at the start position of that attribute.
 */
public class ValidObjectAttributesCoCo implements ODBasisASTODObjectCoCo {
  
  public static final String ERROR_ATTRIBUTE_NOT_DEFINED =
      "0x0D006: The attribute '%s' is not defined as a field in type '%s'.";
  
  @Override
  public void check(ASTODObject node) {
    Optional<TypeSymbol> objectType =
        node.getEnclosingScope().resolveType(node.getMCObjectType().printType());
    if (objectType.isPresent()) {
      for (ASTODAttribute attribute : node.getODAttributeList()) {
        Optional<VariableSymbol> field =
            objectType.get().getSpannedScope().resolveVariableDown(attribute.getName());
        if (field.isEmpty()) {
          Log.error(ERROR_ATTRIBUTE_NOT_DEFINED.formatted(attribute.getName(), node.getName()),
              attribute.get_SourcePositionStart());
        }
      }
    }
  }
}
