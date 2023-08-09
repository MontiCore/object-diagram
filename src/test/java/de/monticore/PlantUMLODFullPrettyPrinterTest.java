package de.monticore;

import de.monticore.od4report._parser.OD4ReportParser;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.se_rwth.commons.logging.Log;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This class contains test cases for the PlantUMLODFullPrettyPrinter, which is responsible for generating
 * PlantUML syntax from parsed Object Diagram artifacts. The tests ensure the correctness of the generated
 * PlantUML syntax by comparing it with the expected results stored in test resource files.
 */
public class PlantUMLODFullPrettyPrinterTest {

    /**
     * The base directory where test resources are located.
     */
    protected static String basedir = "src/test/resources/examples/od2cd/";

    /**
     * Disables the fail-quick behavior of the logging system before running the test cases.
     */
    @BeforeClass
    public static void disableFailQuick() {
        Log.enableFailQuick(false);
    }

    /**
     * Parameterized test method that reads test data from CSV files and compares the generated PlantUML syntax
     * with expected results.
     *
     * @param input1 The name of the input file containing Object Diagram artifact.
     * @param input2 The name of the input file containing expected PlantUML syntax.
     * @throws IOException If an I/O error occurs while reading the files.
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/examples/od2cd/Tests")
    public void test(String input1, String input2) throws IOException{
        check(basedir + input1, basedir + input2);
    }

    /**
     * Ignores a specific test case where the OD model cannot be parsed by the OD4Report grammar.
     *
     * @throws IOException If an I/O error occurs while reading the files.
     */
    @Ignore
    @Test
    public void test10() throws IOException {
        // OD Model is Incorrect and cannot be parsed by OD4Report grammar
        check(basedir + "InnerObjectWithoutLink.od",
                basedir + "ExpectedOutputInnerObjectWithoutLink.puml");
    }

    @Ignore
    @Test
    public void test11() throws IOException {
        // OD Model doesn't have values for attributes and exception is thrown by OD4Report grammar
        // Test works with OD4 Development grammar
        check(basedir + "Variants.od", basedir + "ExpectedOutputVariants.puml");
    }

    @Ignore
    @Test
    public void test12() throws IOException {
        check(basedir + "MyFamily.od", basedir + "ExpectedOutputVariants.puml");
    }

    /**
     * Removes spaces from the given string.
     *
     * @param str The input string.
     * @return The input string with spaces removed.
     */
    public String removeSpace(String str) {
        str = str.replaceAll("\\s", "");
        return str;
    }

    /**
     * Parses the input Object Diagram artifact, generates PlantUML syntax, and compares it with the expected syntax.
     *
     * @param odModel       The path to the input Object Diagram artifact.
     * @param plantUMLModel The path to the file containing expected PlantUML syntax.
     * @throws IOException If an I/O error occurs while reading the files.
     */
    private void check(String odModel, String plantUMLModel) throws IOException {

        OD4ReportParser odParser = new OD4ReportParser();
        Optional<ASTODArtifact> parsedOD = odParser.parse(odModel);
        assertTrue(parsedOD.isPresent());

        PlantUMLODFullPrettyPrinter odPrettyPrinter = new PlantUMLODFullPrettyPrinter();

        String printedPlantUMLSyntax = odPrettyPrinter.prettyprint(parsedOD.get());

        byte[] bytes = Files.readAllBytes(Path.of(plantUMLModel));
        String expectedPlantUMLSyntax = new String(bytes, StandardCharsets.UTF_8);

        assertEquals(String.format(
                        "The Pretty Printed Output %s does not match with the expected PlantUML Output %s",
                        printedPlantUMLSyntax, expectedPlantUMLSyntax), removeSpace(expectedPlantUMLSyntax),
                removeSpace(printedPlantUMLSyntax));

    }
}
