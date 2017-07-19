/*
 * Copyright (c) 2017 RWTH Aachen. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
package de.monticore.lang.od._cocos;

import de.monticore.lang.od._ast.ASTODName;
import de.monticore.lang.od._ast.ASTODObject;
import de.monticore.lang.od._ast.ASTODValue;
import de.monticore.lang.od._symboltable.ODObjectSymbol;
import de.se_rwth.commons.Symbol;
import de.se_rwth.commons.logging.Log;

import java.util.Optional;

public class ValidObjectReferenceCoCo implements ODASTODObjectCoCo {

  @Override public void check(ASTODObject node) {
    node.getODAttributes().stream().forEach(astodAttribute -> {
      Optional<ASTODValue> astodValue = astodAttribute.getODValue();
      if (astodValue.isPresent() && astodValue.get() instanceof ASTODName && !this
          .checkReference((ASTODName) (astodValue.get()), node)) {
        Log.error("Violation of CoCo 'ValidObjectReferenceCoCo'", node.get_SourcePositionStart());
      }
    });
  }

  private boolean checkReference(ASTODName astodName, ASTODObject node) {
    Optional<? extends Symbol> symbol = Optional.empty();
    if (node.getSymbol().isPresent()) {
      if (astodName.getName().isPresent()) {
        symbol = node.getSymbol().get().getEnclosingScope()
            .resolve(astodName.getName().get(), ODObjectSymbol.KIND);
      }
      else if (astodName.getODSpecialName().isPresent()) {
        symbol = node.getSymbol().get().getEnclosingScope()
            .resolve(astodName.getODSpecialName().get(), ODObjectSymbol.KIND);
      }
    }
    return symbol.isPresent();
  }
}
