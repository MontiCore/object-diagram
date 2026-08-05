/* (c) https://github.com/MontiCore/monticore */
package de.monticore.od4development;

import de.monticore.ODOutTestBasis;
import de.monticore.runtime.junit.MCAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class OD4DevelopmentToolTest extends ODOutTestBasis {
  
  private final Path INPUT_OD = PATH.resolve(Paths.get("examples", "od2cd", "Example.od"));
  private final Path INPUT_PATH_DIR = PATH.resolve(Paths.get("symboltable", "tooltest"));
  
  @BeforeEach
  void setUp() {
    OD4DevelopmentMill.reset();
  }
  
  @Test
  public void testOD4DevelopmentToolHelp() {
    String[] help = { "-h" };
    new OD4DevelopmentTool().run(help);
    
    assertContains(getOut(), "usage:  OD4DevelopmentTool [-c] [-h]");
    MCAssertions.assertNoFindings();
  }
  
  @Test
  public void testOD4DevelopmentToolMissingInput() {
    String[] input = {};
    new OD4DevelopmentTool().run(input);
    
    assertContains(getOut(), "usage:  OD4DevelopmentTool [-c] [-h]");
    MCAssertions.assertNoFindings();
  }
  
  @Test
  public void testOD4DevelopmentToolPath() {
    String[] input = { "-i", INPUT_OD.toString(), "-path", INPUT_PATH_DIR.toString(),
        Paths.get(INPUT_PATH_DIR.toString(), "cocos").toString() };
    new OD4DevelopmentTool().run(input);
    
    assertEquals(String.format(OD4DevelopmentTool.PARSE_SUCCESSFUL, "Examples"), getOut());
    MCAssertions.assertNoFindings();
  }
  
  @Test
  public void testOD4DevelopmentToolIntraCoCos() {
    String[] input =
        { "-i", INPUT_OD.toString(), "-path", INPUT_PATH_DIR.toString(), "-c", "intra" };
    new OD4DevelopmentTool().run(input);
    
    assertEquals(String.format(OD4DevelopmentTool.PARSE_SUCCESSFUL, "Examples") + String.format(
        OD4DevelopmentTool.CHECK_SUCCESSFUL, "Examples"), getOut());
    MCAssertions.assertNoFindings();
  }
  
  @Test
  public void testOD4ToolPrettyPrint() {
    String ppOutPath = getTmpFilePath("pp.od").toString();
    String[] input =
        { "-i", INPUT_OD.toString(), "-path", INPUT_PATH_DIR.toString(), "-pp", ppOutPath };
    new OD4DevelopmentTool().run(input);
    
    assertTrue(Paths.get(ppOutPath).toFile().exists());
    MCAssertions.assertNoFindings();
  }
  
  @Test
  public void testAddSymtabFile() {
    String[] args = new String[] { "-i", INPUT_OD.toString(), "-path", INPUT_PATH_DIR.toString() };
    assertTrue(OD4DevelopmentMill.globalScope().getSymbolPath().isEmpty());
    new OD4DevelopmentTool().run(args);
    assertTrue(OD4DevelopmentMill.globalScope().getSymbolPath().toString()
        .endsWith("resources/symboltable/tooltest/]"));
    
    assertEquals(String.format(OD4DevelopmentTool.PARSE_SUCCESSFUL, "Examples"), getOut());
    MCAssertions.assertNoFindings();
  }
  
  @Test
  public void testStoreSymtabFile() {
    Path stTargetPath = getTmpFilePath("symboltable", "examples", "od2cd", "Examples.odsym");
    new OD4DevelopmentTool().run(
        new String[] { "-i", INPUT_OD.toString(), "-path", INPUT_PATH_DIR.toString(), "-s",
            stTargetPath.toString() });
    File symTab = stTargetPath.toFile();
    assertTrue(symTab.exists() && symTab.isFile());
    assertEquals(String.format(OD4DevelopmentTool.PARSE_SUCCESSFUL, "Examples") + String.format(
        OD4DevelopmentTool.STEXPORT_SUCCESSFUL, symTab.getAbsolutePath()), getOut());
    MCAssertions.assertNoFindings();
  }
  
  @Test
  public void testStoreSymtabFile2() {
    Path existingTargetDirPath = getTmpFilePath("existing");
    assertTrue(existingTargetDirPath.toFile().mkdir());
    new OD4DevelopmentTool().run(
        new String[] { "-i", INPUT_OD.toString(), "-path", INPUT_PATH_DIR.toString(), "-s",
            existingTargetDirPath.toString() });
    File symTab = getTmpFilePath("existing", "Example.odsym").toFile();
    assertTrue(symTab.exists() && symTab.isFile());
    assertEquals(String.format(OD4DevelopmentTool.PARSE_SUCCESSFUL, "Examples") + String.format(
        OD4DevelopmentTool.STEXPORT_SUCCESSFUL, symTab.getAbsolutePath()), getOut());
    MCAssertions.assertNoFindings();
  }
}
