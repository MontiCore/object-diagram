package de.monticore.od2plantuml;

import de.monticore.ODTestBasis;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ODPlantUMLToolTest extends ODTestBasis {
  
  private final Path INPUT_OD = PATH.resolve(Paths.get("examples", "od2cd", "Example.od"));
  private final Path INPUT_PATH_DIR = PATH.resolve(Paths.get("symboltable", "tooltest"));
  
  @Test
  void testHelp() {
    String[] args = new String[] { "-h" };
    List<String> out = ODPlantUMLTestUtil.runToolInSeparateProcess(args);
    
    assertEquals(14, out.size());
    assertEquals(" usage:  ODPlantUMLTool [-h] [-i <file>] [-path <dirlist>] [-pp <file>] [-s",
        out.getFirst());
    assertEquals("    <file>]", out.get(1));
  }
  
  @Test
  void testMissingInputPrintsHelp() {
    String[] args = new String[] {};
    List<String> out = ODPlantUMLTestUtil.runToolInSeparateProcess(args);
    
    assertEquals(14, out.size());
    assertEquals(" usage:  ODPlantUMLTool [-h] [-i <file>] [-path <dirlist>] [-pp <file>] [-s",
        out.getFirst());
    assertEquals("    <file>]", out.get(1));
  }
  
  @Test
  void testPrettyPrintToStdout() {
    String[] args =
        new String[] { "-i", INPUT_OD.toString(), "-path", INPUT_PATH_DIR.toString(), "-pp" };
    List<String> out = ODPlantUMLTestUtil.runToolInSeparateProcess(args);
    
    assertEquals(12, out.size());
    assertEquals("@startuml", out.get(1));
    assertEquals("note \"OD\" as tag #white", out.get(2));
    assertEquals("@enduml", out.get(11));
  }
  
  @Test
  void testPrettyPrintToPngFile() {
    Path output = getTmpFilePath("diagram.png");
    
    String[] args =
        new String[] { "-i", INPUT_OD.toString(), "-path", INPUT_PATH_DIR.toString(), "-pp",
            output.toString() };
    List<String> out = ODPlantUMLTestUtil.runToolInSeparateProcess(args);
    
    assertEquals(2, out.size());
    assertEquals(ODPlantUMLTool.SUCCESS_IMAGE_GENERATED.formatted(output.toString()), out.get(1));
    
    assertTrue(Files.exists(output));
  }
  
  @Test
  void testPrettyPrintWithUnsupportedFormat() {
    Path output = getTmpFilePath("diagram.xyz");
    
    String[] args =
        new String[] { "-i", INPUT_OD.toString(), "-path", INPUT_PATH_DIR.toString(), "-pp",
            output.toString() };
    List<String> out = ODPlantUMLTestUtil.runToolInSeparateProcess(args);
    
    assertEquals(2, out.size());
    assertEquals("[ERROR]  " + ODPlantUMLTool.ERROR_UNSUPPORTED_FORMAT.formatted("xyz"),
        out.get(1));
    
    assertFalse(Files.exists(output));
  }
  
  @Test
  void testPrettyPrintWithMissingExtension() {
    Path output = getTmpFilePath("diagram");
    
    String[] args =
        new String[] { "-i", INPUT_OD.toString(), "-path", INPUT_PATH_DIR.toString(), "-pp",
            output.toString() };
    List<String> out = ODPlantUMLTestUtil.runToolInSeparateProcess(args);
    
    assertEquals(2, out.size());
    assertEquals("[ERROR]  " + ODPlantUMLTool.ERROR_MISSING_EXTENSION, out.get(1));
    assertFalse(Files.exists(output));
  }
  
  @Test
  void testPrettyPrintToSvgFile() {
    Path output = getTmpFilePath("diagram.svg");
    
    String[] args =
        new String[] { "-i", INPUT_OD.toString(), "-path", INPUT_PATH_DIR.toString(), "-pp",
            output.toString() };
    List<String> out = ODPlantUMLTestUtil.runToolInSeparateProcess(args);
    
    assertEquals(2, out.size());
    assertEquals(ODPlantUMLTool.SUCCESS_IMAGE_GENERATED.formatted(output.toString()), out.get(1));
    assertTrue(Files.exists(output));
  }
  
  @Test
  void testParseOnlyWithoutPrettyPrint() {
    String[] args = new String[] { "-i", INPUT_OD.toString(), "-path", INPUT_PATH_DIR.toString() };
    List<String> out = ODPlantUMLTestUtil.runToolInSeparateProcess(args);
    
    // no output expected: no -pp given
    assertEquals(0, out.size());
  }
  
  @Test
  void testNonExistentInputFile() {
    String[] args =
        new String[] { "-i", "nonexistent/does_not_exist.od", "-path", INPUT_PATH_DIR.toString(),
            "-pp" };
    List<String> out = ODPlantUMLTestUtil.runToolInSeparateProcess(args);
    
    assertEquals(18, out.size());
    assertEquals(
        "[ERROR]  " + ODPlantUMLTool.PARSE_ERROR_IO.formatted("nonexistent/does_not_exist.od"),
        out.get(0));
  }
}
