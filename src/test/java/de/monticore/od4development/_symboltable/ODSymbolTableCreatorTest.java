// (c) https://github.com/MontiCore/monticore

package de.monticore.od4development._symboltable;

import de.monticore.ODTestBasis;
import de.monticore.io.paths.MCPath;
import de.monticore.od4development.OD4DevelopmentTestUtil;
import de.monticore.od4development.OD4DevelopmentMill;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.runtime.junit.TestWithMCLanguage;
import de.monticore.symbols.basicsymbols._symboltable.DiagramSymbol;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestWithMCLanguage(OD4DevelopmentMill.class)
public class ODSymbolTableCreatorTest extends ODTestBasis {

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
    Path odPath = PATH.resolve(Paths.get("symboltable", odName + ".od"));
    ASTODArtifact artifact = OD4DevelopmentTestUtil.loadModel(odPath, new MCPath(PATH));
    IOD4DevelopmentArtifactScope odBasisArtifactScope = OD4DevelopmentMill.scopesGenitorDelegator().createFromAST(artifact);
    return odBasisArtifactScope.getDiagramSymbols().get(odName).getFirst();
  }

}
