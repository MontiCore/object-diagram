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

    public String removeSpace(String str)
    {
        str = str.replaceAll("\\s","");
        return str;
    }

    private void check(String s, String x) throws IOException {

        OD4DevelopmentParser p = new OD4DevelopmentParser();
        Optional<ASTODArtifact> pp = p.parse(s);
        assertTrue(pp.isPresent());

        PlantUMLODFullPrettyPrinter q = new PlantUMLODFullPrettyPrinter();

        String printed = q.prettyprint(pp.get());
        Optional<ASTODArtifact> parsed = p.parse_String(printed);

        byte[] bytes = Files.readAllBytes(Path.of(x));
        String expected = new String(bytes,StandardCharsets.UTF_8);

        System.out.println(printed);
        System.out.println(expected);

        assertEquals("The Pretty Printed Output of the Object Diagram does not match with the expected PlantUML syntax."
                + "\n" + "Actual Pretty Printed OD : " + "\n" + printed + "\n" + "Expected Pretty Printed OD" + "\n"
                + expected,removeSpace(expected),removeSpace(printed));
    }
}
