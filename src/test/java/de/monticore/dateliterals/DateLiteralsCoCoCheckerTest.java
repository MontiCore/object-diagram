// (c) https://github.com/MontiCore/monticore

package de.monticore.dateliterals;

import de.monticore.ODOutTestBasis;
import de.monticore.dateliterals._cocos.DateLiteralsCoCos;
import de.monticore.io.paths.MCPath;
import de.monticore.od4report.OD4ReportMill;
import de.monticore.od4report.OD4ReportTestUtil;
import de.monticore.od4report._cocos.OD4ReportCoCoChecker;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.se_rwth.commons.logging.Log;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class DateLiteralsCoCoCheckerTest extends ODOutTestBasis {
  
  private static OD4ReportCoCoChecker odCoCoChecker;
  
  @BeforeEach
  public void setup() {
    OD4ReportMill.reset();
    OD4ReportMill.init();
    
    odCoCoChecker = new OD4ReportCoCoChecker();
  }
  
  @Test
  public void testDateConsistencyCoCo() {
    Path odPath = PATH.resolve(Paths.get("cocos", "WrongDate.od"));
    ASTODArtifact artifact = OD4ReportTestUtil.loadModelAndST(odPath, new MCPath(PATH));
    
    odCoCoChecker.addChecker(new DateLiteralsCoCos().getCheckerForAllCoCos());
    odCoCoChecker.checkAll(artifact);
    
    assertEquals(3, Log.getErrorCount());
    Log.clearFindings();
  }
}
