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
 * Tests for {@link ValidObjectReferenceCoCo}.
 */
@TestWithMCLanguage(OD4DevelopmentMill.class)
class ValidObjectReferenceCoCoTest extends ODOutTestBasis {

  private final Path cocoExamples = PATH.resolve("cocos");

  private OD4DevelopmentCoCoChecker odCoCoChecker;

  @BeforeEach
  void setUp() {
    odCoCoChecker = new OD4DevelopmentCoCoChecker();
    odCoCoChecker.addCoCo(new ValidObjectReferenceCoCo());
    Log.getFindings().clear();
  }

  /**
   * Tests that the CoCo accepts an object diagram when all object attribute references can be
   * resolved to existing objects in the enclosing scope.
   */
  @Test
  void shouldAcceptResolvableObjectReferences() {
    ASTODArtifact artifact = loadArtifact("ValidObjectReference.od");

    odCoCoChecker.checkAll(artifact);

    checkLogError();
  }

  /**
   * Tests that the CoCo reports exactly the configured error message when an attribute value
   * references an object name that cannot be resolved in the enclosing scope.
   */
  @Test
  void shouldReportUnresolvedObjectReference() {
    ASTODArtifact artifact = loadArtifact("InvalidSingleObjectReference.od");

    odCoCoChecker.checkAll(artifact);

    assertEquals(1, Log.getFindings().size());
    assertEquals(
        ValidObjectReferenceCoCo.ERROR_UNRESOLVED_OBJECT_REFERENCE.formatted("undefinedObject"),
        Log.getFindings().getFirst().getMsg());
  }

  protected ASTODArtifact loadArtifact(String fileName) {
    return OD4DevelopmentTestUtil.loadModelAndST(cocoExamples.resolve(fileName), new MCPath(PATH));
  }
}

