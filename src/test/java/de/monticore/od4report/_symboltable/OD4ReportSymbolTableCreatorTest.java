package de.monticore.od4report._symboltable;

import de.monticore.cd4analysis.CD4AnalysisMill;
import de.monticore.cd4analysis._symboltable.ICD4AnalysisGlobalScope;
import de.monticore.cd4analysis.resolver.CD4AnalysisResolver;
import de.monticore.io.paths.ModelPath;
import de.monticore.od4report.OD4ReportMill;
import de.monticore.od4report.OD4ReportTool;
import de.monticore.od4report._parser.OD4ReportParser;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.symbols.basicsymbols._symboltable.TypeSymbol;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.se_rwth.commons.logging.Log;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

public class OD4ReportSymbolTableCreatorTest {

  private ModelPath modelPath = new ModelPath(
      Paths.get("src", "test", "resources", "symboltable", "cd"));

  private final Path INPUTFOLDER = Paths.get("src", "test", "resources", "symboltable");

  @Test
  public void testOD4ReportSymbolTableCreator() throws IOException {
    OD4ReportParser parser = new OD4ReportParser();
    Optional<ASTODArtifact> artifact = parser.parse(
        Paths.get(INPUTFOLDER.toString(), "AuctionParticipants.od").toString());
    assertTrue(artifact.isPresent());

    ICD4AnalysisGlobalScope cdGlobalScope = CD4AnalysisMill.globalScope();
    CD4AnalysisResolver cd4AnalysisResolver = CD4AnalysisMill.cD4AnalysisResolvingDelegate(
        cdGlobalScope);
    cdGlobalScope.addBuiltInTypes();

    IOD4ReportGlobalScope odGlobalScope = OD4ReportMill.globalScope();
    odGlobalScope.addAdaptedOOTypeSymbolResolver(cd4AnalysisResolver);
    odGlobalScope.addAdaptedVariableSymbolResolver(cd4AnalysisResolver);
    odGlobalScope.addAdaptedFieldSymbolResolver(cd4AnalysisResolver);

    IOD4ReportArtifactScope symbolTable = OD4ReportTool.createSymbolTable(artifact.get());

    Optional<TypeSymbol> person = symbolTable.resolveType("Person");
    Optional<VariableSymbol> lisa = symbolTable.resolveVariable("lisa");
    assertTrue(lisa.isPresent());
  }

  @Before
  public void setUp() {
    Log.enableFailQuick(false);

    CD4AnalysisMill.reset();
    CD4AnalysisMill.init();
    CD4AnalysisMill.globalScope().setModelPath(modelPath);
    CD4AnalysisMill.globalScope().setFileExt("cd");

    OD4ReportMill.reset();
    OD4ReportMill.init();
    OD4ReportMill.globalScope().setModelPath(modelPath);
    OD4ReportMill.globalScope().setFileExt("od");
  }

}
