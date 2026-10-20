// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report._cocos;

import de.monticore.dateliterals._cocos.DateLiteralsCoCos;
import de.monticore.od4development._cocos.link.ValidLinkQualifierCoCo;
import de.monticore.odbasis._cocos.ODBasisCoCos;
import de.monticore.odlink._cocos.ODLinkCoCos;

public class OD4ReportCoCos {

  public OD4ReportCoCoChecker getCheckerForAllIntraCoCos() {
    final OD4ReportCoCoChecker checker = new OD4ReportCoCoChecker();
    checker.addChecker(new ODBasisCoCos().getCheckerForAllIntraCoCos());
    checker.addChecker(new ODLinkCoCos().getCheckerForAllIntraCoCos());
    checker.addCoCo(new ValidLinkQualifierCoCo());
    checker.addChecker(new DateLiteralsCoCos().getCheckerForAllCoCos());

    return checker;
  }

  public OD4ReportCoCoChecker getCheckerForAllCoCos() {
    final OD4ReportCoCoChecker checker = new OD4ReportCoCoChecker();
    checker.addChecker(getCheckerForAllIntraCoCos());
    checker.addChecker(getCheckerForAllInterCoCos());

    return checker;
  }

  public OD4ReportCoCoChecker getCheckerForAllInterCoCos() {
    final OD4ReportCoCoChecker checker = new OD4ReportCoCoChecker();
    checker.addChecker(new ODBasisCoCos().getCheckerForAllInterCoCos());
    checker.addChecker(new ODLinkCoCos().getCheckerForAllInterCoCos());

    return checker;
  }

}
