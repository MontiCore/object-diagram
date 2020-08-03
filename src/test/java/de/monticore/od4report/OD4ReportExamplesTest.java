// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore
package de.monticore.od4report;

import de.monticore.od4report._parser.OD4ReportParser;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.Slf4jLog;
import org.antlr.v4.runtime.RecognitionException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This test compares the ASTs of the files in the examples folder with the pretty-printed versions
 * of these files.
 */
public class OD4ReportExamplesTest {

  @BeforeClass
  public static void disableFailQuick() {
    Slf4jLog.init();
    Log.enableFailQuick(false);
  }

  @Test
  public void testExamples() throws RecognitionException, IOException {
    test("src/test/resources/examples/od/Examples.od");
  }

  @Test
  public void testSimpleOD() throws RecognitionException, IOException {
    test("src/test/resources/examples/od/SimpleOD.od");
  }

  private void test(String modelName) throws RecognitionException, IOException {
    Path model = Paths.get(modelName);
    OD4ReportParser parser = new OD4ReportParser();
    Optional<ASTODArtifact> odDef = parser.parse(model.toString());
    assertFalse(parser.hasErrors());
    assertTrue(odDef.isPresent());

    // pretty print the AST
    String ppResult = OD4ReportTool.prettyPrintODNode(odDef.get());

    // parse the printers content
    Optional<ASTODArtifact> ppOd = parser.parse_StringODArtifact(ppResult);

    assertFalse(parser.hasErrors());
    assertTrue(ppOd.isPresent());

    // must be equal to original parsed AST.
    assertTrue("pretty printed OD: " + ppResult, odDef.get().deepEquals(ppOd.get()));
  }

  private void negativTest(String modelName) throws RecognitionException, IOException {
    Path model = Paths.get(modelName);
    OD4ReportParser parser = new OD4ReportParser();
    parser.parseODArtifact(model.toString());
    assertTrue(parser.hasErrors());
  }

}
