/* (c) https://github.com/MontiCore/monticore */

package de.monticore.od4report._symboltable;

import de.monticore.ODTestBasis;
import de.monticore.io.paths.MCPath;
import de.monticore.od4report.OD4ReportMill;
import de.monticore.od4report.OD4ReportTestUtil;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.odbasis._ast.ASTODNamedObject;
import de.monticore.runtime.junit.TestWithMCLanguage;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestWithMCLanguage(OD4ReportMill.class)
public class OD4ReportSymbolTableCreatorTest extends ODTestBasis {

  private final Path INPUT_DIR = PATH.resolve("symboltable");

  /** Each object of the diagram gets a symbol with its AST node and the type from the CD. */
  @Test
  public void testOD4ReportSymbolTableCreator() {
    ASTODArtifact artifact =
        OD4ReportTestUtil.loadModel(INPUT_DIR.resolve("AuctionParticipants.od"), new MCPath(PATH));

    IOD4ReportArtifactScope symbolTable = OD4ReportTestUtil.createSymbolTableFromAST(artifact);

    assertEquals("AuctionParticipants", symbolTable.getName());
    assertEquals("symboltable", symbolTable.getPackageName());

    Map<String, String> expectedTypes = Map.of(
        "kupfer912", "examples.cd.Auction.Auction",
        "theo", "examples.cd.Auction.Person",
        "otto", "examples.cd.Auction.Person",
        "lisa", "examples.cd.Auction.Person",
        "bp", "examples.cd.Auction.BiddingPolicy",
        "tp", "examples.cd.Auction.TimingPolicy");
    assertEquals(expectedTypes.size(), symbolTable.getLocalVariableSymbols().size());

    expectedTypes.forEach((name, type) -> {
      VariableSymbol symbol = symbolTable.resolveVariable("symboltable.AuctionParticipants." + name)
          .orElseGet(() -> fail("No symbol for object " + name));
      assertSame(symbolTable, symbol.getEnclosingScope(), name);
      assertEquals(name, assertInstanceOf(ASTODNamedObject.class, symbol.getAstNode()).getName());
      assertEquals(type, symbol.getType().printFullName(), name);
    });
  }

  /** Objects of other diagrams are resolved via the symbol path from stored symbol tables. */
  @Test
  public void testResolveObjectOfOtherDiagram() {
    ASTODArtifact artifact =
        OD4ReportTestUtil.loadModel(INPUT_DIR.resolve("AuctionParticipants.od"), new MCPath(PATH));
    IOD4ReportArtifactScope symbolTable = OD4ReportTestUtil.createSymbolTableFromAST(artifact);

    VariableSymbol alice = symbolTable.resolveVariable("symboltable.symbols.MyFamily.alice")
        .orElseGet(() -> fail("Object alice of diagram MyFamily not resolved"));
    assertNotSame(symbolTable, alice.getEnclosingScope());
  }
}
