/* (c) https://github.com/MontiCore/monticore */
package de.monticore.od4development;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class OD4DevelopmentToolTest {

  @Test
  public void testTool() {
    OD4DevelopmentToolTOP.main(new String[] {"-i","src/test/resources/examples/od/SimpleOD.od"});
    assertTrue(!false);
  }

}
