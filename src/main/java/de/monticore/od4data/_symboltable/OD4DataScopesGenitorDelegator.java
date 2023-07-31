/*
 (c) https://github.com/MontiCore/monticore
 */

package de.monticore.od4data._symboltable;

import de.monticore.odbasis.ODBasisMill;
import de.monticore.odbasis._symboltable.ODBasisScopesGenitor;
import de.monticore.odbasis.typescalculator.FullODBasisSynthesizer;

public class OD4DataScopesGenitorDelegator extends OD4DataScopesGenitorDelegatorTOP {
  private FullODBasisSynthesizer synthesizer;
  
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
  
  public FullODBasisSynthesizer getSynthesizer() {
    return synthesizer;
  }
  
  public void setSynthesizer(FullODBasisSynthesizer synthesizer) {
    this.synthesizer = synthesizer;
    
    ((ODBasisScopesGenitor) this.traverser.getODBasisHandler().get()).setSynthesizer(synthesizer);
  }
  
}
