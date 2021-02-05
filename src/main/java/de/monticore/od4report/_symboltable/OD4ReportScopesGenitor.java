package de.monticore.od4report._symboltable;

import de.monticore.od4report.OD4ReportMill;
import de.monticore.od4report._ast.ASTODReportObject;
import de.monticore.odbasis._symboltable.ODBasisScopesGenitor;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbolBuilder;

import java.util.Deque;

public class OD4ReportScopesGenitor extends OD4ReportScopesGenitorTOP {

  private ODBasisScopesGenitor odBasisScopesGenitor = new ODBasisScopesGenitor(scopeStack);

  public OD4ReportScopesGenitor() {
    super();
  }

  public OD4ReportScopesGenitor(IOD4ReportScope enclosingScope) {
    super(enclosingScope);
  }

  public OD4ReportScopesGenitor(Deque<? extends IOD4ReportScope> scopeStack) {
    super(scopeStack);
  }

  @Override
  public void visit(ASTODReportObject astodReportObject) {
    VariableSymbol symbol = create_ODReportObject(astodReportObject).build();
    addToScope(symbol);
    symbol.setAstNode(astodReportObject);
    astodReportObject.setSymbol(symbol);
    astodReportObject.setEnclosingScope(symbol.getEnclosingScope());
  }

  @Override
  public void endVisit(ASTODReportObject astodReportObject) {
    super.endVisit(astodReportObject);
    odBasisScopesGenitor.endVisit(astodReportObject);
  }

  private VariableSymbolBuilder create_ODReportObject(ASTODReportObject astodReportObject) {
    return OD4ReportMill.variableSymbolBuilder().setName(astodReportObject.getName());
  }

}
