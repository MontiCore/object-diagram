/* (c) https://github.com/MontiCore/monticore */
package de.monticore.od4report._symboltable;

import com.google.common.base.Preconditions;
import de.monticore.od4report.OD4ReportMill;
import de.monticore.od4report._ast.ASTODReportObject;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbolBuilder;
import de.monticore.symboltable.ImportStatement;
import de.se_rwth.commons.logging.Log;

import java.util.ArrayList;

public class OD4ReportScopesGenitor extends OD4ReportScopesGenitorTOP {
  
  public OD4ReportScopesGenitor() {
    super();
  }
  
  @Override
  public void visit(ASTODReportObject astodReportObject) {
    VariableSymbol symbol = create_ODReportObject(astodReportObject).build();
    if (getCurrentScope().isPresent()) {
      getCurrentScope().get().add(symbol);
    }
    else {
      Log.warn("0xA50213 Symbol cannot be added to current scope, since no scope exists.");
    }
    symbol.setAstNode(astodReportObject);
    astodReportObject.setSymbol(symbol);
    astodReportObject.setEnclosingScope(symbol.getEnclosingScope());
  }
  
  private VariableSymbolBuilder create_ODReportObject(ASTODReportObject astodReportObject) {
    return OD4ReportMill.variableSymbolBuilder().setName(astodReportObject.getName());
  }
  
  @Override
  public IOD4ReportArtifactScope createFromAST(ASTODArtifact rootNode) {
    Preconditions.checkNotNull(rootNode,
        "0xAE881 Error by creating of the OD4ReportScopesGenitor symbol table: top ast node"
            + " is null");
    IOD4ReportArtifactScope artifactScope = de.monticore.od4report.OD4ReportMill.artifactScope();
    if (rootNode.isPresentMCPackageDeclaration()) {
      artifactScope.setPackageName(
          rootNode.getMCPackageDeclaration().getMCQualifiedName().getQName());
    }
    else {
      artifactScope.setPackageName("");
    }
    artifactScope.setImportsList(new ArrayList<>());
    rootNode.getMCImportStatementList()
        .forEach(i -> artifactScope.addImports(new ImportStatement(i.getQName(), i.isStar())));
    
    artifactScope.setName(rootNode.getObjectDiagram().getName());
    putOnStack(artifactScope);
    rootNode.accept(getTraverser());
    return artifactScope;
  }
  
}
