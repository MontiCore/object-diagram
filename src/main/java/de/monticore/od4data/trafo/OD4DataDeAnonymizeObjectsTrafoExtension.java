/*
 *  (c) https://github.com/MontiCore/monticore
 */

package de.monticore.od4data.trafo;

import de.monticore.od4data.OD4DataMill;
import de.monticore.odattribute._ast.ASTODList;
import de.monticore.odattribute._ast.ASTODMap;
import de.monticore.odattribute._ast.ASTODMapElement;
import de.monticore.odattribute._visitor.ODAttributeVisitor2;
import de.monticore.odbasis._ast.*;
import de.monticore.odbasis.trafo.ODBasisDeAnonymizeObjectsTrafo;

/**
 * This AST transformation replaces all anonymous objects with
 * NamedObjects and assigns generated names.
 * <p>
 *   This extends the ODBasisDeAnonymizeObjectsTrafo with
 *   transformations within list and maps.
 * <p>
 * Note: Since no scope exists at this point, the generated names
 * are not checked for collisions with object names already defined
 * in the object diagram.
 * <p>
 * Example:
 * <pre>
 *   :A {
 *     foo = :B {};
 *   };
 * </pre>
 * will be transformed to
 * <pre>
 *   __a_anonymous_0:A {
 *     foo = __b_anonymous_0:B {};
 *   };
 * </pre>
 */
public class OD4DataDeAnonymizeObjectsTrafoExtension extends ODBasisDeAnonymizeObjectsTrafo
    implements ODAttributeVisitor2 {
  
  @Override
  public void endVisit(ASTODList node) {
    for (int i = 0; i < node.getODValueList().size(); i++) {
      ASTODValue value = node.getODValue(i);
      if (OD4DataMill.typeDispatcher().isODBasisASTODAnonymousObject(value)) {
        ASTODAnonymousObject anonymousObject =
            OD4DataMill.typeDispatcher().asODBasisASTODAnonymousObject(value);
        ASTODNamedObject namedObject = copyToNamedObject(anonymousObject);
        node.setODValue(i, namedObject);
      }
    }
  }
  
  @Override
  public void endVisit(ASTODMap node) {
    for (int i = 0; i < node.getODMapElementList().size(); i++) {
      ASTODMapElement mapElement = node.getODMapElement(i);
      ASTODValue value = mapElement.getVal();
      
      if (OD4DataMill.typeDispatcher().isODBasisASTODAnonymousObject(value)) {
        ASTODAnonymousObject anonymousObject =
            OD4DataMill.typeDispatcher().asODBasisASTODAnonymousObject(value);
        ASTODNamedObject namedObject = copyToNamedObject(anonymousObject);
        mapElement.setVal(namedObject);
      }
    }
  }
}
