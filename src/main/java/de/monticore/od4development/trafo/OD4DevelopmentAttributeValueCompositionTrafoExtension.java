/*
 *  (c) https://github.com/MontiCore/monticore
 */

package de.monticore.od4development.trafo;

import de.monticore.od4development.OD4DevelopmentMill;
import de.monticore.odattribute._ast.ASTODList;
import de.monticore.odattribute._ast.ASTODMap;
import de.monticore.odattribute._ast.ASTODMapElement;
import de.monticore.odattribute._visitor.ODAttributeVisitor2;
import de.monticore.odbasis._ast.ASTODAttribute;
import de.monticore.odbasis._ast.ASTODNamedObject;
import de.monticore.odbasis._ast.ASTODObject;
import de.monticore.odbasis._ast.ASTODValue;
import de.monticore.odlink.ODLinkMill;
import de.monticore.odlink._ast.ASTODLink;
import de.monticore.odlink.trafo.ODLinkAttributeValueCompositionTrafo;

import java.util.ArrayList;
import java.util.List;

public class OD4DevelopmentAttributeValueCompositionTrafoExtension
    extends ODLinkAttributeValueCompositionTrafo implements ODAttributeVisitor2 {
  
  @Override
  public void endVisit(ASTODObject node) {
    for (ASTODAttribute attribute : node.getODAttributeList()) {
      if (attribute.isPresentODValue()) {
        if (OD4DevelopmentMill.typeDispatcher().isODAttributeASTODList(attribute.getODValue())) {
          ASTODList list =
              OD4DevelopmentMill.typeDispatcher().asODAttributeASTODList(attribute.getODValue());
          List<ASTODValue> valueToRemove = new ArrayList<>();
          for (ASTODValue value : list.getODValueList()) {
            if (OD4DevelopmentMill.typeDispatcher().isODBasisASTODNamedObject(value)) {
              ASTODNamedObject namedObject =
                  ODLinkMill.typeDispatcher().asODBasisASTODNamedObject(value);
              valueToRemove.add(value);
              objectsToMove.add(namedObject);
              
              ASTODLink link =
                  createComposition(node.getName(), namedObject.getName(), attribute.getName());
              compositionsToCreate.add(link);
            }
          }
          list.removeAllODValues(valueToRemove);
          if (list.isEmptyODValues()) {
            attributesToRemove.add(attribute);
          }
        }
        else if (OD4DevelopmentMill.typeDispatcher().isODAttributeASTODMap(attribute.getODValue())) {
          ASTODMap map = OD4DevelopmentMill.typeDispatcher().asODAttributeASTODMap(attribute.getODValue());
          List<ASTODMapElement> mapElementsToRemove = new ArrayList<>();
          for (ASTODMapElement mapElement : map.getODMapElementList()) {
            
            if (OD4DevelopmentMill.typeDispatcher().isODBasisASTODNamedObject(mapElement.getVal())) {
              ASTODNamedObject namedObject =
                  ODLinkMill.typeDispatcher().asODBasisASTODNamedObject(mapElement.getVal());
              mapElementsToRemove.add(mapElement);
              objectsToMove.add(namedObject);
              
              ASTODLink link =
                  createComposition(node.getName(), namedObject.getName(), attribute.getName(),
                      mapElement.getKey());
              compositionsToCreate.add(link);
            }
          }
          map.removeAllODMapElements(mapElementsToRemove);
          if (map.isEmptyODMapElements()) {
            attributesToRemove.add(attribute);
          }
        }
      }
    }
    super.endVisit(node);
  }
}
