/*
 * Copyright (c) 2017, MontiCore. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
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
