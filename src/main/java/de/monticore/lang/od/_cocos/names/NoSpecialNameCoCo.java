/*
 * Copyright (c) 2017, MontiCore. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
package de.monticore.lang.od._cocos.names;

import de.monticore.lang.od._ast.ASTODName;
import de.monticore.lang.od._cocos.ODASTODNameCoCo;
import de.se_rwth.commons.logging.Log;

public class NoSpecialNameCoCo implements ODASTODNameCoCo {
  
  @Override public void check(ASTODName name) {
    if (name.isPresentODSpecialName()) {
      Log.error("The object name " + name.getODSpecialName() + " is not supported!");
    }
  }
}
