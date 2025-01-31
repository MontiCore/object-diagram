/* (c) https://github.com/MontiCore/monticore */
package de.monticore.od4development;

import de.monticore.ODTestBasis;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OD4DevelopmentToolTest extends ODTestBasis {
  
  private final Path INPUT_OD = PATH.resolve(Paths.get("examples", "od2cd", "Example.od"));
  private final Path INPUT_PATH_DIR = PATH.resolve(Paths.get("symboltable", "tooltest"));
  private final Path TARGET_PATH_DIR =
      Paths.get("target", "generated-test-sources", "tooltest", "symboltable");
  
  @BeforeEach
  public void before() {
    OD4DevelopmentMill.reset();
    OD4DevelopmentMill.init();
  }
  
  @Test
  public void testAddSymtabFile() {
    String[] args = new String[] { "-i", INPUT_OD.toString(), "-path", INPUT_PATH_DIR.toString() };
    assertTrue(OD4DevelopmentMill.globalScope().getSymbolPath().isEmpty());
    OD4DevelopmentToolTOP.main(args);
    assertTrue(OD4DevelopmentMill.globalScope().getSymbolPath().toString()
        .endsWith("resources/symboltable/tooltest/]"));
  }
  
  @Test
  public void testStoreSymtabFile() {
    OD4DevelopmentToolTOP.main(
        new String[] { "-i", INPUT_OD.toString(), "-path", INPUT_PATH_DIR.toString(), "-s",
            TARGET_PATH_DIR.toString() });
    File symTab =
        TARGET_PATH_DIR.resolve(Paths.get("examples", "od2cd", "Examples.odsym")).toFile();
    assertTrue(symTab.exists() && symTab.isFile());
  }
  
}
