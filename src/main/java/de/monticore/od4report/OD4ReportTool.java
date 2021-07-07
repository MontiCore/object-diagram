// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report;

import de.monticore.od4report._cocos.OD4ReportCoCos;
import de.monticore.od4report._parser.OD4ReportParser;
import de.monticore.od4report._symboltable.IOD4ReportArtifactScope;
import de.monticore.od4report._symboltable.OD4ReportScopesGenitorDelegator;
import de.monticore.odbasis._ast.ASTODArtifact;
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
   * @param ast ODArtifact AST
   * @return SymbolTable created from AST
   */
  public static IOD4ReportArtifactScope createSymbolTable(ASTODArtifact ast, boolean checkTypes) {
    OD4ReportScopesGenitorDelegator od4ReportScopesGenitorDelegator =
        OD4ReportMill.scopesGenitorDelegator();
    od4ReportScopesGenitorDelegator.setCheckTypes(checkTypes);
    return od4ReportScopesGenitorDelegator.createFromAST(ast);
  }

  public static IOD4ReportArtifactScope createSymbolTable(ASTODArtifact ast) {
    return OD4ReportTool.createSymbolTable(ast, false);
  }

  public static void runAllCoCos(ASTODArtifact ast) {
    new OD4ReportCoCos().getCheckerForAllCoCos().checkAll(ast);
  }

  public static void runAllIntraCoCos(ASTODArtifact ast) {
    new OD4ReportCoCos().getCheckerForAllIntraCoCos().checkAll(ast);
  }

}
