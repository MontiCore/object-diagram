// (c) https://github.com/MontiCore/monticore

package de.monticore.lang.od4report.cocos;

import de.monticore.lang.od4report._cocos.OD4ReportCoCoChecker;
import de.monticore.lang.odbasics.cocos.ODBasicsCoCos;

public class OD4ReportCoCos {

  public OD4ReportCoCoChecker getCheckerForAllCoCos() {
    final OD4ReportCoCoChecker checker = new OD4ReportCoCoChecker();
    checker.addChecker(new ODBasicsCoCos().getCheckerForAllCoCos());
    // TODO add Cocos

    return checker;
  }

}
