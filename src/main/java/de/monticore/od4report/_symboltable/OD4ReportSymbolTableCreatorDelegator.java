// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report._symboltable;

import de.monticore.od4report.typescalculator.DeriveSymTypeOfOD4Report;
import de.monticore.odbasis._symboltable.ODBasisSymbolTableCreator;
import de.monticore.odbasis.typescalculator.ODTypesCalculator;

public class OD4ReportSymbolTableCreatorDelegator extends OD4ReportSymbolTableCreatorDelegatorTOP {

  private ODTypesCalculator typechecker;

  public OD4ReportSymbolTableCreatorDelegator() {
    super();
    setRealThis(this);
    setTypechecker(new DeriveSymTypeOfOD4Report());
  }

  public OD4ReportSymbolTableCreatorDelegator(IOD4ReportGlobalScope globalScope) {
    super(globalScope);
    setRealThis(this);

    setTypechecker(new DeriveSymTypeOfOD4Report());
  }

  public ODTypesCalculator getTypechecker() {
    return typechecker;
  }

  public void setTypechecker(ODTypesCalculator typechecker) {
    this.typechecker = typechecker;

    ((ODBasisSymbolTableCreator) this.getODBasisVisitor().get()).setTypechecker(typechecker);
  }

}
