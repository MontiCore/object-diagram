/*
 *  (c) https://github.com/MontiCore/monticore
 */

package de.monticore.odbasis.trafo;

import de.monticore.odbasis.ODBasisMill;
import de.monticore.odbasis._ast.*;
import de.monticore.odbasis._visitor.ODBasisVisitor2;
import de.monticore.types.mcbasictypes._ast.ASTMCObjectType;

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
 *   __a_anonymous_0:A {
 *     foo = __b_anonymous_0:B {};
 *   };
 * </pre>
 */
public class ODBasisDeAnonymizeObjectsTrafo implements ODBasisVisitor2 {
  
  protected Map<ASTMCObjectType, Integer> pseudoCounts = new LinkedHashMap<>();
  
  @Override
  public void endVisit(ASTObjectDiagram node) {
    for (int i = 0; i < node.getODElementList().size(); i++) {
      ASTODElement element = node.getODElementList().get(i);
      if (ODBasisMill.typeDispatcher().isODBasisASTODAnonymousObject(element)) {
        ASTODAnonymousObject anonymousObject =
            ODBasisMill.typeDispatcher().asODBasisASTODAnonymousObject(element);
        
        ASTODNamedObject namedCopy = copyToNamedObject(anonymousObject);
        node.setODElement(i, namedCopy);
      }
    }
  }
  
  @Override
  public void endVisit(ASTODAttribute node) {
    if (node.isPresentODValue()) {
      ASTODValue value = node.getODValue();
      if (ODBasisMill.typeDispatcher().isODBasisASTODAnonymousObject(value)) {
        ASTODAnonymousObject anonymousObject =
            ODBasisMill.typeDispatcher().asODBasisASTODAnonymousObject(value);
        ASTODNamedObject namedCopy = copyToNamedObject(anonymousObject);
        node.setODValue(namedCopy);
      }
    }
  }
  
  protected String generateObjectPseudoName(ASTODObject object) {
    ASTMCObjectType type = object.getMCObjectType();
    String typeName = type.printType().toLowerCase().replaceAll("\\.", "_");
    
    pseudoCounts.putIfAbsent(type, 0);
    int pseudoIdx = pseudoCounts.merge(type, 1, Integer::sum);
    
    StringBuilder sb = new StringBuilder();
    sb.append("__");
    sb.append(typeName);
    sb.append("_anonymous_");
    sb.append(pseudoIdx);
    
    String generatedName = sb.toString();
    
    return generatedName;
  }
  
  protected ASTODNamedObject copyToNamedObject(ASTODAnonymousObject object) {
    ASTODNamedObjectBuilder builder = ODBasisMill.oDNamedObjectBuilder();
    builder.setName(generateObjectPseudoName(object));
    builder.setModifier(object.getModifier());
    builder.setODAttributesList(object.getODAttributeList());
    builder.setMCObjectType(object.getMCObjectType());
    builder.set_SourcePositionStart(object.get_SourcePositionStart());
    builder.set_SourcePositionEnd(object.get_SourcePositionEnd());
    builder.set_PostCommentList(object.get_PostCommentList());
    
    ASTODNamedObject namedObject = builder.build();
    namedObject.setEnclosingScope(object.getEnclosingScope());
    return namedObject;
  }
}
