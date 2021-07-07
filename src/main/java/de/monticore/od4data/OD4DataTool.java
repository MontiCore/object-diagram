// (c) https://github.com/MontiCore/monticore

package de.monticore.od4data;

import de.monticore.od4data._cocos.OD4DataCoCos;
import de.monticore.od4data._parser.OD4DataParser;
import de.monticore.od4data._symboltable.IOD4DataArtifactScope;
import de.monticore.od4data._symboltable.OD4DataScopesGenitorDelegator;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.se_rwth.commons.logging.Log;
import org.antlr.v4.runtime.RecognitionException;

import java.io.IOException;
import java.util.Optional;

public class OD4DataTool {

  /**
   * Parse the model contained in the specified file.
   *
   * @param model - file to parse
   * @return Artifact containing the OD
   */
  public static ASTODArtifact parse(String model) {
    try {
      OD4DataParser parser = new OD4DataParser();
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
   * @param ast ODArtifact ast
   * @return Symboltable created form AST
   */
  public static IOD4DataArtifactScope createSymbolTable(ASTODArtifact ast, boolean checkTypes) {
    OD4DataScopesGenitorDelegator od4DataScopesGenitorDelegator =
        OD4DataMill.scopesGenitorDelegator();
    od4DataScopesGenitorDelegator.setCheckTypes(checkTypes);
    return od4DataScopesGenitorDelegator.createFromAST(ast);
  }

  public static IOD4DataArtifactScope createSymbolTable(ASTODArtifact ast) {
    return OD4DataTool.createSymbolTable(ast, false);
  }

  public static void runAllCoCos(ASTODArtifact ast) {
    new OD4DataCoCos().getCheckerForAllCoCos().checkAll(ast);
  }

  public static void runAllIntraCoCos(ASTODArtifact ast) {
    new OD4DataCoCos().getCheckerForAllIntraCoCos().checkAll(ast);
  }

}
