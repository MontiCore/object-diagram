/*
 * Copyright (c)  RWTH Aachen. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
package de.se_rwth.commons.logging;

import java.util.ArrayList;

/**
 * Resets findings of the current MC-Log
 *
 * @author (last commit) $$Author$$
 * @version $$Revision$$, $$Date$$
 */
public class ODLogReset extends Log {

  public static void resetFindings() {
    getLog().findings = new ArrayList<>();
  }

}
