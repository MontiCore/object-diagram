// (c) https://github.com/MontiCore/monticore

package de.monticore.lang.odbasics._cocos;

import de.monticore.lang.odbasics._cocos.attributes.NoAbstractAttributesCoCo;
import de.monticore.lang.odbasics._cocos.attributes.PartialAndCompleteAttributesCoCo;
import de.monticore.lang.odbasics._cocos.attributes.UniqueAttributeNamesCoCo;
import de.monticore.lang.odbasics._cocos.link.LinkEndConsistencyCoCo;
import de.monticore.lang.odbasics._cocos.link.NoAbstractLinkCoCo;
import de.monticore.lang.odbasics._cocos.link.ValidLinkReferenceCoCo;
import de.monticore.lang.odbasics._cocos.names.UniqueObjectNamesCoCo;

public class ODBasicsCoCos {

  public ODBasicsCoCoChecker getCheckerForAllCoCos() {
    final ODBasicsCoCoChecker checker = new ODBasicsCoCoChecker();

    // add default cocos
    // attributes
    checker.addCoCo(new NoAbstractAttributesCoCo());
    checker.addCoCo(new PartialAndCompleteAttributesCoCo());
    checker.addCoCo(new UniqueAttributeNamesCoCo());

    // links
    checker.addCoCo(new LinkEndConsistencyCoCo());
    checker.addCoCo(new NoAbstractLinkCoCo());
    checker.addCoCo(new ValidLinkReferenceCoCo());

    // names
    checker.addCoCo(new UniqueObjectNamesCoCo());

    // objects
    checker.addCoCo(new ValidLinkReferenceCoCo());

    return checker;
  }

}
