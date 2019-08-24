/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.od._cocos.names;

import de.monticore.lang.od._ast.ASTODName;
import de.monticore.lang.od._cocos.ODASTODNameCoCo;
import de.se_rwth.commons.logging.Log;

public class NoSpecialNameCoCo implements ODASTODNameCoCo {
  
  @Override public void check(ASTODName name) {
    if (name.isPresentSpecialName()) {
      Log.error("The object name " + name.getSpecialName() + " is not supported!");
    }
  }
}
