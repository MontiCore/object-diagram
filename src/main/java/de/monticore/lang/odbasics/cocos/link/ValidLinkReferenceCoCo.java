/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.odbasics.cocos.link;

import de.monticore.lang.odbasics._ast.ASTODName;
import de.monticore.lang.odbasics._ast.ASTObjectDiagram;
import de.monticore.lang.odbasics._cocos.ODBasicsASTObjectDiagramCoCo;
import de.monticore.lang.odbasics._symboltable.ODObjectSymbol;
import de.se_rwth.commons.logging.Log;

import java.util.Optional;
import java.util.stream.Stream;

public class ValidLinkReferenceCoCo implements ODBasicsASTObjectDiagramCoCo {

  @Override
  public void check(ASTObjectDiagram node) {
    node.getODLinkList().forEach(astodLink -> Stream
        .concat(astodLink.getLeftReferenceNameList().stream(),
            astodLink.getRightReferenceNameList().stream()).forEach(astodName -> {
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

