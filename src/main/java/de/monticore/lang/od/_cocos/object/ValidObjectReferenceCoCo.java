/*
 * Copyright (c) 2017, MontiCore. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
package de.monticore.lang.od._cocos.object;

import de.monticore.lang.od._ast.ASTODName;
import de.monticore.lang.od._ast.ASTODObject;
import de.monticore.lang.od._ast.ASTODValue;
import de.monticore.lang.od._cocos.ODASTODObjectCoCo;
import de.monticore.lang.od._symboltable.ODObjectSymbol;
import de.se_rwth.commons.Symbol;
import de.se_rwth.commons.logging.Log;

import java.util.Optional;

public class ValidObjectReferenceCoCo implements ODASTODObjectCoCo {

  @Override public void check(ASTODObject node) {
    node.getODAttributeList().stream().forEach(astodAttribute -> {
      Optional<ASTODValue> astodValue = astodAttribute.getODValueOpt();
      if (astodValue.isPresent() && astodValue.get() instanceof ASTODName && !this
          .checkReference((ASTODName) (astodValue.get()), node)) {
        Log.error("Violation of CoCo 'ValidObjectReferenceCoCo'", node.get_SourcePositionStart());
      }
    });
  }

  private boolean checkReference(ASTODName astodName, ASTODObject node) {
    Optional<? extends Symbol> symbol = Optional.empty();
    if (node.isPresentSymbol()) {
      if (astodName.isPresentName()) {
        symbol = node.getSymbol().getEnclosingScope()
            .resolve(astodName.getName(), ODObjectSymbol.KIND);
      }
      else if (astodName.isPresentODSpecialName()) {
        symbol = node.getSymbol().getEnclosingScope()
            .resolve(astodName.getODSpecialName(), ODObjectSymbol.KIND);
      }
    }
    return symbol.isPresent();
  }
}
