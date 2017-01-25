/*
 * Copyright (c) 2015 RWTH Aachen. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
package de.monticore.lang.od._symboltable;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Optional;

import de.monticore.lang.od._ast.ASTODDefinition;
import de.monticore.symboltable.ArtifactScope;
import de.monticore.symboltable.MutableScope;
import de.monticore.symboltable.ResolvingConfiguration;
import de.monticore.symboltable.Scope;

public class ODSymbolTableCreator extends ODSymbolTableCreatorTOP {

  public ODSymbolTableCreator(ResolvingConfiguration resolverConfig,
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


}
