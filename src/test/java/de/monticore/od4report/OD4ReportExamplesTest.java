// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report;

import de.monticore.ODTestBasis;
import de.monticore.od4report._parser.OD4ReportParser;
import de.monticore.od4report._prettyprint.OD4ReportFullPrettyPrinter;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.prettyprint.IndentPrinter;
import de.se_rwth.commons.logging.Log;
import org.antlr.v4.runtime.RecognitionException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This test compares the ASTs of the files in the examples folder with the pretty-printed versions
 * of these files.
 */
public class OD4ReportExamplesTest extends ODTestBasis {
  
  @BeforeEach
  public void setUp() {
    OD4ReportMill.reset();
    OD4ReportMill.init();
  }
  
  @ParameterizedTest
  @ValueSource(strings = { "src/test/resources/examples/od/Examples.od",
      "src/test/resources/examples/od/SimpleOD.od",
      "src/test/resources/examples/od/ProjectListOD.od",
      "src/test/resources/examples/od/MyFamily.od" })
  public void testSuccessfulParsePP(String modelName) throws RecognitionException, IOException {
    Path model = Paths.get(modelName);
    OD4ReportParser parser = new OD4ReportParser();
    Optional<ASTODArtifact> odDef = parser.parse(model.toString());
    assertFalse(parser.hasErrors());
    assertTrue(odDef.isPresent());
    
    // pretty print the AST
    String ppResult = new OD4ReportFullPrettyPrinter(new IndentPrinter()).prettyprint(odDef.get());
    
    // parse the printers content
    Optional<ASTODArtifact> ppOd = parser.parse_StringODArtifact(ppResult);
    
    assertFalse(parser.hasErrors());
    assertTrue(ppOd.isPresent());
    assertEquals(0, Log.getFindingsCount());
    
    // must be equal to original parsed AST.
    assertTrue(odDef.get().deepEquals(ppOd.get()), "pretty printed OD: " + ppResult);
  }
  
  private void negativTest(String modelName) throws RecognitionException, IOException {
    Path model = Paths.get(modelName);
    OD4ReportParser parser = new OD4ReportParser();
    parser.parseODArtifact(model.toString());
    assertTrue(parser.hasErrors());
    assertEquals(0, Log.getFindingsCount());
  }
  
}
