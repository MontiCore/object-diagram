// (c) https://github.com/MontiCore/monticore

package de.monticore.od4data._symboltable;

import de.monticore.od4data.OD4DataMill;
import de.monticore.od4data.OD4DataToolAPI;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.symbols.basicsymbols._symboltable.DiagramSymbol;
import de.monticore.symbols.basicsymbols._symboltable.TypeSymbol;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.LogStub;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class ODSymbolTableCreatorTest {
  
  @Before
  public void setup() {
    LogStub.init();
    Log.enableFailQuick(false);
    
    OD4DataMill.reset();
    OD4DataMill.init();
    IOD4DataGlobalScope gs = OD4DataMill.globalScope();
    
    TypeSymbol auction = OD4DataMill.typeSymbolBuilder().setName("Auction").setEnclosingScope(gs)
        .setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol person = OD4DataMill.typeSymbolBuilder().setName("Person").setEnclosingScope(gs)
        .setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol string = OD4DataMill.typeSymbolBuilder().setName("String").setEnclosingScope(gs)
        .setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol biddingPolicy =
        OD4DataMill.typeSymbolBuilder().setName("BiddingPolicy").setEnclosingScope(gs)
            .setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol timingPolicy =
        OD4DataMill.typeSymbolBuilder().setName("TimingPolicy").setEnclosingScope(gs)
            .setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol objectType =
        OD4DataMill.typeSymbolBuilder().setName("ObjectType").setEnclosingScope(gs)
            .setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol t1 = OD4DataMill.typeSymbolBuilder().setName("T1").setEnclosingScope(gs)
        .setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol t2 = OD4DataMill.typeSymbolBuilder().setName("T2").setEnclosingScope(gs)
        .setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol t3 = OD4DataMill.typeSymbolBuilder().setName("T3").setEnclosingScope(gs)
        .setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol typeOfObject =
        OD4DataMill.typeSymbolBuilder().setName("TypeOfObject").setEnclosingScope(gs)
            .setSpannedScope(OD4DataMill.scope()).build();
    gs.add(auction);
    gs.add(person);
    gs.add(string);
    gs.add(biddingPolicy);
    gs.add(timingPolicy);
    gs.add(objectType);
    gs.add(t1);
    gs.add(t2);
    gs.add(t3);
    gs.add(typeOfObject);
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
    ASTODArtifact astodArtifact = OD4DataToolAPI.parse(
        Paths.get("src/test/resources/symboltable", odName + ".od").toString());
    IOD4DataArtifactScope odBasisArtifactScope = OD4DataToolAPI.createSymbolTable(astodArtifact);
    return odBasisArtifactScope.getDiagramSymbols().get(odName).get(0);
  }
  
}
