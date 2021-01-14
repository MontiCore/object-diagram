// (c) https://github.com/MontiCore/monticore

package de.monticore.od4data._symboltable;

import de.monticore.od4data.OD4DataMill;
import de.monticore.od4data.OD4DataTool;
import de.monticore.od4data._parser.OD4DataParser;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.utils.Names;
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

public class OD4DataDeSerTest {

  private final Path SIMPLEOD2 = Paths
      .get("src", "test", "resources", "examples", "od", "SimpleOD2" + ".od");

  private final Path SYMBOL_TARGET = Paths.get("target", "deser");

  @Before
  public void setUp() {
    Slf4jLog.init();
    Log.enableFailQuick(false);

    OD4DataMill.reset();
    OD4DataMill.init();
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
    String pathFromQualifiedName = Names.getPathFromQualifiedName(
        astodArtifact.get().getMCPackageDeclaration().getMCQualifiedName().getQName() + "."
            + astodArtifact.get().getObjectDiagram().getName());
    String storedPath = Paths.get(SYMBOL_TARGET.toString(), pathFromQualifiedName, fileName)
        .toString();
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

}
