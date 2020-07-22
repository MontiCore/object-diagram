// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

/* (c) https://github.com/MontiCore/monticore */
package de.monticore.odbasis._cocos.object;

import de.monticore.odbasis._ast.ASTODAttribute;
import de.monticore.odbasis._ast.ASTODName;
import de.monticore.odbasis._ast.ASTODObject;
import de.monticore.odbasis._ast.ASTODValue;
import de.monticore.odbasis._cocos.ODBasisASTODObjectCoCo;
import de.monticore.odbasis._symboltable.ODNamedObjectSymbol;
import de.se_rwth.commons.logging.Log;

import java.util.Optional;

public class ValidObjectReferenceCoCo implements ODBasisASTODObjectCoCo {

  @Override
  public void check(ASTODObject node) {
    for (ASTODAttribute astodAttribute : node.getODAttributesList()) {
      if (astodAttribute.isPresentODValue()) {
        ASTODValue astodValue = astodAttribute.getODValue();
        if (astodValue instanceof ASTODName && !this.checkReference((ASTODName) astodValue, node)) {
          Log.error("Violation of CoCo 'ValidObjectReferenceCoCo'", node.get_SourcePositionStart());
        }
      }
    }
  }

  private boolean checkReference(ASTODName astodName, ASTODObject node) {
    Optional<ODNamedObjectSymbol> symbol = Optional.empty();
    if (node.getEnclosingScope() != null) {
      symbol = node.getEnclosingScope().resolveODNamedObject(astodName.getName());
    }
    return symbol.isPresent();
  }

}
