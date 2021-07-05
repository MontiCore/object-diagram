/* (c) https://github.com/MontiCore/monticore */
package de.monticore.od4data._symboltable;

import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.odbasis._symboltable.ODBasisScopesGenitor;
import de.se_rwth.commons.logging.Log;

import java.util.ArrayList;

public class OD4DataScopesGenitor extends OD4DataScopesGenitorTOP {

  private ODBasisScopesGenitor odBasisScopesGenitor = new ODBasisScopesGenitor();

  public OD4DataScopesGenitor() {
    super();
  }

  @Override
  public IOD4DataArtifactScope createFromAST(ASTODArtifact rootNode) {
    Log.errorIfNull(rootNode,
        "0xA7004x40398 Error by creating of the OD4DataScopesGenitor symbol table: top ast node"
            + " is null");
    IOD4DataArtifactScope artifactScope = de.monticore.od4data.OD4DataMill.artifactScope();
    if (rootNode.isPresentMCPackageDeclaration()) {
      artifactScope.setPackageName(
          rootNode.getMCPackageDeclaration().getMCQualifiedName().getQName());
    }
    else {
      artifactScope.setPackageName("");
    }
    artifactScope.setImportsList(new ArrayList<>());
    artifactScope.setName(rootNode.getObjectDiagram().getName());
    putOnStack(artifactScope);
    rootNode.accept(getTraverser());
    return artifactScope;
  }

}
