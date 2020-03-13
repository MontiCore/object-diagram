// (c) https://github.com/MontiCore/monticore
package de.monticore.lang.odbasics;

import de.monticore.generating.templateengine.reporting.commons.ReportingRepository;
import de.monticore.io.paths.ModelPath;
import de.monticore.lang.odbasics._ast.ASTODArtifact;
import de.monticore.lang.odbasics._parser.ODBasicsParser;
import de.monticore.lang.odbasics._symboltable.ODBasicsGlobalScope;
import de.monticore.lang.odbasics._symboltable.ODBasicsLanguage;
import de.monticore.lang.odbasics._symboltable.ODBasicsSymbolTableCreatorDelegator;
import de.monticore.lang.odbasics._symboltable.ObjectDiagramSymbol;
import de.monticore.lang.odbasics.report.AST2ODReporter;
import de.monticore.lang.odbasics.report.ODNodeIdentHelper;
import de.monticore.lang.odbasics.report.ST2ODReporter;
import de.se_rwth.commons.logging.Log;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link ODBasicsParser}.
 *
 * @author Marita Breuer
 */
public class ODReportingTest {

  private static ODBasicsLanguage odLanguage;

  private static ModelPath modelPath;

  @BeforeClass
  public static void setup() {
    odLanguage = new ODBasicsLanguage();
  }

  private void createAST(String packageName, String modelName) throws IOException {
    Path model = Paths.get("src/test/resources/" + packageName + modelName + ".od");
    modelPath = new ModelPath(Paths.get("src/test/resources/" + packageName));
    ODBasicsParser parser = new ODBasicsParser();
    Optional<ASTODArtifact> astodArtifact = parser.parseODArtifact(model.toString());
    assertFalse(parser.hasErrors());
    assertTrue(astodArtifact.isPresent());

    ODBasicsGlobalScope globalScope = new ODBasicsGlobalScope(modelPath, odLanguage);

    ODBasicsSymbolTableCreatorDelegator symTabVisitor = odLanguage
        .getSymbolTableCreator(globalScope);
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
    ODBasicsParser parser = new ODBasicsParser();

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
    Path astModel = Paths.get(
        "target/generated-sources/monticore/sourcecode/reports/de.monticore.lang.OD/OD_AST.od");
    Path stModel = Paths
        .get("target/generated-sources/monticore/sourcecode/reports/de.monticore.lang.OD/OD_ST.od");

    // Initialize the CoCoChecker
    // ODCoCoChecker odCoCoChecker = new ODCoCoChecker();
    // odCoCoChecker.addCoCo(new UniqueObjectNamesCoCo());
    // odCoCoChecker.addCoCo(new ValidObjectReferenceCoCo());
    // odCoCoChecker.addCoCo(new ValidLinkReferenceCoCo());
    // odCoCoChecker.addCoCo(new LinkConsistsOfReferenceNamesCoCo());

    // Get the Parser
    ODBasicsParser parser = new ODBasicsParser();

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
