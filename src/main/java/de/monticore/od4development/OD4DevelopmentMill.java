/*
 *  (c) https://github.com/MontiCore/monticore
 */

package de.monticore.od4development;

import de.monticore.od4development.types3.OD4DevelopmentTypeCheck3;

public class OD4DevelopmentMill extends OD4DevelopmentMillTOP {
  
  /** additionally inits the TypeCheck */
  public static void init() {
    OD4DevelopmentMillTOP.init();
    OD4DevelopmentTypeCheck3.init();
  }
}
