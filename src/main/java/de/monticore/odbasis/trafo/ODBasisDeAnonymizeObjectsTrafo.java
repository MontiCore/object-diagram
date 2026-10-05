/*
 *  (c) https://github.com/MontiCore/monticore
 */

package de.monticore.odbasis.trafo;

import de.monticore.odbasis.ODBasisMill;
import de.monticore.odbasis._ast.*;
import de.monticore.odbasis._visitor.ODBasisVisitor2;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This AST transformation replaces all anonymous objects with
 * NamedObjects and assigns generated names.
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
 *   __a_anonymous_1:A {
 *     foo = __b_anonymous_1:B {};
 *   };
 * </pre>
 */
public class ODBasisDeAnonymizeObjectsTrafo implements ODBasisVisitor2 {
  
  /**
   * Number of generated names per type. Keyed by the printed type, as AST
   * nodes are compared by identity and each object has its own type node.
   */
  protected Map<String, Integer> pseudoCounts = new LinkedHashMap<>();
  
  @Override
  public void endVisit(ASTObjectDiagram node) {
    for (int i = 0; i < node.getODElementList().size(); i++) {
      ASTODElement element = node.getODElementList().get(i);
      if (element instanceof ASTODAnonymousObject anonymousObject) {
        ASTODNamedObject namedCopy = copyToNamedObject(anonymousObject);
        node.setODElement(i, namedCopy);
      }
    }
  }
  
  @Override
  public void endVisit(ASTODAttribute node) {
    if (node.isPresentODValue()) {
      ASTODValue value = node.getODValue();
      if (value instanceof ASTODAnonymousObject anonymousObject) {
        ASTODNamedObject namedCopy = copyToNamedObject(anonymousObject);
        node.setODValue(namedCopy);
      }
    }
  }
  
  protected String generateObjectPseudoName(ASTODObject object) {
    String typeName = object.getMCObjectType().printType().toLowerCase().replaceAll("\\.", "_");

    int pseudoIdx = pseudoCounts.merge(typeName, 1, Integer::sum);

    return "__" + typeName + "_anonymous_" + pseudoIdx;
  }
  
  protected ASTODNamedObject copyToNamedObject(ASTODAnonymousObject object) {
    ASTODNamedObjectBuilder builder = ODBasisMill.oDNamedObjectBuilder();
    builder.setName(generateObjectPseudoName(object));
    builder.setModifier(object.getModifier());
    builder.setODAttributesList(object.getODAttributeList());
    builder.setMCObjectType(object.getMCObjectType());
    builder.set_SourcePositionStart(object.get_SourcePositionStart());
    builder.set_SourcePositionEnd(object.get_SourcePositionEnd());
    builder.set_PreCommentList(object.get_PreCommentList());
    builder.set_PostCommentList(object.get_PostCommentList());
    
    ASTODNamedObject namedObject = builder.build();
    namedObject.setEnclosingScope(object.getEnclosingScope());
    return namedObject;
  }
}
