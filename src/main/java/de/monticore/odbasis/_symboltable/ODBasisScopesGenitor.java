/* (c) https://github.com/MontiCore/monticore */

package de.monticore.odbasis._symboltable;

import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.odbasis._ast.ASTODNamedObject;
import de.monticore.odbasis._ast.ASTObjectDiagram;
import de.monticore.odbasis.typescalculator.DeriveSymTypeOfODBasis;
import de.monticore.odbasis.utils.FullQualifiedNameCalculator;
import de.monticore.types.check.IDerive;
import de.monticore.types.check.SymTypeExpression;
import de.monticore.types.mcbasictypes.MCBasicTypesMill;
import de.se_rwth.commons.logging.Log;

import java.util.Deque;
import java.util.Optional;

public class ODBasisScopesGenitor extends ODBasisScopesGenitorTOP {

  private ASTODArtifact rootNode;

  private IDerive typechecker = new DeriveSymTypeOfODBasis();

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
    node.getMCObjectType().accept(typechecker.getTraverser());
    Optional<SymTypeExpression> typeResult = typechecker.getResult();
    if (!typeResult.isPresent()) {
      Log.error(String.format("0xODXXX: The type of the return type (%s) could not be calculated",
          node.getMCObjectType().getClass().getSimpleName()),
          node.getMCObjectType().get_SourcePositionStart());
    }
    else {
      node.getSymbol().setType(typeResult.get());
    }
  }

  public void setTypechecker(IDerive typechecker) {
    this.typechecker = typechecker;
  }

}
