// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

/* (c) https://github.com/MontiCore/monticore */
package de.monticore.odlink._cocos.link;

import de.monticore.odbasis._symboltable.ODNamedObjectSymbol;
import de.monticore.odlink._ast.ASTODLink;
import de.monticore.odlink._cocos.ODLinkASTODLinkCoCo;
import de.monticore.symbols.basicsymbols._symboltable.BasicSymbolsSymbolTablePrinter;
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
    List<SymTypeExpression> leftTypes = node.getLeftReferenceNames().stream().map(refName -> {
      Optional<ODNamedObjectSymbol> symbol = node.getEnclosingScope().resolveODNamedObject(refName);
      if (!symbol.isPresent()) {
        Log.error("Violation of CoCo 'LinkEndConsistencyCoCo'", node.get_SourcePositionStart());
      }
      return symbol.get().getType();
    }).collect(Collectors.toList());

    /* TODO SH: Is there any nicer way to check equality? */
    BasicSymbolsSymbolTablePrinter tablePrinter = new BasicSymbolsSymbolTablePrinter();
    tablePrinter.serializeVariableType(leftTypes.get(0));
    String firstTypeJSON = tablePrinter.getSerializedString();
    for (SymTypeExpression type : leftTypes) {
      tablePrinter = new BasicSymbolsSymbolTablePrinter();
      tablePrinter.serializeVariableType(type);
      String typeJSON = tablePrinter.getSerializedString();

      if (!firstTypeJSON.equals(typeJSON)) {
        Log.error("Violation of CoCo 'LinkEndConsistencyCoCo'", node.get_SourcePositionStart());
      }
    }

    /* TODO SH: Is there any nicer way to check equality? */
    List<SymTypeExpression> rightTypes = node.getRightReferenceNames().stream().map(refName -> {
      Optional<ODNamedObjectSymbol> symbol = node.getEnclosingScope().resolveODNamedObject(refName);
      if (!symbol.isPresent()) {
        Log.error("Violation of CoCo 'LinkEndConsistencyCoCo'", node.get_SourcePositionStart());
      }
      return symbol.get().getType();
    }).collect(Collectors.toList());

    tablePrinter = new BasicSymbolsSymbolTablePrinter();
    tablePrinter.serializeVariableType(rightTypes.get(0));
    firstTypeJSON = tablePrinter.getSerializedString();
    for (SymTypeExpression type : rightTypes) {
      tablePrinter = new BasicSymbolsSymbolTablePrinter();
      tablePrinter.serializeVariableType(type);
      String typeJSON = tablePrinter.getSerializedString();

      if (!firstTypeJSON.equals(typeJSON)) {
        Log.error("Violation of CoCo 'LinkEndConsistencyCoCo'", node.get_SourcePositionStart());
      }
    }
  }

}
