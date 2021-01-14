// (c) https://github.com/MontiCore/monticore

package de.monticore.dateliterals;

import de.monticore.dateliterals._cocos.DateLiteralsCoCos;
import de.monticore.io.paths.ModelPath;
import de.monticore.od4report.OD4ReportMill;
import de.monticore.od4report._cocos.OD4ReportCoCoChecker;
import de.monticore.od4report._parser.OD4ReportParser;
import de.monticore.od4report._symboltable.OD4ReportGlobalScope;
import de.monticore.od4report._symboltable.OD4ReportSymbolTableCreatorDelegator;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.ODLogReset;
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

public class DateLiteralsCoCoCheckerTest {

  private static ModelPath modelPath;

  private static Path path;

  private static OD4ReportCoCoChecker odCoCoChecker;

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
    path = Paths.get("src/test/resources/cocos");
    modelPath = new ModelPath(path);
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

  @Test
  public void testDateConcistencyCoCo() {
    Optional<ASTODArtifact> odASTODArtifact = createASTandSTFromFile("WrongDate");

    if (odASTODArtifact.isPresent()) {

      odCoCoChecker.addChecker(new DateLiteralsCoCos().getCheckerForAllCoCos());

      odCoCoChecker.checkAll(odASTODArtifact.get());

      assertEquals(3, Log.getErrorCount());
    }
  }

  private Optional<ASTODArtifact> createASTandSTFromFile(String odName) {

    Optional<ASTODArtifact> artifact = Optional.empty();

    try {
      OD4ReportParser odBasicsParser = new OD4ReportParser();
      artifact = odBasicsParser.parseODArtifact(path.toString() + "/" + odName + ".od");
      assertFalse(odBasicsParser.hasErrors());
      assertTrue(artifact.isPresent());
    }
    catch (IOException e) {
      Log.error("Cannot parse model: " + odName + " in " + path.toString());
    }

    OD4ReportGlobalScope globalScope = new OD4ReportGlobalScope(modelPath, "od");

    OD4ReportSymbolTableCreatorDelegator symTabVisitor = OD4ReportMill.oD4ReportSymbolTableCreatorDelegator();

    artifact.ifPresent(symTabVisitor::createFromAST);

    return artifact;
  }

}
