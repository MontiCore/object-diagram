/*
 * Copyright (c) 2017, MontiCore. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
package de.monticore.lang.od._symboltable;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import de.monticore.symboltable.Symbol;
import de.monticore.symboltable.resolving.CommonResolvingFilter;
import de.monticore.symboltable.resolving.ResolvingFilter;
import de.monticore.symboltable.resolving.ResolvingInfo;

public class ODObjectResolvingFilter extends CommonResolvingFilter<ODObjectSymbol> {

  public ODObjectResolvingFilter() {
    super(ODObjectSymbol.KIND);
  }
  @Override
  public Optional<Symbol> filter(ResolvingInfo resolvingInfo, String name, Map<String, Collection<Symbol>> symbols) {
    final Set<Symbol> resolvedSymbols = new LinkedHashSet<>();

    final String simpleName = name;

    if (symbols.containsKey(simpleName)) {
      for (Symbol symbol : symbols.get(simpleName)) {
        if (symbol.isKindOf(super.getTargetKind())) {
          if (symbol.getName().equals(name) || symbol.getFullName().equals(name)) {
            resolvedSymbols.add(symbol);
          }
        }
      }
    }

    return ResolvingFilter.getResolvedOrThrowException(resolvedSymbols);
  }
}
