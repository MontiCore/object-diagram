// (c) https://github.com/MontiCore/monticore

package de.monticore.odbasis._cocos.attributes;

import de.monticore.odbasis._ast.ASTODAttribute;
import de.monticore.odbasis._ast.ASTODObject;
import de.monticore.odbasis._cocos.ODBasisASTODObjectCoCo;
import de.se_rwth.commons.logging.Log;

/**
 * Checks that an attribute name is not used both as a partial attribute and as a complete
 * attribute within the same {@link ASTODObject}.
 * <p>
 * The CoCo compares all attributes of an object pairwise. If two attributes with the same name
 * are declared and at least one of them is a complete attribute declaration, the combination is
 * invalid because partial and complete declarations for the same attribute name must not be mixed.
 * In that case, an error is reported at the start position of the object.
 */
public class PartialAndCompleteAttributesCoCo implements ODBasisASTODObjectCoCo {

  public static final String ERROR_PARTIAL_AND_COMPLETE_ATTRIBUTE_MIX =
      "0x0D004: The attribute '%s' must not be declared as both a partial and a complete attribute within the same object.";

  @Override
  public void check(ASTODObject node) {
    for (int i = 0; i < node.getODAttributeList().size(); i++) {
      ASTODAttribute firstAttribute = node.getODAttributeList().get(i);
      for (int j = i + 1; j < node.getODAttributeList().size(); j++) {
        ASTODAttribute secondAttribute = node.getODAttributeList().get(j);
        if (firstAttribute.getName().equals(secondAttribute.getName())) {
          if (firstAttribute.isPresentComplete() || (!firstAttribute.isPresentComplete()
              && secondAttribute.isPresentComplete())) {
            Log.error(String.format(ERROR_PARTIAL_AND_COMPLETE_ATTRIBUTE_MIX,
                    firstAttribute.getName()),
                node.get_SourcePositionStart());
          }
        }
      }
    }
  }

}
