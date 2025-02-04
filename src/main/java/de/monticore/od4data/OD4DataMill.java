/*
 *  (c) https://github.com/MontiCore/monticore
 */

package de.monticore.od4data;

import de.monticore.od4data.types3.OD4DataTypeCheck3;

public class OD4DataMill extends OD4DataMillTOP {
  
  /** additionally inits the TypeCheck */
  public static void init() {
    OD4DataMillTOP.init();
    OD4DataTypeCheck3.init();
  }
}
