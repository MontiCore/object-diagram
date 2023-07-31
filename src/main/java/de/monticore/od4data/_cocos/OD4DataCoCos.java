// (c) https://github.com/MontiCore/monticore

package de.monticore.od4data._cocos;

import de.monticore.odbasis._cocos.ODBasicsCoCos;
import de.monticore.odlink._cocos.ODLinkCoCos;

public class OD4DataCoCos {
  
  public OD4DataCoCoChecker getCheckerForAllIntraCoCos() {
    final OD4DataCoCoChecker checker = new OD4DataCoCoChecker();
    checker.addChecker(new ODBasicsCoCos().getCheckerForAllIntraCoCos());
    checker.addChecker(new ODLinkCoCos().getCheckerForAllIntraCoCos());
    
    return checker;
  }
  
  public OD4DataCoCoChecker getCheckerForAllCoCos() {
    final OD4DataCoCoChecker checker = new OD4DataCoCoChecker();
    checker.addChecker(new ODBasicsCoCos().getCheckerForAllCoCos());
    checker.addChecker(new ODLinkCoCos().getCheckerForAllCoCos());
    
    return checker;
  }
  
}
