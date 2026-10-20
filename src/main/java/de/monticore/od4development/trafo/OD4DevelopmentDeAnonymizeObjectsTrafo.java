/*
 *  (c) https://github.com/MontiCore/monticore
 */

package de.monticore.od4development.trafo;

import de.monticore.od4development.OD4DevelopmentMill;
import de.monticore.od4development._visitor.OD4DevelopmentTraverser;
import de.monticore.odbasis._ast.ASTODArtifact;

public class OD4DevelopmentDeAnonymizeObjectsTrafo {
  
  protected OD4DevelopmentTraverser traverser;
  
  public OD4DevelopmentDeAnonymizeObjectsTrafo() {
    this.traverser = OD4DevelopmentMill.inheritanceTraverser();
    
    init(traverser);
  }
  
  public static void init(OD4DevelopmentTraverser traverser) {
    final OD4DevelopmentDeAnonymizeObjectsTrafoExtension trafo =
        new OD4DevelopmentDeAnonymizeObjectsTrafoExtension();
    traverser.add4ODBasis(trafo);
    traverser.add4ODAttribute(trafo);
  }
  
  public OD4DevelopmentTraverser getTraverser() {
    return traverser;
  }
  
  public void setTraverser(OD4DevelopmentTraverser traverser) {
    this.traverser = traverser;
  }
  
  public void transform(ASTODArtifact diagram) {
    diagram.accept(getTraverser());
  }
}
