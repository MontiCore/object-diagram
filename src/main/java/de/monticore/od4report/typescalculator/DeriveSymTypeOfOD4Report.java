// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report.typescalculator;

import de.monticore.od4report.OD4ReportMill;
import de.monticore.od4report._visitor.OD4ReportTraverser;
import de.monticore.odbasis.typescalculator.DeriveSymTypeOfODBasis;
import de.monticore.types.check.*;

public class DeriveSymTypeOfOD4Report extends DeriveSymTypeOfODBasis {

  public DeriveSymTypeOfOD4Report() {
    this(OD4ReportMill.traverser());
  }

  public DeriveSymTypeOfOD4Report(OD4ReportTraverser traverser) {
    this.traverser = traverser;
    this.init(traverser);
  }

  protected void init(OD4ReportTraverser traverser) {
    typeCheckResult = new TypeCheckResult();

    final SynthesizeSymTypeFromMCBasicTypes synthesizeSymTypeFromMCBasicTypes =
      new SynthesizeSymTypeFromMCBasicTypes();
    synthesizeSymTypeFromMCBasicTypes.setTypeCheckResult(getTypeCheckResult());
    traverser.add4MCBasicTypes(synthesizeSymTypeFromMCBasicTypes);
    traverser.setMCBasicTypesHandler(synthesizeSymTypeFromMCBasicTypes);

    final DeriveSymTypeOfCommonExpressions deriveSymTypeOfCommonExpressions =
      new DeriveSymTypeOfCommonExpressions();
    deriveSymTypeOfCommonExpressions.setTypeCheckResult(typeCheckResult);
    traverser.add4CommonExpressions(deriveSymTypeOfCommonExpressions);
    traverser.setCommonExpressionsHandler(deriveSymTypeOfCommonExpressions);

    final DeriveSymTypeOfMCCommonLiterals deriveSymTypeOfMCCommonLiterals =
      new DeriveSymTypeOfMCCommonLiterals();
    deriveSymTypeOfMCCommonLiterals.setTypeCheckResult(typeCheckResult);
    traverser.add4MCCommonLiterals(deriveSymTypeOfMCCommonLiterals);

    final SynthesizeSymTypeFromMCCollectionTypes synthesizeSymTypeFromMCCollectionTypes =
      new SynthesizeSymTypeFromMCCollectionTypes();
    synthesizeSymTypeFromMCCollectionTypes.setTypeCheckResult(getTypeCheckResult());
    traverser.add4MCCollectionTypes(synthesizeSymTypeFromMCCollectionTypes);
    traverser.setMCCollectionTypesHandler(synthesizeSymTypeFromMCCollectionTypes);
  }
}
