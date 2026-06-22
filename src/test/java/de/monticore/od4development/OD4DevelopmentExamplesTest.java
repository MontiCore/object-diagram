// (c) https://github.com/MontiCore/monticore

package de.monticore.od4development;

import de.monticore.ODTestBasis;
import de.monticore.io.paths.MCPath;
import de.monticore.od4development._parser.OD4DevelopmentParser;
import de.monticore.od4development._symboltable.IOD4DevelopmentArtifactScope;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.runtime.junit.MCAssertions;
import de.monticore.runtime.junit.TestWithMCLanguage;
import org.antlr.v4.runtime.RecognitionException;

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
@TestWithMCLanguage(OD4DevelopmentMill.class)
public class OD4DevelopmentExamplesTest extends ODTestBasis {
  
  @ParameterizedTest
  @ValueSource(strings = { "examples/od/SpecialValues.od", "examples/od/QualifiedLinks.od",
      "examples/od/QualifiedInnerLinks.od", "examples/od/AuctionParticipants.od",
      "examples/od/Variants.od", "examples/od/StereoWithKeyword.od",
      "examples/hierarchical/StandardInnerLink.od", "examples/hierarchical/InnerLinkVariants.od",
      "examples/valuecollections/ValueCollection.od", "examples/od/SimpleOD2.od" })
  public void test(String modelName) throws RecognitionException, IOException {
    Path model = PATH.resolve(Paths.get(modelName));
    
    ASTODArtifact artifact = OD4DevelopmentTestUtil.loadModel(model, new MCPath(PATH));
    
    IOD4DevelopmentArtifactScope odBasicsArtifactScope =
        OD4DevelopmentTestUtil.createSymbolTableFromAST(artifact);
    assertNotNull(odBasicsArtifactScope);
    
    // pretty print the AST
    String ppResult = OD4DevelopmentMill.prettyPrint(artifact, false);
    
    // parse the printers content
    OD4DevelopmentParser parser = OD4DevelopmentMill.parser();
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
    OD4DevelopmentParser parser = OD4DevelopmentMill.parser();
    parser.parseODArtifact(model.toString());
    assertTrue(parser.hasErrors());
    MCAssertions.assertHasFindingStartingWith("missing ';' at '}' in rule stack");
  }
  
}
