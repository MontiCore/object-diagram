// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

package de.monticore.odlink._cocos;

import de.monticore.odlink._cocos.link.LinkEndConsistencyCoCo;
import de.monticore.odlink._cocos.link.NoAbstractLinkCoCo;
import de.monticore.odlink._cocos.link.ValidLinkReferenceCoCo;

public class ODLinkCoCos {

  public ODLinkCoCoChecker getCheckerForAllCoCos() {
    final ODLinkCoCoChecker checker = new ODLinkCoCoChecker();

    // links
    checker.addCoCo(new LinkEndConsistencyCoCo());
    checker.addCoCo(new NoAbstractLinkCoCo());
    checker.addCoCo(new ValidLinkReferenceCoCo());

    // objects
    checker.addCoCo(new ValidLinkReferenceCoCo());

    return checker;
  }

}
