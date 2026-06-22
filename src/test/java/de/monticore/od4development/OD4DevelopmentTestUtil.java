/* (c) https://github.com/MontiCore/monticore */
package de.monticore.od4development;

import com.google.common.collect.Lists;
import de.monticore.io.paths.MCPath;
import de.monticore.od4development._symboltable.IOD4DevelopmentArtifactScope;
import de.monticore.odbasis._ast.ASTODArtifact;
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
}
