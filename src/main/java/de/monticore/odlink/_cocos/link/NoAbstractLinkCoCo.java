/* (c) https://github.com/MontiCore/monticore */

package de.monticore.odlink._cocos.link;

import de.monticore.odlink._ast.ASTODLink;
import de.monticore.odlink._cocos.ODLinkASTODLinkCoCo;
import de.monticore.umlmodifier._ast.ASTModifier;
import de.se_rwth.commons.logging.Log;

/**
 * Links with an abstract modifier are not allowed.
 */
public class NoAbstractLinkCoCo implements ODLinkASTODLinkCoCo {

  @Override
  public void check(ASTODLink node) {
    ASTModifier linkLeftModifier = node.getODLinkLeftSide().getModifier();
    if (linkLeftModifier.isAbstract()) {
      Log.error("Violation of CoCo 'NoAbstractAttributesCoCo'", node.get_SourcePositionStart());
    }

    ASTModifier linkRightModifier = node.getODLinkRightSide().getModifier();
    if (linkRightModifier.isAbstract()) {
      Log.error("Violation of CoCo 'NoAbstractAttributesCoCo'", node.get_SourcePositionStart());
    }
  }

}
