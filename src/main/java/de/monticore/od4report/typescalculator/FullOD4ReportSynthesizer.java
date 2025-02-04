/* (c) https://github.com/MontiCore/monticore */
package de.monticore.od4report.typescalculator;

import de.monticore.od4report.OD4ReportMill;
import de.monticore.od4report._visitor.OD4ReportTraverser;
import de.monticore.odbasis.typescalculator.FullODBasisSynthesizer;
import de.monticore.types.check.SynthesizeSymTypeFromMCBasicTypes;
import de.monticore.types.check.SynthesizeSymTypeFromMCCollectionTypes;

/**
 * @deprecated using TC3 instead.
 */
@Deprecated
public class FullOD4ReportSynthesizer extends FullODBasisSynthesizer {

  public FullOD4ReportSynthesizer(){
    this(OD4ReportMill.traverser());
  }

  public FullOD4ReportSynthesizer(OD4ReportTraverser traverser){
    super(traverser);
    init(traverser);
  }

  public void init(OD4ReportTraverser traverser){
    final SynthesizeSymTypeFromMCBasicTypes synthesizeSymTypeFromMCBasicTypes =
      new SynthesizeSymTypeFromMCBasicTypes();
    synthesizeSymTypeFromMCBasicTypes.setTypeCheckResult(getTypeCheckResult());
    traverser.add4MCBasicTypes(synthesizeSymTypeFromMCBasicTypes);
    traverser.setMCBasicTypesHandler(synthesizeSymTypeFromMCBasicTypes);

    final SynthesizeSymTypeFromMCCollectionTypes synthesizeSymTypeFromMCCollectionTypes =
      new SynthesizeSymTypeFromMCCollectionTypes();
    synthesizeSymTypeFromMCCollectionTypes.setTypeCheckResult(getTypeCheckResult());
    traverser.add4MCCollectionTypes(synthesizeSymTypeFromMCCollectionTypes);
    traverser.setMCCollectionTypesHandler(synthesizeSymTypeFromMCCollectionTypes);
  }

}
