/* (c) https://github.com/MontiCore/monticore */
package de.monticore.od4development._cocos;

import de.monticore.od4development._cocos.link.ValidLinkQualifierCoCo;
import de.monticore.odbasis._cocos.ODBasisCoCos;
import de.monticore.odlink._cocos.ODLinkCoCos;

public class OD4DevelopmentCoCos {
  
  public OD4DevelopmentCoCoChecker getCheckerForAllIntraCoCos() {
    final OD4DevelopmentCoCoChecker checker = new OD4DevelopmentCoCoChecker();
    checker.addChecker(new ODBasisCoCos().getCheckerForAllIntraCoCos());
    checker.addChecker(new ODLinkCoCos().getCheckerForAllIntraCoCos());
    checker.addCoCo(new ValidLinkQualifierCoCo());

    return checker;
  }

  public OD4DevelopmentCoCoChecker getCheckerForAllCoCos() {
    final OD4DevelopmentCoCoChecker checker = new OD4DevelopmentCoCoChecker();
    checker.addChecker(new ODBasisCoCos().getCheckerForAllCoCos());
    checker.addChecker(new ODLinkCoCos().getCheckerForAllCoCos());
    checker.addCoCo(new ValidLinkQualifierCoCo());

    return checker;
  }

  public OD4DevelopmentCoCoChecker getCheckerForAllInterCoCos() {
    final OD4DevelopmentCoCoChecker checker = new OD4DevelopmentCoCoChecker();
    checker.addChecker(new ODBasisCoCos().getCheckerForAllInterCoCos());
    checker.addChecker(new ODLinkCoCos().getCheckerForAllInterCoCos());

    return checker;
  }

}
