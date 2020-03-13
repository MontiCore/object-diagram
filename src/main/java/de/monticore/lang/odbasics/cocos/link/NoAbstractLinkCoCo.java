/*
 * Copyright (c) 2017, MontiCore. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
package de.monticore.lang.odbasics.cocos.link;

import de.monticore.lang.odbasics._ast.ASTODLink;
import de.monticore.lang.odbasics._cocos.ODBasicsASTODLinkCoCo;
import de.monticore.umlmodifier._ast.ASTModifier;
import de.se_rwth.commons.logging.Log;

/**
 * Links with an abstract modifier are not allowed.
 */
public class NoAbstractLinkCoCo implements ODBasicsASTODLinkCoCo {

  @Override
  public void check(ASTODLink node) {
    ASTModifier linkLeftModifier = node.getLeftModifier();
    if (linkLeftModifier.isAbstract()) {
      Log.error("Violation of CoCo 'NoAbstractAttributesCoCo'", node.get_SourcePositionStart());
    }

    ASTModifier linkRightModifier = node.getRightModifier();
    if (linkRightModifier.isAbstract()) {
      Log.error("Violation of CoCo 'NoAbstractAttributesCoCo'", node.get_SourcePositionStart());
    }
  }

}
