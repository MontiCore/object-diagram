// (c) https://github.com/MontiCore/monticore

package de.monticore.od4data;

import de.monticore.ODOutTestBasis;
import de.se_rwth.commons.logging.Log;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OD4DataToolTest extends ODOutTestBasis {
  
  private final Path INPUT_DIR = PATH.resolve(Paths.get("examples","od"));
  private final Path INPUT_OD = INPUT_DIR.resolve("SimpleOD2.od");
  
  @BeforeEach
  public void setUp() {
    OD4DataMill.reset();
    OD4DataMill.init();
  }
  
  @Test
  public void testOD4DataToolHelp() {
    String[] help = { "-h" };
    OD4DataTool.main(help);
    
    assertContains(getOut(), "usage: OD4DataTool");
    checkLogError();
  }
  
  @Test
  public void testOD4DataToolMissingInput() {
    String[] input = {};
    OD4DataTool.main(input);
    
    assertContains(getOut(), "usage: OD4DataTool");
    checkLogError();
  }
  
  @Test
  public void testOD4DataToolPath() {
    String[] input = { "-i", INPUT_OD.toString(), "-path", PATH.toString(),
        Paths.get(INPUT_DIR.toString(), "cocos").toString() };
    OD4DataTool.main(input);
    
    assertEquals("", getOut());
    assertEquals(0, Log.getFindingsCount());
  }
  
  @Test
  public void testOD4DataToolIntraCoCos() {
    String[] input = { "-i", INPUT_OD.toString(), "-path", PATH.toString(), "-c", "intra" };
    OD4DataTool.main(input);
    
    assertEquals("", getOut());
    assertEquals(0, Log.getFindingsCount());
  }
  
  @Test
  public void testOD4ToolPrettyPrint() {
    String ppOutPath = getTmpFilePath("pp.od");
    String[] input = { "-i", INPUT_OD.toString(), "-path", PATH.toString(), "-pp", ppOutPath };
    OD4DataTool.main(input);
    
    assertTrue(Paths.get(ppOutPath).toFile().exists());
    assertEquals(0, Log.getFindingsCount());
  }
  
  @Test
  public void testOD4DataStoreST() {
    String symOutPath = getTmpFilePath("Examples.odsym");
    String[] input = { "-i", INPUT_OD.toString(), "-path", PATH.toString(), "-s", symOutPath };
    OD4DataTool.main(input);
    
    assertTrue(Paths.get(symOutPath).toFile().exists());
    assertEquals(0, Log.getFindingsCount());
  }
  
}
