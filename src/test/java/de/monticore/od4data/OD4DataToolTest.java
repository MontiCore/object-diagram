// (c) https://github.com/MontiCore/monticore

package de.monticore.od4data;

import de.monticore.ODOutTestBasis;
import de.monticore.runtime.junit.MCAssertions;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class OD4DataToolTest extends ODOutTestBasis {
  
  private final Path INPUT_DIR = PATH.resolve(Paths.get("examples", "od"));
  private final Path INPUT_OD = INPUT_DIR.resolve("SimpleOD2.od");
  
  @BeforeEach
  void setUp() {
    OD4DataMill.reset();
  }
  
  @Test
  public void testOD4DataToolHelp() {
    String[] help = { "-h" };
    new OD4DataTool().run(help);
    
    assertContains(getOut(), "usage:  OD4DataTool [-c <arg>] [-h]");
    MCAssertions.assertNoFindings();
  }
  
  @Test
  public void testOD4DataToolMissingInput() {
    String[] input = {};
    new OD4DataTool().run(input);
    
    assertContains(getOut(), "usage:  OD4DataTool [-c <arg>] [-h]");
    MCAssertions.assertNoFindings();
  }
  
  @Test
  public void testOD4DataToolPath() {
    String[] input = { "-i", INPUT_OD.toString(), "-path", PATH.toString(),
        Paths.get(INPUT_DIR.toString(), "cocos").toString() };
    new OD4DataTool().run(input);
    
    assertEquals(String.format(OD4DataTool.PARSE_SUCCESSFUL, "SimpleOD2"), getOut());
    MCAssertions.assertNoFindings();
  }
  
  @Test
  public void testOD4DataToolIntraCoCos() {
    String[] input = { "-i", INPUT_OD.toString(), "-path", PATH.toString(), "-c", "intra" };
    new OD4DataTool().run(input);
    
    assertEquals(String.format(OD4DataTool.PARSE_SUCCESSFUL, "SimpleOD2") + String.format(
        OD4DataTool.CHECK_SUCCESSFUL, "SimpleOD2"), getOut());
    MCAssertions.assertNoFindings();
  }
  
  @Test
  public void testOD4ToolPrettyPrint() {
    String ppOutPath = getTmpFilePath("pp.od").toString();
    String[] input = { "-i", INPUT_OD.toString(), "-path", PATH.toString(), "-pp", ppOutPath };
    new OD4DataTool().run(input);
    
    assertTrue(Paths.get(ppOutPath).toFile().exists());
    MCAssertions.assertNoFindings();
  }
  
  @Test
  public void testOD4DataStoreST() {
    String symOutPath = getTmpFilePath("SimpleOD2.odsym").toString();
    String[] input = { "-i", INPUT_OD.toString(), "-path", PATH.toString(), "-s", symOutPath };
    new OD4DataTool().run(input);
    
    assertTrue(Paths.get(symOutPath).toFile().exists());
    MCAssertions.assertNoFindings();
  }
  
  @Test
  public void testStoreSymtabFile() {
    Path stTargetPath = getTmpFilePath("symboltable", "examples", "od", "SimpleOD2.odsym");
    new OD4DataTool().run(new String[] { "-i", INPUT_OD.toString(), "-path", PATH.toString(), "-s",
        stTargetPath.toString() });
    File symTab = stTargetPath.toFile();
    assertTrue(symTab.exists() && symTab.isFile());
    MCAssertions.assertNoFindings();
  }
  
  @Test
  public void testStoreSymtabFile2() {
    Path existingTargetDirPath = getTmpFilePath("existing");
    assertTrue(existingTargetDirPath.toFile().mkdir());
    new OD4DataTool().run(new String[] { "-i", INPUT_OD.toString(), "-path", PATH.toString(), "-s",
        existingTargetDirPath.toString() });
    File symTab = existingTargetDirPath.resolve("SimpleOD2.odsym").toFile();
    assertTrue(symTab.exists() && symTab.isFile());
    MCAssertions.assertNoFindings();
  }
  
  @Test
  public void testStoreSymtabFile3() {
    Path copiedInputFile = getTmpFilePath("examples", "od", "SimpleOD2.od");
    assertDoesNotThrow(() -> FileUtils.copyFile(INPUT_OD.toFile(), copiedInputFile.toFile()));
    new OD4DataTool().run(
        new String[] { "-i", copiedInputFile.toString(), "-path", PATH.toString(), "-s" });
    File symTab = copiedInputFile.getParent().resolve("SimpleOD2.odsym").toFile();
    assertTrue(symTab.exists() && symTab.isFile());
    MCAssertions.assertNoFindings();
  }
  
}
