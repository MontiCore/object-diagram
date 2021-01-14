// (c) https://github.com/MontiCore/monticore

package de.monticore.odbasis._symboltable;

import de.monticore.odbasis._ast.ASTODNamedObject;
import de.monticore.odbasis._ast.ASTObjectDiagram;
import de.monticore.odbasis.typescalculator.DeriveSymTypeOfODBasis;
import de.monticore.odbasis.typescalculator.ODTypesCalculator;
import de.monticore.types.check.SymTypeExpression;
import de.se_rwth.commons.logging.Log;

import java.util.Deque;
import java.util.Optional;

public class ODBasisSymbolTableCreator extends ODBasisSymbolTableCreatorTOP {

  private ODTypesCalculator typechecker = new DeriveSymTypeOfODBasis();

  public ODBasisSymbolTableCreator() {
    super();
  }

  public ODBasisSymbolTableCreator(IODBasisScope enclosingScope) {
    super(enclosingScope);
  }

  public ODBasisSymbolTableCreator(Deque<? extends IODBasisScope> scopeStack) {
    super(scopeStack);
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
    Optional<SymTypeExpression> typeResult = typechecker.calculateType(node.getMCObjectType());
    if (!typeResult.isPresent()) {
      Log.error(String.format("0xODXXX: The type of the return type (%s) could not be calculated",
          node.getMCObjectType().getClass().getSimpleName()),
          node.getMCObjectType().get_SourcePositionStart());
    }
    else {
      node.getSymbol().setType(typeResult.get());
    }
  }

  public void setTypechecker(ODTypesCalculator typechecker) {
    this.typechecker = typechecker;
  }

}
