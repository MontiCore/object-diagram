// (c) https://github.com/MontiCore/monticore

package de.monticore.od4data;

import de.monticore.od4data._parser.OD4DataParser;
import de.monticore.od4data._symboltable.IOD4DataArtifactScope;
import de.monticore.od4data._symboltable.IOD4DataGlobalScope;
import de.monticore.od4data.prettyprinter.OD4DataFullPrettyPrinter;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.symbols.basicsymbols._symboltable.TypeSymbol;
import de.se_rwth.commons.logging.Log;
import org.antlr.v4.runtime.RecognitionException;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * This test compares the ASTs of the files in the examples folder with the pretty-printed versions
 * of these files.
 */
public class OD4DataExamplesTest {

  @Before
  public void disableFailQuick() {
    Log.init();
    Log.enableFailQuick(false);

    OD4DataMill.reset();
    OD4DataMill.init();
    OD4DataMill.globalScope().clear();
    IOD4DataGlobalScope gs = OD4DataMill.globalScope();

    TypeSymbol auction = OD4DataMill.typeSymbolBuilder().setName("Auction").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol person = OD4DataMill.typeSymbolBuilder().setName("Person").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol string = OD4DataMill.typeSymbolBuilder().setName("String").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol biddingPolicy = OD4DataMill.typeSymbolBuilder().setName("BiddingPolicy").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol timingPolicy = OD4DataMill.typeSymbolBuilder().setName("TimingPolicy").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol objectType = OD4DataMill.typeSymbolBuilder().setName("ObjectType").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol t1 = OD4DataMill.typeSymbolBuilder().setName("T1").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol t2 = OD4DataMill.typeSymbolBuilder().setName("T2").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol t3 = OD4DataMill.typeSymbolBuilder().setName("T3").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol typeOfObject = OD4DataMill.typeSymbolBuilder().setName("TypeOfObject").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol type1 = OD4DataMill.typeSymbolBuilder().setName("type1").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol allData = OD4DataMill.typeSymbolBuilder().setName("AllData").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol innerObjectType = OD4DataMill.typeSymbolBuilder().setName("InnerObjectType").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol javaSourceFile = OD4DataMill.typeSymbolBuilder().setName("JavaSourceFile").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol type3 = OD4DataMill.typeSymbolBuilder().setName("type3").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol objectType2 = OD4DataMill.typeSymbolBuilder().setName("ObjectType2").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol directory = OD4DataMill.typeSymbolBuilder().setName("Directory").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol myObject = OD4DataMill.typeSymbolBuilder().setName("MyObject").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol javaClassType = OD4DataMill.typeSymbolBuilder().setName("JavaClassType").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    gs.add(auction);
    gs.add(person);
    gs.add(string);
    gs.add(biddingPolicy);
    gs.add(timingPolicy);
    gs.add(objectType);
    gs.add(t1);
    gs.add(t2);
    gs.add(t3);
    gs.add(typeOfObject);
    gs.add(type1);
    gs.add(allData);
    gs.add(innerObjectType);
    gs.add(javaSourceFile);
    gs.add(type3);
    gs.add(objectType2);
    gs.add(directory);
    gs.add(myObject);
    gs.add(javaClassType);
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

    OD4DataParser parser = new OD4DataParser();
    Optional<ASTODArtifact> astodArtifact = parser.parseODArtifact(model.toString());
    assertFalse(parser.hasErrors());
    assertTrue(astodArtifact.isPresent());

    IOD4DataArtifactScope odBasicsArtifactScope = OD4DataTool.createSymbolTable(astodArtifact.get());
    assertNotNull(odBasicsArtifactScope);

    // pretty print the AST
    String ppResult = new OD4DataFullPrettyPrinter(new IndentPrinter()).prettyprint(
        astodArtifact.get());

    // parse the printers content
    Optional<ASTODArtifact> ppOd = parser.parse_StringODArtifact(ppResult);

    assertFalse(parser.hasErrors());
    assertTrue(ppOd.isPresent());

    // must be equal to original parsed AST.
    assertTrue("pretty printed OD: " + ppResult, astodArtifact.get().deepEquals(ppOd.get()));
  }

  private void negativTest(String modelName) throws RecognitionException, IOException {
    // redirect System.out
    PrintStream originalOut = System.out;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out));

    //redirect System.err
    PrintStream originalErr = System.err;
    ByteArrayOutputStream err = new ByteArrayOutputStream();
    System.setErr(new PrintStream(err));

    Path model = Paths.get(modelName);
    OD4DataParser parser = new OD4DataParser();
    parser.parseODArtifact(model.toString());
    assertTrue(parser.hasErrors());

    System.setOut(originalOut);
    System.setErr(originalErr);
  }

}
