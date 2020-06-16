/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.odbasics._cocos.link;

import de.monticore.lang.odbasics._ast.ASTODLink;
import de.monticore.lang.odbasics._ast.ASTODName;
import de.monticore.lang.odbasics._ast.ASTODObject;
import de.monticore.lang.odbasics._ast.ASTObjectDiagram;
import de.monticore.lang.odbasics._cocos.ODBasicsASTObjectDiagramCoCo;
import de.monticore.lang.odbasics._symboltable.ODObjectSymbol;
import de.monticore.lang.odbasics.prettyprinter.ODPrettyPrinterDelegator;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedType;
import de.monticore.types.mcbasictypes._ast.ASTMCType;
import de.se_rwth.commons.logging.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Check whether named links consists of the same object type references on the left and right side
 * of the definition
 */
public class LinkEndConsistencyCoCo implements ODBasicsASTObjectDiagramCoCo {

  @Override
  public void check(ASTObjectDiagram node) {
    node.getODLinkList().forEach(link -> {
      if (link.isPresentName()) {
        // collect left link reference type names
        List<ASTMCType> leftRefTypes = getReferenceTypes(link, true, node);
        List<String> leftRefNames = new ArrayList<>();
        leftRefTypes
            .forEach(type -> leftRefNames.add(new ODPrettyPrinterDelegator().prettyprint(type)));
        // collect right link reference type names
        List<ASTMCType> rightRefTypes = getReferenceTypes(link, false, node);
        List<String> rightRefNames = new ArrayList<>();
        rightRefTypes
            .forEach(type -> rightRefNames.add(new ODPrettyPrinterDelegator().prettyprint(type)));
        // compare left and right reference names
        if (!((leftRefNames.isEmpty()
            || leftRefNames.stream().allMatch(leftRefNames.get(0)::equals) && (
            rightRefNames.isEmpty() || rightRefNames.stream()
                .allMatch(rightRefNames.get(0)::equals))))) {
          Log.error("Violation of CoCo 'LinkEndConsistencyCoCo'", link.get_SourcePositionStart());
        }

      }
    });
  }

  private List<ASTMCType> getReferenceTypes(ASTODLink link, boolean left,
      ASTObjectDiagram astObjectDiagram) {
    List<ASTMCType> astRefTypes = new ArrayList<>();
    if (left) {
      link.getODLinkLeftSide().getReferenceNameList().forEach(leftName -> {
        Optional<ASTMCType> astReferenceType = this.getASTReference(leftName, astObjectDiagram);
        if (astReferenceType.isPresent() && astReferenceType.get() instanceof ASTMCQualifiedType) {
          astRefTypes.add(astReferenceType.get());
        }
      });
    }
    else {
      link.getODLinkRightSide().getReferenceNameList().forEach(rightName -> {
        Optional<ASTMCType> astReferenceType = this.getASTReference(rightName, astObjectDiagram);
        if (astReferenceType.isPresent() && astReferenceType.get() instanceof ASTMCQualifiedType) {
          astRefTypes.add(astReferenceType.get());
        }
      });
    }

    return astRefTypes;
  }

  private Optional<ASTMCType> getASTReference(ASTODName astodName,
      ASTObjectDiagram astObjectDiagram) {
    Optional<ODObjectSymbol> symbol = Optional.empty();
    if (astObjectDiagram.isPresentSymbol()) {
      if (!astodName.getName().isEmpty()) {
        symbol = astObjectDiagram.getSpannedScope().resolveODObject(astodName.getName());
      }
    }

    if (symbol.isPresent()) {
      ODObjectSymbol odSymbol = symbol.get();
      ASTODObject astNode = odSymbol.getAstNode();
      if (astNode != null) {
        return Optional.of(astNode.getMCType());
      }
    }

    return Optional.empty();
  }

}
