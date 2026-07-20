/* (c) https://github.com/MontiCore/monticore */
package de.monticore.odbasis._cocos.attributes;

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
 * Tests for {@link PartialAndCompleteAttributesCoCo}.
 */
@TestWithMCLanguage(OD4DevelopmentMill.class)
class PartialAndCompleteAttributesCoCoTest extends ODOutTestBasis {

  private final Path cocoExamples = PATH.resolve("cocos");

  private OD4DevelopmentCoCoChecker odCoCoChecker;

  @BeforeEach
  void setUp() {
    odCoCoChecker = new OD4DevelopmentCoCoChecker();
    odCoCoChecker.addCoCo(new PartialAndCompleteAttributesCoCo());
    Log.getFindings().clear();
  }

  /**
   * Tests that the CoCo accepts an object whose attributes are not mixed between partial and
   * complete declarations for the same attribute name.
   */
  @Test
  void shouldAcceptNonMixedAttributes() {
    ASTODArtifact artifact = loadArtifact("ValidPartialAndCompleteAttributes.od");

    odCoCoChecker.checkAll(artifact);

    checkLogError();
  }

  /**
   * Tests that the CoCo reports exactly the configured error message when an attribute is declared
   * once as partial and once as complete within the same object.
   */
  @Test
  void shouldReportPartialAndCompleteAttributeMix() {
    ASTODArtifact artifact = loadArtifact("InvalidPartialAndCompleteAttributes.od");

    odCoCoChecker.checkAll(artifact);

    assertEquals(1, Log.getFindings().size());
    assertEquals(
        PartialAndCompleteAttributesCoCo.ERROR_PARTIAL_AND_COMPLETE_ATTRIBUTE_MIX.formatted("foobar"),
        Log.getFindings().getFirst().getMsg());
  }

  protected ASTODArtifact loadArtifact(String fileName) {
    return OD4DevelopmentTestUtil.loadModelAndST(cocoExamples.resolve(fileName), new MCPath(PATH));
  }
}

