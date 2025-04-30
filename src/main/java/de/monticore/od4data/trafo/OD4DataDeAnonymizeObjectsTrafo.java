/*
 *  (c) https://github.com/MontiCore/monticore
 */

package de.monticore.od4data.trafo;

import de.monticore.od4data.OD4DataMill;
import de.monticore.od4data._visitor.OD4DataTraverser;
import de.monticore.odbasis._ast.ASTODArtifact;

public class OD4DataDeAnonymizeObjectsTrafo {
  
  protected OD4DataTraverser traverser;
  
  public OD4DataDeAnonymizeObjectsTrafo() {
    this.traverser = OD4DataMill.inheritanceTraverser();
    
    init(traverser);
  }
  
  public static void init(OD4DataTraverser traverser) {
    final OD4DataDeAnonymizeObjectsTrafoExtension trafo =
        new OD4DataDeAnonymizeObjectsTrafoExtension();
    traverser.add4ODBasis(trafo);
    traverser.add4ODAttribute(trafo);
  }
  
  public OD4DataTraverser getTraverser() {
    return traverser;
  }
  
  public void setTraverser(OD4DataTraverser traverser) {
    this.traverser = traverser;
  }
  
  public void transform(ASTODArtifact diagram) {
    diagram.accept(getTraverser());
  }
}
