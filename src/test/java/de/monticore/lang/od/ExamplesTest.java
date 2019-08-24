/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.od;

import de.monticore.lang.od._ast.ASTODArtifact;
import de.monticore.lang.od._parser.ODParser;
import de.monticore.lang.od.prettyprint.ODPrettyPrinterConcreteVisitor;
import de.monticore.prettyprint.IndentPrinter;
import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.Slf4jLog;
import org.antlr.v4.runtime.RecognitionException;
import org.junit.BeforeClass;
import org.junit.Ignore;
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
 *
 * @author Robert Heim, Timo Greifenberg
 */
public class ExamplesTest {

  @BeforeClass
  public static void disableFailQuick() {
    Slf4jLog.init();
    Log.enableFailQuick(false);
  }

  @Test
  public void testSpecialValues() throws RecognitionException, IOException {
    Log.enableFailQuick(false);
    test("src/test/resources/examples/od/SpecialValues.od");
  }

  @Test
  public void testQualifiedLinks() throws RecognitionException, IOException {
    Log.enableFailQuick(false);
    test("src/test/resources/examples/od/QualifiedLinks.od");
  }

  @Test
  public void testQualifiedInnerLinks() throws RecognitionException, IOException {
    Log.enableFailQuick(false);
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
  public void testAstOd() throws RecognitionException, IOException {
    test("src/test/resources/examples/od/AstOd.od");
  }

  @Test
  public void testSymTabOd() throws RecognitionException, IOException {
    test("src/test/resources/examples/od/SymTabOd.od");
  }

  @Test
  public void testExamples() throws RecognitionException, IOException {
    test("src/test/resources/examples/od/Examples.od");
  }

  @Test
  public void testInnerObjectWithoutLink() throws RecognitionException, IOException {
    negativTest("src/test/resources/examples/od/InnerObjectWithoutLink.od");
  }

  @Test
  public void testSimpleOD() throws RecognitionException, IOException {
    test("src/test/resources/examples/od/SimpleOD.od");
  }
  
  private void test(String modelName)
      throws RecognitionException, IOException {
    Path model = Paths.get(modelName);
    ODParser parser = new ODParser();
    Optional<ASTODArtifact> odDef = parser.parseODArtifact(model.toString());
    assertFalse(parser.hasErrors());
    assertTrue(odDef.isPresent());

    // pretty print the AST
    ODPrettyPrinterConcreteVisitor pp;
    pp = new ODPrettyPrinterConcreteVisitor(new IndentPrinter());

    pp.handle(odDef.get());
    String ppResult = pp.getPrinter().getContent();
    // System.out.println(ppResult);

    // parse the printers content
    Optional<ASTODArtifact> ppOd = parser.parse_StringODArtifact(ppResult);

    assertFalse(parser.hasErrors());
    assertTrue(ppOd.isPresent());

    // must be equal to original parsed AST.
    assertTrue("pretty printed OD: " + ppResult, odDef.get().deepEquals(ppOd.get()));

  }

  private void negativTest(String modelName) throws RecognitionException, IOException {
    Path model = Paths.get(modelName);
    ODParser parser = new ODParser();
    parser.parseODArtifact(model.toString());
    assertTrue(parser.hasErrors());
  }
}
