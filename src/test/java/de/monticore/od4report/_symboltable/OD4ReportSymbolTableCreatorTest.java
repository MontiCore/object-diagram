/* (c) https://github.com/MontiCore/monticore */

package de.monticore.od4report._symboltable;

import de.monticore.io.paths.ModelPath;
import de.monticore.od4report.OD4ReportMill;
import de.monticore.od4report.OD4ReportTool;
import de.monticore.od4report._parser.OD4ReportParser;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.symbols.basicsymbols._symboltable.TypeSymbol;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.se_rwth.commons.logging.Log;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

public class OD4ReportSymbolTableCreatorTest {

  private ModelPath modelPath = new ModelPath(Paths.get("src", "test", "resources"));

  private final Path INPUTFOLDER = Paths.get("src", "test", "resources", "symboltable");

  @Test
  public void testOD4ReportSymbolTableCreator() throws IOException {
    OD4ReportParser parser = new OD4ReportParser();
    Optional<ASTODArtifact> artifact = parser.parse(
        Paths.get(INPUTFOLDER.toString(), "AuctionParticipants.od").toString());
    assertTrue(artifact.isPresent());

    IOD4ReportArtifactScope symbolTable = OD4ReportTool.createSymbolTable(artifact.get());
    //TODO uncomment when ImportStatements are fixed
//    Optional<VariableSymbol> person = symbolTable.resolveVariable(
//        "symboltable.symbols.MyFamily.alice");
//    assertTrue(person.isPresent());
  }

  @Before
  public void setUp() {
    Log.enableFailQuick(false);

    OD4ReportMill.reset();
    OD4ReportMill.init();
    OD4ReportMill.globalScope().setModelPath(modelPath);
    OD4ReportMill.globalScope().clear();
    IOD4ReportGlobalScope gs = OD4ReportMill.globalScope();

    TypeSymbol auction = OD4ReportMill.typeSymbolBuilder().setName("Auction").setEnclosingScope(gs).setSpannedScope(OD4ReportMill.scope()).build();
    TypeSymbol person = OD4ReportMill.typeSymbolBuilder().setName("Person").setEnclosingScope(gs).setSpannedScope(OD4ReportMill.scope()).build();
    TypeSymbol string = OD4ReportMill.typeSymbolBuilder().setName("String").setEnclosingScope(gs).setSpannedScope(OD4ReportMill.scope()).build();
    TypeSymbol biddingPolicy = OD4ReportMill.typeSymbolBuilder().setName("BiddingPolicy").setEnclosingScope(gs).setSpannedScope(OD4ReportMill.scope()).build();
    TypeSymbol timingPolicy = OD4ReportMill.typeSymbolBuilder().setName("TimingPolicy").setEnclosingScope(gs).setSpannedScope(OD4ReportMill.scope()).build();
    gs.add(auction);
    gs.add(person);
    gs.add(string);
    gs.add(biddingPolicy);
    gs.add(timingPolicy);
  }

}
