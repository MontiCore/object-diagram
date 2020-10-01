// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report;

import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

public class OD4ReportCLITest {

  private final Path INPUTOD = Paths.get("src", "test", "resources", "examples", "od",
      "Examples" + ".od");

  private final Path INPUTDIR = Paths.get("src", "resources", "examples", "od");

  private final Path TARGET = Paths.get("target", "cli", "od4Report");

  @Test
  public void testOD4ReportCLIHelp() {
    String[] help = { "-h" };
    OD4ReportCLI.main(help);
  }

  @Test
  public void testOD4ReportCLIPath() {
    String[] input = { "-i", INPUTOD.toString(), "-path",
        Paths.get(INPUTDIR.toString(), "examples").toString(),
        Paths.get(INPUTDIR.toString(), "cocos").toString() };
    OD4ReportCLI.main(input);
  }

  @Test
  public void testOD4ReportCLIPrettyPrint() {
    String[] input = { "-i", INPUTOD.toString(), "-pp",
        Paths.get(TARGET.toString(), "pp.od").toString() };
    OD4ReportCLI.main(input);
    assertTrue(Paths.get(TARGET.toString(), "pp.od")
        .toFile()
        .exists());
  }

  @Test
  public void testOD4ReportStoreST() {
    String[] input = { "-i", INPUTOD.toString(), "-s",
        Paths.get(TARGET.toString(), "Examples.odsym").toString() };
    OD4ReportCLI.main(input);
    assertTrue(Paths.get(TARGET.toString(), "Examples.odsym")
        .toFile()
        .exists());
  }

}
