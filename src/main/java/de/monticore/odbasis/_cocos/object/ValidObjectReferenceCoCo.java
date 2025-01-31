/* (c) https://github.com/MontiCore/monticore */
package de.monticore.odbasis._cocos.object;

import de.monticore.expressions.expressionsbasis._ast.ASTNameExpression;
import de.monticore.odbasis._ast.ASTODAttribute;
import de.monticore.odbasis._ast.ASTODObject;
import de.monticore.odbasis._ast.ASTODSimpleAttributeValue;
import de.monticore.odbasis._ast.ASTODValue;
import de.monticore.odbasis._cocos.ODBasisASTODObjectCoCo;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.se_rwth.commons.logging.Log;

import java.util.Optional;

public class ValidObjectReferenceCoCo implements ODBasisASTODObjectCoCo {

  @Override
  public void check(ASTODObject node) {
    for (ASTODAttribute astodAttribute : node.getODAttributeList()) {
      if (astodAttribute.isPresentODValue()) {
        ASTODValue astodValue = astodAttribute.getODValue();
        if (astodValue instanceof ASTODSimpleAttributeValue) {
          ASTODSimpleAttributeValue simpleAttributeValue = (ASTODSimpleAttributeValue) astodValue;
          if (simpleAttributeValue.getExpression() instanceof ASTNameExpression && !this
              .checkReference((ASTNameExpression) simpleAttributeValue.getExpression(), node)) {
            Log.error("0x0D001: Violation of CoCo 'ValidObjectReferenceCoCo'",
                node.get_SourcePositionStart());
          }
        }
      }
    }
  }

  private boolean checkReference(ASTNameExpression astNameExpression, ASTODObject node) {
    Optional<VariableSymbol> symbol = Optional.empty();
    if (node.getEnclosingScope() != null) {
      symbol = node.getEnclosingScope().resolveVariable(astNameExpression.getName());
    }
    return symbol.isPresent();
  }

}
