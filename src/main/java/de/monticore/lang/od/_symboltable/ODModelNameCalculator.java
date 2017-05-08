/* generated from model null*/
/* generated by template symboltable.ModelNameCalculator*/

package de.monticore.lang.od._symboltable;

import de.monticore.symboltable.SymbolKind;

import java.util.LinkedHashSet;
import java.util.Set;

public class ODModelNameCalculator extends ODModelNameCalculatorTOP {

  @Override
  public Set<String> calculateModelNames(final String name, final SymbolKind kind) {
    final Set<String> calculatedModelNames = new LinkedHashSet<>();

    if (ObjectDiagramSymbol.KIND.isKindOf(kind)) {
      calculatedModelNames.addAll(calculateModelNamesForObjectDiagram(name));
    }
    if (ODObjectSymbol.KIND.isKindOf(kind)) {
      calculatedModelNames.addAll(calculateModelNamesForODObject(name));
    }

    return calculatedModelNames;
  }

  protected Set<String> calculateModelNamesForODObject(String name) {
    final Set<String> modelNames = new LinkedHashSet<>();
    modelNames.add(name);
    return modelNames;
  }

}
