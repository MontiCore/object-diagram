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


    @Test
    public void test1() throws IOException {
        check(basedir + "Example.od", basedir + "ExpectedOutputExample.od");
    }

    @Test
    public void test2() throws IOException {
        check(basedir + "SimpleOD.od", basedir + "ExpectedOutputSimpleOD.od");
    }

    @Test
    public void test3() throws IOException {
        check(basedir + "StereoWithKeyword.od", basedir + "ExpectedOutputStereoWithKeyword.od");
    }

    @Test
    public void test4() throws IOException {
        check(basedir + "SpecialValues.od", basedir + "ExpectedOutputSpecialValues.od");
    }

    @Test
    public void test5() throws IOException {
        check(basedir + "SimpleOD2.od", basedir + "ExpectedOutputSimpleOD2.od");
    }
    @Test
    public void test6() throws IOException {
        check(basedir + "QualifiedLinks.od", basedir + "ExpectedOutputQualifiedLinks.od");
    }
    @Test
    public void test7() throws IOException {
        check(basedir + "QualifiedInnerLinks.od", basedir + "ExpectedOutputQualifiedInnerLinks.od");
    }
    @Test
    public void test8() throws IOException {
        check("gentest/src/main/resources/AuctionParticipants.od", basedir + "ExpectedOutputAuctionParticipants.od");
    }
    @Test
    public void test9() throws IOException {
        check(basedir + "ProjectListOD.od", basedir + "ExpectedOutputProjectListOD.od");
    }
    @Ignore
    @Test
    public void test10() throws IOException {
        // OD Model is Incorrect and cannot be parsed by OD4Report grammar
        check(basedir + "InnerObjectWithoutLink.od", basedir + "ExpectedOutputInnerObjectWithoutLink.od");
    }
    @Ignore
    @Test
    public void test11() throws IOException {
        // OD Model doesn't have values for attributes and exception is thrown by OD4Report grammar
        // Test works with OD4 Development grammar
        check(basedir + "Variants.od", basedir + "ExpectedOutputVariants.od");
    }
    @Ignore
    @Test
    public void test12() throws IOException {
        check(basedir + "MyFamily.od", basedir + "ExpectedOutputVariants.od");
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

        assertEquals("The Pretty Printed Output of the Object Diagram does not match with the expected PlantUML syntax."
                + "\n" + "Actual Pretty Printed OD : " + "\n" + printedPlantUMLSyntax + "\n" + "Expected Pretty Printed OD" + "\n"
                + expectedPlantUMLSyntax,removeSpace(expectedPlantUMLSyntax),removeSpace(printedPlantUMLSyntax));

    }
}
