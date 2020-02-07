// (c) https://github.com/MontiCore/monticore

package de.monticore.lang.odbasics;

import de.monticore.io.paths.ModelPath;
import de.monticore.lang.odbasics._ast.ASTODArtifact;
import de.monticore.lang.odbasics._ast.ASTODObject;
import de.monticore.lang.odbasics._parser.ODBasicsParser;
import de.monticore.lang.odbasics._symboltable.ODBasicsArtifactScope;
import de.monticore.lang.odbasics._symboltable.ODBasicsGlobalScope;
import de.monticore.lang.odbasics._symboltable.ODBasicsLanguage;
import de.monticore.lang.odbasics._symboltable.ODBasicsSymbolTableCreatorDelegator;
import de.monticore.lang.odbasics.cocos.ODBasicsCoCos;
import de.monticore.lang.odbasics.prettyprinter.ODBasicsPrettyPrinter;
import de.monticore.lang.odbasics.prettyprinter.ODPrettyPrinterDelegator;
import de.monticore.prettyprint.IndentPrinter;
import de.se_rwth.commons.logging.Log;
import org.antlr.v4.runtime.RecognitionException;

import java.io.IOException;
import java.util.Optional;

public class ODBasicsTool {

  /**
   * Parse the model contained in the specified file.
   *
   * @param model - file to parse
   * @return Artifact containing the OD
   */
  public static ASTODArtifact parse(String model) {
    try {
      ODBasicsParser parser = new ODBasicsParser();
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
   * @param ast ODArtifact ast
   * @return Symboltable created form AST
   */
  public static ODBasicsArtifactScope createSymbolTable(ODBasicsLanguage lang, ASTODArtifact ast) {

    ODBasicsGlobalScope globalScope = new ODBasicsGlobalScope(new ModelPath(), lang);

    ODBasicsSymbolTableCreatorDelegator symbolTable = lang.getSymbolTableCreator(globalScope);
    return symbolTable.createFromAST(ast);
  }

  public static void runDefaultCoCos(ASTODArtifact ast) {
    new ODBasicsCoCos().getCheckerForAllCoCos().checkAll(ast);
  }

  /**
   * Print object artifact.
   *
   * @param astodArtifact ODArtifact to be printed
   * @return OD as String
   */
  public static String prettyPrintOD(ASTODArtifact astodArtifact) {
    if (astodArtifact == null) {
      return "";
    }

    return new ODPrettyPrinterDelegator(new IndentPrinter()).prettyprint(astodArtifact);
  }

  /**
   * Print object diagram.
   *
   * @param astodObject object diagram to be printed
   * @return OD as String
   */
  public static String prettyPrintOD(ASTODObject astodObject) {
    if (astodObject == null) {
      return "";
    }

    return new ODPrettyPrinterDelegator(new IndentPrinter()).prettyprint(astodObject);
  }

}