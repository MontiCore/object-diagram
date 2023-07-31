/* (c) https://github.com/MontiCore/monticore */
package de.monticore.od4development;

import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.LogStub;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

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
    OD4DevelopmentToolTOP.main(
        new String[] { "-i", "src/test/resources/examples/od2cd/Example.od", "-s",
            "resources/symboltable/tooltest/" });
    assertTrue(OD4DevelopmentMill.globalScope().getSymbolPath().toString()
        .endsWith("resources/symboltable/tooltest]"));
  }
  
}
