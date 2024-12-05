/* (c) https://github.com/MontiCore/monticore */
package de.monticore.od4development;

import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.LogStub;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class OD4DevelopmentToolTest {

  @BeforeEach
  public void before() {
    LogStub.init();
    Log.enableFailQuick(false);
    OD4DevelopmentMill.reset();
    OD4DevelopmentMill.init();
  }

  @Test
  public void testAddSymtabFile() {
    assertTrue(OD4DevelopmentMill.globalScope().getSymbolPath().isEmpty());
    OD4DevelopmentToolTOP.main(new String[] {"-i","src/test/resources/examples/od2cd/Example.od", "-path", "src/test/resources/symboltable/tooltest/"});
    assertTrue(OD4DevelopmentMill.globalScope().getSymbolPath().toString().endsWith("resources/symboltable/tooltest/]"));
  }
  
  @Test
  public void testStoreSymtabFile() {
    OD4DevelopmentToolTOP.main(new String[] {"-i","src/test/resources/examples/od2cd/Example.od", "-path", "src/test/resources/symboltable/tooltest/", "-s", "target/generated-test-sources/tooltest/symboltable/"});
    File symTab = new File("target/generated-test-sources/tooltest/symboltable/examples/od2cd/Examples.odsym");
    assertTrue(symTab.exists() && symTab.isFile());
  }

}
