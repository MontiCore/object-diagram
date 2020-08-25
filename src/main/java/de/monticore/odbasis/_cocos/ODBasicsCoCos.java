// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

package de.monticore.odbasis._cocos;

import de.monticore.odbasis._cocos.attributes.NoAbstractAttributesCoCo;
import de.monticore.odbasis._cocos.attributes.PartialAndCompleteAttributesCoCo;
import de.monticore.odbasis._cocos.attributes.UniqueAttributeNamesCoCo;
import de.monticore.odbasis._cocos.names.UniqueObjectNamesCoCo;

public class ODBasicsCoCos {

  public ODBasisCoCoChecker getCheckerForAllCoCos() {
    final ODBasisCoCoChecker checker = new ODBasisCoCoChecker();

    // add default cocos
    // attributes
    checker.addCoCo(new NoAbstractAttributesCoCo());
    checker.addCoCo(new PartialAndCompleteAttributesCoCo());
    checker.addCoCo(new UniqueAttributeNamesCoCo());

    // names
    checker.addCoCo(new UniqueObjectNamesCoCo());

    return checker;
  }

}
