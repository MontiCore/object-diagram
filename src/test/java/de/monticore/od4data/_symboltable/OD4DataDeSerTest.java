// (c) https://github.com/MontiCore/monticore

package de.monticore.od4data._symboltable;

import de.monticore.od4data.OD4DataMill;
import de.monticore.od4data.OD4DataToolAPI;
import de.monticore.od4data._parser.OD4DataParser;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.symbols.basicsymbols._symboltable.TypeSymbol;
import de.se_rwth.commons.Names;
import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.LogStub;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OD4DataDeSerTest {

  private final Path SIMPLEOD2 = Paths.get("src", "test", "resources", "examples", "od",
      "SimpleOD2" + ".od");

  private final Path SYMBOL_TARGET = Paths.get("target", "deser");

  @Before
  public void setUp() {
    LogStub.init();
    Log.enableFailQuick(false);

    OD4DataMill.reset();
    OD4DataMill.init();
    OD4DataMill.globalScope().clear();
    IOD4DataGlobalScope gs = OD4DataMill.globalScope();

    TypeSymbol objectType = OD4DataMill.typeSymbolBuilder()
        .setName("ObjectType")
        .setEnclosingScope(gs)
        .setSpannedScope(OD4DataMill.scope())
        .build();
    TypeSymbol objectType2 = OD4DataMill.typeSymbolBuilder()
        .setName("ObjectType2")
        .setEnclosingScope(gs)
        .setSpannedScope(OD4DataMill.scope())
        .build();
    TypeSymbol t1 = OD4DataMill.typeSymbolBuilder()
        .setName("T1")
        .setEnclosingScope(gs)
        .setSpannedScope(OD4DataMill.scope())
        .build();
    TypeSymbol t2 = OD4DataMill.typeSymbolBuilder()
        .setName("T2")
        .setEnclosingScope(gs)
        .setSpannedScope(OD4DataMill.scope())
        .build();
    TypeSymbol t3 = OD4DataMill.typeSymbolBuilder()
        .setName("T3")
        .setEnclosingScope(gs)
        .setSpannedScope(OD4DataMill.scope())
        .build();
    gs.add(objectType);
    gs.add(objectType2);
    gs.add(t1);
    gs.add(t2);
    gs.add(t3);
  }

  @Test
  public void testOD4DataDeSer() throws IOException {
    OD4DataParser od4DataParser = new OD4DataParser();
    Optional<ASTODArtifact> astodArtifact = od4DataParser.parse(SIMPLEOD2.toString());
    assertTrue(astodArtifact.isPresent());

    IOD4DataArtifactScope od4DataArtifactScope = OD4DataToolAPI.createSymbolTable(astodArtifact.get(),
        true);

    // serialize
    OD4DataSymbols2Json od4DataSymbols2Json = new OD4DataSymbols2Json();
    String fileName = Paths.get(SIMPLEOD2.toString()).getFileName().toString() + "sym";
    String pathFromQualifiedName = Names.getPathFromQualifiedName(
        astodArtifact.get().getMCPackageDeclaration().getMCQualifiedName().getQName() + "."
            + astodArtifact.get().getObjectDiagram().getName());
    String storedPath = Paths.get(SYMBOL_TARGET.toString(), pathFromQualifiedName, fileName)
        .toString();
    od4DataSymbols2Json.store(od4DataArtifactScope, storedPath);

    Path storedSymTable = Paths.get(storedPath);
    assertTrue(storedSymTable.toFile().exists());

    // deserialize
    IOD4DataArtifactScope loadedBasicsArtifactScope = od4DataSymbols2Json.load(
        storedSymTable.toString());

    // clear buffer of traverser, as elements should be traversed again
    od4DataSymbols2Json.getTraverser().clearTraversedElements();

    assertEquals(od4DataSymbols2Json.serialize(od4DataArtifactScope),
        od4DataSymbols2Json.serialize(loadedBasicsArtifactScope));
  }

}
