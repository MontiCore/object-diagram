/* (c) https://github.com/MontiCore/monticore */
package de.monticore.odbasis.typescalculator;

import de.monticore.odbasis.ODBasisMill;
import de.monticore.odbasis._visitor.ODBasisTraverser;
import de.monticore.types.check.*;
import de.monticore.visitor.ITraverser;

/**
 * @deprecated using TC3 instead.
 */
@Deprecated
public class FullODBasisSynthesizer extends AbstractSynthesize {

  public FullODBasisSynthesizer(){
    this(ODBasisMill.traverser());
  }

  public FullODBasisSynthesizer(ODBasisTraverser traverser) {
    super(traverser);
    init(traverser);
  }

  protected void init(ODBasisTraverser traverser) {
    //falls TypeCheck keine Typdefinitionen brauchen soll
    //final SynthesizeFromMCBT4Grammar synthesizeSymTypeFromMCBasicTypes =
    //  new SynthesizeFromMCBT4Grammar();
    final SynthesizeSymTypeFromMCBasicTypes synthesizeSymTypeFromMCBasicTypes =
      new SynthesizeSymTypeFromMCBasicTypes();
    synthesizeSymTypeFromMCBasicTypes.setTypeCheckResult(getTypeCheckResult());
    traverser.add4MCBasicTypes(synthesizeSymTypeFromMCBasicTypes);
    traverser.setMCBasicTypesHandler(synthesizeSymTypeFromMCBasicTypes);
  }
}
