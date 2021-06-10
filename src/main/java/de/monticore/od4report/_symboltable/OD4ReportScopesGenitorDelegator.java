/* (c) https://github.com/MontiCore/monticore */
package de.monticore.od4report._symboltable;

import de.monticore.od4report.OD4ReportMill;
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

    traverser.getOD4ReportVisitorList().clear();
    OD4ReportScopesGenitor od4ReportScopesGenitor = OD4ReportMill.scopesGenitor();
    od4ReportScopesGenitor.setScopeStack(scopeStack);
    od4ReportScopesGenitor.setCheckTypes(typeCheck);
    traverser.add4OD4Report(od4ReportScopesGenitor);
    traverser.setOD4ReportHandler(od4ReportScopesGenitor);
  }

  public IDerive getTypechecker() {
    return typechecker;
  }

  public void setTypechecker(IDerive typechecker) {
    this.typechecker = typechecker;

    ((ODBasisScopesGenitor) this.traverser.getODBasisHandler().get()).setTypechecker(typechecker);
  }

}
