package de.monticore.od4report._symboltable;

import de.monticore.od4report._ast.ASTODReportObject;
import de.monticore.odbasis._symboltable.ODBasisScopesGenitor;

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
    odBasisScopesGenitor.visit(astodReportObject);
  }

  @Override
  public void endVisit(ASTODReportObject astodReportObject) {
    odBasisScopesGenitor.endVisit(astodReportObject);
  }

}
