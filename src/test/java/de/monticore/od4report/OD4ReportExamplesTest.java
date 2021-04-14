// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report;

import de.monticore.od4data.OD4DataMill;
import de.monticore.od4report._parser.OD4ReportParser;
import de.monticore.od4report._symboltable.IOD4ReportGlobalScope;
import de.monticore.od4report.prettyprinter.OD4ReportFullPrettyPrinter;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.symbols.basicsymbols._symboltable.TypeSymbol;
import de.se_rwth.commons.logging.Log;
import org.antlr.v4.runtime.RecognitionException;
import org.junit.Before;
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
    Log.init();
    Log.enableFailQuick(false);
  }

  @Before
  public void setUp() {
    OD4ReportMill.reset();
    OD4ReportMill.init();
    OD4ReportMill.globalScope().clear();
    IOD4ReportGlobalScope gs = OD4ReportMill.globalScope();

    TypeSymbol rule = OD4ReportMill.typeSymbolBuilder().setName("Rule").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol actionA = OD4ReportMill.typeSymbolBuilder().setName("ActionA").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol actionB = OD4ReportMill.typeSymbolBuilder().setName("ActionB").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol objectType = OD4ReportMill.typeSymbolBuilder().setName("ObjectType").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol objectType2 = OD4ReportMill.typeSymbolBuilder().setName("ObjectType2").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol gitLab = OD4ReportMill.typeSymbolBuilder().setName("GitLab").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol test = OD4ReportMill.typeSymbolBuilder().setName("Test").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol person = OD4ReportMill.typeSymbolBuilder().setName("Person").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol bmw = OD4ReportMill.typeSymbolBuilder().setName("BMW").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol jaguar = OD4ReportMill.typeSymbolBuilder().setName("Jaguar").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    gs.add(rule);
    gs.add(actionA);
    gs.add(actionB);
    gs.add(objectType);
    gs.add(objectType2);
    gs.add(gitLab);
    gs.add(test);
    gs.add(person);
    gs.add(bmw);
    gs.add(jaguar);
  }

  @Test
  public void testExamples() throws RecognitionException, IOException {
    test("src/test/resources/examples/od/Examples.od");
  }

  @Test
  public void testSimpleOD() throws RecognitionException, IOException {
    test("src/test/resources/examples/od/SimpleOD.od");
  }

  @Test
  public void testProjectOD() throws RecognitionException, IOException {
    test("src/test/resources/examples/od/ProjectListOD.od");
  }

  @Test
  public void testTeaser() throws RecognitionException, IOException {
    test("src/test/resources/examples/od/MyFamily.od");
  }

  private void test(String modelName) throws RecognitionException, IOException {
    Path model = Paths.get(modelName);
    OD4ReportParser parser = new OD4ReportParser();
    Optional<ASTODArtifact> odDef = parser.parse(model.toString());
    assertFalse(parser.hasErrors());
    assertTrue(odDef.isPresent());

    // pretty print the AST
    String ppResult = new OD4ReportFullPrettyPrinter(new IndentPrinter()).prettyprint(
        odDef.get());

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
