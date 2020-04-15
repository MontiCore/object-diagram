/* (c) https://github.com/MontiCore/monticore */
package de.se_rwth.commons.logging;

import java.util.ArrayList;

/**
 * Resets findings of the current MC-Log
 *
 */
public class ODLogReset extends Log {

  public static void resetFindings() {
    getLog().findings = new ArrayList<>();
  }

}
