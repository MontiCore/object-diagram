/*
 * Copyright (c) 2017 RWTH Aachen. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
package de.monticore.lang.od._symboltable;

import de.monticore.ast.ASTNode;
import de.monticore.modelloader.ModelingLanguageModelLoader;

public class ODLanguage extends ODLanguageTOP {

  public static final String FILE_ENDING = "od";

  public ODLanguage() {
    super("Object Diagram Language", FILE_ENDING);
  }

  @Override protected ModelingLanguageModelLoader<? extends ASTNode> provideModelLoader() {
    return new ODModelLoader(this);
  }

  @Override protected void initResolvingFilters() {
    addResolvingFilter(new ObjectDiagramResolvingFilter());
    addResolvingFilter(new ODObjectResolvingFilter());
  }
}
