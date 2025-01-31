// (c) https://github.com/MontiCore/monticore

package de.monticore.od4data;

import de.monticore.ODTestBasis;
import de.monticore.io.paths.MCPath;
import de.monticore.od4data._parser.OD4DataParser;
import de.monticore.od4data._prettyprint.OD4DataFullPrettyPrinter;
import de.monticore.od4data._symboltable.IOD4DataArtifactScope;
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
public class OD4DataExamplesTest extends ODTestBasis {
  
  @BeforeEach
  public void setUp() {
    OD4DataMill.reset();
    OD4DataMill.init();
  }
  
  @ParameterizedTest
  @ValueSource(strings = { "examples/od/SpecialValues.od", "examples/od/QualifiedLinks.od",
      "examples/od/QualifiedInnerLinks.od", "examples/od/AuctionParticipants.od",
      "examples/od/Variants.od", "examples/od/StereoWithKeyword.od",
      "examples/hierarchical/StandardInnerLink.od", "examples/hierarchical/InnerLinkVariants.od",
      "examples/valuecollections/ValueCollection.od", "examples/od/SimpleOD2.od" })
  public void test(String modelName) throws RecognitionException, IOException {
    Path model = PATH.resolve(Paths.get(modelName));
    
    ASTODArtifact artifact = OD4DataTestUtil.loadModel(model, new MCPath(PATH));
    
    IOD4DataArtifactScope odBasicsArtifactScope =
        OD4DataTestUtil.createSymbolTableFromAST(artifact);
    assertNotNull(odBasicsArtifactScope);
    
    // pretty print the AST
    String ppResult = new OD4DataFullPrettyPrinter(new IndentPrinter()).prettyprint(artifact);
    
    // parse the printers content
    OD4DataParser parser = new OD4DataParser();
    Optional<ASTODArtifact> ppOd = parser.parse_StringODArtifact(ppResult);
    
    assertFalse(parser.hasErrors());
    assertTrue(ppOd.isPresent());
    
    // must be equal to original parsed AST.
    assertTrue(artifact.deepEquals(ppOd.get()), "pretty printed OD: " + ppResult);
  }
  
  @ParameterizedTest
  @ValueSource(strings = { "src/test/resources/examples/od/InnerObjectWithoutLink.od" })
  public void negativTest(String modelName) throws RecognitionException, IOException {
    Path model = Paths.get(modelName);
    OD4DataParser parser = new OD4DataParser();
    parser.parseODArtifact(model.toString());
    assertTrue(parser.hasErrors());
    assertEquals(1, Log.getErrorCount());
    Log.clearFindings();
  }
  
}
