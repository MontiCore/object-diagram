/*
 (c) https://github.com/MontiCore/monticore
 */

package de.monticore.od.prettyprinter;

import de.monticore.prettyprint.IndentPrinter;

public abstract class ODFullPrettyPrinter {

  protected IndentPrinter printer;

  protected IndentPrinter getPrinter() {
    return this.printer;
  }

  public ODFullPrettyPrinter() {
    this.printer = new IndentPrinter();
  }

  public ODFullPrettyPrinter(IndentPrinter printer) {
    this.printer = printer;
  }

  protected abstract void init();

}
