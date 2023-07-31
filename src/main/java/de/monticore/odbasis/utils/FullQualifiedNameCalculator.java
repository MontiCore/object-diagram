/*
 (c) https://github.com/MontiCore/monticore
 */

package de.monticore.odbasis.utils;

import de.monticore.types.mcbasictypes._ast.ASTMCImportStatement;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedName;
import de.se_rwth.commons.Names;

import java.util.ArrayList;
import java.util.List;

public class FullQualifiedNameCalculator {
  /*
   * computes possible full-qualified name candidates for the symbol named simpleName.
   * The symbol may be imported, be located in the same package or be defined inside the model
   * itself.
   */
  public static List<String> calcFQNameCandidates(List<ASTMCImportStatement> imports,
      ASTMCQualifiedName packageDeclaration, String simpleName) {
    List<String> fqNameCandidates = new ArrayList<>();
    for (ASTMCImportStatement anImport : imports) {
      if (anImport.isStar()) {
        // star import imports everything one level below the qualified model element
        fqNameCandidates.add(anImport.getQName() + "." + simpleName);
      }
      else if (Names.getSimpleName(anImport.getQName()).equals(simpleName)) {
        // top level symbol that has the same name as the node, e.g. diagram symbol
        fqNameCandidates.add(anImport.getQName());
      }
    }
    // The searched symbol might be located in the same package as the artifact
    if (!packageDeclaration.getQName().isEmpty()) {
      fqNameCandidates.add(packageDeclaration + "." + simpleName);
    }
    
    // Symbol might be defined in the model itself
    fqNameCandidates.add(simpleName);
    
    return fqNameCandidates;
  }
  
}
