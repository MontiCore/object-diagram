// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report._symboltable;

import de.monticore.odbasis.typescalculator.DeriveSymTypeOfODBasis;

import java.util.Deque;

public class OD4ReportSymbolTableCreator extends OD4ReportSymbolTableCreatorTOP {

  private DeriveSymTypeOfODBasis typechecker = new DeriveSymTypeOfODBasis();

  public OD4ReportSymbolTableCreator(IOD4ReportScope enclosingScope) {
    super(enclosingScope);
  }

  public OD4ReportSymbolTableCreator(Deque<? extends IOD4ReportScope> scopeStack) {
    super(scopeStack);
  }

}
