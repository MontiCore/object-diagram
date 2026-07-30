/* (c) https://github.com/MontiCore/monticore */
package de.monticore.od4development;

import de.monticore.ODTestBasis;
import de.se_rwth.commons.logging.RichConsoleLogHook;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class OD4DevelopmentToolTest extends ODTestBasis {
  
  private final Path INPUT_OD = PATH.resolve(Paths.get("examples", "od2cd", "Example.od"));
  private final Path INPUT_PATH_DIR = PATH.resolve(Paths.get("symboltable", "tooltest"));
  
  @BeforeEach
  void setUp() {
    OD4DevelopmentMill.reset();
  }
  
  @Test
  public void testOD4DevelopmentToolHelp() {
    String[] help = { "-h" };
    List<String> out = OD4DevelopmentTestUtil.runToolInSeparateProcess(help);
    
    assertEquals(16, out.size());
    assertContains(out.getFirst(), "usage: OD4DevelopmentTool");
  }
  
  @Test
  public void testOD4DevelopmentToolMissingInput() {
    String[] input = {};
    List<String> out = OD4DevelopmentTestUtil.runToolInSeparateProcess(input);
    
    assertEquals(1, out.size());
    assertEquals(getAsError(OD4DevelopmentTool.INPUT_OPTION_NOT_PRESENT), out.getFirst());
  }
  
  @Test
  public void testOD4DevelopmentToolPath() {
    String[] input = { "-i", INPUT_OD.toString(), "-path", INPUT_PATH_DIR.toString(),
        Paths.get(INPUT_PATH_DIR.toString(), "cocos").toString() };
    
    List<String> out = OD4DevelopmentTestUtil.runToolInSeparateProcess(input);
    
    assertEquals(1, out.size());
    assertEquals(getAsInfo(OD4DevelopmentTool.PARSE_SUCCESSFUL, "Examples"), out.getFirst());
  }
  
  @Test
  public void testOD4DevelopmentToolIntraCoCos() {
    String[] input =
        { "-i", INPUT_OD.toString(), "-c", "intra" };
    new OD4DevelopmentTool().run(input);
    List<String> out = OD4DevelopmentTestUtil.runToolInSeparateProcess(input);
    
    assertEquals(2, out.size());
    assertEquals(getAsInfo(OD4DevelopmentTool.PARSE_SUCCESSFUL, "Examples"), out.getFirst());
    assertEquals(getAsInfo(OD4DevelopmentTool.CHECK_SUCCESSFUL, "Examples"), out.get(1));
  }

  @Test
  public void testOD4DevelopmentToolAllCoCosWithoutArgument() {
    String[] input = { "-i", INPUT_OD.toString(), "-path", INPUT_PATH_DIR.toString(), "-c" };
    List<String> out = OD4DevelopmentTestUtil.runToolInSeparateProcess(input);
    
    assertEquals(2, out.size());
    assertEquals(getAsInfo(OD4DevelopmentTool.PARSE_SUCCESSFUL, "Examples"), out.getFirst());
    assertEquals(getAsInfo(OD4DevelopmentTool.CHECK_SUCCESSFUL, "Examples"), out.get(1));
  }

  @Test
  public void testOD4DevelopmentToolCocoInvalidArgument() {
    String[] input = { "-i", INPUT_OD.toString(), "-path", INPUT_PATH_DIR.toString(), "-c",
        "foo" };
    List<String> out = OD4DevelopmentTestUtil.runToolInSeparateProcess(input);
    
    assertEquals(2, out.size());
    assertEquals(getAsInfo(OD4DevelopmentTool.PARSE_SUCCESSFUL, "Examples"), out.getFirst());
    assertEquals(getAsError(OD4DevelopmentTool.COCO_OPTION_INVALID, "foo"), out.get(1));
  }

  @Test
  public void testOD4DevelopmentToolCocoTooManyArguments() {
    String[] input = { "-i", INPUT_OD.toString(), "-path", INPUT_PATH_DIR.toString(), "-c",
        "intra", "-c", "inter" };
    List<String> out = OD4DevelopmentTestUtil.runToolInSeparateProcess(input);
    
    assertEquals(2, out.size());
    assertEquals(getAsInfo(OD4DevelopmentTool.PARSE_SUCCESSFUL, "Examples"), out.getFirst());
    assertEquals(getAsError(OD4DevelopmentTool.COCO_OPTION_TOO_MANY_ARGS), out.get(1));
  }

  @Test
  public void testOD4DevelopmentToolPrettyPrintToStdout() {
    String[] input = { "-i", INPUT_OD.toString(), "-pp" };
    List<String> out = OD4DevelopmentTestUtil.runToolInSeparateProcess(input);
    
    assertEquals(18, out.size());
    assertEquals("/* (c) https://github.com/MontiCore/monticore */", out.getFirst());
    assertEquals("package examples.od2cd;", out.get(1));
    assertEquals("objectdiagram Examples { ", out.get(2));
  }

  @Test
  public void testOD4DevelopmentToolOutputWithoutArgument() {
    String[] input = { "-i", INPUT_OD.toString(), "-path", INPUT_PATH_DIR.toString(), "-o" };
    List<String> out = OD4DevelopmentTestUtil.runToolInSeparateProcess(input);
    
    assertEquals(2, out.size());
    assertEquals(getAsInfo(OD4DevelopmentTool.PARSE_SUCCESSFUL, "Examples"), out.getFirst());
    assertEquals(getAsError(OD4DevelopmentTool.OUTPUT_OPTION_MISSING_ARG), out.get(1));
  }

  @Test
  public void testOD4DevelopmentToolOutputWithBlankArgument() {
    String[] input = { "-i", INPUT_OD.toString(), "-path", INPUT_PATH_DIR.toString(), "-o", "   " };
    List<String> out = OD4DevelopmentTestUtil.runToolInSeparateProcess(input);
    
    assertEquals(2, out.size());
    assertEquals(getAsInfo(OD4DevelopmentTool.PARSE_SUCCESSFUL, "Examples"), out.getFirst());
    assertEquals(getAsError(OD4DevelopmentTool.OUTPUT_OPTION_MISSING_ARG), out.get(1));
  }

  @Test
  public void testOD4DevelopmentToolOutputDirectoryGeneration() throws Exception {
    Path outputDir = getTmpFilePath("generated-cd");
    String[] input = { "-i", INPUT_OD.toString(), "-path", INPUT_PATH_DIR.toString(), "-o", outputDir.toString() };
    List<String> out = OD4DevelopmentTestUtil.runToolInSeparateProcess(input);
    
    assertEquals(3, out.size());
    assertEquals(getAsInfo(OD4DevelopmentTool.PARSE_SUCCESSFUL, "Examples"), out.getFirst());
    assertTrue(Files.isDirectory(outputDir));
    try (Stream<Path> generatedFiles = Files.walk(outputDir)) {
      assertTrue(generatedFiles.anyMatch(Files::isRegularFile));
    }
  }

  @Test
  public void testOD4DevelopmentToolOutputPathInvalidFile() throws Exception {
    Path invalidOutput = getTmpFilePath("output-target.txt");
    Files.writeString(invalidOutput, "dummy");

    String[] input = { "-i", INPUT_OD.toString(), "-path", INPUT_PATH_DIR.toString(), "-o", invalidOutput.toString() };
    List<String> out = OD4DevelopmentTestUtil.runToolInSeparateProcess(input);
    
    assertEquals(2, out.size());
    assertEquals(getAsInfo(OD4DevelopmentTool.PARSE_SUCCESSFUL, "Examples"), out.getFirst());
    assertEquals(getAsError(OD4DevelopmentTool.OUTPUT_PATH_INVALID, invalidOutput.toString()), out.get(1));
  }
  
  @Test
  public void testOD4ToolPrettyPrint() {
    String ppOutPath = getTmpFilePath("pp.od").toString();
    String[] input =
        { "-i", INPUT_OD.toString(), "-pp", ppOutPath };
    List<String> out = OD4DevelopmentTestUtil.runToolInSeparateProcess(input);
    
    assertEquals(2, out.size());
    assertEquals(getAsInfo(OD4DevelopmentTool.PARSE_SUCCESSFUL, "Examples"), out.getFirst());
    assertEquals(getAsInfo(OD4DevelopmentTool.PRETTYPRINT_SUCCESSFUL, ppOutPath), out.get(1));
    assertTrue(Paths.get(ppOutPath).toFile().exists());
  }
  
  @Test
  public void testStoreSymtabFile() {
    Path stTargetPath = getTmpFilePath("symboltable", "examples", "od2cd", "Examples.odsym");
    String[] args = new String[] { "-i", INPUT_OD.toString(), "-path", INPUT_PATH_DIR.toString(), "-s",
        stTargetPath.toString() };
    List<String> out = OD4DevelopmentTestUtil.runToolInSeparateProcess(args);
    File symTab = stTargetPath.toFile();
    assertTrue(symTab.exists() && symTab.isFile());
    
    assertEquals(2, out.size());
    assertEquals(getAsInfo(OD4DevelopmentTool.PARSE_SUCCESSFUL, "Examples"), out.getFirst());
    assertEquals(getAsInfo(OD4DevelopmentTool.STEXPORT_SUCCESSFUL, symTab.getAbsolutePath()), out.get(1));
  }
  
  @Test
  public void testStoreSymtabFile2() {
    Path existingTargetDirPath = getTmpFilePath("existing");
    assertTrue(existingTargetDirPath.toFile().mkdir());
    String[] args = new String[] { "-i", INPUT_OD.toString(), "-path", INPUT_PATH_DIR.toString(), "-s",
        existingTargetDirPath.toString() };
    List<String> out = OD4DevelopmentTestUtil.runToolInSeparateProcess(args);
    
    File symTab = getTmpFilePath("existing", "Example.odsym").toFile();
    assertTrue(symTab.exists() && symTab.isFile());
    
    assertEquals(2, out.size());
    assertEquals(getAsInfo(OD4DevelopmentTool.PARSE_SUCCESSFUL, "Examples"), out.getFirst());
    assertEquals(getAsInfo(OD4DevelopmentTool.STEXPORT_SUCCESSFUL, symTab.getAbsolutePath()), out.get(1));
  }
  
  @Test
  public void testStoreSymtabFile3() {
    Path copiedInputFile = getTmpFilePath("examples", "od", "SimpleOD2.od");
    assertDoesNotThrow(() -> FileUtils.copyFile(INPUT_OD.toFile(), copiedInputFile.toFile()));
    String[] args = new String[] { "-i", copiedInputFile.toString(), "-path", INPUT_PATH_DIR.toString(), "-s" };
    List<String> out = OD4DevelopmentTestUtil.runToolInSeparateProcess(args);
    
    File symTab = copiedInputFile.getParent().resolve("SimpleOD2.odsym").toFile();
    assertTrue(symTab.exists() && symTab.isFile());
    
    assertEquals(2, out.size());
    assertEquals(getAsInfo(OD4DevelopmentTool.PARSE_SUCCESSFUL, "Examples"), out.getFirst());
    assertEquals(getAsInfo(OD4DevelopmentTool.STEXPORT_SUCCESSFUL, symTab.getAbsolutePath()), out.get(1));
  }

  @Test
  public void testStoreSymtabFileWithoutParentPath() {
    Path tempDir = getTmpFilePath("relative-input");
    assertDoesNotThrow(() -> Files.createDirectories(tempDir));

    Path relativeInput = tempDir.resolve("Example.od");
    assertDoesNotThrow(() -> FileUtils.copyFile(INPUT_OD.toFile(), relativeInput.toFile()));

    String previousUserDir = System.getProperty("user.dir");
    try {
      System.setProperty("user.dir", tempDir.toString());
      String[] args = new String[] { "-i", relativeInput.toString(), "-path", INPUT_PATH_DIR.toString(), "-s" };
      List<String> out = OD4DevelopmentTestUtil.runToolInSeparateProcess(args);
      
      assertEquals(2, out.size());
      assertEquals(getAsInfo(OD4DevelopmentTool.PARSE_SUCCESSFUL, "Examples"), out.getFirst());
      assertEquals(getAsInfo(OD4DevelopmentTool.STEXPORT_SUCCESSFUL, relativeInput.getParent().resolve("Example.odsym").toFile().getAbsolutePath()), out.get(1));
    }
    finally {
      System.setProperty("user.dir", previousUserDir);
    }

    File symTab = tempDir.resolve("Example.odsym").toFile();
    assertTrue(symTab.exists() && symTab.isFile());
  }
  
  protected String getAsInfo(String base, String... data) {
    return RichConsoleLogHook.BLUE + "[INFO]" + RichConsoleLogHook.RESET
        + "  de.monticore.od4development.OD4DevelopmentTool " + base.formatted(data).strip();
  }
  
  protected String getAsError(String base, String... data) {
    return RichConsoleLogHook.RED_BOLD + "[ERROR]" + RichConsoleLogHook.RESET + "  "
        + base.formatted(data).strip();
  }
}
