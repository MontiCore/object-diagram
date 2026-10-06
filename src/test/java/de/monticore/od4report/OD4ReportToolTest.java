// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report;

import de.monticore.ODTestBasis;

import de.se_rwth.commons.logging.RichConsoleLogHook;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
    
    assertEquals(35, out.size());
    assertContains(out.getFirst(), "usage:  OD4ReportTool [-c <arg>] [-h] [-i <file>]");
  }
  
  @Test
  public void testOD4ReportToolVersion() {
    List<String> out = OD4ReportTestUtil.runToolInSeparateProcess("-v");

    assertEquals(1, out.size());
    assertContains(out.getFirst(), "based on MontiCore version");
  }

  @Test
  public void testOD4ReportToolStacktrace() {
    List<String> out =
        OD4ReportTestUtil.runToolInSeparateProcess("-i", INPUT_OD.toString(), "--stacktrace");

    assertEquals(1, out.size());
    assertEquals(getAsInfo(OD4ReportTool.PARSE_SUCCESSFUL, "Examples"), out.getFirst());
  }

  @Test
  public void testOD4ReportToolReportNotSupported() {
    List<String> out =
        OD4ReportTestUtil.runToolInSeparateProcess("-i", INPUT_OD.toString(), "-r", "reports");

    assertEquals(1, out.size());
    assertEquals(getAsError(OD4ReportTool.REPORT_OPTION_NOT_SUPPORTED), out.getFirst());
  }

  @Test
  public void testOD4ReportToolMissingInput() {
    String[] input = {};
    List<String> out = OD4ReportTestUtil.runToolInSeparateProcess(input);
    
    assertEquals(1, out.size());
    assertEquals(getAsError(OD4ReportTool.INPUT_OPTION_NOT_PRESENT, "Examples"), out.getFirst());
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
  public void testOD4ReportToolCocosDefaultWithoutArgument() {
    String[] input =
        { "-i", INPUT_DIR.resolve("SimpleOD.od").toString(), "-path", PATH.toString(), "-c" };
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
    String[] input =
        { "-i", INPUT_DIR.resolve("SimpleOD.od").toString(), "-path", PATH.toString(), "-c",
            "inter" };
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
    String[] input =
        { "-i", INPUT_DIR.resolve("SimpleOD.od").toString(), "-c", "intra", "-c", "inter" };
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
  
  /** {@code -c inter} checks only inter-model CoCos, so intra-model errors are not reported. */
  @Test
  public void testOD4ReportInterCoCosOnly() throws IOException {
    Path model = getTmpFilePath("IntraError.od");
    Files.writeString(model, """
        import examples.cd.SimpleOD.*;
        objectdiagram IntraError {
          o:ObjectType2 { foobar3 -> 1; foobar3 = 2; };
        }
        """);
    String[] args = { "-i", model.toString(), "-path", PATH.toAbsolutePath().toString(), "-c" };

    List<String> inter = OD4ReportTestUtil.runToolInSeparateProcess(
        ArrayUtils.add(args, "inter"));
    assertEquals(getAsInfo(OD4ReportTool.CHECK_SUCCESSFUL, "IntraError"), inter.getLast(),
        String.join("\n", inter));

    List<String> intra = OD4ReportTestUtil.runToolInSeparateProcess(
        ArrayUtils.add(args, "intra"));
    assertTrue(intra.stream().anyMatch(line -> line.contains("0x0D004")), String.join("\n", intra));
  }

  /** Primitive types must be added only once, also if symbol types are given. */
  @Test
  public void testOD4ReportSymtypesWithPrimitiveTypes() {
    Path input = INPUT_DIR.resolve("PrimitiveAttributes.od");
    List<String> out = OD4ReportTestUtil.runToolInSeparateProcess("-i", input.toString(), "-path",
        PATH.toString(), "-symtypes", "de.monticore.cdbasis._symboltable.CDTypeSymbol",
        "TypeSymbolDeSer", "-c");

    assertEquals(2, out.size(), String.join("\n", out));
    assertEquals(getAsInfo(OD4ReportTool.PARSE_SUCCESSFUL, "PrimitiveAttributes"), out.getFirst());
    assertEquals(getAsInfo(OD4ReportTool.CHECK_SUCCESSFUL, "PrimitiveAttributes"), out.get(1));
  }

  /** The symbol table is stored next to an input file that is given without a directory. */
  @Test
  public void testOD4ReportStoreSTWithoutParentPath() throws IOException {
    Path workingDir = getTmpDirPath();
    Files.copy(INPUT_DIR.resolve("PrimitiveAttributes.od"),
        workingDir.resolve("PrimitiveAttributes.od"));

    List<String> out = OD4ReportTestUtil.runToolInSeparateProcess(workingDir, "-i",
        "PrimitiveAttributes.od", "-path", PATH.toAbsolutePath().toString(), "-s");

    Path symTab = workingDir.resolve("PrimitiveAttributes.odsym");
    assertEquals(2, out.size(), String.join("\n", out));
    assertEquals(getAsInfo(OD4ReportTool.STEXPORT_SUCCESSFUL, symTab.toAbsolutePath().toString()),
        out.get(1));
    assertTrue(Files.isRegularFile(symTab));
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
    List<String> out = OD4ReportTestUtil.runToolInSeparateProcess(
        new String[] { "-i", INPUT_OD.toString(), "-path", PATH.toString(), "-s",
            stTargetPath.toString(), "-symtypes", "de.monticore.cdbasis._symboltable.CDTypeSymbol",
            "TypeSymbolDeSer" });
    File symTab = stTargetPath.toFile();
    assertTrue(symTab.exists() && symTab.isFile());

    assertEquals(2, out.size());
    assertEquals(getAsInfo(OD4ReportTool.PARSE_SUCCESSFUL, "Examples"), out.getFirst());
    assertEquals(getAsInfo(OD4ReportTool.STEXPORT_SUCCESSFUL, symTab.getAbsolutePath()),
        out.get(1));
  }
  
  @Test
  public void testStoreSymtabFile2() {
    Path existingTargetDirPath = getTmpFilePath("existing");
    assertTrue(existingTargetDirPath.toFile().mkdir());
    List<String> out = OD4ReportTestUtil.runToolInSeparateProcess(
        new String[] { "-i", INPUT_OD.toString(), "-path", PATH.toString(), "-s",
            existingTargetDirPath.toString(), "-symtypes",
            "de.monticore.cdbasis._symboltable.CDTypeSymbol", "TypeSymbolDeSer" });
    File symTab = existingTargetDirPath.resolve("Examples.odsym").toFile();
    assertTrue(symTab.exists() && symTab.isFile());

    assertEquals(2, out.size());
    assertEquals(getAsInfo(OD4ReportTool.PARSE_SUCCESSFUL, "Examples"), out.getFirst());
    assertEquals(getAsInfo(OD4ReportTool.STEXPORT_SUCCESSFUL, symTab.getAbsolutePath()),
        out.get(1));
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
    assertEquals(getAsInfo(OD4ReportTool.STEXPORT_SUCCESSFUL, symTab.getAbsolutePath()),
        out.get(1));
  }

  protected String getAsInfo(String base, String... data) {
    return RichConsoleLogHook.BLUE + "[INFO]" + RichConsoleLogHook.RESET
        + "  de.monticore.od4report.OD4ReportTool " + base.formatted(data).strip();
  }

  protected String getAsError(String base, String... data) {
    return RichConsoleLogHook.RED_BOLD + "[ERROR]" + RichConsoleLogHook.RESET + "  "
        + base.formatted(data).strip();
  }
  
}
