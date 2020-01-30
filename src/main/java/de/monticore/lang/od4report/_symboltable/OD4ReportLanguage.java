// (c) https://github.com/MontiCore/monticore

package de.monticore.lang.od4report._symboltable;

import java.nio.file.Paths;

public class OD4ReportLanguage extends OD4ReportLanguageTOP {

  public static final String FILE_ENDING = "od";

  public OD4ReportLanguage() {
    super("OD4Report Language", FILE_ENDING);
  }

  @Override
  protected OD4ReportModelLoader provideModelLoader() {
    return new OD4ReportModelLoader(this);
  }

  public String getQualifiedModelNameFromScope(OD4ReportArtifactScope scope) {
    String fileName =
        (scope.isPresentName() ? scope.getName() : "symbols") + "." + getSymbolFileExtension();
    return Paths.get(scope.getPackageName(), fileName).toString();
  }

}
