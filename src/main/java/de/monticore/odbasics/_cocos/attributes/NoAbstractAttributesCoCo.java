// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore
package de.monticore.odbasics._cocos.attributes;

import de.monticore.odbasics._ast.ASTODAttribute;
import de.monticore.odbasics._cocos.ODBasicsASTODAttributeCoCo;
import de.monticore.umlmodifier._ast.ASTModifier;
import de.se_rwth.commons.logging.Log;

/**
 * Attributes shouldn't be abstract.
 */
public class NoAbstractAttributesCoCo implements ODBasicsASTODAttributeCoCo {

  @Override
  public void check(ASTODAttribute node) {
    ASTModifier attrModifier = node.getModifier();
    if (attrModifier.isAbstract()) {
      Log.error("Violation of CoCo 'NoAbstractAttributesCoCo'", node.get_SourcePositionStart());
    }
  }

}
