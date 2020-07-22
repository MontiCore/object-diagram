// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore
package de.monticore.od4data;

import de.monticore.generating.templateengine.reporting.commons.ReportingRepository;
import de.monticore.io.paths.ModelPath;
import de.monticore.od4report.OD4ReportMill;
import de.monticore.od4report._parser.OD4ReportParser;
import de.monticore.od4report._symboltable.OD4ReportGlobalScope;
import de.monticore.od4report._symboltable.OD4ReportSymbolTableCreatorDelegator;
import de.monticore.od4report.report.AST2ODReporter;
import de.monticore.od4report.report.ODNodeIdentHelper;
import de.monticore.od4report.report.ST2ODReporter;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.odbasis._symboltable.ObjectDiagramSymbol;
import de.se_rwth.commons.logging.Log;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ODReportingTest {

  private static ModelPath modelPath;

  private void createAST(String packageName, String modelName) throws IOException {
    Path model = Paths.get("src/test/resources/" + packageName + modelName + ".od");
    modelPath = new ModelPath(Paths.get("src/test/resources/" + packageName));
    OD4ReportParser parser = new OD4ReportParser();
    Optional<ASTODArtifact> astodArtifact = parser.parseODArtifact(model.toString());
    assertFalse(parser.hasErrors());
    assertTrue(astodArtifact.isPresent());

    OD4ReportGlobalScope globalScope = new OD4ReportGlobalScope(modelPath, "od");

    OD4ReportSymbolTableCreatorDelegator symTabVisitor = OD4ReportMill
        .oD4ReportSymbolTableCreatorDelegatorBuilder().setGlobalScope(globalScope).build();
    symTabVisitor.createFromAST(astodArtifact.get());

    ObjectDiagramSymbol objectDiagramSymbol = globalScope.resolveObjectDiagram(modelName)
        .orElse(null);

    if (objectDiagramSymbol != null && objectDiagramSymbol.isPresentAstNode()) {
      Log.warn("Cannot find AST for symbol " + modelName);
    }

    ReportingRepository reporting = new ReportingRepository(new ODNodeIdentHelper());

    // Report AST
    AST2ODReporter reporter = new AST2ODReporter("target", modelName, reporting);
    reporter.flush(astodArtifact.get().getObjectDiagram());

    // Report ST
    ST2ODReporter st2ODReporter = new ST2ODReporter("target", modelName, reporting);
    st2ODReporter.flush(astodArtifact.get().getObjectDiagram());

  }

  @Ignore
  @Test
  public void reportInnerLinkVariants() throws IOException {
    createAST("examples/hierarchical/", "InnerLinkVariants");

    Path astModel = Paths.get("target/reports/InnerLinkVariants/InnerLinkVariants_AST.od");
    Path stModel = Paths.get("target/reports/InnerLinkVariants/InnerLinkVariants_ST.od");

    // Initialize the CoCoChecker
    // ODCoCoChecker odCoCoChecker = new ODCoCoChecker();
    // odCoCoChecker.addCoCo(new UniqueObjectNamesCoCo());
    // odCoCoChecker.addCoCo(new ValidObjectReferenceCoCo());
    // odCoCoChecker.addCoCo(new ValidLinkReferenceCoCo());
    // odCoCoChecker.addCoCo(new LinkConsistsOfReferenceNamesCoCo());

    // Get the Parser
    OD4ReportParser parser = new OD4ReportParser();

    // Parse and check AST-Report
    Optional<ASTODArtifact> ASTODArtifact = parser.parseODArtifact(astModel.toString());
    assertFalse(parser.hasErrors());
    assertTrue(ASTODArtifact.isPresent());
    // odCoCoChecker.checkAll(ASTODArtifact.get());

    // Parse and check ST-Report
    ASTODArtifact = parser.parseODArtifact(stModel.toString());
    assertFalse(parser.hasErrors());
    assertTrue(ASTODArtifact.isPresent());
    // odCoCoChecker.checkAll(ASTODArtifact.get());
  }

  @Ignore
  @Test
  public void checkReportODGrammar() throws IOException {
    Path astModel = Paths
        .get("target/generated-sources/monticore/sourcecode/reports/de.monticore.OD/OD_AST.od");
    Path stModel = Paths
        .get("target/generated-sources/monticore/sourcecode/reports/de.monticore.OD/OD_ST.od");

    // Initialize the CoCoChecker
    // ODCoCoChecker odCoCoChecker = new ODCoCoChecker();
    // odCoCoChecker.addCoCo(new UniqueObjectNamesCoCo());
    // odCoCoChecker.addCoCo(new ValidObjectReferenceCoCo());
    // odCoCoChecker.addCoCo(new ValidLinkReferenceCoCo());
    // odCoCoChecker.addCoCo(new LinkConsistsOfReferenceNamesCoCo());

    // Get the Parser
    OD4ReportParser parser = new OD4ReportParser();

    // Parse and check AST-Report
    Optional<ASTODArtifact> ASTODArtifact = parser.parseODArtifact(astModel.toString());
    assertFalse(parser.hasErrors());
    assertTrue(ASTODArtifact.isPresent());
    // odCoCoChecker.checkAll(ASTODArtifact.get());

    // Parse and check ST-Report
    ASTODArtifact = parser.parseODArtifact(stModel.toString());
    assertFalse(parser.hasErrors());
    assertTrue(ASTODArtifact.isPresent());
    // odCoCoChecker.checkAll(ASTODArtifact.get());
  }

}
