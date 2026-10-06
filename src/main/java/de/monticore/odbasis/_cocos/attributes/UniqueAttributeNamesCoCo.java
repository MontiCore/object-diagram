// (c) https://github.com/MontiCore/monticore

package de.monticore.odbasis._cocos.attributes;

import de.monticore.odbasis._ast.ASTODObject;
import de.monticore.odbasis._cocos.ODBasisASTODObjectCoCo;
import de.se_rwth.commons.logging.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Checks that all complete attributes of an {@link ASTODObject} have unique names within that
 * object.
 * <p>
 * The CoCo only considers attributes that are marked as complete. If the same complete attribute
 * name occurs more than once in a single object, the object contains an invalid duplicate
 * attribute declaration and an error is reported at the start position of the duplicate
 * attribute.
 */
public class UniqueAttributeNamesCoCo implements ODBasisASTODObjectCoCo {

  public static final String ERROR_ATTRIBUTE_NAME_NOT_UNIQUE =
      "0x0D003: The attribute name '%s' is declared more than once within the same object.";

  @Override
  public void check(ASTODObject node) {
    List<String> attributeNames = new ArrayList<>();
    node.getODAttributeList().forEach(attribute -> {
      if (attribute.isPresentComplete()) {
        if (attributeNames.contains(attribute.getName())) {
          Log.error(String.format(ERROR_ATTRIBUTE_NAME_NOT_UNIQUE, attribute.getName()),
              attribute.get_SourcePositionStart());
        }
        else {
          attributeNames.add(attribute.getName());
        }
      }
    });
  }

}
