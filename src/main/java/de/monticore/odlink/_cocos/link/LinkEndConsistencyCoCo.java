// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

/* (c) https://github.com/MontiCore/monticore */
package de.monticore.odlink._cocos.link;

import de.monticore.odlink._ast.ASTODLink;
import de.monticore.odlink._cocos.ODLinkASTODLinkCoCo;
import de.monticore.symbols.basicsymbols._symboltable.BasicSymbolsSymbols2Json;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
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
      if (!symbol.isPresent()) {
        Log.error("Violation of CoCo 'LinkEndConsistencyCoCo'", node.get_SourcePositionStart());
      }
      return symbol.get();
    }).collect(Collectors.toList());

    /* TODO SH: Is there any nicer way to check equality? */
    BasicSymbolsSymbols2Json tablePrinter = new BasicSymbolsSymbols2Json();
    tablePrinter.visit(leftSymbols.get(0));
    String firstTypeJSON = tablePrinter.getSerializedString();
    for (VariableSymbol sym : leftSymbols) {
      tablePrinter = new BasicSymbolsSymbols2Json();
      tablePrinter.visit(sym);
      String typeJSON = tablePrinter.getSerializedString();

      if (!firstTypeJSON.equals(typeJSON)) {
        Log.error("Violation of CoCo 'LinkEndConsistencyCoCo'", node.get_SourcePositionStart());
      }
    }

    /* TODO SH: Is there any nicer way to check equality? */
    List<VariableSymbol> rightSymbols = node.getRightReferenceNames().stream().map(refName -> {
      Optional<VariableSymbol> symbol = node.getEnclosingScope().resolveVariable(refName);
      if (!symbol.isPresent()) {
        Log.error("Violation of CoCo 'LinkEndConsistencyCoCo'", node.get_SourcePositionStart());
      }
      return symbol.get();
    }).collect(Collectors.toList());

    tablePrinter = new BasicSymbolsSymbols2Json();
    tablePrinter.visit(rightSymbols.get(0));
    firstTypeJSON = tablePrinter.getSerializedString();
    for (VariableSymbol sym : rightSymbols) {
      tablePrinter = new BasicSymbolsSymbols2Json();
      tablePrinter.visit(sym);
      String typeJSON = tablePrinter.getSerializedString();

      if (!firstTypeJSON.equals(typeJSON)) {
        Log.error("Violation of CoCo 'LinkEndConsistencyCoCo'", node.get_SourcePositionStart());
      }
    }
  }

}
