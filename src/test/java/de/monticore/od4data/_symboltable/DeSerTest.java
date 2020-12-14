// (c) https://github.com/MontiCore/monticore

package de.monticore.od4data._symboltable;

import de.monticore.od4data.OD4DataMill;
import de.monticore.od4data.OD4DataTool;
import de.monticore.od4data._parser.OD4DataParser;
import de.monticore.od4report.OD4ReportMill;
import de.monticore.od4report.OD4ReportTool;
import de.monticore.od4report._parser.OD4ReportParser;
import de.monticore.od4report._symboltable.IOD4ReportArtifactScope;
import de.monticore.od4report._symboltable.OD4ReportScopeDeSer;
import de.monticore.od4report._symboltable.OD4ReportSymbols2Json;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.Slf4jLog;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DeSerTest {

  private final Path TEASEROD = Paths
      .get("src", "test", "resources", "examples", "od", "Teaser" + ".od");

  private final Path SIMPLEOD2 = Paths
      .get("src", "test", "resources", "examples", "od", "SimpleOD2" + ".od");

  private final Path EXAMPLESEOD = Paths
      .get("src", "test", "resources", "examples", "od", "Examples" + ".od");

  private final Path SYMBOL_TARGET = Paths.get("target", "deser");

  @Before
  public void disableFailQuick() {
    Slf4jLog.init();
    Log.enableFailQuick(false);
  }

  @Test
  public void testOD4DataDeSer() throws IOException {
    OD4DataMill.init();
    OD4DataParser od4DataParser = new OD4DataParser();
    Optional<ASTODArtifact> astodArtifact = od4DataParser.parse(SIMPLEOD2.toString());
    assertTrue(astodArtifact.isPresent());

    IOD4DataArtifactScope od4DataArtifactScope = OD4DataTool.createSymbolTable(astodArtifact.get());

    // serialize
    OD4DataSymbols2Json od4DataSymbols2Json = new OD4DataSymbols2Json();
    String fileName = Paths.get(SIMPLEOD2.toString()).getFileName().toString() + "sym";
    String packagePath = astodArtifact.get().getMCPackageDeclaration().getMCQualifiedName().getQName();
    String storedPath = Paths.get(SYMBOL_TARGET.toString(), packagePath, fileName).toString();
    od4DataSymbols2Json.store(od4DataArtifactScope, storedPath);

    Path storedSymTable = Paths.get(storedPath);
    assertTrue(storedSymTable.toFile().exists());

    // deserialize
    IOD4DataArtifactScope loadedBasicsArtifactScope = od4DataSymbols2Json
        .load(storedSymTable.toString());

    OD4DataScopeDeSer odBasicsScopeDeSer = new OD4DataScopeDeSer();
    assertEquals(odBasicsScopeDeSer.serialize(od4DataArtifactScope),
        odBasicsScopeDeSer.serialize(loadedBasicsArtifactScope));
  }

  @Test
  public void testOD4ReportDeSer() throws IOException {
    OD4ReportMill.init();
    OD4ReportParser od4ReportParser = new OD4ReportParser();
    Optional<ASTODArtifact> astodArtifact = od4ReportParser.parse(EXAMPLESEOD.toString());
    assertTrue(astodArtifact.isPresent());

    IOD4ReportArtifactScope od4ReportArtifactScope = OD4ReportTool
        .createSymbolTable(astodArtifact.get());

    // serialize
    OD4ReportSymbols2Json od4ReportSymbols2Json = new OD4ReportSymbols2Json();
    String fileName = Paths.get(TEASEROD.toString()).getFileName().toString() + "sym";
    String packagePath = astodArtifact.get().getMCPackageDeclaration().getMCQualifiedName().getQName();
    String storedPath = Paths.get(SYMBOL_TARGET.toString(), packagePath, fileName).toString();
    od4ReportSymbols2Json.store(od4ReportArtifactScope, storedPath);

    Path storedSymTable = Paths.get(storedPath);
    assertTrue(storedSymTable.toFile().exists());

    // deserialize
    IOD4ReportArtifactScope loadedBasicsArtifactScope = od4ReportSymbols2Json
        .load(storedSymTable.toString());

    OD4ReportScopeDeSer od4ReportScopeDeSer = new OD4ReportScopeDeSer();
    assertEquals(od4ReportScopeDeSer.serialize(od4ReportArtifactScope),
        od4ReportScopeDeSer.serialize(loadedBasicsArtifactScope));
  }

}
