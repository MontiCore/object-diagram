// (c) https://github.com/MontiCore/monticore

package de.monticore.odbasis.typescalculator;

import de.monticore.expressions.expressionsbasis._visitor.ExpressionsBasisTraverser;
import de.monticore.grammar.grammar_withconcepts.SynthesizeFromMCBT4Grammar;
import de.monticore.odbasis.ODBasisMill;
import de.monticore.odbasis._visitor.ODBasisTraverser;
import de.monticore.types.check.*;

import java.util.Optional;

public class DeriveSymTypeOfODBasis implements IDerive {

  private TypeCheckResult typeCheckResult;

  protected ODBasisTraverser traverser = ODBasisMill.traverser();

  public DeriveSymTypeOfODBasis() {
    init();
  }

  @Override
  public Optional<SymTypeExpression> getResult() {
    return Optional.ofNullable(typeCheckResult.getCurrentResult());
  }

  public TypeCheckResult getTypeCheckResult() {
    return typeCheckResult;
  }

  @Override
  public void init() {
    typeCheckResult = new TypeCheckResult();

    //falls TypeCheck keine Typdefinitionen brauchen soll
    //final SynthesizeFromMCBT4Grammar synthesizeSymTypeFromMCBasicTypes =
    //  new SynthesizeFromMCBT4Grammar();
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
  }

  @Override
  public ExpressionsBasisTraverser getTraverser() {
    return traverser;
  }

}
