// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report.utils;

import de.monticore.ODTestBasis;
import de.monticore.io.paths.MCPath;
import de.monticore.od4report.OD4ReportMill;
import de.monticore.od4report.OD4ReportTestUtil;
import de.monticore.od4report._ast.ASTODReportObject;
import de.monticore.od4report.util.OD4ReportCollector;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.se_rwth.commons.logging.Log;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OD4ReportCollectorTest extends ODTestBasis {
  
  private final Path EXAMPLE_DIR = PATH.resolve(Paths.get("examples", "od"));
  
  @BeforeEach
  public void setUp() {
    OD4ReportMill.reset();
    OD4ReportMill.init();
  }
  
  @Test
  public void testCollector() {
    Path inputOD = EXAMPLE_DIR.resolve("Examples.od");
    ASTODArtifact astodArtifact = OD4ReportTestUtil.loadModel(inputOD, new MCPath(PATH));
    
    assertNotNull(astodArtifact);
    
    OD4ReportCollector od4ReportCollector = new OD4ReportCollector();
    int objectCount = od4ReportCollector.getODObjects(astodArtifact.getObjectDiagram()).size();
    int linkCount = od4ReportCollector.getODLinks(astodArtifact.getObjectDiagram()).size();
    int anonymousCount =
        od4ReportCollector.getAnonymousObjects(astodArtifact.getObjectDiagram()).size();
    int namedCount = od4ReportCollector.getNamedObjects(astodArtifact.getObjectDiagram()).size();
    assertEquals(4, objectCount);
    assertEquals(2, linkCount);
    assertEquals(0, anonymousCount);
    assertEquals(4, namedCount);
    
    List<ASTODReportObject> reportObjects =
        od4ReportCollector.getReportObjects(astodArtifact.getObjectDiagram());
    assertEquals(1, reportObjects.size());
    
    assertEquals(0, Log.getFindingsCount());
  }
  
  @Test
  public void testAnonymousCollect() {
    Path inputOD = EXAMPLE_DIR.resolve("SimpleOD2.od");
    ASTODArtifact astodArtifact = OD4ReportTestUtil.loadModel(inputOD, new MCPath(PATH));
    
    assertNotNull(astodArtifact);
    
    OD4ReportCollector od4ReportCollector = new OD4ReportCollector();
    int objectCount = od4ReportCollector.getODObjects(astodArtifact.getObjectDiagram()).size();
    int linkCount = od4ReportCollector.getODLinks(astodArtifact.getObjectDiagram()).size();
    int anonymousCount =
        od4ReportCollector.getAnonymousObjects(astodArtifact.getObjectDiagram()).size();
    int namedCount = od4ReportCollector.getNamedObjects(astodArtifact.getObjectDiagram()).size();
    assertEquals(5, objectCount);
    assertEquals(1, linkCount);
    assertEquals(1, anonymousCount);
    assertEquals(4, namedCount);
    
    List<ASTODReportObject> reportObjects =
        od4ReportCollector.getReportObjects(astodArtifact.getObjectDiagram());
    assertEquals(0, reportObjects.size());
    
    assertEquals(0, Log.getFindingsCount());
  }
  
}
