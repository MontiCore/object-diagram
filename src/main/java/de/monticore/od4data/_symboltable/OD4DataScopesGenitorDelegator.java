/*
 (c) https://github.com/MontiCore/monticore
 */

package de.monticore.od4data._symboltable;

import de.monticore.odbasis.ODBasisMill;
import de.monticore.odbasis._symboltable.ODBasisScopesGenitor;
import de.monticore.odbasis.typescalculator.DeriveSymTypeOfODBasis;

public class OD4DataScopesGenitorDelegator extends OD4DataScopesGenitorDelegatorTOP {
  private DeriveSymTypeOfODBasis typechecker;

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

  public DeriveSymTypeOfODBasis getTypechecker() {
    return typechecker;
  }

  public void setTypechecker(DeriveSymTypeOfODBasis typechecker) {
    this.typechecker = typechecker;

    ((ODBasisScopesGenitor) this.traverser.getODBasisHandler().get()).setTypechecker(typechecker);
  }

}
