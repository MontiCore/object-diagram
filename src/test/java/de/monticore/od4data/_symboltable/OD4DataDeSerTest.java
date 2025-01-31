// (c) https://github.com/MontiCore/monticore

package de.monticore.od4data._symboltable;

import de.monticore.ODTestBasis;
import de.monticore.io.paths.MCPath;
import de.monticore.od4data.OD4DataMill;
import de.monticore.od4data.OD4DataTestUtil;
import de.monticore.od4data.OD4DataToolAPI;
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

public class OD4DataDeSerTest extends ODTestBasis {
  
  private final Path SIMPLEOD2 = PATH.resolve(Paths.get("examples", "od", "SimpleOD2.od"));
  
  @BeforeEach
  public void setUp() {
    OD4DataMill.reset();
    OD4DataMill.init();
  }
  
  @Test
  public void testOD4DataDeSer() {
    String artifact = SIMPLEOD2.toString();
    MCPath symbolPath = new MCPath(PATH);
    ASTODArtifact astodArtifact = OD4DataTestUtil.loadModel(artifact, symbolPath);
    
    IOD4DataArtifactScope od4DataArtifactScope =
        OD4DataToolAPI.createSymbolTable(astodArtifact, true);
    
    // serialize
    OD4DataSymbols2Json od4DataSymbols2Json = new OD4DataSymbols2Json();
    String fileName = Paths.get(SIMPLEOD2.toString()).getFileName().toString() + "sym";
    String pathFromQualifiedName = Names.getPathFromQualifiedName(
        astodArtifact.getMCPackageDeclaration().getMCQualifiedName().getQName() + "."
            + astodArtifact.getObjectDiagram().getName());
    String storedPath = Paths.get(folder.toString(), pathFromQualifiedName, fileName).toString();
    od4DataSymbols2Json.store(od4DataArtifactScope, storedPath);
    
    Path storedSymTable = Paths.get(storedPath);
    assertTrue(storedSymTable.toFile().exists());
    
    // deserialize
    IOD4DataArtifactScope loadedBasicsArtifactScope =
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
    ASTODArtifact ast = OD4DataTestUtil.loadModel(artifact, symbolPath);
    
    // create symbol table
    IOD4DataArtifactScope artifactScope = OD4DataTestUtil.createSymbolTableFromAST(ast);
    OD4DataSymbols2Json symbols2Json = new OD4DataSymbols2Json();
    String serialized = symbols2Json.serialize(artifactScope);
    assertNotNull(serialized);
    assertNotEquals("", serialized);
    
    // check for contents
    assertContains(serialized, "\"name\":\"SimpleOD2\"");
    assertContains(serialized, "\"fullName\":\"examples.od.myObject1\"");
    assertContains(serialized, "\"fullName\":\"examples.od.fooBar2\"");
    assertContains(serialized, "\"fullName\":\"examples.od.myObject2\"");
    assertContains(serialized, "\"objName\":\"examples.cd.SimpleOD2.ObjectType2\"");
    
    assertEquals(0, Log.getErrorCount());
  }
  
  @Test
  public void deserializationTest() {
    IOD4DataGlobalScope gs = OD4DataMill.globalScope();
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
