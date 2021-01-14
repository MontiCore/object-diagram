// (c) https://github.com/MontiCore/monticore

package de.monticore.od4data._symboltable;

import de.monticore.odbasis._symboltable.ODBasisSymbolTableCreator;
import de.monticore.odbasis.typescalculator.DeriveSymTypeOfODBasis;
import de.monticore.odbasis.typescalculator.ODTypesCalculator;

public class OD4DataSymbolTableCreatorDelegator extends OD4DataSymbolTableCreatorDelegatorTOP {

  private ODTypesCalculator typechecker;

  public OD4DataSymbolTableCreatorDelegator() {
    super();
    setRealThis(this);
    setTypechecker(new DeriveSymTypeOfODBasis());
  }

  public OD4DataSymbolTableCreatorDelegator(IOD4DataGlobalScope globalScope) {
    super(globalScope);
    setRealThis(this);

    setTypechecker(new DeriveSymTypeOfODBasis());
  }

  public ODTypesCalculator getTypechecker() {
    return typechecker;
  }

  public void setTypechecker(ODTypesCalculator typechecker) {
    this.typechecker = typechecker;

    ((ODBasisSymbolTableCreator) this.getODBasisVisitor().get()).setTypechecker(typechecker);
  }

}
