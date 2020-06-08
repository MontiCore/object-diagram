// (c) https://github.com/MontiCore/monticore
package de.monticore.lang.odbasics;

import de.monticore.lang.odbasics._ast.ASTODArtifact;
import de.monticore.lang.odbasics._parser.ODBasicsParser;
import de.monticore.lang.odbasics._symboltable.ODBasicsArtifactScope;
import de.monticore.lang.odbasics._symboltable.ODBasicsLanguage;
import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.Slf4jLog;
import org.antlr.v4.runtime.RecognitionException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * This test compares the ASTs of the files in the examples folder with the pretty-printed versions
 * of these files.
 *
 */
public class ExamplesTest {

  @BeforeClass
  public static void disableFailQuick() {
    Slf4jLog.init();
    Log.enableFailQuick(false);
  }

  @Test
  public void testSpecialValues() throws RecognitionException, IOException {
    test("src/test/resources/examples/od/SpecialValues.od");
  }

  @Test
  public void testQualifiedLinks() throws RecognitionException, IOException {
    test("src/test/resources/examples/od/QualifiedLinks.od");
  }

  @Test
  public void testQualifiedInnerLinks() throws RecognitionException, IOException {
    test("src/test/resources/examples/od/QualifiedInnerLinks.od");
  }

  @Test
  public void testAuctionParticipats() throws RecognitionException, IOException {
    test("src/test/resources/examples/od/AuctionParticipants.od");
  }

  @Test
  public void testVariants() throws RecognitionException, IOException {
    test("src/test/resources/examples/od/Variants.od");
  }

  @Test
  public void testStereoWithKeyword() throws RecognitionException, IOException {
    test("src/test/resources/examples/od/StereoWithKeyword.od");
  }

  @Test
  public void testHierarchicalOd() throws RecognitionException, IOException {
    test("src/test/resources/examples/hierarchical/StandardInnerLink.od");
  }

  @Test
  public void testHierarchicalOdVariants() throws RecognitionException, IOException {
    test("src/test/resources/examples/hierarchical/InnerLinkVariants.od");
  }

  @Test
  public void testValueCollection() throws RecognitionException, IOException {
    test("src/test/resources/examples/valuecollections/ValueCollection.od");
  }

  @Test
  public void testSimpleOD() throws RecognitionException, IOException {
    test("src/test/resources/examples/od/SimpleOD2.od");
  }

  @Test
  public void testInnerObjectWithoutLink() throws RecognitionException, IOException {
    negativTest("src/test/resources/examples/od/InnerObjectWithoutLink.od");
  }

  private void test(String modelName) throws RecognitionException, IOException {
    Path model = Paths.get(modelName);
    ODBasicsParser parser = new ODBasicsParser();
    Optional<ASTODArtifact> odDef = parser.parseODArtifact(model.toString());
    ASTODArtifact astodArtifact = ODBasicsTool.parse(model.toString());
    ODBasicsArtifactScope odBasicsArtifactScope =
        ODBasicsTool.createSymbolTable(new ODBasicsLanguage(), astodArtifact);
    assertFalse(parser.hasErrors());
    assertTrue(odDef.isPresent());
    assertNotNull(odBasicsArtifactScope);

    // pretty print the AST
    String ppResult = ODBasicsTool.prettyPrintODNode(odDef.get());

    // parse the printers content
    Optional<ASTODArtifact> ppOd = parser.parse_StringODArtifact(ppResult);

    assertFalse(parser.hasErrors());
    assertTrue(ppOd.isPresent());

    // must be equal to original parsed AST.
    assertTrue("pretty printed OD: " + ppResult, odDef.get().deepEquals(ppOd.get()));
  }

  private void negativTest(String modelName) throws RecognitionException, IOException {
    Path model = Paths.get(modelName);
    ODBasicsParser parser = new ODBasicsParser();
    parser.parseODArtifact(model.toString());
    assertTrue(parser.hasErrors());
  }

}
