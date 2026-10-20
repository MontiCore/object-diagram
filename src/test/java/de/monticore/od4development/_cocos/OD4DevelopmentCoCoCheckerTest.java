// (c) https://github.com/MontiCore/monticore

package de.monticore.od4development._cocos;

import de.monticore.ODOutTestBasis;
import de.monticore.io.paths.MCPath;
import de.monticore.od4development.OD4DevelopmentTestUtil;
import de.monticore.od4development.OD4DevelopmentMill;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.odbasis._cocos.attributes.PartialAndCompleteAttributesCoCo;
import de.monticore.odbasis._cocos.attributes.UniqueAttributeNamesCoCo;
import de.monticore.odbasis._cocos.attributes.ValidObjectAttributesCoCo;
import de.monticore.odbasis._cocos.names.UniqueObjectNamesCoCo;
import de.monticore.odbasis._cocos.object.ValidObjectReferenceCoCo;
import de.monticore.odlink._cocos.link.LinkEndConsistencyCoCo;
import de.monticore.odlink._cocos.link.ValidLinkReferenceCoCo;
import de.monticore.runtime.junit.MCAssertions;
import de.monticore.runtime.junit.TestWithMCLanguage;
import de.se_rwth.commons.logging.Log;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestWithMCLanguage(OD4DevelopmentMill.class)
public class OD4DevelopmentCoCoCheckerTest extends ODOutTestBasis {
  
  private final Path cocoExamples = PATH.resolve("cocos");
  
  private OD4DevelopmentCoCoChecker odCoCoChecker;
  
  @BeforeEach
  public void setUp() {
    odCoCoChecker = new OD4DevelopmentCoCoChecker();
  }
  
  @Test
  public void checkUniqueObjectNamesCoCo() {
    String testOD = cocoExamples.resolve("NoUniqueNames.od").toString();
    ASTODArtifact artifact = OD4DevelopmentTestUtil.loadModelAndST(testOD, new MCPath(PATH));
    
    odCoCoChecker.addCoCo(new UniqueObjectNamesCoCo());
    odCoCoChecker.checkAll(artifact);
    MCAssertions.assertHasFindingStartingWith("0x0D002");
    MCAssertions.assertHasFindingStartingWith("0x0D002");
  }
  
  @Test
  public void checkAnonymousObjectsValid() {
    String testOD = cocoExamples.resolve("AnonymousObject.od").toString();
    ASTODArtifact artifact = OD4DevelopmentTestUtil.loadModelAndST(testOD, new MCPath(PATH));
    
    odCoCoChecker.addCoCo(new UniqueObjectNamesCoCo());
    odCoCoChecker.checkAll(artifact);
  }
  
  @Test
  public void checkValidReferenceCoCo() {
    String testOD = cocoExamples.resolve("InvalidLeftLinkReference.od").toString();
    ASTODArtifact artifact = OD4DevelopmentTestUtil.loadModelAndST(testOD, new MCPath(PATH));
    
    odCoCoChecker.addCoCo(new ValidLinkReferenceCoCo());
    odCoCoChecker.checkAll(artifact);
    MCAssertions.assertHasFindingStartingWith("0x0D00C");
  }
  
  @Test
  public void checkObjectReferenceCoCo() {
    String testOD = cocoExamples.resolve("InvalidSingleObjectReference.od").toString();
    ASTODArtifact artifact = OD4DevelopmentTestUtil.loadModelAndST(testOD, new MCPath(PATH));
    
    odCoCoChecker.addCoCo(new ValidObjectReferenceCoCo());
    odCoCoChecker.checkAll(artifact);
    MCAssertions.assertHasFindingStartingWith("0x0D001");
  }
  
  @Test
  public void checkPartialAndCompleteAttributesCoCo() {
    String testOD = cocoExamples.resolve("PartialAndCompleteAttributes.od").toString();
    ASTODArtifact artifact = OD4DevelopmentTestUtil.loadModelAndST(testOD, new MCPath(PATH));
    
    odCoCoChecker.addCoCo(new PartialAndCompleteAttributesCoCo());
    odCoCoChecker.checkAll(artifact);
    // bar2 and bar3 mix partial and complete, bar4 is reported by UniqueAttributeNamesCoCo
    assertEquals(2, MCAssertions.assertHasFindingsStartingWith("0x0D004").size());
  }

  @Test
  public void checkTwoCompleteAttributesAreReportedOnlyOnce() {
    String testOD = cocoExamples.resolve("PartialAndCompleteAttributes.od").toString();
    ASTODArtifact artifact = OD4DevelopmentTestUtil.loadModelAndST(testOD, new MCPath(PATH));

    odCoCoChecker.addCoCo(new PartialAndCompleteAttributesCoCo());
    odCoCoChecker.addCoCo(new UniqueAttributeNamesCoCo());
    odCoCoChecker.checkAll(artifact);
    assertEquals(2, MCAssertions.assertHasFindingsStartingWith("0x0D004").size());
    assertEquals(1, MCAssertions.assertHasFindingsStartingWith("0x0D003").size());
    assertEquals(3, Log.getFindings().size());
  }

  @Test
  public void checkUniqueAttributeNamesCoCo() {
    String testOD = cocoExamples.resolve("InvalidDuplicateAttributeNames.od").toString();
    ASTODArtifact artifact = OD4DevelopmentTestUtil.loadModelAndST(testOD, new MCPath(PATH));

    odCoCoChecker.addCoCo(new UniqueAttributeNamesCoCo());
    odCoCoChecker.checkAll(artifact);
    MCAssertions.assertHasFindingStartingWith("0x0D003");
  }
  
  @Test
  public void checkLinkEndConsistencyCoCo() {
    String testOD = cocoExamples.resolve("InvalidLinkEndConsistency.od").toString();
    ASTODArtifact artifact = OD4DevelopmentTestUtil.loadModelAndST(testOD, new MCPath(PATH));
    OD4DevelopmentTestUtil.completeSymbolTable(artifact, true);
    
    odCoCoChecker.addCoCo(new LinkEndConsistencyCoCo());
    odCoCoChecker.checkAll(artifact);
    MCAssertions.assertHasFindingStartingWith("0x0D009");
  }

  @Test
  public void checkValidObjectAttributeCoCo() {
    String testOD = cocoExamples.resolve("InvalidObjectAttributes.od").toString();
    ASTODArtifact artifact = OD4DevelopmentTestUtil.loadModelAndST(testOD, new MCPath(PATH));
    OD4DevelopmentTestUtil.completeSymbolTable(artifact, true);

    odCoCoChecker.addCoCo(new ValidObjectAttributesCoCo());
    odCoCoChecker.checkAll(artifact);
    MCAssertions.assertHasFindingStartingWith("0x0D006");
  }
  
}
