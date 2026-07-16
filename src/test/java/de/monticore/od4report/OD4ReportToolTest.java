// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report;

import de.monticore.ODTestBasis;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OD4ReportToolTest extends ODTestBasis {
  
  private final Path INPUT_DIR = PATH.resolve(Paths.get("examples", "od"));
  private final Path INPUT_OD = INPUT_DIR.resolve("Examples.od");
  
  @Test
  public void testOD4ReportToolHelp() {
    String[] help = { "-h" };
    List<String> out = OD4ReportTestUtil.runToolInSeparateProcess(help);

    assertEquals(16, out.size());
    assertEquals("usage: OD4ReportTool", out.getFirst());
  }
  
  @Test
  public void testOD4ReportToolMissingInput() {
    String[] input = {};
    List<String> out = OD4ReportTestUtil.runToolInSeparateProcess(input);

    assertEquals(16, out.size());
    assertEquals("usage: OD4ReportTool", out.getFirst());
  }
  
  @Test
  public void testOD4ReportToolPath() {
    String[] input = { "-i", INPUT_OD.toString(), "-path", PATH.toString(),
        INPUT_DIR.getParent().getParent().resolve("cocos").toString(), "-c", "intra" };
    List<String> out = OD4ReportTestUtil.runToolInSeparateProcess(input);

    assertEquals(2, out.size());
    assertEquals(getAsInfo(OD4ReportTool.PARSE_SUCCESSFUL, "Examples"), out.getFirst());
    assertEquals(getAsInfo(OD4ReportTool.CHECK_SUCCESSFUL, "Examples"), out.get(1));
  }
  
  @Test
  public void testOD4ReportToolPathWithSymtype() {
    String[] input =
        { "-i", INPUT_DIR.resolve("MyFamily.od").toString(), "-path", PATH.toString(), "-symtypes",
            "de.monticore.cdbasis._symboltable.CDTypeSymbol", "TypeSymbolDeSer", "-c" };
    List<String> out = OD4ReportTestUtil.runToolInSeparateProcess(input);

    assertEquals(2, out.size());
    assertEquals(getAsInfo(OD4ReportTool.PARSE_SUCCESSFUL, "MyFamily"), out.getFirst());
    assertEquals(getAsInfo(OD4ReportTool.CHECK_SUCCESSFUL, "MyFamily"), out.get(1));
  }

  @Test
  public void testOD4ReportToolCocosDefaultWithoutArgument() {
    String[] input = { "-i", INPUT_DIR.resolve("SimpleOD.od").toString(), "-c" };
    List<String> out = OD4ReportTestUtil.runToolInSeparateProcess(input);

    assertEquals(2, out.size());
    assertEquals(getAsInfo(OD4ReportTool.PARSE_SUCCESSFUL, "SimpleOD"), out.getFirst());
    assertEquals(getAsInfo(OD4ReportTool.CHECK_SUCCESSFUL, "SimpleOD"), out.get(1));
  }
  
  @Test
  public void testOD4ReportToolCocosIntra() {
    String[] input = { "-i", INPUT_DIR.resolve("SimpleOD.od").toString(), "-c", "intra" };

    List<String> out = OD4ReportTestUtil.runToolInSeparateProcess(input);

    assertEquals(2, out.size());
    assertEquals(getAsInfo(OD4ReportTool.PARSE_SUCCESSFUL, "SimpleOD"), out.getFirst());
    assertEquals(getAsInfo(OD4ReportTool.CHECK_SUCCESSFUL, "SimpleOD"), out.get(1));
  }

  @Test
  public void testOD4ReportToolCocosInter() {
    String[] input = { "-i", INPUT_DIR.resolve("SimpleOD.od").toString(), "-c", "inter" };
    List<String> out = OD4ReportTestUtil.runToolInSeparateProcess(input);

    assertEquals(2, out.size());
    assertEquals(getAsInfo(OD4ReportTool.PARSE_SUCCESSFUL, "SimpleOD"), out.getFirst());
    assertEquals(getAsInfo(OD4ReportTool.CHECK_SUCCESSFUL, "SimpleOD"), out.get(1));
  }

  @Test
  public void testOD4ReportToolCocosInvalidArgument() {
    String[] input = { "-i", INPUT_DIR.resolve("SimpleOD.od").toString(), "-c", "foo" };
    List<String> out = OD4ReportTestUtil.runToolInSeparateProcess(input);

    assertEquals(2, out.size());
    assertEquals(getAsInfo(OD4ReportTool.PARSE_SUCCESSFUL, "SimpleOD"), out.getFirst());
    assertEquals(getAsError(OD4ReportTool.COCO_OPTION_INVALID, "foo"), out.get(1));
  }

  @Test
  public void testOD4ReportToolCocosTooManyArguments() {
    String[] input = { "-i", INPUT_DIR.resolve("SimpleOD.od").toString(), "-c", "intra",
        "-c", "inter" };
    List<String> out = OD4ReportTestUtil.runToolInSeparateProcess(input);

    assertEquals(2, out.size());
    assertEquals(getAsInfo(OD4ReportTool.PARSE_SUCCESSFUL, "SimpleOD"), out.getFirst());
    assertEquals(getAsError(OD4ReportTool.COCO_OPTION_TOO_MANY_ARGS), out.get(1));
  }
  
  @Test
  public void testOD4ReportToolPrettyPrint() {
    String[] input = { "-i", INPUT_OD.toString(), "-pp", "-c", "intra" };
    List<String> out = OD4ReportTestUtil.runToolInSeparateProcess(input);

    assertTrue(out.stream().anyMatch(line -> line.contains("objectdiagram Examples {")));
  }
  
  @Test
  public void testOD4ReportToolPrettyPrintToFile() {
    String ppOutPath = getTmpFilePath("pp.od").toString();
    String[] input = { "-i", INPUT_OD.toString(), "-pp", ppOutPath, "-c", "intra" };
    List<String> out = OD4ReportTestUtil.runToolInSeparateProcess(input);

    assertEquals(2, out.size());
    assertEquals(getAsInfo(OD4ReportTool.PARSE_SUCCESSFUL, "Examples"), out.getFirst());
    assertEquals(getAsInfo(OD4ReportTool.CHECK_SUCCESSFUL, "Examples"), out.get(1));
    assertTrue(Paths.get(ppOutPath).toFile().exists());
  }
  
  @Test
  public void testOD4ReportStoreST() {
    String symOutPath = getTmpFilePath("Examples.odsym").toString();
    String[] input =
        { "-i", INPUT_OD.toString(), "-s", symOutPath, "-path", PATH.toString(), "-symtypes",
            "de.monticore.cdbasis._symboltable.CDTypeSymbol", "TypeSymbolDeSer" };
    List<String> out = OD4ReportTestUtil.runToolInSeparateProcess(input);

    assertEquals(2, out.size());
    assertEquals(getAsInfo(OD4ReportTool.PARSE_SUCCESSFUL, "Examples"), out.getFirst());
    assertEquals(getAsInfo(OD4ReportTool.STEXPORT_SUCCESSFUL,
        Paths.get(symOutPath).toFile().getAbsolutePath()), out.get(1));
    assertTrue(Paths.get(symOutPath).toFile().exists());
  }
  
  @Test
  public void testStoreSymtabFile() {
    Path stTargetPath = getTmpFilePath("symboltable", "examples", "od", "Examples.odsym");
    List<String> out = OD4ReportTestUtil.runToolInSeparateProcess(new String[] { "-i",
        INPUT_OD.toString(), "-path", PATH.toString(), "-s", stTargetPath.toString(), "-symtypes",
        "de.monticore.cdbasis._symboltable.CDTypeSymbol", "TypeSymbolDeSer" });
    File symTab = stTargetPath.toFile();
    assertTrue(symTab.exists() && symTab.isFile());

    assertEquals(2, out.size());
    assertEquals(getAsInfo(OD4ReportTool.PARSE_SUCCESSFUL, "Examples"), out.getFirst());
    assertEquals(getAsInfo(OD4ReportTool.STEXPORT_SUCCESSFUL, symTab.getAbsolutePath()), out.get(1));
  }
  
  @Test
  public void testStoreSymtabFile2() {
    Path existingTargetDirPath = getTmpFilePath("existing");
    assertTrue(existingTargetDirPath.toFile().mkdir());
    List<String> out = OD4ReportTestUtil.runToolInSeparateProcess(new String[] { "-i",
        INPUT_OD.toString(), "-path", PATH.toString(), "-s", existingTargetDirPath.toString(),
        "-symtypes", "de.monticore.cdbasis._symboltable.CDTypeSymbol", "TypeSymbolDeSer" });
    File symTab = existingTargetDirPath.resolve("Examples.odsym").toFile();
    assertTrue(symTab.exists() && symTab.isFile());

    assertEquals(2, out.size());
    assertEquals(getAsInfo(OD4ReportTool.PARSE_SUCCESSFUL, "Examples"), out.getFirst());
    assertEquals(getAsInfo(OD4ReportTool.STEXPORT_SUCCESSFUL, symTab.getAbsolutePath()), out.get(1));
  }
  
  @Test
  public void testStoreSymtabFile3() {
    Path copiedInputFile = getTmpFilePath("examples", "od", "Examples.od");
    assertDoesNotThrow(() -> FileUtils.copyFile(INPUT_OD.toFile(), copiedInputFile.toFile()));
    List<String> out = OD4ReportTestUtil.runToolInSeparateProcess(
        new String[] { "-i", copiedInputFile.toString(), "-path", PATH.toString(), "-s",
            "-symtypes", "de.monticore.cdbasis._symboltable.CDTypeSymbol", "TypeSymbolDeSer" });
    File symTab = copiedInputFile.getParent().resolve("Examples.odsym").toFile();
    assertTrue(symTab.exists() && symTab.isFile());

    assertEquals(2, out.size());
    assertEquals(getAsInfo(OD4ReportTool.PARSE_SUCCESSFUL, "Examples"), out.getFirst());
    assertEquals(getAsInfo(OD4ReportTool.STEXPORT_SUCCESSFUL, symTab.getAbsolutePath()), out.get(1));
  }

  protected String getAsInfo(String base, String... data) {
    return "[INFO]  de.monticore.od4report.OD4ReportTool " + base.formatted(data).strip();
  }

  protected String getAsError(String base, String... data) {
    return "[ERROR]  " + base.formatted(data).strip();
  }

}
