// (c) https://github.com/MontiCore/monticore

package de.monticore.odbasis._cocos.attributes;

import de.monticore.odbasis._ast.ASTODAttribute;
import de.monticore.odbasis._ast.ASTODObject;
import de.monticore.odbasis._cocos.ODBasisASTODObjectCoCo;
import de.se_rwth.commons.logging.Log;

/**
 * Attributes defined with a partial operator must not be defined in a complete attribute definition
 * and vice versa.
 */
public class PartialAndCompleteAttributesCoCo implements ODBasisASTODObjectCoCo {
  
  @Override
  public void check(ASTODObject node) {
    for (int i = 0; i < node.getODAttributeList().size(); i++) {
      ASTODAttribute firstAttribute = node.getODAttributeList().get(i);
      for (int j = i + 1; j < node.getODAttributeList().size(); j++) {
        ASTODAttribute secondAttribute = node.getODAttributeList().get(j);
        if (firstAttribute.getName().equals(secondAttribute.getName())) {
          if (firstAttribute.isPresentComplete() || (!firstAttribute.isPresentComplete()
              && secondAttribute.isPresentComplete())) {
            Log.error("Violation of CoCo 'PartialAndCompleteAttributesCoCo'",
                node.get_SourcePositionStart());
          }
        }
      }
    }
  }
  
}
