/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.odbasics._symboltable;

import de.monticore.lang.odbasics._ast.ASTODObject;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.check.SymTypeExpression;
import de.monticore.types.check.SymTypeOfObject;
import de.monticore.types.prettyprint.MCFullGenericTypesPrettyPrinter;
import de.monticore.types.typesymbols._symboltable.TypeSymbolLoader;

import java.util.Deque;
import java.util.Optional;

public class ODBasicsSymbolTableCreator extends ODBasicsSymbolTableCreatorTOP {

  public ODBasicsSymbolTableCreator(IODBasicsScope enclosingScope) {
    super(enclosingScope);
  }

  public ODBasicsSymbolTableCreator(Deque<? extends IODBasicsScope> scopeStack) {
    super(scopeStack);
  }

  public void visit(ASTODObject node) {
    // no symbols for anonymous objects
    if ("".equals(node.getName())) {
      return;
    }

    super.visit(node);
  }

  public void endVisit(ASTODObject node) {
    // no symbols for anonymous objects
    if ("".equals(node.getName())) {
      return;
    }

    super.endVisit(node);

    // create SymTypeExperssion
    Optional<IODBasicsScope> currentScope = getCurrentScope();
    if (currentScope.isPresent()) {
      SymTypeExpression objectType = new SymTypeOfObject(new TypeSymbolLoader(
          node.getMCType().printType(new MCFullGenericTypesPrettyPrinter(new IndentPrinter())),
          currentScope.get()));
      node.getSymbol().setObjectType(objectType);
    }
  }

}
