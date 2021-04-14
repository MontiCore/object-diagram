// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report._cocos;

import de.monticore.dateliterals._cocos.DateLiteralsCoCos;
import de.monticore.odbasis._cocos.ODBasicsCoCos;
import de.monticore.odlink._cocos.ODLinkCoCos;

public class OD4ReportCoCos {

  public OD4ReportCoCoChecker getCheckerForAllIntraCoCos() {
    final OD4ReportCoCoChecker checker = new OD4ReportCoCoChecker();
    checker.addChecker(new ODBasicsCoCos().getCheckerForAllIntraCoCos());
    checker.addChecker(new ODLinkCoCos().getCheckerForAllCoCos());
    checker.addChecker(new DateLiteralsCoCos().getCheckerForAllCoCos());

    return checker;
  }

  public OD4ReportCoCoChecker getCheckerForAllCoCos() {
    final OD4ReportCoCoChecker checker = new OD4ReportCoCoChecker();
    checker.addChecker(new ODBasicsCoCos().getCheckerForAllCoCos());
    checker.addChecker(new ODLinkCoCos().getCheckerForAllCoCos());
    checker.addChecker(new DateLiteralsCoCos().getCheckerForAllCoCos());

    return checker;
  }

}
