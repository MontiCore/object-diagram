// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

package de.monticore.od4data._symboltable;

import de.monticore.od4data.OD4DataTool;
import de.monticore.od4data._parser.OD4DataParser;
import de.monticore.od4report.OD4ReportTool;
import de.monticore.od4report._parser.OD4ReportParser;
import de.monticore.od4report._symboltable.IOD4ReportArtifactScope;
import de.monticore.od4report._symboltable.OD4ReportScopeDeSer;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.Slf4jLog;
import org.junit.BeforeClass;
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

  private final Path EXAMPLESEOD = Paths
      .get("src", "test", "resources", "examples", "od", "Examples" + ".od");

  private final Path SYMBOL_TARGET = Paths.get("target", "deser");

  private final String EXTENSION = "odsym";

  @BeforeClass
  public static void disableFailQuick() {
    Slf4jLog.init();
    Log.enableFailQuick(false);
  }

  @Test
  public void testOD4DataDeSer() throws IOException {
    OD4DataParser odBasicsParser = new OD4DataParser();
    Optional<ASTODArtifact> astodArtifact = odBasicsParser.parse(TEASEROD.toString());
    assertTrue(astodArtifact.isPresent());

    IOD4DataArtifactScope od4DataArtifactScope = OD4DataTool.createSymbolTable(astodArtifact.get());

    // serialize
    OD4DataScopeDeSer odBasicsScopeDeSer = new OD4DataScopeDeSer();
    odBasicsScopeDeSer.setSymbolFileExtension(EXTENSION);
    odBasicsScopeDeSer.store(od4DataArtifactScope, SYMBOL_TARGET);

    Path storedSymTable = Paths.get(SYMBOL_TARGET.toString(),
        astodArtifact.get().getObjectDiagram().getName() + "." + EXTENSION);
    assertTrue(storedSymTable.toFile().exists());

    // deserialize
    IOD4DataArtifactScope loadedBasicsArtifactScope = odBasicsScopeDeSer
        .load(storedSymTable.toString());

    assertEquals(odBasicsScopeDeSer.serialize(od4DataArtifactScope),
        odBasicsScopeDeSer.serialize(loadedBasicsArtifactScope));
  }

  @Test
  public void testOD4ReportDeSer() throws IOException {
    OD4ReportParser od4ReportParser = new OD4ReportParser();
    Optional<ASTODArtifact> astodArtifact = od4ReportParser.parse(EXAMPLESEOD.toString());
    assertTrue(astodArtifact.isPresent());

    IOD4ReportArtifactScope od4ReportArtifactScope = OD4ReportTool
        .createSymbolTable(astodArtifact.get());

    // serialize
    OD4ReportScopeDeSer od4ReportScopeDeSer = new OD4ReportScopeDeSer();
    od4ReportScopeDeSer.setSymbolFileExtension(EXTENSION);
    od4ReportScopeDeSer.store(od4ReportArtifactScope, SYMBOL_TARGET);

    Path storedSymTable = Paths.get(SYMBOL_TARGET.toString(),
        astodArtifact.get().getObjectDiagram().getName() + "." + EXTENSION);
    assertTrue(storedSymTable.toFile().exists());

    // deserialize
    IOD4ReportArtifactScope loadedBasicsArtifactScope = od4ReportScopeDeSer
        .load(storedSymTable.toString());

    assertEquals(od4ReportScopeDeSer.serialize(od4ReportArtifactScope),
        od4ReportScopeDeSer.serialize(loadedBasicsArtifactScope));
  }

}
