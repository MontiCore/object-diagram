/* (c) https://github.com/MontiCore/monticore */
package de.monticore.odbasis._cocos.attributes;

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
 * Tests for {@link ValidObjectAttributesCoCo}.
 */
@TestWithMCLanguage(OD4DevelopmentMill.class)
class ValidObjectAttributeCoCoTest extends ODOutTestBasis {

  private final Path cocoExamples = PATH.resolve("cocos");

  private OD4DevelopmentCoCoChecker odCoCoChecker;

  @BeforeEach
  void setUp() {
    odCoCoChecker = new OD4DevelopmentCoCoChecker();
    odCoCoChecker.addCoCo(new ValidObjectAttributesCoCo());
    Log.getFindings().clear();
  }

  /**
   * Tests that the CoCo accepts an object when all declared attributes are defined as fields in
   * the object's type.
   */
  @Test
  void shouldAcceptValidObjectAttributes() {
    ASTODArtifact artifact = loadArtifact("ValidObjectAttributes.od");
    OD4DevelopmentTestUtil.completeSymbolTable(artifact, true);

    odCoCoChecker.checkAll(artifact);

    checkLogError();
  }

  /**
   * Tests that the CoCo reports exactly the configured error message when an attribute is
   * declared but not defined as a field in the object's type.
   */
  @Test
  void shouldReportUndefinedObjectAttribute() {
    ASTODArtifact artifact = loadArtifact("InvalidObjectAttributes.od");
    OD4DevelopmentTestUtil.completeSymbolTable(artifact, true);

    odCoCoChecker.checkAll(artifact);

    assertEquals(1, Log.getFindings().size());
    MCAssertions.assertHasFindingStartingWith("0x0D006");
    assertEquals(
        ValidObjectAttributesCoCo.ERROR_ATTRIBUTE_NOT_DEFINED.formatted("undefinedField", "foo"),
        Log.getFindings().getFirst().getMsg());
  }

  protected ASTODArtifact loadArtifact(String fileName) {
    return OD4DevelopmentTestUtil.loadModelAndST(cocoExamples.resolve(fileName), new MCPath(PATH));
  }
}

