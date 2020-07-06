// (c) https://github.com/MontiCore/monticore

/* (c) https://github.com/MontiCore/monticore */
package de.monticore.odbasics._cocos.link;

import de.monticore.odbasics._ast.ASTODName;
import de.monticore.odbasics._ast.ASTObjectDiagram;
import de.monticore.odbasics._cocos.ODBasicsASTObjectDiagramCoCo;
import de.monticore.odbasics._symboltable.ODObjectSymbol;
import de.se_rwth.commons.logging.Log;

import java.util.Optional;
import java.util.stream.Stream;

public class ValidLinkReferenceCoCo implements ODBasicsASTObjectDiagramCoCo {

  @Override
  public void check(ASTObjectDiagram node) {
    node.getODLinkList().forEach(astodLink -> Stream
        .concat(astodLink.getODLinkLeftSide().getReferenceNameList().stream(),
            astodLink.getODLinkRightSide().getReferenceNameList().stream()).forEach(astodName -> {
          if (!this.checkReference(astodName, node)) {
            Log.error("Violation of CoCo 'ValidLinkReferenceCoCo'",
                astodLink.get_SourcePositionStart());
          }
        }));
  }

  private boolean checkReference(ASTODName astodName, ASTObjectDiagram astObjectDiagram) {
    Optional<ODObjectSymbol> symbol = Optional.empty();
    if (astObjectDiagram.isPresentSymbol()) {
      symbol = astObjectDiagram.getSpannedScope().resolveODObject(astodName.getName());
    }
    return symbol.isPresent();
  }

}

