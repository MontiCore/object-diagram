// (c) https://github.com/MontiCore/monticore

package de.monticore.lang.odbasics._symboltable;

import java.nio.file.Paths;

public class ODBasicsLanguage extends ODBasicsLanguageTOP {

  public static final String FILE_ENDING = "od";

  public ODBasicsLanguage() {
    super("ODBasic Language", FILE_ENDING);
  }

  @Override
  protected ODBasicsModelLoader provideModelLoader() {
    return new ODBasicsModelLoader(this);
  }

  public String getQualifiedModelNameFromScope(ODBasicsArtifactScope scope) {
    String fileName =
        (scope.isPresentName() ? scope.getName() : "symbols") + "." + getSymbolFileExtension();
    return Paths.get(scope.getPackageName(), fileName).toString();
  }

}
