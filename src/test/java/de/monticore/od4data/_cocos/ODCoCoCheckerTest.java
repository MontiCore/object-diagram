// (c) https://github.com/MontiCore/monticore

package de.monticore.od4data._cocos;

import de.monticore.od4data.OD4DataMill;
import de.monticore.od4data._parser.OD4DataParser;
import de.monticore.od4data._symboltable.OD4DataSymbolTableCreatorDelegator;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.odbasis._cocos.attributes.PartialAndCompleteAttributesCoCo;
import de.monticore.odbasis._cocos.names.UniqueObjectNamesCoCo;
import de.monticore.odbasis._cocos.object.ValidObjectReferenceCoCo;
import de.monticore.odlink._cocos.link.LinkEndConsistencyCoCo;
import de.monticore.odlink._cocos.link.ValidLinkReferenceCoCo;
import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.ODLogReset;
import de.se_rwth.commons.logging.Slf4jLog;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.Assert.*;

public class ODCoCoCheckerTest {

  private static Path path;

  private static OD4DataCoCoChecker odCoCoChecker;

  private PrintStream originalOut;

  private PrintStream originalErr;

  private ByteArrayOutputStream out;

  private ByteArrayOutputStream err;

  @Before
  public void setup() {
    Slf4jLog.init();
    Slf4jLog.enableFailQuick(false);
    ODLogReset.resetFindings();
    odCoCoChecker = new OD4DataCoCoChecker();
    path = Paths.get("src/test/resources/cocos");

    OD4DataMill.reset();
    OD4DataMill.init();
  }

  @Before
  public void setStreams() {
    // redirect System.out
    originalOut = System.out;
    out = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out));

    //redirect System.err
    originalErr = System.err;
    err = new ByteArrayOutputStream();
    System.setErr(new PrintStream(err));
  }

  @After
  public void restoreSysOut() {
    System.setOut(originalOut);
    System.setErr(originalErr);
  }

  private Optional<ASTODArtifact> createASTandSTFromFile(String odName) {
    Optional<ASTODArtifact> artifact = Optional.empty();

    try {
      OD4DataParser parser = new OD4DataParser();
      artifact = parser.parseODArtifact(path.toString() + "/" + odName + ".od");
      assertFalse(parser.hasErrors());
      assertTrue(artifact.isPresent());
    }
    catch (IOException e) {
      Log.error("Cannot parse model: " + odName + " in " + path.toString());
    }

    OD4DataSymbolTableCreatorDelegator symTabVisitor =
        OD4DataMill.oD4DataSymbolTableCreatorDelegator();

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
      odCoCoChecker.addCoCo(new ValidLinkReferenceCoCo());
      odCoCoChecker.checkAll(odASTODArtifact.get());
      assertEquals(1, Slf4jLog.getErrorCount());
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
      assertEquals(2, Slf4jLog.getErrorCount());
    }
  }

}
