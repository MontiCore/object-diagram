/*
 * Copyright (c) 2014 RWTH Aachen. All rights reserved.
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

    setModelNameCalculator(new ODModelNameCalculator());
  }

  @Override protected ModelingLanguageModelLoader<? extends ASTNode> provideModelLoader() {
    return new ODModelLoader(this);
  }
}
