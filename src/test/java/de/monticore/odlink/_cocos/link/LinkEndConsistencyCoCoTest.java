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
 * Tests for {@link LinkEndConsistencyCoCo}.
 */
@TestWithMCLanguage(OD4DevelopmentMill.class)
class LinkEndConsistencyCoCoTest extends ODOutTestBasis {

  private final Path cocoExamples = PATH.resolve("cocos");

  private OD4DevelopmentCoCoChecker odCoCoChecker;

  @BeforeEach
  void setUp() {
    odCoCoChecker = new OD4DevelopmentCoCoChecker();
    odCoCoChecker.addCoCo(new LinkEndConsistencyCoCo());
    Log.getFindings().clear();
  }

  /**
   * Tests that the CoCo accepts a link whose left and right side are each internally consistent,
   * even if both sides resolve to different types from one another.
   */
  @Test
  void shouldAcceptDifferentTypesAcrossSidesWhenEachSideIsInternallyConsistent() {
    ASTODArtifact artifact = loadArtifact("ValidLinkEndConsistency.od");

    odCoCoChecker.checkAll(artifact);

    checkLogError();
  }

  /**
   * Tests that the CoCo reports error code {@code 0x0D008} when one reference on the right side
   * of a named link cannot be resolved in the enclosing scope.
   */
  @Test
  void shouldReportUnresolvedReferenceOnRightSide() {
    ASTODArtifact artifact = loadArtifact("UnresolvedRightLinkEndConsistency.od");

    odCoCoChecker.checkAll(artifact);

    MCAssertions.assertHasFindingStartingWith("0x0D008");
    assertEquals(LinkEndConsistencyCoCo.ERROR_UNRESOLVED_REFERENCE.formatted("right side", "missingObject"), Log.getFindings().getFirst().getMsg());
    assertEquals(1, Log.getFindings().size());
  }

  /**
   * Tests that the CoCo reports error code {@code 0x0D009} when the left side of a named link
   * contains references that resolve to different variable types.
   */
  @Test
  void shouldReportInconsistentTypesOnLeftSide() {
    ASTODArtifact artifact = loadArtifact("InconsistentLeftLinkEndConsistency.od");

    odCoCoChecker.checkAll(artifact);

    MCAssertions.assertHasFindingStartingWith("0x0D009");
    assertEquals(LinkEndConsistencyCoCo.ERROR_INCONSISTENT_REFERENCE_TYPES.formatted("left side", "fooBar", "examples.cd.SimpleOD.ObjectType2", "foo", "examples.cd.SimpleOD.ObjectType"), Log.getFindings().getFirst().getMsg());
    assertEquals(1, Log.getFindings().size());
  }

  /**
   * Tests that the CoCo reports error code {@code 0x0D009} when the right side of a named link
   * contains references that resolve to different variable types.
   */
  @Test
  void shouldReportInconsistentTypesOnRightSide() {
    ASTODArtifact artifact = loadArtifact("InconsistentRightLinkEndConsistency.od");

    odCoCoChecker.checkAll(artifact);

    MCAssertions.assertHasFindingStartingWith("0x0D009");
    assertEquals(LinkEndConsistencyCoCo.ERROR_INCONSISTENT_REFERENCE_TYPES.formatted("right side", "bar", "examples.cd.SimpleOD.ObjectType", "fooBar", "examples.cd.SimpleOD.ObjectType2"), Log.getFindings().getFirst().getMsg());
    assertEquals(1, Log.getFindings().size());
  }

  protected ASTODArtifact loadArtifact(String fileName) {
    ASTODArtifact artifact =
        OD4DevelopmentTestUtil.loadModelAndST(cocoExamples.resolve(fileName), new MCPath(PATH));
    OD4DevelopmentTestUtil.completeSymbolTable(artifact, true);
    return artifact;
  }
}

