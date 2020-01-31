// (c) https://github.com/MontiCore/monticore

package de.monticore.lang.od4report;

import de.monticore.io.paths.ModelPath;
import de.monticore.lang.od4report._parser.OD4ReportParser;
import de.monticore.lang.od4report._symboltable.OD4ReportArtifactScope;
import de.monticore.lang.od4report._symboltable.OD4ReportGlobalScope;
import de.monticore.lang.od4report._symboltable.OD4ReportLanguage;
import de.monticore.lang.od4report._symboltable.OD4ReportSymbolTableCreatorDelegator;
import de.monticore.lang.od4report.cocos.OD4ReportCoCos;
import de.monticore.lang.od4report.prettyprinter.OD4ReportPrettyPrinter;
import de.monticore.lang.odbasics._ast.ASTODArtifact;
import de.monticore.prettyprint.IndentPrinter;
import de.se_rwth.commons.logging.Log;
import org.antlr.v4.runtime.RecognitionException;

import java.io.IOException;
import java.util.Optional;

public class OD4ReportTool {

  /**
   * Parse the model contained in the specified file.
   *
   * @param model - file to parse
   * @return Artifact containing OD
   */
  public static ASTODArtifact parse(String model) {
    try {
      OD4ReportParser parser = new OD4ReportParser();
      Optional<ASTODArtifact> optODArtifact = parser.parse(model);

      if (!parser.hasErrors() && optODArtifact.isPresent()) {
        return optODArtifact.get();
      }
      Log.error("Model could not be parsed.");
    }
    catch (RecognitionException | IOException e) {
      Log.error("Failed to parse " + model, e);
    }
    return null;
  }

  /**
   * Create the symbol table from the parsed AST.
   *
   * @param lang ODlanguage
   * @param ast Arifact AST
   * @return SymbolTable created from AST
   */
  public static OD4ReportArtifactScope createSymbolTable(OD4ReportLanguage lang,
      ASTODArtifact ast) {

    OD4ReportGlobalScope globalScope = new OD4ReportGlobalScope(new ModelPath(), lang);

    OD4ReportSymbolTableCreatorDelegator symbolTable = lang.getSymbolTableCreator(globalScope);
    return symbolTable.createFromAST(ast);
  }

  public static void runDefaultCoCos(ASTODArtifact ast) {
    new OD4ReportCoCos().getCheckerForAllCoCos().checkAll(ast);
  }

  /**
   * Print object diagram.
   *
   * @param astodArtifact ODArtifact to be printed
   * @return OD as String
   */
  public static String prettyPrintOD(ASTODArtifact astodArtifact) {
    if (astodArtifact == null) {
      return "";
    }

    OD4ReportPrettyPrinter odBasicsPrettyPrinter = new OD4ReportPrettyPrinter(new IndentPrinter());
    astodArtifact.accept(odBasicsPrettyPrinter);

    return odBasicsPrettyPrinter.getPrinter().getContent();
  }

}
