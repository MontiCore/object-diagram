/*
 *  (c) https://github.com/MontiCore/monticore
 */

package de.monticore.odlink;

import de.monticore.odlink.types3.ODLinkTypeCheck3;

public class ODLinkMill extends ODLinkMillTOP {
  
  /** additionally inits the TypeCheck */
  public static void init() {
    ODLinkMillTOP.init();
    ODLinkTypeCheck3.init();
  }
}
