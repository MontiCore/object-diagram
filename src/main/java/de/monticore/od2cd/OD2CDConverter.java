/* (c) https://github.com/MontiCore/monticore */
package de.monticore.od2cd;

import de.monticore.cd4code.CD4CodeMill;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.odbasis.ODBasisMill;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.odbasis._visitor.ODBasisTraverser;

public class OD2CDConverter {
  
  public OD2CDData doConvert(ASTODArtifact ast, GlobalExtensionManagement glex) {
    
    OD2CDObjectVisitor visitor = new OD2CDObjectVisitor(glex);
    ODBasisTraverser traverser = ODBasisMill.inheritanceTraverser();
    traverser.add4ODBasis(visitor);
    
    CD4CodeMill.init();
    
    traverser.handle(ast);
    
    return new OD2CDData(visitor.getCDCompilationUnit(), visitor.getInstantiatorClass(),
        visitor.getCheckerClass(),
        visitor.getObjectToClassMap().values());
  }
  
}
