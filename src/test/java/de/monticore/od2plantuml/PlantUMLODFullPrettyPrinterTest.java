package de.monticore.od2plantuml;

import de.monticore.od2plantuml.prettyprinter.PlantUMLODFullPrettyPrinter;
import de.monticore.od4data.trafo.OD4DataAttributeValueCompositionTrafo;
import de.monticore.od4data.trafo.OD4DataDeAnonymizeObjectsTrafo;
import de.monticore.od4report.OD4ReportMill;
import de.monticore.od4report._parser.OD4ReportParser;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.se_rwth.commons.logging.Log;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * This class contains test cases for the PlantUMLODFullPrettyPrinter, which is responsible for
 * generating PlantUML syntax from parsed Object Diagram artifacts. The tests ensure the correctness
 * of the generated PlantUML syntax by comparing it with the expected results stored in test
 * resource files.
 */
public class PlantUMLODFullPrettyPrinterTest {
  
  /**
   * The base directory where test resources are located.
   */
  protected static String basedir = "src/test/resources/examples/";
  
  /**
   * Disables the fail-quick behavior of the logging system before running the test cases.
   */
  @BeforeAll
  public static void disableFailQuick() {
    Log.initDEBUG();
    Log.enableFailQuick(false);
  }
  
  @BeforeEach
  public void setupMills(){
    OD4ReportMill.reset();
    OD4ReportMill.init();
  }
  
  /**
   * Parameterized test method that reads test data from CSV files and compares the generated
   * PlantUML syntax with expected results.
   *
   * @param input The relative path to the input file containing Object Diagram artifact and PlantUML.
   * @throws IOException If an I/O error occurs while reading the files.
   */
  @ParameterizedTest
  @ValueSource(strings = {
      "od2cd/Example",
      "od2cd/SimpleOD",
      "od2cd/StereoWithKeyword",
      "od2cd/SpecialValues",
      "od2cd/SimpleOD2",
      "od2cd/QualifiedLinks",
      "od2cd/QualifiedInnerLinks",
      "od/AuctionParticipants",
      "od2cd/ProjectListOD",
      "od2cd/Variants",
      "od2cd/InnerObject",
      "od2cd/MyFamily"
  })
  public void test(String input) throws IOException {
    OD4ReportParser parser = OD4ReportMill.parser();
    Optional<ASTODArtifact> optOD = parser.parse(basedir + input + ".od");
    Assertions.assertTrue(optOD.isPresent());
    
    ASTODArtifact transformableArtifact = optOD.get();
    new OD4DataDeAnonymizeObjectsTrafo().transform(transformableArtifact);
    new OD4DataAttributeValueCompositionTrafo().transform(transformableArtifact);
    
    PlantUMLODFullPrettyPrinter prettyPrinter = new PlantUMLODFullPrettyPrinter();
    String plantUML = prettyPrinter.prettyprint(transformableArtifact);
    
    byte[] bytes = Files.readAllBytes(Path.of(basedir + input + ".puml"));
    String expectedPlantUML = new String(bytes, StandardCharsets.UTF_8);
    
    var expected = removeSpace(expectedPlantUML);
    var actual = removeSpace(plantUML);
    Assertions.assertEquals(expected, actual, () -> String.format(
        "The printed output\n\n\"\"\"\n%s\n\"\"\"\n\ndoes not match with the expected PlantUML output\n\n\"\"\"\n%s\n\"\"\"\n.",
        plantUML, expectedPlantUML)
    );
    
    // TODO MSm parse PlantUML
  }
  
  /**
   * Removes spaces from the given string.
   *
   * @param str The input string.
   * @return The input string with spaces removed.
   */
  public String removeSpace(String str) {
    return str.replaceAll("\\s", "");
  }
}
