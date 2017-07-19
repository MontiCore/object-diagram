/*
 * Copyright (c) 2017 RWTH Aachen. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
package de.monticore.lang.od._symboltable;

import de.monticore.symboltable.resolving.CommonResolvingFilter;

public class ODObjectResolvingFilter extends CommonResolvingFilter<ODObjectSymbol> {

  public ODObjectResolvingFilter() {
    super(ODObjectSymbol.KIND);
  }

}
