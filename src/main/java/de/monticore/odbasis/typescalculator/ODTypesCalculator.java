// (c) https://github.com/MontiCore/monticore

package de.monticore.odbasis.typescalculator;

import de.monticore.types.check.SymTypeExpression;
import de.monticore.types.mcbasictypes._ast.ASTMCObjectType;

import java.util.Optional;

public interface ODTypesCalculator {

  Optional<SymTypeExpression> calculateType(ASTMCObjectType type);

  void reset();

  Optional<SymTypeExpression> getResult();

}
