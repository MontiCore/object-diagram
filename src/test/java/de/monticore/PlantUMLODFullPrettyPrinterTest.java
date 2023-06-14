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

public class PlantUMLODFullPrettyPrinterTest {

    protected static String basedir ="src/test/resources/examples/od2cd/";

    @BeforeClass
    public static void disableFailQuick(){
        Log.enableFailQuick(false);
    }

    @Ignore
    @Test
    public void test1() throws IOException {
        check(basedir + "Example.od");
    }

    private void check(String s) throws IOException {

        OD4DevelopmentParser p = new OD4DevelopmentParser();
        Optional<ASTODArtifact> pp = p.parse(s);
        assertTrue(pp.isPresent());

        PlantUMLODFullPrettyPrinter q = new PlantUMLODFullPrettyPrinter();

        String printed = q.prettyprint(pp.get());
        Optional<ASTODArtifact> parsed = p.parse_String(printed);

        assertTrue("Printed model " + s + " could not be parsed. Printed model: \n" + printed , parsed.isPresent());
        assertTrue("Printed and parsed model " + s + " is not identical to parsed original model. Printed model: \n" + printed, pp.get().deepEquals(parsed.get()));
    }
}
