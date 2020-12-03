// (c) https://github.com/MontiCore/monticore

package de.monticore.odbasis.typescalculator;

import de.monticore.odbasis.ODBasisMill;
import de.monticore.odbasis._visitor.ODBasisTraverser;
import de.monticore.types.check.SymTypeExpression;
import de.monticore.types.check.SynthesizeSymTypeFromMCBasicTypes;
import de.monticore.types.check.TypeCheckResult;
import de.monticore.types.mcbasictypes._ast.ASTMCObjectType;

import java.util.Optional;

public class DeriveSymTypeOfODBasis implements ODTypesCalculator {

  private TypeCheckResult typeCheckResult;
  
  protected ODBasisTraverser traverser = ODBasisMill.traverser();

  public DeriveSymTypeOfODBasis() {
    init();
  }

  @Override
  public Optional<SymTypeExpression> calculateType(ASTMCObjectType type) {
    type.accept(traverser);
    return getResult();
  }

  @Override
  public Optional<SymTypeExpression> getResult() {
    return Optional.ofNullable(typeCheckResult.getCurrentResult());
  }

  @Override
  public void reset() {
    getTypeCheckResult().setCurrentResultAbsent();
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
  }

}
