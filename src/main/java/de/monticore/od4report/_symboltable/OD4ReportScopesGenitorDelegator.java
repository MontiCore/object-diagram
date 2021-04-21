package de.monticore.od4report._symboltable;

import de.monticore.odbasis.ODBasisMill;
import de.monticore.odbasis._symboltable.ODBasisScopesGenitor;
import de.monticore.types.check.IDerive;

public class OD4ReportScopesGenitorDelegator extends OD4ReportScopesGenitorDelegatorTOP {

  private IDerive typechecker;

  public OD4ReportScopesGenitorDelegator() {
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
