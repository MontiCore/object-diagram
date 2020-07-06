// (c) https://github.com/MontiCore/monticore

/* (c) https://github.com/MontiCore/monticore */
package de.monticore.odbasics._cocos.link;

import de.monticore.odbasics._ast.ASTODLink;
import de.monticore.odbasics._cocos.ODBasicsASTODLinkCoCo;
import de.monticore.umlmodifier._ast.ASTModifier;
import de.se_rwth.commons.logging.Log;

/**
 * Links with an abstract modifier are not allowed.
 */
public class NoAbstractLinkCoCo implements ODBasicsASTODLinkCoCo {

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
