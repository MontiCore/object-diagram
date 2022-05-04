/* (c) https://github.com/MontiCore/monticore */
package de.monticore.odbasis.typescalculator;

import de.monticore.odbasis.ODBasisMill;
import de.monticore.odbasis._visitor.ODBasisTraverser;
import de.monticore.types.check.*;
import de.monticore.visitor.ITraverser;

public class FullODBasisDeriver extends AbstractDerive {

  public FullODBasisDeriver(){
    this(ODBasisMill.traverser());
  }

  public FullODBasisDeriver(ODBasisTraverser traverser) {
    super(traverser);
    init(traverser);
  }

  protected void init(ODBasisTraverser traverser) {
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
}
