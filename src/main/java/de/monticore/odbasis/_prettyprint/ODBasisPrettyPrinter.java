/* (c) https://github.com/MontiCore/monticore */
package de.monticore.odbasis._prettyprint;

import de.monticore.odbasis._ast.ASTODAttribute;
import de.monticore.prettyprint.IndentPrinter;

public class ODBasisPrettyPrinter extends ODBasisPrettyPrinterTOP {

  public ODBasisPrettyPrinter(IndentPrinter printer, boolean printComments){
    super(printer, printComments);
  }

  @Override
  public void handle(ASTODAttribute a) {
    // print modifier
    a.getModifier().accept(getTraverser());

    // print type
    if (a.isPresentMCType()) {
      a.getMCType().accept(getTraverser());
      getPrinter().print(" ");
    }
    // print name
    getPrinter().print(a.getName());

    if (a.isPresentComplete() || a.isPresentODValue()) {
      if (a.isPresentComplete()) {
        getPrinter().print(" = ");
      }
      else {
        getPrinter().print(" -> ");
      }

      // print value
      if (a.isPresentODValue()) {
        a.getODValue().accept(getTraverser());
      }
    }

    getPrinter().println(";");
  }


}
