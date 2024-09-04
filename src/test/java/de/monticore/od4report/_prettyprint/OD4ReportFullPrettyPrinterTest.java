// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report._prettyprint;

import de.monticore.od4report.OD4ReportMill;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.se_rwth.commons.logging.Log;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class OD4ReportFullPrettyPrinterTest {

  @Before
  public void setUp() {
    Log.init();
    Log.enableFailQuick(false);

    OD4ReportMill.reset();
    OD4ReportMill.init();
  }

  @Test
  public void testPrettyPrint() throws IOException {
    Path inputOD = Paths.get("src", "test", "resources", "examples", "od", "SimpleOD.od");
    Optional<ASTODArtifact> simpleOD = OD4ReportMill.parser().parse(inputOD.toString());
    Assert.assertTrue(simpleOD.isPresent());

    // Test pretty printer by printing and parsing again
    String printedOD = OD4ReportMill.prettyPrint(simpleOD.get(), true);
    Optional<ASTODArtifact> reparsedSimpleOD = OD4ReportMill.parser().parse_String(printedOD);
    Assert.assertTrue(reparsedSimpleOD.isPresent());
    Assert.assertTrue(simpleOD.get().deepEquals(reparsedSimpleOD.get(), false));
  }

}
