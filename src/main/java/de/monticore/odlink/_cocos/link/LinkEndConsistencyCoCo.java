/* (c) https://github.com/MontiCore/monticore */

package de.monticore.odlink._cocos.link;

import de.monticore.odlink._ast.ASTODLink;
import de.monticore.odlink._cocos.ODLinkASTODLinkCoCo;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.monticore.types.check.SymTypeExpression;
import de.se_rwth.commons.logging.Log;

import java.util.List;
import java.util.Optional;

/**
 * Checks that both sides of a named link are internally type-consistent.
 * <p>
 * For each side, all referenced variables must be resolvable in the enclosing scope and all
 * resolved variables on that side must have the same {@link SymTypeExpression}.
 * The left and right side are checked independently; this CoCo does not compare the two sides
 * with each other.
 */
public class LinkEndConsistencyCoCo implements ODLinkASTODLinkCoCo {
  
  public static final String ERROR_UNRESOLVED_REFERENCE =
      "0x0D008: The %s reference '%s' of the named link cannot be resolved in the enclosing scope.";
  
  public static final String ERROR_INCONSISTENT_REFERENCE_TYPES =
      "0x0D009: All references on the %s of a named link must resolve to variables of the same type, but '%s' resolves to type '%s' and '%s' resolves to type '%s'.";
  
  @Override
  public void check(ASTODLink node) {
    checkReferenceSide(node, node.getLeftReferenceNames(), "left side");
    checkReferenceSide(node, node.getRightReferenceNames(), "right side");
  }
  
  protected void checkReferenceSide(ASTODLink node, List<String> referenceNames, String sideName) {
    String firstReferenceName = null;
    SymTypeExpression firstReferenceType = null;
    
    for (String referenceName : referenceNames) {
      Optional<VariableSymbol> symbol = node.getEnclosingScope().resolveVariable(referenceName);
      if (symbol.isEmpty()) {
        Log.error(String.format(ERROR_UNRESOLVED_REFERENCE, sideName, referenceName),
            node.get_SourcePositionStart());
      }
      else {
        SymTypeExpression currentType = symbol.get().getType();
        if (firstReferenceType == null) {
          firstReferenceName = referenceName;
          firstReferenceType = currentType;
        }
        else if (!firstReferenceType.deepEquals(currentType)) {
          Log.error(String.format(ERROR_INCONSISTENT_REFERENCE_TYPES, sideName, firstReferenceName,
              firstReferenceType.printFullName(), referenceName, currentType.printFullName()), node.get_SourcePositionStart());
          return;
        }
      }
    }
  }
  
}
