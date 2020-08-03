// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

/* (c) https://github.com/MontiCore/monticore */
package de.monticore.odlink._cocos.link;

import de.monticore.odlink._ast.ASTODLink;
import de.monticore.odlink._cocos.ODLinkASTODLinkCoCo;
import de.se_rwth.commons.logging.Log;

public class ValidLinkReferenceCoCo implements ODLinkASTODLinkCoCo {

  @Override
  public void check(ASTODLink node) {
    node.getLeftReferenceNames().forEach(refName -> {
      if (!node.getEnclosingScope().resolveVariable(refName).isPresent()) {
        Log.error(
            "Violation of CoCo 'ValidLinkReferenceCoCo': " + refName + " cannot be " + "resloved",
            node.get_SourcePositionStart());
      }
    });

    node.getRightReferenceNames().forEach(refName -> {
      if (!node.getEnclosingScope().resolveVariable(refName).isPresent()) {
        Log.error(
            "Violation of CoCo 'ValidLinkReferenceCoCo': " + refName + " cannot be " + "resloved",
            node.get_SourcePositionStart());
      }
    });
  }

}
