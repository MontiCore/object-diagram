// (c) https://github.com/MontiCore/monticore

package de.monticore.odlink._cocos;

import de.monticore.odlink._cocos.link.LinkEndConsistencyCoCo;
import de.monticore.odlink._cocos.link.NoAbstractLinkCoCo;
import de.monticore.odlink._cocos.link.ValidLinkReferenceCoCo;

public class ODLinkCoCos {

  public ODLinkCoCoChecker getCheckerForAllIntraCoCos() {
    final ODLinkCoCoChecker checker = new ODLinkCoCoChecker();

    // links
    checker.addCoCo(new NoAbstractLinkCoCo());

    // objects
    checker.addCoCo(new ValidLinkReferenceCoCo());

    return checker;
  }

  public ODLinkCoCoChecker getCheckerForAllCoCos() {
    final ODLinkCoCoChecker checker = new ODLinkCoCoChecker();
    checker.addChecker(getCheckerForAllIntraCoCos());

    // links
    checker.addCoCo(new LinkEndConsistencyCoCo());
    
    // objects

    return checker;
  }

}
