// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore
package de.monticore.od4data;

import de.monticore.io.paths.ModelPath;
import de.monticore.od4data._symboltable.OD4DataArtifactScope;
import de.monticore.od4data._symboltable.OD4DataGlobalScope;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.odbasis._symboltable.ObjectDiagramSymbol;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.se_rwth.commons.logging.Slf4jLog;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class ODSymbolTableCreatorTest {

  private static ModelPath modelPath;

  private OD4DataGlobalScope globalScope;

  @BeforeClass
  public static void setup() {
    Slf4jLog.init();
    Slf4jLog.enableFailQuick(false);

    modelPath = new ModelPath(Paths.get("src/test/resources/symboltable"));
  }

  @Test
  public void testResolveODObjectFromFile() {
    final ObjectDiagramSymbol objectDiagramSymbol = createODDefinitionFromFile(
        "AuctionParticipants");

    List<VariableSymbol> odObjects = objectDiagramSymbol.getObjects();

    assertFalse(odObjects.isEmpty());
    assertEquals(6, odObjects.size());
    for (VariableSymbol obj : odObjects) {
      assertTrue(obj.isPresentAstNode());
    }

    Optional<VariableSymbol> kupferObject = objectDiagramSymbol.getSpannedScope()
        .resolveVariable("kupfer912");
    assertTrue(kupferObject.isPresent());
  }

  @Test
  public void testResolveODObjectFromAST() {
    final ObjectDiagramSymbol objectDiagramSymbol = createObjectDiagramFromAST(
        "AuctionParticipants");

    List<VariableSymbol> odObjects = objectDiagramSymbol.getObjects();

    assertFalse(odObjects.isEmpty());
    for (VariableSymbol obj : odObjects) {
      assertTrue(obj.isPresentAstNode());
    }

    assertTrue(objectDiagramSymbol.getSpannedScope().resolveVariable("kupfer912").isPresent());
  }

  @Test
  public void testAvoidanceOfUnnamedObjects() {
    final ObjectDiagramSymbol objectDiagramSymbol = createODDefinitionFromFile(
        "STInnerLinkVariants");

    List<VariableSymbol> odObjects = objectDiagramSymbol.getObjects();

    assertEquals(5, odObjects.size());
    for (VariableSymbol obj : odObjects) {
      assertTrue(obj.isPresentAstNode());
    }

    Optional<VariableSymbol> fooBarObject = objectDiagramSymbol.getSpannedScope()
        .resolveVariable("fooBar2");
    assertTrue(fooBarObject.isPresent());
  }

  private ObjectDiagramSymbol createODDefinitionFromFile(String odName) {

    globalScope = new OD4DataGlobalScope(modelPath, "od");

    return globalScope.resolveObjectDiagram(odName).orElse(null);
  }

  private ObjectDiagramSymbol createObjectDiagramFromAST(String odName) {
    ASTODArtifact astodArtifact = OD4DataTool
        .parse(Paths.get("src/test/resources/symboltable", odName + ".od").toString());
    OD4DataArtifactScope odBasisArtifactScope = OD4DataTool.createSymbolTable(astodArtifact);
    return odBasisArtifactScope.getObjectDiagramSymbols().get(odName).get(0);
  }

}
