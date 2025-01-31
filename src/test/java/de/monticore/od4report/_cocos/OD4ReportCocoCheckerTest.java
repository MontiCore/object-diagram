/*
 (c) https://github.com/MontiCore/monticore
 */

package de.monticore.od4report._cocos;

import de.monticore.ODTestBasis;
import de.monticore.io.paths.MCPath;
import de.monticore.od4data.OD4DataMill;
import de.monticore.od4report.OD4ReportMill;
import de.monticore.od4report.OD4ReportTestUtil;
import de.monticore.od4report._symboltable.IOD4ReportGlobalScope;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.odbasis._cocos.object.ValidObjectTypeCoco;
import de.monticore.symbols.basicsymbols._symboltable.TypeSymbol;
import de.se_rwth.commons.logging.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class OD4ReportCocoCheckerTest extends ODTestBasis {
  
  private OD4ReportCoCoChecker odCoCoChecker;
  
  @BeforeEach
  public void setUp() {
    odCoCoChecker = new OD4ReportCoCoChecker();
    
    OD4ReportMill.reset();
    OD4ReportMill.init();
  }
  
  @Test
  public void checkValidObjectTypeCocoSuccess() {
    Path odPath = PATH.resolve(Paths.get("examples", "od", "MyFamily.od"));
    ASTODArtifact artifact = OD4ReportTestUtil.loadModelAndST(odPath, new MCPath(PATH));
    
    odCoCoChecker.addCoCo(new ValidObjectTypeCoco());
    odCoCoChecker.checkAll(artifact);
    
    assertEquals(0, Log.getErrorCount());
    
  }
  
  @Test
  public void checkValidObjectTypeCocoFailure() {
    IOD4ReportGlobalScope gs = OD4ReportMill.globalScope();
    TypeSymbol person = OD4ReportMill.typeSymbolBuilder().setName("Person").setEnclosingScope(gs)
        .setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol bmw = OD4ReportMill.typeSymbolBuilder().setName("BMW").setEnclosingScope(gs)
        .setSpannedScope(OD4DataMill.scope()).build();
    gs.add(person);
    gs.add(bmw);
    
    Path odPath = PATH.resolve(Paths.get("examples", "od", "MyFamily.od"));
    ASTODArtifact artifact = OD4ReportTestUtil.loadModelAndST(odPath, new MCPath());
    
    odCoCoChecker.addCoCo(new ValidObjectTypeCoco());
    odCoCoChecker.checkAll(artifact);
    
    assertEquals(2, Log.getErrorCount());
    assertContains(Log.getFindings().get(0).getMsg(), "0xA0324 Cannot find symbol Jaguar");
    assertContains(Log.getFindings().get(1).getMsg(),
        "0xB0035: Type 'Jaguar' is used but not defined.");
    Log.clearFindings();
  }
  
}
