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
 * Tests for {@link NoAbstractAttributesCoCo}.
 */
@TestWithMCLanguage(OD4DevelopmentMill.class)
class NoAbstractAttributesCoCoTest extends ODOutTestBasis {

  private final Path cocoExamples = PATH.resolve("cocos");

  private OD4DevelopmentCoCoChecker odCoCoChecker;

  @BeforeEach
  void setUp() {
    odCoCoChecker = new OD4DevelopmentCoCoChecker();
    odCoCoChecker.addCoCo(new NoAbstractAttributesCoCo());
    Log.getFindings().clear();
  }

  /**
   * Tests that the CoCo accepts a regular object diagram attribute when the attribute does not
   * use the {@code abstract} modifier.
   */
  @Test
  void shouldAcceptNonAbstractAttribute() {
    ASTODArtifact artifact = loadArtifact("ValidNonAbstractAttribute.od");

    odCoCoChecker.checkAll(artifact);

    checkLogError();
  }

  /**
   * Tests that the CoCo reports exactly the configured error message when an object diagram
   * attribute is declared with the {@code abstract} modifier.
   */
  @Test
  void shouldReportAbstractAttribute() {
    ASTODArtifact artifact = loadArtifact("InvalidAbstractAttribute.od");

    odCoCoChecker.checkAll(artifact);

    assertEquals(1, Log.getFindings().size());
    assertEquals(
        NoAbstractAttributesCoCo.ERROR_ABSTRACT_ATTRIBUTE,
        Log.getFindings().getFirst().getMsg());
  }

  protected ASTODArtifact loadArtifact(String fileName) {
    return OD4DevelopmentTestUtil.loadModelAndST(cocoExamples.resolve(fileName), new MCPath(PATH));
  }
}

