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

/**
 * Checks that every object attribute reference used inside an {@link ASTODObject} resolves to a
 * variable that is available in the enclosing scope of that object.
 * <p>
 * The CoCo inspects simple attribute values whose expression is a name expression. If such a name
 * cannot be resolved to a {@link VariableSymbol}, the attribute reference is considered invalid
 * and an error is reported at the start position of the object.
 */
public class ValidObjectReferenceCoCo implements ODBasisASTODObjectCoCo {

  public static final String ERROR_UNRESOLVED_OBJECT_REFERENCE =
      "0x0D001: The referenced object attribute '%s' cannot be resolved in the enclosing scope.";

  @Override
  public void check(ASTODObject node) {
    for (ASTODAttribute astodAttribute : node.getODAttributeList()) {
      if (astodAttribute.isPresentODValue()) {
        ASTODValue astodValue = astodAttribute.getODValue();
        if (astodValue instanceof ASTODSimpleAttributeValue simpleAttributeValue
            && simpleAttributeValue.getExpression() instanceof ASTNameExpression astNameExpression
            && !this.checkReference(astNameExpression, node)) {
          Log.error(String.format(ERROR_UNRESOLVED_OBJECT_REFERENCE, astNameExpression.getName()),
              node.get_SourcePositionStart());
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
