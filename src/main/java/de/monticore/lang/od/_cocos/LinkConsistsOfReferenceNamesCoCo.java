/*
 * Copyright (c)  RWTH Aachen. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
package de.monticore.lang.od._cocos;

import de.monticore.lang.od._ast.ASTObjectDiagram;
import de.se_rwth.commons.logging.Log;

public class LinkConsistsOfReferenceNamesCoCo implements ODASTObjectDiagramCoCo {

  @Override public void check(ASTObjectDiagram node) {
    node.getODLinks().stream().forEach(astodLink -> {
      if (astodLink.getRightReferenceNames() == null || astodLink.getRightReferenceNames().isEmpty()
          || astodLink.getLeftReferenceNames() == null || astodLink.getLeftReferenceNames()
          .isEmpty()) {
        Log.error("Violation of CoCo 'LinkConsistsOfReferenceNamesCoCo'",
            astodLink.get_SourcePositionStart());
      }
    });
  }
}
