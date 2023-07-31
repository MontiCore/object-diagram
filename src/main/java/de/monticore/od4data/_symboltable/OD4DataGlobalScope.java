/*
 (c) https://github.com/MontiCore/monticore
 */

package de.monticore.od4data._symboltable;

import de.monticore.io.paths.MCPath;
import de.se_rwth.commons.Names;

import java.util.Set;

public class OD4DataGlobalScope extends OD4DataGlobalScopeTOP {
  private OD4DataGlobalScope realThis;
  
  public OD4DataGlobalScope() {
    realThis = this;
  }
  
  public OD4DataGlobalScope(MCPath modelPath, String extension) {
    super(modelPath, extension);
  }
  
  @Override
  public OD4DataGlobalScope getRealThis() {
    return realThis;
  }
  
  public void setRealThis(OD4DataGlobalScope realThis) {
    this.realThis = realThis;
  }
  
  @Override
  public Set<String> calculateModelNamesForOOType(String name) {
    Set<String> result = super.calculateModelNamesForOOType(name);
    
    while (name.contains(".")) {
      name = Names.getQualifier(name);
      result.add(name);
    }
    return result;
  }
  
  @Override
  public Set<String> calculateModelNamesForType(String name) {
    return calculateModelNamesForOOType(name);
  }
  
}
