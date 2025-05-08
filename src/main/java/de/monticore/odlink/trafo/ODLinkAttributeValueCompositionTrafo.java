/*
 *  (c) https://github.com/MontiCore/monticore
 */

package de.monticore.odlink.trafo;

import de.monticore.expressions.expressionsbasis._ast.ASTNameExpression;
import de.monticore.odbasis._ast.*;
import de.monticore.odbasis._visitor.ODBasisVisitor2;
import de.monticore.odlink.ODLinkMill;
import de.monticore.odlink._ast.*;
import de.monticore.odlink._visitor.ODLinkVisitor2;
import de.monticore.umlmodifier._ast.ASTModifier;
import de.se_rwth.commons.logging.Log;

import java.util.*;

public class ODLinkAttributeValueCompositionTrafo implements ODBasisVisitor2, ODLinkVisitor2 {
  
  protected List<ASTODObject> objectsToMove = new ArrayList<>();
  protected List<ASTODAttribute> attributesToRemove = new ArrayList<>();
  protected List<ASTODLink> compositionsToCreate = new ArrayList<>();
  
  @Override
  public void endVisit(ASTODObject node) {
    for (ASTODAttribute attribute : node.getODAttributeList()) {
      if (attribute.isPresentODValue()) {
        ASTODValue value = attribute.getODValue();
        if (ODLinkMill.typeDispatcher().isODBasisASTODNamedObject(value)) {
          ASTODNamedObject namedObject =
              ODLinkMill.typeDispatcher().asODBasisASTODNamedObject(value);
          attributesToRemove.add(attribute);
          objectsToMove.add(namedObject);
          
          ASTODLink link = createComposition(node.getName(), namedObject.getName(), attribute.getName());
          compositionsToCreate.add(link);
        }
        else if (ODLinkMill.typeDispatcher().isODBasisASTODAnonymousObject(value)) {
          Log.warn("0x0D021: Could not extract composed object because its anonymous!",
              value.get_SourcePositionStart());
        }
        else if (ODLinkMill.typeDispatcher().isODBasisASTODName(value)) {
          ASTODName odName = ODLinkMill.typeDispatcher().asODBasisASTODName(value);
          ASTODLink link = createAssociation(node.getName(), odName.getName(), attribute.getName());
          attributesToRemove.add(attribute);
          compositionsToCreate.add(link);
        }
        else if (ODLinkMill.typeDispatcher().isODBasisASTODSimpleAttributeValue(value)) {
          ASTODSimpleAttributeValue simpleValue = ODLinkMill.typeDispatcher().asODBasisASTODSimpleAttributeValue(value);
          if (ODLinkMill.typeDispatcher().isExpressionsBasisASTNameExpression(simpleValue.getExpression())) {
            // TODO JRa: Use Typecheck3 to check the reference of the NameExpression. Do not transform, if its an ENUM value!
            ASTNameExpression nameExpression =
                ODLinkMill.typeDispatcher().asExpressionsBasisASTNameExpression(simpleValue.getExpression());
            ASTODLink link = createAssociation(node.getName(), nameExpression.getName(), attribute.getName());
            attributesToRemove.add(attribute);
            compositionsToCreate.add(link);
          }
        }
      }
    }
    node.removeAllODAttributes(attributesToRemove);
  }
  
  @Override
  public void endVisit(ASTODArtifact node) {
    node.getObjectDiagram().addAllODElements(objectsToMove);
    node.getObjectDiagram().addAllODElements(compositionsToCreate);
    objectsToMove.clear();
    compositionsToCreate.clear();
  }
  
  protected ASTODLinkBuilder createLinkBase(String sourceName, String targetName, String roleName) {
    ASTODName leftSideName = ODLinkMill.oDNameBuilder().setName(sourceName).build();
    ASTODName rightSideName = ODLinkMill.oDNameBuilder().setName(targetName).build();
    ASTModifier leftModifier = ODLinkMill.modifierBuilder().build();
    ASTModifier rightModifier = ODLinkMill.modifierBuilder().build();
    ASTODLinkLeftSide leftSide =
        ODLinkMill.oDLinkLeftSideBuilder().addReferenceNames(leftSideName).setModifier(leftModifier)
            .build();
    ASTODLinkRightSide rightSide =
        ODLinkMill.oDLinkRightSideBuilder().addReferenceNames(rightSideName).setRole(roleName)
            .setModifier(rightModifier).build();
    ASTODLinkDirection linkDirection = ODLinkMill.oDLeftToRightDirBuilder().build();
    return ODLinkMill.oDLinkBuilder().setODLinkLeftSide(leftSide)
        .setODLinkDirection(linkDirection).setODLinkRightSide(rightSide);
  }
  
  protected ASTODLinkBuilder createLink(String sourceName, String targetName, String roleName,
      ASTODValue qualifierValue) {
    ASTODLinkBuilder linkBaseBuilder = createLinkBase(sourceName, targetName, roleName);
    ASTODLinkQualifier qualifier =
        ODLinkMill.oDLinkQualifierBuilder().setODValue(qualifierValue).build();
    linkBaseBuilder.getODLinkLeftSide().setODLinkQualifier(qualifier);
    return linkBaseBuilder;
  }
  
  protected ASTODLinkBuilder createLink(String sourceName, String targetName, String roleName) {
    return createLinkBase(sourceName, targetName, roleName);
  }
  
  protected ASTODLink createAssociation(String sourceName, String targetName, String roleName,
      ASTODValue qualifierValue) {
    return createLink(sourceName, targetName, roleName, qualifierValue).build();
  }
  
  protected ASTODLink createAssociation(String sourceName, String targetName, String roleName) {
    return createLink(sourceName, targetName, roleName).build();
  }
  
  protected ASTODLink createComposition(String sourceName, String targetName, String roleName,
      ASTODValue qualifierValue) {
    return createLink(sourceName, targetName, roleName, qualifierValue).setComposition(true).build();
  }
  
  protected ASTODLink createComposition(String sourceName, String targetName, String roleName) {
    return createLink(sourceName, targetName, roleName).setComposition(true).build();
  }
}
