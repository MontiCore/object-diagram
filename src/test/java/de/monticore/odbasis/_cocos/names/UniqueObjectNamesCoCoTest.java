/* (c) https://github.com/MontiCore/monticore */
package de.monticore.odbasis._cocos.names;

import de.monticore.ODOutTestBasis;
import de.monticore.io.paths.MCPath;
import de.monticore.od4development.OD4DevelopmentMill;
import de.monticore.od4development.OD4DevelopmentTestUtil;
import de.monticore.od4development._cocos.OD4DevelopmentCoCoChecker;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.runtime.junit.MCAssertions;
import de.monticore.runtime.junit.TestWithMCLanguage;
import de.se_rwth.commons.logging.Log;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link UniqueObjectNamesCoCo}.
 */
@TestWithMCLanguage(OD4DevelopmentMill.class)
class UniqueObjectNamesCoCoTest extends ODOutTestBasis {

  private final Path cocoExamples = PATH.resolve("cocos");

  private OD4DevelopmentCoCoChecker odCoCoChecker;

  @BeforeEach
  void setUp() {
    odCoCoChecker = new OD4DevelopmentCoCoChecker();
    odCoCoChecker.addCoCo(new UniqueObjectNamesCoCo());
    Log.getFindings().clear();
  }

  /**
   * Tests that the CoCo accepts an object diagram without duplicate named objects.
   */
  @Test
  void shouldAcceptUniqueObjectNames() {
    ASTODArtifact artifact = loadArtifact("AnonymousObject.od");

    odCoCoChecker.checkAll(artifact);

    checkLogError();
  }

  /**
   * Tests that the CoCo reports exactly the configured error message for each duplicated named
   * object in the enclosing scope.
   */
  @Test
  void shouldReportDuplicateObjectNames() {
    ASTODArtifact artifact = loadArtifact("NoUniqueNames.od");

    odCoCoChecker.checkAll(artifact);

    assertEquals(2, Log.getFindings().size());
    MCAssertions.assertHasFindingsStartingWith("0x0D002");
    assertEquals(
        UniqueObjectNamesCoCo.ERROR_OBJECT_NAME_NOT_UNIQUE.formatted("myObject"),
        Log.getFindings().get(0).getMsg());
    assertEquals(
        UniqueObjectNamesCoCo.ERROR_OBJECT_NAME_NOT_UNIQUE.formatted("myObject"),
        Log.getFindings().get(1).getMsg());
  }

  protected ASTODArtifact loadArtifact(String fileName) {
    return OD4DevelopmentTestUtil.loadModelAndST(cocoExamples.resolve(fileName), new MCPath(PATH));
  }
}

