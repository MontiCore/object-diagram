// (c) https://github.com/MontiCore/monticore

package de.monticore.odbasis.typescalculator;

import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.monticore.literals.mcliteralsbasis._ast.ASTLiteral;
import de.monticore.odbasis.ODBasisMill;
import de.monticore.odbasis._visitor.ODBasisTraverser;
import de.monticore.types.check.*;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedName;
import de.monticore.types.mcbasictypes._ast.ASTMCReturnType;
import de.monticore.types.mcbasictypes._ast.ASTMCType;
import de.monticore.visitor.ITraverser;

public class DeriveSymTypeOfODBasis implements IDerive, ISynthesize {

  protected TypeCheckResult typeCheckResult;

  protected ODBasisTraverser traverser;

  public DeriveSymTypeOfODBasis() {
    this(ODBasisMill.traverser());
  }

  public DeriveSymTypeOfODBasis(ODBasisTraverser traverser) {
    this.traverser = traverser;
    this.init(traverser);
  }

  protected TypeCheckResult getTypeCheckResult() {
    return typeCheckResult;
  }

  protected void init(ODBasisTraverser traverser) {
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

  protected ITraverser getTraverser() {
    return traverser;
  }

  @Override
  public TypeCheckResult deriveType(ASTExpression expr) {
    this.getTypeCheckResult().reset();
    expr.accept(this.getTraverser());
    return this.getTypeCheckResult().copy();
  }

  @Override
  public TypeCheckResult deriveType(ASTLiteral lit) {
    this.getTypeCheckResult().reset();
    lit.accept(this.getTraverser());
    return this.getTypeCheckResult().copy();
  }

  @Override
  public TypeCheckResult synthesizeType(ASTMCType type) {
    this.getTypeCheckResult().reset();
    type.accept(this.getTraverser());
    return this.getTypeCheckResult().copy();
  }

  @Override
  public TypeCheckResult synthesizeType(ASTMCReturnType type) {
    this.getTypeCheckResult().reset();
    type.accept(this.getTraverser());
    return this.getTypeCheckResult().copy();
  }

  @Override
  public TypeCheckResult synthesizeType(ASTMCQualifiedName qName) {
    this.getTypeCheckResult().reset();
    qName.accept(this.getTraverser());
    return this.getTypeCheckResult().copy();
  }
}
