// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report.utils;

import de.monticore.od4report.OD4ReportMill;
import de.monticore.od4report.OD4ReportToolAPI;
import de.monticore.od4report.util.OD4ReportCollector;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.se_rwth.commons.logging.LogStub;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OD4ReportCollectorTest {
  
  private final Path EXAMPLEOD = Paths.get("src/test/resources/examples/od/Examples.od");
  
  private final Path SIMPLEOD2 = Paths.get("src/test/resources/examples/od/SimpleOD2.od");
  
  @Before
  public void setup() {
    LogStub.init();
    OD4ReportMill.reset();
    OD4ReportMill.init();
  }
  
  @Test
  public void testCollector() {
    ASTODArtifact astodArtifact = OD4ReportToolAPI.parse(EXAMPLEOD.toString());
    
    assertNotNull(astodArtifact);
    
    OD4ReportCollector od4ReportCollector = new OD4ReportCollector();
    assertEquals(4, od4ReportCollector.getODObjects(astodArtifact.getObjectDiagram()).size()
        + od4ReportCollector.getODLinks(astodArtifact.getObjectDiagram()).size());
  }
  
  @Test
  public void testAnonymousCollect() {
    ASTODArtifact astodArtifact = OD4ReportToolAPI.parse(SIMPLEOD2.toString());
    
    assertNotNull(astodArtifact);
    
    OD4ReportCollector od4ReportCollector = new OD4ReportCollector();
    assertEquals(5, od4ReportCollector.getODObjects(astodArtifact.getObjectDiagram()).size());
  }
  
}
