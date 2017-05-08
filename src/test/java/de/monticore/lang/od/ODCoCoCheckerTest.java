/*
 * Copyright (c)  RWTH Aachen. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
package de.monticore.lang.od;

import de.monticore.io.paths.ModelPath;
import de.monticore.lang.od._ast.ASTODArtefact;
import de.monticore.lang.od._cocos.*;
import de.monticore.lang.od._symboltable.ODLanguage;
import de.monticore.lang.od._symboltable.ODSymbolTableCreator;
import de.monticore.symboltable.GlobalScope;
import de.monticore.symboltable.ResolvingConfiguration;
import de.se_rwth.commons.logging.Log;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ODCoCoCheckerTest {

  private static ODLanguage odLanguage;

  private static ResolvingConfiguration resolverConfiguration;

  private static ModelPath modelPath;

  private static Path path;

  private static ODCoCoChecker odCoCoChecker;

  @BeforeClass
  public static void setup() {

    Log.enableFailQuick(false);

    odLanguage = new ODLanguage();

    resolverConfiguration = new ResolvingConfiguration();
    resolverConfiguration.addDefaultFilters(odLanguage.getResolvers());

    odCoCoChecker = new ODCoCoChecker();

    path = Paths.get("src/test/resources/cocos");

    modelPath = new ModelPath(path);

  }

  private Optional<ASTODArtefact> createASTandSTFromFile(String odName) {

    Optional<ASTODArtefact> astodArtefact = Optional.empty();

    try {
      astodArtefact = odLanguage.getParser()
          .parseODArtefact(path.toString() + "/" + odName + ".od");
      assertFalse(odLanguage.getParser().hasErrors());
      assertTrue(astodArtefact.isPresent());
    }
    catch (IOException e) {
      Log.error("Cannot parse model: " + odName + " in " + path.toString());
    }

    GlobalScope globalScope = new GlobalScope(modelPath, odLanguage, resolverConfiguration);

    Optional<ODSymbolTableCreator> symbolTable = odLanguage.getSymbolTableCreator(
        resolverConfiguration, globalScope);

    if (symbolTable.isPresent()) {
      symbolTable.get().createFromAST(astodArtefact.get().getObjectDiagram());
    }

    return astodArtefact;
  }

  @Test
  public void checkUniqueObjectNamesCoCo() {

    Optional<ASTODArtefact> odAstodArtefact = createASTandSTFromFile("NoUniqueNames");

    if (odAstodArtefact.isPresent()) {

      odCoCoChecker.addCoCo(new UniqueObjectNamesCoCo());

      odCoCoChecker.checkAll(odAstodArtefact.get());

      //assertTrue(Log.getErrorCount() == 2);

    }

  }

  @Test
  public void checkValidReferenceCoCo() {

    Optional<ASTODArtefact> odAstodArtefact = createASTandSTFromFile("ValidLinkReference");

    if (odAstodArtefact.isPresent()) {

      odCoCoChecker.addCoCo(new ValidObjectReferenceCoCo());
      odCoCoChecker.addCoCo(new ValidLinkReferenceCoCo());
      odCoCoChecker.addCoCo(new LinkConsistsOfReferenceNamesCoCo());

      odCoCoChecker.checkAll(odAstodArtefact.get());

      //assertTrue(Log.getErrorCount() == 2);

    }

  }

}
