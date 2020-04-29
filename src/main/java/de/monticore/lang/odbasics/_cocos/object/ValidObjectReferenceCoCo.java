/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.odbasics._cocos.object;

import de.monticore.lang.odbasics._ast.ASTODAttribute;
import de.monticore.lang.odbasics._ast.ASTODName;
import de.monticore.lang.odbasics._ast.ASTODObject;
import de.monticore.lang.odbasics._ast.ASTODValue;
import de.monticore.lang.odbasics._cocos.ODBasicsASTODObjectCoCo;
import de.monticore.lang.odbasics._symboltable.ODObjectSymbol;
import de.se_rwth.commons.logging.Log;

import java.util.Optional;

public class ValidObjectReferenceCoCo implements ODBasicsASTODObjectCoCo {

  @Override
  public void check(ASTODObject node) {
    for (ASTODAttribute astodAttribute : node.getODAttributeList()) {
      if (astodAttribute.isPresentODValue()) {
        ASTODValue astodValue = astodAttribute.getODValue();
        if (astodValue instanceof ASTODName && !this.checkReference((ASTODName) astodValue, node)) {
          Log.error("Violation of CoCo 'ValidObjectReferenceCoCo'", node.get_SourcePositionStart());
        }
      }
    }
  }

  private boolean checkReference(ASTODName astodName, ASTODObject node) {
    Optional<ODObjectSymbol> symbol = Optional.empty();
    if (node.isPresentSymbol()) {
      symbol = node.getSymbol().getEnclosingScope().resolveODObject(astodName.getName());
    }
    return symbol.isPresent();
  }

}
