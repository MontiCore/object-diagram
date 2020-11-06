// (c) https://github.com/MontiCore/monticore

package de.monticore.od4data._symboltable;

import de.monticore.odattribute._symboltable.ODAttributeSymbolTableCreator;
import de.monticore.odattribute.typescalculator.DeriveSymTypeOfODAttribute;
import de.monticore.odbasis._symboltable.ODBasisSymbolTableCreator;
import de.monticore.odbasis.typescalculator.ODTypesCalculator;
import de.monticore.odlink._symboltable.ODLinkSymbolTableCreator;

public class OD4DataSymbolTableCreatorDelegator extends OD4DataSymbolTableCreatorDelegatorTOP {

  private ODTypesCalculator typechecker;

  public OD4DataSymbolTableCreatorDelegator() {
    super();
    setRealThis(this);
    setTypechecker(new DeriveSymTypeOfODAttribute());
  }

  public OD4DataSymbolTableCreatorDelegator(IOD4DataGlobalScope globalScope) {
    super(globalScope);
    setRealThis(this);

    setTypechecker(new DeriveSymTypeOfODAttribute());
  }

  public ODTypesCalculator getTypechecker() {
    return typechecker;
  }

  public void setTypechecker(ODTypesCalculator typechecker) {
    this.typechecker = typechecker;

    ((ODBasisSymbolTableCreator) this.getODBasisVisitor().get()).setTypechecker(typechecker);
    ((ODAttributeSymbolTableCreator) this.getODAttributeVisitor().get()).setTypechecker(
        typechecker);
    ((ODLinkSymbolTableCreator) this.getODLinkVisitor().get()).setTypechecker(typechecker);
  }

}
