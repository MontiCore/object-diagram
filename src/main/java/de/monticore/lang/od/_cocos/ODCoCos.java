/*
 * Copyright (c) 2017, MontiCore. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
package de.monticore.lang.od._cocos;

import de.monticore.lang.od._cocos.attributes.NoAbstractAttributesCoCo;
import de.monticore.lang.od._cocos.attributes.PartialAndCompleteAttributesCoCo;
import de.monticore.lang.od._cocos.attributes.UniqueAttributeNamesCoCo;
import de.monticore.lang.od._cocos.link.LinkEndConsistencyCoCo;
import de.monticore.lang.od._cocos.link.NoAbstractLinkCoCo;
import de.monticore.lang.od._cocos.link.ValidLinkReferenceCoCo;
import de.monticore.lang.od._cocos.names.NoSpecialNameCoCo;
import de.monticore.lang.od._cocos.names.UniqueObjectNamesCoCo;
import de.monticore.lang.od._cocos.object.ValidObjectReferenceCoCo;

public class ODCoCos {
  
  public ODCoCoChecker getChecker() {
    ODCoCoChecker checker = new ODCoCoChecker();
    checker.addCoCo(new NoAbstractAttributesCoCo());
    checker.addCoCo(new PartialAndCompleteAttributesCoCo());
    checker.addCoCo(new UniqueAttributeNamesCoCo());
    
    checker.addCoCo(new LinkEndConsistencyCoCo());
    checker.addCoCo(new NoAbstractLinkCoCo());
    checker.addCoCo(new ValidLinkReferenceCoCo());
    
    checker.addCoCo(new UniqueObjectNamesCoCo());
    checker.addCoCo(new ValidObjectReferenceCoCo());
    
    return checker;
  }
  
  public ODCoCoChecker getCheckerForSimpleNames() {
    ODCoCoChecker checker = new ODCoCoChecker();
    checker.addChecker(getChecker());
    checker.addCoCo(new NoSpecialNameCoCo());
    return checker;
  }
  
}
