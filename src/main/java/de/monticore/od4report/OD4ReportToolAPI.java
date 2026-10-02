// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report;

import de.monticore.od4report._cocos.OD4ReportCoCos;
import de.monticore.od4report._parser.OD4ReportParser;
import de.monticore.od4report._symboltable.IOD4ReportArtifactScope;
import de.monticore.od4report._symboltable.OD4ReportScopesGenitorDelegator;
import de.monticore.od4report._visitor.OD4ReportTraverser;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.odbasis._symboltable.ODBasisSymbolTableCompleter;
import de.se_rwth.commons.logging.Log;
import org.antlr.v4.runtime.RecognitionException;

import java.io.IOException;
import java.util.Optional;

public class OD4ReportToolAPI {

  /**
   * Parse the model contained in the specified file.
   *
   * @param model - file to parse
   * @return Artifact containing OD
   */
  public static ASTODArtifact parse(String model) {
    try {
      OD4ReportParser parser = OD4ReportMill.parser();
      Optional<ASTODArtifact> optODArtifact = parser.parse(model);

      if (!parser.hasErrors() && optODArtifact.isPresent()) {
        return optODArtifact.get();
      }
      Log.error("0x0D013: Model could not be parsed.");
    }
    catch (RecognitionException | IOException e) {
      Log.error("0x0D014: Failed to parse " + model, e);
    }
    return null;
  }

  /**
   * Create the symbol table from the parsed AST.
   *
   * @param ast ODArtifact AST
   * @return SymbolTable created from AST
   */
  public static IOD4ReportArtifactScope createSymbolTable(ASTODArtifact ast) {
    OD4ReportScopesGenitorDelegator od4ReportScopesGenitorDelegator =
        OD4ReportMill.scopesGenitorDelegator();
    return od4ReportScopesGenitorDelegator.createFromAST(ast);
  }
  
  public static void completeSymbolTable(ASTODArtifact ast, boolean checkObjectTypes) {
    OD4ReportTraverser traverser = OD4ReportMill.inheritanceTraverser();
    
    ODBasisSymbolTableCompleter odbasisSymbolTableCompleter = new ODBasisSymbolTableCompleter(checkObjectTypes);
    traverser.add4ODBasis(odbasisSymbolTableCompleter);
    odbasisSymbolTableCompleter.setTraverser(traverser);
    ast.accept(traverser);
  }

  public static void runAllCoCos(ASTODArtifact ast) {
    new OD4ReportCoCos().getCheckerForAllCoCos().checkAll(ast);
  }

  public static void runAllIntraCoCos(ASTODArtifact ast) {
    new OD4ReportCoCos().getCheckerForAllIntraCoCos().checkAll(ast);
  }

}
