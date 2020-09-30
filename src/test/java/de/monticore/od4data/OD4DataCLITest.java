// (c) https://github.com/MontiCore/monticore

package de.monticore.od4data;

import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

public class OD4DataCLITest {

  private final Path INPUTOD = Paths.get("src", "test", "resources", "examples", "od", "Teaser.od");

  private final Path TARGET = Paths.get("target", "cli", "od4data");

  @Test
  public void testOD4DataCLIHelp() {
    String[] help = { "-h" };
    OD4DataCLI.main(help);
  }

  @Test
  public void testOD4CLIPrettyPrint() {
    String[] input = { "-i", INPUTOD.toString(), "-pp",
        Paths.get(TARGET.toString(), "pp.od").toString() };
    OD4DataCLI.main(input);
    assertTrue(Paths.get(TARGET.toString(), "pp.od").toFile().exists());
  }

  @Test
  public void testOD4DataStoreST() {
    String[] input = { "-i", INPUTOD.toString(), "-s",
        Paths.get(TARGET.toString(), "MyFamily.odsym").toString() };
    OD4DataCLI.main(input);
    assertTrue(Paths.get(TARGET.toString(), "MyFamily.odsym").toFile().exists());
  }

}