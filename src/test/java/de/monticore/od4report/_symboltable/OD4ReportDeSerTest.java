// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report._symboltable;

import de.monticore.od4data.OD4DataMill;
import de.monticore.od4report.OD4ReportMill;
import de.monticore.od4report.OD4ReportTool;
import de.monticore.od4report._parser.OD4ReportParser;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.symbols.basicsymbols._symboltable.TypeSymbol;
import de.monticore.utils.Names;
import de.se_rwth.commons.logging.Log;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OD4ReportDeSerTest {

  private final Path TEASEROD = Paths.get("src", "test", "resources", "examples", "od",
      "MyFamily.od");

  private final Path SYMBOL_TARGET = Paths.get("target", "deser");

  @Before
  public void disableFailQuick() {
    Log.init();
    Log.enableFailQuick(false);

    OD4ReportMill.reset();
    OD4ReportMill.init();
    OD4ReportMill.globalScope().clear();
    IOD4ReportGlobalScope gs = OD4ReportMill.globalScope();

    //TODO remove after ImportStatements fix
    TypeSymbol person = OD4ReportMill.typeSymbolBuilder().setName("Person").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol bmw = OD4ReportMill.typeSymbolBuilder().setName("BMW").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol jaguar = OD4ReportMill.typeSymbolBuilder().setName("Jaguar").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    gs.add(person);
    gs.add(bmw);
    gs.add(jaguar);
  }

  @Test
  public void testOD4ReportDeSer() throws IOException {
    OD4ReportMill.init();
    OD4ReportParser od4ReportParser = new OD4ReportParser();
    Optional<ASTODArtifact> astodArtifact = od4ReportParser.parse(TEASEROD.toString());
    assertTrue(astodArtifact.isPresent());

    IOD4ReportArtifactScope od4ReportArtifactScope = OD4ReportTool.createSymbolTable(
        astodArtifact.get(), true);

    // serialize
    OD4ReportSymbols2Json od4ReportSymbols2Json = new OD4ReportSymbols2Json();
    String fileName = Paths.get(TEASEROD.toString()).getFileName().toString() + "sym";
    String pathFromQualifiedName = Names.getPathFromQualifiedName(
        astodArtifact.get().getMCPackageDeclaration().getMCQualifiedName().getQName() + "."
            + astodArtifact.get().getObjectDiagram().getName());
    String storedPath = Paths.get(SYMBOL_TARGET.toString(), pathFromQualifiedName, fileName)
        .toString();
    od4ReportSymbols2Json.store(od4ReportArtifactScope, storedPath);

    Path storedSymTable = Paths.get(storedPath);
    assertTrue(storedSymTable.toFile().exists());

    // deserialize
    IOD4ReportArtifactScope loadedBasicsArtifactScope = od4ReportSymbols2Json.load(
        storedSymTable.toString());

    assertEquals(od4ReportSymbols2Json.serialize(od4ReportArtifactScope),
        od4ReportSymbols2Json.serialize(loadedBasicsArtifactScope));
  }

}
