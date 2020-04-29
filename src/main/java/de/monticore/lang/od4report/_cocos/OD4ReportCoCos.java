// (c) https://github.com/MontiCore/monticore

package de.monticore.lang.od4report._cocos;

import de.monticore.lang.dateliterals._cocos.DateLiteralsCoCos;
import de.monticore.lang.odbasics._cocos.ODBasicsCoCos;

public class OD4ReportCoCos {

  public OD4ReportCoCoChecker getCheckerForAllCoCos() {
    final OD4ReportCoCoChecker checker = new OD4ReportCoCoChecker();
    checker.addChecker(new ODBasicsCoCos().getCheckerForAllCoCos());
    checker.addChecker(new DateLiteralsCoCos().getCheckerForAllCoCos());

    return checker;
  }

}
