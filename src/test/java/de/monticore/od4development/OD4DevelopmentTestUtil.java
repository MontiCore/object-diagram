/* (c) https://github.com/MontiCore/monticore */
package de.monticore.od4development;

import com.google.common.collect.Lists;
import de.monticore.ToolProcessRunner;
import de.monticore.io.paths.MCPath;
import de.monticore.od4development._symboltable.IOD4DevelopmentArtifactScope;
import de.monticore.od4development._visitor.OD4DevelopmentTraverser;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.odbasis._symboltable.ODBasisSymbolTableCompleter;
import de.monticore.symbols.basicsymbols.BasicSymbolsMill;
import de.monticore.symboltable.ImportStatement;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;

public class OD4DevelopmentTestUtil {
  
  public static ASTODArtifact loadModelAndST(String pathToArtifact, MCPath symbolPath) {
    Path path = Paths.get(pathToArtifact);
    return loadModelAndST(path, symbolPath);
  }

  public static ASTODArtifact loadModelAndST(Path pathToArtifact, MCPath symbolPath) {
    ASTODArtifact artifact = loadModel(pathToArtifact, symbolPath);
    createSymbolTableFromAST(artifact);
    return artifact;
  }

  public static ASTODArtifact loadModel(String pathToArtifact, MCPath symbolPath) {
    Path path = Paths.get(pathToArtifact);
    return loadModel(path, symbolPath);
  }

  public static ASTODArtifact loadModel(Path pathToArtifact, MCPath symbolPath) {
    BasicSymbolsMill.initializePrimitives();
    OD4DevelopmentMill.globalScope().setSymbolPath(symbolPath);
    OD4DevelopmentMill.globalScope().putTypeSymbolDeSer("de.monticore.cdbasis._symboltable.CDTypeSymbol");
    Optional<ASTODArtifact> artifact = assertDoesNotThrow(() -> OD4DevelopmentMill.parser().parse(pathToArtifact.toString()));
    if (artifact.isEmpty()) {
      fail("Loading artifact: " + pathToArtifact + " failed!");
    }
    return artifact.get();
  }

  public static IOD4DevelopmentArtifactScope createSymbolTableFromAST(ASTODArtifact ast) {
    IOD4DevelopmentArtifactScope as = OD4DevelopmentMill.scopesGenitorDelegator().createFromAST(ast);

    // add imports
    List<ImportStatement> imports = Lists.newArrayList();
    ast.getMCImportStatementList()
        .forEach(i -> imports.add(new ImportStatement(i.getQName(), i.isStar())));
    as.setImportsList(imports);
    return as;
  }
  
  public static void completeSymbolTable(ASTODArtifact ast, boolean checkObjectTypes) {
    OD4DevelopmentTraverser traverser = OD4DevelopmentMill.inheritanceTraverser();
    
    ODBasisSymbolTableCompleter odBasisCompleter =
        new ODBasisSymbolTableCompleter(checkObjectTypes);
    traverser.add4ODBasis(odBasisCompleter);
    odBasisCompleter.setTraverser(traverser);
    ast.accept(traverser);
  }

  /**
   * Runs the OD tool in a separate JVM process and returns the merged console output.
   *
   * @see ToolProcessRunner
   */
  public static List<String> runToolInSeparateProcess(String... args) {
    return runToolInSeparateProcess(null, args);
  }

  /**
   * Runs the OD tool in a separate JVM process with the given working directory and returns the
   * merged console output.
   *
   * @param workingDir working directory of the process, {@code null} for the current one
   * @see ToolProcessRunner
   */
  public static List<String> runToolInSeparateProcess(Path workingDir, String... args) {
    return ToolProcessRunner.run(OD4DevelopmentTool.class, workingDir, args);
  }
}
