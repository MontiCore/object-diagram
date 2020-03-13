// (c) https://github.com/MontiCore/monticore

package de.monticore.lang.odbasics.cocos;

import de.monticore.lang.odbasics._cocos.ODBasicsCoCoChecker;
import de.monticore.lang.odbasics.cocos.attributes.NoAbstractAttributesCoCo;
import de.monticore.lang.odbasics.cocos.attributes.PartialAndCompleteAttributesCoCo;
import de.monticore.lang.odbasics.cocos.attributes.UniqueAttributeNamesCoCo;
import de.monticore.lang.odbasics.cocos.link.LinkEndConsistencyCoCo;
import de.monticore.lang.odbasics.cocos.link.NoAbstractLinkCoCo;
import de.monticore.lang.odbasics.cocos.link.ValidLinkReferenceCoCo;
import de.monticore.lang.odbasics.cocos.names.UniqueObjectNamesCoCo;

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
