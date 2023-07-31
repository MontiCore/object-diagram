// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report._ast;

public class ASTODReportObject extends ASTODReportObjectTOP {
  
  @Override
  public String getName() {
    return getODSpecialName();
  }
  
  @Override
  public void setName(String name) {
    this.setODSpecialName(name);
  }
  
}
