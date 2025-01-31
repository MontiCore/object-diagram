/* (c) https://github.com/MontiCore/monticore */
package de.monticore.od4report;

import com.google.common.collect.Lists;
import de.monticore.io.paths.MCPath;

import de.monticore.od4report._symboltable.IOD4ReportArtifactScope;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.symbols.basicsymbols.BasicSymbolsMill;
import de.monticore.symboltable.ImportStatement;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.fail;

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
    IOD4ReportArtifactScope as = OD4ReportToolAPI.createSymbolTable(ast, true);
    
    // add imports
    List<ImportStatement> imports = Lists.newArrayList();
    ast.getMCImportStatementList()
        .forEach(i -> imports.add(new ImportStatement(i.getQName(), i.isStar())));
    as.setImportsList(imports);
    
    return as;
  }
}
