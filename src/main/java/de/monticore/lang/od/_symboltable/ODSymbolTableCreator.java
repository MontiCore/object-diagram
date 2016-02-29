/*
 * Copyright (c) 2015 RWTH Aachen. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
package de.monticore.lang.od._symboltable;

import de.monticore.lang.od._ast.ASTODDefinition;
import de.monticore.lang.od._ast.ASTODObject;
import de.monticore.symboltable.ArtifactScope;
import de.monticore.symboltable.MutableScope;
import de.monticore.symboltable.ResolverConfiguration;
import de.monticore.symboltable.Scope;

import java.util.ArrayList;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class ODSymbolTableCreator extends ODSymbolTableCreatorTOP {

  public ODSymbolTableCreator(ResolverConfiguration resolverConfig,
      MutableScope enclosingScope) {
    super(resolverConfig, enclosingScope);
  }

  public Scope createFromAST(ASTODDefinition astodDefinition) {
    requireNonNull(astodDefinition);

    final ArtifactScope artifactScope = new ArtifactScope(Optional.empty(), "", new ArrayList<>());
    putOnStack(artifactScope);

    astodDefinition.accept(this);

    return artifactScope;
  }

  @Override
  public void visit(final ASTODDefinition astodDefinition) {
    final ODDefinitionSymbol od = new ODDefinitionSymbol(astodDefinition.getName());
    addToScopeAndLinkWithNode(od, astodDefinition);
  }

  @Override
  public void endVisit(final ASTODDefinition astodDefinition) {
    removeCurrentScope();
  }

  @Override
  public void visit(final ASTODObject objectNode) {
    final ODObjectSymbol objectSymbol = new ODObjectSymbol(objectNode.getName().get());
    addToScopeAndLinkWithNode(objectSymbol, objectNode);
  }

}
