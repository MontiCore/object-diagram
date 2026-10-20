/* (c) https://github.com/MontiCore/monticore */
package de.monticore.odlink._cocos.link;

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
 * Tests for {@link ValidLinkReferenceCoCo}.
 */
@TestWithMCLanguage(OD4DevelopmentMill.class)
class ValidLinkReferenceCoCoTest extends ODOutTestBasis {

  private final Path cocoExamples = PATH.resolve("cocos");

  private OD4DevelopmentCoCoChecker odCoCoChecker;

  @BeforeEach
  void setUp() {
    odCoCoChecker = new OD4DevelopmentCoCoChecker();
    odCoCoChecker.addCoCo(new ValidLinkReferenceCoCo());
    Log.getFindings().clear();
  }

  /**
   * Tests that the CoCo accepts a named link when all references on the left and on the right
   * side can be resolved in the enclosing scope.
   */
  @Test
  void shouldAcceptResolvableReferencesOnBothSides() {
    ASTODArtifact artifact = loadArtifact("ValidLinkReference.od");

    odCoCoChecker.checkAll(artifact);

    checkLogError();
  }

  /**
   * Tests that the CoCo reports error code {@code 0x0D00C} when a reference on the left side of
   * a named link cannot be resolved in the enclosing scope.
   */
  @Test
  void shouldReportUnresolvedLeftReference() {
    ASTODArtifact artifact = loadArtifact("InvalidLeftLinkReference.od");

    odCoCoChecker.checkAll(artifact);

    MCAssertions.assertHasFindingStartingWith("0x0D00C");
    assertEquals(ValidLinkReferenceCoCo.ERROR_LEFT_REFERENCE_UNRESOLVED.formatted("missingLeft"), Log.getFindings().getFirst().getMsg());
    assertEquals(1, Log.getFindings().size());
  }

  /**
   * Tests that the CoCo reports error code {@code 0x0D00D} when a reference on the right side of
   * a named link cannot be resolved in the enclosing scope.
   */
  @Test
  void shouldReportUnresolvedRightReference() {
    ASTODArtifact artifact = loadArtifact("InvalidRightLinkReference.od");

    odCoCoChecker.checkAll(artifact);

    MCAssertions.assertHasFindingStartingWith("0x0D00D");
    assertEquals(ValidLinkReferenceCoCo.ERROR_RIGHT_REFERENCE_UNRESOLVED.formatted("missingRight"), Log.getFindings().getFirst().getMsg());
    assertEquals(1, Log.getFindings().size());
  }

  protected ASTODArtifact loadArtifact(String fileName) {
    return OD4DevelopmentTestUtil.loadModelAndST(cocoExamples.resolve(fileName), new MCPath(PATH));
  }
}

