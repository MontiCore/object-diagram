// (c) https://github.com/MontiCore/monticore

package de.monticore.od4data._cocos;

import de.monticore.od4report._cocos.OD4ReportCoCoChecker;
import de.monticore.odbasis._cocos.ODBasicsCoCos;
import de.monticore.odlink._cocos.ODLinkCoCos;

public class OD4DataCoCos {

  public OD4ReportCoCoChecker getCheckerForAllIntraCoCos() {
    final OD4ReportCoCoChecker checker = new OD4ReportCoCoChecker();
    checker.addChecker(new ODBasicsCoCos().getCheckerForAllIntraCoCos());
    checker.addChecker(new ODLinkCoCos().getCheckerForAllIntraCoCos());

    return checker;
  }

  public OD4ReportCoCoChecker getCheckerForAllCoCos() {
    final OD4ReportCoCoChecker checker = new OD4ReportCoCoChecker();
    checker.addChecker(new ODBasicsCoCos().getCheckerForAllCoCos());
    checker.addChecker(new ODLinkCoCos().getCheckerForAllCoCos());

    return checker;
  }

}
