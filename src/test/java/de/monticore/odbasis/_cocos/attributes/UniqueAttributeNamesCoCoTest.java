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
 * Tests for {@link UniqueAttributeNamesCoCo}.
 */
@TestWithMCLanguage(OD4DevelopmentMill.class)
class UniqueAttributeNamesCoCoTest extends ODOutTestBasis {

  private final Path cocoExamples = PATH.resolve("cocos");

  private OD4DevelopmentCoCoChecker odCoCoChecker;

  @BeforeEach
  void setUp() {
    odCoCoChecker = new OD4DevelopmentCoCoChecker();
    odCoCoChecker.addCoCo(new UniqueAttributeNamesCoCo());
    Log.getFindings().clear();
  }

  /**
   * Tests that the CoCo accepts an object if all complete attributes have unique names.
   */
  @Test
  void shouldAcceptUniqueCompleteAttributeNames() {
    ASTODArtifact artifact = loadArtifact("ValidUniqueAttributeNames.od");

    odCoCoChecker.checkAll(artifact);

    checkLogError();
  }

  /**
   * Tests that the CoCo reports exactly the configured error message when a complete attribute
   * name is used more than once within the same object.
   */
  @Test
  void shouldReportDuplicateCompleteAttributeNames() {
    ASTODArtifact artifact = loadArtifact("InvalidDuplicateAttributeNames.od");

    odCoCoChecker.checkAll(artifact);

    assertEquals(1, Log.getFindings().size());
    MCAssertions.assertHasFindingStartingWith("0x0D003");
    assertEquals(
        UniqueAttributeNamesCoCo.ERROR_ATTRIBUTE_NAME_NOT_UNIQUE.formatted("foobar"),
        Log.getFindings().getFirst().getMsg());
  }

  protected ASTODArtifact loadArtifact(String fileName) {
    return OD4DevelopmentTestUtil.loadModelAndST(cocoExamples.resolve(fileName), new MCPath(PATH));
  }
}

