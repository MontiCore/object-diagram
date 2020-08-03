// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report._symboltable;

import de.monticore.od4report._ast.ASTODReportObject;
import de.monticore.odbasis._symboltable.ODBasisSymbolTableCreator;

import java.util.Deque;

public class OD4ReportSymbolTableCreator extends OD4ReportSymbolTableCreatorTOP {

  private ODBasisSymbolTableCreator odBasisSymbolTableCreator = new ODBasisSymbolTableCreator(
      scopeStack);

  public OD4ReportSymbolTableCreator(IOD4ReportScope enclosingScope) {
    super(enclosingScope);
  }

  public OD4ReportSymbolTableCreator(Deque<? extends IOD4ReportScope> scopeStack) {
    super(scopeStack);
  }

  @Override
  public void visit(ASTODReportObject astodReportObject) {
    odBasisSymbolTableCreator.visit(astodReportObject);
  }

  @Override
  public void endVisit(ASTODReportObject astodReportObject) {
    odBasisSymbolTableCreator.endVisit(astodReportObject);
  }

}
