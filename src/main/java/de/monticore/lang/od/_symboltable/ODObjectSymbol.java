/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.od._symboltable;

import de.monticore.symboltable.CommonSymbol;
import de.monticore.types.types._ast.ASTReferenceType;

public class ODObjectSymbol extends CommonSymbol {

  public static final ODObjectKind KIND = new ODObjectKind();

  private ASTReferenceType type;

  public ODObjectSymbol(String name) {
    super(name, KIND);
  }

  public void setType(ASTReferenceType referenceType) {
    this.type = referenceType;
  }

  public ASTReferenceType getType() {
    return this.type;
  }

}
