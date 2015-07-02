package de.monticore.lang.od.prettyprint;

import java.util.Iterator;

import com.google.common.base.Joiner;

import de.monticore.lang.od._ast.ASTCompleteness;
import de.monticore.lang.od._ast.ASTModifier;
import de.monticore.lang.od._ast.ASTODAttribute;
import de.monticore.lang.od._ast.ASTODBase;
import de.monticore.lang.od._ast.ASTODCompilationUnit;
import de.monticore.lang.od._ast.ASTODDefinition;
import de.monticore.lang.od._ast.ASTODLink;
import de.monticore.lang.od._ast.ASTODLinkQualifier;
import de.monticore.lang.od._ast.ASTODObject;
import de.monticore.lang.od._ast.ASTStereoValue;
import de.monticore.lang.od._ast.ASTStereoValueList;
import de.monticore.lang.od._ast.ASTStereotype;
import de.monticore.lang.od._ast.ASTStereotypeList;
import de.monticore.lang.od._ast.ASTVersion;
import de.monticore.lang.od._visitor.ODVisitor;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.prettyprint.TypesPrettyPrinterConcreteVisitor;
import de.monticore.types.types._ast.ASTImportStatement;
import de.se_rwth.commons.Joiners;
import de.se_rwth.commons.Names;

/**
 * This class is responsible for pretty-printing object diagrams. It is
 * implemented using the Visitor pattern. The Visitor pattern traverses a tree
 * in depth first, the visit and ownVisit-methods are called when a node is
 * traversed, the endVisit methods are called when the whole subtree of a node
 * has been traversed. The ownVisit-Methods stop the automatic traversal order
 * and allow to explictly visit subtrees by calling
 * getVisitor().startVisit(ASTNode)
 * 
 * @author Martin Schindler, Robert Heim
 */
