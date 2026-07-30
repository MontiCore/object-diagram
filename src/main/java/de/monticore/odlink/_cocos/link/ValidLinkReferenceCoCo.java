/* (c) https://github.com/MontiCore/monticore */

package de.monticore.odlink._cocos.link;

import de.monticore.odlink._ast.ASTODLink;
import de.monticore.odlink._cocos.ODLinkASTODLinkCoCo;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.se_rwth.commons.logging.Log;

import java.util.Optional;

/**
 * Checks that every reference used in an {@link ASTODLink} can be resolved in the enclosing
 * scope.
 * <p>
 * The CoCo validates the left and right reference lists independently. Every referenced name on
 * the left side and every referenced name on the right side must resolve to an existing
 * {@link VariableSymbol}; otherwise an error is reported at the start position of the link.
 */
public class ValidLinkReferenceCoCo implements ODLinkASTODLinkCoCo {

  public static final String ERROR_LEFT_REFERENCE_UNRESOLVED =
      "0x0D00C: The left reference '%s' of a named link cannot be resolved in the enclosing scope.";

  public static final String ERROR_RIGHT_REFERENCE_UNRESOLVED =
      "0x0D00D: The right reference '%s' of a named link cannot be resolved in the enclosing scope.";

  @Override
  public void check(ASTODLink node) {
    node.getLeftReferenceNames().forEach(refName -> {
      Optional<VariableSymbol> variableSymbol = node.getEnclosingScope().resolveVariable(refName);
      if (variableSymbol.isEmpty()) {
        Log.error(String.format(ERROR_LEFT_REFERENCE_UNRESOLVED, refName), node.get_SourcePositionStart());
      }
    });

    node.getRightReferenceNames().forEach(refName -> {
      if (node.getEnclosingScope().resolveVariable(refName).isEmpty()) {
        Log.error(String.format(ERROR_RIGHT_REFERENCE_UNRESOLVED, refName), node.get_SourcePositionStart());
      }
    });
  }

}

