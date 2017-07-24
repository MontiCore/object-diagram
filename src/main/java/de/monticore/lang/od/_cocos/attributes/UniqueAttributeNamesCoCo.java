/*
 * Copyright (c) 2017, MontiCore. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
package de.monticore.lang.od._cocos.attributes;

import de.monticore.lang.od._ast.ASTODObject;
import de.monticore.lang.od._cocos.ODASTODObjectCoCo;
import de.se_rwth.commons.logging.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Checks whether the attribute names within an object are unique
 */
public class UniqueAttributeNamesCoCo implements ODASTODObjectCoCo {

  @Override public void check(ASTODObject node) {
    List<String> attributeNames = new ArrayList<>();
    node.getODAttributes().forEach(attribute -> {
      if (attribute.completeIsPresent()) {
        if (attributeNames.contains(attribute.getName())) {
          Log.error("Violation of CoCo 'UniqueAttributeNamesCoCo'",
              attribute.get_SourcePositionStart());
        }
        else {
          attributeNames.add(attribute.getName());
        }
      }
    });
  }

}