public class ODPrettyPrinterConcreteVisitor extends TypesPrettyPrinterConcreteVisitor implements
    ODVisitor {
  
  /**
   * Constructor.
   * 
   * @param parent the parent pretty printer, needed to give control to the
   * embedded pretty printer when embedding is detected.
   * @param printer the printer to write to.
   */
  public ODPrettyPrinterConcreteVisitor(IndentPrinter printer) {
    super(printer);
  }
  
  /**
   * Prints the compilation unit of an object diagram (start of the pretty
   * print)
   * 
   * @param unit a OD compilation unit
   */
  @Override
  public void handle(ASTODCompilationUnit unit) {
    if (unit.getPackage() != null && !unit.getPackage().isEmpty()) {
      getPrinter()
          .println("package " + Names.getQualifiedName(unit.getPackage()) + ";\n");
    }
    if (unit.getVersion().isPresent()) {
      unit.getVersion().get().accept(getRealThis());
    }
    if (unit.getImportStatements() != null && !unit.getImportStatements().isEmpty()) {
      for (ASTImportStatement s : unit.getImportStatements()) {
        getPrinter().print("import " + Names.getQualifiedName(s.getImportList()));
        if (s.isStar()) {
          getPrinter().println(".*;");
        }
        else {
          getPrinter().println(";");
        }
      }
      getPrinter().println();
    }
    unit.getODDefinition().accept(getRealThis());
    
  }
  
  @Override
  public void handle(ASTVersion node) {
    getPrinter().print("version ");
    node.getStringLiteral().accept(getRealThis());
    getPrinter().println(";");
    getPrinter().println();
  }
  
  /**
   * Prints the object diagram definition
   * 
   * @param a object diagram definition
   */
  @Override
  public void handle(ASTODDefinition a) {
    // print completeness
    if (a.getCompleteness().isPresent()) {
      a.getCompleteness().get().accept(getRealThis());
    }
    // print stereotype
    if (a.getStereotype().isPresent()) {
      a.getStereotype().get().accept(getRealThis());
      getPrinter().println();
    }
    // print object diagram name and parameters
    getPrinter().print("objectdiagram " + a.getName());
    // print body
    getPrinter().println(" {");
    getPrinter().indent();
    // TODO a.getInvariants().accept(getRealThis());
    a.getODObjects().accept(getRealThis());
    a.getODLinks().accept(getRealThis());
    getPrinter().unindent();
    getPrinter().print("\n}\n");
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
    a.getModifier().accept(getRealThis());
    // print object name and type
    if (a.getName().isPresent()) {
      getPrinter().print(a.getName().get());
    }
    if (a.getType().isPresent()) {
      getPrinter().print(":");
      a.getType().get().accept(getRealThis());
    }
    // print object body
    if (!a.getODAttributes().isEmpty()) {
      getPrinter().println(" {");
      getPrinter().indent();
      a.getODAttributes().accept(getRealThis());
      getPrinter().unindent();
      getPrinter().println("}");
    }
    else {
      getPrinter().println(";");
    }
  }
  
  /**
   * Prints an attribute of an object in an object diagram
   * 
   * @param a attribute
   */
  @Override
  public void handle(ASTODAttribute a) {
    // print modifier
    a.getModifier().accept(getRealThis());
    // print type
    if (a.getType().isPresent()) {
      a.getType().get().accept(getRealThis());
      getPrinter().print(" ");
    }
    // print name
    getPrinter().print(a.getName());
    // print value
    if (a.getValue().isPresent()) {
      getPrinter().print(" = ");
      a.getValue().get().accept(getRealThis());
    }
    getPrinter().println(";");
  }
  
  /**
   * Prints a qualifier of a link in an object diagram
   * 
   * @param a qualifier of a link
   */
  @Override
  public void handle(ASTODLinkQualifier a) {
    if (a.getName().isPresent()) {
      getPrinter().print("[[");
      getPrinter().print(a.getName().get());
      getPrinter().print("]]");
    }
    else {
      getPrinter().print("[");
      a.getValue().get().accept(getRealThis());
      getPrinter().print("]");
    }
    getPrinter().print(" ");
  }
  
  /**
   * Prints an association, aggregation or composition in an object diagram
   * 
   * @param a association, aggregation or composition
   */
  @Override
  public void handle(ASTODLink a) {
    getPrinter().println();
    // print stereotype
    if (a.getStereotype().isPresent()) {
      a.getStereotype().get().accept(getRealThis());
      getPrinter().print(" ");
    }
    // print type of the link
    if (a.isLink()) {
      getPrinter().print("link ");
    }
    else if (a.isAggregation()) {
      getPrinter().print("aggregation ");
    }
    else if (a.isComposition()) {
      getPrinter().print("composition ");
    }
    // print name
    if (a.isDerived()) {
      getPrinter().print("/");
    }
    if (a.getName().isPresent()) {
      getPrinter().print(a.getName().get() + " ");
    }
    // print left modifier
    a.getLeftModifier().accept(getRealThis());
    // print objects referenced on the left side of the link
    getPrinter().print(Joiners.COMMA.join(a.getLeftReferenceNames()));
    getPrinter().print(" ");
    // print left qualifier
    if (a.getLeftQualifier().isPresent()) {
      a.getLeftQualifier().get().accept(getRealThis());
    }
    // print left role
    if (a.getLeftRole().isPresent()) {
      getPrinter().print("(");
      getPrinter().print(a.getLeftRole().get());
      getPrinter().print(") ");
    }
    // print arrow
    if (a.isLeftToRight()) {
      getPrinter().print("->");
    }
    if (a.isRightToLeft()) {
      getPrinter().print("<-");
    }
    if (a.isBidirectional()) {
      getPrinter().print("<->");
    }
    if (a.isUnspecified()) {
      getPrinter().print("--");
    }
    // print right role
    if (a.getRightRole().isPresent()) {
      getPrinter().print(" (");
      getPrinter().print(a.getRightRole().get());
      getPrinter().print(")");
    }
    // print right qualifier
    if (a.getRightQualifier().isPresent()) {
      a.getRightQualifier().get().accept(getRealThis());
    }
    // print objects referenced on the right side of the link
    getPrinter().print(" ");
    getPrinter().print(Joiners.COMMA.join(a.getRightReferenceNames()));
    getPrinter().print(" ");
    // print right modifier
    a.getRightModifier().accept(getRealThis());
    getPrinter().println(";");
  }
  
  @Override
  public void handle(ASTModifier a) {
    if (a.getStereotype().isPresent()) {
      a.getStereotype().get().accept(getRealThis());
      getPrinter().print(" ");
    }
    if (a.isAbstract()) {
      getPrinter().print("abstract ");
    }
    if (a.isLocal()) {
      getPrinter().print("local ");
    }
    if (a.isReadonly()) {
      getPrinter().print("readonly ");
    }
    if (a.isFinal()) {
      getPrinter().print("final ");
    }
    if (a.isStatic()) {
      getPrinter().print("static ");
    }
    if (a.isPrivate()) {
      getPrinter().print("private ");
    }
    if (a.isProtected()) {
      getPrinter().print("protected ");
    }
    if (a.isPublic()) {
      getPrinter().print("public ");
    }
    if (a.isDerived()) {
      getPrinter().print("derived ");
    }
  }
  
  @Override
  public void visit(ASTStereotype node) {
    getPrinter().print("<<");
  }
  
  @Override
  public void endVisit(ASTStereotype node) {
    getPrinter().print(">>");
  }
  
  @Override
  public void handle(ASTStereotypeList node) {
    getPrinter().print(Joiner.on(" ").join(node));
  }
  
  @Override
  public void visit(ASTStereoValue a) {
    getPrinter().print(a.getName());
    if (a.getValue().isPresent()) {
      printer.print("=\"" + a.getValue().get() + "\"");
    }
  }
  
  @Override
  public void handle(ASTStereoValueList a) {
    printSeparator(a.iterator(), ",");
  }
  
  @Override
  public void handle(ASTCompleteness a) {
    if (a.isComplete()) {
      getPrinter().print("(c) ");
    }
    if (a.isIncomplete()) {
      getPrinter().print("(...) ");
    }
    if (a.isLeftComplete()) {
      getPrinter().print("(c,...) ");
    }
    if (a.isRightComplete()) {
      getPrinter().print("(...,c) ");
    }
  }
  
  /**
   * Prints a list separating the elements by separator
   * 
   * @param iter iterator for the list of ASTQualifiedNames
   * @param seperator string for seperating the ASTQualifiedNames
   */
  private void printSeparator(Iterator<? extends ASTODBase> iter, String separator) {
    // print by iterate through all items
    String sep = "";
    while (iter.hasNext()) {
      getPrinter().print(sep);
      iter.next().accept(getRealThis());
      sep = separator;
    }
  }
  
}
