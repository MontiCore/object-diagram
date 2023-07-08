package de.monticore;

import de.monticore.prettyprint.IndentPrinter;
import de.se_rwth.commons.logging.Log;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import de.monticore.od4development._parser.*;
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
        check(basedir + "Example1.od", basedir + "ExpectedOutputExample1.od");
    }

    @Ignore
    @Test
    public void test3() throws IOException {
        check(basedir + "Variants.od", basedir + "ExpectedOutputVariants.od");
    }

    @Test
    public void test4() throws IOException {
        check(basedir + "StereoWithKeyword.od", basedir + "ExpectedOutputStereoWithKeyword.od");
    }

    @Test
    public void test5() throws IOException {
        check(basedir + "SpecialValues.od", basedir + "ExpectedOutputSpecialValues.od");
    }

    @Test
    public void test6() throws IOException {
        check(basedir + "SimpleOD2.od", basedir + "ExpectedOutputSimpleOD2.od");
    }
    @Test
    public void test7() throws IOException {
        check(basedir + "QualifiedLinks.od", basedir + "ExpectedOutputQualifiedLinks.od");
    }
    @Test
    public void test8() throws IOException {
        check(basedir + "QualifiedInnerLinks.od", basedir + "ExpectedOutputQualifiedInnerLinks.od");
    }
    @Test
    public void test9() throws IOException {
        check("gentest/src/main/resources/AuctionParticipants.od", basedir + "ExpectedOutputAuctionParticipants.od");
    }
    @Test
    public void test10() throws IOException {
        check(basedir + "ProjectListOD.od", basedir + "ExpectedOutputProjectListOD.od");
    }

    public String removeSpace(String str)
    {
        str = str.replaceAll("\\s","");
        return str;
    }

    private void check(String odModel, String plantUMLModel) throws IOException {

        OD4DevelopmentParser odParser = new OD4DevelopmentParser();
        Optional<ASTODArtifact> parsedOD = odParser.parse(odModel);
        assertTrue(parsedOD.isPresent());

        PlantUMLODFullPrettyPrinter odPrettyPrinter = new PlantUMLODFullPrettyPrinter();

        String printedPlantUMLSyntax = odPrettyPrinter.prettyprint(parsedOD.get());

        byte[] bytes = Files.readAllBytes(Path.of(plantUMLModel));
        String expectedPlantUMLSyntax = new String(bytes,StandardCharsets.UTF_8);

        List<String> printedLines = Arrays.asList(printedPlantUMLSyntax.split("\\r?\\n"));
        List<String> expectedLines = Arrays.asList(expectedPlantUMLSyntax.split("\\r?\\n"));
        int numLines = Math.min(printedLines.size(), expectedLines.size());
        boolean areEqual = printedLines.size() == expectedLines.size();

        StringBuilder mismatchBuilder = new StringBuilder();
        for (int i = 0; i < numLines; i++) {
            if (!printedLines.get(i).equals(expectedLines.get(i))) {
                mismatchBuilder.append("Mismatch at line ").append(i + 1).append(":\n");
                mismatchBuilder.append("Actual:   ").append(printedLines.get(i)).append("\n");
                mismatchBuilder.append("Expected: ").append(expectedLines.get(i)).append("\n");
                areEqual = false;
            }
        }

        if (!areEqual) {
            String diffMessage = mismatchBuilder.toString();
            fail("The Pretty Printed Output of the Object Diagram does not match with the expected PlantUML syntax.\n"
                    + "Differences:\n" + diffMessage);
        }
    }
}
