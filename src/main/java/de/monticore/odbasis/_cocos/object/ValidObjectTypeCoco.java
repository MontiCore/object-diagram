/* (c) https://github.com/MontiCore/monticore */

package de.monticore.odbasis._cocos.object;

import com.google.common.collect.Iterables;
import de.monticore.odbasis._ast.ASTODObject;
import de.monticore.odbasis._cocos.ODBasisASTODObjectCoCo;
import de.monticore.odbasis.utils.FullQualifiedNameCalculator;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.symbols.basicsymbols._symboltable.TypeSymbol;
import de.monticore.types.mcbasictypes._ast.ASTMCImportStatement;
import de.monticore.types.mcbasictypes._ast.ASTMCPackageDeclaration;
import de.monticore.types.prettyprint.MCBasicTypesFullPrettyPrinter;
import de.se_rwth.commons.logging.Log;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This is a inter model coco checking if the type of the object is defined.
 */
public class ValidObjectTypeCoco implements ODBasisASTODObjectCoCo {

  private final String TYPE_DEFINED_MUTLIPLE_TIMES =
      "0xB0032: Type '%s' is defined more " + "than once.";

  private final String TYPE_USED_BUT_UNDEFINED = "0xB0035: Type '%s' is used but not " + "defined.";

  private List<ASTMCImportStatement> importStatementList;

  private ASTMCPackageDeclaration packageDeclaration;

  public ValidObjectTypeCoco(List<ASTMCImportStatement> importStatementList,
      ASTMCPackageDeclaration packageDeclaration) {
    this.importStatementList = importStatementList;
    this.packageDeclaration = packageDeclaration;
  }

  @Override
  public void check(ASTODObject node) {
    TypeSymbol typeSymbol = this.resolveOOTypeSymbol(node,
        node.getMCObjectType().printType(new MCBasicTypesFullPrettyPrinter(new IndentPrinter())));
    System.out.println(typeSymbol.getName());
  }

  private TypeSymbol resolveOOTypeSymbol(ASTODObject node, String typeName) {
    Set<TypeSymbol> typeSymbols = new HashSet<>();
    for (String fqNameCandidate : FullQualifiedNameCalculator.calcFQNameCandidates(
        importStatementList, packageDeclaration.getMCQualifiedName(), typeName)) {
      typeSymbols.addAll(node.getEnclosingScope().resolveOOTypeMany(fqNameCandidate));
    }

    if (typeSymbols.isEmpty()) {
      Log.error(String.format(TYPE_USED_BUT_UNDEFINED, typeName), node.get_SourcePositionStart());
    }
    else if (typeSymbols.size() > 1) {
      Log.error(String.format(TYPE_DEFINED_MUTLIPLE_TIMES, typeName),
          node.get_SourcePositionStart());
    }

    return Iterables.getFirst(typeSymbols, null);
  }

}
