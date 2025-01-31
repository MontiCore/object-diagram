// (c) https://github.com/MontiCore/monticore

package de.monticore.od4data._cocos;

import de.monticore.ODOutTestBasis;
import de.monticore.io.paths.MCPath;
import de.monticore.od4data.OD4DataMill;
import de.monticore.od4data.OD4DataTestUtil;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.odbasis._cocos.attributes.PartialAndCompleteAttributesCoCo;
import de.monticore.odbasis._cocos.names.UniqueObjectNamesCoCo;
import de.monticore.odbasis._cocos.object.ValidObjectReferenceCoCo;
import de.monticore.odlink._cocos.link.LinkEndConsistencyCoCo;
import de.monticore.odlink._cocos.link.ValidLinkReferenceCoCo;
import de.se_rwth.commons.logging.Log;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OD4DataCoCoCheckerTest extends ODOutTestBasis {
  
  private final Path cocoExamples = PATH.resolve("cocos");
  
  private OD4DataCoCoChecker odCoCoChecker;
  
  @BeforeEach
  public void setUp() {
    odCoCoChecker = new OD4DataCoCoChecker();
    
    OD4DataMill.reset();
    OD4DataMill.init();
  }
  
  @Test
  public void checkUniqueObjectNamesCoCo() {
    String testOD = cocoExamples.resolve("NoUniqueNames.od").toString();
    ASTODArtifact artifact = OD4DataTestUtil.loadModelAndST(testOD, new MCPath(PATH));
    
    odCoCoChecker.addCoCo(new UniqueObjectNamesCoCo());
    odCoCoChecker.checkAll(artifact);
    assertEquals(2, Log.getErrorCount());
    Log.clearFindings();
  }
  
  @Test
  public void checkAnonymousObjectsValid() {
    String testOD = cocoExamples.resolve("AnonymousObject.od").toString();
    ASTODArtifact artifact = OD4DataTestUtil.loadModelAndST(testOD, new MCPath(PATH));
    
    odCoCoChecker.addCoCo(new UniqueObjectNamesCoCo());
    odCoCoChecker.checkAll(artifact);
    assertEquals(0, Log.getErrorCount());
  }
  
  @Test
  public void checkValidReferenceCoCo() {
    String testOD = cocoExamples.resolve("InvalidLinkReference.od").toString();
    ASTODArtifact artifact = OD4DataTestUtil.loadModelAndST(testOD, new MCPath(PATH));
    
    odCoCoChecker.addCoCo(new ValidLinkReferenceCoCo());
    odCoCoChecker.checkAll(artifact);
    assertEquals(1, Log.getErrorCount());
    Log.clearFindings();
  }
  
  @Test
  public void checkObjectReferenceCoCo() {
    String testOD = cocoExamples.resolve("InvalidObjectReference.od").toString();
    ASTODArtifact artifact = OD4DataTestUtil.loadModelAndST(testOD, new MCPath(PATH));
    
    odCoCoChecker.addCoCo(new ValidObjectReferenceCoCo());
    odCoCoChecker.checkAll(artifact);
    assertEquals(1, Log.getErrorCount());
    Log.clearFindings();
  }
  
  @Test
  public void checkPartialAndCompleteAttributesCoCo() {
    String testOD = cocoExamples.resolve("PartialAndCompleteAttributes.od").toString();
    ASTODArtifact artifact = OD4DataTestUtil.loadModelAndST(testOD, new MCPath(PATH));
    
    odCoCoChecker.addCoCo(new PartialAndCompleteAttributesCoCo());
    odCoCoChecker.checkAll(artifact);
    assertEquals(3, Log.getErrorCount());
    Log.clearFindings();
  }
  
  @Test
  public void checkLinkEndConsistencyCoCo() {
    String testOD = cocoExamples.resolve("InvalidLinkEndConsistency.od").toString();
    ASTODArtifact artifact = OD4DataTestUtil.loadModelAndST(testOD, new MCPath(PATH));
    
    odCoCoChecker.addCoCo(new LinkEndConsistencyCoCo());
    odCoCoChecker.checkAll(artifact);
    assertEquals(1, Log.getErrorCount());
    Log.clearFindings();
  }
  
}
