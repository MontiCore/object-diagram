/* (c) https://github.com/MontiCore/monticore */
package de.se_rwth.commons.logging;

import org.junit.Before;

import java.util.ArrayList;

/**
 * Resets findings of the current MC-Log
 */
public class ODLogReset extends Log {
  
  @Before
  public void setup() {
    LogStub.init();
    Log.enableFailQuick(false);
  }
  
  public static void resetFindings() {
    getLog().findings = new ArrayList<>();
  }
  
}
