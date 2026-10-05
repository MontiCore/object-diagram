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
    checker.addChecker(new ODBasisCoCos().getCheckerForAllCoCos());
    checker.addChecker(new ODLinkCoCos().getCheckerForAllCoCos());
    checker.addCoCo(new ValidLinkQualifierCoCo());
    checker.addChecker(new DateLiteralsCoCos().getCheckerForAllCoCos());

    return checker;
  }

}
