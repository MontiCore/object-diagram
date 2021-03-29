package de.monticore.od4report._symboltable;

import de.monticore.odbasis._symboltable.ODBasisScopesGenitor;
import de.monticore.types.check.IDerive;

public class OD4ReportScopesGenitorDelegator extends OD4ReportScopesGenitorDelegatorTOP {

  private IDerive typechecker;

  public OD4ReportScopesGenitorDelegator() {
    super();
  }

  public OD4ReportScopesGenitorDelegator(
      de.monticore.od4report._symboltable.IOD4ReportGlobalScope globalScope) {
    super(globalScope);
  }

  public IDerive getTypechecker() {
    return typechecker;
  }

  public void setTypechecker(IDerive typechecker) {
    this.typechecker = typechecker;

    ((ODBasisScopesGenitor) this.traverser.getODBasisHandler().get()).setTypechecker(typechecker);
  }

}
