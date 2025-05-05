// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report;

import de.monticore.ODOutTestBasis;
import de.se_rwth.commons.logging.Log;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OD4ReportToolTest extends ODOutTestBasis {
  
  private final Path INPUT_DIR = PATH.resolve(Paths.get("examples", "od"));
  private final Path INPUT_OD = INPUT_DIR.resolve("Examples.od");
  
  @BeforeEach
  public void setUp() {
    OD4ReportMill.reset();
    OD4ReportMill.init();
  }
  
  @Test
  public void testOD4ReportToolHelp() {
    String[] help = { "-h" };
    OD4ReportTool.main(help);
    
    assertContains(getOut(), "usage: OD4ReportTool");
    checkLogError();
  }
  
  @Test
  public void testOD4ReportToolMissingInput() {
    String[] input = {};
    OD4ReportTool.main(input);
    
    assertContains(getOut(), "usage: OD4ReportTool");
    checkLogError();
  }
  
  @Test
  public void testOD4ReportToolPath() {
    String[] input = { "-i", INPUT_OD.toString(), "-path", PATH.toString(),
        INPUT_DIR.getParent().getParent().resolve("cocos").toString(), "-c", "intra" };
    OD4ReportTool.main(input);
    
    assertEquals("", getOut());
    assertEquals(0, Log.getFindingsCount());
  }
  
  @Test
  public void testOD4ReportToolPathWithSymtype() {
    String[] input =
        { "-i", INPUT_DIR.resolve("MyFamily.od").toString(), "-path", PATH.toString(), "-symtypes",
            "de.monticore.cdbasis._symboltable.CDTypeSymbol", "TypeSymbolDeSer", "-c" };
    OD4ReportTool.main(input);
    
    assertEquals("", getOut());
    assertEquals(0, Log.getFindingsCount());
  }
  
  @Test
  public void testOD4ReportToolCocosIntra() {
    String[] input = { "-i", INPUT_DIR.resolve("SimpleOD.od").toString(), "-c", "intra" };
    
    assertEquals("", getOut());
    OD4ReportTool.main(input);
    
    assertEquals("", getOut());
    assertEquals(0, Log.getFindingsCount());
  }
  
  @Test
  public void testOD4ReportToolPrettyPrint() {
    String[] input = { "-i", INPUT_OD.toString(), "-pp", "-c", "intra" };
    OD4ReportTool.main(input);
    
    assertContains(getOut(), "objectdiagram Examples {");
    assertEquals(0, Log.getFindingsCount());
  }
  
  @Test
  public void testOD4ReportToolPrettyPrintToFile() {
    String ppOutPath = getTmpFilePath("pp.od");
    String[] input = { "-i", INPUT_OD.toString(), "-pp", ppOutPath, "-c", "intra" };
    OD4ReportTool.main(input);
    
    assertTrue(Paths.get(ppOutPath).toFile().exists());
    assertEquals(0, Log.getFindingsCount());
  }
  
  @Test
  public void testOD4ReportStoreST() {
    String symOutPath = getTmpFilePath("Examples.odsym");
    String[] input =
        { "-i", INPUT_OD.toString(), "-s", symOutPath, "-path", PATH.toString(), "-symtypes",
            "de.monticore.cdbasis._symboltable.CDTypeSymbol", "TypeSymbolDeSer" };
    OD4ReportTool.main(input);
    
    assertTrue(Paths.get(symOutPath).toFile().exists());
    assertEquals(0, Log.getFindingsCount());
  }
  
}
