// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report._symboltable;

import de.monticore.ODTestBasis;
import de.monticore.io.paths.MCPath;
import de.monticore.od4report.OD4ReportMill;
import de.monticore.od4report.OD4ReportTestUtil;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;

import de.se_rwth.commons.Names;
import de.se_rwth.commons.logging.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class OD4ReportDeSerTest extends ODTestBasis {
  
  private final Path INPUT_OD = PATH.resolve(Paths.get("examples", "od", "MyFamily.od"));
  
  private final Path SYMBOL_TARGET = Paths.get("target", "deser");
  
  @BeforeEach
  public void setUp() {
    OD4ReportMill.reset();
    OD4ReportMill.init();
  }
  
  @Test
  public void testOD4ReportDeSer() {
    ASTODArtifact artifact = OD4ReportTestUtil.loadModel(INPUT_OD, new MCPath(PATH));
    IOD4ReportArtifactScope artifactScope = OD4ReportTestUtil.createSymbolTableFromAST(artifact);
    
    // serialize
    OD4ReportSymbols2Json od4ReportSymbols2Json = new OD4ReportSymbols2Json();
    String fileName = INPUT_OD.getFileName().toString() + "sym";
    String pathFromQualifiedName = Names.getPathFromQualifiedName(
        artifact.getMCPackageDeclaration().getMCQualifiedName().getQName() + "."
            + artifact.getObjectDiagram().getName());
    Path storedPath = SYMBOL_TARGET.resolve(pathFromQualifiedName).resolve(fileName);
    od4ReportSymbols2Json.store(artifactScope, storedPath.toString());
    
    assertTrue(storedPath.toFile().exists());
    
    // deserialize
    IOD4ReportArtifactScope loadedBasicsArtifactScope =
        od4ReportSymbols2Json.load(storedPath.toString());
    
    // clear buffer of traverser, as elements should be traversed again
    od4ReportSymbols2Json.getTraverser().clearTraversedElements();
    
    assertEquals(od4ReportSymbols2Json.serialize(artifactScope),
        od4ReportSymbols2Json.serialize(loadedBasicsArtifactScope));
    assertEquals(0, Log.getFindingsCount());
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
    assertContains(serialized, "\"name\":\"MyFamily\"");
    assertContains(serialized, "\"fullName\":\"examples.od.tiger\"");
    assertContains(serialized, "\"fullName\":\"examples.od.alice\"");
    assertContains(serialized, "\"fullName\":\"examples.od.bob\"");
    assertContains(serialized, "\"objName\":\"examples.cd.MyFamily.Person\"");
    
    assertEquals(0, Log.getErrorCount());
  }
  
  @Test
  public void deserializationTest() {
    IOD4ReportGlobalScope gs = OD4ReportMill.globalScope();
    gs.clear();
    gs.setSymbolPath(new MCPath(PATH));
    assertTrue(gs.getSubScopes().isEmpty());
    gs.loadFileForModelName("examples.od.MyFamily");
    assertEquals(1, gs.getSubScopes().size());
    
    // resolve for object alice
    Optional<VariableSymbol> alice = gs.resolveVariable("examples.od.MyFamily.alice");
    assertTrue(alice.isPresent());
    
    // resolve for object bob
    Optional<VariableSymbol> bob = gs.resolveVariable("examples.od.MyFamily.bob");
    assertTrue(bob.isPresent());
    
    // resolve for object tiger
    Optional<VariableSymbol> tiger = gs.resolveVariable("examples.od.MyFamily.tiger");
    assertTrue(tiger.isPresent());
  }
}
