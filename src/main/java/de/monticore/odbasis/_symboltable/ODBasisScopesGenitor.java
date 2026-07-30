/* (c) https://github.com/MontiCore/monticore */

package de.monticore.odbasis._symboltable;

import de.monticore.odbasis._ast.ASTODNamedObject;
import de.monticore.odbasis._ast.ASTObjectDiagram;

public class ODBasisScopesGenitor extends ODBasisScopesGenitorTOP {
  
  public ODBasisScopesGenitor() {
    super();
  }
  
  @Override
  public void endVisit(ASTObjectDiagram node) {
    super.endVisit(node);
    node.getEnclosingScope().setName(node.getName());
  }
  
  @Override
  public void visit(ASTODNamedObject node) {
    super.visit(node);
    node.getSymbol().setName(node.getName());
  }
}
