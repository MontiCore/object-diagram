// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

package de.monticore.od4data;

import de.monticore.io.paths.ModelPath;
import de.monticore.od4data._ast.ASTOD4DataNode;
import de.monticore.od4data._cocos.OD4DataCoCoChecker;
import de.monticore.od4data._parser.OD4DataParser;
import de.monticore.od4data._symboltable.OD4DataArtifactScope;
import de.monticore.od4data._symboltable.OD4DataGlobalScope;
import de.monticore.od4data._symboltable.OD4DataSymbolTableCreatorDelegator;
import de.monticore.od4data.prettyprinter.OD4DataPrettyPrinterDelegator;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.odbasis._ast.ASTODBasisNode;
import de.monticore.odbasis._cocos.ODBasicsCoCos;
import de.monticore.odlink._cocos.ODLinkCoCos;
import de.monticore.prettyprint.IndentPrinter;
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
        createSymbolTable(optODArtifact.get());
        runDefaultCoCos(optODArtifact.get());
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
  public static OD4DataArtifactScope createSymbolTable(ASTODArtifact ast) {
    OD4DataGlobalScope globalScope = new OD4DataGlobalScope(new ModelPath(), "od");

    OD4DataSymbolTableCreatorDelegator symbolTable = OD4DataMill
        .oD4DataSymbolTableCreatorDelegatorBuilder().setGlobalScope(globalScope).build();
    return symbolTable.createFromAST(ast);
  }

  public static void runDefaultCoCos(ASTODArtifact ast) {
    OD4DataCoCoChecker od4DataCoCoChecker = new OD4DataCoCoChecker();
    od4DataCoCoChecker.addChecker(new ODBasicsCoCos().getCheckerForAllCoCos());
    od4DataCoCoChecker.addChecker(new ODLinkCoCos().getCheckerForAllCoCos());
    od4DataCoCoChecker.checkAll(ast);
  }

  /**
   * Print object diagram node.
   *
   * @param astod4DataNode object node to be printed
   * @return node as String
   */
  public static String prettyPrintODNode(ASTOD4DataNode astod4DataNode) {
    if (astod4DataNode == null) {
      return "";
    }

    return new OD4DataPrettyPrinterDelegator(new IndentPrinter()).prettyprint(astod4DataNode);
  }

  public static String prettyPrintODNode(ASTODBasisNode astodBasisNode) {
    if (astodBasisNode == null) {
      return "";
    }

    return new OD4DataPrettyPrinterDelegator(new IndentPrinter()).prettyprint(astodBasisNode);
  }

}
