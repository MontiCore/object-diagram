/* (c) https://github.com/MontiCore/monticore */

package de.monticore.odbasis._symboltable;

import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.odbasis._ast.ASTODNamedObject;
import de.monticore.odbasis._ast.ASTObjectDiagram;
import de.monticore.odbasis.typescalculator.DeriveSymTypeOfODBasis;
import de.monticore.types.check.TypeCheckResult;
import de.se_rwth.commons.logging.Log;

public class ODBasisScopesGenitor extends ODBasisScopesGenitorTOP {

  private boolean checkTypes = true;

  private ASTODArtifact rootNode;

  private DeriveSymTypeOfODBasis typechecker = new DeriveSymTypeOfODBasis();

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
      TypeCheckResult typeResult = typechecker.synthesizeType(node.getMCObjectType());
      if (!typeResult.isPresentCurrentResult()) {
        Log.error(String.format("0xODXXX: The type of the return type (%s) could not be calculated",
            node.getMCObjectType().getClass().getSimpleName()),
          node.getMCObjectType().get_SourcePositionStart());
      } else {
        node.getSymbol().setType(typeResult.getCurrentResult());
      }
    }
  }

  public void setTypechecker(DeriveSymTypeOfODBasis typechecker) {
    this.typechecker = typechecker;
  }

  public boolean isCheckTypes() {
    return checkTypes;
  }

  public void setCheckTypes(boolean checkTypes) {
    this.checkTypes = checkTypes;
  }

}
