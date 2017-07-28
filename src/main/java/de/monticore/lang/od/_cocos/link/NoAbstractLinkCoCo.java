/*
 * Copyright (c) 2017, MontiCore. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
package de.monticore.lang.od._cocos.link;

import de.monticore.common.common._ast.ASTModifier;
import de.monticore.lang.od._ast.ASTODLink;
import de.monticore.lang.od._cocos.ODASTODLinkCoCo;
import de.se_rwth.commons.logging.Log;

import java.util.Optional;

/**
 * Links with an abstract modifier are not allowed.
 */
public class NoAbstractLinkCoCo implements ODASTODLinkCoCo {
  @Override public void check(ASTODLink node) {
    Optional<ASTModifier> linkLeftModifier = node.getLeftModifier();
    if (linkLeftModifier.isPresent() && linkLeftModifier.get().isAbstract()) {
      Log.error("Violation of CoCo 'NoAbstractAttributesCoCo'",
          node.get_SourcePositionStart());
    }

    Optional<ASTModifier> linkRightModifier = node.getRightModifier();
    if (linkRightModifier.isPresent() && linkRightModifier.get().isAbstract()) {
      Log.error("Violation of CoCo 'NoAbstractAttributesCoCo'",
          node.get_SourcePositionStart());
    }
  }
}