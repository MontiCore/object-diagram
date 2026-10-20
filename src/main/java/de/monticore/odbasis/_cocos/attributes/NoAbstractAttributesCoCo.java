// (c) https://github.com/MontiCore/monticore

package de.monticore.odbasis._cocos.attributes;

import de.monticore.odbasis._ast.ASTODAttribute;
import de.monticore.odbasis._cocos.ODBasisASTODAttributeCoCo;
import de.monticore.umlmodifier._ast.ASTModifier;
import de.se_rwth.commons.logging.Log;

/**
 * Checks that an {@link ASTODAttribute} is not declared with the {@code abstract} modifier.
 * <p>
 * Object diagram attributes represent concrete attribute values or declarations of objects.
 * Therefore, an abstract modifier is not allowed on such attributes. If an attribute is declared
 * as abstract, an error is reported at the start position of that attribute.
 */
public class NoAbstractAttributesCoCo implements ODBasisASTODAttributeCoCo {

  public static final String ERROR_ABSTRACT_ATTRIBUTE =
      "0x0D005: An object diagram attribute must not be declared abstract.";

  @Override
  public void check(ASTODAttribute node) {
    ASTModifier attrModifier = node.getModifier();
    if (attrModifier.isAbstract()) {
      Log.error(ERROR_ABSTRACT_ATTRIBUTE, node.get_SourcePositionStart());
    }
  }

}
