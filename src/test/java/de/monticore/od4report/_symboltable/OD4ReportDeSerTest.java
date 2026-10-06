// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report._symboltable;

import de.monticore.ODTestBasis;
import de.monticore.io.paths.MCPath;
import de.monticore.od4report.OD4ReportMill;
import de.monticore.od4report.OD4ReportTestUtil;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.runtime.junit.TestWithMCLanguage;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.se_rwth.commons.Names;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestWithMCLanguage(OD4ReportMill.class)
public class OD4ReportDeSerTest extends ODTestBasis {

  private final Path INPUT_OD = PATH.resolve(Paths.get("examples", "od", "MyFamily.od"));

  @Test
  public void testOD4ReportDeSer() {
    ASTODArtifact artifact = OD4ReportTestUtil.loadModel(INPUT_OD, new MCPath(PATH));
    IOD4ReportArtifactScope artifactScope = OD4ReportTestUtil.createSymbolTableFromAST(artifact);
    OD4ReportSymbols2Json symbols2Json = new OD4ReportSymbols2Json();

    Path storedPath = store(symbols2Json, artifact, artifactScope);
    IOD4ReportArtifactScope loadedArtifactScope = symbols2Json.load(storedPath.toString());

    // clear buffer of traverser, as elements should be traversed again
    symbols2Json.getTraverser().clearTraversedElements();

    assertEquals(symbols2Json.serialize(artifactScope), symbols2Json.serialize(loadedArtifactScope));
  }

  @Test
  public void serializationTest() {
    ASTODArtifact ast = OD4ReportTestUtil.loadModel(INPUT_OD, new MCPath(PATH));

    // create symbol table
    IOD4ReportArtifactScope artifactScope = OD4ReportTestUtil.createSymbolTableFromAST(ast);
    OD4ReportSymbols2Json symbols2Json = new OD4ReportSymbols2Json();
    String serialized = symbols2Json.serialize(artifactScope);
    assertNotNull(serialized);
    assertNotEquals("", serialized);

    // check for contents
    IOD4ReportArtifactScope deserialized = symbols2Json.deserialize(serialized);
    assertEquals("MyFamily", deserialized.getName());
    Optional<VariableSymbol> tiger = deserialized.resolveVariable("tiger");
    assertEquals("examples.od.tiger", tiger.get().getFullName());
    Optional<VariableSymbol> alice = deserialized.resolveVariable("alice");
    assertEquals("examples.od.alice", alice.get().getFullName());
    Optional<VariableSymbol> bob = deserialized.resolveVariable("bob");
    assertEquals("examples.od.bob", bob.get().getFullName());
    assertEquals("examples.cd.MyFamily.Person", alice.get().getType().printFullName());
  }

  /**
   * Loads a symbol file, which is created from the current model, via the symbol path of the
   * global scope and resolves its objects.
   */
  @Test
  public void deserializationTest() {
    ASTODArtifact artifact = OD4ReportTestUtil.loadModel(INPUT_OD, new MCPath(PATH));
    IOD4ReportArtifactScope artifactScope = OD4ReportTestUtil.createSymbolTableFromAST(artifact);
    store(new OD4ReportSymbols2Json(), artifact, artifactScope);

    IOD4ReportGlobalScope gs = OD4ReportMill.globalScope();
    gs.clear();
    gs.setSymbolPath(new MCPath(getTmpDirPath()));
    assertTrue(gs.getSubScopes().isEmpty());
    gs.loadFileForModelName("examples.od.MyFamily");
    assertEquals(1, gs.getSubScopes().size());

    for (String object : new String[] { "alice", "bob", "tiger" }) {
      assertTrue(gs.resolveVariable("examples.od.MyFamily." + object).isPresent(),
          "Could not resolve object " + object);
    }
  }

  /**
   * Stores the symbol table in the temporary directory at the location of the model's qualified
   * name, e.g., {@code examples/od/MyFamily.odsym}.
   */
  protected Path store(OD4ReportSymbols2Json symbols2Json, ASTODArtifact artifact,
      IOD4ReportArtifactScope artifactScope) {
    String qualifiedName = artifact.getMCPackageDeclaration().getMCQualifiedName().getQName()
        + "." + artifact.getObjectDiagram().getName();
    Path storedPath = getTmpDirPath().resolve(Names.getPathFromQualifiedName(qualifiedName))
        .resolve(artifact.getObjectDiagram().getName() + ".odsym");

    symbols2Json.store(artifactScope, storedPath.toString());

    assertTrue(Files.isRegularFile(storedPath), "Symbol file was not stored: " + storedPath);
    return storedPath;
  }
}
