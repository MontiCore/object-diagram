/* (c) https://github.com/MontiCore/monticore */
package de.monticore.odbasis._cocos.object;

import de.monticore.ODOutTestBasis;
import de.monticore.io.paths.MCPath;
import de.monticore.od4development.OD4DevelopmentMill;
import de.monticore.od4development.OD4DevelopmentTestUtil;
import de.monticore.od4development._cocos.OD4DevelopmentCoCoChecker;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.runtime.junit.TestWithMCLanguage;
import de.se_rwth.commons.logging.Log;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link ValidObjectTypeCoCo}.
 */
@TestWithMCLanguage(OD4DevelopmentMill.class)
class ValidObjectTypeCoCoTest extends ODOutTestBasis {

  private final Path cocoExamples = PATH.resolve("cocos");

  private OD4DevelopmentCoCoChecker odCoCoChecker;

  @BeforeEach
  void setUp() {
    odCoCoChecker = new OD4DevelopmentCoCoChecker();
    odCoCoChecker.addCoCo(new ValidObjectTypeCoCo());
    Log.getFindings().clear();
  }

  /**
   * Tests that the CoCo accepts an object diagram when all object types can be resolved in the
   * enclosing scope.
   */
  @Test
  void shouldAcceptValidObjectTypes() {
    ASTODArtifact artifact = loadArtifact("ValidObjectType.od");

    odCoCoChecker.checkAll(artifact);

    checkLogError();
  }

  /**
   * Tests that the CoCo reports exactly the configured error message when an object is declared
   * with a type that cannot be resolved in the enclosing scope.
   */
  @Test
  void shouldReportUndefinedObjectType() {
    ASTODArtifact artifact = loadArtifact("InvalidObjectType.od");

    odCoCoChecker.checkAll(artifact);

    assertEquals(1, Log.getFindings().size());
    assertEquals(
        ValidObjectTypeCoCo.ERROR_TYPE_USED_BUT_UNDEFINED.formatted("UndefinedType"),
        Log.getFindings().getFirst().getMsg());
  }

  protected ASTODArtifact loadArtifact(String fileName) {
    return OD4DevelopmentTestUtil.loadModelAndST(cocoExamples.resolve(fileName), new MCPath(PATH));
  }
}

