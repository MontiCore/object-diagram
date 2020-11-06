// (c) https://github.com/MontiCore/monticore

package de.monticore.od4data;

import de.monticore.io.paths.ModelPath;
import de.monticore.od4data._symboltable.IOD4DataArtifactScope;
import de.monticore.od4data._symboltable.IOD4DataGlobalScope;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.symbols.basicsymbols._symboltable.DiagramSymbol;
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

  private IOD4DataGlobalScope globalScope;

  @BeforeClass
  public static void setup() {
    Slf4jLog.init();
    Slf4jLog.enableFailQuick(false);

    modelPath = new ModelPath(Paths.get("src/test/resources/symboltable"));
  }

  @Test
  public void testResolveODObjectFromFile() {
    final DiagramSymbol objectDiagramSymbol = createObjectDiagramFromAST("AuctionParticipants");

    List<VariableSymbol> odObjects = objectDiagramSymbol.getEnclosingScope()
        .getLocalVariableSymbols();

    assertFalse(odObjects.isEmpty());
    assertEquals(6, odObjects.size());
    for (VariableSymbol obj : odObjects) {
      assertTrue(obj.isPresentAstNode());
    }

    Optional<VariableSymbol> kupferObject = objectDiagramSymbol.getEnclosingScope()
        .resolveVariable("kupfer912");
    assertTrue(kupferObject.isPresent());
  }

  @Test
  public void testResolveODObjectFromAST() {
    final DiagramSymbol objectDiagramSymbol = createObjectDiagramFromAST("AuctionParticipants");

    List<VariableSymbol> odObjects = objectDiagramSymbol.getEnclosingScope()
        .getLocalVariableSymbols();

    assertFalse(odObjects.isEmpty());
    for (VariableSymbol obj : odObjects) {
      assertTrue(obj.isPresentAstNode());
    }

    assertTrue(objectDiagramSymbol.getEnclosingScope().resolveVariable("kupfer912").isPresent());
  }

  @Test
  public void testAvoidanceOfUnnamedObjects() {
    final DiagramSymbol objectDiagramSymbol = createObjectDiagramFromAST("STInnerLinkVariants");

    List<VariableSymbol> odObjects = objectDiagramSymbol.getEnclosingScope()
        .getLocalVariableSymbols();

    assertEquals(5, odObjects.size());
    for (VariableSymbol obj : odObjects) {
      assertTrue(obj.isPresentAstNode());
    }

    Optional<VariableSymbol> fooBarObject = objectDiagramSymbol.getEnclosingScope()
        .resolveVariable("fooBar2");
    assertTrue(fooBarObject.isPresent());
  }

  private DiagramSymbol createObjectDiagramFromAST(String odName) {
    ASTODArtifact astodArtifact = OD4DataTool.parse(
        Paths.get("src/test/resources/symboltable", odName + ".od").toString());
    IOD4DataArtifactScope odBasisArtifactScope = OD4DataTool.createSymbolTable(astodArtifact);
    return odBasisArtifactScope.getDiagramSymbols().get(odName).get(0);
  }

}
