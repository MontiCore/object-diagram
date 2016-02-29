package de.monticore.lang.od.prettyprint;

import java.util.Iterator;

import de.monticore.lang.od._ast.ASTODAttribute;
import de.monticore.lang.od._ast.ASTODInnerLink;
import de.monticore.lang.od._ast.ASTODObject;
import de.monticore.lang.od._ast.ASTODValueCollection;
import de.monticore.lang.od._ast.ASTValue;
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
    if (!a.getODAttributes().isEmpty() || !a.getInnerLinks().isEmpty()
        || !a.getValueCollections().isEmpty()) {
      getPrinter().println(" {");
      getPrinter().indent();
      for (ASTODAttribute ast : a.getODAttributes()) {
        ast.accept(getRealThis());
      }
      
      for (ASTODValueCollection ast : a.getValueCollections()) {
        ast.accept(getRealThis());
      }
      
      for (ASTODInnerLink ast : a.getInnerLinks()) {
        ast.accept(getRealThis());
      }
      getPrinter().unindent();
      getPrinter().println("}");
    }
    else {
      getPrinter().println(";");
    }
  }
  
  /**
   * Prints an object in an object diagram
   * 
   * @param a object
   */
  @Override
  public void handle(ASTODInnerLink a) {
    if (a.getLinkName().isPresent()) {
      getPrinter().print(a.getLinkName().get() + " = ");
    }
    a.getODObject().accept(getRealThis());
  }
  
  /**
   * Prints an object in an object diagram
   * 
   * @param a object
   */
  @Override
  public void handle(ASTODValueCollection a) {
//    getPrinter().indent();
    getPrinter().print("[");
    Iterator<ASTValue> it = a.getValues().iterator();
    while (it.hasNext()) {
      ASTValue v = it.next();
      v.accept(getRealThis());
      if (it.hasNext()) {
        getPrinter().print(",");
      }
    }
    
    getPrinter().print("];\n");
//    getPrinter().unindent();
  }
}