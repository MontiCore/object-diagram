/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.od._cocos.link;

import de.monticore.lang.od._ast.ASTODName;
import de.monticore.lang.od._ast.ASTObjectDiagram;
import de.monticore.lang.od._cocos.ODASTObjectDiagramCoCo;
import de.monticore.lang.od._symboltable.ODObjectSymbol;
import de.se_rwth.commons.Symbol;
import de.se_rwth.commons.logging.Log;

import java.util.Optional;
import java.util.stream.Stream;

public class ValidLinkReferenceCoCo implements ODASTObjectDiagramCoCo {

  @Override public void check(ASTObjectDiagram node) {
    node.getODLinkList().stream().forEach(astodLink -> {
      Stream.concat(astodLink.getLeftReferenceNameList().stream(),
          astodLink.getRightReferenceNameList().stream())
          .forEach(astodName -> {
            if (!this.checkReference(astodName, node)) {
              Log.error("Violation of CoCo 'ValidLinkReferenceCoCo'",
                  astodLink.get_SourcePositionStart());
            }
          });
    });
  }

  private boolean checkReference(ASTODName astodName, ASTObjectDiagram astObjectDiagram) {
    Optional<? extends Symbol> symbol = Optional.empty();
    if (astObjectDiagram.isPresentSymbol()) {
      if (astObjectDiagram.isPresentSpannedScope()) {
        symbol = astObjectDiagram.getSpannedScope()
            .resolve(astodName.getName(), ODObjectSymbol.KIND);
      }
    }
    return symbol.isPresent();
  }
}

