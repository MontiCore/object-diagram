// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report._prettyprint;

import de.monticore.ODTestBasis;
import de.monticore.od4report.OD4ReportMill;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.runtime.junit.TestWithMCLanguage;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestWithMCLanguage(OD4ReportMill.class)
public class OD4ReportFullPrettyPrinterTest extends ODTestBasis {
  
  @Test
  public void testPrettyPrint() throws IOException {
    Path inputOD = PATH.resolve(Paths.get("examples", "od", "SimpleOD.od"));
    Optional<ASTODArtifact> simpleOD = OD4ReportMill.parser().parse(inputOD.toString());
    assertTrue(simpleOD.isPresent());
    
    // Test pretty printer by printing and parsing again
    String printedOD = OD4ReportMill.prettyPrint(simpleOD.get(), true);
    Optional<ASTODArtifact> reparsedSimpleOD = OD4ReportMill.parser().parse_String(printedOD);
    assertTrue(reparsedSimpleOD.isPresent());
    assertTrue(simpleOD.get().deepEquals(reparsedSimpleOD.get(), false));
  }
  
}
