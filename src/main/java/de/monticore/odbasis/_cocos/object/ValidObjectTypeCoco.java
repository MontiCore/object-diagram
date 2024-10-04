/* (c) https://github.com/MontiCore/monticore */

package de.monticore.odbasis._cocos.object;

import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.odbasis._ast.ASTODObject;
import de.monticore.odbasis._cocos.ODBasisASTODArtifactCoCo;
import de.monticore.odbasis._cocos.ODBasisASTODObjectCoCo;
import de.monticore.odbasis._cocos.ODBasisCoCoChecker;
import de.monticore.odbasis.utils.FullQualifiedNameCalculator;
import de.monticore.symbols.basicsymbols._symboltable.TypeSymbol;
import de.monticore.types.mcbasictypes._ast.ASTMCImportStatement;
import de.monticore.types.mcbasictypes._ast.ASTMCPackageDeclaration;
import de.se_rwth.commons.logging.Log;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This is a inter model coco checking if the type of the object is defined.
 */
public class ValidObjectTypeCoco implements ODBasisASTODArtifactCoCo {

  private final String TYPE_DEFINED_MUTLIPLE_TIMES =
      "0xB0032: Type '%s' is defined more " + "than once.";

  private final String TYPE_USED_BUT_UNDEFINED = "0xB0035: Type '%s' is used but not " + "defined.";

  private List<ASTMCImportStatement> importStatementList;

  private ASTMCPackageDeclaration packageDeclaration;

  @Override
  public void check(ASTODArtifact node) {
    importStatementList = node.getMCImportStatementList();
    packageDeclaration = node.getMCPackageDeclaration();

    ODBasisASTODObjectCoCo odBasisASTODObjectCoCo = new ODBasisASTODObjectCoCo() {
      @Override
      public void check(ASTODObject node) {
        this.resolveOOTypeSymbol(node, node.getMCObjectType().printType());
      }

      private void resolveOOTypeSymbol(ASTODObject node, String typeName) {
        Set<TypeSymbol> typeSymbols = new HashSet<>();
        for (String fqNameCandidate : FullQualifiedNameCalculator.calcFQNameCandidates(
            importStatementList, packageDeclaration.getMCQualifiedName(), typeName)) {
          typeSymbols.addAll(node.getEnclosingScope().resolveOOTypeMany(fqNameCandidate));
        }

        if (typeSymbols.isEmpty()) {
          Log.error(String.format(TYPE_USED_BUT_UNDEFINED, typeName),
              node.get_SourcePositionStart());
        }
        else if (typeSymbols.size() > 1) {
          Log.error(String.format(TYPE_DEFINED_MUTLIPLE_TIMES, typeName),
              node.get_SourcePositionStart());
        }
      }
    };

    ODBasisCoCoChecker odBasisCoCoChecker = new ODBasisCoCoChecker();
    odBasisCoCoChecker.addCoCo(odBasisASTODObjectCoCo);
    odBasisCoCoChecker.checkAll(node);
  }

}
