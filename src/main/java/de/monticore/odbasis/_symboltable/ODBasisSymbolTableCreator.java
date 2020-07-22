// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

/* (c) https://github.com/MontiCore/monticore */
package de.monticore.odbasis._symboltable;

import de.monticore.odbasis._ast.ASTODNamedObject;
import de.monticore.odbasis.typescalculator.DeriveSymTypeOfODBasis;
import de.monticore.types.check.SymTypeExpression;
import de.se_rwth.commons.logging.Log;

import java.util.Deque;
import java.util.Optional;

public class ODBasisSymbolTableCreator extends ODBasisSymbolTableCreatorTOP {

  private DeriveSymTypeOfODBasis typechecker = new DeriveSymTypeOfODBasis();

  public ODBasisSymbolTableCreator(IODBasisScope enclosingScope) {
    super(enclosingScope);
  }

  public ODBasisSymbolTableCreator(Deque<? extends IODBasisScope> scopeStack) {
    super(scopeStack);
  }

  @Override
  public void visit(ASTODNamedObject node) {
    super.visit(node);
    node.getSymbol().setName(node.getName());
    node.getMCObjectType()
        .setEnclosingScope(scopeStack.peekLast()); // TODO SH: remove when #2549 is fixed
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

}
