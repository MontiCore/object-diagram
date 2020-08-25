// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report;

import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

public class OD4ReportCLITest {

  private final Path INPUTOD = Paths
      .get("src", "test", "resources", "examples", "od", "Examples" + ".od");

  private final Path TARGET = Paths.get("target", "cli", "od4Report");

  @Test
  public void testOD4DataCLIHelp() {
    String[] help = { "-h" };
    OD4ReportCLI.main(help);
  }

  @Test
  public void testOD4CLIPrettyPrint() {
    String[] input = { "-i", INPUTOD.toString(), "-pp",
        Paths.get(TARGET.toString(), "pp.od").toString() };
    OD4ReportCLI.main(input);
    assertTrue(Paths.get(TARGET.toString(), "pp.od").toFile().exists());
  }

  @Test
  public void testOD4DataStoreST() {
    String[] input = { "-i", INPUTOD.toString(), "-s", TARGET.toString() };
    OD4ReportCLI.main(input);
    assertTrue(Paths.get(TARGET.toString(), "Examples.odsym").toFile().exists());
  }

}
