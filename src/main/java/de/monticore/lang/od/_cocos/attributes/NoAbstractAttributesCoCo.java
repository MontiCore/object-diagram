/*
 * Copyright (c) 2017, MontiCore. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
package de.monticore.lang.od._cocos.attributes;

import de.monticore.common.common._ast.ASTModifier;
import de.monticore.lang.od._ast.ASTODAttribute;
import de.monticore.lang.od._cocos.ODASTODAttributeCoCo;
import de.se_rwth.commons.logging.Log;

import java.util.Optional;

/**
 * Attributes shouldn't be abstract.
 */
public class NoAbstractAttributesCoCo implements ODASTODAttributeCoCo {
  @Override public void check(ASTODAttribute node) {
    Optional<ASTModifier> attrModifier = node.getModifier();
    if (attrModifier.isPresent() && attrModifier.get().isAbstract()) {
      Log.error("Violation of CoCo 'NoAbstractAttributesCoCo'",
          node.get_SourcePositionStart());
    }
  }
}
