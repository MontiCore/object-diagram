/*
 (c) https://github.com/MontiCore/monticore
 */

package de.monticore.od4data._symboltable;

import de.monticore.odbasis.ODBasisMill;
import de.monticore.odbasis._symboltable.ODBasisScopesGenitor;
import de.monticore.types.check.IDerive;

public class OD4DataScopesGenitorDelegator extends OD4DataScopesGenitorDelegatorTOP {
  private IDerive typechecker;

  public OD4DataScopesGenitorDelegator() {
    super();
  }

  public void setCheckTypes(boolean typeCheck) {
    traverser.getODBasisVisitorList().clear();
    ODBasisScopesGenitor odBasisScopesGenitor = ODBasisMill.scopesGenitor();
    odBasisScopesGenitor.setScopeStack(scopeStack);
    odBasisScopesGenitor.setCheckTypes(typeCheck);
    traverser.add4ODBasis(odBasisScopesGenitor);
    traverser.setODBasisHandler(odBasisScopesGenitor);
  }

  public IDerive getTypechecker() {
    return typechecker;
  }

  public void setTypechecker(IDerive typechecker) {
    this.typechecker = typechecker;

    ((ODBasisScopesGenitor) this.traverser.getODBasisHandler().get()).setTypechecker(typechecker);
  }

}
