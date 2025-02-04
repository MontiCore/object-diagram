/*
 *  (c) https://github.com/MontiCore/monticore
 */

package de.monticore.odbasis;

import de.monticore.odbasis.types3.ODBasisTypeCheck3;

public class ODBasisMill extends ODBasisMillTOP {
  
  /** additionally inits the TypeCheck */
  public static void init() {
    ODBasisMillTOP.init();
    ODBasisTypeCheck3.init();
  }
}
