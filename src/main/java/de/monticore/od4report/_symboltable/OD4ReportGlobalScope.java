/*
 (c) https://github.com/MontiCore/monticore
 */

package de.monticore.od4report._symboltable;

import de.monticore.io.paths.ModelPath;
import de.monticore.odbasis.utils.FullQualifiedNameCalculator;
import de.monticore.utils.Names;

import java.util.Set;

public class OD4ReportGlobalScope extends OD4ReportGlobalScopeTOP {

  private OD4ReportGlobalScope realThis;

  public OD4ReportGlobalScope() {
    realThis = this;
  }

  public OD4ReportGlobalScope(ModelPath modelPath, String extension) {
    super(modelPath, extension);
  }

  @Override
  public OD4ReportGlobalScope getRealThis() {
    return realThis;
  }

  public void setRealThis(OD4ReportGlobalScope realThis) {
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
