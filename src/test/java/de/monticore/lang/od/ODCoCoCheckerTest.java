/*
 * Copyright (c) 2017, MontiCore. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
package de.monticore.lang.od;

import de.monticore.io.paths.ModelPath;
import de.monticore.lang.od._ast.ASTODArtifact;
import de.monticore.lang.od._cocos.ODCoCoChecker;
import de.monticore.lang.od._cocos.attributes.PartialAndCompleteAttributesCoCo;
import de.monticore.lang.od._cocos.link.LinkEndConsistencyCoCo;
import de.monticore.lang.od._cocos.link.ValidLinkReferenceCoCo;
import de.monticore.lang.od._cocos.names.UniqueObjectNamesCoCo;
import de.monticore.lang.od._cocos.object.ValidObjectReferenceCoCo;
import de.monticore.lang.od._symboltable.ODLanguage;
import de.monticore.lang.od._symboltable.ODSymbolTableCreator;
import de.monticore.symboltable.GlobalScope;
import de.monticore.symboltable.ResolvingConfiguration;
import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.ODLogReset;
import de.se_rwth.commons.logging.Slf4jLog;
import org.junit.Before;
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

  @Before
  public void setup() {

    Slf4jLog.init();

    Slf4jLog.enableFailQuick(false);

    ODLogReset.resetFindings();

    odLanguage = new ODLanguage();

    resolverConfiguration = new ResolvingConfiguration();
    resolverConfiguration.addDefaultFilters(odLanguage.getResolvingFilters());

    odCoCoChecker = new ODCoCoChecker();

    path = Paths.get("src/test/resources/cocos");

    modelPath = new ModelPath(path);

  }

  private Optional<ASTODArtifact> createASTandSTFromFile(String odName) {

    Optional<ASTODArtifact> artifact = Optional.empty();

    try {
      artifact = odLanguage.getParser()
          .parseODArtifact(path.toString() + "/" + odName + ".od");
      assertFalse(odLanguage.getParser().hasErrors());
      assertTrue(artifact.isPresent());
    }
    catch (IOException e) {
      Log.error("Cannot parse model: " + odName + " in " + path.toString());
    }

    GlobalScope globalScope = new GlobalScope(modelPath, odLanguage, resolverConfiguration);

    Optional<ODSymbolTableCreator> symbolTable = odLanguage.getSymbolTableCreator(
        resolverConfiguration, globalScope);

    if (symbolTable.isPresent()) {
      symbolTable.get().createFromAST(artifact.get());
    }

    return artifact;
  }

  @Test
  public void checkUniqueObjectNamesCoCo() {

    Optional<ASTODArtifact> odASTODArtifact = createASTandSTFromFile("NoUniqueNames");

    if (odASTODArtifact.isPresent()) {

      odCoCoChecker.addCoCo(new UniqueObjectNamesCoCo());

      odCoCoChecker.checkAll(odASTODArtifact.get());

      assertTrue(Slf4jLog.getErrorCount() == 2);

    }

  }

  @Test
  public void checkValidReferenceCoCo() {

    Optional<ASTODArtifact> odASTODArtifact = createASTandSTFromFile("InvalidLinkReference");

    if (odASTODArtifact.isPresent()) {

      odCoCoChecker.addCoCo(new ValidObjectReferenceCoCo());
      odCoCoChecker.addCoCo(new ValidLinkReferenceCoCo());

      odCoCoChecker.checkAll(odASTODArtifact.get());

      assertTrue(Slf4jLog.getErrorCount() == 2);

    }

  }

  @Test
  public void checkObjectReferenceCoCo() {

    Optional<ASTODArtifact> odASTODArtifact = createASTandSTFromFile("InvalidObjectReference");

    if (odASTODArtifact.isPresent()) {

      odCoCoChecker.addCoCo(new ValidObjectReferenceCoCo());

      odCoCoChecker.checkAll(odASTODArtifact.get());

      assertTrue(Slf4jLog.getErrorCount() == 1);

    }

  }

  @Test
  public void checkPartialAndCompleteAttributesCoCo() {

    Optional<ASTODArtifact> odASTODArtifact = createASTandSTFromFile(
        "PartialAndCompleteAttributes");

    if (odASTODArtifact.isPresent()) {

      odCoCoChecker.addCoCo(new PartialAndCompleteAttributesCoCo());

      odCoCoChecker.checkAll(odASTODArtifact.get());

      assertTrue(Slf4jLog.getErrorCount() == 3);

    }

  }

  @Test
  public void checkLinkEndConsistencyCoCo() {

    Optional<ASTODArtifact> odASTODArtifact = createASTandSTFromFile(
        "InvalidLinkEndConsistency");

    if (odASTODArtifact.isPresent()) {

      odCoCoChecker.addCoCo(new LinkEndConsistencyCoCo());

      odCoCoChecker.checkAll(odASTODArtifact.get());

      assertTrue(Slf4jLog.getErrorCount() == 1);

    }

  }

}
