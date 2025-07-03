// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report;

import de.monticore.ODOutTestBasis;
import de.monticore.runtime.junit.MCAssertions;
import de.monticore.runtime.junit.TestWithMCLanguage;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@TestWithMCLanguage(OD4ReportMill.class)
public class OD4ReportToolTest extends ODOutTestBasis {
  
  private final Path INPUT_DIR = PATH.resolve(Paths.get("examples", "od"));
  private final Path INPUT_OD = INPUT_DIR.resolve("Examples.od");
  
  @BeforeEach
  void setUp() {
    OD4ReportMill.reset();
  }
  
  @Test
  public void testOD4ReportToolHelp() {
    String[] help = { "-h" };
    OD4ReportTool.main(help);
    
    assertContains(getOut(), "usage: OD4ReportTool");
    MCAssertions.assertNoFindings();
  }
  
  @Test
  public void testOD4ReportToolMissingInput() {
    String[] input = {};
    OD4ReportTool.main(input);
    
    assertContains(getOut(), "usage: OD4ReportTool");
    MCAssertions.assertNoFindings();
  }
  
  @Test
  public void testOD4ReportToolPath() {
    String[] input = { "-i", INPUT_OD.toString(), "-path", PATH.toString(),
        INPUT_DIR.getParent().getParent().resolve("cocos").toString(), "-c", "intra" };
    OD4ReportTool.main(input);
    
    assertEquals(String.format(OD4ReportTool.PARSE_SUCCESSFUL, "Examples") + String.format(
        OD4ReportTool.CHECK_SUCCESSFUL, "Examples"), getOut());
    MCAssertions.assertNoFindings();
  }
  
  @Test
  public void testOD4ReportToolPathWithSymtype() {
    String[] input =
        { "-i", INPUT_DIR.resolve("MyFamily.od").toString(), "-path", PATH.toString(), "-symtypes",
            "de.monticore.cdbasis._symboltable.CDTypeSymbol", "TypeSymbolDeSer", "-c" };
    OD4ReportTool.main(input);
    
    assertEquals(String.format(OD4ReportTool.PARSE_SUCCESSFUL, "MyFamily") + String.format(
        OD4ReportTool.CHECK_SUCCESSFUL, "MyFamily"), getOut());
    MCAssertions.assertNoFindings();
  }
  
  @Test
  public void testOD4ReportToolCocosIntra() {
    String[] input = { "-i", INPUT_DIR.resolve("SimpleOD.od").toString(), "-c", "intra" };
    
    assertEquals("", getOut());
    OD4ReportTool.main(input);
    
    assertEquals(String.format(OD4ReportTool.PARSE_SUCCESSFUL, "SimpleOD") + String.format(
        OD4ReportTool.CHECK_SUCCESSFUL, "SimpleOD"), getOut());
    MCAssertions.assertNoFindings();
  }
  
  @Test
  public void testOD4ReportToolPrettyPrint() {
    String[] input = { "-i", INPUT_OD.toString(), "-pp", "-c", "intra" };
    OD4ReportTool.main(input);
    
    assertContains(getOut(), "objectdiagram Examples {");
    MCAssertions.assertNoFindings();
  }
  
  @Test
  public void testOD4ReportToolPrettyPrintToFile() {
    String ppOutPath = getTmpFilePath("pp.od").toString();
    String[] input = { "-i", INPUT_OD.toString(), "-pp", ppOutPath, "-c", "intra" };
    OD4ReportTool.main(input);
    
    assertTrue(Paths.get(ppOutPath).toFile().exists());
    MCAssertions.assertNoFindings();
  }
  
  @Test
  public void testOD4ReportStoreST() {
    String symOutPath = getTmpFilePath("Examples.odsym").toString();
    String[] input =
        { "-i", INPUT_OD.toString(), "-s", symOutPath, "-path", PATH.toString(), "-symtypes",
            "de.monticore.cdbasis._symboltable.CDTypeSymbol", "TypeSymbolDeSer" };
    OD4ReportTool.main(input);
    
    assertTrue(Paths.get(symOutPath).toFile().exists());
    MCAssertions.assertNoFindings();
  }
  
  @Test
  public void testStoreSymtabFile() {
    Path stTargetPath = getTmpFilePath("symboltable", "examples", "od", "Examples.odsym");
    OD4ReportTool.main(new String[] { "-i", INPUT_OD.toString(), "-path", PATH.toString(), "-s",
        stTargetPath.toString(), "-symtypes", "de.monticore.cdbasis._symboltable.CDTypeSymbol",
        "TypeSymbolDeSer" });
    File symTab = stTargetPath.toFile();
    assertTrue(symTab.exists() && symTab.isFile());
    MCAssertions.assertNoFindings();
  }
  
  @Test
  public void testStoreSymtabFile2() {
    Path existingTargetDirPath = getTmpFilePath("existing");
    assertTrue(existingTargetDirPath.toFile().mkdir());
    OD4ReportTool.main(new String[] { "-i", INPUT_OD.toString(), "-path", PATH.toString(), "-s",
        existingTargetDirPath.toString(), "-symtypes",
        "de.monticore.cdbasis._symboltable.CDTypeSymbol", "TypeSymbolDeSer" });
    File symTab = existingTargetDirPath.resolve("Examples.odsym").toFile();
    assertTrue(symTab.exists() && symTab.isFile());
    MCAssertions.assertNoFindings();
  }
  
  @Test
  public void testStoreSymtabFile3() {
    Path copiedInputFile = getTmpFilePath("examples", "od", "Examples.od");
    assertDoesNotThrow(() -> FileUtils.copyFile(INPUT_OD.toFile(), copiedInputFile.toFile()));
    OD4ReportTool.main(
        new String[] { "-i", copiedInputFile.toString(), "-path", PATH.toString(), "-s",
            "-symtypes", "de.monticore.cdbasis._symboltable.CDTypeSymbol", "TypeSymbolDeSer" });
    File symTab = copiedInputFile.getParent().resolve("Examples.odsym").toFile();
    assertTrue(symTab.exists() && symTab.isFile());
    MCAssertions.assertNoFindings();
  }
  
}
