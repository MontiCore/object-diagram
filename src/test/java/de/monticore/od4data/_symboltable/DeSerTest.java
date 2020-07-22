// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

package de.monticore.od4data._symboltable;

import de.monticore.od4data.OD4DataTool;
import de.monticore.od4data._parser.OD4DataParser;
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

  private final Path SYMBOL_TARGET = Paths.get("target", "deser");

  private final String EXTENSION = "odsym";

  @BeforeClass
  public static void disableFailQuick() {
    Slf4jLog.init();
    Log.enableFailQuick(false);
  }

  @Test
  public void testODDeSer() throws IOException {
    OD4DataParser odBasicsParser = new OD4DataParser();
    Optional<ASTODArtifact> astodArtifact = odBasicsParser.parse(TEASEROD.toString());
    assertTrue(astodArtifact.isPresent());

    OD4DataArtifactScope od4DataArtifactScope = OD4DataTool
        .createSymbolTable(astodArtifact.get());

    // deserialize
    OD4DataScopeDeSer odBasicsScopeDeSer = new OD4DataScopeDeSer();
    odBasicsScopeDeSer.setSymbolFileExtension(EXTENSION);
    odBasicsScopeDeSer.store(od4DataArtifactScope, SYMBOL_TARGET);

    Path storedSymTable = Paths.get(SYMBOL_TARGET.toString(),
        astodArtifact.get().getObjectDiagram().getName() + "." + EXTENSION);
    assertTrue(storedSymTable.toFile().exists());

    // serialize
    OD4DataArtifactScope loadedBasicsArtifactScope = odBasicsScopeDeSer
        .load(storedSymTable.toString());
    odBasicsScopeDeSer = new OD4DataScopeDeSer();
    String s1 = odBasicsScopeDeSer.serialize(loadedBasicsArtifactScope);
    odBasicsScopeDeSer = new OD4DataScopeDeSer();
    String s2 = odBasicsScopeDeSer.serialize(od4DataArtifactScope);
    assertEquals(s1, s2);

//    assertEquals(odBasicsScopeDeSer.serialize(odBasicsArtifactScope),
//        odBasicsScopeDeSer.serialize(loadedBasicsArtifactScope));
  }

}
