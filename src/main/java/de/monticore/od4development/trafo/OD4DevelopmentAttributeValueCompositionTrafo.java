/*
 *  (c) https://github.com/MontiCore/monticore
 */

/*
 *  (c) https://github.com/MontiCore/monticore
 */

package de.monticore.od4development.trafo;

import de.monticore.od4development.OD4DevelopmentMill;
import de.monticore.od4development._visitor.OD4DevelopmentTraverser;
import de.monticore.odbasis._ast.ASTODArtifact;

public class OD4DevelopmentAttributeValueCompositionTrafo {
  
  protected OD4DevelopmentTraverser traverser;
  
  public OD4DevelopmentAttributeValueCompositionTrafo() {
    this.traverser = OD4DevelopmentMill.inheritanceTraverser();
    
    init(traverser);
  }
  
  public static void init(OD4DevelopmentTraverser traverser) {
    final OD4DevelopmentAttributeValueCompositionTrafoExtension trafo =
        new OD4DevelopmentAttributeValueCompositionTrafoExtension();
    traverser.add4ODBasis(trafo);
    traverser.add4ODAttribute(trafo);
    traverser.add4ODLink(trafo);
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
