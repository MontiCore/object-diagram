// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report.typescalculator;

import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.monticore.expressions.expressionsbasis._visitor.ExpressionsBasisTraverser;
import de.monticore.literals.mccommonliterals._ast.ASTSignedLiteral;
import de.monticore.literals.mcliteralsbasis._ast.ASTLiteral;
import de.monticore.od4report.OD4ReportMill;
import de.monticore.od4report._visitor.OD4ReportTraverser;
import de.monticore.odbasis.typescalculator.ODTypesCalculator;
import de.monticore.types.check.*;
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

  @Override
  public Optional<SymTypeExpression> calculateType(ASTExpression ex) {
    ex.accept(traverser);
    return getResult();
  }

  @Override
  public Optional<SymTypeExpression> calculateType(ASTLiteral lit) {
    lit.accept(traverser);
    return getResult();
  }

  @Override
  public Optional<SymTypeExpression> calculateType(ASTSignedLiteral lit) {
    lit.accept(traverser);
    return getResult();
  }

  @Override
  public void init() {
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

  @Override
  public ExpressionsBasisTraverser getTraverser() {
    return null;
  }

}
