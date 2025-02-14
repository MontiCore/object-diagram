/* (c) https://github.com/MontiCore/monticore */
package de.monticore.od4development._symboltable;

import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.symboltable.ImportStatement;
import de.se_rwth.commons.logging.Log;

import java.util.ArrayList;

public class OD4DevelopmentScopesGenitor extends OD4DevelopmentScopesGenitorTOP {

  public IOD4DevelopmentArtifactScope createFromAST (ASTODArtifact rootNode) {
    Log.errorIfNull(rootNode, "0xA7004x78995 Error by creating of the OD4DevelopmentScopesGenitor symbol table: top ast node is null");
    IOD4DevelopmentArtifactScope artifactScope = de.monticore.od4development.OD4DevelopmentMill.artifactScope();
    
    // set package
    if (rootNode.isPresentMCPackageDeclaration()) {
      artifactScope.setPackageName(
              rootNode.getMCPackageDeclaration().getMCQualifiedName().getQName());
    } else {
      artifactScope.setPackageName("");
    }
    
    // add imports
    artifactScope.setImportsList(new ArrayList<>());
    rootNode
        .getMCImportStatementList()
        .forEach(i -> artifactScope.addImports(new ImportStatement(i.getQName(), i.isStar())));
    
    artifactScope.setName(rootNode.getObjectDiagram().getName());
    artifactScope.setAstNode(rootNode);
    putOnStack(artifactScope);
    initArtifactScopeHP1(artifactScope);
    rootNode.accept(getTraverser());
    initArtifactScopeHP2(artifactScope);
    return artifactScope;
  }

}
