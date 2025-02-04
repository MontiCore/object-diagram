/*
 *  (c) https://github.com/MontiCore/monticore
 */

package de.monticore.od4report;

import de.monticore.od4report.types3.OD4ReportTypeCheck3;

public class OD4ReportMill extends OD4ReportMillTOP {
  
  /** additionally inits the TypeCheck */
  public static void init() {
    OD4ReportMillTOP.init();
    OD4ReportTypeCheck3.init();
  }
}
