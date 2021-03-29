/* (c) https://github.com/MontiCore/monticore */
package de.monticore.od4data._symboltable;

import de.monticore.odbasis._ast.ASTODArtifact;
import de.se_rwth.commons.logging.Log;

import java.util.ArrayList;
import java.util.Deque;

public class OD4DataScopesGenitor extends OD4DataScopesGenitorTOP {

  public OD4DataScopesGenitor(){
    super();
  }

  public OD4DataScopesGenitor(Deque<? extends IOD4DataScope> scopeStack){
    super(scopeStack);
  }

  @Override
  public IOD4DataArtifactScope createFromAST(ASTODArtifact rootNode) {
    Log.errorIfNull(rootNode, "0xA7004x20649 Error by creating of the OD4DataScopesGenitor symbol table: top ast node is null");
    IOD4DataArtifactScope artifactScope = de.monticore.od4data.OD4DataMill.artifactScope();
    if(rootNode.isPresentMCPackageDeclaration()){
      artifactScope.setPackageName(rootNode.getMCPackageDeclaration().getMCQualifiedName().getQName());
    }else{
      artifactScope.setPackageName("");
    }
    artifactScope.setName(rootNode.getObjectDiagram().getName());
    artifactScope.setImportsList(new ArrayList<>());
    putOnStack(artifactScope);
    rootNode.accept(getTraverser());
    return artifactScope;
  }
}
