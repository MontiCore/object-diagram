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

  public static final String WARN_ANONYMOUS_PARENT =
      "0x0D034: Could not extract composed object because its parent object is anonymous!";

  protected List<ASTODObject> objectsToMove = new ArrayList<>();
  protected List<ASTODAttribute> attributesToRemove = new ArrayList<>();
  protected List<ASTODLink> compositionsToCreate = new ArrayList<>();
  
  @Override
  public void endVisit(ASTODObject node) {
    for (ASTODAttribute attribute : node.getODAttributeList()) {
      if (attribute.isPresentODValue()) {
        ASTODValue value = attribute.getODValue();
        switch (value) {
          case ASTODNamedObject namedObject -> {
            if (isAnonymousParent(node, value)) {
              break;
            }
            attributesToRemove.add(attribute);
            objectsToMove.add(namedObject);

            ASTODLink link = createComposition(node.getName(), namedObject.getName(), attribute.getName());
            compositionsToCreate.add(link);
          }
          case ASTODAnonymousObject anonymousObject -> Log.warn("0x0D032: Could not extract composed object because its anonymous!",
              value.get_SourcePositionStart());
          case ASTODName odName -> {
            if (isAnonymousParent(node, value)) {
              break;
            }
            ASTODLink link = createAssociation(node.getName(), odName.getName(), attribute.getName());
            attributesToRemove.add(attribute);
            compositionsToCreate.add(link);
          }
          case ASTODSimpleAttributeValue simpleAttributeValue -> {
            if (simpleAttributeValue.getExpression() instanceof ASTNameExpression nameExpression
                && !isAnonymousParent(node, value)) {
              // TODO JRa: Use Typecheck3 to check the reference of the NameExpression. Do not transform, if its an ENUM value!
              ASTODLink link = createAssociation(node.getName(), nameExpression.getName(), attribute.getName());
              attributesToRemove.add(attribute);
              compositionsToCreate.add(link);
            }
          }
          default -> {} // ignore
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
    attributesToRemove.clear();
    compositionsToCreate.clear();
  }

  /**
   * Links require the name of the parent object, so no links can be created for values of
   * anonymous objects. In this case, a warning is logged at the value.
   *
   * @return {@code true} if the parent object is anonymous and the value must be skipped
   */
  protected boolean isAnonymousParent(ASTODObject parent, ASTODValue value) {
    if (parent instanceof ASTODAnonymousObject) {
      Log.warn(WARN_ANONYMOUS_PARENT, value.get_SourcePositionStart());
      return true;
    }
    return false;
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
