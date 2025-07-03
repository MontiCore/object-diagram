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
    OD4DataTool.main(help);
    
    assertContains(getOut(), "usage: OD4DataTool");
    MCAssertions.assertNoFindings();
  }
  
  @Test
  public void testOD4DataToolMissingInput() {
    String[] input = {};
    OD4DataTool.main(input);
    
    assertContains(getOut(), "usage: OD4DataTool");
    MCAssertions.assertNoFindings();
  }
  
  @Test
  public void testOD4DataToolPath() {
    String[] input = { "-i", INPUT_OD.toString(), "-path", PATH.toString(),
        Paths.get(INPUT_DIR.toString(), "cocos").toString() };
    OD4DataTool.main(input);
    
    assertEquals(String.format(OD4DataTool.PARSE_SUCCESSFUL, "SimpleOD2"), getOut());
    MCAssertions.assertNoFindings();
  }
  
  @Test
  public void testOD4DataToolIntraCoCos() {
    String[] input = { "-i", INPUT_OD.toString(), "-path", PATH.toString(), "-c", "intra" };
    OD4DataTool.main(input);
    
    assertEquals(String.format(OD4DataTool.PARSE_SUCCESSFUL, "SimpleOD2") + String.format(
        OD4DataTool.CHECK_SUCCESSFUL, "SimpleOD2"), getOut());
    MCAssertions.assertNoFindings();
  }
  
  @Test
  public void testOD4ToolPrettyPrint() {
    String ppOutPath = getTmpFilePath("pp.od").toString();
    String[] input = { "-i", INPUT_OD.toString(), "-path", PATH.toString(), "-pp", ppOutPath };
    OD4DataTool.main(input);
    
    assertTrue(Paths.get(ppOutPath).toFile().exists());
    MCAssertions.assertNoFindings();
  }
  
  @Test
  public void testOD4DataStoreST() {
    String symOutPath = getTmpFilePath("SimpleOD2.odsym").toString();
    String[] input = { "-i", INPUT_OD.toString(), "-path", PATH.toString(), "-s", symOutPath };
    OD4DataTool.main(input);
    
    assertTrue(Paths.get(symOutPath).toFile().exists());
    MCAssertions.assertNoFindings();
  }
  
  @Test
  public void testStoreSymtabFile() {
    Path stTargetPath = getTmpFilePath("symboltable", "examples", "od", "SimpleOD2.odsym");
    OD4DataTool.main(new String[] { "-i", INPUT_OD.toString(), "-path", PATH.toString(), "-s",
        stTargetPath.toString() });
    File symTab = stTargetPath.toFile();
    assertTrue(symTab.exists() && symTab.isFile());
    MCAssertions.assertNoFindings();
  }
  
  @Test
  public void testStoreSymtabFile2() {
    Path existingTargetDirPath = getTmpFilePath("existing");
    assertTrue(existingTargetDirPath.toFile().mkdir());
    OD4DataTool.main(new String[] { "-i", INPUT_OD.toString(), "-path", PATH.toString(), "-s",
        existingTargetDirPath.toString() });
    File symTab = existingTargetDirPath.resolve("SimpleOD2.odsym").toFile();
    assertTrue(symTab.exists() && symTab.isFile());
    MCAssertions.assertNoFindings();
  }
  
  @Test
  public void testStoreSymtabFile3() {
    Path copiedInputFile = getTmpFilePath("examples", "od", "SimpleOD2.od");
    assertDoesNotThrow(() -> FileUtils.copyFile(INPUT_OD.toFile(), copiedInputFile.toFile()));
    OD4DataTool.main(
        new String[] { "-i", copiedInputFile.toString(), "-path", PATH.toString(), "-s" });
    File symTab = copiedInputFile.getParent().resolve("SimpleOD2.odsym").toFile();
    assertTrue(symTab.exists() && symTab.isFile());
    MCAssertions.assertNoFindings();
  }
  
}
