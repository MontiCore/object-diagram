package de.monticore;

import de.monticore.prettyprint.IndentPrinter;
import de.se_rwth.commons.logging.Log;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import de.monticore.od4development._parser.*;
import de.monticore.od4report._parser.*;
import de.monticore.PlantUMLODBasisPrettyPrinter;
import de.monticore.odbasis._ast.ASTODArtifact;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class PlantUMLODFullPrettyPrinterTest {

    protected static String basedir ="src/test/resources/examples/od2cd/";

    @BeforeClass
    public static void disableFailQuick(){
        Log.enableFailQuick(false);
    }

    @ParameterizedTest
    @CsvSource({"Example.od,ExpectedOutputExample.puml"})
    public void test1(String input1, String input2) throws IOException {
        check(basedir + input1, basedir + input2);
    }

    @ParameterizedTest
    @CsvSource({"SimpleOD.od,ExpectedOutputSimpleOD.puml"})
    public void test2(String input1, String input2) throws IOException {
        check(basedir + input1, basedir + input2);
    }

    @ParameterizedTest
    @CsvSource({"StereoWithKeyword.od,ExpectedOutputStereoWithKeyword.puml"})
    public void test3(String input1, String input2) throws IOException {
        check(basedir + input1, basedir + input2);
    }

    @ParameterizedTest
    @CsvSource({"SpecialValues.od,ExpectedOutputSpecialValues.puml"})
    public void test4(String input1, String input2) throws IOException {
        check(basedir + input1, basedir + input2);
    }

    @ParameterizedTest
    @CsvSource({"SimpleOD2.od,ExpectedOutputSimpleOD2.puml"})
    public void test5(String input1, String input2) throws IOException {
        check(basedir + input1, basedir + input2);
    }

    @ParameterizedTest
    @CsvSource({"QualifiedLinks.od,ExpectedOutputQualifiedLinks.puml"})
    public void test6(String input1, String input2) throws IOException {
        check(basedir + input1, basedir + input2);
    }
    @ParameterizedTest
    @CsvSource({"QualifiedInnerLinks.od,ExpectedOutputQualifiedInnerLinks.puml"})
    public void test7(String input1, String input2) throws IOException {
        check(basedir + input1, basedir + input2);
    }
    @ParameterizedTest
    @CsvSource({"AuctionParticipants.od,ExpectedOutputAuctionParticipants.puml"})
    public void test8(String input1, String input2) throws IOException {
        check(basedir + input1, basedir + input2);
    }
    @ParameterizedTest
    @CsvSource({"ProjectListOD.od,ExpectedOutputProjectListOD.puml"})
    public void test9(String input1, String input2) throws IOException {
        check(basedir + input1, basedir + input2);
    }
    @Ignore
    @Test
    public void test10() throws IOException {
        // OD Model is Incorrect and cannot be parsed by OD4Report grammar
        check(basedir + "InnerObjectWithoutLink.od", basedir + "ExpectedOutputInnerObjectWithoutLink.puml");
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

    public String removeSpace(String str)
    {
        str = str.replaceAll("\\s","");
        return str;
    }

    private void check(String odModel, String plantUMLModel) throws IOException {

        OD4ReportParser odParser = new OD4ReportParser();
        Optional<ASTODArtifact> parsedOD = odParser.parse(odModel);
        assertTrue(parsedOD.isPresent());

        PlantUMLODFullPrettyPrinter odPrettyPrinter = new PlantUMLODFullPrettyPrinter();

        String printedPlantUMLSyntax = odPrettyPrinter.prettyprint(parsedOD.get());

        byte[] bytes = Files.readAllBytes(Path.of(plantUMLModel));
        String expectedPlantUMLSyntax = new String(bytes,StandardCharsets.UTF_8);

        assertEquals(String.format("The Pretty Printed Output %s does not match with the expected PlantUML Output %s",printedPlantUMLSyntax,expectedPlantUMLSyntax),removeSpace(expectedPlantUMLSyntax),removeSpace(printedPlantUMLSyntax));

    }
}
