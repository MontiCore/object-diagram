package de.monticore.lang.od._ast;

import java.util.Optional;

public class ASTODObject extends ASTODObjectTOP {
  
  protected ASTODObject(
      de.monticore.common.common._ast.ASTCompleteness completeness,
      de.monticore.common.common._ast.ASTModifier modifier,
      de.monticore.lang.od._ast.ASTObjectId objectId,
      de.monticore.types.types._ast.ASTReferenceType type,
      java.util.List<de.monticore.lang.od._ast.ASTODAttribute> oDAttributes,
      java.util.List<de.monticore.lang.od._ast.ASTODObject> oDObjects) {
    super(completeness, modifier, objectId, type, oDAttributes, oDObjects);
  }
  
  protected ASTODObject() {
    super();
  }
  
  public Optional<String> getName() {
    if (!objectIdIsPresent()) {
      return Optional.empty();
    }
    
    ASTObjectId objectId = getObjectId().get();
    String name = "";
    if (objectId.nameIsPresent()) {
      name = objectId.getName().get();
    }
    else if (objectId.stringLiteralIsPresent()) {
      name += objectId.getStringLiteral().get().getSource();
    }
    
    return Optional.of(name);
  }
  
  public void setName(String name) {
    ASTObjectId objectId = ODNodeFactory.createASTObjectId();
    objectId.setName(name);
    this.objectId = Optional.ofNullable(objectId);
  }
}
