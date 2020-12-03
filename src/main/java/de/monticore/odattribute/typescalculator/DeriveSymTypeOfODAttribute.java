// (c) https://github.com/MontiCore/monticore

package de.monticore.odattribute.typescalculator;

import de.monticore.odattribute.ODAttributeMill;
import de.monticore.odattribute._visitor.ODAttributeTraverser;
import de.monticore.odbasis.typescalculator.ODTypesCalculator;
import de.monticore.types.check.*;
import de.monticore.types.mcbasictypes._ast.ASTMCObjectType;

import java.util.Optional;

public class DeriveSymTypeOfODAttribute implements ODTypesCalculator {

  private TypeCheckResult typeCheckResult;
  
  protected ODAttributeTraverser traverser = ODAttributeMill.traverser();

  public DeriveSymTypeOfODAttribute() {
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

    final SynthesizeSymTypeFromMCSimpleGenericTypes synthesizeSymTypeFromMCSimpleGenericTypes =
        new SynthesizeSymTypeFromMCSimpleGenericTypes();
    synthesizeSymTypeFromMCSimpleGenericTypes.setTypeCheckResult(getTypeCheckResult());
    traverser.add4MCSimpleGenericTypes(synthesizeSymTypeFromMCSimpleGenericTypes);
    traverser.setMCSimpleGenericTypesHandler(synthesizeSymTypeFromMCSimpleGenericTypes);

    final SynthesizeSymTypeFromMCCollectionTypes synthesizeSymTypeFromMCCollectionTypes =
        new SynthesizeSymTypeFromMCCollectionTypes();
    synthesizeSymTypeFromMCCollectionTypes.setTypeCheckResult(getTypeCheckResult());
    traverser.add4MCCollectionTypes(synthesizeSymTypeFromMCCollectionTypes);
    traverser.setMCCollectionTypesHandler(synthesizeSymTypeFromMCCollectionTypes);

    final SynthesizeSymTypeFromMCFullGenericTypes synthesizeSymTypeFromMCFullGenericTypes =
        new SynthesizeSymTypeFromMCFullGenericTypes();
    synthesizeSymTypeFromMCFullGenericTypes.setTypeCheckResult(getTypeCheckResult());
    traverser.add4MCFullGenericTypes(synthesizeSymTypeFromMCFullGenericTypes);
    traverser.setMCFullGenericTypesHandler(synthesizeSymTypeFromMCFullGenericTypes);
  }

}
