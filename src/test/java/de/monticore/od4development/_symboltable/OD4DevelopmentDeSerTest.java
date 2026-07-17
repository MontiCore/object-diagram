// (c) https://github.com/MontiCore/monticore

package de.monticore.od4development._symboltable;

import de.monticore.ODTestBasis;
import de.monticore.io.paths.MCPath;
import de.monticore.od4development.OD4DevelopmentTestUtil;
import de.monticore.od4development.OD4DevelopmentMill;
import de.monticore.od4development.OD4DevelopmentTool;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.runtime.junit.TestWithMCLanguage;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.se_rwth.commons.Names;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestWithMCLanguage(OD4DevelopmentMill.class)
public class OD4DevelopmentDeSerTest extends ODTestBasis {
  
  private final Path SIMPLEOD2 = PATH.resolve(Paths.get("examples", "od", "SimpleOD2.od"));
  
  @Test
  public void testOD4DataDeSer() {
    String artifact = SIMPLEOD2.toString();
    MCPath symbolPath = new MCPath(PATH);
    ASTODArtifact astodArtifact = OD4DevelopmentTestUtil.loadModel(artifact, symbolPath);
    
    IOD4DevelopmentArtifactScope od4DataArtifactScope = OD4DevelopmentTestUtil.createSymbolTableFromAST(astodArtifact);
    OD4DevelopmentTestUtil.completeSymbolTable(astodArtifact, true);
    
    // serialize
    OD4DevelopmentSymbols2Json od4DataSymbols2Json = new OD4DevelopmentSymbols2Json();
    String fileName = Paths.get(SIMPLEOD2.toString()).getFileName().toString() + "sym";
    String pathFromQualifiedName = Names.getPathFromQualifiedName(
        astodArtifact.getMCPackageDeclaration().getMCQualifiedName().getQName() + "."
            + astodArtifact.getObjectDiagram().getName());
    String storedPath = Paths.get(folder.toString(), pathFromQualifiedName, fileName).toString();
    od4DataSymbols2Json.store(od4DataArtifactScope, storedPath);
    
    Path storedSymTable = Paths.get(storedPath);
    assertTrue(storedSymTable.toFile().exists());
    
    // deserialize
    IOD4DevelopmentArtifactScope loadedBasicsArtifactScope =
        od4DataSymbols2Json.load(storedSymTable.toString());
    
    // clear buffer of traverser, as elements should be traversed again
    od4DataSymbols2Json.getTraverser().clearTraversedElements();
    
    assertEquals(od4DataSymbols2Json.serialize(od4DataArtifactScope),
        od4DataSymbols2Json.serialize(loadedBasicsArtifactScope));
  }
  
  @Test
  public void serializationTest() {
    String artifact = SIMPLEOD2.toString();
    MCPath symbolPath = new MCPath(PATH);
    ASTODArtifact ast = OD4DevelopmentTestUtil.loadModel(artifact, symbolPath);
    
    // create symbol table
    IOD4DevelopmentArtifactScope artifactScope = OD4DevelopmentTestUtil.createSymbolTableFromAST(ast);
    OD4DevelopmentTestUtil.completeSymbolTable(ast, true);
    OD4DevelopmentSymbols2Json symbols2Json = new OD4DevelopmentSymbols2Json();
    String serialized = symbols2Json.serialize(artifactScope);
    assertNotNull(serialized);
    assertNotEquals("", serialized);
    
    // check for contents
    assertContains(serialized, "\"name\":\"SimpleOD2\"");
    assertContains(serialized, "\"fullName\":\"examples.od.myObject1\"");
    assertContains(serialized, "\"fullName\":\"examples.od.fooBar2\"");
    assertContains(serialized, "\"fullName\":\"examples.od.myObject2\"");
    assertContains(serialized, "\"objName\":\"examples.cd.SimpleOD2.ObjectType2\"");
  }
  
  @Test
  public void deserializationTest() {
    IOD4DevelopmentGlobalScope gs = OD4DevelopmentMill.globalScope();
    gs.clear();
    gs.setSymbolPath(new MCPath(PATH));
    assertTrue(gs.getSubScopes().isEmpty());
    gs.loadFileForModelName("examples.od.SimpleOD2");
    assertEquals(1, gs.getSubScopes().size());
    
    // resolve for object alice
    Optional<VariableSymbol> obj1 = gs.resolveVariable("examples.od.SimpleOD2.myObject1");
    assertTrue(obj1.isPresent());
    
    // resolve for object bob
    Optional<VariableSymbol> obj2 = gs.resolveVariable("examples.od.SimpleOD2.myObject2");
    assertTrue(obj2.isPresent());
    
    // resolve for object tiger
    Optional<VariableSymbol> obj3 = gs.resolveVariable("examples.od.SimpleOD2.fooBar3");
    assertTrue(obj3.isPresent());
  }
}
