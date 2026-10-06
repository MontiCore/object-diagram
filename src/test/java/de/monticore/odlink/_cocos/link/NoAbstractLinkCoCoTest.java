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
 * Tests for {@link NoAbstractLinkCoCo}.
 */
@TestWithMCLanguage(OD4DevelopmentMill.class)
class NoAbstractLinkCoCoTest extends ODOutTestBasis {

  private final Path cocoExamples = PATH.resolve("cocos");

  private OD4DevelopmentCoCoChecker odCoCoChecker;

  @BeforeEach
  void setUp() {
    odCoCoChecker = new OD4DevelopmentCoCoChecker();
    odCoCoChecker.addCoCo(new NoAbstractLinkCoCo());
    Log.getFindings().clear();
  }

  /**
   * Tests that the CoCo accepts a link when neither the left side nor the right side is declared
   * with the {@code abstract} modifier.
   */
  @Test
  void shouldAcceptNonAbstractLinkSides() {
    ASTODArtifact artifact = loadArtifact("ValidNonAbstractLink.od");

    odCoCoChecker.checkAll(artifact);

    checkLogError();
  }

  /**
   * Tests that the CoCo reports exactly the configured left-side error message when the left side
   * of a link is declared abstract.
   */
  @Test
  void shouldReportAbstractLeftSide() {
    ASTODArtifact artifact = loadArtifact("InvalidAbstractLeftLink.od");

    odCoCoChecker.checkAll(artifact);

    assertEquals(1, Log.getFindings().size());
    MCAssertions.assertHasFindingStartingWith("0x0D00A");
    assertEquals(
        NoAbstractLinkCoCo.ERROR_LEFT_SIDE_ABSTRACT,
        Log.getFindings().getFirst().getMsg());
  }

  /**
   * Tests that the CoCo reports exactly the configured right-side error message when the right
   * side of a link is declared abstract.
   */
  @Test
  void shouldReportAbstractRightSide() {
    ASTODArtifact artifact = loadArtifact("InvalidAbstractRightLink.od");

    odCoCoChecker.checkAll(artifact);

    assertEquals(1, Log.getFindings().size());
    MCAssertions.assertHasFindingStartingWith("0x0D00B");
    assertEquals(
        NoAbstractLinkCoCo.ERROR_RIGHT_SIDE_ABSTRACT,
        Log.getFindings().getFirst().getMsg());
  }

  protected ASTODArtifact loadArtifact(String fileName) {
    return OD4DevelopmentTestUtil.loadModelAndST(cocoExamples.resolve(fileName), new MCPath(PATH));
  }
}

