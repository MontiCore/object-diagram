// (c) https://github.com/MontiCore/monticore

package de.monticore.odattribute.typescalculator;

import de.monticore.odattribute._visitor.ODAttributeDelegatorVisitor;
import de.monticore.odbasis.typescalculator.ODTypesCalculator;
import de.monticore.types.check.*;
import de.monticore.types.mcbasictypes._ast.ASTMCObjectType;

import java.util.Optional;

public class DeriveSymTypeOfODAttribute extends ODAttributeDelegatorVisitor
    implements ODTypesCalculator {

  private TypeCheckResult typeCheckResult;

  public DeriveSymTypeOfODAttribute() {
    setRealThis(this);
    init();
  }

  @Override
  public Optional<SymTypeExpression> calculateType(ASTMCObjectType type) {
    type.accept(getRealThis());
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
    setMCBasicTypesVisitor(synthesizeSymTypeFromMCBasicTypes);

    final SynthesizeSymTypeFromMCSimpleGenericTypes synthesizeSymTypeFromMCSimpleGenericTypes =
        new SynthesizeSymTypeFromMCSimpleGenericTypes();
    synthesizeSymTypeFromMCSimpleGenericTypes.setTypeCheckResult(getTypeCheckResult());
    setMCSimpleGenericTypesVisitor(synthesizeSymTypeFromMCSimpleGenericTypes);

    final SynthesizeSymTypeFromMCCollectionTypes synthesizeSymTypeFromMCCollectionTypes =
        new SynthesizeSymTypeFromMCCollectionTypes();
    synthesizeSymTypeFromMCCollectionTypes.setTypeCheckResult(getTypeCheckResult());
    setMCCollectionTypesVisitor(synthesizeSymTypeFromMCCollectionTypes);

    final SynthesizeSymTypeFromMCFullGenericTypes synthesizeSymTypeFromMCFullGenericTypes =
        new SynthesizeSymTypeFromMCFullGenericTypes();
    synthesizeSymTypeFromMCFullGenericTypes.setTypeCheckResult(getTypeCheckResult());
    setMCFullGenericTypesVisitor(synthesizeSymTypeFromMCFullGenericTypes);
  }

}
