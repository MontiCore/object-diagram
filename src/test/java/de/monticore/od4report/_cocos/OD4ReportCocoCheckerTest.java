/*
 (c) https://github.com/MontiCore/monticore
 */

package de.monticore.od4report._cocos;

import de.monticore.io.paths.MCPath;
import de.monticore.od4report.OD4ReportMill;
import de.monticore.od4report._parser.OD4ReportParser;
import de.monticore.od4report._symboltable.OD4ReportScopesGenitorDelegator;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.odbasis._cocos.object.ValidObjectTypeCoco;
import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.ODLogReset;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.Assert.*;

public class OD4ReportCocoCheckerTest {
  private MCPath symbolPath = new MCPath(Paths.get("src", "test", "resources"));

  private OD4ReportCoCoChecker odCoCoChecker;

  private PrintStream originalOut;

  private PrintStream originalErr;

  private ByteArrayOutputStream out;

  private ByteArrayOutputStream err;

  @Before
  public void setup() {
    Log.init();
    Log.enableFailQuick(false);
    ODLogReset.resetFindings();
    odCoCoChecker = new OD4ReportCoCoChecker();

    OD4ReportMill.reset();
    OD4ReportMill.init();
    OD4ReportMill.globalScope().setSymbolPath(symbolPath);
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

  private Optional<ASTODArtifact> createASTandSTFromFile(String path) {
    Optional<ASTODArtifact> artifact = Optional.empty();

    try {
      OD4ReportParser parser = new OD4ReportParser();
      artifact = parser.parseODArtifact(path);
      assertFalse(parser.hasErrors());
      assertTrue(artifact.isPresent());
    }
    catch (IOException e) {
      Log.error("Cannot parse model: " + path);
    }

    OD4ReportScopesGenitorDelegator od4ReportScopesGenitorDelegator =
        OD4ReportMill.scopesGenitorDelegator();

    artifact.ifPresent(od4ReportScopesGenitorDelegator::createFromAST);

    return artifact;
  }

  // TODO: This does not work due to MC's DeSer implementation.
  @Ignore
  @Test
  public void checkValidObjectTypeCoco() {
    Optional<ASTODArtifact> astodArtifact = createASTandSTFromFile(
        Paths.get("src", "test", "resources", "examples", "od", "MyFamily.od").toString());

    if (astodArtifact.isPresent()) {
      odCoCoChecker.addCoCo(new ValidObjectTypeCoco());
      odCoCoChecker.checkAll(astodArtifact.get());

      assertEquals(0, Log.getErrorCount());
    }
  }

}
