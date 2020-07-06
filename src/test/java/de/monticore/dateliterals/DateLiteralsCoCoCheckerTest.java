// (c) https://github.com/MontiCore/monticore

package de.monticore.dateliterals;

import de.monticore.dateliterals._cocos.DateLiteralsCoCos;
import de.monticore.io.paths.ModelPath;
import de.monticore.od4report.OD4ReportMill;
import de.monticore.od4report._cocos.OD4ReportCoCoChecker;
import de.monticore.od4report._parser.OD4ReportParser;
import de.monticore.od4report._symboltable.OD4ReportGlobalScope;
import de.monticore.od4report._symboltable.OD4ReportSymbolTableCreatorDelegator;
import de.monticore.odbasics._ast.ASTODArtifact;
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

public class DateLiteralsCoCoCheckerTest {

  private static ModelPath modelPath;

  private static Path path;

  private static OD4ReportCoCoChecker odCoCoChecker;

  @Before
  public void setup() {

    Slf4jLog.init();

    Slf4jLog.enableFailQuick(false);

    ODLogReset.resetFindings();

    odCoCoChecker = new OD4ReportCoCoChecker();

    path = Paths.get("src/test/resources/cocos");

    modelPath = new ModelPath(path);
  }

  @Test
  public void testDateConcistencyCoCo() {
    Optional<ASTODArtifact> odASTODArtifact = createASTandSTFromFile("WrongDate");

    if (odASTODArtifact.isPresent()) {

      odCoCoChecker.addChecker(new DateLiteralsCoCos().getCheckerForAllCoCos());

      odCoCoChecker.checkAll(odASTODArtifact.get());

      assertEquals(3, Slf4jLog.getErrorCount());
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

    OD4ReportSymbolTableCreatorDelegator symTabVisitor = OD4ReportMill
        .oD4ReportSymbolTableCreatorDelegatorBuilder().setGlobalScope(globalScope).build();

    artifact.ifPresent(symTabVisitor::createFromAST);

    return artifact;
  }

}
