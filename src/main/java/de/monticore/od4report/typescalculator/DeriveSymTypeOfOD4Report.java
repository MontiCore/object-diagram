// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report.typescalculator;

import de.monticore.od4report.OD4ReportMill;
import de.monticore.od4report._visitor.OD4ReportTraverser;
import de.monticore.odbasis.typescalculator.ODTypesCalculator;
import de.monticore.types.check.SymTypeExpression;
import de.monticore.types.check.SynthesizeSymTypeFromMCBasicTypes;
import de.monticore.types.check.SynthesizeSymTypeFromMCCollectionTypes;
import de.monticore.types.check.TypeCheckResult;
import de.monticore.types.mcbasictypes._ast.ASTMCObjectType;

import java.util.Optional;

public class DeriveSymTypeOfOD4Report implements ODTypesCalculator {

  private TypeCheckResult typeCheckResult;

  protected OD4ReportTraverser traverser = OD4ReportMill.traverser();

  public DeriveSymTypeOfOD4Report() {
    init();
  }

  @Override
  public Optional<SymTypeExpression> calculateType(ASTMCObjectType type) {
    type.accept(traverser);
    return getResult();
  }

  @Override
  public void reset() {
    getTypeCheckResult().setCurrentResultAbsent();
  }

  @Override
  public Optional<SymTypeExpression> getResult() {
    return Optional.ofNullable(typeCheckResult.getCurrentResult());
  }

  public TypeCheckResult getTypeCheckResult() {
    return typeCheckResult;
  }

  private void init() {
    typeCheckResult = new TypeCheckResult();

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
