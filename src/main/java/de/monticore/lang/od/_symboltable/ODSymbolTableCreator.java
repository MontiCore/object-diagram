/*
 * Copyright (c) 2015 RWTH Aachen. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
package de.monticore.lang.od._symboltable;

import de.monticore.lang.od._ast.ASTODObject;
import de.monticore.lang.od._ast.ASTObjectDiagram;
import de.monticore.symboltable.ArtifactScope;
import de.monticore.symboltable.MutableScope;
import de.monticore.symboltable.ResolvingConfiguration;
import de.monticore.symboltable.Scope;

import java.util.ArrayList;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class ODSymbolTableCreator extends ODSymbolTableCreatorTOP {

  public ODSymbolTableCreator(ResolvingConfiguration resolverConfig,
      MutableScope enclosingScope) {
    super(resolverConfig, enclosingScope);
  }

  public Scope createFromAST(ASTObjectDiagram astObjectDiagram) {
    requireNonNull(astObjectDiagram);

    final ArtifactScope artifactScope = new ArtifactScope(Optional.empty(), "", new ArrayList<>());
    putOnStack(artifactScope);

    astObjectDiagram.accept(this);

    return artifactScope;
  }

  @Override public void visit(ASTODObject ast) {
    ODObjectSymbol oDObject = create_ODObject(ast);
    if (!oDObject.getName().isEmpty()) {
      initialize_ODObject(oDObject, ast);
      addToScopeAndLinkWithNode(oDObject, ast);
    }
  }

  @Override protected ODObjectSymbol create_ODObject(ASTODObject ast) {
    if (ast.getODName().isPresent()) {
      if (ast.getODName().get().getName().isPresent()) {
        return new ODObjectSymbol(ast.getODName().get().getName().get());
      }
      else if (ast.getODName().get().getODSpecialName().isPresent()) {
        return new ODObjectSymbol(ast.getODName().get().getODSpecialName().get());
      }
    }
    return super.create_ODObject(ast);
  }
}
