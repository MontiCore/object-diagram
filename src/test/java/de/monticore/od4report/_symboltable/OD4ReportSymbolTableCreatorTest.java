/* (c) https://github.com/MontiCore/monticore */

package de.monticore.od4report._symboltable;

import de.monticore.ODTestBasis;
import de.monticore.io.paths.MCPath;
import de.monticore.od4report.OD4ReportMill;
import de.monticore.od4report.OD4ReportTestUtil;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.runtime.junit.TestWithMCLanguage;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestWithMCLanguage(OD4ReportMill.class)
public class OD4ReportSymbolTableCreatorTest extends ODTestBasis {
  
  private final Path INPUT_DIR = PATH.resolve("symboltable");
  
  @Test
  public void testOD4ReportSymbolTableCreator() {
    Path inputOD = INPUT_DIR.resolve("AuctionParticipants.od");
    ASTODArtifact artifact = OD4ReportTestUtil.loadModel(inputOD, new MCPath(PATH));
    
    IOD4ReportArtifactScope symbolTable = OD4ReportTestUtil.createSymbolTableFromAST(artifact);
    Optional<VariableSymbol> person =
        symbolTable.resolveVariable("symboltable.symbols.MyFamily.alice");
    assertTrue(person.isPresent());
  }
}
