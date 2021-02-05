package de.monticore.od4report._symboltable;

import de.monticore.odbasis._symboltable.ODBasisScopesGenitor;
import de.monticore.odbasis.typescalculator.ODTypesCalculator;

public class OD4ReportScopesGenitorDelegator extends OD4ReportScopesGenitorDelegatorTOP {

  private ODTypesCalculator typechecker;

  public OD4ReportScopesGenitorDelegator() {
    super();
  }

  public OD4ReportScopesGenitorDelegator(
      de.monticore.od4report._symboltable.IOD4ReportGlobalScope globalScope) {
    super(globalScope);
  }

  public ODTypesCalculator getTypechecker() {
    return typechecker;
  }

  public void setTypechecker(ODTypesCalculator typechecker) {
    this.typechecker = typechecker;

    ((ODBasisScopesGenitor) this.traverser.getODBasisHandler().get()).setTypechecker(typechecker);
  }

}
