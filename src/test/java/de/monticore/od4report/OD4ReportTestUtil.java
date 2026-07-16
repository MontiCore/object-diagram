/* (c) https://github.com/MontiCore/monticore */
package de.monticore.od4report;

import com.google.common.collect.Lists;
import de.monticore.io.paths.MCPath;

import de.monticore.od4report._symboltable.IOD4ReportArtifactScope;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.symbols.basicsymbols.BasicSymbolsMill;
import de.monticore.symboltable.ImportStatement;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.nio.charset.StandardCharsets.UTF_8;

import static org.junit.jupiter.api.Assertions.fail;

public class OD4ReportTestUtil {
  
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
    OD4ReportMill.globalScope().setSymbolPath(symbolPath);
    OD4ReportMill.globalScope().putTypeSymbolDeSer("de.monticore.cdbasis._symboltable.CDTypeSymbol");
    ASTODArtifact artifact = OD4ReportToolAPI.parse(pathToArtifact.toString());
    if (artifact == null) {
      fail("Loading artifact: " + pathToArtifact + " failed!");
    }
    return artifact;
  }
  
  public static IOD4ReportArtifactScope createSymbolTableFromAST(ASTODArtifact ast) {
    IOD4ReportArtifactScope as = OD4ReportToolAPI.createSymbolTable(ast);
    
    OD4ReportToolAPI.completeSymbolTable(ast, true);
    
    // add imports
    List<ImportStatement> imports = Lists.newArrayList();
    ast.getMCImportStatementList()
        .forEach(i -> imports.add(new ImportStatement(i.getQName(), i.isStar())));
    as.setImportsList(imports);
    
    return as;
  }

  /**
   * Runs the OD tool in a separate JVM process and returns the merged console output.
   * This isolates the JUnit JVM from potential System.exit(...) calls inside the tool.
   */
  public static List<String> runToolInSeparateProcess(String... args) {
    List<String> command = new ArrayList<>();
    command.add(Paths.get(System.getProperty("java.home"), "bin", "java").toString());
    command.add("-cp");
    command.add(System.getProperty("java.class.path"));
    command.add(OD4ReportTool.class.getName());
    command.addAll(List.of(args));

    try {
      Process process = new ProcessBuilder(command).redirectErrorStream(true).start();
      List<String> outputLines;
      try (BufferedReader reader =
          new BufferedReader(new InputStreamReader(process.getInputStream(), UTF_8))) {
        outputLines = reader.lines().toList();
      }

      boolean finished = process.waitFor(30, TimeUnit.SECONDS);
      if (!finished) {
        process.destroyForcibly();
        fail("Tool process did not finish within timeout.");
      }

      return outputLines;
    }
    catch (Exception e) {
      fail("Running tool in separate process failed: " + e.getMessage());
      return List.of();
    }
  }
}
