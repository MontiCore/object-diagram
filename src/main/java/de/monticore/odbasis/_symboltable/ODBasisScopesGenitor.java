/* (c) https://github.com/MontiCore/monticore */

package de.monticore.odbasis._symboltable;

import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.odbasis._ast.ASTODNamedObject;
import de.monticore.odbasis._ast.ASTObjectDiagram;
import de.monticore.types.check.SymTypeExpression;
import de.monticore.types.mcbasictypes._ast.ASTMCObjectType;
import de.monticore.types3.TypeCheck3;
import de.se_rwth.commons.logging.Log;

public class ODBasisScopesGenitor extends ODBasisScopesGenitorTOP {

  private boolean checkTypes = true;

  private ASTODArtifact rootNode;

  public ODBasisScopesGenitor() {
    super();
  }

  @Override
  public void visit(ASTODArtifact astodArtifact) {
    this.rootNode = astodArtifact;
  }

  @Override
  public void endVisit(ASTObjectDiagram node) {
    super.endVisit(node);
    node.getEnclosingScope().setName(node.getName());
  }

  @Override
  public void visit(ASTODNamedObject node) {
    super.visit(node);
    node.getSymbol().setName(node.getName());
  }

  @Override
  public void endVisit(ASTODNamedObject node) {
    super.endVisit(node);
    if (checkTypes) {
      ASTMCObjectType objectType = node.getMCObjectType();
      final SymTypeExpression typeResult = TypeCheck3.symTypeFromAST(objectType);
      if (typeResult.isObscureType()) {
        Log.error(String.format("0x0D013: The type of the return type (%s) could not be calculated",
                node.getMCObjectType().getClass().getSimpleName()),
            node.getMCObjectType().get_SourcePositionStart());
      } else {
        node.getSymbol().setType(typeResult);
      }
    }
  }

  public boolean isCheckTypes() {
    return checkTypes;
  }

  public void setCheckTypes(boolean checkTypes) {
    this.checkTypes = checkTypes;
  }

}
