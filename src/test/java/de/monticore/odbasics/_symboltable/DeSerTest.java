// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

package de.monticore.odbasics._symboltable;

import de.monticore.odbasics.ODBasicsTool;
import de.monticore.odbasics._ast.ASTODArtifact;
import de.monticore.odbasics._parser.ODBasicsParser;
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
    ODBasicsParser odBasicsParser = new ODBasicsParser();
    Optional<ASTODArtifact> astodArtifact = odBasicsParser.parse(TEASEROD.toString());
    assertTrue(astodArtifact.isPresent());

    ODBasicsArtifactScope odBasicsArtifactScope = ODBasicsTool
        .createSymbolTable(astodArtifact.get());

    // deserialize
    ODBasicsScopeDeSer odBasicsScopeDeSer = new ODBasicsScopeDeSer();
    odBasicsScopeDeSer.setSymbolFileExtension(EXTENSION);
    odBasicsScopeDeSer.store(odBasicsArtifactScope, SYMBOL_TARGET);

    Path storedSymTable = Paths.get(SYMBOL_TARGET.toString(),
        astodArtifact.get().getObjectDiagram().getName() + "." + EXTENSION);
    assertTrue(storedSymTable.toFile().exists());

    // serialize
    ODBasicsArtifactScope loadedBasicsArtifactScope = odBasicsScopeDeSer
        .load(storedSymTable.toString());
    odBasicsScopeDeSer = new ODBasicsScopeDeSer();
    String s1 = odBasicsScopeDeSer.serialize(odBasicsArtifactScope);
    odBasicsScopeDeSer = new ODBasicsScopeDeSer();
    String s2 = odBasicsScopeDeSer.serialize(odBasicsArtifactScope);
    assertEquals(s1, s2);

//    assertEquals(odBasicsScopeDeSer.serialize(odBasicsArtifactScope),
//        odBasicsScopeDeSer.serialize(loadedBasicsArtifactScope));
  }

}
