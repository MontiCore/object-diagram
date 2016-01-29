package de.monticore.lang.od.prettyprint;

import de.monticore.lang.od._ast.ASTODAttribute;
import de.monticore.lang.od._ast.ASTODObject;
import de.monticore.prettyprint.IndentPrinter;

/**
 * This class is responsible for pretty-printing hierarchical object diagrams.
 * 
 * @author Timo Greifenberg
 */
public class HierachicalODPrettyPrinterConcreteVisitor extends ODPrettyPrinterConcreteVisitor {
  
  /**
   * Constructor for
   * de.monticore.lang.od.prettyprint.HierachicalODPrettyPrinterConcreteVisitor
   * 
   * @param printer
   */
  public HierachicalODPrettyPrinterConcreteVisitor(IndentPrinter printer) {
    super(printer);
  }
  
  /**
   * Prints an object in an object diagram
   * 
   * @param a object
   */
  @Override
  public void handle(ASTODObject a) {
    getPrinter().println();
    // print completeness
    if (a.getCompleteness().isPresent()) {
      a.getCompleteness().get().accept(getRealThis());
    }
    // print object modifier
    if (a.getModifier().isPresent()) {
      a.getModifier().get().accept(getRealThis());
    }
    // print object name and type
    if (a.getName().isPresent()) {
      getPrinter().print(a.getName().get());
    }
    if (a.getType().isPresent()) {
      getPrinter().print(":");
      a.getType().get().accept(getRealThis());
    }
    // print object body
    if (!a.getODAttributes().isEmpty() || !a.getODObjects().isEmpty()) {
      getPrinter().println(" {");
      getPrinter().indent();
      for (ASTODAttribute ast : a.getODAttributes()) {
        ast.accept(getRealThis());
      }
      for (ASTODObject ast : a.getODObjects()) {
        ast.accept(getRealThis());
      }
      getPrinter().unindent();
      getPrinter().println("}");
    }
    else {
      getPrinter().println(";");
    }
  }
}
