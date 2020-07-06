// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore
package de.monticore.odbasics._cocos.attributes;

import de.monticore.odbasics._ast.ASTODObject;
import de.monticore.odbasics._cocos.ODBasicsASTODObjectCoCo;
import de.se_rwth.commons.logging.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Checks whether the attribute names within an object are unique
 */
public class UniqueAttributeNamesCoCo implements ODBasicsASTODObjectCoCo {

  @Override
  public void check(ASTODObject node) {
    List<String> attributeNames = new ArrayList<>();
    node.getODAttributeList().forEach(attribute -> {
      if (attribute.isPresentComplete()) {
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
