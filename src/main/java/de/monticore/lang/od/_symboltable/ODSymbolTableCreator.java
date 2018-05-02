/*
 * Copyright (c) 2017, MontiCore. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
package de.monticore.lang.od._symboltable;

import de.monticore.lang.od._ast.ASTODArtifact;
import de.monticore.lang.od._ast.ASTODObject;
import de.monticore.symboltable.ArtifactScope;
import de.monticore.symboltable.MutableScope;
import de.monticore.symboltable.ResolvingConfiguration;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Optional;

public class ODSymbolTableCreator extends ODSymbolTableCreatorTOP {

  public ODSymbolTableCreator(ResolvingConfiguration resolverConfig,
      MutableScope enclosingScope) {
    super(resolverConfig, enclosingScope);
  }

  public ODSymbolTableCreator(final ResolvingConfiguration resolvingConfig,
      final Deque<MutableScope> scopeStack) {
    super(resolvingConfig, scopeStack);
  }

  @Override
  public void visit(ASTODArtifact ast) {
    final ArtifactScope artifactScope = new ArtifactScope(Optional.empty(), "", new ArrayList<>());
    artifactScope.setName(ast.getObjectDiagram().getName());
    artifactScope.setAstNode(ast);
    ast.setEnclosingScope(artifactScope);
    putOnStack(artifactScope);
  }

  @Override
  public void visit(ASTODObject ast) {
    Optional<ODObjectSymbol> oDObjectOpt = create_ODObject(ast);
    if (oDObjectOpt.isPresent()) {
      initialize_ODObject(oDObjectOpt.get(), ast);
      addToScopeAndLinkWithNode(oDObjectOpt.get(), ast);
    }
  }

  protected Optional<ODObjectSymbol> create_ODObject(ASTODObject ast) {
    if (ast.isPresentODName()) {
      if (ast.getODName().getNameOpt().isPresent()) {
        return Optional.of(new ODObjectSymbol(ast.getODName().getName()));
      }
      else if (ast.getODName().isPresentODSpecialName()) {
        return Optional.of(new ODObjectSymbol(ast.getODName().getODSpecialName()));
      }
    }
    return Optional.empty();
  }

  protected void initialize_ODObject(ODObjectSymbol odObjectSymbol, ASTODObject astodObject) {
    odObjectSymbol.setType(astodObject.getReferenceType());
  }
}
