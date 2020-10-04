// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report.utils;

import de.monticore.od4report.OD4ReportTool;
import de.monticore.od4report.util.OD4ReportCollector;
import de.monticore.odbasis._ast.ASTODArtifact;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OD4ReportCollectorTest {

  private final Path EXAMPLEOD = Paths.get("src/test/resources/examples/od/Examples.od");

  private final Path SIMPLEOD2 = Paths.get("src/test/resources/examples/od/SimpleOD2.od");

  @Test
  public void testCollector() {
    ASTODArtifact astodArtifact = OD4ReportTool.parse(EXAMPLEOD.toString());

    assertNotNull(astodArtifact);

    OD4ReportCollector od4ReportCollector = new OD4ReportCollector();
    astodArtifact.accept(od4ReportCollector);

    assertEquals(6, od4ReportCollector.getODObjects()
        .size() + od4ReportCollector.getODLinks()
        .size());
  }

  @Test
  public void testAnonymousCollect() {
    ASTODArtifact astodArtifact = OD4ReportTool.parse(SIMPLEOD2.toString());

    assertNotNull(astodArtifact);

    OD4ReportCollector od4ReportCollector = new OD4ReportCollector();
    astodArtifact.accept(od4ReportCollector);

    assertEquals(5, od4ReportCollector.getODObjects()
        .size());
  }

}
