// (c) https://github.com/MontiCore/monticore

package de.monticore.dateliterals;

import de.monticore.ODOutTestBasis;
import de.monticore.dateliterals._cocos.DateLiteralsCoCos;
import de.monticore.io.paths.MCPath;
import de.monticore.od4report.OD4ReportMill;
import de.monticore.od4report.OD4ReportTestUtil;
import de.monticore.od4report._cocos.OD4ReportCoCoChecker;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.runtime.junit.MCAssertions;
import de.monticore.runtime.junit.TestWithMCLanguage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

@TestWithMCLanguage(OD4ReportMill.class)
public class DateLiteralsCoCoCheckerTest extends ODOutTestBasis {
  
  private static OD4ReportCoCoChecker odCoCoChecker;
  
  @BeforeEach
  public void setup() {
    odCoCoChecker = new OD4ReportCoCoChecker();
  }
  
  @Test
  public void testDateConsistencyCoCo() {
    Path odPath = PATH.resolve(Paths.get("cocos", "WrongDate.od"));
    ASTODArtifact artifact = OD4ReportTestUtil.loadModelAndST(odPath, new MCPath(PATH));
    
    odCoCoChecker.addChecker(new DateLiteralsCoCos().getCheckerForAllCoCos());
    odCoCoChecker.checkAll(artifact);
    
    MCAssertions.assertHasFindingStartingWith("0x0D009");
    MCAssertions.assertHasFindingStartingWith("0x0D009");
    MCAssertions.assertHasFindingStartingWith("0x0D009");
  }
}
