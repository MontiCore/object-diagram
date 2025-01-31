/* (c) https://github.com/MontiCore/monticore */

package de.monticore.odlink._cocos.link;

import de.monticore.odlink._ast.ASTODLink;
import de.monticore.odlink._cocos.ODLinkASTODLinkCoCo;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.monticore.types.check.SymTypeExpression;
import de.se_rwth.commons.logging.Log;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Check whether named links consists of the same object type references on the left and right side
 * of the definition
 */
public class LinkEndConsistencyCoCo implements ODLinkASTODLinkCoCo {
  
  @Override
  public void check(ASTODLink node) {
    List<VariableSymbol> leftSymbols = node.getLeftReferenceNames().stream().map(refName -> {
      Optional<VariableSymbol> symbol = node.getEnclosingScope().resolveVariable(refName);
      if (symbol.isEmpty()) {
        Log.error("0x0D008: Violation of CoCo 'LinkEndConsistencyCoCo'",
            node.get_SourcePositionStart());
      }
      return symbol.get();
    }).collect(Collectors.toList());
    
    if (hasMultipleTypes(leftSymbols)) {
      Log.error("0x0D008: Violation of CoCo 'LinkEndConsistencyCoCo'",
          node.get_SourcePositionStart());
    }
    
    List<VariableSymbol> rightSymbols = node.getRightReferenceNames().stream().map(refName -> {
      Optional<VariableSymbol> symbol = node.getEnclosingScope().resolveVariable(refName);
      if (symbol.isEmpty()) {
        Log.error("0x0D008: Violation of CoCo 'LinkEndConsistencyCoCo'",
            node.get_SourcePositionStart());
      }
      return symbol.get();
    }).collect(Collectors.toList());
    
    if (hasMultipleTypes(rightSymbols)) {
      Log.error("0x0D008: Violation of CoCo 'LinkEndConsistencyCoCo'",
          node.get_SourcePositionStart());
    }
  }
  
  protected boolean hasMultipleTypes(List<VariableSymbol> symbols) {
    if (symbols.isEmpty()) {
      return false;
    }
    VariableSymbol firstSymbol = symbols.get(0);
    SymTypeExpression firstSymbolType = firstSymbol.getType();
    for (VariableSymbol symbol : symbols) {
      SymTypeExpression symbolType = symbol.getType();
      if (!firstSymbolType.deepEquals(symbolType)) {
        return true;
      }
    }
    return false;
  }
  
}
