// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore
package de.monticore.odbasics;

import de.monticore.io.paths.ModelPath;
import de.monticore.odbasics._ast.ASTODArtifact;
import de.monticore.odbasics._cocos.ODBasicsCoCoChecker;
import de.monticore.odbasics._cocos.attributes.PartialAndCompleteAttributesCoCo;
import de.monticore.odbasics._cocos.link.LinkEndConsistencyCoCo;
import de.monticore.odbasics._cocos.link.ValidLinkReferenceCoCo;
import de.monticore.odbasics._cocos.names.UniqueObjectNamesCoCo;
import de.monticore.odbasics._cocos.object.ValidObjectReferenceCoCo;
import de.monticore.odbasics._parser.ODBasicsParser;
import de.monticore.odbasics._symboltable.ODBasicsGlobalScope;
import de.monticore.odbasics._symboltable.ODBasicsSymbolTableCreatorDelegator;
import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.ODLogReset;
import de.se_rwth.commons.logging.Slf4jLog;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.Assert.*;

public class ODCoCoCheckerTest {

  private static ModelPath modelPath;

  private static Path path;

  private static ODBasicsCoCoChecker odCoCoChecker;

  @Before
  public void setup() {

    Slf4jLog.init();

    Slf4jLog.enableFailQuick(false);

    ODLogReset.resetFindings();

    odCoCoChecker = new ODBasicsCoCoChecker();

    path = Paths.get("src/test/resources/cocos");

    modelPath = new ModelPath(path);
  }

  private Optional<ASTODArtifact> createASTandSTFromFile(String odName) {

    Optional<ASTODArtifact> artifact = Optional.empty();

    try {
      ODBasicsParser odBasicsParser = new ODBasicsParser();
      artifact = odBasicsParser.parseODArtifact(path.toString() + "/" + odName + ".od");
      assertFalse(odBasicsParser.hasErrors());
      assertTrue(artifact.isPresent());
    }
    catch (IOException e) {
      Log.error("Cannot parse model: " + odName + " in " + path.toString());
    }

    ODBasicsGlobalScope globalScope = new ODBasicsGlobalScope(modelPath, "od");

    ODBasicsSymbolTableCreatorDelegator symTabVisitor = ODBasicsMill.getMill()
        ._oDBasicsSymbolTableCreatorDelegatorBuilder().setGlobalScope(globalScope).build();

    artifact.ifPresent(symTabVisitor::createFromAST);

    return artifact;
  }

  @Test
  public void checkUniqueObjectNamesCoCo() {

    Optional<ASTODArtifact> odASTODArtifact = createASTandSTFromFile("NoUniqueNames");

    if (odASTODArtifact.isPresent()) {

      odCoCoChecker.addCoCo(new UniqueObjectNamesCoCo());

      odCoCoChecker.checkAll(odASTODArtifact.get());

      assertEquals(2, Slf4jLog.getErrorCount());
    }
  }

  @Test
  public void checkAnonymusObjectsValid() {

    Optional<ASTODArtifact> odASTODArtifact = createASTandSTFromFile("AnonymusObject");

    if (odASTODArtifact.isPresent()) {

      odCoCoChecker.addCoCo(new UniqueObjectNamesCoCo());

      odCoCoChecker.checkAll(odASTODArtifact.get());

      assertEquals(0, Slf4jLog.getErrorCount());
    }
  }

  @Test
  public void checkValidReferenceCoCo() {

    Optional<ASTODArtifact> odASTODArtifact = createASTandSTFromFile("InvalidLinkReference");

    if (odASTODArtifact.isPresent()) {

      odCoCoChecker.addCoCo(new ValidObjectReferenceCoCo());
      odCoCoChecker.addCoCo(new ValidLinkReferenceCoCo());

      odCoCoChecker.checkAll(odASTODArtifact.get());

      assertEquals(2, Slf4jLog.getErrorCount());
    }
  }

  @Test
  public void checkObjectReferenceCoCo() {

    Optional<ASTODArtifact> odASTODArtifact = createASTandSTFromFile("InvalidObjectReference");

    if (odASTODArtifact.isPresent()) {

      odCoCoChecker.addCoCo(new ValidObjectReferenceCoCo());

      odCoCoChecker.checkAll(odASTODArtifact.get());

      assertEquals(1, Slf4jLog.getErrorCount());
    }
  }

  @Test
  public void checkPartialAndCompleteAttributesCoCo() {

    Optional<ASTODArtifact> odASTODArtifact = createASTandSTFromFile(
        "PartialAndCompleteAttributes");

    if (odASTODArtifact.isPresent()) {

      odCoCoChecker.addCoCo(new PartialAndCompleteAttributesCoCo());

      odCoCoChecker.checkAll(odASTODArtifact.get());

      assertEquals(3, Slf4jLog.getErrorCount());
    }
  }

  @Test
  public void checkLinkEndConsistencyCoCo() {

    Optional<ASTODArtifact> odASTODArtifact = createASTandSTFromFile("InvalidLinkEndConsistency");

    if (odASTODArtifact.isPresent()) {

      odCoCoChecker.addCoCo(new LinkEndConsistencyCoCo());

      odCoCoChecker.checkAll(odASTODArtifact.get());

      assertEquals(1, Slf4jLog.getErrorCount());
    }
  }

}
