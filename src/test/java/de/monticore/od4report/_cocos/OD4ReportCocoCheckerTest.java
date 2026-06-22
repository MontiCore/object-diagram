/*
 (c) https://github.com/MontiCore/monticore
 */

package de.monticore.od4report._cocos;

import de.monticore.ODTestBasis;
import de.monticore.io.paths.MCPath;
import de.monticore.od4report.OD4ReportMill;
import de.monticore.od4report.OD4ReportTestUtil;
import de.monticore.od4report._symboltable.IOD4ReportGlobalScope;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.odbasis._cocos.object.ValidObjectTypeCoco;
import de.monticore.runtime.junit.MCAssertions;
import de.monticore.runtime.junit.TestWithMCLanguage;
import de.monticore.symbols.basicsymbols._symboltable.TypeSymbol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

@TestWithMCLanguage(OD4ReportMill.class)
public class OD4ReportCocoCheckerTest extends ODTestBasis {
  
  private OD4ReportCoCoChecker odCoCoChecker;
  
  @BeforeEach
  public void setUp() {
    odCoCoChecker = new OD4ReportCoCoChecker();
  }
  
  @Test
  public void checkValidObjectTypeCocoSuccess() {
    Path odPath = PATH.resolve(Paths.get("examples", "od", "MyFamily.od"));
    ASTODArtifact artifact = OD4ReportTestUtil.loadModelAndST(odPath, new MCPath(PATH));
    
    odCoCoChecker.addCoCo(new ValidObjectTypeCoco());
    odCoCoChecker.checkAll(artifact);
  }
  
  @Test
  public void checkValidObjectTypeCocoFailure() {
    IOD4ReportGlobalScope gs = OD4ReportMill.globalScope();
    TypeSymbol person = OD4ReportMill.typeSymbolBuilder().setName("Person").setEnclosingScope(gs)
        .setSpannedScope(OD4ReportMill.scope()).build();
    TypeSymbol bmw = OD4ReportMill.typeSymbolBuilder().setName("BMW").setEnclosingScope(gs)
        .setSpannedScope(OD4ReportMill.scope()).build();
    gs.add(person);
    gs.add(bmw);
    
    Path odPath = PATH.resolve(Paths.get("examples", "od", "MyFamily.od"));
    ASTODArtifact artifact = OD4ReportTestUtil.loadModelAndST(odPath, new MCPath());
    
    odCoCoChecker.addCoCo(new ValidObjectTypeCoco());
    odCoCoChecker.checkAll(artifact);
    
    MCAssertions.assertHasFindingStartingWith("0xA0324 Cannot find symbol Jaguar");
    MCAssertions.assertHasFindingStartingWith(
        "0x0D013: The type of the return type (ASTMCQualifiedType) could not be calculated");
    MCAssertions.assertHasFindingStartingWith("0xB0035: Type 'Jaguar' is used but not defined.");
  }
  
}
