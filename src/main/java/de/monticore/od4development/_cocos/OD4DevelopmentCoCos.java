/* (c) https://github.com/MontiCore/monticore */
package de.monticore.od4development._cocos;

import de.monticore.odbasis._cocos.ODBasicsCoCos;
import de.monticore.odlink._cocos.ODLinkCoCos;

public class OD4DevelopmentCoCos {
  
  public OD4DevelopmentCoCoChecker getCheckerForAllIntraCoCos() {
    final OD4DevelopmentCoCoChecker checker = new OD4DevelopmentCoCoChecker();
    checker.addChecker(new ODBasicsCoCos().getCheckerForAllIntraCoCos());
    checker.addChecker(new ODLinkCoCos().getCheckerForAllIntraCoCos());
    
    return checker;
  }
  
  public OD4DevelopmentCoCoChecker getCheckerForAllCoCos() {
    final OD4DevelopmentCoCoChecker checker = new OD4DevelopmentCoCoChecker();
    checker.addChecker(new ODBasicsCoCos().getCheckerForAllCoCos());
    checker.addChecker(new ODLinkCoCos().getCheckerForAllCoCos());
    
    return checker;
  }

}
