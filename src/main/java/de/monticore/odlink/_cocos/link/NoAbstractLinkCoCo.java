/* (c) https://github.com/MontiCore/monticore */

package de.monticore.odlink._cocos.link;

import de.monticore.odlink._ast.ASTODLink;
import de.monticore.odlink._cocos.ODLinkASTODLinkCoCo;
import de.monticore.umlmodifier._ast.ASTModifier;
import de.se_rwth.commons.logging.Log;

/**
 * Checks that no link end of an {@link ASTODLink} is declared with the {@code abstract} modifier.
 * <p>
 * This CoCo validates both link ends independently:
 * the modifier of the left link end and the modifier of the right link end must not be abstract.
 * If one side is abstract, an error is reported at the start position of the link.
 */
public class NoAbstractLinkCoCo implements ODLinkASTODLinkCoCo {

  public static final String ERROR_LEFT_SIDE_ABSTRACT =
      "0x0D00A: The left side of a link must not be declared abstract.";

  public static final String ERROR_RIGHT_SIDE_ABSTRACT =
      "0x0D00B: The right side of a link must not be declared abstract.";

  @Override
  public void check(ASTODLink node) {
    ASTModifier linkLeftModifier = node.getODLinkLeftSide().getModifier();
    if (linkLeftModifier.isAbstract()) {
      Log.error(ERROR_LEFT_SIDE_ABSTRACT, node.get_SourcePositionStart());
    }

    ASTModifier linkRightModifier = node.getODLinkRightSide().getModifier();
    if (linkRightModifier.isAbstract()) {
      Log.error(ERROR_RIGHT_SIDE_ABSTRACT, node.get_SourcePositionStart());
    }
  }

}
