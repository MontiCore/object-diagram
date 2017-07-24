/*
 * Copyright (c) 2017, MontiCore. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
package de.monticore.lang.od._cocos.link;

import de.monticore.ast.ASTNode;
import de.monticore.lang.od._ast.ASTODLink;
import de.monticore.lang.od._ast.ASTODName;
import de.monticore.lang.od._ast.ASTODObject;
import de.monticore.lang.od._ast.ASTObjectDiagram;
import de.monticore.lang.od._cocos.ODASTObjectDiagramCoCo;
import de.monticore.lang.od._symboltable.ODObjectSymbol;
import de.monticore.symboltable.CommonSymbol;
import de.monticore.types.types._ast.ASTReferenceType;
import de.monticore.types.types._ast.ASTSimpleReferenceType;
import de.se_rwth.commons.logging.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Check whether named links consists of the same object type references on the left and right side of the definition
 */
public class LinkEndConsistencyCoCo implements ODASTObjectDiagramCoCo {

  @Override public void check(ASTObjectDiagram node) {
    node.getODLinks().forEach(link -> {
      if (link.getName().isPresent()) {
        // collect left link reference type names
        List<ASTSimpleReferenceType> leftRefTypes = getReferenceTypes(link, true, node);
        List<List<String>> leftRefNames = new ArrayList<>();
        leftRefTypes.forEach(type -> {
          leftRefNames.add(type.getNames());
        });
        // collect right link reference type names
        List<ASTSimpleReferenceType> rightRefTypes = getReferenceTypes(link, false, node);
        List<List<String>> rightRefNames = new ArrayList<>();
        rightRefTypes.forEach(type -> {
          rightRefNames.add(type.getNames());
        });
        // compare left and right reference names
        if (!((leftRefNames.isEmpty() || leftRefNames.stream()
            .allMatch(leftRefNames.get(0)::equals) && (rightRefNames.isEmpty() || rightRefNames
            .stream().allMatch(rightRefNames.get(0)::equals))))) {
          Log.error("Violation of CoCo 'LinkEndConsistencyCoCo'",
              link.get_SourcePositionStart());
        }

      }
    });
  }

  private List<ASTSimpleReferenceType> getReferenceTypes(ASTODLink link, boolean left,
      ASTObjectDiagram astObjectDiagram) {
    List<ASTSimpleReferenceType> astRefTypes = new ArrayList<>();
    if (left) {
      link.getLeftReferenceNames().forEach(leftName -> {
        Optional<ASTReferenceType> astReferenceType = this
            .getASTReference(leftName, astObjectDiagram);
        if (astReferenceType.isPresent() && astReferenceType
            .get() instanceof ASTSimpleReferenceType) {
          astRefTypes.add((ASTSimpleReferenceType) astReferenceType.get());
        }
      });
    }
    else {
      link.getRightReferenceNames().forEach(rightName -> {
        Optional<ASTReferenceType> astReferenceType = this
            .getASTReference(rightName, astObjectDiagram);
        if (astReferenceType.isPresent() && astReferenceType
            .get() instanceof ASTSimpleReferenceType) {
          astRefTypes.add((ASTSimpleReferenceType) astReferenceType.get());
        }
      });
    }

    return astRefTypes;
  }

  private Optional<ASTReferenceType> getASTReference(ASTODName astodName,
      ASTObjectDiagram astObjectDiagram) {
    Optional<CommonSymbol> symbol = Optional.empty();
    if (astObjectDiagram.getSymbol().isPresent()) {
      if (astodName.getName().isPresent() || astodName.getODSpecialName().isPresent()) {
        symbol = astObjectDiagram.getSpannedScope().get()
            .resolve(astodName.getName().isPresent() ?
                astodName.getName().get() :
                astodName.getODSpecialName().get(), ODObjectSymbol.KIND);
      }
    }

    if (symbol.isPresent()) {
      ODObjectSymbol odSymbol = (ODObjectSymbol) (symbol.get());
      Optional<ASTNode> astNode = odSymbol.getAstNode();
      if (astNode.isPresent()) {
        ASTODObject astOdObject = (ASTODObject) (astNode.get());
        return Optional.of(astOdObject.getReferenceType());
      }
    }

    return Optional.empty();
  }
}
