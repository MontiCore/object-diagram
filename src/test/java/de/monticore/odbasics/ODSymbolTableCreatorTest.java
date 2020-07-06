// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore
package de.monticore.odbasics;

import de.monticore.io.paths.ModelPath;
import de.monticore.odbasics._ast.ASTODArtifact;
import de.monticore.odbasics._symboltable.ODBasicsArtifactScope;
import de.monticore.odbasics._symboltable.ODBasicsGlobalScope;
import de.monticore.odbasics._symboltable.ODObjectSymbol;
import de.monticore.odbasics._symboltable.ObjectDiagramSymbol;
import de.se_rwth.commons.logging.Slf4jLog;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.Collection;
import java.util.Optional;

import static org.junit.Assert.*;

public class ODSymbolTableCreatorTest {

  private static ModelPath modelPath;

  private ODBasicsGlobalScope globalScope;

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

    Collection<ODObjectSymbol> odObjects = objectDiagramSymbol.getObjects();

    assertFalse(odObjects.isEmpty());
    assertEquals(6, odObjects.size());
    for (ODObjectSymbol obj : odObjects) {
      assertTrue(obj.isPresentAstNode());
    }

    Optional<ODObjectSymbol> kupferObject = objectDiagramSymbol.getSpannedScope()
        .resolveODObject("kupfer912");
    assertTrue(kupferObject.isPresent());

  }

  @Test
  public void testResolveODObjectFromAST() {
    final ObjectDiagramSymbol objectDiagramSymbol = createObjectDiagramFromAST(
        "AuctionParticipants");

    Collection<ODObjectSymbol> odObjects = objectDiagramSymbol.getObjects();

    assertFalse(odObjects.isEmpty());
    for (ODObjectSymbol obj : odObjects) {
      assertTrue(obj.isPresentAstNode());
    }

    assertTrue(objectDiagramSymbol.getSpannedScope().resolveODObject("kupfer912").isPresent());
  }

  @Test
  public void testAvoidanceOfUnnamedObjects() {
    final ObjectDiagramSymbol objectDiagramSymbol = createODDefinitionFromFile(
        "STInnerLinkVariants");

    Collection<ODObjectSymbol> odObjects = objectDiagramSymbol.getObjects();

    assertEquals(5, odObjects.size());
    for (ODObjectSymbol obj : odObjects) {
      assertTrue(obj.isPresentAstNode());
    }

    Optional<ODObjectSymbol> fooBarObject = objectDiagramSymbol.getSpannedScope()
        .resolveODObject("fooBar2");
    assertTrue(fooBarObject.isPresent());
  }

  private ObjectDiagramSymbol createODDefinitionFromFile(String odName) {

    globalScope = new ODBasicsGlobalScope(modelPath, "od");

    return globalScope.resolveObjectDiagram(odName).orElse(null);
  }

  private ObjectDiagramSymbol createObjectDiagramFromAST(String odName) {
    ASTODArtifact astodArtifact = ODBasicsTool
        .parse(Paths.get("src/test/resources/symboltable", odName + ".od").toString());
    ODBasicsArtifactScope odBasicsArtifactScope = ODBasicsTool.createSymbolTable(astodArtifact);
    return odBasicsArtifactScope.getObjectDiagramSymbols().get(odName).get(0);
  }

}
